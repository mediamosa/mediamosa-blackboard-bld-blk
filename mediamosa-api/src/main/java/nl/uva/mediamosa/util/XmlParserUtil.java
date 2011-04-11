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

package nl.uva.mediamosa.util;

import java.io.StringReader;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import nl.uva.mediamosa.Header;

public class XmlParserUtil {

	private static final Logger log = Logger.getLogger(XmlParserUtil.class.getName());
	
	/**
	 * @param content
	 * @return
	 */
	public static Header parseResponse(String content) {
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		String exprHeader = "/response/header";
		Header header = null;
		//InputSource inputSource = new InputSource("response.xml");
		InputSource inputSource = new InputSource(new StringReader(content));
		try {
			NodeList nodes = (NodeList) xpath.evaluate(exprHeader, inputSource, XPathConstants.NODESET);

			header = new Header();
			Element element = (Element) nodes.item(0);
			Element item_countNode = (Element) element.getElementsByTagName("item_count").item(0);
			header.setItemCount(Integer.parseInt(item_countNode.getTextContent()));
			
			Element request_process_timeNode = (Element) element.getElementsByTagName("request_process_time").item(0);
			header.setRequestProcessTime(Float.parseFloat(request_process_timeNode.getTextContent()));
			
			Element request_query_countNode = (Element) element.getElementsByTagName("request_query_count").item(0);
			header.setRequestQueryCount(Integer.parseInt(request_query_countNode.getTextContent()));
		    
			Element request_resultNode = (Element) element.getElementsByTagName("request_result").item(0);
			header.setRequestResult(request_resultNode.getTextContent());
			
		    Element request_result_descriptionNode = (Element) element.getElementsByTagName("request_result_description").item(0);
		    header.setRequestResultDescription(request_result_descriptionNode.getTextContent());
		    
		    Element request_result_idNode = (Element) element.getElementsByTagName("request_result_id").item(0);
		    header.setRequestResultId(Integer.parseInt(request_result_idNode.getTextContent()));
		    
		    Element request_uriNode = (Element) element.getElementsByTagName("request_uri").item(0);
		    header.setRequestUri(request_uriNode.getTextContent());
		    
		    Element vpx_versionNode = (Element) element.getElementsByTagName("vpx_version").item(0);
		    header.setVpxVersion(vpx_versionNode.getTextContent());
		    
		} catch (XPathExpressionException e) {
			log.severe(e.getMessage());
		}
		return header;
	}
}
