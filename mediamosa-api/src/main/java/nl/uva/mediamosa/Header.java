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

public class Header {
	private int itemCount;
	private float requestProcessTime;
	private int requestQueryCount;
	private String requestResult;
	private String requestResultDescription;
	private int requestResultId;
	private String requestUri;
	private String vpxVersion;
	
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public float getRequestProcessTime() {
		return requestProcessTime;
	}
	public void setRequestProcessTime(float requestProcessTime) {
		this.requestProcessTime = requestProcessTime;
	}
	public int getRequestQueryCount() {
		return requestQueryCount;
	}
	public void setRequestQueryCount(int requestQueryCount) {
		this.requestQueryCount = requestQueryCount;
	}
	public String getRequestResult() {
		return requestResult;
	}
	public void setRequestResult(String requestResult) {
		this.requestResult = requestResult;
	}
	public String getRequestResultDescription() {
		return requestResultDescription;
	}
	public void setRequestResultDescription(String requestResultDescription) {
		this.requestResultDescription = requestResultDescription;
	}
	public int getRequestResultId() {
		return requestResultId;
	}
	public void setRequestResultId(int requestResultId) {
		this.requestResultId = requestResultId;
	}
	public String getRequestUri() {
		return requestUri;
	}
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}
	public String getVpxVersion() {
		return vpxVersion;
	}
	public void setVpxVersion(String vpxVersion) {
		this.vpxVersion = vpxVersion;
	}

}
