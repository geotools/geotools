/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mil.nga.giat.data.elasticsearch.ElasticParserUtil;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    
    private Map<String,Object> properties;
    
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
        final double lat = rand.nextDouble()*90-90;
        final double lon = rand.nextDouble()*180-180;
        final String value = lat + "," + lon;
        final Geometry geom = parserUtil.createGeometry(value);
        assertTrue(geom.equals(geometryFactory.createPoint(new Coordinate(lon,lat))));
    }
    
    @Test
    public void testGeoPointPatternForFractions() {
        final double lat = rand.nextDouble()*2-1;
        final double lon = rand.nextDouble()*2-1;
        final String value = (lat + "," + lon).replace("0.", ".");
        final Geometry geom = parserUtil.createGeometry(value);
        assertTrue(geom.equals(geometryFactory.createPoint(new Coordinate(lon,lat))));
    }
    
    @Test
    public void testGeoPointPatternForWholeValues() {
        final Geometry geom = parserUtil.createGeometry("45,90");
        assertTrue(geom.equals(geometryFactory.createPoint(new Coordinate(90,45))));
    }
    
    @Test
    public void testGeoPointAsProperties() {
        final double lat = rand.nextDouble()*90-90;
        final double lon = rand.nextDouble()*180-180;
        properties.put("lat", lat);
        properties.put("lon", lon);
        final Geometry geometry = parserUtil.createGeometry(properties);
        assertTrue(geometry.equals(geometryFactory.createPoint(new Coordinate(lon,lat))));
    }
    
    @Test
    public void testGeoPointAsArray() {
        final double lat = rand.nextDouble()*90-90;
        final double lon = rand.nextDouble()*180-180;
        final Geometry geometry = parserUtil.createGeometry(Arrays.asList(new Double[] {lon,lat}));
        assertTrue(geometry.equals(geometryFactory.createPoint(new Coordinate(lon,lat))));
    }
    
    @Test
    public void testGeoShapePoint() throws JsonParseException, JsonMappingException, IOException {
        Point geom = rgb.createRandomPoint();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }
    
    @Test
    public void testGeoShapeLineString() throws JsonParseException, JsonMappingException, IOException {
        LineString geom = rgb.createRandomLineString();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }
    
    @Test
    public void testGeoShapePolygon() throws JsonParseException, JsonMappingException, IOException {
        Polygon geom = rgb.createRandomPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }
    
    @Test
    public void testGeoShapeMultiPoint() throws JsonParseException, JsonMappingException, IOException {
        MultiPoint geom = rgb.createRandomMultiPoint();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }
    
    @Test
    public void testGeoShapeMultiLineString() throws JsonParseException, JsonMappingException, IOException {
        MultiLineString geom = rgb.createRandomMultiLineString();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }
    
    @Test
    public void testGeoShapeMultiPolygon() throws JsonParseException, JsonMappingException, IOException {
        MultiPolygon geom = rgb.createRandomMultiPolygon();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }
    
    @Test
    public void testGeoShapeGeometryCollection() throws JsonParseException, JsonMappingException, IOException {
        rgb.setNumGeometries(5);
        GeometryCollection geom = rgb.createRandomGeometryCollection();
        assertTrue(parserUtil.createGeometry(rgb.toMap(geom)).equalsExact(geom, 1e-9));
    }
    
    @Test
    public void testGeoShapeEnvelope() throws JsonParseException, JsonMappingException, IOException {
        Envelope envelope = rgb.createRandomEnvelope();
        Geometry expected = geometryFactory.toGeometry(envelope);
        assertTrue(parserUtil.createGeometry(rgb.toMap(envelope)).equalsExact(expected, 1e-9));
    }
    
    @Test
    public void testReadStringField() {
        properties.put("attr", "value");
        List<Object> values = parserUtil.readField(properties, "attr");
        assertTrue(values.size()==1);
        assertTrue(values.get(0).equals("value"));
    }
    
    @Test
    public void testReadNumericField() {
        properties.put("attr", 2.3);
        List<Object> values = parserUtil.readField(properties, "attr");
        assertTrue(values.size()==1);
        assertTrue(values.get(0).equals(2.3));
    }
    
    @Test
    public void testReadStringFieldWithConfuser() {
        properties.put("parent1", new LinkedHashMap<String,Object>());
        ((Map) properties.get("parent1")).put("attr", "value2");
        properties.put("attr", "value");
        properties.put("parent2", new LinkedHashMap<String,Object>());
        ((Map) properties.get("parent2")).put("attr", "value3");
        List<Object> values = parserUtil.readField(properties, "attr");
        assertTrue(values.size()==1);
        assertTrue(values.get(0).equals("value"));
    }
    
    @Test
    public void testReadInnerString() {
        properties.put("parent", new LinkedHashMap<String,Object>());
        ((Map) properties.get("parent")).put("attr", "value");
        List<Object> values = parserUtil.readField(properties, "parent.attr");
        assertTrue(values.size()==1);
        assertTrue(values.get(0).equals("value"));
    }
    
    @Test
    public void testReadInnerStringArray() {
        properties.put("parent", new LinkedHashMap<String,Object>());
        ((Map) properties.get("parent")).put("attr", Arrays.asList(new String[] {"value1", "value2"}));
        List<Object> values = parserUtil.readField(properties, "parent.attr");
        assertTrue(values.size()==2);
        assertTrue(values.get(0).equals("value1"));
        assertTrue(values.get(1).equals("value2"));
    }
    
    @Test
    public void testReadStringFromObjectArray() {
        properties.put("parent", new ArrayList<Map<String,Object>>());
        ((List)properties.get("parent")).add(new LinkedHashMap<String,Object>());
        ((Map) ((List)properties.get("parent")).get(0)).put("attr", "value1");
        ((List)properties.get("parent")).add(new LinkedHashMap<String,Object>());
        ((Map) ((List)properties.get("parent")).get(1)).put("attr", "value2");
        List<Object> values = parserUtil.readField(properties, "parent.attr");
        assertTrue(values.size()==2);
        assertTrue(values.get(0).equals("value1"));
        assertTrue(values.get(1).equals("value2"));
    }
    
    @Test
    public void testReadStringFromObjectArrayOnceRemoved() {
        properties.put("parent", new ArrayList<Map<String,Object>>());
        ((List)properties.get("parent")).add(new LinkedHashMap<String,Object>());
        ((Map) ((List)properties.get("parent")).get(0)).put("child", new LinkedHashMap<String,Object>());
        ((Map)((Map) ((List)properties.get("parent")).get(0)).get("child")).put("attr", "value1");
        ((List)properties.get("parent")).add(new LinkedHashMap<String,Object>());
        ((Map) ((List)properties.get("parent")).get(1)).put("child", new LinkedHashMap<String,Object>());
        ((Map)((Map) ((List)properties.get("parent")).get(1)).get("child")).put("attr", "value2");
        List<Object> values = parserUtil.readField(properties, "parent.child.attr");
        assertTrue(values.size()==2);
        assertTrue(values.get(0).equals("value1"));
        assertTrue(values.get(1).equals("value2"));
    }
    
    @Test
    public void testReadMapField() {
        final Map<String,Object> map = new LinkedHashMap<String,Object>();
        properties.put("attr", map);
        map.put("attr2", "value2");
        map.put("attr3", "value3");
        List<Object> values = parserUtil.readField(properties, "attr");
        assertTrue(values.size()==1);
        assertTrue(values.get(0).equals(map));
    }
}
