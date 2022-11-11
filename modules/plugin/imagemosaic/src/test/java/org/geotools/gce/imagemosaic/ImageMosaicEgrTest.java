/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertEquals;

import java.awt.RenderingHints;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProviderFactory;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;

public class ImageMosaicEgrTest {

    private static ExecutorService coverageExecutor;

    private static Hints hints;

    private File testMosaic;

    private URL testMosaicUrl;

    private File testAlphaMosaic;

    private URL testAlphaMosaicUrl;

    private File testAlphaHeteroMosaic;

    private URL testAlphaHeteroMosaicUrl;

    @AfterClass
    public static void close() {
        System.clearProperty("org.geotools.referencing.forceXY");
        CRS.reset("all");

        coverageExecutor.shutdownNow();
    }

    @BeforeClass
    public static void init() {
        // make sure CRS ordering is correct
        CRS.reset("all");
        System.setProperty("org.geotools.referencing.forceXY", "true");

        // create the executor
        coverageExecutor = Executors.newCachedThreadPool();
        hints = new Hints(new RenderingHints(Hints.EXECUTOR_SERVICE, coverageExecutor));
    }

    @Before
    public void setupMosaic() throws IOException {
        // clean up
        testMosaic = new File("target", "egrMosaic");
        if (testMosaic.exists()) {
            FileUtils.deleteDirectory(testMosaic);
        }

        // create the base mosaic we are going to use
        File mosaicSource = TestData.file(this, "egr");
        FileUtils.copyDirectory(mosaicSource, testMosaic);
        testMosaicUrl = URLs.fileToUrl(testMosaic);
    }

    @Before
    public void setupAlphaMosaic() throws IOException {
        // clean up
        testAlphaMosaic = new File("target", "egrAlphaMosaic");
        if (testAlphaMosaic.exists()) {
            FileUtils.deleteDirectory(testAlphaMosaic);
        }

        // create the base mosaic we are going to use
        File mosaicSource = TestData.file(this, "rgba");
        FileUtils.copyDirectory(mosaicSource, testAlphaMosaic);
        Arrays.stream(
                        testAlphaMosaic.listFiles(
                                f -> {
                                    String name = f.getName();
                                    return name.startsWith("rgba")
                                            || name.equals("sample_image.dat");
                                }))
                .forEach(f -> f.delete());
        testAlphaMosaicUrl = URLs.fileToUrl(testAlphaMosaic);
    }

    @Before
    public void setupAlphaHeteroMosaic() throws IOException {
        // clean up
        testAlphaHeteroMosaic = new File("target", "egrAlphaHeteroMosaic");
        if (testAlphaHeteroMosaic.exists()) {
            FileUtils.deleteDirectory(testAlphaHeteroMosaic);
        }

        // create the base mosaic we are going to use
        File mosaicSource = TestData.file(this, "egrAlpha");
        FileUtils.copyDirectory(mosaicSource, testAlphaHeteroMosaic);
        testAlphaHeteroMosaicUrl = URLs.fileToUrl(testAlphaHeteroMosaic);
    }

    private GeneralParameterValue[] getFootprintReadParams(
            GridCoverage2DReader reader, SimpleEntry... entries) {
        // activate footprint management
        List<GeneralParameterValue> params = new ArrayList<>();
        ParameterValue<String> footprintManagement =
                AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintManagement.setValue(FootprintBehavior.Transparent.name());
        params.add(footprintManagement);

        // this prevents us from having problems with link to files still open.
        ParameterValue<Boolean> jaiImageRead = ImageMosaicFormat.USE_JAI_IMAGEREAD.createValue();
        jaiImageRead.setValue(false);
        params.add(jaiImageRead);

        // other entries if available
        boolean foundGridGeometry = false;
        if (entries != null) {
            for (SimpleEntry entry : entries) {
                ParameterValue<?> pv = ((ParameterDescriptor) entry.getKey()).createValue();
                pv.setValue(entry.getValue());
                params.add(pv);
                foundGridGeometry |= entry.getKey().equals(AbstractGridFormat.READ_GRIDGEOMETRY2D);
            }
        }

        // makes the output image small
        if (!foundGridGeometry) {
            ParameterValue<GridGeometry2D> geom =
                    AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
            geom.setValue(
                    new GridGeometry2D(
                            new GridEnvelope2D(0, 0, 300, 300), reader.getOriginalEnvelope()));
            params.add(geom);
        }

        GeneralParameterValue[] result = params.toArray(new GeneralParameterValue[params.size()]);
        return result;
    }

