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
package org.geotools.renderer.lite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import org.geotools.image.test.ImageAssert;
import org.geotools.styling.Style;
import org.junit.Test;

/** Tests labels underling. */
public class LabelSpacingTest extends AbstractLabelLineTest {

    @Test
    public void testLabelsIncreaseSpacing() throws Exception {
        // load the style that will increase the char spacing
        Style style =
                loadParametricStyle(
                        this,
                        "lineStyleTemplate.sld",
                        "VENDOR_KEY",
                        "charSpacing",
                        "VENDOR_VALUE",
                        "10");
        // set the map content
        BufferedImage image = renderNonStraightLines(featureSource, style, 1000, 1000, bounds);
        // let's see if the result image match our expectations
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/lineLabelsIncreaseSpacing.png");
        ImageAssert.assertEquals(reference, image, 3000);
    }

    @Test
    public void testLabelsDecreaseSpacing() throws Exception {
        // load the style that will decrease the char spacing
        Style style =
                loadParametricStyle(
                        this,
                        "lineStyleTemplate.sld",
                        "VENDOR_KEY",
                        "charSpacing",
                        "VENDOR_VALUE",
                        "-5");
        // set the map content
        BufferedImage image = renderNonStraightLines(featureSource, style, 1000, 1000, bounds);
        // let's see if the result image match our expectations
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/lineLabelsDecreaseSpacing.png");
        ImageAssert.assertEquals(reference, image, 3000);
    }

    @Test
    public void testLabelsIncreaseWordSpacing() throws Exception {
        // load the style that will increase the word spacing in labels
        Style style =
                loadParametricStyle(
                        this,
                        "lineStyleTemplate.sld",
                        "VENDOR_KEY",
                        "wordSpacing",
                        "VENDOR_VALUE",
                        "10");
        // set the map content
        BufferedImage image = renderNonStraightLines(featureSource, style, 1000, 1000, bounds);
        // let's see if the result image match our expectations
        File reference =
                new File(
                        "./src/test/resources/org/geotools/renderer/lite/test-data/lineLabelsIncreaseWordSpacing.png");
        ImageAssert.assertEquals(reference, image, 5000);
    }
}
