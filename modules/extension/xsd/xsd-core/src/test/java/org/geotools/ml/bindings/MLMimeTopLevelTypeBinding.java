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

import javax.xml.namespace.QName;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;


/**
 * Strategy object for the type http://mails/refractions/net:mimeTopLevelType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:simpleType name="mimeTopLevelType"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:enumeration value="text"/&gt;
 *          &lt;xsd:enumeration value="multipart"/&gt;
 *          &lt;xsd:enumeration value="application"/&gt;
 *          &lt;xsd:enumeration value="message"/&gt;
 *          &lt;xsd:enumeration value="image"/&gt;
 *          &lt;xsd:enumeration value="audio"/&gt;
 *          &lt;xsd:enumeration value="video"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
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
public class MLMimeTopLevelTypeBinding extends AbstractSimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return ML.MIMETOPLEVELTYPE;
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
        //shouldn't have to do anything special here
        return value;
    }
}
