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

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.geotools.sld.CssParameter;
import org.geotools.styling.Font;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.expression.Expression;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding object for the element http://www.opengis.net/sld:Font.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="Font"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;Font&quot; element specifies
 *              the text font to use.  The allowed         CssParameters
 *              are: &quot;font-family&quot;, &quot;font-style&quot;,
 *              &quot;font-weight&quot;,         and &quot;font-size&quot;.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:CssParameter" minOccurs="0" maxOccurs="unbounded"/&gt;
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
public class SLDFontBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;

    public SLDFontBinding(StyleFactory styleFactory) {
        this.styleFactory = styleFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.FONT;
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
        return Font.class;
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
        //&quot;font-family&quot;
        //&quot;font-style&quot;
        //&quot;font-weight&quot;
        //&quot;font-size&quot
        Expression family = null;

        //&quot;font-family&quot;
        //&quot;font-style&quot;
        //&quot;font-weight&quot;
        //&quot;font-size&quot
        Expression style = null;

        //&quot;font-family&quot;
        //&quot;font-style&quot;
        //&quot;font-weight&quot;
        //&quot;font-size&quot
        Expression weight = null;

        //&quot;font-family&quot;
        //&quot;font-style&quot;
        //&quot;font-weight&quot;
        //&quot;font-size&quot
        Expression size = null;

        for (Iterator i = node.getChildValues("CssParameter").iterator(); i.hasNext();) {
            CssParameter css = (CssParameter) i.next();

            if (css.getExpressions().isEmpty()) {
                continue;
            }

            if ("font-family".equals(css.getName())) {
                family = (Expression) css.getExpressions().get(0);
            }

            if ("font-style".equals(css.getName())) {
                style = (Expression) css.getExpressions().get(0);
            }

            if ("font-weight".equals(css.getName())) {
                weight = (Expression) css.getExpressions().get(0);
            }

            if ("font-size".equals(css.getName())) {
                size = (Expression) css.getExpressions().get(0);
            }
        }

        return styleFactory.createFont(family, style, weight, size);
    }
}
