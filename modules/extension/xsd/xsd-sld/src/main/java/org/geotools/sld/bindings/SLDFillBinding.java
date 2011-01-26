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
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.sld.CssParameter;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding object for the element http://www.opengis.net/sld:Fill.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="Fill"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;Fill&quot; specifies the
 *              pattern for filling an area geometry.         The allowed
 *              CssParameters are: &quot;fill&quot; (color) and
 *              &quot;fill-opacity&quot;.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:GraphicFill" minOccurs="0"/&gt;
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
public class SLDFillBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;
    FilterFactory filterFactory;
   
    public SLDFillBinding(StyleFactory styleFactory, FilterFactory filterFactory) {
        this.styleFactory = styleFactory;
        this.filterFactory = filterFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.FILL;
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
        return Fill.class;
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
        Expression color = null;
        Expression opacity = null;
        Graphic graphicFill = null;

        graphicFill = (Graphic) node.getChildValue("GraphicFill");

        //&quot;fill&quot; (color) 
        //&quot;fill-opacity&quot;
        List params = node.getChildValues(CssParameter.class);

        for (Iterator itr = params.iterator(); itr.hasNext();) {
            CssParameter param = (CssParameter) itr.next();

            if ("fill".equals(param.getName())) {
                color = param.getExpression();
            }

            if ("fill-opacity".equals(param.getName())) {
                opacity = param.getExpression();
            }
        }

        Fill fill = styleFactory.createFill(color);

        if (opacity != null) {
            fill.setOpacity(opacity);
        }

        return fill;
    }
}
