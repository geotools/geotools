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

import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.ScalarCapabilities;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:Scalar_CapabilitiesType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="Scalar_CapabilitiesType"&gt;
 *      &lt;xsd:choice maxOccurs="unbounded"&gt;
 *          &lt;xsd:element ref="ogc:Logical_Operators"/&gt;
 *          &lt;xsd:element name="Comparison_Operators" type="ogc:Comparison_OperatorsType"/&gt;
 *          &lt;xsd:element name="Arithmetic_Operators" type="ogc:Arithmetic_OperatorsType"/&gt;
 *      &lt;/xsd:choice&gt;
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
public class Scalar_CapabilitiesTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public Scalar_CapabilitiesTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.Scalar_CapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ScalarCapabilities.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //&lt;xsd:element ref="ogc:Logical_Operators"/&gt;
        boolean logical = node.hasChild("Logical_Operators") || node.hasChild("LogicalOperators"); /* 1.1 */

        //&lt;xsd:element name="Comparison_Operators" type="ogc:Comparison_OperatorsType"/&gt;
        ComparisonOperators comparison = (ComparisonOperators) node.getChildValue(ComparisonOperators.class);

        //&lt;xsd:element name="Arithmetic_Operators" type="ogc:Arithmetic_OperatorsType"/&gt;
        ArithmeticOperators arithmetic = (ArithmeticOperators) node.getChildValue(ArithmeticOperators.class);

        return factory.scalarCapabilities(comparison, arithmetic, logical);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        ScalarCapabilities scalar = (ScalarCapabilities) object;

        if ((OGC.Logical_Operators.equals(name)
                || org.geotools.filter.v1_1.OGC.LogicalOperators.equals(name))
                && scalar.hasLogicalOperators()) {
            return new Object();
        }

        if ("Comparison_Operators".equals(name.getLocalPart())
                || "ComparisonOperators".equals(name.getLocalPart()) /* 1.1 */) {
            return scalar.getComparisonOperators();
        }

        if ("Arithmetic_Operators".equals(name.getLocalPart())
                || "ArithmeticOperators".equals(name.getLocalPart()) /* 1.1 */) {
            return scalar.getArithmeticOperators();
        }

        return null;
    }
}
