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
package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;

import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.style.FontCache;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

/**
 * Tests labels underling.
 */
public class LabelUnderlineTest {

    private ReferencedEnvelope bounds;
    private ContentFeatureSource featureSource;

    @Before
    public void setUp() throws Exception {
        FontCache.getDefaultInstance().registerFont(
                Font.createFont(Font.TRUETYPE_FONT, TestData.getResource(this, "Vera.ttf").openStream()));
        
        // load the data, in this case a set of different linestring
        File property = new File(TestData.getResource(this, "nonStraightLines.properties").toURI());
        PropertyDataStore dataStore = new PropertyDataStore(property.getParentFile());
        featureSource = dataStore.getFeatureSource("nonStraightLines");
        // expand the bands so we can view all the labels
        bounds = featureSource.getBounds();
        bounds.expandBy(1, 1);
    }

    @Test
    public void testLabelsUnderline() throws Exception {
        // load the style that will underline the labels
        Style style = RendererBaseTest.loadStyle(this, "underlineStyle.sld");
        // set the map content
        MapContent mapContent = new MapContent();
        mapContent.addLayer(new FeatureLayer(featureSource, style));
        // instantiate and initiate the render
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        // create the output image and add a dark background for testing the halo
        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        // render the lines with the underline labels
        renderer.paint(graphics, new Rectangle(0, 0, image.getWidth(), image.getHeight()), bounds);
        // let's see if the result image match our expectations
        File reference = new File("./src/test/resources/org/geotools/renderer/lite/test-data/underlineStyle.sld.png");
        ImageAssert.assertEquals(reference, image, 3000);
    }

    @Test
    public void testLabelsUnderline_legacyAnchorPoint() throws Exception {
        System.setProperty(SLDStyleFactory.USE_LEGACY_ANCHOR_POINT_KEY, "true");
        // load the style that will underline the labels
        Style style = RendererBaseTest.loadStyle(this, "underlineStyle.sld");
        // set the map content
        MapContent mapContent = new MapContent();
        mapContent.addLayer(new FeatureLayer(featureSource, style));
        // instantiate and initiate the render
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        // create the output image and add a dark background for testing the halo
        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        // render the lines with the underline labels
        renderer.paint(graphics, new Rectangle(0, 0, image.getWidth(), image.getHeight()), bounds);
        // let's see if the result image match our expectations
        File reference = new File("./src/test/resources/org/geotools/renderer/lite/test-data/underlineStyle-legacyAnchorPoint.sld.png");
        ImageAssert.assertEquals(reference, image, 3000);
        System.clearProperty(SLDStyleFactory.USE_LEGACY_ANCHOR_POINT_KEY);
    }
    
    @Test
    public void testLabelInOrigin() throws Exception {
        MapContent mapContent = new MapContent();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        SimpleFeatureSource view = DataUtilities.createView(this.featureSource, new Query(null, ff.id(Collections.singleton(ff.featureId("Line.4")))));
        Style style = RendererBaseTest.loadStyle(this, "labelStyle.sld");
        mapContent.addLayer(new FeatureLayer(view, style));

        // instantiate and initiate the render
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        // create the output image and add a dark background for testing the halo
        BufferedImage image = new BufferedImage(768, 754, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        // render the lines with the underline labels
        ReferencedEnvelope bounds = featureSource.getBounds();
        bounds.expandBy(-1, -1);
        renderer.paint(graphics, new Rectangle(0, 0, image.getWidth(), image.getHeight()), bounds);
        // let's see if the result image match our expectations
        File reference = new File("./src/test/resources/org/geotools/renderer/lite/test-data/labelTopLeft.sld.png");
        ImageAssert.assertEquals(reference, image, 3000);
    }
}
