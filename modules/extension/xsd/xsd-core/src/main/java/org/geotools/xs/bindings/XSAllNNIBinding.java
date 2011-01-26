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
 * Binding object for the type http://www.w3.org/2001/XMLSchema:allNNI.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="allNNI"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation&gt;    for maxOccurs&lt;/xs:documentation&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:union memberTypes="xs:nonNegativeInteger"&gt;
 *          &lt;xs:simpleType&gt;
 *              &lt;xs:restriction base="xs:NMTOKEN"&gt;
 *                  &lt;xs:enumeration value="unbounded"/&gt;
 *              &lt;/xs:restriction&gt;
 *          &lt;/xs:simpleType&gt;
 *      &lt;/xs:union&gt;
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
public class XSAllNNIBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.ALLNNI;
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
