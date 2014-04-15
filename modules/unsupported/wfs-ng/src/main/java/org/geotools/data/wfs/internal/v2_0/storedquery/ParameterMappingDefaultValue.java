package org.geotools.data.wfs.internal.v2_0.storedquery;

public class ParameterMappingDefaultValue extends ParameterMapping {
	private final String defaultValue;
	
	public ParameterMappingDefaultValue(String parameterName, String defaultValue) {
		super(parameterName);
		this.defaultValue = defaultValue;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}
