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
import org.geotools.swing.tool.CursorTool;
import org.geotools.swing.tool.InfoTool;
import org.geotools.swing.tool.PanTool;
import org.geotools.swing.tool.ZoomInTool;
import org.geotools.swing.tool.ZoomOutTool;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JPanelFixture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

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
    
    private static final double TOL = 1.0e-8;
    
    private static final ReferencedEnvelope WORLD = new ReferencedEnvelope(
            0, 100, 0, 100, 
            DefaultEngineeringCRS.GENERIC_2D);
    
    private static final ReferencedEnvelope SMALL_WORLD = new ReferencedEnvelope(
            25, 75, 25, 75,
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
    
    @Test
    public void toolbarButton_Info() throws Exception {
        assertToolbarButtonLoadsCorrectTool(JMapFrame.TOOLBAR_INFO_BUTTON_NAME, InfoTool.class);
    }
    
    @Test
    public void toolbarButton_Pan() throws Exception {
        assertToolbarButtonLoadsCorrectTool(JMapFrame.TOOLBAR_PAN_BUTTON_NAME, PanTool.class);
    }
    
    @Test
    public void toolbarButton_Pointer() throws Exception {
        assertToolbarButtonLoadsCorrectTool(JMapFrame.TOOLBAR_POINTER_BUTTON_NAME, null);
    }
    
    @Test
    public void toolbarButton_ZoomIn() throws Exception {
        assertToolbarButtonLoadsCorrectTool(JMapFrame.TOOLBAR_ZOOMIN_BUTTON_NAME, ZoomInTool.class);
    }
    
    @Test
    public void toolbarButton_ZoomOut() throws Exception {
        assertToolbarButtonLoadsCorrectTool(JMapFrame.TOOLBAR_ZOOMOUT_BUTTON_NAME, ZoomOutTool.class);
    }
    
    @Test
    public void toolbarButton_Reset() throws Exception {
        showWithStaticMethod(mapContent);
        mapContent.getViewport().setBounds(SMALL_WORLD);
        
        JButtonFixture button = windowFixture.toolBar().button(JMapFrame.TOOLBAR_RESET_BUTTON_NAME);
        
        button.click();
        windowFixture.robot.waitForIdle();
        
        assertTrue(mapContent.getViewport().getBounds().covers(WORLD));
    }
    
    private void assertToolbarButtonLoadsCorrectTool(String btnName, 
            Class<? extends CursorTool> expectedToolClass) throws Exception {
        
        showWithStaticMethod(mapContent);
        
        JButtonFixture button = windowFixture.toolBar().button(btnName);
        
        button.click();
        windowFixture.robot.waitForIdle();
        
        CursorTool cursorTool = ((JMapFrame) windowFixture.component()).getMapPane().getCursorTool();
        if (expectedToolClass == null) {
            assertNull(cursorTool);   
        } else {
            assertEquals(expectedToolClass, cursorTool.getClass());
        }
    }
    
    private void showWithStaticMethod(MapContent mapContent) throws Exception {
        JMapFrame.showMap(mapContent);
        assertComponentDisplayed(JMapFrame.class);
        windowFixture = listener.getFixture(DISPLAY_TIMEOUT);
    }

}