package org.geotools.renderer.lite;

import static java.awt.RenderingHints.*;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

public class GeometryTransformationTest {
    private static final long TIME = 2000;

    SimpleFeatureSource fs;

    SimpleFeatureSource bfs;

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

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        RendererBaseTest.showRender("lineBuffer.sld", renderer, TIME, bounds);
    }

    @Test
    public void testBufferPoly() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "polyBuffer.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(bfs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        RendererBaseTest.showRender("polyBuffer.sld", renderer, TIME, bounds);
    }

    @Test
    public void testVertices() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineVertices.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        RendererBaseTest.showRender("lineVertices.sld", renderer, TIME, bounds);
    }

    @Test
    public void testStartEnd() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "lineStartEnd.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        RendererBaseTest.showRender("lineStartEnd.sld", renderer, TIME, bounds);
    }

    @Test
    public void testIsometric() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "isometric.sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(bfs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        RendererBaseTest.showRender("lineStartEnd.sld", renderer, TIME, bbounds);
    }

    @Test
    public void testOutOfThinAir() throws Exception {
        // generate a collection with just strings (but one is wkt)
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("points");
        typeBuilder.add("wkt", String.class);
        typeBuilder.add("label", String.class);
        SimpleFeatureType TYPE = typeBuilder.buildFeatureType();

        ListFeatureCollection features = new ListFeatureCollection(TYPE);
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(TYPE);
        for (int i = 0; i < 10; i++) {
            fb.add("POINT(" + i + " " + i + ")");
            fb.add("this is " + i);
            features.add(fb.buildFeature(null));
        }

        // setup a point layer with the right geometry trnasformation
        Style style = SLD.createPointStyle("circle", Color.BLUE, Color.BLUE, 1f, 10f);
        PointSymbolizer ps =
                (PointSymbolizer)
                        style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        ps.setGeometry(ff.function("convert", ff.property("wkt"), ff.literal(Point.class)));

        // setup the map
        MapContent map = new MapContent();
        Layer layer = new FeatureLayer(features, style);
        map.addLayer(layer);

        // render it
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        StreamingRenderer renderer = new StreamingRenderer();
        Graphics2D graphics = bi.createGraphics();
        renderer.setMapContent(map);
        renderer.paint(
                graphics, new Rectangle(100, 100), new ReferencedEnvelope(0, 10, 0, 10, null));
        graphics.dispose();
        map.dispose();

        // ImageIO.write(bi, "png", new File("/tmp/sample.png"));

        // check we have a diagonal set of dots
        int[] pixel = new int[3];
        for (int i = 0; i < 100; i += 10) {
            bi.getData().getPixel(i, 99 - i, pixel);
            assertEquals(0, pixel[0]);
            assertEquals(0, pixel[1]);
            assertEquals(255, pixel[2]);
        }
    }
}
