/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.geotiff;

import it.geosolutions.imageio.plugins.tiff.BaselineTIFFTagSet;
import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Map;
import java.util.logging.Logger;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataEncoder.TagSet;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.data.WorldFileReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.projection.AzimuthalEquidistant;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.test.TestData;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/** @author Simone Giannecchini */
public class GeoTiffWriterTest extends Assert {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GeoTiffWriterTest.class);

    private static final double DELTA = 1E-4;

    private void setFinalStaticField(String fieldName, boolean value)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException,
                    IllegalAccessException {
        // Playing with System.Properties and Static boolean fields can raises issues
        // when running Junit tests via Maven, due to initialization orders.
        // So let's change the fields via reflections for these tests
        Field field = GeoTiffFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, value);
    }

    /** Test our ability to write non standard CRS */
    @Test
    public void testDefaultGeographicWGS84() throws Exception {

        // file
        final File geotiff = TestData.temp(this, "test");

        // write down a fake geotiff with non-standard CRS
        GridCoverageFactory factory = new GridCoverageFactory();
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        GridCoverage2D test = factory.create("test", bi, envelope);
        GeoTiffWriter writer = new GeoTiffWriter(geotiff);
        writer.write(test, null);
        writer.dispose();

        // read
        final GeoTiffReader reader = new GeoTiffReader(geotiff);
        if (TestData.isInteractiveTest()) {
            IIOMetadataDumper iIOMetadataDumper =
                    new IIOMetadataDumper(((GeoTiffReader) reader).getMetadata().getRootNode());
            // System.out.println(iIOMetadataDumper.getMetadata());
        }
        assertTrue(
                CRS.findMathTransform(
                                reader.getCoordinateReferenceSystem(),
                                DefaultGeographicCRS.WGS84,
                                true)
                        .isIdentity()); // more lenient towards metadata differences
        assertTrue(
                CRS.equalsIgnoreMetadata(
                        reader.getCoordinateReferenceSystem(), DefaultGeographicCRS.WGS84));
    }

    /** Testing {@link GeoTiffWriter} capabilities to write a cropped coverage. */
    @Test
    //	    @Ignore
    public void testWriteCroppedCoverage()
            throws IllegalArgumentException, IOException, UnsupportedOperationException,
                    ParseException, FactoryException, TransformException {

        // /////////////////////////////////////////////////////////////////////
        //
        // Look for the original coverage that we want to crop.
        //
        // /////////////////////////////////////////////////////////////////////
        final File readdir = TestData.file(GeoTiffWriterTest.class, "");
        final File writedir =
                new File(
                        new StringBuilder(readdir.getAbsolutePath())
                                .append("/testWriter/")
                                .toString());
        writedir.mkdir();
        final File tiff = new File(readdir, "latlon.tiff");
        assert tiff.exists() && tiff.canRead() && tiff.isFile();
        if (TestData.isInteractiveTest()) LOGGER.info(tiff.getAbsolutePath());

        // /////////////////////////////////////////////////////////////////////
        //
        // Create format and reader
        //
        // /////////////////////////////////////////////////////////////////////
        final GeoTiffFormat format = new GeoTiffFormat();
        // getting a reader
        GridCoverageReader reader = format.getReader(tiff);
        assertNotNull(reader);

        // /////////////////////////////////////////////////////////////////////
        //
        // Play with metadata
        //
        // /////////////////////////////////////////////////////////////////////
        IIOMetadataDumper metadataDumper =
                new IIOMetadataDumper(((GeoTiffReader) reader).getMetadata().getRootNode());
        if (TestData.isInteractiveTest()) {
            LOGGER.info(metadataDumper.getMetadata());
        } else metadataDumper.getMetadata();

        // /////////////////////////////////////////////////////////////////////
        //
        // Read the original coverage.
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D gc = (GridCoverage2D) reader.read(null);
        if (TestData.isInteractiveTest()) {
            LOGGER.info(
                    new StringBuilder("Coverage before: ")
                            .append("\n")
                            .append(gc.getCoordinateReferenceSystem().toWKT())
                            .append(gc.getEnvelope().toString())
                            .toString());
        }
        final CoordinateReferenceSystem sourceCRS = gc.getCoordinateReferenceSystem2D();
        final GeneralEnvelope sourceEnvelope = (GeneralEnvelope) gc.getEnvelope();
        final GridGeometry2D sourcedGG = (GridGeometry2D) gc.getGridGeometry();
        final MathTransform sourceG2W = sourcedGG.getGridToCRS(PixelInCell.CELL_CENTER);

        // /////////////////////////////////////////////////////////////////////
        //
        //
        // CROP
        //
        //
        // /////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////
        //
        // Crop the original coverage.
        //
        // /////////////////////////////////////////////////////////////////////
        double xc = sourceEnvelope.getMedian(0);
        double yc = sourceEnvelope.getMedian(1);
        double xl = sourceEnvelope.getSpan(0);
        double yl = sourceEnvelope.getSpan(1);
        final GeneralEnvelope cropEnvelope =
                new GeneralEnvelope(
                        new double[] {xc - xl / 4.0, yc - yl / 4.0},
                        new double[] {xc + xl / 4.0, yc + yl / 4.0});
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(gc);
        param.parameter("Envelope").setValue(cropEnvelope);
        final GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);

        // /////////////////////////////////////////////////////////////////////
        //
        // Check that we got everything correctly after the crop.
        //
        // /////////////////////////////////////////////////////////////////////
        // checking the ranges of the output image.
        final GridGeometry2D croppedGG = (GridGeometry2D) cropped.getGridGeometry();
        final GridEnvelope croppedGR = croppedGG.getGridRange();
        final MathTransform croppedG2W = croppedGG.getGridToCRS(PixelInCell.CELL_CENTER);
        final GeneralEnvelope croppedEnvelope = (GeneralEnvelope) cropped.getEnvelope();
        assertTrue("min x do not match after crop", 29 == croppedGR.getLow(0));
        assertTrue("min y do not match after crop", 30 == croppedGR.getLow(1));
        assertTrue("max x do not match after crop", 90 == croppedGR.getHigh(0) + 1);
        assertTrue("max y do not match after crop", 91 == croppedGR.getHigh(1) + 1);
        // check that the affine transform are the same thing
        assertTrue(
                "The Grdi2World tranformations of the original and the cropped covearage do not match",
                sourceG2W.equals(croppedG2W));
        // check that the envelope is correct
        final GeneralEnvelope expectedEnvelope =
                new GeneralEnvelope(
                        croppedGR,
                        PixelInCell.CELL_CENTER,
                        croppedG2W,
                        cropped.getCoordinateReferenceSystem2D());
        assertTrue(
                "Expected envelope is different from the computed one",
                expectedEnvelope.equals(
                        croppedEnvelope,
                        XAffineTransform.getScale((AffineTransform) croppedG2W) / 2.0,
                        false));

        // release things
        cropped.dispose(true);
        gc.dispose(true);
        try {
            if (reader != null) reader.dispose();
        } catch (Throwable e) {
        }
        // /////////////////////////////////////////////////////////////////////
        //
        //
        // WRITING AND TESTING
        //
        //
        // /////////////////////////////////////////////////////////////////////
        final File writeFile =
                new File(
                        new StringBuilder(writedir.getAbsolutePath())
                                .append(File.separatorChar)
                                .append(cropped.getName().toString())
                                .append(".tiff")
                                .toString());
        final GridCoverageWriter writer = format.getWriter(writeFile);

        try {
            writer.write(cropped, null);
        } catch (IOException e) {
        } finally {
            try {
                writer.dispose();
            } catch (Throwable e) {
            }
        }
        try {
            reader = new GeoTiffReader(writeFile, null);
            assertNotNull(reader);
            gc = (GridCoverage2D) reader.read(null);
            assertNotNull(gc);
            final CoordinateReferenceSystem targetCRS = gc.getCoordinateReferenceSystem2D();
            assertTrue(
                    "Source and Target coordinate reference systems do not match",
                    CRS.equalsIgnoreMetadata(sourceCRS, targetCRS));
            assertEquals(
                    "Read-back and Cropped envelopes do not match",
                    cropped.getEnvelope(),
                    croppedEnvelope);

            if (TestData.isInteractiveTest()) {
                LOGGER.info(
                        new StringBuilder("Coverage after: ")
                                .append("\n")
                                .append(gc.getCoordinateReferenceSystem().toWKT())
                                .append(gc.getEnvelope().toString())
                                .toString());
                if (TestData.isInteractiveTest()) gc.show();
                else PlanarImage.wrapRenderedImage(gc.getRenderedImage()).getTiles();
            }
        } finally {
            try {
                if (reader != null) reader.dispose();
            } catch (Throwable e) {
            }

            if (!TestData.isInteractiveTest()) gc.dispose(true);
        }
    }

    @Test
    //    @Ignore
    public void testWriteGoogleMercator() throws Exception {
        final String google =
                "PROJCS[\"WGS84 / Google Mercator\", GEOGCS[\"WGS 84\", DATUM[\"World Geodetic System 1984\", SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], AUTHORITY[\"EPSG\",\"6326\"]], PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], UNIT[\"degree\", 0.017453292519943295], AUTHORITY[\"EPSG\",\"4326\"]], PROJECTION[\"Mercator (1SP)\", AUTHORITY[\"EPSG\",\"9804\"]], PARAMETER[\"semi_major\", 6378137.0], PARAMETER[\"semi_minor\", 6378137.0], PARAMETER[\"latitude_of_origin\", 0.0], PARAMETER[\"central_meridian\", 0.0], PARAMETER[\"scale_factor\", 1.0], PARAMETER[\"false_easting\", 0.0], PARAMETER[\"false_northing\", 0.0], UNIT[\"m\", 1.0],  AUTHORITY[\"EPSG\",\"900913\"]]";
        final CoordinateReferenceSystem googleCRS = CRS.parseWKT(google);

        //
        // world geotiff
        //
        final File testFile = TestData.file(GeoTiffReaderTest.class, "latlon.tiff");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(testFile));

        // getting a reader
        GeoTiffReader reader = new GeoTiffReader(testFile);

        // reading the coverage
        GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage);
        assertNotNull(coverage.getCoordinateReferenceSystem());
        assertEquals(
                CRS.lookupIdentifier(coverage.getCoordinateReferenceSystem(), true), "EPSG:4267");
        reader.dispose();

        // reproject
        coverage = (GridCoverage2D) Operations.DEFAULT.resample(coverage, googleCRS);

        // get a writer
        final File mercator =
                new File(TestData.file(GeoTiffReaderTest.class, "."), "wms_900913.tif");
        GeoTiffWriter writer = new GeoTiffWriter(mercator);

        writer.write(coverage, null);
        writer.dispose();

        // getting a reader
        reader = new GeoTiffReader(mercator);
        // reading the coverage
        GridCoverage2D coverageMercator = (GridCoverage2D) reader.read(null);
        // check coverage and crs
        assertNotNull(coverageMercator);
        assertNotNull(coverageMercator.getCoordinateReferenceSystem());
        assertTrue(CRS.equalsIgnoreMetadata(coverage.getCoordinateReferenceSystem(), googleCRS));
        assertTrue(
                coverage.getEnvelope2D()
                        .getFrame()
                        .equals(coverageMercator.getEnvelope2D().getFrame()));
        reader.dispose();
        coverage.dispose(true);
        coverage.dispose(true);
    }

    @Test
    //	    @Ignore
    public void testWriteTFW() throws Exception {

        //
        // no crs geotiff
        //
        final File noCrs = TestData.file(GeoTiffReaderTest.class, "no_crs.tif");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(noCrs));

        // hint for CRS
        final CoordinateReferenceSystem crs = CRS.decode("EPSG:32632", true);
        final Hints hint = new Hints();
        hint.put(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM, crs);

        // getting a reader
        GeoTiffReader reader = new GeoTiffReader(noCrs, hint);

        // reading the coverage
        GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage);
        assertNotNull(coverage.getCoordinateReferenceSystem());
        assertEquals(
                CRS.lookupIdentifier(coverage.getCoordinateReferenceSystem(), true), "EPSG:32632");
        reader.dispose();

        // get a writer
        final File noCrsTFW =
                new File(TestData.file(GeoTiffReaderTest.class, "."), "no_crs_tfw.tif");
        GeoTiffWriter writer = new GeoTiffWriter(noCrsTFW);

        final ParameterValue<Boolean> tfw = GeoTiffFormat.WRITE_TFW.createValue();
        tfw.setValue(true);
        writer.write(coverage, new GeneralParameterValue[] {tfw});
        writer.dispose();
        coverage.dispose(true);

        final File finalTFW =
                new File(noCrsTFW.getParent(), noCrsTFW.getName().replace("tif", "tfw"));
        assertTrue(finalTFW.canRead());
        WorldFileReader worldFileReader = new WorldFileReader(finalTFW);
        assertEquals(656217.52088, worldFileReader.getXULC(), DELTA);
        assertEquals(4737594.70047, worldFileReader.getYULC(), DELTA);
        assertEquals(32.08376, worldFileReader.getXPixelSize(), DELTA);
        assertEquals(-32.0590, worldFileReader.getYPixelSize(), DELTA);
        assertEquals(0, worldFileReader.getRotationX(), DELTA);
        assertEquals(0, worldFileReader.getRotationY(), DELTA);
    }

    @Test
    //    @Ignore
    public void testWriteWithMetadata() throws Exception {

        //
        // no crs geotiff
        //
        final File input = TestData.file(GeoTiffReaderTest.class, "geo.tiff");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(input));

        // getting a reader
        GeoTiffReader reader = new GeoTiffReader(input);

        // reading the coverage
        GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage);
        assertNotNull(coverage.getCoordinateReferenceSystem());
        reader.dispose();

        // get a writer
        final File output =
                new File(TestData.file(GeoTiffReaderTest.class, "."), "outMetadata.tif");
        GeoTiffWriter writer = new GeoTiffWriter(output);

        // Setting a COPYRIGHT metadata
        String copyrightInfo = "(C) GEOTOOLS sample writer";
        String software = "GeoTools Coverage Writer test";

        writer.setMetadataValue(Integer.toString(BaselineTIFFTagSet.TAG_COPYRIGHT), copyrightInfo);
        writer.setMetadataValue(
                TagSet.BASELINE + ":" + Integer.toString(BaselineTIFFTagSet.TAG_SOFTWARE),
                software);

        writer.write(coverage, null);
        writer.dispose();
        coverage.dispose(true);

        // getting a reader
        reader = new GeoTiffReader(output);

        GeoTiffIIOMetadataDecoder metadata = reader.getMetadata();
        String readSoftware =
                metadata.getAsciiTIFFTag(Integer.toString(BaselineTIFFTagSet.TAG_SOFTWARE));
        assertTrue(software.equalsIgnoreCase(readSoftware));
        String readCopyright =
                metadata.getAsciiTIFFTag(Integer.toString(BaselineTIFFTagSet.TAG_COPYRIGHT));
        assertTrue(copyrightInfo.equalsIgnoreCase(readCopyright));

        reader.dispose();
    }

    @Test
    public void testProgress() throws Exception {

        //
        // no crs geotiff
        //
        final File input = TestData.file(GeoTiffReaderTest.class, "geo.tiff");
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(input));

        // getting a reader
        GeoTiffReader reader = new GeoTiffReader(input);

        // reading the coverage
        GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage);
        assertNotNull(coverage.getCoordinateReferenceSystem());
        reader.dispose();

        // get a writer
        final File output =
                new File(TestData.file(GeoTiffReaderTest.class, "."), "outMetadata.tif");
        GeoTiffWriter writer = new GeoTiffWriter(output);

        // Setting a COPYRIGHT metadata
        ParameterValue<ProgressListener> listener = GeoTiffFormat.PROGRESS_LISTENER.createValue();

        listener.setValue(
                new ProgressListener() {
                    private boolean canceled;

                    @Override
                    public InternationalString getTask() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public void setTask(InternationalString task) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void started() {
                        LOGGER.info("started");
                    }

                    @Override
                    public void progress(float percent) {
                        LOGGER.info("progress:" + percent);
                        assertTrue(percent >= 0);
                        assertTrue(percent <= 100);
                        if (percent > 50) setCanceled(true);
                    }

                    @Override
                    public float getProgress() {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void complete() {
                        LOGGER.info("complete");
                    }

                    @Override
                    public void dispose() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public boolean isCanceled() {
                        return canceled;
                    }

                    @Override
                    public void setCanceled(boolean cancel) {
                        canceled = cancel;
                    }

                    @Override
                    public void warningOccurred(String source, String location, String warning) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void exceptionOccurred(Throwable exception) {
                        // TODO Auto-generated method stub

                    }
                });
        writer.write(coverage, new GeneralParameterValue[] {listener});
        writer.dispose();
        coverage.dispose(true);
    }

    @Test
    public void testWriteLatLon() throws Exception {

        String file = "latlon.tiff";

        final File input = TestData.file(GeoTiffReaderTest.class, file);
        final AbstractGridFormat format = new GeoTiffFormat();
        assertTrue(format.accepts(input));

        // getting a reader
        GeoTiffReader reader = new GeoTiffReader(input);

        // reading the coverage
        GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

        // check coverage and crs
        assertNotNull(coverage);
        assertNotNull(coverage.getCoordinateReferenceSystem());
        reader.dispose();

        // get a writer
        final File writedir = new File("./target", "testWriter");
        writedir.mkdirs();
        final File output = new File(writedir, "target.tif");
        GeoTiffWriter writer = new GeoTiffWriter(output);

        ParameterValue<Boolean> value = GeoTiffFormat.RETAIN_AXES_ORDER.createValue();
        value.setValue(true);

        // switching axes
        final CoordinateReferenceSystem latLon4267 = CRS.decode("EPSG:4267");
        assertEquals(CRS.getAxisOrder(latLon4267), AxisOrder.NORTH_EAST);
        final GeneralEnvelope envelope =
                (GeneralEnvelope) CRS.transform(coverage.getEnvelope(), latLon4267);
        envelope.setCoordinateReferenceSystem(latLon4267);

        coverage =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints())
                        .create(coverage.getName(), coverage.getRenderedImage(), envelope);

        writer.write(coverage, new GeneralParameterValue[] {value});
        writer.dispose();
        coverage.dispose(true);

        // getting a reader
        reader = new GeoTiffReader(output, null); // this way I do not impose the lonlat ordering
        final GridCoverage2D gc = reader.read(null);
        final MathTransform g2w_ = gc.getGridGeometry().getGridToCRS();
        assertTrue(g2w_ instanceof AffineTransform2D);
        AffineTransform2D g2w = (AffineTransform2D) g2w_;
        assertTrue(XAffineTransform.getSwapXY(g2w) == -1);
        assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(gc.getCoordinateReferenceSystem()));
        RenderedImage ri = gc.getRenderedImage();
        assertEquals(ri.getWidth(), 120);
        assertEquals(ri.getHeight(), 121);
        assertTrue(
                ((GeneralEnvelope) gc.getEnvelope())
                        .equals(
                                coverage.getEnvelope(),
                                XAffineTransform.getScaleX0(g2w) * 1E-1,
                                false));
        reader.dispose();
    }

    @Test
    //    @Ignore
    public void testWriteBigTiff() throws Exception {

        String files[] = new String[] {"geo.tiff", "no_crs_no_envelope.tif"};

        int i = 0;
        for (String file : files) {
            final File input = TestData.file(GeoTiffReaderTest.class, file);
            final AbstractGridFormat format = new GeoTiffFormat();
            assertTrue(format.accepts(input));

            // getting a reader
            GeoTiffReader reader = new GeoTiffReader(input);

            // reading the coverage
            GridCoverage2D coverage = (GridCoverage2D) reader.read(null);

            // check coverage and crs
            assertNotNull(coverage);
            assertNotNull(coverage.getCoordinateReferenceSystem());
            reader.dispose();

            // get a writer
            final File output =
                    new File(TestData.file(GeoTiffReaderTest.class, "."), "bigtiff" + i + ".tif");
            GeoTiffWriter writer = new GeoTiffWriter(output);

            GeoTiffWriteParams params = new GeoTiffWriteParams();
            params.setForceToBigTIFF(true);
            ParameterValue<GeoToolsWriteParams> value =
                    GeoTiffFormat.GEOTOOLS_WRITE_PARAMS.createValue();
            value.setValue(params);

            writer.write(coverage, new GeneralParameterValue[] {value});
            writer.dispose();
            coverage.dispose(true);

            // getting a reader
            reader = new GeoTiffReader(output);
            RenderedImage ri = reader.read(null).getRenderedImage();
            assertEquals(ri.getWidth(), i == 0 ? 120 : 12);
            assertEquals(ri.getHeight(), i == 0 ? 120 : 12);
            reader.dispose();

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(output);

                byte[] bytes = new byte[6];
                fis.read(bytes);
                if (bytes[0] == 77 && bytes[1] == 77) {
                    // Big Endian Case
                    assertEquals(bytes[3], 43); // 43 is the magic number of BigTiff
                } else {
                    // Little Endian Case
                    assertEquals(bytes[4], 43); // 43 is the magic number of BigTiff
                }
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Throwable t) {

                    }
                }
            }
            i++;
        }
    }

    /** Test writing and reading a GeoTIFF with an {@link AzimuthalEquidistant} projection. */
    @Test
    public void testWriteReadAzimuthalEquidistant() throws Exception {
        // @formatter:off
        CoordinateReferenceSystem crs =
                CRS.parseWKT(
                        "PROJCS[\"unnamed\", "
                                + "GEOGCS[\"unnamed ellipse\", "
                                + "DATUM[\"unknown\", SPHEROID[\"unnamed\",6370841.391468334,0]], "
                                + "PRIMEM[\"Greenwich\",0], "
                                + "UNIT[\"degree\",0.0174532925199433]], "
                                + "PROJECTION[\"Azimuthal_Equidistant\"], "
                                + "PARAMETER[\"latitude_of_center\",42.42], "
                                + "PARAMETER[\"longitude_of_center\",16.16], "
                                + "PARAMETER[\"false_easting\",100000], "
                                + "PARAMETER[\"false_northing\",200000],"
                                + "UNIT[\"metre\", 1, AUTHORITY[\"EPSG\",\"9001\"]]]");
        // @formatter:on
        double range = 100;
        ReferencedEnvelope envelope = new ReferencedEnvelope(-range, range, -range, range, crs);
        GridCoverageFactory f = new GridCoverageFactory();
        GridCoverage2D coverage =
                f.create(
                        "test-azimuthal-equidistant",
                        new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB),
                        envelope,
                        null,
                        null,
                        null);
        // test that writing succeeds without an exception
        File outputDir = new File("./target", "test-azimuthal-equidistant");
        outputDir.mkdirs();
        File output = new File(outputDir, "test-azimuthal-equidistant.tif");
        GeoTiffFormat format = new GeoTiffFormat();
        GridCoverageWriter writer = format.getWriter(output);
        writer.write(coverage, null);
        writer.dispose();
        coverage.dispose(true);
        // test that reading succeeds and that the projection name and parameters are correct
        GeoTiffReader reader = format.getReader(output);
        GridCoverage2D readCoverage = reader.read(null);
        String wkt = readCoverage.getCoordinateReferenceSystem().toWKT();
        readCoverage.dispose(true);
        reader.dispose();
        org.junit.Assert.assertTrue(wkt.contains("PROJECTION[\"Azimuthal_Equidistant\"]"));
        org.junit.Assert.assertTrue(wkt.contains("PARAMETER[\"latitude_of_center\", 42.42]"));
        org.junit.Assert.assertTrue(wkt.contains("PARAMETER[\"longitude_of_center\", 16.16]"));
        org.junit.Assert.assertTrue(wkt.contains("PARAMETER[\"false_easting\", 100000.0]"));
        org.junit.Assert.assertTrue(wkt.contains("PARAMETER[\"false_northing\", 200000.0]"));
    }

    @Test
    public void testWriteNoData() throws Exception {

        // Input geotiff including noData information
        final File input = TestData.file(GeoTiffReaderTest.class, "nodata.tiff");
        final double noDataValue = -9999;

        // reading the coverage, checking it has nodata
        GeoTiffReader reader = new GeoTiffReader(input);
        GridCoverage2D coverage = (GridCoverage2D) reader.read(null);
        Map props = coverage.getProperties();

        assertTrue(props.containsKey(NoDataContainer.GC_NODATA));
        NoDataContainer nodata = (NoDataContainer) props.get(NoDataContainer.GC_NODATA);
        assertEquals(noDataValue, nodata.getAsSingleValue(), 1e-6);
        reader.dispose();

        // Writing it, including noData
        writeAndRead(coverage, Boolean.TRUE, noDataValue);

        // Writing it again, excluding noData this time
        writeAndRead(coverage, Boolean.FALSE, noDataValue);

        // Writing it one more time, this time with no parameter being specified
        // but using the default value being set by system properties
        setFinalStaticField("DEFAULT_WRITE_NODATA", false);
        writeAndRead(coverage, null, noDataValue);
        setFinalStaticField("DEFAULT_WRITE_NODATA", true);
    }

    private void writeAndRead(GridCoverage2D coverage, Boolean writeNoDataParam, double noDataValue)
            throws IOException {
        String suffix = "outputWithNodataParam";
        suffix += (writeNoDataParam != null ? (writeNoDataParam.toString()) : "none");
        final File output = new File(TestData.file(GeoTiffReaderTest.class, "."), suffix + ".tif");
        GeoTiffWriter writer = new GeoTiffWriter(output);
        GeneralParameterValue[] writeParams = null;
        if (writeNoDataParam != null) {
            ParameterValue<Boolean> writeNodata = GeoTiffFormat.WRITE_NODATA.createValue();
            writeNodata.setValue(writeNoDataParam);
            writeParams = new GeneralParameterValue[] {writeNodata};
        }
        writer.write(coverage, writeParams);
        writer.dispose();
        coverage.dispose(true);

        // Reading it back
        GeoTiffReader reader = new GeoTiffReader(output);
        coverage = (GridCoverage2D) reader.read(null);
        Map props = coverage.getProperties();
        if (writeNoDataParam == null) {
            writeNoDataParam = GeoTiffFormat.WRITE_NODATA.getDefaultValue();
        }
        // checking that nodata exists/not exists if the writing params is true/false
        assertTrue(props.containsKey(NoDataContainer.GC_NODATA) == writeNoDataParam.booleanValue());
        if (writeNoDataParam) {
            NoDataContainer nodata = (NoDataContainer) props.get(NoDataContainer.GC_NODATA);
            assertEquals(noDataValue, nodata.getAsSingleValue(), 1e-6);
        }
        reader.dispose();
    }
}
