package org.geotools.data.wfs.internal.v2_0.storedquery;

import java.util.Collections;
import java.util.Map;

import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;


public class ParameterMappingContext {
	// Set in constructor
	private final Filter filter;
	private final Map<String, String> viewParams;
	private final FeatureTypeInfo featureTypeInfo;
	
	// Cached
	private Envelope bbox;
	
	public ParameterMappingContext(Filter filter, Map<String, String> viewParams, 
			FeatureTypeInfo featureTypeInfo) {
		this.filter = filter;
		if (viewParams == null) {
			viewParams = Collections.emptyMap();
		}
		this.viewParams = viewParams;
		this.featureTypeInfo = featureTypeInfo;
	}
	
	public Filter getFilter() {
		return filter;
	}
	
	public Map<String, String> getViewParams() {
		return viewParams;
	}
	
	public FeatureTypeInfo getFeatureTypeInfo() {
		return featureTypeInfo;
	}

	public Envelope getBBOX() {
		if (bbox != null) return bbox;
		
	    bbox = new ReferencedEnvelope();
	    bbox = (Envelope) filter.accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);

	    return bbox;
	}
}
