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
package org.geotools.jdbc;

import static org.geotools.api.filter.sort.SortOrder.ASCENDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.geotools.api.data.Query;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.AverageVisitor;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.feature.visitor.GroupByVisitorBuilder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.NearestVisitor;
import org.geotools.feature.visitor.StandardDeviationVisitor;
import org.geotools.feature.visitor.SumAreaVisitor;
import org.geotools.feature.visitor.SumVisitor;
import org.geotools.feature.visitor.UniqueCountVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.function.FilterFunction_area;
import org.geotools.util.Converters;
import org.junit.Before;
import org.junit.Test;

public abstract class JDBCAggregateFunctionOnlineTest extends JDBCTestSupport {

    boolean visited = false;

    @Override
    protected abstract JDBCAggregateTestSetup createTestSetup();

    @Before
    public void resetVisited() {
        visited = false;
    }

    class MySumVisitor extends SumVisitor {

        public MySumVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        @Override
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        @Override
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    @Test
    public void testSum() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        SumVisitor v = new MySumVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals(3.3, v.getResult().toDouble(), 0.01);
    }

    class MySumAreaVisitor extends SumAreaVisitor {

        public MySumAreaVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        @Override
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        @Override
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    @Test
    public void testSumArea() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("geom"));

        SumAreaVisitor v = new MySumAreaVisitor(p);
        dataStore.getFeatureSource(tname("aggregate")).accepts(Query.ALL, v, null);
        assertEquals(
                visited, !dataStore.getFilterCapabilities().supports(FilterFunction_area.class));
        assertEquals(30.0, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testSumAreaWithGroupBy() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("geom"));

        GroupByVisitor v =
                new GroupByVisitorBuilder()
                        .withAggregateAttribute(ff.function("area2", p))
                        .withAggregateVisitor("SumArea")
                        .withGroupByAttributes(
                                Collections.singleton(aname("name")),
                                dataStore.getSchema(tname("aggregate")))
                        .build();

