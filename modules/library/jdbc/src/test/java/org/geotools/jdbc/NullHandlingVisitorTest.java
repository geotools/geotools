package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;

public class NullHandlingVisitorTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void testBetween() {
        PropertyIsBetween between =
                ff.between(ff.property("a"), ff.property("b"), ff.property("c"));
        NullHandlingVisitor visitor = new NullHandlingVisitor();
        Filter result = (Filter) between.accept(visitor, null);
        assertTrue(result instanceof And);
        Filter expected =
                ff.and(
                        Arrays.asList(
                                between,
                                propertyNotNull("a"),
                                propertyNotNull("b"),
                                propertyNotNull("c")));
        assertEquals(expected, result);

        between = ff.between(ff.property("a"), ff.property("b"), ff.literal(10));
        result = (Filter) between.accept(visitor, null);
        assertTrue(result instanceof And);
        expected = ff.and(Arrays.asList(between, propertyNotNull("a"), propertyNotNull("b")));
        assertEquals(expected, result);

        between = ff.between(ff.property("a"), ff.literal(5), ff.literal(10));
        result = (Filter) between.accept(visitor, null);
        assertTrue(result instanceof And);
        expected = ff.and(Arrays.asList(between, propertyNotNull("a")));
        assertEquals(expected, result);

        between = ff.between(ff.literal(7), ff.literal(5), ff.literal(10));
        result = (Filter) between.accept(visitor, null);
        assertEquals(between, result);
    }

    @Test
    public void testLike() {
        PropertyIsLike like = ff.like(ff.property("a"), "*A*");
        NullHandlingVisitor visitor = new NullHandlingVisitor();
        Filter result = (Filter) like.accept(visitor, null);
        assertTrue(result instanceof And);
        Filter expected = ff.and(like, propertyNotNull("a"));
        assertEquals(expected, result);

        like = ff.like(ff.literal("a"), "*A*");
        result = (Filter) like.accept(visitor, null);
        assertEquals(like, result);
    }

    @Test
    public void testBinaryComparison() {
        PropertyIsEqualTo equal = ff.equal(ff.property("a"), ff.property("b"), false);
        NullHandlingVisitor visitor = new NullHandlingVisitor();
        Filter result = (Filter) equal.accept(visitor, null);
        assertTrue(result instanceof And);
        Filter expected = ff.and(Arrays.asList(equal, propertyNotNull("a"), propertyNotNull("b")));
        assertEquals(expected, result);

        equal = ff.equal(ff.property("a"), ff.literal(10), false);
        result = (Filter) equal.accept(visitor, null);
        assertTrue(result instanceof And);
        expected = ff.and(Arrays.asList(equal, propertyNotNull("a")));
        assertEquals(expected, result);

        equal = ff.equal(ff.literal(10), ff.property("b"), false);
        result = (Filter) equal.accept(visitor, null);
        assertTrue(result instanceof And);
        expected = ff.and(Arrays.asList(equal, propertyNotNull("b")));
        assertEquals(expected, result);

        equal = ff.equal(ff.literal(10), ff.literal(20), false);
        result = (Filter) equal.accept(visitor, null);
        assertEquals(equal, result);
    }

    @Test
    public void testNullableAttributes() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.nillable(true);
        tb.add("a", String.class);
        tb.nillable(false);
        tb.add("b", String.class);
        tb.nillable(true);
        tb.add("c", String.class);
        tb.setName("test");
        SimpleFeatureType schema = tb.buildFeatureType();

        PropertyIsBetween between =
                ff.between(ff.property("a"), ff.property("b"), ff.property("c"));
        NullHandlingVisitor visitor = new NullHandlingVisitor(schema);
        Filter result = (Filter) between.accept(visitor, null);
        assertTrue(result instanceof And);
        Filter expected =
                ff.and(Arrays.asList(between, propertyNotNull("a"), propertyNotNull("c")));
        assertEquals(expected, result);
    }

    @Test
    public void testSimplifyRedundant() {
        PropertyIsBetween between =
                ff.between(ff.property("a"), ff.property("b"), ff.property("c"));
        PropertyIsEqualTo equal = ff.equal(ff.property("a"), ff.property("b"), false);
        And and = ff.and(between, equal);

        NullHandlingVisitor nh = new NullHandlingVisitor();
        Filter nhResult = (Filter) and.accept(nh, null);
        SimplifyingFilterVisitor simplifier = new SimplifyingFilterVisitor();
        Filter simplified = (Filter) nhResult.accept(simplifier, null);
        assertTrue(simplified instanceof And);
        Filter expected =
                ff.and(
                        Arrays.asList(
                                between,
                                equal,
                                propertyNotNull("a"),
                                propertyNotNull("b"),
                                propertyNotNull("c")));
        assertEquals(expected, simplified);
    }

    private Not propertyNotNull(String name) {
        return ff.not(ff.isNull(ff.property(name)));
    }
}
