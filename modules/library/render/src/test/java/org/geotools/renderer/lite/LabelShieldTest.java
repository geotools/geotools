package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class LabelShieldTest {
    
    private static final long TIME = 1000;
    SimpleFeatureSource fs;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;

    @Before
    public void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "diaglines.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("diaglines");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        
        renderer = new StreamingRenderer();
        Map rendererParams = new HashMap();
        LabelCacheImpl labelCache = new LabelCacheImpl();
        rendererParams.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
        renderer.setRendererHints(rendererParams);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        
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
    public void testOnlyShield() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textOnlyShield.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("Labels and shield, fontsize = 0", renderer, TIME, bounds);
    }

    
}
