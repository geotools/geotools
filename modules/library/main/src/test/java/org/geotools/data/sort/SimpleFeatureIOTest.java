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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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
}
