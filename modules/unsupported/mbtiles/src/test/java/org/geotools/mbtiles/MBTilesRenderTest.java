/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbtiles;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.util.URLs;
import org.geotools.xml.styling.SLDParser;
import org.junit.Test;

/** Checks clip masks are doing their job */
public class MBTilesRenderTest {

    @Test
    public void testRender() throws IOException {
        // get a generic style that works will all layer types
        URL styleResource = MBTilesRenderTest.class.getResource("generic.sld");
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        StyledLayerDescriptor sld = new SLDParser(styleFactory, styleResource).parseSLD();
        Style style = ((NamedLayer) sld.getStyledLayers()[0]).getStyles()[0];

        MapContent mc = new MapContent();
        File file = URLs.urlToFile(getClass().getResource("madagascar.mbtiles"));
        MBTilesDataStore store = new MBTilesDataStore(new MBTilesFile(file));
        for (String typeName : store.getTypeNames()) {
            ContentFeatureSource fs = store.getFeatureSource(typeName);
            mc.addLayer(new FeatureLayer(fs, style));
        }

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));

        int w = 300;
        int h = 500;
        final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        renderer.paint(
                g,
                new Rectangle(0, 0, w, h),
                new ReferencedEnvelope(
                        4700000, 5700000, -3000000, -1300000, MBTilesDataStore.DEFAULT_CRS));
        g.dispose();

        File expected = new File("src/test/resources/org/geotools/mbtiles/madagascar.png");
        ImageAssert.assertEquals(expected, image, (int) (w * h * 0.05));
    }
}
