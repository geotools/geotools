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
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Expression;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/ogc:BinaryOperatorType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="BinaryOperatorType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:ExpressionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element maxOccurs="2" minOccurs="2" ref="ogc:expression"/&gt;
 *              &lt;/xsd:sequence&gt;
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
public class OGCBinaryOperatorTypeBinding extends AbstractComplexBinding {
    private FilterFactory factory;

    public OGCBinaryOperatorTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.BinaryOperatorType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BinaryExpression.class;
    }

    /**
     * <!-- begin-user-doc -->
     * We check out the instance for the <code>op</code> so we can fail early.
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
        //implemented by element bindings
        return null;

        //        //TODO: replace this binding with element bindings
        //        Expression e1 = (Expression) node.getChildValue(0);
        //        Expression e2 = (Expression) node.getChildValue(1);
        //
        //        String name = instance.getName();
        //
        //        // <xsd:element name="Add" substitutionGroup="ogc:expression" type="ogc:BinaryOperatorType"/>
        //        if ("Add".equals(name)) {
        //            return factory.add(e1, e2);
        //        }
        //
        //        // <xsd:element name="Sub" substitutionGroup="ogc:expression" type="ogc:BinaryOperatorType"/>
        //        if ("Sub".equals(name)) {
        //            return factory.subtract(e1, e2);
        //        }
        //
        //        // <xsd:element name="Mul" substitutionGroup="ogc:expression" type="ogc:BinaryOperatorType"/>
        //        if ("Mul".equals(name)) {
        //            return factory.multiply(e1, e2);
        //        }
        //
        //        // <xsd:element name="Div" substitutionGroup="ogc:expression" type="ogc:BinaryOperatorType"/>
        //        if ("Div".equals(name)) {
        //            return factory.divide(e1, e2);
        //        }
        //
        //        throw new IllegalStateException("BinaryOpperatorType supports Add, Sub, Mul, Div");
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        BinaryExpression binary = (BinaryExpression) object;

        if (OGC.expression.equals(name)) {
            return new Expression[] { binary.getExpression1(), binary.getExpression2() };
        }

        return null;
    }
}
