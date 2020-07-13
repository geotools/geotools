/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.media.jai.Interpolation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.renderer.crs.ProjectionHandler;
import org.geotools.renderer.crs.ProjectionHandlerFinder;
import org.geotools.renderer.lite.GridCoverageRendererTest;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;

public class GridCoverageReaderHelperTest {

    @Rule public TemporaryFolder crsMosaicFolder = new TemporaryFolder();

    static final double EPS = 1e-9;

    private GeoTiffReader reader;

    File coverageFile;

    @Before
    public void getData() throws IOException {
        MapProjection.SKIP_SANITY_CHECKS = true;
        coverageFile = TestData.copy(this, "geotiff/world.tiff");
        assertTrue(coverageFile.exists());
        reader = new GeoTiffReader(coverageFile);
    }

    @After
    public void close() {
        MapProjection.SKIP_SANITY_CHECKS = false;
        reader.dispose();
    }

    @Test
    public void testGeographicLarge() throws Exception {
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(-360, 360, -90, 90, DefaultGeographicCRS.WGS84);
        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(
                        reader,
                        new Rectangle(720, 180),
                        mapExtent,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read single coverage with no projection handling
        GridCoverage2D coverage = helper.readCoverage(null);
        Envelope2D envelope = coverage.getEnvelope2D();
        assertEquals(-180, envelope.getMinX(), EPS);
        assertEquals(180, envelope.getMaxX(), EPS);
        assertEquals(-90, envelope.getMinY(), EPS);
        assertEquals(90, envelope.getMaxY(), EPS);

        // try multiple coverage with projection handling, should not make a difference
        // since we are already reading everything in a single shot, just in need of coverage
        // replication
        // (which has to be performed after the eventual reprojection, so not here in the reader)
        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(
                        mapExtent, reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        // System.out.println(coverages);
        assertEquals(1, coverages.size());
        assertEquals(envelope, coverages.get(0).getEnvelope2D());
    }

    @Test
    public void testGeographicLargeAccurateResolutionFlags() throws Exception {
        String testLocation = "geographicLarge";
        URL storeUrl = TestData.url(this, "geotiff/world.tiff");
        File testData = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyFileToDirectory(testData, testDirectory);
        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);

        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(-180, 180, -80, 80, DefaultGeographicCRS.WGS84);
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857");
        ReferencedEnvelope requestedEnvelope = mapExtent.transform(targetCRS, true);

        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(
                        reader,
                        new Rectangle(90, 40),
                        requestedEnvelope,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        MathTransform originalG2W = imReader.getOriginalGridToWorld(PixelInCell.CELL_CENTER);
        AffineTransform at = (AffineTransform) originalG2W;
        final double originalResolution = 0.9d;
        assertEquals(Math.abs(at.getScaleX()), originalResolution, EPS);
        assertEquals(Math.abs(at.getScaleY()), originalResolution, EPS);

        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(
                        mapExtent, reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        at = (AffineTransform) coverages.get(0).getGridGeometry().getGridToCRS();

        // Accurate resolution has been used
        assertTrue(Math.abs(at.getScaleX()) <= originalResolution);
        assertTrue(Math.abs(at.getScaleY()) <= originalResolution);

        ParameterValue<Boolean> accurateResolution =
                ImageMosaicFormat.ACCURATE_RESOLUTION.createValue();
        accurateResolution.setValue(false);
        coverages = helper.readCoverages(new GeneralParameterValue[] {accurateResolution}, handler);
        at = (AffineTransform) coverages.get(0).getGridGeometry().getGridToCRS();

        // Accurate resolution has not been used.
        assertTrue(Math.abs(at.getScaleX()) > (originalResolution * 3));
        assertTrue(Math.abs(at.getScaleY()) > (originalResolution * 3));
    }

    @Test
    public void testGeographicDatelineCross() throws Exception {
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(170, 190, 70, 80, DefaultGeographicCRS.WGS84);
        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(
                        reader,
                        new Rectangle(100, 100),
                        mapExtent,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read single coverage with no projection handling, the geotiff reader gives us all
        GridCoverage2D coverage = helper.readCoverage(null);
        Envelope2D envelope = coverage.getEnvelope2D();
        assertEquals(-180, envelope.getMinX(), EPS);
        assertEquals(180, envelope.getMaxX(), EPS);
        assertEquals(-90, envelope.getMinY(), EPS);
        assertEquals(90, envelope.getMaxY(), EPS);

        // now read with projection handling instead, we must get two at the
        // two ends of the dateline
        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(
                        mapExtent, reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        // System.out.println(coverages);
        assertEquals(2, coverages.size());
        Envelope2D firstEnvelope = coverages.get(0).getEnvelope2D();
        assertEquals(169.2, firstEnvelope.getMinX(), EPS);
        assertEquals(180, firstEnvelope.getMaxX(), EPS);
        assertEquals(69.3, firstEnvelope.getMinY(), EPS);
        assertEquals(80.1, firstEnvelope.getMaxY(), EPS);
        Envelope2D secondEnvelope = coverages.get(1).getEnvelope2D();
        assertEquals(-180, secondEnvelope.getMinX(), EPS);
        assertEquals(-169.2, secondEnvelope.getMaxX(), EPS);
        assertEquals(69.3, secondEnvelope.getMinY(), EPS);
        assertEquals(80.1, secondEnvelope.getMaxY(), EPS);
    }

    @Test
    public void testUTM() throws Exception {
        // setup a request large enough to cause severe reprojection deformation
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32632", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-1.5e7, 1.5e7, 0, 1e6, crs);
        // System.out.println(mapExtent.transform(DefaultGeographicCRS.WGS84, true));
        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(
                        reader,
                        new Rectangle(400, 200),
                        mapExtent,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read single coverage with no projection handling, we should get the full requested area
        GridCoverage2D coverage = helper.readCoverage(null);
        Envelope2D envelope = coverage.getEnvelope2D();
        // System.out.println(envelope);
        assertTrue(envelope.getMinX() < -100);
        assertTrue(envelope.getMaxX() > 100);

        // now read via the projection handlers
        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(
                        mapExtent, reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        // System.out.println(coverages);
        assertEquals(1, coverages.size());
        envelope = coverages.get(0).getEnvelope2D();
        // west/east limited to 45 degrees from the central meridian, plus reading gutter
        assertEquals(-36, envelope.getMinX(), EPS);
        assertEquals(54, envelope.getMaxX(), EPS);
    }

    @Test
    public void testConic() throws Exception {
        // setup a request large enough to cause severe reprojection deformation
        CoordinateReferenceSystem crs = CRS.decode("EPSG:32632", true);
        ReferencedEnvelope mapExtent = new ReferencedEnvelope(-1.5e7, 1.5e7, 0, 1e6, crs);
        // System.out.println(mapExtent.transform(DefaultGeographicCRS.WGS84, true));
        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(
                        reader,
                        new Rectangle(400, 200),
                        mapExtent,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read single coverage with no projection handling, we should get the full requested area
        GridCoverage2D coverage = helper.readCoverage(null);
        Envelope2D envelope = coverage.getEnvelope2D();
        // System.out.println(envelope);
        assertTrue(envelope.getMinX() < -100);
        assertTrue(envelope.getMaxX() > 100);

        // now read via the projection handlers
        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(
                        mapExtent, reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        // System.out.println(coverages);
        assertEquals(1, coverages.size());
        envelope = coverages.get(0).getEnvelope2D();
        // west/east limited to 45 degrees from the central meridian
        assertEquals(-36, envelope.getMinX(), EPS);
        assertEquals(54, envelope.getMaxX(), EPS);
    }

    @Test
    public void testOutsideDefinitionArea() throws Exception {
        // setup a request that is outside of the coverage
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(-1250000, 0, -13750000, -12500000, crs);
        // System.out.println(mapExtent.transform(DefaultGeographicCRS.WGS84, true));
        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(
                        reader,
                        new Rectangle(400, 200),
                        mapExtent,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read, nothing should come out
        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(
                        mapExtent, reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        assertTrue(coverages.isEmpty());
    }

    @Test
    public void testFullResolutionNull() throws Exception {
        // this one has null native resolutions
        final GridCoverage2D coverage =
                new GridCoverageFactory()
                        .create(
                                "test",
                                new float[200][100],
                                new ReferencedEnvelope(
                                        -180, 180, -90, 90, DefaultGeographicCRS.WGS84));
        GridCoverage2DReader reader =
                new AbstractGridCoverage2DReader() {

                    {
                        this.crs = DefaultGeographicCRS.WGS84;
                        this.originalEnvelope =
                                new GeneralEnvelope((BoundingBox) coverage.getEnvelope2D());
                        this.originalGridRange = coverage.getGridGeometry().getGridRange();
                    }

                    @Override
                    public Format getFormat() {
                        return null;
                    }

                    @Override
                    public GridCoverage2D read(GeneralParameterValue[] parameters)
                            throws IllegalArgumentException, IOException {
                        // return fake coveage
                        return coverage;
                    }
                };
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031", true);
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(-20000000, 20000000, -20000000, 20000000, crs);

        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(
                        reader,
                        new Rectangle(400, 200),
                        mapExtent,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST));

        // read, we should get back a coverage, not a exception
        ProjectionHandler handler =
                ProjectionHandlerFinder.getHandler(
                        mapExtent, reader.getCoordinateReferenceSystem(), true);
        List<GridCoverage2D> coverages = helper.readCoverages(null, handler);
        assertEquals(1, coverages.size());
    }

    @Test
    public void testCutUnreferenced() throws Exception {
        // force a CRS that does not have a projection handler (and most likely never will)
        Hints hints =
                new Hints(
                        Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM,
                        DefaultEngineeringCRS.GENERIC_2D);
        GridCoverage2DReader reader = null;

        try {
            reader = new GeoTiffReader(coverageFile, hints);
            // setup the read
            ReferencedEnvelope mapExtent =
                    new ReferencedEnvelope(-90, 0, -45, 45, DefaultEngineeringCRS.GENERIC_2D);
            GridCoverageReaderHelper helper =
                    new GridCoverageReaderHelper(
                            reader,
                            new Rectangle(200, 200),
                            mapExtent,
                            Interpolation.getInstance(Interpolation.INTERP_NEAREST));
            List<GridCoverage2D> coverages = helper.readCoverages(null, null);
            assertEquals(1, coverages.size());
            // check it has been cut
            GridCoverage2D gc = coverages.get(0);
            Envelope envelope = gc.getEnvelope();
            assertEquals(-90, envelope.getMinimum(0), EPS);
            assertEquals(0, envelope.getMaximum(0), EPS);
            assertEquals(-45, envelope.getMinimum(1), EPS);
            assertEquals(45, envelope.getMaximum(1), EPS);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    @Test
    public void testReadResolution3003InvalidArea() throws Exception {
        coverageFile =
                URLs.urlToFile(
                        GridCoverageRendererTest.class.getResource("test-data/test3003.tif"));
        assertTrue(coverageFile.exists());
        GeoTiffReader reader = new GeoTiffReader(coverageFile);
        try {
            reader = new GeoTiffReader(coverageFile);
            ReferencedEnvelope mapExtent =
                    new ReferencedEnvelope(-130, -120, -40, 30, DefaultGeographicCRS.WGS84);
            GridCoverageReaderHelper helper =
                    new GridCoverageReaderHelper(
                            reader,
                            new Rectangle(200, 200),
                            mapExtent,
                            Interpolation.getInstance(Interpolation.INTERP_NEAREST));
            // make sure the accurate resolution does not happen, it cannot work in this context
            ReferencedEnvelope readExtent =
                    mapExtent.transform(reader.getCoordinateReferenceSystem(), true);
            assertFalse(helper.isAccurateResolutionComputationSafe(readExtent));
            // nothing is really getting read
            ProjectionHandler handler =
                    ProjectionHandlerFinder.getHandler(
                            new ReferencedEnvelope(DefaultGeographicCRS.WGS84),
                            reader.getCoordinateReferenceSystem(),
                            false);
            List<GridCoverage2D> coverages =
                    helper.readCoverageInEnvelope(mapExtent, null, handler, true);
            assertNull(coverages);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    /**
     * Checks that code does not end up reading an mosaicking a secondary part of data that's
     * overlapping with the larger area requested, only because the source data self overlaps (has
     * data beyond the dateline in both directions)
     */
    @Test
    public void testReadOffDatelineBothSides() throws Exception {
        coverageFile = URLs.urlToFile(getClass().getResource("test-data/off_dateline.tif"));
        assertTrue(coverageFile.exists());
        GeoTiffReader reader = new GeoTiffReader(coverageFile);
        try {
            reader = new GeoTiffReader(coverageFile);
            ReferencedEnvelope mapExtent =
                    new ReferencedEnvelope(-180, 0, -90, 90, DefaultGeographicCRS.WGS84);
            GridCoverageReaderHelper helper =
                    new GridCoverageReaderHelper(
                            reader,
                            new Rectangle(1024, 512),
                            mapExtent,
                            Interpolation.getInstance(Interpolation.INTERP_NEAREST));
            ProjectionHandler handler =
                    ProjectionHandlerFinder.getHandler(
                            new ReferencedEnvelope(DefaultGeographicCRS.WGS84),
                            DefaultGeographicCRS.WGS84,
                            true);
            List<GridCoverage2D> coverages =
                    helper.readCoverageInEnvelope(mapExtent, null, handler, true);
            assertEquals(1, coverages.size());
            GridCoverage2D coverage = coverages.get(0);
            Envelope2D envelope = coverage.getEnvelope2D();
            final double EPS = 0.2; // this is the native resolution
            assertEquals(-180.4, envelope.getMinX(), EPS);
            assertEquals(2, envelope.getMaxX(), EPS);
            assertEquals(-90, envelope.getMinY(), EPS);
            assertEquals(90, envelope.getMaxY(), EPS);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }

    @Test
    public void testPaddingHeteroMosaic() throws Exception {
        String testLocation = "hetero_utm";
        URL storeUrl = TestData.url(this.getClass(), testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        ReferencedEnvelope mapExtent =
                new ReferencedEnvelope(11, 13, -1, 1, DefaultGeographicCRS.WGS84);
        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(
                        imReader,
                        new Rectangle(1024, 512),
                        mapExtent,
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        ReferencedEnvelope readEnvelope = helper.getReadEnvelope();
        final double EPS = 1e-3;
        assertEquals(10.981, readEnvelope.getMinX(), EPS);
        assertEquals(13.019, readEnvelope.getMaxX(), EPS);
        assertEquals(-1.039, readEnvelope.getMinY(), EPS);
        assertEquals(1.039, readEnvelope.getMaxY(), EPS);
    }

    // fails on Travis but not locally in IDE nor Maven. There are other tests covering this
    // same behavior in GridCoverageRendererTest and they don't fail...
    @Test
    @Ignore
    public void testHarvestSpatialTwoReaders() throws Exception {
        File source = TestData.file(this.getClass(), "red_footprint_test");
        File testDataDir = org.geotools.test.TestData.file(this, ".");
        File directory1 = new File(testDataDir, "redHarvest1");
        File directory2 = new File(testDataDir, "redHarvest2");
        if (directory1.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        if (directory2.exists()) {
            FileUtils.deleteDirectory(directory1);
        }
        FileUtils.copyDirectory(source, directory1);
        // move all files except red3 to the second dir
        directory2.mkdirs();
        for (File file : FileUtils.listFiles(directory1, new RegexFileFilter("red[^3].*"), null)) {
            assertTrue(file.renameTo(new File(directory2, file.getName())));
        }

        // crate the first reader
        URL harvestSingleURL = URLs.fileToUrl(directory1);
        ImageMosaicReader reader = new ImageMosaicReader(directory1, null);

        // now create a second reader that won't be informed of the harvesting changes
        // (simulating changes over a cluster, where the bbox information won't be updated from one
        // node to the other)
        ImageMosaicReader reader2 = new ImageMosaicReader(directory1, null);

        // harvest the other files with the first reader
        for (File file : directory2.listFiles()) {
            assertTrue(file.renameTo(new File(directory1, file.getName())));
        }
        reader.harvest(null, directory1, null);

        // now use the GridCoveargeReaderHelper to read data outside of the original envelope of
        // reader2
        ReferencedEnvelope readEnvelope =
                new ReferencedEnvelope(
                        991000, 992000, 216000, 217000, reader2.getCoordinateReferenceSystem());
        Rectangle rasterArea = new Rectangle(0, 0, 10, 10);
        GridCoverageReaderHelper helper =
                new GridCoverageReaderHelper(reader2, rasterArea, readEnvelope, null);
        ParameterValue<GridGeometry2D> ggParam =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(new GridGeometry2D(new GridEnvelope2D(rasterArea), readEnvelope));
        GridCoverage2D coverage = helper.readCoverage(new GeneralParameterValue[] {ggParam});
        assertNotNull(coverage);
        coverage.dispose(true);

        reader.dispose();
        reader2.dispose();
    }
}
