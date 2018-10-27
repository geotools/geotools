/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;
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
    public Element encode(Object object, Document document, Element value) throws Exception {
        Element element = super.encode(object, document, value);

        Object parameterValue = ((ParameterType) object).getValue();
        if (parameterValue instanceof String) {
            element.setTextContent((String) parameterValue);
        }

        return element;
    }
}
