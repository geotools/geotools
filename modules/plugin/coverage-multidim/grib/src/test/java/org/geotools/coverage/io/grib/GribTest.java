/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import javax.imageio.spi.ImageReaderSpi;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.io.netcdf.NetCDFDriver;
import org.geotools.coverage.io.netcdf.NetCDFReader;
import org.geotools.coverage.io.netcdf.crs.NetCDFCoordinateReferenceSystemType;
import org.geotools.coverage.io.netcdf.crs.NetCDFProjection;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.imageio.netcdf.NetCDFImageReaderSpi;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.operation.projection.RotatedPole;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Projection;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/** Unit test for testing Grib data. */
public class GribTest extends Assert {

    private static final Double DELTA = 1E-9;

    protected File cacheDir;

    @Before
    public void setup() throws FileNotFoundException, IOException {
        final File testDir = TestData.file(this, ".").getCanonicalFile();
        cacheDir = new File(testDir, "cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        String cacheDirPath = cacheDir.getAbsolutePath();
        System.setProperty("GRIB_CACHE_DIR", cacheDirPath);
    }

    @Test
    public void testWeirdGribFileCanBeOpened() throws FileNotFoundException, IOException {
        final String referenceDir = "testGrib";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "weird.zip");
        FileUtils.copyFile(TestData.file(this, "weird.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/weird.zip");
        // This file is a valid GRIB file but it has wrong magic number and
        // it doesn't have a recognized grib extension
        final File file = new File(workDir, "weird.model");
        NetCDFImageReaderSpi spi = new NetCDFImageReaderSpi();
        assertTrue(spi.canDecodeInput(file));
    }

    @Test
    public void testGribEarthShape() throws FileNotFoundException, IOException {
        final String referenceDir = "earthShape";
        final File workDir = new File(TestData.file(this, "."), referenceDir);
        if (!workDir.mkdir()) {
            FileUtils.deleteDirectory(workDir);
            assertTrue("Unable to create workdir:" + workDir, workDir.mkdir());
        }
        final File zipFile = new File(workDir, "TT_FC_INCA_EarthShape.zip");
        FileUtils.copyFile(TestData.file(this, "TT_FC_INCA_EarthShape.zip"), zipFile);

        TestData.unzipFile(this, referenceDir + "/TT_FC_INCA_EarthShape.zip");
        final File file = new File(workDir, "TT_FC_INCA_EarthShape.grb2");
        NetcdfDataset dataset = NetcdfDataset.openDataset(file.getAbsolutePath());
        Variable var = dataset.findVariable(null, "LambertConformal_Projection");
        assertNotNull(var);

        // Before switching to NetCDF 4.6.2 there was a bug which was returning
        // semi_major_axis = 6.377397248E9 and inverse_flattening = 299.15349328722255;
        // https://github.com/Unidata/thredds/issues/133

        // Checking that it's now reporting proper values
        Attribute attribute = var.findAttribute("semi_major_axis");
        assertNotNull(attribute);

        assertEquals(6377397.0, attribute.getNumericValue().doubleValue(), DELTA);
        attribute = var.findAttribute("inverse_flattening");
        assertNotNull(attribute);
        assertEquals(299.15550239234693, attribute.getNumericValue().doubleValue(), DELTA);
    }

    /** Test if the Grib format is accepted by the NetCDF reader */
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

    /** Test if the Grib extension is supported by the NetCDF reader */
    @Test
    public void testGribExtension() {
        // Creation of a NetCDFDriver
        NetCDFDriver driver = new NetCDFDriver();
        // Selection of the extensions used by the NetCDF driver
        List<String> extensions = driver.getFileExtensions();
        // Creation of a list of the grib extensions
        List<String> possibleExt = Arrays.asList(new String[] {"grb", "grb2", "grib"});
        // Check if the extensions are contained
        Assert.assertTrue(extensions.containsAll(possibleExt));

        // Creation of a NetCDFReaderSPI objectfor checking if it supports the GRIB extension
        ImageReaderSpi spi = new NetCDFImageReaderSpi();
        // Selection of the suffixes, formatNames and mimetypes from the spi object
        List<String> suffixes = Arrays.asList(spi.getFileSuffixes());
        List<String> formatNames = Arrays.asList(spi.getFormatNames());
        List<String> MIMETypes = Arrays.asList(spi.getMIMETypes());
        // Creation of similar lists containing the values for the grib format
        List<String> gribSuffixes = Arrays.asList(new String[] {"grib", "grb", "grb2"});
        List<String> gribFormatNames =
                Arrays.asList(new String[] {"grib", "grib2", "GRIB", "GRIB2"});
        List<String> gribMIMETypes = Arrays.asList(new String[] {"application/octet-stream"});
        // Check if the grib informations are present
        Assert.assertTrue(suffixes.containsAll(gribSuffixes));
        Assert.assertTrue(formatNames.containsAll(gribFormatNames));
        Assert.assertTrue(MIMETypes.containsAll(gribMIMETypes));
    }

    /** Test on a Grib image. */
    @Test
    public void testGribImage() throws MalformedURLException, IOException {
        // Selection of the input file
        final File file = TestData.file(this, "sampleGrib.grb2");
        // Testing the 2 points
        testGribFile(file, new Point2D.Double(304, 8), new Point2D.Double(304, 4));
    }

    /**
     * Private method for checking if the selected point are nodata or not. This ensures that the
     * images are flipped vertically only if needed. If the image is not correctly flipped then,
     * validpoint and nodatapoint should be inverted.
     */
    private void testGribFile(final File inputFile, Point2D validPoint, Point2D nodataPoint)
            throws MalformedURLException, IOException {
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(inputFile.toURI().toURL(), null);

        assertTrue(format.accepts(inputFile));

        AbstractGridCoverage2DReader reader = null;
        assertNotNull(format);
        try {

            reader = format.getReader(inputFile, null);
            assertNotNull(reader);

            // Selection of all the Coverage names
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);
            // Selections of one Coverage
            GridCoverage2D grid = reader.read(names[0], null);
            assertNotNull(grid);
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
            // Dispose
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    /** Test on a Grib image asking for a larger bounding box. */
    @Test
    public void testGribImageWithLargeBBOX() throws MalformedURLException, IOException {
        // Selection of the input file
        final File inputFile = TestData.file(this, "sampleGrib.grb2");
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(inputFile.toURI().toURL(), null);
        assertTrue(format.accepts(inputFile));
        AbstractGridCoverage2DReader reader = null;
        assertNotNull(format);
        try {

            reader = format.getReader(inputFile, null);
            assertNotNull(reader);

            // Selection of all the Coverage names
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);

            // Name of the first coverage
            String coverageName = names[0];

            // Parsing metadata values
            assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));

            // Expanding the envelope
            final ParameterValue<GridGeometry2D> gg =
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
            final GeneralEnvelope newEnvelope = new GeneralEnvelope(originalEnvelope);
            newEnvelope.setCoordinateReferenceSystem(
                    reader.getCoordinateReferenceSystem(coverageName));
            newEnvelope.add(
                    new DirectPosition2D(
                            newEnvelope.getMinimum(0) - 10, newEnvelope.getMinimum(1) - 10));

            // Selecting the same gridRange
            GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);
            gg.setValue(new GridGeometry2D(gridRange, newEnvelope));

