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
import org.geotools.styling.Graphic;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbol;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.expression.Expression;
import org.opengis.style.AnchorPoint;
import org.opengis.style.Displacement;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:Graphic.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="Graphic"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;Graphic&quot; specifies or
 *              refers to a &quot;graphic symbol&quot; with inherent
 *              shape, size, and coloring.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:choice minOccurs="0" maxOccurs="unbounded"&gt;
 *                  &lt;xsd:element ref="sld:ExternalGraphic"/&gt;
 *                  &lt;xsd:element ref="sld:Mark"/&gt;
 *              &lt;/xsd:choice&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="sld:Opacity" minOccurs="0"/&gt;
 *                  &lt;xsd:element ref="sld:Size" minOccurs="0"/&gt;
 *                  &lt;xsd:element ref="sld:Rotation" minOccurs="0"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SLDGraphicBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDGraphicBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /** @generated */
    public QName getTarget() {
        return SLD.GRAPHIC;
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
        return Graphic.class;
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

        List<Symbol> symbols = node.getChildValues(Symbol.class);

        Expression opacity = (Expression) node.getChildValue("Opacity");
        Expression size = (Expression) node.getChildValue("Size");
        Expression rotation = (Expression) node.getChildValue("Rotation");

        Graphic graphic =
                styleFactory.createGraphic(
                        null,
                        null,
                        (Symbol[]) symbols.toArray(new Symbol[symbols.size()]),
                        opacity,
                        size,
                        rotation);

        if (node.getChild("Displacement") != null) {
            graphic.setDisplacement((Displacement) node.getChildValue("Displacement"));
        }
        if (node.getChild("AnchorPoint") != null) {
            graphic.setAnchorPoint((AnchorPoint) node.getChildValue("AnchorPoint"));
        }

        return graphic;
    }
}
