package org.geotools.gce.geotiff;

import static org.junit.Assert.fail;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.geotools.test.TestData;
import org.junit.Test;

public class GeoTiffDeadlockTest {

    /** Increase this value to get more threads than files */
    int multiplier = 2;

    @Test
    public void testForDeadlock() throws Exception {
        // grab all the test data files (but not those that contain known errors)
        final File dir = TestData.file(GeoTiffReaderTest.class, "");
        final File[] files = dir.listFiles(file -> {
            String name = file.getName();
            if (name.startsWith("no_crs_no_envelope")
                    || name.startsWith("ovr")
                    || name.startsWith("leak")
                    || file.isDirectory()) {
                return false;
            }
            return true;
        });
        final int numFiles = files.length;

        // prepare the loaders
        final AtomicInteger ai = new AtomicInteger(numFiles);

        // start them
        // System.out.println("Testing with " + numFiles + " files");
        final List<Thread> threads = new ArrayList<>();
        final int total = numFiles * multiplier;
        // System.out.println("Testing with " + total + " threads");
        for (int index = 0; index < total; index++) {
            final File file = files[index % numFiles];
            Runnable testRunner = () -> {
                try {
                    GeoTiffReader reader = new GeoTiffReader(file);
                    reader.read();
                } catch (Exception e) {
                    throw new RuntimeException("Exception opening file " + file, e);
                } finally {
                    ai.decrementAndGet();
                }
            };

            final Thread thread = new Thread(testRunner);
            thread.start();
            threads.add(thread);
        }

        // use jmx to do deadlock detection
        try {
            final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
            while (ai.get() > 0) {
                long[] deadlockedThreads = mbean.findMonitorDeadlockedThreads();
                if (deadlockedThreads != null && deadlockedThreads.length > 0) {
                    fail("Deadlock detected between the following threads: " + Arrays.toString(deadlockedThreads));
                }
                // sleep for a bit
                Thread.sleep(10);
            }
        } finally {
            // kill all the threads
            for (final Thread thread : threads) {
                if (thread.isAlive()) {
                    thread.interrupt();
                }
            }
        }
    }
}
