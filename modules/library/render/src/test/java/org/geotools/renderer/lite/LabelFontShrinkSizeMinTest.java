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
package org.geotools.renderer.lite;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import junit.framework.TestCase;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Test class for verifying behaviour of displacementMode vendor option for both single layer and
 * multi-layer map
 *
 * @author nprigour
 */
public class LabelFontShrinkSizeMinTest extends TestCase {

    private static final long TIME = 5000;
    SimpleFeatureSource fs;

    ReferencedEnvelope bounds;

    @Override
    protected void setUp() throws Exception {
        RendererBaseTest.setupVeraFonts();

        bounds = new ReferencedEnvelope(0, 10, 0, 10, null);

        // System.setProperty("org.geotools.test.interactive", "true");

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("geom", Polygon.class);
        builder.add("label", String.class);
        builder.setName("labelFontShrinkSizeMin");
        SimpleFeatureType type = builder.buildFeatureType();

        GeometryFactory gf = new GeometryFactory();
        SimpleFeature f1 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPolygon(
                                    new Coordinate[] {
                                        new Coordinate(4, 7),
                                        new Coordinate(4, 9),
                                        new Coordinate(7, 9),
                                        new Coordinate(7, 7),
                                        new Coordinate(4, 7)
                                    }),
                            "labelPoly A"
                        },
                        null);
        SimpleFeature f2 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {
                            gf.createPolygon(
                                    new Coordinate[] {
                                        new Coordinate(2.5, 7),
                                        new Coordinate(2.5, 8),
                                        new Coordinate(4, 8),
                                        new Coordinate(4, 7),
                                        new Coordinate(2.5, 7)
                                    }),
                            "labelPoly B"
                        },
                        null);

        MemoryDataStore data = new MemoryDataStore();
        data.addFeature(f2);
        data.addFeature(f1);
        fs = data.getFeatureSource("labelFontShrinkSizeMin");
    }

    public void testFonstShrinkSize() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fontShrinkSize/fontShrinkSizeMin.sld");
        BufferedImage image = renderLabels(fs, style, "Label fontShrink");
        // ImageIO.write(image, "PNG", new
        // File("./src/test/resources/org/geotools/renderer/lite/test-data/fontShrinkSize/fontShrinkSizeMin.png"));
        String refPath =
                "./src/test/resources/org/geotools/renderer/lite/test-data/fontShrinkSize/fontShrinkSizeMin.png";
        ImageAssert.assertEquals(new File(refPath), image, 800);
    }

    private BufferedImage renderLabels(SimpleFeatureSource fs, Style style, String title)
            throws Exception {
        MapContent mc = new MapContent();
        mc.getViewport().setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mc.addLayer(new FeatureLayer(fs, style));

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        return RendererBaseTest.showRender(title, renderer, TIME, bounds);
    }
}
