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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.feature.visitor.GroupByVisitorBuilder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.NearestVisitor;
import org.geotools.feature.visitor.SumAreaVisitor;
import org.geotools.feature.visitor.SumVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.SortOrder;
import org.geotools.filter.function.FilterFunction_area;
import org.geotools.util.Converters;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;

public abstract class JDBCAggregateFunctionOnlineTest extends JDBCTestSupport {

    boolean visited = false;

    @Override
    protected abstract JDBCAggregateTestSetup createTestSetup();

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        visited = false;
    }

    class MySumVisitor extends SumVisitor {

        public MySumVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

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

        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    public void testSumArea() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("geom"));

        SumAreaVisitor v = new MySumAreaVisitor(p);
        dataStore.getFeatureSource(tname("aggregate")).accepts(Query.ALL, v, null);
        assertEquals(
                visited, !dataStore.getFilterCapabilities().supports(FilterFunction_area.class));
        assertEquals(30.0, v.getResult().toDouble(), 0.01);
    }

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
        if (dataStore.getSupportedFunctions().containsKey(FilterFunction_area.NAME.getName())) {
            assertFalse(visited);
        }
        List groups = v.getResult().toList();
        assertEquals(20.0, (Double) ((Object[]) groups.get(0))[1], 0.01);
        assertEquals(10.0, (Double) ((Object[]) groups.get(1))[1], 0.01);
    }

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

    public void testSumWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
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

        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    public void testMax() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MaxVisitor v = new MyMaxVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals(2.2, v.getResult().toDouble(), 0.01);
    }

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

    public void testMaxWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
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

        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

    public void testMin() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("doubleProperty"));

        MinVisitor v = new MyMinVisitor(p);
        dataStore.getFeatureSource(tname("ft1")).accepts(Query.ALL, v, null);
        assertFalse(visited);
        assertEquals(0.0, v.getResult().toDouble(), 0.01);
    }

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

    public void testMinWithLimitOffset() throws Exception {
        if (!dataStore.getSQLDialect().isLimitOffsetSupported()) {
            return;
        }
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

        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

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

    public void testUniqueWithLimitOffset() throws Exception {

        if (!dataStore.getSQLDialect().isLimitOffsetSupported()
                || !dataStore.getSQLDialect().isAggregatedSortSupported("distinct")) {
            return;
        }

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

    public void testUniqueWithLimitOnVisitor() throws Exception {

        if (!dataStore.getSQLDialect().isLimitOffsetSupported()
                || !dataStore.getSQLDialect().isAggregatedSortSupported("distinct")) {
            return;
        }

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new MyUniqueVisitor(p);
        v.setPreserveOrder(true);
        v.setStartIndex(0);
        v.setMaxFeatures(2);
        Query q = new Query(tname("ft1"));
        q.setSortBy(new SortBy[] {new SortByImpl(p, SortOrder.ASCENDING)});
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(2, result.size());
        assertEquals("one", result.iterator().next());
    }

    public void testStoreChecksVisitorLimits() throws Exception {

        if (!dataStore.getSQLDialect().isLimitOffsetSupported()
                || !dataStore.getSQLDialect().isAggregatedSortSupported("distinct")) {
            return;
        }

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
        q.setSortBy(new SortBy[] {new SortByImpl(p, SortOrder.ASCENDING)});
        dataStore.getFeatureSource(tname("ft1")).accepts(q, v, null);
        assertFalse(visited);
        Set result = v.getResult().toSet();
        assertEquals(3, result.size());
        assertEquals("one", result.iterator().next());
    }

    public void testUniqueWithLimitOffsetOnVisitor() throws Exception {

        if (!dataStore.getSQLDialect().isLimitOffsetSupported()
                || !dataStore.getSQLDialect().isAggregatedSortSupported("distinct")) {
            return;
        }

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property(aname("stringProperty"));

        UniqueVisitor v = new MyUniqueVisitor(p);
        v.setPreserveOrder(true);
        v.setStartIndex(1);
        v.setMaxFeatures(2);
        Query q = new Query(tname("ft1"));
        q.setSortBy(new SortBy[] {new SortByImpl(p, SortOrder.ASCENDING)});
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

        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }
    }

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
}
