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

package org.geotools.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.WritableRaster;
import org.geotools.map.MapContent;
import org.geotools.swing.testutils.MockRenderer;
import org.geotools.swing.testutils.WaitingRenderingExecutorListener;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for SingleTaskRenderingExecutor.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
public class DefaultRenderingExecutorTest extends RenderingExecutorTestBase {

    @Before
    public void localSetup() {
        super.setup();
    }

    @Test
    public void shutdownExecutor() {
        assertFalse(executor.isShutdown());
        executor.shutdown();
        assertTrue(executor.isShutdown());
    }

    @Test(expected = IllegalStateException.class)
    public void submitAfterShutdown() {
        executor.shutdown();
        createSubmitObjects();
        executor.submit(mapContent, renderer, graphics, listener);
    }

    @Test
    public void setAndGetPollingInterval() {
        long poll = executor.getPollingInterval();
        executor.setPollingInterval(poll * 2);
        assertEquals(poll * 2, executor.getPollingInterval());
    }

    @Test
    public void invalidPollingInterval() {
        long poll = executor.getPollingInterval();

        // should be ignored
        executor.setPollingInterval(-1);
        assertEquals(poll, executor.getPollingInterval());
    }

    /**
     * GEOT-5563
     *
     * <p>Tests that the renderer continue to work on subsequent submits after a single submit has
     * failed due to a layer failure.
     */
    @Test
    public void testContinuedOperationAfterLayerFail() {
        createSubmitObjects();
        // Test requires that a map is _actually_ drawn, so cannot use the MockRenderer - replace it
        // here
        class FailableMockRenderer extends MockRenderer {
            boolean mockFail;

            public FailableMockRenderer(MapContent map) {
                super(map);
            }

            public void setFail(boolean fail) {
                this.mockFail = fail;
            }
            // Just fill the graphics with red ... our test will be looking for this
            @Override
            protected void pretendToPaint() {
                graphics.setColor(Color.red);
                graphics.fill(PANE);
                // Do the locking/waiting bit
                super.pretendToPaint();
                // And now fail if we're told to.  Note that this fail is _after_ the render
                // which simulate the actual behaviour if the map has multiple layers, only
                // one of which fails.
                if (mockFail) {
                    listeners.forEach(
                            listener ->
                                    listener.errorOccurred(
                                            new RuntimeException(
                                                    "Simulated layer rendering failure")));
                }
            }
        };

        FailableMockRenderer failableRenderer = new FailableMockRenderer(mapContent);
        this.renderer = failableRenderer;
        renderer.setPaintTime(10);

        // Execute with no problems
        clearGraphics();
        listener.setExpected(WaitingRenderingExecutorListener.Type.STARTED);
        listener.setExpected(WaitingRenderingExecutorListener.Type.COMPLETED);
        executor.submit(mapContent, renderer, graphics, listener);
        int timeoutMillis = 1000;
        listener.await(WaitingRenderingExecutorListener.Type.STARTED, timeoutMillis);
        listener.await(WaitingRenderingExecutorListener.Type.COMPLETED, timeoutMillis);
        assertTrue(listener.eventReceived(WaitingRenderingExecutorListener.Type.STARTED));
        assertTrue(listener.eventReceived(WaitingRenderingExecutorListener.Type.COMPLETED));
        checkGraphicsWasPainted();

        // Now deliberately fail the render
        clearGraphics();
        listener.setExpected(WaitingRenderingExecutorListener.Type.STARTED);
        listener.setExpected(WaitingRenderingExecutorListener.Type.FAILED);
        failableRenderer.setFail(true);
        executor.submit(mapContent, renderer, graphics, listener);
        listener.await(WaitingRenderingExecutorListener.Type.STARTED, timeoutMillis);
        listener.await(WaitingRenderingExecutorListener.Type.FAILED, timeoutMillis);
        assertTrue(listener.eventReceived(WaitingRenderingExecutorListener.Type.STARTED));
        assertTrue(listener.eventReceived(WaitingRenderingExecutorListener.Type.FAILED));
        // Actual failed submission still paints as expected
        checkGraphicsWasPainted();

        // Now remove the failure and submit again
        clearGraphics();
        listener.setExpected(WaitingRenderingExecutorListener.Type.STARTED);
        listener.setExpected(WaitingRenderingExecutorListener.Type.COMPLETED);
        failableRenderer.setFail(false);
        executor.submit(mapContent, renderer, graphics, listener);
        listener.await(WaitingRenderingExecutorListener.Type.STARTED, timeoutMillis);
        listener.await(WaitingRenderingExecutorListener.Type.COMPLETED, timeoutMillis);
        assertTrue(listener.eventReceived(WaitingRenderingExecutorListener.Type.STARTED));
        assertTrue(listener.eventReceived(WaitingRenderingExecutorListener.Type.COMPLETED));
        checkGraphicsWasPainted(); // Prior to fix, nothing is drawn
    }

    private void checkGraphicsWasPainted() {
        WritableRaster raster = image.getRaster();

        int[] pixel = new int[4];
        raster.getPixel(1, 1, pixel);
        assertEquals("Pixel at (1,1) was not renderered", 255, pixel[0]);
        assertEquals("Pixel at (1,1) was not renderered", 0, pixel[1]);
        assertEquals("Pixel at (1,1) was not renderered", 0, pixel[2]);
    }

    private void clearGraphics() {
        graphics.setColor(Color.white);
        graphics.fill(PANE);
    }
}
