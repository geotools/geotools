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

import java.awt.Frame;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;

import org.geotools.map.MapContent;
import org.geotools.swing.MapPane;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.MockMapPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Unit tests for CRSStatusBarItem which require graphics.
 *
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class CRSStatusBarItemGraphicsTest extends GraphicsTestBase<Frame> {

    private MapContent mapContent;
    private MapPane mapPane;
    private CRSStatusBarItem item;

    @Before
    public void setup() {
        JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>() {

            @Override
            protected JFrame executeInEDT() throws Throwable {
                JFrame frame = new JFrame();
                frame.setLayout(new BorderLayout());
                mapContent = new MapContent();
                mapPane = new MockMapPane();
                mapPane.setMapContent(mapContent);
                item = new CRSStatusBarItem(mapPane);
                frame.add(item, BorderLayout.CENTER);
                frame.pack();
                return frame;
            }
        });
        
        windowFixture = new FrameFixture(frame);
        ((FrameFixture) windowFixture).show();
    }
    
    @Test
    public void displaysCorrectCRSName() {
        String name = mapContent.getCoordinateReferenceSystem().getName().getCode();
        
        JButtonFixture button = windowFixture.button();
        assertNotNull(button);
        
        button.requireText(name);
    }
}
