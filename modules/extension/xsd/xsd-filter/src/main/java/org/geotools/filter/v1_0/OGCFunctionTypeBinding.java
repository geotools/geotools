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
package org.geotools.filter.v1_0;

import org.picocontainer.MutablePicoContainer;
import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/ogc:FunctionType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="FunctionType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:ExpressionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element maxOccurs="unbounded" minOccurs="0" ref="ogc:expression"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attribute name="name" type="xsd:string" use="required"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
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
public class OGCFunctionTypeBinding extends AbstractComplexBinding {
    private FilterFactory factory;

    public OGCFunctionTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.FunctionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Function.class;
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
        Expression[] args = new Expression[node.getChildren().size()];

        for (int i = 0; i < node.getChildren().size(); i++) {
            Node child = (Node) node.getChildren().get(i);
            args[i] = (Expression) child.getValue();
        }

        String name = (String) node.getAttribute("name").getValue();

        return factory.function(name, args);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        Function function = (Function) object;

        //&lt;xsd:element maxOccurs="unbounded" minOccurs="0" ref="ogc:expression"/&gt;
        if (OGC.expression.equals(name)) {
            return function.getParameters();
        }

        //&lt;xsd:attribute name="name" type="xsd:string" use="required"/&gt;
        if ("name".equals(name.getLocalPart())) {
            return function.getName();
        }

        return null;
    }
}
