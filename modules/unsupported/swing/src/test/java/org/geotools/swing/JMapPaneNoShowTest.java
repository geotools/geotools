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


import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.map.MapContent;
import org.geotools.swing.event.MapPaneEvent;
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
 * Unit tests for JMapPane methods which require a graphics environment but which
 * do not display the map pane.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class JMapPaneNoShowTest extends JMapPaneGraphicsTestBase {
    
    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(-10, 10, -5, 5, DefaultEngineeringCRS.CARTESIAN_2D);
    
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
    }
    
    @After
    public void cleanup() {
        window.cleanUp();
    }
    
    @Test
    public void moveImageIgnoredWhenPaneNotVisible() {
        MapContent mapContent = createMapContent(WORLD);
        mapPane.setMapContent(mapContent);
        
        mapPane.addMapPaneListener(listener);
        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        mapPane.moveImage(100, 0);
        
        assertFalse(listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, WAIT_TIMEOUT));
    }
    
    
}
