package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoredQueryConfiguration implements Serializable {
	
	private List<ParameterMapping> storedQueryParameterMappings = 
			new ArrayList<ParameterMapping>(); 
	
	public List<ParameterMapping> getStoredQueryParameterMappings() {
		return storedQueryParameterMappings;
	}
	
	public void setStoredQueryParameterMappings(
			List<ParameterMapping> storedQueryParameterMappings) {
		this.storedQueryParameterMappings = storedQueryParameterMappings;
	}
	
}
