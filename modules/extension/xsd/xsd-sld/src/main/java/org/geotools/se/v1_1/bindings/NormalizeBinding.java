/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.sld.bindings.SLDNormalizeBinding;
import org.geotools.styling.StyleFactory;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the element http://www.opengis.net/se:Normalize.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Normalize" type="se:NormalizeType"/&gt;
 *
 *   </code>
 * </pre>
 *
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="NormalizeType"/&gt;
 *
 *        </code>
 * </pre>
 *
 * @generated
 */
public class NormalizeBinding extends SLDNormalizeBinding {

    /** */
    public NormalizeBinding(StyleFactory styleFactory, FilterFactory filterFactory) {
        super(styleFactory, filterFactory);
        // TODO Auto-generated constructor stub
    }

    /** @generated */
    public QName getTarget() {
        return SE.Normalize;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
