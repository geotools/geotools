/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gce.imagemosaic;

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
import static org.geotools.util.URLs.fileToUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sun.media.jai.operator.ImageReadDescriptor;
import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.Interpolation;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.grid.io.GranuleSource;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultRepository;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.DecoratingDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.test.TestData;
import org.geotools.util.factory.Hints;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.locationtech.jts.geom.Geometry;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.BBOX;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/** Testing whether a simple mosaic correctly has its elements reprojected */
public class HeterogenousCRSTest {

    public static final double DELTA = 1E-6;
    @Rule public TemporaryFolder crsMosaicFolder = new TemporaryFolder();

    @BeforeClass
    public static void ignoreReciprocals() throws Exception {
        MapProjection.SKIP_SANITY_CHECKS = true;
    }

    @BeforeClass
    public static void resetReciprocals() throws Exception {
        MapProjection.SKIP_SANITY_CHECKS = false;
    }

    @AfterClass
    public static void cleanupCRS() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");
    }

    @BeforeClass
    public static void initCRS() {
        // make sure CRS ordering is correct
        CRS.reset("all");
        System.setProperty("org.geotools.referencing.forceXY", "true");
    }

    @Ignore
    @Test
    public void testHeterogeneousCRS()
            throws IOException, URISyntaxException, TransformException, FactoryException {
        testMosaic(
                "heterogeneous_crs",
                "location D, crs A",
                "red_blue_results/red_blue_heterogeneous_results.tiff",
                "EPSG:3587");
    }

    @Test
    public void testDiffCRSSorting() throws IOException, URISyntaxException {
        testMosaic(
                "diff_crs_sorting_test",
                "resolution D, crs A",
                "diff_crs_sorting_test_results/results.tiff",
                "EPSG:32610");
    }

    @Test
    public void testWithInterpolation() throws IOException, URISyntaxException {
        ParameterValue<Interpolation> interpolationParam =
                AbstractGridFormat.INTERPOLATION.createValue();
        interpolationParam.setValue(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
        testMosaic(
                "diff_crs_sorting_test",
                "resolution D, crs A",
                null,
                "EPSG:32610",
                interpolationParam);
    }

    @Test
    public void testUpdatingMosaic() throws IOException, URISyntaxException {
        File second = TestData.file(this, "heterogeneous_crs/zblue.tiff");
        File indexer = TestData.file(this, "heterogeneous_crs/indexer.properties");
        File first = TestData.file(this, "heterogeneous_crs/red.tiff");
        File resultsImage = TestData.file(this, "red_blue_results/red_blue_update_test.tiff");

        File testStoreDirectory = crsMosaicFolder.newFolder("updateTest");

        FileUtils.copyFile(first, new File(testStoreDirectory, first.getName()));
        FileUtils.copyFile(indexer, new File(testStoreDirectory, indexer.getName()));

        ImageMosaicReader reader = new ImageMosaicReader(testStoreDirectory);
        File sfdemDest = new File(testStoreDirectory, second.getName());
        FileUtils.copyFile(second, sfdemDest);

        reader.harvest(null, sfdemDest, null);

        GridCoverage2D gc2d = reader.read(new GeneralParameterValue[0]);

        RenderedImage renderImage = gc2d.getRenderedImage();
        ImageAssert.assertEquals(resultsImage, renderImage, 1000);
        reader.dispose();
    }

    private void testMosaic(
            String testLocation,
            String sortOrder,
            String resultLocation,
            String expectedCRS,
            GeneralParameterValue... params)
            throws URISyntaxException, IOException {

        ImageMosaicReader imReader = getTestMosaic(testLocation);

        // check it advertises the mixed crs
        assertEquals("true", imReader.getMetadataValue(GridCoverage2DReader.MULTICRS_READER));

        // hack workaround for the store not being created with a consistent CRS in certain
        // environments.
        assertEquals(CRS.toSRS(imReader.getCoordinateReferenceSystem()), expectedCRS);
        Collection<GeneralParameterValue> finalParamsCollection =
                new ArrayList<>(Arrays.asList(params));

        // Let's do a sort order to get the correct results
        ParameterValue<String> sortByParam = ImageMosaicFormat.SORT_BY.createValue();
        sortByParam.setValue(sortOrder);

        finalParamsCollection.add(sortByParam);

        GridCoverage2D gc2d =
                imReader.read(finalParamsCollection.toArray(new GeneralParameterValue[] {}));
        assertEquals(CRS.toSRS(gc2d.getCoordinateReferenceSystem()), expectedCRS);

        if (resultLocation != null) {
            RenderedImage renderImage = gc2d.getRenderedImage();
            File resultsFile = testFile(resultLocation);

            // number 1000 was a bit arbitrary for differences, should account for small differences
            // in
            // interpolation and such, but not the reprojection of the blue tiff. Correct and
            // incorrect
            // images will be pretty similar anyway
            ImageAssert.assertEquals(resultsFile, renderImage, 1000);
        }
        imReader.dispose();
    }

    private ImageMosaicReader getTestMosaic(String testLocation)
            throws URISyntaxException, IOException {
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        Hints creationHints = new Hints();
        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, creationHints);
        Assert.assertNotNull(imReader);
        return imReader;
    }

    @Test
    public void testHarvestHeteroUTM() throws Exception {
        File indexer = TestData.file(this, "hetero_utm/indexer.properties");
        File utm32n = TestData.file(this, "hetero_utm/utm32n.tiff");
        File utm33n = TestData.file(this, "hetero_utm/utm33n.tiff");
        File utm32s = TestData.file(this, "hetero_utm/utm32s.tiff");
        File utm33s = TestData.file(this, "hetero_utm/utm33s.tiff");

        File testStoreDirectory = crsMosaicFolder.newFolder("harvestHeteroUtm");
        FileUtils.copyFile(utm32n, new File(testStoreDirectory, utm32n.getName()));
        FileUtils.copyFile(indexer, new File(testStoreDirectory, indexer.getName()));

        // setup reader and check initial status
        ImageMosaicReader reader = new ImageMosaicReader(testStoreDirectory);
        Assert.assertNotNull(reader);
        assertExpectedBounds(new ReferencedEnvelope(11, 12, 0, 1, WGS84), reader);
        assertExpectedMosaic(reader, "hetero_utm_results/topleft.png");

        // update and check
        assertEquals(1, reader.harvest(null, utm33n, null).size());
        assertExpectedBounds(new ReferencedEnvelope(11, 13, 0, 1, WGS84), reader);
        assertExpectedMosaic(reader, "hetero_utm_results/top.png");

        // update and check
        assertEquals(1, reader.harvest(null, utm32s, null).size());
        assertExpectedBounds(new ReferencedEnvelope(11, 13, -1, 1, WGS84), reader);
        assertExpectedMosaic(reader, "hetero_utm_results/top_bottoleft.png");

        // update and check
        assertEquals(1, reader.harvest(null, utm33s, null).size());
        assertExpectedBounds(new ReferencedEnvelope(11, 13, -1, 1, WGS84), reader);
        assertExpectedMosaic(reader, "hetero_utm_results/full.png");

        reader.dispose();
    }

    @Test
    public void testHeteroUTM() throws Exception {
        String testLocation = "hetero_utm";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        Assert.assertNotNull(imReader);

        // check we have the expected bounds and CRS
        assertExpectedBounds(new ReferencedEnvelope(11, 13, -1, 1, WGS84), imReader);

        // read a coverage and compare with expected image
        final String expectedResultLocation = "hetero_utm_results/full.png";
        assertExpectedMosaic(imReader, expectedResultLocation);
        imReader.dispose();
    }

    @Test
    public void testConcurrentHeteroUTMH2FullArea() throws Exception {
        runConcurrentHeteroUTMH2(r -> null);
    }

    @Test
    public void testConcurrentHeteroUTMH2Across() throws Exception {
        runConcurrentHeteroUTMH2(
                r -> {
                    ParameterValue<GridGeometry2D> ggp =
                            AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
                    GridEnvelope range = r.getOriginalGridRange();
                    // move read area partially outside
                    ReferencedEnvelope bounds =
                            ReferencedEnvelope.reference(r.getOriginalEnvelope());
                    bounds.translate(bounds.getWidth() / 2, 0);
                    GridGeometry2D gg = new GridGeometry2D(range, bounds);
                    ggp.setValue(gg);
                    return new GeneralParameterValue[] {ggp};
                });
    }

    @Test
    public void testConcurrentHeteroUTMH2NoGranules() throws Exception {
        runConcurrentHeteroUTMH2(
                r -> {
                    ParameterValue<Filter> filter = ImageMosaicFormat.FILTER.createValue();
                    filter.setValue(Filter.EXCLUDE);
                    return new GeneralParameterValue[] {filter};
                });
    }

    private void runConcurrentHeteroUTMH2(
            Function<ImageMosaicReader, GeneralParameterValue[]> parameters)
            throws URISyntaxException, IOException, InterruptedException,
                    java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException {
        String testLocation = "hetero_utm";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        // to similate the deadlock we need a connection pool with just one connection,
        // a single request will cause the deadlock in this case (but we try with a pool
        // nevertheless, to be extra sure)
        File datastoreProperties = new File(testDirectory, "datastore.properties");
        try (FileWriter out = new FileWriter(datastoreProperties)) {
            out.write("database=hetero_concurrent\n");
            out.write(
                    "SPI=org.geotools.data.h2.H2DataStoreFactory\n"
                            + "dbtype=h2\n"
                            + "user=gs\n"
                            + "passwd=gs\n"
                            + "Connection\\ timeout=3600\n"
                            + "max \\connections=1"
                            + "min \\connections=1");
            out.flush();
        }

        final ExecutorService executors = Executors.newFixedThreadPool(4);
        ImageMosaicReader reader = new ImageMosaicReader(testDirectory, null);
        try {
            List<Future> futures = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Future<GridCoverage2D> future =
                        executors.submit(() -> reader.read(parameters.apply(reader)));
                futures.add(future);
            }
            for (Future future : futures) {
                // just to make sure it cannot get stuck forever, but allow execution on
                // very slow runtimes
                future.get(120, TimeUnit.SECONDS);
            }
        } finally {
            reader.dispose();
        }
    }

    @Test
    public void testHeteroSentinel2() throws Exception {
        String testLocation = "hetero_s2";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        Assert.assertNotNull(imReader);

        // read a coverage and compare with expected image
        final String expectedResultLocation = "hetero_s2_results/overlap.png";
        ParameterValue<Color> inputTransparentColor =
                AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
        inputTransparentColor.setValue(Color.BLACK);
        ParameterValue<Color> outputTransparentColor =
                ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
        outputTransparentColor.setValue(Color.BLACK);

        assertExpectedMosaic(
                imReader, expectedResultLocation, inputTransparentColor, outputTransparentColor);
        imReader.dispose();
    }

    @Test
    public void testHeteroSentinel2Filtered() throws Exception {
        String testLocation = "hetero_s2";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        Assert.assertNotNull(imReader);

        // read a coverage and compare with expected image
        ParameterValue<Color> inputTransparentColor =
                AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
        inputTransparentColor.setValue(Color.BLACK);
        ParameterValue<Color> outputTransparentColor =
                ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
        outputTransparentColor.setValue(Color.BLACK);
        ParameterValue<GridGeometry2D> ggp = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        GridEnvelope2D range = new GridEnvelope2D(0, 0, 158, 103);
        GridGeometry2D gg =
                new GridGeometry2D(
                        range,
                        new ReferencedEnvelope(
                                11.6834779,
                                11.8616874,
                                47.6380806,
                                47.7542552,
                                CRS.decode("EPSG:4236", true)));
        ggp.setValue(gg);
        ParameterValue<Filter> filter = ImageMosaicFormat.FILTER.createValue();
        filter.setValue(CQL.toFilter("location = 'g1.tif'"));

        final String expectedResultLocation = "hetero_s2_results/filtered.png";
        assertExpectedMosaic(
                imReader,
                expectedResultLocation,
                inputTransparentColor,
                outputTransparentColor,
                filter,
                ggp);
        imReader.dispose();
    }

    @Test
    public void testHeteroSentinel2Footprints() throws Exception {
        String testLocation = "hetero_s2_footprints";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        Assert.assertNotNull(imReader);

        // read a coverage and compare with expected image
        final String expectedResultLocation = "hetero_s2_results/footprints.png";
        ParameterValue<String> footprintBehavior =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintBehavior.setValue("Transparent");

        assertExpectedMosaic(imReader, expectedResultLocation, footprintBehavior);
        imReader.dispose();
    }

    @Test
    public void testHeteroUtmFootprintTransparency() throws Exception {
        String testLocation = "hetero_utm_footprint";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        Assert.assertNotNull(imReader);

        // read a coverage and compare with expected image
        final String expectedResultLocation = "hetero_utm_footprint_results/footprints.png";
        ParameterValue<String> footprintBehavior =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintBehavior.setValue("Transparent");
        // use sort to get a stable result
        ParameterValue<String> sortBy = ImageMosaicFormat.SORT_BY.createValue();
        sortBy.setValue("location A");

        assertExpectedMosaic(imReader, expectedResultLocation, footprintBehavior, sortBy);
        imReader.dispose();
    }

    @Test
    public void testHeteroCRSDateline()
            throws IOException, URISyntaxException, TransformException, FactoryException {
        ImageMosaicReader imReader = getTestMosaic("hetero_crs_dateline");
        assertEquals(CRS.toSRS(imReader.getCoordinateReferenceSystem()), "EPSG:4326");

        // read before dateline
        GeneralParameterValue gg1 =
                buildGridGeometryParameter(
                        new ReferencedEnvelope(179, 180, 60, 62, DefaultGeographicCRS.WGS84),
                        128,
                        256);
        GridCoverage2D gcBefore = imReader.read(new GeneralParameterValue[] {gg1});
        RenderedImage riBefore = gcBefore.getRenderedImage();
        ImageAssert.assertEquals(
                testFile("hetero_crs_dateline_results/before.png"), riBefore, 1000);

        // read after the dateline
        GeneralParameterValue ggAfter =
                buildGridGeometryParameter(
                        new ReferencedEnvelope(180, 181, 60, 62, DefaultGeographicCRS.WGS84),
                        128,
                        256);
        GridCoverage2D gcAfter = imReader.read(new GeneralParameterValue[] {ggAfter});
        RenderedImage riAfter = gcAfter.getRenderedImage();
        ImageAssert.assertEquals(testFile("hetero_crs_dateline_results/after.png"), riAfter, 1000);

        GranuleSource gs = imReader.getGranules(null, true);
        SimpleFeatureCollection granules = gs.getGranules(Query.ALL);
        try (SimpleFeatureIterator fi = granules.features()) {
            while (fi.hasNext()) {
                SimpleFeature sf = fi.next();
                Geometry geom = (Geometry) sf.getDefaultGeometry();
                // check it did not wrap around the globe
                assertTrue(geom.toText(), geom.getEnvelopeInternal().getWidth() < 3);
            }
        }
        imReader.dispose();
    }

    @Test
    public void testHeteroCRSRasterMask()
            throws IOException, URISyntaxException, TransformException, FactoryException {
        URL storeUrl = TestData.url(this, "rastermask2");

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder("hetero_crs_rastermask");
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        // force hetero setup, different CRS for the mosaic
        String indexer =
                "GranuleAcceptors=org.geotools.gce.imagemosaic.acceptors.HeterogeneousCRSAcceptorFactory\n"
                        + "GranuleHandler=org.geotools.gce.imagemosaic.granulehandler.ReprojectingGranuleHandlerFactory\n"
                        + "HeterogeneousCRS=true\n"
                        + //
                        "MosaicCRS=EPSG\\:3857\n"
                        + // the reprojection bit
                        "Schema=*the_geom:Polygon,location:String,crs:String";
        FileUtils.writeStringToFile(
                new File(testDirectory, "indexer.properties"), indexer, "UTF-8");

        // footprint management
        String footprints = "footprint_source=raster";
        FileUtils.writeStringToFile(
                new File(testDirectory, "footprints.properties"), footprints, "UTF-8");

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, new Hints());
        Assert.assertNotNull(imReader);
        assertEquals(CRS.toSRS(imReader.getCoordinateReferenceSystem()), "EPSG:3857");

        // check it's not empty (used to be)
        final ParameterValue<String> footprintParam =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintParam.setValue("Transparent");
        GridCoverage2D coverage = imReader.read(new GeneralParameterValue[] {footprintParam});
        assertNotNull(coverage);
        ImageAssert.assertEquals(
                testFile("hetero_crs_rastermask.png"), coverage.getRenderedImage(), 1000);
        imReader.dispose();
    }

    GeneralParameterValue buildGridGeometryParameter(
            ReferencedEnvelope envelope, int width, int height) {
        final ParameterValue<GridGeometry2D> gg =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GridEnvelope2D range = new GridEnvelope2D(0, 0, width, height);
        final GridGeometry2D value = new GridGeometry2D(range, envelope);
        gg.setValue(value);
        return gg;
    }

    private void assertExpectedBounds(ReferencedEnvelope expected, ImageMosaicReader imReader) {
        // the specified CRS has been honored
        assertTrue(
                CRS.equalsIgnoreMetadata(
                        imReader.getCoordinateReferenceSystem(),
                        expected.getCoordinateReferenceSystem()));

        // getting the expected bounds (more or less)
        double EPS = 0.5d / 110; // pixel size is 1km, use half a pixel tolerance
        GeneralEnvelope envelope = imReader.getOriginalEnvelope();

        assertEquals(expected.getMinX(), envelope.getMinimum(0), EPS);
        assertEquals(expected.getMaxX(), envelope.getMaximum(0), EPS);
        assertEquals(expected.getMinY(), envelope.getMinimum(1), EPS);
        assertEquals(expected.getMaxY(), envelope.getMaximum(1), EPS);
    }

    private void assertExpectedMosaic(
            ImageMosaicReader imReader,
            final String expectedResultLocation,
            GeneralParameterValue... params)
            throws IOException {
        GridCoverage2D coverage = imReader.read(params);
        File resultsFile = testFile(expectedResultLocation);
        RenderedImage image = coverage.getRenderedImage();
        ImageAssert.assertEquals(resultsFile, image, 1000);

        // cleanup
        coverage.dispose(true);
    }

    File testFile(String name) {
        return new File("src/test/resources/org/geotools/gce/imagemosaic/test-data/" + name);
    }

    @Test
    public void testCrsResolutionDomains() throws Exception {
        ImageMosaicReader reader = getTestMosaic("diff_crs_sorting_test");
        String coverageName = reader.getGridCoverageNames()[0];

        Map<String, DimensionDescriptor> descriptors =
                reader.getDimensionDescriptors(coverageName).stream()
                        .collect(Collectors.toMap(dd -> dd.getName(), dd -> dd));
        assertEquals(4, descriptors.size());
        DimensionDescriptor crsDescriptor = descriptors.get(DimensionDescriptor.CRS);
        assertNotNull(crsDescriptor);
        assertEquals("crs", crsDescriptor.getStartAttribute());
        DimensionDescriptor resolutionDescriptor = descriptors.get(DimensionDescriptor.RESOLUTION);
        assertNotNull(resolutionDescriptor);
        assertEquals("resolution", resolutionDescriptor.getStartAttribute());
        DimensionDescriptor resolutionXDescriptor =
                descriptors.get(DimensionDescriptor.RESOLUTION_X);
        assertNotNull(resolutionXDescriptor);
        assertEquals("resX", resolutionXDescriptor.getStartAttribute());
        DimensionDescriptor resolutionYDescriptor =
                descriptors.get(DimensionDescriptor.RESOLUTION_Y);
        assertNotNull(resolutionYDescriptor);
        assertEquals("resY", resolutionYDescriptor.getStartAttribute());

        GranuleSource granules = reader.getGranules(coverageName, true);
        SimpleFeatureCollection features = granules.getGranules(Query.ALL);
        List<SimpleFeature> featureList = DataUtilities.list(features);
        for (SimpleFeature sf : featureList) {
            String location = (String) sf.getAttribute("location");
            String crs = (String) sf.getAttribute("crs");
            assertEquals("EPSG:32610", crs);
            Double resolution = (Double) sf.getAttribute("resolution");
            Double resX = (Double) sf.getAttribute("resolution");
            Double resY = (Double) sf.getAttribute("resolution");
            // yes, weird difference between file name and resolution, resolution here comes
            // from a gdalinfo run on the files
            if (location.startsWith("32km")) {
                assertEquals(17550, resolution, 10d);
                assertEquals(17550, resX, 10d);
                assertEquals(17550, resY, 10d);
            } else if (location.startsWith("16km")) {
                assertEquals(8712, resolution, 10d);
                assertEquals(8712, resX, 10d);
                assertEquals(8712, resY, 10d);
            }
        }
        reader.dispose();
    }

    @Test
    public void testExistingSchemaWithCrsAttribute() throws Exception {
        // prepare test data and let it run once
        String testLocation = "hetero_utm";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        GridCoverage2D coverage = imReader.read(null);
        if (coverage.getRenderedImage() instanceof PlanarImage) {
            ImageUtilities.disposePlanarImageChain((PlanarImage) coverage.getRenderedImage());
        }
        coverage.dispose(true);
        imReader.dispose();

        // append the CrsAttribute to the indexer.properties
        try (FileWriter out =
                new FileWriter(new File(testDirectory, "hetero_utm.properties"), true)) {
            out.write("UseExistingSchema=true\n");
            out.write("CrsAttribute=crs\n");
            out.flush();
        }

        // now rebuild mosaic (used to fail)
        imReader = new ImageMosaicReader(testDirectory, null);
        imReader.dispose();
    }

    @Test
    public void testHeteroExternalOverviews() throws Exception {
        String testLocation = "hetero_s2_ovr";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        Assert.assertNotNull(imReader);

        // check what gets used for source file for native resolution
        List<String> filesNativeRes = getSourceFilesForParams(imReader);
        assertThat(filesNativeRes, containsInAnyOrder("g1.tif", "g2.tif", "g4.tif", "g3.tif"));

        // check what gets used for source file at half resolution
        ParameterValue<GridGeometry2D> ggp = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        GridEnvelope originalRange = imReader.getOriginalGridRange();
        GridEnvelope readRange =
                new GridEnvelope2D(
                        0, 0, originalRange.getSpan(0) / 4, originalRange.getSpan(1) / 4);
        GridGeometry2D gg = new GridGeometry2D(readRange, imReader.getOriginalEnvelope());
        ggp.setValue(gg);
        List<String> filesExtOvr = getSourceFilesForParams(imReader, ggp);
        assertThat(
                filesExtOvr,
                containsInAnyOrder("g1.tif.ovr", "g2.tif.ovr", "g4.tif.ovr", "g3.tif.ovr"));

        imReader.dispose();
    }

    public List<String> getSourceFilesForParams(
            ImageMosaicReader imReader, GeneralParameterValue... params) throws IOException {
        GridCoverage2D coverage = imReader.read(params);
        RenderedImage ri = coverage.getRenderedImage();
        return getInputFileNames(ri);
    }

    @SuppressWarnings("PMD.CloseResource") // image stream closed along the reader
    private List<String> getInputFileNames(RenderedImage inputImage) {
        List<String> files = new ArrayList<>();
        if (inputImage instanceof PlanarImage) {
            PlanarImage planarImage = (PlanarImage) inputImage;

            final int nSources = planarImage.getNumSources();
            if (nSources > 0) {
                for (int k = 0; k < nSources; k++) {
                    Object source = null;
                    try {
                        source = planarImage.getSourceObject(k);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Ignore
                    }
                    if (source != null) {
                        if (source instanceof PlanarImage) {
                            List<String> piFiles = getInputFileNames((RenderedImage) source);
                            files.addAll(piFiles);
                        }
                    }
                }
            } else {
                // grab the streams from the reader, the particular implementation being
                // used has an accessor for the source files
                Object imageReader =
                        inputImage.getProperty(ImageReadDescriptor.PROPERTY_NAME_IMAGE_READER);
                if ((imageReader != null) && (imageReader instanceof ImageReader)) {
                    final ImageReader reader = (ImageReader) imageReader;
                    final ImageInputStream stream = (ImageInputStream) reader.getInput();
                    if (stream instanceof FileImageInputStreamExtImpl) {
                        FileImageInputStreamExtImpl fis = (FileImageInputStreamExtImpl) stream;
                        File file = fis.getFile();
                        files.add(file.getName());
                    }
                }
            }
        }
        return files;
    }

    @Test
    public void testHeterogeneousCRSReadInGranulesCRS() throws Exception {

        // Sample mosaic is made of 3 different UTM zones and mosaicCRS is 4326.
        // Each granule is filled with a color and have a white stripe in the middle.
        // red: EPSG:32631
        // green: EPSG:32632
        // blue: EPSG:32633
        String testLocation = "heterogeneous_crs_2";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        CoordinateReferenceSystem utmZone32N = CRS.decode("EPSG:32632", true);
        GeneralEnvelope envelope =
                new GeneralEnvelope(new double[] {150000, 600000}, new double[] {850000, 1200000});
        envelope.setCoordinateReferenceSystem(utmZone32N);
        GridEnvelope2D gridRange = new GridEnvelope2D(0, 0, 700, 600);

        // Setting up an UTM gridGeometry
        GridGeometry2D readingGridGeometry = new GridGeometry2D(gridRange, envelope);
        ParameterValue<GridGeometry2D> ggParam =
                AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(readingGridGeometry);

        GridCoverage2D gc = imReader.read(new GeneralParameterValue[] {ggParam});

        // Check that we get back ImageMosaic on its "common" CRS (4326)
        CoordinateReferenceSystem wgs84 = DefaultGeographicCRS.WGS84;
        assertTrue(CRS.equalsIgnoreMetadata(wgs84, gc.getCoordinateReferenceSystem()));
        RenderedImage ri = gc.getRenderedImage();
        Map<String, Set<RenderedOp>> operationsGroups = new HashMap<>();
        groupOperations(ri, operationsGroups);

        Set<RenderedOp> imageReads = operationsGroups.get("ImageRead");
        int granulesRead = imageReads.size();
        // All the 3 granules have been reprojected
        assertEquals(3, granulesRead);
        assertEquals(granulesRead, operationsGroups.get("Warp").size());
        gc.dispose(true);

        //
        // Repeat the TEST by setting the flag to provide the output in alternative CRS.
        //

        // Test the metadata value
        String epsgCodes =
                imReader.getMetadataValue(AbstractGridCoverage2DReader.MULTICRS_EPSGCODES);

        // The reader supports EPSG:32632
        assertTrue(epsgCodes.contains("EPSG:32632"));
        assertTrue(Utils.isSupportedCRS(imReader, utmZone32N));

        ParameterValue<Boolean> useAlternativeCRS =
                ImageMosaicFormat.OUTPUT_TO_ALTERNATIVE_CRS.createValue();
        useAlternativeCRS.setValue(true);
        gc = imReader.read(new GeneralParameterValue[] {ggParam, useAlternativeCRS});

        // Check that the output is in the requested CRS (no 4326 anymore)
        assertTrue(CRS.equalsIgnoreMetadata(utmZone32N, gc.getCoordinateReferenceSystem()));
        MathTransform transform = gc.getGridGeometry().getGridToCRS();
        AffineTransform tx = (AffineTransform) transform;

        // Check that we are getting the original resolution
        // of the granule in that projection
        assertEquals(1000, XAffineTransform.getScaleX0(tx), DELTA);
        assertEquals(1000, XAffineTransform.getScaleY0(tx), DELTA);

        ri = gc.getRenderedImage();
        operationsGroups.clear();
        groupOperations(ri, operationsGroups);
        // Check that all the 3 granules have been read but only 2
        // of them have been warped
        imageReads = operationsGroups.get("ImageRead");
        Set<RenderedOp> warps = operationsGroups.get("Warp");
        granulesRead = imageReads.size();
        assertEquals(3, granulesRead);
        assertEquals(2, warps.size());
        for (RenderedOp warp : warps) {
            removeImagesBeingWarped(warp, imageReads);
        }
        // get the only imageRead not being warped along the chain
        assertEquals(1, imageReads.size());
        RenderedOp unwarpedImage = imageReads.iterator().next();
        final ParameterBlock block = unwarpedImage.getParameterBlock();
        List<Object> paramValues = block.getParameters();
        // The green.tif is the image with native CRS = 32632 so the only one not being reprojected
        assertTrue(
                ((FileImageInputStreamExtImpl) paramValues.get(0))
                        .getFile()
                        .getAbsolutePath()
                        .contains("green.tif"));
        imReader.dispose();
    }

    private void removeImagesBeingWarped(RenderedOp image, Set<RenderedOp> imageReads) {
        List sources = image.getSources();
        Iterator it = sources.iterator();
        while (it.hasNext()) {
            Object source = it.next();
            if (source instanceof RenderedOp) {
                RenderedOp op = (RenderedOp) source;
                String opName = op.getOperationName();
                if (opName.equalsIgnoreCase("ImageRead")) {
                    imageReads.remove(op);
                    return;
                } else {
                    removeImagesBeingWarped(op, imageReads);
                }
            }
        }
    }

    private void groupOperations(Object ri, Map<String, Set<RenderedOp>> operationsSet) {
        if (ri instanceof RenderedOp) {
            RenderedOp op = (RenderedOp) ri;
            String opName = op.getOperationName();
            Set<RenderedOp> set = operationsSet.get(opName);
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(op);
            operationsSet.put(opName, set);
            List sources = op.getSources();
            Iterator it = sources.iterator();
            while (it.hasNext()) {
                Object source = it.next();
                groupOperations(source, operationsSet);
            }
            return;
        }
    }

    @Test
    public void testNativeEnvelope() throws Exception {
        String testLocation = "heterogeneous_crs_2";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        GranuleSource source = imReader.getGranules(imReader.getGridCoverageNames()[0], true);

        // no decoration requested, no time should be spent adding native bounds
        SimpleFeatureCollection fc = source.getGranules(Query.ALL);
        try (SimpleFeatureIterator fi = fc.features()) {
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                assertThat(f.getUserData(), not(hasKey(GranuleSource.NATIVE_BOUNDS_KEY)));
            }
        }

        // decoration requested this time
        Query q = new Query();
        q.getHints().put(GranuleSource.NATIVE_BOUNDS, true);
        SimpleFeatureCollection fcb = source.getGranules(q);
        try (SimpleFeatureIterator fi = fcb.features()) {
            while (fi.hasNext()) {
                SimpleFeature f = fi.next();
                assertThat(f.getUserData(), hasKey(GranuleSource.NATIVE_BOUNDS_KEY));
                // check the native bounds are reported in the native CRS
                String crs = (String) f.getAttribute("crs");
                ReferencedEnvelope envelope =
                        (ReferencedEnvelope) f.getUserData().get(GranuleSource.NATIVE_BOUNDS_KEY);
                assertTrue(
                        CRS.equalsIgnoreMetadata(
                                CRS.decode(crs, true), envelope.getCoordinateReferenceSystem()));
            }
        }
    }

    @Test
    public void testHeteroSentinel2DryRun() throws Exception {
        String testLocation = "hetero_s2";
        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);

        // create a reader so that the shapefile store is generated
        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, null);
        Assert.assertNotNull(imReader);
        imReader.dispose();

        // setup the datastore.properties wit the store name
        final String TEST_STORE = "testStore";
        File dsProperties = new File(testDirectory, "datastore.properties");
        Properties properties = new Properties();
        properties.put(Utils.Prop.STORE_NAME, "testStore");
        try (FileOutputStream fos = new FileOutputStream(dsProperties)) {
            properties.store(fos, null);
        }

        // wrap the shapefile store with one that records the Queries being used
        URL shapefileURL = fileToUrl(new File(testDirectory, "hetero_s2.shp"));
        ShapefileDataStore ds = new ShapefileDataStore(shapefileURL);
        List<Query> queries = new ArrayList<>();
        AtomicBoolean recordQueries = new AtomicBoolean(false);
        DecoratingDataStore decoratingStore =
                new DecoratingDataStore(ds) {
                    @Override
                    public SimpleFeatureSource getFeatureSource(String typeName)
                            throws IOException {
                        SimpleFeatureSource source = super.getFeatureSource(typeName);
                        if (recordQueries.get()) {
                            return new QueryCollectingFeatureSource(source, queries);
                        }
                        return source;
                    }
                };

        // setup and pass the repository
        DefaultRepository repository = new DefaultRepository();
        repository.register(TEST_STORE, decoratingStore);

        ImageMosaicReader repoReader =
                new ImageMosaicReader(testDirectory, new Hints(Hints.REPOSITORY, repository));

        // setup a grid geometry matching data, but a filter that won't let tiles be loaded
        ParameterValue<GridGeometry2D> ggp = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        GridEnvelope2D range = new GridEnvelope2D(0, 0, 158, 103);
        GridGeometry2D gg =
                new GridGeometry2D(
                        range,
                        new ReferencedEnvelope(
                                11.6834779,
                                11.8616874,
                                47.6380806,
                                47.7542552,
                                CRS.decode("EPSG:4236", true)));
        ggp.setValue(gg);
        ParameterValue<Filter> filter = ImageMosaicFormat.FILTER.createValue();
        filter.setValue(CQL.toFilter("location = 'IAmNotThere.tif'"));

        // record queries only during the read
        recordQueries.set(true);
        GridCoverage2D coverage = repoReader.read(new GeneralParameterValue[] {ggp, filter});
        recordQueries.set(false);
        if (coverage != null) coverage.dispose(true);
        repoReader.dispose();
        repository.dataStore(TEST_STORE).dispose();

        // check the last query did not have the sorting (it's the dry run),
        // but the second to last, which was the first attempt load the granules, did
        // (a bit fragile, but don't have a better idea)
        int size = queries.size();
        Query dryRunQuery = queries.get(size - 1);
        // dry run, no sorting, just spatial filtering
        assertNull(dryRunQuery.getSortBy());
        assertThat(dryRunQuery.getFilter(), Matchers.instanceOf(BBOX.class));
        // data access query, also attribute filtering and sorting present
        Query dataQuery = queries.get(size - 2);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        assertArrayEquals(
                new SortBy[] {ff.sort("crs", SortOrder.ASCENDING)}, dataQuery.getSortBy());
        assertThat(dataQuery.getFilter(), Matchers.instanceOf(And.class));
    }
}
