/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.feature.visitor.GroupByVisitorBuilder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.expression.InternalVolatileFunction;
import org.geotools.filter.function.DateDifferenceFunction;
import org.geotools.filter.function.math.FilterFunction_floor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;

public abstract class JDBCGroupByVisitorOnlineTest extends JDBCTestSupport {

    public void testSimpleGroupByWithMax() throws Exception {
        List<Object[]> value = genericGroupByTestTest(Aggregate.MAX, "building_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "HOUSE", "6.0");
        checkValueContains(value, "FABRIC", "500.0");
        checkValueContains(value, "SCHOOL", "60.0");
    }

    public void testUnkonwnFunction() throws Exception {
        // use a made up function that cannot be possibly known by JDBCDataStore,
        // and is not subject to cloning or modifications of any kind
        Function aggregateFunction =
                new InternalVolatileFunction() {

                    @Override
                    public Object evaluate(Object object) {
                        return ((SimpleFeature) object).getAttribute(aname("building_type"))
                                + "_foo";
                    }
                };

        List<Object[]> value =
                genericGroupByTestTest(Query.ALL, Aggregate.MAX, false, aggregateFunction);
        assertNotNull(value);

        assertTrue(value.size() == 3);
        checkValueContains(value, "HOUSE_foo", "6.0");
        checkValueContains(value, "FABRIC_foo", "500.0");
        checkValueContains(value, "SCHOOL_foo", "60.0");
    }

    public void testAggregateOnMathExpression() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName pn = ff.property("energy_consumption");
        Multiply expression = ff.multiply(pn, ff.literal(10));

        List<Object[]> value = genericGroupByTestTest(Query.ALL, Aggregate.COUNT, expression);
        assertNotNull(value);

        assertTrue(value.size() == 9);
        checkValueContains(value, "40.0", "1");
        checkValueContains(value, "200.0", "2");
        checkValueContains(value, "600.0", "1");
        checkValueContains(value, "5000.0", "1");
        checkValueContains(value, "60.0", "1");
        checkValueContains(value, "100.0", "2");
        checkValueContains(value, "300.0", "2");
        checkValueContains(value, "500.0", "1");
        checkValueContains(value, "1500.0", "1");
    }

    public void testComputeOnMathExpression() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName pn = ff.property(aname("energy_consumption"));
        Multiply computeAttribute = ff.multiply(pn, ff.literal(10));
        PropertyName groupAttribute = ff.property(aname("building_type"));
        List<Object[]> value =
                genericGroupByTestTest(
                        Query.ALL, Aggregate.MAX, computeAttribute, true, groupAttribute);
        assertNotNull(value);

