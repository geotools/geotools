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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.logging.Logging;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class ArcGISRestFeatureReaderTest {

    private static final Logger LOGGER = Logging.getLogger(ArcGISRestFeatureReaderTest.class);

    ArcGISRestFeatureReader reader;
    SimpleFeatureType fType;
    String json;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("jsonfeature");
        builder.add("vint", Integer.class);
        builder.add("vfloat", Float.class);
        builder.add("vstring", String.class);
        builder.add("vboolean", Boolean.class);
        builder.add("geometry", Geometry.class);

        this.fType = builder.buildFeatureType();
    }

    @Test(expected = IOException.class)
    public void emptyInputStreamHasNext() throws Exception {

        this.reader =
                new ArcGISRestFeatureReader(
                        this.fType, new ByteArrayInputStream("".getBytes()), this.LOGGER);
        assertFalse(this.reader.hasNext());
    }

    @Test
    public void noFeaturesHasNext() throws Exception {

        this.json =
                ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/noFeatures.geo.json");
        this.reader =
                new ArcGISRestFeatureReader(
                        this.fType, new ByteArrayInputStream(json.getBytes()), this.LOGGER);

        assertFalse(this.reader.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void noFeaturesNext() throws Exception {

        this.json =
                ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/noFeatures.geo.json");
        this.reader =
                new ArcGISRestFeatureReader(
                        this.fType, new ByteArrayInputStream(json.getBytes()), this.LOGGER);

        this.reader.next();
    }

    @Test
    public void noProperties() throws Exception {

        this.json =
                ArcGISRestDataStoreFactoryTest.readJSONAsString("test-data/noProperties.geo.json");
        this.reader =
                new ArcGISRestFeatureReader(
                        this.fType, new ByteArrayInputStream(json.getBytes()), this.LOGGER);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat = this.reader.next();
        assertTrue(this.reader.hasNext());
        feat = this.reader.next();
        assertTrue(this.reader.hasNext());
        feat = this.reader.next();
        assertFalse(this.reader.hasNext());
        assertEquals("geometry", feat.getDefaultGeometryProperty().getName().getLocalPart());
    }
}
