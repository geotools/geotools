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
import org.geotools.xml.impl.DatatypeConverterImpl;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:base64Binary.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="base64Binary" id="base64Binary"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:appinfo&gt;
 *              &lt;hfp:hasFacet name="length"/&gt;
 *              &lt;hfp:hasFacet name="minLength"/&gt;
 *              &lt;hfp:hasFacet name="maxLength"/&gt;
 *              &lt;hfp:hasFacet name="pattern"/&gt;
 *              &lt;hfp:hasFacet name="enumeration"/&gt;
 *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
 *              &lt;hfp:hasProperty name="ordered" value="false"/&gt;
 *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
 *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
 *              &lt;hfp:hasProperty name="numeric" value="false"/&gt;
 *          &lt;/xs:appinfo&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#base64Binary"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:anySimpleType"&gt;
 *          &lt;xs:whiteSpace value="collapse" fixed="true" id="base64Binary.whiteSpace"/&gt;
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
public class XSBase64BinaryBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.BASE64BINARY;
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
     * This binding returns arrays of type byte[].
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return byte[].class;
    }

    /**
     * <!-- begin-user-doc -->
     * This binding returns arrays of type byte[].
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {

        return DatatypeConverterImpl.getInstance().parseBase64Binary((String)value); 
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        byte[] bin = (byte[]) object;

        return DatatypeConverterImpl.getInstance().printBase64Binary(bin);
    }
}
