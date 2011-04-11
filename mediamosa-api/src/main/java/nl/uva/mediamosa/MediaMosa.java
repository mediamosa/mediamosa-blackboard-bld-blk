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
import java.util.List;
import java.util.Map;

import nl.uva.mediamosa.model.AssetDetailsType;
import nl.uva.mediamosa.model.AssetType;
import nl.uva.mediamosa.model.ErrorcodeType;
import nl.uva.mediamosa.model.LinkType;
import nl.uva.mediamosa.model.MediafileDetailsType;
import nl.uva.mediamosa.model.ProfileType;
import nl.uva.mediamosa.model.Response;
import nl.uva.mediamosa.model.StatsDatausagevideoType;
import nl.uva.mediamosa.model.UploadTicketType;
import nl.uva.mediamosa.util.ServiceException;


public interface MediaMosa {
	
	/**
	 * Sets authentication credentials used for login.
	 * 
	 * @param username
	 * @param password
	 * @throws IOException
	 * @throws ServiceException
	 */
	void setCredentials(String username, String password) throws ServiceException;
	
	/**
	 * Sets hostname of MediaMosa server (for example http://yourserver.com)
	 * 
	 * @param hostname
	 */
	void setHostname(String hostname);
	
	/**
	 * Returns boolean for login.
	 * 
	 * @return boolean
	 * @throws ServiceException
	 */
	boolean login() throws IOException, ServiceException;
	
	/**
	 * 
	 * @return waarom?
	 */
	boolean isValidCookie(); 
	
	/**
	 * Return List of ErrorCodeType
	 * @return <code>List<ErrorcodeType></code>
	 * @throws ServiceException
	 */
	List<ErrorcodeType> getErrorCodes() throws ServiceException;

	/**
	 * Returns protocol version of the MediaMosa webservice you are connected with.
	 * @return <code>String</code> containing the version number.
	 * @throws ServiceException
	 */
	String getVersion() throws ServiceException;
	
	/**
	 * 
	 * @param assetId
	 * @param mediafileId
	 * @param userId
	 * @return <code>LinkType</code> object. The actual url of the media file is obtained by calling LinkType.getOutput()
	 * @throws ServiceException
	 */
	LinkType getPlayLink(String assetId, String mediafileId, String userId) throws ServiceException;
	
	/**
	 * 
	 * @param assetId
	 * @param mediafileId
	 * @param userId
	 * @param responseType
	 * @return <code>LinkType</code> object. ResponseType can be set to 'plain' (plain URL),
	 * 'metafile' (metafile format, this depends on the type of video), 'object' (html object), 
	 * 'download' (download url) or 'still' (still). The actual content of the response is obtained by calling LinkType.getOutput()
	 * @throws ServiceException
	 */
	LinkType getPlayLink(String assetId, String mediafileId, String userId, String responseType) throws ServiceException;
	
	/**
	 * 
	 * @param assetId
	 * @param mediafileId
	 * @param userId
	 * @param width
	 * @return <code>LinkType</code> object. The actual content of the response is obtained by calling LinkType.getOutput(). It contains generated object code where 'width' is as specified, and 'height' is calculated based on ratio.
	 * @throws ServiceException
	 */
	LinkType getPlayLink(String assetId, String mediafileId, String userId, int width) throws ServiceException;
	
	
	/**
	 * 
	 * @param assetId
	 * @param userId
	 * @return <code>LinkType</code> object. The actual url of the still image is obtained by calling LinkType.getOutput()
	 * @throws ServiceException
	 */
	LinkType getStillLink(String assetId, String userId) throws ServiceException;
	
	/**
	 * 
	 * @param mediafileId
	 * @param userId
	 * @return Ticket needed for uploading a file in the mediafile container specified by mediafileId
	 * @throws ServiceException
	 */
	UploadTicketType createUploadTicket(String mediafileId, String userId) throws ServiceException;
	
	/**
	 * 
	 * @return <code>String</code> assetId of the newly created asset
	 */
	String createAsset(String userId);
	
	/**
	 * 
	 * @return <code>String</code> mediafileId of the newly create mediafile
	 */
	String createMediafile(String assetId, String userId);

	/**
	 * @return List<AssetType>
	 * @throws ServiceException
	 */
	List<AssetType> getAssets() throws ServiceException;
	
	/**
	 * @param properties
	 * @return List<AssetType>
	 * @throws ServiceException
	 */
	List<AssetType> getAssets(Map properties) throws ServiceException; 

	/**
	 * @param assetId
	 * @return AssetDetailsType
	 * @throws ServiceException
	 */
	AssetDetailsType getAssetDetails(String assetId) throws ServiceException;

	/**
	 * @param assetId
	 * @param userId
	 * @param metadata
	 * @return
	 */
	Response setMetadata(String assetId, String userId, Map metadata);

	/**
	 * @param assetId
	 * @param userId
	 * @throws ServiceException
	 */
	void deleteMetadata(String assetId, String userId) throws ServiceException;

	/**
	 * @param assetId
	 * @param userId
	 * @param cascade
	 * @throws ServiceException
	 */
	void deleteAsset(String assetId, String userId, boolean cascade) throws ServiceException;

	/**
	 * @param mediafileId
	 * @param userId
	 * @param properties
	 * @return
	 */
	Response updateMediafile(String mediafileId, String userId, Map properties);

	/**
	 * @param mediafileId
	 * @param userId
	 * @throws ServiceException
	 */
	void deleteMediafile(String mediafileId, String userId) throws ServiceException;

	/**
	 * @param mediafileId
	 * @return
	 */
	MediafileDetailsType getMediafileDetails(String mediafileId);
	
	/**
	 * @param year
	 * @param month
	 * @param type
	 * @param limit
	 * @param offset
	 * @return
	 * @throws ServiceException
	 */
	List<StatsDatausagevideoType> getStatsDatausageVideo(int year, int month, String type, int limit, int offset) throws ServiceException;

	/**
	 * @return
	 * @throws ServiceException
	 */
	List<ProfileType> getProfiles() throws ServiceException;
	
	/**
	 * @param cql
	 * @return
	 * @throws ServiceException
	 */
	List<AssetType> getCqlAssets(String cql) throws ServiceException;
}
