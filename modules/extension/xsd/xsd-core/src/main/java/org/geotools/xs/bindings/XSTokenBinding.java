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
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:token.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="token" id="token"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#token"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:normalizedString"&gt;
 *          &lt;xs:whiteSpace value="collapse" id="token.whiteSpace"/&gt;
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
public class XSTokenBinding extends AbstractSimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.TOKEN;
    }

    /**
     * <!-- begin-user-doc -->
     * This binding delegates to its parent binding, which returns objects of
     * type {@link String}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return String.class;
    }

    /**
     * <!-- begin-user-doc -->
     * This binding delegates to its parent binding, which returns objects of
     * type {@link String}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        //Simply return string value, Whitespace facet is already handled    
        return value;
    }
}
