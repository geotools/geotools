/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.renderer.lite;


import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.test.TestData;
import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Sebastian Graca, ISPiK S.A.
 */
public class LayerOpacityTest {
    private static final long TIMEOUT = 3000;

    @Test
    public void featureLayerWithOpacity() throws Exception {
        File property = new File(TestData.getResource(this, "buildings.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        SimpleFeatureSource fs = ds.getFeatureSource("buildings");
        ReferencedEnvelope bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        StyleBuilder sb = new StyleBuilder();
        Style pst = sb.createStyle(sb.createPolygonSymbolizer(null, sb.createFill(Color.GRAY, 0.5)));

        showRenderer("fetureLayerWithOpacity", new FeatureLayer(fs, pst), bounds);
    }

    @Test
    public void coverageLayerWithOpacity() throws Exception {
        ReferencedEnvelope bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        BufferedImage bi = new BufferedImage(300, 300, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = bi.getGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 300, 300);
        g.dispose();
        GridCoverage2D coverage = new GridCoverageFactory().create("test_red", bi, bounds);

        StyleBuilder sb = new StyleBuilder();
        Style rst = sb.createStyle(sb.createRasterSymbolizer());

        showRenderer("coverageLayerWithOpacity", new GridCoverageLayer(coverage, rst), bounds);
    }

    @Test
    public void directLayerWithOpacity() throws Exception {
        ReferencedEnvelope bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        showRenderer("directLayerWithOpacity", new MockLayer(100, bounds), bounds);
    }

    private static void showRenderer(String name, Layer layer, ReferencedEnvelope bounds)
            throws Exception {
        MapContent mc = new MapContent();
        mc.addLayer(layer);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        RendererBaseTest.showRender(name + " (no opacity)", renderer, TIMEOUT, bounds);

        layer.setOpacity(0.2f);
        renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        RendererBaseTest.showRender(name + " (20% opacity)", renderer, TIMEOUT, bounds);
    }

    private static class MockLayer extends DirectLayer {
        private final int radius;
        private final ReferencedEnvelope bounds;

        private MockLayer(int radius, ReferencedEnvelope bounds) {
            this.radius = radius;
            this.bounds = bounds;
        }

        @Override
        public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
            Paint oldPaint = graphics.getPaint();
            graphics.setPaint(Color.RED);
            graphics.fillOval(0, 0, 2 * radius, 2 * radius);
            graphics.setPaint(oldPaint);
        }

        @Override
        public ReferencedEnvelope getBounds() {
            return bounds;
        }
    }

}
