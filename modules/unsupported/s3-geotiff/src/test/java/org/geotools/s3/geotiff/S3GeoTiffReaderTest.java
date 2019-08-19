/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.s3.geotiff;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.imageio.stream.FileImageInputStream;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.image.test.ImageAssert;
import org.geotools.s3.S3ImageInputStreamImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;

/**
 * Tests for the S3GeoTiffReader and S3ImageInputStream. These are very basic and ignored for now
 * since they rely on S3 access to run.
 */
public class S3GeoTiffReaderTest {

    @Test
    @Ignore
    public void testGeotiffReader() throws IOException, URISyntaxException {
        S3GeoTiffReader reader =
                new S3GeoTiffReader(
                        new S3ImageInputStreamImpl("s3://geoserver-ec-s3/salinity.tif"));
        GridCoverage2D coverage2D = reader.read(new GeneralParameterValue[0]);
        File expectedFile = getSalinityTestFile();
        ImageAssert.assertEquals(expectedFile, coverage2D.getRenderedImage(), 15);
    }

    private File getSalinityTestFile() throws URISyntaxException {
        return new File(this.getClass().getClassLoader().getResource("salinity.tif").toURI());
    }

    /**
     * Test doing buffered reads
     *
     * @throws IOException if something goes wrong
     */
    @Test
    @Ignore
    public void testBufferingOutputStream() throws IOException, URISyntaxException {
        try (S3ImageInputStreamImpl in =
                new S3ImageInputStreamImpl("s3://geoserver-ec-s3/salinity.tif")) {
            try (FileImageInputStream fileIn = new FileImageInputStream(getSalinityTestFile())) {
                long readRemaining = fileIn.length();
                while (readRemaining > 0) {
                    long readSize = Math.min(2048L, readRemaining);
                    byte[] fileBuffer = new byte[(int) readSize];
                    byte[] s3Buffer = new byte[(int) readSize];

                    int fileBytesRead = fileIn.read(fileBuffer, 0, (int) readSize);
                    int s3BytesRead = in.read(s3Buffer, 0, (int) readSize);

                    if (!Arrays.equals(fileBuffer, s3Buffer)) {
                        // System.out.println("Arrays aren't equal");
                    }

                    assertEquals(
                            "Bytes read expected to be equal. File stream is at pos: "
                                    + (fileIn.getStreamPosition() - 1),
                            fileBytesRead,
                            s3BytesRead);
                    assertArrayEquals(
                            "Expected the byte arrays to be equals. File stream is at pos: "
                                    + (fileIn.getStreamPosition() - 1),
                            fileBuffer,
                            s3Buffer);

                    readRemaining -= fileBytesRead;
                }
            }
        }
    }

    @Test
    @Ignore
    public void testImageInputStream() throws IOException, URISyntaxException {
        S3ImageInputStreamImpl in = new S3ImageInputStreamImpl("s3://geoserver-ec-s3/salinity.tif");
        FileImageInputStream fileIn = new FileImageInputStream(getSalinityTestFile());
        int fileResult;
        int s3Result;
        while ((fileResult = fileIn.read()) > -1) {
            s3Result = in.read();
            assertEquals(
                    "S3 result must equal file result at stream position: "
                            + (fileIn.getStreamPosition() - 1),
                    fileResult,
                    s3Result);
        }

        fileIn.close();
        in.close();
    }

    @Test
    @Ignore
    public void testAnonymousS3() throws IOException {
        S3GeoTiffReader reader =
                new S3GeoTiffReader(
                        new S3ImageInputStreamImpl(
                                "s3://landsat-pds/L8/001/002/LC80010022016230LGN00/LC80010022016230LGN00_B1.TIF"
                                        + "?useAnon=true&awsRegion=US_WEST_2"));
        GridCoverage2D coverage2D = reader.read(new GeneralParameterValue[0]);
    }
}
