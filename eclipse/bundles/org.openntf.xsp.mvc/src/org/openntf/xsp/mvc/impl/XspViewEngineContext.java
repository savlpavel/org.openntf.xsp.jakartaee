package org.openntf.xsp.mvc.impl;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.FilterRegistration.Dynamic;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.SessionTrackingMode;
import jakarta.servlet.descriptor.JspConfigDescriptor;

public class XspViewEngineContext implements ServletContext {
	private final ServletContext delegate;
	
	public XspViewEngineContext(ServletContext delegate) {
		this.delegate = delegate;
	}

	public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
		return delegate.addFilter(arg0, arg1);
	}

	public Dynamic addFilter(String arg0, Filter arg1) {
		return delegate.addFilter(arg0, arg1);
	}

	public Dynamic addFilter(String arg0, String arg1) {
		return delegate.addFilter(arg0, arg1);
	}

	public jakarta.servlet.ServletRegistration.Dynamic addJspFile(String arg0, String arg1) {
		return delegate.addJspFile(arg0, arg1);
	}

	public void addListener(Class<? extends EventListener> arg0) {
		delegate.addListener(arg0);
	}

	public void addListener(String arg0) {
		delegate.addListener(arg0);
	}

	public <T extends EventListener> void addListener(T arg0) {
		delegate.addListener(arg0);
	}

	public jakarta.servlet.ServletRegistration.Dynamic addServlet(String arg0, Class<? extends Servlet> arg1) {
		return delegate.addServlet(arg0, arg1);
	}

	public jakarta.servlet.ServletRegistration.Dynamic addServlet(String arg0, Servlet arg1) {
		return delegate.addServlet(arg0, arg1);
	}

	public jakarta.servlet.ServletRegistration.Dynamic addServlet(String arg0, String arg1) {
		return delegate.addServlet(arg0, arg1);
	}

	public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
		return delegate.createFilter(arg0);
	}

	public <T extends EventListener> T createListener(Class<T> arg0) throws ServletException {
		return delegate.createListener(arg0);
	}

	public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
		return delegate.createServlet(arg0);
	}

	public void declareRoles(String... arg0) {
		delegate.declareRoles(arg0);
	}

	public Object getAttribute(String arg0) {
		System.out.println("get context attr: " + arg0 + " - " + delegate.getAttribute(arg0));
		return delegate.getAttribute(arg0);
	}

	public Enumeration<String> getAttributeNames() {
		return delegate.getAttributeNames();
	}

	public ClassLoader getClassLoader() {
		return delegate.getClassLoader();
	}

	public ServletContext getContext(String arg0) {
		return delegate.getContext(arg0);
	}

	public String getContextPath() {
		System.out.println("asked for context path! I see " + delegate.getContextPath());
		return delegate.getContextPath();
	}

	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return delegate.getDefaultSessionTrackingModes();
	}

	public int getEffectiveMajorVersion() {
		return delegate.getEffectiveMajorVersion();
	}

	public int getEffectiveMinorVersion() {
		return delegate.getEffectiveMinorVersion();
	}

	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return delegate.getEffectiveSessionTrackingModes();
	}

	public FilterRegistration getFilterRegistration(String arg0) {
		return delegate.getFilterRegistration(arg0);
	}

	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return delegate.getFilterRegistrations();
	}

	public String getInitParameter(String arg0) {
		return delegate.getInitParameter(arg0);
	}

	public Enumeration<String> getInitParameterNames() {
		return delegate.getInitParameterNames();
	}

	public JspConfigDescriptor getJspConfigDescriptor() {
		return delegate.getJspConfigDescriptor();
	}

	public int getMajorVersion() {
		return delegate.getMajorVersion();
	}

	public String getMimeType(String arg0) {
		return delegate.getMimeType(arg0);
	}

	public int getMinorVersion() {
		return delegate.getMinorVersion();
	}

	public RequestDispatcher getNamedDispatcher(String arg0) {
		return delegate.getNamedDispatcher(arg0);
	}

	public String getRealPath(String arg0) {
		System.out.println("asked for real path for " + arg0);
		return delegate.getRealPath(arg0);
	}

	public String getRequestCharacterEncoding() {
		return delegate.getRequestCharacterEncoding();
	}

	public RequestDispatcher getRequestDispatcher(String arg0) {
		return delegate.getRequestDispatcher(arg0);
	}

	public URL getResource(String arg0) throws MalformedURLException {
		return delegate.getResource(arg0);
	}

	public InputStream getResourceAsStream(String arg0) {
		return delegate.getResourceAsStream(arg0);
	}

	public Set<String> getResourcePaths(String arg0) {
		return delegate.getResourcePaths(arg0);
	}

	public String getResponseCharacterEncoding() {
		return delegate.getResponseCharacterEncoding();
	}

	public String getServerInfo() {
		return delegate.getServerInfo();
	}

	public Servlet getServlet(String arg0) throws ServletException {
		System.out.println("called getServlet " + arg0);
		return delegate.getServlet(arg0);
	}

	public String getServletContextName() {
		System.out.println("called getServletContextName");
		return delegate.getServletContextName();
	}

	public Enumeration<String> getServletNames() {
		return delegate.getServletNames();
	}

	public ServletRegistration getServletRegistration(String arg0) {
		return delegate.getServletRegistration(arg0);
	}

	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return delegate.getServletRegistrations();
	}

	public Enumeration<Servlet> getServlets() {
		return delegate.getServlets();
	}

	public SessionCookieConfig getSessionCookieConfig() {
		return delegate.getSessionCookieConfig();
	}

	public int getSessionTimeout() {
		return delegate.getSessionTimeout();
	}

	public String getVirtualServerName() {
		return delegate.getVirtualServerName();
	}

	public void log(Exception arg0, String arg1) {
		delegate.log(arg0, arg1);
	}

	public void log(String arg0, Throwable arg1) {
		delegate.log(arg0, arg1);
	}

	public void log(String arg0) {
		delegate.log(arg0);
	}

	public void removeAttribute(String arg0) {
		delegate.removeAttribute(arg0);
	}

	public void setAttribute(String arg0, Object arg1) {
		delegate.setAttribute(arg0, arg1);
	}

	public boolean setInitParameter(String arg0, String arg1) {
		return delegate.setInitParameter(arg0, arg1);
	}

	public void setRequestCharacterEncoding(String arg0) {
		delegate.setRequestCharacterEncoding(arg0);
	}

	public void setResponseCharacterEncoding(String arg0) {
		delegate.setResponseCharacterEncoding(arg0);
	}

	public void setSessionTimeout(int arg0) {
		delegate.setSessionTimeout(arg0);
	}

	public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) {
		delegate.setSessionTrackingModes(arg0);
	}

}
