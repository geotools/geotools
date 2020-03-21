/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagepyramid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.DateRange;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Testing {@link ImagePyramidReader}.
 *
 * @author Simone Giannecchini
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Test coverage for pyramids stored in JARs
 *     and referenced by URLs
 * @since 2.3
 */
public class ImagePyramidReaderTest extends ImageLevelsMapperTest {

    /** File to be used for testing purposes. */
    private static final String TEST_FILE = "pyramid.properties";

    // private final static String TEST_JAR_FILE = "pyramid.jar";

    /** Tests automatic building of all the mosaic and pyramid files */
    @Test
    public void testAutomaticBuild() throws IOException {
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE);
        File sourceDir = URLs.urlToFile(testFile).getParentFile();
        File targetDir = File.createTempFile("pyramid", "tst", TestData.file(this, "."));
        targetDir.delete();
        targetDir.mkdir();
        try {
            prepareEmptyMosaic(sourceDir, targetDir);

            // now make sure we can actually rebuild the mosaic
            final AbstractGridFormat format = new ImagePyramidFormat();
            final Hints hints =
                    new Hints(
                            Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84);
            assertTrue(((ImagePyramidFormat) format).accepts(targetDir, hints));
            final ImagePyramidReader reader =
                    (ImagePyramidReader) format.getReader(targetDir, hints);
            assertNotNull(reader);
        } finally {
            // cleanup
            FileUtils.deleteQuietly(targetDir);
        }
    }

    /**
     * Tests automatic building of all the mosaic and pyramid files from a gdal_retile like
     * directory structure
     */
    @Test
    public void testAutomaticBuildGdalRetile() throws IOException {
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE);
        buildPyramid(testFile, "goodpyramid");
    }

    /**
     * Tests automatic building of all the mosaic and pyramid files from a gdal_retile like
     * directory structure
     */
    @Test
    public void testWindowsPath() throws IOException {
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE);
        buildPyramid(testFile, "good pyramid");
    }

    /** */
    private void buildPyramid(final URL testFile, String targetName)
            throws IOException, FileNotFoundException {
        File sourceDir = URLs.urlToFile(testFile).getParentFile();
        File targetDir = File.createTempFile(targetName, "tst", TestData.file(this, "."));
        targetDir.delete();
        targetDir.mkdir();

        try {
            prepareEmptyMosaic(sourceDir, targetDir);

            // move the files so that it looks like a gdal_retile created directory
            File zeroDir = new File(targetDir, "0");
            assertTrue(zeroDir.isDirectory());
            FileUtils.copyDirectory(zeroDir, targetDir);
            FileUtils.deleteDirectory(zeroDir);

            // now make sure we can actually rebuild the mosaic
            final AbstractGridFormat format = new ImagePyramidFormat();
            final Hints hints =
                    new Hints(
                            Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84);
            assertTrue(((ImagePyramidFormat) format).accepts(targetDir, hints));
            final ImagePyramidReader reader =
                    (ImagePyramidReader) format.getReader(targetDir, hints);
            assertNotNull(reader);
        } finally {
            // cleanup
            FileUtils.deleteQuietly(targetDir);
        }
    }

    /**
     * Copies the mosaic from the source dir to the target dir and removes all metadata files from
     * it
     */
    void prepareEmptyMosaic(File sourceDir, File targetDir) throws IOException {
        FileUtils.copyDirectory(sourceDir, targetDir);

        // remove the files we want to recreate
        File[] dirs =
                new File[] {
                    targetDir,
                    new File(targetDir, "0"),
                    new File(targetDir, "2"),
                    new File(targetDir, "4"),
                    new File(targetDir, "8")
                };
        FileFilter metadataFilter = FileFilterUtils.prefixFileFilter("pyramid.");
        for (File dir : dirs) {
            for (File file : dir.listFiles(metadataFilter)) {
                file.delete();
            }
        }
    }

    @Test
    public void testDefaultParameterValue()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {

        //
        // Get the resource.
        //
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE); //
        assertNotNull(testFile);

        //
        // Get a reader
        //
        final AbstractGridFormat format = new ImagePyramidFormat();
        assertTrue(format.accepts(testFile));
        final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
        assertNotNull(reader);

        //
        // Show the coverage
        //
        final GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
        assertEquals("pyramid", coverage.getName().toString());
        assertNotNull("Null value returned instead of a coverage", coverage);
        assertTrue(
                "coverage dimensions different from what we expected",
                coverage.getGridGeometry().getGridRange().getSpan(0) == 250
                        && coverage.getGridGeometry().getGridRange().getSpan(1) == 250);

        if (TestData.isInteractiveTest()) coverage.show("testDefaultParameterValue");
        else
            PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage())
                    .getTiles();
    }

    @Test
    public void testDefaultParameterValueFile()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {
        //
        // Get the resource.
        //
        final File testFile = TestData.file(this, "goodpyramid/" + TEST_FILE); //
        assertNotNull(testFile);

        //
        // Get a reader
        //
        final AbstractGridFormat format = new ImagePyramidFormat();
        assertTrue(format.accepts(testFile));
        final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
        assertNotNull(reader);

        //
        // Show the coverage
        //
        final GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
        assertNotNull("Null value returned instead of a coverage", coverage);
        assertTrue(
                "coverage dimensions different from what we expected",
                coverage.getGridGeometry().getGridRange().getSpan(0) == 250
                        && coverage.getGridGeometry().getGridRange().getSpan(1) == 250);
        if (TestData.isInteractiveTest()) coverage.show("testDefaultParameterValueFile");
        else
            PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage())
                    .getTiles();
    }

    @Test
    public void testDefaultParameterValueString()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {

        //
        // Get the resource.
        //
        final String testFile =
                TestData.file(this, "goodpyramid/" + TEST_FILE).getCanonicalPath(); //
        assertNotNull(testFile);

        //
        // Get a reader
        //
        final AbstractGridFormat format = new ImagePyramidFormat();
        assertTrue(format.accepts(testFile));
        final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
        assertNotNull(reader);

        //
        // Show the coverage
        //
        final GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
        assertNotNull("Null value returned instead of a coverage", coverage);
        assertTrue(
                "coverage dimensions different from what we expected",
                coverage.getGridGeometry().getGridRange().getSpan(0) == 250
                        && coverage.getGridGeometry().getGridRange().getSpan(1) == 250);
        if (TestData.isInteractiveTest()) coverage.show("testDefaultParameterValueString");
        else
            PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage())
                    .getTiles();
    }

    @Test
    public void testForErrors()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {
        //
        // Get the resource.
        //
        final File testFile = TestData.file(this, "goodpyramid/" + TEST_FILE); //
        assertNotNull(testFile);

        //
        // Null argument
        //
        ImagePyramidReader reader = null;
        try {
            reader =
                    new ImagePyramidReader(
                            null, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        } catch (DataSourceException e) {

        }
        assertNull(reader);

        //
        // Illegal arguments
        //
        try {
            reader =
                    new ImagePyramidReader(
                            new FileInputStream(testFile),
                            new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        } catch (DataSourceException e) {

        }
        assertNull(reader);
        try {
            reader =
                    new ImagePyramidReader(
                            ImageIO.createImageInputStream(testFile),
                            new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        } catch (DataSourceException e) {

        }
        assertNull(reader);
    }

    @Test
    public void testComplete()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {

        //
        // Get the resource.
        //
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE);
        assertNotNull(testFile);

        //
        // Get a reader
        //
        final ImagePyramidReader reader =
                new ImagePyramidReader(
                        testFile, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        assertNotNull(reader);

        //
        // alpha on output
        //
        final ParameterValue<Color> transp =
                ImageMosaicFormat.INPUT_TRANSPARENT_COLOR.createValue();
        transp.setValue(Color.black);

        //
        // Show the coverage
        //
        GridCoverage2D coverage =
                (GridCoverage2D) reader.read(new GeneralParameterValue[] {transp});
        assertNotNull(coverage);
        assertTrue(
                "coverage dimensions different from what we expected",
                coverage.getGridGeometry().getGridRange().getSpan(0) == 250
                        && coverage.getGridGeometry().getGridRange().getSpan(1) == 250);
        if (TestData.isInteractiveTest()) coverage.show("testComplete");
        else
            PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage())
                    .getTiles();
    }

    /**
     * This is related to http://jira.codehaus.org/browse/GEOS-4081 and happens only if the
     * requested envelope is overlapping with the pyramid envelope for way less than a pixel
     */
    @Test
    public void testRequestOutsideBounds()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {
        // grab the reader
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE);
        assertNotNull(testFile);
        final ImagePyramidReader reader =
                new ImagePyramidReader(
                        testFile, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        assertNotNull(reader);

        // prepare a request that crosses the bounds for a really minimal part
        GeneralEnvelope ge = reader.getOriginalEnvelope();
        ReferencedEnvelope requestedEnvelope =
                new ReferencedEnvelope(
                        ge.getMinimum(0) - 5,
                        ge.getMinimum(0),
                        ge.getMinimum(1),
                        ge.getMaximum(1),
                        ge.getCoordinateReferenceSystem());
        final Parameter<GridGeometry2D> readGG =
                new Parameter<GridGeometry2D>(AbstractGridFormat.READ_GRIDGEOMETRY2D);
        readGG.setValue(new GridGeometry2D(new GridEnvelope2D(0, 0, 400, 400), requestedEnvelope));

        // make sure we get back a null, not an exception
        assertNull(reader.read(new GeneralParameterValue[] {readGG}));
    }

    /**
     * Testing {@link ImagePyramidReader} by cropping requesting a the best possible dimension.
     *
     * <p>The underlying pyramid i made by 4 levels on the same area, more or less italy, with
     * resolution decreasing as a power of 2.
     *
     * <p>Size of the original mosaic is 250,250.
     */
    @Test
    public void testCropHighestLevel()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {
        //
        // Get the resource.
        //
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE);

        //
        // Get a reader
        //
        final AbstractGridFormat format = new ImagePyramidFormat();
        final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
        assertNotNull(reader);
        //
        // crop
        //
        final ParameterValue<GridGeometry2D> gg =
                ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope oldEnvelop = reader.getOriginalEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelop.getLowerCorner().getOrdinate(0),
                            oldEnvelop.getLowerCorner().getOrdinate(1)
                        },
                        new double[] {
                            oldEnvelop.getLowerCorner().getOrdinate(0) + oldEnvelop.getSpan(0) / 2,
                            oldEnvelop.getLowerCorner().getOrdinate(1) + oldEnvelop.getSpan(1) / 2
                        });
        cropEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        gg.setValue(
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(0, 0, 125, 125)), cropEnvelope));

        //
        // Show the coverage
        //
        GridCoverage2D coverage = ((GridCoverage2D) reader.read(new GeneralParameterValue[] {gg}));
        assertNotNull("Null value returned instead of a coverage", coverage);

        // used to match exactly, but now we compute the exact bbox matching the request on the fly
        assertEquals(127, coverage.getGridGeometry().getGridRange().getSpan(0), 5);
        assertEquals(127, coverage.getGridGeometry().getGridRange().getSpan(1), 5);
        if (TestData.isInteractiveTest()) coverage.show("testCropHighestLevel");
        else PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getTiles();
    }

    /**
     * Testing {@link ImagePyramidReader} by cropping requesting a the second better available
     * resolution.
     *
     * <p>The underlying pyramid is made by 4 levels on the same area, more or less italy, with
     * resolution decreasing as a power of 2.
     *
     * <p>Size of the original mosaic is 250,250.
     */
    @Test
    public void testCropLevel1()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {

        //
        // Get the resource.
        //
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE);

        //
        // Get a reader
        //
        final AbstractGridFormat format = new ImagePyramidFormat();
        final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
        assertNotNull(reader);

        //
        // crop
        //
        final ParameterValue<GridGeometry2D> gg =
                ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope oldEnvelop = reader.getOriginalEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelop.getLowerCorner().getOrdinate(0),
                            oldEnvelop.getLowerCorner().getOrdinate(1)
                        },
                        new double[] {
                            oldEnvelop.getLowerCorner().getOrdinate(0) + oldEnvelop.getSpan(0) / 2,
                            oldEnvelop.getLowerCorner().getOrdinate(1) + oldEnvelop.getSpan(1) / 2
                        });
        cropEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        gg.setValue(
                new GridGeometry2D(
                        new GridEnvelope2D(new Rectangle(0, 0, 125, 125)), cropEnvelope));

        //
        // Show the coverage
        //
        GridCoverage2D coverage = ((GridCoverage2D) reader.read(new GeneralParameterValue[] {gg}));
        assertNotNull("Null value returned instead of a coverage", coverage);
        if (TestData.isInteractiveTest()) coverage.show("testCropLevel1");
        else
            PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage())
                    .getTiles();
    }

    /**
     * Testing {@link ImagePyramidReader} by cropping requesting a the third better avialble
     * resolution.
     *
     * <p>The underlying pyramid i made by 4 levels on the same area, more or less italy, with
     * resolution decreasing as a power of 2.
     *
     * <p>Size of the original mosaic is 250,250.
     */
    @Test
    public void testCropLevel2()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {

        // /////////////////////////////////////////////////////////////////
        //
        // Get the resource.
        //
        //
        // /////////////////////////////////////////////////////////////////
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE);

        //
        // Get a reader
        //
        final AbstractGridFormat format = new ImagePyramidFormat();
        final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
        assertNotNull(reader);

        //
        // crop
        //
        final ParameterValue<GridGeometry2D> gg =
                ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope oldEnvelop = reader.getOriginalEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelop.getLowerCorner().getOrdinate(0),
                            oldEnvelop.getLowerCorner().getOrdinate(1)
                        },
                        new double[] {
                            oldEnvelop.getLowerCorner().getOrdinate(0) + oldEnvelop.getSpan(0) / 2,
                            oldEnvelop.getLowerCorner().getOrdinate(1) + oldEnvelop.getSpan(1) / 2
                        });
        cropEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        gg.setValue(
                new GridGeometry2D(new GridEnvelope2D(new Rectangle(0, 0, 62, 62)), cropEnvelope));

        //
        // Show the coverage
        //
        GridCoverage2D coverage = ((GridCoverage2D) reader.read(new GeneralParameterValue[] {gg}));
        assertNotNull("Null value returned instead of a coverage", coverage);
        if (TestData.isInteractiveTest()) coverage.show("testCropLevel2");
        else
            PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage())
                    .getTiles();
    }

    /**
     * Testing {@link ImagePyramidReader} by cropping requesting a the worst available resolution.
     *
     * <p>The underlying pyramid i made by 4 levels on the same area, more or less italy, with
     * resolution decreasing as a power of 2.
     *
     * <p>Size of the original mosaic is 250,250.
     */
    @Test
    public void testCropLevel3()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException {

        // Get the resource.
        //
        final URL testFile = TestData.getResource(this, "goodpyramid/" + TEST_FILE);

        //
        // Get a reader
        //
        final AbstractGridFormat format = new ImagePyramidFormat();
        final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(testFile);
        assertNotNull(reader);

        //
        // crop
        //
        final ParameterValue<GridGeometry2D> gg =
                ImageMosaicFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope oldEnvelop = reader.getOriginalEnvelope();
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {
                            oldEnvelop.getLowerCorner().getOrdinate(0),
                            oldEnvelop.getLowerCorner().getOrdinate(1)
                        },
                        new double[] {
                            oldEnvelop.getLowerCorner().getOrdinate(0) + oldEnvelop.getSpan(0) / 2,
                            oldEnvelop.getLowerCorner().getOrdinate(1) + oldEnvelop.getSpan(1) / 2
                        });
        cropEnvelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        gg.setValue(
                new GridGeometry2D(new GridEnvelope2D(new Rectangle(0, 0, 25, 25)), cropEnvelope));

        //
        // Show the coverage
        //
        GridCoverage2D coverage = ((GridCoverage2D) reader.read(new GeneralParameterValue[] {gg}));
        assertNotNull("Null value returned instead of a coverage", coverage);
        // assertTrue("coverage dimensions different from what we expected",
        // coverage.getGridGeometry().getGridRange().getSpan(0) == 15
        // && coverage.getGridGeometry().getGridRange().getSpan(
        // 1) == 15);
        if (TestData.isInteractiveTest()) coverage.show("testCropLevel3");
        else
            PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage())
                    .getTiles();
    }

    // private final static String TEST_JAR_FILE = "pyramid.jar";

    /** Tests that we recognize gdal_retile structure */
    @Test
    public void badPyramid1() throws IOException {
        final URL sourceDir = TestData.getResource(this, "badpyramid1");
        // now make sure we can actually rebuild the mosaic
        final AbstractGridFormat format = new ImagePyramidFormat();
        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84);
        assertFalse(((ImagePyramidFormat) format).accepts(sourceDir, hints));
        final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(sourceDir, hints);
        assertNull(reader);
    }

    /** Tests that we recognize gdal_retile structure */
    @Test
    public void badPyramid2() throws IOException {
        final URL sourceDir = TestData.getResource(this, "badpyramid2");
        // now make sure we can actually rebuild the mosaic
        final AbstractGridFormat format = new ImagePyramidFormat();
        final Hints hints =
                new Hints(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, DefaultGeographicCRS.WGS84);
        assertFalse(((ImagePyramidFormat) format).accepts(sourceDir, hints));
        final ImagePyramidReader reader = (ImagePyramidReader) format.getReader(sourceDir, hints);
        assertNull(reader);
    }

    @Test
    public void timePyramid()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException,
                    InvalidParameterValueException, ParseException {

        //
        // Get the resource.
        //
        final URL testFile = TestData.getResource(this, "timepyramid/timepyramid.properties");
        assertNotNull(testFile);

        //
        // Get the reader
        //
        final ImagePyramidReader reader = new ImagePyramidReader(testFile);
        assertNotNull(reader);

        assertEquals("true", reader.getMetadataValue("HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue("TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(timeMetadata.split(",")[0], reader.getMetadataValue("TIME_DOMAIN_MINIMUM"));
        assertEquals(
                timeMetadata.split(",")[timeMetadata.split(",").length - 1],
                reader.getMetadataValue("TIME_DOMAIN_MAXIMUM"));

        //
        // alpha on output
        //
        final ParameterValue<Color> transp =
                ImageMosaicFormat.INPUT_TRANSPARENT_COLOR.createValue();
        transp.setValue(Color.black);

        //
        // Show the coverage
        //
        GridCoverage2D coverage =
                (GridCoverage2D) reader.read(new GeneralParameterValue[] {transp});
        assertNotNull(coverage);
        assertTrue(
                "coverage dimensions different from what we expected",
                coverage.getGridGeometry().getGridRange().getSpan(0) == 200
                        && coverage.getGridGeometry().getGridRange().getSpan(1) == 200);
        if (TestData.isInteractiveTest()) coverage.show("testComplete");
        else
            PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage())
                    .getTiles();

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        time.setValue(
                new ArrayList() {
                    {
                        add(
                                new DateRange(
                                        formatD.parse("2004-01-01T00:00:00.000Z"),
                                        formatD.parse("2004-07-01T00:00:00.000Z")));
                    }
                });
        // Testing output coverage for level 0
        TestUtils.checkCoverage(
                reader.getImageMosaicReaderForLevel(0),
                new GeneralParameterValue[] {gg, useJai, time},
                "time test");
    }

    /**
     * This test is designed to test the retrieval of the TIME domain metadata values through {@link
     * org.geotools.gce.imagepyramid.ImagePyramidReader#getMetadataValue(String, String)}. This
     * method is called by GeoServer.
     */
    @Test
    public void timePyramidForGeoserver()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException,
                    InvalidParameterValueException, ParseException {

        //
        // Get the resource.
        //
        final URL testFile = TestData.getResource(this, "timepyramid/timepyramid.properties");
        assertNotNull(testFile);

        //
        // Get the reader
        //
        final ImagePyramidReader reader = new ImagePyramidReader(testFile);
        assertNotNull(reader);
        String coverageName = reader.getGridCoverageNames()[0];
        assertEquals("true", reader.getMetadataValue(coverageName, "HAS_TIME_DOMAIN"));
        final String timeMetadata = reader.getMetadataValue(coverageName, "TIME_DOMAIN");
        assertNotNull(timeMetadata);
        assertEquals(
                timeMetadata.split(",")[0],
                reader.getMetadataValue(coverageName, "TIME_DOMAIN_MINIMUM"));
        assertEquals(
                timeMetadata.split(",")[timeMetadata.split(",").length - 1],
                reader.getMetadataValue(coverageName, "TIME_DOMAIN_MAXIMUM"));

        //
        // alpha on output
        //
        final ParameterValue<Color> transp =
                ImageMosaicFormat.INPUT_TRANSPARENT_COLOR.createValue();
        transp.setValue(Color.black);

        //
        // Show the coverage
        //
        GridCoverage2D coverage =
                (GridCoverage2D) reader.read(new GeneralParameterValue[] {transp});
        assertNotNull(coverage);
        assertTrue(
                "coverage dimensions different from what we expected",
                coverage.getGridGeometry().getGridRange().getSpan(0) == 200
                        && coverage.getGridGeometry().getGridRange().getSpan(1) == 200);
        if (TestData.isInteractiveTest()) coverage.show("testComplete");
        else
            PlanarImage.wrapRenderedImage(((GridCoverage2D) coverage).getRenderedImage())
                    .getTiles();

        // limit yourself to reading just a bit of it
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = reader.getOriginalEnvelope();
        final Dimension dim = new Dimension();
        dim.setSize(
                reader.getOriginalGridRange().getSpan(0) / 2.0,
                reader.getOriginalGridRange().getSpan(1) / 2.0);
        final Rectangle rasterArea = ((GridEnvelope2D) reader.getOriginalGridRange());
        rasterArea.setSize(dim);
        final GridEnvelope2D range = new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));

        final SimpleDateFormat formatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatD.setTimeZone(TimeZone.getTimeZone("GMT"));

        // use imageio with defined tiles
        final ParameterValue<Boolean> useJai = AbstractGridFormat.USE_JAI_IMAGEREAD.createValue();
        useJai.setValue(false);

        // specify time
        final ParameterValue<List> time = ImageMosaicFormat.TIME.createValue();
        time.setValue(
                new ArrayList() {
                    {
                        add(
                                new DateRange(
                                        formatD.parse("2004-01-01T00:00:00.000Z"),
                                        formatD.parse("2004-07-01T00:00:00.000Z")));
                    }
                });
        // Testing output coverage for level 0
        TestUtils.checkCoverage(
                reader.getImageMosaicReaderForLevel(0),
                new GeneralParameterValue[] {gg, useJai, time},
                "time test");
    }

    /** */
    @Test
    public void multicoveragePyramid()
            throws IOException, MismatchedDimensionException, NoSuchAuthorityCodeException,
                    InvalidParameterValueException, ParseException {

        //
        // Get the resource.
        //
        final URL testFile = TestData.getResource(this, "multipyramid");
        File mosaicFolder = URLs.urlToFile(testFile);
        assertNotNull(testFile);
        File[] pyramidLevels =
                mosaicFolder.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
        for (File pyramidLevel : pyramidLevels) {
            cleanFiles(pyramidLevel);
        }
        cleanFiles(mosaicFolder);

        //
        // Get the reader
        //
        final ImagePyramidReader reader =
                new ImagePyramidReader(
                        testFile, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE));
        assertNotNull(reader);
        assertEquals(2, reader.getGridCoverageCount());

        String coverageNames[] = reader.getGridCoverageNames();
        Arrays.sort(coverageNames);
        assertEquals("gray", coverageNames[0]);
        assertEquals("rgb", coverageNames[1]);

        //
        // Get the coverage
        //
        GridCoverage2D coverage = (GridCoverage2D) reader.read(coverageNames[0], null);
        assertNotNull(coverage);
        RenderedImage renderedImage = coverage.getRenderedImage();
        int colorSpaceType = renderedImage.getColorModel().getColorSpace().getType();
        assertEquals(ColorSpace.TYPE_GRAY, colorSpaceType);
        GridEnvelope gridEnvelope = coverage.getGridGeometry().getGridRange();
        assertEquals(20, gridEnvelope.getSpan(0), DELTA);
        assertEquals(20, gridEnvelope.getSpan(1), DELTA);

        coverage = (GridCoverage2D) reader.read(coverageNames[1], null);
        assertNotNull(coverage);
        renderedImage = coverage.getRenderedImage();
        colorSpaceType = renderedImage.getColorModel().getColorSpace().getType();
        assertEquals(ColorSpace.TYPE_RGB, colorSpaceType);
    }
}
