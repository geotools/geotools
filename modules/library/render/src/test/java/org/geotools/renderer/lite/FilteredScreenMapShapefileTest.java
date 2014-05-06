package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class FilteredScreenMapShapefileTest {

    private static final double X = -112.0033;
    private static final double Y = 33.6256;
    private static final double WIDTH = 0.0002;
    private static final double HEIGHT = WIDTH;
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 50;
    private static final int BLACK_X = 50;
    private static final int BLACK_Y = 25;
    
    private FeatureSource<?, ?> src;
    private BufferedImage expected;
    
    @Before
    public void setUp() throws Exception {
        // load shapefile
        FileDataStore store = new ShapefileDataStoreFactory().createDataStore(TestData.getResource(this, "filter-coincident-points.shp"));
        src = store.getFeatureSource();
        expected = renderImage(null);
        
        // confirm that unfiltered rendering is as expected, and then use as expected for tests
        for (int i = 0; i < IMAGE_WIDTH; i++) {
            for (int j = 0; j < IMAGE_HEIGHT; j++) {
                RendererBaseTest.assertPixel(expected, i, j, i == BLACK_X && j == BLACK_Y ? Color.BLACK : Color.WHITE);
            }
        }
    }

    @Test
    public void testCoincidentPointFilteringBackwardCompatible() throws Exception {
        System.clearProperty("filterBeforeScreenMap");
        
        // point with NAME = 'a' renders
        BufferedImage actual = renderImage("NAME='a'");
        assertEqualsImage(expected, actual);

        // point with NAME = 'b' is preemptively filtered out by screen map and does not render 
        actual = renderImage("NAME='b'");

        // confirm that point named 'b' is not rendered
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 50; j++) {
                RendererBaseTest.assertPixel(actual, i, j, Color.WHITE);
            }
        }
    }

    @Test
    public void testCoincidentPointFilterBeforeScreenMap() throws Exception {
        System.setProperty("filterBeforeScreenMap", "true");
        
        // point with NAME = 'a' renders
        BufferedImage actual = renderImage("NAME='a'");

        assertEqualsImage(expected, actual);

        // point with NAME = 'b' should not be filtered out by screen map and does render 
        actual = renderImage("NAME='b'");

        assertEqualsImage(expected, actual);
    }

    private static void assertEqualsImage(BufferedImage expected, BufferedImage actual) {
        assertEquals(expected.getWidth(), actual.getWidth());
        assertEquals(expected.getHeight(), actual.getHeight());
        for (int y = 0; y < expected.getHeight(); ++y) {
            for (int x = 0; x < expected.getWidth(); ++x) {
                int expectedRgb = expected.getRGB(x, y);
                int actualRgb = actual.getRGB(x, y);
                assertEquals("[" + y + ", " + x + "]", new Color(expectedRgb), new Color(actualRgb));
            }
        }
    }

    private BufferedImage renderImage(String cql) throws Exception {
        int width = IMAGE_WIDTH;
        int height = IMAGE_HEIGHT;
        Rectangle2D mapArea = new Rectangle2D.Double(X, Y, WIDTH, HEIGHT);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        Style style = RendererBaseTest.loadStyle(this, "filter-coincident-points.sld");

        MapContent mapContent = new MapContent();
        MapViewport viewport = mapContent.getViewport();
        viewport.setBounds(new ReferencedEnvelope(mapArea, DefaultGeographicCRS.WGS84));
        viewport.setScreenArea(new Rectangle(width, height));
        FeatureLayer layer = new FeatureLayer(src, style);
        if (cql != null) {
            Query query = new Query();
            query.setFilter(CQL.toFilter(cql));
            layer.setQuery(query);
        }
        mapContent.addLayer(layer);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        renderer.paint(g, viewport.getScreenArea(), viewport.getBounds());
        return image;
    }

}