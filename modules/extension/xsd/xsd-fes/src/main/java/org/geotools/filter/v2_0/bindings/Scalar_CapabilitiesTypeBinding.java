package org.geotools.filter.v2_0.bindings;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.Operator;
import org.opengis.filter.capability.ScalarCapabilities;

public class Scalar_CapabilitiesTypeBinding extends AbstractComplexBinding {
	
	private FilterFactory factory;
	
	public Scalar_CapabilitiesTypeBinding(FilterFactory filterFactory) {
		factory = filterFactory;
	}
	
	@Override
	public QName getTarget() {
		return FES.Scalar_CapabilitiesType;
	}
	
	@Override
	public Class getType() {
		return ScalarCapabilities.class;
	}

	@Override
	public Object parse(ElementInstance instance, Node node, Object value)
			throws Exception {
		boolean logical = node.hasChild("LogicalOperators");
	
		ComparisonOperators comparison = (ComparisonOperators) node.getChildValue(ComparisonOperators.class);
		
		// This is outside ScalarCapabilities in FES 2.0
		ArithmeticOperators arithmetic = null; 
		
		return factory.scalarCapabilities(comparison, arithmetic, logical);
	}

}
