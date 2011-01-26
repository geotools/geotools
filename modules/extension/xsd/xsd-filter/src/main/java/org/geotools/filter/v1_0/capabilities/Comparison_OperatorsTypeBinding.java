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

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.Operator;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:Comparison_OperatorsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="Comparison_OperatorsType"&gt;
 *      &lt;xsd:choice maxOccurs="unbounded"&gt;
 *          &lt;xsd:element ref="ogc:Simple_Comparisons"/&gt;
 *          &lt;xsd:element ref="ogc:Like"/&gt;
 *          &lt;xsd:element ref="ogc:Between"/&gt;
 *          &lt;xsd:element ref="ogc:NullCheck"/&gt;
 *      &lt;/xsd:choice&gt;
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
public class Comparison_OperatorsTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public Comparison_OperatorsTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.Comparison_OperatorsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ComparisonOperators.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        List comparisons = new ArrayList();

        //&lt;xsd:element ref="ogc:Simple_Comparisons"/&gt;
        if (node.hasChild("Simple_Comparisons")) {
            comparisons.add(factory.operator("LessThan"));
            comparisons.add(factory.operator("LessThanOrEqualTo"));
            comparisons.add(factory.operator("GreaterThan"));
            comparisons.add(factory.operator("GreaterThanOrEqualTo"));
            comparisons.add(factory.operator("EqualTo"));
            comparisons.add(factory.operator("NotEqualTo"));
        }

        //&lt;xsd:element ref="ogc:Like"/&gt;
        if (node.hasChild("Like")) {
            comparisons.add(factory.operator("Like"));
        }

        //&lt;xsd:element ref="ogc:Between"/&gt;
        if (node.hasChild("Between")) {
            comparisons.add(factory.operator("Between"));
        }

        //&lt;xsd:element ref="ogc:NullCheck"/&gt;
        if (node.hasChild("NullCheck")) {
            comparisons.add(factory.operator("NullCheck"));
        }

        return factory.comparisonOperators((Operator[]) comparisons.toArray(
                new Operator[comparisons.size()]));
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        ComparisonOperators comparison = (ComparisonOperators) object;

        if (name.equals(OGC.Simple_Comparisons) && (comparison.getOperator("LessThan") != null)) {
            return new Object();
        }

        if (comparison.getOperator(name.getLocalPart()) != null) {
            return new Object();
        }

        return null;
    }
}
