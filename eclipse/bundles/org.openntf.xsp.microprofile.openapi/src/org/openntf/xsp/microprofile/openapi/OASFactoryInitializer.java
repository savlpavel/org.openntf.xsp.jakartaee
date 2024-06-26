package org.openntf.xsp.microprofile.openapi;

import org.eclipse.microprofile.openapi.spi.OASFactoryResolver;
import org.openntf.xsp.jaxrs.spi.JAXRSActivationParticipant;
import org.osgi.framework.BundleContext;

import io.smallrye.openapi.spi.OASFactoryResolverImpl;

/**
 * @since 2.16.0
 */
public class OASFactoryInitializer implements JAXRSActivationParticipant {

	@Override
	public void start(BundleContext context) throws Exception {
		OASFactoryResolver.setInstance(new OASFactoryResolverImpl());
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
