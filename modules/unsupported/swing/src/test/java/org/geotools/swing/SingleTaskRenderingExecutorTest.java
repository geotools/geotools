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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.geotools.geometry.jts.ReferencedEnvelope;

import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for SingleTaskRenderingExecutor.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/main/java/org/geotools/swing/RenderingExecutor.java $
 * @version $Id: RenderingExecutor.java 37658 2011-07-18 09:18:52Z mbedward $
 */
public class SingleTaskRenderingExecutorTest {
    
    private RenderingExecutor executor;
    private WaitingListener listener;
    private MapContent mapContent;
    private BufferedImage image;
    private Graphics2D graphics;
    
    private static final ReferencedEnvelope WORLD = 
            new ReferencedEnvelope(150, 152, -33, -35, DefaultGeographicCRS.WGS84);
    
    private static final Rectangle PANE = new Rectangle(200, 150);
    
    @Before
    public void setup() {
        executor = new SingleTaskRenderingExecutor();
        listener = new WaitingListener();
        mapContent = new MapContent();
        mapContent.getViewport().setBounds(WORLD);
        mapContent.getViewport().setScreenArea(PANE);
    }
    
    @Test
    public void shutdownExecutor() {
        assertFalse(executor.isShutdown());
        executor.shutdown();
        assertTrue(executor.isShutdown());
    }
    
    @Test(expected=IllegalStateException.class)
    public void submitAfterShutdown() {
        executor.shutdown();
        createImage();
        executor.submit(new MapContent(), new MockRenderer(), graphics, listener);
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
    
    @Test
    public void submitAndGetStartedEvent() {
        createImage();
        listener.setExpected(WaitingListener.EventType.STARTED);
        executor.submit(mapContent, new MockRenderer(), graphics, listener);
        boolean gotEvent = listener.await(WaitingListener.EventType.STARTED, 1000);
        assertTrue(gotEvent);
    }
    
    @Test
    public void submitAndGetCompletedEvent() {
        createImage();
        listener.setExpected(WaitingListener.EventType.COMPLETED);
        executor.submit(mapContent, new MockRenderer(), graphics, listener);
        boolean gotEvent = listener.await(WaitingListener.EventType.COMPLETED, 1000);
        assertTrue(gotEvent);
    }
    
    private void createImage() {
        image = new BufferedImage(PANE.width, PANE.height, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
    }
    
}
