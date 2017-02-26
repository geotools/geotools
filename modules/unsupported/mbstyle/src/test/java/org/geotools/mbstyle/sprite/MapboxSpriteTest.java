package org.geotools.mbstyle.sprite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.geotools.TestData;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.transform.MBStyleTransformer;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.renderer.style.DynamicSymbolFactoryFinder;
import org.geotools.renderer.style.ExternalGraphicFactory;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

/**
 * Tests for {@link MapboxGraphicFactory}.
 * 
 * Some of the tests are perceptual. In order to display these tests as they run, uncomment the following line below:
 * 
 * // System.setProperty("org.geotools.test.interactive", "true");
 * 
 * HOW TO ADD A NEW PERCEPTUAL TEST:
 * 
 * The first time you run a new test, the reference image must be generated. To do so, run the test with
 * <code>-Dorg.geotools.image.test.interactive=true</code>.
 *
 */
public class MapboxSpriteTest {

    protected MapboxGraphicFactory mgf = new MapboxGraphicFactory();

    JSONParser jsonParser = new JSONParser();

    private static final long DISPLAY_TIME = 5000;

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    String spriteBaseUrl;

    SimpleFeatureSource pointFS;

    SimpleFeatureSource bgFS;

    ReferencedEnvelope bounds;

    @BeforeClass
    public static void setupClass() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @Before
    public void setUp() throws Exception {
        File property = new File(TestData.getResource(this, "testpoints.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        pointFS = ds.getFeatureSource("testpoints");
        bgFS = ds.getFeatureSource("background");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, CRS.decode("EPSG:4326"));

        // The sprite base url should not have an extension.
        URL url = this.getClass().getResource("test-data/sprite.png");
        String urlStr = url.toExternalForm();
        spriteBaseUrl = urlStr.substring(0, urlStr.lastIndexOf(".png"));

        // System.setProperty("org.geotools.test.interactive", "true");
    }

    /**
     * Test that the {@link MapboxGraphicFactory} is registered with the {@link DynamicSymbolFactoryFinder}.
     */
    @Test
    public void testServiceRegistered() {
        Iterator<ExternalGraphicFactory> it = DynamicSymbolFactoryFinder
                .getExternalGraphicFactories();
        boolean foundIt = false;
        while (it.hasNext()) {
            ExternalGraphicFactory egf = it.next();
            if (egf instanceof MapboxGraphicFactory) {
                foundIt = true;
            }
        }
        assertTrue(foundIt);
    }

    /**
     * 
     * Perform a perceptual test verifying that icons can be correctly retrieved by name from a sprite sheet.
     */
    @Test
    public void testRenderSpriteIcons() throws Exception {
        // Use the feature's "icon" property.
        String iconUrl = constructSpriteUrl(spriteBaseUrl, "${icon}");
        MapContent mc = new MapContent();
        ExternalGraphic eg = styleFactory.createExternalGraphic(iconUrl, "mbsprite");
        
        
        // Also add a simple background layer.
        mc.addLayer(
                new FeatureLayer(bgFS,
                        SLD.wrapSymbolizers(styleFactory.createPolygonSymbolizer(null,
                                styleFactory.createFill(
                                        filterFactory.literal("#6ba3ff")),
                                null))));
        
        mc.addLayer(new FeatureLayer(pointFS, pointStyleWithExternalGraphic(eg, null)));
               
        // Render the image and do a perceptual assert.
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = MapboxTestUtils.showRender("Mapbox Sprite Icon Test", renderer,
                DISPLAY_TIME, new ReferencedEnvelope[] { bounds }, null);
        ImageAssert.assertEquals(file("rendered-icons"), image, 50);
    }  

    /**
     * Append a base url with ?icon={iconName}. This parameter is used by the {@link MapboxGraphicFactory} to pull the correct icon from the
     * spritesheet.
     */
    private String constructSpriteUrl(String baseUrl, String iconName) {
        return baseUrl + "?icon=" + iconName;
    }

    /**
     * Create a simple style with a {@link PointSymbolizer} using the provided graphic and size.
     * 
     * @param eg The external graphic to use 
     * @param size The size in pixels for the point symbolizer's graphic. If null, defaults to the external graphic's default size.
     */
    public Style pointStyleWithExternalGraphic(ExternalGraphic eg, String size) {
        Graphic gr = styleFactory.graphic(Arrays.asList(eg), filterFactory.literal(1),
                filterFactory.literal(size), null, null, null);
        Rule rule = styleFactory.createRule();
        PointSymbolizer p = styleFactory.createPointSymbolizer(gr, null);
        rule.symbolizers().add(p);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
        Style pointStyle = styleFactory.createStyle();
        pointStyle.featureTypeStyles().addAll(Arrays.asList(fts));
        return pointStyle;
    }
    
    File file(String name) throws IOException {
        // The first time you run a new test, the reference image must be generated. To do so, run the test with
        // -Dorg.geotools.image.test.interactive=true</code>
        return new File("src/test/resources/org/geotools/mbstyle/sprite/test-data/rendered/"
                + name + ".png");
    }
}
