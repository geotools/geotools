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
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.PropertyName;

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

    @Test
    public void testRepeatedOr() {
        PropertyName p = ff.property("prop");
        Or orFilter =
                ff.or(
                        Arrays.asList(
                                ff.equal(p, ff.literal("zero"), true),
                                ff.equal(p, ff.literal("one"), true),
                                ff.equal(p, ff.literal("two"), true)));

        NullHandlingVisitor nh = new NullHandlingVisitor();
        Filter nhResult = (Filter) orFilter.accept(nh, null);
        Filter expected = ff.and(Arrays.asList(orFilter, ff.not(ff.isNull(p))));
        assertEquals(expected, nhResult);
    }

    @Test
    public void testRepeatedMixedOr() {
        PropertyName spp = ff.property("sp");
        PropertyName ipp = ff.property("ip");
        PropertyName dpp = ff.property("dp");
        Or orFilter =
                ff.or(
                        Arrays.asList(
                                ff.equal(spp, ff.literal("zero"), true),
                                ff.equal(ipp, ff.literal(1), true),
                                ff.equal(dpp, ff.literal(0d), true),
                                ff.equal(spp, ff.literal("two"), true),
                                ff.equal(ipp, ff.literal(2), true)));
        NullHandlingVisitor nh = new NullHandlingVisitor();
        Filter nhResult = (Filter) orFilter.accept(nh, null);
        Filter expected =
                ff.or(
                        Arrays.asList(
                                ff.and(
                                        ff.or(
                                                ff.equal(spp, ff.literal("zero"), true),
                                                ff.equal(spp, ff.literal("two"), true)),
                                        ff.not(ff.isNull(spp))),
                                ff.and(
                                        ff.or(
                                                ff.equal(ipp, ff.literal(1), true),
                                                ff.equal(ipp, ff.literal(2), true)),
                                        ff.not(ff.isNull(ipp))),
                                ff.and(
                                        ff.equal(dpp, ff.literal(0d), true),
                                        ff.not(ff.isNull(dpp)))));
        assertEquals(expected, nhResult);
    }
}
