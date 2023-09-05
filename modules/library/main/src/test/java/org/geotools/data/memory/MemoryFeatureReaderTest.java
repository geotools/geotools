/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.data.DataTestCase;
import org.geotools.data.DefaultTransaction;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Test;

public class MemoryFeatureReaderTest extends DataTestCase {

    private MemoryDataStore memoryDataStore;

    private final Transaction transaction = new DefaultTransaction();

    @Override
    public void init() throws Exception {
        super.init();
        memoryDataStore = new MemoryDataStore(roadFeatures);
    }

    @Test
    public void testReaderIsNotBrokenWhileWritingFeatureDirectly() throws IOException {
        // a write should not "destroy" readers
        int expectedFeatureCount = roadFeatures.length;
        int currentFeatureCount = 0;

        try (FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                memoryDataStore.getFeatureReader(
                        new Query(roadType.getTypeName(), Filter.INCLUDE), transaction)) {

            // start iterating through content
            if (featureReader.hasNext()) {
                featureReader.next();
                currentFeatureCount++;
            }
            SimpleFeature newFeature = SimpleFeatureBuilder.template(roadType, null);

            memoryDataStore.addFeature(newFeature);

            assertReaderHasFeatureCount(expectedFeatureCount, currentFeatureCount, featureReader);
        }
    }

    @Test
    public void testReaderIsNotBrokenWhileWritingWithWriterAndTransaction() throws IOException {
        // a write should not "destroy" readers
        int expectedFeatureCount = roadFeatures.length;
        int currentFeatureCount = 0;

        try (FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                memoryDataStore.getFeatureReader(
                        new Query(roadType.getTypeName(), Filter.INCLUDE), transaction)) {

            // start iterating through content
            if (featureReader.hasNext()) {
                featureReader.next();
                currentFeatureCount++;
            }

            try (FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter =
                    memoryDataStore.getFeatureWriter(roadType.getTypeName(), transaction)) {

                while (featureWriter.hasNext()) {
                    featureWriter.next();
                }

                SimpleFeature newFeature = featureWriter.next();
                assertNotNull(newFeature);

                transaction.commit();

                assertReaderHasFeatureCount(
                        expectedFeatureCount, currentFeatureCount, featureReader);
            }
        }
    }

    public void shutDown() throws IOException {
        transaction.close();
    }

    private void assertReaderHasFeatureCount(
            int expectedFeatureCount,
            int currentFeatureCount,
            FeatureReader<SimpleFeatureType, SimpleFeature> featureReader)
            throws IOException {
        while (featureReader.hasNext()) {
            featureReader.next();
            currentFeatureCount++;
        }

        assertEquals(
                "a write in MemoryDataStore should not 'destroy' readers",
                expectedFeatureCount,
                currentFeatureCount);
    }
}
