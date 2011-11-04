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

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.testutils.MockRenderer;
import org.geotools.swing.testutils.WaitingMapPaneListener;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for JMapPane methods that can run in a headless build.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class JMapPaneHeadlessTest extends JMapPaneTestBase {
    
    private static final Rectangle PANE = new Rectangle(100, 100);
    
    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(-10, 10, -5, 5, DefaultEngineeringCRS.CARTESIAN_2D);
    
    private static final ReferencedEnvelope SMALL_WORLD =
            new ReferencedEnvelope(-4, 4, -2, 2, DefaultEngineeringCRS.CARTESIAN_2D);
    
    @BeforeClass
    public static void setupOnce() {
        FailOnThreadViolationRepaintManager.install();
    }
    
    @Before
    public void setup() {
        mapPane = GuiActionRunner.execute(new GuiQuery<JMapPane>() {
            @Override
            protected JMapPane executeInEDT() throws Throwable {
                return new JMapPane();
            }
        });
        listener = new WaitingMapPaneListener();
    }
    
    @Test
    public void defaultRenderingExecutorCreated() {
        RenderingExecutor executor = mapPane.getRenderingExecutor();
        assertNotNull(executor);
        assertTrue(executor instanceof DefaultRenderingExecutor);
    }
    
    @Test
    public void settingRendererLinksToMapContent() {
        MapContent mapContent = new MapContent();
        mapPane.setMapContent(mapContent);
        
        GTRenderer renderer = new MockRenderer();
        mapPane.setRenderer(renderer);
        
        assertTrue(renderer.getMapContent() == mapContent);
    }
    
    @Test
    public void settingMapContentLinksToRenderer() {
        GTRenderer renderer = new MockRenderer();
        mapPane.setRenderer(renderer);
        
        MapContent mapContent = new MapContent();
        mapPane.setMapContent(mapContent);
        
        assertTrue(renderer.getMapContent() == mapContent);
    }
    
    @Test
    public void setMapContentFiresEvent() {
        mapPane.addMapPaneListener(listener);
        listener.setExpected(MapPaneEvent.Type.NEW_MAPCONTENT);
        
        MapContent mapContent = new MapContent();
        mapPane.setMapContent(mapContent);
        
        assertTrue(listener.await(MapPaneEvent.Type.NEW_MAPCONTENT, WAIT_TIMEOUT));
        
        MapPaneEvent event = listener.getEvent(MapPaneEvent.Type.NEW_MAPCONTENT);
        Object o = event.getData();
        assertNotNull(o);
        assertTrue(o instanceof MapContent);
        assertTrue(o == mapContent);
    }
    
    @Test
    public void setDisplayArea_WithMapContentSet() {
        MapContent mapContent = new MapContent();
        mapPane.setMapContent(mapContent);
        mapPane.setDisplayArea(WORLD);
        assertDisplayArea(WORLD, mapPane.getDisplayArea());
    }
    
    @Test
    public void setDisplayArea_NoMapContentSet() {
        mapPane.setDisplayArea(WORLD);
        assertDisplayArea(WORLD, mapPane.getDisplayArea());
    }
    
    @Test
    public void getDisplayArea_NoAreaSet() {
        assertTrue(mapPane.getDisplayArea().isEmpty());
    }
    
    @Test
    public void setDisplayAreaFiresEvent_WithMapContent() {
        mapPane.setMapContent(new MapContent());
        mapPane.addMapPaneListener(listener);
        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        
        mapPane.setDisplayArea(WORLD);
        assertTrue(listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, WAIT_TIMEOUT));
    }
    
    @Test
    public void setDisplayAreaFiresEvent_NoMapContent() {
        mapPane.addMapPaneListener(listener);
        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        
        mapPane.setDisplayArea(WORLD);
        assertTrue(listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, WAIT_TIMEOUT));
        
        MapPaneEvent event = listener.getEvent(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        assertNotNull(event.getData());
        assertTrue(event.getData() instanceof ReferencedEnvelope);
        
        // Since no screen area has been set the envelope sent via the
        // event should be equal to that passed to setDisplayArea
        ReferencedEnvelope eventEnv = (ReferencedEnvelope) event.getData();
        assertTrue(WORLD.boundsEquals2D(eventEnv, TOL));
    }
    
    @Test
    public void displayAreaIsSetFromMapContent() {
        MapContent mapContent = createMapContent(WORLD);
        mapPane.setMapContent(mapContent);
        
        // Since no screen area has been set the map pane's bounds should
        // be equal to the feature collection bounds
        assertTrue(WORLD.boundsEquals2D(mapPane.getDisplayArea(), TOL));
    }
    
    @Test
    public void addingLayerBeyondCurrentBoundsDoesNotChangeDisplayArea() {
        MapContent mapContent = createMapContent(SMALL_WORLD);
        mapPane.setMapContent(mapContent);

        // add larger layer and check that display area is unchanged
        mapContent.addLayer(createLayer(WORLD));
        assertTrue(SMALL_WORLD.boundsEquals2D(mapPane.getDisplayArea(), TOL));
    }
    
    @Test
    public void resetWithNoMapContentIsIgnored() {
        // just checking no exception is thrown
        mapPane.reset();
    }
    
    @Test
    public void resetWithNoLayersIsIgnored() {
        // just checking no exception is thrown
        mapPane.setMapContent(createMapContent());
        mapPane.reset();
    }
    
    @Test
    public void resetAfterRemovingAllLayersIsIgnored() {
        MapContent mapContent = createMapContent(WORLD);
        mapPane.setMapContent(mapContent);
        mapContent.removeLayer(mapContent.layers().get(0));

        // just checking no exception is thrown
        mapPane.reset();
    }
    
    @Test
    public void moveImageIgnoredWhenPaneNotVisible() {
        MapContent mapContent = createMapContent(WORLD);
        mapPane.setMapContent(mapContent);
        
        mapPane.addMapPaneListener(listener);
        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        mapPane.moveImage(100, 0);
        
        assertFalse(listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, WAIT_TIMEOUT));
    }
    
    /**
     * Compares requested display area (world bounds) to realized display area.
     * 
     * @param requestedArea requested area
     * @param realizedArea  realized area
     */
    private void assertDisplayArea(ReferencedEnvelope requestedArea, ReferencedEnvelope realizedArea) {
        // realized area should not be empty
        assertFalse(realizedArea.isEmpty());
        
        // realized area should cover the requested area
        assertTrue(mapPane.getDisplayArea().covers(requestedArea));
        
        // realized area should have the same centre coordinates as the
        // requested area
        assertEquals(realizedArea.getMedian(0), requestedArea.getMedian(0), TOL);
        assertEquals(realizedArea.getMedian(1), requestedArea.getMedian(1), TOL);
    }
    
}
