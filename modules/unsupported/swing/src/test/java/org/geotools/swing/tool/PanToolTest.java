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

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.testutils.GraphicsTestRunner;

import org.fest.swing.core.MouseButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Tests for the pan cursor tool.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class PanToolTest extends CursorToolTestBase {
    private PanTool tool;
    
    @Before
    public void setup() {
        tool = new PanTool();
    }
    
    @Test
    public void doesNotDrawDragBox() throws Exception {
        assertFalse(tool.drawDragBox());
    }
    
    @Test
    public void dragPanMap() throws Exception {
        ReferencedEnvelope startEnv = mapPane.getDisplayArea();
        AffineTransform tr = mapPane.getScreenToWorldTransform();

        Point startWindowPos = new Point(SCREEN.width / 4, SCREEN.height / 4);
        Point endWindowPos = new Point(SCREEN.width / 2, SCREEN.height / 2);
        
        Point screenPos = mapPaneFixture.component().getLocationOnScreen();
        
        Point mouseStartPos = new Point(
                screenPos.x + startWindowPos.x, 
                screenPos.y + startWindowPos.y);
        
        Point mouseEndPos = new Point(
                screenPos.x + endWindowPos.x,
                screenPos.y + endWindowPos.y);
        
        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        
        mapPane.setCursorTool(tool);
        mapPaneFixture.robot.pressMouse(mouseStartPos, MouseButton.LEFT_BUTTON);
        mapPaneFixture.robot.moveMouse(mouseEndPos);
        mapPaneFixture.robot.releaseMouseButtons();
        
        assertTrue( listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, EVENT_TIMEOUT) );
        
        ReferencedEnvelope endEnv = mapPane.getDisplayArea();

        Point2D expectedDelta = tr.deltaTransform(new Point(
                startWindowPos.x - endWindowPos.x, 
                startWindowPos.y - endWindowPos.y), 
                null);
        
        assertEquals(startEnv.getMinX() + expectedDelta.getX(), endEnv.getMinX(), TOL);
        assertEquals(startEnv.getMinY() + expectedDelta.getY(), endEnv.getMinY(), TOL);
    }

}
