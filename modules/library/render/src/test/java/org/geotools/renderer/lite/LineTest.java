package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import junit.framework.TestCase;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;

public class LineTest extends TestCase {
    
    private static final long TIME = 4000;
    SimpleFeatureSource fs;
    ReferencedEnvelope bounds;

    @Override
    protected void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "line.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("line");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        
         // System.setProperty("org.geotools.test.interactive", "true");
    }
    
    File file(String name) {
        return new File("src/test/resources/org/geotools/renderer/lite/test-data/line/" + name
                + ".png");
    }
    
    public void testLineCircle() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineCircle.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        BufferedImage image = RendererBaseTest.showRender("Lines with circl stroke", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("circle"), image, 10);
    }
    
    public void testLineRailway() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineRailway.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        BufferedImage image = RendererBaseTest.showRender("Lines with circl stroke", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("railway"), image, 10);
    }
    
    public void testDotsStars() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "dotsStars.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        BufferedImage image = RendererBaseTest.showRender("Lines with circl stroke", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("dotstar"), image, 10);
    }

}
