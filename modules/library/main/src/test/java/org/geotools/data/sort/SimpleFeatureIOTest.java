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
package org.geotools.data.sort;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

/** Testing class for {@link SimpleFeatureIO} util type */
public class SimpleFeatureIOTest {

    private static final String NAME_FIELD = "name";
    private static final String BASE_STRING = "Testing Simple Feature IO big string";
    private static final int MULTIPLIER = 3333;

    @Before
    @After
    public void resetProperty() {
        System.clearProperty(SimpleFeatureIO.ENABLE_DESERIALIZATION);
    }

    /** Test for big string (bytes > 65535) encoding/decoding */
    @Test
    public void testBigString() throws Exception {
        String bigString = bigString();
        checkStringNameEncodeDecode(bigString);
    }

    /** Tests a normal small String attribute */
    @Test
    public void testSmallString() throws Exception {
        String smallString = "Hello geotools.";
        checkStringNameEncodeDecode(smallString);
    }

    private void checkStringNameEncodeDecode(String name)
            throws IOException, NoSuchAuthorityCodeException, FactoryException, FileNotFoundException {
        File tempFile = File.createTempFile("temp", "simpleFeatureIO");
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("type1");
        typeBuilder.add(NAME_FIELD, String.class);
        typeBuilder.add("location", Point.class, CRS.decode("EPSG:4326"));
        SimpleFeatureType type = typeBuilder.buildFeatureType();
        if (name.getBytes(StandardCharsets.UTF_8).length >= SimpleFeatureIO.MAX_BYTES_LENGTH)
            type.getDescriptor(NAME_FIELD).getUserData().put(SimpleFeatureIO.BIG_STRING, Boolean.TRUE);
        // create a feature
        SimpleFeatureBuilder fbuilder = new SimpleFeatureBuilder(type);
        fbuilder.set(NAME_FIELD, name);
        GeometryBuilder gb = new GeometryBuilder();
        fbuilder.set("location", gb.point(10, 10));
        SimpleFeature feature = fbuilder.buildFeature("1");
        // io util
        SimpleFeatureIO sfio = new SimpleFeatureIO(tempFile, type);
        sfio.write(feature);
        sfio.close(false);
        // check reading
        sfio = new SimpleFeatureIO(tempFile, type);
        SimpleFeature readFeature = sfio.read();
        assertEquals(name.length(), ((String) readFeature.getAttribute(NAME_FIELD)).length());
        assertEquals(name, readFeature.getAttribute(NAME_FIELD));
        sfio.close(true);
    }

    private String bigString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MULTIPLIER; i++) {
            sb.append(BASE_STRING);
        }
        return sb.toString();
    }

    @Test
    public void testDeserializationDefault() throws Exception {
        // deserialization will only be allowed when SimpleFeatureIO is created with an empty file
        doTestDeserialization(false, true);
    }

    @Test
    public void testDeserializationEnabled() throws Exception {
        System.setProperty(SimpleFeatureIO.ENABLE_DESERIALIZATION, "true");
        doTestDeserialization(false, false);
    }

    @Test
    public void testDeserializationDisabled() throws Exception {
        System.setProperty(SimpleFeatureIO.ENABLE_DESERIALIZATION, "false");
        doTestDeserialization(true, true);
    }

    private static void doTestDeserialization(boolean exception1, boolean exception2) throws Exception {
        URI uri = URI.create("http://localhost/");
        File tempFile = File.createTempFile("temp", "simpleFeatureIODeserialization");
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("type2");
        typeBuilder.add("uri", URI.class);
        SimpleFeatureType type = typeBuilder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        featureBuilder.set("uri", uri);
        // created with an empty file
        SimpleFeatureIO sfio1 = new SimpleFeatureIO(tempFile, type);
        try {
            sfio1.write(featureBuilder.buildFeature("1"));
            sfio1.seek(0);
            if (exception1) {
                assertThrows(IllegalStateException.class, () -> sfio1.read());
            } else {
                assertEquals(uri, sfio1.read().getAttribute("uri"));
            }
        } finally {
            sfio1.close(false);
        }
        // created with an non-empty file
        SimpleFeatureIO sfio2 = new SimpleFeatureIO(tempFile, type);
        try {
            if (exception2) {
                assertThrows(IllegalStateException.class, () -> sfio2.read());
            } else {
                assertEquals(uri, sfio2.read().getAttribute("uri"));
            }
        } finally {
            sfio2.close(true);
        }
    }

    /**
     * Round trips features holding a value of every supported type, with a geometry above the size the feature buffers
     * keep, then all values null, then a small geometry.
     */
    @Test
    public void testRoundTripAllTypes() throws Exception {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("allTypes");
        typeBuilder.add("boolean", Boolean.class);
        typeBuilder.add("byte", Byte.class);
        typeBuilder.add("short", Short.class);
        typeBuilder.add("int", Integer.class);
        typeBuilder.add("long", Long.class);
        typeBuilder.add("float", Float.class);
        typeBuilder.add("double", Double.class);
        typeBuilder.add("string", String.class);
        typeBuilder.add(NAME_FIELD, String.class);
        typeBuilder.add("sqlDate", java.sql.Date.class);
        typeBuilder.add("time", Time.class);
        typeBuilder.add("timestamp", Timestamp.class);
        typeBuilder.add("date", Date.class);
        typeBuilder.add("uri", URI.class);
        typeBuilder.add("geom", Geometry.class);
        SimpleFeatureType type = typeBuilder.buildFeatureType();
        type.getDescriptor(NAME_FIELD).getUserData().put(SimpleFeatureIO.BIG_STRING, Boolean.TRUE);
        Object[] values = {
            true,
            (byte) 1,
            (short) 2,
            3,
            4L,
            5.5f,
            6.5d,
            "small",
            bigString(),
            new java.sql.Date(1000),
            new Time(2000),
            new Timestamp(3000),
            new Date(4000),
            URI.create("http://localhost/"),
            // a circle of 100000 points has a WKB of about 1.6MB
            new GeometryBuilder().circle(0, 0, 10, 100000)
        };
        Object[] smallGeometry = values.clone();
        smallGeometry[values.length - 1] = new GeometryBuilder().point(1, 2);
        List<SimpleFeature> features = List.of(
                SimpleFeatureBuilder.build(type, values, "f0"),
                SimpleFeatureBuilder.build(type, new Object[values.length], "f1"),
                SimpleFeatureBuilder.build(type, smallGeometry, "f2"));

        File file = File.createTempFile("allTypes", ".features");
        SimpleFeatureIO io = new SimpleFeatureIO(file, type);
        try {
            for (SimpleFeature feature : features) {
                io.write(feature);
            }
            io.seek(0);
            for (SimpleFeature feature : features) {
                SimpleFeature read = io.read();
                assertEquals(feature.getID(), read.getID());
                assertEquals(feature.getAttributes(), read.getAttributes());
            }
            assertTrue(io.endOfFile());
        } finally {
            io.close(true);
        }
    }
}
