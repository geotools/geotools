/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sun.media.jai.util.CacheDiagnostics;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.TileCache;
import org.geotools.TestData;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.Position;
import org.geotools.api.metadata.spatial.PixelOrientation;
import org.geotools.api.parameter.InvalidParameterValueException;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.operation.Mosaic;
import org.geotools.coverage.processing.operation.Mosaic.GridGeometryPolicy;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.data.WorldFileReader;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the {@link Mosaic} operation. The tests ensures that the final {@link GridCoverage2D} created
 * contains the union of the input bounding box or is equal to that provided by the external {@link GridGeometry2D}.
 * Also the tests check if the output {@link GridCoverage2D} resolution is equal to that imposed in input with the
 * {@link GridGeometryPolicy}.
 *
 * @author Nicola Lagomarsini GesoSolutions S.A.S.
 */
public class MosaicTest extends GridProcessingTestBase {

    private static final GridCoverageFactory GRID_COVERAGE_FACTORY = CoverageFactoryFinder.getGridCoverageFactory(null);

    /** Tolerance value for the double comparison */
    private static final double TOLERANCE = 0.01d;

    /** Tile size used for testing the Mosaic */
    private static final int TILE_SIZE = 10;

    /**
     * WKT used for testing that the operation throws an exception when the input {@link GridCoverage2D}s does not have
     * the same {@link CoordinateReferenceSystem}.
     */
    private static final String GOOGLE_MERCATOR_WKT = "PROJCS[\"WGS 84 / Pseudo-Mercator\","
            + "GEOGCS[\"Popular Visualisation CRS\","
            + "DATUM[\"Popular_Visualisation_Datum\","
            + "SPHEROID[\"Popular Visualisation Sphere\",6378137,0,"
            + "AUTHORITY[\"EPSG\",\"7059\"]],"
            + "TOWGS84[0,0,0,0,0,0,0],"
            + "AUTHORITY[\"EPSG\",\"6055\"]],"
            + "PRIMEM[\"Greenwich\",0,"
            + "AUTHORITY[\"EPSG\",\"8901\"]],"
            + "UNIT[\"degree\",0.01745329251994328,"
            + "AUTHORITY[\"EPSG\",\"9122\"]],"
            + "AUTHORITY[\"EPSG\",\"4055\"]],"
            + "UNIT[\"metre\",1,"
            + "AUTHORITY[\"EPSG\",\"9001\"]],"
            + "PROJECTION[\"Mercator_1SP\"],"
            + "PARAMETER[\"central_meridian\",0],"
            + "PARAMETER[\"scale_factor\",1],"
            + "PARAMETER[\"false_easting\",0],"
            + "PARAMETER[\"false_northing\",0],"
            + "AUTHORITY[\"EPSG\",\"3785\"],"
            + "AXIS[\"X\",EAST],"
            + "AXIS[\"Y\",NORTH]]";

    /** First Coverage used */
    private static GridCoverage2D coverage1;

    /** Second Coverage used. It is equal to the first one but translated on the X axis. */
    private static GridCoverage2D coverage2;

    /** Third Coverage used. It is equal to the first one but contains an alpha band */
    private static GridCoverage2D coverage3;

    /** Fourth Coverage used. It is equal to the second one but contains an alpha band */
    private static GridCoverage2D coverage4;

    /** The processor to be used for all tests. */
    private static final CoverageProcessor processor = CoverageProcessor.getInstance(GeoTools.getDefaultHints());

    // private static final Mosaic MOSAIC = (Mosaic) processor.getOperation("Mosaic");

    // Static method used for preparing the input data.
    @BeforeClass
    public static void setup() throws FileNotFoundException, IOException {
        TestData.unzipFile(MosaicTest.class, "sampleData.zip");
        coverage1 = readInputFile("sampleData");
        coverage2 = readInputFile("sampleData2");
        coverage3 = readInputFile("sampleData3");
        coverage4 = readInputFile("sampleData4");
    }

