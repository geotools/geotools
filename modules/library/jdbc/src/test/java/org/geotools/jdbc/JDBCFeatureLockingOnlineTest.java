/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.util.Collections;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockException;
import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

public abstract class JDBCFeatureLockingOnlineTest extends JDBCTestSupport {

    JDBCFeatureStore store;

    protected void connect() throws Exception {
        super.connect();

        store = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));
        store.setFeatureLock(FeatureLock.TRANSACTION);
    }

    public void testLockFeatures() throws Exception {

        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);

        try (Transaction tx = new DefaultTransaction()) {
            store.setTransaction(tx);
            store.setFeatureLock(lock);

            // lock features
            int locked = store.lockFeatures();
            assertTrue(locked > 0);

            // grabbing a reader should be no problem
            Query query = new Query(tname("ft1"));
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                    dataStore.getFeatureReader(query, tx)) {

                int count = 0;
                while (reader.hasNext()) {
                    count++;
                    reader.next();
                }
                assertTrue(count > 0);
            }

            // grab a writer
            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    dataStore.getFeatureWriter(tname("ft1"), tx)) {
                assertTrue(writer.hasNext());
                SimpleFeature feature = writer.next();

                feature.setAttribute(aname("intProperty"), new Integer(100));
                try {
                    writer.write();
                    fail("should have thrown feature lock exception");
                } catch (FeatureLockException e) {
                    // good
                }
            }

            tx.addAuthorization(lock.getAuthorization());
            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    dataStore.getFeatureWriter(tname("ft1"), tx)) {
                assertTrue(writer.hasNext());
                SimpleFeature feature = writer.next();
                feature.setAttribute(aname("intProperty"), new Integer(100));
                writer.write();
            }
        }
    }

    public void testLockFeaturesWithFilter() throws Exception {

        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);

        try (Transaction tx = new DefaultTransaction()) {
            store.setTransaction(tx);
            store.setFeatureLock(lock);

            // lock features
            FilterFactory ff = dataStore.getFilterFactory();
            PropertyIsEqualTo f = ff.equals(ff.property(aname("intProperty")), ff.literal(1));

            int locked = store.lockFeatures(f);
            assertEquals(1, locked);

            // grabbing a reader should be no problem
            boolean failure = false;
            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    dataStore.getFeatureWriter(tname("ft1"), tx)) {
                while (writer.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) writer.next();
                    Number old = (Number) feature.getAttribute(aname("intProperty"));

                    feature.setAttribute(aname("intProperty"), new Integer(100));
                    if (new Integer(1).equals(old.intValue())) {
                        try {
                            writer.write();
                            fail("writer should have thrown exception for locked feature");
                        } catch (FeatureLockException e) {
                            failure = true;
                        }
                    } else {
                        writer.write();
                    }
                }
            }

            assertTrue(failure);
        }
    }

    public void testLockFeaturesWithInvalidFilter() throws Exception {

        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);

        try (Transaction tx = new DefaultTransaction()) {
            store.setTransaction(tx);
            store.setFeatureLock(lock);

            // lock features
            FilterFactory ff = dataStore.getFilterFactory();
            PropertyIsEqualTo f = ff.equals(ff.property(aname("invalidProperty")), ff.literal(1));

            try {
                store.lockFeatures(f);
                fail("Should have failed with an exception, the filter is not valid");
            } catch (Exception e) {
                // fine
            }
        }
    }

    public void testLockFeaturesWithInvalidQuery() throws Exception {
        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);

        try (Transaction tx = new DefaultTransaction()) {
            store.setTransaction(tx);
            store.setFeatureLock(lock);

            // lock features
            FilterFactory ff = dataStore.getFilterFactory();
            PropertyIsEqualTo f = ff.equals(ff.property(aname("invalidProperty")), ff.literal(1));

            try {
                store.lockFeatures(new Query(store.getSchema().getTypeName(), f));
                fail("Should have failed with an exception, the filter is not valid");
            } catch (Exception e) {
                // fine
            }
        }
    }

    public void testUnlockFeatures() throws Exception {
        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);

        try (Transaction tx = new DefaultTransaction()) {
            store.setTransaction(tx);
            store.setFeatureLock(lock);

            store.lockFeatures();

            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    dataStore.getFeatureWriter(tname("ft1"), tx)) {
                assertTrue(writer.hasNext());
                SimpleFeature feature = writer.next();
                feature.setAttribute(aname("intProperty"), new Integer(100));
                try {
                    writer.write();
                    fail("write should have thrown exception");
                } catch (FeatureLockException e) {

                }
            }

            try {
                store.unLockFeatures();
                fail("unlock should have thrown an exception");
            } catch (FeatureLockException e) {
            }

            tx.addAuthorization(lock.getAuthorization());
            store.unLockFeatures();

            try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer =
                    dataStore.getFeatureWriter(tname("ft1"), tx)) {
                assertTrue(writer.hasNext());

                SimpleFeature feature = writer.next();
                feature.setAttribute(aname("intProperty"), new Integer(100));

                writer.write();
            }
        }
    }

    public void testUnlockFeaturesInvalidFilter() throws Exception {
        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);

        try (Transaction tx = new DefaultTransaction()) {
            store.setTransaction(tx);
            store.setFeatureLock(lock);
            tx.addAuthorization(lock.getAuthorization());

            store.lockFeatures();

            // uhnlock features
            FilterFactory ff = dataStore.getFilterFactory();
            PropertyIsEqualTo f = ff.equals(ff.property(aname("invalidProperty")), ff.literal(1));

            try {
                store.unLockFeatures(new Query(store.getSchema().getTypeName(), f));
                fail("Should have failed with an exception, the filter is not valid");
            } catch (Exception e) {
                // fine
            }

            store.unLockFeatures();
        }
    }

    public void testDeleteLockedFeatures() throws Exception {
        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);

        try (Transaction tx = new DefaultTransaction()) {
            store.setTransaction(tx);
            store.setFeatureLock(lock);
            tx.addAuthorization(lock.getAuthorization());

            FilterFactory ff = dataStore.getFilterFactory();
            Filter f1 = ff.id(Collections.singleton(ff.featureId(tname("ft1") + ".1")));

            assertEquals(1, store.lockFeatures(f1));

            try (Transaction tx1 = new DefaultTransaction()) {
                store.setTransaction(tx1);
                try {
                    store.removeFeatures(f1);
                    fail("Locked feature should not be deleted.");
                } catch (FeatureLockException e) {
                }
            }

            store.setTransaction(tx);
            store.removeFeatures(f1);

            tx.commit();
        }
    }

    public void testModifyLockedFeatures() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();

        Filter f0 = ff.equal(ff.property(aname("intProperty")), ff.literal(1000), true);
        assertEquals(0, store.getCount(new Query(tname("ft1"), f0)));

        FeatureLock lock = FeatureLockFactory.generate(tname("ft1"), 60 * 60 * 1000);

        try (Transaction tx = new DefaultTransaction()) {
            store.setTransaction(tx);
            store.setFeatureLock(lock);
            tx.addAuthorization(lock.getAuthorization());

            Filter f1 = ff.id(Collections.singleton(ff.featureId(tname("ft1") + ".1")));
            store.lockFeatures(f1);

            AttributeDescriptor ad;
            Integer v;
            try (Transaction tx1 = new DefaultTransaction()) {
                store.setTransaction(tx1);

                ad = store.getSchema().getDescriptor(aname("intProperty"));
                v = new Integer(1000);

                try {
                    store.modifyFeatures(ad, v, f1);
                    fail("Locked feature should not be modified.");
                } catch (FeatureLockException e) {
                }
            }

            store.setTransaction(tx);
            store.modifyFeatures(ad, v, f1);
            tx.commit();

            assertEquals(1, store.getCount(new Query(tname("ft1"), f0)));
        }
    }
}
