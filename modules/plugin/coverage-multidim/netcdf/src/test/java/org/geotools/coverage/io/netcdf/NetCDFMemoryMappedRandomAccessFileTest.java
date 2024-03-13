/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import org.geotools.api.geometry.Position;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.geometry.Position2D;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.io.MemoryMappedRandomAccessFile;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.iosp.AbstractIOServiceProvider;
import ucar.nc2.util.cache.FileCacheIF;
import ucar.unidata.io.RandomAccessFile;

public class NetCDFMemoryMappedRandomAccessFileTest extends NetCDFBaseTest {

    @Before
    public void cleanup() {
        cleanCache();
    }

    @BeforeClass
    public static void enableCache() {
        NetCDFBaseTest.init();
        NetCDFUtilities.enableNetCDFFileCaches();
    }

    @Test
    public void testMemoryMappedRandomAccessFileIsBeingUsed()
            throws IOException, NoSuchFieldException, IllegalAccessException {

        File file = TestData.file(this, "O3-NO2.nc");
        try (NetcdfDataset netcdfDataset = NetCDFUtilities.acquireDataset(file.toURI())) {

            // Let's use reflection to verify if the underlying machinery
            // is actually using the MemoryMappedRandomAccessFile

            // First: Access the orgFile field from the dataset object
            Field orgFileField = NetcdfDataset.class.getDeclaredField("orgFile");
            orgFileField.setAccessible(true); // Make the field accessible

            // Second: Access the iosp field from the orgFile object
            Object orgFile = orgFileField.get(netcdfDataset);
            Field iospField = orgFile.getClass().getDeclaredField("iosp");
            iospField.setAccessible(true); // Make the field accessible

            // Third: Access the raf field from the iosp object
            Object iosp = iospField.get(orgFile);
            Field rafField = AbstractIOServiceProvider.class.getDeclaredField("raf");
            rafField.setAccessible(true);

            // Finally, get the RandomAccessFile object
            @SuppressWarnings("PMD.CloseResource")
            RandomAccessFile raf = (RandomAccessFile) rafField.get(iosp);
            assertTrue(raf instanceof MemoryMappedRandomAccessFile);
            assertTrue(raf.getLocation().endsWith("O3-NO2.nc"));
        }
    }

    @Test
    /**
     * This test is going to access a file that won't fit entirely in the memory mapped buffer and
     * that will involve some seek back and forth. (File is 122Kb whilst configured buffer limit is
     * 32k)
     */
    public void testMemoryMappingPartialBuffering() throws IOException, ParseException {
        final File testURL = TestData.file(this, "O3-NO2.nc");
        final NetCDFReader reader = new NetCDFReader(testURL, null);
        assertNotNull(reader);
        final String t1 = "2012-04-01T00:00:00.000Z";
        final String t2 = "2012-04-01T01:00:00.000Z";
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        final ParameterValue<List> elevation = ImageMosaicFormat.ELEVATION.createValue();
        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));
        final double elevation1 = 10d;
        final double elevation2 = 450d;

        try {
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);
            assertEquals(2, names.length);
            time.setValue(List.of(formatD.parse(t1)));
            elevation.setValue(List.of(elevation1));

            GeneralParameterValue[] values = {time, elevation};
            GridCoverage2D grid = reader.read("NO2", values);
            assertFalse(grid.getSampleDimension(0).getDescription().toString().endsWith(":sd"));
            assertNotNull(grid);
            float[] value =
                    grid.evaluate(
                            (Position) new Position2D(DefaultGeographicCRS.WGS84, 5, 45),
                            new float[1]);
            assertEquals(6.15991f, value[0], 0.00001);

            time.setValue(List.of(formatD.parse(t2)));
            elevation.setValue(List.of(elevation2));
            grid = reader.read("O3", values);
            assertFalse(grid.getSampleDimension(0).getDescription().toString().endsWith(":sd"));
            assertNotNull(grid);
            value =
                    grid.evaluate(
                            (Position) new Position2D(DefaultGeographicCRS.WGS84, 5, 45),
                            new float[1]);
            assertEquals(56.946774f, value[0], 0.00001);

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

    @Test
    /**
     * This tests memoryMapping for a file that can be fully mapped, also making sure that the file
     * get cached
     */
    public void testMemoryMapping() throws IOException {
        final File file = TestData.file(this, "sst.nc");

        String cachedRaf1 = testReadingData(file);
        assertTrue(cachedRaf1.startsWith("true 0")); // it's locked and no hits yet
        // Read it again. It should use the same cached randomaccessfile
        String cachedRaf2 = testReadingData(file);

        assertTrue(cachedRaf2.startsWith("true 1")); // it's still locked and 1 hit
    }

    private String testReadingData(File file) throws IOException {
        NetCDFReader reader = new NetCDFReader(file, null);
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            GridCoverage2D grid = reader.read(names[0], null);
            assertFalse(grid.getSampleDimension(0).getDescription().toString().endsWith(":sd"));
            assertNotNull(grid);
            float[] value =
                    grid.evaluate(
                            (Position) new Position2D(DefaultGeographicCRS.WGS84, -84, 26),
                            new float[1]);
            assertEquals(24.0f, value[0], 0.00001);
            FileCacheIF fileCache = NetCDFUtilities.getRafFileCache();
            List<String> cached = fileCache.showCache();
            return cached.get(0);
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

    @AfterClass
    public static void disableCache() {
        NetCDFUtilities.disableNetCDFFileCaches();
    }
}
