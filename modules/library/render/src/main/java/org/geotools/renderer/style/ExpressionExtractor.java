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
package org.geotools.renderer.style;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.SoftValueHashMap;

/**
 * Helper class that allows the extraction of CQL expressions out of a plain text string using special separators.
 * Parsing rules are:
 *
 * <ul>
 *   <li>whatever is between <code>${</code> and <code>}</code> is considered a CQL expression
 *   <li><code>$</code> and <code>}</code> can be used stand alone only escaped with <code>\</code> (e.g. <code>\$
 *       </code> and <code>\}</code>)
 *   <li><code>\</code> can be used stand alone only escaped with another <code>\</code> (e.g. <code>\\</code>)
 * </ul>
 *
 * Examples of valid expressions:
 *
 * <ul>
 *   <li>"one two three \} \$ \\" (simple literal with special chars escaped)
 *   <li>"My name is ${name}" (a simple attribute reference)
 *   <li>"Hi this is ${(intAtt + 2) * 10}" (cql using attribute and math expressions)
 * </ul>
 *
 * Examples of non valid expressions:
 *
 * <ul>
 *   <li>"bla ${myAttName" (unclosed expression section)
 *   <li>"bla } bla" (<code>}</code> is reserved, should have been escaped)
 *
 * @author Andrea Aime - TOPP
 */
public class ExpressionExtractor {
    static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    static final int EXPRESSION_CACHE_HARD_REFERENCES = 1000;
    // cache for the CQL expressions (thread safe)
    static final SoftValueHashMap<String, Expression> EXPRESSION_CACHE =
            new SoftValueHashMap<>(EXPRESSION_CACHE_HARD_REFERENCES);

    /**
     * Parses the original string and returns an array or parsed expressions, in particular, the result of parsing each
     * embedded cql expression and string literals in between the cql expressions, in the order they appear in the
     * original string
     */
    static List<Expression> splitCqlExpressions(String expression) {
        boolean inCqlExpression = false;
        List<Expression> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            final char curr = expression.charAt(i);
            final boolean last = i == expression.length() - 1;
            final char next = last ? 0 : expression.charAt(i + 1);

            if (curr == '\\') {
                if (last) throw new IllegalArgumentException("Unescaped \\ at position " + (i + 1));

                if (next == '\\') sb.append('\\');
                else if (next == '$') sb.append('$');
                else if (next == '}') sb.append('}');
                else throw new IllegalArgumentException("Unescaped \\ at position " + (i + 1));

                // skip the next character
                i++;
            } else if (curr == '$') {
                if (last || next != '{') throw new IllegalArgumentException("Unescaped $ at position " + (i + 1));
                if (inCqlExpression)
                    throw new IllegalArgumentException("Already found a ${ sequence before the one at " + (i + 1));

                // if we extracted a literal in between two expressions, add it to the result
                if (sb.length() > 0) {
                    result.add(ff.literal(sb.toString()));
                    sb.setLength(0);
                }

                // mark the beginning and skip the next character
                inCqlExpression = true;
                i++;
            } else if (curr == '}') {
                if (!inCqlExpression)
                    throw new IllegalArgumentException("Already found a ${ sequence before the one at " + (i + 1));

                if (sb.length() == 0)
                    throw new IllegalArgumentException("Invalid empty cql expression ${} at " + (i - 1));

                try {
                    result.add(ECQL.toExpression(sb.toString()));
                    sb.setLength(0);
                } catch (CQLException e) {
                    throw new IllegalArgumentException("Invalid cql expression '" + sb + "'", e);
                }
                inCqlExpression = false;
            } else {
                sb.append(curr);
            }
        }

        // when done, if we are still in a CQL expression, it means it hasn't been closed
        if (inCqlExpression) {
            throw new IllegalArgumentException("Unclosed CQL expression '" + sb + "'");
        } else if (sb.length() > 0) {
            result.add(ff.literal(sb.toString()));
        }
        return result;
    }

    /** Given an expression list will create an expression concatenating them. */
    static Expression catenateExpressions(List<Expression> expressions) {
        if (expressions == null || expressions.isEmpty())
            throw new IllegalArgumentException("You should provide at least one expression in the list");

        if (expressions.size() == 1) {
            return expressions.get(0);
        } else {
            return ff.function("Concatenate", expressions.toArray(new Expression[] {}));
        }
    }

    /**
     * Builds a CQL expression equivalent to the specified string, see class javadocs for rules on how to build the
     * expression in string form
     */
    public static Expression extractCqlExpressions(String expression) {
        Expression result = EXPRESSION_CACHE.get(expression);
        if (result == null) {
            result = catenateExpressions(splitCqlExpressions(expression));
            EXPRESSION_CACHE.put(expression, result);
        }
        return result;
    }

    /** Clears the CQL expression cache */
    public static void cleanExpressionCache() {
        EXPRESSION_CACHE.clear();
    }
}
