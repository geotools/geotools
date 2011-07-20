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

import java.awt.Rectangle;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.map.FeatureLayer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.event.MapPaneEvent;

import org.geotools.swing.testutils.MockRenderer;
import org.geotools.swing.testutils.TestData;
import org.geotools.swing.testutils.WaitingMapPaneListener;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for JMapPane methods that can run in a headless build.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class JMapPaneHeadlessTest {
    
    private static final long WAIT_TIMEOUT = 500;
    private static final double TOL = 1.0E-6;
    
    private static final Rectangle PANE = new Rectangle(100, 100);
    
    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(-10, 10, -5, 5, DefaultEngineeringCRS.CARTESIAN_2D);
    
    private WaitingMapPaneListener listener;
    private JMapPane mapPane;
    
    
    @Before
    public void setup() {
        mapPane = new JMapPane();
        listener = new WaitingMapPaneListener();
    }
    
    @Test
    public void defaultRenderingExecutorCreated() {
        RenderingExecutor executor = mapPane.getRenderingExecutor();
        assertNotNull(executor);
        assertTrue(executor instanceof SingleTaskRenderingExecutor);
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
    public void setRendererFiresEvent() {
        mapPane.addMapPaneListener(listener);
        listener.setExpected(MapPaneEvent.Type.NEW_RENDERER);
        
        GTRenderer renderer = new MockRenderer();
        mapPane.setRenderer(renderer);
        
        assertTrue(listener.await(MapPaneEvent.Type.NEW_RENDERER, WAIT_TIMEOUT));
        
        MapPaneEvent event = listener.getEvent(MapPaneEvent.Type.NEW_RENDERER);
        Object o = event.getData();
        assertNotNull(o);
        assertTrue(o instanceof GTRenderer);
        assertTrue(o == renderer);
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
        MapContent mapContent = new MapContent();
        SimpleFeatureCollection fc = TestData.singlePolygonFeatureCollection(WORLD);
        Style style = SLD.createSimpleStyle(fc.getSchema());
        mapContent.addLayer(new FeatureLayer(fc, style));
        
        mapPane.setMapContent(mapContent);
        
        // Since no screen area has been set the map pane's bounds should
        // be equal to the feature collection bounds
        assertTrue(WORLD.boundsEquals2D(mapPane.getDisplayArea(), TOL));
    }
    
    /**
     * Compare requested display area (world bounds) to realized display area.
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
