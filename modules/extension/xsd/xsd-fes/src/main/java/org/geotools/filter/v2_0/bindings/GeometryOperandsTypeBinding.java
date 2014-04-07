package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.GeometryOperandsType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class GeometryOperandsTypeBinding extends AbstractComplexEMFBinding {

	public GeometryOperandsTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.GeometryOperandsType;
	}
	
	@Override
	public Class getType() {
		return GeometryOperandsType.class;
	}

}
