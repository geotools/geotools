package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.LogicalOperatorsType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class _LogicalOperatorsBinding extends AbstractComplexEMFBinding {

	public _LogicalOperatorsBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES._LogicalOperators;
	}
	
	@Override
	public Class getType() {
		return LogicalOperatorsType.class;
	}

}
