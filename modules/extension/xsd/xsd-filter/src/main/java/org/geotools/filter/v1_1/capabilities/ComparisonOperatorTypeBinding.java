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
package org.geotools.filter.v1_1.capabilities;

import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.Operator;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:ComparisonOperatorType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:simpleType name="ComparisonOperatorType"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:enumeration value="LessThan"/&gt;
 *          &lt;xsd:enumeration value="GreaterThan"/&gt;
 *          &lt;xsd:enumeration value="LessThanEqualTo"/&gt;
 *          &lt;xsd:enumeration value="GreaterThanEqualTo"/&gt;
 *          &lt;xsd:enumeration value="EqualTo"/&gt;
 *          &lt;xsd:enumeration value="NotEqualTo"/&gt;
 *          &lt;xsd:enumeration value="Like"/&gt;
 *          &lt;xsd:enumeration value="Between"/&gt;
 *          &lt;xsd:enumeration value="NullCheck"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class ComparisonOperatorTypeBinding extends AbstractSimpleBinding {
    FilterFactory factory;

    public ComparisonOperatorTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.ComparisonOperatorType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Operator.class;
    }

    public int getExecutionMode() {
        return Binding.OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        return factory.operator((String) value);
    }

    public String encode(Object object, String value) throws Exception {
        Operator op = (Operator) object;

        return op.getName();
    }
}
