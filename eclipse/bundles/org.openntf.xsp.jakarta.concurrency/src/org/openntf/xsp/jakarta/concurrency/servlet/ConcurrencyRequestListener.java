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
package org.openntf.xsp.jakarta.concurrency.servlet;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;

/**
 * 
 * @author Jesse Gallagher
 * @since 2.7.0
 */
public class ConcurrencyRequestListener implements AbstractConcurrencyJndiConfigurator, ServletRequestListener {
	
	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		pushExecutors(sre.getServletContext());
	}
	
	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		popExecutors(sre.getServletContext());
	}
}
