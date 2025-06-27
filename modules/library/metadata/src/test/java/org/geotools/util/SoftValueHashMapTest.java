/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.junit.Test;

/**
 * Tests {@link SoftValueHashMap}.
 *
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Ugo Moschini
 */
public final class SoftValueHashMapTest {
    /** The size of the test sets to be created. */
    private static final int SAMPLE_SIZE = 200;

    private static int NUMTHREADS = 16;
    private static int THREAD_CYCLES = (int) 1E1;
    private static int TEST_CYCLES = (int) 1E1;

    /**
     * Tests the {@link SoftValueHashMap} using strong references. The tested {@link SoftValueHashMap} should behave
     * like a standard {@link Map} object.
     */
    @Test
    public void testStrongReferences() {
        final Random random = getRandom();
        for (int pass = 0; pass < 4; pass++) {
            // make sure we can keep as many strong references as the sample, since there is no
            // guarantee
            // the distribution of random.nextBoolean() is uniform in the short term
            final SoftValueHashMap<Integer, Integer> softMap = new SoftValueHashMap<>(SAMPLE_SIZE);
            final HashMap<Integer, Integer> strongMap = new HashMap<>();
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                Integer key = random.nextInt(SAMPLE_SIZE);
                final Integer value = random.nextInt(SAMPLE_SIZE);
                if (random.nextBoolean()) // test from time to time with the null key
                key = null;

                assertEquals("containsKey:", strongMap.containsKey(key), softMap.containsKey(key));
                assertEquals("containsValue:", strongMap.containsValue(value), softMap.containsValue(value));
                assertSame("get:", strongMap.get(key), softMap.get(key));
                assertEquals("equals:", strongMap, softMap);
                if (random.nextBoolean()) {
                    // Test addition.
                    assertSame("put:", strongMap.put(key, value), softMap.put(key, value));
                } else {
                    // Test remove
                    assertSame("remove:", strongMap.remove(key), softMap.remove(key));
                }

                assertEquals("equals:", strongMap, softMap);
            }
        }
    }

    /**
     * Tests the {@link SoftValueHashMap} using soft references. In this test, we have to keep in mind than some
     * elements in {@code softMap} may disappear at any time.
     */
    @Test
    public void testSoftReferences() throws InterruptedException {
        final Random random = getRandom();
        final SoftValueHashMap<UUID, UUID> softMap = new SoftValueHashMap<>();
        final HashMap<UUID, UUID> strongMap = new HashMap<>();
        for (int pass = 0; pass < 2; pass++) {
            int count = 0;
            softMap.clear();
            strongMap.clear();
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                // We really want new instances below.
                UUID key = UUID.randomUUID();
                final UUID value = UUID.randomUUID();
                if (random.nextBoolean()) // test from time to time with the null key
                key = null;

                if (random.nextBoolean()) {
                    /*
                     * Test addition.
                     */
                    final UUID softPrevious = softMap.put(key, value);
                    final UUID strongPrevious = strongMap.put(key, value);
                    if (softPrevious == null) {
                        // If the element was not in the SoftValueHashMap (i.e. if the garbage
                        // collector has cleared it), then it must not been in HashMap neither
                        // (otherwise GC should not have cleared it).
                        assertNull("put:", strongPrevious);
                        count++; // Count only the new values.
                    } else {
                        assertNotSame(value, softPrevious);
                    }
                    if (strongPrevious != null) {
                        // Note: If 'strongPrevious==null', 'softPrevious' may not
                        //       be null if GC has not collected its entry yet.
                        assertSame("put:", strongPrevious, softPrevious);
                    }
                } else {
                    /*
                     * Test remove
                     */
                    final UUID softPrevious = softMap.get(key);
                    final UUID strongPrevious = strongMap.remove(key);
                    if (strongPrevious != null) {
                        assertSame("remove:", strongPrevious, softPrevious);
                    }
                }
                assertTrue("containsAll:", softMap.entrySet().containsAll(strongMap.entrySet()));
            }

            // Do our best to lets GC finish its work.
            for (int i = 0; i < 20; i++) {
                Runtime.getRuntime().gc();
                Runtime.getRuntime().runFinalization();
            }
            assertTrue(softMap.isValid());
            assertTrue("size:", softMap.size() <= count);
            /*
             * Make sure that all values are of the correct type. More specifically, we
             * want to make sure that we didn't forget to convert some Reference object.
             */
            for (Object value : softMap.values()) {
                assertTrue(value instanceof UUID);
                assertNotNull(value);
            }
        }
    }

    /** Tests the {@link SoftValueHashMap} with threads performing a sequence of put and get operations on the cache. */
    @Test
    public void testGetPutInMultithreadEnv() throws InterruptedException {
        final Random random = getRandom();
        SoftValueHashMap<Integer, Integer> cache = new SoftValueHashMap<>();

        // create threads
        ExecutorService executor = Executors.newFixedThreadPool(NUMTHREADS);

        for (int iter = 0; iter < TEST_CYCLES; iter++) {
            final CountDownLatch latch = new CountDownLatch(NUMTHREADS);
            for (int i = 0; i < NUMTHREADS; i++) {
                Runnable th = new CacheTestThreadGetPut(cache, random, latch);
                executor.execute(th);
            }
            latch.await();
        }

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
    }

    /**
     * Tests the {@link SoftValueHashMap} with threads that perform a sequence of put and get and threads that access
     * elements in cache through the iterator
     */
    @Test
    public void testGetPutIteratorsInMultithreadEnv() throws InterruptedException {
        final Random random = getRandom();
        SoftValueHashMap<Integer, Integer> cache = new SoftValueHashMap<>();

        // create threads
        ExecutorService executor = Executors.newFixedThreadPool(NUMTHREADS);

        for (int iter = 0; iter < TEST_CYCLES; iter++) {

            final CountDownLatch latch = new CountDownLatch(NUMTHREADS);

            // Threads that fill the cache with random values
            for (int i = 0; i < NUMTHREADS / 2; i++) {
                Runnable th = new CacheTestThreadGetPut(cache, random, latch);
                executor.execute(th);
            }

            // Threads that use the iterator on the cache
            for (int i = 0; i < NUMTHREADS / 2; i++) {
                Runnable th = new CacheTestThreadIterators(cache, latch);
                executor.execute(th);
            }

            latch.await();
        }

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
    }

    private static class CacheTestThreadGetPut implements Runnable {

        private SoftValueHashMap<Integer, Integer> cache;
        private Random random;
        private CountDownLatch latch;

        public CacheTestThreadGetPut(SoftValueHashMap<Integer, Integer> cache, Random random, CountDownLatch latch) {
            this.cache = cache;
            this.random = random;
            this.latch = latch;
        }

        @Override
        public void run() {
            for (int i = 0; i < THREAD_CYCLES; i++) {
                final Integer key = Integer.valueOf(random.nextInt(SAMPLE_SIZE));
                final Integer value = Integer.valueOf(random.nextInt(SAMPLE_SIZE));
                if (random.nextBoolean()) {
                    Object o = cache.put(key, value);
                    if (o != null) assertTrue("Put in multithread", o instanceof Integer);
                } else {
                    Object o = cache.get(key);
                    if (o != null) assertTrue("Get in multithread", o instanceof Integer);
                }
            }
            latch.countDown();
        }
    }

    private static class CacheTestThreadIterators implements Runnable {

        private SoftValueHashMap<Integer, Integer> cache;
        private CountDownLatch latch;

        public CacheTestThreadIterators(SoftValueHashMap<Integer, Integer> cache, CountDownLatch latch) {
            this.cache = cache;
            this.latch = latch;
        }

        @Override
        public void run() {
            for (int i = 0; i < THREAD_CYCLES; i++) {
                for (Object value : cache.values()) {
                    if (value != null) {
                        assertTrue("Value from iterator:", value instanceof Integer);
                    }
                }
            }
            latch.countDown();
        }
    }

    private Random getRandom() {
        long seed = System.currentTimeMillis() + hashCode();
        Random random = new Random(seed);
        Logger.getLogger(this.getClass().getName()).info("Using Random Seed: " + seed);
        return random;
    }
}
