package org.openntf.xsp.jakartaee;

import java.util.Collections;
import java.util.List;

public interface JakartaLibraryContributor {
	default List<String> getXspConfigFiles() {
		return Collections.emptyList();
	}
	
	default List<String> getFacesConfigFiles() {
		return Collections.emptyList();
	}
}
