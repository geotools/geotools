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
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:attribute.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xs:complexType name="attribute"&gt;
 *      &lt;xs:complexContent&gt;
 *          &lt;xs:extension base="xs:annotated"&gt;
 *              &lt;xs:sequence&gt;
 *                  &lt;xs:element name="simpleType" minOccurs="0" type="xs:localSimpleType"/&gt;
 *              &lt;/xs:sequence&gt;
 *              &lt;xs:attributeGroup ref="xs:defRef"/&gt;
 *              &lt;xs:attribute name="type" type="xs:QName"/&gt;
 *              &lt;xs:attribute name="use" use="optional" default="optional"&gt;
 *                  &lt;xs:simpleType&gt;
 *                      &lt;xs:restriction base="xs:NMTOKEN"&gt;
 *                          &lt;xs:enumeration value="prohibited"/&gt;
 *                          &lt;xs:enumeration value="optional"/&gt;
 *                          &lt;xs:enumeration value="required"/&gt;
 *                      &lt;/xs:restriction&gt;
 *                  &lt;/xs:simpleType&gt;
 *              &lt;/xs:attribute&gt;
 *              &lt;xs:attribute name="default" type="xs:string"/&gt;
 *              &lt;xs:attribute name="fixed" type="xs:string"/&gt;
 *              &lt;xs:attribute name="form" type="xs:formChoice"/&gt;
 *          &lt;/xs:extension&gt;
 *      &lt;/xs:complexContent&gt;
 *  &lt;/xs:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class XSAttributeBinding extends AbstractComplexBinding {
    /** @generated */
    public QName getTarget() {
        return XS.ATTRIBUTE;
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
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // TODO: implement
        return null;
    }
}
