package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.Operator;

public class ComparisonOperatorTypeBinding extends AbstractComplexBinding {

	private FilterFactory factory;
	
	public ComparisonOperatorTypeBinding(FilterFactory filterFactory) {
		factory = filterFactory;
	}
	
	@Override
	public QName getTarget() {
		return FES.ComparisonOperatorType;
	}

	@Override
	public Class getType() {
		return Operator.class;
	}
	
	@Override
	public Object parse(ElementInstance instance, Node node, Object value)
			throws Exception {
		String name = (String)node.getAttributeValue("name");
		if (name != null) {
			return factory.operator(name);
		}

		return super.parse(instance, node, value);
	}

}
