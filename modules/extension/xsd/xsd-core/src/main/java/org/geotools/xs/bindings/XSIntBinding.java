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
import org.geotools.xs.XS;
import org.geotools.xsd.InstanceComponent;
import org.geotools.xsd.SimpleBinding;

/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:int.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xs:simpleType name="int" id="int"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#int"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:long"&gt;
 *          &lt;xs:minInclusive value="-2147483648" id="int.minInclusive"/&gt;
 *          &lt;xs:maxInclusive value="2147483647" id="int.maxInclusive"/&gt;
 *      &lt;/xs:restriction&gt;
 *  &lt;/xs:simpleType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class XSIntBinding implements SimpleBinding {
    /** @generated */
    public QName getTarget() {
        return XS.INT;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * This binding returns objects of type {@link Integer}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Integer.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * This binding returns objects of type {@link Integer}. This binding is an override of the
     * parent.
     * <!-- end-user-doc -->
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        String text = (String) value;

        if (text.charAt(0) == '+') {
            text = text.substring(1);
        }

        return Integer.valueOf(text);
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        Integer integer = (Integer) object;

        return integer.toString();
    }
}
