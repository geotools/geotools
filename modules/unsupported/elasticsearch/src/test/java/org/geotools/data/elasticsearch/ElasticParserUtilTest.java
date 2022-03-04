/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.github.davidmoten.geo.GeoHash;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

// unfortunately Geometry.equals(Geometry) does not behave as Geometry.equals(Object)
// see https://github.com/locationtech/jts/issues/159
@SuppressWarnings({"unchecked", "PMD.SimplifiableTestAssertion"})
public class ElasticParserUtilTest {

    private ElasticParserUtil parserUtil;

    private GeometryFactory geometryFactory;

    private Map<String, Object> properties;

    private Random rand;

    private RandomGeometryBuilder rgb;

    @Before
    public void setUp() {
        parserUtil = new ElasticParserUtil();
        geometryFactory = new GeometryFactory();
        properties = new LinkedHashMap<>();
        rand = new Random(123456789L);
        rgb = new RandomGeometryBuilder();
    }

    @Test
    public void testParseGeoPointPatternForNegatives() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        final String value = lat + "," + lon;
        final Geometry geom = parserUtil.createGeometry(value);
        assertTrue(geom.equals(geometryFactory.createPoint(new Coordinate(lon, lat))));
    }

    @Test
    public void testGeoPointPatternForFractions() {
        final double lat = rand.nextDouble() * 2 - 1;
        final double lon = rand.nextDouble() * 2 - 1;
        final String value = (lat + "," + lon).replace("0.", ".");
        final Geometry geom = parserUtil.createGeometry(value);
        assertTrue(geom.equals(geometryFactory.createPoint(new Coordinate(lon, lat))));
    }

    @Test
    public void testGeoPointPatternForWholeValues() {
        final Geometry geom = parserUtil.createGeometry("45,90");
        assertTrue(geom.equals(geometryFactory.createPoint(new Coordinate(90, 45))));
    }

    @Test
    public void testGeoPointPatternWithSpace() {
        final Geometry geom = parserUtil.createGeometry("45, 90");
        assertTrue(geom.equals(geometryFactory.createPoint(new Coordinate(90, 45))));
    }

    @Test
    public void testGeoPointAsDoubleProperties() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        properties.put("lat", lat);
        properties.put("lon", lon);
        final Geometry geometry = parserUtil.createGeometry(properties);
        assertTrue(geometry.equals(geometryFactory.createPoint(new Coordinate(lon, lat))));
    }

    @Test
    public void testGeoPointAsIntegerProperties() {
        final int lat = rand.nextInt(180) - 90;
        final int lon = rand.nextInt(360) - 180;
        properties.put("lat", lat);
        properties.put("lon", lon);
        final Geometry geometry = parserUtil.createGeometry(properties);
        assertTrue(geometry.equals(geometryFactory.createPoint(new Coordinate(lon, lat))));
    }

    @Test
    public void testGeoPointAsStringProperties() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        properties.put("lat", String.valueOf(lat));
        properties.put("lon", String.valueOf(lon));
        final Geometry geometry = parserUtil.createGeometry(properties);
        assertTrue(geometry.equals(geometryFactory.createPoint(new Coordinate(lon, lat))));
    }

    @Test
    public void testGeoPointAsInvalidProperties() {
        properties.put("lat", true);
        properties.put("lon", true);
        final Geometry geometry = parserUtil.createGeometry(properties);
        assertNull(geometry);
    }

    @Test
    public void testGeoPointAsUnrecognizedProperties() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        properties.put("latD", lat);
        properties.put("lonD", lon);
        final Geometry geometry = parserUtil.createGeometry(properties);
        assertNull(geometry);
    }

    @Test
    public void testGeoPointAsStringArray() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        final Geometry geometry =
                parserUtil.createGeometry(Arrays.asList(String.valueOf(lon), String.valueOf(lat)));
        assertTrue(geometry.equals(geometryFactory.createPoint(new Coordinate(lon, lat))));
    }

    @Test
    public void testGeoPointAsInvalidArray() {
        final Geometry geometry = parserUtil.createGeometry(Arrays.asList(true, true));
        assertNull(geometry);
    }

    @Test
    public void testGeoPointAsDoubleArray() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        final Geometry geometry = parserUtil.createGeometry(Arrays.asList(lon, lat));
        assertTrue(geometry.equals(geometryFactory.createPoint(new Coordinate(lon, lat))));
    }

    @Test
    public void testGeoHash() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        String geohash = GeoHash.encodeHash(lat, lon, 11);
        final Geometry expected = geometryFactory.createPoint(new Coordinate(lon, lat));
        final Geometry actual = parserUtil.createGeometry(geohash);
        assertEquals(0, expected.distance(actual), 1e-5);
    }

    @Test
    public void testInvalidStringGeometry() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        assertNull(parserUtil.createGeometry(String.valueOf(lat)));
        assertNull(parserUtil.createGeometry(lat + "," + lon + "," + 0));
        assertNull(parserUtil.createGeometry("x:" + lat + "," + lon));
    }

    @Test
    public void testGeoShapePoint() throws IOException {
        Point geom = rgb.createRandomPoint();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapePointString() {
        Point geom = rgb.createRandomPoint();
        final Map<String, Object> map = new HashMap<>();
        final List<String> coords = new ArrayList<>();
        coords.add(String.valueOf(geom.getX()));
        coords.add(String.valueOf(geom.getY()));
        map.put("coordinates", coords);
        map.put("type", "Point");
        assertTrue(parserUtil.createGeometry(map).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapePointWkt() {
        Point geom = rgb.createRandomPoint();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeLineString() throws IOException {
        LineString geom = rgb.createRandomLineString();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeLineStringWkt() {
        LineString geom = rgb.createRandomLineString();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapePolygon() throws IOException {
        Polygon geom = rgb.createRandomPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeCircle() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("type", "circle");
        inputMap.put("radius", "5nmi");
        List<String> posList = new ArrayList<>();
        posList.add("8.0");
        posList.add("35.0");
        inputMap.put("coordinates", posList);
        Geometry geometry = parserUtil.createGeometry(inputMap);

        assertNotNull(geometry);
    }

    @Test
    public void testGeoShapeCircleWithMissingCenter() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("type", "circle");
        inputMap.put("radius", "5nmi");
        Geometry geometry = parserUtil.createGeometry(inputMap);

        assertNull(geometry);
    }

    @Test
    public void testGeoShapeCircleWithInvalidRadii() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("type", "circle");
        inputMap.put("radius", "5qx");
        List<String> posList = new ArrayList<>();
        posList.add("8.0");
        posList.add("35.0");
        inputMap.put("coordinates", posList);
        Geometry geometry = parserUtil.createGeometry(inputMap);

        assertNull(geometry);
    }

    @Test
    public void testGeoShapeCircleWithSmallRadii() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("type", "circle");
        inputMap.put("radius", "0.0000000000001m");
        List<String> posList = new ArrayList<>();
        posList.add("8.0");
        posList.add("35.0");
        inputMap.put("coordinates", posList);
        Geometry geometry = parserUtil.createGeometry(inputMap);

        assertNull(geometry);
    }

    @Test
    public void testGeoShapeCircleWithMissingRadii() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("type", "circle");
        List<String> posList = new ArrayList<>();
        posList.add("8.0");
        posList.add("35.0");
        inputMap.put("coordinates", posList);
        Geometry geometry = parserUtil.createGeometry(inputMap);

        assertNull(geometry);
    }

    @Test
    public void testGeoShapePolygonWkt() {
        Polygon geom = rgb.createRandomPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiPoint() throws IOException {
        MultiPoint geom = rgb.createRandomMultiPoint();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiPointWkt() {
        MultiPoint geom = rgb.createRandomMultiPoint();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiLineString() throws IOException {
        MultiLineString geom = rgb.createRandomMultiLineString();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiLineStringWkt() {
        MultiLineString geom = rgb.createRandomMultiLineString();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiPolygon() throws IOException {
        MultiPolygon geom = rgb.createRandomMultiPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiPolygonWkt() {
        MultiPolygon geom = rgb.createRandomMultiPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeGeometryCollection() throws IOException {
        rgb.setNumGeometries(5);
        GeometryCollection geom = rgb.createRandomGeometryCollection();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeGeometryCollectionWkt() {
        rgb.setNumGeometries(5);
        GeometryCollection geom = rgb.createRandomGeometryCollection();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeEnvelope() {
        Envelope envelope = rgb.createRandomEnvelope();
        Geometry expected = geometryFactory.toGeometry(envelope);
        assertTrue(parserUtil.createGeometry(rgb.toMap(envelope)).equalsExact(expected, 1e-9));
    }

    @Test
    public void testUnrecognizedGeometry() {
        final Geometry geom = parserUtil.createGeometry(3.0);
        assertNull(geom);
    }

    @Test
    public void testReadStringField() {
        properties.put("attr", "value");
        List<Object> values = parserUtil.readField(properties, "attr");
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testReadNumericField() {
        properties.put("attr", 2.3);
        List<Object> values = parserUtil.readField(properties, "attr");
        assertEquals(1, values.size());
        assertEquals(2.3, values.get(0));
    }

    @Test
    public void testReadStringFieldWithConfuser() {
        properties.put("parent1", new LinkedHashMap<String, Object>());
        ((Map) properties.get("parent1")).put("attr", "value2");
        properties.put("attr", "value");
        properties.put("parent2", new LinkedHashMap<String, Object>());
        ((Map) properties.get("parent2")).put("attr", "value3");
        List<Object> values = parserUtil.readField(properties, "attr");
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testReadInnerString() {
        properties.put("parent", new LinkedHashMap<String, Object>());
        ((Map) properties.get("parent")).put("attr", "value");
        List<Object> values = parserUtil.readField(properties, "parent.attr");
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testReadInnerStringArray() {
        properties.put("parent", new LinkedHashMap<String, Object>());
        ((Map) properties.get("parent")).put("attr", Arrays.asList("value1", "value2"));
        List<Object> values = parserUtil.readField(properties, "parent.attr");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testReadStringFromObjectArray() {
        properties.put("parent", new ArrayList<Map<String, Object>>());
        ((List) properties.get("parent")).add(new LinkedHashMap<String, Object>());
        ((Map) ((List) properties.get("parent")).get(0)).put("attr", "value1");
        ((List) properties.get("parent")).add(new LinkedHashMap<String, Object>());
        ((Map) ((List) properties.get("parent")).get(1)).put("attr", "value2");
        List<Object> values = parserUtil.readField(properties, "parent.attr");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testReadStringFromObjectArrayOnceRemoved() {
        properties.put("parent", new ArrayList<Map<String, Object>>());
        ((List) properties.get("parent")).add(new LinkedHashMap<String, Object>());
        ((Map) ((List) properties.get("parent")).get(0))
                .put("child", new LinkedHashMap<String, Object>());
        ((Map) ((Map) ((List) properties.get("parent")).get(0)).get("child")).put("attr", "value1");
        ((List) properties.get("parent")).add(new LinkedHashMap<String, Object>());
        ((Map) ((List) properties.get("parent")).get(1))
                .put("child", new LinkedHashMap<String, Object>());
        ((Map) ((Map) ((List) properties.get("parent")).get(1)).get("child")).put("attr", "value2");
        List<Object> values = parserUtil.readField(properties, "parent.child.attr");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testReadMapField() {
        final Map<String, Object> map = new LinkedHashMap<>();
        properties.put("attr", map);
        map.put("attr2", "value2");
        map.put("attr3", "value3");
        List<Object> values = parserUtil.readField(properties, "attr");
        assertEquals(1, values.size());
        assertEquals(values.get(0), map);
    }

    @Test
    public void testConvertToMeters() {
        double distance = ElasticParserUtil.convertToMeters("1.2mm");
        assertEquals(0.0012, distance, 0.0000000001);
        distance = ElasticParserUtil.convertToMeters("1.2");
        assertEquals(1.2, distance, 0.0000000001);
        distance = ElasticParserUtil.convertToMeters("12");
        assertEquals(12.0, distance, 0.0000000001);
        distance = ElasticParserUtil.convertToMeters("0.12cm");
        assertEquals(0.0012, distance, 0.0000000001);
        try {
            ElasticParserUtil.convertToMeters("999xyz");
            fail("Shouldn't get here");
        } catch (IllegalArgumentException ignored) {

        }
        try {
            ElasticParserUtil.convertToMeters("mm1.2");
            fail("Shouldn't get here");
        } catch (IllegalArgumentException ignored) {

        }
        try {
            ElasticParserUtil.convertToMeters(".2");
            fail("Shouldn't get here");
        } catch (IllegalArgumentException ignored) {

        }
        try {
            ElasticParserUtil.convertToMeters(".2m");
            fail("Shouldn't get here");
        } catch (IllegalArgumentException ignored) {

        }
    }

    /** This test ensures that dots within attribute names are handled. */
    @Test
    public void testAttributesContainingDots() {
        properties.put("coalesceentity.name", "value");
        List<Object> values = parserUtil.readField(properties, "coalesceentity.name");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(properties.get("coalesceentity.name"), values.get(0));
    }

    /**
     * This test ensures that GeoJSON formatted values are returned correctly when dots are used
     * within the attribute name.
     */
    @Test
    public void testGeoJSONAttributesContainingDots() {

        Map<String, Object> geojson = new HashMap<>();
        geojson.put("type", "circle");
        geojson.put("coordinates", "[1,1]");
        geojson.put("radius", "1m");

        properties.put("coalesceentity.geo", geojson);
        List<Object> values = parserUtil.readField(properties, "coalesceentity.geo");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(properties.get("coalesceentity.geo"), values.get(0));
    }
}
