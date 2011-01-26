package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.io.File;
import java.util.Collections;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class DrawTest {
    private static final long TIME = 20000;
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
    
    @Test
    public void testImageFill() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillHouse.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(squareFS, style);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY, true));
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("ImageFill", renderer, TIME, bounds);
    }
    
    
    @Test 
    public void testPoint() throws Exception {
            Style pStyle = RendererBaseTest.loadStyle(this, "pointHouse.sld");
            Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");
            
            DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
            mc.addLayer(lineFS, lStyle);
            mc.addLayer(pointFS, pStyle);
            
            StreamingRenderer renderer = new StreamingRenderer();
            renderer.setContext(mc);
            renderer.setRendererHints(Collections.singletonMap(StreamingRenderer.VECTOR_RENDERING_KEY, true));
            renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
            
            RendererBaseTest.showRender("Decorative marks", renderer, TIME, bounds);
   }
}
