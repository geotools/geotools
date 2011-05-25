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

import javax.xml.namespace.QName;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:negativeInteger.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="negativeInteger" id="negativeInteger"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#negativeInteger"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:nonPositiveInteger"&gt;
 *          &lt;xs:maxInclusive value="-1" id="negativeInteger.maxInclusive"/&gt;
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
public class XSNegativeIntegerBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.NEGATIVEINTEGER;
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
     * This method returns objects of type {@link Number}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Number.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * This method returns objects of type {@link Number}. The value returned
     * is negative.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        Number number = (Number) value;

        if (number.longValue() == 0) {
            throw new IllegalArgumentException("negativeInteger value '" + number
                + "' required to be negative");
        }

        return number;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        //TODO: implement
        return null;
    }
}
