/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import static java.awt.RenderingHints.*;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class CurveTest {
    private static final long TIME = 40000;

    @Before
    public void setUp() throws Exception {
        // System.setProperty("org.geotools.test.interactive", "true");
    }

    @Test
    public void testCurvePolygons() throws Exception {
        File property = new File(TestData.getResource(this, "curvepolygons.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        SimpleFeatureSource fs = ds.getFeatureSource("curvepolygons");
        ReferencedEnvelope bounds = fs.getBounds();
        bounds.expandBy(1, 1);

        Style style = RendererBaseTest.loadStyle(this, "fillSolid.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        RenderingHints hints = new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(KEY_STROKE_CONTROL, VALUE_STROKE_PURE));
        renderer.setJava2DHints(hints);

        BufferedImage image =
                RendererBaseTest.showRender("Curved polygons", renderer, TIME, bounds);
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/curvedPolygons.png");
        ImageAssert.assertEquals(reference, image, 100);
    }

    @Test
    public void testCurveLines() throws Exception {
        File property = new File(TestData.getResource(this, "curvelines.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        SimpleFeatureSource fs = ds.getFeatureSource("curvelines");
        ReferencedEnvelope bounds = fs.getBounds();
        bounds.expandBy(1, 1);

        Style style = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        RenderingHints hints = new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(KEY_STROKE_CONTROL, VALUE_STROKE_PURE));
        renderer.setJava2DHints(hints);

        BufferedImage image = RendererBaseTest.showRender("Curved lines", renderer, TIME, bounds);
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/curvedLines.png");
        ImageAssert.assertEquals(reference, image, 100);
    }
}
