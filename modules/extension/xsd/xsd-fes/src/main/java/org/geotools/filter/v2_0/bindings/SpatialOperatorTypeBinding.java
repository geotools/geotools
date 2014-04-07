package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.SpatialOperatorType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class SpatialOperatorTypeBinding extends AbstractComplexEMFBinding {

	public SpatialOperatorTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.SpatialOperatorType;
	}
	
	@Override
	public Class getType() {
		return SpatialOperatorType.class;
	}

}
