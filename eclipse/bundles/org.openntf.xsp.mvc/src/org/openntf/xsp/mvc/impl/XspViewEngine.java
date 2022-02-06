package org.openntf.xsp.mvc.impl;

import java.io.IOException;
import java.net.URI;

import org.eclipse.krazo.engine.ServletViewEngine;
import org.openntf.xsp.jakartaee.servlet.ServletUtil;

import com.ibm.commons.util.PathUtil;
import com.ibm.designer.runtime.domino.adapter.ComponentModule;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.webapp.DesignerFacesServlet;

import jakarta.mvc.engine.ViewEngineContext;
import jakarta.mvc.engine.ViewEngineException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

public class XspViewEngine extends ServletViewEngine {

	@Override
	public boolean supports(String view) {
		return view.endsWith(".xsp"); //$NON-NLS-1$
	}

	@Override
	public void processView(ViewEngineContext context) throws ViewEngineException {
		System.out.println("handling xpage!");
        HttpServletRequest request = context.getRequest(HttpServletRequest.class);
		System.out.println("true request URI is " + request.getRequestURI());
		System.out.println("true request URL is " + request.getRequestURL());
		System.out.println("true path info is " + request.getPathInfo());
		System.out.println("true context is " + request.getContextPath());
		
		
		String xpage = resolveView(context);
		String xpagePath = PathUtil.concat(request.getContextPath(), xpage, '/');
		URI uri = URI.create(request.getRequestURL().toString());
		String xpageUrl = uri.resolve(xpagePath).toString();
		System.out.println("new path: " + xpagePath);
		System.out.println("new url: " + xpageUrl);
		
        final HttpServletResponse response = context.getResponse(HttpServletResponse.class);
        request = new HttpServletRequestWrapper(request) {
            @Override
            public String getRequestURI() {
            	System.out.println("asked for request URI");
            	new Throwable().printStackTrace();
            	return xpagePath;
            }

            @Override
            public String getServletPath() {
            	System.out.println("asked for servlet path, returning " + xpagePath);
            	return xpagePath;
            }

            @Override
            public String getPathInfo() {
            	System.out.println("asked for path info");
                return null;
            }

            @Override
            public StringBuffer getRequestURL() {
            	System.out.println("asked for request URL");
            	new Throwable().printStackTrace();
                return new StringBuffer(xpageUrl);
            }
            
            @Override
            public ServletRequest getRequest() {
            	System.out.println("asked for request!");
            	return super.getRequest();
            }
            
            @Override
            public String getPathTranslated() {
            	System.out.println("Getting path translated: " + super.getPathTranslated());
            	return super.getPathTranslated();
            }
            
            @Override
            public ServletContext getServletContext() {
            	return new XspViewEngineContext(super.getServletContext());
            }
        };
        
        javax.servlet.http.HttpServletRequest oldReq = ServletUtil.newToOld(request);
        javax.servlet.http.HttpServletResponse oldResp = ServletUtil.newToOld(response);
        
        ComponentModule module = NotesContext.getCurrent().getModule();
        try {
			DesignerFacesServlet delegate = (DesignerFacesServlet)module.getServlet(resolveView(context)).getServlet();
			delegate.service(oldReq, oldResp);
		} catch (javax.servlet.ServletException | IOException e) {
			throw new ViewEngineException(e);
		}
	}

	@Override
	protected String getViewFolder(ViewEngineContext context) {
		return ""; //$NON-NLS-1$
	}
}
