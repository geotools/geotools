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
package org.geotools.filter.text.ecql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.awt.*;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.IsNullImpl;
import org.geotools.filter.function.FilterFunction_relatePattern;
import org.geotools.filter.function.PropertyExistsFunction;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.temporal.Before;

/**
 * ECQL Test Case.
 *
 * <p>Test for the implementation of {@link ECQL} facade. This facade is the package interface and
 * provides access to all functionalities of the ECQL parser.
 *
 * <p>The samples are intended as an overview of <b>ECQL language </b> scope.
 *
 * <p>The test method provide a basic test for each kind of ECQL predicates. The rest of test case
 * classes included in this package implements a detailed test for each predicate.
 *
 * @author Jody Garnett
 * @author Mauricio Pazos (Axios Engineering)
 * @version Revision: 1.9
 * @since 2.6
 */
public final class ECQLTest {

    @Test
    public void include() throws CQLException {
        Filter filter = assertFilter("INCLUDE", IncludeFilter.class);
        assertSame(Filter.INCLUDE, filter);
    }

    @Test
    public void exclude() throws CQLException {
        Filter filter = assertFilter("EXCLUDE", ExcludeFilter.class);
        assertSame(Filter.EXCLUDE, filter);
    }

    /**
     * Between predicate sample
     *
     * @see ECQLBetweenPredicateTest
     */
    @Test
    public void betweenPredicate() throws CQLException {
        assertFilter("ATTR1 BETWEEN 10 AND 20", PropertyIsBetween.class);
    }

    /**
     * Equals predicate sample
     *
     * @see ECQLComparisonPredicateTest
     */
    @Test
    public void comparisonPredicate() throws Exception {
        assertFilter("POP_RANK > 6", PropertyIsGreaterThan.class);
        assertFilter("Area(the_geom) < 3000", PropertyIsLessThan.class);
    }

    @Test
    public void relateGeoOperation() throws CQLException {
        PropertyIsEqualTo filter =
                assertFilter(
                        "RELATE(geometry,LINESTRING (-134.921387 58.687767, -135.303391 59.092838),T*****FF*)",
                        PropertyIsEqualTo.class);

        Assert.assertTrue(
                "Relate Pattern Function was expected",
                filter.getExpression1() instanceof FilterFunction_relatePattern);

        Assert.assertTrue("Literal TRUE was expected", filter.getExpression2() instanceof Literal);
    }

    /**
     * GeoOperation predicate sample
     *
     * @see ECQLGeoOperationTest
     */
    @Test
    public void geoOperationPredicate() throws CQLException {
        assertFilter("INTERSECTS(the_geom, POINT (1 2))", Intersects.class);
        assertFilter("INTERSECTS(buffer(the_geom,10), POINT (1 2))", Intersects.class);
    }

    @Test
    public void dwithinGeometry() throws Exception {
        assertFilter(
                "DWITHIN(buffer(the_geom,5), POINT (1 2), 10.0, kilometers)",
                DistanceBufferOperator.class);
    }

    @Test
    public void dwithinReferencedGeometry() throws Exception {
        assertFilter(
                "DWITHIN(buffer(the_geom,5), SRID=3857;POINT (1 2), 10.0, kilometers)",
                DistanceBufferOperator.class);
    }

    /**
     * Temporal predicate sample
     *
     * @see ECQLTemporalPredicateTest
     */
    @Test
    public void temporalPredicate() throws Exception {
        Filter filter = ECQL.toFilter("ATTR1 BEFORE 2006-12-31T01:30:00Z");
        Assert.assertTrue(filter instanceof Before);

        assertFilter("ATTR1 BEFORE 2006-12-31T01:30:00+00:00", Before.class);
    }

    /**
     * And / Or / Not predicate
     *
     * @see ECQLBooleanValueExpressionTest
     */
    @Test
    public void booleanPredicate() throws Exception {

        Filter filter;

        // and sample
        assertFilter("ATTR1 < 10 AND ATTR2 < 2", And.class);

        // or sample
        assertFilter("ATTR1 < 10 OR ATTR2 < 2", Or.class);

        // not sample
        assertFilter("NOT (ATTR < 10)", Not.class);

        // compound example
        assertFilter("(A = 1 OR B = 2) AND C = 3", And.class);

        // compound example
        assertFilter("(A = 1 OR B = 2) AND NOT (C = 3)", And.class);
    }

