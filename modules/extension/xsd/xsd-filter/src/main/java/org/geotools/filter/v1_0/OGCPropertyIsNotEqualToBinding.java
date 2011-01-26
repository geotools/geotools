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
package org.geotools.filter.v1_0;

import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the element http://www.opengis.net/ogc:PropertyIsNotEqualTo.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="PropertyIsNotEqualTo"
 *      substitutionGroup="ogc:comparisonOps" type="ogc:BinaryComparisonOpType"/&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class OGCPropertyIsNotEqualToBinding extends AbstractComplexBinding {
    FilterFactory filterfactory;

    public OGCPropertyIsNotEqualToBinding(FilterFactory filterfactory) {
        this.filterfactory = filterfactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.PropertyIsNotEqualTo;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return PropertyIsNotEqualTo.class;
    }

    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        Expression e1 = (Expression) node.getChildValue(0);
        Expression e2 = (Expression) node.getChildValue(1);

        //filter 1.1 only
        //&lt;xsd:attribute default="true" name="matchCase" type="xsd:boolean" use="optional"/&gt;
        Boolean matchCase = Boolean.TRUE;

        if (node.hasAttribute("matchCase")) {
            matchCase = (Boolean) node.getAttributeValue("matchCase");
        }

        return filterfactory.notEqual(e1, e2, matchCase.booleanValue());
    }
}
