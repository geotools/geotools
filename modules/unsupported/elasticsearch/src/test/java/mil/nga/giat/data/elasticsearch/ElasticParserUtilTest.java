/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import static org.junit.Assert.*;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.davidmoten.geo.GeoHash;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class ElasticParserUtilTest {

    private ElasticParserUtil parserUtil;

    private GeometryFactory geometryFactory;

    private Map<String, Object> properties;

    private Random rand;

    private RandomGeometryBuilder rgb;

    @Before
    public void setUp() throws ReflectiveOperationException {
        parserUtil = new ElasticParserUtil();
        geometryFactory = new GeometryFactory();
        properties = new LinkedHashMap<>();
        rand = new Random(123456789l);
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
        assertTrue(geometry == null);
    }

    @Test
    public void testGeoPointAsUnrecognizedProperties() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        properties.put("latD", lat);
        properties.put("lonD", lon);
        final Geometry geometry = parserUtil.createGeometry(properties);
        assertTrue(geometry == null);
    }

    @Test
    public void testGeoPointAsStringArray() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        final Geometry geometry = parserUtil.createGeometry(Arrays.asList(new String[]{
            String.valueOf(lon), String.valueOf(lat)}));
        assertTrue(geometry.equals(geometryFactory.createPoint(new Coordinate(lon, lat))));
    }

    @Test
    public void testGeoPointAsInvalidArray() {
        final Geometry geometry = parserUtil.createGeometry(Arrays.asList(new Boolean[]{true, true}));
        assertTrue(geometry == null);
    }

    @Test
    public void testGeoPointAsDoubleArray() {
        final double lat = rand.nextDouble() * 90 - 90;
        final double lon = rand.nextDouble() * 180 - 180;
        final Geometry geometry = parserUtil.createGeometry(Arrays.asList(new Double[]{lon, lat}));
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
        assertTrue(parserUtil.createGeometry(String.valueOf(lat)) == null);
        assertTrue(parserUtil.createGeometry(lat + "," + lon + "," + 0) == null);
        assertTrue(parserUtil.createGeometry("x:" + lat + "," + lon) == null);
    }

    @Test
    public void testGeoShapePoint() throws JsonParseException, JsonMappingException, IOException {
        Point geom = rgb.createRandomPoint();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapePointString() throws JsonParseException, JsonMappingException, IOException {
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
    public void testGeoShapePointWkt() throws JsonParseException, JsonMappingException, IOException {
        Point geom = rgb.createRandomPoint();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeLineString() throws JsonParseException, JsonMappingException, IOException {
        LineString geom = rgb.createRandomLineString();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeLineStringWkt() throws JsonParseException, JsonMappingException, IOException {
        LineString geom = rgb.createRandomLineString();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapePolygon() throws JsonParseException, JsonMappingException, IOException {
        Polygon geom = rgb.createRandomPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeCircle() throws JsonParseException, JsonMappingException, IOException {
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

    public void testGeoShapePolygonWkt() throws JsonParseException, JsonMappingException, IOException {
        Polygon geom = rgb.createRandomPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiPoint() throws JsonParseException, JsonMappingException, IOException {
        MultiPoint geom = rgb.createRandomMultiPoint();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiPointWkt() throws JsonParseException, JsonMappingException, IOException {
        MultiPoint geom = rgb.createRandomMultiPoint();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiLineString() throws JsonParseException, JsonMappingException, IOException {
        MultiLineString geom = rgb.createRandomMultiLineString();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiLineStringWkt() throws JsonParseException, JsonMappingException, IOException {
        MultiLineString geom = rgb.createRandomMultiLineString();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiPolygon() throws JsonParseException, JsonMappingException, IOException {
        MultiPolygon geom = rgb.createRandomMultiPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeMultiPolygonWkt() throws JsonParseException, JsonMappingException, IOException {
        MultiPolygon geom = rgb.createRandomMultiPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeGeometryCollection() throws JsonParseException, JsonMappingException, IOException {
        rgb.setNumGeometries(5);
        GeometryCollection geom = rgb.createRandomGeometryCollection();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeGeometryCollectionWkt() throws JsonParseException, JsonMappingException, IOException {
        rgb.setNumGeometries(5);
        GeometryCollection geom = rgb.createRandomGeometryCollection();
        assertTrue(parserUtil.createGeometry(rgb.toWkt(geom)).equalsExact(geom, 1e-9));
    }

    @Test
    public void testGeoShapeEnvelope() throws JsonParseException, JsonMappingException, IOException {
        Envelope envelope = rgb.createRandomEnvelope();
        Geometry expected = geometryFactory.toGeometry(envelope);
        assertTrue(parserUtil.createGeometry(rgb.toMap(envelope)).equalsExact(expected, 1e-9));
    }

    @Test
    public void testUnrecognizedGeometry() {
        final Geometry geom = parserUtil.createGeometry(3.0);
        assertTrue(geom == null);
    }

    @Test
    public void testReadStringField() {
        properties.put("attr", "value");
        List<Object> values = parserUtil.readField(properties, "attr");
        assertTrue(values.size() == 1);
        assertTrue(values.get(0).equals("value"));
    }

    @Test
    public void testReadNumericField() {
        properties.put("attr", 2.3);
        List<Object> values = parserUtil.readField(properties, "attr");
        assertTrue(values.size() == 1);
        assertTrue(values.get(0).equals(2.3));
    }

    @Test
    public void testReadStringFieldWithConfuser() {
        properties.put("parent1", new LinkedHashMap<String, Object>());
        ((Map) properties.get("parent1")).put("attr", "value2");
        properties.put("attr", "value");
        properties.put("parent2", new LinkedHashMap<String, Object>());
        ((Map) properties.get("parent2")).put("attr", "value3");
        List<Object> values = parserUtil.readField(properties, "attr");
        assertTrue(values.size() == 1);
        assertTrue(values.get(0).equals("value"));
    }

    @Test
    public void testReadInnerString() {
        properties.put("parent", new LinkedHashMap<String, Object>());
        ((Map) properties.get("parent")).put("attr", "value");
        List<Object> values = parserUtil.readField(properties, "parent.attr");
        assertTrue(values.size() == 1);
        assertTrue(values.get(0).equals("value"));
    }

    @Test
    public void testReadInnerStringArray() {
        properties.put("parent", new LinkedHashMap<String, Object>());
        ((Map) properties.get("parent")).put("attr", Arrays.asList(new String[]{"value1", "value2"}));
        List<Object> values = parserUtil.readField(properties, "parent.attr");
        assertTrue(values.size() == 2);
        assertTrue(values.get(0).equals("value1"));
        assertTrue(values.get(1).equals("value2"));
    }

    @Test
    public void testReadStringFromObjectArray() {
        properties.put("parent", new ArrayList<Map<String, Object>>());
        ((List) properties.get("parent")).add(new LinkedHashMap<String, Object>());
        ((Map) ((List) properties.get("parent")).get(0)).put("attr", "value1");
        ((List) properties.get("parent")).add(new LinkedHashMap<String, Object>());
        ((Map) ((List) properties.get("parent")).get(1)).put("attr", "value2");
        List<Object> values = parserUtil.readField(properties, "parent.attr");
        assertTrue(values.size() == 2);
        assertTrue(values.get(0).equals("value1"));
        assertTrue(values.get(1).equals("value2"));
    }

    @Test
    public void testReadStringFromObjectArrayOnceRemoved() {
        properties.put("parent", new ArrayList<Map<String, Object>>());
        ((List) properties.get("parent")).add(new LinkedHashMap<String, Object>());
        ((Map) ((List) properties.get("parent")).get(0)).put("child", new LinkedHashMap<String, Object>());
        ((Map) ((Map) ((List) properties.get("parent")).get(0)).get("child")).put("attr", "value1");
        ((List) properties.get("parent")).add(new LinkedHashMap<String, Object>());
        ((Map) ((List) properties.get("parent")).get(1)).put("child", new LinkedHashMap<String, Object>());
        ((Map) ((Map) ((List) properties.get("parent")).get(1)).get("child")).put("attr", "value2");
        List<Object> values = parserUtil.readField(properties, "parent.child.attr");
        assertTrue(values.size() == 2);
        assertTrue(values.get(0).equals("value1"));
        assertTrue(values.get(1).equals("value2"));
    }

    @Test
    public void testReadMapField() {
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        properties.put("attr", map);
        map.put("attr2", "value2");
        map.put("attr3", "value3");
        List<Object> values = parserUtil.readField(properties, "attr");
        assertTrue(values.size() == 1);
        assertTrue(values.get(0).equals(map));
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
            distance = ElasticParserUtil.convertToMeters("999xyz");
            fail("Shouldn't get here");
        }
        catch(IllegalArgumentException iae) {
            
        }
         try {
            distance = ElasticParserUtil.convertToMeters("mm1.2");
            fail("Shouldn't get here");
        }
        catch(IllegalArgumentException iae) {
            
        }
        try {
            distance = ElasticParserUtil.convertToMeters(".2");
            fail("Shouldn't get here");
        }
        catch(IllegalArgumentException iae) {
            
        }
        try {
            distance = ElasticParserUtil.convertToMeters(".2m");
            fail("Shouldn't get here");
        }
        catch(IllegalArgumentException iae) {
            
        }
    }

    /**
     * This test ensures that dots within attribute names are handled.
     */
    @Test
    public void testAttributesContainingDots() {
        properties.put("coalesceentity.name", "value");
        List<Object> values = parserUtil.readField(properties, "coalesceentity.name");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(properties.get("coalesceentity.name"), values.get(0));
    }

    /**
     * This test ensures that GeoJSON formatted values are returned correctly when dots are used within the attribute name.
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
