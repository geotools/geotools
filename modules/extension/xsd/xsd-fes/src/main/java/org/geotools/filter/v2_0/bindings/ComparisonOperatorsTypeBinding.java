package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.ComparisonOperatorsType;
import net.opengis.fes20.Fes20Factory;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;


public class ComparisonOperatorsTypeBinding extends AbstractComplexEMFBinding {

	public ComparisonOperatorsTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.ComparisonOperatorsType;
	}

	@Override
	public Class getType() {
		return ComparisonOperatorsType.class;
	}
	
}
