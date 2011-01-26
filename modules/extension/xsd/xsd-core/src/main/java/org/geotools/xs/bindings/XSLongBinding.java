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
 * Binding object for the type http://www.w3.org/2001/XMLSchema:long.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="long" id="long"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:appinfo&gt;
 *              &lt;hfp:hasProperty name="bounded" value="true"/&gt;
 *              &lt;hfp:hasProperty name="cardinality" value="finite"/&gt;
 *          &lt;/xs:appinfo&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#long"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:integer"&gt;
 *          &lt;xs:minInclusive value="-9223372036854775808" id="long.minInclusive"/&gt;
 *          &lt;xs:maxInclusive value="9223372036854775807" id="long.maxInclusive"/&gt;
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
public class XSLongBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.LONG;
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
     * This binding returns objects of type {@link Long}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Long.class;
    }

    /**
     * <!-- begin-user-doc -->
     * This binding returns objects of type {@link Long}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        String text = (String) value;

        if(text == null || text.length() == 0){
            return null;
        }

        if (text.charAt(0) == '+') {
            text = text.substring(1);
        }

        return new Long(text);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        Long l = (Long) object;

        return l.toString();
    }
}
