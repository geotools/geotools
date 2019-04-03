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
package org.geotools.data.epavic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.geotools.data.FeatureSource;
import org.geotools.data.epavic.schema.MeasurementFields;
import org.geotools.data.epavic.schema.Monitors;
import org.geotools.data.epavic.schema.Sites;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class EpaVicFeatureReaderTest {

    EpaVicDatastore dataStore;

    EpaVicFeatureReader reader;

    SimpleFeatureType fType;

    String json;

    ObjectMapper om = new ObjectMapper();

    Sites sites;

    Monitors monitors;

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

        sites =
                om.readValue(
                        EpaVicDataStoreFactoryTest.readJSONAsString("test-data/sites.json")
                                .getBytes(),
                        Sites.class);
        monitors =
                om.readValue(
                        EpaVicDataStoreFactoryTest.readJSONAsString("test-data/monitors.json")
                                .getBytes(),
                        Monitors.class);
    }

    @Test(expected = IOException.class)
    public void emptyInputStreamHasNext() throws Exception {

        this.reader =
                new EpaVicFeatureReader(
                        this.fType, new ByteArrayInputStream("".getBytes()), null, null);
        assertFalse(this.reader.hasNext());
    }

    @Test
    public void noFeaturesHasNext() throws Exception {

        this.json = EpaVicDataStoreFactoryTest.readJSONAsString("test-data/noFeatures.json");
        this.reader =
                new EpaVicFeatureReader(
                        this.fType, new ByteArrayInputStream(json.getBytes()), null, null);

        assertFalse(this.reader.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void noFeaturesNext() throws Exception {

        this.json = EpaVicDataStoreFactoryTest.readJSONAsString("test-data/noFeatures.json");
        this.reader =
                new EpaVicFeatureReader(
                        this.fType, new ByteArrayInputStream(json.getBytes()), null, null);

        this.reader.next();
    }

    @Test
    public void readFirstFeature() throws Exception {

        this.dataStore =
                (EpaVicDatastore) EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        List<Name> names = this.dataStore.createTypeNames();

        ByteArrayInputStream nine =
                new ByteArrayInputStream(
                        EpaVicDataStoreFactoryTest.readJSONAsString("test-data/9measurements.json")
                                .getBytes());

        this.reader =
                new EpaVicFeatureReader(
                        EpaVicFeatureSource.buildType(this.dataStore.getNamespace()),
                        nine,
                        sites,
                        monitors);

        if (reader.hasNext()) {
            SimpleFeature f = reader.next();
            for (MeasurementFields field : MeasurementFields.values()) {
                f.getAttribute(field.getFieldName());
            }
        }
    }

    @Test
    public void readFeaturesAsGeoServer() throws Exception {

        this.dataStore =
                (EpaVicDatastore) EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        List<Name> names = this.dataStore.createTypeNames();

        ByteArrayInputStream nine =
                new ByteArrayInputStream(
                        EpaVicDataStoreFactoryTest.readJSONAsString("test-data/9measurements.json")
                                .getBytes());
        FeatureSource<SimpleFeatureType, SimpleFeature> src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(
                                        EpaVicDataStoreFactoryTest.NAMESPACE,
                                        EpaVicDataStoreTest.TYPENAME1)));
        this.reader = new EpaVicFeatureReader(src.getSchema(), nine, this.sites, this.monitors);

        DefaultFeatureCollection results = new DefaultFeatureCollection();
        while (this.reader.hasNext()) {
            results.add(this.reader.next());
        }

        SimpleFeatureType featureType = (SimpleFeatureType) results.getSchema();
        assertEquals("measurement", featureType.getName().getLocalPart());
        assertEquals(EpaVicDataStoreFactoryTest.NAMESPACE, featureType.getName().getNamespaceURI());
    }

    @Test
    public void oneStream() throws Exception {

        this.dataStore =
                (EpaVicDatastore) EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        List<Name> names = this.dataStore.createTypeNames();

        ByteArrayInputStream nine =
                new ByteArrayInputStream(
                        EpaVicDataStoreFactoryTest.readJSONAsString("test-data/9measurements.json")
                                .getBytes());

        this.reader =
                new EpaVicFeatureReader(
                        EpaVicFeatureSource.buildType(this.dataStore.getNamespace()),
                        nine,
                        sites,
                        monitors);

        int c = 0;
        while (reader.hasNext()) {
            c++;
            this.reader.next();
        }

        assertEquals(9, c);
    }

    @Test
    public void multipleStreams() throws Exception {

        this.dataStore =
                (EpaVicDatastore) EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        List<Name> names = this.dataStore.createTypeNames();

        Queue<InputStream> l = new LinkedList<>();
        l.add(
                new ByteArrayInputStream(
                        EpaVicDataStoreFactoryTest.readJSONAsString("test-data/9measurements.json")
                                .getBytes()));
        l.add(
                new ByteArrayInputStream(
                        EpaVicDataStoreFactoryTest.readJSONAsString("test-data/17measurements.json")
                                .getBytes()));

        this.reader =
                new EpaVicFeatureReader(
                        EpaVicFeatureSource.buildType(this.dataStore.getNamespace()),
                        l,
                        sites,
                        monitors);

        int c = 0;
        while (reader.hasNext()) {
            c++;
            this.reader.next();
        }

        assertEquals(26, c);
    }
}