    /**
     * Id predicate sample
     *
     * @see ECQLIDPredicateTest
     */
    @Test
    public void idPredicate() throws Exception {
        assertFilter("IN (1,2,3,4)", Id.class);
        assertFilter("IN ('river.1','river.2')", Id.class);
    }

    private <F extends Expression> F assertExpression(String ecql, Class<F> expected)
            throws CQLException {
        Expression expression = ECQL.toExpression(ecql);
        Assert.assertTrue(expected.getSimpleName(), expected.isInstance(expression));
        Assert.assertEquals(ecql, ecql, ECQL.toCQL(expression));

        return expected.cast(expression);
    }

    private <F extends Filter> F assertFilter(String ecql, Class<F> type) throws CQLException {
        Filter filter = ECQL.toFilter(ecql);
        Assert.assertTrue(type.getSimpleName(), type.isInstance(filter));
        Assert.assertEquals(ecql, ecql, ECQL.toCQL(filter));

        return type.cast(filter);
    }

    private <F extends Filter> F assertFilter(String ecql, String expected, Class<F> type)
            throws CQLException {
        Filter filter = ECQL.toFilter(ecql);
        Assert.assertEquals(ecql, expected, ECQL.toCQL(filter));

        return type.cast(filter);
    }

    /**
     * in predicate sample
     *
     * @see ECQLINPredicateTest
     */
    @Test
    public void inPredicate() throws CQLException {
        assertFilter("length IN (4100001,4100002,4100003)", Or.class);

        assertFilter("A IN (1,2,3)", Or.class);
        assertFilter("(A IN (1,2,3)) OR B = 1", Or.class);
        assertFilter("(A IN (1,2,3)) OR (B = 1 AND C = 3)", Or.class);
        assertFilter("(A IN (1,2,3)) OR (B IN (4,5))", Or.class);

        assertFilter("(A IN (1,2,3)) AND (B IN (5,6,7,8))", And.class);
        assertFilter("(A = 1 OR A = 2)", "A IN (1,2)", Or.class);

        // the following glitches should be fixed - but it looks like
        // it will require a change to the grammer to support getting a list
        // of filters
        // assertFilter("A = 1 OR A = 2 OR A = 3","(A IN (1,2,3)",Or.class);
        assertFilter("A = 1 OR A = 2 OR A = 3", "(A IN (1,2)) OR A = 3", Or.class);
    }

    /**
     * Like predicate sample
     *
     * @see ECQLLikePredicateTest
     */
    @Test
    public void likePredicate() throws Exception {

        // using a property as expression
        assertFilter("aProperty LIKE '%bb%'", PropertyIsLike.class);

        // using function as expression
        PropertyIsLike like =
                assertFilter("strToUpperCase(anAttribute) LIKE '%BB%'", PropertyIsLike.class);

        Assert.assertTrue(like.getExpression() instanceof Function);
    }

    /**
     * Null predicate sample
     *
     * @see ECQLNullPredicateTest
     */
    @Test
    public void isNullPredicate() throws Exception {
        assertFilter("centroid(the_geom) IS NULL", IsNullImpl.class);
    }

    /**
     * Exist property predicate sample
     *
     * @see ECQLExistenceTest
     */
    @Test
    public void existProperty() throws Exception {
        PropertyIsEqualTo eq = assertFilter("aProperty EXISTS", PropertyIsEqualTo.class);
        Expression expr = eq.getExpression1();
        Assert.assertTrue(expr instanceof PropertyExistsFunction);
    }

    @Test
    public void expression() throws Exception {
        assertExpression("A + 1", Add.class);
    }

    @Test
    public void listOfPredicates() throws Exception {

        List<Filter> list = ECQL.toFilterList("A=1; B<4");

        Assert.assertTrue(list.size() == 2);

        Assert.assertTrue(list.get(0) instanceof PropertyIsEqualTo);

        Assert.assertTrue(list.get(1) instanceof PropertyIsLessThan);
    }

    @Test
    public void greaterFilterToCQL() throws Exception {

        String expectedECQL = FilterECQLSample.PROPERTY_GREATER_MINUS_INGEGER;
        Filter filter = FilterECQLSample.getSample(expectedECQL);

        String resultECQL = ECQL.toCQL(filter);

        Assert.assertEquals(expectedECQL, resultECQL);
    }

    @Test
    public void likeFilterToCQL() throws Exception {

        String expectedECQL = FilterECQLSample.LITERAL_LIKE_ECQL_PATTERN;
        Filter filter = FilterECQLSample.getSample(expectedECQL);

        String resultECQL = ECQL.toCQL(filter);

        Assert.assertEquals(expectedECQL, resultECQL);
    }