        dataStore.getFeatureSource(tname("aggregate")).accepts(Query.ALL, v, null);
        if (dataStore.getFilterCapabilities().supports(FilterFunction_area.class)) {
            assertFalse(visited);
        }
        List groups = v.getResult().toList();
        assertEquals(20.0, (Double) ((Object[]) groups.get(0))[1], 0.01);
        assertEquals(10.0, (Double) ((Object[]) groups.get(1))[1], 0.01);
    }

    @Test
    public void testSumWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        SumVisitor v = new MySumVisitor(p);

        Filter f = ff.less(ff.property(aname("doubleProperty")), ff.literal(2));
        Query q = new Query(tname("ft1"), f);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        assertEquals(1.1, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testSumWithFunctionFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        SumVisitor v = new MySumVisitor(p);

        Filter f =
                ff.equals(
                        ff.function(
                                "strMatches",
                                ff.property(aname("stringProperty")),
                                ff.literal("zero*")),
                        ff.literal(false));
        Query q = new Query(tname("ft1"), f);

        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertEquals(3.3, v.getResult().toDouble(), 0.00001);
    }

    @Test
    public void testSumWithLimitOffset() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isLimitOffsetSupported());
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        SumVisitor v = new MySumVisitor(p);

        Query q = new Query(tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);

        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        assertEquals(1.1, v.getResult().toDouble(), 0.01);
    }

    class MyMaxVisitor extends MaxVisitor {

        public MyMaxVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        @Override
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        @Override
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    @Test
    public void testMax() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MaxVisitor v = new MyMaxVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals(2.2, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testMaxWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MaxVisitor v = new MyMaxVisitor(p);

        Filter f = ff.less(ff.property(aname("doubleProperty")), ff.literal(2));
        Query q = new Query(tname("ft1"), f);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        assertEquals(1.1, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testMaxWithLimitOffset() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isLimitOffsetSupported());
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MaxVisitor v = new MyMaxVisitor(p);

        Query q = new Query(tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);

        assertFalse(visited);
        assertEquals(1.1, v.getResult().toDouble(), 0.01);
    }

    class MyMinVisitor extends MinVisitor {

        public MyMinVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        @Override
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        @Override
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    @Test
    public void testMin() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MinVisitor v = new MyMinVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals(0.0, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testMinWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MinVisitor v = new MyMinVisitor(p);

        Filter f = ff.greater(ff.property(aname("doubleProperty")), ff.literal(1));
        Query q = new Query(tname("ft1"), f);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        assertEquals(1.1, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testMinWithLimitOffset() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isLimitOffsetSupported());
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MinVisitor v = new MyMinVisitor(p);

        Query q = new Query(tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);

        assertFalse(visited);
        assertEquals(0.0, v.getResult().toDouble(), 0.01);
    }

    class MyUniqueVisitor extends UniqueVisitor {

        public MyUniqueVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        @Override
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        @Override
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    @Test
    public void testUnique() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new MyUniqueVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(3, result.size());
        assertTrue(result.contains("zero"));
        assertTrue(result.contains("one"));
        assertTrue(result.contains("two"));
    }

    @Test
    public void testUniqueWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new MyUniqueVisitor(p);
        Filter f = ff.greater(ff.property(aname("doubleProperty")), ff.literal(1));
        Query q = new Query(tname("ft1"), f);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(2, result.size());
        assertTrue(result.contains("one"));
        assertTrue(result.contains("two"));
    }

    @Test
    public void testUniqueWithLimitOffset() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isLimitOffsetSupported());
        assumeTrue(dataStore.getSQLDialect().isAggregatedSortSupported("distinct"));

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new MyUniqueVisitor(p);
        Query q = new Query(tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(2, result.size());
    }

    @Test
    public void testUniqueWithLimitOnVisitor() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isLimitOffsetSupported());
        assumeTrue(dataStore.getSQLDialect().isAggregatedSortSupported("distinct"));

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new MyUniqueVisitor(p);
        v.setPreserveOrder(true);
        v.setStartIndex(0);
        v.setMaxFeatures(2);
        Query q = new Query(tname("ft1"));
        q.setSortBy(new SortByImpl(p, ASCENDING));
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(2, result.size());
        assertEquals("one", result.iterator().next());
    }

    @Test
    public void testUniqueWithNonMatchingSort() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isAggregatedSortSupported("distinct"));

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new MyUniqueVisitor(p);
        v.setStartIndex(0);
        Query q = new Query(tname("ft1"));
        PropertyName pNonMatching = ff.property(aname("doubleProperty"));
        q.setSortBy(new SortByImpl(pNonMatching, ASCENDING));
        // used to fail with an exception, because stringProperty was in order by, but not in select
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(3, result.size());
    }

    @Test
    public void testUniqueWithNonMatchingSortPreserveOrder() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isAggregatedSortSupported("distinct"));

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new MyUniqueVisitor(p);
        v.setStartIndex(0);
        v.setPreserveOrder(true);
        Query q = new Query(tname("ft1"));
        PropertyName pNonMatching = ff.property(aname("doubleProperty"));
        q.setSortBy(new SortByImpl(pNonMatching, ASCENDING));
        // used to fail with an exception, because stringProperty was in order by, but not in select
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        // this cannot run in the database, so it will run in memory instead (SQL support is an

        assertTrue(visited);
        Set result = v.getResult().toSet();
        assertEquals(3, result.size());
    }

    @Test
    public void testStoreChecksVisitorLimits() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isLimitOffsetSupported());
        assumeTrue(dataStore.getSQLDialect().isAggregatedSortSupported("distinct"));

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v =
                new MyUniqueVisitor(p) {
                    @Override
                    public boolean hasLimits() {
                        // forced to return true, to check that the JDBCDataStore
                        // asks the visitor if it has limits, and if answered true
                        // it ignores query limits
                        return true;
                    }
                };
        v.setPreserveOrder(true);

        Query q = new Query(tname("ft1"));
        q.setMaxFeatures(1);
        q.setSortBy(new SortByImpl(p, ASCENDING));
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(3, result.size());
        assertEquals("one", result.iterator().next());
    }

    @Test
    public void testUniqueWithLimitOffsetOnVisitor() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isLimitOffsetSupported());
        assumeTrue(dataStore.getSQLDialect().isAggregatedSortSupported("distinct"));

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new MyUniqueVisitor(p);
        v.setPreserveOrder(true);
        v.setStartIndex(1);
        v.setMaxFeatures(2);
        Query q = new Query(tname("ft1"));
        q.setSortBy(new SortByImpl(p, ASCENDING));
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(2, result.size());
        assertEquals("two", result.iterator().next());
    }

    class MyNearestVisitor extends NearestVisitor {

        public MyNearestVisitor(Expression expr, Object valueToMatch) {
            super(expr, valueToMatch);
        }

        @Override
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    @Test
    public void testNearest() throws IOException {
        // test strings
        testNearest("ft1", "stringProperty", "two", "two"); // exact match
        testNearest("ft1", "stringProperty", "aaa", "one"); // below
        testNearest("ft1", "stringProperty", "rrr", "one", "two"); // mid
        testNearest("ft1", "stringProperty", "zzz", "zero"); // above

        // test integer
        testNearest("ft1", "intProperty", 1, 1); // exact match
        testNearest("ft1", "intProperty", -10, 0); // below
        testNearest("ft1", "intProperty", 10, 2); // above

        // test double
        testNearest("ft1", "doubleProperty", 1.1, 1.1); // exact match
        testNearest("ft1", "doubleProperty", -10d, 0d); // below
        testNearest("ft1", "doubleProperty", 1.3, 1.1); // mid
        testNearest("ft1", "doubleProperty", 1.9, 2.2); // mid
        testNearest("ft1", "doubleProperty", 10d, 2.2); // above
    }

    private void testNearest(
            String typeName, String attributeName, Object target, Object... validResults)
            throws IOException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        PropertyName expr = ff.property(aname(attributeName));

        MyNearestVisitor v = new MyNearestVisitor(expr, target);
        dataStore.getFeatureSource(tname(typeName)).accepts(Query.ALL, v, null);
        assertFalse(visited);
        Object nearestMatch = v.getNearestMatch();
        if (validResults.length == 0) {
            assertNull(nearestMatch);
        } else {
            boolean found = false;
            for (Object object : validResults) {
                if (object.equals(Converters.convert(nearestMatch, object.getClass()))) {
                    found = true;
                    break;
                }
            }
            assertTrue(
                    "Could not match nearest "
                            + nearestMatch
                            + " among valid values "
                            + Arrays.asList(validResults),
                    found);
        }
    }

    class MyAverageVisitor extends AverageVisitor {

        public MyAverageVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        @Override
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        @Override
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    @Test
    public void testAverage() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MyAverageVisitor v = new MyAverageVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals(1.1, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testAverageWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MyAverageVisitor v = new MyAverageVisitor(p);

        Filter f = ff.greater(ff.property(aname("doubleProperty")), ff.literal(1));
        Query q = new Query(tname("ft1"), f);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        assertEquals(1.65, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testAverageWithLimitOffset() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isLimitOffsetSupported());
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MyAverageVisitor v = new MyAverageVisitor(p);

        Query q = new Query(tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);

        assertFalse(visited);
        assertEquals(0.55, v.getResult().toDouble(), 0.01);
    }

    class MyStandardDeviationVisitor extends StandardDeviationVisitor {

        public MyStandardDeviationVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        @Override
        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        @Override
        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    @Test
    public void testStandardDeviation() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MyStandardDeviationVisitor v = new MyStandardDeviationVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals(0.89, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testStandardDeviationWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MyStandardDeviationVisitor v = new MyStandardDeviationVisitor(p);
        Filter f = ff.greater(ff.property(aname("doubleProperty")), ff.literal(1));
        Query q = new Query(tname("ft1"), f);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        assertEquals(0.55, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testStandardDeviationWithLimitOffset() throws Exception {
        assumeTrue(dataStore.getSQLDialect().isLimitOffsetSupported());
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MyStandardDeviationVisitor v = new MyStandardDeviationVisitor(p);
        Query q = new Query(tname("ft1"));
        q.setStartIndex(0);
        q.setMaxFeatures(2);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);

        assertFalse(visited);
        assertEquals(0.55, v.getResult().toDouble(), 0.01);
    }

    @Test
    public void testUniqueCount() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new UniqueCountVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        int result = v.getResult().toInt();
        assertEquals(0, v.getUnique().size());
        assertEquals(3, result);
    }

    @Test
    public void testUniqueCountWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueCountVisitor v = new UniqueCountVisitor(p);
        Filter f = ff.greater(ff.property(aname("doubleProperty")), ff.literal(1));
        Query q = new Query(tname("ft1"), f);
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getUnique();
        assertEquals(0, result.size());
        assertEquals(2, v.getResult().toInt());
    }

    @Test
    public void testUniqueCountWithLimit() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new UniqueCountVisitor(p);
        v.setMaxFeatures(2);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        int result = v.getResult().toInt();
        assertEquals(0, v.getUnique().size());
        assertEquals(2, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUniqueMultipleAttributes() throws Exception {
        UniqueVisitor v = new UniqueVisitor("stringProperty", "doubleProperty");
        dataStore.getFeatureSource(tname("ft4")).accepts(Query.ALL, v, null);
        Set result = v.getResult().toSet();

        // makes sure types of numeric values are of the same type. Eg. Oracle DB
        // uses BigDecimal and BigInteger instead of Double and Integer.
        convertNumbers(result, new int[] {1}, new Class<?>[] {Double.class});

        Set expected = new HashSet();
        addValues(expected, "zero", 0.0);
        addValues(expected, "one", 1.1);
        addValues(expected, "one_2", 1.1);
        addValues(expected, "two", 2.2);
        addValues(expected, "two_2", 2.2);
        addValues(expected, "three", 3.3);
        assertEquals(expected.size(), result.size());
        assertTrue(expected.containsAll(result));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUniqueMultipleAttributes2() throws Exception {
        UniqueVisitor v = new UniqueVisitor("intProperty", "doubleProperty");
        dataStore.getFeatureSource(tname("ft4")).accepts(Query.ALL, v, null);
        Set result = v.getResult().toSet();

        // makes sure types of numeric values are of the same type. Eg. Oracle DB
        // uses BigDecimal and BigInteger instead of Double and Integer.
        convertNumbers(result, new int[] {0, 1}, new Class<?>[] {Integer.class, Double.class});
        Set expected = new HashSet();
        addValues(expected, 0, 0.0);
        addValues(expected, 1, 1.1);
        addValues(expected, 2, 2.2);
        addValues(expected, 3, 3.3);
        assertEquals(expected.size(), result.size());
        assertTrue(expected.containsAll(result));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUniqueMultipleAttributes3() throws Exception {
        UniqueVisitor v = new UniqueVisitor("intProperty", "doubleProperty", "stringProperty");
        dataStore.getFeatureSource(tname("ft4")).accepts(Query.ALL, v, null);
        Set result = v.getResult().toSet();

        // makes sure types of numeric values are of the same type. Eg. Oracle DB
        // uses BigDecimal and BigInteger instead of Double and Integer.
        convertNumbers(result, new int[] {0, 1}, new Class<?>[] {Integer.class, Double.class});

        Set expected = new HashSet();
        addValues(expected, 0, 0.0, "zero");
        addValues(expected, 1, 1.1, "one");
        addValues(expected, 1, 1.1, "one_2");
        addValues(expected, 2, 2.2, "two");
        addValues(expected, 2, 2.2, "two_2");
        addValues(expected, 3, 3.3, "three");
        assertEquals(expected.size(), result.size());
        assertTrue(expected.containsAll(result));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUniqueMultipleAttrOneResult() throws Exception {
        // check that when the result is a single pair the result is returned as a single entry List
        // and not as separate values.
        UniqueVisitor v = new UniqueVisitor("intProperty", "doubleProperty");
        v.setMaxFeatures(1);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        Set result = v.getResult().toSet();
        assertEquals(1, result.size());
        assertTrue(result.iterator().next() instanceof List);
    }

    @SuppressWarnings("unchecked")
    private void addValues(Set set, Object... values) {
        LinkedList<Object> list =
                Arrays.stream(values).collect(Collectors.toCollection(LinkedList::new));
        set.add(list);
    }

    @SuppressWarnings("unchecked")
    private void convertNumbers(Set values, int[] indexes, Class<?>[] clazz) {
        for (Object o : values) {
            List uniques = (List) o;
            for (int i = 0; i < indexes.length; i++) {
                Object val = uniques.get(indexes[i]);
                Object result = Converters.convert(val, clazz[i]);
                uniques.set(indexes[i], result);
            }
        }
    }
}
