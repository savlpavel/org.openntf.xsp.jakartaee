/**
 * Copyright © 2018-2022 Contributors to the XPages Jakarta EE Support Project
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
package it.org.openntf.xsp.jakartaee.concurrency;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.org.openntf.xsp.jakartaee.AbstractWebClientTest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

@SuppressWarnings("nls")
public class TestConcurrency extends AbstractWebClientTest {
	@Test
	public void testBasics() {
		Client client = getAnonymousClient();
		WebTarget target = client.target(getRestUrl(null) + "/concurrency");
		Response response = target.request().get();
		
		String output = response.readEntity(String.class);
		
		assertTrue(output.startsWith("Hello from executor\n"), () -> "Received unexpected output: " + output);
		assertTrue(output.contains("CDI is org"), () -> "Received unexpected output: " + output);
		assertTrue(output.contains("Username is: Anonymous"), () -> "Received unexpected output: " + output);
		assertTrue(output.contains("Database is: dev"), () -> "Received unexpected output: " + output);
		assertTrue(output.contains("applicationGuy is: I'm application guy"), () -> "Received unexpected output: " + output);
	}

	@Test
	public void testBasicsAuthenticated() {
		Client client = getAdminClient();
		WebTarget target = client.target(getRestUrl(null) + "/concurrency");
		Response response = target.request().get();
		
		String output = response.readEntity(String.class);
		
		assertTrue(output.startsWith("Hello from executor\n"), () -> "Received unexpected output: " + output);
		assertTrue(output.contains("CDI is org"), () -> "Received unexpected output: " + output);
		assertTrue(output.contains("Username is: CN="), () -> "Received unexpected output: " + output);
		assertTrue(output.contains("Database is: dev"), () -> "Received unexpected output: " + output);
		assertTrue(output.contains("applicationGuy is: I'm application guy"), () -> "Received unexpected output: " + output);
	}
	
	@Test
	public void testScheduled() {
		Client client = getAnonymousClient();
		WebTarget target = client.target(getRestUrl(null) + "/concurrency/scheduled");
		Response response = target.request().get();
		
		String output = response.readEntity(String.class);
		
		assertTrue(output.contains("hello from scheduler"), () -> "Received unexpected output: " + output);
	}
	
	@Test
	public void testXPages() {
		Client client = getAnonymousClient();
		WebTarget target = client.target(getRootUrl(null) + "/concurrency.xsp");
		Response response = target.request().get();
		
		String output = response.readEntity(String.class);
		
		assertTrue(output.contains("bean says: Hello from executor"), () -> "Received unexpected output: " + output);
	}
}
