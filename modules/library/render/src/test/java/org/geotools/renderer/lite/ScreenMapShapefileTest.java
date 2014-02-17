package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author Sebastian Graca, ISPiK S.A.
 */
public class ScreenMapShapefileTest {

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("render-test");
        ftb.add("the_geom", Point.class, DefaultGeographicCRS.WGS84);
        ftb.setDefaultGeometry("the_geom");
        SimpleFeatureType featureType = ftb.buildFeatureType();

        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();

        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(featureType);
        fb.set("the_geom", gf.createPoint(new Coordinate(10, 10)));
        feature = fb.buildFeature(null);
    }

    @Test
    public void testFeatureCollection() throws IOException {
        SimpleFeatureSource featureSource = DataUtilities.source(new SimpleFeature[] { feature });
        checkTiles(featureSource);
    }

    @Test
    public void testShapeFile() throws Exception {
        File shpFile = new File("./target/screenMapTest/"
                + feature.getFeatureType().getName().getLocalPart() + ".shp");
        shpFile.getParentFile().mkdirs();

        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
        DataStore shapeFileDataStore = dataStoreFactory.createNewDataStore(params);
        try {
            shapeFileDataStore.createSchema(feature.getFeatureType());
            SimpleFeatureStore featureStore = (SimpleFeatureStore) shapeFileDataStore
                    .getFeatureSource(shapeFileDataStore.getTypeNames()[0]);
            featureStore.addFeatures(DataUtilities.collection(feature));

            checkTiles(featureStore);
        } finally {
            shapeFileDataStore.dispose();
        }
    }

    private static void checkTiles(SimpleFeatureSource featureSource) {
        BufferedImage untiled = renderImage(featureSource, 100, 100, new Rectangle2D.Double(5, 5,
                10, 10));
        BufferedImage tile1 = renderImage(featureSource, 50, 100, new Rectangle2D.Double(5, 5, 5,
                10));
        BufferedImage tile2 = renderImage(featureSource, 50, 100, new Rectangle2D.Double(10, 5, 5,
                10));

        assertEqualsImage(untiled.getSubimage(0, 0, 50, 100), tile1);
        assertEqualsImage(untiled.getSubimage(50, 0, 50, 100), tile2);
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

    private static BufferedImage renderImage(SimpleFeatureSource featureSource, int width,
            int height, Rectangle2D mapArea) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        Style style = createStyle();

        MapContent mapContent = new MapContent();
        MapViewport viewport = mapContent.getViewport();
        viewport.setBounds(new ReferencedEnvelope(mapArea, DefaultGeographicCRS.WGS84));
        viewport.setScreenArea(new Rectangle(width, height));
        mapContent.addLayer(new FeatureLayer(featureSource, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        renderer.paint(g, viewport.getScreenArea(), viewport.getBounds());
        return image;
    }

    private static Style createStyle() {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        URL iconUrl = ScreenMapShapefileTest.class.getResource("icon.png");
        ExternalGraphic icon = sf.createExternalGraphic(iconUrl, "image/png");
        Graphic graphic = sf.createGraphic(new ExternalGraphic[] { icon }, null, null, null, null,
                null);
        PointSymbolizer symbolizer = sf.createPointSymbolizer(graphic, "the_geom");

        Rule rule = sf.createRule();
        rule.symbolizers().add(symbolizer);

        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.rules().add(rule);

        Style style = sf.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }

    private SimpleFeature feature;
}