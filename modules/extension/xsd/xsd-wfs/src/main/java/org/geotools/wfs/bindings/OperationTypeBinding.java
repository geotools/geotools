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

import net.opengis.wfs.OperationType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;


/**
 * Binding object for the type http://www.opengis.net/wfs:OperationType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:simpleType name="OperationType"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:enumeration value="Insert"/&gt;
 *          &lt;xsd:enumeration value="Update"/&gt;
 *          &lt;xsd:enumeration value="Delete"/&gt;
 *          &lt;xsd:enumeration value="Query"/&gt;
 *          &lt;xsd:enumeration value="Lock"/&gt;
 *          &lt;xsd:enumeration value="GetGmlObject"/&gt;
 *      &lt;/xsd:restriction&gt;
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
public class OperationTypeBinding extends AbstractSimpleBinding {
    public OperationTypeBinding(WfsFactory factory) {
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.OperationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return OperationType.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        String string = (String) value;

        //&lt;xsd:enumeration value="Insert"/&gt;
        if (OperationType.INSERT_LITERAL.getLiteral().equals(string)) {
            return OperationType.INSERT_LITERAL;
        }

        //&lt;xsd:enumeration value="Update"/&gt;
        if (OperationType.UPDATE_LITERAL.getLiteral().equals(string)) {
            return OperationType.UPDATE_LITERAL;
        }

        //&lt;xsd:enumeration value="Delete"/&gt;
        if (OperationType.DELETE_LITERAL.getLiteral().equals(string)) {
            return OperationType.DELETE_LITERAL;
        }

        //&lt;xsd:enumeration value="Query"/&gt;
        if (OperationType.QUERY_LITERAL.getLiteral().equals(string)) {
            return OperationType.QUERY_LITERAL;
        }

        //&lt;xsd:enumeration value="Lock"/&gt;
        if (OperationType.LOCK_LITERAL.getLiteral().equals(string)) {
            return OperationType.LOCK_LITERAL;
        }

        //&lt;xsd:enumeration value="GetGmlObject"/&gt;
        if (OperationType.GET_GML_OBJECT_LITERAL.getLiteral().equals(string)) {
            return OperationType.GET_GML_OBJECT_LITERAL;
        }

        return value;
    }
}
