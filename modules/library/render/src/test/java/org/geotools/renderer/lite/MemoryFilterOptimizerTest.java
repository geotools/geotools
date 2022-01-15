/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.data.DataTestCase;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.function.FilterFunction_strConcat;
import org.geotools.filter.function.InFunction;
import org.geotools.renderer.lite.MemoryFilterOptimizer.IndexPropertyName;
import org.junit.Test;
import org.mockito.Mockito;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

public class MemoryFilterOptimizerTest extends DataTestCase {

    private PropertyName name;
    private PropertyName id;
    private PropertyIsEqualTo equalName;
    private PropertyIsEqualTo equalId;
    private And and;

    @Override
    public void init() throws Exception {
        super.init();

        name = ff.property("name");
        equalName = ff.equal(name, ff.literal("r1"), false);
        id = ff.property("id");
        equalId = ff.equal(id, ff.literal(1), false);
        and = ff.and(equalName, equalId);
    }

    @Test
    public void testDuplicateWithoutTargets() {
        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(roadType, Collections.emptySet());

        final And copy = (And) and.accept(optimizer, null);

        // filters are not memoized, but property accesses should be indexed now
        List<Filter> children = copy.getChildren();
        PropertyIsEqualTo equalNameCopy = (PropertyIsEqualTo) children.get(0);
        PropertyIsEqualTo equalIdCopy = (PropertyIsEqualTo) children.get(1);

        assertFalse(Proxy.isProxyClass(equalNameCopy.getClass()));
        assertFalse(Proxy.isProxyClass(equalIdCopy.getClass()));

        checkPropertiesIndexed(equalNameCopy, equalIdCopy);
    }

    public void checkPropertiesIndexed(
            PropertyIsEqualTo equalNameCopy, PropertyIsEqualTo equalIdCopy) {
        assertTrue(equalNameCopy.getExpression1() instanceof IndexPropertyName);
        IndexPropertyName indexedName = (IndexPropertyName) equalNameCopy.getExpression1();
        assertEquals(2, indexedName.index);
        assertEquals(name, indexedName.delegate);

        assertTrue(equalIdCopy.getExpression1() instanceof IndexPropertyName);
        IndexPropertyName indexedId = (IndexPropertyName) equalIdCopy.getExpression1();
        assertEquals(0, indexedId.index);
        assertEquals(id, indexedId.delegate);
    }

    @Test
    public void testDuplicateAndMemoize() {
        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(
                        roadType, new HashSet<>(Arrays.asList(equalName, equalId)));

        final And copy = (And) and.accept(optimizer, null);
        // this time they are not memoized, but property accesses should be indexed now
        List<Filter> children = copy.getChildren();
        PropertyIsEqualTo equalNameCopy = (PropertyIsEqualTo) children.get(0);
        PropertyIsEqualTo equalIdCopy = (PropertyIsEqualTo) children.get(1);

        assertTrue(Proxy.isProxyClass(equalNameCopy.getClass()));
        assertTrue(Proxy.isProxyClass(equalIdCopy.getClass()));

        checkPropertiesIndexed(equalNameCopy, equalIdCopy);
    }

    @Test
    public void testMemoizeDefaultGeometry() {
        PropertyName property = ff.property("");

        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(roadType, Collections.singleton(property));
        PropertyName memoized = (PropertyName) property.accept(optimizer, null);

        SimpleFeature spy = Mockito.spy(roadFeatures[0]);
        assertSame(roadFeatures[0].getDefaultGeometry(), memoized.evaluate(spy));
        assertSame(roadFeatures[0].getDefaultGeometry(), memoized.evaluate(spy));

        // check the default geometry property has been called once only
        Mockito.verify(spy, Mockito.times(1)).getDefaultGeometry();
    }

    @Test
    public void testMemoizeNonExistingProperty() {
        // Property accessors would return null instead of an exception, check this behavior has
        // been replicated
        PropertyName property = ff.property("foobar");

        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(roadType, Collections.singleton(property));
        PropertyName memoized = (PropertyName) property.accept(optimizer, null);
        assertNull(memoized.evaluate(roadFeatures[0]));
    }

    @Test
    public void testEqualFeatureTypes() throws Exception {
        String name = "name";
        PropertyName property = ff.property(name);
        SimpleFeatureType subtype1 = SimpleFeatureTypeBuilder.retype(roadType, name);
        SimpleFeatureType subtype2 = SimpleFeatureTypeBuilder.retype(roadType, name);

        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(subtype1, Collections.singleton(name));
        PropertyName memoized = (PropertyName) property.accept(optimizer, null);

        // retype the feature with the second subtype
        SimpleFeature retyped = SimpleFeatureBuilder.retype(roadFeatures[0], subtype2);

        // evalute and spy the results, it should still use index based access
        SimpleFeature spy = Mockito.spy(retyped);
        assertSame(retyped.getAttribute(name), memoized.evaluate(spy));
        Mockito.verify(spy, Mockito.times(0)).getAttribute(name);
        Mockito.verify(spy, Mockito.times(1)).getAttribute(0);
    }

