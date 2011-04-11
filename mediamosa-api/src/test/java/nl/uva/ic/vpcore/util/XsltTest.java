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

package nl.uva.ic.vpcore.util;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import nl.uva.mediamosa.util.XsltUtil;

import org.apache.commons.io.IOUtils;

public class XsltTest {

	@Test
	public void testTransform() throws TransformerException, IOException, ParserConfigurationException, SAXException {
		
		File input = new File("src/test/resources/response.xml");
		InputStream inputStream = new FileInputStream(input);
		String xmlaftertransform = XsltUtil.transform(IOUtils.toString(inputStream), new File("src/test/resources/convert_item_elements.xsl"));
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputStream is = new ByteArrayInputStream(xmlaftertransform.getBytes("UTF-8"));
		Document document =  builder.parse(is);
		NodeList nl = document.getElementsByTagName("asset");
		assertTrue("No asset elements in result", nl.getLength()>0);
	}
	
	@Test
	public void testTransformErrorCodes() throws TransformerException, IOException, ParserConfigurationException, SAXException {
		
		File input = new File("src/test/resources/response_errorcodes.xml");
		InputStream inputStream = new FileInputStream(input);
		String xmlaftertransform = XsltUtil.transform(IOUtils.toString(inputStream), new File("src/test/resources/convert_item_elements.xsl"));
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputStream is = new ByteArrayInputStream(xmlaftertransform.getBytes("UTF-8"));
		Document document =  builder.parse(is);
		NodeList nl = document.getElementsByTagName("errorcode");
		assertTrue("No errorcode elements in result", nl.getLength()>0);
	}
	
	@Test
	public void testTransformLink() throws TransformerException, IOException, ParserConfigurationException, SAXException {
		File input = new File("src/test/resources/response_link.xml");
		InputStream inputStream = new FileInputStream(input);
		String xmlaftertransform = XsltUtil.transform(IOUtils.toString(inputStream), new File("src/test/resources/convert_item_elements.xsl"));
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputStream is = new ByteArrayInputStream(xmlaftertransform.getBytes("UTF-8"));
		Document document =  builder.parse(is);
		NodeList nl = document.getElementsByTagName("link");
		assertTrue("No link elements in result", nl.getLength()>0);		
	}

}
