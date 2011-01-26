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
package org.geotools.ml.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;


/**
 * Strategy object for the type http://mails/refractions/net:bodyType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:simpleType name="bodyType"&gt;
 *      &lt;xsd:restriction base="xsd:string"/&gt;
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
public class MLBodyTypeBinding extends AbstractSimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return ML.BODYTYPE;
    }

    public Class getType() {
        return String.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        //just a string
        return value;
    }
}
