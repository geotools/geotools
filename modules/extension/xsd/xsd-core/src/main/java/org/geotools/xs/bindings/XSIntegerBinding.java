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
 * Binding object for the type http://www.w3.org/2001/XMLSchema:integer.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="integer" id="integer"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#integer"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:decimal"&gt;
 *          &lt;xs:fractionDigits value="0" fixed="true" id="integer.fractionDigits"/&gt;
 *          &lt;xs:pattern value="[\-+]?[0-9]+"/&gt;
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
public class XSIntegerBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.INTEGER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
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
     * This binding returns objects of type {@link BigInteger}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        String string = (String) value;

        if (string.startsWith("+")) {
            string = string.substring(1);
        }

        return new BigInteger(string);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        //dont cast to big integer, this binding is often subclassed 
        Number integer = (Number) object;

        return integer.toString();
    }
}
