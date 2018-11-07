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

import static org.junit.Assert.*;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.geotools.map.MapContent;
import org.geotools.swing.MapPane;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.MockMapPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for JCRSStatusBarItem.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class JCRSStatusBarItemTest extends GraphicsTestBase {

    private MapPane mapPane;
    private JCRSStatusBarItem item;

    @Before
    public void setup() {
        GuiActionRunner.execute(
                new GuiTask() {
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
        GuiActionRunner.execute(
                new GuiTask() {
                    @Override
                    protected void executeInEDT() throws Throwable {
                        item = new JCRSStatusBarItem(mapPane);
                    }
                });
    }
}
