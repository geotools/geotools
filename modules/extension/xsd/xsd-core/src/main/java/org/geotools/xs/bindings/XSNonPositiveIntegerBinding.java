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
package org.geotools.xs.bindings;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:nonPositiveInteger.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="nonPositiveInteger" id="nonPositiveInteger"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#nonPositiveInteger"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:integer"&gt;
 *          &lt;xs:maxInclusive value="0" id="nonPositiveInteger.maxInclusive"/&gt;
 *      &lt;/xs:restriction&gt;
 *  &lt;/xs:simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class XSNonPositiveIntegerBinding implements SimpleBinding {
    final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
    final BigInteger MIN_INTEGER = BigInteger.valueOf(Integer.MIN_VALUE);

    /**
     * @generated
     */
    public QName getTarget() {
        return XS.NONPOSITIVEINTEGER;
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
     * This binding returns objects of type {@link BigInteger}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BigInteger.class;
    }

    /**
     * <!-- begin-user-doc -->
     * @param instance
     * @param value    a BigInteger (after processing by parent)
     * @return a Number that is not positive
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        BigInteger number = (BigInteger) value;

        if (BigInteger.ZERO.compareTo(number) < 0) {
            throw new IllegalArgumentException("Value '" + number
                + "' must be non-positive (0 or below).");
        }

        if (MIN_INTEGER.compareTo(number) <= 0) {
            return new Integer(number.intValue());
        }

        if (MIN_LONG.compareTo(number) <= 0) {
            return new Long(number.longValue());
        }

        return number;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) throws Exception {
        Number number = (Number) object;

        if (number.longValue() > 0) {
            throw new IllegalArgumentException("Value '" + number
                + "' must be non-positive (0 or below).");
        }

        return value;
    }
}