    @Test
    public void functionExpressionToCQL() throws Exception {

        Expression[] absArgs = new Expression[1];
        FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
        absArgs[0] = ff.literal(10);
        Function abs = ff.function("abs", absArgs);

        String resultECQL = ECQL.toCQL(abs);

        Assert.assertEquals("abs(10)", resultECQL);
    }

    /** Verify the parser uses the provided FilterFactory implementation */
    @Test
    public void toFilterUsesProvidedFilterFactory() throws Exception {
        final boolean[] called = {false};

        FilterFactory ff =
                new FilterFactoryImpl() {
                    public PropertyName property(String propName) {
                        called[0] = true;

                        return super.property(propName);
                    }
                };

        ECQL.toFilter("attName > 20", ff);
        Assert.assertTrue("Provided FilterFactory was not called", called[0]);
    }

    @Test
    public void filterListToECQL() throws Exception {

        String expectedECQL = "QUANTITY = 1; YEAR < 1963";
        List<Filter> list = ECQL.toFilterList(expectedECQL);

        Assert.assertTrue(list.size() == 2);

        String cqlResult = ECQL.toCQL(list);

        Assert.assertEquals(expectedECQL, cqlResult);
    }

    @Test
    public void filterListToECQLWithRefencedGeometries() throws Exception {

        String expectedECQL =
                "INTERSECTS(the_geom, SRID=4326;POINT (1 2)); INTERSECTS(abcd, SRID=4962;POINT (0 0))";
        List<Filter> list = ECQL.toFilterList(expectedECQL);

        Assert.assertTrue(list.size() == 2);

        String cqlResult = ECQL.toCQL(list);

        Assert.assertEquals(expectedECQL, cqlResult);
    }

    @Test
    public void filterToECQL() throws Exception {

        String expectedECQL = "QUANTITY = 1";
        Filter list = ECQL.toFilter(expectedECQL);
        String cqlResult = ECQL.toCQL(list);

        Assert.assertEquals(expectedECQL, cqlResult);
    }

    @Test
    public void filterListToCQL() throws Exception {

        String expectedCQL = "QUANTITY = 1; YEAR < 1963";
        List<Filter> list = CQL.toFilterList(expectedCQL);

        Assert.assertTrue(list.size() == 2);

        String cqlResult = CQL.toCQL(list);

        Assert.assertEquals(expectedCQL, cqlResult);
    }
    /** Verify the parser uses the provided FilterFactory implementation */
    @Test
    public void toExpressionUsesProvidedFilterFactory() throws Exception {
        final boolean[] called = {false};

        FilterFactory ff =
                new FilterFactoryImpl() {
                    public PropertyName property(String propName) {
                        called[0] = true;

                        return super.property(propName);
                    }
                };

        ECQL.toExpression("attName", ff);
        Assert.assertTrue("Provided FilterFactory was not called", called[0]);
    }

    @Test
    public void testDivideEncode() throws Exception {
        final FilterFactory2 filterFactory2 = CommonFactoryFinder.getFilterFactory2();
        final Filter javaFilter =
                filterFactory2.less(
                        filterFactory2.divide(
                                filterFactory2.property("population"), filterFactory2.literal(2)),
                        filterFactory2.divide(
                                filterFactory2.property("pop2000"), filterFactory2.literal(2)));
        Assert.assertEquals("population/2<pop2000/2", ECQL.toCQL(javaFilter).replace(" ", ""));
    }

    @Test
    public void testQuotedComparison() throws Exception {
        Filter filter = ECQL.toFilter("\"a\"=\"b\"");
        final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        final Filter expected = ff.equal(ff.property("a"), ff.property("b"), false);
        assertEquals(expected, filter);
    }

    @Test
    public void colorLiterals() throws CQLException {
        String expected = "Interpolate(population,0,'#FF0000',10,'#0000FF')";
        Expression expr = ECQL.toExpression(expected);
        String actual = ECQL.toCQL(expr);
        assertEquals("color literals", expected, actual);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        Function function =
                ff.function(
                        "Interpolate",
                        ff.property("population"),
                        ff.literal(0),
                        ff.literal(Color.RED),
                        ff.literal(10),
                        ff.literal(Color.BLUE));

        actual = ECQL.toCQL(function);
        assertEquals("color literals", expected, actual);
    }
}
