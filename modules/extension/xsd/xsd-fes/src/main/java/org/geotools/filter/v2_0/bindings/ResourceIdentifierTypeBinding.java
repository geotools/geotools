package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

public class ResourceIdentifierTypeBinding extends AbstractComplexEMFBinding {

	@Override
	public QName getTarget() {
		return FES.ResourceIdentifierType;
	}
	
	@Override
	public Class getType() {
		return QName.class;
	}
	
	@Override
	public Object parse(ElementInstance instance, Node node, Object value)
			throws Exception {
		QName ret = null;
		Node nameAttribute = node.getAttribute("name");
		if (nameAttribute != null) {
			Object o = nameAttribute.getValue();
			if (o instanceof QName) {
				ret = (QName)o;
			}
		}
		return ret;
	}

}
