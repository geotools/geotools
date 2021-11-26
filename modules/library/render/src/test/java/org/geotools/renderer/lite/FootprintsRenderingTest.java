/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.ImageMosaicReaderTest;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.junit.Test;

public class FootprintsRenderingTest {

    @Test
    public void testFootprintsTransformation() throws Exception {
        File zip = new File("./target/rgb.jar");
        try (InputStream is = TestData.openStream(new ImageMosaicReaderTest(), "rgb.jar");
                OutputStream os = new FileOutputStream(zip)) {
            IOUtils.copy(is, os);
        }
        TestData.unzip(zip, new File("./target"));

        ImageMosaicReader reader = new ImageMosaicReader(new File("./target/rgb"));
        Style style = RendererBaseTest.loadStyle(this, "footprintstx.sld");
        GridReaderLayer layer = new GridReaderLayer(reader, style);

        MapContent mc = new MapContent();
        mc.addLayer(layer);
        StreamingRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(mc);

        ReferencedEnvelope bounds = mc.getViewport().getBounds();
        bounds.expandBy(bounds.getWidth() * 0.1);
        BufferedImage image = RendererBaseTest.renderImage(renderer, bounds, null);
        File expected =
                new File(
                        "src/test/resources/org/geotools/renderer/lite/test-data/footprintstx.png");
        ImageAssert.assertEquals(expected, image, 100);
    }
}
