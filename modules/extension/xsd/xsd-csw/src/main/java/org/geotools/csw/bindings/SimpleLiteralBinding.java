/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.csw.bindings;

import java.net.URI;
import javax.xml.namespace.QName;
import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.SimpleLiteral;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.geotools.xsd.SimpleContentComplexEMFBinding;

public class SimpleLiteralBinding extends SimpleContentComplexEMFBinding {

    public SimpleLiteralBinding(QName target) {
        super(Csw20Factory.eINSTANCE, target);
    }

    @Override
    public Class getType() {
        return SimpleLiteral.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        SimpleLiteral sl = Csw20Factory.eINSTANCE.createSimpleLiteral();
        sl.setName(instance.getName());
        sl.setValue(value);
        Node scheme = node.getAttribute("scheme");
        if (scheme != null) {
            sl.setScheme((URI) scheme.getValue());
        }

        return sl;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        Object result = super.getProperty(object, name);
        return result;
    }
}
