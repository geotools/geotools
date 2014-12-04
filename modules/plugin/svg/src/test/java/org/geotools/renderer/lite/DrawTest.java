package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

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

/**
 * 
 * 
 * @source $URL$
 */
public class DrawTest {
    private static final long TIME = 4000;

    SimpleFeatureSource squareFS;

    SimpleFeatureSource lineFS;

    SimpleFeatureSource pointFS;

    ReferencedEnvelope bounds;

    @BeforeClass
    public static void setupClass() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "square.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        squareFS = ds.getFeatureSource("square");
        lineFS = ds.getFeatureSource("line");
        pointFS = ds.getFeatureSource("point");
        bounds = squareFS.getBounds();
        bounds.expandBy(0.2, 0.2);

        // System.setProperty("org.geotools.test.interactive", "true");
    }

    private void runFillTest(String styleName) throws Exception {
        Style style = RendererBaseTest.loadStyle(this, styleName);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(squareFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image = RendererBaseTest.showRender(styleName, renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/" + styleName + ".png"),
                image, 1000);
    }

    @Test
    public void testImageFill() throws Exception {
        runFillTest("fillHouse.sld");
    }

    @Test
    public void testRandomImageFill() throws Exception {
        runFillTest("fillRandomSVG.sld");
    }

    @Test
    public void testRandomRotatedImageFill() throws Exception {
        runFillTest("fillRandomRotatedSVG.sld");
    }

    @Test
    public void testPoint() throws Exception {
        StreamingRenderer renderer = setupPointRenderer("pointHouse.sld");

        BufferedImage image = RendererBaseTest.showRender("PointHouse", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/pointHouse.png"), image,
                1000);
    }

    @Test
    public void testDisplacedPoint() throws Exception {
        StreamingRenderer renderer = setupPointRenderer("pointHouseDisplaced.sld");

        BufferedImage image = RendererBaseTest.showRender("PointHouseDisplaced", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                  "./src/test/resources/org/geotools/renderer/lite/test-data/pointHouseDisplaced.png"),
                  image, 1000);
    }

    @Test
    public void testDisplacedLine() throws Exception {
        StreamingRenderer renderer = setupLineRenderer("lineHouseDisplaced.sld");

        BufferedImage image = RendererBaseTest.showRender("LineHouseDisplaced", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                  "./src/test/resources/org/geotools/renderer/lite/test-data/lineHouseDisplaced.png"),
                  image, 1000);
    }

    @Test
    public void testAnchorPoint() throws Exception {
        StreamingRenderer renderer = setupPointRenderer("pointHouseAnchor.sld");

        BufferedImage image = RendererBaseTest.showRender("PointHouse", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/pointHouseAnchor.png"),
                image, 1000);
    }

    private StreamingRenderer setupPointRenderer(String pointStyle) throws IOException {
        Style pStyle = RendererBaseTest.loadStyle(this, pointStyle);
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY,
                true));
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        return renderer;
    }

    private StreamingRenderer setupLineRenderer(String lineStyle) throws IOException {
        Style lStyle = RendererBaseTest.loadStyle(this, lineStyle);
        Style baseStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, baseStyle));
        mc.addLayer(new FeatureLayer(lineFS, lStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY,
                true));
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        return renderer;
    }
}