        assertTrue(value.size() == 3);
        checkValueContains(value, "HOUSE", "60.0");
        checkValueContains(value, "FABRIC", "5000.0");
        checkValueContains(value, "SCHOOL", "600.0");
    }

    public void testComputeOnMathExpressionWithLimit() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        // aggregate on expression
        PropertyName pn = ff.property(aname("energy_consumption"));
        Multiply computeAttribute = ff.multiply(pn, ff.literal(10));
        PropertyName groupAttribute = ff.property(aname("building_type"));
        List<Object[]> value =
                genericGroupByTestTest(
                        queryWithLimits(3, 2),
                        Aggregate.MAX,
                        computeAttribute,
                        true,
                        groupAttribute);
        assertNotNull(value);

        assertTrue(value.size() >= 1 && value.size() <= 2);
    }

    public void testNumericHistogram() throws Exception {
        // buckets with a size of 100, the function returns an integer from 0 onwards, which
        // is a zero based bucket number in the bucket sequence
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName pn = ff.property(aname("energy_consumption"));
        Expression expression = ff.function("floor", ff.divide(pn, ff.literal(100)));

        boolean expectOptimized =
                dataStore.getFilterCapabilities().supports(FilterFunction_floor.class);
        List<Object[]> value =
                genericGroupByTestTest(Query.ALL, Aggregate.COUNT, expectOptimized, expression);
        assertNotNull(value);

        assertEquals(value.size(), 3);
        checkValueContains(value, "0", "10");
        checkValueContains(value, "1", "1");
        checkValueContains(value, "5", "1");
    }

    public void testTimestampHistogram() throws Exception {
        // buckets with a size of one day, the function returns an integer from 0 onwards, which
        // is a zero based bucket number in the bucket sequence
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName pn = ff.property(aname("last_update"));
        Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-06-03 00:00:00");
        Expression difference = ff.function("dateDifference", pn, ff.literal(baseDate));
        int dayInMs = 1000 * 60 * 60 * 24;
        Expression expression = ff.function("floor", ff.divide(difference, ff.literal(dayInMs)));

        FilterCapabilities capabilities = dataStore.getFilterCapabilities();
        boolean expectOptimized =
                capabilities.supports(FilterFunction_floor.class)
                        && capabilities.supports(DateDifferenceFunction.class);
        List<Object[]> value =
                genericGroupByTestTest(Query.ALL, Aggregate.COUNT, expectOptimized, expression);
        assertNotNull(value);

        assertEquals(5, value.size());
        checkValueContains(value, "0", "3"); // 2016-06-03
        checkValueContains(value, "2", "1"); // 2016-06-05
        checkValueContains(value, "3", "2"); // 2016-06-06
        checkValueContains(value, "4", "3"); // 2016-06-07
        checkValueContains(value, "12", "3"); // 2016-06-15
    }

    public void testMultipleGroupByWithMax() throws Exception {
        List<Object[]> value =
                genericGroupByTestTest(Aggregate.MAX, "building_type", "energy_type");
        assertTrue(value.size() == 11);
        checkValueContains(value, "SCHOOL", "WIND", "20.0");
        checkValueContains(value, "SCHOOL", "FUEL", "60.0");
        checkValueContains(value, "HOUSE", "NUCLEAR", "4.0");
        checkValueContains(value, "SCHOOL", "SOLAR", "30.0");
        checkValueContains(value, "SCHOOL", "FLOWING_WATER", "50.0");
        checkValueContains(value, "SCHOOL", "NUCLEAR", "10.0");
        checkValueContains(value, "HOUSE", "FUEL", "6.0");
        checkValueContains(value, "FABRIC", "NUCLEAR", "150.0");
        checkValueContains(value, "FABRIC", "SOLAR", "30.0");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "500.0");
        checkValueContains(value, "FABRIC", "WIND", "20.0");
    }

    public void testMultipleGroupByWithMaxWithFilter() throws Exception {
        List<Object[]> value =
                genericGroupByTestTest(
                        energyConsumptionGreaterThan(50.0),
                        Aggregate.MAX,
                        "building_type",
                        "energy_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "SCHOOL", "FUEL", "60.0");
        checkValueContains(value, "FABRIC", "NUCLEAR", "150.0");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "500.0");
    }

    public void testMultipleGroupByWithMaxWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        List<Object[]> value =
                genericGroupByTestTest(
                        queryWithLimits(0, 3), Aggregate.MAX, "building_type", "energy_type");
        assertFalse(value.isEmpty());
        assertTrue(value.size() <= 3);
    }

    public void testSimpleGroupByWithMin() throws Exception {
        List<Object[]> value = genericGroupByTestTest(Aggregate.MIN, "building_type");
        checkValueContains(value, "HOUSE", "4.0");
        checkValueContains(value, "FABRIC", "20.0");
        checkValueContains(value, "SCHOOL", "10.0");
    }

    public void testMultipleGroupByWithMin() throws Exception {
        List<Object[]> value =
                genericGroupByTestTest(Aggregate.MIN, "building_type", "energy_type");
        checkValueContains(value, "SCHOOL", "WIND", "20.0");
        checkValueContains(value, "SCHOOL", "FUEL", "60.0");
        checkValueContains(value, "HOUSE", "NUCLEAR", "4.0");
        checkValueContains(value, "SCHOOL", "SOLAR", "30.0");
        checkValueContains(value, "SCHOOL", "FLOWING_WATER", "50.0");
        checkValueContains(value, "SCHOOL", "NUCLEAR", "10.0");
        checkValueContains(value, "HOUSE", "FUEL", "6.0");
        checkValueContains(value, "FABRIC", "NUCLEAR", "150.0");
        checkValueContains(value, "FABRIC", "SOLAR", "30.0");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "500.0");
        checkValueContains(value, "FABRIC", "WIND", "20.0");
    }

    public void testMultipleGroupByWithMinWithFilter() throws Exception {
        List<Object[]> value =
                genericGroupByTestTest(
                        energyConsumptionGreaterThan(50.0),
                        Aggregate.MIN,
                        "building_type",
                        "energy_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "SCHOOL", "FUEL", "60.0");
        checkValueContains(value, "FABRIC", "NUCLEAR", "150.0");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "500.0");
    }

    public void testMultipleGroupByWithMinWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        List<Object[]> value =
                genericGroupByTestTest(
                        queryWithLimits(0, 3), Aggregate.MIN, "building_type", "energy_type");
        assertFalse(value.isEmpty());
        assertTrue(value.size() <= 3);
    }

    public void testSimpleGroupByWithCount() throws Exception {
        List<Object[]> value = genericGroupByTestTest(Aggregate.COUNT, "building_type");
        checkValueContains(value, "HOUSE", "2");
        checkValueContains(value, "FABRIC", "4");
        checkValueContains(value, "SCHOOL", "6");
    }

    public void testMultipleGroupByWithCount() throws Exception {
        List<Object[]> value =
                genericGroupByTestTest(Aggregate.COUNT, "building_type", "energy_type");
        checkValueContains(value, "SCHOOL", "WIND", "1");
        checkValueContains(value, "SCHOOL", "FUEL", "1");
        checkValueContains(value, "HOUSE", "NUCLEAR", "1");
        checkValueContains(value, "SCHOOL", "SOLAR", "1");
        checkValueContains(value, "SCHOOL", "FLOWING_WATER", "1");
        checkValueContains(value, "SCHOOL", "NUCLEAR", "2");
        checkValueContains(value, "HOUSE", "FUEL", "1");
        checkValueContains(value, "FABRIC", "NUCLEAR", "1");
        checkValueContains(value, "FABRIC", "SOLAR", "1");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "1");
        checkValueContains(value, "FABRIC", "WIND", "1");
    }

    public void testMultipleGroupByWithCountWithFilter() throws Exception {
        List<Object[]> value =
                genericGroupByTestTest(
                        energyConsumptionGreaterThan(50.0),
                        Aggregate.COUNT,
                        "building_type",
                        "energy_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "SCHOOL", "FUEL", "1");
        checkValueContains(value, "FABRIC", "NUCLEAR", "1");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "1");
    }

    public void testMultipleGroupByWithCountWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        List<Object[]> value =
                genericGroupByTestTest(
                        queryWithLimits(0, 3), Aggregate.COUNT, "building_type", "energy_type");
        assertFalse(value.isEmpty());
        assertTrue(value.size() <= 3);
    }

    public void testSimpleGroupByWithSum() throws Exception {
        List<Object[]> value = genericGroupByTestTest(Aggregate.SUM, "building_type");
        checkValueContains(value, "HOUSE", "10.0");
        checkValueContains(value, "FABRIC", "700.0");
        checkValueContains(value, "SCHOOL", "180.0");
    }

    public void testMultipleGroupByWithSum() throws Exception {
        List<Object[]> value =
                genericGroupByTestTest(Aggregate.SUM, "building_type", "energy_type");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "500.0");
        checkValueContains(value, "FABRIC", "NUCLEAR", "150.0");
        checkValueContains(value, "FABRIC", "SOLAR", "30.0");
        checkValueContains(value, "FABRIC", "WIND", "20.0");
        checkValueContains(value, "HOUSE", "FUEL", "6.0");
        checkValueContains(value, "HOUSE", "NUCLEAR", "4.0");
        checkValueContains(value, "SCHOOL", "FLOWING_WATER", "50.0");
        checkValueContains(value, "SCHOOL", "FUEL", "60.0");
        checkValueContains(value, "SCHOOL", "NUCLEAR", "20.0");
        checkValueContains(value, "SCHOOL", "SOLAR", "30.0");
        checkValueContains(value, "SCHOOL", "WIND", "20.0");
    }

    public void testMultipleGroupByWithSumWithFilter() throws Exception {
        List<Object[]> value =
                genericGroupByTestTest(
                        energyConsumptionGreaterThan(50.0),
                        Aggregate.SUM,
                        "building_type",
                        "energy_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "SCHOOL", "FUEL", "60.0");
        checkValueContains(value, "FABRIC", "NUCLEAR", "150.0");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "500.0");
    }

    public void testWithMinWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        List<Object[]> value =
                genericGroupByTestTest(
                        queryWithLimits(0, 3), Aggregate.SUM, "building_type", "energy_type");
        assertFalse(value.isEmpty());
        assertTrue(value.size() <= 3);
    }

    private Query queryWithLimits(int lower, int upper) {
        Query query = new Query(tname("buildings"));
        query.setStartIndex(lower);
        query.setMaxFeatures(upper);
        return query;
    }

    private Query energyConsumptionGreaterThan(double value) {
        FilterFactory filterFactory = dataStore.getFilterFactory();
        Filter filter =
                filterFactory.greater(
                        filterFactory.property(aname("energy_consumption")),
                        filterFactory.literal(value));
        return new Query(tname("buildings"), filter);
    }

    protected List<Object[]> genericGroupByTestTest(
            Aggregate aggregateVisitor, String... groupByAttributes) throws IOException {
        return genericGroupByTestTest(Query.ALL, aggregateVisitor, groupByAttributes);
    }

    private List<Object[]> genericGroupByTestTest(
            Query query, Aggregate aggregateVisitor, String... groupByAttributes)
            throws IOException {
        Expression[] expressions =
                new Expression[groupByAttributes != null ? groupByAttributes.length : 0];
        if (groupByAttributes != null) {
            int i = 0;
            for (String attribute : groupByAttributes) {
                PropertyName property = dataStore.getFilterFactory().property(aname(attribute));
                expressions[i++] = property;
            }
        }

        return genericGroupByTestTest(query, aggregateVisitor, expressions);
    }

    private List<Object[]> genericGroupByTestTest(
            Query query, Aggregate aggregateVisitor, Expression... groupByAttributes)
            throws IOException {
        return genericGroupByTestTest(query, aggregateVisitor, true, groupByAttributes);
    }

    protected List<Object[]> genericGroupByTestTest(
            Query query,
            Aggregate aggregateVisitor,
            boolean expectOptimized,
            Expression... groupByAttributes)
            throws IOException {
        PropertyName aggregateAttribute =
                CommonFactoryFinder.getFilterFactory().property(aname("energy_consumption"));
        return genericGroupByTestTest(
                query, aggregateVisitor, aggregateAttribute, expectOptimized, groupByAttributes);
    }

    private List<Object[]> genericGroupByTestTest(
            Query query,
            Aggregate aggregateVisitor,
            Expression aggregateAttribute,
            boolean expectOptimized,
            Expression... groupByAttributes)
            throws IOException {
        ContentFeatureSource featureSource =
                dataStore.getFeatureSource(tname("buildings_group_by_tests"));

        GroupByVisitorBuilder visitorBuilder =
                new GroupByVisitorBuilder()
                        .withAggregateAttribute(aggregateAttribute)
                        .withAggregateVisitor(aggregateVisitor);
        for (Expression groupByAttribute : groupByAttributes) {
            visitorBuilder.withGroupByAttribute(groupByAttribute);
        }
        GroupByVisitor visitor = visitorBuilder.build();
        featureSource.accepts(query, visitor, null);
        assertEquals(expectOptimized, visitor.wasOptimized());
        assertEquals(!expectOptimized, visitor.wasVisited());
        List<Object[]> value = visitor.getResult().toList();
        assertNotNull(value);
        return value;
    }

    protected void checkValueContains(List<Object[]> value, String... expectedResult) {
        assertTrue(
                value.stream()
                        .anyMatch(
                                result -> {
                                    if (result.length != expectedResult.length) {
                                        return false;
                                    }
                                    for (int i = 0; i < result.length; i++) {
                                        if (result[i] instanceof Number) {
                                            double r = ((Number) result[i]).doubleValue();
                                            double e = Double.parseDouble(expectedResult[i]);
                                            return r == e;
                                        } else if (!result[i]
                                                .toString()
                                                .equals(expectedResult[i])) {
                                            return false;
                                        }
                                    }
                                    return true;
                                }));
    }
}
