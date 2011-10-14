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

import java.awt.AWTEvent;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JPanel;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.swing.control.JMapStatusBar;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.MockLayer;
import org.geotools.swing.testutils.MockMapContent;
import org.geotools.swing.testutils.WindowActivatedListener;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.fixture.JPanelFixture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for JMapFrame.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
@RunWith(GraphicsTestRunner.class)
public class JMapFrameTest extends GraphicsTestBase<Frame> {
    private static ReferencedEnvelope WORLD = new ReferencedEnvelope(0, 100, 0, 100, 
            DefaultEngineeringCRS.GENERIC_2D);

    private WindowActivatedListener listener;
    private MapContent mapContent;
    
    @Before
    public void setup() {
        Layer layer = new MockLayer(WORLD);
        mapContent = new MockMapContent();
        mapContent.addLayer(layer);
        
        listener = new WindowActivatedListener(JMapFrame.class);
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.WINDOW_EVENT_MASK);
    }
    
    @After
    public void cleanup() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
    }
    
    /**
     * First test is just to allow the FEST repaint manager to check for any 
     * EDT violations when the static {@linkplain JMapFrame#showMap(org.geotools.map.MapContent)}
     * method is called.
     */
    @Test
    public void showMapIsEDTSafe() throws Exception {
        showWithStaticMethod(mapContent);
    }
    
    @Test
    public void checkDefaultFrameComponentsAreDisplayed() throws Exception {
        showWithStaticMethod(mapContent);
        
        // map pane displayed
        JPanelFixture mapPane = windowFixture.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            @Override
            protected boolean isMatching(JPanel component) {
                return component instanceof JMapPane;
            }
        });
        
        mapPane.requireVisible();
        
        // tool bar displayed and enabled
        windowFixture.toolBar().requireEnabled().requireVisible();

        // status bar displayed
        JPanelFixture statusBar = windowFixture.panel(new GenericTypeMatcher<JPanel>(JPanel.class) {
            @Override
            protected boolean isMatching(JPanel component) {
                return component instanceof JMapStatusBar;
            }
        });
        
        statusBar.requireVisible();
    }

    private void showWithStaticMethod(MapContent mapContent) throws Exception {
        JMapFrame.showMap(mapContent);
        assertComponentDisplayed(JMapFrame.class);
        windowFixture = listener.getFixture(DISPLAY_TIMEOUT);
    }

}