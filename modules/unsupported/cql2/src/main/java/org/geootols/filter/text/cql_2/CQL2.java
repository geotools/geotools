/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.ExpressionToText;
import org.geotools.filter.text.cql2.CQLException;
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
     * Parses the input string in CQL2 format into a Filter, using the systems default FilterFactory
     * implementation.
     *
     * @param cql2Predicate a string containing a query predicate in CQL format.
     * @return a {@link Filter} equivalent to the constraint specified in <code>CQL2</code> .
     */
    public static Filter toFilter(final String cql2Predicate) throws CQLException {
        Filter filter = CQL2.toFilter(cql2Predicate, null);

        return filter;
    }

    /**
     * Parses the input string in CQL2 format into a Filter, using the provided FilterFactory.
     *
     * @param cql2Predicate a string containing a query predicate in CQL2 format.
     * @param filterFactory the {@link FilterFactory} to use for the creation of the Filter. If it
     *     is null the method finds the default implementation.
     * @return a {@link Filter} equivalent to the constraint specified in <code>Predicate</code>.
     */
    public static Filter toFilter(final String cql2Predicate, final FilterFactory filterFactory)
            throws CQLException {

        CQL2CompilerFactory compilerFactory = new CQL2CompilerFactory();
        Filter result = CompilerUtil.parseFilter(cql2Predicate, compilerFactory, filterFactory);

        return result;
    }

    /**
     * Parses the input string in CQL2 format into an Expression, using the systems default {@link
     * FilterFactory} implementation.
     *
     * @param cql2Expression a string containing an CQL2 expression.
     * @return a {@link Expression} equivalent to the one specified in <code>cql2Expression</code>.
     */
    public static Expression toExpression(String cql2Expression) throws CQLException {
        return toExpression(cql2Expression, null);
    }

    /**
     * Parses the input string in CQL2 format and makes the correspondent Expression , using the
     * provided FilterFactory.
     *
     * @param cql2Expression a string containing a CQL2 expression.
     * @param filterFactory the {@link FilterFactory} to use for the creation of the Expression. If
     *     it is null the method finds the default implementation.
     * @return a {@link Filter} equivalent to the constraint specified in <code>cql2Expression
     *     </code>.
     */
    public static Expression toExpression(
            final String cql2Expression, final FilterFactory filterFactory) throws CQLException {

        CQL2CompilerFactory compilerFactory = new CQL2CompilerFactory();

        Expression expression =
                CompilerUtil.parseExpression(cql2Expression, compilerFactory, filterFactory);

        return expression;
    }

    /**
     * Generates the CQL2 predicate associated to the {@link Filter} object.
     *
     * @return CQL2 predicate
     */
    public static String toCQL2(Filter filter) {
        FilterToCQL2 toCQL = new FilterToCQL2();

        StringBuilder output = (StringBuilder) filter.accept(toCQL, new StringBuilder());

        return output.toString();
    }

    /**
     * Generates the expression text associated to the {@link Expression} object.
     *
     * @return expression as text
     */
    public static String toCQL2(Expression expression) {
        ExpressionToText toCQL2 = new CQL2ExpressionToText();

        StringBuilder output = (StringBuilder) expression.accept(toCQL2, new StringBuilder());

        return output.toString();
    }
}
