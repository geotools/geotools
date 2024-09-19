/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.imageio.netcdf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.Arrays;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.coverage.io.netcdf.NetCDFReaderTest;
import org.geotools.test.TestData;
import org.hamcrest.Matchers;
import org.junit.Test;

public class NetCDFImageReaderTest {

    @Test
    public void testImageReader() throws Exception {
        File file = TestData.file(new NetCDFReaderTest(), "DUMMY.GOME2.NO2.PGL.nc");
        File aux = TestData.file(new NetCDFReaderTest(), "DUMMYGOME2_IRT.xml");
        NetCDFImageReaderSpi spi = new NetCDFImageReaderSpi();
        assertTrue(spi.canDecodeInput(file));

        NetCDFImageReader reader = (NetCDFImageReader) spi.createReaderInstance();
        reader.setAuxiliaryFilesPath(aux.getAbsolutePath());
        reader.setInput(file);

        BufferedImage image = reader.read(0);
        assertEquals(1, image.getSampleModel().getNumBands());
        assertEquals(DataBuffer.TYPE_FLOAT, image.getSampleModel().getDataType());

        // not "default", but "z"
        CoverageSlicesCatalog catalog = reader.getCatalog();
        assertThat(Arrays.asList(catalog.getTypeNames()), Matchers.contains("z"));
    }
}
