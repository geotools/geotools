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
 * Binding object for the type http://www.w3.org/2001/XMLSchema:language.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="language" id="language"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#language"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:token"&gt;
 *          &lt;xs:pattern value="[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*" id="language.pattern"&gt;
 *              &lt;xs:annotation&gt;
 *                  &lt;xs:documentation
 *                      source="http://www.ietf.org/rfc/rfc3066.txt"&gt;
 *                      pattern specifies the content of section 2.12 of XML
 *                      1.0e2             and RFC 3066 (Revised version of
 *                      RFC 1766).           &lt;/xs:documentation&gt;
 *              &lt;/xs:annotation&gt;
 *          &lt;/xs:pattern&gt;
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
public class XSLanguageBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.LANGUAGE;
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
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        //TODO: implement me	
        return null;
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
