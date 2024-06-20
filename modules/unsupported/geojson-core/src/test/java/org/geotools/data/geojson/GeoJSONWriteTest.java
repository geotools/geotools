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
package org.geotools.data.geojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.geotools.TestData;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.URLs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Informal test used to document expected functionality for workshop.
 *
 * <p>This test has a setup method used to copy locations.csv to a temporary file.
 */
@RunWith(Parameterized.class)
public class GeoJSONWriteTest {
    private Locale previousLocale;

    File tmp;

    File file;

    URL url;

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> locales() {
        return Arrays.asList(new Object[][] {{Locale.ENGLISH}, {Locale.ITALIAN}});
    }

    public GeoJSONWriteTest(Locale locale) {
        this.previousLocale = Locale.getDefault();
        Locale.setDefault(locale);
    }

    @After
    public void resetLocale() {
        Locale.setDefault(previousLocale);
    }

    @Before
    public void createTemporaryLocations() throws IOException {
        // Setting the system-wide default at startup time

        tmp = File.createTempFile("example", "");
        boolean exists = tmp.exists();
        if (exists) {
            // System.err.println("Removing tempfile " + tmp);
            if (!tmp.delete()) {
                throw new IOException("could not delete: " + tmp);
            }
        }
        boolean created = tmp.mkdirs();
        if (!created) {
            // System.err.println("Could not create " + tmp);
            return;
        }

        file = new File(tmp, "locations.json");

        URL resource = TestData.getResource(GeoJSONWriteTest.class, "locations.json");
        url = resource;
        if (url == null) throw new RuntimeException("Input datafile not found");
        // System.out.println("copying " + resource.toExternalForm() + " to " +
        // file);
        Files.copy(resource.openStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        url = URLs.fileToUrl(file);
    }

    @Test
    public void testNumberDecimals() throws SchemaException, IOException {
        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureType type = DataUtilities.createType("test", "the_geom:Point:srid=4326");
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(gf.createPoint(new Coordinate(1.23456789, 0.123456789)));
        SimpleFeature feature = builder.buildFeature(null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GeoJSONWriter writer = new GeoJSONWriter(out);
        writer.setMaxDecimals(6);

        writer.write(feature);

        assertTrue("Incorrect number of decimals", out.toString().contains("[0.123457,1.234568]"));
        out.close();
        writer.close();
        out = new ByteArrayOutputStream();
        writer = new GeoJSONWriter(out);
        writer.write(feature);
        assertTrue("Incorrect number of decimals", out.toString().contains("[0.123457,1.234568]"));
        out.close();
        writer.close();
        out = new ByteArrayOutputStream();
        writer = new GeoJSONWriter(out);
        writer.setMaxDecimals(0);
        writer.write(feature);

        assertTrue("Incorrect number of decimals", out.toString().contains("[0,1]"));
    }

    @Test
    public void testBoundingBox() throws SchemaException, IOException {
        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureType type = DataUtilities.createType("test", "the_geom:Polygon:srid=27700");
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        Coordinate[] coords = {
            new Coordinate(100, 200),
            new Coordinate(140, 500),
            new Coordinate(300, 600),
            new Coordinate(100, 200)
        };
        builder.add(gf.createPolygon(coords));
        SimpleFeature feature = builder.buildFeature(null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GeoJSONWriter writer = new GeoJSONWriter(out);
        writer.setEncodeFeatureBounds(true);

        writer.write(feature);

        assertTrue(
                "missing bbox in " + out.toString(),
                out.toString().contains("\"bbox\":[-7.555983,49.768664,-7.553629,49.772378]"));
        out.close();
        writer.close();
    }

    @Test
    public void testWriteEmptyCollection() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SimpleFeatureType type = DataUtilities.createType("test", "the_geom:Polygon:srid=27700");
        try (GeoJSONWriter writer = new GeoJSONWriter(out)) {
            writer.writeFeatureCollection(new EmptyFeatureCollection(type));
        }
        String json = new String(out.toByteArray(), StandardCharsets.UTF_8);
        assertEquals("{\"type\":\"FeatureCollection\",\"features\":[]}", json);
    }

    @Test
    public void testWriteEmptyGeometery() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String featureDef = "1=POINT EMPTY";
        SimpleFeatureType schema = DataUtilities.createType("test", "p:Point:srid=27700");
        SimpleFeature feature = DataUtilities.createFeature(schema, featureDef);
        try (GeoJSONWriter writer = new GeoJSONWriter(out)) {
            writer.write(feature);
        }
        String json = new String(out.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(
                "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"type\":\"Point\",\"coordinates\":[]},\"id\":\"1\"}]}",
                json);

        out = new ByteArrayOutputStream();
        featureDef = "1=LINESTRING EMPTY";
        schema = DataUtilities.createType("test", "p:LineString:srid=27700");
        feature = DataUtilities.createFeature(schema, featureDef);
        try (GeoJSONWriter writer = new GeoJSONWriter(out)) {
            writer.write(feature);
        }
        json = new String(out.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(
                "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[]},\"id\":\"1\"}]}",
                json);
    }

    @Test
    public void testWriteMissingGeometery() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String featureDef = "1=POINT EMPTY";
        SimpleFeatureType schema = DataUtilities.createType("test", "p:String");
        SimpleFeature feature = DataUtilities.createFeature(schema, featureDef);
        try (GeoJSONWriter writer = new GeoJSONWriter(out)) {
            writer.write(feature);
        }
        String json = new String(out.toByteArray(), StandardCharsets.UTF_8);
        assertEquals(
                "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"p\":\"POINT EMPTY\"},\"geometry\":null,\"id\":\"1\"}]}",
                json);
    }

    @Test
    public void testPrettyPrint() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GeoJSONWriter writer = new GeoJSONWriter(out)) {
            writer.setPrettyPrinting(true);
            SimpleFeatureType type =
                    DataUtilities.createType("test", "the_geom:Polygon:srid=27700");
            writer.writeFeatureCollection(new EmptyFeatureCollection(type));
        }
        String json = new String(out.toByteArray(), StandardCharsets.UTF_8);
        String expected =
                "{\n" //
                        + "  \"type\" : \"FeatureCollection\",\n" //
                        + "  \"features\" : [ ]\n" //
                        + "}";
        assertEquals(normalizeLineEnds(expected), normalizeLineEnds(json));
    }

