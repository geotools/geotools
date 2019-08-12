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

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.geotools.data.DataTestCase;
import org.geotools.renderer.lite.MemoryFilterOptimizer.IndexPropertyName;
import org.mockito.Mockito;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.PropertyName;

public class MemoryFilterOptimizerTest extends DataTestCase {

    private PropertyName name;
    private PropertyName id;
    private PropertyIsEqualTo equalName;
    private PropertyIsEqualTo equalId;
    private And and;

    public void setUp() throws Exception {
        super.setUp();

        name = ff.property("name");
        equalName = ff.equal(name, ff.literal("r1"), false);
        id = ff.property("id");
        equalId = ff.equal(id, ff.literal(1), false);
        and = ff.and(equalName, equalId);
    }

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

    public void testMemoizeNonExistingProperty() {
        // Property accessors would return null instead of an exception, check this behavior has
        // been replicated
        PropertyName property = ff.property("foobar");

        MemoryFilterOptimizer optimizer =
                new MemoryFilterOptimizer(roadType, Collections.singleton(property));
        PropertyName memoized = (PropertyName) property.accept(optimizer, null);
        assertNull(memoized.evaluate(roadFeatures[0]));
    }
}
