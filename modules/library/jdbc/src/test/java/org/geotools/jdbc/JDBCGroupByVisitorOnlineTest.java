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

import org.geotools.data.Query;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.feature.visitor.GroupByVisitorBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import java.io.IOException;
import java.util.List;

public abstract class JDBCGroupByVisitorOnlineTest extends JDBCTestSupport {

    public void testSimpleGroupByWithMax() throws Exception {
        List<Object[]> value = genericGroupByTestTest(Aggregate.MAX, "building_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "HOUSE", "6.0");
        checkValueContains(value, "FABRIC", "500.0");
        checkValueContains(value, "SCHOOL", "60.0");
    }

    public void testMultipleGroupByWithMax() throws Exception {
        List<Object[]> value = genericGroupByTestTest(Aggregate.MAX, "building_type", "energy_type");
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
        List<Object[]> value = genericGroupByTestTest(energyConsumptionGreaterThan(50.0),
                Aggregate.MAX, "building_type", "energy_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "SCHOOL", "FUEL", "60.0");
        checkValueContains(value, "FABRIC", "NUCLEAR", "150.0");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "500.0");
    }

    public void testMultipleGroupByWithMaxWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        List<Object[]> value = genericGroupByTestTest(queryWithLimits(0, 3),
                Aggregate.MAX, "building_type", "energy_type");
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
        List<Object[]> value = genericGroupByTestTest(Aggregate.MIN, "building_type", "energy_type");
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
        List<Object[]> value = genericGroupByTestTest(energyConsumptionGreaterThan(50.0),
                Aggregate.MIN, "building_type", "energy_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "SCHOOL", "FUEL", "60.0");
        checkValueContains(value, "FABRIC", "NUCLEAR", "150.0");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "500.0");
    }

    public void testMultipleGroupByWithMinWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        List<Object[]> value = genericGroupByTestTest(queryWithLimits(0, 3),
                Aggregate.MIN, "building_type", "energy_type");
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
        List<Object[]> value = genericGroupByTestTest(Aggregate.COUNT, "building_type", "energy_type");
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
        List<Object[]> value = genericGroupByTestTest(energyConsumptionGreaterThan(50.0),
                Aggregate.COUNT, "building_type", "energy_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "SCHOOL", "FUEL", "1");
        checkValueContains(value, "FABRIC", "NUCLEAR", "1");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "1");
    }

    public void testMultipleGroupByWithCountWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        List<Object[]> value = genericGroupByTestTest(queryWithLimits(0, 3),
                Aggregate.COUNT, "building_type", "energy_type");
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
        List<Object[]> value = genericGroupByTestTest(Aggregate.SUM, "building_type", "energy_type");
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
        List<Object[]> value = genericGroupByTestTest(energyConsumptionGreaterThan(50.0),
                Aggregate.SUM, "building_type", "energy_type");
        assertTrue(value.size() == 3);
        checkValueContains(value, "SCHOOL", "FUEL", "60.0");
        checkValueContains(value, "FABRIC", "NUCLEAR", "150.0");
        checkValueContains(value, "FABRIC", "FLOWING_WATER", "500.0");
    }

    public void testWithMinWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
        List<Object[]> value = genericGroupByTestTest(queryWithLimits(0, 3),
                Aggregate.SUM, "building_type", "energy_type");
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
        Filter filter = filterFactory.greater(filterFactory.property(aname("energy_consumption")), filterFactory.literal(value));
        return new Query(tname("buildings"), filter);
    }

    private List<Object[]> genericGroupByTestTest(Aggregate aggregateVisitor,
                                                  String... groupByAttributes) throws IOException {
        return genericGroupByTestTest(Query.ALL, aggregateVisitor, groupByAttributes);
    }

    private List<Object[]> genericGroupByTestTest(Query query, Aggregate aggregateVisitor,
                                                  String... groupByAttributes) throws IOException {
        ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("buildings_group_by_tests"));
        SimpleFeatureType featureType = featureSource.getSchema();
        GroupByVisitorBuilder visitorBuilder = new GroupByVisitorBuilder()
                .withAggregateAttribute("energy_consumption", featureType)
                .withAggregateVisitor(aggregateVisitor);
        for (String groupByAttribute : groupByAttributes) {
            visitorBuilder.withGroupByAttribute(groupByAttribute, featureType);
        }
        GroupByVisitor visitor = visitorBuilder.build();
        featureSource.accepts(query, visitor, null);
        assertTrue(visitor.wasOptimized());
        assertFalse(visitor.wasVisited());
        List<Object[]> value = visitor.getResult().toList();
        assertNotNull(value);
        return value;
    }

    private void checkValueContains(List<Object[]> value, String... expectedResult) {
        assertTrue(value.stream().anyMatch(result -> {
            if (result.length != expectedResult.length) {
                return false;
            }
            for (int i = 0; i < result.length; i++) {
                if (!result[i].toString().equals(expectedResult[i])) {
                    return false;
                }
            }
            return true;
        }));
    }
}
