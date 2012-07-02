package org.geotools.renderer.lite;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class PerpendicularOffsetTest {

    private static final long TIME = 8000;
    
    StreamingRenderer renderer;
    ReferencedEnvelope bounds;

    @Before
    public void initialize() throws Exception {
        renderer = new StreamingRenderer();
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        
        //System.setProperty("org.geotools.test.interactive", "true");
    }
    
    SimpleFeatureSource getData(String name) throws IOException, URISyntaxException {
    	File property = new File(TestData.getResource(this, name + ".properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        SimpleFeatureSource fs = ds.getFeatureSource(name);
        return fs;
    }
    
    void setupRenderer(String name, Style style) throws IOException, URISyntaxException {
    	MapContent mc = new MapContent();
        Layer layer = new FeatureLayer(getData(name), style);
        mc.addLayer(layer);
        renderer.setMapContent(mc);
    }
    
    @Test
    public void testDiagonalPerpendicularOffset() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "linePerpendicularOffset.sld");
        
        setupRenderer("diaglines", style);
        
        RendererBaseTest.showRender("Diagonal lines PerpendicularOffset", renderer, TIME, bounds);
    }
    
    @Test
    public void testCurvedPerpendicularOffset() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "linePerpendicularOffset.sld");
        
        setupRenderer("curvedline", style);
        
        RendererBaseTest.showRender("Curved line PerpendicularOffset", renderer, TIME, bounds);
    }
    
    /**
     * TODO: This test shows a case where the current PerpendicularOffset implementation does not produce a
     * valid result.
     */
    @Test
    public void testNarrowCurvedPerpendicularOffset() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "linePerpendicularOffsetWide.sld");
        
        setupRenderer("narrowcurvedline", style);
        
        RendererBaseTest.showRender("Narrow curved line wide PerpendicularOffset", renderer, TIME, bounds);
    }
    
}
