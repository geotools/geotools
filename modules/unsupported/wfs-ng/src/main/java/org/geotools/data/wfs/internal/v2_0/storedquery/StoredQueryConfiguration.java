package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoredQueryConfiguration implements Serializable {
	
	private String storedQueryId;
	
	private List<ParameterMapping> storedQueryParameterMappings = 
			new ArrayList<ParameterMapping>(); 
	
	public void setStoredQueryId(String storedQueryId) {
		this.storedQueryId = storedQueryId;
	}
	
	public String getStoredQueryId() {
		return storedQueryId;
	}
	
	public List<ParameterMapping> getStoredQueryParameterMappings() {
		return storedQueryParameterMappings;
	}
	
	public void setStoredQueryParameterMappings(
			List<ParameterMapping> storedQueryParameterMappings) {
		this.storedQueryParameterMappings = storedQueryParameterMappings;
	}
	
}
