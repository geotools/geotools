/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.shape;

import junit.framework.TestCase;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.styling.Style;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Shapefile renderer delegates to Streaming Renderer if a layer is not a Shapefile layer. This
 * tests that.
 * 
 * @author Jesse
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 *
 * @source $URL$
 */
public class RenderNonShapefileTest extends TestCase {
    
    public void testRender() throws Exception {
        MemoryDataStore store = new MemoryDataStore();
        IndexedShapefileDataStore polys = TestUtilites.getPolygons();
        SimpleFeatureCollection featureCollection = polys
                .getFeatureSource().getFeatures();
        store.createSchema(polys.getSchema());
        SimpleFeatureSource target = store.getFeatureSource(store
                .getTypeNames()[0]);
        ((SimpleFeatureStore) target).addFeatures(featureCollection);
        Style testStyle = TestUtilites.createTestStyle(target.getSchema().getTypeName(), null);
        MapLayer layer = new DefaultMapLayer(target, testStyle);
        MapContext context = new DefaultMapContext(new MapLayer[] { layer }, 
                polys.getSchema().getCoordinateReferenceSystem());
        ShapefileRenderer renderer = new ShapefileRenderer(context);
        ReferencedEnvelope env = target.getBounds();
        TestUtilites.showRender("testSimpleRender", renderer, 10000, env);
    }

    /**
     * Test {@link FeatureSourceMapLayer} using version of {@link #testRender()}.
     * 
     * @throws Exception
     */
    public void testRenderFeatureSourceMapLayer() throws Exception {
        MemoryDataStore store = new MemoryDataStore();
        IndexedShapefileDataStore polys = TestUtilites.getPolygons();
        SimpleFeatureCollection featureCollection = polys
                .getFeatureSource().getFeatures();
        store.createSchema(polys.getSchema());
        SimpleFeatureSource target = store.getFeatureSource(store
                .getTypeNames()[0]);
        ((SimpleFeatureStore) target).addFeatures(featureCollection);
        Style testStyle = TestUtilites.createTestStyle(target.getSchema().getTypeName(), null);
        MapLayer layer = new DefaultMapLayer( new FeatureLayer(target, testStyle));
        MapContext context = new DefaultMapContext(new MapLayer[] { layer }, polys.getSchema().getCoordinateReferenceSystem());
        ShapefileRenderer renderer = new ShapefileRenderer(context);
        Envelope env = context.getLayerBounds();
        env = new Envelope(env.getMinX(), env.getMaxX(), env.getMinY(), env.getMaxY());
        TestUtilites.showRender("testSimpleRender", renderer, 1000, env);
    }

}
