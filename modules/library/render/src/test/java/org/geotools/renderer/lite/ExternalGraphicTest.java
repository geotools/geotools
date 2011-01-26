package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;

import junit.framework.TestCase;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.Style;
import org.geotools.test.TestData;

public class ExternalGraphicTest extends TestCase {
    private static final long TIME = 3000;
    SimpleFeatureSource pointFS;
    SimpleFeatureSource lineFS;
    ReferencedEnvelope bounds;

    @Override
    protected void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "point.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        pointFS = ds.getFeatureSource("point");
        lineFS = ds.getFeatureSource("line");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        
        // System.setProperty("org.geotools.test.interactive", "true");
    }
    
    public void testExternalGraphic() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "externalGraphic.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(lineFS, lStyle);
        mc.addLayer(pointFS, pStyle);
        
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        RendererBaseTest.showRender("Decorative marks", renderer, TIME, bounds);
    }

   
}
