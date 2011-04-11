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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import nl.uva.mediamosa.model.*;
import nl.uva.mediamosa.util.DataTypeConverterUtil;

public class JaxbTest {

	/**
	 * @param args
	 * @throws JAXBException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{

			JAXBContext context = JAXBContext.newInstance("nl.uva.mediamosa.model");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			// this implementation is a part of the API and convenient for trouble-shooting,
			// as it prints out errors to System.out
			//unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			Response response = (Response)unmarshaller.unmarshal(new File("src/test/resources/response_after_xsl.xml"));
			//Asset asset = (Asset)unmarshaller.unmarshal(new File("asset.xml"));
			
			System.out.println(response.getHeader().getVpxVersion());
			
			System.out.println(response.getItems().getLinkOrAssetOrAssetDetails().size());
			
			ItemsType items = response.getItems();
			
			List <AssetType> assets = new ArrayList<AssetType>();
			List <AssetDetailsType> assetDetails = new ArrayList<AssetDetailsType>();
			List <CollectionType> collections = new ArrayList<CollectionType>();
			List <ErrorcodeType> errorcodes = new ArrayList<ErrorcodeType>();
			List <GroupDetailsType> groupDetails = new ArrayList<GroupDetailsType>();
			List <GroupType> grouptypes = new ArrayList<GroupType>();
			List <JobDetailsType> jobDetails = new ArrayList<JobDetailsType>();
			List <JobType> jobs = new ArrayList<JobType>();
			List <LinkType> links = new ArrayList<LinkType>();
			List <MediafileDetailsType> mediafileDetails = new ArrayList<MediafileDetailsType>();
			List <MediafileType> mediafiles = new ArrayList<MediafileType>();
			List <MetadataTagType> metadatatags = new ArrayList<MetadataTagType>();
			List <PreviewProfileType> previewprofiles = new ArrayList<PreviewProfileType>();
			List <ProfileType> profiles = new ArrayList<ProfileType>();
			List <StatsDatauploadType> datauploadsStats = new ArrayList<StatsDatauploadType>();
			List <StatsDatausagevideoType> datausagevideoStats = new ArrayList<StatsDatausagevideoType>();
			List <StatsPlayedstreamsType> playedstreamsStats = new ArrayList<StatsPlayedstreamsType>();
			List <StatsPopularcollectionsType> popularcollectionsStats = new ArrayList<StatsPopularcollectionsType>();
			List <StatsPopularstreamsType> popularstreamsStats = new ArrayList<StatsPopularstreamsType>();
			List <SupplementType> supplements = new ArrayList<SupplementType>();
			List <UserDetailsType> userDetails = new ArrayList<UserDetailsType>();
			List <UserType> users = new ArrayList<UserType>();
			
			
			for (Object o : items.getLinkOrAssetOrAssetDetails() ) {
				if (o instanceof AssetType) {
					assets.add((AssetType) o);
				} else if (o instanceof AssetDetailsType) {
					assetDetails.add((AssetDetailsType) o);
				} else if (o instanceof CollectionType) {
					collections.add((CollectionType) o); 
				} else if (o instanceof ErrorcodeType) {
					errorcodes.add((ErrorcodeType) o); 
				} else if (o instanceof GroupDetailsType) {
					groupDetails.add((GroupDetailsType) o); 
				} else if (o instanceof GroupType) {
					grouptypes.add((GroupType) o);
				} else if (o instanceof JobDetailsType) {			
					jobDetails.add((JobDetailsType) o);
				} else if (o instanceof JobType) {					
					jobs.add((JobType) o);
				} else if (o instanceof LinkType) {
					links.add((LinkType) o);
				} else if (o instanceof MediafileDetailsType) {
					mediafileDetails.add((MediafileDetailsType) o);
				} else if (o instanceof MediafileType) {
					mediafiles.add((MediafileType) o);
				} else if (o instanceof MetadataTagType) {
					metadatatags.add((MetadataTagType) o);					
				} else if (o instanceof PreviewProfileType) {
					previewprofiles.add((PreviewProfileType) o);
				} else if (o instanceof ProfileType) {
					profiles.add((ProfileType) o);
				} else if (o instanceof StatsDatauploadType) {
					datauploadsStats.add((StatsDatauploadType) o);
				} else if (o instanceof StatsDatausagevideoType) {
					datausagevideoStats.add((StatsDatausagevideoType) o);
				} else if (o instanceof StatsPlayedstreamsType) {
					playedstreamsStats.add((StatsPlayedstreamsType) o);
				} else if (o instanceof StatsPopularcollectionsType) {
					popularcollectionsStats.add((StatsPopularcollectionsType) o);
				} else if (o instanceof StatsPopularstreamsType) {
					popularstreamsStats.add((StatsPopularstreamsType) o);
				} else if (o instanceof SupplementType) {
					supplements.add((SupplementType) o);
				} else if (o instanceof UserDetailsType) {
					userDetails.add((UserDetailsType) o);
				} else if (o instanceof UserType) {
					users.add((UserType) o);
				} else {
					throw new IllegalArgumentException( "class " + o.getClass() );
				}
			}
			
			
			AssetType asset = assets.get(0);
			Calendar cal = asset.getVideotimestampmodified();
			System.out.println("videostampmodified: " + cal.getTime());
			System.out.println("videostampmodified (printMethod) : " + DataTypeConverterUtil.printDateTime(cal));
			System.out.println("times played: " + asset.getPlayed());
			
			/*
			System.out.println("asset_id = " + asset.getAssetId());
			System.out.println("app_id = "+ asset.getAppId());
			System.out.println(asset.getVideotimestamp());
			System.out.println(asset.isIsprivate());
			System.out.println(asset.dublinCore.getDescription());
			System.out.println(asset.getMediafileDuration());
			*/
			
		} catch (Exception e) {
			System.out.println("###\n" + e);
		}
	}

}
