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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.styling.Style;
import org.geotools.styling.TextSymbolizer;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

/** Tests labels underling. */
public class LabelUnderlineTest extends AbstractLabelLineTest {

    @Test
    public void testLabelsUnderline() throws Exception {
        // load the style that will underline the labels
        Style style =
                loadParametricStyle(
                        this,
                        "lineStyleTemplate.sld",
                        "VENDOR_KEY",
                        TextSymbolizer.UNDERLINE_TEXT_KEY,
                        "VENDOR_VALUE",
                        "true");
        // set the map content
        BufferedImage image = renderNonStraightLines(featureSource, style, 1000, 1000, bounds);
        // let's see if the result image match our expectations
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/underlineStyle.sld.png");
        ImageAssert.assertEquals(reference, image, 4900);
    }

    @Test
    public void testLabelsUnderline_legacyAnchorPoint() throws Exception {
        System.setProperty(SLDStyleFactory.USE_LEGACY_ANCHOR_POINT_KEY, "true");
        // load the style that will underline the labels
        Style style =
                loadParametricStyle(
                        this,
                        "lineStyleTemplate.sld",
                        "VENDOR_KEY",
                        TextSymbolizer.UNDERLINE_TEXT_KEY,
                        "VENDOR_VALUE",
                        "true");
        BufferedImage image = renderNonStraightLines(featureSource, style, 1000, 1000, bounds);
        // let's see if the result image match our expectations
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/underlineStyle-legacyAnchorPoint.sld.png");
        ImageAssert.assertEquals(reference, image, 4900);
        System.clearProperty(SLDStyleFactory.USE_LEGACY_ANCHOR_POINT_KEY);
    }

    @Test
    public void testLabelInOrigin() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        SimpleFeatureSource view =
                DataUtilities.createView(
                        this.featureSource,
                        new Query(null, ff.id(Collections.singleton(ff.featureId("Line.4")))));
        Style style = RendererBaseTest.loadStyle(this, "labelStyle.sld");
        ReferencedEnvelope bounds = featureSource.getBounds();
        bounds.expandBy(-1, -1);
        BufferedImage image = renderNonStraightLines(view, style, 768, 754, bounds);
        // let's see if the result image match our expectations
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/labelTopLeft.sld.png");
        ImageAssert.assertEquals(reference, image, 3000);
    }

    @Test
    @Ignore // with JDK11 the build server has the labels with different spacing and position, but
    // it's impossible to debug... removing the test
    public void testLabelsUnderlineWithOffset() throws Exception {
        // load the style that will underline the labels
        Style style =
                loadParametricStyle(
                        this,
                        "lineStyleTemplate.sld",
                        "VENDOR_KEY",
                        TextSymbolizer.UNDERLINE_TEXT_KEY,
                        "VENDOR_VALUE",
                        "true");
        Style offsetStyle = PerpendicularOffsetVisitor.apply(style, 15);
        // set the map content
        BufferedImage image =
                renderNonStraightLines(featureSource, offsetStyle, 1000, 1000, bounds);
        // let's see if the result image match our expectations
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/underlineOffsetStyle.sld.png");
        ImageAssert.assertEquals(reference, image, 3600);
    }

    @Test
    @Ignore // with JDK11 the build server has the labels with different spacing and position, but
    // it's impossible to debug... removing the test
    public void testLabelsUnderlineWithNegativeOffset() throws Exception {
        // load the style that will underline the labels
        Style style =
                loadParametricStyle(
                        this,
                        "lineStyleTemplate.sld",
                        "VENDOR_KEY",
                        TextSymbolizer.UNDERLINE_TEXT_KEY,
                        "VENDOR_VALUE",
                        "true");
        Style offsetStyle = PerpendicularOffsetVisitor.apply(style, -15);
        // set the map content
        BufferedImage image =
                renderNonStraightLines(featureSource, offsetStyle, 1000, 1000, bounds);
        // let's see if the result image match our expectations
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/underlineNegativeOffsetStyle.sld.png");
        ImageAssert.assertEquals(reference, image, 4800);
    }
}
