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

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XsltUtil {

	/**
	 * @param xmlsource
	 * @param xsltfile
	 * @return
	 * @throws TransformerException
	 */
	public static String transform(String xmlsource, File xsltfile) throws TransformerException{

		Source xmlSource = new StreamSource(new StringReader(xmlsource));
		Source xsltSource = new StreamSource(xsltfile);
		StringWriter stringWriter = new StringWriter();
		
		TransformerFactory transFact = TransformerFactory.newInstance();
		Templates cachedXSLT = transFact.newTemplates(xsltSource);
		Transformer trans = cachedXSLT.newTransformer();
		trans.transform(xmlSource, new StreamResult(stringWriter));
		
		return stringWriter.toString();
	}
	
	/**
	 * @param xmlsource
	 * @param xsltstream
	 * @return
	 * @throws TransformerException
	 */
	public static String transform(String xmlsource, InputStream xsltstream) throws TransformerException{

		Source xmlSource = new StreamSource(new StringReader(xmlsource));
		Source xsltSource = new StreamSource(xsltstream);
		StringWriter stringWriter = new StringWriter();
		
		TransformerFactory transFact = TransformerFactory.newInstance();
		Templates cachedXSLT = transFact.newTemplates(xsltSource);
		Transformer trans = cachedXSLT.newTransformer();
		trans.transform(xmlSource, new StreamResult(stringWriter));
		
		return stringWriter.toString();
	}
	
}
