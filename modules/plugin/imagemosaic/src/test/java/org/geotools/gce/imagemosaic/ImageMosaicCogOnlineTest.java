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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

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

    @Test
    public void testCogMosaicOverview() throws Exception {
        File workDir = new File(TestData.file(this, "."), "overview");
        workDir = new File(workDir, "cogtest");
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        FileUtils.copyFile(TestData.file(this, "cogtest.zip"), new File(workDir, "cogtest.zip"));
        TestData.unzipFile(this, "overview/cogtest/cogtest.zip");
        ImageMosaicFormat format = new ImageMosaicFormat();
        ImageMosaicReader reader = format.getReader(workDir);

        GeneralParameterValue[] params = new GeneralParameterValue[1];
        // Define a GridGeometry in order to reduce the output
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 24,
                reader.getOriginalGridRange().getSpan(1) / 24);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));
        params[0] = gg;

        GridCoverage2D coverage = reader.read(params);
        RenderedImage image = coverage.getRenderedImage();
        assertNotNull(coverage);
        int numTileX = image.getNumXTiles();
        int numTileY = image.getNumYTiles();
        Raster raster = image.getTile(numTileX / 2, numTileY / 2);
        assertEquals(512, raster.getWidth());
        assertEquals(512, raster.getHeight());
        assertEquals(1, raster.getNumBands());
        Object fileLocation =
                coverage.getProperty(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY);
        assertNotNull(fileLocation);
        assertTrue(fileLocation instanceof String);
        String path = (String) fileLocation;
        assertTrue(!path.isEmpty());
        assertTrue(path.endsWith(".ovr"));
        reader.dispose();
    }

    @Test
    public void testCogMosaicDefaultConfig() throws Exception {
        File workDir = new File(TestData.file(this, "."), "default");
        workDir = new File(workDir, "cogtest");
        if (!workDir.mkdirs()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdirs());
        }
        FileUtils.copyFile(TestData.file(this, "cogtest.zip"), new File(workDir, "cogtest.zip"));
        TestData.unzipFile(this, "default/cogtest/cogtest.zip");
        File file = new File(workDir, "cogtest.properties");
        Properties properties = new Properties();
        try (FileInputStream fin = new FileInputStream(file)) {
            properties.load(fin);
        }

        try (FileWriter fw = new FileWriter(file)) {
            assertNotNull(properties.remove("CogRangeReader"));
            assertNotNull(properties.remove("SuggestedSPI"));
            properties.store(fw, "");
        }

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
