/**
 * Copyright © 2018 Jesse Gallagher
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

import java.util.Arrays;

import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.library.AbstractXspLibrary;

public class CDILibrary extends AbstractXspLibrary {
	public static final String LIBRARY_ID = CDILibrary.class.getPackage().getName();

	@Override
	public String getLibraryId() {
		return LIBRARY_ID;
	}
	
	@Override
	public String getPluginId() {
		return Activator.getDefault().getBundle().getSymbolicName();
	}
	
	@Override
	public String[] getDependencies() {
		return new String[] {
			"com.ibm.xsp.core.library", //$NON-NLS-1$
			"com.ibm.xsp.extsn.library", //$NON-NLS-1$
			"com.ibm.xsp.designer.library" //$NON-NLS-1$
		};
	}
	
	@Override
	public boolean isGlobalScope() {
		return false;
	}
	
	@Override
	public String[] getFacesConfigFiles() {
		return new String[] {
			"/res/jsf.xml" //$NON-NLS-1$
		};
	}

	public static boolean usesLibrary(ApplicationEx app) {
		String prop = app.getProperty("xsp.library.depends", ""); //$NON-NLS-1$ //$NON-NLS-2$
		return Arrays.asList(prop.split(",")).contains(LIBRARY_ID); //$NON-NLS-1$
	}

}