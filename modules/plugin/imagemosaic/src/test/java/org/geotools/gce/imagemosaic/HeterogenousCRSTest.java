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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.media.jai.Interpolation;

import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 * Testing whether a simple mosaic correctly has its elements reprojected
 */
public class HeterogenousCRSTest {

    @Rule
    public TemporaryFolder crsMosaicFolder = new TemporaryFolder();
    
    @BeforeClass
    public static void ignoreReciprocals() throws Exception {
        MapProjection.SKIP_SANITY_CHECKS = true;
    }
    
    @BeforeClass
    public static void resetReciprocals() throws Exception {
        MapProjection.SKIP_SANITY_CHECKS = false;
    }

    @Test
    public void testHeterogeneousCRS() throws IOException, URISyntaxException, TransformException,
            FactoryException {
        testMosaic("heterogeneous_crs", "location D, crs A", "red_blue_results/red_blue_heterogeneous_results.tiff", "EPSG:3587");
    }

    @Test
    public void testDiffCRSSorting() throws IOException, URISyntaxException {
        testMosaic("diff_crs_sorting_test", "resolution D, crs A", "diff_crs_sorting_test_results/results.tiff", "EPSG:32610");
    }

    @Test
    public void testWithInterpolation() throws IOException, URISyntaxException {
        ParameterValue<Interpolation> interpolationParam =
            AbstractGridFormat.INTERPOLATION.createValue();
        interpolationParam.setValue(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
        testMosaic("diff_crs_sorting_test", "resolution D, crs A", null, "EPSG:32610", interpolationParam);
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
    }

    private void testMosaic(String testLocation, String sortOrder, String resultLocation,
        String expectedCRS, GeneralParameterValue... params)
        throws URISyntaxException, IOException {

        URL storeUrl = TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        Hints creationHints = new Hints();
        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, creationHints);
        Assert.assertNotNull(imReader);

        //hack workaround for the store not being created with a consistent CRS in certain
        //environments.
        assertEquals(CRS.toSRS(imReader.getCoordinateReferenceSystem()), expectedCRS);
        Collection<GeneralParameterValue> finalParamsCollection =
            new ArrayList<>(Arrays.asList(params));

        //Let's do a sort order to get the correct results
        ParameterValue<String> sortByParam = ImageMosaicFormat.SORT_BY.createValue();
        sortByParam.setValue(sortOrder);

        finalParamsCollection.add(sortByParam);

        GridCoverage2D gc2d = imReader
            .read(finalParamsCollection.toArray(new GeneralParameterValue[]{}));
        assertEquals(CRS.toSRS(gc2d.getCoordinateReferenceSystem()), expectedCRS);

        if (resultLocation != null) {
            RenderedImage renderImage = gc2d.getRenderedImage();
            File resultsFile = testFile(resultLocation);

            //number 1000 was a bit arbitrary for differences, should account for small differences in
            //interpolation and such, but not the reprojection of the blue tiff. Correct and incorrect
            //images will be pretty similar anyway
            ImageAssert.assertEquals(resultsFile, renderImage, 1000);
        }
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
        ParameterValue<Color> inputTransparentColor = AbstractGridFormat.INPUT_TRANSPARENT_COLOR.createValue();
        inputTransparentColor.setValue(Color.BLACK);
        ParameterValue<Color> outputTransparentColor = ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.createValue();
        outputTransparentColor.setValue(Color.BLACK);

        
        assertExpectedMosaic(imReader, expectedResultLocation, inputTransparentColor, outputTransparentColor);
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
        ParameterValue<String> footprintBehavior = AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
        footprintBehavior.setValue("Transparent");
        
        assertExpectedMosaic(imReader, expectedResultLocation, footprintBehavior);
        imReader.dispose();
    }
    
    @Test
    public void testHeteroCRSDateline() throws IOException, URISyntaxException, TransformException,
            FactoryException {
        URL storeUrl = TestData.url(this, "hetero_crs_dateline");

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder("hetero_crs_dateline");
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        Hints creationHints = new Hints();
        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, creationHints);
        Assert.assertNotNull(imReader);
        assertEquals(CRS.toSRS(imReader.getCoordinateReferenceSystem()), "EPSG:4326");
        
        // read before dateline
        GeneralParameterValue gg1 = buildGridGeometryParameter(new ReferencedEnvelope(179, 180, 60, 62, DefaultGeographicCRS.WGS84), 128, 256);
        GridCoverage2D gcBefore = imReader.read(new GeneralParameterValue[] {gg1});
        RenderedImage riBefore = gcBefore.getRenderedImage();
        ImageAssert.assertEquals(testFile("hetero_crs_dateline_results/before.png"), riBefore, 1000);
        
        // read after the dateline
        GeneralParameterValue ggAfter = buildGridGeometryParameter(new ReferencedEnvelope(180, 181, 60, 62, DefaultGeographicCRS.WGS84), 128, 256);
        GridCoverage2D gcAfter = imReader.read(new GeneralParameterValue[] {ggAfter});
        RenderedImage riAfter = gcAfter.getRenderedImage();
        ImageAssert.assertEquals(testFile("hetero_crs_dateline_results/after.png"), riAfter, 1000);
    }
    
    GeneralParameterValue buildGridGeometryParameter(ReferencedEnvelope envelope, int width, int height) {
        final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GridEnvelope2D range= new GridEnvelope2D(0, 0, width, height);
        final GridGeometry2D value = new GridGeometry2D(range,envelope);
        gg.setValue(value);
        return gg;
    }

    
    private void assertExpectedBounds(ReferencedEnvelope expected, ImageMosaicReader imReader) {
        // the specified CRS has been honored
        assertTrue(CRS.equalsIgnoreMetadata(imReader.getCoordinateReferenceSystem(), expected.getCoordinateReferenceSystem()));
        
        // getting the expected bounds (more or less)
        double EPS = 0.5d / 110; // pixel size is 1km, use half a pixel tolerance
        GeneralEnvelope envelope = imReader.getOriginalEnvelope();
        
        assertEquals(expected.getMinX(), envelope.getMinimum(0), EPS);
        assertEquals(expected.getMaxX(), envelope.getMaximum(0), EPS);
        assertEquals(expected.getMinY(), envelope.getMinimum(1), EPS);
        assertEquals(expected.getMaxY(), envelope.getMaximum(1), EPS);
    }

    private void assertExpectedMosaic(ImageMosaicReader imReader,
            final String expectedResultLocation, GeneralParameterValue... params) throws IOException {
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
    

}
