package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
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
public class LabelShieldTest {
    
    private static final long TIME = 1000;
    SimpleFeatureSource fs;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;

    SimpleFeatureSource fs_multiline;

    @Before
    public void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "diaglines.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("diaglines");
        fs_multiline = ds.getFeatureSource("diaglines_multiline");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        
        renderer = new StreamingRenderer();
        Map rendererParams = new HashMap();
        LabelCacheImpl labelCache = new LabelCacheImpl();
        rendererParams.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
        renderer.setRendererHints(rendererParams);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        FontCache.getDefaultInstance().registerFont(
                Font.createFont(Font.TRUETYPE_FONT, TestData.getResource(this, "Vera.ttf")
                        .openStream()));
        // System.setProperty("org.geotools.test.interactive", "true");
    }
    
    @Test
    public void testLabelShield() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLabelShield.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("Labels and shield", renderer, TIME, bounds);
    }
    
    @Test
    public void testLabelShieldMultiline() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLabelShield.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_multiline, style);

        renderer.setContext(mc);

        BufferedImage image = RendererBaseTest.showRender("Labels and shield", renderer, TIME,
                bounds);
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/textLabelShieldMultiline.png";
        ImageAssert.assertEquals(new File(refPath), image, 1200);
    }

    @Test
    public void testLabelShieldMultilineStretch() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLabelShieldStretch.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs_multiline, style);

        renderer.setContext(mc);

        BufferedImage image = RendererBaseTest.showRender("Labels and shield", renderer, TIME,
                bounds);
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/textLabelShieldMultilineStretch.png";
        ImageAssert.assertEquals(new File(refPath), image, 1200);
    }

    @Test
    public void testOnlyShield() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textOnlyShield.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("Labels and shield, fontsize = 0", renderer, TIME, bounds);
    }

    
}
