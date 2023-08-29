package org.geotools.renderer.chart;

import java.io.File;
import org.geotools.api.style.Style;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class ChartRenderingTest {

    private static final long TIME = 10000;
    SimpleFeatureSource fs;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;

    @Before
    public void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "cities.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("cities");
        bounds = fs.getBounds();
        bounds.expandBy(10);

        renderer = new StreamingRenderer();

        // System.setProperty("org.geotools.test.interactive", "true");
    }

    @Test
    public void testPieCharts() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "pieCharts.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        renderer.setMapContent(mc);

        RendererBaseTest.showRender("Pie charts", renderer, TIME, bounds, 600, 300);
    }
}
