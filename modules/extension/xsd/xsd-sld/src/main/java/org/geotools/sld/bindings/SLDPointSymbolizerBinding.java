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

import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.geotools.styling.Graphic;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.*;


/**
 * Binding object for the element http://www.opengis.net/sld:PointSymbolizer.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="PointSymbolizer" substitutionGroup="sld:Symbolizer"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;PointSymbolizer&quot;
 *              specifies the rendering of a &quot;graphic symbol&quot;
 *              at a point.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:complexContent&gt;
 *              &lt;xsd:extension base="sld:SymbolizerType"&gt;
 *                  &lt;xsd:sequence&gt;
 *                      &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Graphic" minOccurs="0"/&gt;
 *                  &lt;/xsd:sequence&gt;
 *              &lt;/xsd:extension&gt;
 *          &lt;/xsd:complexContent&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class SLDPointSymbolizerBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDPointSymbolizerBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.POINTSYMBOLIZER;
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
        return PointSymbolizer.class;
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
        PointSymbolizer ps = styleFactory.createPointSymbolizer();

        //&lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
        if(node.hasChild("Geometry")) {
            Expression geometry = (Expression) node.getChildValue("Geometry");
            if(geometry instanceof PropertyName) {
                PropertyName propertyName = (PropertyName) geometry;
                ps.setGeometryPropertyName(propertyName.getPropertyName());
            } else {
                ps.setGeometry(geometry);
            }
        }

        //&lt;xsd:element ref="sld:Graphic" minOccurs="0"/&gt;
        if (node.hasChild("Graphic")) {
            ps.setGraphic((Graphic) node.getChildValue("Graphic"));
        }

        return ps;
    }
}
