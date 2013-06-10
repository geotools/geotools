package org.geotools.renderer.lite;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;

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

/**
 * 
 *
 * @source $URL$
 */
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
        Font f = Font.createFont(Font.TRUETYPE_FONT, TestData.getResource(this, "recreate.ttf").openStream());
        FontCache.getDefaultInstance().registerFont(f);
        
        // System.setProperty("org.geotools.test.interactive", "true");
        
    }
    
    @Test
    public void testFill() throws Exception {
        Style style = RendererBaseTest.loadSEStyle(this, "margin/fill.sld");
        
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(bfs, style));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        
        BufferedImage image = RendererBaseTest.showRender("MarginFill", renderer, TIME, bounds);
        ImageAssert.assertEquals(new File("./src/test/resources/org/geotools/renderer/lite/test-data/margin/expected.png"), image, 100); 
    }
}
