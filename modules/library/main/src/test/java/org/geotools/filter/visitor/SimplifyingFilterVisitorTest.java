/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.function.math.FilterFunction_random;
import org.geotools.filter.visitor.SimplifyingFilterVisitor.FIDValidator;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.Identifier;

public class SimplifyingFilterVisitorTest extends TestCase {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    Id emptyFid;
    SimplifyingFilterVisitor simpleVisitor;
    SimplifyingFilterVisitor complexVisitor;
    PropertyIsEqualTo property;

    @Override
    protected void setUp() throws Exception {
        emptyFid = ff.id(new HashSet<Identifier>());
        property = ff.equal(ff.property("test"), ff.literal("oneTwoThree"), false);
        // visitor assuming simple features
        simpleVisitor =
                new SimplifyingFilterVisitor() {
                    protected boolean isSimpleFeature() {
                        return true;
                    };
                };
        simpleVisitor.setRangeSimplicationEnabled(true);
        // one that does not know, and thus assumes complex ones
        complexVisitor = new SimplifyingFilterVisitor();
    }

    @Override
    protected void tearDown() throws Exception {
        EnvFunction.clearLocalValues();
    }

    public void testIncludeAndInclude() {
        Filter result = (Filter) ff.and(Filter.INCLUDE, Filter.INCLUDE).accept(simpleVisitor, null);
        assertEquals(Filter.INCLUDE, result);
    }

    public void testIncludeAndExclude() {
        Filter result = (Filter) ff.and(Filter.INCLUDE, Filter.EXCLUDE).accept(simpleVisitor, null);
        assertEquals(Filter.EXCLUDE, result);
    }

    public void testExcludeAndExclude() {
        Filter result = (Filter) ff.and(Filter.EXCLUDE, Filter.EXCLUDE).accept(simpleVisitor, null);
        assertEquals(Filter.EXCLUDE, result);
    }

    public void testIncludeAndProperty() {
        Filter result = (Filter) ff.and(Filter.INCLUDE, property).accept(simpleVisitor, null);
        assertEquals(property, result);
    }

    public void testExcludeAndProperty() {
        Filter result = (Filter) ff.or(Filter.EXCLUDE, property).accept(simpleVisitor, null);
        assertEquals(property, result);
    }

    public void testIncludeOrInclude() {
        Filter result = (Filter) ff.or(Filter.INCLUDE, Filter.INCLUDE).accept(simpleVisitor, null);
        assertEquals(Filter.INCLUDE, result);
    }

    public void testIncludeOrExclude() {
        Filter result = (Filter) ff.or(Filter.INCLUDE, Filter.EXCLUDE).accept(simpleVisitor, null);
        assertEquals(Filter.INCLUDE, result);
    }

    public void testExcludeOrExclude() {
        Filter result = (Filter) ff.or(Filter.EXCLUDE, Filter.EXCLUDE).accept(simpleVisitor, null);
        assertEquals(Filter.EXCLUDE, result);
    }

    public void testIncludeOrProperty() {
        Filter result = (Filter) ff.or(Filter.INCLUDE, property).accept(simpleVisitor, null);
        assertEquals(Filter.INCLUDE, result);
    }

    public void testExcludeOrProperty() {
        Filter result = (Filter) ff.or(Filter.EXCLUDE, property).accept(simpleVisitor, null);
        assertEquals(property, result);
    }

    public void testEmptyFid() {
        Filter result = (Filter) emptyFid.accept(simpleVisitor, null);
        assertEquals(Filter.EXCLUDE, result);
    }

    public void testRecurseAnd() {
        Filter test = ff.and(Filter.INCLUDE, ff.or(property, Filter.EXCLUDE));
        assertEquals(property, test.accept(simpleVisitor, null));
    }

    public void testRecurseOr() {
        Filter test = ff.or(Filter.EXCLUDE, ff.and(property, Filter.INCLUDE));
        assertEquals(property, test.accept(simpleVisitor, null));
    }

