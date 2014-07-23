/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.grib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.spi.ImageReaderSpi;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.io.netcdf.NetCDFDriver;
import org.geotools.coverage.io.netcdf.NetCDFReader;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.imageio.netcdf.NetCDFImageReaderSpi;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

/**
 * Unit test for testing Grib data.
 */
public class GribTest {

    /**
     * Test if the Grib format is accepted by the NetCDF reader
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public void testGribFormat() throws FileNotFoundException, IOException {
        // Selection of the input file
        final File file = TestData.file(this, "sampleGrib.grb2");
        // Creation of a NetCDFFormat object for checking if the GRIB format is accepted
        GRIBFormat format = new GRIBFormat();
        // Check if the format is accepted
        Assert.assertTrue(format.accepts(file));
        // Creation of a NetCDF reader SPI object for checking if it can decode the input file
        ImageReaderSpi spi = new NetCDFImageReaderSpi();
        // Check if the file can be decoded
        Assert.assertTrue(spi.canDecodeInput(file));

        // Check not a grib file
        final File fileTif = TestData.file(this, "sample.tif");
        Assert.assertFalse(format.accepts(fileTif));
    }

    /**
     * Test if the Grib extension is supported by the NetCDF reader
     * 
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public void testGribExtension() {
        // Creation of a NetCDFDriver
        NetCDFDriver driver = new NetCDFDriver();
        // Selection of the extensions used by the NetCDF driver
        List<String> extensions = driver.getFileExtensions();
        // Creation of a list of the grib extensions
        List<String> possibleExt = Arrays.asList(new String[] { "grb", "grb2", "grib" });
        // Check if the extensions are contained
        Assert.assertTrue(extensions.containsAll(possibleExt));

        // Creation of a NetCDFReaderSPI objectfor checking if it supports the GRIB extension
        ImageReaderSpi spi = new NetCDFImageReaderSpi();
        // Selection of the suffixes, formatNames and mimetypes from the spi object
        List<String> suffixes = Arrays.asList(spi.getFileSuffixes());
        List<String> formatNames = Arrays.asList(spi.getFormatNames());
        List<String> MIMETypes = Arrays.asList(spi.getMIMETypes());
        // Creation of similar lists containing the values for the grib format
        List<String> gribSuffixes = Arrays.asList(new String[] { "grib", "grb", "grb2" });
        List<String> gribFormatNames = Arrays.asList(new String[] { "grib", "grib2", "GRIB",
                "GRIB2" });
        List<String> gribMIMETypes = Arrays.asList(new String[] { "application/octet-stream" });
        // Check if the grib informations are present
        Assert.assertTrue(suffixes.containsAll(gribSuffixes));
        Assert.assertTrue(formatNames.containsAll(gribFormatNames));
        Assert.assertTrue(MIMETypes.containsAll(gribMIMETypes));
    }

    /**
     * Test on a Grib image.
     * 
     * @throws DataSourceException
     * @throws MalformedURLException
     * @throws IOException
     */
    @Test
    public void testGribImage() throws MalformedURLException, IOException {
        // Selection of the input file
        final File file = TestData.file(this, "sampleGrib.grb2");
        // Testing the 2 points
        testGribFile(file, new Point2D.Double(-56, 8), new Point2D.Double(-56, 4));
    }

    /**
     * Private method for checking if the selected point are nodata or not. This ensures that the images are flipped vertically only if needed. If the
     * image is not correctly flipped then, validpoint and nodatapoint should be inverted.
     * 
     * @param inputFile
     * @param validPoint
     * @param nodataPoint
     * @throws MalformedURLException
     * @throws DataSourceException
     * @throws IOException
     */
    private void testGribFile(final File inputFile, Point2D validPoint, Point2D nodataPoint)
            throws MalformedURLException, IOException {
        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(
                inputFile.toURI().toURL(), null);
        final NetCDFReader reader = new NetCDFReader(inputFile, null);
        Assert.assertNotNull(format);
        Assert.assertNotNull(reader);
        try {
            // Selection of all the Coverage names
            String[] names = reader.getGridCoverageNames();
            Assert.assertNotNull(names);
            // Selections of one Coverage
            GridCoverage2D grid = reader.read(names[0], null);
            Assert.assertNotNull(grid);
            // Selection of one coordinate from the Coverage and check if the
            // value is not a NaN
            float[] result = new float[1];
            grid.evaluate(validPoint, result);
            Assert.assertTrue(!Float.isNaN(result[0]));
            // Selection of one coordinate from the Coverage and check if the
            // value is NaN
            float[] result_2 = new float[1];
            grid.evaluate(nodataPoint, result_2);
            Assert.assertTrue(Float.isNaN(result_2[0]));
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    /**
     * Test on a Grib image asking for a larger bounding box.
     * 
     * @throws DataSourceException
     * @throws MalformedURLException
     * @throws IOException
     */
    @Test
    public void testGribImageWithLargeBBOX() throws MalformedURLException, IOException {
        // Selection of the input file
        final File inputFile = TestData.file(this, "sampleGrib.grb2");
        // Get format
        final AbstractGridFormat format = (AbstractGridFormat) GridFormatFinder.findFormat(
                inputFile.toURI().toURL(), null);
        final NetCDFReader reader = new NetCDFReader(inputFile, null);
        Assert.assertNotNull(format);
        Assert.assertNotNull(reader);
        try {
            // Selection of all the Coverage names
            String[] names = reader.getGridCoverageNames();
            Assert.assertNotNull(names);

            // Name of the first coverage
            String coverageName = names[0];

            // Parsing metadata values
            assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));

            // Expanding the envelope
            final ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D
                    .createValue();
            final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
            final GeneralEnvelope newEnvelope = new GeneralEnvelope(originalEnvelope);
            newEnvelope.setCoordinateReferenceSystem(reader
                    .getCoordinateReferenceSystem(coverageName));
            newEnvelope.add(new DirectPosition2D(newEnvelope.getMinimum(0) - 10, newEnvelope
                    .getMinimum(1) - 10));

            // Selecting the same gridRange
            GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
            gg.setValue(new GridGeometry2D(gridRange, newEnvelope));

            GeneralParameterValue[] values = new GeneralParameterValue[] { gg };
            // Read with the larger BBOX
            GridCoverage2D grid = reader.read(coverageName, values);
            // Check if the result is not null
            assertNotNull(grid);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }
}
