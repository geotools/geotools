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
import org.opengis.filter.PropertyIsBetween;


/**
 * Defines a 'between' filter (which is a specialized compare filter). A
 * between filter is just shorthand for a less-than-or-equal filter ANDed with
 * a greater-than-or-equal filter.  Arguably, this would be better handled
 * using those constructs, but the OGC filter specification creates its own
 * object for this, so we do as well.  An important note here is that a
 * between filter is actually a math filter, so its outer (left and right)
 * expressions must be math expressions.  This is enforced by the
 * FilterAbstract class, which considers a BETWEEN operator to be a math
 * filter.
 *
 * @author Rob Hranac, TOPP
 * @source $URL$
 * @version $Id$
 *
 * @deprecated use {@link org.opengis.filter.PropertyIsBetween}
 */
public interface BetweenFilter extends CompareFilter, PropertyIsBetween {
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

    /**
     * @deprecated use {@link PropertyIsBetween#getLowerBoundary()}
     */
    org.opengis.filter.expression.Expression getExpression1();

    /**
     * @deprecated use {@link PropertyIsBetween#getUpperBoundary()()}
     */
    org.opengis.filter.expression.Expression getExpression2();

    /**
     * @deprecated use {@link PropertyIsBetween#setLowerBoundary(Expression)()}
     */
    void setExpression1(org.opengis.filter.expression.Expression expression);

    /**
     * @deprecated use {@link PropertyIsBetween#setUpperBoundary(Expression)()}
     */
    void setExpression2(org.opengis.filter.expression.Expression expression);

    /**
     * Gets the middle value of the between.  Should generally be an
     * AttributeExpression: 1 <= area <= 200 makes sense,
     * 1 <= 200 <= area should just use a less-than-or-equal filter.
     *
     * @return the expression in the middle.
     *
     * @deprecated use {@link PropertyIsBetween#getExpression()}
     */
    Expression getMiddleValue();

    /**
     * Sets the values to be compared as between the left and right values.
     *
     * @param middleValue The expression to be compared.
     * @task REVISIT: rename to setMiddleValue?  You can't have more than 1.
     *
     * @deprecated use {@link PropertyIsBetween#setExpression(Expression)}
     */
    void addMiddleValue(Expression middleValue);
}
