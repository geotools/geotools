/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.media.jai.PlanarImage;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

/**
 * Tests that a data set that contains a dimension with multiple bands is correctly handled. Is very
 * important to use a clean test directory for each run, this guarantees that generated files (like
 * the binary index for example) are not reused. The stations data set is very small (perfect for
 * testing proposes) please use netcdf dump if you need to check is content.
 */
public final class NetCDFStationsTest extends Assert {

    @BeforeClass
    public static void init() {
        // make sure CRS ordering is correct
        System.setProperty("org.geotools.referencing.forceXY", "true");
        CRS.reset("all");
    }

    @Test
    public void readMultipleBandsDimension() throws Exception {

        // we should have three bands and all the values should be present
        checkRasterData(
                new GeneralParameterValue[] {},
                new int[] {5, 6, 7, 8, 9, 0, 1, 2, 3, 4},
                new int[] {105, 106, 107, 108, 109, 100, 101, 102, 103, 104},
                new int[] {1005, 1006, 1007, 1008, 1009, 1000, 1001, 1002, 1003, 1004});
    }

    @Test
    public void readMultipleBandsDimensionSelectingOnlyOneBand() throws Exception {

        // we should have only band a single band
        ParameterValue<int[]> selectedBands = AbstractGridFormat.BANDS.createValue();
        selectedBands.setValue(new int[] {1});
        checkRasterData(
                new GeneralParameterValue[] {selectedBands},
                new int[] {105, 106, 107, 108, 109, 100, 101, 102, 103, 104});
    }

    @Test
    public void readMultipleBandsDimensionWithDifferentOrderBandsSelection() throws Exception {

        // we should have three bands with values indexes ordered as 2, 0, 1
        ParameterValue<int[]> selectedBands = AbstractGridFormat.BANDS.createValue();
        selectedBands.setValue(new int[] {2, 0, 1});
        checkRasterData(
                new GeneralParameterValue[] {selectedBands},
                new int[] {1005, 1006, 1007, 1008, 1009, 1000, 1001, 1002, 1003, 1004},
                new int[] {5, 6, 7, 8, 9, 0, 1, 2, 3, 4},
                new int[] {105, 106, 107, 108, 109, 100, 101, 102, 103, 104});
    }

    @Test
    public void readMultipleBandsDimensionWithDifferentOrderAndRepeatedBandsSelection()
            throws Exception {

        // we should have six bands with values indexes ordered as 2, 0, 1, 1, 2, 0
        ParameterValue<int[]> selectedBands = AbstractGridFormat.BANDS.createValue();
        selectedBands.setValue(new int[] {2, 0, 1, 1, 2, 0});
        checkRasterData(
                new GeneralParameterValue[] {selectedBands},
                new int[] {1005, 1006, 1007, 1008, 1009, 1000, 1001, 1002, 1003, 1004},
                new int[] {5, 6, 7, 8, 9, 0, 1, 2, 3, 4},
                new int[] {105, 106, 107, 108, 109, 100, 101, 102, 103, 104},
                new int[] {105, 106, 107, 108, 109, 100, 101, 102, 103, 104},
                new int[] {1005, 1006, 1007, 1008, 1009, 1000, 1001, 1002, 1003, 1004},
                new int[] {5, 6, 7, 8, 9, 0, 1, 2, 3, 4});
    }

    /**
     * Helper method that simply checks that the raster contains the expected data. The number of
     * bands should match the number of expected banks in the provided order. This testes are run
     * for the NetCDF reader and the ImageMosaicReader.
     */
    private void checkRasterData(GeneralParameterValue[] parameters, int[]... expected)
            throws Exception {
        checkRasterData(readCoverageUsingNetCdfReader(parameters), expected);
        checkRasterData(readCoverageUsingImageMosaicReader(parameters), expected);
    }

    /**
     * Helper method that simply checks that the raster contains the expected data. The number of
     * bands should match the number of expected banks in the provided order.
     */
    private void checkRasterData(Raster data, int[]... expected) {

        // we should have a number of bands equal to the number of banks abd equal to number of
        // expected results
        assertThat(data.getNumBands(), is(expected.length));
        assertThat(data.getDataBuffer().getNumBanks(), is(expected.length));

        // let's check if the banks values match the expected values
        for (int i = 0; i < expected.length; i++) {
            int[] bank = readBank(data.getDataBuffer(), i);
            checkArrayContainsArray(bank, expected[i]);
        }
    }

    /** Checks that arrayA contains arrayB. */
    private void checkArrayContainsArray(int[] arrayA, int[] arrayB) {
        for (int i = 0; i < arrayB.length; i++) {
            boolean found = false;
            for (int j = 0; j < arrayA.length; j++) {
                if (arrayB[i] == arrayA[j]) {
                    found = true;
                }
            }
            assertThat(found, is(true));
        }
    }

