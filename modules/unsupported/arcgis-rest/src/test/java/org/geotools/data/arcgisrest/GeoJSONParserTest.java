/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.arcgisrest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

public class GeoJSONParserTest {

    ArcGISRestFeatureReader reader;
    SimpleFeatureType fType;
    String json;

    @Before
    public void setUp() throws Exception {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("jsonfeature");
        builder.add("vint", Integer.class);
        builder.add("vfloat", Float.class);
        builder.add("vboolean", Boolean.class);
        builder.add("vstring", String.class);
        builder.add("geometry", Geometry.class);

        this.fType = builder.buildFeatureType();
    }

    @Test
    public void parsePointCoordinateList() throws Exception {

        List<Double> coordList = new ArrayList<>();

        try (GeoJSONParser parser =
                new GeoJSONParser(new ByteArrayInputStream("[1.0, 2.0]".getBytes()), this.fType, null)) {
            parser.parsePointCoordinates(coordList);
            double[] expCoords = {1.0f, 2.0f};

            assertArrayEquals(expCoords, GeoJSONParser.listToArray(coordList), 0.1f);
        }
    }

    @Test
    public void parsePointCoordinateArray() throws Exception {

        try (GeoJSONParser parser =
                new GeoJSONParser(new ByteArrayInputStream("[1.0, 2.0]".getBytes()), this.fType, null)) {
            double[] coords = parser.parsePointCoordinates();
            double[] expCoords = {1.0f, 2.0f};

            assertArrayEquals(expCoords, coords, 0.1f);
        }
    }

