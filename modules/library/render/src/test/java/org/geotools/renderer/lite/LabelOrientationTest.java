package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.styling.Style;
import org.geotools.test.TestData;

public class LabelOrientationTest extends TestCase {

    private static final long TIME = 10000;
    SimpleFeatureSource fs;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;

    @Override
    protected void setUp() throws Exception {
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

        
//        System.setProperty("org.geotools.test.interactive", "true");
    }
    
    public void testLabelNatural() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textNaturalOrientation.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("Lines with circl stroke", renderer, TIME, bounds);
    }
    
    public void testLabelLineOrientation() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLineOrientation.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("Lines with circl stroke", renderer, TIME, bounds);
    }
    
    
}
