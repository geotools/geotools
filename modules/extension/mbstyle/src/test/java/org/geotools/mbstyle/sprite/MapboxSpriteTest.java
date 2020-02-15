/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.mbstyle.sprite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static org.junit.Assert.*;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.geotools.TestData;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.renderer.style.DynamicSymbolFactoryFinder;
import org.geotools.renderer.style.ExternalGraphicFactory;
import org.geotools.styling.*;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

/**
 * Tests for {@link SpriteGraphicFactory}.
 *
 * <p>Some of the tests are perceptual. In order to display these tests as they run, uncomment the
 * following line below:
 *
 * <p>// System.setProperty("org.geotools.test.interactive", "true");
 *
 * <p>HOW TO ADD A NEW PERCEPTUAL TEST:
 *
 * <p>The first time you run a new test, the reference image must be generated. To do so, run the
 * test with <code>-Dorg.geotools.image.test.interactive=true</code>.
 */
public class MapboxSpriteTest {

    protected SpriteGraphicFactory mgf = new SpriteGraphicFactory();

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

        // UNCOMMENT THE BELOW LINE TO SHOW VISUAL TESTS
        // System.setProperty("org.geotools.test.interactive", "true");
    }

    /**
     * Test that the {@link SpriteGraphicFactory} is registered with the {@link
     * DynamicSymbolFactoryFinder}.
     */
    @Test
    public void testServiceRegistered() {
        Iterator<ExternalGraphicFactory> it =
                DynamicSymbolFactoryFinder.getExternalGraphicFactories();
        boolean foundIt = false;
        while (it.hasNext()) {
            ExternalGraphicFactory egf = it.next();
            if (egf instanceof SpriteGraphicFactory) {
                foundIt = true;
            }
        }
        assertTrue(foundIt);
    }

    /**
     * Perform a perceptual test verifying that icons can be correctly retrieved by name from a
     * sprite sheet.
     */
    @Test
    public void testRenderSpriteIcons() throws Exception {
        // Use the feature's "icon" property.
        String iconUrl = constructSpriteUrl(spriteBaseUrl, "${icon}");
        MapContent mc = new MapContent();
        ExternalGraphic eg = styleFactory.createExternalGraphic(iconUrl, "mbsprite");

        // Also add a simple background layer.
        mc.addLayer(
                new FeatureLayer(
                        bgFS,
                        SLD.wrapSymbolizers(
                                styleFactory.createPolygonSymbolizer(
                                        null,
                                        styleFactory.createFill(filterFactory.literal("#6ba3ff")),
                                        null))));

        mc.addLayer(new FeatureLayer(pointFS, pointStyleWithExternalGraphic(eg, null)));

        // Render the image and do a perceptual assert.
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image =
                MapboxTestUtils.showRender(
                        "Mapbox Sprite Icon Test",
                        renderer,
                        DISPLAY_TIME,
                        new ReferencedEnvelope[] {bounds},
                        null);
        ImageAssert.assertEquals(file("rendered-icons"), image, 50);
    }

    /**
     * Test that {@link SpriteGraphicFactory} correctly parses both the sprite base URL and the icon
     * name from External Graphic URLs.
     */
    @Test
    public void testSpriteUrlFunctions() throws MalformedURLException {
        URL url = new URL("file:/GeoServerDataDirs/release/styles/testSpritesheet#someIconName");
        assertEquals(
                "file:/GeoServerDataDirs/release/styles/testSpritesheet",
                SpriteGraphicFactory.parseBaseUrl(url).toExternalForm());
        assertEquals("someIconName", SpriteGraphicFactory.parseIconName(url));

        URL url2x =
                new URL("file:/GeoServerDataDirs/release/styles/testSpritesheet@2x#someIconName");
        assertEquals(
                "file:/GeoServerDataDirs/release/styles/testSpritesheet@2x",
                SpriteGraphicFactory.parseBaseUrl(url2x).toExternalForm());
        assertEquals("someIconName", SpriteGraphicFactory.parseIconName(url2x));
    }

    /** Test that {@link SpriteGraphicFactory} correctly fetches and parses a sprite index file. */
    @Test
    public void testParseSpriteIndexFile() throws MalformedURLException, IOException {
        SpriteIndex spriteIndex = mgf.getSpriteIndex(new URL(spriteBaseUrl));
        assertNotNull(spriteIndex);

        assertEquals(6, spriteIndex.getIcons().keySet().size());
        assertTrue(
                spriteIndex
                        .getIcons()
                        .keySet()
                        .containsAll(
                                Arrays.asList(
                                        "bomb", "face", "goldfish", "owl", "owlhead", "pattern")));

        assertEquals(32, spriteIndex.getIcon("goldfish").getHeight());
        assertEquals(32, spriteIndex.getIcon("goldfish").getWidth());
        assertEquals(1, spriteIndex.getIcon("goldfish").getPixelRatio());
        assertEquals(64, spriteIndex.getIcon("goldfish").getX());
        assertEquals(64, spriteIndex.getIcon("goldfish").getY());
    }

    /**
     * Test the parsing of additional sprite parameters from the #-fragment part of an external
     * graphic URL.
     */
    @Test
    public void testParseFragmentParameters() throws MalformedURLException {
        String urlString = "http://localhost:8080/testlocation#icon=testName&size=1.25";
        URL url = new URL(urlString);
        Map<String, String> paramsMap = SpriteGraphicFactory.parseFragmentParams(url);
        assertEquals("testName", paramsMap.get("icon"));
        assertEquals(1.25, Double.valueOf(paramsMap.get("size")), .000001);

        urlString = "http://localhost:8080/testlocation#icon=testName";
        url = new URL(urlString);
        paramsMap = SpriteGraphicFactory.parseFragmentParams(url);
        assertEquals("testName", paramsMap.get("icon"));
        assertTrue(null == paramsMap.get("size"));

        urlString = "http://localhost:8080/testlocation#size=1.25&icon=testName";
        url = new URL(urlString);
        paramsMap = SpriteGraphicFactory.parseFragmentParams(url);
        assertEquals("testName", paramsMap.get("icon"));
        assertEquals(1.25, Double.valueOf(paramsMap.get("size")), .000001);

        urlString = "http://localhost:8080/testlocation#testName";
        url = new URL(urlString);
        paramsMap = SpriteGraphicFactory.parseFragmentParams(url);
        assertEquals("testName", paramsMap.get("icon"));
        assertTrue(null == paramsMap.get("size"));
    }

    /**
     * Append a base url with #{iconName}. This parameter is used by the {@link
     * SpriteGraphicFactory} to pull the correct icon from the spritesheet.
     */
    private String constructSpriteUrl(String baseUrl, String iconName) {
        return baseUrl + "#" + iconName;
    }

    /**
     * Create a simple style with a {@link PointSymbolizer} using the provided graphic and size.
     *
     * @param eg The external graphic to use
     * @param size The size in pixels for the point symbolizer's graphic. If null, defaults to the
     *     external graphic's default size.
     */
    public Style pointStyleWithExternalGraphic(ExternalGraphic eg, String size) {
        size = size == null ? "-1" : size;
        Graphic gr =
                styleFactory.graphic(
                        Arrays.asList(eg),
                        filterFactory.literal(1),
                        filterFactory.literal(size),
                        null,
                        null,
                        null);
        Rule rule = styleFactory.createRule();
        PointSymbolizer p = styleFactory.createPointSymbolizer(gr, null);
        rule.symbolizers().add(p);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] {rule});
        Style pointStyle = styleFactory.createStyle();
        pointStyle.featureTypeStyles().addAll(Arrays.asList(fts));
        return pointStyle;
    }

    File file(String name) throws IOException {
        // The first time you run a new test, the reference image must be generated. To do so, run
        // the test with
        // -Dorg.geotools.image.test.interactive=true</code>
        return new File(
                "src/test/resources/org/geotools/mbstyle/sprite/test-data/rendered/"
                        + name
                        + ".png");
    }
}
