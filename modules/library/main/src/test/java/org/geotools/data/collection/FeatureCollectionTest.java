/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Created on July 21, 2003, 5:58 PM
 */

package org.geotools.data.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureComparators;
import org.geotools.feature.FeatureComparators.Name;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * TestCase that can be extended to verify the functionality of different FeatureCollection implementations.
 *
 * @author Jody
 */
@SuppressWarnings("unchecked")
public abstract class FeatureCollectionTest {

    SimpleFeatureCollection features;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("Dummy");
        SimpleFeatureType schema = tb.buildFeatureType();

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(schema);
        List<SimpleFeature> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(b.buildFeature(null));
        }

        features = newCollection(schema, list);
    }

    /**
     * Override this method to create an instance of the feature collection implementation to test.
     *
     * <p>As an example to test the current "default" feature collection:
     *
     * <pre>
     * protected SimpleFeatureCollection newCollection(SimpleFeatureType schema) {
     *     return FeatureCollections.newCollection();
     * }
     * </pre>
     *
     * @return a new feature collection
     */
    protected abstract SimpleFeatureCollection newCollection(SimpleFeatureType schema, List<SimpleFeature> list);

    public Collection randomPiece(Collection original) {
        LinkedList next = new LinkedList<>();
        Iterator og = original.iterator();
        while (og.hasNext()) {
            if (Math.random() > .5) {
                next.add(og.next());
            } else {
                og.next();
            }
        }
        return next;
    }

    public <F extends Feature> Collection<F> randomPiece(FeatureCollection<?, F> original) {
        LinkedList<F> next = new LinkedList<>();
        try (FeatureIterator<F> og = original.features()) {
            while (og.hasNext()) {
                if (Math.random() > .5) {
                    next.add(og.next());
                } else {
                    og.next();
                }
            }
            return next;
        }
    }

    @Test
    public void testBounds() throws Exception {
        PrecisionModel pm = new PrecisionModel();
        Geometry[] g = new Geometry[4];
        GeometryFactory gf = new GeometryFactory(pm);

        g[0] = gf.createPoint(new Coordinate(0, 0));
        g[1] = gf.createPoint(new Coordinate(0, 10));
        g[2] = gf.createPoint(new Coordinate(10, 0));
        g[3] = gf.createPoint(new Coordinate(10, 10));

        GeometryCollection gc = gf.createGeometryCollection(g);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("bounds");
        tb.setCRS(null);
        tb.add("p1", Point.class);

        SimpleFeatureType t = tb.buildFeatureType();

        TreeSetFeatureCollection fc = new TreeSetFeatureCollection(null, t);
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(t);
        for (Geometry geometry : g) {
            b.add(geometry);
            fc.add(b.buildFeature(null));
        }
        Assert.assertEquals(gc.getEnvelopeInternal(), fc.getBounds());
    }

    @Test
    public void testSetAbilities() {
        int size = features.size();
        if (features instanceof Collection) {
            ((Collection<SimpleFeature>) features).addAll(randomPiece(features));
            Assert.assertEquals(features.size(), size);
        }
    }

    @Test
    public void testAddRemoveAllAbilities() throws Exception {
        Collection half = randomPiece(features);
        Collection otherHalf = DataUtilities.list(features);

        if (features instanceof Collection) {
            Collection<SimpleFeature> collection = (Collection<SimpleFeature>) features;

            otherHalf.removeAll(half);
            collection.removeAll(half);
            Assert.assertTrue(features.containsAll(otherHalf));
            Assert.assertFalse(features.containsAll(half));
            collection.removeAll(otherHalf);
            Assert.assertEquals(0, features.size());
            collection.addAll(half);
            Assert.assertTrue(features.containsAll(half));
            collection.addAll(otherHalf);
            Assert.assertTrue(features.containsAll(otherHalf));
            collection.retainAll(otherHalf);
            Assert.assertTrue(features.containsAll(otherHalf));
            Assert.assertFalse(features.containsAll(half));
            collection.addAll(otherHalf);
            Iterator<SimpleFeature> i = collection.iterator();
            while (i.hasNext()) {
                i.next();
                i.remove();
            }
            Assert.assertEquals(features.size(), 0);

            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setName("XXX");
            SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

            Assert.assertFalse(collection.remove(b.buildFeature(null)));
        }
    }

    @Test
    public void testAssorted() {
        TreeSetFeatureCollection copy = new TreeSetFeatureCollection();
        copy.addAll(features);
        copy.clear();
        Assert.assertTrue(copy.isEmpty());
        copy.addAll(features);
        Assert.assertFalse(copy.isEmpty());

        List<SimpleFeature> list = DataUtilities.list(features);
        SimpleFeature[] f1 = list.toArray(new SimpleFeature[list.size()]);
        SimpleFeature[] f2 = features.toArray(new SimpleFeature[list.size()]);
        Assert.assertEquals(f1.length, f2.length);
        for (int i = 0; i < f1.length; i++) {
            Assert.assertSame(f1[i], f2[i]);
        }
        try (SimpleFeatureIterator copyIterator = copy.features();
                SimpleFeatureIterator featuresIterator = features.features()) {
            while (copyIterator.hasNext() && featuresIterator.hasNext()) {
                Assert.assertEquals(copyIterator.next(), featuresIterator.next());
            }
        }
    }

    /**
     * This test uses FeatureComparators.Name and uses it on an attribute, that contains <code>null
     * </code>s. It should not throw an NPE.
     */
    @Test
    public void testFeatureComparatorsNameWithNullValues() {

        // features = FeatureCollections.newCollection();

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("DummyToBeSorted");
        tb.add("name", String.class);
        tb.add("number", Integer.class);

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        SimpleFeature f1 = b.buildFeature(null, "Steve", 32);
        SimpleFeature f2 = b.buildFeature(null, null, null);
        SimpleFeature f3 = b.buildFeature(null, null, null);

        Name compareName = new FeatureComparators.Name("name");

        // f1 has name, f2 has null name, expecting that f1 is greater g2
        Assert.assertTrue(compareName.compare(f1, f2) > 0);
        Assert.assertTrue(compareName.compare(f2, f1) < 0);
        Assert.assertEquals(0, compareName.compare(f2, f3));

        Name compareNumber = new FeatureComparators.Name("name");
        // f1 has number, f2 has null number, expecting that f1 is greater g2
        Assert.assertTrue(compareNumber.compare(f1, f2) > 0);
        Assert.assertTrue(compareNumber.compare(f2, f1) < 0);
        Assert.assertEquals(0, compareNumber.compare(f2, f3));
    }

    /**
     * A simple colleciton listener used to count change events to verify event handling during testing.
     *
     * @author Jody
     */
    static class ListenerProxy implements CollectionListener {
        int changeEvents = 0;

        @Override
        public void collectionChanged(org.geotools.feature.CollectionEvent tce) {
            changeEvents++;
        }
    }
}
