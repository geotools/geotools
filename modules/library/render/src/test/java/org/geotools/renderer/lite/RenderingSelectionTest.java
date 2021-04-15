package org.geotools.renderer.lite;

import java.awt.image.BufferedImage;
import java.io.File;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class RenderingSelectionTest {

    SimpleFeatureSource pointFS;

    @Before
    public void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "pointRotation.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        pointFS = ds.getFeatureSource("pointRotation");
    }

    @Test
    public void testRuleSelection() throws Exception {
        // test <VendorOption name=renderingMap>false</VendorOption> defined for rules.
        // only two light blue points should be painted (second rule)
        // the other are disabled
        Style pStyle = RendererBaseTest.loadStyle(this, "pointRenderingSelectionRule.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(pointFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        BufferedImage image =
                RendererBaseTest.showRender(
                        "Rule rendering selection",
                        renderer,
                        3000,
                        new ReferencedEnvelope(0, 10, 0, 10, CRS.decode("EPSG:4326")));
        ImageAssert.assertEquals(file("mapRenderingSelectionRule"), image, 150);
    }

    @Test
    public void testFeatureTypeStyleSelection() throws Exception {
        // test <VendorOption name=renderingMap>false</VendorOption> defined for feature type
        // styles,
        // only two red points should be painted (first feature type style).
        Style pStyle = RendererBaseTest.loadStyle(this, "pointRenderingSelectionFTS.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(pointFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        BufferedImage image =
                RendererBaseTest.showRender(
                        "FTS rendering selection",
                        renderer,
                        3000,
                        new ReferencedEnvelope(0, 10, 0, 10, CRS.decode("EPSG:4326")));
        ImageAssert.assertEquals(file("mapRenderingSelectionFTS"), image, 150);
    }

    @Test
    public void testSymbolizerStyleSelection() throws Exception {
        // test <VendorOption name=renderingMap>false</VendorOption> option in symbolizers;
        // of all the points only one should have a yellow contour around
        // for the other points the additional symbolizer is disabled
        Style pStyle = RendererBaseTest.loadStyle(this, "pointRenderingSelectionSymbolizer.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(pointFS, pStyle));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        BufferedImage image =
                RendererBaseTest.showRender(
                        "FTS rendering selection",
                        renderer,
                        3000,
                        new ReferencedEnvelope(0, 10, 0, 10, CRS.decode("EPSG:4326")));
        ImageAssert.assertEquals(file("mapRenderingSelectionSymbolizer"), image, 150);
    }

    File file(String name) {
        return new File(
                "src/test/resources/org/geotools/renderer/lite/test-data/renderingSelection/"
                        + name
                        + ".png");
    }
}
