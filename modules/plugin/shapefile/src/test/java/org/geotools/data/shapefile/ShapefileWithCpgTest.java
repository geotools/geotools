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
package org.geotools.data.shapefile;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureIterator;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class ShapefileWithCpgTest extends TestCaseSupport {

    private String savedEnableCPGSwitch;

    @Before
    public void setUp() throws Exception {
        copyShapefiles(RUSSIAN);
        enableCPGSwitch();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        restoreCPGSwitch();
        super.tearDown();
    }

    @Test
    public void testShapefileDataStoreFeatureIterator() throws Exception {
        URL url = TestData.url(this, RUSSIAN);
        ShapefileDataStore dataStore = new ShapefileDataStore(url);
        dataStore.setTryCPGFile(true);
        try {
            String typeName = dataStore.getTypeNames()[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> source =
                    dataStore.getFeatureSource(typeName);

            try (FeatureIterator<SimpleFeature> features =
                    source.getFeatures(Filter.INCLUDE).features()) {

                assertTrue(features.hasNext());
                SimpleFeature feature = features.next();
                assertThat(feature.getAttribute("TEXT"), is(equalTo("Кириллица"))); // "Cyrillic"
            }
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testShapefileDataStoreFeatureIteratorDefaultCharset() throws Exception {
        URL url = TestData.url(this, RUSSIAN);
        ShapefileDataStore dataStore = new ShapefileDataStore(url);
        try {
            String typeName = dataStore.getTypeNames()[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> source =
                    dataStore.getFeatureSource(typeName);

            try (FeatureIterator<SimpleFeature> features =
                    source.getFeatures(Filter.INCLUDE).features()) {

                assertTrue(features.hasNext());
                SimpleFeature feature = features.next();
                assertThat(feature.getAttribute("TEXT"), is(not(equalTo("Кириллица"))));
            }
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testShapefileDataStoreFeatureReader() throws Exception {
        URL url = TestData.url(this, RUSSIAN);
        ShapefileDataStore dataStore = new ShapefileDataStore(url);
        dataStore.setTryCPGFile(true);
        try {
            Query q = new Query(dataStore.getTypeNames()[0]);
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT)) {

                assertTrue(reader.hasNext());
                SimpleFeature feature = reader.next();
                assertThat(feature.getAttribute("TEXT"), is(equalTo("Кириллица")));
            }
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testShapefileDataStoreFeatureWriter() throws Exception {
        URL url = TestData.url(this, RUSSIAN);
        ShapefileDataStore dataStore = new ShapefileDataStore(url);
        dataStore.setTryCPGFile(true);
        int counter = 0;
        try {
            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    dataStore.getFeatureWriter(Transaction.AUTO_COMMIT)) {

                SimpleFeature feature = null;

                while (writer.hasNext()) {
                    feature = writer.next();
                    feature.setAttribute("TEXT", "Изменено"); // "Changed"
                    writer.write();
                    counter++;
                }

                assertThat(feature, is(notNullValue()));

                SimpleFeature newFeature = writer.next();
                newFeature.setAttributes(feature.getAttributes());
                newFeature.setAttribute("TEXT", "Новый"); // "New"
                writer.write();
                counter++;
            }

            Query q = new Query(dataStore.getTypeNames()[0]);
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT)) {

                SimpleFeature feature;
                for (int i = 0; i < counter - 1; i++) {
                    assertTrue(reader.hasNext());
                    feature = reader.next();
                    assertThat(feature.getAttribute("TEXT"), is(equalTo("Изменено")));
                }

                assertTrue(reader.hasNext());
                feature = reader.next();
                assertThat(feature.getAttribute("TEXT"), is(equalTo("Новый")));
            }
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testDirectoryDataStoreFeatureIterator() throws Exception {
        File file = copyShapefiles(DANISH);

        Map<String, Object> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, file.getParentFile().toURI().toURL());
        ShapefileDirectoryFactory factory = new ShapefileDirectoryFactory();

        DataStore dataStore = factory.createDataStore(params);

        try {
            assertThat(
                    Arrays.asList(dataStore.getTypeNames()),
                    hasItems("rus-windows-1251", "danish_point"));

            for (String typeName : dataStore.getTypeNames()) {
                FeatureSource<SimpleFeatureType, SimpleFeature> source =
                        dataStore.getFeatureSource(typeName);

                try (FeatureIterator<SimpleFeature> features =
                        source.getFeatures(Filter.INCLUDE).features()) {
                    SimpleFeature feature = features.next();
                    if (typeName.equals("rus-windows-1251")) {
                        assertThat(feature.getAttribute("TEXT"), is(equalTo("Кириллица")));
                    } else {
                        assertThat(feature.getAttribute("TEKST1"), is(equalTo("Charl\u00F8tte")));
                    }
                }
            }
        } finally {
            dataStore.dispose();
        }
    }

    @Test
    public void testDirectoryDataStoreFeatureIteratorForcedCharset() throws Exception {
        File file = copyShapefiles(DANISH);

        Map<String, Object> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, file.getParentFile().toURI().toURL());
        params.put(ShapefileDataStoreFactory.DBFCHARSET.key, StandardCharsets.ISO_8859_1);
        ShapefileDirectoryFactory factory = new ShapefileDirectoryFactory();

        DataStore dataStore = factory.createDataStore(params);

        try {
            assertThat(
                    Arrays.asList(dataStore.getTypeNames()),
                    hasItems("rus-windows-1251", "danish_point"));

            for (String typeName : dataStore.getTypeNames()) {
                FeatureSource<SimpleFeatureType, SimpleFeature> source =
                        dataStore.getFeatureSource(typeName);

                try (FeatureIterator<SimpleFeature> features =
                        source.getFeatures(Filter.INCLUDE).features()) {
                    SimpleFeature feature = features.next();
                    if (typeName.equals("rus-windows-1251")) {
                        assertThat(feature.getAttribute("TEXT"), is(not(equalTo("Кириллица"))));
                    } else {
                        assertThat(feature.getAttribute("TEKST1"), is(equalTo("Charl\u00F8tte")));
                    }
                }
            }
        } finally {
            dataStore.dispose();
        }
    }

    private void enableCPGSwitch() {
        savedEnableCPGSwitch = System.getProperty(ShapefileDataStoreFactory.ENABLE_CPG_SWITCH);
        System.setProperty(ShapefileDataStoreFactory.ENABLE_CPG_SWITCH, "true");
    }

    private void restoreCPGSwitch() {
        if (savedEnableCPGSwitch != null) {
            System.setProperty(ShapefileDataStoreFactory.ENABLE_CPG_SWITCH, savedEnableCPGSwitch);
        } else {
            System.clearProperty(ShapefileDataStoreFactory.ENABLE_CPG_SWITCH);
        }
    }
}
