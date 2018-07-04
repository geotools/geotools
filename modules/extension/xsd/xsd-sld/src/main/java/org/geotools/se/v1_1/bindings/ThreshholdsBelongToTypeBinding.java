/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import javax.xml.namespace.QName;
import org.geotools.filter.visitor.ThreshholdsBelongTo;
import org.geotools.se.v1_1.SE;
import org.geotools.xml.*;

/**
 * Binding object for the type http://www.opengis.net/se:ThreshholdsBelongToType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:simpleType name="ThreshholdsBelongToType"&gt;
 *      &lt;xsd:restriction base="xsd:token"&gt;
 *          &lt;xsd:enumeration value="succeeding"/&gt;
 *          &lt;xsd:enumeration value="preceding"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 * @source $URL$
 */
public class ThreshholdsBelongToTypeBinding extends AbstractSimpleBinding {

    /** @generated */
    public QName getTarget() {
        return SE.ThreshholdsBelongToType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ThreshholdsBelongTo.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        if ("succeeding".equals(value)) {
            return ThreshholdsBelongTo.SUCCEEDING;
        }
        if ("preceding".equals(value)) {
            return ThreshholdsBelongTo.PRECEDING;
        }

        return null;
    }
}
