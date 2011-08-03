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

package org.geotools.swing.tool;

import org.junit.Ignore;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.MockGraphicsMapPane;
import org.geotools.swing.testutils.WaitingMapMouseListener;

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
 * Unit tests for MapToolManager that require a graphics environment.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
@Ignore("Still working out how to write these tests")
public class MapToolManagerGraphicsTest {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    
    private static final long AWAIT_TIMEOUT = 2000;
    
    private MockGraphicsMapPane mapPane;
    private FrameFixture window;
    
    @BeforeClass 
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }
    
    @Before
    public void setup() {

        TestFrame frame = GuiActionRunner.execute(new GuiQuery<TestFrame>(){
            @Override
            protected TestFrame executeInEDT() throws Throwable {
                return new TestFrame();
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
    public void testMouseClicked() {
        WaitingMapMouseListener listener = new WaitingMapMouseListener();
        mapPane.addMouseListener(listener);
        
        listener.setExpected(WaitingMapMouseListener.Type.CLICKED);
        window.robot.click(mapPane);
        assertTrue(listener.await(WaitingMapMouseListener.Type.CLICKED, AWAIT_TIMEOUT));
    }

    @Test
    public void testMousePressed() {
    }

    @Test
    public void testMouseReleased() {
    }

    @Test
    public void testMouseEntered() {
    }

    @Test
    public void testMouseExited() {
    }

    @Test
    public void testMouseDragged() {
    }

    @Test
    public void testMouseMoved() {
    }

    @Test
    public void testMouseWheelMoved() {
    }


    class TestFrame extends JFrame {
        public TestFrame() {
            mapPane = new MockGraphicsMapPane();
            add(mapPane);
        }
    }
}
