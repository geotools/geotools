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

import java.awt.image.BufferedImage;
import java.io.File;

import org.geotools.image.test.ImageAssert;
import org.geotools.styling.Style;
import org.junit.Test;

/**
 * Tests labels underling.
 */
public class LabelStrikethoughtTest extends AbstractLabelLineTest {

    @Test
    public void testLabelsUnderline() throws Exception {
        // load the style that will underline the labels
        Style style = RendererBaseTest.loadStyle(this, "strikethroughStyle.sld");
        // set the map content
        BufferedImage image = renderNonStraightLines(featureSource, style, 1000, 1000, bounds);
        // let's see if the result image match our expectations
        File reference = new File(
                "./src/test/resources/org/geotools/renderer/lite/test-data/strikethroughStyle.sld.png");
        ImageAssert.assertEquals(reference, image, 3000);
    }
}
