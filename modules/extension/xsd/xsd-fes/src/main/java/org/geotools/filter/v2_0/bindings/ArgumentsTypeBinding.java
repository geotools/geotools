package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.ArgumentsType;
import net.opengis.fes20.Fes20Factory;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class ArgumentsTypeBinding extends AbstractComplexEMFBinding {

	public ArgumentsTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.ArgumentsType;
	}

	@Override
	public Class getType() {
		return ArgumentsType.class;
	}
	
}
