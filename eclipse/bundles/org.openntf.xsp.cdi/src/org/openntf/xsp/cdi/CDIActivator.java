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
package org.openntf.xsp.cdi;

import org.openntf.xsp.cdi.provider.NSFCDIProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import jakarta.enterprise.inject.spi.CDI;

/**
 * @author Jesse Gallagher
 * @since 2.5.0
 */
public class CDIActivator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		CDI.setCDIProvider(new NSFCDIProvider());
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}