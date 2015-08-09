package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @source $URL$
 */
public class ZOrderTest {
    private static final long TIME = 40000;

    private static final int THRESHOLD = 100;

    SimpleFeatureSource fs;

    ReferencedEnvelope bounds;

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "zorder/zsquares.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("zsquares");
        bounds = fs.getBounds();
        bounds.expandBy(0.2, 0.2);

        // System.setProperty("org.geotools.test.interactive", "true");

    }
    
    private void runSingleLayerTest(String styleName, String sortBy, String referenceName)
            throws Exception {
        Style style = RendererBaseTest.loadStyle(this, styleName);
        style.featureTypeStyles().get(0).getOptions().put(FeatureTypeStyle.SORT_BY, sortBy);

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        
        BufferedImage image = RendererBaseTest.showRender(styleName, renderer, TIME, bounds);
        File reference = new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/zorder/"
                + referenceName + ".png");
        ImageAssert.assertEquals(reference, image, THRESHOLD);
    }

    @Test
    public void testZAscending() throws Exception {
        runSingleLayerTest("zorder/zpolygon.sld", "z", "z-ascending");
    }

    @Test
    public void testZDescending() throws Exception {
        runSingleLayerTest("zorder/zpolygon.sld", "z D", "z-descending");
    }

    @Test
    public void testCatDescendingZDescending() throws Exception {
        runSingleLayerTest("zorder/zpolygon.sld", "cat D,z D", "cat-desc-z-desc");
    }

    @Test
    public void testCatDescendingZAscending() throws Exception {
        runSingleLayerTest("zorder/zpolygon.sld", "cat D,z A", "cat-desc-z-asc");
    }


}
