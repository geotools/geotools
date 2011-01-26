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
package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;


/**
 * Binding object for the type http://www.opengis.net/wfs:Base_TypeNameListType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:simpleType name="Base_TypeNameListType"&gt;
 *      &lt;xsd:list itemType="xsd:QName"/&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class Base_TypeNameListTypeBinding extends AbstractSimpleBinding {
    public Base_TypeNameListTypeBinding(WfsFactory factory) {
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.Base_TypeNameListType;
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
        //TODO: implement and remove call to super
        return super.parse(instance, value);
    }
}
