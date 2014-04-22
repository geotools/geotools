package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.data.wfs.internal.v2_0.FeatureTypeInfoImpl;
import org.opengis.filter.Filter;

public class StoredQueryConfiguration {
	
	private final String storedQueryId;
	
	private List<ParameterMapping> storedQueryParameterMappings; 
	
	public StoredQueryConfiguration(String storedQueryId) {
		this.storedQueryId = storedQueryId;
		this.storedQueryParameterMappings = new ArrayList<ParameterMapping>();
	}
	
	public String getStoredQueryId() {
		return storedQueryId;
	}
	
	public List<ParameterMapping> getStoredQueryParameterMappings() {
		return storedQueryParameterMappings;
	}
	

	public ParameterMapping getStoredQueryParameterMapping(
			String parameterName) {
		ParameterMapping ret = null;
		for (ParameterMapping sqpm : storedQueryParameterMappings) {
			if (sqpm.getParameterName().equals(parameterName)) {
				ret = sqpm;
				break;
			}
		}

		return ret;
	}
	
	public void setStoredQueryParameterMappings(
			List<ParameterMapping> storedQueryParameterMappings) {
		this.storedQueryParameterMappings = storedQueryParameterMappings;
	}
	
	public List<ParameterType> createStoredQueryParameters(Filter filter, 
    		Map<String, String> viewParams, StoredQueryDescriptionType desc,
    		FeatureTypeInfo featureTypeInfo) {
    	final Wfs20Factory factory = Wfs20Factory.eINSTANCE;
    	
    	ParameterMappingContext mappingContext = new ParameterMappingContext(filter, viewParams,
    			featureTypeInfo);
    	
    	List<ParameterType> ret = new ArrayList<ParameterType>();
    	
    	for (ParameterExpressionType parameter : desc.getParameter()) {
    		String value = null;
    		
    		ParameterMapping mapping = this.getStoredQueryParameterMapping(
					parameter.getName());
			
    		// Primarily use the one provided by the user
    		if (value == null && viewParams != null) {
    			value = viewParams.get(parameter.getName());
    		}
    		
    		if (value == null && mapping != null) { 
    			if (mapping instanceof ParameterMappingDefaultValue) {
    				value = ((ParameterMappingDefaultValue)mapping).getDefaultValue();
    			} else if (mapping instanceof ParameterMappingExpressionValue) {
    				value = ((ParameterMappingExpressionValue)mapping).evaluate(mappingContext);
    			} else {
    				throw new IllegalArgumentException("Unknown StoredQueryParameterMapping: "
    						+ mapping.getClass());
    			}
    		}
    		
    		// If there is a value, add a parameter to the query
    		if (value != null) {
    			ParameterType tmp = factory.createParameterType();
    			tmp.setName(parameter.getName());
    			tmp.setValue(value);
    			ret.add(tmp);
    		}
    	}
    	
    	return ret;
	}
	
	public static StoredQueryConfiguration createMock(String id) {
		StoredQueryConfiguration ret = new StoredQueryConfiguration(id);

		ret.getStoredQueryParameterMappings().add(
				new ParameterMappingExpressionValue("bbox", "CQL", 
						"strConCat("
						+ "numberFormat('0.00000000000', bboxMinY), strConCat("
						+ "',', strConCat("
						+ "numberFormat('0.00000000000', bboxMinX), strConCat("
						+ "',', strConCat("
						+ "numberFormat('0.00000000000', bboxMaxY), strConCat("
						+ "',', strConCat("
						+ "numberFormat('0.00000000000', bboxMaxX), ',EPSG:4258')))))))" 
					));

		ret.getStoredQueryParameterMappings().add(
				new ParameterMappingDefaultValue("timestep", "720"));

		return ret;
	}
}
