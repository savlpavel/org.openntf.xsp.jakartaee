package org.openntf.xsp.cdi.jigsaw;

import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.weld.exceptions.UnsupportedOperationException;

import com.ibm.domino.xsp.module.nsf.ModuleClassLoader;

import jakarta.annotation.Priority;

/**
 * This subclass of {@link WeldDefaultProxyServices} keeps an internal cache of generated
 * classes. This is to avoid {@code java.lang.LinkageError: A duplicate class definition for jakarta/enterprise/event/Event$WeldEvent$Proxy$_$$_Weld$Proxy$ is found}
 * 
 * @author Jesse Gallagher
 * @since 2.11.0
 */
@Priority(1)
public class JigsawNSFProxyServices extends WeldDefaultProxyServices {
	
	private static Map<String, Class<?>> classCache = Collections.synchronizedMap(new HashMap<>());
	
	@Override
	public Class<?> defineClass(Class<?> originalClass, String className, byte[] classBytes, int off, int len)
			throws ClassFormatError {
		Class<?> result = super.defineClass(originalClass, className, classBytes, off, len);
		if(result != null && !(originalClass.getClassLoader() instanceof ModuleClassLoader)) {
			classCache.put(className + originalClass.hashCode(), result);
		}
		return result;
	}
	
	@Override
	public Class<?> defineClass(Class<?> originalClass, String className, byte[] classBytes, int off, int len,
			ProtectionDomain protectionDomain) throws ClassFormatError {
		Class<?> result = super.defineClass(originalClass, className, classBytes, off, len, protectionDomain);
		if(result != null && !(originalClass.getClassLoader() instanceof ModuleClassLoader)) {
			classCache.put(className + originalClass.hashCode(), result);
		}
		return result;
	}
	
	@Override
	public synchronized Class<?> loadClass(Class<?> originalClass, String classBinaryName) throws ClassNotFoundException {
		String key = classBinaryName + originalClass.hashCode();
		if(classCache.containsKey(key)) {
			return classCache.get(key);
		}
		return super.loadClass(originalClass, classBinaryName);
	}
	
	@Override
	public boolean supportsClassDefining() {
		return true;
	}

	public ClassLoader getClassLoader(final Class<?> proxiedBeanType) {
        throw new UnsupportedOperationException("WeldDefaultProxyServices.getClassLoader(Class<?>) is not implemented.");
    }

    public Class<?> loadBeanClass(final String className) {
        throw new UnsupportedOperationException("WeldDefaultProxyServices.loadBeanClass(String) is not implemented.");
    }
}