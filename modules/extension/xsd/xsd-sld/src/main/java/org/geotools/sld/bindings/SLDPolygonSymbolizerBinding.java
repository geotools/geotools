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

import javax.xml.namespace.QName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.sld.CssParameter;
import org.geotools.styling.FillImpl;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.StyleFactory;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:PolygonSymbolizer.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="PolygonSymbolizer" substitutionGroup="sld:Symbolizer"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;PolygonSymbolizer&quot;
 *              specifies the rendering of a polygon or         area
 *              geometry, including its interior fill and border stroke.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:complexContent&gt;
 *              &lt;xsd:extension base="sld:SymbolizerType"&gt;
 *                  &lt;xsd:sequence&gt;
 *                      &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Fill" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Stroke" minOccurs="0"/&gt;
 *                  &lt;/xsd:sequence&gt;
 *              &lt;/xsd:extension&gt;
 *          &lt;/xsd:complexContent&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SLDPolygonSymbolizerBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDPolygonSymbolizerBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return SLD.POLYGONSYMBOLIZER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return PolygonSymbolizerImpl.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        PolygonSymbolizerImpl ps = styleFactory.createPolygonSymbolizer();

        // &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
        if (node.hasChild("Geometry")) {
            Expression geometry = (Expression) node.getChildValue("Geometry");
            if (geometry instanceof PropertyName) {
                PropertyName propertyName = (PropertyName) geometry;
                ps.setGeometryPropertyName(propertyName.getPropertyName());
            } else {
                ps.setGeometry(geometry);
            }
        }

        // &lt;xsd:element ref="sld:Fill" minOccurs="0"/&gt;
        if (node.hasChild(FillImpl.class)) {
            ps.setFill(node.getChildValue(FillImpl.class));
        }

        // &lt;xsd:element ref="sld:Stroke" minOccurs="0"/&gt;
        if (node.hasChild(StrokeImpl.class)) {
            ps.setStroke(node.getChildValue(StrokeImpl.class));
        }

        // &lt;xsd:element ref="sld:VendorOption" minOccurs="0" maxOccurs="unbounded"/&gt;
        for (CssParameter param : node.getChildValues(CssParameter.class)) {
            ps.getOptions()
                    .put(param.getName(), param.getExpression().evaluate(null, String.class));
        }

        return ps;
    }
}
