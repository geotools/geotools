package org.geotools.renderer.chart;

import java.io.File;

import org.geotools.data.FeatureSource;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import junit.framework.TestCase;

public class ChartRenderingTest extends TestCase {
    
    private static final long TIME = 10000;
    FeatureSource<SimpleFeatureType, SimpleFeature> fs;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;

    @Override
    protected void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "cities.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("cities");
        bounds = fs.getBounds();
        bounds.expandBy(10);
        
        renderer = new StreamingRenderer();
    }
    
    public void testPieCharts() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "pieCharts.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);
        
        renderer.setContext(mc);
        
        RendererBaseTest.showRender("Pie charts", renderer, TIME, bounds, 600, 300);
    }

}