    /** Normalizes line ends to \n, allows for cross-platform string comparisons */
    private String normalizeLineEnds(String s) {
        return s.replace("\r\n", "\n").replace('\r', '\n');
    }

    @Test
    public void testSingleFeature() throws Exception {
        // test feature
        SimpleFeatureType type = DataUtilities.createType("test", "the_geom:Point:srid=4326");
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(new GeometryFactory().createPoint(new Coordinate(1.23456789, 0.123456789)));
        SimpleFeature feature = builder.buildFeature(null);

        String json = writeSingleFeature(feature);
        JsonNode root = new ObjectMapper().readTree(json);
        assertEquals("Feature", root.get("type").textValue());
    }

    @Test
    public void testWriteDate() throws Exception {
        // test feature
        SimpleFeatureType type =
                DataUtilities.createType("test", "the_geom:Point:srid=4326,date:java.util.Date");
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(new GeometryFactory().createPoint(new Coordinate(1, 2)));
        builder.add(new Date());
        SimpleFeature feature = builder.buildFeature(null);

        String json = writeSingleFeature(feature);
        JsonNode root = new ObjectMapper().readTree(json);
        String isoDatePattern =
                "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2})\\:(\\d{2})\\:(\\d{2})\\.(\\d{3})Z";
        String date = root.get("properties").get("date").textValue();
        assertTrue(date + " does not match ISO date", date.matches(isoDatePattern));
    }

    @Test
    public void testDateFormat() throws Exception {
        Date testDate = new Date();

        // test feature
        SimpleFeatureType type =
                DataUtilities.createType("test", "the_geom:Point:srid=4326,date:java.util.Date");
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(new GeometryFactory().createPoint(new Coordinate(1, 2)));
        builder.add(testDate);
        SimpleFeature feature = builder.buildFeature(null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GeoJSONWriter writer = new GeoJSONWriter(out)) {
            writer.setSingleFeature(true);
            writer.setDatePattern(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern());
            writer.write(feature);
        }

        String json = out.toString(StandardCharsets.UTF_8);
        JsonNode root = new ObjectMapper().readTree(json);
        String date = root.get("properties").get("date").textValue();

        FastDateFormat utm =
                FastDateFormat.getInstance(
                        DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern(),
                        GeoJSONWriter.DEFAULT_TIME_ZONE);
        assertEquals(utm.format(testDate), date);
    }

