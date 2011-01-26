package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.style.FontCache;
import org.geotools.styling.Style;
import org.geotools.test.TestData;

public class PointPlacementTest extends TestCase {
    private static final long TIME = 5000;

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

        // load font
        Font f = Font.createFont(Font.TRUETYPE_FONT, TestData.getResource(this, "recreate.ttf")
                .openStream());
        FontCache.getDefaultInstance().registerFont(f);

//        System.setProperty("org.geotools.test.interactive", "true");
    }

    public void testDefaultLabelCache() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "textAnchorRotation.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(lineFS, lStyle);
        mc.addLayer(pointFS, pStyle);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        RendererBaseTest.showRender("Old labeller", renderer, TIME, bounds);
    }

    public void testLabelCacheImpl() throws Exception {
        Style pStyle = RendererBaseTest.loadStyle(this, "textAnchorRotation.sld");
        Style lStyle = RendererBaseTest.loadStyle(this, "lineGray.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(lineFS, lStyle);
        mc.addLayer(pointFS, pStyle);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        Map rendererParams = new HashMap();
        LabelCacheImpl labelCache = new LabelCacheImpl();
        rendererParams.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
        renderer.setRendererHints(rendererParams);

        RendererBaseTest.showRender("New labeller", renderer, TIME, bounds);
    }

}
