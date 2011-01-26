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
package org.geotools.filter.v1_0.capabilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.List;
import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:Function_NameType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="Function_NameType"&gt;
 *      &lt;xsd:simpleContent&gt;
 *          &lt;xsd:extension base="xsd:string"&gt;
 *              &lt;xsd:attribute name="nArgs" type="xsd:string" use="required"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:simpleContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class Function_NameTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public Function_NameTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.Function_NameType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return FunctionName.class;
    }

    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        String name = (String) value;

        //&lt;xsd:attribute name="nArgs" type="xsd:string" use="required"/&gt;
        String nargs = (String) node.getAttributeValue("nArgs");

        return factory.functionName(name, Integer.parseInt(nargs));
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        FunctionName function = (FunctionName) object;
        value.appendChild(document.createTextNode(function.getName()));
        value.setAttributeNS("", "nArgs", function.getArgumentCount() + "");

        return value;
    }
}
