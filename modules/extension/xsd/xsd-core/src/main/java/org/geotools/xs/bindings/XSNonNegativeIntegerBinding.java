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
 * Binding object for the type http://www.w3.org/2001/XMLSchema:nonNegativeInteger.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="nonNegativeInteger" id="nonNegativeInteger"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#nonNegativeInteger"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:integer"&gt;
 *          &lt;xs:minInclusive value="0" id="nonNegativeInteger.minInclusive"/&gt;
 *      &lt;/xs:restriction&gt;
 *  &lt;/xs:simpleType&gt;
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
public class XSNonNegativeIntegerBinding implements SimpleBinding {
    final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    final BigInteger MAX_INTEGER = BigInteger.valueOf(Integer.MAX_VALUE);

    /**
     * @generated
     */
    public QName getTarget() {
        return XS.NONNEGATIVEINTEGER;
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
     * This binding returns objects of type {@link Number.class}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BigInteger.class;
    }

    /**
     * <!-- begin-user-doc -->
     * Restriction of integer to non negative values.
     * <p>
     * Please just treat this as a Number, actual value returned
     * may be BigInteger or Long or Integer.
     * </p>
     * @param instance with text to be parsed
     * @param value BigInteger from parent XSIntegerStratagy
     * @return Number positive in range 0 to Integer.MAX_INT
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        BigInteger number = (BigInteger) value;

        if (BigInteger.ZERO.compareTo(number) > 0) {
            throw new IllegalArgumentException("Value '" + number
                + "' must be non-negative (0 or above).");
        }

        if (MAX_INTEGER.compareTo(number) >= 0) {
            return new Integer(number.intValue());
        }

        if (MAX_LONG.compareTo(number) >= 0) {
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

        if (number.longValue() < 0) {
            throw new IllegalArgumentException("Value '" + number
                + "' must be non-negative (0 or above).");
        }

        return value;
    }
}
