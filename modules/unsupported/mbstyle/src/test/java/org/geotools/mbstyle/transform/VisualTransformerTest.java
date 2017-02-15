/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.transform;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.geotools.TestData;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.referencing.CRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.geotools.renderer.lite.RendererBaseTest;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

/**
 * Perceptual tests for {@link MBStyleTransformer}.
 * 
 * In order to display the visual tests as they run, uncomment the following line below:
 * 
 * // System.setProperty("org.geotools.test.interactive", "true");
 * 
 * HOW TO ADD A NEW TEST:
 * 
 * The first time you run a new test, the reference image must be generated. To do so, run the test with
 * <code>-Dorg.geotools.image.test.interactive=true</code>.
 *
 */
public class VisualTransformerTest {

    JSONParser jsonParser = new JSONParser();

    private static final long DISPLAY_TIME = 5000;

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    SimpleFeatureSource pointFS;

    SimpleFeatureSource gridFS;

    SimpleFeatureSource bgFS;

    SimpleFeatureSource polygonFS;

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
        gridFS = ds.getFeatureSource("testgrid");
        polygonFS = ds.getFeatureSource("testpolygons");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, CRS.decode("EPSG:4326"));

        // System.setProperty("org.geotools.test.interactive", "true");
    }

    /**
     * Test generation of a GeoTools style from an MBFillLayer
     */
    @Test
    public void mbFillLayerVisualTest() throws Exception {

        // Read file to JSONObject
        JSONObject jsonObject = MapboxTestUtils.parseTestStyle("fillStyleTest.json");

        // Get the style
        MBStyle mbStyle = new MBStyle(jsonObject);
        StyledLayerDescriptor sld = new MBStyleTransformer().tranform(mbStyle);
        UserLayer l = (UserLayer) sld.layers().get(0);
        Style style = l.getUserStyles()[0];

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(polygonFS, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        BufferedImage image = showRender("Fill Test", renderer, DISPLAY_TIME,
                new ReferencedEnvelope[] { bounds }, null);
        ImageAssert.assertEquals(file("fill"), image, 50);
    }

    public Style defaultLineStyle() {
        Rule rule = styleFactory.createRule();
        Stroke stroke = styleFactory.createStroke(filterFactory.literal(Color.BLACK),
                filterFactory.literal(1));
        rule.symbolizers().add(styleFactory.createLineSymbolizer(stroke, null));
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
        Style lineStyle = styleFactory.createStyle();
        lineStyle.featureTypeStyles().addAll(Arrays.asList(fts));
        return lineStyle;
    }

    public Style defaultPointStyle() {

        Graphic gr = styleFactory.createDefaultGraphic();
        Mark mark = styleFactory.getCircleMark();
        mark.setStroke(styleFactory.createStroke(filterFactory.literal(Color.BLACK),
                filterFactory.literal(1)));
        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.BLACK)));

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(1));

        Rule rule = styleFactory.createRule();
        PointSymbolizer p = styleFactory.createPointSymbolizer(gr, null);

        rule.symbolizers().add(p);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
        Style pointStyle = styleFactory.createStyle();
        pointStyle.featureTypeStyles().addAll(Arrays.asList(fts));
        return pointStyle;
    }

    public Style defaultPolyStyle() {

        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(filterFactory.literal(Color.BLACK),
                filterFactory.literal(1), filterFactory.literal(1));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(filterFactory.literal(Color.BLACK),
                filterFactory.literal(1));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to draw the default geometry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     */
    protected static BufferedImage showRender(String testName, GTRenderer renderer, long timeOut,
            ReferencedEnvelope[] bounds, RenderListener listener) throws Exception {
        BufferedImage[] images = new BufferedImage[bounds.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = RendererBaseTest.renderImage(renderer, bounds[i], listener);
        }
        final BufferedImage image = RendererBaseTest.mergeImages(images);

        RendererBaseTest.showImage(testName, timeOut, image);
        boolean hasData = false; // All I can seem to check reliably.

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (image.getRGB(x, y) != 0) {
                    hasData = true;
                }
            }
        }

        assert (hasData);
        return image;
    }

    File file(String name) throws IOException {
        // The first time you run a new test, the reference image must be generated. To do so, run the test with
        // -Dorg.geotools.image.test.interactive=true</code>
        return new File("src/test/resources/org/geotools/mbstyle/transform/test-data/rendered/"
                + name + ".png");
    }

}
