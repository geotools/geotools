package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.FilterCapabilitiesType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class Filter_CapabilitiesTypeBinding extends AbstractComplexEMFBinding {
	
    public Filter_CapabilitiesTypeBinding(Fes20Factory factory) {
        super(factory);
    }
    
	@Override
	public QName getTarget() {
		return FES.Filter_Capabilities;
	}
	
	@Override
	public Class getType() {
		return FilterCapabilitiesType.class;
	}
	
}
