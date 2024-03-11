/**
 * Copyright (c) 2018-2024 Contributors to the XPages Jakarta EE Support Project
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
package config;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * Demonstrates the use of a custom {@link ConfigSource} implementation provided
 * by {@link ExampleConfigSourceProvider}.
 * 
 * @since 2.11.0
 */
public class ExampleProvidedConfigSource implements ConfigSource {

	private final Map<String, String> properties;
	
	public ExampleProvidedConfigSource() {
		properties = Collections.unmodifiableMap(Collections.singletonMap("ExampleProvidedConfig", "I am the example value from a provided source"));
	}
	
	@Override
	public Set<String> getPropertyNames() {
		return properties.keySet();
	}

	@Override
	public String getValue(String propertyName) {
		return properties.get(propertyName);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

}
