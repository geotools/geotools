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

import java.awt.GraphicsEnvironment;
import org.junit.BeforeClass;
import org.geotools.swing.testutils.MockMapPane;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for MapToolManager. Requires a graphics environment.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class MapToolManagerTest {
    private static boolean headless;
    private MockMapPane pane;
    private MapToolManager manager;
    
    @BeforeClass
    public static void setupOnce() {
        headless = GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance();
    }
    
    @Before
    public void setup() {
        if (!headless) {
            pane = new MockMapPane();
            manager = new MapToolManager(pane);
        }
    }

    @Test
    public void setAndUnsetCursorTool() {
        if (!headless) {
            CursorTool tool = new InfoTool();
            manager.setCursorTool(tool);
            assertTrue(tool == manager.getCursorTool());

            manager.setNoCursorTool();
            assertNull(manager.getCursorTool());
        }
    }

}
