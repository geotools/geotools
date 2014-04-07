package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.ArgumentType;
import net.opengis.fes20.Fes20Factory;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class ArgumentTypeBinding extends AbstractComplexEMFBinding {

	public ArgumentTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.ArgumentType;
	}
	
	@Override
	public Class getType() {
		return ArgumentType.class;
	}

}
