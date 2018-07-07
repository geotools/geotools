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

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Map.Entry;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.swing.testutils.MockLayer;
import org.geotools.swing.testutils.TestDataUtils;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

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

    @Before
    public void setup() throws Exception {
        helper = new FeatureLayerHelper();
    }

    @Test
    public void isLayerSupported() throws Exception {
        Layer layer = TestDataUtils.getPolygonLayer();
        assertTrue(helper.isSupportedLayer(layer));
        assertFalse(helper.isSupportedLayer(new MockLayer()));
    }

    @Test
    public void getInfoPolygonFeatures() throws Exception {
        doGetInfoTest(TestDataUtils.getPolygonLayer(), 10);
    }

    @Test
    public void doGetInfoLineFeatures() throws Exception {
        doGetInfoTest(TestDataUtils.getLineLayer(), 10);
    }

    @Test
    public void doGetInfoPointFeatures() throws Exception {
        doGetInfoTest(TestDataUtils.getPointLayer(), 10);
    }

    @Test
    public void getInfoOutsideLayerBoundsReturnsEmptyResult() throws Exception {
        Layer layer = TestDataUtils.getPointLayer();
        MapContent mapContent = new MapContent();
        mapContent.addLayer(layer);

        helper.setMapContent(mapContent);
        helper.setLayer(layer);

        ReferencedEnvelope bounds = layer.getBounds();
        DirectPosition2D pos =
                new DirectPosition2D(
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

        SimpleFeatureIterator iter =
                ((SimpleFeatureSource) layer.getFeatureSource()).getFeatures().features();
        try {
            int n = 0;
            while (iter.hasNext() && n < maxFeatures) {
                SimpleFeature feature = iter.next();
                assertGetInfo(feature);
                n++;
            }

        } finally {
            iter.close();
            mapContent.dispose();
        }
    }

    private void assertGetInfo(SimpleFeature feature) throws Exception {
        DirectPosition2D pos = TestDataUtils.getPosInFeature(feature);
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
                assertEquals(
                        "Attribute " + e.getKey(), e.getValue(), value.getClass().getSimpleName());
            } else {
                assertEquals("Attribute " + e.getKey(), e.getValue(), value);
            }
        }
    }
}
