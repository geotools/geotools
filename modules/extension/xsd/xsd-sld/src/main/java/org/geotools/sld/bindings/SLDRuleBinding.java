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
import org.geotools.styling.Rule;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.Filter;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding object for the element http://www.opengis.net/sld:Rule.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="Rule"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A Rule is used to attach
 *              property/scale conditions to and group         the
 *              individual symbolizers used for rendering.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Title" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Abstract" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:LegendGraphic" minOccurs="0"/&gt;
 *              &lt;xsd:choice minOccurs="0"&gt;
 *                  &lt;xsd:element ref="ogc:Filter"/&gt;
 *                  &lt;xsd:element ref="sld:ElseFilter"/&gt;
 *              &lt;/xsd:choice&gt;
 *              &lt;xsd:element ref="sld:MinScaleDenominator" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:MaxScaleDenominator" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Symbolizer" maxOccurs="unbounded"/&gt;
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
public class SLDRuleBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDRuleBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.RULE;
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
        return Rule.class;
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
        Rule rule = styleFactory.createRule();

        //&lt;xsd:element ref="sld:Name" minOccurs="0"/&gt;
        if (node.hasChild("Name")) {
            rule.setName((String) node.getChildValue("Name"));
        }

        //&lt;xsd:element ref="sld:Title" minOccurs="0"/&gt;
        if (node.hasChild("Title")) {
            rule.setTitle((String) node.getChildValue("Title"));
        }

        //&lt;xsd:element ref="sld:Abstract" minOccurs="0"/&gt;
        if (node.hasChild("Abstract")) {
            rule.setAbstract((String) node.getChildValue("Abstract"));
        }

        //&lt;xsd:element ref="sld:LegendGraphic" minOccurs="0"/&gt;
        if (node.hasChild("LegendGraphic")) {
            rule.setLegendGraphic(new Graphic[] { (Graphic) node.getChildValue("LegendGraphic") });
        }

        //&lt;xsd:choice minOccurs="0"&gt;
        //	 &lt;xsd:element ref="ogc:Filter"/&gt;
        //	 &lt;xsd:element ref="sld:ElseFilter"/&gt;
        //&lt;/xsd:choice&gt;
        if (node.hasChild(Filter.class)) {
            rule.setFilter((Filter) node.getChildValue(Filter.class));
        } else if (node.hasChild("ElseFilter")) {
            rule.setIsElseFilter(true);
        }

        //&lt;xsd:element ref="sld:MinScaleDenominator" minOccurs="0"/&gt;
        if (node.hasChild("MinScaleDenominator")) {
            rule.setMinScaleDenominator(((Double) node.getChildValue("MinScaleDenominator"))
                .doubleValue());
        }

        //&lt;xsd:element ref="sld:MaxScaleDenominator" minOccurs="0"/&gt;
        if (node.hasChild("MaxScaleDenominator")) {
            rule.setMaxScaleDenominator(((Double) node.getChildValue("MaxScaleDenominator"))
                .doubleValue());
        }

        //&lt;xsd:element ref="sld:Symbolizer" maxOccurs="unbounded"/&gt;
        List syms = node.getChildValues(Symbolizer.class);
        rule.setSymbolizers((Symbolizer[]) syms.toArray(new Symbolizer[syms.size()]));

        return rule;
    }
}