    private void createRasterFootprintsProperties(File testMosaicRaster)
            throws FileNotFoundException, IOException {
        Properties p = new Properties();
        // Setting Raster property
        p.put(MultiLevelROIProviderFactory.SOURCE_PROPERTY, "raster");
        try (FileOutputStream fos =
                new FileOutputStream(new File(testMosaicRaster, "footprints.properties"))) {
            p.store(fos, null);
        }
    }

    private void createVectorFootprintsProperties(File testMosaicRaster)
            throws FileNotFoundException, IOException {
        Properties p = new Properties();
        // Setting to use a sidecar wkt
        p.put(MultiLevelROIProviderFactory.SOURCE_PROPERTY, "sidecar");
        // the vector is not an exact match, that would result in black artifacts
        p.put("footprint_inset", "0.01");
        try (FileOutputStream fos =
                new FileOutputStream(new File(testMosaicRaster, "footprints.properties"))) {
            p.store(fos, null);
        }
    }

    @Test
    public void testAllImagesRaster() throws Exception {
        createRasterFootprintsProperties(testMosaic);
        File sample =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-all-desc-raster.png");
        testAllImages(sample);
    }

    @Test
    public void testAllImagesVector() throws Exception {
        createVectorFootprintsProperties(testMosaic);
        File sample =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-all-desc-vector.png");
        testAllImages(sample);
    }

    private void testAllImages(File expectedOutput) throws Exception {
        ImageMosaicReader reader = new ImageMosaicReader(testMosaicUrl, hints);

        // test with no EGR
        GeneralParameterValue[] readParams =
                getFootprintReadParams(reader, new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z D"));
        testOutputCoverage(
                reader,
                readParams,
                expectedOutput,
                "3_mid.tiff",
                "2_right.tiff",
                "1_left.tiff",
                "0_large.tiff");

        // test with EGR, should be the same, all features are essential
        readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z D"),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI));
        testOutputCoverage(
                reader,
                readParams,
                expectedOutput,
                "3_mid.tiff",
                "2_right.tiff",
                "1_left.tiff",
                "0_large.tiff");

        // test with EGR and MT
        readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z D"),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI),
                        new SimpleEntry<>(ImageMosaicFormat.ALLOW_MULTITHREADING, true));
        testOutputCoverage(
                reader,
                readParams,
                expectedOutput,
                "3_mid.tiff",
                "2_right.tiff",
                "1_left.tiff",
                "0_large.tiff");
        reader.dispose();
    }

    @Test
    public void testRedCoversAllRaster() throws Exception {
        createRasterFootprintsProperties(testMosaic);
        File sample =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-red-covers-all-raster.png");
        testRedCoversAll(sample);
    }

    @Test
    public void testRedCoversAllVector() throws Exception {
        createVectorFootprintsProperties(testMosaic);
        File sample =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-red-covers-all-vector.png");
        testRedCoversAll(sample);
    }

    private void testRedCoversAll(File expectedOutput) throws Exception {
        ImageMosaicReader reader = new ImageMosaicReader(testMosaicUrl, hints);

        // test with no EGR
        GeneralParameterValue[] readParams =
                getFootprintReadParams(reader, new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z A"));
        testOutputCoverage(
                reader,
                readParams,
                expectedOutput,
                "0_large.tiff",
                "1_left.tiff",
                "2_right.tiff",
                "3_mid.tiff");

        // test with EGR, should be the same, all features are essential
        readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z A"),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI));
        testOutputCoverage(reader, readParams, expectedOutput, "0_large.tiff");

        // test with EGR and MT
        readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z A"),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI),
                        new SimpleEntry<>(ImageMosaicFormat.ALLOW_MULTITHREADING, true));
        testOutputCoverage(reader, readParams, expectedOutput, "0_large.tiff");
        reader.dispose();
    }

