/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.style.Style;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.test.TestData;
import org.junit.Test;

public class CurvedLabelTest extends RendererBaseTest {

    @Test
    public void testCurvedLabelsMultiLine() throws Exception {
        RendererBaseTest.setupVeraFonts();

        // load the data, in this case a set of different linestring
        File property = new File(
                TestData.getResource(this, "curvedLabelsMultiLine.properties").toURI());
        PropertyDataStore dataStore = new PropertyDataStore(property.getParentFile());
        SimpleFeatureSource featureSource = dataStore.getFeatureSource("curvedLabelsMultiLine");
        // expand the bands so we can view all the labels
        ReferencedEnvelope bounds = featureSource.getBounds();
        bounds.expandBy(1, 1);

        Style style = RendererBaseTest.loadStyle(this, "labelStyle.sld");

        MapContent mapContent = new MapContent();
        mapContent.addLayer(new FeatureLayer(featureSource, style));
        // instantiate and initiate the render
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        // create the output image and add a dark background for testing the halo
        BufferedImage image = new BufferedImage(200, 400, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        // render the lines with the chosen style
        renderer.paint(graphics, new Rectangle(0, 0, image.getWidth(), image.getHeight()), bounds);
        mapContent.dispose();

        File reference =
                new File("./src/test/resources/org/geotools/renderer/lite/test-data/curvedLabelsMultiLine.png");
        ImageAssert.assertEquals(reference, image, 1000);
    }
}
