/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import javax.xml.namespace.QName;
import org.geotools.se.v1_1.SE;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Binding object for the element http://www.opengis.net/se:FormatDate.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="FormatDate" substitutionGroup="se:Function" type="se:FormatDateType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *  Function for dates.
 *               &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *  &lt;/xsd:element&gt;
 *
 *   </code>
 * </pre>
 *
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="FormatDateType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="se:FunctionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="se:DateValue"/&gt;
 *                  &lt;xsd:element ref="se:Pattern"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *        </code>
 * </pre>
 *
 * @generated
 */
public class FormatDateBinding extends AbstractComplexBinding {

    FilterFactory filterFactory;

    public FormatDateBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    /** @generated */
    public QName getTarget() {
        return SE.FormatDate;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Function.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // &lt;xsd:element ref="se:DateValue"/&gt;
        Expression date = (Expression) node.getChildValue("DateValue");

        // &lt;xsd:element ref="se:Pattern"/&gt;
        Expression format = filterFactory.literal(node.getChildValue("Pattern"));

        return filterFactory.function("dateFormat", format, date);
    }
}
