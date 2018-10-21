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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.SLD;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;

public class FillTest {
    private static final long TIME = 40000;

    SimpleFeatureSource fs;

    SimpleFeatureSource bfs;

    ReferencedEnvelope bounds;

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "square.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("square");
        bfs = ds.getFeatureSource("bigsquare");
        bounds = fs.getBounds();
        bounds.expandBy(0.2, 0.2);

        // load font
        Font f =
                Font.createFont(
                        Font.TRUETYPE_FONT,
                        TestData.getResource(this, "recreate.ttf").openStream());
        FontCache.getDefaultInstance().registerFont(f);

        // System.setProperty("org.geotools.test.interactive", "true");

    }

    private void runSingleLayerTest(String styleName) throws Exception {
        runSingleLayerTest(styleName, 100);
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

    @Test
    public void testSolidFill() throws Exception {
        runSingleLayerTest("fillSolid.sld");
    }

    @Test
    public void testCrossFill() throws Exception {
        runSingleLayerTest("fillCross.sld", 250);
    }

    @Test
    public void testCrossFillZoomedOut() throws Exception {
        String styleName = "fillCrossUom.sld";
        Style style = RendererBaseTest.loadStyle(this, styleName);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        // this used to blow up while building the repeatable tile used to perform the fill
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = bi.createGraphics();
        renderer.paint(graphics, new Rectangle(0, 0, 10, 10), bounds);
        graphics.dispose();
        mc.dispose();
    }

    @Test
    public void testTriangleFill() throws Exception {
        runSingleLayerTest("fillTriangle.sld", 250);
    }

    @Test
    public void testCircleFill() throws Exception {
        runSingleLayerTest("fillCircle.sld");
    }

    @Test
    public void testSlash() throws Exception {
        runSingleLayerTest("fillSlash.sld");
    }

    @Test
    public void testImageFill() throws Exception {
        runSingleLayerTest("fillImage.sld");
    }

    @Test
    public void testMarkFillRotated() throws Exception {
        runSingleLayerTest("fillMarkRotated.sld");
    }

    @Test
    public void testFontFill() throws Exception {
        runSingleLayerTest("fillTTFDecorative.sld");
    }

    @Test
    public void testRandomSlash() throws Exception {
        runSingleLayerTest("fillRandomSlash.sld");
    }

    @Test
    public void testRandomRotatedSlash() throws Exception {
        runSingleLayerTest("fillRandomRotatedSlash.sld");
    }

    @Test
    public void testFillRandomGraphic() throws Exception {
        runSingleLayerTest("fillRandomGraphic.sld");
    }

    @Test
    public void testFillRandomRotatedGraphic() throws Exception {
        runSingleLayerTest("fillRandomRotatedGraphic.sld");
    }

    @Test
    public void testFillRandomTwoMarks() throws Exception {
        runSingleLayerTest("fillRandomTwoMarks.sld");
    }

    @Test
    public void testFillRandomGridSlash() throws Exception {
        runSingleLayerTest("fillRandomGridSlash.sld");
    }

    @Test
    public void testFillRandomGridGraphic() throws Exception {
        runSingleLayerTest("fillRandomGridGraphic.sld");
    }

    @Test
    public void testFillRandomGridRotatedSlash() throws Exception {
        runSingleLayerTest("fillRandomGridRotatedSlash.sld");
    }

    @Test
    public void testFillRandomGridRotatedGraphic() throws Exception {
        runSingleLayerTest("fillRandomGridRotatedGraphic.sld");
    }

    @Test
    public void testFTSComposition() throws Exception {
        Style bgStyle = RendererBaseTest.loadStyle(this, "fillSolid.sld");
        Style fgStyle = RendererBaseTest.loadStyle(this, "fillSolidFTS.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(bfs, bgStyle));
        mc.addLayer(new FeatureLayer(fs, fgStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        RendererBaseTest.showRender("FTS composition", renderer, TIME, bounds);
    }

    @Test
    public void testGEOT3111() throws Exception {
        FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2(null);
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        Symbolizer sym =
                sf.createPolygonSymbolizer(
                        Stroke.NULL, sf.createFill(ff2.literal(Color.CYAN)), null);
        Style style = SLD.wrapSymbolizers(sym);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        RendererBaseTest.showRender("GEOT-3111", renderer, TIME, bounds);
    }
}
