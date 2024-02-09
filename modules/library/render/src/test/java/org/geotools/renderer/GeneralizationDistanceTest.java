/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.RendererBaseTest;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GeneralizationDistanceTest {

    private static final double EPS = 1e-3;
    private SimpleFeatureCollection collection;
    private Style style;
    private Double lastDistance;
    private GeneralizingCollectionFeatureSource source;
    private CoordinateReferenceSystem webMercator;

    class GeneralizingCollectionFeatureSource extends CollectionFeatureSource {

        public GeneralizingCollectionFeatureSource(SimpleFeatureCollection collection) {
            super(collection);
        }

        @Override
        public SimpleFeatureCollection getFeatures(Query query) {
            lastDistance = (Double) query.getHints().get(Hints.GEOMETRY_DISTANCE);
            return super.getFeatures(query);
        }

        @Override
        public synchronized Set<RenderingHints.Key> getSupportedHints() {
            return Collections.singleton(Hints.GEOMETRY_DISTANCE);
        }
    }

    @Before
    public void setupData() throws SchemaException, ParseException, FactoryException {
        // buildings in 3857, a troublesome CRS to back-project to for query and
        SimpleFeatureType buildingType =
                DataUtilities.createType("building", "id:0,geom:Polygon:srid=3857,name:String");
        SimpleFeature buildingFeature =
                SimpleFeatureBuilder.build(
                        buildingType,
                        new Object[] {
                            Integer.valueOf(0),
                            new WKTReader().read("POLYGON((0 0, 10 0, 10 10, 0 10, 0 0))"),
                            "church"
                        },
                        "building.bd1");
        collection = DataUtilities.collection(buildingFeature);
        source = new GeneralizingCollectionFeatureSource(collection);
        this.lastDistance = null;

        // style for it
        StyleBuilder sb = new StyleBuilder();
        style = sb.createStyle(sb.createPolygonSymbolizer());

        // the CRS
        webMercator = CRS.decode("EPSG:3857", true);
    }

    @Test
    public void testDistanceNoReprojection() throws Exception {
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(source, style));
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);

        RendererBaseTest.renderImage(
                sr, new ReferencedEnvelope(0, 10, 0, 10, webMercator), null, 100, 100);
        assertNotNull(lastDistance);
        // SR uses 80% of a pixel size as the gen distance
        assertEquals(0.08, lastDistance, EPS);
    }

    @Test
    public void testDistanceReprojectionValidArea() throws Exception {
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(source, style));
        StreamingRenderer sr = new StreamingRenderer();
        sr.setMapContent(mc);

        RendererBaseTest.renderImage(
                sr,
                new ReferencedEnvelope(0, 0.001, 0, 0.001, DefaultGeographicCRS.WGS84),
                null,
                100,
                100);
        assertNotNull(lastDistance);
        // SR uses 80% of a pixel size as the gen distance, 0.001 * 111_699 / 100 * 0.8 = 0.89
        assertEquals(0.89, lastDistance, EPS);
    }

    @Test
    public void testDistanceReprojectionWholeWorld() throws Exception {
        // when APH is disabled the calculation will fail, disabling generalization
        assertDistanceWholeWorldRendering(Collections.emptyMap(), null);

        // before the fix we would have gotten zero with APH enabled too (failure to compute the
        // distance), but no more, it's using a valid distance for a envelope spanning the planet
        assertDistanceWholeWorldRendering(
                Collections.singletonMap(
                        StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, Boolean.TRUE),
                160300d);
    }

    public void assertDistanceWholeWorldRendering(
            Map<Object, Object> hints, Double expectedDistance) {
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(source, style));
        StreamingRenderer sr = new StreamingRenderer();
        sr.setRendererHints(hints);
        sr.setMapContent(mc);

        RendererBaseTest.renderImage(
                sr,
                new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84),
                null,
                200,
                100);
        if (expectedDistance == null) {
            assertNull(lastDistance);
        } else {
            assertNotNull(lastDistance);
            assertEquals(expectedDistance, lastDistance, 1);
        }
    }
}
