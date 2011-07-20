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

import org.geotools.swing.event.MapPaneEvent.Type;
import org.geotools.swing.event.MapPaneListener;
import org.geotools.swing.testutils.WaitingMapPaneListener;

import org.geotools.swing.testutils.GraphicsTestRunner;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;

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
        window.show();
    }
    
    @After
    public void cleanup() {
        window.cleanUp();
    }
    
    @Test
    public void foo() {
        listener.setExpected(Type.PANE_RESIZED);
        window.resizeTo(new Dimension(WIDTH * 2, HEIGHT * 2));
        assertTrue( listener.await(Type.PANE_RESIZED, WAIT_TIMEOUT) );
        
        Object obj = listener.getEvent(Type.PANE_RESIZED).getData();
        assertNotNull(obj);
        assertTrue(obj instanceof Rectangle);
        
        Rectangle r = (Rectangle) obj;
        assertEquals(mapPane.getVisibleRect(), r);
    }
    
    class TestFrame extends JFrame {
        
        public TestFrame(MapPaneListener listener) {
            mapPane = new JMapPane();
            add(mapPane);
            setSize(WIDTH, HEIGHT);
            mapPane.addMapPaneListener(listener);
        }
        
    }
}
