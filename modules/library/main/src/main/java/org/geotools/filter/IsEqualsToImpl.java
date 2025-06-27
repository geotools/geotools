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

import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;

/** @author jdeolive TODO: rename this class to IsEqualToImpl */
public class IsEqualsToImpl extends MultiCompareFilterImpl implements PropertyIsEqualTo {

    public static final Hints SAFE_CONVERSION_HINTS = new Hints(ConverterFactory.SAFE_CONVERSION, true);

    protected IsEqualsToImpl(Expression expression1, Expression expression2) {
        this(expression1, expression2, true);
    }

    protected IsEqualsToImpl(Expression expression1, Expression expression2, MatchAction matchAction) {
        this(expression1, expression2, true, matchAction);
    }

    protected IsEqualsToImpl(Expression expression1, Expression expression2, boolean matchCase) {
        super(expression1, expression2, matchCase);
    }

    protected IsEqualsToImpl(
            Expression expression1, Expression expression2, boolean matchCase, MatchAction matchAction) {
        super(expression1, expression2, matchCase, matchAction);
    }

    @Override
    public boolean evaluateInternal(Object value1, Object value2) {
        if (value1 == value2) {
            // Includes the (value1 == null && value2 == null) case.
            return true;
        }
        if (value1 == null || value2 == null) {
            // No need to check for (value2 != null) or (value1 != null).
            // If they were null, the previous check would have caugh them.
            return false;
        }
        /*
         * For the following "if (value1.equals(value2))" line, we do not check for
         * "value1.getClass().equals(value2.getClass())" because:
         *
         * 1) It is usually done inside the 'Object.equals(Object)' method, so our check would be
         * redundant. 2) It would lead to a subtile issue with the 0.0 floating point value, as
         * explained below.
         *
         * Even if 'value1' and 'value2' are of the same class, we can not implement this code as
         * "return value1.equals(value2)" because a 'false' value doesn't means that the values are
         * numerically different. Float and Double.equals(Object) return 'false' when comparing +0.0
         * with -0.0, while +0.0 == -0.0 returns 'true'. We would need to make a special case for
         * them. It is better to fallback on the more generic code following the "if" block in order
         * to ensure consistent behavior.
         */
        if (!matchingCase && value1 instanceof String && value2 instanceof String) {
            return ((String) value1).equalsIgnoreCase((String) value2);
        } else if (value1.equals(value2)) {
            return true;
        } else if (value1.getClass().equals(value2.getClass())) {
            return false;
        }

        // if we are doing delayed evaluation of a literal, try conversions to the actual type
        if (expression1 instanceof Literal && !(expression2 instanceof Literal)) {
            Object v1 = Converters.convert(value1, value2.getClass());
            if (v1 != null && v1.equals(value2)) return true;
        } else if (expression2 instanceof Literal && !(expression1 instanceof Literal)) {
            Object v2 = Converters.convert(value2, value1.getClass());
            if (v2 != null && v2.equals(value1)) return true;
        }

        // if one side is string and the other is not, try to convert the string to non-string
        if (value1 instanceof String && !(value2 instanceof String)) {
            Object v1 = Converters.convert(value1, value2.getClass(), SAFE_CONVERSION_HINTS);
            if (v1 != null && v1.equals(value2)) return true;
        } else if (value2 instanceof String && !(value1 instanceof String)) {
            Object v2 = Converters.convert(value2, value1.getClass(), SAFE_CONVERSION_HINTS);
            if (v2 != null && v2.equals(value1)) return true;
        }

        // try the usual conversions then
        final boolean isNumeric1 = value1 instanceof Number;
        final boolean isNumeric2 = value2 instanceof Number;
        if (isNumeric1 && isNumeric2
                || isNumeric1 && value2 instanceof CharSequence
                || isNumeric2 && value1 instanceof CharSequence) {
            // Numeric comparison, try to parse strings to numbers and do proper
            // comparison between, say, 5 and 5.0 (Long and Double would say
            // they are different)
            final Number n1, n2;
            try {
                n1 = isNumeric1 ? (Number) value1 : parseToNumber(value1.toString());
                n2 = isNumeric2 ? (Number) value2 : parseToNumber(value2.toString());
            } catch (NumberFormatException e) {
                // The string cannot be cast to number, so it's different.
                return false;
            }
            final double fp1 = n1.doubleValue();
            final double fp2 = n2.doubleValue();
            final long lg1, lg2; // 'lg2' will not be initialized if not needed.
            if (fp1 == (lg1 = n1.longValue()) && fp2 == (lg2 = n2.longValue())) {
                // Compares the values as 'long' if and only if the 'double' values
                // do not contains any additional informations.
                return lg1 == lg2;
            } else {
                // Floating point comparaisons. Note: we do NOT use Double.equals or
                // Double.doubleToLongBits because we want to consider +0.0 == -0.0.
                // The Double.equals method would returns 'false' in the above case.
                return fp1 == fp2 || Double.isNaN(fp1) && Double.isNaN(fp2);
            }
        } else if (!isMatchingCase()) {
            // fall back to string and check the case insensitive flag
            String s1 = Converters.convert(value1, String.class);
            String s2 = Converters.convert(value2, String.class);

            return s1.equalsIgnoreCase(s2);
        }

        return false;
    }

    /**
     * Parses the specified string as a {@link Long} or a {@link Double} value.
     *
     * @param value The string to parse.
     * @return The value as a number.
     * @throws NumberFormatException if the string can't be parsed.
     */
    private static Number parseToNumber(final String value) throws NumberFormatException {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return Double.valueOf(value);
        }
    }

    @Override
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
}
