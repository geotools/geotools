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

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.media.jai.Interpolation;

import org.apache.commons.io.FileUtils;
import org.geotools.TestData;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.image.test.ImageAssert;
import org.geotools.referencing.CRS;
import org.junit.Assert;
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

        URL storeUrl = org.geotools.TestData.url(this, testLocation);

        File testDataFolder = new File(storeUrl.toURI());
        File testDirectory = crsMosaicFolder.newFolder(testLocation);
        FileUtils.copyDirectory(testDataFolder, testDirectory);
        Hints creationHints = new Hints();
        ImageMosaicReader imReader = new ImageMosaicReader(testDirectory, creationHints);
        Assert.assertNotNull(imReader);

        //hack workaround for the store not being created with a consistent CRS in certain
        //environments.
        if (CRS.toSRS(imReader.getCoordinateReferenceSystem()).equals(expectedCRS)) {
            Collection<GeneralParameterValue> finalParamsCollection =
                new ArrayList<>(Arrays.asList(params));

            //Let's do a sort order to get the correct results
            ParameterValue<String> sortByParam = ImageMosaicFormat.SORT_BY.createValue();
            sortByParam.setValue(sortOrder);

            finalParamsCollection.add(sortByParam);

            GridCoverage2D gc2d = imReader
                .read(finalParamsCollection.toArray(new GeneralParameterValue[]{}));

            if (resultLocation != null) {
                RenderedImage renderImage = gc2d.getRenderedImage();
                File resultsFile = org.geotools.TestData
                    .file(this, resultLocation);

                //number 1000 was a bit arbitrary for differences, should account for small differences in
                //interpolation and such, but not the reprojection of the blue tiff. Correct and incorrect
                //images will be pretty similar anyway
                ImageAssert.assertEquals(resultsFile, renderImage, 1000);
            }
        }
    }
}