    @Test
    public void testWriteBooleans() throws Exception {
        // test feature
        SimpleFeatureType type =
                DataUtilities.createType(
                        "test", "the_geom:Point:srid=4326,boolTrue:Boolean,boolFalse:Boolean");
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(new GeometryFactory().createPoint(new Coordinate(1, 2)));
        builder.add(Boolean.TRUE);
        builder.add(Boolean.FALSE);
        SimpleFeature feature = builder.buildFeature(null);

        String json = writeSingleFeature(feature);
        JsonNode root = new ObjectMapper().readTree(json);
        assertTrue(root.get("properties").get("boolTrue").isBoolean());
        assertTrue(root.get("properties").get("boolTrue").booleanValue());
        assertTrue(root.get("properties").get("boolFalse").isBoolean());
        assertFalse(root.get("properties").get("boolFalse").booleanValue());
    }

    @Test
    public void testWriteArray() throws Exception {
        // test feature
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.add("the_geom", Point.class, 4326);
        tb.add("array", Integer[].class);
        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(new GeometryFactory().createPoint(new Coordinate(1, 2)));
        builder.add(new int[] {1, 2, 3});
        SimpleFeature feature = builder.buildFeature(null);

        String json = writeSingleFeature(feature);
        JsonNode root = new ObjectMapper().readTree(json);
        assertTrue(root.get("properties").get("array").isArray());
        ArrayNode array = (ArrayNode) root.get("properties").get("array");
        assertEquals(JsonNodeType.NUMBER, array.get(0).getNodeType());
        assertEquals(1, array.get(0).intValue());
        assertEquals(JsonNodeType.NUMBER, array.get(1).getNodeType());
        assertEquals(2, array.get(1).intValue());
        assertEquals(JsonNodeType.NUMBER, array.get(2).getNodeType());
        assertEquals(3, array.get(2).intValue());
    }

    @Test
    public void testWriteList() throws Exception {
        // test feature
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.add("the_geom", Point.class, 4326);
        tb.add("list", List.class);
        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(new GeometryFactory().createPoint(new Coordinate(1, 2)));
        builder.add(Arrays.asList(1, "two", null));
        SimpleFeature feature = builder.buildFeature(null);

        String json = writeSingleFeature(feature);
        JsonNode root = new ObjectMapper().readTree(json);
        assertTrue(root.get("properties").get("list").isArray());
        ArrayNode array = (ArrayNode) root.get("properties").get("list");
        assertEquals(JsonNodeType.NUMBER, array.get(0).getNodeType());
        assertEquals(1, array.get(0).intValue());
        assertEquals(JsonNodeType.STRING, array.get(1).getNodeType());
        assertEquals("two", array.get(1).textValue());
        assertEquals(JsonNodeType.NULL, array.get(2).getNodeType());
    }

    private String writeSingleFeature(SimpleFeature feature) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GeoJSONWriter writer = new GeoJSONWriter(out)) {
            writer.setPrettyPrinting(true);
            writer.setSingleFeature(true);
            writer.write(feature);
        }

        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    @Test
    public void testRoundTripNestedObject() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.add("the_geom", Point.class, 4326);
        tb.add("object", Object.class);
        SimpleFeatureType type = tb.buildFeatureType();
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"a\": 10, \"b\": \"foo\"}");
        builder.add(new GeometryFactory().createPoint(new Coordinate(1, 2)));
        builder.add(node);
        SimpleFeature feature = builder.buildFeature(null);

        // write and check
        String json = writeSingleFeature(feature);
        JsonNode root = mapper.readTree(json);
        JsonNode object = root.get("properties").get("object");
        assertEquals(JsonNodeType.NUMBER, object.get("a").getNodeType());
        assertEquals(10, object.get("a").intValue());
        assertEquals(JsonNodeType.STRING, object.get("b").getNodeType());
        assertEquals("foo", object.get("b").textValue());

        // parse back and check object is retained
        SimpleFeature parsedFeature = GeoJSONReader.parseFeature(json);
        assertEquals(feature.getAttribute("object"), parsedFeature.getAttribute("object"));
    }
}
