package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.AvailableFunctionType;
import net.opengis.fes20.Fes20Factory;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class AvailableFunctionTypeBinding extends AbstractComplexEMFBinding {
	
	public AvailableFunctionTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.AvailableFunctionType;
	}
	
	@Override
	public Class getType() {
		return AvailableFunctionType.class;
	}
}
