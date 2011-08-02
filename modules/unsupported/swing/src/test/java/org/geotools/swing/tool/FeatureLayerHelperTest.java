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

package org.geotools.swing.tool;


import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import org.geotools.TestData;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.swing.testutils.MockLayer;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for FeatureLayerHelper.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
public class FeatureLayerHelperTest {
    private FeatureLayerHelper helper;
    private SimpleFeatureSource featureSource;

    @Before
    public void setup() throws Exception {
        helper = new FeatureLayerHelper();
    }

    @Test
    public void isLayerSupported() throws Exception {
        Layer layer = getPolygonLayer();
        assertTrue(helper.isSupportedLayer(layer));
        assertFalse(helper.isSupportedLayer(new MockLayer()));
    }

    @Test
    public void getInfoPolygonFeatures() throws Exception {
        doGetInfoTest(getPolygonLayer(), 10);
    }
    
    @Test
    public void doGetInfoLineFeatures() throws Exception {
        doGetInfoTest(getLineLayer(), 10);
    }
    
    @Test
    public void doGetInfoPointFeatures() throws Exception {
        doGetInfoTest(getPointLayer(), 10);
    }
    
    @Test
    public void getInfoOutsideLayerBoundsReturnsEmptyResult() throws Exception {
        Layer layer = getPointLayer();
        MapContent mapContent = new MapContent();
        mapContent.addLayer(layer);

        helper.setMapContent(mapContent);
        helper.setLayer(layer);
        
        ReferencedEnvelope bounds = layer.getBounds();
        DirectPosition2D pos = new DirectPosition2D(
                bounds.getCoordinateReferenceSystem(),
                bounds.getMinX() - 1,
                bounds.getMinY() - 1);
        
        InfoToolResult info = helper.getInfo(pos);
        assertNotNull(info);
        assertEquals(0, info.getNumFeatures());
    }
    
    private void doGetInfoTest(Layer layer, int maxFeatures) throws Exception {
        MapContent mapContent = new MapContent();
        mapContent.addLayer(layer);

        helper.setMapContent(mapContent);
        helper.setLayer(layer);

        SimpleFeatureIterator iter = featureSource.getFeatures().features();
        try {
            int n = 0;
            while (iter.hasNext() && n < maxFeatures) {
                SimpleFeature feature = iter.next();
                assertGetInfo(feature);
                n++ ;
            }

        } finally {
            iter.close();
            mapContent.dispose();
        }
    }

    private void assertGetInfo(SimpleFeature feature) throws Exception {
        DirectPosition2D pos = getFeatureQueryPos(feature);
        InfoToolResult info = helper.getInfo(pos);
        assertFalse(info.getNumFeatures() < 1);

        // Allow that there might be more than one feature in
        // the result
        int index = -1;
        String fid = feature.getIdentifier().getID();
        for (int i = 0; i < info.getNumFeatures(); i++) {
            if (info.getFeatureId(i).equals(fid)) {
                index = i;
                break;
            }
        }
        
        assertFalse("Feature not in result object", index < 0);

        Map<String, Object> data = info.getFeatureData(index);
        assertEquals(feature.getAttributeCount(), data.size());

        for (Entry<String, Object> e : data.entrySet()) {
            Object value = feature.getAttribute(e.getKey());
            assertNotNull(value);

            if (value instanceof Geometry) {
                assertEquals("Attribute " + e.getKey(),
                        e.getValue(), value.getClass().getSimpleName());
            } else {
                assertEquals("Attribute " + e.getKey(),
                        e.getValue(), value);
            }
        }
    }

    private Layer getPolygonLayer() throws Exception {
        URL url = TestData.url("shapes/statepop.shp");
        return createLayer(url);
    }
    
    private Layer getLineLayer() throws Exception {
        URL url = TestData.url("shapes/roads.shp");
        return createLayer(url);
    }
    
    private Layer getPointLayer() throws Exception {
        URL url = TestData.url("shapes/archsites.shp");
        return createLayer(url);
    }
    
    private Layer createLayer(URL url) throws Exception {
        Map params = new HashMap();
        params.put("url", url);
        DataStore dataStore = DataStoreFinder.getDataStore(params);
        String typeName = dataStore.getTypeNames()[0];
        featureSource = dataStore.getFeatureSource(typeName);

        return new FeatureLayer(featureSource, null);
    }

    private DirectPosition2D getFeatureQueryPos(SimpleFeature feature) {
        CoordinateReferenceSystem crs = featureSource.getSchema().getCoordinateReferenceSystem();
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        
        Coordinate c = null;
        switch (Geometries.get(geom)) {
            case MULTIPOLYGON:
            case POLYGON:
                c = geom.getCentroid().getCoordinate();
                break;
                
            case MULTILINESTRING:
            case LINESTRING:
                Coordinate[] coords = geom.getCoordinates();
                c = coords[coords.length / 2];
                break;
                
            case MULTIPOINT:
            case POINT:
                c = geom.getCoordinate();
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported geometry type");
        }
        
        return new DirectPosition2D(crs, c.x, c.y);
    }

}
