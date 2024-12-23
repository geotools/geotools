/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.api.data;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

public class DataStoreFinderTest {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DataStoreFinderTest.class);

    /** Should find at least the {@link org.geotools.data.DataAccessFinderTest} mock store */
    @Test
    public void testLookup() throws Exception {
        Iterator<DataStoreFactorySpi> it = DataStoreFinder.getAvailableDataStores();
        assertTrue(it.hasNext());
    }

    @Test
    public void testDynamicRegistration() throws Exception {
        // setting up the mocks
        Map<String, ?> params = Map.of("testKey", "testValue");
        DataStoreFactorySpi mockFactory = createMock(DataStoreFactorySpi.class);
        expect(mockFactory.isAvailable()).andReturn(true).anyTimes();
        expect(mockFactory.canProcess(params)).andReturn(true).anyTimes();
        DataStore mockStore = createMock(DataStore.class);
        expect(mockFactory.createDataStore(params)).andReturn(mockStore).anyTimes();
        replay(mockFactory);

        // first lookup attempt, not registered
        assertNull(DataStoreFinder.getDataStore(params));

        // register and second lookup
        DataStoreFinder.registerFactory(mockFactory);
        try {
            assertEquals(mockStore, DataStoreFinder.getDataStore(params));
        } finally {
            // unregister, should stop working
            DataStoreFinder.deregisterFactory(mockFactory);
        }
        assertNull(DataStoreFinder.getDataStore(params));
    }

    /**
     * Test that synchronization between calling DataStoreFinder and DataAccessFinder does not result in a deadlock.
     *
     * @throws Exception unhandled exceptions during test run
     */
    @Test
    public void testDataAccessDeadlock() throws Exception {
        SlowFactory slowFactory = new SlowFactory();

        CountDownLatch latch = new CountDownLatch(2);
        Runnable t1 = () -> {
            try {
                DataAccessFinder.getDataStore(new HashMap<>());
                latch.countDown();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error calling DataAccessFinder.getDataStore", e);
            }
        };
        Runnable t2 = () -> {
            try {
                DataStoreFinder.getDataStore(new HashMap<>());
                latch.countDown();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error calling DataStoreFinder.getDataStore", e);
            }
        };
        ExecutorService es = Executors.newFixedThreadPool(3);
        try {
            DataAccessFinder.registerFactory(slowFactory);
            try {
                DataStoreFinder.registerFactory(slowFactory);
                try {
                    Future<?> f1 = es.submit(t1);
                    slowFactory.waitForIsAvailable();
                    Future<?> f2 = es.submit(t2);
                    // wait for t2 to hit the synchronization block in DataStoreFinder
                    // note: this is bad as it's based on timing, but there aren't any other hooks
                    // we can use to declaratively determine when to proceed
                    Thread.sleep(1000);
                    // allow all the rest of the calls to go through
                    slowFactory.allowIsAvailable();
                    // note: DataAccessFinder calls isAvailable twice since it delegates to
                    // DataStoreFinder
                    slowFactory.waitForIsAvailable();
                    slowFactory.allowIsAvailable();
                    slowFactory.waitForIsAvailable();
                    slowFactory.allowIsAvailable();
                    f1.get(10, TimeUnit.SECONDS);
                    f2.get(10, TimeUnit.SECONDS);
                } finally {
                    // note: if the test fails, this call is likely to hang on a deadlocked sync
                    // lock
                    DataStoreFinder.deregisterFactory(slowFactory);
                }
            } finally {
                // note: if the test fails, this call is likely to hang on a deadlocked sync lock
                DataAccessFinder.deregisterFactory(slowFactory);
            }
        } finally {
            es.shutdownNow();
        }

        boolean success = latch.await(1, TimeUnit.SECONDS);
        assertTrue(success);
    }

    static class SlowFactory implements DataStoreFactorySpi {

        private final LinkedBlockingQueue<Boolean> isAvailableGate = new LinkedBlockingQueue<>();
        private final SynchronousQueue<Boolean> isAvailableNotifier = new SynchronousQueue<>();

        /** Allow isAvailable to return for one method call */
        public void allowIsAvailable() {
            LOGGER.finest("allow isAvailable " + Thread.currentThread().getId());
            isAvailableGate.offer(false);
        }

        public void waitForIsAvailable() {
            LOGGER.finest("wait for isAvailable " + Thread.currentThread().getId());
            try {
                isAvailableNotifier.poll(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Interrupted in waitForIsAvailable", e);
            }
        }

        @Override
        public DataStore createDataStore(Map<String, ?> params) {
            return null;
        }

        @Override
        public String getDisplayName() {
            return "";
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public DataAccessFactory.Param[] getParametersInfo() {
            return new DataAccessFactory.Param[0];
        }

        @Override
        public boolean isAvailable() {
            LOGGER.log(
                    Level.FINEST, "call isAvailable " + Thread.currentThread().getId(), new Exception());
            try {
                isAvailableNotifier.offer(false, 10, TimeUnit.SECONDS);
                isAvailableGate.poll(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Interrupted in isAvailable", e);
            }
            return false;
        }

        @Override
        public DataStore createNewDataStore(Map<String, ?> params) {
            return null;
        }
    }
}
