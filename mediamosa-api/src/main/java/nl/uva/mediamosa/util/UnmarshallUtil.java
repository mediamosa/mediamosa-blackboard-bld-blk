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

import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;

import nl.uva.mediamosa.model.Response;

public class UnmarshallUtil {
	
	private static final Logger log = Logger.getLogger(UnmarshallUtil.class.getName());

	/**
	 * @param is
	 * @return
	 */
	public static Response unmarshall(InputStream is){
		JAXBContext context;
		Response response = null;
		try {
			context = JAXBContext.newInstance("nl.uva.mediamosa.model");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			response = (Response) unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			log.severe(e.getMessage());
		}
		return response;
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static Response unmarshall(String str){
		JAXBContext context;
		Response response = null;
		try {
			context = JAXBContext.newInstance("nl.uva.mediamosa.model");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = IOUtils.toInputStream(str);
			response = (Response) unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			log.severe(e.getMessage());
		}
		return response;
	}
}
