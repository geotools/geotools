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
package org.geotools.filter;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.BinaryComparisonOperator;


/**
 * Defines a comparison filter (can be a math comparison or generic equals).
 * This filter implements a comparison - of some sort - between two
 * expressions. The comparison may be a math comparison or a generic equals
 * comparison.  If it is a math comparison, only math expressions are allowed;
 * if it is an equals comparison, any expression types are allowed. Note that
 * this comparison does not attempt to restrict its expressions to be
 * meaningful.  This means that it considers itself a valid filter as long as
 * the expression comparison returns a valid result.  It does no checking to
 * see whether or not the expression comparison is meaningful with regard to
 * checking feature attributes.  In other words, this is a valid filter: <b>52
 * = 92</b>, even though it will always return the same result and could be
 * simplified away.  It is up the the filter creator, therefore, to attempt to
 * simplify/make meaningful filter logic.
 *
 * @author Rob Hranac, Vision for New York
 * @source $URL$
 * @version $Id$
 *
 * @deprecated use {@link org.opengis.filter.BinaryComparisonOperator}
 */
public interface CompareFilter extends Filter, BinaryComparisonOperator {
    /**
     * Adds the 'left' value to this filter.
     *
     * @param leftValue Expression for 'left' value.
     *
     * @throws IllegalFilterException Filter is not internally consistent.
     *
     * @task REVISIT: immutability?
     * @deprecated use {@link BinaryComparisonOperator#setExpression1(Expression)}
     */
    void addLeftValue(Expression leftValue) throws IllegalFilterException;

    /**
     * Adds the 'right' value to this filter.
     *
     * @param rightValue Expression for 'right' value.
     *
     * @throws IllegalFilterException Filter is not internally consistent.
     *
     * @task REVISIT: make immutable.
     *
     * @deprecated use {@link BinaryComparisonOperator#setExpression2(Expression)}
     */
    void addRightValue(Expression rightValue) throws IllegalFilterException;

    /**
     * Gets the left expression.
     *
     * @return The expression on the left of the comparison.
     *
     * @deprecated use {@link BinaryComparisonOperator#getExpression1()}
     */
    Expression getLeftValue();

    /**
     * Gets the right expression.
     *
     * @return The expression on the right of the comparison.
     *
     * @deprecated use {@link BinaryComparisonOperator#getExpression2()}
     */
    Expression getRightValue();

    /**
     * Determines whether or not a given feature is 'inside' this filter.
     *
     * @param feature Specified feature to examine.
     *
     * @return Whether or not this feature is inside the filter.
     *
     * @deprecated use {@link org.opengis.filter.Filter#evaluate(Feature)}
     */
    boolean contains(SimpleFeature feature);
}
