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
package org.geootols.filter.text.cql_2;

import java.util.Iterator;
import java.util.List;
import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.ExpressionToText;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.util.factory.Hints;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * <b>OGC API CQL2</b>. This class presents the operations available to parse the CQL2 language and
 * generates the correspondent filter.
 *
 * <p>
 */
public class CQL2 {

    private CQL2() {
        // do nothing, private constructor
        // to indicate it is a pure utility class
    }

    /**
     * Parses the input string in ECQL format into a Filter, using the systems default FilterFactory
     * implementation.
     *
     * @param ecqlPredicate a string containing a query predicate in ECQL format.
     * @return a {@link Filter} equivalent to the constraint specified in <code>ecqlPredicate</code>
     *     .
     */
    public static Filter toFilter(final String ecqlPredicate) throws CQLException {
        Filter filter = CQL2.toFilter(ecqlPredicate, null);

        return filter;
    }

    /**
     * Parses the input string in ECQL format into a Filter, using the provided FilterFactory.
     *
     * @param ecqlPredicate a string containing a query predicate in ECQL format.
     * @param filterFactory the {@link FilterFactory} to use for the creation of the Filter. If it
     *     is null the method finds the default implementation.
     * @return a {@link Filter} equivalent to the constraint specified in <code>Predicate</code>.
     */
    public static Filter toFilter(final String ecqlPredicate, final FilterFactory filterFactory)
            throws CQLException {

        ECQLCompilerFactory compilerFactory = new ECQLCompilerFactory();
        Filter result = CompilerUtil.parseFilter(ecqlPredicate, compilerFactory, filterFactory);

        return result;
    }

    /**
     * Parses the input string in ECQL format into an Expression, using the systems default {@link
     * FilterFactory} implementation.
     *
     * @param ecqlExpression a string containing an ECQL expression.
     * @return a {@link Expression} equivalent to the one specified in <code>ecqlExpression</code>.
     */
    public static Expression toExpression(String ecqlExpression) throws CQLException {
        return toExpression(ecqlExpression, null);
    }

    /**
     * Parses the input string in ECQL format and makes the correspondent Expression , using the
     * provided FilterFactory.
     *
     * @param ecqlExpression a string containing a ECQL expression.
     * @param filterFactory the {@link FilterFactory} to use for the creation of the Expression. If
     *     it is null the method finds the default implementation.
     * @return a {@link Filter} equivalent to the constraint specified in <code>ecqlExpression
     *     </code>.
     */
    public static Expression toExpression(
            final String ecqlExpression, final FilterFactory filterFactory) throws CQLException {

        ECQLCompilerFactory compilerFactory = new ECQLCompilerFactory();

        Expression expression =
                CompilerUtil.parseExpression(ecqlExpression, compilerFactory, filterFactory);

        return expression;
    }

    /**
     * Parses the input string, which has to be a list of ECQL predicates separated by "<code>;
     * </code>" into a {@link List} of {@link Filter}, using the provided FilterFactory.
     *
     * @param ecqlSequencePredicate a list of ECQL predicates separated by "<code>;</code>"
     * @return a List of {@link Filter}, one for each input ECQL statement
     */
    public static List<Filter> toFilterList(final String ecqlSequencePredicate)
            throws CQLException {

        return toFilterList(ecqlSequencePredicate, null);
    }

    /**
     * Parses the input string, which has to be a list of ECQL predicates separated by "<code>;
     * </code>" into a {@link List} of {@link Filter}, using the provided FilterFactory.
     *
     * @param ecqlSequencePredicate a ECQL predicate sequence
     * @param filterFactory the factory used to make the filters
     * @return a List of {@link Filter}, one for each input ECQL statement
     */
    public static List<Filter> toFilterList(
            final String ecqlSequencePredicate, FilterFactory filterFactory) throws CQLException {

        ECQLCompilerFactory compilerFactory = new ECQLCompilerFactory();

        List<Filter> filters =
                CompilerUtil.parseFilterList(ecqlSequencePredicate, compilerFactory, filterFactory);

        return filters;
    }

    /**
     * Generates the ecql predicates associated to the {@link List} of {@link Filter}s object.
     *
     * @return ecql predicates separated by ";"
     */
    public static String toCQL(List<Filter> filterList) {
        FilterToCQL2 toECQL = new FilterToCQL2(isEwktEncodingEnabled());

        StringBuilder output = new StringBuilder();
        Iterator<Filter> iter = filterList.iterator();
        while (iter.hasNext()) {
            Filter filter = iter.next();
            filter.accept(toECQL, output);
            if (iter.hasNext()) {
                output.append("; ");
            }
        }
        return output.toString();
    }

    /**
     * Generates the ecql predicate associated to the {@link Filter} object.
     *
     * @return ecql predicate
     */
    public static String toCQL(Filter filter) {
        FilterToCQL2 toCQL = new FilterToCQL2(isEwktEncodingEnabled());

        StringBuilder output = (StringBuilder) filter.accept(toCQL, new StringBuilder());

        return output.toString();
    }

    /** Convenience method checking if EWKT encoding should be enabled in ECQL, or not */
    public static boolean isEwktEncodingEnabled() {
        Object value = Hints.getSystemDefault(Hints.ENCODE_EWKT);
        if (value == null) {
            return true;
        }
        return Boolean.TRUE.equals(value);
    }

    /**
     * Generates the expression text associated to the {@link Expression} object.
     *
     * @return expression as text
     */
    public static String toCQL(Expression expression) {
        ExpressionToText toECQL = new ExpressionToText(isEwktEncodingEnabled());

        StringBuilder output = (StringBuilder) expression.accept(toECQL, new StringBuilder());

        return output.toString();
    }
}
