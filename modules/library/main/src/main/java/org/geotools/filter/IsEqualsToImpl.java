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

import org.geotools.util.Converters;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * 
 * @author jdeolive TODO: rename this class to IsEqualToImpl
 *
 *
 * @source $URL$
 */
public class IsEqualsToImpl extends CompareFilterImpl implements PropertyIsEqualTo {

    protected IsEqualsToImpl(org.opengis.filter.FilterFactory factory) {
        this(factory, null, null);
    }

    protected IsEqualsToImpl(org.opengis.filter.FilterFactory factory, Expression expression1, Expression expression2) {
        this(factory, expression1, expression2, true);
    }

    protected IsEqualsToImpl(org.opengis.filter.FilterFactory factory, Expression expression1, Expression expression2,
            boolean matchCase) {
        super(factory, expression1, expression2, matchCase);

        // backwards compat with old type system
        this.filterType = COMPARE_EQUALS;
    }

    // @Override
    public boolean evaluate(Object feature) {
        final Object value1 = eval(expression1, feature);
        final Object value2 = eval(expression2, feature);

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
        if (value1.equals(value2)) {
            return true;
        }
        
        // if we are doing delayed evaluation of a literal, try conversions to the actual type
        if(expression1 instanceof Literal && !(expression2 instanceof Literal)) {
            Object v1 = Converters.convert(value1, value2.getClass());
            if(v1 != null && value2.equals(v1))
                return true;
        } else if (expression2 instanceof Literal && !(expression1 instanceof Literal)) {
            Object v2 = Converters.convert(value2, value1.getClass());
            if(v2 != null && value1.equals(v2))
                return true;
        }
        
        // try the usual conversions then
        final boolean isNumeric1 = (value1 instanceof Number);
        final boolean isNumeric2 = (value2 instanceof Number);
        if ((isNumeric1 && isNumeric2) || (isNumeric1 && (value2 instanceof CharSequence))
                || (isNumeric2 && (value1 instanceof CharSequence))) {
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
            if (fp1 == (double) (lg1 = n1.longValue()) && fp2 == (double) (lg2 = n2.longValue())) {
                // Compares the values as 'long' if and only if the 'double' values
                // do not contains any additional informations.
                return lg1 == lg2;
            } else {
                // Floating point comparaisons. Note: we do NOT use Double.equals or
                // Double.doubleToLongBits because we want to consider +0.0 == -0.0.
                // The Double.equals method would returns 'false' in the above case.
                return (fp1 == fp2) || (Double.isNaN(fp1) && Double.isNaN(fp2));
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
     * @param value
     *            The string to parse.
     * @return The value as a number.
     * @throws NumberFormatException
     *             if the string can't be parsed.
     */
    private static Number parseToNumber(final String value) throws NumberFormatException {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return Double.valueOf(value);
        }
    }

    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
}
