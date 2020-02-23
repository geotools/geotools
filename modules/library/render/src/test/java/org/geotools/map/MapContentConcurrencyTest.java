/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.map;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for concurrent use of MapContent.
 *
 * @author Michael Bedward
 * @version $Id$
 */
public class MapContentConcurrencyTest {

    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(0, 100, 0, 100, DefaultEngineeringCRS.GENERIC_2D);

    // timeout period for waiting listener (see end of class)
    private static final long LISTENER_TIMEOUT = 500;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private MapContent mapContent;
    private WaitingMapListener listener;

    @Before
    public void setup() {
        mapContent = new MapContent();
        listener = new WaitingMapListener();
        mapContent.addMapLayerListListener(listener);
    }

    @Test
    public void addingLayersOnSeparateThreads() throws Exception {
        final CountDownLatch startLatch = new CountDownLatch(1);

        Layer layer1 = new MockLayer(WORLD);
        Layer layer2 = new MockLayer(WORLD);

        // Add each layer twice. If all goes well, the list should get
        // a aingle instance of each layer and a single layer-added event
        // should be fired for each
        executor.submit(new AddLayerTask(layer1, startLatch));
        executor.submit(new AddLayerTask(layer1, startLatch));
        executor.submit(new AddLayerTask(layer2, startLatch));
        executor.submit(new AddLayerTask(layer2, startLatch));

        listener.setExpected(WaitingMapListener.Type.ADDED, 2);
        startLatch.countDown();

        listener.await(WaitingMapListener.Type.ADDED, LISTENER_TIMEOUT);
        assertEquals(2, mapContent.layers().size());
    }

    /**
     * In this test we create multiple tasks: half of which add a layer to the layer list and the
     * rest which remove the layer. Then the tasks are shuffled, submitted to the executor, and all
     * started at the same time (or at least given permission to run at the same time).
     */
    @Test
    public void addAndRemoveOnSeparateThreads() throws Exception {
        final CountDownLatch startLatch = new CountDownLatch(1);
        final int numThreads = 100;

        Layer layer1 = new MockLayer(WORLD);

        List<Runnable> tasks = new ArrayList<Runnable>(numThreads);
        int k = 0;
        while (k < numThreads / 2) {
            tasks.add(
                    new AddLayerTask(layer1, startLatch) {
                        @Override
                        public void postRun() {
                            assertTrue(mapContent.layers().size() == 1);
                        }
                    });
            k++;
        }
        while (k < numThreads) {
            tasks.add(
                    new RemoveLayerTask(layer1, startLatch) {
                        @Override
                        public void postRun() {
                            assertTrue(mapContent.layers().isEmpty());
                        }
                    });
            k++;
        }

        Collections.shuffle(tasks);
        for (Runnable task : tasks) {
            executor.submit(task);
        }

        // Counting down the latch allows the tasks to run
        startLatch.countDown();
    }

    /*
     * Waits for a latch to be zeroed then adds a layer to the map content.
     */
    private class AddLayerTask implements Runnable {
        private final CountDownLatch startLatch;
        private final Layer layer;

        public AddLayerTask(Layer layer, CountDownLatch startLatch) {
            this.layer = layer;
            this.startLatch = startLatch;
        }

        @Override
        public void run() {
            try {
                startLatch.await();
                preRun();
                mapContent.layers().add(layer);
                postRun();

            } catch (InterruptedException ex) {
                /* empty */
            }
        }

        public void preRun() {}

        public void postRun() {}
    }

    /*
     * Waits for a latch to be zeroed then adds a layer to the map content.
     */
    private class RemoveLayerTask implements Runnable {
        private final CountDownLatch startLatch;
        private final Layer layer;

        public RemoveLayerTask(Layer layer, CountDownLatch startLatch) {
            this.layer = layer;
            this.startLatch = startLatch;
        }

        @Override
        public void run() {
            try {
                startLatch.await();
                preRun();
                mapContent.layers().remove(layer);
                postRun();

            } catch (InterruptedException ex) {
                /* empty */
            }
        }

        public void preRun() {}

        public void postRun() {}
    }
}
