/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.opengis.filter.MultiValuedFilter;
import org.opengis.filter.MultiValuedFilter.MatchAction;
import org.opengis.filter.expression.Expression;

/**
 * Abstract implementation for binary filters.
 *
 * <p>This implementation gathers up expression1, expression2 and match action support.
 *
 * <p>For the SAX parsers setExpression1 and setExpression2 can be used to fill in the filter after
 * creation. Everyone else is asked to treat the filter as immutable and use the appropriate
 * FilterFactory2 creation method.
 *
 * @author Justin Deoliveira (Boundless)
 */
public abstract class BinaryComparisonAbstract extends AbstractFilter implements MultiValuedFilter {

    protected Expression expression1;
    protected Expression expression2;

    boolean matchingCase;

    /** No argument constructor for use by SAX parsers. */
    protected BinaryComparisonAbstract() {
        this(null, null);
    }

    /** Immutable constructor for use by FilterFactory2 */
    protected BinaryComparisonAbstract(Expression expression1, Expression expression2) {
        this(expression1, expression2, true);
    }
    /** Immutable constructor for use by FilterFactory2 */
    protected BinaryComparisonAbstract(
            Expression expression1, Expression expression2, boolean matchingCase) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.matchingCase = matchingCase;
    }

    public Expression getExpression1() {
        return expression1;
    }

    public void setExpression1(Expression expression) {
        this.expression1 = expression;
    }

    public Expression getExpression2() {
        return expression2;
    }

    public void setExpression2(Expression expression) {
        this.expression2 = expression;
    }

    public boolean isMatchingCase() {
        return matchingCase;
    }

    public MatchAction getMatchAction() {
        return MatchAction.ANY; // default
    }
    /**
     * Convenience method which evaluates the expressions and trys to align the values to be of the
     * same type.
     *
     * <p>If the values can not be aligned, the original values are returned.
     */
    protected Object[] eval(Object object) {
        Object v1 = eval(getExpression1(), object);
        Object v2 = eval(getExpression2(), object);

        return eval(v1, v2);
    }

    /**
     * Convenience method which evaluates the expressions and trys to align the values to be of the
     * same type.
     *
     * <p>If the values can not be aligned, the original values are returned.
     */
    protected Object[] eval(Object v1, Object v2) {
        if (v1 != null && v2 != null) {
            // try to convert so that values are of same type
            if (v1.getClass().equals(v2.getClass())) {
                // nothing to do
                return new Object[] {v1, v2};
            }

            // try safe conversions
            Hints hints = new Hints(ConverterFactory.SAFE_CONVERSION, Boolean.TRUE);
            Object o = Converters.convert(v2, v1.getClass(), hints);
            if (o != null) {
                return new Object[] {v1, o};
            }
            // try the other way
            o = Converters.convert(v1, v2.getClass(), hints);
            if (o != null) {
                return new Object[] {o, v2};
            }

            // unsafe conversions
            hints.put(ConverterFactory.SAFE_CONVERSION, Boolean.FALSE);
            o = Converters.convert(v2, v1.getClass(), hints);
            if (o != null) {
                return new Object[] {v1, o};
            }
            o = Converters.convert(v1, v2.getClass(), hints);
            if (o != null) {
                return new Object[] {o, v2};
            }
        }

        return new Object[] {v1, v2};
    }

    /**
     * Wraps an object in a Comparable.
     *
     * @param value The original value.
     * @return A comparable
     */
    protected final Comparable comparable(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Comparable) {
            return (Comparable) value;
        } else {
            return String.valueOf(value);
        }
    }
}