    public void testFidValidity() {
        simpleVisitor.setFIDValidator(
                new SimplifyingFilterVisitor.FIDValidator() {
                    public boolean isValid(String fid) {
                        return fid.startsWith("pass");
                    }
                });

        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(ff.featureId("notPass"));
        Id filter = ff.id(ids);

        assertEquals(Filter.EXCLUDE, filter.accept(simpleVisitor, null));

        ids.add(ff.featureId("pass1"));
        ids.add(ff.featureId("pass2"));
        filter = ff.id(ids);

        Set<Identifier> validIds = new HashSet<Identifier>();
        validIds.add(ff.featureId("pass2"));
        validIds.add(ff.featureId("pass1"));
        Filter expected = ff.id(validIds);
        assertEquals(expected, filter.accept(simpleVisitor, null));
    }

    public void testRegExFIDValidator() {
        FIDValidator validator = new SimplifyingFilterVisitor.RegExFIDValidator("abc\\.\\d+");
        simpleVisitor.setFIDValidator(validator);

        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(ff.featureId("abc.."));
        ids.add(ff.featureId(".abc.1"));
        ids.add(ff.featureId("abc.123"));
        ids.add(ff.featureId("abc.ax"));
        Id filter = ff.id(ids);
        Filter result = (Filter) filter.accept(simpleVisitor, null);
        Filter expected = ff.id(Collections.singleton(ff.featureId("abc.123")));

        assertEquals(expected, result);
    }

    public void testTypeNameDotNumberValidator() {
        final String typeName = "states";
        FIDValidator validator;
        validator = new SimplifyingFilterVisitor.TypeNameDotNumberFidValidator(typeName);
        simpleVisitor.setFIDValidator(validator);

        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(ff.featureId("_states"));
        ids.add(ff.featureId("states.abc"));
        ids.add(ff.featureId("states.."));
        ids.add(ff.featureId("states.123"));
        Id filter = ff.id(ids);
        Filter result = (Filter) filter.accept(simpleVisitor, null);
        Filter expected = ff.id(Collections.singleton(ff.featureId("states.123")));

        assertEquals(expected, result);
    }

    public void testNegateEquals() {
        Filter f = ff.not(ff.equals(ff.property("prop"), ff.literal(10)));
        Filter result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(ff.notEqual(ff.property("prop"), ff.literal(10)), result);
        // not simplified for complex features
        result = (Filter) f.accept(complexVisitor, null);
        assertEquals(f, result);
    }

    public void testNegateGreater() {
        Filter f = ff.not(ff.greater(ff.property("prop"), ff.literal(10)));
        Filter result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(ff.lessOrEqual(ff.property("prop"), ff.literal(10)), result);
        // not simplified for complex features
        result = (Filter) f.accept(complexVisitor, null);
        assertEquals(f, result);
    }

    public void testNegateGreaterOrEqual() {
        Filter f = ff.not(ff.greaterOrEqual(ff.property("prop"), ff.literal(10)));
        Filter result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(ff.less(ff.property("prop"), ff.literal(10)), result);
        // not simplified for complex features
        result = (Filter) f.accept(complexVisitor, null);
        assertEquals(f, result);
    }

    public void testNegateLess() {
        Filter f = ff.not(ff.less(ff.property("prop"), ff.literal(10)));
        Filter result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(ff.greaterOrEqual(ff.property("prop"), ff.literal(10)), result);
        // not simplified for complex features
        result = (Filter) f.accept(complexVisitor, null);
        assertEquals(f, result);
    }

    public void testNegateLessOrEqual() {
        Filter f = ff.not(ff.lessOrEqual(ff.property("prop"), ff.literal(10)));
        Filter result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(ff.greater(ff.property("prop"), ff.literal(10)), result);
        // not simplified for complex features
        result = (Filter) f.accept(complexVisitor, null);
        assertEquals(f, result);
    }

    public void testNegateBetween() {
        PropertyName prop = ff.property("prop");
        Literal l10 = ff.literal(10);
        Literal l20 = ff.literal(20);
        Filter f = ff.not(ff.between(prop, l10, l20));
        Filter result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(
                ff.or(
                        Arrays.asList(
                                (Filter) ff.less(prop, l10),
                                ff.greater(prop, l20),
                                ff.isNull(prop))),
                result);
    }

