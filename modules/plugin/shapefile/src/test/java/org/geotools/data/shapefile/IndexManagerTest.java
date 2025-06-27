/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.data.shapefile.files.ShpFileType.QIX;
import static org.geotools.data.shapefile.files.ShpFileType.SHP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.feature.NameImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IndexManagerTest extends TestCaseSupport {

    private ShapefileDataStore mockDataStore;
    private ShpFiles shpFiles;

    @Before
    public void setUp() throws IOException {
        File statesShp = copyShapefiles(STATE_POP);
        shpFiles = new ShpFiles(statesShp);

        mockDataStore = mock(ShapefileDataStore.class);
        when(mockDataStore.getTypeName()).thenReturn(new NameImpl("statepop"));
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        shpFiles.dispose();
    }

    @Test
    public void testCreateSpatialIndex() throws Exception {
        deleteQixFile();
        IndexManager indexManager = new IndexManager(shpFiles, mockDataStore);

        assertFalse(shpFiles.exists(QIX));
        final boolean force = false;
        assertTrue(indexManager.createSpatialIndex(force));
        assertTrue(shpFiles.exists(QIX));
        assertFalse(indexManager.createSpatialIndex(force));
    }

    @Test
    public void testCreateSpatialIndex_force() throws Exception {
        deleteQixFile();
        IndexManager indexManager = new IndexManager(shpFiles, mockDataStore);

        final boolean force = true;
        assertTrue(indexManager.createSpatialIndex(force));

        File file = null;
        try {
            file = shpFiles.acquireReadFile(QIX, indexManager.writer);
            assertNotNull(file);
        } finally {
            shpFiles.unlockRead(file, indexManager.writer);
        }

        final long maxWaitNanos = TimeUnit.NANOSECONDS.convert(30, TimeUnit.SECONDS);
        final long initial = System.nanoTime();
        long now = initial;

        final long currentLastModified = file.lastModified();
        while (file.lastModified() == currentLastModified && now - initial < maxWaitNanos) {
            assertTrue(indexManager.createSpatialIndex(force));
            assertTrue(shpFiles.exists(QIX));
        }
        assertTrue(file.lastModified() > currentLastModified);
    }

    @Test
    public void testCreateSpatialIndex_stale_index_rebuilds() throws Exception {
        IndexManager indexManager = new IndexManager(shpFiles, mockDataStore);
        if (!shpFiles.exists(QIX)) {
            assertTrue(indexManager.createSpatialIndex(true));
        }

        File shpFile = null;
        File indexFile = null;
        try {
            shpFile = shpFiles.acquireReadFile(SHP, indexManager.writer);
            indexFile = shpFiles.acquireReadFile(QIX, indexManager.writer);
            assertTrue(indexFile.exists());
        } finally {
            shpFiles.unlockRead(indexFile, indexManager.writer);
            shpFiles.unlockRead(shpFile, indexManager.writer);
        }

        // make the file stale (i.e. older than the .shp)
        indexFile.setLastModified(shpFile.lastModified() - 100_000);
        final long lastModified = indexFile.lastModified();

        final boolean force = false;
        assertTrue("stale index should force rebuild", indexManager.createSpatialIndex(force));
        assertTrue(shpFiles.exists(QIX));

        assertTrue(indexFile.lastModified() > lastModified);

        assertFalse("index is not stale anymore", indexManager.createSpatialIndex(force));
    }

    @Test(timeout = 30_000) // timeout at 30s to fail in case of deadlock, too slow build server
    public void testCreateSpatialIndex_Concurrency_runs_once() throws Exception {
        deleteQixFile();
        IndexManager indexManager = new IndexManager(shpFiles, mockDataStore);

        final int threadCount = Math.min(8, 2 * Runtime.getRuntime().availableProcessors());
        final int taskCount = 2 * threadCount;
        final boolean force = false;
        List<Boolean> builds = buildSpatialIndexConcurrently(indexManager, threadCount, taskCount, force);

        long buildCount = builds.stream().filter(Boolean::booleanValue).count();

        String msg =
                String.format("Expected only 1 true result from %d concurrent createSpatialIndex calls", taskCount);
        assertEquals(msg, 1, buildCount);
    }

    @Test(timeout = 30_000) // timeout at 30s to fail in case of deadlock, too slow build server
    public void testCreateSpatialIndex_Concurrency_with_force_run_sequentially() throws Exception {
        deleteQixFile();
        final AtomicInteger concurrentBuilds = new AtomicInteger();
        final AtomicInteger maxConcurrentBuilds = new AtomicInteger();
        IndexManager indexManager = new IndexManager(shpFiles, mockDataStore) {
            @Override
            protected void doCreateSpatialIndex() throws Exception {
                int concurrency = concurrentBuilds.incrementAndGet();
                super.doCreateSpatialIndex();
                concurrentBuilds.decrementAndGet();
                maxConcurrentBuilds.set(Math.max(maxConcurrentBuilds.get(), concurrency));
            }
        };

        final int threadCount = Math.min(8, 2 * Runtime.getRuntime().availableProcessors());
        final int taskCount = 2 * threadCount;
        final boolean force = true;
        List<Boolean> builds = buildSpatialIndexConcurrently(indexManager, threadCount, taskCount, force);

        long buildCount = builds.stream().filter(Boolean::booleanValue).count();

        String msg = String.format(
                "Expected only %d true results from %d concurrent createSpatialIndex calls with force == true",
                taskCount, taskCount);
        assertEquals(msg, taskCount, buildCount);
        assertEquals(1, maxConcurrentBuilds.get());
    }

    private List<Boolean> buildSpatialIndexConcurrently(
            IndexManager indexManager, final int threadCount, final int taskCount, final boolean force)
            throws Exception {

        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Callable<Boolean>> tasks = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            tasks.add(() -> indexManager.createSpatialIndex(force));
        }
        List<Future<Boolean>> results;
        try {
            results = executor.invokeAll(tasks);
        } finally {
            executor.shutdownNow();
        }
        List<Boolean> retCodes = new ArrayList<>();
        for (Future<Boolean> f : results) {
            retCodes.add(f.get());
        }
        return retCodes;
    }

    private void deleteQixFile() throws Exception {
        if (shpFiles.exists(QIX)) {
            File path = new File(new URL(shpFiles.get(QIX)).toURI());
            assertTrue(path.delete());
        }
    }
}
