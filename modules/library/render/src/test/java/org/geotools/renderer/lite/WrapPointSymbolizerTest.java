package org.geotools.renderer.lite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class WrapPointSymbolizerTest {
    private static final long TIME = 4000;
    SimpleFeatureSource fs;

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "bigsquare.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("bigsquare");
    }
    
    @Test
    public void testWrapPointSymbolizer() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "pointPoly.sld");
        
        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));
        ReferencedEnvelope bounds = new ReferencedEnvelope(0, 370, 0, 10, DefaultGeographicCRS.WGS84);
        mc.getViewport().setBounds(bounds);
        
        StreamingRenderer renderer = new StreamingRenderer();
        Map<Object, Object> rendererParams = new HashMap<Object, Object>();
        rendererParams.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        rendererParams.put(StreamingRenderer.CONTINUOUS_MAP_WRAPPING, true);
        renderer.setRendererHints(rendererParams);
        renderer.setMapContent(mc);
        
        // RendererBaseTest.showRender("WrapPointSymbolizer", renderer, TIME, bounds);
        
        final BufferedImage image = new BufferedImage(400, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 400, 80);
        renderer.paint(g, new Rectangle(0, 0, 400, 80), bounds);
        
        // RendererBaseTest.showImage("WrapPointSymbolizer", TIME, image);
        
        // the first instance, black polygon and red central point
        RendererBaseTest.assertPixel(image, 5, 0, Color.BLACK);
        RendererBaseTest.assertPixel(image, 5, 40, Color.RED);
        
        // the second instance, black polygon and red central point
        RendererBaseTest.assertPixel(image, 395, 0, Color.BLACK);
        RendererBaseTest.assertPixel(image, 395, 40, Color.RED);
    }

}