    /** Private method for reading the input file. */
    private static GridCoverage2D readInputFile(String filename) throws FileNotFoundException, IOException {
        final File tiff = TestData.file(MosaicTest.class, filename + ".tif");
        final File tfw = TestData.file(MosaicTest.class, filename + ".tfw");

        final TIFFImageReader reader = (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader)
                new TIFFImageReaderSpi().createReaderInstance();
        reader.setInput(ImageIO.createImageInputStream(tiff));
        final BufferedImage image = reader.read(0);
        final MathTransform transform = new WorldFileReader(tfw).getTransform();
        final GridCoverage2D coverage2D = GRID_COVERAGE_FACTORY.create(
                "coverage" + filename,
                image,
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(image).getBounds()),
                        transform,
                        DefaultGeographicCRS.WGS84),
                null,
                null,
                null);
        return coverage2D;
    }

    // Simple test which mosaics two input coverages without any additional parameter
    @Test
    public void testMosaicSimple() {
        GridCoverage2D mosaic = simpleMosaic(coverage1, coverage2);

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(mosaic);
    }

    @Test
    public void testCacheCleanup() {
        // make sure the tile cache is empty
        TileCache tc = JAI.getDefaultInstance().getTileCache();
        tc.flush();

        testMosaicWithAnotherNoData();

        // the cleanup was full, no leftovers
        assertEquals(0, ((CacheDiagnostics) tc).getCacheTileCount());
        assertEquals(0, ((CacheDiagnostics) tc).getCacheMemoryUsed());
    }

    private GridCoverage2D simpleMosaic(GridCoverage2D coverage1, GridCoverage2D coverage2) {
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(coverage1);
        sources.add(coverage2);
        // Setting of the sources
        param.parameter("Sources").setValue(sources);
        // RenderingHints
        Hints hints = new Hints();
        // Ensure No Layout is set
        assertFalse(hints.containsKey(JAI.KEY_IMAGE_LAYOUT));
        // Add a fake Layout for the operation
        ImageLayout il = new ImageLayout();
        hints.put(JAI.KEY_IMAGE_LAYOUT, il);
        il.setTileHeight(TILE_SIZE);
        il.setTileWidth(TILE_SIZE);
        // Mosaic operation
        GridCoverage2D mosaic = (GridCoverage2D) processor.doOperation(param, hints);

        // Check that the final GridCoverage BoundingBox is equal to the union of the separate
        // coverages bounding box
        ReferencedEnvelope expected = coverage1.getEnvelope2D();
        expected.include(coverage2.getEnvelope2D());
        // Mosaic Envelope
        ReferencedEnvelope actual = mosaic.getEnvelope2D();

        // Check the same Bounding Box
        assertEqualBBOX(expected, actual);

        // Check that Tiling has been defined correctly
        RenderedImage renderedImage = mosaic.getRenderedImage();
        assertEquals(TILE_SIZE, renderedImage.getTileHeight());
        assertEquals(TILE_SIZE, renderedImage.getTileWidth());

        // Check that the final Coverage resolution is equal to that of the first coverage
        double initialRes = calculateResolution(coverage1);
        double finalRes = calculateResolution(mosaic);
        double percentual = Math.abs(initialRes - finalRes) / initialRes;
        Assert.assertTrue(percentual < TOLERANCE);

        // Check that on the center of the image there are nodata
        Position point =
                new Position2D(mosaic.getCoordinateReferenceSystem(), actual.getMedian(0), actual.getMedian(1));
        double nodata = CoverageUtilities.getBackgroundValues(coverage1)[0];
        double result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertEquals(nodata, result, TOLERANCE);

        // Check that on the Upper Left border pixel there is valid data
        point = new Position2D(
                mosaic.getCoordinateReferenceSystem(), actual.getMinX() + finalRes, actual.getMinY() + finalRes);
        result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Ensure the Layout is already present after the mosaic
        Assert.assertTrue(hints.containsKey(JAI.KEY_IMAGE_LAYOUT));
        // Ensure no additional bound parameter is set
        ImageLayout layout = (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT);
        assertFalse(layout.isValid(ImageLayout.MIN_X_MASK));
        assertFalse(layout.isValid(ImageLayout.MIN_Y_MASK));
        assertFalse(layout.isValid(ImageLayout.WIDTH_MASK));
        assertFalse(layout.isValid(ImageLayout.HEIGHT_MASK));
        Assert.assertTrue(layout.isValid(ImageLayout.TILE_HEIGHT_MASK));
        Assert.assertTrue(layout.isValid(ImageLayout.TILE_WIDTH_MASK));
        return mosaic;
    }

    @Test
    public void testMosaicSimpleWithNullROI() {

        // mosaic the two coverages, one with a ROI, the other with none (used to blow up)
        GridCoverage2D mosaic = simpleMosaic(getCoverageWithFullROI(coverage1), coverage2);

        // check we have a ROI and it's the union of the two
        ROI roi = CoverageUtilities.getROIProperty(mosaic);
        assertNotNull(roi);
        Rectangle bounds1 = getImageBounds(coverage1.getRenderedImage());
        Rectangle bounds2 = getImageBounds(coverage2.getRenderedImage());
        Rectangle boundsMosaic = bounds1.union(bounds2);
        assertEquals(boundsMosaic, roi.getBounds());

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(mosaic);
    }

    private GridCoverage2D getCoverageWithFullROI(GridCoverage2D coverage) {
        RenderedImage ri = coverage.getRenderedImage();
        ROIShape roi = new ROIShape(getImageBounds(ri));
        return getCoverageWithROI(coverage, roi);
    }

    private GridCoverage2D getCoverageWithROI(GridCoverage2D coverage, ROI roi) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = coverage.getProperties() != null ? coverage.getProperties() : Collections.emptyMap();
        Map<String, Object> properties = new HashMap<>(map);
        CoverageUtilities.setROIProperty(properties, roi);
        GridCoverage2D coverageWithRoi = GRID_COVERAGE_FACTORY.create(
                coverage.getName(),
                coverage.getRenderedImage(),
                coverage.getEnvelope(),
                coverage.getSampleDimensions(),
                null,
                properties);
        return coverageWithRoi;
    }

    private Rectangle getImageBounds(RenderedImage ri) {
        return new Rectangle(ri.getMinX(), ri.getMinY(), ri.getWidth(), ri.getHeight());
    }

    // Simple test which tries to mosaic an input coverage without settings sources parameter
    @Test(expected = ParameterNotFoundException.class)
    public void testMosaicNoSource() {
        /*
         * Getting parameters
         */
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();
        // Setting source0 parameter
        param.parameter("source0").setValue(coverage1);
    }

    // Simple test which mosaics two input coverages with a different value for the output nodata
    @Test
    public void testMosaicWithAnotherNoData() {
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(coverage1);
        sources.add(coverage2);
        // Setting of the sources
        param.parameter("Sources").setValue(sources);
        // Setting of the nodata
        double nodata = -9999;
        param.parameter(Mosaic.OUTNODATA_NAME).setValue(new double[] {nodata});
        // Mosaic
        GridCoverage2D mosaic = (GridCoverage2D) processor.doOperation(param);

        // Check that the final GridCoverage BoundingBox is equal to the union of the separate
        // coverages bounding box
        ReferencedEnvelope expected = coverage1.getEnvelope2D();
        expected.include(coverage2.getEnvelope2D());
        // Mosaic Envelope
        ReferencedEnvelope actual = mosaic.getEnvelope2D();

        // Check the same Bounding Box
        assertEqualBBOX(expected, actual);

        // Check that the final Coverage resolution is equal to that of the first coverage
        double initialRes = calculateResolution(coverage1);
        double finalRes = calculateResolution(mosaic);
        double percentual = Math.abs(initialRes - finalRes) / initialRes;
        Assert.assertTrue(percentual < TOLERANCE);

        // Check that on the center of the image there are nodata
        Position point =
                new Position2D(mosaic.getCoordinateReferenceSystem(), actual.getCenterX(), actual.getCenterY());
        double result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertEquals(nodata, result, TOLERANCE);

        // Check that on the Upper Left border pixel there is valid data
        point = new Position2D(
                mosaic.getCoordinateReferenceSystem(), actual.getMinX() + finalRes, actual.getMinY() + finalRes);
        result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(mosaic);
    }

    // Test which mosaics two input coverages with alpha band
    @Test
    public void testMosaicWithAlpha() {
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(coverage3);
        sources.add(coverage4);
        // Setting of the sources
        param.parameter("Sources").setValue(sources);
        // Mosaic
        GridCoverage2D mosaic = (GridCoverage2D) processor.doOperation(param);

        // Check that the final Coverage Bands are 2
        Assert.assertEquals(2, mosaic.getNumSampleDimensions());

        // Check that the final GridCoverage BoundingBox is equal to the union of the separate
        // coverages bounding box
        ReferencedEnvelope expected = coverage3.getEnvelope2D();
        expected.include(coverage4.getEnvelope2D());
        // Mosaic Envelope
        ReferencedEnvelope actual = mosaic.getEnvelope2D();

        // Check the same Bounding Box
        assertEqualBBOX(expected, actual);

        // Check that the final Coverage resolution is equal to that of the first coverage
        double initialRes = calculateResolution(coverage3);
        double finalRes = calculateResolution(mosaic);
        double percentual = Math.abs(initialRes - finalRes) / initialRes;
        Assert.assertTrue(percentual < TOLERANCE);

        // Check that on the center of the image there are nodata
        Position point =
                new Position2D(mosaic.getCoordinateReferenceSystem(), actual.getCenterX(), actual.getCenterY());
        double nodata = CoverageUtilities.getBackgroundValues(coverage1)[0];
        double result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertEquals(nodata, result, TOLERANCE);

        // Check that on the Upper Left border pixel there is valid data
        point = new Position2D(
                mosaic.getCoordinateReferenceSystem(), actual.getMinX() + finalRes, actual.getMinY() + finalRes);
        result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(mosaic);
    }

    // Test which mosaics two input coverages and resamples them by using the resolution of an
    // external GridGeometry2D
    @Test
    public void testMosaicExternalGeometry() {
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(coverage1);
        sources.add(coverage2);
        // Setting of the sources
        param.parameter("Sources").setValue(sources);

        // Initial Bounding box
        ReferencedEnvelope startBBOX = coverage1.getEnvelope2D();
        startBBOX.include(coverage2.getEnvelope2D());
        ReferencedEnvelope expected = new ReferencedEnvelope(startBBOX);
        Point2D pt = new Point2D.Double(startBBOX.getMaxX() + 1, startBBOX.getMaxY() + 1);
        expected.expandToInclude(pt);
        // External GridGeometry
        GridGeometry2D ggStart = new GridGeometry2D(
                PixelInCell.CELL_CORNER,
                coverage1.getGridGeometry().getGridToCRS2D(PixelOrientation.UPPER_LEFT),
                expected,
                GeoTools.getDefaultHints());

        param.parameter("geometry").setValue(ggStart);
        // Mosaic
        GridCoverage2D mosaic = (GridCoverage2D) processor.doOperation(param);

        // Check that the final GridCoverage BoundingBox is equal to the bounding box provided in
        // input

        // Mosaic Envelope
        ReferencedEnvelope actual = mosaic.getEnvelope2D();

        // Check the same Bounding Box
        assertEqualBBOX(expected, actual);

        // Check that the final Coverage resolution is equal to that of the first coverage
        double initialRes = calculateResolution(coverage1);
        double finalRes = calculateResolution(mosaic);
        Assert.assertEquals(initialRes, finalRes, TOLERANCE);

        // Check that on the Upper Right pixel of the image there are nodata
        Position point = new Position2D(
                mosaic.getCoordinateReferenceSystem(), actual.getMinX() + finalRes, actual.getMaxY() - finalRes);
        double nodata = CoverageUtilities.getBackgroundValues(coverage1)[0];
        double result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertEquals(nodata, result, TOLERANCE);

        // Check that on the Upper Left border pixel there is valid data
        point = new Position2D(
                mosaic.getCoordinateReferenceSystem(), actual.getMinX() + finalRes, actual.getMinY() + finalRes);
        result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(mosaic);
    }

    // Test which mosaics two input coverages and tries to impose a null GridGeometry. An exception
    // will be thrown
    @Test(expected = CoverageProcessingException.class)
    public void testMosaicNoExternalGeometry() {
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(coverage1);
        sources.add(coverage2);
        // Setting of the sources
        param.parameter("Sources").setValue(sources);
        param.parameter(Mosaic.POLICY).setValue("external");
        // Mosaic
        processor.doOperation(param);
    }

    // Test which mosaics two input coverages and creates a final GridCoverage with the worst
    // resolution between those of the input GridCoverages
    @Test
    public void testMosaicCoarseResolution() {
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(coverage1);

        // Resampling of the second Coverage to an higher resolution
        ParameterValueGroup paramResampling = processor.getOperation("resample").getParameters();
        paramResampling.parameter("Source").setValue(coverage2);
        GridEnvelope2D gridRange = coverage2.getGridGeometry().getGridRange2D();
        gridRange.add(gridRange.getMaxX() + 100, gridRange.getMaxY() + 100);
        GridGeometry2D ggNew = new GridGeometry2D(gridRange, coverage2.getEnvelope());
        paramResampling.parameter("GridGeometry").setValue(ggNew);
        GridCoverage2D resampled = (GridCoverage2D) processor.doOperation(paramResampling);

        sources.add(resampled);
        // Setting of the sources
        param.parameter("Sources").setValue(sources);
        param.parameter(Mosaic.POLICY).setValue("coarse");
        // Mosaic
        GridCoverage2D mosaic = (GridCoverage2D) processor.doOperation(param);

        // Check that the final GridCoverage BoundingBox is equal to the union of the separate
        // coverages bounding box
        ReferencedEnvelope expected = coverage1.getEnvelope2D();
        expected.include(resampled.getEnvelope2D());
        // Mosaic Envelope
        ReferencedEnvelope actual = mosaic.getEnvelope2D();

        // Check the same Bounding Box
        assertEqualBBOX(expected, actual);

        // Check that the final Coverage resolution is equal to that of the first coverage
        double initialRes = calculateResolution(coverage1);
        double finalRes = calculateResolution(mosaic);
        double percentual = Math.abs(initialRes - finalRes) / initialRes;
        Assert.assertTrue(percentual < TOLERANCE);

        // Check that on the center of the image there are nodata
        Position point =
                new Position2D(mosaic.getCoordinateReferenceSystem(), actual.getCenterX(), actual.getCenterY());
        double nodata = CoverageUtilities.getBackgroundValues(coverage1)[0];
        double result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertEquals(nodata, result, TOLERANCE);

        // Check that on the Upper Left border pixel there is valid data
        point = new Position2D(
                mosaic.getCoordinateReferenceSystem(), actual.getMinX() + finalRes, actual.getMinY() + finalRes);
        result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(resampled);
        disposeCoveragePlanarImage(mosaic);
    }

    // Test which mosaics two input coverages and creates a final GridCoverage with the best
    // resolution between those of the input GridCoverages
    @Test
    public void testMosaicFineResolution() {
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(coverage1);

        // Resampling of the second Coverage to an higher resolution
        ParameterValueGroup paramResampling = processor.getOperation("resample").getParameters();
        paramResampling.parameter("Source").setValue(coverage2);
        GridEnvelope2D gridRange = coverage2.getGridGeometry().getGridRange2D();
        gridRange.add(gridRange.getMaxX() + 100, gridRange.getMaxY() + 100);
        GridGeometry2D ggNew = new GridGeometry2D(gridRange, coverage2.getEnvelope());
        paramResampling.parameter("GridGeometry").setValue(ggNew);
        GridCoverage2D resampled = (GridCoverage2D) processor.doOperation(paramResampling);

        sources.add(resampled);
        // Setting of the sources
        param.parameter("Sources").setValue(sources);
        param.parameter(Mosaic.POLICY).setValue("fine");
        // Mosaic
        GridCoverage2D mosaic = (GridCoverage2D) processor.doOperation(param);

        // Check that the final GridCoverage BoundingBox is equal to the union of the separate
        // coverages bounding box
        ReferencedEnvelope expected = coverage1.getEnvelope2D();
        expected.include(resampled.getEnvelope2D());
        // Mosaic Envelope
        ReferencedEnvelope actual = mosaic.getEnvelope2D();

        // Check the same Bounding Box
        assertEqualBBOX(expected, actual);

        // Check that the final Coverage resolution is equal to that of the second coverage
        double initialRes = calculateResolution(resampled);
        double finalRes = calculateResolution(mosaic);
        double percentual = Math.abs(initialRes - finalRes) / initialRes;
        Assert.assertTrue(percentual < TOLERANCE);

        // Check that on the center of the image there are nodata
        Position point =
                new Position2D(mosaic.getCoordinateReferenceSystem(), actual.getCenterX(), actual.getCenterY());
        double nodata = CoverageUtilities.getBackgroundValues(coverage1)[0];
        double result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertEquals(nodata, result, TOLERANCE);

        // Check that on the Upper Left border pixel there is valid data
        point = new Position2D(
                mosaic.getCoordinateReferenceSystem(), actual.getMinX() + finalRes, actual.getMinY() + finalRes);
        result = ((int[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(mosaic);
        disposeCoveragePlanarImage(resampled);
    }

    // Test which mosaics two input coverages with different CRS. An exception will be thrown
    @Test(expected = CoverageProcessingException.class)
    public void testWrongCRS() throws InvalidParameterValueException, ParameterNotFoundException, FactoryException {
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(coverage1);

        // Reprojection of the second Coverage
        ParameterValueGroup paramReprojection =
                processor.getOperation("resample").getParameters();
        paramReprojection.parameter("Source").setValue(coverage2);
        paramReprojection.parameter("CoordinateReferenceSystem").setValue(CRS.parseWKT(GOOGLE_MERCATOR_WKT));
        GridCoverage2D resampled = (GridCoverage2D) processor.doOperation(paramReprojection);

        sources.add(resampled);
        // Setting of the sources
        param.parameter("Sources").setValue(sources);
        // Mosaic
        processor.doOperation(param);
    }

    // Test which takes an input file, extracts two coverages from it, shifts the first on the right
    // of the second one and then mosaics them.
    @Test
    public void testWorldFile() throws FileNotFoundException, IOException {
        // read the coverage
        GridCoverage2D test = readInputFile("sample0");
        // Envelope for the first half of the image
        ReferencedEnvelope re1 = new ReferencedEnvelope(10, 180, -90, 90, DefaultGeographicCRS.WGS84);
        // Coverage crop for extracting the first half of the image
        GridCoverage2D c1 = crop(test, new GeneralBounds(re1));
        // Envelope for the second half of the image
        ReferencedEnvelope re2 = new ReferencedEnvelope(-180, -10, -90, 90, DefaultGeographicCRS.WGS84);
        // Coverage crop for extracting the second half of the image
        GridCoverage2D c2 = crop(test, new GeneralBounds(re2));

        // Shift the first image on the right
        ReferencedEnvelope re3 = new ReferencedEnvelope(180, 350, -90, 90, DefaultGeographicCRS.WGS84);
        GridCoverage2D shifted = new GridCoverageFactory().create(c2.getName(), c2.getRenderedImage(), re3);
        // Envelope containing the bounding box for the two images
        ReferencedEnvelope reUnion = new ReferencedEnvelope(10, 350, -90, 90, DefaultGeographicCRS.WGS84);
        // Mosaic operation
        GridCoverage2D mosaic =
                mosaic(sortCoverages(Arrays.asList(c1, shifted)), new GeneralBounds(reUnion), new Hints());

        // Ensure the mosaic Bounding box is equal to that expected
        ReferencedEnvelope expected = new ReferencedEnvelope(reUnion);
        assertEqualBBOX(expected, mosaic.getEnvelope2D());

        // Check that the final Coverage resolution is equal to that of the first coverage
        double finalRes = calculateResolution(mosaic);

        // Check that on the center of the image there is valid data
        Position point =
                new Position2D(mosaic.getCoordinateReferenceSystem(), expected.getCenterX(), expected.getCenterY());
        double nodata = 0;
        double result = ((byte[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Check that on the Upper Left border pixel there is valid data
        point = new Position2D(
                mosaic.getCoordinateReferenceSystem(), expected.getMinX() + finalRes, expected.getMinY() + finalRes);
        result = ((byte[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Check that on the Upper Right border pixel there is valid data
        point = new Position2D(
                mosaic.getCoordinateReferenceSystem(), expected.getMaxX() - finalRes, expected.getMinY() + finalRes);
        result = ((byte[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(mosaic);
    }

    // Test which takes an input file, extracts 4 coverages from it, shifts them in order to created
    // a replication.
    @Test
    public void testWorldFile2() throws FileNotFoundException, IOException {
        // read the two coverages
        GridEnvelope2D gridRange = new GridEnvelope2D(0, 0, 400, 200);
        ReferencedEnvelope re = new ReferencedEnvelope(-180, 180, -85, 85, DefaultGeographicCRS.WGS84);
        GridGeometry2D gg = new GridGeometry2D(gridRange, re);

        GridCoverage2D cStart = readInputFile("sample0");
        GridCoverage2D cCrop = crop(cStart, new GeneralBounds(re));

        // Resampling of the Coverage to the defined resolution
        ParameterValueGroup paramResampling = processor.getOperation("resample").getParameters();
        paramResampling.parameter("Source").setValue(cCrop);
        paramResampling.parameter("GridGeometry").setValue(gg);
        GridCoverage2D c = (GridCoverage2D) processor.doOperation(paramResampling);

        // first shifted
        ReferencedEnvelope re2 = new ReferencedEnvelope(-540, -180, -85, 85, DefaultGeographicCRS.WGS84);
        GridCoverage2D c2 = new GridCoverageFactory().create(c.getName(), c.getRenderedImage(), re2);

        // second shifted
        ReferencedEnvelope re3 = new ReferencedEnvelope(180, 540, -85, 85, DefaultGeographicCRS.WGS84);
        GridCoverage2D c3 = new GridCoverageFactory().create(c.getName(), c.getRenderedImage(), re3);

        // third shifted
        ReferencedEnvelope re4 = new ReferencedEnvelope(-540, -900, -85, 85, DefaultGeographicCRS.WGS84);
        GridCoverage2D c4 = new GridCoverageFactory().create(c.getName(), c.getRenderedImage(), re4);

        ReferencedEnvelope reUnion = new ReferencedEnvelope(-900, 540, -85, 85, DefaultGeographicCRS.WGS84);
        List<GridCoverage2D> sorted = sortCoverages(Arrays.asList(c4, c2, c, c3));
        GridCoverage2D mosaic = mosaic(sorted, new GeneralBounds(reUnion), new Hints());

        // Ensure the mosaic Bounding box is equal to that expected
        ReferencedEnvelope expected = new ReferencedEnvelope(reUnion);
        assertEqualBBOX(expected, mosaic.getEnvelope2D());

        // Calculate the mosaic resolution
        double res = calculateResolution(mosaic);

        // Ensure that no black lines are present on the border between the input images
        // Check that on the center of the image there is valid data
        Position point = new Position2D(mosaic.getCoordinateReferenceSystem(), -540, -84);
        double nodata = 0;
        double result = ((byte[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);
        point = new Position2D(mosaic.getCoordinateReferenceSystem(), -540 - res, -84);
        result = ((byte[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);
        point = new Position2D(mosaic.getCoordinateReferenceSystem(), -540 + res, -84);
        result = ((byte[]) mosaic.evaluate(point))[0];
        Assert.assertNotEquals(nodata, result, TOLERANCE);

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(mosaic);
    }

    @Test
    public void testPaletted() throws IOException {
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        GridCoverage2D world = readWorldPaletted();
        sources.add(world);
        ReferencedEnvelope reShifted = new ReferencedEnvelope(-360, -180, -90, 90, DefaultGeographicCRS.WGS84);
        GridCoverage2D shifted = new GridCoverageFactory().create(world.getName(), world.getRenderedImage(), reShifted);
        sources.add(shifted);
        param.parameter("Sources").setValue(sources);
        // Mosaic simulating a hints set that contains index color model expansion
        Hints hints = new Hints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.TRUE);
        GridCoverage2D mosaic = (GridCoverage2D) processor.doOperation(param, hints);
        assertNotNull(mosaic);
        assertEquals(3, mosaic.getRenderedImage().getSampleModel().getNumBands());
    }

    private GridCoverage2D readWorldPaletted() throws IOException {
        File tiff = TestData.copy(this, "geotiff/worldPalette.tiff");
        final TIFFImageReader reader = (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader)
                new TIFFImageReaderSpi().createReaderInstance();
        reader.setInput(ImageIO.createImageInputStream(tiff));
        final BufferedImage image = reader.read(0);
        final MathTransform transform = new GridToEnvelopeMapper(
                        new GridEnvelope2D(0, 0, image.getWidth(), image.getHeight()),
                        new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84))
                .createTransform();
        final GridCoverage2D coverage2D = GRID_COVERAGE_FACTORY.create(
                "world",
                image,
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(image).getBounds()),
                        transform,
                        DefaultGeographicCRS.WGS84),
                null,
                null,
                null);
        return coverage2D;
    }

    @AfterClass
    public static void finalStep() {
        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(coverage1);
        disposeCoveragePlanarImage(coverage2);
    }

    /** Method for disposing the {@link RenderedImage} chain of the {@link GridCoverage2D} */
    private static void disposeCoveragePlanarImage(GridCoverage2D coverage) {
        // Dispose the single planar image since some images are input of other tests and
        // disposing the whole chain may therefore throw exceptions
        ImageUtilities.disposeSinglePlanarImage(PlanarImage.wrapRenderedImage(coverage.getRenderedImage()));
    }

    /** Method for calculating the resolution of the input {@link GridCoverage2D} */
    private static double calculateResolution(GridCoverage2D coverage) {
        GridGeometry2D gg2D = coverage.getGridGeometry();
        double envW = gg2D.getEnvelope2D().getWidth();
        double gridW = gg2D.getGridRange2D().getWidth();
        double res = envW / gridW;
        return res;
    }

    /** Method which ensures that the two {@link BoundingBox} objects are equals. */
    private void assertEqualBBOX(BoundingBox expected, BoundingBox actual) {
        Assert.assertEquals(expected.getMinX(), actual.getMinX(), TOLERANCE);
        Assert.assertEquals(expected.getMinY(), actual.getMinY(), TOLERANCE);
        Assert.assertEquals(expected.getMaxX(), actual.getMaxX(), TOLERANCE);
        Assert.assertEquals(expected.getMaxY(), actual.getMaxY(), TOLERANCE);
    }

    /** Method for cropping the input coverage with the defined envelope. */
    private GridCoverage2D crop(GridCoverage2D gc, GeneralBounds envelope) {
        final GeneralBounds oldEnvelope = (GeneralBounds) gc.getEnvelope();
        // intersect the envelopes in order to prepare for cropping the coverage
        // down to the neded resolution
        final GeneralBounds intersectionEnvelope = new GeneralBounds(envelope);
        intersectionEnvelope.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
        intersectionEnvelope.intersect(oldEnvelope);

        // Do we have something to show? After the crop I could get a null
        // coverage which would mean nothing to show.
        if (intersectionEnvelope.isEmpty()) {
            return null;
        }

        // crop
        final ParameterValueGroup param =
                processor.getOperation("CoverageCrop").getParameters().clone();
        param.parameter("source").setValue(gc);
        param.parameter("Envelope").setValue(intersectionEnvelope);
        return (GridCoverage2D) processor.doOperation(param, GeoTools.getDefaultHints());
    }

    /** Method for mosaicking two input images and setting the final BoundingBox */
    private GridCoverage2D mosaic(List<GridCoverage2D> coverages, GeneralBounds renderingEnvelope, Hints hints) {
        // setup the grid geometry
        try {
            // find the intersection between the target envelope and the coverages one
            ReferencedEnvelope targetEnvelope = ReferencedEnvelope.reference(renderingEnvelope);
            ReferencedEnvelope coveragesEnvelope = null;
            for (GridCoverage2D coverage : coverages) {
                ReferencedEnvelope re = ReferencedEnvelope.reference(coverage.getEnvelope2D());
                if (coveragesEnvelope == null) {
                    coveragesEnvelope = re;
                } else {
                    coveragesEnvelope.expandToInclude(re);
                }
            }
            targetEnvelope = new ReferencedEnvelope(
                    targetEnvelope.intersection(coveragesEnvelope), renderingEnvelope.getCoordinateReferenceSystem());
            if (targetEnvelope.isEmpty() || targetEnvelope.isNull()) {
                return null;
            }

            MathTransform2D mt = coverages.get(0).getGridGeometry().getCRSToGrid2D();
            Rectangle rasterSpaceEnvelope =
                    CRS.transform(mt, targetEnvelope).toRectangle2D().getBounds();
            GridEnvelope2D gridRange = new GridEnvelope2D(rasterSpaceEnvelope);
            GridGeometry2D gridGeometry = new GridGeometry2D(gridRange, targetEnvelope);

            // mosaic
            final ParameterValueGroup param =
                    processor.getOperation("Mosaic").getParameters().clone();
            param.parameter("sources").setValue(coverages);
            param.parameter("geometry").setValue(gridGeometry);
            return (GridCoverage2D) ((Mosaic) processor.getOperation("Mosaic")).doOperation(param, hints);
        } catch (Exception e) {
            throw new RuntimeException("Failed to mosaic the input coverages", e);
        }
    }

    private List<GridCoverage2D> sortCoverages(List<GridCoverage2D> coverages) {
        Collections.sort(coverages, new Comparator<>() {

            @Override
            public int compare(GridCoverage2D o1, GridCoverage2D o2) {
                double minx1 = o1.getEnvelope().getMinimum(0);
                double minx2 = o2.getEnvelope().getMinimum(0);
                if (minx1 == minx2) {
                    double maxy1 = o1.getEnvelope().getMaximum(1);
                    double maxy2 = o2.getEnvelope().getMaximum(1);
                    return compareDoubles(maxy1, maxy2);
                } else {
                    return compareDoubles(minx1, minx2);
                }
            }

            private int compareDoubles(double maxy1, double maxy2) {
                if (maxy1 == maxy2) {
                    return 0;
                } else {
                    return (int) Math.signum(maxy1 - maxy2);
                }
            }
        });
        return coverages;
    }

    @Test
    public void testPadding() {
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();

        // Creation of a List of the input Sources
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(coverage1);
        // Setting of the sources
        param.parameter("Sources").setValue(sources);

        // Compute a padded grid geometry
        GridGeometry2D gg1 = coverage1.getGridGeometry();
        GridEnvelope2D range = gg1.getGridRange2D();
        GridEnvelope2D range2 = new GridEnvelope2D(range.x - 10, range.y - 10, range.width + 20, range.height + 20);
        GridGeometry2D targetGG = new GridGeometry2D(range2, gg1.getGridToCRS(), gg1.getCoordinateReferenceSystem2D());
        param.parameter("geometry").setValue(targetGG);
        // Mosaic
        GridCoverage2D mosaic = (GridCoverage2D) processor.doOperation(param);

        // Check that the final grid has been padded
        assertEquals(targetGG.getEnvelope2D(), mosaic.getEnvelope2D());

        // Coverage and RenderedImage disposal
        disposeCoveragePlanarImage(mosaic);
    }

    @Test
    public void testPreserveROI() throws Exception {
        // Creates coverages with model space coordinates, mind the ROI, it's in raster space
        // The two coverages are side by side, the ROIs cover the second half of the first
        // image, and the first half of the second:
        // +---------------+---------------+
        // |       xxxxxxxx|xxxxxxxx       |
        // |       xxxxxxxx|xxxxxxxx       |
        // |       xxxxxxxx|xxxxxxxx       |
        // |       xxxxxxxx|xxxxxxxx       |
        // |       xxxxxxxx|xxxxxxxx       |
        // +---------------|---------------|
        GridCoverage2D c1 = createGradientCoverage(0, 0, 50, 50, new ROIShape(new Rectangle(25, 0, 25, 50)));
        GridCoverage2D c2 = createGradientCoverage(50, 0, 50, 50, new ROIShape(new Rectangle(0, 0, 25, 50)));

        // setup the mosaic
        ParameterValueGroup param = processor.getOperation("Mosaic").getParameters();
        List<GridCoverage2D> sources = new ArrayList<>(2);
        sources.add(c1);
        sources.add(c2);
        param.parameter("Sources").setValue(sources);

        GridCoverage2D mosaic = (GridCoverage2D) processor.doOperation(param);

        Object roiObject = mosaic.getRenderedImage().getProperty("roi");
        assertThat(roiObject, CoreMatchers.instanceOf(ROI.class));

        ROI roi = (ROI) roiObject;
        assertFalse(roi.contains(15, 5));
        assertFalse(roi.contains(15, 45));
        assertTrue(roi.contains(45, 5));
        assertTrue(roi.contains(45, 45));
        assertTrue(roi.contains(55, 5));
        assertTrue(roi.contains(55, 45));
        assertFalse(roi.contains(85, 5));
        assertFalse(roi.contains(85, 45));
    }

    /**
     * Creates a grid coverage with real world coordinates x0, y0, and given width and height. The raster backing it has
     * the same dimensions, but it's always located in the origin.
     */
    public GridCoverage2D createGradientCoverage(int x0, int y0, int width, int height, ROI roi) {
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        final WritableRaster raster = (WritableRaster) image.getData();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setSample(x, y, 0, x + y);
            }
        }
        image.setData(raster);
        PlanarImage pi = PlanarImage.wrapRenderedImage(image);
        Map<String, Object> coverageProperties = new HashMap<>();
        if (roi != null) {
            pi.setProperty("roi", roi);
            CoverageUtilities.setROIProperty(coverageProperties, roi);
        }
        return GRID_COVERAGE_FACTORY.create(
                "Test coverage",
                pi,
                ReferencedEnvelope.rect(x0, y0, width, height, DefaultEngineeringCRS.GENERIC_2D),
                null,
                null,
                coverageProperties);
    }
}
