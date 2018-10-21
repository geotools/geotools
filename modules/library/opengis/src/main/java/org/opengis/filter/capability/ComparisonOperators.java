/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.capability;

import java.util.Collection;

/**
 * Supported comparison operators in a filter capabilities document.
 *
 * <p>
 *
 * <pre>
 * &lt;xsd:complexType name="ComparisonOperatorsType">
 *     &lt;xsd:sequence maxOccurs="unbounded">
 *        &lt;xsd:element name="ComparisonOperator" type="ogc:ComparisonOperatorType"/>
 *     &lt;/xsd:sequence>
 *  &lt;/xsd:complexType>
 * </pre>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface ComparisonOperators {

    /**
     * Provided comparison operators.
     *
     * <p>
     *
     * <pre>
     *  &lt;xsd:element name="ComparisonOperator" type="ogc:ComparisonOperatorType"/>
     *  </pre>
     */
    Collection<Operator> getOperators();

    /**
     * Looks up an operator by name, returning null if no such operator found.
     *
     * @param name the name of the operator.
     * @return The operator, or null.
     */
    Operator getOperator(String name);
}
