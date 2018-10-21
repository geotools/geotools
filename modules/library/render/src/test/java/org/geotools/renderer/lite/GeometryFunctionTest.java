/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.*;
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

public class GeometryFunctionTest {
    private static final long TIME = 40000;

    SimpleFeatureSource fs;

    ReferencedEnvelope bounds;

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "squareUTM.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("square");
        bounds = fs.getBounds();
        bounds.expandBy(0.5);

        RendererBaseTest.setupVeraFonts();
    }

    @Test
    public void testPointSize() throws Exception {
        runSingleLayerTest("pointSizeGeometryFunction.sld", 100);
    }

    @Test
    public void testLabel() throws Exception {
        // larger tolerance since it's a label
        runSingleLayerTest("labelGeometryFunction.sld", 300);
    }

    private void runSingleLayerTest(String styleName, int threshold) throws Exception {
        Style style = RendererBaseTest.loadStyle(this, styleName);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image = RendererBaseTest.showRender(styleName, renderer, TIME, bounds);
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/"
                                + styleName
                                + ".png");
        ImageAssert.assertEquals(reference, image, threshold);
    }
}
