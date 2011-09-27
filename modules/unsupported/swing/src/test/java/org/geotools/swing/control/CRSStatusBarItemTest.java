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

package org.geotools.swing.control;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiTask;
import org.junit.BeforeClass;
import org.geotools.map.MapContent;
import org.geotools.swing.MapPane;
import org.geotools.swing.testutils.MockMapPane;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for CRSStatusBarItem. These tests use FEST to check for EDT correctness
 * but they can be run in a headless environment.
 *
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class CRSStatusBarItemTest {

    private MapPane mapPane;
    private CRSStatusBarItem item;

    @BeforeClass
    public static void setupOnce() {
        FailOnThreadViolationRepaintManager.install();
    }
    
    @Before
    public void setup() {
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                mapPane = new MockMapPane();
            }
        });
    }
    
    @Test
    public void createItemWhenMapContentHasBeenSet() {
        MapContent mapContent = new MapContent();
        mapPane.setMapContent(mapContent);
        createItemInEDT();
    }

    @Test
    public void canCreateItemBeforeMapContentIsSet() {
        assertNull(mapPane.getMapContent());
        createItemInEDT();
    }

    private void createItemInEDT() {
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() throws Throwable {
                item = new CRSStatusBarItem(mapPane);
            }
        });
    }
    
}
