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
package org.geotools.sld.bindings;

import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.namespace.QName;
import org.opengis.filter.expression.PropertyName;
import org.geotools.xml.*;


/**
 * Binding object for the element http://www.opengis.net/sld:Geometry.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="Geometry"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A Geometry gives reference to a
 *              (the) geometry property of a         feature to be used for
 *              rendering.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="ogc:PropertyName"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class SLDGeometryBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.GEOMETRY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return PropertyName.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return node.getChildValue(PropertyName.class);
    }
}
