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

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

import org.geotools.swing.JMapPane;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.MockMapContent;
import org.geotools.swing.testutils.TestDataUtils;
import org.geotools.swing.testutils.WaitingMapPaneListener;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;

import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Base class for tests of map pane cursor tools. Sets up the map pane and test data.
 * Extends {@linkplain GraphicsTestBase} to install the FEST error-detecting repaint
 * manager.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public abstract class CursorToolTestBase extends GraphicsTestBase {

    // Allow a long time for initial rendering of the test data
    protected static final long RENDERING_TIMEOUT = 5000;
    
    // Allow shorter time for event handling
    protected static final long EVENT_TIMEOUT = 1000;
    
    protected static final Rectangle SCREEN = new Rectangle(300, 300);
    
    protected static final double TOL = 1.0e-8;

    protected MockMapContent mapContent;
    protected JMapPane mapPane;
    protected JPanelFixture mapPaneFixture;
    protected WaitingMapPaneListener listener;


    @Before
    public void setupPaneAndTool() throws Exception {
        mapContent = new MockMapContent();
        mapContent.addLayer(TestDataUtils.getPointLayer());
        JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>() {

            @Override
            protected JFrame executeInEDT() throws Throwable {
                JFrame frame = new JFrame("Cursor tool test");
                mapPane = new JMapPane(mapContent);
                mapPane.setPreferredSize(new Dimension(ZoomInToolTest.SCREEN.width, ZoomInToolTest.SCREEN.height));
                frame.add(mapPane);
                return frame;
            }
        });
        
        listener = new WaitingMapPaneListener();
        mapPane.addMapPaneListener(listener);
        windowFixture = new FrameFixture(frame);
        mapPaneFixture = new JPanelFixture(windowFixture.robot, mapPane);
        listener.setExpected(MapPaneEvent.Type.RENDERING_STOPPED);
        ((FrameFixture) windowFixture).show();
        assertTrue(listener.await(MapPaneEvent.Type.RENDERING_STOPPED, ZoomInToolTest.RENDERING_TIMEOUT));
    }
    
}
