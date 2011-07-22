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
import java.awt.Dimension;
import javax.swing.JFrame;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.swing.event.MapPaneEvent.Type;
import org.geotools.swing.event.MapPaneListener;
import org.geotools.swing.testutils.WaitingMapPaneListener;

import org.geotools.swing.testutils.GraphicsTestRunner;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;

import org.geotools.map.MapContent;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
    private static final long WAIT_TIMEOUT = 1000;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 150;
    private static final double TOL = 1.0e-6;
    
    private JMapPane mapPane;
    private WaitingMapPaneListener listener;

    private FrameFixture window;
    
    @BeforeClass 
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }
    
    @Before
    public void setup() {
        listener = new WaitingMapPaneListener();
        
        TestFrame frame = GuiActionRunner.execute(new GuiQuery<TestFrame>(){
            @Override
            protected TestFrame executeInEDT() throws Throwable {
                return new TestFrame(listener);
            }
        });
        
        window = new FrameFixture(frame);
        window.show(new Dimension(WIDTH, HEIGHT));
    }
    
    @After
    public void cleanup() {
        window.cleanUp();
    }
    
    @Test
    public void resizingPaneFiresEvent() {
        mapPane.setMapContent(createMapContentWithBoundsSet());
        
        listener.setExpected(Type.DISPLAY_AREA_CHANGED);
        window.resizeTo(new Dimension(WIDTH * 2, HEIGHT * 2));
        assertTrue( listener.await(Type.DISPLAY_AREA_CHANGED, WAIT_TIMEOUT) );
        
        Object obj = listener.getEvent(Type.DISPLAY_AREA_CHANGED).getData();
        assertNotNull(obj);
        assertTrue(obj instanceof ReferencedEnvelope);
    }
    
    /**
     * Creates a new, empty MapContent with the bounds set for
     * the viewport to match the aspect ratio of the map pane.
     * 
     * @return new map content
     */
    private MapContent createMapContentWithBoundsSet() {
        MapContent mapContent = new MapContent();
        mapContent.getViewport().setBounds(createMatchedBounds());
        return mapContent;
    }
    
    /**
     * Creates a ReferencedEnvelope with the same aspect ratio as the
     * map pane.
     * 
     * @return new envelope
     */
    private ReferencedEnvelope createMatchedBounds() {
        Rectangle r0 = mapPane.getVisibleRect();
        return new ReferencedEnvelope(
                0, (double) r0.width / r0.height, 0, 1.0, DefaultEngineeringCRS.CARTESIAN_2D);
    }
    
    class TestFrame extends JFrame {
        
        public TestFrame(MapPaneListener listener) {
            mapPane = new JMapPane();
            add(mapPane);
            mapPane.addMapPaneListener(listener);
        }
        
    }
}
