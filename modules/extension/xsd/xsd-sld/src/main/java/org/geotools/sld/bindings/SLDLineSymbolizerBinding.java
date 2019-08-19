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

import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.sld.CssParameter;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactory;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:LineSymbolizer.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="LineSymbolizer" substitutionGroup="sld:Symbolizer"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A LineSymbolizer is used to render a
 *              &quot;stroke&quot; along a linear geometry.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:complexContent&gt;
 *              &lt;xsd:extension base="sld:SymbolizerType"&gt;
 *                  &lt;xsd:sequence&gt;
 *                      &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
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
public class SLDLineSymbolizerBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDLineSymbolizerBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /** @generated */
    public QName getTarget() {
        return SLD.LINESYMBOLIZER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
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
    public Class getType() {
        return LineSymbolizer.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        LineSymbolizer ls = styleFactory.createLineSymbolizer();

        // &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
        if (node.hasChild("Geometry")) {
            Expression geometry = (Expression) node.getChildValue("Geometry");
            if (geometry instanceof PropertyName) {
                PropertyName propertyName = (PropertyName) geometry;
                ls.setGeometryPropertyName(propertyName.getPropertyName());
            } else {
                ls.setGeometry(geometry);
            }
        }

        // &lt;xsd:element ref="sld:Stroke" minOccurs="0"/&gt;
        if (node.hasChild(Stroke.class)) {
            ls.setStroke((Stroke) node.getChildValue(Stroke.class));
        }

        // &lt;xsd:element ref="sld:VendorOption" minOccurs="0" maxOccurs="unbounded"/&gt;
        for (CssParameter param : (List<CssParameter>) node.getChildValues(CssParameter.class)) {
            ls.getOptions()
                    .put(param.getName(), param.getExpression().evaluate(null, String.class));
        }

        return ls;
    }
}
