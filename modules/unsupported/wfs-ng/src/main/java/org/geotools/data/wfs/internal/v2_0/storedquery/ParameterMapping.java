package org.geotools.data.wfs.internal.v2_0.storedquery;

public abstract class ParameterMapping {
	private final String parameterName;
	
	protected ParameterMapping(String parameterName) {
		this.parameterName = parameterName;
	}
	
	public String getParameterName() {
		return parameterName;
	}
	
}
