/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.geotools.api.coverage.processing.Operation;
import org.geotools.api.coverage.processing.OperationNotFoundException;
import org.geotools.coverage.processing.operation.Crop;
import org.junit.Ignore;
import org.junit.Test;

/** Checks the lazy plugin scan of {@link CoverageProcessor} under concurrent access. */
public class CoverageProcessorTest {

    private static final int THREADS = 16;

    /** Lookups per thread performed by {@link #benchmarkConcurrentLookup()}. */
    private static final int BENCHMARK_LOOKUPS = 2_000_000;

    /** Counts the scans, the whole point of the lazy scan is that it runs once. */
    private static class CountingProcessor extends CoverageProcessor {
        final AtomicInteger scans = new AtomicInteger();

        @Override
        public void scanForPlugins() {
            scans.incrementAndGet();
            super.scanForPlugins();
        }
    }

    /**
     * Threads racing on a fresh processor must all get the same operation, from a single scan: the lookup itself is
     * lock free, so only the scan may be serialized.
     */
    @Test
    public void testConcurrentFirstLookupScansOnce() throws Exception {
        CountingProcessor processor = new CountingProcessor();
        assertEquals("the scan must still be pending", 0, processor.scans.get());
        List<Operation> found = runConcurrently(() -> processor.getOperation("CoverageCrop"));

        assertEquals(1, processor.scans.get());
        Operation first = found.get(0);
        assertNotNull(first);
        assertEquals(Crop.class, first.getClass());
        for (Operation operation : found) {
            assertSame(first, operation);
        }
    }

    /** Same race on the whole collection: every thread sees the fully scanned set. */
    @Test
    public void testConcurrentGetOperationsScansOnce() throws Exception {
        CountingProcessor processor = new CountingProcessor();
        int expected = new CoverageProcessor().getOperations().size();
        assertEquals("the scan must still be pending", 0, processor.scans.get());
        List<Collection<Operation>> found = runConcurrently(() -> processor.getOperations());

        assertEquals(1, processor.scans.get());
        for (Collection<Operation> operations : found) {
            assertEquals(expected, operations.size());
        }
    }

    /** A miss must not be mistaken for an unscanned processor and trigger a classpath rescan. */
    @Test
    public void testUnknownOperationDoesNotRescan() {
        CountingProcessor processor = new CountingProcessor();
        for (int i = 0; i < 3; i++) {
            assertThrows(OperationNotFoundException.class, () -> processor.getOperation("NotAnOperation"));
        }
        assertEquals(1, processor.scans.get());
    }

    /**
     * Measures the contention the lock free lookup is meant to remove. Not an assertion, and not part of the build: run
     * it from the IDE against this and the previous locking strategy, and compare the printed times.
     *
     * <p>On a 12 core Ryzen 9 AI: 17862 ms with the operations held in a {@code Collections.synchronizedMap(TreeMap)},
     * 2230 ms with the current {@code ConcurrentSkipListMap}. Absolute numbers depend on the machine, the ratio is the
     * point.
     */
    @Ignore("manual benchmark, run it from the IDE")
    @Test
    public void benchmarkConcurrentLookup() throws Exception {
        CoverageProcessor processor = new CoverageProcessor();
        timeLookups(processor); // warmup, so the measured run finds the lookup path compiled
        long millis = TimeUnit.NANOSECONDS.toMillis(timeLookups(processor));
        CoverageProcessor.LOGGER.info(THREADS + " threads x " + BENCHMARK_LOOKUPS + " lookups: " + millis + " ms");
    }

    /** Wall clock nanos for every thread to complete {@link #BENCHMARK_LOOKUPS} lookups. */
    private static long timeLookups(CoverageProcessor processor) throws Exception {
        long start = System.nanoTime();
        runConcurrently(() -> {
            // returned so the loop cannot be optimized away
            Operation last = null;
            for (int i = 0; i < BENCHMARK_LOOKUPS; i++) {
                last = processor.getOperation("CoverageCrop");
            }
            return last;
        });
        return System.nanoTime() - start;
    }

    /** Runs the task on all threads at once, so they contend on the very first lookup. */
    private static <T> List<T> runConcurrently(Callable<T> task) throws Exception {
        CyclicBarrier barrier = new CyclicBarrier(THREADS);
        List<Callable<T>> tasks = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            tasks.add(() -> {
                barrier.await();
                return task.call();
            });
        }
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        try {
            List<T> results = new ArrayList<>();
            for (Future<T> future : executor.invokeAll(tasks)) {
                results.add(future.get());
            }
            return results;
        } finally {
            executor.shutdownNow();
        }
    }
}
