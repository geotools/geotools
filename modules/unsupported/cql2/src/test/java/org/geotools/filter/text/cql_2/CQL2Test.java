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
package org.geotools.filter.text.cql_2;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.List;
import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.IsNullImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.FilterECQLSample;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
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
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.During;

/** Changes since ECQL: see the other tests, nothing new here. */
public class CQL2Test {

    @Test(expected = CQLException.class)
    public void include() throws CQLException {
        CQL2.toFilter("INCLUDE");
    }

    @Test(expected = CQLException.class) // not present in
    public void exclude() throws CQLException {
        CQL2.toFilter("EXCLUDE");
    }

    /**
     * Between predicate sample
     *
     * @see CQL2BetweenPredicateTest
     */
    @Test
    public void betweenPredicate() throws CQLException {
        assertFilter("ATTR1 BETWEEN 10 AND 20", PropertyIsBetween.class);
    }

    /**
     * Equals predicate sample
     *
     * @see CQL2ComparisonPredicateTest
     */
    @Test
    public void comparisonPredicate() throws Exception {
        assertFilter("POP_RANK > 6", PropertyIsGreaterThan.class);
        assertFilter("Area(the_geom) < 3000", PropertyIsLessThan.class);
    }

    /**
     * GeoOperation predicate sample
     *
     * @see CQL2GeoOperationTest
     */
    @Test
    public void geoOperationPredicate() throws CQLException {
        assertFilter("S_INTERSECTS(the_geom, POINT (1 2))", Intersects.class);
        assertFilter("S_INTERSECTS(buffer(the_geom,10), POINT (1 2))", Intersects.class);
    }

    @Test
    public void beforePredicate() throws Exception {
        String cql = "T_BEFORE(ATTR1, TIMESTAMP('2006-12-31T01:30:00Z'))";
        assertFilter(cql, Before.class);
    }

    @Test
    public void afterPredicate() throws Exception {
        String cql = "T_AFTER(ATTR1, TIMESTAMP('2006-12-31T01:30:00Z'))";
        assertFilter(cql, After.class);
    }

    @Test
    public void duringPredicate() throws Exception {
        String cql = "T_DURING(ATTR1, INTERVAL(DATE('2006-10-31'), DATE('2006-12-31')))";
        assertFilter(cql, During.class);
    }

