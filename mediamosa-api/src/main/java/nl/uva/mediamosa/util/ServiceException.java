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

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * The Class ServiceException.
 */
public class ServiceException extends Exception {
	
    /**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 8811217125415893940L;

	/**
     * Instantiates a new service exception.
     * 
     * @param message the message
     */
    public ServiceException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new service exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates a new service exception.
	 * 
	 * @param cause the cause
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}	
	
	/**
	 * Instantiates a new service exception.
	 * 
	 * @param httpConn the http conn
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ServiceException(HttpURLConnection httpConn) throws IOException {
		super(httpConn.getResponseMessage());
	}

}