            GeneralParameterValue[] values = new GeneralParameterValue[] {gg};
            // Read with the larger BBOX
            GridCoverage2D grid = reader.read(coverageName, values);
            // Check if the result is not null
            assertNotNull(grid);
        } finally {
            // Dispose
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    /** Test on a Grib image with temporal bands, querying different bands */
    @Test
    public void testGribImageWithTimeDimension() throws MalformedURLException, IOException {
        // Selection of the input file
        final File inputFile = TestData.file(this, "tpcprblty.2019100912.incremental.grib2");
        // Get format
        final AbstractGridFormat format =
                (AbstractGridFormat) GridFormatFinder.findFormat(inputFile.toURI().toURL(), null);
        assertTrue(format.accepts(inputFile));
        AbstractGridCoverage2DReader reader = null;
        assertNotNull(format);
        try {

            reader = format.getReader(inputFile, null);
            assertNotNull(reader);

            // Selection of all the Coverage names
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);

            // Name of the first coverage
            String coverageName = names[0];

            // Parsing metadata values
            assertEquals(
                    "true",
                    reader.getMetadataValue(coverageName, GridCoverage2DReader.HAS_TIME_DOMAIN));
            assertEquals(
                    "false",
                    reader.getMetadataValue(
                            coverageName, GridCoverage2DReader.HAS_ELEVATION_DOMAIN));

            // Get the envelope
            final ParameterValue<GridGeometry2D> gg =
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope(coverageName);
            // gg.setValue(originalEnvelope);

            // Selecting the same gridRange
            GridEnvelope gridRange = reader.getOriginalGridRange(coverageName);

            final ParameterValue<List> time =
                    new DefaultParameterDescriptor<>("TIME", List.class, null, null).createValue();
            // 2019-10-09T18:00:00.000Z
            time.setValue(new ArrayList<>(Collections.singletonList(new Date(1570644000000L))));

            // HEIGHT_ABOVE_GROUND = "[10.0]"
            final ParameterValue<List> height =
                    new DefaultParameterDescriptor<>("HEIGHT_ABOVE_GROUND", List.class, null, null)
                            .createValue();
            height.setValue(new ArrayList<>(Collections.singletonList(10.0)));

            GeneralParameterValue[] values = new GeneralParameterValue[] {gg, time, height};

            // Read with 0th date
            GridCoverage2D grid = reader.read(coverageName, values);
            assertNotNull(grid);

            // Read with 12th date
            // 2019-10-12T18:00:00.000Z
            time.setValue(new ArrayList<>(Collections.singletonList(new Date(1570903200000L))));
            grid = reader.read(coverageName, values);
            assertNotNull(grid);
        } finally {
            // Dispose
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
     * Check that a GRIB2 file is interpreted as a rotated pole projection with expected parameters.
     *
     * @param gribFileName name of the GRIB2 file
     * @param expectedCentralMeridian expected central meridian of rotated pole projection
     * @param expectedLatitudeOfOrigin expected latitude of origin of the rotated pole projection
     */
    private void checkRotatedPole(
            String gribFileName, double expectedCentralMeridian, double expectedLatitudeOfOrigin)
            throws Exception {
        File file = TestData.file(this, gribFileName);
        NetCDFReader reader = null;
        try {
            reader = new NetCDFReader(file, null);
            String[] coverages = reader.getGridCoverageNames();
            CoordinateReferenceSystem crs = reader.getCoordinateReferenceSystem(coverages[0]);
            NetCDFCoordinateReferenceSystemType crsType =
                    NetCDFCoordinateReferenceSystemType.parseCRS(crs);
            assertSame(NetCDFCoordinateReferenceSystemType.ROTATED_POLE, crsType);
            assertSame(
                    NetCDFCoordinateReferenceSystemType.NetCDFCoordinate.RLATLON_COORDS,
                    crsType.getCoordinates());
            assertSame(NetCDFProjection.ROTATED_POLE, crsType.getNetCDFProjection());
            assertTrue(crs instanceof ProjectedCRS);
            ProjectedCRS projectedCRS = ((ProjectedCRS) crs);
            Projection projection = projectedCRS.getConversionFromBase();
            MathTransform transform = projection.getMathTransform();
            assertTrue(transform instanceof RotatedPole);
            RotatedPole rotatedPole = (RotatedPole) transform;
            ParameterValueGroup values = rotatedPole.getParameterValues();
            assertEquals(
                    expectedCentralMeridian,
                    values.parameter(NetCDFUtilities.CENTRAL_MERIDIAN).doubleValue(),
                    DELTA);
            assertEquals(
                    expectedLatitudeOfOrigin,
                    values.parameter(NetCDFUtilities.LATITUDE_OF_ORIGIN).doubleValue(),
                    DELTA);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /**
     * Test that an RAP native GRIB2 file with GDS template 32769 is interpreted as a {@link
     * RotatedPole} projection with expected parameters.
     */
    @Test
    public void testRapNativeRotatedPole() throws Exception {
        checkRotatedPole("rap-native.grib2", -106, 54);
    }

    /**
     * Test that a COSMO EU GRIB2 file with GDS template 1 is interpreted as a {@link RotatedPole}
     * projection with expected parameters.
     */
    @Test
    public void testCosmoEuRotatedPole() throws Exception {
        checkRotatedPole("cosmo-eu.grib2", 10, 50);
    }
}
