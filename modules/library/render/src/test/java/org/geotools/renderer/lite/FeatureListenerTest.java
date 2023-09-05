package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.style.Style;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class FeatureListenerTest {

    SimpleFeatureSource squareFS;
    ReferencedEnvelope bounds;

    @Before
    public void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "square.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        squareFS = ds.getFeatureSource("square");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
    }

    @Test
    public void testTwoRulesCatchAll() throws Exception {
        testFeatureCount("fillSolidTwoRules.sld", 2);
    }

    @Test
    public void testTwoRulesElseCatchAll() throws Exception {
        testFeatureCount("fillSolidTwoRuleElse.sld", 2);
    }

    protected void testFeatureCount(String sldFilename, int expectedCount) throws Exception {
        Style style = RendererBaseTest.loadStyle(this, sldFilename);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(squareFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        AtomicInteger count = new AtomicInteger();
        renderer.addRenderListener(
                new RenderListener() {

                    @Override
                    public void featureRenderer(SimpleFeature feature) {
                        count.incrementAndGet();
                    }

                    @Override
                    public void errorOccurred(Exception e) {}
                });

        RendererBaseTest.renderImage(renderer, bounds, null);
        mc.dispose();
        assertEquals(expectedCount, count.get());
    }
}
