package org.geotools.referencing;

import static org.junit.Assert.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.opengis.referencing.crs.CRSAuthorityFactory;

public class CrsCreationDeadlockTest {

    private static final int NUMBER_OF_THREADS = 32;

    @Test
    public void testForDeadlock() throws InterruptedException {
        // prepare the loaders
        final AtomicInteger ai = new AtomicInteger(NUMBER_OF_THREADS);
        final Runnable runnable = new Runnable() {
            public void run() {
                try {
                    final CRSAuthorityFactory authorityFactory = ReferencingFactoryFinder
                            .getCRSAuthorityFactory("EPSG", null);
                    authorityFactory.createCoordinateReferenceSystem("4326");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    ai.decrementAndGet();
                }
            }
        };

        // start them
        final List<Thread> threads = new ArrayList<Thread>();
        for (int index = 0; index < NUMBER_OF_THREADS; index++) {
            final Thread thread = new Thread(runnable);
            thread.start();
            threads.add(thread);
        }

        // use jmx to do deadlock detection
        try {
            final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
            while (ai.get() > 0) {
                long[] deadlockedThreads = mbean.findMonitorDeadlockedThreads();
                if (deadlockedThreads != null && deadlockedThreads.length > 0) {
                    fail("Deadlock detected between the following threads: "
                            + Arrays.toString(deadlockedThreads));
                }
                // sleep for a bit
                Thread.currentThread().sleep(10);
            }
        } finally {
            // kill all the 
            for (final Thread thread : threads) {
                if(thread.isAlive()) {
                    thread.interrupt();
                }
            }
        }
    }
}