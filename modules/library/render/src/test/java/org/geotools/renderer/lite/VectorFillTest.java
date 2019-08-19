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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.Mark;
import org.geotools.styling.Style;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

/**
 * Similar to {@link FillTest}, but uses the vector rendering hint, and only tests stuff that can
 * work with such hint. Also accounts for the fact the texture paint and the full vector rendering
 * do not provide the same output
 */
public class VectorFillTest {
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

        runSingleLayerTest(styleName, threshold, style);
    }

    private void runSingleLayerTest(String fileName, int threshold, Style style)
            throws Exception, IOException {
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        Map<String, Object> rendererParams = new HashMap<String, Object>();
        rendererParams.put(StreamingRenderer.VECTOR_RENDERING_KEY, Boolean.TRUE);
        renderer.setRendererHints(rendererParams);

        BufferedImage image = RendererBaseTest.showRender(fileName, renderer, TIME, bounds);
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/vector"
                                + fileName
                                + ".png");
        ImageAssert.assertEquals(reference, image, threshold);
    }

    @Test
    public void testCrossFill() throws Exception {
        runSingleLayerTest("fillCross.sld", 300);
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
        mc.dispose();
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
    public void testVertLine() throws Exception {
        testParametricMark("vertline", "shape://vertline");
    }

    @Test
    public void testHorLine() throws Exception {
        testParametricMark("horline", "shape://horline");
    }

    @Test
    public void testBackslash() throws Exception {
        testParametricMark("backslash", "shape://backslash");
    }

    @Test
    public void testPlus() throws Exception {
        testParametricMark("plus", "shape://plus");
    }

    @Test
    public void testTimes() throws Exception {
        testParametricMark("times", "shape://times");
    }

    @Test
    public void testWktShortSlash() throws Exception {
        testParametricMark("shortslash", "wkt://LINESTRING(-0.5 0, 0.5 0.5)");
    }

    @Test
    public void testWktShortBackslash() throws Exception {
        testParametricMark("shortbackslash", "wkt://LINESTRING(-0.5 0.5, 0.5 0)");
    }

    @Test
    public void testWktComposite() throws Exception {
        testParametricMark(
                "wktcomposite", "wkt://MULTILINESTRING((-0.5 -0.5, 0.5 0.5), (0 -0.5, 0 0.5))");
    }

    public void testParametricMark(String fileName, final String markName) throws Exception {
        Style slashStyle = RendererBaseTest.loadStyle(this, "fillSlash.sld");
        final DuplicatingStyleVisitor markReplacer =
                new DuplicatingStyleVisitor() {
                    @Override
                    public void visit(Mark mark) {
                        super.visit(mark);
                        Mark copy = (Mark) pages.peek();
                        copy.setWellKnownName(ff.literal(markName));
                    }
                };
        slashStyle.accept(markReplacer);
        Style style = (Style) markReplacer.getCopy();
        runSingleLayerTest(fileName, 100, style);
    }
}
