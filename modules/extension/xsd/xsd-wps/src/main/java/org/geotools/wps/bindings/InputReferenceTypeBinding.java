/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.wps.bindings;

import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.MethodType;
import net.opengis.wps10.Wps10Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wps.WPS;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.EMFUtils;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Encoder;
import org.geotools.xml.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Binding for inputReference attribute of Method element
 * @author Lucas Reed, Refractions Research Inc
 *
 *
 *
 * @source $URL$
 */
public class InputReferenceTypeBinding extends ComplexEMFBinding {
    public InputReferenceTypeBinding(Wps10Factory factory) {
        super(factory, WPS.InputReferenceType);
    }

    @Override
    public QName getTarget() {
        return WPS.InputReferenceType;
    }

    @Override
    public Class<?> getType() {
        return InputReferenceType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Node attr = node.getAttribute("method");

        if (null != attr) {
            attr.setValue(MethodType.get((String)attr.getValue()));
        }

        return super.parse(instance, node, value);
    }

	@Override
	public Element encode(Object object, Document document, Element value)
			throws Exception {
		EObject eobject = (EObject) object;
        if (EMFUtils.has(eobject, "body")) {
        	Object v = EMFUtils.get(((EObject) object), "body");
        	if (v instanceof ExecuteType) {
                StringWriter output = new StringWriter();
        		Encoder e = new Encoder(new WPSConfiguration());
                e.setIndenting(true);
                e.setOmitXMLDeclaration(true);
                e.setNamespaceAware(true);
                TransformerHandler serializer = ((SAXTransformerFactory)SAXTransformerFactory.newInstance()).newTransformerHandler();
                serializer.setResult(new StreamResult(output));
                e.encode(v, WPS.Execute, serializer);
                
                //add the body manually
                Element body = document.createElementNS( WPS.NAMESPACE, "Body");
                body.setTextContent(WPS.cleanUpHeader(output.toString()));
                value.appendChild( body );
        	}
        }
        
        return value;
	}
    
}
