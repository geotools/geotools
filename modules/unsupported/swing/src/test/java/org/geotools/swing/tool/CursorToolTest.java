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

import java.awt.Cursor;

import org.geotools.swing.testutils.GraphicsTestBase;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the {@linkplain CursorTool} base class. These tests can be run
 * in a headless environment.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class CursorToolTest extends GraphicsTestBase {

    private CursorTool tool;

    @Before
    public void setup() {
        tool = new CursorTool() {};
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullMapPaneArgCausesException() throws Exception {
        tool.setMapPane(null);
    }

    @Test
    public void defaultIsNotToDrawDragBox() throws Exception {
        assertFalse(tool.drawDragBox());
    }

    @Test
    public void returnsDefaultCursor() throws Exception {
        Cursor.getDefaultCursor().equals(tool.getCursor());
    }
}
