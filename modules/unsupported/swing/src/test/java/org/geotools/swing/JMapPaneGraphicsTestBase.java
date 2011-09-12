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

import java.awt.BorderLayout;
import javax.swing.JFrame;

import org.geotools.swing.event.MapPaneListener;

import org.fest.swing.fixture.FrameFixture;

/**
 * Test base class which adds a frame fixture for graphics tests.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/test/java/org/geotools/swing/JMapPaneGraphicsTest.java $
 * @version $Id: JMapPaneGraphicsTest.java 37777 2011-08-03 13:19:58Z mbedward $
 */
public class JMapPaneGraphicsTestBase extends JMapPaneTestBase {
    
    protected FrameFixture window;
    
    protected class TestFrame extends JFrame {
        
        public TestFrame(MapPaneListener listener) {
            mapPane = new JMapPane();
            mapPane.addMapPaneListener(listener);
            
            setLayout(new BorderLayout());
            add(mapPane, BorderLayout.CENTER);
            pack();
        }
    }
    
}