    @Test
    public void testInFunctionOptimizer() throws Exception {
        String name = "name";
        PropertyName property = ff.property(name);
        SimpleFeatureType subtype1 = SimpleFeatureTypeBuilder.retype(roadType, name);
        Filter nameR1 = ff.equal(property, ff.literal("r1"), true);
        Filter nameR2 = ff.equal(property, ff.literal("r2"), true);
        Filter nameR3 = ff.equal(ff.property(name), ff.literal("r3"), true);
        List<Filter> filters = Arrays.asList(nameR1, nameR2, nameR3);
        Or or = ff.or(filters);
        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(subtype1, Collections.singleton(name));
        Object object = or.accept(optimizer, null);
        assertTrue(object instanceof PropertyIsEqualTo);
        PropertyIsEqualTo eq = (PropertyIsEqualTo) object;
        assertTrue(eq.getExpression1() instanceof InFunction);
        InFunction inFunction = (InFunction) eq.getExpression1();
        assertEquals(4, inFunction.getParameters().size());
        PropertyName propname = (PropertyName) inFunction.getParameters().get(0);
        assertEquals(name, propname.getPropertyName());
        List<String> inLiterals =
                inFunction.getParameters().stream()
                        .filter(ex -> ex instanceof Literal)
                        .map(ex -> ((Literal) ex).evaluate(null, String.class))
                        .collect(Collectors.toList());
        assertTrue(inLiterals.contains("r1"));
        assertTrue(inLiterals.contains("r2"));
        assertTrue(inLiterals.contains("r3"));
    }

    @Test
    public void testInFunctionOptimizerNotUsed() throws Exception {
        String name = "name";
        PropertyName property = ff.property(name);
        PropertyName property2 = ff.property("other");
        SimpleFeatureType subtype1 = SimpleFeatureTypeBuilder.retype(roadType, name);
        Filter nameR1 = ff.equal(property, ff.literal("r1"), true);
        Filter nameR2 = ff.equal(property2, ff.literal("r2"), true);
        Filter nameR3 = ff.equal(property, ff.literal("r3"), true);
        List<Filter> filters = Arrays.asList(nameR1, nameR2, nameR3);
        Or or = ff.or(filters);
        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(subtype1, Collections.singleton(name));
        Object object = or.accept(optimizer, null);
        assertTrue(object instanceof Or);
    }

    @Test
    public void testInFunctionOptimizerNotUsedOtherFilter() throws Exception {
        String name = "name";
        PropertyName property = ff.property(name);
        SimpleFeatureType subtype1 = SimpleFeatureTypeBuilder.retype(roadType, name);
        Filter nameR1 = ff.equal(property, ff.literal("r1"), true);
        Filter nameR2 = ff.notEqual(property, ff.literal("r2"), true);
        Filter nameR3 = ff.equal(property, ff.literal("r3"), true);
        List<Filter> filters = Arrays.asList(nameR1, nameR2, nameR3);
        Or or = ff.or(filters);
        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(subtype1, Collections.singleton(name));
        Object object = or.accept(optimizer, null);
        assertTrue(object instanceof Or);
    }

    @Test
    public void testInFunctionOptimizerExpression() throws Exception {
        String name = "name";
        PropertyName property = ff.property(name);
        FilterFunction_strConcat concat = new FilterFunction_strConcat();
        List<Expression> propExpressions = new ArrayList<>();
        propExpressions.add(property);
        propExpressions.add(ff.literal("-id"));
        concat.setParameters(propExpressions);
        SimpleFeatureType subtype1 = SimpleFeatureTypeBuilder.retype(roadType, name);
        Filter nameR1 = ff.equal(concat, ff.literal("r1"), true);
        Filter nameR2 = ff.equal(concat, ff.literal("r2"), true);
        Filter nameR3 = ff.equal(concat, ff.literal("r3"), true);
        List<Filter> filters = Arrays.asList(nameR1, nameR2, nameR3);
        Or or = ff.or(filters);
        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(subtype1, Collections.singleton(name));
        Object object = or.accept(optimizer, null);
        assertTrue(object instanceof PropertyIsEqualTo);
        PropertyIsEqualTo eq = (PropertyIsEqualTo) object;
        assertTrue(eq.getExpression1() instanceof InFunction);
        InFunction inFunction = (InFunction) eq.getExpression1();
        assertEquals(4, inFunction.getParameters().size());
        assertTrue(inFunction.getParameters().get(0) instanceof FilterFunction_strConcat);
        List<String> inLiterals =
                inFunction.getParameters().stream()
                        .filter(ex -> ex instanceof Literal)
                        .map(ex -> ((Literal) ex).evaluate(null, String.class))
                        .collect(Collectors.toList());
        assertTrue(inLiterals.contains("r1"));
        assertTrue(inLiterals.contains("r2"));
        assertTrue(inLiterals.contains("r3"));
    }
}