    @Test
    public void testLeftRightOnTopRaster() throws Exception {
        createRasterFootprintsProperties(testMosaic);
        File sample =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-left-right-top-raster.png");
        testLeftRightOnTop(sample);
    }

    @Test
    public void testLeftRightOnTopVector() throws Exception {
        createVectorFootprintsProperties(testMosaic);
        File sample =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-left-right-top-vector.png");
        testLeftRightOnTop(sample);
    }

    private void testLeftRightOnTop(File expectedOutput) throws Exception {
        ImageMosaicReader reader = new ImageMosaicReader(testMosaicUrl, hints);

        // filter out the large granule, the left and right ones will still cover the mid one
        Filter filter = ECQL.toFilter("z <> 0");

        // test with no EGR
        GeneralParameterValue[] readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z A"),
                        new SimpleEntry<>(ImageMosaicFormat.FILTER, filter));
        testOutputCoverage(
                reader, readParams, expectedOutput, "1_left.tiff", "2_right.tiff", "3_mid.tiff");

        // test with EGR, should be the same, all features are essential
        readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z A"),
                        new SimpleEntry<>(ImageMosaicFormat.FILTER, filter),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI));
        testOutputCoverage(reader, readParams, expectedOutput, "1_left.tiff", "2_right.tiff");

        // test with EGR and MT
        readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z A"),
                        new SimpleEntry<>(ImageMosaicFormat.FILTER, filter),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI),
                        new SimpleEntry<>(ImageMosaicFormat.ALLOW_MULTITHREADING, true));
        testOutputCoverage(reader, readParams, expectedOutput, "1_left.tiff", "2_right.tiff");
        reader.dispose();
    }

    @Test
    public void testSingleRaster() throws Exception {
        createRasterFootprintsProperties(testMosaic);
        File sample =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-red-rect.png");
        checkSingle(sample);
    }

    @Test
    public void testSingleVector() throws Exception {
        createVectorFootprintsProperties(testMosaic);
        File sample =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-red-rect.png");
        checkSingle(sample);
    }

    public void checkSingle(File expectedOutput) throws Exception {
        ImageMosaicReader reader = new ImageMosaicReader(testMosaicUrl, hints);

        // read a small grid geometry that will only catch one feature
        GridGeometry2D readGeometry =
                new GridGeometry2D(
                        new GridEnvelope2D(0, 0, 300, 300),
                        new ReferencedEnvelope(
                                -0.667, -0.640, 0.386, 0.412, DefaultGeographicCRS.WGS84));

        // test with no EGR
        GeneralParameterValue[] readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.READ_GRIDGEOMETRY2D, readGeometry),
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z D"));
        testOutputCoverage(reader, readParams, expectedOutput);

        // test with EGR, should be the same, all features are essential
        readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.READ_GRIDGEOMETRY2D, readGeometry),
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z D"),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI));
        testOutputCoverage(reader, readParams, expectedOutput, "0_large.tiff");

        // test with EGR and MT
        readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.READ_GRIDGEOMETRY2D, readGeometry),
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z D"),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI),
                        new SimpleEntry<>(ImageMosaicFormat.ALLOW_MULTITHREADING, true));
        testOutputCoverage(reader, readParams, expectedOutput, "0_large.tiff");
        reader.dispose();
    }

    private void testOutputCoverage(
            ImageMosaicReader reader,
            GeneralParameterValue[] readParams,
            File expectedOutput,
            String... expectedImages)
            throws IOException {
        GridCoverage2D coverage = reader.read(readParams);
        assertSourceFileNames(coverage, expectedImages);
        ImageAssert.assertEquals(expectedOutput, coverage.getRenderedImage(), 300);
    }

    private void assertSourceFileNames(GridCoverage2D coverage, String... expectedNamesArray) {
        String sources = (String) coverage.getProperty(GridCoverage2DReader.FILE_SOURCE_PROPERTY);
        if (expectedNamesArray != null && expectedNamesArray.length > 0) {
            String[] names = sources.split("\\s*,\\s*");
            LinkedHashSet<String> actualNames = new LinkedHashSet<>();
            for (String name : names) {
                actualNames.add(new File(name).getName());
            }
            LinkedHashSet<String> expectedNames =
                    new LinkedHashSet<>(Arrays.asList(expectedNamesArray));
            assertEquals(expectedNames, actualNames);
        }
    }

    @Test
    public void testHeteroCRSAlpha() throws Exception {
        ImageMosaicReader reader = new ImageMosaicReader(testAlphaHeteroMosaicUrl, hints);

        // EGR does not really work when all images are in different CRSs, as it operates
        // at the single uniform CRS sub-mosaic level, but we check the output is still fine
        GeneralParameterValue[] readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z D"),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI),
                        new SimpleEntry<>(ImageMosaicFormat.FOOTPRINT_BEHAVIOR, "Transparent"));
        GridCoverage2D coverage = reader.read(readParams);
        // there is a bit of black-ish border as the images have been painted with antialiasing
        File expectedOutput =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-alpha-hetero.png");
        ImageAssert.assertEquals(expectedOutput, coverage.getRenderedImage(), 300);
    }

    @Test
    public void testHeteroCRSAlphaBlend() throws Exception {
        ImageMosaicReader reader = new ImageMosaicReader(testAlphaHeteroMosaicUrl, hints);

        // EGR does not really work when all images are in different CRSs, as it operates
        // at the single uniform CRS sub-mosaic level, but we check the output is still fine
        GeneralParameterValue[] readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "z D"),
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI),
                        new SimpleEntry<>(ImageMosaicFormat.FOOTPRINT_BEHAVIOR, "Transparent"),
                        new SimpleEntry<>(ImageMosaicFormat.FADING, true));
        GridCoverage2D coverage = reader.read(readParams);
        // in fading mode images are blent toghether rather than mosaicked. Just checking
        // that handling alpha as a ROI does not cause issues
        File expectedOutput =
                new File(
                        "src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-alpha-hetero-fade.png");
        ImageAssert.assertEquals(expectedOutput, coverage.getRenderedImage(), 300);
    }

    @Test
    public void testAlpha() throws Exception {
        ImageMosaicReader reader = new ImageMosaicReader(testAlphaMosaicUrl, hints);

        // in this overlap only a single image will be used
        GridGeometry2D geom =
                new GridGeometry2D(
                        new GridEnvelope2D(0, 0, 300, 300),
                        new ReferencedEnvelope(
                                -1380000,
                                -1026000,
                                1866000,
                                2200000,
                                reader.getCoordinateReferenceSystem()));

        GeneralParameterValue[] readParams =
                getFootprintReadParams(
                        reader,
                        new SimpleEntry<>(
                                ImageMosaicFormat.EXCESS_GRANULE_REMOVAL, ExcessGranulePolicy.ROI),
                        new SimpleEntry<>(ImageMosaicFormat.FOOTPRINT_BEHAVIOR, "Transparent"),
                        new SimpleEntry<>(ImageMosaicFormat.SORT_BY, "location A"),
                        new SimpleEntry<>(AbstractGridFormat.READ_GRIDGEOMETRY2D, geom));
        GridCoverage2D coverage = reader.read(readParams);
        // there is a bit of black-ish border as the images have been painted with antialiasing
        File expectedOutput =
                new File("src/test/resources/org/geotools/gce/imagemosaic/test-data/egr-alpha.png");
        ImageAssert.assertEquals(expectedOutput, coverage.getRenderedImage(), 300);
        assertSourceFileNames(coverage, "passA2006128193711.png");
    }
}
