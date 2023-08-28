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
import org.geotools.api.style.Fill;
import org.geotools.api.style.Font;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Halo;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.sld.CssParameter;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:TextSymbolizer.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="TextSymbolizer" substitutionGroup="sld:Symbolizer"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;TextSymbolizer&quot; is used
 *              to render text labels according to         various graphical
 *              parameters.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:complexContent&gt;
 *              &lt;xsd:extension base="sld:SymbolizerType"&gt;
 *                  &lt;xsd:sequence&gt;
 *                      &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Label" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Font" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:LabelPlacement" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Halo" minOccurs="0"/&gt;
 *                      &lt;xsd:element ref="sld:Fill" minOccurs="0"/&gt;
 *
 *                      &lt;!-- geotools specific vendor option --&gt;
 *                      &lt;xsd:element ref="sld:VendorOption" minOccurs="0" maxOccurs="unbounded"/&gt;
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
public class SLDTextSymbolizerBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDTextSymbolizerBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return SLD.TEXTSYMBOLIZER;
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
        return TextSymbolizer.class;
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
        TextSymbolizer ts = styleFactory.createTextSymbolizer();

        // &lt;xsd:element ref="sld:Geometry" minOccurs="0"/&gt;
        if (node.hasChild("Geometry")) {
            Expression geometry = (Expression) node.getChildValue("Geometry");
            if (geometry instanceof PropertyName) {
                PropertyName propertyName = (PropertyName) geometry;
                ts.setGeometryPropertyName(propertyName.getPropertyName());
            } else {
                ts.setGeometry(geometry);
            }
        }

        // &lt;xsd:element ref="sld:Label" minOccurs="0"/&gt;
        if (node.hasChild("Label")) {
            ts.setLabel((Expression) node.getChildValue("Label"));
        }

        // &lt;xsd:element ref="sld:Font" minOccurs="0"/&gt;
        if (node.hasChild("Font")) {
            ts.fonts().add((Font) node.getChildValue("Font"));
        }

        // &lt;xsd:element ref="sld:LabelPlacement" minOccurs="0"/&gt;
        if (node.hasChild("LabelPlacement")) {
            ts.setLabelPlacement((LabelPlacement) node.getChildValue("LabelPlacement"));
        }

        // &lt;xsd:element ref="sld:Halo" minOccurs="0"/&gt;
        if (node.hasChild("Halo")) {
            ts.setHalo((Halo) node.getChildValue("Halo"));
        }

        // &lt;xsd:element ref="sld:Fill" minOccurs="0"/&gt;
        if (node.hasChild("Fill")) {
            ts.setFill((Fill) node.getChildValue("Fill"));
        }

        if (node.hasChild("Graphic")) {
            ts.setGraphic((Graphic) node.getChildValue("Graphic"));
        }

        if (node.hasChild("Priority")) {
            ts.setPriority((Expression) node.getChildValue("Priority"));
        }

        // &lt;xsd:element ref="sld:VendorOption" minOccurs="0" maxOccurs="unbounded"/&gt;
        for (CssParameter param : node.getChildValues(CssParameter.class)) {
            ts.getOptions()
                    .put(param.getName(), param.getExpression().evaluate(null, String.class));
        }
        return ts;
    }
}
