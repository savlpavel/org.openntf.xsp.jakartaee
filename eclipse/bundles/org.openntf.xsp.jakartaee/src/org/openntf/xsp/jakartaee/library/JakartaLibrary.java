package org.openntf.xsp.jakartaee.library;

import java.util.List;

import org.openntf.xsp.jakartaee.JakartaConstants;
import org.openntf.xsp.jakartaee.JakartaLibraryContributor;
import org.openntf.xsp.jakartaee.LibraryUtil;
import org.osgi.framework.FrameworkUtil;

import com.ibm.xsp.library.AbstractXspLibrary;

public class JakartaLibrary extends AbstractXspLibrary {
	@Override
	public String getLibraryId() {
		return JakartaConstants.LIBRARY_ID;
	}
	
	@Override
	public String getPluginId() {
		return FrameworkUtil.getBundle(getClass()).getSymbolicName();
	}

	@Override
	public String[] getXspConfigFiles() {
		return LibraryUtil.findExtensions(JakartaLibraryContributor.class)
			.stream()
			.map(JakartaLibraryContributor::getXspConfigFiles)
			.flatMap(List::stream)
			.toArray(String[]::new);
	}
	
	@Override
	public String[] getFacesConfigFiles() {
		return LibraryUtil.findExtensions(JakartaLibraryContributor.class)
			.stream()
			.map(JakartaLibraryContributor::getFacesConfigFiles)
			.flatMap(List::stream)
			.toArray(String[]::new);
	}
}
