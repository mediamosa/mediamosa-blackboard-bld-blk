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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class SHA1Util {
	private static final Logger log = Logger.getLogger(SHA1Util.class.getName());
	
	/**
	 * Creates a SHA1 hash from a <code>String</code>.
	 * @param value Input to be encoded.
	 * @return SHA1 encoded value of input.
	 */
	public static String getSHA1(String value) {
		
		try {
		    
			byte[] intext = value.getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(intext);
			byte[] digest = md.digest();
	        StringBuffer buf = new StringBuffer();
	        for (int i = 0; i < digest.length; i++) {
	        	int halfbyte = (digest[i] >>> 4) & 0x0F;
	        	int two_halfs = 0;
	        	do {
		            if ((0 <= halfbyte) && (halfbyte <= 9)) {
		                buf.append((char) ('0' + halfbyte));
		            } else {
		            	buf.append((char) ('a' + (halfbyte - 10)));
		            }
		            halfbyte = digest[i] & 0x0F;
	        	} while(two_halfs++ < 1);
	        }
	        return buf.toString();

		} catch(NoSuchAlgorithmException e) {
		    log.severe(e.getMessage());
			return null;			
		}
	}	
}
