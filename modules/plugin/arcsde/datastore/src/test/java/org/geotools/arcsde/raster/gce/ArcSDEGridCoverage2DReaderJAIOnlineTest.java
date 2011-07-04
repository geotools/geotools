/**
 * 
 */
package org.geotools.arcsde.raster.gce;

import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_1BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_REAL;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_4BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_64BIT_REAL;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_8BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_8BIT_U;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.geotools.arcsde.ArcSDERasterFormatFactory;
import org.geotools.arcsde.raster.info.RasterCellType;
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.resources.image.ColorUtilities;
import org.geotools.util.logging.Logging;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.esri.sde.sdk.client.SeRaster;

/**
 * @author groldan
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/arcsde/datastore/src/test/java/org/geotools/arcsde/raster/gce/ArcSDEGridCoverage2DReaderJAIOnlineTest.java $
 */
@SuppressWarnings({ "deprecation", "nls" })
public class ArcSDEGridCoverage2DReaderJAIOnlineTest {

    private static final String RASTER_TEST_DEBUG_TO_DISK = "raster.test.debugToDisk";

    private static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde.gce");

    /**
     * Whether to write the fetched rasters to disk or not
     */
    private static boolean DEBUG;

    static RasterTestData rasterTestData;

    private static String tableName;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LOGGER.setLevel(Level.FINEST);
        // rasterTestData = new RasterTestData();
        // rasterTestData.setUp();
        // DEBUG = Boolean
        // .valueOf(rasterTestData.getRasterTestDataProperty(RASTER_TEST_DEBUG_TO_DISK));
        // rasterTestData.setOverrideExistingTestTables(false);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // rasterTestData.tearDown();
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        rasterTestData = new RasterTestData();
        rasterTestData.setUp();
        DEBUG = Boolean
                .valueOf(rasterTestData.getRasterTestDataProperty(RASTER_TEST_DEBUG_TO_DISK));
        //rasterTestData.setOverrideExistingTestTables(false);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        try {
            LOGGER.info("tearDown: deleting " + tableName);
            rasterTestData.deleteTable(tableName);
            rasterTestData.tearDown();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Error deleting test table " + tableName, e);
        }
    }

    @Test
    public void testRead_01bit_1Band() throws Exception {
        testReadFullLevel0(TYPE_1BIT, 1);
    }

    @Test
    public void testRead_01bit_MoreThanOneBandIsUnsupported() throws Exception {
        try {
            testReadFullLevel0(TYPE_1BIT, 2);
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /*
     * 4bit rasters are not supported by now, need to check exactly what color model/sample model
     * combination makes JAI happy, or to unpack the incoming samples into full bytes
     */
    @Test
    @Ignore
    public void testRead_04bit_1Band() throws Exception {
        testReadFullLevel0(TYPE_4BIT, 1, TYPE_8BIT_U);
    }

    @Test
    public void testRead_04bit_MoreThanOneBandIsUnsupported() throws Exception {
        try {
            testReadFullLevel0(TYPE_4BIT, 2);
            fail("Expected IAE");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRead_08bit_U_1Band() throws Exception {
        testReadFullLevel0(TYPE_8BIT_U, 1, TYPE_16BIT_U);
    }

    @Test
    public void testRead_08bit_U_4Band() throws Exception {
        GridCoverage2D coverage = testReadFullLevel0(TYPE_8BIT_U, 4);

        final RenderedImage image = coverage.view(ViewType.GEOPHYSICS).getRenderedImage();
        assertEquals(DataBuffer.TYPE_BYTE, image.getSampleModel().getTransferType());

        ColorModel colorModel = image.getColorModel();

        assertEquals(ColorSpace.TYPE_RGB, colorModel.getColorSpace().getType());
        assertEquals(4, colorModel.getNumComponents());
        assertTrue(colorModel.hasAlpha());
    }

    @Test
    public void testRead_08bit_U_3Band() throws Exception {
        GridCoverage2D coverage = testReadFullLevel0(TYPE_8BIT_U, 3);

        final RenderedImage image = coverage.view(ViewType.GEOPHYSICS).getRenderedImage();
        assertEquals(DataBuffer.TYPE_BYTE, image.getSampleModel().getTransferType());

        ColorModel colorModel = image.getColorModel();

        assertEquals(ColorSpace.TYPE_RGB, colorModel.getColorSpace().getType());
        assertEquals(3, colorModel.getNumComponents());
        assertFalse(colorModel.hasAlpha());
    }

    @Test
    public void testRead_08bit_U_7Band() throws Exception {
        // type promotion caused becuase test data has no statistics...
        testReadFullLevel0(TYPE_8BIT_U, 7, TYPE_16BIT_U);
    }

    @Test
    public void testRead_08bitU_ColorMapped() throws Exception {
        tableName = rasterTestData.loadRGBColorMappedRaster();
        testReadFullLevel0(TYPE_8BIT_U, 1, TYPE_8BIT_U, "testRead_8bitU_RGBColorMappedRaster");
    }

    @Test
    public void testRead_08bit_S_1Band() throws Exception {
        testReadFullLevel0(TYPE_8BIT_S, 1, TYPE_16BIT_S);
    }

    @Test
    public void testRead_08bit_S_7Band() throws Exception {
        testReadFullLevel0(TYPE_8BIT_S, 7, TYPE_16BIT_S);
    }

    @Test
    public void testRead_16bit_S_1Band() throws Exception {
        testReadFullLevel0(TYPE_16BIT_S, 1, TYPE_32BIT_S);
    }

    @Test
    public void testRead_16bit_S_7Band() throws Exception {
        testReadFullLevel0(TYPE_16BIT_S, 7, TYPE_32BIT_S);
    }

    @Test
    public void testRead_16bit_U_1Band() throws Exception {
        testReadFullLevel0(TYPE_16BIT_U, 1, TYPE_32BIT_U);
    }

    @Test
    public void testRead_16bit_U_7Band() throws Exception {
        testReadFullLevel0(TYPE_16BIT_U, 7, TYPE_32BIT_U);
    }

    @Test
    public void testRead_16bit_U_ColorMapped() throws Exception {
        int[] ARGB = new int[65536];
        ColorUtilities.expand(new Color[] { Color.BLACK, Color.WHITE }, ARGB, 0, ARGB.length);
        IndexColorModel colorModel = ColorUtilities.getIndexColorModel(ARGB);
        tableName = rasterTestData.getRasterTableName(TYPE_16BIT_U, 1, true);
        rasterTestData.loadTestRaster(tableName, 1, TYPE_16BIT_U, colorModel);
        testReadFullLevel0(TYPE_16BIT_U, 1, TYPE_16BIT_U, "testRead_16bit_U_ColorMapped");
    }

    @Test
    public void testRead_32bit_REAL_1Band() throws Exception {
        GridCoverage2D coverage = testReadFullLevel0(TYPE_32BIT_REAL, 1);

        RenderedImage geophysics = coverage.geophysics(true).getRenderedImage();
        RenderedImage rendered = coverage.geophysics(false).getRenderedImage();

        ColorModel gpCm = geophysics.getColorModel();
        SampleModel gpSm = geophysics.getSampleModel();

        ColorModel rCm = rendered.getColorModel();
        SampleModel rSm = geophysics.getSampleModel();

        System.out.println("Geophysics: \t" + gpCm + "\n\t" + gpSm);
        System.out.println("Rendered  : \t" + rCm + "\n\t" + rSm);

        System.out.println(Float.NaN);
        Float valueOf = Float.valueOf("NaN");
        System.out.println(valueOf);
        System.out.println(valueOf.floatValue());
    }

    @Test
    public void testRead_32bit_REAL_7Band() throws Exception {
        testReadFullLevel0(TYPE_32BIT_REAL, 7);
    }

    @Test
    public void testRead_32bit_U_1Band() throws Exception {
        testReadFullLevel0(TYPE_32BIT_U, 1);
    }

    @Test
    public void testRead_32bit_U_7Band() throws Exception {
        testReadFullLevel0(TYPE_32BIT_U, 7);
    }

    @Test
    public void testRead_32bit_S_1Band() throws Exception {
        testReadFullLevel0(TYPE_32BIT_S, 1);
    }

    @Test
    public void testRead_32bit_S_7Band() throws Exception {
        testReadFullLevel0(TYPE_32BIT_S, 7);
    }

    @Test
    public void testRead_64bit_REAL_1Band() throws Exception {
        testReadFullLevel0(TYPE_64BIT_REAL, 1);
    }

    @Test
    public void testRead_64bit_REAL_7Band() throws Exception {
        testReadFullLevel0(TYPE_64BIT_REAL, 7);
    }

    @Test
    public void testReadSampleRGB() throws Exception {
        tableName = rasterTestData.loadRGBRaster();
        testReadFullLevel0(TYPE_8BIT_U, 3, TYPE_8BIT_U, "sampleRGB");
    }

    @Test
    public void testReadRasterCatalogFull() throws Exception {
        tableName = rasterTestData.loadRasterCatalog();
        testReadFullLevel0(TYPE_8BIT_U, 3, TYPE_8BIT_U, "RasterCatalog");

    }

    @Test
    public void testReadRasterCatalogSubset() throws Exception {
        tableName = rasterTestData.loadRasterCatalog();
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
        final GridEnvelope originalGridRange = reader.getOriginalGridRange();

        final int reqWidth = originalGridRange.getSpan(0) / 2;
        final int reqHeight = originalGridRange.getSpan(1) / 2;

        GeneralEnvelope reqEnvelope = new GeneralEnvelope(
                originalEnvelope.getCoordinateReferenceSystem());
        double deltaX = originalEnvelope.getSpan(0) / 6;
        double deltaY = originalEnvelope.getSpan(1) / 6;

        double minx = originalEnvelope.getMinimum(0) + deltaX;
        double miny = originalEnvelope.getMinimum(1) + deltaY;
        double maxx = originalEnvelope.getMaximum(0) - deltaX;
        double maxy = originalEnvelope.getMaximum(1) - deltaY;
        reqEnvelope.setEnvelope(minx, miny, maxx, maxy);

        assertTrue(originalEnvelope.intersects(reqEnvelope, true));

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnvelope);
        assertNotNull("read coverage returned null", coverage);

        writeToDisk(coverage, "testReadRasterCatalogSubset");
    }

    @Test
    public void testReadRasterCatalog2() throws Exception {
        tableName = rasterTestData.loadRasterCatalog();
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + tableName, reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
        final GridEnvelope originalGridRange = reader.getOriginalGridRange();

        final int reqWidth = originalGridRange.getSpan(0) / 10;
        final int reqHeight = originalGridRange.getSpan(1) / 10;

        GeneralEnvelope reqEnvelope = new GeneralEnvelope(
                originalEnvelope.getCoordinateReferenceSystem());
        double minx = originalEnvelope.getMinimum(0);
        double miny = originalEnvelope.getMinimum(1);
        double maxx = minx + originalEnvelope.getSpan(0);// / 2;
        double maxy = miny + originalEnvelope.getSpan(1);// / 2;
        reqEnvelope.setEnvelope(minx, miny, maxx, maxy);

        assertTrue(originalEnvelope.intersects(reqEnvelope, true));

        final GridCoverage2D coverage = readCoverage(reader, reqWidth, reqHeight, reqEnvelope);
        assertNotNull("read coverage returned null", coverage);

        writeToDisk(coverage, "testReadRasterCatalog2");
    }

    private GridCoverage2D testReadFullLevel0(final RasterCellType cellType, final int numBands)
            throws Exception {
        return testReadFullLevel0(cellType, numBands, cellType);
    }

    private GridCoverage2D testReadFullLevel0(final RasterCellType cellType, final int numBands,
            final RasterCellType resultingCellType) throws Exception {

        tableName = rasterTestData.getRasterTableName(cellType, numBands, false);
        rasterTestData.loadTestRaster(tableName, numBands, cellType, null);
        return testReadFullLevel0(cellType, numBands, resultingCellType, tableName + "_" + numBands
                + "-Band");
    }

    private GridCoverage2D testReadFullLevel0(final RasterCellType cellType, final int numBands,
            final RasterCellType resultingCellType, final String fileNamePostFix) throws Exception {

        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull("Couldn't obtain a reader for " + fileNamePostFix, reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
        final GridEnvelope originalGridRange = reader.getOriginalGridRange();

        final int origWidth = originalGridRange.getSpan(0);
        final int origHeight = originalGridRange.getSpan(1);

        final GridCoverage2D coverage = readCoverage(reader, origWidth, origHeight,
                originalEnvelope);
        assertNotNull("read coverage returned null", coverage);

        assertEquals(numBands, coverage.getNumSampleDimensions());
        // for (int i = 0; i < numBands; i++) {
        // NumberRange<?> range = cellType.getSampleValueRange();
        // GridSampleDimension sampleDimension = coverage.getSampleDimension(i);
        // assertNotNull("Sample dimension #" + i, sampleDimension);
        // assertEquals(range, sampleDimension.getRange());
        // }

        assertNotNull(coverage.getEnvelope());
        GeneralEnvelope envelope = (GeneralEnvelope) coverage.getEnvelope();
        assertTrue(originalEnvelope.intersects(envelope, true));

        // ///////////////////////////////////////////////////////////assertEquals(originalGridRange,
        // gridGeometry.getGridRange());

        final String fileName = "testReadFullLevel0_" + fileNamePostFix;

        writeToDisk(coverage, fileName);

        // ////assertEquals(cellType.getDataBufferType(), image.getSampleModel().getDataType());

        RenderedImage nativeImage = coverage.view(ViewType.NATIVE).getRenderedImage();
        final int[] sampleSize = nativeImage.getSampleModel().getSampleSize();
        final ColorModel colorModel = nativeImage.getColorModel();

        if (colorModel instanceof IndexColorModel) {
            switch (cellType) {
            case TYPE_1BIT:
                assertEquals("1-bit image should have been promoted to 8-bit", 8, sampleSize[0]);
                break;
            case TYPE_8BIT_U:
                assertEquals("8-bit indexed image should have been "
                        + "promoted to 16bit to account for no-data values", 16, sampleSize[0]);
                break;
            case TYPE_16BIT_U:
                assertEquals(16, sampleSize[0]);
                break;
            default:
                throw new IllegalArgumentException(cellType.toString());
            }
        } else {
            for (int band = 0; band < numBands; band++) {
                assertEquals(resultingCellType.getBitsPerSample(), sampleSize[band]);
            }
        }

        return coverage;
    }

    @Test
    public void tesReadOverlapsSampleRGBIamge() throws Exception {
        tableName = rasterTestData.getRasterTableName(RasterCellType.TYPE_8BIT_U, 3);
        rasterTestData.loadRGBRaster();
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull(reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();

        final CoordinateReferenceSystem originalCrs = originalEnvelope
                .getCoordinateReferenceSystem();
        final GridEnvelope originalGridRange = reader.getOriginalGridRange();
        final int requestedWidth = originalGridRange.getSpan(0);
        final int requestedHeight = originalGridRange.getSpan(1);

        final GeneralEnvelope requestedEnvelope;
        final GridCoverage2D coverage;
        {
            final double minx = originalEnvelope.getMinimum(0);
            final double miny = originalEnvelope.getMinimum(1);

            double shiftX = originalEnvelope.getSpan(0) / 2;
            double shiftY = originalEnvelope.getSpan(1) / 2;

            double x1 = minx - shiftX;
            double x2 = minx + shiftX;
            double y1 = miny + shiftY;
            double y2 = miny + 2 * shiftY;

            requestedEnvelope = new GeneralEnvelope(new ReferencedEnvelope(x1, x2, y1, y2,
                    originalCrs));
            coverage = readCoverage(reader, requestedWidth, requestedHeight, requestedEnvelope);
        }
        assertNotNull(coverage);
        assertNotNull(coverage.getRenderedImage());
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
        assertNotNull(crs);

        final String fileName = "tesReadOverlapsSampleRGBIamge";

        final RenderedImage image = coverage.view(ViewType.GEOPHYSICS).getRenderedImage();
        assertNotNull(image);
        writeToDisk(coverage, fileName);

        assertSame(originalCrs, crs);

        final Envelope returnedEnvelope = coverage.getEnvelope();

        // these ones should equal to the tile dimension in the arcsde raster
        int tileWidth = image.getTileWidth();
        int tileHeight = image.getTileHeight();
        assertTrue(tileWidth > 0);
        assertTrue(tileHeight > 0);

        GeneralEnvelope expectedEnvelope = new GeneralEnvelope(originalCrs);
        expectedEnvelope.setRange(0, originalEnvelope.getMinimum(0), originalEnvelope.getMinimum(0)
                + (originalEnvelope.getSpan(0) / 2));

        expectedEnvelope.setRange(1, originalEnvelope.getMinimum(1), originalEnvelope.getMinimum(1)
                + (originalEnvelope.getSpan(1) / 2));

        LOGGER.info("\nRequested width : " + requestedWidth + "\nReturned width  :"
                + image.getWidth() + "\nRequested height:" + requestedHeight
                + "\nReturned height :" + image.getHeight());

        LOGGER.info("\nOriginal envelope  : " + originalEnvelope + "\n requested envelope :"
                + requestedEnvelope + "\n expected envelope  :" + expectedEnvelope
                + "\n returned envelope  :" + returnedEnvelope);

        assertEquals(501, image.getWidth());
        assertEquals(501, image.getHeight());
        // assertEquals(expectedEnvelope, returnedEnvelope);
    }

    @Test
    public void tesReadOverlaps() throws Exception {
        tableName = rasterTestData.getRasterTableName(RasterCellType.TYPE_8BIT_U, 1);
        rasterTestData.loadTestRaster(tableName, 1, 100, 100, TYPE_8BIT_U, null, true, false,
                SeRaster.SE_INTERPOLATION_NEAREST, null);
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull(reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();

        final CoordinateReferenceSystem originalCrs = originalEnvelope
                .getCoordinateReferenceSystem();
        final GridEnvelope originalGridRange = reader.getOriginalGridRange();
        final int requestedWidth = originalGridRange.getSpan(0);
        final int requestedHeight = originalGridRange.getSpan(1);

        final GeneralEnvelope requestedEnvelope;
        requestedEnvelope = new GeneralEnvelope(new ReferencedEnvelope(-100, 100, -100, 100,
                originalCrs));

        final GridCoverage2D coverage;
        coverage = readCoverage(reader, requestedWidth, requestedHeight, requestedEnvelope);

        assertNotNull(coverage);
        assertNotNull(coverage.getRenderedImage());

        final String fileName = "tesReadOverlaps_Level0_8BitU_1-Band";

        final RenderedImage image = coverage.view(ViewType.GEOPHYSICS).getRenderedImage();
        assertNotNull(image);
        writeToDisk(coverage, fileName);

        final Envelope returnedEnvelope = coverage.getEnvelope();

        // these ones should equal to the tile dimension in the arcsde raster
        int tileWidth = image.getTileWidth();
        int tileHeight = image.getTileHeight();
        assertTrue(tileWidth > 0);
        assertTrue(tileHeight > 0);

        GeneralEnvelope expectedEnvelope = new GeneralEnvelope(originalCrs);
        expectedEnvelope.setRange(0, 0, 100);
        expectedEnvelope.setRange(1, 0, 100);

        LOGGER.info("\nRequested width : " + requestedWidth + "\nReturned width  :"
                + image.getWidth() + "\nRequested height:" + requestedHeight
                + "\nReturned height :" + image.getHeight());

        LOGGER.info("\nOriginal envelope  : " + originalEnvelope + "\n requested envelope :"
                + requestedEnvelope + "\n expected envelope  :" + expectedEnvelope
                + "\n returned envelope  :" + returnedEnvelope);

        assertEquals(50, image.getWidth());
        assertEquals(50, image.getHeight());
        // assertEquals(expectedEnvelope, returnedEnvelope);
    }

    @Test
    public void tesReadNotOverlaps() throws Exception {
        tableName = rasterTestData.getRasterTableName(RasterCellType.TYPE_8BIT_U, 1);
        rasterTestData.loadTestRaster(tableName, 1, 100, 100, TYPE_8BIT_U, null, true, false,
                SeRaster.SE_INTERPOLATION_NEAREST, null);
        final AbstractGridCoverage2DReader reader = getReader();
        assertNotNull(reader);

        final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();

        final CoordinateReferenceSystem originalCrs = originalEnvelope
                .getCoordinateReferenceSystem();
        final GridEnvelope originalGridRange = reader.getOriginalGridRange();
        final int requestedWidth = originalGridRange.getSpan(0);
        final int requestedHeight = originalGridRange.getSpan(1);

        final GeneralEnvelope nonOverlappingEnvelope;
        nonOverlappingEnvelope = new GeneralEnvelope(new ReferencedEnvelope(300, 500, 300, 500,
                originalCrs));

        try {
            readCoverage(reader, requestedWidth, requestedHeight, nonOverlappingEnvelope);
            fail("Expected IAE, envelopes does not overlap");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    private void writeToDisk(GridCoverage2D coverage, String fileName) throws Exception {
        if (!DEBUG) {
            LOGGER.fine("DEBUG == false, not writing image to disk");
            return;
        }
        Object destination;
        {
            String directory = System.getProperty("user.home");
            directory += File.separator + "arcsde_test" + File.separator + fileName
                    + "_native.tiff";
            File path = new File(directory);
            path.getParentFile().mkdirs();
            destination = path;
        }
        if (coverage.getRenderedImage().getColorModel() instanceof IndexColorModel) {
            LOGGER.info("not writing GeoTiff for " + fileName
                    + " because it fails with IndexColorModel. Don't know why");
        } else {
            GeoTiffWriter writer = new GeoTiffWriter(destination);
            System.out.println("\n --- Writing to " + destination);
            try {
                long t = System.currentTimeMillis();
                writer.write(coverage, null);
                System.out.println(" - wrote in " + t + "ms" + destination);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        RenderedImage rendered = coverage.view(ViewType.RENDERED).getRenderedImage();
        writeToDisk(rendered, fileName + "_rendered");
        RenderedImage geophysics = coverage.view(ViewType.GEOPHYSICS).getRenderedImage();
        writeToDisk(geophysics, fileName + "_geophysics");
        RenderedImage photographic = coverage.view(ViewType.PHOTOGRAPHIC).getRenderedImage();
        writeToDisk(photographic, fileName + "_photographic");
    }

    private void writeToDisk(final RenderedImage image, String fileName) throws Exception {
        if (!DEBUG) {
            LOGGER.fine("DEBUG == false, not writing image to disk");
            return;
        }
        String file = System.getProperty("user.home");
        file += File.separator + "arcsde_test" + File.separator + fileName;
        File targetFile = new File(file + ".tiff");
        targetFile.getParentFile().mkdirs();
        System.out.println("\n --- Writing to " + file);

        try {
            ImageIO.write(image, "TIFF", targetFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private GridCoverage2D readCoverage(final AbstractGridCoverage2DReader reader,
            final int reqWidth, final int reqHeight, final Envelope reqEnv) throws Exception {

        GeneralParameterValue[] requestParams = new Parameter[2];

        GridGeometry2D gg2d;
        gg2d = new GridGeometry2D(new GridEnvelope2D(new Rectangle(reqWidth, reqHeight)), reqEnv);

        requestParams[0] = new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D,
                gg2d);
        requestParams[1] = new Parameter<OverviewPolicy>(AbstractGridFormat.OVERVIEW_POLICY,
                OverviewPolicy.SPEED);

        final GridCoverage2D coverage;
        coverage = (GridCoverage2D) reader.read(requestParams);

        return coverage;
    }

    private AbstractGridCoverage2DReader getReader() throws IOException {
        final ArcSDEConnectionConfig config = rasterTestData.getConnectionPool().getConfig();

        final String rgbUrl = "sde://" + config.getUserName() + ":" + config.getPassword() + "@"
                + config.getServerName() + ":" + config.getPortNumber() + "/"
                + config.getDatabaseName() + "#" + tableName;

        final ArcSDERasterFormat format = new ArcSDERasterFormatFactory().createFormat();
        // we can't create statistics here so tell ArcSDERasterFormat not to fail
        format.setStatisticsMandatory(false);

        AbstractGridCoverage2DReader reader = format.getReader(rgbUrl);
        return reader;
    }

}
