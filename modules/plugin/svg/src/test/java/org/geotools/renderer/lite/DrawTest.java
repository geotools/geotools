package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;

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
                image, 100);
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
        Style pStyle = RendererBaseTest.loadStyle(this, "pointHouse.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY,
                true));
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image = RendererBaseTest.showRender("PointHouse", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/pointHouse.png"), image,
                100);
    }
}
