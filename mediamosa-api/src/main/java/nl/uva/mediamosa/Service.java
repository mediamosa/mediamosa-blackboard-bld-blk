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

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import nl.uva.mediamosa.util.ServiceException;



/**
 * The Class Service.
 */
public abstract class Service {
	
	/** 
	 * Logger instance. 
	 */
	public static final Logger logger = Logger.getLogger(Service.class.getName());

	protected void doGetRequest() throws ServiceException {
		
	}
	
	protected void doPostRequest() throws ServiceException {
		
	}
	
	/**
	 * Handles response from VP-Core service. The default implementation is 
	 * empty and may be overwritten in specific implementations.
	 * 
	 * @param responseStream Response stream from vpcore
	 * 
	 * @throws ServiceException the service exception
	 */
	protected void processResponse(InputStream responseStream) throws ServiceException {
		//TODO: Add default HTTP response handling
		//empty implementation
	}
	
	/**
	 * Returns the version of the underlying service
	 * 
	 * @return Protocol version string
	 * @throws ServiceException 
	 * @throws IOException 
	 */
	public abstract String getVersion() throws ServiceException, IOException;

	
}
