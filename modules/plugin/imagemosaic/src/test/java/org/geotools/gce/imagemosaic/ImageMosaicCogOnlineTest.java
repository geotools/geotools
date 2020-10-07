/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.awt.*;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.sql.*;
import java.util.*;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.test.TestData;
import org.junit.Test;

/** Testing using COG remote granules on an ImageMosaic */
public class ImageMosaicCogOnlineTest extends TestCase {

    @Test
    public void testCogMosaic() throws Exception {
        final File workDir = new File(TestData.file(this, "."), "cogtest");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(TestData.file(this, "cogtest.zip"), new File(workDir, "cogtest.zip"));
        TestData.unzipFile(this, "cogtest/cogtest.zip");
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(workDir);
        GridCoverage2D coverage = reader.read(null);
        assertNotNull(coverage);
        RenderedImage image = coverage.getRenderedImage();
        int numTileX = image.getNumXTiles();
        int numTileY = image.getNumYTiles();
        Raster raster = image.getTile(numTileX / 2, numTileY / 2);
        assertEquals(512, raster.getWidth());
        assertEquals(512, raster.getHeight());
        assertEquals(1, raster.getNumBands());
        reader.dispose();
    }
}
