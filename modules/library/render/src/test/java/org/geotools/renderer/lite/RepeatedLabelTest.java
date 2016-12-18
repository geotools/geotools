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

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.test.TestData;

import junit.framework.TestCase;

public class RepeatedLabelTest extends TestCase {

    SimpleFeatureSource fs_line;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;

    @Override
    protected void setUp() throws Exception {
        File property_line = new File(TestData.getResource(this, "partialLineLabel.properties").toURI());
        PropertyDataStore ds_line = new PropertyDataStore(property_line.getParentFile());
        fs_line = ds_line.getFeatureSource("partialLineLabel");

        bounds = new ReferencedEnvelope(2, 8, 2, 8, DefaultGeographicCRS.WGS84);
    }
    
    public void testRepeatedLabel() throws Exception {
        testRepeatedLabels("repeatedLabelsLine");
    }
    
    public void testRepeatedLabelAlongLine() throws Exception {
        testRepeatedLabels("repeatedLabelsAlongLine");
    }
    
    public void testRepeatedLabelAlongLineSmall() throws Exception {
        testRepeatedLabels("repeatedLabelsAlongLineSmall");
    }

    public void testRepeatedLabels(String styleName) throws Exception {
        Style style = RendererBaseTest.loadStyle(this, styleName + ".sld");

        MapContent mc = new MapContent();
        mc.addLayer(new FeatureLayer(fs_line, style));
        mc.getViewport().setBounds(bounds);

        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);
        renderer.setJava2DHints(new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON));
        renderer.setMapContent(mc);

        BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null, 500, 500);
        File expected = new File("src/test/resources/org/geotools/renderer/lite/test-data/" + styleName + ".png");
        int tolerance = 300;
        ImageAssert.assertEquals(expected, image, tolerance);
    }

}
