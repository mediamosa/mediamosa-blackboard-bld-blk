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
import java.util.Date;

public class MD5Util {
	
    /**
     * Creates a MD5 hash from a <code>String</code>.
     * @param value Input to be encoded.
	 * @return MD5 encoded value of input.
     */
	private static String getMD5(String value) {
		
		try {
		    
			byte[] intext = value.getBytes();
			StringBuffer sb = new StringBuffer();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md5rslt = md5.digest(intext);
			
			for (int i = 0; i < md5rslt.length; i++) {
				String tmpStr = "0" + Integer.toHexString((0xff & md5rslt[i]));
				sb.append(tmpStr.substring(tmpStr.length() - 2));
			}
			
			return sb.toString();
			
		} catch(NoSuchAlgorithmException e) {
		    		return null;			
		}
	}
	
	/**
	 * Creates a random <code>String</code> value of length 10.
	 * @return Randomly generated <code>String</code>.
	 */
	public static String getRandomValue() {
		Date now = new Date();
		String md5 = getMD5(Long.toString(now.getTime()));
		return md5.substring(0, 10);	
	}

}
