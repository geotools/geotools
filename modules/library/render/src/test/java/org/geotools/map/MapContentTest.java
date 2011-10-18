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

package org.geotools.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests to ensure the consistency of MapContent and MapViewport functionality.
 * 
 * @author Jody Garnett
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class MapContentTest {
    
    // timeout period for waiting listener (see end of class)
    private static final long LISTENER_TIMEOUT = 500;
    
    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(149, 153, -32, -36, DefaultGeographicCRS.WGS84);
    
    private static final ReferencedEnvelope SMALL_WORLD =
            new ReferencedEnvelope(150, 152, -33, -35, DefaultGeographicCRS.WGS84);
    
    private static final double TOL = 1.0e-6;

    private MapContent mapContent;
    private WaitingMapListener listener;

    @Before
    public void setup() {
        mapContent = new MapContent();
        listener = new WaitingMapListener();
        mapContent.addMapLayerListListener(listener);
    }
    
    @After
    public void cleanup() {
        mapContent.dispose();
    }
    
    /**
     * Test DefaultMapContext handles layers that return null bounds.
     */
    @Test
    public void testNullLayerBounds() {
        Layer mapLayerBoundsNull = new MockLayer(null);
        mapContent.addLayer(mapLayerBoundsNull);
                
        ReferencedEnvelope maxBounds = mapContent.getMaxBounds();
        assertNotNull(maxBounds);
        assertTrue( maxBounds.isEmpty() );
    }
    
    /**
     * Calling {@link MapContent#getViewport()} initially creates a 
     * new viewport instance with default settings.
     */
    @Test
    public void getDefaultViewport() throws Exception {
        mapContent.addLayer(new MockLayer(WORLD));
        MapViewport viewport = mapContent.getViewport();
        
        assertNotNull(viewport);
        assertTrue(WORLD.boundsEquals2D(viewport.getBounds(), TOL));
    }
    
    @Test
    public void setNewViewportAndCheckBounds() {
        mapContent.addLayer(new MockLayer(WORLD));
        
        MapViewport newViewport = new MapViewport();
        newViewport.setBounds(SMALL_WORLD);
        mapContent.setViewport(newViewport);
        
        ReferencedEnvelope bounds = mapContent.getBounds();
        assertTrue(SMALL_WORLD.boundsEquals2D(bounds, TOL));
    }

    @Test
    public void addLayerAndGetEvent() {
        listener.setExpected(WaitingMapListener.Type.ADDED);
        mapContent.addLayer(new MockLayer(WORLD));
        assertTrue(listener.await(WaitingMapListener.Type.ADDED, LISTENER_TIMEOUT));
    }
    
    @Test
    public void removeLayerAndGetEvent() {
        Layer layer = new MockLayer(WORLD);
        mapContent.addLayer(layer);
        
        listener.setExpected(WaitingMapListener.Type.REMOVED);
        mapContent.removeLayer(layer);
        assertTrue(listener.await(WaitingMapListener.Type.REMOVED, LISTENER_TIMEOUT));
    }
    
    @Test
    public void moveLayerAndGetEvent() {
        Layer layer0 = new MockLayer(WORLD);
        Layer layer1 = new MockLayer(WORLD);
        mapContent.addLayer(layer0);
        mapContent.addLayer(layer1);
        
        listener.setExpected(WaitingMapListener.Type.MOVED);
        mapContent.moveLayer(0, 1);
        assertTrue(listener.await(WaitingMapListener.Type.MOVED, LISTENER_TIMEOUT));
    }
    
    @Test
    public void disposeMapContentAndGetLayerPreDisposeEvent() {
        Layer layer = new MockLayer(WORLD);
        mapContent.addLayer(layer);
        
        listener.setExpected(WaitingMapListener.Type.PRE_DISPOSE);
        mapContent.dispose();
        assertTrue(listener.await(WaitingMapListener.Type.PRE_DISPOSE, LISTENER_TIMEOUT));
    }
    
    @Test
    public void crsIsAutoSetFromFirstLayerWithNonNullCRS() {
        CoordinateReferenceSystem startCRS = mapContent.getCoordinateReferenceSystem();
        
        ReferencedEnvelope envNoCRS = new ReferencedEnvelope(WORLD, null);
        Layer layerNoCRS = new MockLayer(envNoCRS);
        mapContent.addLayer(layerNoCRS);
        assertEquals(startCRS, mapContent.getCoordinateReferenceSystem());
        
        Layer layerWithCRS = new MockLayer(WORLD);
        mapContent.addLayer(layerWithCRS);
        assertEquals(WORLD.getCoordinateReferenceSystem(), mapContent.getCoordinateReferenceSystem());
    }
    
    @Test
    public void crsIsNotAutoSetIfViewportIsNotEditable() {
        MapViewport viewport = new MapViewport();
        viewport.setEditable(false);
        mapContent.setViewport(viewport);
        
        Layer layerWithCRS = new MockLayer(WORLD);
        mapContent.addLayer(layerWithCRS);

        assertEquals(MapViewport.DEFAULT_CRS, mapContent.getCoordinateReferenceSystem());

        assertFalse(WORLD.getCoordinateReferenceSystem().equals(
                mapContent.getCoordinateReferenceSystem()));
    }
    
    @Test
    public void crsIsNotAutoSetIfViewportHasExplicitCRS() {
        MapViewport viewport = new MapViewport();
        
        // Explicitly set the CRS. Even though it is the default viewport CRS
        // it will be treated as an explicit (user-set) value
        viewport.setCoordinateReferenceSystem(MapViewport.DEFAULT_CRS);

        mapContent.setViewport(viewport);
        
        Layer layerWithCRS = new MockLayer(WORLD);
        mapContent.addLayer(layerWithCRS);

        assertTrue(viewport.isEditable());
        
        assertEquals(MapViewport.DEFAULT_CRS, mapContent.getCoordinateReferenceSystem());

        assertFalse(WORLD.getCoordinateReferenceSystem().equals(
                mapContent.getCoordinateReferenceSystem()));
    }
    
    @Test
    public void crsIsAutoSetWhenGetViewportCalledBeforeAddingLayers() {
        // Call getViewport to force creation of a default viewport
        MapViewport vp = mapContent.getViewport();
        assertEquals(MapViewport.DEFAULT_CRS, vp.getCoordinateReferenceSystem());
        
        CoordinateReferenceSystem startCRS = mapContent.getCoordinateReferenceSystem();
        
        ReferencedEnvelope envNoCRS = new ReferencedEnvelope(WORLD, null);
        Layer layerNoCRS = new MockLayer(envNoCRS);
        mapContent.addLayer(layerNoCRS);
        assertEquals(startCRS, mapContent.getCoordinateReferenceSystem());
        
        Layer layerWithCRS = new MockLayer(WORLD);
        mapContent.addLayer(layerWithCRS);
        assertEquals(WORLD.getCoordinateReferenceSystem(), vp.getCoordinateReferenceSystem());
    }

    @Test
    public void addingLayerViaLayersListFiresEvent() throws Exception {
        listener.setExpected(WaitingMapListener.Type.ADDED);
        mapContent.layers().add(new MockLayer(WORLD));
        listener.await(WaitingMapListener.Type.ADDED, LISTENER_TIMEOUT);
    }
    
    @Test
    public void addLayerWithMethodAndRemoveViaLayersList() throws Exception {
        Layer layer = new MockLayer(WORLD);
        mapContent.addLayer(layer);
        assertTrue( mapContent.layers().remove(layer) );
    }
    
    @Test
    public void addLayerAtPosViaLayerList() throws Exception {
        Layer layer1 = new MockLayer(WORLD);
        Layer layer2 = new MockLayer(WORLD);
        
        mapContent.addLayer(layer1);
        mapContent.layers().add(0, layer2);
        
        // check reference equality
        assertTrue(mapContent.layers().get(0) == layer2);
        assertTrue(mapContent.layers().get(1) == layer1);
    }
    
    @Test
    public void addFromCollectionViaLayerList() {
        Layer layer1 = new MockLayer(WORLD);
        Layer layer2 = new MockLayer(WORLD);
        Layer layer3 = new MockLayer(WORLD);
        
        List<Layer> layers = Arrays.asList(layer1, layer2, layer3);
        
        mapContent.addLayer(layer2);
        assertTrue( mapContent.layers().addAll(layers) );
        
        // check that layer2 was not added again
        assertEquals(layers.size(), mapContent.layers().size());
    }
    
    @Test
    public void addFromCollectionViaLayerListAtPosition() {
        Layer layer1 = new MockLayer(WORLD);
        Layer layer2 = new MockLayer(WORLD);
        Layer layer3 = new MockLayer(WORLD);
        
        List<Layer> layers = Arrays.asList(layer1, layer2, layer3);
        
        mapContent.addLayer(layer2);
        assertTrue( mapContent.layers().addAll(0, layers) );
        
        // check that layer2 was not added again
        assertEquals(layers.size(), mapContent.layers().size());
        
        // check expected layer order
        assertTrue(mapContent.layers().get(0) == layer1);
        assertTrue(mapContent.layers().get(1) == layer3);
        assertTrue(mapContent.layers().get(2) == layer2);
    }
    
    @Test
    public void removeLayerViaLayerListByPosition() throws Exception {
        MockLayer layer1 = new MockLayer(WORLD);
        mapContent.layers().add(layer1);
        
        MockLayer layer2 = new MockLayer(WORLD);
        mapContent.layers().add(layer2);
        
        // remove layer at position 0 (layer1)
        mapContent.layers().remove(0);
        
        assertEquals(1, mapContent.layers().size());
        assertTrue(mapContent.layers().get(0) == layer2);
        assertTrue(layer1.isDisposed());
        assertFalse(layer2.isDisposed());
    }
    
    @Test
    public void removeLayerViaLayerListByReference() throws Exception {
        MockLayer layer1 = new MockLayer(WORLD);
        mapContent.layers().add(layer1);
        
        MockLayer layer2 = new MockLayer(WORLD);
        mapContent.layers().add(layer2);
        
        mapContent.layers().remove(layer1);
        
        assertEquals(1, mapContent.layers().size());
        assertTrue(mapContent.layers().get(0) == layer2);
        assertTrue(layer1.isDisposed());
        assertFalse(layer2.isDisposed());
    }
    
    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void removeLayerIndexOutOfRangeThrowsException() throws Exception {
        mapContent.layers().add(new MockLayer(WORLD));
        mapContent.layers().remove(1);
    }
    
    @Test
    public void removeLayerNotInListReturnsFalse() throws Exception {
        assertFalse( mapContent.layers().remove(new MockLayer(WORLD)) );
    }
    
    @Test
    public void removeAllLayersInCollection() throws Exception {
        MockLayer layerFirst = new MockLayer(WORLD);
        mapContent.addLayer(layerFirst);
        
        List<MockLayer> layers = new ArrayList<MockLayer>();
        for (int i = 0; i < 3; i++) {
            MockLayer layer = new MockLayer(WORLD);
            layers.add(layer);
            mapContent.addLayer(layer);
        }
        
        MockLayer layerLast = new MockLayer(WORLD);
        mapContent.addLayer(layerLast);
        
        assertTrue(mapContent.layers().removeAll(layers));
        
        assertEquals(2, mapContent.layers().size());
        assertTrue(mapContent.layers().get(0) == layerFirst);
        assertTrue(mapContent.layers().get(1) == layerLast);
        
        assertFalse(layerFirst.isDisposed());
        assertFalse(layerLast.isDisposed());
        
        for (MockLayer layer : layers) {
            assertTrue(layer.isDisposed());
        }
    }
    
    @Test
    public void retainAllLayersInCollection() throws Exception {
        MockLayer layerFirst = new MockLayer(WORLD);
        mapContent.addLayer(layerFirst);
        
        List<MockLayer> layers = new ArrayList<MockLayer>();
        for (int i = 0; i < 3; i++) {
            MockLayer layer = new MockLayer(WORLD);
            layers.add(layer);
            mapContent.addLayer(layer);
        }
        
        MockLayer layerLast = new MockLayer(WORLD);
        mapContent.addLayer(layerLast);
        
        assertTrue(mapContent.layers().retainAll(layers));
        
        assertEquals(layers.size(), mapContent.layers().size());
        for (int i = 0; i < layers.size(); i++) {
            assertTrue(mapContent.layers().get(i) == layers.get(i));
        }
        
        assertTrue(layerFirst.isDisposed());
        assertTrue(layerLast.isDisposed());
        
        for (MockLayer layer : layers) {
            assertFalse(layer.isDisposed());
        }
    }
    
    @Test
    public void setLayerInLayerList() throws Exception {
        MockLayer layer1 = new MockLayer(WORLD);
        MockLayer layer2 = new MockLayer(WORLD);
        mapContent.addLayer(layer1);
        
        listener.setExpected(WaitingMapListener.Type.REMOVED);
        listener.setExpected(WaitingMapListener.Type.ADDED);
        
        mapContent.layers().set(0, layer2);
        
        assertTrue( listener.await(WaitingMapListener.Type.REMOVED, LISTENER_TIMEOUT) );
        assertTrue( listener.await(WaitingMapListener.Type.ADDED, LISTENER_TIMEOUT) );
        
        assertEquals(1, mapContent.layers().size());
        assertTrue(mapContent.layers().get(0) == layer2);
        assertTrue(layer1.isDisposed());
        assertFalse(layer2.isDisposed());
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void addingLayerViaListIteratorIsNotSupported() throws Exception {
        ListIterator<Layer> listIterator = mapContent.layers().listIterator();
        listIterator.add(new MockLayer(WORLD));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void removingLayerViaListIteratorIsNotSupported() throws Exception {
        mapContent.addLayer(new MockLayer(WORLD));
        ListIterator<Layer> listIterator = mapContent.layers().listIterator();
        listIterator.next();
        listIterator.remove();
    }
    
}
