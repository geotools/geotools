/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2015, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class RepeatedLabelTest {

    SimpleFeatureSource fs_line;
    SimpleFeatureSource square;
    SimpleFeatureSource squareHoles;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;

    @Before
    public void setUp() throws Exception {
        File property_line =
                new File(TestData.getResource(this, "partialLineLabel.properties").toURI());
        PropertyDataStore ds_line = new PropertyDataStore(property_line.getParentFile());
        fs_line = ds_line.getFeatureSource("partialLineLabel");
        square = ds_line.getFeatureSource("square");
        squareHoles = ds_line.getFeatureSource("square-hole");

        bounds = new ReferencedEnvelope(2, 8, 2, 8, DefaultGeographicCRS.WGS84);

        RendererBaseTest.setupVeraFonts();
    }

    @Test
    public void testRepeatedLabel() throws Exception {
        checkRepeatedLabels("repeatedLabelsLine");
    }

    @Test
    public void testRepeatedLabelAlongLine() throws Exception {
        checkRepeatedLabels("repeatedLabelsAlongLine");
    }

    @Test
    public void testRepeatedLabelAlongLineSmall() throws Exception {
        checkRepeatedLabels("repeatedLabelsAlongLineSmall");
    }

    private void checkRepeatedLabels(String styleName) throws Exception {
        Style style = RendererBaseTest.loadStyle(this, styleName + ".sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs_line, style));
        mc.getViewport().setBounds(bounds);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null, 500, 500);
        File expected =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/test-data/"
                                + styleName
                                + ".png");
        int tolerance = 2000;
        ImageAssert.assertEquals(expected, image, tolerance);
    }

    @Test
    public void testLabelSquareBorders() throws Exception {
        checkRepeatedLabelsPolygonBorder(square, "repeatedLabelsAlongLine", "poly", null, 1600);
    }

    @Test
    public void testLabelSquareBordersWithHoles() throws Exception {
        checkRepeatedLabelsPolygonBorder(
                squareHoles, "repeatedLabelsAlongLine", "polyHole", null, 2100);
    }

    @Test
    public void testLabelSquareBordersPositiveOffset() throws Exception {
        PerpendicularOffsetVisitor visitor = new PerpendicularOffsetVisitor(10);
        checkRepeatedLabelsPolygonBorder(
                square, "repeatedLabelsAlongLine", "poly-perp-offset", visitor, 1700);
    }

    @Test
    public void testLabelSquareBordersNegativeOffset() throws Exception {
        PerpendicularOffsetVisitor visitor = new PerpendicularOffsetVisitor(-10);
        checkRepeatedLabelsPolygonBorder(
                square, "repeatedLabelsAlongLine", "poly-perp-negative-offset", visitor, 1500);
    }

    @Test
    public void testLabelSquareBordersHolesPositiveOffset() throws Exception {
        PerpendicularOffsetVisitor visitor = new PerpendicularOffsetVisitor(5);
        checkRepeatedLabelsPolygonBorder(
                squareHoles, "repeatedLabelsAlongLine", "poly-hole-perp-offset", visitor, 2100);
    }

    @Test
    public void testLabelSquareBordersHoleNegativeOffset() throws Exception {
        PerpendicularOffsetVisitor visitor = new PerpendicularOffsetVisitor(-5);
        checkRepeatedLabelsPolygonBorder(
                squareHoles,
                "repeatedLabelsAlongLine",
                "poly-hole-perp-negative-offset",
                visitor,
                2200);
    }

    private void checkRepeatedLabelsPolygonBorder(
            SimpleFeatureSource features,
            String styleName,
            String testImageSuffix,
            DuplicatingStyleVisitor styleVisitor,
            int tolerance)
            throws Exception {
        Style style = RendererBaseTest.loadStyle(this, styleName + ".sld");
        if (styleVisitor != null) {
            style.accept(styleVisitor);
            style = (Style) styleVisitor.getCopy();
        }

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(features, style));
        ReferencedEnvelope bounds = features.getBounds();
        bounds.expandBy(bounds.getWidth() / 10);
        mc.getViewport().setBounds(bounds);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null, 500, 500);
        File expected =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/test-data/"
                                + styleName
                                + "-"
                                + testImageSuffix
                                + ".png");
        ImageAssert.assertEquals(expected, image, tolerance);
    }
}
