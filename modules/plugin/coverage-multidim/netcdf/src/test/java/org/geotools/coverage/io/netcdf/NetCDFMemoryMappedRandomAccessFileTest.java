package org.geotools.coverage.io.netcdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import org.geotools.api.geometry.Position;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.Position2D;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.io.MemoryMappedRandomAccessFile;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Test;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.iosp.AbstractIOServiceProvider;
import ucar.unidata.io.RandomAccessFile;

public class NetCDFMemoryMappedRandomAccessFileTest extends NetCDFBaseTest {

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
    public void testMemoryMappingBuffering() throws IOException {
        final File testURL = TestData.file(this, "O3-NO2.nc");
        // Get format
        // final AbstractGridFormat format = (AbstractGridFormat)
        GridFormatFinder.findFormat(testURL.toURI().toURL(), null);
        final NetCDFReader reader = new NetCDFReader(testURL, null);
        // assertNotNull(format);
        assertNotNull(reader);
        try {
            String[] names = reader.getGridCoverageNames();
            assertNotNull(names);
            assertEquals(2, names.length);

            GridCoverage2D grid = reader.read("O3", null);
            assertFalse(grid.getSampleDimension(0).getDescription().toString().endsWith(":sd"));
            assertNotNull(grid);
            float[] value =
                    grid.evaluate(
                            (Position) new Position2D(DefaultGeographicCRS.WGS84, 5, 45),
                            new float[1]);
            assertEquals(47.63341f, value[0], 0.00001);

            value =
                    grid.evaluate(
                            (Position) new Position2D(DefaultGeographicCRS.WGS84, 5, 45.125),
                            new float[1]);
            assertEquals(52.7991f, value[0], 0.000001);

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
