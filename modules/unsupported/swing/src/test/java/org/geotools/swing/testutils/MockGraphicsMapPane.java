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

package org.geotools.swing.testutils;

import javax.swing.JPanel;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.swing.event.MapMouseListener;
import org.geotools.swing.tool.MapToolManager;

/**
 * Mock map pane class for tests that require a graphics environment.
 * Allows access to the MapToolManager field.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/test/java/org/geotools/swing/testutils/MockMapPane.java $
 * @version $Id: MockMapPane.java 37675 2011-07-19 12:34:04Z mbedward $
 */
public class MockGraphicsMapPane extends JPanel {
    private MockMapPane innerPane;
    private MapToolManager toolManager;

    public MockGraphicsMapPane() {
        innerPane = new MockMapPane();
        
        toolManager = new MapToolManager(innerPane);
        addMouseListener(toolManager);
        addMouseMotionListener(toolManager);
    }

    public void setMapContent(MapContent content) {
        innerPane.setMapContent(content);
    }

    public ReferencedEnvelope getDisplayArea() {
        return innerPane.getDisplayArea();
    }

    public void addMouseListener(MapMouseListener listener) {
        innerPane.addMouseListener(listener);
    }
    
}
