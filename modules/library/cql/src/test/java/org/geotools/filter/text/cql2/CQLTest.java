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

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.IsNullImpl;
import org.geotools.filter.function.FilterFunction_relatePattern;
import org.geotools.filter.function.PropertyExistsFunction;
import org.geotools.filter.text.ecql.ECQLBetweenPredicateTest;
import org.geotools.filter.text.ecql.ECQLBooleanValueExpressionTest;
import org.geotools.filter.text.ecql.ECQLComparisonPredicateTest;
import org.geotools.filter.text.ecql.ECQLExistenceTest;
import org.geotools.filter.text.ecql.ECQLGeoOperationTest;
import org.geotools.filter.text.ecql.ECQLLikePredicateTest;
import org.geotools.filter.text.ecql.ECQLNullPredicateTest;
import org.geotools.filter.text.ecql.ECQLTemporalPredicateTest;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.temporal.Before;
import org.opengis.referencing.FactoryException;

/**
 * CQL Test
 *
 * <p>Test Common CQL language
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.5
 */
public class CQLTest {

    /**
     * Between predicate sample
     *
     * @see ECQLBetweenPredicateTest
     */
    @Test
    public void betweenPredicate() throws CQLException {

        Filter filter = CQL.toFilter("QUANTITY BETWEEN 10 AND 20");

        Assert.assertTrue(filter instanceof PropertyIsBetween);
    }

    /**
     * Equals predicate sample
     *
     * @see ECQLComparisonPredicateTest
     */
    @Test
    public void comparisonPredicate() throws Exception {

        Filter filter;

        filter = CQL.toFilter("POP_RANK > 6");

        Assert.assertTrue(filter instanceof PropertyIsGreaterThan);
    }

    /**
     * GeoOperation predicate sample
     *
     * @see ECQLGeoOperationTest
     */
    @Test
    public void geoOperationPredicate() throws CQLException {

        Filter filter;

        filter = CQL.toFilter("DISJOINT(the_geom, POINT(1 2))");

        Assert.assertTrue("Disjoint was expected", filter instanceof Disjoint);
    }

    @Test
    public void relateGeoOperation() throws CQLException {

        PropertyIsEqualTo filter =
                (PropertyIsEqualTo)
                        CQL.toFilter(
                                "RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)");

        Assert.assertTrue(
                "Relate Pattern Function was expected",
                filter.getExpression1() instanceof FilterFunction_relatePattern);

        Assert.assertTrue("Literal TRUE was expected", filter.getExpression2() instanceof Literal);
    }

    @Test
    public void dwithinGeometry() throws Exception {
        Filter resultFilter;

        // DWITHIN
        resultFilter = CQL.toFilter("DWITHIN(the_geom, POINT(1 2), 10, kilometers)");

        Assert.assertTrue(resultFilter instanceof DistanceBufferOperator);
    }

    /**
     * Temporal predicate sample
     *
     * @see ECQLTemporalPredicateTest
     */
    @Test
    public void temporalPredicate() throws Exception {

        Filter filter = CQL.toFilter("DATE BEFORE 2006-12-31T01:30:00Z");

        Assert.assertTrue(filter instanceof Before);
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
        filter = CQL.toFilter("QUANTITY < 10 AND QUANTITY < 2 ");

        Assert.assertTrue(filter instanceof And);

        // or sample
        filter = CQL.toFilter("QUANTITY < 10 OR QUANTITY < 2 ");

        Assert.assertTrue(filter instanceof Or);

        // not sample
        filter = CQL.toFilter("NOT QUANTITY < 10");

        Assert.assertTrue(filter instanceof Not);
    }

    /**
     * Like predicate sample
     *
     * @see ECQLLikePredicateTest
     */
    @Test
    public void likePredicate() throws Exception {

        Filter filter = CQL.toFilter("NAME like '%new%'");

        Assert.assertTrue(filter instanceof PropertyIsLike);
    }

    /**
     * Null predicate sample
     *
     * @see ECQLNullPredicateTest
     */
    @Test
    public void isNullPredicate() throws Exception {

        Filter filter = CQL.toFilter("SHAPE IS NULL");

        Assert.assertTrue(filter instanceof IsNullImpl);
    }

    /**
     * Exist property predicate sample
     *
     * @see ECQLExistenceTest
     */
    @Test
    public void existProperty() throws Exception {

        Filter resultFilter = CQL.toFilter("NAME EXISTS");

        Assert.assertTrue(resultFilter instanceof PropertyIsEqualTo);

        PropertyIsEqualTo eq = (PropertyIsEqualTo) resultFilter;

        Expression expr = eq.getExpression1();

        Assert.assertTrue(expr instanceof PropertyExistsFunction);
    }

    @Test
    public void addExpression() throws Exception {

        Expression expr = CQL.toExpression("QUANTITY + 1");

        Assert.assertTrue(expr instanceof Add);
    }

    @Test
    public void listOfPredicates() throws Exception {

        List<Filter> list = CQL.toFilterList("QUANTITY=1; YEAR<1963");

        Assert.assertTrue(list.size() == 2);

        Assert.assertTrue(list.get(0) instanceof PropertyIsEqualTo);

        Assert.assertTrue(list.get(1) instanceof PropertyIsLessThan);
    }

    @Test
    public void filterListToCQL() throws Exception {

        String expectedCQL = "QUANTITY = 1; YEAR < 1963";
        List<Filter> list = CQL.toFilterList(expectedCQL);

        Assert.assertTrue(list.size() == 2);

        String cqlResult = CQL.toCQL(list);

        Assert.assertEquals(expectedCQL, cqlResult);
    }

    @Test
    public void filterToCQL() throws Exception {

        String expectedCQL = "QUANTITY = 1";
        Filter list = CQL.toFilter(expectedCQL);
        String cqlResult = CQL.toCQL(list);

        Assert.assertEquals(expectedCQL, cqlResult);
    }

    @Test
    public void expressionToCQLExpression() throws Exception {

        String expectedCQL = "abs(-10) + 1";
        Expression list = CQL.toExpression(expectedCQL);
        String cqlResult = CQL.toCQL(list);

        Assert.assertEquals(expectedCQL, cqlResult);
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

        CQL.toFilter("attName > 20", ff);
        Assert.assertTrue("Provided FilterFactory was not called", called[0]);
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

        CQL.toExpression("attName", ff);
        Assert.assertTrue("Provided FilterFactory was not called", called[0]);
    }

    @Test
    public void testEWKTEncodingDisabled() throws Exception {
        Literal literalGeometry = getWgs84PointLiteral();

        // check EWKT is not used for CQL
        String cql = CQL.toCQL(literalGeometry);
        assertEquals("POINT (1 2)", cql);
    }

    private Literal getWgs84PointLiteral() throws FactoryException {
        Point p = new GeometryFactory().createPoint(new Coordinate(1, 2));
        p.setUserData(CRS.decode("EPSG:4326", true));
        return CommonFactoryFinder.getFilterFactory2().literal(p);
    }
}
