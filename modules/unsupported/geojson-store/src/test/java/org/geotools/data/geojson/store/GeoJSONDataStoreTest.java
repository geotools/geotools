package org.geotools.data.geojson.store;
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

public class GeoJSONDataStoreTest {
    GeoJSONDataStore ds;

    @Before
    public void setup() throws IOException {
        URL url = TestData.url(GeoJSONDataStore.class, "ne_110m_admin_1_states_provinces.geojson");

        ds = new GeoJSONDataStore(url);
    }

    @Test
    public void testSource() throws IOException {

        String type = ds.getNames().get(0).getLocalPart();
        Query query = new Query(type);
        SimpleFeatureSource source = ds.getFeatureSource(ds.getNames().get(0));

        assertEquals(51, source.getCount(query)); // includes DC
        ReferencedEnvelope expected =
                new ReferencedEnvelope(
                        -171.79111060289117,
                        -66.96466,
                        18.916190000000142,
                        71.35776357694175,
                        DefaultGeographicCRS.WGS84);
        ReferencedEnvelope obs = source.getBounds();
        assertNotNull(obs);
        assertEquals(expected.getMinX(), obs.getMinX(), 0.00001);
        assertEquals(expected.getMinY(), obs.getMinY(), 0.00001);
        assertEquals(expected.getMaxX(), obs.getMaxX(), 0.00001);
        assertEquals(expected.getMaxY(), obs.getMaxY(), 0.00001);
        assertEquals(expected.getCoordinateReferenceSystem(), obs.getCoordinateReferenceSystem());
    }

    @Test
    public void testReader() throws IOException {
        String type = ds.getNames().get(0).getLocalPart();
        Query query = new Query(type);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                ds.getFeatureReader(query, null)) {
            SimpleFeatureType schema = reader.getFeatureType();
            assertNotNull(schema);
        }
    }

    @Test
    public void testFeatures() throws IOException {
        URL url = TestData.url(GeoJSONDataStore.class, "featureCollection.json");

        GeoJSONDataStore fds = new GeoJSONDataStore(url);
        String type = fds.getNames().get(0).getLocalPart();
        Query query = new Query(type);
        Class<?> lastGeometryBinding = null;
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                fds.getFeatureReader(query, null)) {
            SimpleFeatureType schema = reader.getFeatureType();
            assertNotNull(schema);
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature sf = reader.next();
                lastGeometryBinding =
                        sf.getFeatureType().getGeometryDescriptor().getType().getBinding();
                count++;
            }

            assertEquals(7, count);
        }
        assertEquals(Geometry.class, lastGeometryBinding);
    }

    // An ogr written file
    @Test
    public void testLocations() throws IOException {
        URL url = TestData.url(GeoJSONDataStore.class, "locations.json");

        GeoJSONDataStore fds = new GeoJSONDataStore(url);
        String type = fds.getNames().get(0).getLocalPart();
        Query query = new Query(type);

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                fds.getFeatureReader(query, null)) {
            SimpleFeatureType schema = reader.getFeatureType();
            // System.out.println(schema);
            assertEquals(Point.class, schema.getGeometryDescriptor().getType().getBinding());
            assertNotNull(schema);
            int count = 0;
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
            assertEquals(9, count);
        }
    }

    @Test
    public void testLocationsPresetIds() throws IOException {
        URL url = TestData.url(GeoJSONDataStore.class, "location_with_id.json");

        GeoJSONDataStore fds = new GeoJSONDataStore(url);
        String type = fds.getNames().get(0).getLocalPart();
        Query query = new Query(type);

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                fds.getFeatureReader(query, null)) {
            while (reader.hasNext()) {
                SimpleFeature sf = reader.next();
                assertThat(sf.getID(), startsWith("location_with_id.f"));
            }
        }
    }

    @Test
    public void testVeryChangeableSchema() throws IOException {
        URL url = TestData.url(GeoJSONDataStore.class, "jagged.json");
        GeoJSONDataStore fds = new GeoJSONDataStore(url);
        fds.setQuickSchema(false);
        assertNotNull(fds);
        String name = fds.getTypeNames()[0];
        assertNotNull(name);
        SimpleFeatureType schema = fds.getSchema();
        assertNotNull(schema);

        assertEquals(4, schema.getAttributeCount());
        int cnt = 0;
        for (int i = 0; i < schema.getAttributeCount(); i++) {
            if (schema.getDescriptor(i).getLocalName().equals(GeoJSONReader.GEOMETRY_NAME)) {
                cnt++;
            }
        }
        assertEquals(1, cnt);
    }

    @Test
    public void testEmptyFeatures() throws IOException {
        URL url = TestData.url(GeoJSONDataStore.class, "empty-featureCollection.json");

        GeoJSONDataStore fds = new GeoJSONDataStore(url);
        String type = fds.getNames().get(0).getLocalPart();
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                fds.getFeatureReader(new Query(type), null)) {
            // No Feature to read, also no NullPointer exception
            assertFalse(reader.hasNext());
            SimpleFeatureType schema = reader.getFeatureType();
            assertNotNull(schema);
            assertEquals(0, reader.getFeatureType().getAttributeCount());
        }
    }
}
