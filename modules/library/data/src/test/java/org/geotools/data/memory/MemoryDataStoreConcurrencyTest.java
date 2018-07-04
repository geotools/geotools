package org.geotools.data.memory;

import org.geotools.data.DataTestCase;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Concurrency test from GEOT-2515.
 *
 * @author Frank Gasdorf
 */
public class MemoryDataStoreConcurrencyTest extends DataTestCase {

    public MemoryDataStoreConcurrencyTest(String name) {
        super(name);
    }

    public void testConcurrencyReadsAndWrite() throws Exception {
        final MemoryDataStore dataStore = new MemoryDataStore();
        dataStore.createSchema(roadType);

        // start thread to write each second a feature
        Runnable writeSomeFeatures =
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10000; i++) {
                            if (Thread.interrupted()) break;
                            SimpleFeature feature =
                                    SimpleFeatureBuilder.build(
                                            roadType,
                                            new Object[] {
                                                new Integer(i),
                                                line(new int[] {10, 10, 20, 10}),
                                                "r" + i
                                            },
                                            "road.rd" + i);
                            dataStore.addFeature(feature);
                        }
                    }
                };
        Thread writeThread = new Thread(writeSomeFeatures);
        writeThread.start();
        try {
            // start some reads from FeatureSource
            for (int i = 0; i < 10; i++) {
                // just run through the features
                assertTrue(dataStore != null);
                Query query = new Query(roadType.getTypeName());
                FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                        dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT);
                assertTrue(featureReader != null);
                while (featureReader.hasNext()) {
                    featureReader.next();
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
