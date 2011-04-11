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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.util.ServiceException;

import org.junit.BeforeClass;

public class AbstractTest {

	protected static String USERNAME;

	protected static String PASSWORD;
	
	protected static String HOSTNAME;

	protected static Properties sysProp = System.getProperties();

	protected static MediaMosaService service;
	
	protected static String assetId;

	/**
	 * @throws ServiceException 
	 * 
	 */
	@BeforeClass
	public static void init() throws ServiceException {

		Properties prop = getProperties();

		USERNAME = prop.getProperty("vpcore.username");
		PASSWORD = prop.getProperty("vpcore.password");
		HOSTNAME = prop.getProperty("vpcore.hostname");

		service = new MediaMosaService(HOSTNAME);
		service.setCredentials(USERNAME, PASSWORD);

	}
	
	/**
	 * @return
	 */
	private static Properties getProperties() {
		Properties prop = new Properties();

		try {
			InputStream ips = AbstractTest.class.getResourceAsStream("/vpcore.properties");
			prop.load(ips);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}
}
