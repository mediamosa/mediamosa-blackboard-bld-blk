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

import nl.uva.mediamosa.MediaMosaConnector;
import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.util.ServiceException;

public class ConnectorTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		MediaMosaConnector vpcon = new MediaMosaConnector("http://app.pilot.vpcore.snkn.nl");
		vpcon.setCredentials("UvA", "8zvZ7HjSToWakBOuJGDROGkB");
		vpcon.doLogin();
		//vpcon.doLogin("UvA", "8zvZ7HjSToWakBOuJGDROGkB");
		//vpcon.doLogin("UvA", "8zvZ7HjSToWakBOuJGDROGkB2"); //fout met opzet
		vpcon.doGetRequest("/asset?limit=50");
		vpcon.doGetRequest("/check_cookie");
		vpcon.doGetRequest("/transcode/profiles");
		//vpcon.isValidCookie();
		//vpcon.doGetRequest("/preview_profile_id");
		//vpcon.doPostRequest("/metadata_tag/create", "name=custom_tag_1&type=blah");
		//vpcon.doPostRequest("/metadata_tag/delete", "name=custom_tag_1");
		
		MediaMosaService service = new MediaMosaService("http://app.pilot.vpcore.snkn.nl");
		try {
			service.getPlayLink("V2MPUBacvSUx8PH23DnqYXt1", "CoQk2UDRcffpwvTxHZU06Vpu", "admin");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
