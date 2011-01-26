package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class FillTest {
    private static final long TIME = 4000;
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
    public void testSolidFill() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillSolid.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("SolidFill", renderer, TIME, bounds);
    }

    @Test
    public void testCrossFill() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillCross.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        RendererBaseTest.showRender("CrossFill", renderer, TIME, bounds);
    }
    
    @Test
    public void testTriangleFill() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillTriangle.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        RendererBaseTest.showRender("TriangleFill", renderer, TIME, bounds);
    }
    
    @Test
    public void testCircleFill() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillCircle.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        RendererBaseTest.showRender("CircleFill", renderer, TIME, bounds);
    }
    
    @Test
    public void testSlash() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillSlash.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        RendererBaseTest.showRender("SlashFill", renderer, TIME, bounds);
    }
    
    @Test
    public void testImageFill() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillImage.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("ImageFill", renderer, TIME, bounds);
    }
    
    @Test
    public void testFontFill() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillTTFDecorative.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        RendererBaseTest.showRender("TTF decorative", renderer, TIME, bounds);
    }
    
    @Test
    public void testFTSComposition() throws Exception {
    	
    	Style bgStyle = RendererBaseTest.loadStyle(this, "fillSolid.sld");
        Style fgStyle = RendererBaseTest.loadStyle(this, "fillSolidFTS.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(bfs, bgStyle);
        mc.addLayer(fs, fgStyle);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("FTS composition", renderer, TIME, bounds);
    }
}
