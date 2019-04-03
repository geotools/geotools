package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LabelShieldTest {

    private static final long TIME = 1000;
    SimpleFeatureSource fs;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;

    SimpleFeatureSource fs_multiline;
    ContentFeatureSource pointShield;

    @BeforeClass
    public static void prepareCRS() {
        CRS.reset("all");
    }

    @Before
    public void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "diaglines.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("diaglines");
        fs_multiline = ds.getFeatureSource("diaglines_multiline");
        pointShield = ds.getFeatureSource("point_shield");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        renderer = new StreamingRenderer();
        Map rendererParams = new HashMap();
        LabelCacheImpl labelCache = new LabelCacheImpl();
        rendererParams.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
        renderer.setRendererHints(rendererParams);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        RendererBaseTest.setupVeraFonts();
        // System.setProperty("org.geotools.test.interactive", "true");
    }

    @Test
    public void testLabelShield() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLabelShield.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));
        renderer.setMapContent(mc);

        RendererBaseTest.showRender("Labels and shield", renderer, TIME, bounds);
    }

    @Test
    public void testPointShieldUnderTheLine() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLabelShieldStretch2.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(pointShield, style));

        renderer.setMapContent(mc);
        final ReferencedEnvelope pointBounds = pointShield.getBounds();
        pointBounds.expandBy(3, 3);
        BufferedImage image =
                RendererBaseTest.showRender("Text under the line", renderer, TIME, pointBounds);

        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textLabelShieldUnderTheLine.png";
        ImageAssert.assertEquals(new File(refPath), image, 1200);
    }

    @Test
    public void testLabelShieldMultiline() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLabelShield.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs_multiline, style));

        renderer.setMapContent(mc);

        BufferedImage image =
                RendererBaseTest.showRender("Labels and shield", renderer, TIME, bounds);
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textLabelShieldMultiline.png";
        ImageAssert.assertEquals(new File(refPath), image, 1200);
    }

    @Test
    public void testLabelShieldMultilineStretch() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textLabelShieldStretch.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs_multiline, style));

        renderer.setMapContent(mc);

        BufferedImage image =
                RendererBaseTest.showRender("Labels and shield", renderer, TIME, bounds);
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/textLabelShieldMultilineStretch.png";
        ImageAssert.assertEquals(new File(refPath), image, 1200);
    }

    @Test
    public void testOnlyShield() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "textOnlyShield.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        renderer.setMapContent(mc);

        RendererBaseTest.showRender("Labels and shield, fontsize = 0", renderer, TIME, bounds);
    }
}
