package org.geotools.data.flatgeobuf;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.geojson.GeoJSONWriter;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.json.JSONObject;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

public class AttributeRoundtripTest {

    String roundTrip(String geojson) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SimpleFeatureCollection fc = GeoJSONReader.parseFeatureCollection(geojson);
        FeatureCollectionConversions.serialize(fc, 1, os);
        byte[] bytes = os.toByteArray();
        fc = FeatureCollectionConversions.deserializeSFC(new ByteArrayInputStream(bytes));
        String result = GeoJSONWriter.toGeoJSON(fc);
        return result;
    }

    String getResource(String name)
            throws URISyntaxException, UnsupportedEncodingException, IOException {
        URL url = TestData.url(FlatGeobufDataStore.class, name);
        File file = URLs.urlToFile(url);
        String resource =
                new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8.name());
        return GeoJSONWriter.toGeoJSON(GeoJSONReader.parseFeatureCollection(resource));
    }

    String removeId(String json) {
        JSONObject jsonObject = new JSONObject(json);
        for (Object feature : jsonObject.getJSONArray("features"))
            ((JSONObject) feature).remove("id");
        return jsonObject.toString(1);
    }

    @Test
    public void mixed1() throws IOException, URISyntaxException {
        String expected = removeId(getResource("1.json"));
        String actual = removeId(roundTrip(expected));
        assertEquals(expected, actual);
    }

    @Test
    public void mixed2() throws IOException, URISyntaxException {
        String expected = removeId(getResource("2.json"));
        String actual = removeId(roundTrip(expected));
        assertEquals(expected, actual);
    }

    @Test
    public void exotic1() throws IOException {
        SimpleFeatureTypeBuilder sftb = new SimpleFeatureTypeBuilder();
        sftb.setName("testName");
        sftb.add("geometryPropertyName", Geometry.class);
        sftb.add("exotic1_1", Integer.class);
        sftb.add("exotic1_2", Long.class);
        sftb.add("exotic1_3", LocalDateTime.class);
        sftb.add("exotic1_4", LocalDate.class);
        sftb.add("exotic1_5", LocalTime.class);
        sftb.add("exotic1_6", Double.class);
        sftb.add("exotic1_7", Byte.class);
        sftb.add("exotic1_8", Short.class);
        sftb.add("exotic1_9", Date.class);
        SimpleFeatureType ft = sftb.buildFeatureType();
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(ft);
        sfb.set("exotic1_1", Integer.valueOf("99"));
        sfb.set("exotic1_2", Long.valueOf("1111111111111111111"));
        sfb.set("exotic1_3", LocalDateTime.now().withNano(0));
        sfb.set("exotic1_4", LocalDate.now());
        sfb.set("exotic1_5", LocalTime.now().withNano(0));
        sfb.set("exotic1_6", Double.valueOf("1.1111"));
        sfb.set("exotic1_7", Byte.valueOf("99"));
        sfb.set("exotic1_8", Short.valueOf("9999"));
        sfb.set("exotic1_9", new Date());
        SimpleFeature sf = sfb.buildFeature("0");
        MemoryFeatureCollection expected = new MemoryFeatureCollection(ft);
        expected.add(sf);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FeatureCollectionConversions.serialize(expected, 0, os);
        byte[] bytes = os.toByteArray();
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        SimpleFeatureCollection actual =
                FeatureCollectionConversions.deserializeSFC(new ByteArrayInputStream(bytes));
        SimpleFeature expectedFeature = (SimpleFeature) expected.toArray()[0];
        SimpleFeature actualFeature = (SimpleFeature) actual.toArray()[0];
        assertEquals(expectedFeature.getAttribute(1), actualFeature.getAttribute(1));
        assertEquals(expectedFeature.getAttribute(2), actualFeature.getAttribute(2));
        assertEquals(
                ISO_LOCAL_DATE_TIME.format((LocalDateTime) expectedFeature.getAttribute(3)),
                actualFeature.getAttribute(3));
        assertEquals(
                ISO_LOCAL_DATE.format((LocalDate) expectedFeature.getAttribute(4)),
                actualFeature.getAttribute(4));
        assertEquals(
                ISO_LOCAL_TIME.format((LocalTime) expectedFeature.getAttribute(5)),
                actualFeature.getAttribute(5));
        assertEquals(expectedFeature.getAttribute(6), actualFeature.getAttribute(6));
        assertEquals(expectedFeature.getAttribute(7), actualFeature.getAttribute(7));
        assertEquals(expectedFeature.getAttribute(8), actualFeature.getAttribute(8));
        assertEquals(
                ISO_INSTANT.format(((java.util.Date) expectedFeature.getAttribute(9)).toInstant()),
                actualFeature.getAttribute(9));
    }
}
