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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/** Testing class for {@link SimpleFeatureIO} util type */
public class SimpleFeatureIOTest {

    private static final String NAME_FIELD = "name";
    private static final String BASE_STRING = "Testing Simple Feature IO big string";
    private static final int MULTIPLIER = 3333;

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
            throws IOException, NoSuchAuthorityCodeException, FactoryException,
                    FileNotFoundException {
        File tempFile = File.createTempFile("temp", "simpleFeatureIO");
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("type1");
        typeBuilder.add(NAME_FIELD, String.class);
        typeBuilder.add("location", Point.class, CRS.decode("EPSG:4326"));
        SimpleFeatureType type = typeBuilder.buildFeatureType();
        if (name.getBytes().length >= SimpleFeatureIO.MAX_BYTES_LENGTH)
            type.getDescriptor(NAME_FIELD)
                    .getUserData()
                    .put(SimpleFeatureIO.BIG_STRING, Boolean.TRUE);
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
        assertEquals(name, (String) readFeature.getAttribute(NAME_FIELD));
        sfio.close(true);
    }

    private String bigString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MULTIPLIER; i++) {
            sb.append(BASE_STRING);
        }
        return sb.toString();
    }
}
