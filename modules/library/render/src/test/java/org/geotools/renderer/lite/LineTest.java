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
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class LineTest {
    
    private static final long TIME = 4000;
    SimpleFeatureSource fs;
    ReferencedEnvelope bounds;

    @Before
    public void setUp() throws Exception {
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
    
    @Test
    public void testLineCircle() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineCircle.sld");
        
        BufferedImage image = RendererBaseTest.showRender("Lines with circl stroke", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("circle"), image, 10);
    }
    
    @Test
    public void testLineDoubleDash() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineDoubleDash.sld");
        MapViewport viewport = renderer.getMapContent().getViewport();
        ReferencedEnvelope re = viewport.getBounds();
        ReferencedEnvelope shifted = new ReferencedEnvelope(re.getMinX() + 2, re.getMaxX() - 3, re.getMinY() + 2, re.getMaxY() - 3, re.getCoordinateReferenceSystem());
        viewport.setBounds(shifted);
        
        BufferedImage image = RendererBaseTest.showRender("Lines with double dash array (2 fts)", renderer, TIME, shifted);
        ImageAssert.assertEquals(file("doubleDash"), image, 10);
    }

    private StreamingRenderer setupLineMap(String styleFile) throws IOException {
        Style style = RendererBaseTest.loadStyle(this, styleFile);
        
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        return renderer;
    }
    
    @Test
    @Ignore
    public void testPerPropertyUOM10() throws Exception {
        StreamingRenderer renderer = setupLineMap("linePerPropertyUom.sld");
        
        BufferedImage image = RendererBaseTest.showRender("linePerPropertyUom", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("linePerPropertyUom10"), image, 10);
    }
    
    @Test
    public void testLineRailway() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineRailway.sld");
        
        BufferedImage image = RendererBaseTest.showRender("Railway", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("railway"), image, 10);
    }
    
    @Test
    public void testLineRotatedSymbol() throws Exception {
        StreamingRenderer renderer = setupLineMap("lineRotatedSymbol.sld");
        
        BufferedImage image = RendererBaseTest.showRender("Rotated symbol", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("lineRotatedSymbol"), image, 10);
    }
    
    @Test
    public void testDotsStars() throws Exception {
        StreamingRenderer renderer = setupLineMap("dotsStars.sld");
        
        BufferedImage image = RendererBaseTest.showRender("Dots and stars", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("dotstar"), image, 200);
    }

    @Test
    public void testRenderingTransform() throws Exception {
        StreamingRenderer renderer = setupLineMap("line_rendering_transform.sld");
        
        BufferedImage image = RendererBaseTest.showRender("Lines with buffer rendering transform", renderer, TIME, bounds);
        ImageAssert.assertEquals(file("renderingTransform"), image, 10);
    }
    
}
