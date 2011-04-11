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

package nl.uva.ic.vpcore;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.uva.mediamosa.ErrorCodes;
import nl.uva.mediamosa.SearchParameters;
import nl.uva.mediamosa.model.AssetDetailsType;
import nl.uva.mediamosa.model.AssetType;
import nl.uva.mediamosa.model.LinkType;
import nl.uva.mediamosa.model.MediafileDetailsType;
import nl.uva.mediamosa.model.Response;
import nl.uva.mediamosa.model.UploadTicketType;
import nl.uva.mediamosa.util.ServiceException;

import org.junit.Test;

public class ConnectionTest extends AbstractTest {

	@Test
	public void testConnection() throws ServiceException, IOException{
		assertTrue("Connection failed", service.getVersion().equals("1.7.4"));
	}

	@Test
	public void testErrorCodes() throws ServiceException, IOException {
		assertTrue("Empty list of errorcodes", service.getErrorCodes().size() != 0);
	}
	
	@Test
	public void testGetPlayLink() throws ServiceException, IOException {
		LinkType playLink = service.getPlayLink("ZSHr5KQvsQ6S80HEHUg0NwJV", "815r3Koiwg7GZFtSndQxgbyK", "admin");
		assertTrue("No PlayLink found", playLink.getOutput().startsWith("mms://wms.acceptatie.streaming.kennisnet.nl/vpcore-pilot/"));
		// the link is generated on each request, testing by using equals() fails.
		//assertTrue("No PlayLink found", playLink.getOutput().equals("mms://wms.acceptatie.streaming.kennisnet.nl/vpcore-pilot/EFKHLnRmTOzIQJ4Qu16rsBOi"));		
	}
	
	@Test
	public void testGetPlayLinkReponseType() throws ServiceException, IOException {
		LinkType playLink = service.getPlayLink("RopolDT79mZxjc4sW5EsDNaE", "6nluuYCIC9C7LVB1XKOlxbur", "admin", "object");		
		assertTrue("No PlayLink found", playLink.getOutput().startsWith("<script"));
		// the link is generated on each request, testing by using equals() fails.
		//assertTrue("No PlayLink found", playLink.getOutput().equals("mms://wms.acceptatie.streaming.kennisnet.nl/vpcore-pilot/EFKHLnRmTOzIQJ4Qu16rsBOi"));		
	}
	
	@Test
	public void testGetPlayLinkWidth() throws ServiceException, IOException {
		LinkType playLink = service.getPlayLink("RopolDT79mZxjc4sW5EsDNaE", "6nluuYCIC9C7LVB1XKOlxbur", "admin", 650);		
		System.out.println(playLink.getOutput());
		assertTrue("No PlayLink found", playLink.getOutput().startsWith("<script"));
		// the link is generated on each request, testing by using equals() fails.
		//assertTrue("No PlayLink found", playLink.getOutput().equals("mms://wms.acceptatie.streaming.kennisnet.nl/vpcore-pilot/EFKHLnRmTOzIQJ4Qu16rsBOi"));		
	}
	

	@Test
	public void testGetStillLink() throws ServiceException, IOException {
		LinkType stillLink = service.getStillLink("ZSHr5KQvsQ6S80HEHUg0NwJV", "admin");
		assertTrue("No StillLink found", stillLink.getOutput().startsWith("http://download.pilot.vpcore.snkn.nl/still/"));
		// the link is generated on each request, testing by using equals() fails.
		//assertTrue("No StillLink found",stillLink.getOutput().equals("http://download.pilot.vpcore.snkn.nl/still/eGdLfHlK39wRqJjUSYy0B7IK"));
	}
	
	@Test
	public void testCreateUploadTicket() throws ServiceException, IOException {
		String userId = "admin";
		assetId = service.createAsset(userId);
		String mediafileId = service.createMediafile(assetId, userId);
		
		UploadTicketType uploadTicket = service.createUploadTicket(mediafileId, userId);
		System.out.println("\n\n[ " + uploadTicket.getAction() + " ]\n\n");
		assertTrue("No uploadticket found", uploadTicket.getAction().startsWith("http://gir.ic.uva.nl/mediamosa/mediafile/upload"));
	}
	
	@Test
	public void testGetAssetDetails() throws ServiceException, IOException {
		AssetDetailsType assetDetails = service.getAssetDetails("V2MPUBacvSUx8PH23DnqYXt1");
		//assertTrue("MediaFileId and Asset don't match", assetDetails.getMediafiles().getMediafile().get(0).getMediafileId().endsWith("CoQk2UDRcffpwvTxHZU06Vpu"));
	}
	
	@Test
	public void testCreateAsset() throws ServiceException, IOException {
		assetId = service.createAsset("admin");
		assertTrue("Failed to create Asset", assetId != null);
	}
	
	@Test
	public void testSetMetadata() throws ServiceException, IOException {
		Map <String, String> metadata = new HashMap <String, String>();
		metadata.put("title", "metadata title");
		metadata.put("description", "metadata description");
		Response vpxResp = service.setMetadata(assetId, "admin", metadata);
		assertTrue("Failed to set metadata", vpxResp.getHeader().getRequestResultId() == ErrorCodes.ERRORCODE_OKAY);
	}
	
	@Test
	public void testDeleteAsset() throws ServiceException, IOException {
		service.deleteAsset(assetId, "admin", true);
		// service.deleteAsset("3cDgm4pQjCK8toapaHmiKmbz", "ad_tom", true);
		//assertTrue("Failed to delete asset", vpxResp.getHeader().getRequestResultId() == ErrorCodes.ERRORCODE_OKAY);
	}
	
	@Test
	public void testGetProfiles() throws ServiceException, IOException {
		assertTrue("Empty list of errorcodes", service.getProfiles().size() != 0);
	}
	
	@Test
	public void testGetAssets() throws ServiceException, IOException {
		Map <String, String> searchParams = new HashMap <String, String>();
		searchParams.put(SearchParameters.TITLE, "beren");
		List<AssetType> assetList = service.getAssets(searchParams);
		CharSequence sequence = "beren".subSequence(0, "beren".length()-1);
		assertTrue("Failed to find specified asset", assetList.get(0).getDublinCore().getTitle().contains(sequence));
	}
	/*
	@Test
	public void testGetAssetsOffsetLimit() throws ServiceException, IOException {
		
		getAssets(int limit, int offset, Map properties)	
	}
	*/
	
	@Test
	public void testGetCqlAssets() throws ServiceException, IOException {
		String searchterm = "weg";
		String cql = "((title all \"" + searchterm +  "\") OR (description all \"" + searchterm + "\") OR (subject all \"" + searchterm + "\") OR (owner_id all \"" + searchterm + "\") OR (group_id all \"" + searchterm + "\"))";
		List<AssetType> assetList = service.getCqlAssets(cql);
		CharSequence sequence = searchterm.subSequence(0, searchterm.length()-1);
		assertTrue("Failed to find specified asset", assetList.get(0).getDublinCore().getTitle().contains(sequence)  );
	}
	
	@Test
	public void testGetMediafileDetails() throws ServiceException, IOException {
		String mediafileId = "LVKa0EGcsl8P5bvXIMWAxWZF";
		MediafileDetailsType mediafileDetails = service.getMediafileDetails(mediafileId);
		assertTrue(mediafileId.equals(mediafileDetails.getMediafileId()));
		
	}
	
}
