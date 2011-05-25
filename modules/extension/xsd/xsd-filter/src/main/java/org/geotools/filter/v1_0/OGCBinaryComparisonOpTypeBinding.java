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
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/ogc:BinaryComparisonOpType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="BinaryComparisonOpType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:ComparisonOpsType"&gt;
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
 *
 * @source $URL$
 */
public class OGCBinaryComparisonOpTypeBinding extends AbstractComplexBinding {
    private FilterFactory factory;

    public OGCBinaryComparisonOpTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.BinaryComparisonOpType;
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
        return BinaryComparisonOperator.class;
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
        //implemented by comcreate elements;
        return null;

        //    	//TODO: replace with element bindings
        //        Expression e1 = (Expression) node.getChildValue(0);
        //        Expression e2 = (Expression) node.getChildValue(1);
        //
        //        String name = instance.getName();
        //
        //        //		<xsd:element name="PropertyIsEqualTo" substitutionGroup="ogc:comparisonOps" type="ogc:BinaryComparisonOpType"/>
        //        if ("PropertyIsEqualTo".equals(name)) {
        //            return factory.equals(e1, e2);
        //        }
        //        //		<xsd:element name="PropertyIsNotEqualTo" substitutionGroup="ogc:comparisonOps" type="ogc:BinaryComparisonOpType"/>
        //        else if ("PropertyIsNotEqualTo".equals(name)) {
        //            //TODO: add geoapi interface
        //            return factory.not(factory.equals(e1, e2));
        //        }
        //        //		<xsd:element name="PropertyIsLessThan" substitutionGroup="ogc:comparisonOps" type="ogc:BinaryComparisonOpType"/>
        //        else if ("PropertyIsLessThan".equals(name)) {
        //            return factory.less(e1, e2);
        //        }
        //        //		<xsd:element name="PropertyIsGreaterThan" substitutionGroup="ogc:comparisonOps" type="ogc:BinaryComparisonOpType"/>
        //        else if ("PropertyIsGreaterThan".equals(name)) {
        //            return factory.greater(e1, e2);
        //        }
        //        //		<xsd:element name="PropertyIsLessThanOrEqualTo" substitutionGroup="ogc:comparisonOps" type="ogc:BinaryComparisonOpType"/>
        //        else if ("PropertyIsLessThanOrEqualTo".equals(name)) {
        //            return factory.lessOrEqual(e1, e2);
        //        }
        //        //		<xsd:element name="PropertyIsGreaterThanOrEqualTo" substitutionGroup="ogc:comparisonOps" type="ogc:BinaryComparisonOpType"/>		
        //        else if ("PropertyIsGreaterThanOrEqualTo".equals(name)) {
        //            return factory.greaterOrEqual(e1, e2);
        //        } else {
        //            throw new IllegalStateException(name);
        //        }
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (OGC.expression.equals(name)) {
            BinaryComparisonOperator op = (BinaryComparisonOperator) object;

            return new Expression[] { op.getExpression1(), op.getExpression2() };
        }

        //filter 1.1 only
        if ("matchCase".equals(name.getLocalPart())) {
            BinaryComparisonOperator op = (BinaryComparisonOperator) object;

            return Boolean.valueOf(op.isMatchingCase());
        }

        return null;
    }
}
