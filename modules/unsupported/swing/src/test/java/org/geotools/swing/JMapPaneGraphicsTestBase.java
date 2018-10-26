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
import org.assertj.swing.fixture.FrameFixture;
import org.geotools.swing.event.MapPaneListener;

/**
 * Test base class which adds a frame fixture for graphics tests.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
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
