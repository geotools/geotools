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
import java.util.Map;
import javax.xml.namespace.QName;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:complexType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:complexType name="complexType" abstract="true"&gt;
 *      &lt;xs:complexContent&gt;
 *          &lt;xs:extension base="xs:annotated"&gt;
 *              &lt;xs:group ref="xs:complexTypeModel"/&gt;
 *              &lt;xs:attribute name="name" type="xs:NCName"&gt;
 *                  &lt;xs:annotation&gt;
 *                      &lt;xs:documentation&gt;       Will be restricted to
 *                          required or forbidden&lt;/xs:documentation&gt;
 *                  &lt;/xs:annotation&gt;
 *              &lt;/xs:attribute&gt;
 *              &lt;xs:attribute name="mixed" type="xs:boolean" use="optional" default="false"&gt;
 *                  &lt;xs:annotation&gt;
 *                      &lt;xs:documentation&gt;       Not allowed if
 *                          simpleContent child is chosen.       May be
 *                          overriden by setting on complexContent child.&lt;/xs:documentation&gt;
 *                  &lt;/xs:annotation&gt;
 *              &lt;/xs:attribute&gt;
 *              &lt;xs:attribute name="abstract" type="xs:boolean"
 *                  use="optional" default="false"/&gt;
 *              &lt;xs:attribute name="final" type="xs:derivationSet"/&gt;
 *              &lt;xs:attribute name="block" type="xs:derivationSet"/&gt;
 *          &lt;/xs:extension&gt;
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
public class XSComplexTypeBinding extends AbstractComplexBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.COMPLEXTYPE;
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
     * This binding delegates to its parent binding, which returns objects of
     * type {@link Map}.
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
     * This binding delegates to its parent binding, which returns objects of
     * type {@link Map}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return value; //all work done by super type
    }
}
