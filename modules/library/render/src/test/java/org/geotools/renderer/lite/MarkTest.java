package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Font;
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
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.FontCache;
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
public class MarkTest {
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
        
        // load font
        Font f = Font.createFont(Font.TRUETYPE_FONT, TestData.getResource(this, "recreate.ttf").openStream());
        FontCache.getDefaultInstance().registerFont(f);
        
        // System.setProperty("org.geotools.test.interactive", "true");
    }

    File file(String name) {
        return new File("src/test/resources/org/geotools/renderer/lite/test-data/mark/" + name
                + ".png");
    }
    
    @Test
    public void testCircle() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "markCircle.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");
        
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointFS, pStyle));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        BufferedImage image = RendererBaseTest.showRender("Decorative marks", renderer, TIME,
                bounds);
        ImageAssert.assertEquals(file("circle"), image, 50);
    }
    
    @Test
    public void testAnchor() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "markAnchor.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        BufferedImage image = RendererBaseTest.showRender("Decorative marks", renderer, TIME,
                bounds);
        ImageAssert.assertEquals(file("markAnchor"), image, 50);
    }

    @Test
    public void testRenderingBufferCircle() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "markCircle.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(pointFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setRendererHints(Collections.singletonMap(
                StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true));
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        bounds = new ReferencedEnvelope(-10, -0.1, -10, -0.1, DefaultGeographicCRS.WGS84);

        BufferedImage image = RendererBaseTest.showRender("Decorative marks in the corner",
                renderer, TIME, bounds);
        ImageAssert.assertEquals(file("bufferCircle"), image, 50);
    }

    @Test
    public void testTriangle() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "markTriangle.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");
        
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointFS, pStyle));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        BufferedImage image = RendererBaseTest.showRender("Decorative marks", renderer, TIME,
                bounds);
        ImageAssert.assertEquals(file("triangle"), image, 50);
    }
    
    @Test
    public void testDecorative() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "markDecorative.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");
        
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(lineFS, lStyle));
        mc.addLayer(new FeatureLayer(pointFS, pStyle));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        BufferedImage image = RendererBaseTest.showRender("Decorative marks", renderer, TIME,
                bounds);
        ImageAssert.assertEquals(file("decorative"), image, 50);
    }

   
}
