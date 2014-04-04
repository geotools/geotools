package org.geotools.filter.v2_0.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.Operator;

public class ComparisonOperatorsTypeBinding extends AbstractComplexBinding {

	private FilterFactory factory;
	
	public ComparisonOperatorsTypeBinding(FilterFactory filterFactory) {
		factory = filterFactory;
	}
	
	@Override
	public QName getTarget() {
		return FES.ComparisonOperatorsType;
	}

	@Override
	public Class getType() {
		return ComparisonOperators.class;
	}
	
	@Override
	public Object parse(ElementInstance instance, Node node, Object value)
			throws Exception {
		List operators = node.getChildValues(Operator.class);
		Operator [] operatorArray = new Operator[operators.size()];
		for (int i = 0; i < operators.size(); i++) {
			operatorArray[i] = (Operator)operators.get(i);
		}
		return factory.comparisonOperators(operatorArray);
	}

}
