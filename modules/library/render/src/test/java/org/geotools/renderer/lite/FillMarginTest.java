package org.geotools.renderer.lite;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class FillMarginTest {
    private static final long TIME = 2000;
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

    @Test
    public void testFillSE11() throws Exception {
        Style style = RendererBaseTest.loadSEStyle(this, "margin/fill.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(bfs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        BufferedImage image = RendererBaseTest.showRender("MarginFill", renderer, TIME, bounds);
        ImageAssert.assertEquals(
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/margin/expected.png"),
                image,
                100);
    }

    @Test
    public void testFillSLD10() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "margin/fill10.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(bfs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        BufferedImage image = RendererBaseTest.showRender("MarginFill", renderer, TIME, bounds);
        ImageAssert.assertEquals(
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/margin/expected.png"),
                image,
                100);
    }

    @Test
    public void testFillSLD10HighDPI() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "margin/fill10.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(bfs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        Map hints = Collections.singletonMap(StreamingRenderer.DPI_KEY, 300);
        renderer.setRendererHints(hints);

        BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null, 480, 480);
        ImageAssert.assertEquals(
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/margin/expected-dpi300.png"),
                image,
                100);
    }

    @Test
    public void testMarkMargin() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "margin/mark-margin.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(bfs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        BufferedImage image = RendererBaseTest.showRender("MarkMargin", renderer, TIME, bounds);
        ImageAssert.assertEquals(
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/margin/markmargin.png"),
                image,
                100);
    }

    @Test
    public void testFillSingle() throws Exception {
        Style style = RendererBaseTest.loadSEStyle(this, "margin/fill-single.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(bfs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        BufferedImage image = RendererBaseTest.showRender("MarginFill", renderer, TIME, bounds);
        ImageAssert.assertEquals(
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/margin/single-expected.png"),
                image,
                100);
    }
}
