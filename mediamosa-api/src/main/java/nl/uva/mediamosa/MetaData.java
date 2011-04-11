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

/*
 * GLobal metadate definitions
 */
public interface MetaData {

	public static final String TYPE = "type";
	public static final String FORMAT = "format";
	public static final String LANGUAGE = "language";
	public static final String TITLE = "title";
	public static final String CREATOR = "creator";
	public static final String PUBLISHER = "publisher";
	public static final String SUBJECT = "subject";
	public static final String DESCRIPTION = "description";
	public static final String CONTRIBUTOR = "contributor";
	public static final String DATE = "date"; //ISO 8601 YYYY-MM-DD HH:MM:SS
	public static final String IDENTIFIER = "identifier";
	public static final String SOURCE = "source";
	public static final String RELATION = "relation";
	public static final String COVERAGE_TEMPORAL = "coverage_temporal";
	public static final String COVERAGE_SPATIAL = "coverage_spatial";
	public static final String RIGHTS = "rights";
	public static final String ISREFERENCEDBY = "isreferencedby";
	public static final String TITLE_ALTERNATIVE = "title_alternative";
	public static final String DESCRIPTION_ABSTRACT = "description_abstract";
	public static final String CREATED = "created";  //ISO 8601 YYYY-MM-DD HH:MM:SS
	public static final String ISSUED = "issued";  //ISO 8601 YYYY-MM-DD HH:MM:SS
	public static final String HASFORMAT = "hasformat";
	public static final String ISFORMATOF = "isformatof";
	public static final String FORMAT_MEDIUM = "format_medium";
	public static final String FORMAT_EXTENT = "format_extent";
	public static final String LICENSE = "license";
	public static final String RIGHTSHOLDER = "rightsholder";
}
