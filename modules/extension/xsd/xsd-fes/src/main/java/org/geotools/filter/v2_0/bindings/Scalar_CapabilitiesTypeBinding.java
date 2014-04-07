package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.ScalarCapabilitiesType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class Scalar_CapabilitiesTypeBinding extends AbstractComplexEMFBinding {
	
	public Scalar_CapabilitiesTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.Scalar_CapabilitiesType;
	}
	
	@Override
	public Class getType() {
		return ScalarCapabilitiesType.class;
	}

}
