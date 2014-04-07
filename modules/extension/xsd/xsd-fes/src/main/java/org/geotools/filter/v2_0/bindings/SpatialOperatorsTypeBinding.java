package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.SpatialOperatorsType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class SpatialOperatorsTypeBinding extends AbstractComplexEMFBinding {

	public SpatialOperatorsTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.SpatialOperatorsType;
	}

	@Override
	public Class getType() {
		return SpatialOperatorsType.class;
	}
}
