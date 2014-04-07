package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.GeometryOperandType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class GeometryOperandsType_GeometryOperandBinding extends AbstractComplexEMFBinding {

	public GeometryOperandsType_GeometryOperandBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.GeometryOperandsType_GeometryOperand;
	}
	
	@Override
	public Class getType() {
		return GeometryOperandType.class;
	}

}
