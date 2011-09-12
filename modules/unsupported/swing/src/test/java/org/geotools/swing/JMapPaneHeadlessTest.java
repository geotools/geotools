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

import com.vividsolutions.jts.geom.Polygon;
import org.geotools.map.Layer;
import org.junit.BeforeClass;
import java.awt.Rectangle;
import java.util.Arrays;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.map.FeatureLayer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.event.MapPaneEvent;

import org.geotools.swing.testutils.MockRenderer;
import org.geotools.swing.testutils.WaitingMapPaneListener;

import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
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
    
    private static final ReferencedEnvelope SMALL_WORLD =
            new ReferencedEnvelope(-4, 4, -2, 2, DefaultEngineeringCRS.CARTESIAN_2D);
    
    private WaitingMapPaneListener listener;
    private JMapPane mapPane;
    
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
    
    /**
     * Creates a new MapContent optionally populated with single-feature Layers
     * having the specified bounds.
     * 
     * @param boundsOfLayers 0 or more bounds for layers
     * @return new map content
     */
    private MapContent createMapContent(ReferencedEnvelope ...boundsOfLayers) {
        MapContent mapContent = new MapContent();
        if (boundsOfLayers != null) {
            for (ReferencedEnvelope env : boundsOfLayers) {
                mapContent.addLayer(createLayer(env));
            }
        }
        return mapContent;
    }

    /**
     * Creates a new feature layer.
     * 
     * @param env layer bounds
     * @return the new layer
     */
    private Layer createLayer(ReferencedEnvelope env) {
        SimpleFeatureCollection fc = singlePolygonFeatureCollection(env);
        Style style = SLD.createSimpleStyle(fc.getSchema());
        return new FeatureLayer(fc, style);
    }

    /**
     * Creates a feature collection containing a single feature with a
     * polygon geometry based on the input envelope.
     *
     * @param env the input envelope
     * @return new feature collection
     */
    private SimpleFeatureCollection singlePolygonFeatureCollection(ReferencedEnvelope env) {
        if (env == null || env.isEmpty()) {
            throw new IllegalArgumentException("env must not be null or empty");
        }

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("rectangle");
        typeBuilder.add("shape", Polygon.class, env.getCoordinateReferenceSystem());
        typeBuilder.add("label", String.class);
        final SimpleFeatureType TYPE = typeBuilder.buildFeatureType();

        SimpleFeature feature = SimpleFeatureBuilder.build(
                TYPE, new Object[]{JTS.toGeometry(env), "a rectangle"}, null);

        return new ListFeatureCollection(TYPE, Arrays.asList(feature));
    }

}
