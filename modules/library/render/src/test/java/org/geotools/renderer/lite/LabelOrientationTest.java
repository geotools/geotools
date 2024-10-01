package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.style.Style;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class LabelOrientationTest {

    private static final long TIME = 10000;
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
        Map<Object, Object> rendererParams = new HashMap<>();
        LabelCacheImpl labelCache = new LabelCacheImpl();
        rendererParams.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
        renderer.setRendererHints(rendererParams);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        //        System.setProperty("org.geotools.test.interactive", "true");

        RendererBaseTest.setupVeraFonts();
    }

    @Test
    public void testLabelNatural() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textNaturalOrientation.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        renderer.setMapContent(mc);

        RendererBaseTest.showRender("Lines with circle stroke", renderer, TIME, bounds);
    }

    @Test
    public void testLabelLineOrientation() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLineOrientation.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        renderer.setMapContent(mc);

        RendererBaseTest.showRender("Lines with circl stroke", renderer, TIME, bounds);
    }

    @Test
    public void testOneWay() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "oneway.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        renderer.setMapContent(mc);

        BufferedImage image =
                RendererBaseTest.showRender("Lines with circl stroke", renderer, TIME, bounds);
        String refPath = "./src/test/resources/org/geotools/renderer/lite/test-data/oneway.png";
        ImageAssert.assertEquals(new File(refPath), image, 100);
    }
}
