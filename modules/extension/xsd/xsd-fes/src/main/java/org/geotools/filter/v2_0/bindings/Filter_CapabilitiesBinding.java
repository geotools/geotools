package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.IdCapabilities;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;

public class Filter_CapabilitiesBinding extends AbstractComplexEMFBinding {
	
	private FilterFactory factory;
	
    public Filter_CapabilitiesBinding(FilterFactory filterFactory) {
        factory = filterFactory;
    }
    
	@Override
	public QName getTarget() {
		return FES.Filter_Capabilities;
	}
	
	@Override
	public Class getType() {
		return FilterCapabilities.class;
	}

	@Override
	public Object parse(ElementInstance instance, Node node, Object value)
			throws Exception {
		ScalarCapabilities scalarCapabilities = null;
		SpatialCapabilities spatialCapabilities = null;
		IdCapabilities idCapabilities = null;
		
		if (node.hasChild(ScalarCapabilities.class)) {
			scalarCapabilities = (ScalarCapabilities) node.getChildValue(ScalarCapabilities.class);
		}
		
		if (node.hasChild(SpatialCapabilities.class)) {
			spatialCapabilities = (SpatialCapabilities) node.getChildValue(SpatialCapabilities.class);
		}
		
		if (node.hasChild(IdCapabilities.class)) {
			idCapabilities = (IdCapabilities) node.getChildValue(IdCapabilities.class);
		}
		
		return factory.capabilities("2.0", scalarCapabilities, spatialCapabilities, idCapabilities);
	}
	
}
