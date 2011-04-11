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

import nl.uva.mediamosa.util.ChallengeUtil;

import org.junit.Test;

public class ChallengeTest {

	@Test
	public void testChallenge(){
		
		String uuid = "b8225c6d20a6bef3d8dccfb3b5a30062";
		String text = "<random><xml><dbus>DATA vpx 0 b8225c6d20a6bef3d8dccfb3b5a30062</dbus></xml></random>";
		String challenge = ChallengeUtil.getChallenge(text);
		assertTrue("No matching challenge", uuid.equals(challenge));
	}
	
}
