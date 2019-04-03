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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.io.IOUtils;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.test.TestData;
import org.geotools.xml.styling.SLDParser;
import org.junit.Before;

/** Base class for label underline and strikethrough test */
public abstract class AbstractLabelLineTest {

    protected ReferencedEnvelope bounds;
    protected ContentFeatureSource featureSource;

    @Before
    public void setUp() throws Exception {
        RendererBaseTest.setupVeraFonts();

        // load the data, in this case a set of different linestring
        File property = new File(TestData.getResource(this, "nonStraightLines.properties").toURI());
        PropertyDataStore dataStore = new PropertyDataStore(property.getParentFile());
        featureSource = dataStore.getFeatureSource("nonStraightLines");
        // expand the bands so we can view all the labels
        bounds = featureSource.getBounds();
        bounds.expandBy(1, 1);
    }

    protected Style loadParametricStyle(Object loader, String sldFilename, String... parameters)
            throws IOException {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);

        java.net.URL url = TestData.getResource(loader, sldFilename);
        String styleTemplate = IOUtils.toString(url);
        for (int i = 0; i < parameters.length; i += 2) {
            String key = parameters[i];
            String value = parameters[i + 1];
            styleTemplate = styleTemplate.replace("%" + key + "%", value);
        }

        SLDParser stylereader = new SLDParser(factory, new StringReader(styleTemplate));

        Style style = stylereader.readXML()[0];
        return style;
    }

    protected BufferedImage renderNonStraightLines(
            SimpleFeatureSource featureSource,
            Style style,
            int width,
            int height,
            ReferencedEnvelope bounds) {
        MapContent mapContent = new MapContent();
        mapContent.addLayer(new FeatureLayer(featureSource, style));
        // instantiate and initiate the render
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mapContent);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        // create the output image and add a dark background for testing the halo
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        // render the lines with the chosen style
        renderer.paint(graphics, new Rectangle(0, 0, image.getWidth(), image.getHeight()), bounds);
        mapContent.dispose();
        return image;
    }
}
