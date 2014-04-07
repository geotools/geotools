package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.SpatialCapabilitiesType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;


public class Spatial_CapabilitiesTypeBinding extends AbstractComplexEMFBinding {
	
	public Spatial_CapabilitiesTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.Spatial_CapabilitiesType;
	}

	@Override
	public Class getType() {
		return SpatialCapabilitiesType.class;
	}
	
}
