/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExternalGraphicTest {
    private static final long TIME = 3000;
    SimpleFeatureSource pointFS;
    SimpleFeatureSource lineFS;
    ReferencedEnvelope bounds;

    @BeforeClass
    public static void setupClass() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @Before
    public void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "point.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        pointFS = ds.getFeatureSource("point");
        lineFS = ds.getFeatureSource("line");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, CRS.decode("EPSG:4326"));

        // System.setProperty("org.geotools.test.interactive", "true");
    }

    private StreamingRenderer setupMap(String styleFile) throws IOException {
        Style gStyle = RendererBaseTest.loadStyle(this, styleFile);
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointFS, gStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        return renderer;
    }

    File file(String name) {
        return new File(
                "src/test/resources/org/geotools/renderer/lite/test-data/graphic/" + name + ".png");
    }

    @Test
    public void testExternalGraphic() throws Exception {
        StreamingRenderer renderer = setupMap("externalGraphic.sld");

        BufferedImage image =
                RendererBaseTest.showRender("External graphic", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("externalGraphic"), image, 50);
    }

    @Test
    public void testExternalGraphicAnchor() throws Exception {
        StreamingRenderer renderer = setupMap("externalGraphicAnchor.sld");

        BufferedImage image =
                RendererBaseTest.showRender("External graphic anchor", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("externalGraphicAnchor"), image, 50);
    }

    @Test
    public void testExternalGraphicDisplacement() throws Exception {
        StreamingRenderer renderer = setupMap("externalGraphicDisplacement.sld");

        BufferedImage image =
                RendererBaseTest.showRender(
                        "External graphic displacement", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("externalGraphicDisplacement"), image, 50);
    }
}
