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
 * Binding object for the type http://www.w3.org/2001/XMLSchema:IDREF.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xs:simpleType name="IDREF" id="IDREF"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#IDREF"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:NCName"/&gt;
 *  &lt;/xs:simpleType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class XSIDREFBinding implements SimpleBinding {
    /** @generated */
    public QName getTarget() {
        return XS.IDREF;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        // TODO: implement me
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        // TODO: implement
        return null;
    }
}
