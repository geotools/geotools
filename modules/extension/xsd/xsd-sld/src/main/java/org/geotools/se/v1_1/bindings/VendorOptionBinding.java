/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import javax.xml.namespace.QName;

import org.geotools.se.v1_1.SE;
import org.geotools.sld.bindings.SLDVendorOptionBinding;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the element http://www.opengis.net/se:VendorOption.
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
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class VendorOptionBinding extends SLDVendorOptionBinding {

    public VendorOptionBinding(FilterFactory filterFactory) {
        super(filterFactory);
    }
    
    @Override
    public QName getTarget() {
        return SE.VendorOption;
    }

}
