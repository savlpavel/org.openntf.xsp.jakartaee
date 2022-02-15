/**
 * Copyright © 2018-2022 Jesse Gallagher
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
package org.openntf.xsp.test.beanbundle.servlet;


import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;

import org.eclipse.core.runtime.Platform;
import org.jboss.resteasy.cdi.CdiInjectorFactory;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.util.ForwardingBeanManager;
import org.openntf.xsp.cdi.util.ContainerUtil;
import org.osgi.framework.Bundle;

import com.ibm.designer.domino.napi.NotesAPIException;
import com.ibm.designer.domino.napi.NotesDatabase;
import com.ibm.domino.osgi.core.context.ContextInfo;

public class ExampleCDIInjectorFactory extends CdiInjectorFactory {
	private static final BeanManager manager = new ForwardingBeanManager() {
		private static final long serialVersionUID = 1L;

		@Override
		public BeanManager delegate() {
			// Check if we have a NotesDatabase available
			try {
				CDI<Object> container = null;
				NotesDatabase db = ContextInfo.getServerDatabase();
				if(db != null) {
					container = ContainerUtil.getContainer(db);
				}
				// If it couldn't find it from the context DB, use the known-default bundle
				if(container == null) {
					// Failing that, use the known-default bundle
					Bundle xsp = Platform.getBundle("org.openntf.xsp.test.beanbundle"); //$NON-NLS-1$
					container = ContainerUtil.getContainer(xsp);
				}
				if(container != null) {
					BeanManager manager = container.getBeanManager();
					if(manager instanceof BeanManagerImpl) {
						return manager;
					} else if(manager instanceof ForwardingBeanManager) {
						return ((ForwardingBeanManager)manager).delegate();
					} else {
						throw new IllegalStateException("Cannot find BeanManagerImpl in " + manager); //$NON-NLS-1$
					}
				}
				return null;
			} catch(NotesAPIException e) {
				throw new RuntimeException(e);
			}
		}
	};

	public ExampleCDIInjectorFactory() {
	}

	@Override
	protected BeanManager lookupBeanManager() {
		return manager;
	}

}