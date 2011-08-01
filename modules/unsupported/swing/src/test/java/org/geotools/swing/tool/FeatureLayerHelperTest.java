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

import java.io.File;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.TestData;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.DirectPosition2D;
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
    private MapContent mapContent;
    private Layer layer;
    private SimpleFeatureSource featureSource;

    @Before
    public void setup() throws Exception {
        helper = new FeatureLayerHelper();
    }

    @Test
    public void isLayerSupported() throws Exception {
        layer = getStatePopLayer();
        assertTrue(helper.isSupportedLayer(layer));
        assertFalse(helper.isSupportedLayer(new MockLayer()));
    }

    @Test
    public void getInfo() throws Exception {
        layer = getStatePopLayer();
        mapContent = new MapContent();
        mapContent.addLayer(layer);

        helper.setMapContent(mapContent);
        helper.setLayer(layer);

        SimpleFeatureIterator iter = featureSource.getFeatures().features();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                assertGetInfo(feature);
            }

        } finally {
            iter.close();
        }
    }

    private void assertGetInfo(SimpleFeature feature) throws Exception {
        DirectPosition2D pos = getFeatureCentroid(feature);
        InfoToolResult info = helper.getInfo(pos);

        assertEquals(1, info.getNumFeatures());

        Map<String, Object> data = info.getFeatureData(0);
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

    private Layer getStatePopLayer() throws Exception {
        File file = TestData.file("shapes/statepop.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        featureSource = store.getFeatureSource();

        return new FeatureLayer(featureSource, null);
    }

    private DirectPosition2D getFeatureCentroid(SimpleFeature feature) {
        CoordinateReferenceSystem crs = featureSource.getSchema().getCoordinateReferenceSystem();
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        Coordinate c = geom.getCentroid().getCoordinate();
        return new DirectPosition2D(crs, c.x, c.y);
    }

}
