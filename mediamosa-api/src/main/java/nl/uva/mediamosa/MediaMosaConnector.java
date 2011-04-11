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

package nl.uva.mediamosa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import nl.uva.mediamosa.util.ChallengeUtil;
import nl.uva.mediamosa.util.MD5Util;
import nl.uva.mediamosa.util.SHA1Util;
import nl.uva.mediamosa.util.XmlParserUtil;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


public class MediaMosaConnector {
	
	private final String host;
	private String username;
	private String password;
	private final DefaultHttpClient httpclient;
	private Header responseHeader;
	private static final Logger log = Logger.getLogger(MediaMosaConnector.class.getName());
	
	public MediaMosaConnector(String host) {
		this.host = host;
		this.httpclient = new DefaultHttpClient();
	}
	
	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public boolean doLogin() throws IOException{

		String challenge = null;
		HttpPost httppost = new HttpPost(host + "/login");
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

        if (entity != null) {
        	String content = IOUtils.toString(entity.getContent());
    		this.responseHeader = XmlParserUtil.parseResponse(content);
        	// hierna streamclosed
        	entity.consumeContent();
        } 

		return responseHeader.getRequestResultId() == ErrorCodes.ERRORCODE_OKAY;
	}
	
	public String doGetRequest(String getRequest) throws IOException {
		String content = null;
		HttpGet httpget = new HttpGet(host + getRequest);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			content = IOUtils.toString(entity.getContent());
			this.responseHeader = XmlParserUtil.parseResponse(content);
			entity.consumeContent();
		}
		// if cookie is expired, then relogin
		if (responseHeader.getRequestResultId() == ErrorCodes.ERRORCODE_ACCES_DENIED) {
			doLogin();
			doGetRequest(getRequest);
		}
		return content;
	}
	
	public String doPostRequest(String postRequest, String postParams) throws IOException {
		String content = null;
		HttpPost httppost = new HttpPost(host + postRequest);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		URLEncodedUtils.parse(nvps, new Scanner(postParams), HTTP.UTF_8);
		httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			content = IOUtils.toString(entity.getContent());
			this.responseHeader = XmlParserUtil.parseResponse(content);
			entity.consumeContent();
		}
		
		// if cookie is expired, then relogin
		if (responseHeader.getRequestResultId() == ErrorCodes.ERRORCODE_ACCES_DENIED) {
			doLogin();
			doPostRequest(postRequest, postParams);	
		}
		return content;
	}
	
	public boolean isValidCookie() {
		if (httpclient == null) {
			return false;
		} else {
			CookieStore cookiejar = httpclient.getCookieStore();
			// remove expired cookies
			cookiejar.clearExpired(new Date());
			List<Cookie> cookies = cookiejar.getCookies();
			if (!cookies.isEmpty()) {
	            for (int i = 0; i < cookies.size(); i++) {
	                log.fine("Cookie ExpiryDate: " + cookies.get(i).getExpiryDate().toString());
	            }
			}
		}
		return false;
	}
	
}
