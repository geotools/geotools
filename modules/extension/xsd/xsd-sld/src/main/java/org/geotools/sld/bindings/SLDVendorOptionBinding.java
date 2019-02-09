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
package org.geotools.sld.bindings;

import javax.xml.namespace.QName;
import org.geotools.sld.CssParameter;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the element http://www.opengis.net/sld:VendorOption.
 *
 * <pre>
 * &lt;xsd:element name="VendorOption">
 *   &lt;xsd:annotation>
 *     &lt;xsd:documentation>
 *     GeoTools specific vendor extensions that allow for implementation
 *     specific features not necessarily supported by the core SLD spec.
 *     &lt;/xsd:documentation>
 *   &lt;/xsd:annotation>
 *   &lt;xsd:complexType mixed="true">
 *     &lt;xsd:simpleContent>
 *         &lt;xsd:extension base="xsd:string">
 *            &lt;xsd:attribute name="name" type="xsd:string" />
 *         &lt;/xsd:extension>
 *     &lt;/xsd:simpleContent>
 *   &lt;/xsd:complexType>
 * &lt;/xsd:element>
 * </pre>
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class SLDVendorOptionBinding extends AbstractComplexBinding {

    FilterFactory filterFactory;

    public SLDVendorOptionBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    public QName getTarget() {
        return SLD.VENDOROPTION;
    }

    public Class getType() {
        return CssParameter.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        CssParameter option = new CssParameter((String) node.getAttributeValue("name"));
        option.setExpression(filterFactory.literal(instance.getText()));
        return option;
    }
}
