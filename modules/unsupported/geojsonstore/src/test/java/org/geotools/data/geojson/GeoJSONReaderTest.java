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
package org.geotools.data.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.test.TestData;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

/** @author ian */
public class GeoJSONReaderTest {

    private final GeometryFactory gf = new GeometryFactory();

    /** Test method for {@link org.geotools.data.geojson.GeoJSONReader#getFeatures()}. */
    @Test
    public void testGetFeatures() throws IOException, ParseException {
        URL url = TestData.url(GeoJSONDataStore.class, "locations.json");

        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            FeatureCollection features = reader.getFeatures();
            assertNotNull(features);
            assertEquals("wrong number of features read", 9, features.size());
        }
    }

    @Test
    public void testGetChangingSchema() throws IOException, ParseException {
        URL url = TestData.url(GeoJSONDataStore.class, "locations-changable.json");

        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            FeatureCollection features = reader.getFeatures();
            assertNotNull(features);
            assertEquals("wrong number of features read", 9, features.size());

            HashMap<String, Object> expected = new HashMap<>();

            expected.put("LAT", 46.066667);
            expected.put("LON", 11.116667);
            expected.put("CITY", "Trento");
            expected.put("NUMBER", null);
            expected.put("YEAR", null);
            expected.put("the_geom", gf.createPoint(new Coordinate(11.117, 46.067)));
            Feature first = DataUtilities.first(features);
            for (Property prop : first.getProperties()) {
                assertEquals(expected.get(prop.getName().getLocalPart()), prop.getValue());
            }
        }
    }

    @Test
    public void testReadFromInputStream() throws Exception {
        String input =
                "{\n"
                        + "\"type\": \"FeatureCollection\",\n"
                        + "\"features\": [\n"
                        + "{ \"type\": \"Feature\", \"properties\": { \"LAT\": 46.066667, \"LON\": 11.116667, \"CITY\": \"Trento\", \"NUMBER\": 140, \"YEAR\": 2002 }, \"bbox\": [ 11.117, 46.067, 11.117, 46.067 ], \"geometry\": { \"type\": \"Point\", \"coordinates\": [ 11.117, 46.067 ] } },\n"
                        + "{ \"type\": \"Feature\", \"properties\": { \"LAT\": 44.9441, \"LON\": -93.0852, \"CITY\": \"St Paul\", \"NUMBER\": 125, \"YEAR\": 2003 }, \"bbox\": [ -93.085, 44.944, -93.085, 44.944 ], \"geometry\": { \"type\": \"Point\", \"coordinates\": [ -93.085, 44.944 ] } },\n"
                        + "{ \"type\": \"Feature\", \"properties\": { \"LAT\": 13.752222, \"LON\": 100.493889, \"CITY\": \"Bangkok\", \"NUMBER\": 150, \"YEAR\": 2004 }, \"bbox\": [ 100.494, 13.752, 100.494, 13.752 ], \"geometry\": { \"type\": \"Point\", \"coordinates\": [ 100.494, 13.752 ] } },\n"
                        + "{ \"type\": \"Feature\", \"properties\": { \"LAT\": 45.420833, \"LON\": -75.69, \"CITY\": \"Ottawa\", \"NUMBER\": 200, \"YEAR\": 2004 }, \"bbox\": [ -75.69, 45.421, -75.69, 45.421 ], \"geometry\": { \"type\": \"Point\", \"coordinates\": [ -75.69, 45.421 ] } },\n"
                        + "{ \"type\": \"Feature\", \"properties\": { \"LAT\": 44.9801, \"LON\": -93.251867, \"CITY\": \"Minneapolis\", \"NUMBER\": 350, \"YEAR\": 2005 }, \"bbox\": [ -93.252, 44.98, -93.252, 44.98 ], \"geometry\": { \"type\": \"Point\", \"coordinates\": [ -93.252, 44.98 ] } },\n"
                        + "{ \"type\": \"Feature\", \"properties\": { \"LAT\": 46.519833, \"LON\": 6.6335, \"CITY\": \"Lausanne\", \"NUMBER\": 560, \"YEAR\": 2006 }, \"bbox\": [ 6.633, 46.52, 6.633, 46.52 ], \"geometry\": { \"type\": \"Point\", \"coordinates\": [ 6.633, 46.52 ] } },\n"
                        + "{ \"type\": \"Feature\", \"properties\": { \"LAT\": 48.428611, \"LON\": -123.365556, \"CITY\": \"Victoria\", \"NUMBER\": 721, \"YEAR\": 2007 }, \"bbox\": [ -123.366, 48.429, -123.366, 48.429 ], \"geometry\": { \"type\": \"Point\", \"coordinates\": [ -123.366, 48.429 ] } },\n"
                        + "{ \"type\": \"Feature\", \"properties\": { \"LAT\": -33.925278, \"LON\": 18.423889, \"CITY\": \"Cape Town\", \"NUMBER\": 550, \"YEAR\": 2008 }, \"bbox\": [ 18.424, -33.925, 18.424, -33.925 ], \"geometry\": { \"type\": \"Point\", \"coordinates\": [ 18.424, -33.925 ] } },\n"
                        + "{ \"type\": \"Feature\", \"properties\": { \"LAT\": -33.859972, \"LON\": 151.21111, \"CITY\": \"Sydney\", \"NUMBER\": 436, \"YEAR\": 2009 }, \"bbox\": [ 151.211, -33.86, 151.211, -33.86 ], \"geometry\": { \"type\": \"Point\", \"coordinates\": [ 151.211, -33.86 ] } }\n"
                        + "]\n"
                        + "}";

        try (GeoJSONReader reader = new GeoJSONReader(new ByteArrayInputStream(input.getBytes()))) {
            FeatureCollection features = reader.getFeatures();
            assertNotNull(features);
            assertEquals("wrong number of features read", 9, features.size());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFeatureCollectionWithRegularGeometryAttributeReadAndGeometryAfterProperties()
            throws Exception {
        String geojson1 =
                "{"
                        + "'type': 'FeatureCollection',"
                        + "'features': "
                        + "[{"
                        + "  'type': 'Feature',"
                        + "  'id': 'feature.0',"
                        + "  'properties': {"
                        + "    'otherGeometry': {"
                        + "      'type': 'LineString',"
                        + "      'coordinates': [[1.1, 1.2], [1.3, 1.4]]"
                        + "    }"
                        + "  },"
                        + "  'geometry': {"
                        + "    'type': 'Point',"
                        + "    'coordinates': [0.1, 0.1]"
                        + "  }"
                        + "}"
                        + "]"
                        + "}";
        geojson1 = geojson1.replace('\'', '"');

        SimpleFeature f = null;
        try (GeoJSONReader reader =
                new GeoJSONReader(new ByteArrayInputStream(geojson1.getBytes()))) {
            FeatureCollection features = reader.getFeatures();
            assertNotNull(features);
            assertFalse(features.isEmpty());
            f = (SimpleFeature) DataUtilities.first(features);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(f);
        assertEquals("features.0", f.getID());
        WKTReader wkt = new WKTReader();
        assertEquals(wkt.read("POINT (0.1 0.1)"), f.getDefaultGeometry());
        assertEquals(wkt.read("LINESTRING (1.1 1.2, 1.3 1.4)"), f.getAttribute("otherGeometry"));
    }

    @Test
    public void testFeatureWithRegularGeometryAttributeReadAndGeometryAfterProperties()
            throws Exception {
        String geojson1 =
                "{"
                        + "  'type': 'Feature',"
                        + "  'id': 'feature.0',"
                        + "  'properties': {"
                        + "    'otherGeometry': {"
                        + "      'type': 'LineString',"
                        + "      'coordinates': [[1.1, 1.2], [1.3, 1.4]]"
                        + "    }"
                        + "  },"
                        + "  'geometry': {"
                        + "    'type': 'Point',"
                        + "    'coordinates': [0.1, 0.1]"
                        + "  }"
                        + "}";
        geojson1 = geojson1.replace('\'', '"');

        SimpleFeature f = GeoJSONReader.parseFeature(geojson1);
        assertNotNull(f);

        assertNotNull(f);
        assertEquals("features.0", f.getID());
        WKTReader wkt = new WKTReader();
        assertEquals(wkt.read("POINT (0.1 0.1)"), f.getDefaultGeometry());
        assertEquals(wkt.read("LINESTRING (1.1 1.2, 1.3 1.4)"), f.getAttribute("otherGeometry"));
    }
}
