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

import javax.xml.namespace.QName;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.MethodType;
import net.opengis.wps10.Wps10Factory;
import org.geotools.wps.WPS;
import org.geotools.xsd.ComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding for inputReference attribute of Method element
 *
 * @author Lucas Reed, Refractions Research Inc
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
            attr.setValue(MethodType.get((String) attr.getValue()));
        }

        return super.parse(instance, node, value);
    }
}
