package org.geotools.renderer.lite;

import static java.awt.RenderingHints.*;

import java.awt.RenderingHints;
import java.io.File;

import org.geotools.data.FeatureSource;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeometryTransformationTest {
    private static final long TIME = 20000;

    FeatureSource<SimpleFeatureType, SimpleFeature> fs;

    FeatureSource<SimpleFeatureType, SimpleFeature> bfs;

    ReferencedEnvelope bounds;
    ReferencedEnvelope bbounds;

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "line.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        fs = ds.getFeatureSource("line");
        bounds = fs.getBounds();
        bounds.expandBy(3, 3);
        
        bfs = ds.getFeatureSource("buildings");
        bbounds = bfs.getBounds();
        bbounds.expandBy(3, 3);
        
        // System.setProperty("org.geotools.test.interactive", "true");

    }

    @Test
    public void testBufferLine() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineBuffer.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);

        RendererBaseTest.showRender("lineBuffer.sld", renderer, TIME, bounds);
    }
    
    @Test
    public void testVertices() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineVertices.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);

        RendererBaseTest.showRender("lineVertices.sld", renderer, TIME, bounds);
    }
    
    @Test
    public void testStartEnd() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineStartEnd.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(fs, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);

        RendererBaseTest.showRender("lineStartEnd.sld", renderer, TIME, bounds);
    }
    
    @Test
    public void testIsometric() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "isometric.sld");

        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(bfs, style);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setContext(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        RendererBaseTest.showRender("lineStartEnd.sld", renderer, TIME, bbounds);
    }
}
