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
package org.geotools.filter.v1_1.capabilities;

import java.util.List;
import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.Operator;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ogc:ComparisonOperatorsType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="ComparisonOperatorsType"&gt;
 *      &lt;xsd:sequence maxOccurs="unbounded"&gt;
 *          &lt;xsd:element name="ComparisonOperator" type="ogc:ComparisonOperatorType"/&gt;
 *      &lt;/xsd:sequence&gt;
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
public class ComparisonOperatorsTypeBinding extends AbstractComplexBinding {
    FilterFactory factory;

    public ComparisonOperatorsTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.ComparisonOperatorsType;
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
        List ops = node.getChildValues(Operator.class);

        return factory.comparisonOperators((Operator[]) ops.toArray(new Operator[ops.size()]));
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("ComparisonOperator".equals(name.getLocalPart())) {
            ComparisonOperators ops = (ComparisonOperators) object;

            return ops.getOperators();
        }

        return null;
    }
}