    /**
     * Helper method that reads stationA data set using the NetCdf reader directly. Some basic
     * checks are also made.
     */
    private Raster readCoverageUsingNetCdfReader(GeneralParameterValue[] readParameters)
            throws Exception {

        // create test directory for this test removing any existing one
        File testDirectory = new File(TestData.file(this, "."), "MultipleBandsDimensionTest");
        FileUtils.deleteQuietly(testDirectory);
        assertThat(testDirectory.mkdirs(), is(true));

        // just keeping a reference to the reader so we can close it in the finally block
        NetCDFReader reader = null;

        try {

            // move test files to the test directory
            FileUtils.copyFileToDirectory(TestData.file(this, "stations.nc"), testDirectory);
            FileUtils.copyFileToDirectory(TestData.file(this, "stations.xml"), testDirectory);
            File netCdfFile = new File(testDirectory, "stations.nc");
            File auxiliaryFile = new File(testDirectory, "stations.xml");

            // instantiate the netcdf reader, add the auxiliary file as an hint for the reader
            final Hints hints = new Hints(Utils.AUXILIARY_FILES_PATH, auxiliaryFile.getPath());
            reader = new NetCDFReader(netCdfFile, hints);

            // checking that we have four coverages (only the first station has values others
            // stations values are zero)
            List<String> names = Arrays.asList(reader.getGridCoverageNames());
            assertThat(names.contains("stationA"), is(true));
            assertThat(names.contains("stationB"), is(true));
            assertThat(names.contains("stationC"), is(true));
            assertThat(names.contains("stationD"), is(true));

            // reading stationA data set
            GridCoverage2D coverage = reader.read("stationA", readParameters);
            assertThat(coverage, notNullValue());
            return PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getData();

        } finally {

            if (reader != null) {
                reader.dispose();
            }
            // cleaning the tests directory
            FileUtils.deleteQuietly(testDirectory);
        }
    }

    /**
     * Helper method that reads stationA data set using the image mosaic reader directly. Some basic
     * checks are also made.
     */
    private Raster readCoverageUsingImageMosaicReader(GeneralParameterValue[] readParameters)
            throws Exception {

        // create test directory for this test removing any existing one
        File testDirectory = new File(TestData.file(this, "."), "MultipleBandsDimensionTest");
        FileUtils.deleteQuietly(testDirectory);
        assertThat(testDirectory.mkdirs(), is(true));

        // just keeping a reference to the reader so we can close it in the finally block
        ImageMosaicReader reader = null;
        PlanarImage image = null;
        try {

            // move test files to the test directory
            FileUtils.copyFileToDirectory(TestData.file(this, "stations.nc"), testDirectory);
            FileUtils.copyFileToDirectory(
                    TestData.file(this, "stations_mosaic_indexer.xml"), testDirectory);
            FileUtils.copyFileToDirectory(
                    TestData.file(this, "stations_netcdf_auxiliary.xml"), testDirectory);
            FileUtils.copyFileToDirectory(
                    TestData.file(this, "stations_datastore.properties"), testDirectory);
            FileUtils.moveFile(
                    new File(testDirectory, "stations_mosaic_indexer.xml"),
                    new File(testDirectory, "indexer.xml"));
            FileUtils.moveFile(
                    new File(testDirectory, "stations_netcdf_auxiliary.xml"),
                    new File(testDirectory, "netcdf_auxiliary.xml"));
            FileUtils.moveFile(
                    new File(testDirectory, "stations_datastore.properties"),
                    new File(testDirectory, "datastore.properties"));

            // instantiate the image mosaic reader
            ImageMosaicFormat format = new ImageMosaicFormat();
            reader = format.getReader(testDirectory);

            // reading stationA data set
            GridCoverage2D coverage = reader.read("stationA", readParameters);
            assertThat(coverage, notNullValue());
            image = PlanarImage.wrapRenderedImage(coverage.getRenderedImage());
            return image.getData();

        } finally {

            if (image != null) {
                ImageUtilities.disposeImage(image);
            }
            if (reader != null) {
                reader.dispose();
            }

            // cleaning the tests directory
            FileUtils.deleteQuietly(testDirectory);
        }
    }

    /** Helper method that simply reads a bank of data from a data buffer. */
    private int[] readBank(DataBuffer dataBuffer, int bank) {
        int[] data = new int[dataBuffer.getSize()];
        for (int i = 0; i < dataBuffer.getSize(); i++) {
            data[i] = dataBuffer.getElem(bank, i);
        }
        return data;
    }
}
