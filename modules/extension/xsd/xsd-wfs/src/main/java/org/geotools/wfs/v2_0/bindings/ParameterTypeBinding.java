package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ParameterTypeBinding extends AbstractComplexEMFBinding {

	public ParameterTypeBinding(Wfs20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return WFS.ParameterType;
	}
	
	@Override
	public Class getType() {
		return ParameterType.class;
	}

	@Override
	public Element encode(Object object, Document document, Element value)
			throws Exception {
		Element element = super.encode(object, document, value);
		
		Object parameterValue = ((ParameterType)object).getValue();
		if (parameterValue instanceof String) {
			element.setTextContent((String)parameterValue);
		}
		
		return element;
	}
	
}
