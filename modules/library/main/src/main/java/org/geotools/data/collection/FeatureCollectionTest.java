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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureComparators;
import org.geotools.feature.FeatureComparators.Name;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * TestCase that can be extended to verify the functionality of different FeatureCollection
 * implementations.
 * 
 * @author Jody
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/library/main/src/main/java/org/geotools/
 *         data/collection/FeatureCollectionTest.java $
 */
@SuppressWarnings("unchecked")
public abstract class FeatureCollectionTest extends TestCase {

    SimpleFeatureCollection features;

    public FeatureCollectionTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("Dummy");
        SimpleFeatureType schema = tb.buildFeatureType();
        features = newCollection(schema);
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(schema);
        for (int i = 0; i < 100; i++) {
            features.add(b.buildFeature(null));
        }
    }

    /**
     * Override this method to create an instance of the feature collection implementation to test.
     * <p>
     * As an example to test the current "default" feature collection:
     * 
     * <pre>
     * protected SimpleFeatureCollection newCollection(SimpleFeatureType schema) {
     *     return FeatureCollections.newCollection();
     * }
     * </pre>
     * 
     * @param schema
     * @return a new feature collection
     */
    protected abstract SimpleFeatureCollection newCollection(SimpleFeatureType schema);

    public Collection randomPiece(Collection original) {
        LinkedList next = new LinkedList();
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

    public Collection randomPiece(FeatureCollection original) {
        LinkedList next = new LinkedList();
        Iterator og = original.iterator();
        try {
            while (og.hasNext()) {
                if (Math.random() > .5) {
                    next.add(og.next());
                } else {
                    og.next();
                }
            }
            return next;
        } finally {
            original.close(og);
        }
    }

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
        tb.add("p1", Point.class);

        SimpleFeatureType t = tb.buildFeatureType();

        SimpleFeatureCollection fc = FeatureCollections.newCollection();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(t);

        for (int i = 0; i < g.length; i++) {
            b.add(g[i]);
            fc.add(b.buildFeature(null));
        }
        assertEquals(gc.getEnvelopeInternal(), fc.getBounds());
    }

    public void testSetAbilities() {
        int size = features.size();
        features.addAll(randomPiece(features));
        assertEquals(features.size(), size);
    }

    public void testAddRemoveAllAbilities() throws Exception {
        Collection half = randomPiece(features);
        Collection otherHalf = DataUtilities.list(features);
        otherHalf.removeAll(half);
        features.removeAll(half);
        assertTrue(features.containsAll(otherHalf));
        assertTrue(!features.containsAll(half));
        features.removeAll(otherHalf);
        assertTrue(features.size() == 0);
        features.addAll(half);
        assertTrue(features.containsAll(half));
        features.addAll(otherHalf);
        assertTrue(features.containsAll(otherHalf));
        features.retainAll(otherHalf);
        assertTrue(features.containsAll(otherHalf));
        assertTrue(!features.containsAll(half));
        features.addAll(otherHalf);
        Iterator i = features.iterator();
        while (i.hasNext()) {
            i.next();
            i.remove();
        }
        assertEquals(features.size(), 0);

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("XXX");
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        assertTrue(!features.remove(b.buildFeature(null)));
    }

    public void testAssorted() {
        SimpleFeatureCollection copy = FeatureCollections.newCollection();
        copy.addAll(features);
        copy.clear();
        assertTrue(copy.isEmpty());
        copy.addAll(features);
        assertTrue(!copy.isEmpty());

        List<SimpleFeature> list = DataUtilities.list(features);
        SimpleFeature[] f1 = (SimpleFeature[]) list.toArray(new SimpleFeature[list.size()]);
        SimpleFeature[] f2 = (SimpleFeature[]) features.toArray(new SimpleFeature[list.size()]);
        assertEquals(f1.length, f2.length);
        for (int i = 0; i < f1.length; i++) {
            assertSame(f1[i], f2[i]);
        }
        SimpleFeatureIterator copyIterator = copy.features();
        SimpleFeatureIterator featuresIterator = features.features();
        while (copyIterator.hasNext() && featuresIterator.hasNext()) {
            assertEquals(copyIterator.next(), featuresIterator.next());
        }

        SimpleFeatureCollection listen = FeatureCollections.newCollection();
        ListenerProxy counter = new ListenerProxy();
        listen.addListener(counter);
        listen.addAll(features);
        assertEquals(1, counter.changeEvents);
        listen.removeListener(counter);
        listen.removeAll(DataUtilities.list(features));
        assertEquals(1, counter.changeEvents);
    }

    /**
     * This test uses FeatureComparators.Name and uses it on an attribute, that contains
     * <code>null</code>s. It should not throw an NPE.
     */
    public void testFeatureComparatorsNameWithNullValues() {

        // features = FeatureCollections.newCollection();

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("DummyToBeSorted");
        tb.add("name", String.class);
        tb.add("number", Integer.class);

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        SimpleFeature f1 = b.buildFeature(null, new Object[] { "Steve", 32 });
        SimpleFeature f2 = b.buildFeature(null, new Object[] { null, null });
        SimpleFeature f3 = b.buildFeature(null, new Object[] { null, null });

        Name compareName = new FeatureComparators.Name("name");

        // f1 has name, f2 has null name, expecting that f1 is greater g2
        assertTrue(compareName.compare(f1, f2) > 0);
        assertTrue(compareName.compare(f2, f1) < 0);
        assertTrue(compareName.compare(f2, f3) == 0);

        Name compareNumber = new FeatureComparators.Name("name");
        // f1 has number, f2 has null number, expecting that f1 is greater g2
        assertTrue(compareNumber.compare(f1, f2) > 0);
        assertTrue(compareNumber.compare(f2, f1) < 0);
        assertTrue(compareNumber.compare(f2, f3) == 0);
    }

    /**
     * A simple colleciton listener used to count change events to verify event handling during
     * testing.
     * 
     * @author Jody
     */
    class ListenerProxy implements CollectionListener {
        int changeEvents = 0;

        public void collectionChanged(org.geotools.feature.CollectionEvent tce) {
            changeEvents++;
        }
    }
}
