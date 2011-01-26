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

import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding object for the element http://www.opengis.net/sld:Mark.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="Mark"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;Mark&quot; specifies a
 *              geometric shape and applies coloring to it.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:element ref="sld:WellKnownName" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Fill" minOccurs="0"/&gt;
 *              &lt;xsd:element ref="sld:Stroke" minOccurs="0"/&gt;
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
public class SLDMarkBinding extends AbstractComplexBinding {
    protected FilterFactory filterFactory;
    protected StyleFactory styleFactory;

    public SLDMarkBinding(StyleFactory styleFactory, FilterFactory filterFactory) {
        this.styleFactory = styleFactory;
        this.filterFactory = filterFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return SLD.MARK;
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
        return Mark.class;
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
        String wkName = (String) node.getChildValue("WellKnownName");
        Stroke stroke = (Stroke) node.getChildValue("Stroke");
        Fill fill = (Fill) node.getChildValue("Fill");

        Mark mark = styleFactory.createMark();

        if (wkName != null) {
            mark.setWellKnownName(filterFactory.literal(wkName));
        }

        if (stroke != null) {
            mark.setStroke(stroke);
        }

        if (fill != null) {
            mark.setFill(fill);
        }

        return mark;
    }
}
