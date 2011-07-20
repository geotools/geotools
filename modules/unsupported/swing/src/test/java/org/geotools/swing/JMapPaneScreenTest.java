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

import java.awt.Rectangle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.geotools.swing.event.MapPaneEvent;

import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.WaitingMapPaneListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Unit tests for JMapPane methods which require graphics.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class JMapPaneScreenTest {
    private static final long WAIT_TIMEOUT = 2000;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;
    
    private JMapPane mapPane;
    private WaitingMapPaneListener listener;
    private JFrame frame;
    private CountDownLatch setupLatch;
    
    @Before
    public void setup() {
        listener = new WaitingMapPaneListener();
        setupLatch = new CountDownLatch(1);
        doCreateAndShow(WIDTH, HEIGHT);
    }
    
    @After
    public void cleanup() {
        doCleanup();
    }
    
    @Test
    public void resizingPaneFiresEvent() throws Exception {
        assertTrue(setupLatch.await(WAIT_TIMEOUT, TimeUnit.MILLISECONDS));
        
        mapPane.addMapPaneListener(listener);
        listener.setExpected(MapPaneEvent.Type.PANE_RESIZED);
        
        final int w = WIDTH / 2;
        final int h = HEIGHT * 2;
        doSetSize(w, h);
        assertTrue(listener.await(MapPaneEvent.Type.PANE_RESIZED, WAIT_TIMEOUT));
        
        MapPaneEvent event = listener.getEvent(MapPaneEvent.Type.PANE_RESIZED);
        Object o = event.getData();
        assertNotNull(o);
        assert(o instanceof Rectangle);
        
        Rectangle r = (Rectangle) o;
        assertEquals(mapPane.getVisibleRect(), r);
    }

    private void doSetSize(final int w, final int h) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setSize(w, h);
            }
        });
    }

    private void doCreateAndShow(final int w, final int h) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame = new JFrame();
                
                mapPane = new JMapPane();
                frame.add(mapPane);
                frame.setSize(w, h);
                frame.setVisible(true);
                
                setupLatch.countDown();
            }
        });
    }

    private void doCleanup() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }
    
}
