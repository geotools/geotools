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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public abstract class JDBCFeatureCollectionOnlineTest extends JDBCTestSupport {
    SimpleFeatureCollection collection;
    JDBCFeatureStore source;

    public JDBCFeatureCollectionOnlineTest() {
        super();
    }

    protected JDBCFeatureCollectionOnlineTest(double coordinateEps) {
        super(coordinateEps);
    }

    @Override
    protected void connect() throws Exception {
        super.connect();

        source = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));
        collection = source.getFeatures();
    }

    @Test
    public void testIterator() throws Exception {
        try (FeatureIterator<SimpleFeature> i = collection.features()) {
            assertNotNull(i);
            assertFeatureIterator(0, 3, i, new SimpleFeatureAssertion() {

                @Override
                public int toIndex(SimpleFeature feature) {
                    return ((Number) feature.getAttribute(aname("intProperty"))).intValue();
                }

                @Override
                public void check(int index, SimpleFeature feature) {
                    assertNotNull(feature);

                    String fid = feature.getID();

                    int id = Integer.parseInt(fid.substring(fid.indexOf('.') + 1));

                    assertEquals(index, id);
                }
            });
        }
    }

    @Test
    public void testBounds() throws IOException {
        ReferencedEnvelope bounds = collection.getBounds();
        assertNotNull(bounds);

        assertEquals(0d, bounds.getMinX(), 0.1);
        assertEquals(0d, bounds.getMinY(), 0.1);
        assertEquals(2d, bounds.getMaxX(), 0.1);
        assertEquals(2d, bounds.getMaxY(), 0.1);
    }

    @Test
    public void testSize() throws IOException {
        assertEquals(3, collection.size());
    }

    @Test
    public void testSubCollection() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.equals(ff.property(aname("intProperty")), ff.literal(1));

        SimpleFeatureCollection sub = collection.subCollection(f);
        assertNotNull(sub);
        assertEquals(1, sub.size());

        ReferencedEnvelope exp = new ReferencedEnvelope(1, 1, 1, 1, CRS.decode("EPSG:4326"));
        ReferencedEnvelope act = sub.getBounds();

        assertEquals(exp.getMinX(), act.getMinX(), 0.1);
        assertEquals(exp.getMinY(), act.getMinY(), 0.1);
        assertEquals(exp.getMaxX(), act.getMaxX(), 0.1);
        assertEquals(exp.getMaxY(), act.getMaxY(), 0.1);
    }

    @Test
    public void testAdd() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(collection.getSchema());
        b.set(aname("intProperty"), Integer.valueOf(3));
        b.set(aname("doubleProperty"), Double.valueOf(3.3));
        b.set(aname("stringProperty"), "three");
        b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(3, 3)));

        SimpleFeature feature = b.buildFeature(null);
        assertEquals(3, collection.size());

        source.addFeatures(DataUtilities.collection(feature));

        assertEquals(4, collection.size());

        try (SimpleFeatureIterator i = collection.features()) {
            boolean found = false;

            while (i.hasNext()) {
                SimpleFeature f = i.next();

                if ("three".equals(f.getAttribute(aname("stringProperty")))) {
                    assertEquals(
                            ((Double) feature.getAttribute(aname("doubleProperty"))).doubleValue(),
                            ((Double) f.getAttribute(aname("doubleProperty"))).doubleValue(),
                            1e-5);
                    assertEquals(
                            feature.getAttribute(aname("stringProperty")), f.getAttribute(aname("stringProperty")));
                    Geometry expected = (Geometry) feature.getAttribute(aname("geometry"));
                    Geometry actual = (Geometry) f.getAttribute(aname("geometry"));
                    assertTrue(expected.equalsExact(actual, COORDINATE_EPS));
                    found = true;
                }
            }
            assertTrue(found);
        }
    }
}