    /**
     * And / Or / Not predicate
     *
     * @see CQL2BooleanValueExpressionTest
     */
    @Test
    public void booleanPredicate() throws Exception {
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

    private <F extends Expression> F assertExpression(String cql, Class<F> expected)
            throws CQLException {
        Expression expression = CQL2.toExpression(cql);
        Assert.assertTrue(expected.getSimpleName(), expected.isInstance(expression));
        Assert.assertEquals(cql, cql, CQL2.toCQL2(expression));

        return expected.cast(expression);
    }

    private <F extends Filter> F assertFilter(String cql, Class<F> type) throws CQLException {
        Filter filter = CQL2.toFilter(cql);
        Assert.assertTrue(type.getSimpleName(), type.isInstance(filter));
        Assert.assertEquals(cql, cql, CQL2.toCQL2(filter));

        return type.cast(filter);
    }

    private <F extends Filter> F assertFilter(String cql, String expected, Class<F> type)
            throws CQLException {
        Filter filter = CQL2.toFilter(cql);
        Assert.assertEquals(cql, expected, CQL2.toCQL2(filter));

        return type.cast(filter);
    }

    /**
     * in predicate sample
     *
     * @see CQL2INPredicateTest
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
     * @see CQL2LikePredicateTest
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
     * @see CQL2NullPredicateTest
     */
    @Test
    public void isNullPredicate() throws Exception {
        assertFilter("centroid(the_geom) IS NULL", IsNullImpl.class);
    }

    @Test
    public void expression() throws Exception {
        assertExpression("A + 1", Add.class);
    }

    @Test
    public void greaterFilterToCQL() throws Exception {

        String expectedCQL2 = FilterECQLSample.PROPERTY_GREATER_MINUS_INGEGER;
        Filter filter = FilterECQLSample.getSample(expectedCQL2);

        String resultCQL2 = CQL2.toCQL2(filter);

        Assert.assertEquals(expectedCQL2, resultCQL2);
    }

    @Test
    public void likeFilterToCQL() throws Exception {

        String expectedECQL = FilterECQLSample.LITERAL_LIKE_ECQL_PATTERN;
        Filter filter = FilterECQLSample.getSample(expectedECQL);

        String resultCQL2 = CQL2.toCQL2(filter);

        Assert.assertEquals(expectedECQL, resultCQL2);
    }

    @Test
    public void functionExpressionToCQL() throws Exception {

        Expression[] absArgs = new Expression[1];
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        absArgs[0] = ff.literal(10);
        Function abs = ff.function("abs", absArgs);

        String resultECQL = CQL2.toCQL2(abs);

        Assert.assertEquals("abs(10)", resultECQL);
    }

    /** Verify the parser uses the provided FilterFactory implementation */
    @Test
    public void toFilterUsesProvidedFilterFactory() throws Exception {
        final boolean[] called = {false};

        FilterFactory ff =
                new FilterFactoryImpl() {
                    @Override
                    public PropertyName property(String propName) {
                        called[0] = true;

                        return super.property(propName);
                    }
                };

        CQL2.toFilter("attName > 20", ff);
        Assert.assertTrue("Provided FilterFactory was not called", called[0]);
    }

    @Test
    public void filterToECQL() throws Exception {

        String expectedECQL = "QUANTITY = 1";
        Filter list = CQL2.toFilter(expectedECQL);
        String cqlResult = CQL2.toCQL2(list);

        Assert.assertEquals(expectedECQL, cqlResult);
    }

    @Test
    public void filterListToCQL() throws Exception {

        String expectedCQL = "QUANTITY = 1; YEAR < 1963";
        List<Filter> list = CQL.toFilterList(expectedCQL);

        assertEquals(2, list.size());

        String cqlResult = CQL.toCQL(list);

        Assert.assertEquals(expectedCQL, cqlResult);
    }
    /** Verify the parser uses the provided FilterFactory implementation */
    @Test
    public void toExpressionUsesProvidedFilterFactory() throws Exception {
        final boolean[] called = {false};

        FilterFactory ff =
                new FilterFactoryImpl() {
                    @Override
                    public PropertyName property(String propName) {
                        called[0] = true;

                        return super.property(propName);
                    }
                };

        CQL2.toExpression("attName", ff);
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
        Assert.assertEquals("population/2<pop2000/2", CQL2.toCQL2(javaFilter).replace(" ", ""));
    }

    @Test
    public void testQuotedComparison() throws Exception {
        Filter filter = CQL2.toFilter("\"a\"=\"b\"");
        final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        final Filter expected = ff.equal(ff.property("a"), ff.property("b"), false);
        assertEquals(expected, filter);
    }

    @Test
    public void colorLiterals() throws CQLException {
        String expected = "Interpolate(population,0,'#FF0000',10,'#0000FF')";
        Expression expr = CQL2.toExpression(expected);
        String actual = CQL2.toCQL2(expr);
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

        actual = CQL2.toCQL2(function);
        assertEquals("color literals", expected, actual);
    }

    @Test
    public void idEquality() throws CQLException {
        Filter filter = CQL2.toFilter("id = 'abcd'");
        assertThat(filter, CoreMatchers.instanceOf(PropertyIsEqualTo.class));
        PropertyIsEqualTo pet = (PropertyIsEqualTo) filter;
        assertThat(pet.getExpression1(), CoreMatchers.instanceOf(PropertyName.class));
        PropertyName pn = (PropertyName) pet.getExpression1();
        assertEquals("id", pn.getPropertyName());
    }

    @Test
    public void idInChoices() throws CQLException {
        Filter filter = CQL2.toFilter("id in ('abcd', 'efg')");
        assertThat(filter, CoreMatchers.instanceOf(Or.class));
        Or or = (Or) filter;
        assertEquals(2, or.getChildren().size());
        assertThat(or.getChildren().get(0), CoreMatchers.instanceOf(PropertyIsEqualTo.class));
        assertThat(or.getChildren().get(1), CoreMatchers.instanceOf(PropertyIsEqualTo.class));
        PropertyIsEqualTo pet = (PropertyIsEqualTo) or.getChildren().get(0);
        assertThat(pet.getExpression1(), CoreMatchers.instanceOf(PropertyName.class));
        PropertyName pn = (PropertyName) pet.getExpression1();
        assertEquals("id", pn.getPropertyName());
    }
}
