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
 * Binding object for the type http://www.w3.org/2001/XMLSchema:blockSet.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="blockSet"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation&gt;     A utility type, not for public use&lt;/xs:documentation&gt;
 *          &lt;xs:documentation&gt;     #all or (possibly empty) subset of
 *              {substitution, extension,     restriction}&lt;/xs:documentation&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:union&gt;
 *          &lt;xs:simpleType&gt;
 *              &lt;xs:restriction base="xs:token"&gt;
 *                  &lt;xs:enumeration value="#all"/&gt;
 *              &lt;/xs:restriction&gt;
 *          &lt;/xs:simpleType&gt;
 *          &lt;xs:simpleType&gt;
 *              &lt;xs:list&gt;
 *                  &lt;xs:simpleType&gt;
 *                      &lt;xs:restriction base="xs:derivationControl"&gt;
 *                          &lt;xs:enumeration value="extension"/&gt;
 *                          &lt;xs:enumeration value="restriction"/&gt;
 *                          &lt;xs:enumeration value="substitution"/&gt;
 *                      &lt;/xs:restriction&gt;
 *                  &lt;/xs:simpleType&gt;
 *              &lt;/xs:list&gt;
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
public class XSBlockSetBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.BLOCKSET;
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
