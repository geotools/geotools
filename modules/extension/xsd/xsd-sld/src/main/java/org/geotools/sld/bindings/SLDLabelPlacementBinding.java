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
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;


/**
 * Binding object for the element http://www.opengis.net/sld:LabelPlacement.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="LabelPlacement"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         The &quot;LabelPlacement&quot;
 *              specifies where and how a text label should         be
 *              rendered relative to a geometry.  The present mechanism is
 *              poorly aligned with CSS/SVG.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:choice&gt;
 *              &lt;xsd:element ref="sld:PointPlacement"/&gt;
 *              &lt;xsd:element ref="sld:LinePlacement"/&gt;
 *          &lt;/xsd:choice&gt;
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
public class SLDLabelPlacementBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDLabelPlacementBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.LABELPLACEMENT;
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
        return LabelPlacement.class;
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
        //&lt;xsd:choice&gt;
        //      &lt;xsd:element ref="sld:PointPlacement"/&gt;
        //      &lt;xsd:element ref="sld:LinePlacement"/&gt;
        //&lt;/xsd:choice&gt;
        return node.getChildValue(LabelPlacement.class);
    }
}
