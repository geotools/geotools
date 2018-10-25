/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import it.geosolutions.imageio.plugins.tiff.TIFFImageReadParam;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import org.apache.commons.io.IOUtils;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.BeforeClass;
import org.junit.Test;

/** @author Nicola Lagomarsini Geosolutions */
public class ReadTypeTest {

    private static final int IMAGE_INDEX = 0;

    private static final boolean CLOSE_ELEMENTS = true;

    private static URL granuleUrl;

    private static Rectangle rasterDimensions;

    private static Hints hints = GeoTools.getDefaultHints();

    private static ImageReadParam readParameters;

    private static Rectangle rasterDimensionsWrong;

    @BeforeClass
    public static void setup() throws FileNotFoundException {
        // Definition of the url
        granuleUrl = TestData.url(ReadTypeTest.class, "img.tiff");
        // Definition of the parameters
        readParameters = new TIFFImageReadParam();
        readParameters.setSourceRegion(new Rectangle(0, 0, 40, 20));
        // Definition of the rasterDimensions
        rasterDimensions = new Rectangle(0, 0, 20, 20);
        // Wrong Raster Dimensions
        rasterDimensionsWrong = new Rectangle(41, 21, 2, 2);
    }

    @Test
    public void testJAIReadType() throws IOException {
        // Definition of the reader
        ImageReader reader = new TIFFImageReaderSpi().createReaderInstance();
        FileImageInputStream in = new FileImageInputStream(URLs.urlToFile(granuleUrl));

        try {
            reader.setInput(in);

            // Definition of the read type
            ReadType jaiImageRead = ReadType.JAI_IMAGEREAD;

            // Check if the default read type is JAI
            ReadType defaultRead = ReadType.getDefault();
            assertEquals(jaiImageRead, defaultRead);

            // Test 1 = wrong region
            RenderedImage output =
                    jaiImageRead.read(
                            readParameters,
                            IMAGE_INDEX,
                            granuleUrl,
                            rasterDimensionsWrong,
                            reader,
                            hints,
                            CLOSE_ELEMENTS);
            assertNull(output);

            // Test 2 = null URL
            output =
                    jaiImageRead.read(
                            readParameters,
                            IMAGE_INDEX,
                            null,
                            rasterDimensions,
                            reader,
                            hints,
                            CLOSE_ELEMENTS);
            assertNull(output);

            // Test 3 = null Reader
            output =
                    jaiImageRead.read(
                            readParameters,
                            IMAGE_INDEX,
                            granuleUrl,
                            rasterDimensions,
                            null,
                            hints,
                            CLOSE_ELEMENTS);
            assertNull(output);

            // Test 4 = correct
            output =
                    jaiImageRead.read(
                            readParameters,
                            IMAGE_INDEX,
                            granuleUrl,
                            rasterDimensions,
                            reader,
                            hints,
                            CLOSE_ELEMENTS);
            assertNotNull(output);
            Rectangle sourceRegion = readParameters.getSourceRegion();
            // Calculate the intersection between the raster dimension and the read parameters
            Rectangle.intersect(sourceRegion, rasterDimensions, sourceRegion);
            // Check dimensions
            assertEquals(output.getMinX(), sourceRegion.x);
            assertEquals(output.getMinY(), sourceRegion.y);
            assertEquals(output.getWidth(), sourceRegion.width);
            assertEquals(output.getHeight(), sourceRegion.height);
        } finally {
            if (in != null) {
                in.close();
            }
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    @Test
    public void testDirectReadType() throws IOException {
        // Definition of the reader

        // Definition of the read type
        ReadType directRead = ReadType.DIRECT_READ;

        // Test 1 = wrong region
        RenderedImage output =
                testRead(
                        directRead,
                        readParameters,
                        IMAGE_INDEX,
                        granuleUrl,
                        rasterDimensionsWrong,
                        hints,
                        CLOSE_ELEMENTS,
                        true);
        assertNull(output);

        // Test 2 = null URL
        output =
                testRead(
                        directRead,
                        readParameters,
                        IMAGE_INDEX,
                        null,
                        rasterDimensions,
                        hints,
                        CLOSE_ELEMENTS,
                        true);
        assertNull(output);

        // Test 3 = null Reader
        output =
                testRead(
                        directRead,
                        readParameters,
                        IMAGE_INDEX,
                        granuleUrl,
                        rasterDimensions,
                        hints,
                        CLOSE_ELEMENTS,
                        false);
        assertNull(output);

        // Test 4 = correct
        output =
                testRead(
                        directRead,
                        readParameters,
                        IMAGE_INDEX,
                        granuleUrl,
                        rasterDimensions,
                        hints,
                        CLOSE_ELEMENTS,
                        true);
        assertNotNull(output);

        Rectangle sourceRegion = readParameters.getSourceRegion();
        // Calculate the intersection between the raster dimension and the read parameters
        Rectangle.intersect(sourceRegion, rasterDimensions, sourceRegion);
        // Check dimensions
        assertEquals(output.getMinX(), sourceRegion.x);
        assertEquals(output.getMinY(), sourceRegion.y);
        assertEquals(output.getWidth(), sourceRegion.width);
        assertEquals(output.getHeight(), sourceRegion.height);
    }

    private RenderedImage testRead(
            ReadType directRead,
            ImageReadParam readParameters,
            int imageIndex,
            URL granuleUrl,
            Rectangle rasterDimensions,
            Hints hints,
            boolean closeElements,
            boolean getReader)
            throws IOException {
        ImageReader reader = null;
        FileImageInputStream in = null;
        try {
            if (getReader && granuleUrl != null) {
                in = new FileImageInputStream(URLs.urlToFile(granuleUrl));
                reader = new TIFFImageReaderSpi().createReaderInstance();
                reader.setInput(in);
            }
            return directRead.read(
                    readParameters,
                    imageIndex,
                    granuleUrl,
                    rasterDimensions,
                    reader,
                    hints,
                    closeElements);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Exception e) {
                    // silent dispose
                }
            }
            IOUtils.closeQuietly(in);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnspecifiedReadType() throws IOException {
        // Definition of the read type
        ReadType directRead = ReadType.UNSPECIFIED;

        // Try the read operation
        directRead.read(null, 0, null, null, null, null, false);
    }
}
