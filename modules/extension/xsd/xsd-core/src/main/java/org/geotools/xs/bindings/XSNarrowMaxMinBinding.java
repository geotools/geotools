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

import org.picocontainer.MutablePicoContainer;
import javax.xml.namespace.QName;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:narrowMaxMin.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:complexType name="narrowMaxMin"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:documentation&gt;restricted max/min&lt;/xs:documentation&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:complexContent&gt;
 *          &lt;xs:restriction base="xs:localElement"&gt;
 *              &lt;xs:sequence&gt;
 *                  &lt;xs:element ref="xs:annotation" minOccurs="0"/&gt;
 *                  &lt;xs:choice minOccurs="0"&gt;
 *                      &lt;xs:element name="simpleType" type="xs:localSimpleType"/&gt;
 *                      &lt;xs:element name="complexType" type="xs:localComplexType"/&gt;
 *                  &lt;/xs:choice&gt;
 *                  &lt;xs:group ref="xs:identityConstraint" minOccurs="0" maxOccurs="unbounded"/&gt;
 *              &lt;/xs:sequence&gt;
 *              &lt;xs:attribute name="minOccurs" use="optional" default="1"&gt;
 *                  &lt;xs:simpleType&gt;
 *                      &lt;xs:restriction base="xs:nonNegativeInteger"&gt;
 *                          &lt;xs:enumeration value="0"/&gt;
 *                          &lt;xs:enumeration value="1"/&gt;
 *                      &lt;/xs:restriction&gt;
 *                  &lt;/xs:simpleType&gt;
 *              &lt;/xs:attribute&gt;
 *              &lt;xs:attribute name="maxOccurs" use="optional" default="1"&gt;
 *                  &lt;xs:simpleType&gt;
 *                      &lt;xs:restriction base="xs:allNNI"&gt;
 *                          &lt;xs:enumeration value="0"/&gt;
 *                          &lt;xs:enumeration value="1"/&gt;
 *                      &lt;/xs:restriction&gt;
 *                  &lt;/xs:simpleType&gt;
 *              &lt;/xs:attribute&gt;
 *              &lt;xs:anyAttribute namespace="##other" processContents="lax"/&gt;
 *          &lt;/xs:restriction&gt;
 *      &lt;/xs:complexContent&gt;
 *  &lt;/xs:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class XSNarrowMaxMinBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.NARROWMAXMIN;
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
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: implement
        return null;
    }
}
