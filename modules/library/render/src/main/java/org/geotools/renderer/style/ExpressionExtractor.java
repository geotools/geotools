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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Helper class that allows the extraction of CQL expressions out of a plain
 * text string using special separators. Parsing rules are:
 * <ul>
 * <li>whatever is between <code>${</code> and <code>}</code> is considered
 * a CQL expression</li>
 * <li><code>$</code> and <code>}</code> can be used stand alone only
 * escaped with <code>\</code> (e.g. <code>\$</code> and <code>\}</code>)</li>
 * <li><code>\</code> can be used stand alone only escaped with another
 * <code>\</code></li> (e.g. <code>\\</code>)
 * </ul>
 * 
 * Examples of valid expressions:
 * <ul>
 * <li>"one two three \} \$ \\" (simple literal with special chars escaped)</li>
 * <li>"My name is ${name}" (a simple attribute reference)</li>
 * <li>"Hi this is ${(intAtt + 2) * 10}" (cql using attribute and math expressions)</li>
 * </ul>
 * 
 * Examples of non valid expressions:
 * <ul>
 * <li>"bla ${myAttName" (unclosed expression section)</li>
 * <li>"bla } bla" (<code>}</code> is reserved, should have been escaped)</li>
 * 
 * 
 * @author Andrea Aime - TOPP
 * 
 *
 * @source $URL$
 */
public class ExpressionExtractor {
    static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    
    /**
     * Parses the original string and returns an array or parsed expressions, in
     * particular, the result of parsing each embedded cql expression and string
     * literals in between the cql expressions, in the order they appear in the
     * original string
     * 
     * @param expression
     * @return
     */
    static List<Expression> splitCqlExpressions(String expression) {
        boolean inCqlExpression = false;
        List<Expression> result = new ArrayList<Expression>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            final char curr = expression.charAt(i);
            final boolean last = (i == expression.length() - 1);
            final char next = last ? 0 : expression.charAt(i + 1);
            
            if(curr == '\\') {
                if(last) 
                    throw new IllegalArgumentException("Unescaped \\ at position " + (i + 1));

                if(next == '\\')
                    sb.append('\\');
                else if(next == '$')
                    sb.append('$');
                else if(next == '}')
                    sb.append('}');
                else
                    throw new IllegalArgumentException("Unescaped \\ at position " + (i + 1));
                
                // skip the next character
                i++;
            } else if(curr == '$') {
                if(last || next != '{') 
                    throw new IllegalArgumentException("Unescaped $ at position " + (i + 1));
                if(inCqlExpression)
                    throw new IllegalArgumentException("Already found a ${ sequence before the one at " + (i + 1));
                
                // if we extracted a literal in between two expressions, add it to the result
                if(sb.length() > 0) {
                    result.add(ff.literal(sb.toString()));
                    sb.setLength(0);
                }
                
                // mark the beginning and skip the next character
                inCqlExpression = true;
                i++;
            } else if(curr == '}') {
                if(!inCqlExpression)
                    throw new IllegalArgumentException("Already found a ${ sequence before the one at " + (i + 1));
                
                if(sb.length() == 0)
                    throw new IllegalArgumentException("Invalid empty cql expression ${} at " + (i - 1));
                
                try {
                    result.add(ECQL.toExpression(sb.toString()));
                    sb.setLength(0);
                } catch(CQLException e) {
                    throw new IllegalArgumentException("Invalid cql expression '" + sb + "'", e);
                }
                inCqlExpression = false;
            } else {
                sb.append(curr);
            }
        }
        
        // when done, if we are still in a CQL expression, it means it hasn't been closed
        if(inCqlExpression) {
            throw new IllegalArgumentException("Unclosed CQL expression '" + sb + "'");
        } else if(sb.length() > 0){
            result.add(ff.literal(sb.toString()));
        }
        return result;
    }

    /**
     * Given an expression list will catenate it using 
     * @param expression
     * @return
     */
    static Expression catenateExpressions(List<Expression> expressions) {
        if(expressions == null || expressions.size() == 0)
            throw new IllegalArgumentException("You should provide at least one expression in the list");
        
        Expression result = expressions.get(0);
        for (int i = 1; i < expressions.size(); i++) {
            result = ff.function("strConcat", result, expressions.get(i));
        }
        
        return result;
    }

    public static Expression extractCqlExpressions(String expression) {
        return catenateExpressions(splitCqlExpressions(expression));
    }
}
