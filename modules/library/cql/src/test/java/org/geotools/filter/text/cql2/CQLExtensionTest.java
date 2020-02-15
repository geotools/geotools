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
package org.geotools.filter.text.cql2;

import java.util.List;
import org.geotools.filter.function.FilterFunction_relatePattern;
import org.geotools.filter.function.FilterFunction_strConcat;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * Test the extensions in CQL.
 *
 * <p>We adds some extension to Common CQL thinking in convenient uses. In the future we could have
 * different dialects of CQL. That will required extend the interface to allow selecting the dialect
 * to use.
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.5
 * @version Revision: 1.9
 */
public class CQLExtensionTest {
    private static final String DELIMITER = ";";

    /** An INCLUDE token is parsed as {@link Filter#INCLUDE} */
    @Test
    public void testIncludeFilter() throws Exception {
        Filter filter = CQL.toFilter("INCLUDE");
        Assert.assertNotNull(filter);
        Assert.assertTrue(Filter.INCLUDE.equals(filter));

        filter = CQL.toFilter("INCLUDE and a < 1");
        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsLessThan);

        filter = CQL.toFilter("INCLUDE or a < 1");
        Assert.assertNotNull(filter);
        Assert.assertTrue(Filter.INCLUDE.equals(filter));
    }

    /** An EXCLUDE token is parsed as {@link Filter#EXCLUDE} */
    @Test
    public void testExcludeFilter() throws Exception {
        Filter filter = CQL.toFilter("EXCLUDE");
        Assert.assertNotNull(filter);
        Assert.assertTrue(Filter.EXCLUDE.equals(filter));

        filter = CQL.toFilter("EXCLUDE and a < 1");
        Assert.assertNotNull(filter);
        Assert.assertTrue(Filter.EXCLUDE.equals(filter));

        filter = CQL.toFilter("EXCLUDE or a < 1");
        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsLessThan);
    }

    /**
     * Simple test for sequence of search conditions with only one filter [*]
     *
     * <p>
     *
     * <pre>
     * &lt;SequenceOfSearchConditions &gt; ::=
     *          &lt;search condition&gt; [*]
     *     |    &lt;SequenceOfSearchConditions&gt; ; &lt;search condition&gt;
     *
     * </pre>
     *
     * <p>
     */
    @Test
    public void testSequenceOfSearchConditionsWithOneFilter() throws Exception {
        String valueWithDelimiter = "text" + DELIMITER + "with" + DELIMITER + "delimiter";
        final String singleFilterStr = "attr3 = '" + valueWithDelimiter + "'";
        List<Filter> filters = CQL.toFilterList(singleFilterStr);

        Assert.assertNotNull(filters);
        Assert.assertEquals(1, filters.size());
        Assert.assertTrue(filters.get(0) instanceof PropertyIsEqualTo);

        PropertyIsEqualTo filter = (PropertyIsEqualTo) filters.get(0);
        Assert.assertEquals("attr3", ((PropertyName) filter.getExpression1()).getPropertyName());
        Assert.assertEquals(valueWithDelimiter, ((Literal) filter.getExpression2()).getValue());
    }

    /**
     * Simple test for sequence of search conditions with only one filter [*]
     *
     * <p>
     *
     * <pre>
     * &lt;SequenceOfSearchConditions &gt; ::=
     *          &lt;search condition&gt;
     *     |    &lt;SequenceOfSearchConditions&gt; ; &lt;search condition&gt; [*]
     *
     * </pre>
     *
     * <p>Sample: attr1 > 5;attr2 between 1 and 7;attr3
     */
    @Test
    public void testSequenceOfSearchConditionsWithManyFilters() throws Exception {
        String valueWithDelimiter = "text" + DELIMITER + "with" + DELIMITER + "delimiter";

        // "attr1 > 5; attr2 between 1 and 7; attr3 = 'text;with;delimiter
        final String filterListStr =
                "attr1 > 5"
                        + DELIMITER
                        + "attr2 between 1 and 7"
                        + DELIMITER
                        + "attr3 = '"
                        + valueWithDelimiter
                        + "'";

        List<Filter> filters = CQL.toFilterList(filterListStr);
        Assert.assertNotNull(filters);
        Assert.assertEquals(3, filters.size());
        Assert.assertTrue(filters.get(0) instanceof PropertyIsGreaterThan);
        Assert.assertTrue(filters.get(1) instanceof PropertyIsBetween);
        Assert.assertTrue(filters.get(2) instanceof PropertyIsEqualTo);

        PropertyIsGreaterThan gt = (PropertyIsGreaterThan) filters.get(0);
        Assert.assertEquals("attr1", ((PropertyName) gt.getExpression1()).getPropertyName());
        Assert.assertEquals(Long.valueOf(5), ((Literal) gt.getExpression2()).getValue());

        PropertyIsBetween btw = (PropertyIsBetween) filters.get(1);
        Assert.assertEquals("attr2", ((PropertyName) btw.getExpression()).getPropertyName());
        Assert.assertEquals(Long.valueOf(1), ((Literal) btw.getLowerBoundary()).getValue());
        Assert.assertEquals(Long.valueOf(7), ((Literal) btw.getUpperBoundary()).getValue());

        PropertyIsEqualTo equals = (PropertyIsEqualTo) filters.get(2);
        Assert.assertEquals("attr3", ((PropertyName) equals.getExpression1()).getPropertyName());
        Assert.assertEquals(valueWithDelimiter, ((Literal) equals.getExpression2()).getValue());
    }

    /** An empty filter int the constraints list shall be parsed as {@link Filter#INCLUDE} */
    @Test
    public void testParseFilterListWithEmptyFilter() throws Exception {
        String valueWithDelimiter = "text" + DELIMITER + "with" + DELIMITER + "delimiter";

        // "attr1 > 5;INCLUDE;attr3 = 'text;with;delimiter'"
        String filterListStr =
                "attr1 > 5"
                        + DELIMITER
                        + "INCLUDE"
                        + DELIMITER
                        + " attr3 = '"
                        + valueWithDelimiter
                        + "'";
        List<Filter> filters = CQL.toFilterList(filterListStr);
        Assert.assertNotNull(filters);
        Assert.assertEquals(3, filters.size());
        Assert.assertTrue(filters.get(0) instanceof PropertyIsGreaterThan);
        Assert.assertTrue(filters.get(1) instanceof IncludeFilter);
        Assert.assertTrue(filters.get(2) instanceof PropertyIsEqualTo);

        PropertyIsGreaterThan gt = (PropertyIsGreaterThan) filters.get(0);
        Assert.assertEquals("attr1", ((PropertyName) gt.getExpression1()).getPropertyName());
        Assert.assertEquals(Long.valueOf(5), ((Literal) gt.getExpression2()).getValue());

        PropertyIsEqualTo equals = (PropertyIsEqualTo) filters.get(2);
        Assert.assertEquals("attr3", ((PropertyName) equals.getExpression1()).getPropertyName());
        Assert.assertEquals(valueWithDelimiter, ((Literal) equals.getExpression2()).getValue());

        filterListStr =
                "EXCLUDE"
                        + DELIMITER
                        + "INCLUDE"
                        + DELIMITER
                        + "attr3 = '"
                        + valueWithDelimiter
                        + "'";

        filters = CQL.toFilterList(filterListStr);
        Assert.assertTrue(filters.get(0) instanceof ExcludeFilter);
        Assert.assertTrue(filters.get(1) instanceof IncludeFilter);
        Assert.assertTrue(filters.get(2) instanceof PropertyIsEqualTo);
    }
    /** Tests null factory as parameter. */
    @Test
    public void testNullFilterFactory() throws Exception {

        CQL.toFilter("attName > 20", null);

        CQL.toExpression("2+2", null);
    }

    /**
     * Test Function Expression
     *
     * <p>Note: this solves the bug GEOT-1167
     */
    @Test
    public void testFuncitionExpression() throws Exception {
        Expression arg1;
        Expression arg2;
        Expression resultExpr;

        final String cqlExpression = "strConcat(A, B)";

        // simple attribute as argument
        resultExpr = CQL.toExpression(cqlExpression);

        Assert.assertNotNull(resultExpr);
        Assert.assertTrue(resultExpr instanceof Function);

        Function function1 = (Function) resultExpr;
        Assert.assertEquals(2, function1.getParameters().size());

        arg1 = (Expression) function1.getParameters().get(0);
        Assert.assertTrue(arg1 instanceof PropertyName);
        Assert.assertEquals("A", ((PropertyName) arg1).getPropertyName());

        arg2 = (Expression) function1.getParameters().get(1);
        Assert.assertTrue(arg2 instanceof PropertyName);
        Assert.assertEquals("B", ((PropertyName) arg2).getPropertyName());

        // compound attribute as argument
        final String arg1Name = "gmd:aa:bb.gmd:cc.gmd:dd";
        final String arg2Name = "gmd:ee:ff.gmd:gg.gmd:hh";

        final String cqlExpression2 = "strConcat(" + arg1Name + ", " + arg2Name + ")";

        resultExpr = CQL.toExpression(cqlExpression2);

        Assert.assertNotNull(resultExpr);
        Assert.assertTrue(resultExpr instanceof Function);

        Function function = (Function) resultExpr;
        Assert.assertEquals(2, function.getParameters().size());

        arg1 = (Expression) function.getParameters().get(0);
        Assert.assertTrue(arg1 instanceof PropertyName);

        final String arg1Expected = arg1Name.replace('.', '/');
        Assert.assertEquals(arg1Expected, ((PropertyName) arg1).getPropertyName());

        arg2 = (Expression) function.getParameters().get(1);
        Assert.assertTrue(arg2 instanceof PropertyName);

        final String arg2Expected = arg2Name.replace('.', '/');
        Assert.assertEquals(arg2Expected, ((PropertyName) arg2).getPropertyName());
    }

    /**
     * This test the following improvement: GEOT-1169 This is an extension the CQL specification.
     *
     * <pre>
     *  &lt;function&gt; ::= &lt;routine name &gt; &lt;argument list &gt; [*]
     *  &lt;argument list&gt; ::=    [*]
     *       &lt;left paren&gt; [&lt;positional arguments&gt;] &lt;right paren&gt;
     *  &lt;positional arguments&gt; ::=
     *       &lt;argument&gt; [ { &lt;comma&amp;gt &lt;argument&gt; }... ]
     *  &lt;argument&gt;  ::=
     *       &lt;literal&gt;
     *   |   &lt;attribute name&gt;
     *   |   &lt;function&gt;           [*]
     *   |   &lt;binary expression&gt;  [*]
     * </pre>
     */
    @Test
    public void testFunctionComposition() throws Exception {
        String cqlExpression;
        Expression expression;

        // Test 1
        cqlExpression = "strConcat(A, abs(B))";
        expression = CQL.toExpression(cqlExpression);

        // Test 2
        cqlExpression = "strConcat(A, strConcat(B, strConcat(C, '.')))";
        expression = CQL.toExpression(cqlExpression);
        Assert.assertNotNull(expression);
        Assert.assertTrue(expression instanceof Function);

        Function function = (Function) expression;
        Assert.assertEquals(2, function.getParameters().size());

        Expression propertyName = (Expression) function.getParameters().get(0);
        Assert.assertTrue(propertyName instanceof PropertyName);
        Assert.assertEquals("A", ((PropertyName) propertyName).getPropertyName());

        Expression arg2 = (Expression) function.getParameters().get(1);
        Assert.assertTrue(arg2 instanceof Function);

        function = (Function) arg2;
        propertyName = (Expression) function.getParameters().get(0);
        Assert.assertTrue(propertyName instanceof PropertyName);
        Assert.assertEquals("B", ((PropertyName) propertyName).getPropertyName());

        arg2 = (Expression) function.getParameters().get(1);
        Assert.assertTrue(arg2 instanceof Function);

        function = (Function) arg2;
        propertyName = (Expression) function.getParameters().get(0);
        Assert.assertTrue(propertyName instanceof PropertyName);
        Assert.assertEquals("C", ((PropertyName) propertyName).getPropertyName());

        arg2 = (Expression) function.getParameters().get(1);
        Assert.assertTrue(arg2 instanceof Literal);
        Assert.assertEquals(".", ((Literal) arg2).getValue());
    }

    /** complex case Created to analyze http://jira.codehaus.org/browse/GEOT-1655 */
    @Test
    public void testFunctionCompositionComplexCase() throws Exception {

        Expression result =
                CQL.toExpression(
                        "strConcat( strConcat(QS, strConcat('/', RT)), strConcat(strConcat('/', NUMB), strConcat('/', BSUFF)) )");

        assertFunctionCompositionComplex(result);
    }

    @Test
    public void testFunctionCompositionComplexCaseInFilter() throws Exception {

        final String propName = "A";
        Filter result =
                CQL.toFilter(
                        propName
                                + " = strConcat( strConcat(QS, strConcat('/', RT)), strConcat(strConcat('/', NUMB), strConcat('/', BSUFF)) )");

        Assert.assertTrue(result instanceof PropertyIsEqualTo);
        PropertyIsEqualTo eq = (PropertyIsEqualTo) result;

        Expression expr1 = eq.getExpression1();
        Assert.assertTrue(expr1 instanceof PropertyName);
        PropertyName prop = (PropertyName) expr1;
        Assert.assertEquals(propName, prop.getPropertyName());

        Expression expr2 = eq.getExpression2();
        assertFunctionCompositionComplex(expr2);
    }
    /**
     * @param result strConcat( strConcat(QS, strConcat('/', RT)), strConcat(strConcat('/', NUMB),
     *     strConcat('/', BSUFF)) )")
     */
    private void assertFunctionCompositionComplex(final Expression result) {

        Assert.assertTrue(result instanceof Function);

        Function function = (Function) result;
        Assert.assertEquals(2, function.getParameters().size());

        // asserts for strConcat(QS, strConcat('/', RT))
        Expression arg1 = (Expression) function.getParameters().get(0);
        Assert.assertTrue(arg1 instanceof Function);

        Function function1 = (Function) arg1;
        Assert.assertEquals(2, function1.getParameters().size());

        Expression funcion1Arg1 = function1.getParameters().get(0);
        Assert.assertTrue(funcion1Arg1 instanceof PropertyName);
        Assert.assertEquals("QS", ((PropertyName) funcion1Arg1).getPropertyName());

        Expression arg11 = (Expression) function1.getParameters().get(1);
        Assert.assertTrue(arg11 instanceof Function);
        Function function11 = (Function) arg11;

        Expression funcion11Arg1 = (Expression) function11.getParameters().get(0);
        Assert.assertTrue(funcion11Arg1 instanceof Literal);
        Assert.assertEquals("/", ((Literal) funcion11Arg1).getValue());

        // asserts for strConcat(strConcat('/', NUMB), strConcat('/', BSUFF)) )"
        Expression arg2 = (Expression) function.getParameters().get(1);
        Assert.assertTrue(arg2 instanceof Function);
    }

    /**
     * Test for Function Unary Expressions with functions in CQL.
     *
     * <p>
     *
     * <pre>
     *  &lt;unary expression &gt; ::=
     *         &lt;Literal &gt;
     *   |     &lt;Function &gt; [*]
     *   |     &lt;Attribute &gt;
     *   |   ( &lt;Expression &gt;)
     *   |   [ &lt;Expression &gt;]
     * </pre>
     */
    @Test
    public void testUnaryExpressionFunction() throws Exception {
        Filter result;
        Filter expected;
        String cqlUnaryExp;

        cqlUnaryExp = FilterCQLSample.FILTER_WITH_FUNCTION_ABS;
        result = CQL.toFilter(cqlUnaryExp);

        Assert.assertNotNull("filter expected", result);

        expected = FilterCQLSample.getSample(cqlUnaryExp);

        Assert.assertEquals("Equals Functions is expected", expected, result);

        // Key: GEOT-1167 type: BUG
        cqlUnaryExp = FilterCQLSample.FILTER__WITH_FUNCTION_STR_CONCAT;
        result = CQL.toFilter(cqlUnaryExp);
        Assert.assertNotNull("filter expected", result);

        expected = FilterCQLSample.getSample(cqlUnaryExp);

        Assert.assertTrue("Functions", result instanceof PropertyIsEqualTo);
        Assert.assertTrue(
                "Attribute is expecteced",
                ((PropertyIsEqualTo) result).getExpression1() instanceof PropertyName);
        Assert.assertTrue(
                "Attribute is expecteced",
                ((PropertyIsEqualTo) result).getExpression2() instanceof FilterFunction_strConcat);

        // test for improvement Key: GEOT-1168
        cqlUnaryExp = "A = strConcat(B, 'testParam')";
        result = CQL.toFilter(cqlUnaryExp);

        Assert.assertTrue(result instanceof PropertyIsEqualTo);

        Expression expression = ((PropertyIsEqualTo) result).getExpression2();
        Assert.assertNotNull(expression);
        Assert.assertTrue(expression instanceof Function);

        Function function = (Function) expression;
        Assert.assertEquals(2, function.getParameters().size());

        Expression arg1 = (Expression) function.getParameters().get(0);
        Expression arg2 = (Expression) function.getParameters().get(1);
        Assert.assertTrue(arg1 instanceof PropertyName);
        Assert.assertTrue(arg2 instanceof Literal);

        Assert.assertEquals("B", ((PropertyName) arg1).getPropertyName());
        Assert.assertEquals("testParam", ((Literal) arg2).getValue());
    }

    @Test
    public void relateFunction() throws CQLException {

        Filter resultFilter =
                ECQL.toFilter(
                        "ATTR = relatePattern(the_geom, 'LINESTRING (27.3 37, 27.3 37.6)', '**1****') ");

        Assert.assertTrue(resultFilter instanceof PropertyIsEqualTo);

        // relate function in an equal predicate
        resultFilter =
                ECQL.toFilter(
                        "relatePattern(the_geom, 'LINESTRING (27.3 37, 27.3 37.6)', '**1****') = TRUE");

        Assert.assertTrue(resultFilter instanceof PropertyIsEqualTo);

        // relate function to expression
        Expression resultExpression =
                ECQL.toExpression(
                        "relatePattern(the_geom, 'LINESTRING (27.3 37, 27.3 37.6)', '**1****') ");

        Assert.assertTrue(resultExpression instanceof FilterFunction_relatePattern);
    }
}