    public void testNegateBetweenWithNullability() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.nillable(true);
        tb.add("prop", String.class);
        tb.nillable(false);
        tb.add("prop2", String.class);
        tb.setName("test");

        // when FeatureType is null
        PropertyName prop = ff.property("prop");
        Literal l10 = ff.literal(10);
        Literal l20 = ff.literal(20);
        Filter f = ff.not(ff.between(prop, l10, l20));
        Filter result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(
                ff.or(
                        Arrays.asList(
                                (Filter) ff.less(prop, l10),
                                ff.greater(prop, l20),
                                ff.isNull(prop))),
                result);

        // when FeatureType is not null and property is nullable
        simpleVisitor.setFeatureType(tb.buildFeatureType());
        prop = ff.property("prop");
        f = ff.not(ff.between(prop, l10, l20));
        result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(
                ff.or(
                        Arrays.asList(
                                (Filter) ff.less(prop, l10),
                                ff.greater(prop, l20),
                                ff.isNull(prop))),
                result);

        // when FeatureType is not null and property is not nullable
        prop = ff.property("prop2");
        f = ff.not(ff.between(prop, l10, l20));
        result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(
                ff.or(Arrays.asList((Filter) ff.less(prop, l10), ff.greater(prop, l20))), result);
    }

    public void testDoubleNegation() {
        PropertyIsEqualTo equal = ff.equals(ff.property("prop"), ff.literal(10));
        Filter f = ff.not(ff.not(equal));
        Filter result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(equal, result);
    }

    public void testTripleNegation() {
        PropertyIsEqualTo equal = ff.equals(ff.property("prop"), ff.literal(10));
        Filter f = ff.not(ff.not(ff.not(equal)));
        Filter result = (Filter) f.accept(simpleVisitor, null);
        assertEquals(ff.notEqual(ff.property("prop"), ff.literal(10)), result);
    }

    public void testStableFunction() {
        EnvFunction.setLocalValue("var", "123");
        Function f = ff.function("env", ff.literal("var"));

        Expression result = (Expression) f.accept(simpleVisitor, null);
        assertTrue(result instanceof Literal);
        assertEquals("123", result.evaluate(null, String.class));
    }

    public void testVolatileFunction() {
        Function f = ff.function("random");

        Expression result = (Expression) f.accept(simpleVisitor, null);
        assertTrue(result instanceof FilterFunction_random);
    }

    public void testNestedVolatile() {
        EnvFunction.setLocalValue("power", 3);
        Function f =
                ff.function("pow", ff.function("random"), ff.function("env", ff.literal("power")));

        Function result = (Function) f.accept(simpleVisitor, null);
        // main function not simplified out
        assertEquals("pow", result.getName());
        // first argument not simplified out
        Function param1 = (Function) result.getParameters().get(0);
        assertEquals("random", param1.getName());
        // second argument simplified out
        Expression param2 = result.getParameters().get(1);
        assertTrue(param2 instanceof Literal);
        assertEquals(Integer.valueOf(3), param2.evaluate(null, Integer.class));
    }

    public void testCompareFunctionNull() {
        Function f = ff.function("env", ff.literal("var"));
        PropertyIsEqualTo filter = ff.equal(f, ff.literal("test"), false);

        Filter simplified = (Filter) filter.accept(simpleVisitor, null);
        assertEquals(Filter.EXCLUDE, simplified);
    }

    public void testCompareConstantFunction() {
        EnvFunction.setLocalValue("var", "test");
        Function f = ff.function("env", ff.literal("var"));
        PropertyIsEqualTo filter = ff.equal(f, ff.literal("test"), false);

        Filter simplified = (Filter) filter.accept(simpleVisitor, null);
        assertEquals(Filter.INCLUDE, simplified);
    }

    public void testSimplifyStaticExclude() {
        assertEquals(Filter.EXCLUDE, simplify(ff.greater(ff.literal(3), ff.literal(5))));
        assertEquals(Filter.EXCLUDE, simplify(ff.greaterOrEqual(ff.literal(3), ff.literal(5))));
        assertEquals(Filter.EXCLUDE, simplify(ff.less(ff.literal(5), ff.literal(3))));
        assertEquals(Filter.EXCLUDE, simplify(ff.lessOrEqual(ff.literal(5), ff.literal(3))));
        assertEquals(Filter.EXCLUDE, simplify(ff.equal(ff.literal(5), ff.literal(3), true)));
        assertEquals(
                Filter.EXCLUDE, simplify(ff.between(ff.literal(3), ff.literal(1), ff.literal(2))));
    }

    public void testSimplifyStaticInclude() {
        assertEquals(Filter.INCLUDE, simplify(ff.less(ff.literal(3), ff.literal(5))));
        assertEquals(Filter.INCLUDE, simplify(ff.lessOrEqual(ff.literal(3), ff.literal(5))));
        assertEquals(Filter.INCLUDE, simplify(ff.greater(ff.literal(5), ff.literal(3))));
        assertEquals(Filter.INCLUDE, simplify(ff.greaterOrEqual(ff.literal(5), ff.literal(3))));
        assertEquals(Filter.INCLUDE, simplify(ff.equal(ff.literal(5), ff.literal(5), true)));
        assertEquals(
                Filter.INCLUDE, simplify(ff.between(ff.literal(3), ff.literal(1), ff.literal(4))));
    }

    private Filter simplify(Filter filter) {
        return (Filter) filter.accept(new SimplifyingFilterVisitor(), null);
    }

    public void testCoalesheNestedAnd() {
        Filter eq = ff.equal(ff.property("A"), ff.literal("3"), true);
        Filter gt = ff.greater(ff.property("b"), ff.literal("3"));
        Filter lt = ff.less(ff.property("c"), ff.literal("5"));
        And nested = ff.and(Arrays.asList(ff.and(Arrays.asList(eq, gt)), lt));

        And simplified = (And) nested.accept(simpleVisitor, null);
        assertEquals(3, simplified.getChildren().size());
        assertEquals(ff.and(Arrays.asList(eq, gt, lt)), simplified);
    }

    public void testCoalesheNestedOr() {
        Filter eq = ff.equal(ff.property("A"), ff.literal("3"), true);
        Filter gt = ff.greater(ff.property("b"), ff.literal("3"));
        Filter lt = ff.less(ff.property("c"), ff.literal("5"));
        Or nested = ff.or(Arrays.asList(ff.or(Arrays.asList(eq, gt)), lt));

        Or simplified = (Or) nested.accept(simpleVisitor, null);
        assertEquals(3, simplified.getChildren().size());
        assertEquals(ff.or(Arrays.asList(eq, gt, lt)), simplified);
    }

    public void testDualFilterOr() {
        Or or =
                ff.or(
                        Arrays.asList(
                                ff.not(ff.equal(ff.property("a"), ff.literal(3), true)),
                                ff.equal(ff.property("a"), ff.literal(3), true)));
        assertEquals(Filter.INCLUDE, or.accept(simpleVisitor, null));
    }

    public void testDualFilterAnd() {
        Filter original =
                ff.and(
                        Arrays.asList(
                                ff.not(ff.equal(ff.property("a"), ff.literal(3), true)),
                                ff.equal(ff.property("a"), ff.literal(3), true)));
        assertEquals(Filter.EXCLUDE, original.accept(simpleVisitor, null));
    }

    public void testDualFilterNullAnd() {
        Filter original =
                ff.and(
                        Arrays.asList(
                                ff.not(ff.isNull(ff.property("a"))), ff.isNull(ff.property("a"))));
        assertEquals(Filter.EXCLUDE, original.accept(simpleVisitor, null));
    }

    public void testDualFilterNullOr() {
        Filter original =
                ff.or(
                        Arrays.asList(
                                ff.not(ff.isNull(ff.property("a"))), ff.isNull(ff.property("a"))));
        assertEquals(Filter.INCLUDE, original.accept(simpleVisitor, null));
    }

    public void testRepeatedFilter() {
        Filter f1 = ff.equal(ff.property("a"), ff.literal(3), false);
        Filter f2 = ff.equal(ff.property("a"), ff.literal(3), false);

        Filter s1 = (Filter) ff.and(f1, f2).accept(simpleVisitor, null);
        assertEquals(f1, s1);
        Filter s2 = (Filter) ff.or(f1, f2).accept(simpleVisitor, null);
        assertEquals(f1, s2);

        Filter f3 = ff.greater(ff.property("a"), ff.property("b"));

        Filter s3 = (Filter) ff.and(Arrays.asList(f1, f2, f3)).accept(simpleVisitor, null);
        assertEquals(ff.and(Arrays.asList(f1, f3)), s3);
        Filter s4 = (Filter) ff.and(Arrays.asList(f3, f1, f2)).accept(simpleVisitor, null);
        assertEquals(ff.and(Arrays.asList(f3, f1)), s4);
        Filter s5 = (Filter) ff.and(Arrays.asList(f1, f3, f2)).accept(simpleVisitor, null);
        assertEquals(ff.and(Arrays.asList(f1, f3)), s5);

        Filter s6 = (Filter) ff.or(Arrays.asList(f1, f2, f3)).accept(simpleVisitor, null);
        assertEquals(ff.or(Arrays.asList(f1, f3)), s6);
        Filter s7 = (Filter) ff.or(Arrays.asList(f3, f1, f2)).accept(simpleVisitor, null);
        assertEquals(ff.or(Arrays.asList(f3, f1)), s7);
        Filter s8 = (Filter) ff.or(Arrays.asList(f1, f3, f2)).accept(simpleVisitor, null);
        assertEquals(ff.or(Arrays.asList(f1, f3)), s8);
    }

    public void testAndDisjointRanges() throws Exception {
        testAndDisjointRanges(Integer.class, 10, 5);
        testAndDisjointRanges(Byte.class, (byte) 10, (byte) 5);
        testAndDisjointRanges(Long.class, 10l, 5l);
        testAndDisjointRanges(Float.class, 10f, 5f);
        testAndDisjointRanges(Double.class, 10d, 5d);
        testAndDisjointRanges(String.class, "ppp", "bbb");
    }

    private <T> void testAndDisjointRanges(Class<T> type, T max, T min) throws Exception {
        SimpleFeatureType schema = DataUtilities.createType("test", "a:" + type.getName());
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setRangeSimplicationEnabled(true);

        Filter original =
                ff.and(
                        ff.greater(ff.property("a"), ff.literal(max)),
                        ff.less(ff.property("a"), ff.literal(min)));
        Filter simplified = (Filter) original.accept(visitor, null);
        assertEquals(original, simplified);

        visitor.setFeatureType(schema);
        Filter simplified2 = (Filter) original.accept(visitor, null);
        assertEquals(Filter.EXCLUDE, simplified2);
    }

    public void testOrDisjointRanges() throws Exception {
        testOrDisjointRanges(Integer.class, 10, 5);
        testOrDisjointRanges(Byte.class, (byte) 10, (byte) 5);
        testOrDisjointRanges(Long.class, 10l, 5l);
        testOrDisjointRanges(Float.class, 10f, 5f);
        testOrDisjointRanges(Double.class, 10d, 5d);
        testOrDisjointRanges(String.class, "ppp", "bbb");
    }

    private <T> void testOrDisjointRanges(Class<T> type, T max, T min) throws Exception {
        SimpleFeatureType schema = DataUtilities.createType("test", "a:" + type.getName());
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setFeatureType(schema);

        Filter original =
                ff.or(
                        ff.greater(ff.property("a"), ff.literal(max)),
                        ff.less(ff.property("a"), ff.literal(min)));
        Filter simplified = (Filter) original.accept(visitor, null);
        assertEquals(original, simplified);
    }

    public void testAndTouchingRanges() throws Exception {
        testAndTouchingRanges(Integer.class, 10);
        testAndTouchingRanges(Byte.class, (byte) 10);
        testAndTouchingRanges(Long.class, 10l);
        testAndTouchingRanges(Float.class, 10f);
        testAndTouchingRanges(Double.class, 10d);
        testAndTouchingRanges(String.class, "ppp");
    }

    private <T> void testAndTouchingRanges(Class<T> type, T value) throws Exception {
        SimpleFeatureType schema = DataUtilities.createType("test", "a:" + type.getName());
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setRangeSimplicationEnabled(true);
        visitor.setFeatureType(schema);

        Filter original =
                ff.and(
                        ff.greaterOrEqual(ff.property("a"), ff.literal(value)),
                        ff.lessOrEqual(ff.property("a"), ff.literal(value)));
        Filter simplified = (Filter) original.accept(visitor, null);
        assertEquals(ff.equal(ff.property("a"), ff.literal(value), false), simplified);
    }

    public void testOrTouchingRanges() throws Exception {
        testOrTouchingRanges(Integer.class, 10);
        testOrTouchingRanges(Byte.class, (byte) 10);
        testOrTouchingRanges(Long.class, 10l);
        testOrTouchingRanges(Float.class, 10f);
        testOrTouchingRanges(Double.class, 10d);
        testOrTouchingRanges(String.class, "ppp");
    }

    private <T> void testOrTouchingRanges(Class<T> type, T value) throws Exception {
        SimpleFeatureType schema = DataUtilities.createType("test", "a:" + type.getName());
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setRangeSimplicationEnabled(true);
        visitor.setFeatureType(schema);

        Filter original =
                ff.or(
                        ff.greaterOrEqual(ff.property("a"), ff.literal(value)),
                        ff.lessOrEqual(ff.property("a"), ff.literal(value)));
        Filter simplified = (Filter) original.accept(visitor, null);
        assertEquals(Filter.INCLUDE, simplified);
    }

    public void testAndOverlappingRanges() throws Exception {
        testAndOverlappingRanges(Integer.class, 5, 7, 10);
        testAndOverlappingRanges(Byte.class, (byte) 5, (byte) 7, (byte) 10);
        testAndOverlappingRanges(Long.class, 5l, 7l, 10l);
        testAndOverlappingRanges(Float.class, 5f, 7f, 10f);
        testAndOverlappingRanges(Double.class, 5d, 7d, 10d);
        testAndOverlappingRanges(String.class, "bbb", "nnn", "ppp");
    }

    private <T> void testAndOverlappingRanges(Class<T> type, T min, T mid, T max) throws Exception {
        SimpleFeatureType schema = DataUtilities.createType("test", "a:" + type.getName());
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setRangeSimplicationEnabled(true);
        visitor.setFeatureType(schema);

        // excluding extrema, not possible to turn it into a between filter
        Filter original =
                ff.and(
                        Arrays.asList( //
                                (Filter) ff.greater(ff.property("a"), ff.literal(min)), //
                                ff.less(ff.property("a"), ff.literal(max)), //
                                ff.less(ff.property("a"), ff.literal(mid))));

        Filter simplified = (Filter) original.accept(visitor, null);
        assertEquals(
                ff.and(
                        ff.greater(ff.property("a"), ff.literal(min)), //
                        ff.less(ff.property("a"), ff.literal(mid))),
                simplified);
    }

    public void testOrOverlappingRanges() throws Exception {
        testOrOverlappingRanges(Integer.class, 5, 10);
        testOrOverlappingRanges(Byte.class, (byte) 5, (byte) 10);
        testOrOverlappingRanges(Long.class, 5l, 10l);
        testOrOverlappingRanges(Float.class, 5f, 10f);
        testOrOverlappingRanges(Double.class, 5d, 10d);
        testOrOverlappingRanges(String.class, "bbb", "ppp");
    }

    private <T> void testOrOverlappingRanges(Class<T> type, T min, T max) throws Exception {
        SimpleFeatureType schema = DataUtilities.createType("test", "a:" + type.getName());
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setRangeSimplicationEnabled(true);
        visitor.setFeatureType(schema);

        // excluding extrema, not possible to turn it into a between filter
        Filter original =
                ff.or(
                        Arrays.asList( //
                                (Filter) ff.greater(ff.property("a"), ff.literal(min)), //
                                ff.greater(ff.property("a"), ff.literal(max))));

        Filter simplified = (Filter) original.accept(visitor, null);
        assertEquals(ff.greater(ff.property("a"), ff.literal(min)), simplified);
    }

    public void testAndOverlappingRangesToBetween() throws Exception {
        testAndOverlappingRangesToBetween(Integer.class, 5, 7, 10);
        testAndOverlappingRangesToBetween(Byte.class, (byte) 5, (byte) 7, (byte) 10);
        testAndOverlappingRangesToBetween(Long.class, 5l, 7l, 10l);
        testAndOverlappingRangesToBetween(Float.class, 5f, 7f, 10f);
        testAndOverlappingRangesToBetween(Double.class, 5d, 7d, 10d);
        testAndOverlappingRangesToBetween(String.class, "bbb", "nnn", "ppp");
    }

    private <T> void testAndOverlappingRangesToBetween(Class<T> type, T min, T mid, T max)
            throws Exception {
        SimpleFeatureType schema = DataUtilities.createType("test", "a:" + type.getName());
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setRangeSimplicationEnabled(true);
        visitor.setFeatureType(schema);

        // excluding extrema, not possible to turn it into a between filter
        Filter original =
                ff.and(
                        Arrays.asList( //
                                (Filter) ff.greaterOrEqual(ff.property("a"), ff.literal(min)), //
                                ff.less(ff.property("a"), ff.literal(max)), //
                                ff.lessOrEqual(ff.property("a"), ff.literal(mid))));

        Filter simplified = (Filter) original.accept(visitor, null);
        assertEquals(ff.between(ff.property("a"), ff.literal(min), ff.literal(mid)), simplified);
    }

    public void testOrPseudoBetween() throws Exception {
        testOrPseudoBetween(Integer.class, 5, 10);
        testOrPseudoBetween(Byte.class, (byte) 5, (byte) 10);
        testOrPseudoBetween(Long.class, 5l, 10l);
        testOrPseudoBetween(Float.class, 5f, 10f);
        testOrPseudoBetween(Double.class, 5d, 10d);
        testOrPseudoBetween(String.class, "bbb", "ppp");
    }

    private <T> void testOrPseudoBetween(Class<T> type, T min, T max) throws Exception {
        SimpleFeatureType schema = DataUtilities.createType("test", "a:" + type.getName());
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setRangeSimplicationEnabled(true);
        visitor.setFeatureType(schema);

        // (a > min && a <= max) or (a <= min)
        Filter original =
                ff.or(
                        Arrays.asList( //
                                ff.and(
                                        ff.greater(ff.property("a"), ff.literal(min)), //
                                        ff.lessOrEqual(ff.property("a"), ff.literal(max))), //
                                ff.lessOrEqual(ff.property("a"), ff.literal(min))));

        Filter simplified = (Filter) original.accept(visitor, null);
        assertEquals(ff.lessOrEqual(ff.property("a"), ff.literal(max)), simplified);
    }

    public void testRangeExpression() throws Exception {
        SimpleFeatureType schema = DataUtilities.createType("test", "pop:String");
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setRangeSimplicationEnabled(true);
        visitor.setFeatureType(schema);
        Function func = ff.function("parseLong", ff.property("pop"));
        Filter f1 = ff.less(func, ff.literal(20000));
        Filter f2 = ff.between(func, ff.literal(20000), ff.literal(50000));
        Filter or = ff.or(f1, f2);

        Filter simplified = (Filter) or.accept(visitor, null);
        assertEquals(ff.lessOrEqual(func, ff.literal(50000)), simplified);
    }

    public void testSimplifyNegateImpossible() throws Exception {
        PropertyIsEqualTo propertyIsEqualTo = ff.equal(ff.literal("a"), ff.literal("b"), true);
        Not negated = ff.not(propertyIsEqualTo);

        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        SimpleFeatureType schema = DataUtilities.createType("test", "pop:String");
        visitor.setFeatureType(schema);
        Filter simplified = (Filter) negated.accept(visitor, null);
        assertEquals(Filter.INCLUDE, simplified);
    }
}
