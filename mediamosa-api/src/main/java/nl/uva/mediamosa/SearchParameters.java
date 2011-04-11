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

public interface SearchParameters {
	
	public static final String CQL = "cql";
	public static final String ORDERBY = "order_by";
	public static final String ORDERDIRECTION = "order_direction";
	public static final String GRANTED = "granted";
	public static final String ISPUBLICLIST = "is_public_list";
	public static final String HIDEEMPTYASSETS = "hide_empty_assets";
	public static final String USERID = "user_id";
	public static final String AUTUSERID = "aut_user_id";
	
	public static final String ASSETID = "asset_id";
	public static final String GROUPID = "group_id";
	public static final String OWNERID = "owner_id";
	public static final String PROVIDER_ID = "provider_id";
	public static final String REFERENCEID = "reference_id";
	public static final String MEDIAFILEDURATION = "mediafile_duration";
	public static final String MEDIAFILECONTAINERTYPE = "mediafile_container_type";
	public static final String FILENAME = "filename";
	public static final String MIMETYPE = "mime_type";
	
	// dublin core fields
	public static final String TYPE = "type";
	public static final String LANGUAGE = "language";
	public static final String TITLE = "title[]";
	public static final String CREATOR = "creator";
	public static final String DESCRIPTION = "description[]";
	public static final String DATE = "date"; //of als Date()?
	public static final String FORMAT = "format";
	public static final String PUBLISHER = "publisher";
	public static final String SUBJECT = "subject";
	public static final String CONTRIBUTOR = "contributor";
	public static final String IDENTIFIER = "identifier";
	public static final String SOURCE = "source";
	public static final String RELATION = "relation";
	public static final String COVERAGETEMPORAL = "coverage_temporal";
	public static final String COVERAGESPATIAL = "coverage_spatial";
	public static final String RIGHT = "rights";
	
	// qualified dublin core fields
	public static final String ISREFERENCEDBY = "isreferencedby";
	public static final String TITLEALTERNATIVE = "title_alternative";
	public static final String DESCRIPTIONABSTRACT = "description_abstract";
	public static final String CREATED = "created";
	public static final String ISSUED = "issued";
	public static final String HASFORMAT = "hasformat";
	public static final String ISFORMATOF = "isformatof";
	public static final String FORMATMEDIUM = "format_medium";
	public static final String FORMATEXTENT = "format_extent";
	public static final String LICENSE = "license";
	public static final String RIGHSHOLDER = "rightsholder";
	
	// <field name>_match
	/*
	By default, all search matches "contains", but these can be adjusted using <fieldname> _match = <type>.
	Choice of: 'exact', 'contains' and 'begin'
	Example: title=test&title_match=exact
	*/
	public static final String OPERATOR = "operator"; //(operator=OR|AND) default = AND
	public static final String VIDEOTIMESTAMP = "videotimestamp";
	public static final String VIDEOTIMESTAMPMODIFIED = "videotimestampmodified";	
	//coll_id, coll_id[]
	public static final String COLLID = "coll_id";
	//aut_group_id, aut_group_id[]
	public static final String AUTGROUPID = "aut_group_id";
	public static final String AUTDOMAIN = "aut_domain";
	public static final String AUTREALM = "aut_realm";
	public static final String ISAPPADMIN = "is_app_admin";
	public static final String FAVUSERID = "fav_user_id";
	
}
