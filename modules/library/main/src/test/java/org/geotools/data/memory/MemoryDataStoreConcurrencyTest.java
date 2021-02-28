package org.geotools.data.memory;

import static org.junit.Assert.assertNotNull;

import org.geotools.data.DataTestCase;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Concurrency test from GEOT-2515.
 *
 * @author Frank Gasdorf
 */
public class MemoryDataStoreConcurrencyTest extends DataTestCase {

    @Test
    public void testConcurrencyReadsAndWrite() throws Exception {
        final MemoryDataStore dataStore = new MemoryDataStore();
        dataStore.createSchema(roadType);

        // start thread to write each second a feature
        Runnable writeSomeFeatures =
                () -> {
                    for (int i = 0; i < 10000; i++) {
                        if (Thread.interrupted()) break;
                        SimpleFeature feature =
                                SimpleFeatureBuilder.build(
                                        roadType,
                                        new Object[] {
                                            Integer.valueOf(i),
                                            line(new int[] {10, 10, 20, 10}),
                                            "r" + i
                                        },
                                        "road.rd" + i);
                        dataStore.addFeature(feature);
                    }
                };
        Thread writeThread = new Thread(writeSomeFeatures);
        writeThread.start();
        try {
            // start some reads from FeatureSource
            for (int i = 0; i < 10; i++) {
                // just run through the features
                assertNotNull(dataStore);
                Query query = new Query(roadType.getTypeName());
                try (FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                        dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                    assertNotNull(featureReader);
                    while (featureReader.hasNext()) {
                        featureReader.next();
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            writeThread.interrupt();
            writeThread.join();
        }
    }
}
