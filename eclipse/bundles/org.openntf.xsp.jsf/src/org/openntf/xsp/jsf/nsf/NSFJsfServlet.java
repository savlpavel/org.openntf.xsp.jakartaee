/**
 * Copyright (c) 2018-2023 Contributors to the XPages Jakarta EE Support Project
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
package org.openntf.xsp.jsf.nsf;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.myfaces.ee.MyFacesContainerInitializer;
import org.apache.myfaces.shared.config.MyfacesConfig;
import org.eclipse.core.runtime.FileLocator;
import org.openntf.xsp.cdi.context.AbstractProxyingContext;
import org.openntf.xsp.cdi.util.ContainerUtil;
import org.openntf.xsp.jakartaee.servlet.ServletUtil;
import org.openntf.xsp.jakartaee.util.LibraryUtil;
import org.openntf.xsp.jakartaee.util.ModuleUtil;
import org.openntf.xsp.jsf.util.FacesBlockingClassLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;

import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.designer.runtime.domino.adapter.util.XSPErrorPage;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.FactoryFinder;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.HandlesTypes;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

/**
 * 
 * @author Jesse Gallagher
 * @since 2.4.0
 */
public class NSFJsfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(NSFJsfServlet.class.getName());

	private static final String PROP_SESSIONINIT = NSFJsfServlet.class.getName() + "_sessionInit"; //$NON-NLS-1$
	private static final String PROP_CLASSLOADER = NSFJsfServlet.class.getName() + "_classLoader"; //$NON-NLS-1$

	private final ComponentModule module;
	private FacesServlet delegate;
	private boolean initialized;
	private final Collection<Path> tempFiles = Collections.synchronizedList(new ArrayList<>());

	public NSFJsfServlet(ComponentModule module) {
		super();
		this.module = module;
	}

	public void doInit(HttpServletRequest req, ServletConfig config) throws ServletException {
		CDI<Object> cdi = CDI.current();
		ServletContext context = config.getServletContext();
		context.setAttribute("jakarta.enterprise.inject.spi.BeanManager", ContainerUtil.getBeanManager(cdi)); //$NON-NLS-1$
		context.setInitParameter(MyfacesConfig.INIT_PARAM_SUPPORT_JSP_AND_FACES_EL, String.valueOf(false));
		context.setInitParameter(MyfacesConfig.INIT_PARAM_SUPPORT_MANAGED_BEANS, String.valueOf(false));
		// TODO investigate why partial state saving doesn't work with a basic form
		context.setInitParameter("jakarta.faces.PARTIAL_STATE_SAVING", "false"); //$NON-NLS-1$ //$NON-NLS-2$

		Properties props = LibraryUtil.getXspProperties(module);
		String projectStage = props.getProperty(ProjectStage.PROJECT_STAGE_PARAM_NAME, ""); //$NON-NLS-1$
		context.setInitParameter(ProjectStage.PROJECT_STAGE_PARAM_NAME, projectStage);

		Bundle b = FrameworkUtil.getBundle(FacesServlet.class);
		Bundle b2 = FrameworkUtil.getBundle(MyFacesContainerInitializer.class);
		{
			ServletContainerInitializer initializer = new MyFacesContainerInitializer();
			Set<Class<?>> classes = null;
			HandlesTypes types = initializer.getClass().getAnnotation(HandlesTypes.class);
			if (types != null) {
				classes = ModuleUtil.buildMatchingClasses(types, module, b, b2);
			}
			initializer.onStartup(classes, getServletContext());
		}

		{
			// Re-wrap the ServletContext to provide the context path
			javax.servlet.ServletContext oldCtx = ServletUtil.newToOld(getServletContext());
			ServletContext ctx = ServletUtil.oldToNew(req.getContextPath(), oldCtx, 5, 0);
			ServletUtil.contextInitialized(ctx);
		}
		
		this.delegate = new FacesServlet();
		delegate.init(config);
	}

	@Override
	// TODO see if synchronization can be handled better
	public synchronized void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ServletContext ctx = req.getServletContext();
		HttpSession session = req.getSession(true);

		try {
			AccessController.doPrivileged((PrivilegedExceptionAction<Void>) () -> {
				ClassLoader current = Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(buildJsfClassLoader(ctx, session, current));
				try {
					if (!initialized) {
						this.doInit(req, getServletConfig());
						initialized = true;
					}

					ContainerUtil.setThreadContextDatabasePath(req.getContextPath().substring(1));
					AbstractProxyingContext.setThreadContextRequest(req);
					ServletUtil.getListeners(ctx, ServletRequestListener.class)
							.forEach(l -> l.requestInitialized(new ServletRequestEvent(getServletContext(), req)));

					// Fire the session listener if needed
					if (!"1".equals(session.getAttribute(PROP_SESSIONINIT))) { //$NON-NLS-1$
						ServletUtil.getListeners(ctx, HttpSessionListener.class)
								.forEach(l -> l.sessionCreated(new HttpSessionEvent(session)));
						session.setAttribute(PROP_SESSIONINIT, "1"); //$NON-NLS-1$
						// TODO add a hook for session expiration?
					}
					

					delegate.service(req, resp);
				} finally {
					
					ServletUtil.getListeners(ctx, ServletRequestListener.class)
							.forEach(l -> l.requestDestroyed(new ServletRequestEvent(getServletContext(), req)));
					Thread.currentThread().setContextClassLoader(current);
					ContainerUtil.setThreadContextDatabasePath(null);
					AbstractProxyingContext.setThreadContextRequest(null);
				}
				return null;
			});
		} catch (Throwable t) {
			if(log.isLoggable(Level.SEVERE)) {
				log.log(Level.SEVERE, "Encountered unhandled exception in Servlet", t);
			}
			
			try (PrintWriter w = resp.getWriter()) {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				XSPErrorPage.handleException(w, t, req.getRequestURL().toString(), false);
			} catch (javax.servlet.ServletException e) {
				throw new IOException(e);
			} catch (IllegalStateException e) {
				// Happens when the writer or output has already been opened
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} finally {
			// In case it's not flushed on its own
			ServletUtil.close(resp);
		}
	}

	@Override
	public void destroy() {
		ServletContext ctx = getServletContext();
		ServletUtil.getListeners(ctx, ServletContextListener.class)
				.forEach(l -> l.contextDestroyed(new ServletContextEvent(ctx)));

		tempFiles.forEach(path -> {
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				// Ignore
			}
		});
		tempFiles.clear();
		
		ClassLoader cl = (ClassLoader)ctx.getAttribute(PROP_CLASSLOADER);
		if(cl != null && cl instanceof Closeable) {
			try {
				((Closeable)cl).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ctx.removeAttribute(PROP_CLASSLOADER);
		
		this.delegate.destroy();

		super.destroy();
	}

	// *******************************************************************************
	// * Internal utility methods
	// *******************************************************************************
	
	private static final Map<String, FacesBlockingClassLoader> cached = new ConcurrentHashMap<>();

	@SuppressWarnings("deprecation")
	private synchronized ClassLoader buildJsfClassLoader(ServletContext context, HttpSession session, ClassLoader delegate)
			throws BundleException, IOException {
		if (context.getAttribute(PROP_CLASSLOADER) == null) {
			
			// If the app was refreshed, we'll still have a lingering classloader here
			String id = ModuleUtil.getModuleId(module);
			FacesBlockingClassLoader old = cached.get(id);
			if(old != null) {
				destroyOldContext(old, session);
			}
			
			List<URL> urls = new ArrayList<>();
			urls.add(FileLocator.getBundleFile(FrameworkUtil.getBundle(FactoryFinder.class)).toURI().toURL());
			urls.add(FileLocator.getBundleFile(FrameworkUtil.getBundle(MyFacesContainerInitializer.class)).toURI().toURL());

			// Look for JARs in WEB-INF/lib-jakarta
			ModuleUtil.listFiles(module, "WEB-INF/jakarta/lib") //$NON-NLS-1$
				.filter(file -> file.toLowerCase().endsWith(".jar")) //$NON-NLS-1$
				.map(jarName -> {
					try {
						return module.getResource("/" + jarName); //$NON-NLS-1$
					} catch (MalformedURLException e) {
						throw new UncheckedIOException(e);
					}
				})
				.forEach(urls::add);

			FacesBlockingClassLoader cl = new FacesBlockingClassLoader(urls.toArray(new URL[urls.size()]), delegate);
			
			context.setAttribute(PROP_CLASSLOADER, cl);
			cached.put(id, cl);
		}
		return (ClassLoader) context.getAttribute(PROP_CLASSLOADER);
	}
	
	private void destroyOldContext(FacesBlockingClassLoader old, HttpSession session) throws IOException {
		ClassLoader current = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(old);
		try {
			FactoryFinder.releaseFactories();
		} finally {
			Thread.currentThread().setContextClassLoader(current);
		}
		old.close();
		
		tempFiles.forEach(path -> {
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				// Ignore
			}
		});
		tempFiles.clear();
		
		// TODO see if we can handle this differently either by moving
		//      CDI to be based on ComponentModule and refresh that way
		//      or by making this an AbstractXspLifecycleServlet
		// This is not ideal if you're mixing JSF and other technologies
		CDI<Object> container = CDI.current();
		if(container instanceof AutoCloseable) { //WeldContainer
			AutoCloseable c = (AutoCloseable)container;
			try {
				c.close();
			} catch(Exception e) {
				// Ignore - will be thrown if it was already shut down
			}
		}
		
		this.initialized = false;
	}
}
