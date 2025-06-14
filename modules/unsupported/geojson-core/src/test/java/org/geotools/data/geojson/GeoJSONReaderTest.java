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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/** @author ian */
public class GeoJSONReaderTest {

    private final GeometryFactory gf = new GeometryFactory();

    static final Logger LOGGER = Logging.getLogger(GeoJSONReaderTest.class);

    /** Test method for {@link org.geotools.data.geojson.GeoJSONReader#getFeatures()}. */
    @Test
    public void testGetFeatures() throws IOException, ParseException {
        URL url = TestData.url(GeoJSONReaderTest.class, "locations.json");

        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            FeatureCollection features = reader.getFeatures();
            assertNotNull(features);
            assertEquals("wrong number of features read", 9, features.size());
        }
    }

    @Test
    public void testGetChangingSchema() throws IOException, ParseException {
        URL url = TestData.url(GeoJSONReaderTest.class, "locations-changeable.json");

        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeatureCollection features = reader.getFeatures();
            assertNotNull(features);
            assertEquals("wrong number of features read", 9, features.size());

            HashMap<String, Object> expected = new HashMap<>();

            expected.put("LAT", 46.066667);
            expected.put("LON", 11.116667);
            expected.put("CITY", "Trento");
            expected.put("NUMBER", null);
            expected.put("YEAR", null);
            expected.put("geometry", gf.createPoint(new Coordinate(11.117, 46.067)));
            SimpleFeature first = DataUtilities.first(features);
            for (Property prop : first.getProperties()) {
                assertEquals(expected.get(prop.getName().getLocalPart()), prop.getValue());
            }
        }
    }

    @Test
    public void checkUserDataAfterSchemaChange() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "planetary_stac.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeatureCollection collection = reader.getFeatures();
            try (SimpleFeatureIterator features = collection.features()) {
                while (features.hasNext()) {
                    SimpleFeature feature = features.next();
                    assertTrue(feature.getUserData().containsKey(GeoJSONReader.TOP_LEVEL_ATTRIBUTES));
                }
            }
        }
    }

    @Test
    public void testReadFromInputStream() throws Exception {
        String input = "{\n"
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

        try (GeoJSONReader reader =
                new GeoJSONReader(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))) {
            SimpleFeatureCollection features = reader.getFeatures();
            assertNotNull(features);
            assertEquals(
                    Point.class,
                    features.getSchema().getGeometryDescriptor().getType().getBinding());
            assertEquals("wrong number of features read", 9, features.size());
            List<SimpleFeature> list = DataUtilities.list(features);
            // order preserving, since a GeoJSON collection is ordered
            assertEquals("Trento", list.get(0).getAttribute("CITY"));
            assertEquals("St Paul", list.get(1).getAttribute("CITY"));
            assertEquals("Bangkok", list.get(2).getAttribute("CITY"));
            SimpleFeature f = list.get(0);
            Point geom = (Point) f.getDefaultGeometry();
            CoordinateReferenceSystem crs = features.getSchema().getCoordinateReferenceSystem();
            AxisOrder order = CRS.getAxisOrder(crs);
            assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, crs));
            if (order.equals(AxisOrder.EAST_NORTH)) {
                assertEquals(11.117, geom.getX(), 0.0001);
                assertEquals(46.067, geom.getY(), 0.0001);
            } else {
                assertEquals(11.117, geom.getY(), 0.0001);
                assertEquals(46.067, geom.getX(), 0.0001);
            }
        }
    }

    @Test
    public void testFeatureCollectionParser() throws Exception {
        String geojson1 = "{"
                + "'type': 'FeatureCollection',"
                + "'features': "
                + "[{"
                + "  'type': 'Feature',"
                + "  'id': 'features.0',"
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

        SimpleFeatureCollection features = GeoJSONReader.parseFeatureCollection(geojson1);
        assertNotNull(features);
        assertFalse(features.isEmpty());
        f = DataUtilities.first(features);

        assertNotNull(f);
        assertEquals("features.0", f.getID());
        WKTReader wkt = new WKTReader();
        assertEquals(wkt.read("POINT (0.1 0.1)"), f.getDefaultGeometry());
        assertEquals(wkt.read("LINESTRING (1.1 1.2, 1.3 1.4)"), f.getAttribute("otherGeometry"));
    }

    @Test
    public void testParsePoint() throws ParseException {
        String point =
                "{\n" + "         \"type\": \"Point\",\n" + "         \"coordinates\": [100.0, 0.0]\n" + "     }";
        String expected = "POINT (100 0)";
        testGeometry(point, expected);
    }

    @Test
    public void testParseLine() throws ParseException {
        String line = "{\n"
                + "         \"type\": \"LineString\",\n"
                + "         \"coordinates\": [\n"
                + "             [100.0, 0.0],\n"
                + "             [101.0, 1.0]\n"
                + "         ]\n"
                + "     }";
        String expected = "LINESTRING(100 0, 101 1)";
        testGeometry(line, expected);
    }

    @Test
    public void testParsePolygon() throws ParseException {
        String line = "{\n"
                + "         \"type\": \"Polygon\",\n"
                + "         \"coordinates\": [\n"
                + "             [\n"
                + "                 [100.0, 0.0],\n"
                + "                 [101.0, 0.0],\n"
                + "                 [101.0, 1.0],\n"
                + "                 [100.0, 1.0],\n"
                + "                 [100.0, 0.0]\n"
                + "             ],\n"
                + "             [\n"
                + "                 [100.8, 0.8],\n"
                + "                 [100.8, 0.2],\n"
                + "                 [100.2, 0.2],\n"
                + "                 [100.2, 0.8],\n"
                + "                 [100.8, 0.8]\n"
                + "             ]\n"
                + "         ]\n"
                + "     }";
        String expected =
                "POLYGON ((100 0, 101 0, 101 1, 100 1, 100 0), (100.8 0.8, 100.8 0.2, 100.2 0.2, 100.2 0.8, 100.8 0.8))";
        testGeometry(line, expected);
    }

    @Test
    public void testEmptyGeometries() throws Exception {
        String input = "{\"type\":\"Point\",\"coordinates\":[]}";
        String expected = "POINT EMPTY";
        testGeometry(input, expected);

        input = "{\"type\":\"MultiPoint\",\"coordinates\":[[]]}";
        expected = "MULTIPOINT (EMPTY)";
        testGeometry(input, expected);

        input = "{\"type\":\"LineString\",\"coordinates\":[]}";
        expected = "LINESTRING EMPTY";
        testGeometry(input, expected);

        input = "{\"type\":\"MultiLineString\",\"coordinates\":[[]]}";
        expected = "MULTILINESTRING (EMPTY)";
        testGeometry(input, expected);

        input = "{\"type\":\"Polygon\",\"coordinates\":[]}";
        expected = "POLYGON EMPTY";
        testGeometry(input, expected);

        input = "{\"type\":\"MultiPolygon\",\"coordinates\":[[]]}";
        expected = "MULTIPOLYGON (EMPTY)";
        testGeometry(input, expected);
    }

    private void testGeometry(String json, String wkt) throws ParseException {
        Geometry p = GeoJSONReader.parseGeometry(json);

        WKTReader2 reader = new WKTReader2();
        Geometry e = reader.read(wkt);
        assertEquals(e, p);
    }

    @Test
    public void testFeatureCollectionWithRegularGeometryAttributeReadAndGeometryAfterProperties() throws Exception {
        String geojson1 = "{"
                + "'type': 'FeatureCollection',"
                + "'features': "
                + "[{"
                + "  'type': 'Feature',"
                + "  'id': 'features.0',"
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
                new GeoJSONReader(new ByteArrayInputStream(geojson1.getBytes(StandardCharsets.UTF_8)))) {
            FeatureCollection<SimpleFeatureType, SimpleFeature> features = reader.getFeatures();
            assertNotNull(features);
            assertFalse(features.isEmpty());
            f = DataUtilities.first(features);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
        assertNotNull(f);
        assertEquals("features.0", f.getID());
        WKTReader wkt = new WKTReader();
        assertEquals(wkt.read("POINT (0.1 0.1)"), f.getDefaultGeometry());
        assertEquals(wkt.read("LINESTRING (1.1 1.2, 1.3 1.4)"), f.getAttribute("otherGeometry"));
    }

    @Test
    public void testFeatureWithRegularGeometryAttributeReadAndGeometryAfterProperties() throws Exception {
        String geojson1 = "{"
                + "  'type': 'Feature',"
                + "  'id': 'features.0',"
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
        assertEquals(Point.class, f.getType().getGeometryDescriptor().getType().getBinding());
    }

    @Test
    public void testNotGeoJSON() throws IOException {
        String json = "{ \"foo\": \"bar\"}";
        try {
            GeoJSONReader.parseFeature(json);
            fail("Should have failed, not a GeoJSON");
        } catch (RuntimeException e) {
            assertEquals("Missing object type in GeoJSON Parsing, expected type=Feature here", e.getMessage());
        }
    }

    @Test
    public void testReadBoolean() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'id': 'features.0',"
                + "  'properties': {"
                + "    'boolTrue': true,"
                + "    'boolFalse': false"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson);
        assertEquals(true, feature.getAttribute("boolTrue"));
        assertEquals(false, feature.getAttribute("boolFalse"));
        assertEquals(
                Point.class, feature.getType().getGeometryDescriptor().getType().getBinding());
    }

    @Test
    public void testReadNestedObject() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'id': 'features.0',"
                + "  'properties': {"
                + "    'object': {"
                + "       'a': 10,"
                + "       'b': 'foo'"
                + "    }"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson);
        ObjectNode object = (ObjectNode) feature.getAttribute("object");
        assertEquals(10, object.get("a").intValue());
        assertEquals("foo", object.get("b").textValue());
    }

    @Test
    public void testReadNestedArray() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'id': 'features.0',"
                + "  'properties': {"
                + "    'array': [10, 'abc', null]"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson);
        List array = (List) feature.getAttribute("array");
        assertEquals(10, (double) array.get(0), 0d);
        assertEquals("abc", array.get(1));
        assertNull(array.get(2));
    }

    @Test
    public void testObjectInList() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'id': 'features.0',"
                + "  'properties': {"
                + "    'array': [{'a': 10}, {'b': 'foo'}]"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson);
        List array = (List) feature.getAttribute("array");
        assertEquals(10, ((ObjectNode) array.get(0)).get("a").asDouble(), 0d);
        assertEquals("foo", ((ObjectNode) array.get(1)).get("b").asText());
    }

    @Test
    public void testObjectInObject() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'id': 'features.0',"
                + "  'properties': {"
                + "    'parent': {'child': {'a': 10, 'b': 'foo'}}"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson);
        ObjectNode parent = (ObjectNode) feature.getAttribute("parent");
        assertEquals(10, ((ObjectNode) parent.get("child")).get("a").asDouble(), 0d);
        assertEquals("foo", ((ObjectNode) parent.get("child")).get("b").asText());
    }

    @Test
    public void testObjectsListsOutsideOfProperties() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "stac.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeature feature = reader.getFeature();
            Map topLevelAttributes = (Map) feature.getUserData().get(GeoJSONReader.TOP_LEVEL_ATTRIBUTES);
            assertEquals("1.0.0", ((TextNode) topLevelAttributes.get("stac_version")).asText());
            assertEquals("simple-collection", ((TextNode) topLevelAttributes.get("collection")).asText());
            ArrayNode stacExtensions = (ArrayNode) topLevelAttributes.get("stac_extensions");
            assertEquals(
                    "https://stac-extensions.github.io/eo/v1.0.0/schema.json",
                    ((TextNode) stacExtensions.get(0)).asText());
            ArrayNode links = (ArrayNode) topLevelAttributes.get("links");
            assertEquals("./collection.json", ((TextNode) links.get(0).get("href")).asText());
            ObjectNode assets = (ObjectNode) topLevelAttributes.get("assets");
            assertEquals(
                    "https://storage.googleapis.com/open-cogs/stac-examples/20201211_223832_CS2.jpg",
                    ((TextNode) assets.get("thumbnail").get("href")).asText());
        }
    }

    @Test
    public void testIdRetained() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'id': '123',"
                + "  'properties': {"
                + "    'parent': {'child': {'a': 10, 'b': 'foo'}}"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson);
        String id = feature.getID();
        assertEquals("123", id);
    }

    @Test
    public void testIdPrefix() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'id': '123',"
                + "  'properties': {"
                + "    'parent': {'child': {'a': 10, 'b': 'foo'}}"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson, GeoJSONReader.IdStrategy.PREFIX, "meh", null);
        String id = feature.getID();
        assertEquals("meh.123", id);
    }

    @Test
    public void testIdProvided() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'id': '123',"
                + "  'properties': {"
                + "    'parent': {'child': {'a': 10, 'b': 'foo'}}"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson, GeoJSONReader.IdStrategy.PROVIDED, null, null);
        String id = feature.getID();
        assertEquals("123", id);
    }

    @Test
    public void testIdAuto() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'id': '123',"
                + "  'properties': {"
                + "    'parent': {'child': {'a': 10, 'b': 'foo'}}"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson, GeoJSONReader.IdStrategy.AUTO, null, null);
        String id = feature.getID();
        assertEquals("features.0", id);
    }

    @Test
    public void testIdProvidedAltField() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'law': '123',"
                + "  'properties': {"
                + "    'parent': {'child': {'a': 10, 'b': 'foo'}}"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson, GeoJSONReader.IdStrategy.PROVIDED, null, "law");
        String id = feature.getID();
        assertEquals("123", id);
    }

    @Test
    public void testIdProvidedNoId() throws Exception {
        String geojson = "{"
                + "  'type': 'Feature',"
                + "  'properties': {"
                + "    'parent': {'child': {'a': 10, 'b': 'foo'}}"
                + "   },"
                + "  'geometry': {"
                + "    'type': 'Point',"
                + "    'coordinates': [0.1, 0.1]"
                + "  }"
                + "}";
        geojson = geojson.replace('\'', '"');
        SimpleFeature feature = GeoJSONReader.parseFeature(geojson, GeoJSONReader.IdStrategy.PROVIDED, null, null);
        String id = feature.getID();
        assertTrue(id.startsWith("fid"));
    }

    @Test
    public void testPaging() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "poi1.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeatureCollection features = reader.getFeatures();
            assertThat(features, instanceOf(PagingFeatureCollection.class));
            assertNotNull(((PagingFeatureCollection) features).matched);
            assertEquals(6, ((PagingFeatureCollection) features).matched.intValue());
            assertEquals(6, features.size());
            List<SimpleFeature> list = DataUtilities.list(features);
            // first file
            assertEquals("museam", list.get(0).getAttribute("NAME"));
            assertEquals("stock", list.get(1).getAttribute("NAME"));
            assertEquals("art", list.get(2).getAttribute("NAME"));
            assertEquals("lox", list.get(3).getAttribute("NAME"));
            // second file
            assertEquals("church", list.get(4).getAttribute("NAME"));
            assertEquals("fire", list.get(5).getAttribute("NAME"));
        }
    }

    @Test
    public void testPagingSTAC() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "stac1.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeatureCollection features = reader.getFeatures();
            assertThat(features, instanceOf(PagingFeatureCollection.class));
            assertNotNull(((PagingFeatureCollection) features).matched);
            assertEquals(2, ((PagingFeatureCollection) features).matched.intValue());
            assertEquals(2, features.size());
            List<SimpleFeature> list = DataUtilities.list(features);
            // first file
            assertEquals(
                    "S2A_OPER_MSI_L2A_TL_ESRI_20220720T131757_A036953_T50XNK_N04.00",
                    list.get(0).getAttribute("s2:granule_id"));
            // second file
            assertEquals(
                    "S2A_OPER_MSI_L2A_TL_ESRI_20220720T133447_A036953_T50XNH_N04.00",
                    list.get(1).getAttribute("s2:granule_id"));
        }
    }

    @Test
    public void testPropertyless() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "propertyless.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeatureCollection features = reader.getFeatures();
            assertThat(features, instanceOf(ListFeatureCollection.class));
            assertEquals(1, features.size());
            assertEquals(1, features.getSchema().getAttributeCount());
            assertEquals(
                    Polygon.class,
                    features.getSchema().getGeometryDescriptor().getType().getBinding());
            SimpleFeature feature = DataUtilities.first(features);
            assertNotNull(feature.getDefaultGeometry());
        }
    }

    @Test
    public void testGeometryless() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "geometryless.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeatureCollection features = reader.getFeatures();
            assertThat(features, instanceOf(ListFeatureCollection.class));
            assertEquals(1, features.size());
            assertNull(features.getSchema().getGeometryDescriptor());
            SimpleFeature feature = DataUtilities.first(features);
            assertNull(feature.getDefaultGeometry());
            assertEquals("landsat-8", feature.getAttribute("platform"));
        }
    }

    @Test
    public void testGeometryNull() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "geometrynull.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeatureCollection features = reader.getFeatures();
            assertEquals(3, features.size());
        }
    }

    @Test
    public void testEmpty() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "empty.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeatureCollection features = reader.getFeatures();
            assertEquals(0, features.size());
            assertTrue(features.isEmpty());
            assertNotNull(features.getSchema());
            assertEquals(0, features.getSchema().getAttributeCount());
        }
    }

    @Test
    public void testGuessDates() throws Exception {
        URL url = TestData.url(GeoJSONReaderTest.class, "dates.json");
        try (GeoJSONReader reader = new GeoJSONReader(url)) {
            SimpleFeatureCollection features = reader.getFeatures();
            assertThat(features, instanceOf(ListFeatureCollection.class));
            assertEquals(1, features.size());
            SimpleFeature feature = DataUtilities.first(features);
            assertThat(feature.getAttribute("date"), instanceOf(Date.class));
            assertThat(feature.getAttribute("dateTime"), instanceOf(Date.class));
            assertThat(feature.getAttribute("dateTimeFull"), instanceOf(Date.class));
            Object notDateValue = feature.getAttribute("notDate");
            assertThat(notDateValue, instanceOf(String.class));
            assertThat(notDateValue, equalTo("2009-08-29T223949_RE4_1B-NAC_1678843_48007"));
        }
    }
}