    @Test(expected = MalformedJsonException.class)
    public void parseInvalidPointCoordinate1() throws Exception {

        List<Double> coordList = new ArrayList<>();
        try (GeoJSONParser parser =
                new GeoJSONParser(new ByteArrayInputStream("[1.0 2.0]".getBytes()), this.fType, null)) {
            parser.parsePointCoordinates(coordList);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void parseInvalidPointCoordinate2() throws Exception {

        List<Double> coordList = new ArrayList<>();
        try (GeoJSONParser parser =
                new GeoJSONParser(new ByteArrayInputStream("[1.0, 2.0, 3.0, 4.0]".getBytes()), this.fType, null)) {
            parser.parsePointCoordinates(coordList);
        }
    }

    @Test
    public void parse3DCoordinatesArray() throws Exception {
        double[] expCoords = {102.0f, 0.0f, 103.0f, 1.0f, 104.0f, 0.0f, 105.0f, 1.0f};

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "[[102.0, 0.0, 100.0], [103.0, 1.0, 200.0], [104.0, 0.0, 300.0], [105.0, 1.0, 400.0]]"
                                .getBytes()),
                this.fType,
                null)) {
            double[] coords = parser.parseCoordinateArray();

            assertArrayEquals(expCoords, coords, 0.1f);
        }
    }

    @Test(expected = MalformedJsonException.class)
    public void parseIncorrectCoordinatesArray1() throws Exception {

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "[[102.0, 0.0], [103.0, 1.0], [104.0, 0.0 [104.5, 0.5]], [105.0, 1.0]]".getBytes()),
                this.fType,
                null)) {
            parser.parseCoordinateArray();
        }
    }

    @Test
    public void parseMultiPoint() throws Exception {

        double[] expCoords1 = {10.0f, 40.0f};
        double[] expCoords2 = {40.0f, 30.0f};
        double[] expCoords3 = {20.0f, 20.0f};
        double[] expCoords4 = {
            30.0f, 10.0f,
        };

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream("[[10, 40], [40, 30], [20, 20], [30, 10]]".getBytes()), this.fType, null)) {
            List<double[]> coords = parser.parseMultiPointCoordinates();

            assertArrayEquals(expCoords1, coords.get(0), 0.1f);
            assertArrayEquals(expCoords2, coords.get(1), 0.1f);
            assertArrayEquals(expCoords3, coords.get(2), 0.1f);
            assertArrayEquals(expCoords4, coords.get(3), 0.1f);
        }
    }

    @Test
    public void parseLineString() throws Exception {
        double[] expCoords = {100.0f, 0.0f, 101.0f, 0.0f, 101.0f, 1.0f, 100.0f, 1.0f, 105.0f, 1.0f};

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "[[100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [105.0, 1.0]]".getBytes()),
                this.fType,
                null)) {
            double[] coords = parser.parseLineStringCoordinates();

            assertArrayEquals(expCoords, coords, 0.1f);
        }
    }

    @Test
    public void parseMultiLineString() throws Exception {
        double[] expLine1 = {10.0f, 10.0f, 20.0f, 20.0f, 10.0f, 40.0f};
        double[] expLine2 = {40.0f, 40.0f, 30.0f, 30.0f, 40.0f, 20.0f, 30.0f, 10.0f};

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "[[[10, 10], [20, 20], [10, 40]], [[40, 40], [30, 30], [40, 20], [30, 10]]]".getBytes()),
                this.fType,
                null)) {
            List<double[]> lines = parser.parseMultiLineStringCoordinates();

            assertArrayEquals(expLine1, lines.get(0), 0.1f);
            assertArrayEquals(expLine2, lines.get(1), 0.1f);
        }
    }

    @Test
    public void parsePolygon() throws Exception {
        double[] expCoords = {100.0f, 0.0f, 101.0f, 0.0f, 101.0f, 1.0f, 100.0f, 1.0f, 100.0f, 0.0f};

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "[[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]]".getBytes()),
                this.fType,
                null)) {
            List<double[]> rings = parser.parsePolygonCoordinates();

            assertArrayEquals(expCoords, rings.get(0), 0.1f);
        }
    }

    @Test
    public void parsePolygonWithHoles() throws Exception {
        double[] expRing1 = {35.0f, 10.0f, 45.0f, 45.0f, 15.0f, 40.0f, 10.0f, 20.0f, 35.0f, 10.0f};
        double[] expRing2 = {20.0f, 30.0f, 35.0f, 35.0f, 30.0f, 20.0f, 20.0f, 30.0f};

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "[ [[35, 10], [45, 45], [15, 40], [10, 20], [35, 10]], [[20, 30], [35, 35], [30, 20], [20, 30]] ]"
                                .getBytes()),
                this.fType,
                null)) {
            List<double[]> rings = parser.parsePolygonCoordinates();

            assertArrayEquals(expRing1, rings.get(0), 0.1f);
            assertArrayEquals(expRing2, rings.get(1), 0.1f);
        }
    }

    @Test
    public void parseMultiPolygon() throws Exception {
        double[] expPoly1 = {30.0f, 20.0f, 45.0f, 40.0f, 10.0f, 40.0f, 30.0f, 20.0f};
        double[] expPoly2 = {15.0f, 5.0f, 40.0f, 10.0f, 10.0f, 20.0f, 5.0f, 10.0f, 15.0f, 5.0f};

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "[[ [[30, 20], [45, 40], [10, 40], [30, 20]]], [[[15, 5], [40, 10], [10, 20], [5, 10], [15, 5]] ]]"
                                .getBytes()),
                this.fType,
                null)) {
            List<List<double[]>> polys = parser.parseMultiPolygonCoordinates();

            assertArrayEquals(expPoly1, polys.get(0).get(0), 0.1f);
            assertArrayEquals(expPoly2, polys.get(1).get(0), 0.1f);
        }
    }

    @Test
    public void parseMultiPolygonWithHoles() throws Exception {
        double[] expRing1 = {40.0f, 40.0f, 20.0f, 45.0f, 45.0f, 30.0f, 40.0f, 40.0f};
        double[] expRing2 = {20.0f, 35.0f, 10.0f, 30.0f, 10.0f, 10.0f, 30.0f, 5.0f, 45.0f, 20.0f, 20.0f, 35.0f};
        double[] expRing3 = {30.0f, 20.0f, 20.0f, 15.0f, 20.0f, 25.0f, 30.0f, 20.0f};

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "[[[[40, 40], [20, 45], [45, 30], [40, 40]]],[[[20, 35], [10, 30], [10, 10], [30, 5], [45, 20], [20, 35]],[[30, 20], [20, 15], [20, 25], [30, 20]]]]"
                                .getBytes()),
                this.fType,
                null)) {
            List<List<double[]>> rings = parser.parseMultiPolygonCoordinates();

            assertArrayEquals(expRing1, rings.get(0).get(0), 0.1f);
            assertArrayEquals(expRing2, rings.get(1).get(0), 0.1f);
            assertArrayEquals(expRing3, rings.get(1).get(1), 0.1f);
        }
    }

    @Test(expected = JsonSyntaxException.class)
    public void parseGeometryIncorrectGeometryType() throws Exception {

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream("{\"type\": \"POINT\", \"coordinates\": [1.0, 2.0]}".getBytes()),
                this.fType,
                null)) {
            parser.parseGeometry();
        }
    }

    @Test
    public void parseGeometryNull() throws Exception {
        try (GeoJSONParser parser = new GeoJSONParser(new ByteArrayInputStream("null".getBytes()), this.fType, null)) {
            Geometry geom = parser.parseGeometry();
            assertTrue(geom.isEmpty());
        }
    }

    @Test
    public void parseGeometryPoint() throws Exception {

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream("{\"type\": \"Point\", \"coordinates\": [1.0, 2.0]}".getBytes()),
                this.fType,
                null)) {
            Geometry geom = parser.parseGeometry();

            assertEquals("Point", geom.getClass().getSimpleName());
            assertEquals(1.0f, ((Point) (geom)).getX(), 0.1f);
            assertEquals(2.0f, ((Point) (geom)).getY(), 0.1f);
        }
    }

    @Test
    public void parseGeometryPoint3D() throws Exception {

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream("{\"type\": \"Point\", \"coordinates\": [1.0, 2.0, 3.0]}".getBytes()),
                this.fType,
                null)) {
            Geometry geom = parser.parseGeometry();

            assertEquals("Point", geom.getClass().getSimpleName());
            assertEquals(1.0f, ((Point) (geom)).getX(), 0.1f);
            assertEquals(2.0f, ((Point) (geom)).getY(), 0.1f);
        }
    }

    @Test
    public void parseGeometryMultiPoint() throws Exception {
        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "{\"type\": \"MultiPoint\",\"coordinates\": [[10, 40], [40, 30], [20, 20], [30, 10]]}"
                                .getBytes()),
                this.fType,
                null)) {
            Geometry geom = parser.parseGeometry();

            assertEquals("MultiPoint", geom.getClass().getSimpleName());
            assertEquals(30.0f, ((Point) (((MultiPoint) (geom)).getGeometryN(3))).getX(), 0.1f);
            assertEquals(10.0f, ((Point) (((MultiPoint) (geom)).getGeometryN(3))).getY(), 0.1f);
        }
    }

    @Test
    public void parseGeometryLineString() throws Exception {

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "{ \"type\": \"LineString\", \"coordinates\": [[30, 10], [10, 30], [40, 40]]}".getBytes()),
                this.fType,
                null)) {
            Geometry geom = parser.parseGeometry();

            assertEquals("LineString", geom.getClass().getSimpleName());
            assertEquals(22.6f, ((LineString) (geom)).getCentroid().getX(), 0.1f);
            assertEquals(27.9f, ((LineString) (geom)).getCentroid().getY(), 0.1f);
        }
    }

    @Test
    public void parseGeometryMultiLineString() throws Exception {

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "{ \"type\": \"MultiLineString\", \"coordinates\":  [[ [100.0, 0.0], [101.0, 1.0] ],[ [102.0, 2.0], [103.0, 3.0] ]]}"
                                .getBytes()),
                this.fType,
                null)) {
            Geometry geom = parser.parseGeometry();

            assertEquals("MultiLineString", geom.getClass().getSimpleName());
            assertEquals(101.5f, ((MultiLineString) (geom)).getCentroid().getX(), 0.1f);
            assertEquals(1.5f, ((MultiLineString) (geom)).getCentroid().getY(), 0.1f);
        }
    }

    @Test
    public void parsePropertiesNull() throws Exception {
        try (GeoJSONParser parser = new GeoJSONParser(new ByteArrayInputStream("null".getBytes()), this.fType, null)) {
            Map<String, Object> properties = parser.parseProperties();

            assertEquals(0, properties.size());
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void parseMalformedProperties() throws Exception {
        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream("{\"vstring\": \"value0\", {a:1}, \"vint\": 12}".getBytes()),
                this.fType,
                null)) {
            parser.parseProperties();
        }
    }

    @Test
    public void parseNullValuesProperties() throws Exception {
        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream("{\"vstring\": \"value0\", \"vint\": null}".getBytes()), this.fType, null)) {
            Map<String, Object> properties = parser.parseProperties();

            assertEquals("value0", properties.get("vstring"));
            assertNull(properties.get("vint"));
        }
    }

    @Test
    public void parseProperties() throws Exception {
        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream("{\"vstring\": \"value0\", \"vint\": 12, \"vboolean\": true}".getBytes()),
                this.fType,
                null)) {
            Map<String, Object> properties = parser.parseProperties();

            assertEquals("value0", properties.get("vstring"));
            assertEquals(12.0, properties.get("vint"));
            assertEquals(true, properties.get("vboolean"));
        }
    }

    @Test
    public void parseFeature() throws Exception {

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "{\"type\":\"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [1.0, 2.0]}, \"properties\":{\"vstring\": \"value0\", \"vint\": 12, \"vboolean\": true}}"
                                .getBytes()),
                this.fType,
                null)) {
            SimpleFeature feat = parser.parseFeature();

            assertEquals(1.0f, ((Point) (feat.getDefaultGeometry())).getX(), 0.1f);
            assertEquals(2.0f, ((Point) (feat.getDefaultGeometry())).getY(), 0.1f);
            assertEquals("value0", feat.getAttribute("vstring"));
            assertTrue((Boolean) (feat.getAttribute("vboolean")));
            assertEquals(12, feat.getAttribute("vint"));
        }
    }

    @Test
    public void parseFeaturesNotSameId() throws Exception {
        String json = this.json = ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/properties.geo.json");

        try (GeoJSONParser parser = new GeoJSONParser(new ByteArrayInputStream(json.getBytes()), this.fType, null)) {
            parser.parseFeatureCollection();
            Set<String> ids = new HashSet<>();
            while (parser.hasNext()) {
                SimpleFeature feat = parser.next();
                assertNotNull(feat);
                String id = feat.getID();
                assertTrue("Id should not be identical to previous feature id.", ids.add(id));
            }
        }
    }

    @Test
    public void parseFeatureNoProperties() throws Exception {

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream(
                        "{\"type\":\"Feature\", \"geometry\": {\"type\": \"Point\", \"coordinates\": [1.0, 2.0]}, \"properties\":null}"
                                .getBytes()),
                this.fType,
                null)) {
            SimpleFeature feat = parser.parseFeature();
            assertEquals(1.0f, ((Point) (feat.getDefaultGeometry())).getX(), 0.1f);
            assertEquals(2.0f, ((Point) (feat.getDefaultGeometry())).getY(), 0.1f);
        }
    }

    @Test
    public void parseFeatureNoPropertiesNoGeometry() throws Exception {

        try (GeoJSONParser parser = new GeoJSONParser(
                new ByteArrayInputStream("{\"type\":\"Feature\", \"geometry\": null, \"properties\":null}".getBytes()),
                this.fType,
                null)) {
            parser.parseFeature();
            assertTrue(true);
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void parseEmptyFeatureCollection() throws Exception {

        this.json = ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/noFeatures.geo.json");

        try (GeoJSONParser parser =
                new GeoJSONParser(new ByteArrayInputStream(this.json.getBytes()), this.fType, null)) {
            parser.parseFeatureCollection();
            parser.next();
        }
    }

    @Test
    public void parseMalformedFeatureCollection() throws Exception {

        this.json = ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/malformed.geo.json");

        try (GeoJSONParser parser =
                new GeoJSONParser(new ByteArrayInputStream(this.json.getBytes()), this.fType, null)) {
            parser.parseFeatureCollection();
            assertTrue(parser.hasNext());
            assertNotNull(parser.next());
            assertFalse(parser.hasNext());
        }
    }

    @Test(expected = IOException.class)
    public void parseError() throws Exception {

        this.json = ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/error.json");

        try (GeoJSONParser parser =
                new GeoJSONParser(new ByteArrayInputStream(this.json.getBytes()), this.fType, null)) {
            parser.parseFeatureCollection();
        }
    }
}
