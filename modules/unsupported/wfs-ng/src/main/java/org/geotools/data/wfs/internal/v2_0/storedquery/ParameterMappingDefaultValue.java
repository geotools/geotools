package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.io.Serializable;

public class ParameterMappingDefaultValue implements ParameterMapping, Serializable {
	private String parameterName;
	private String defaultValue;
	
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	@Override
	public String getParameterName() {
		return parameterName;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}
