/**
 * MediaMosa API
 *
 * A partial implementation of the MediaMosa API in Java.
 *
 * Copyright 2010 Universiteit van Amsterdam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.uva.mediamosa.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import nl.uva.mediamosa.MediafileProperties;
import nl.uva.mediamosa.MediaMosa;
import nl.uva.mediamosa.model.AssetDetailsType;
import nl.uva.mediamosa.model.AssetIdType;
import nl.uva.mediamosa.model.AssetType;
import static nl.uva.mediamosa.ErrorCodes.*;
import nl.uva.mediamosa.model.ErrorcodeType;
import nl.uva.mediamosa.model.ItemsType;
import nl.uva.mediamosa.model.LinkType;
import nl.uva.mediamosa.model.MediafileDetailsType;
import nl.uva.mediamosa.model.MediafileType;
import nl.uva.mediamosa.model.ProfileType;
import nl.uva.mediamosa.model.Response;
import nl.uva.mediamosa.model.StatsDatausagevideoType;
import nl.uva.mediamosa.model.UploadTicketType;
import nl.uva.mediamosa.util.ChallengeUtil;
import nl.uva.mediamosa.util.MD5Util;
import nl.uva.mediamosa.util.SHA1Util;
import nl.uva.mediamosa.util.ServiceException;
import nl.uva.mediamosa.util.UnmarshallUtil;
import nl.uva.mediamosa.util.XsltUtil;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;


public class MediaMosaImpl implements MediaMosa {
	
	private static final long ONE_HOUR_MILLIS = TimeUnit.SECONDS.toMillis(60L * 60L);
	private String username;
	private String password;
	private DefaultHttpClient httpclient;
	private final AtomicLong cookieExpireTime= new AtomicLong(0);  
	private String hostname;
	private String vpxVersion;
	private static final Logger log = Logger.getLogger(MediaMosaImpl.class.getName());
	
	/** Maximum of items to retrieve at once */
	private static final int LIMIT = 200;
	
	/** Default value of (optional) offset */
	private static final int OFFSET = 0;

	/* (non-Javadoc)
	 * @see nl.uva.mediamosa.VpCore#setHostname(java.lang.String)
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	/* (non-Javadoc)
	 * @see nl.uva.mediamosa.VpCore#setCredentials(java.lang.String, java.lang.String)
	 */
	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 
	 * @param getRequest
	 * @return
	 * @throws IOException
	 */
	public Response doGetRequest(String getRequest) throws IOException {
		Response vpxResponse = null;
		HttpGet httpget = new HttpGet(hostname + getRequest);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null) {		
			String content = IOUtils.toString(entity.getContent());
			String transformedContent = null;
			// do xsl tranformation
			try {
				transformedContent = XsltUtil.transform(content, getClass().getResourceAsStream("/convert_item_elements.xsl"));
			} catch (TransformerException e) {
				log.severe(e.getMessage());
			}

			vpxResponse = UnmarshallUtil.unmarshall(transformedContent); //TK
			/*
			InputStream is = entity.getContent();
			vpxResponse = UnmarshallUtil.unmarshall(is);
			is.close();
			*/
			//content = entity.getContent();
			entity.consumeContent();
		}
		return vpxResponse;
	}
	
	/**
	 * @param postRequest
	 * @param postParams
	 * @return
	 * @throws IOException
	 */
	public Response doPostRequest(String postRequest, String postParams) throws IOException {
		Response vpxResponse = null;
		HttpPost httppost = new HttpPost(hostname + postRequest);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		URLEncodedUtils.parse(nvps, new Scanner(postParams), HTTP.UTF_8);
		httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String content = IOUtils.toString(entity.getContent());
			String transformedContent = null;
			//do xsl transformation
			try {
				transformedContent = XsltUtil.transform(content, getClass().getResourceAsStream("/convert_item_elements.xsl"));
			} catch (TransformerException e) {
				log.severe(e.getMessage());
			}
			vpxResponse = UnmarshallUtil.unmarshall(transformedContent);
			entity.consumeContent();
		}
		return vpxResponse;
	}
	

	/* (non-Javadoc)
	 * @see nl.uva.mediamosa.VpCore#isValidCookie()
	 */
	public boolean isValidCookie() {
		boolean status = false;
		if (httpclient != null) {
			CookieStore cookiejar = httpclient.getCookieStore();

			// Do not let cookies get too old. Stupid workaround for very old sessions.
			if (cookieExpireTime.get() < System.currentTimeMillis()) {
				cookiejar.clear();
				cookieExpireTime.set(System.currentTimeMillis() + ONE_HOUR_MILLIS);
			}

			// remove expired cookies
			cookiejar.clearExpired(new Date());
			List<Cookie> cookies = cookiejar.getCookies();
			if (!cookies.isEmpty()) {
				// still a valid cookie
				status = true;
			}
		}
		return status;
	}

	/* (non-Javadoc)
	 * @see nl.uva.mediamosa.VpCore#login()
	 */
        public static class ThreadSafeClientConnectionManagerFactory implements ClientConnectionManagerFactory {

        public ClientConnectionManager newInstance(HttpParams params, SchemeRegistry schemeRegistry) {
            return new ThreadSafeClientConnManager(params, schemeRegistry);
        }
    }
                
	public boolean login() throws IOException, ServiceException {
		
		String challenge = null;
		httpclient = new DefaultHttpClient() {

                    @Override
                    protected HttpParams createHttpParams() {
                        final HttpParams params = super.createHttpParams();
                        params.setParameter(ClientPNames.CONNECTION_MANAGER_FACTORY_CLASS_NAME, ThreadSafeClientConnectionManagerFactory.class.getName());
                        return params;
                    }
                };
		HttpPost httppost = new HttpPost(hostname + "/login");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("dbus", "AUTH DBUS_COOKIE_SHA1 " + this.username));
		httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();

        if (entity != null) {
        	String content = IOUtils.toString(entity.getContent()); 		
    		challenge = ChallengeUtil.getChallenge(content);
        	// hierna streamclosed
        	entity.consumeContent();
        }
		
        // posting challenge and random value
        String randomValue = MD5Util.getRandomValue();
        String responseValue = SHA1Util.getSHA1(challenge + ":" + randomValue + ":" + this.password);
        nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("dbus", "DATA " + randomValue + " " + responseValue));
        httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        response = httpclient.execute(httppost);
        entity = response.getEntity();

        Response vpxResponse = null; //TK
        
        if (entity != null) {
        	 //TK
    		InputStream is = entity.getContent();
    		vpxResponse = UnmarshallUtil.unmarshall(is);
    		is.close();
        	// TK end
        	//String content = IOUtils.toString(entity.getContent());
    		//this.responseHeader = XmlParserUtil.parseResponse(content);
    		
        	// hierna streamclosed
        	entity.consumeContent();
        } 

        // set VPX version
        this.vpxVersion = vpxResponse.getHeader().getVpxVersion();
        
		return vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY;
	}

	
	/* (non-Javadoc)
	 * @see nl.uva.mediamosa.VpCore#getVersion()
	 */
	public String getVersion() {
		return vpxVersion;
	}
	
	/* (non-Javadoc)
	 * @see nl.uva.mediamosa.VpCore#getAssets()
	 */
	public List<AssetType> getAssets() throws ServiceException {
		int limit = LIMIT;
		int offset = OFFSET;
		return getAssets(limit, offset, null);
	}
	
	/*
	public List<AssetType> getAssets(int offset) throws ServiceException {
		int limit = LIMIT;
		return getAssets(limit, offset, null);
	}
	
	public List<AssetType> getAssets(int limit) throws ServiceException {
		int offset = OFFSET;
		return getAssets(limit);
	}
	*/
	
	// wat als offset groter is dan total items? 
	
	//Map options Object gebruiken voor int limit, int offset?
	
	/* (non-Javadoc)
	 * @see nl.uva.mediamosa.VpCore#getAssets(java.util.Map)
	 */
	public List<AssetType> getAssets(Map properties) throws ServiceException {

		String requestUrl = "/asset";
		String parameters = "limit=" + LIMIT;
		List<AssetType> assets = new ArrayList<AssetType>();
		Response vpxResponse = null;
		
		if (properties != null) {
			// iterate over key value pairs
			Iterator it = properties.entrySet().iterator();
			StringBuilder sb = new StringBuilder();
			while (it.hasNext()) {
				sb.append(parameters).append('&');
				Map.Entry pairs = (Map.Entry)it.next();
				sb.append(pairs.getKey()).append('=').append(pairs.getValue());
			}
			parameters = sb.toString();
		}
		requestUrl += "?" +  parameters;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			ItemsType items = vpxResponse.getItems();
			
			for (Object o : items.getLinkOrAssetOrAssetDetails() ) {
				if (o instanceof AssetType) {
					assets.add((AssetType) o);
				}
			}
		}
		return assets;
	}
	
	public List<AssetType> getAssets(int limit, int offset, Map properties) throws ServiceException {
		String requestUrl = "/asset";
		String parameters = "";
		if (limit <= LIMIT) {
			parameters = "limit=" + limit;
		} else {
			parameters = "limit=" + LIMIT;
		}
		
		List<AssetType> assets = new ArrayList<AssetType>();
		Response vpxResponse = null;
		
		if (properties != null) {
			// iterate over key value pairs
			Iterator it = properties.entrySet().iterator();
			StringBuilder sb = new StringBuilder();
			while (it.hasNext()) {
				sb.append(parameters).append('&');
				Map.Entry pairs = (Map.Entry)it.next();
				sb.append(pairs.getKey()).append('=').append(pairs.getValue());
			}
			parameters = sb.toString();
		}
		requestUrl += "?" +  parameters;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			ItemsType items = vpxResponse.getItems();
			
			for (Object o : items.getLinkOrAssetOrAssetDetails() ) {
				if (o instanceof AssetType) {
					assets.add((AssetType) o);
				}
			}
		}
		return assets;
	}

	
	
	/* (non-Javadoc)
	 * @see nl.uva.mediamosa.VpCore#getAssetDetails(java.lang.String)
	 */
	public AssetDetailsType getAssetDetails(String assetId) throws ServiceException {
		String requestUrl = "/asset/" + assetId;
		AssetDetailsType assetDetails = null;
		Response vpxResponse = null;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			
			ItemsType items = vpxResponse.getItems();
			if (!items.getLinkOrAssetOrAssetDetails().isEmpty()) {
				assetDetails = (AssetDetailsType) items.getLinkOrAssetOrAssetDetails().get(0);
			}
		}
		return assetDetails;
	}

	// is_admin=true en userid paramaters
	public List<ErrorcodeType> getErrorCodes() throws ServiceException {
		String requestUrl = "/errorcodes";
		List <ErrorcodeType> errorCodes = new ArrayList<ErrorcodeType>();
		Response vpxResponse = null;

		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}

		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
					
			ItemsType items = vpxResponse.getItems();
			
			for (Object o : items.getLinkOrAssetOrAssetDetails() ) {
				if (o instanceof ErrorcodeType) {
					errorCodes.add((ErrorcodeType) o);
				}
			}			
		}
		return errorCodes;
	}

	public LinkType getPlayLink(String assetId, String mediafileId, String userId, int width) throws ServiceException {
		String requestUrl = String.format("/asset/%s/play", assetId);
		String parameters = "mediafile_id=" + mediafileId + "&user_id=" + userId + "&response=object&width=" + width;
		requestUrl += "?" +  parameters;
		LinkType playLink = null;
		Response vpxResponse = null;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			
			ItemsType items = vpxResponse.getItems();
			if (!items.getLinkOrAssetOrAssetDetails().isEmpty()) {
				playLink = (LinkType) items.getLinkOrAssetOrAssetDetails().get(0);
			}		
		}
		return playLink;
	}
	
	
	public LinkType getPlayLink(String assetId, String mediafileId, String userId, String responseType) throws ServiceException {
		String requestUrl = String.format("/asset/%s/play", assetId);
		String parameters = "mediafile_id=" + mediafileId + "&user_id=" + userId + "&response=" + responseType;
		requestUrl += "?" +  parameters;
		LinkType playLink = null;
		Response vpxResponse = null;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			
			ItemsType items = vpxResponse.getItems();
			if (!items.getLinkOrAssetOrAssetDetails().isEmpty()) {
				playLink = (LinkType) items.getLinkOrAssetOrAssetDetails().get(0);
			}		
		}
		return playLink;
	}
	
	public LinkType getPlayLink(String assetId, String mediafileId, String userId) throws ServiceException {
		String requestUrl = String.format("/asset/%s/play", assetId);
		String parameters = "mediafile_id=" + mediafileId + "&user_id=" + userId;
		//parameters += "&is_app_admin=true";
		requestUrl += "?" +  parameters;
		LinkType playLink = null;
		Response vpxResponse = null;

		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			
			ItemsType items = vpxResponse.getItems();
			if (!items.getLinkOrAssetOrAssetDetails().isEmpty()) {
				playLink = (LinkType) items.getLinkOrAssetOrAssetDetails().get(0);
			}		
		}
		return playLink;
	}

	public LinkType getStillLink(String assetId, String userId) throws ServiceException {
		String requestUrl = String.format("/asset/%s/still", assetId);
		String parameters = "user_id=" + userId;
		// parameters += "&is_app_admin=true";
		requestUrl += "?" +  parameters;
		LinkType stillLink = null;
		Response vpxResponse = null;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			
			ItemsType items = vpxResponse.getItems();
			if (!items.getLinkOrAssetOrAssetDetails().isEmpty()) {
				stillLink = (LinkType) items.getLinkOrAssetOrAssetDetails().get(0);
			}		
		}
		return stillLink;
	}

	public UploadTicketType createUploadTicket(String mediafileId, String userId) throws ServiceException {
		String requestUrl = String.format("/mediafile/%s/uploadticket/create", mediafileId);
		String parameters = "user_id=" + userId;
		// parameters += "&is_app_admin=true";
		requestUrl += "?" +  parameters;
		UploadTicketType uploadTicket = null;
		Response vpxResponse = null;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			
			ItemsType items = vpxResponse.getItems();
			if (!items.getLinkOrAssetOrAssetDetails().isEmpty()) {
				uploadTicket = (UploadTicketType) items.getLinkOrAssetOrAssetDetails().get(0);
			}		
		}
		return uploadTicket;
	}

	public String createAsset(String userId) {
		String requestUrl = "/asset/create";
		String parameters = "user_id=" + userId;
		String assetId = null;
		Response vpxResponse = null;
		try {
			vpxResponse = doPostRequest(requestUrl, parameters);
			if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
				ItemsType items = vpxResponse.getItems();
				if (!items.getLinkOrAssetOrAssetDetails().isEmpty()) {
					AssetIdType id = (AssetIdType) items.getLinkOrAssetOrAssetDetails().get(0);
					assetId = id.getAssetId();
				}
			}
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		return assetId;
	}

	public String createMediafile(String assetId, String userId) {
		String requestUrl = "/mediafile/create";
		String parameters = "user_id=" + userId + "&asset_id=" + assetId;
		String mediafileId = null;
		Response vpxResponse =  null;
		try {
			vpxResponse = doPostRequest(requestUrl, parameters);
			if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
				ItemsType items = vpxResponse.getItems();
				if (!items.getLinkOrAssetOrAssetDetails().isEmpty()) {
					MediafileType mediafile = (MediafileType) items.getLinkOrAssetOrAssetDetails().get(0);
					mediafileId = mediafile.getMediafileId();
				}
			}
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		return mediafileId;
	}

	public Response setMetadata(String assetId, String userId, Map metadata) {
		String requestUrl = String.format("/asset/%s/metadata", assetId);
		String parameters = "user_id=" + userId;
		
		// iterate over key value pairs
		Iterator it = metadata.entrySet().iterator();
		StringBuilder sb = new StringBuilder(parameters);
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			sb.append('&').append(pairs.getKey()).append('=').append(pairs.getValue());
		}
		parameters = sb.toString();
		
		Response vpxResponse = null;
		try {
			vpxResponse = doPostRequest(requestUrl, parameters);
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		return vpxResponse;
	}
	
	// is dit verwijderen van metadata of van asset
	public void deleteMetadata(String assetId, String userId) throws ServiceException {
		String requestUrl = String.format("/asset/%s/delete", assetId);
		String parameters = "user_id=" + userId;
		Response vpxResponse = null;
		try {
			vpxResponse = doPostRequest(requestUrl, parameters);
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		
		if (vpxResponse.getHeader().getRequestResultId() != ERRORCODE_OKAY) {
			throw new ServiceException(vpxResponse.getHeader().getRequestResultDescription());
		}
	}
	
	// cascade (true) verwijdert alle onderliggende items (mediafiles, stills, koppelingen met collections)
	public void deleteAsset(String assetId, String userId, boolean cascade) throws ServiceException {
		String requestUrl = String.format("/asset/%s/delete", assetId);
		String parameters = "user_id=" + userId;
		if (cascade) {
			parameters += "&delete=cascade";
		}
		Response vpxResponse = null;
		try {
			vpxResponse = doPostRequest(requestUrl, parameters);
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		
		if (vpxResponse.getHeader().getRequestResultId() != ERRORCODE_OKAY) {
			throw new ServiceException(vpxResponse.getHeader().getRequestResultDescription());
		}
	}

	public void deleteMediafile(String mediafileId, String userId) throws ServiceException {
		String requestUrl = String.format("/mediafile/%s/delete", mediafileId);
		String parameters = "user_id=" + userId;
		Response vpxResponse = null;
		try {
			vpxResponse = doPostRequest(requestUrl, parameters);
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		if (vpxResponse.getHeader().getRequestResultId() != ERRORCODE_OKAY) {
			throw new ServiceException(vpxResponse.getHeader().getRequestResultDescription());
		}
	}
	
	public Response updateMediafile(String mediafileId, String userId, Map properties) {
		String requestUrl = "/mediafile/" + mediafileId;
		String parameters = "user_id=" + userId;
		Response vpxResponse = null;

		if (properties != null) {
			// if uri is set, ignore filename and is_downloablde
			if (properties.containsKey(MediafileProperties.URI)) {
				properties.remove(MediafileProperties.FILENAME);
				properties.remove(MediafileProperties.ISDOWNLOADABLE);
			}

			// if filename or is_ downloadable is set, ignore uri
			if (properties.containsKey(MediafileProperties.FILENAME) || properties.containsKey(MediafileProperties.ISDOWNLOADABLE)) {
				properties.remove(MediafileProperties.URI);
			}

			// iterate over key value pairs
			Iterator it = properties.entrySet().iterator();
			StringBuilder sb = new StringBuilder();
			while (it.hasNext()) {
				sb.append(parameters).append('&');
				Map.Entry pairs = (Map.Entry)it.next();
				sb.append(pairs.getKey()).append('=').append(pairs.getValue());
			}
			parameters = sb.toString();
		}
		
		try {
			vpxResponse = doPostRequest(requestUrl, parameters);
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		return vpxResponse;
	}
	
	public MediafileDetailsType	getMediafileDetails(String mediafileId) {
		String requestUrl = "/mediafile/" + mediafileId;
		MediafileDetailsType mediafileDetails = null;
		Response vpxResponse = null;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
			if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
				ItemsType items = vpxResponse.getItems();
				if (!items.getLinkOrAssetOrAssetDetails().isEmpty()) {
					mediafileDetails = (MediafileDetailsType) items.getLinkOrAssetOrAssetDetails().get(0);
				}
			}
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		return mediafileDetails;
	}

	public List<StatsDatausagevideoType> getStatsDatausageVideo(int year, int month, String type, int limit, int offset) throws ServiceException  {
		String requestUrl = "/statistics/datausagevideo";
		String parameters = "limit=" + LIMIT;
		requestUrl += "?" + parameters + "&year=" + year + "&month=" + month + "&type=" + type;
		List<StatsDatausagevideoType> datausagevideo = null;
		Response vpxResponse = null;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
			if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
				ItemsType items = vpxResponse.getItems();
				datausagevideo = new ArrayList<StatsDatausagevideoType>();
				
				for (Object o : items.getLinkOrAssetOrAssetDetails() ) {
					if (o instanceof StatsDatausagevideoType) {
						datausagevideo.add((StatsDatausagevideoType) o);
					}
				}
			}
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		return datausagevideo;
	}
	
	public List<ProfileType> getProfiles() throws ServiceException {
		String requestUrl = "/transcode/profiles";
		List<ProfileType> profiles = null;
		Response vpxResponse = null;
		
		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			ItemsType items = vpxResponse.getItems();
			profiles = new ArrayList<ProfileType>();
			
			for (Object o : items.getLinkOrAssetOrAssetDetails() ) {
				if (o instanceof ProfileType) {
					profiles.add((ProfileType) o);
				}
			}
		}		
		return profiles;
	}

	public List<AssetType> getCqlAssets(String cql) throws ServiceException {
		String requestUrl = "/asset";
		String parameters = "limit=" + LIMIT;
		List<AssetType> assets = new ArrayList<AssetType>();
		Response vpxResponse = null;
		String query = "";
		
		try {
			query = URLEncoder.encode(cql, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.severe(e.getMessage());
		}
		StringBuilder sb = new StringBuilder();
		sb.append(parameters).append("&cql=").append(query);
		parameters = sb.toString();
		requestUrl += "?" +  parameters;
		try {
			vpxResponse = doGetRequest(requestUrl);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if (vpxResponse.getHeader().getRequestResultId() == ERRORCODE_OKAY) {
			ItemsType items = vpxResponse.getItems();
			
			for (Object o : items.getLinkOrAssetOrAssetDetails() ) {
				if (o instanceof AssetType) {
					assets.add((AssetType) o);
				}
			}
		}
		return assets;
	}
}
