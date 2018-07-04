package org.geotools.data.geojson;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeoJSONDataStoreTest {
    GeoJSONDataStore ds;

    @Before
    public void setup() throws IOException {
        URL url = TestData.url(GeoJSONDataStore.class, "ne_110m_admin_1_states_provinces.geojson");
        // URL url = new
        // URL("http://geojson.xyz/naturalearth-3.3.0/ne_110m_admin_1_states_provinces.geojson");
        ds = new GeoJSONDataStore(url);
    }

    @Test
    public void testSource() throws IOException {

        String type = ds.getNames().get(0).getLocalPart();
        DefaultQuery query = new DefaultQuery(type);
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
        assertEquals(expected.getMinX(), obs.getMinX(), 0.00001);
        assertEquals(expected.getMinY(), obs.getMinY(), 0.00001);
        assertEquals(expected.getMaxX(), obs.getMaxX(), 0.00001);
        assertEquals(expected.getMaxY(), obs.getMaxY(), 0.00001);
        assertEquals(expected.getCoordinateReferenceSystem(), obs.getCoordinateReferenceSystem());
    }

    @Test
    public void testReader() throws IOException {
        String type = ds.getNames().get(0).getLocalPart();
        DefaultQuery query = new DefaultQuery(type);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = ds.getFeatureReader(query, null);
        SimpleFeatureType schema = reader.getFeatureType();
        assertNotNull(schema);
        /*
         * while (reader.hasNext()) {
         * System.out.println(reader.next().getAttribute("name")); }
         */
    }

    @Test
    public void testFeatures() throws IOException {
        URL url = TestData.url(GeoJSONDataStore.class, "featureCollection.json");
        // URL url = new
        // URL("http://geojson.xyz/naturalearth-3.3.0/ne_110m_admin_1_states_provinces.geojson");
        GeoJSONDataStore fds = new GeoJSONDataStore(url);
        String type = fds.getNames().get(0).getLocalPart();
        DefaultQuery query = new DefaultQuery(type);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = fds.getFeatureReader(query, null);
        SimpleFeatureType schema = reader.getFeatureType();
        // System.out.println(schema);
        assertEquals(
                "org.locationtech.jts.geom.Point",
                schema.getGeometryDescriptor().getType().getBinding().getCanonicalName());
        assertNotNull(schema);
        int count = 0;
        while (reader.hasNext()) {
            SimpleFeature next = reader.next();
            // System.out.println(next.getAttribute("name"));
            count++;
        }
        assertEquals(7, count);
    }
    // An ogr written file
    @Test
    public void testLocations() throws IOException {
        URL url = TestData.url(GeoJSONDataStore.class, "locations.json");

        GeoJSONDataStore fds = new GeoJSONDataStore(url);
        String type = fds.getNames().get(0).getLocalPart();
        System.out.println(type);
        DefaultQuery query = new DefaultQuery(type);
        System.out.println(query);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = fds.getFeatureReader(query, null);
        SimpleFeatureType schema = reader.getFeatureType();
        System.out.println(schema);
        assertEquals(
                "org.locationtech.jts.geom.Point",
                schema.getGeometryDescriptor().getType().getBinding().getName());
        assertNotNull(schema);
        int count = 0;
        while (reader.hasNext()) {

            SimpleFeature next = reader.next();
            // System.out.println(next.getAttribute("name"));
            count++;
        }
        assertEquals(9, count);
    }
}
