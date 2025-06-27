/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import it.geosolutions.jaiext.stats.Statistics;
import it.geosolutions.jaiext.stats.Statistics.StatsType;
import it.geosolutions.jaiext.zonal.ZoneGeometry;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.WorldFileReader;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This test-class checks the functionalities of the {@link RasterZonalStatistics2} process. The test is performed by
 * comparing the results of the process with the previously known statistics for each geometry.
 *
 * @author geosolutions
 */
public class ZonalStatsProcess2Test extends Assert {

    /** ZonalStats process */
    private RasterZonalStatistics2 process;

    /** Datastore containing the input features */
    private DataStore ds;

    @Before
    public void setup() throws Exception {
        // Preparation of the process
        process = new RasterZonalStatistics2();
        // Selection of the input files
        File file = TestData.file(this, null);
        ds = new PropertyDataStore(file);
        TestData.unzipFile(this, "test.zip");
    }

    @After
    public void tearDown() {
        ds.dispose();
    }

    @Test
    public void simpleZonalStatsProcess() throws Exception {
        DataStore store = null;
        TIFFImageReader reader = null;
        GridCoverage2D coverage2D = null;
        GridCoverage2D covClassificator = null;
        try {
            // build the feature collection
            final File fileshp = TestData.file(this, "testpolygon.shp");
            store = FileDataStoreFinder.getDataStore(fileshp.toURI().toURL());
            assertNotNull(store);
            assertTrue(store instanceof ShapefileDataStore);
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource =
                    store.getFeatureSource(store.getNames().get(0));
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) featureSource.getFeatures();

            List<SimpleFeature> zones = new ArrayList<>(featureCollection.size());
            try (SimpleFeatureIterator iterator = featureCollection.features()) {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();

                    zones.add(feature);
                }
            }

            // build the DataFile
            final File tiff = TestData.file(this, "test.tif");
            final File tfw = TestData.file(this, "test.tfw");
            reader = (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader)
                    new TIFFImageReaderSpi().createReaderInstance();
            assertNotNull(reader);
            reader.setInput(ImageIO.createImageInputStream(tiff));
            final BufferedImage image = reader.read(0);
            reader.dispose();
            // Transformation from the Raster space to the Model space
            final MathTransform transform = new WorldFileReader(tfw).getTransform();
            // Creation of the data coverage
            coverage2D = CoverageFactoryFinder.getGridCoverageFactory(null)
                    .create(
                            "coverage",
                            image,
                            new GridGeometry2D(
                                    new GridEnvelope2D(
                                            PlanarImage.wrapRenderedImage(image).getBounds()),
                                    transform,
                                    DefaultGeographicCRS.WGS84),
                            new GridSampleDimension[] {new GridSampleDimension("coverage")},
                            null,
                            null);
            assertNotNull(coverage2D);

            // build the classificator
            // generate the classificator image
            final BufferedImage imageClassificator = new BufferedImage(120, 80, BufferedImage.TYPE_BYTE_INDEXED);
            final WritableRaster raster = imageClassificator.getRaster();
            for (int i = raster.getWidth(); --i >= 0; ) {
                for (int j = raster.getHeight(); --j >= 0; ) {
                    // create a simple raster used for classification
                    int sampleValue = i % 2 == 0 ? 1 : 2;
                    raster.setSample(i, j, 0, sampleValue);
                }
            }
            // create the coverage for the classification layer
            covClassificator = CoverageFactoryFinder.getGridCoverageFactory(null)
                    .create(
                            "coverageClassificator",
                            imageClassificator,
                            new GridGeometry2D(
                                    new GridEnvelope2D(PlanarImage.wrapRenderedImage(imageClassificator)
                                            .getBounds()),
                                    coverage2D.getEnvelope()),
                            new GridSampleDimension[] {new GridSampleDimension("coverage")},
                            null,
                            null);
            assertNotNull(coverage2D);

            // Statistics definition

            StatsType[] def = {StatsType.MIN, StatsType.MAX, StatsType.SUM, StatsType.MEAN, StatsType.DEV_STD};

            // invoke the process
            List<ZoneGeometry> zoneListStart = process.execute(
                    coverage2D,
                    null,
                    zones,
                    covClassificator,
                    null,
                    null,
                    false,
                    null,
                    def,
                    null,
                    null,
                    null,
                    null,
                    false);

            // Reverse of the list due to a variation on the code
            List<ZoneGeometry> zoneList = new ArrayList<>(zoneListStart);

            // Zone 0
            ZoneGeometry geo0 = zoneList.get(0);

            // Class 1
            Statistics[] statistics01 = geo0.getStatsPerBandNoRange(0, 1);

            double min01 = (Double) statistics01[0].getResult();
            double max01 = (Double) statistics01[1].getResult();
            double mean01 = (Double) statistics01[3].getResult();
            double sum01 = (Double) statistics01[2].getResult();
            double dev_std01 = (Double) statistics01[4].getResult();

            // Percentual variation between the correct statistics and the calculated
            double minResult01 = Math.abs(1251 - min01) / 1251 * 100;
            double maxResult01 = Math.abs(1683 - max01) / 1683 * 100;
            double meanResult01 = Math.abs(1353.75 - mean01) / 1353.75 * 100;
            double sumResult01 = Math.abs(894829 - sum01) / 894829 * 100;
            double dev_stdResult01 = Math.abs(69.15 - dev_std01) / 69.15 * 100;

            assertTrue(minResult01 < 5);
            assertTrue(maxResult01 < 5);
            assertTrue(meanResult01 < 5);
            assertTrue(sumResult01 < 5);
            assertTrue(dev_stdResult01 < 5);

            // Class 2
            Statistics[] statistics02 = geo0.getStatsPerBandNoRange(0, 2);

            double min02 = (Double) statistics02[0].getResult();
            double max02 = (Double) statistics02[1].getResult();
            double mean02 = (Double) statistics02[3].getResult();
            double sum02 = (Double) statistics02[2].getResult();
            double dev_std02 = (Double) statistics02[4].getResult();

            // Percentual variation between the correct statistics and the calculated
            double minResult02 = Math.abs(1251 - min02) / 1251 * 100;
            double maxResult02 = Math.abs(1589 - max02) / 1589 * 100;
            double meanResult02 = Math.abs(1357.7 - mean02) / 1357.7 * 100;
            double sumResult02 = Math.abs(944959 - sum02) / 944959 * 100;
            double dev_stdResult02 = Math.abs(70.23 - dev_std02) / 70.23 * 100;

            assertTrue(minResult02 < 5);
            assertTrue(maxResult02 < 5);
            assertTrue(meanResult02 < 5);
            assertTrue(sumResult02 < 5);
            assertTrue(dev_stdResult02 < 5);

            // Zone 1
            ZoneGeometry geo1 = zoneList.get(1);

            // Class 1
            Statistics[] statistics11 = geo1.getStatsPerBandNoRange(0, 1);

            double min11 = (Double) statistics11[0].getResult();
            double max11 = (Double) statistics11[1].getResult();
            double mean11 = (Double) statistics11[3].getResult();
            double sum11 = (Double) statistics11[2].getResult();
            double dev_std11 = (Double) statistics11[4].getResult();

            // Percentual variation between the correct statistics and the calculated
            double minResult11 = Math.abs(1191 - min11) / 1191 * 100;
            double maxResult11 = Math.abs(1411 - max11) / 1411 * 100;
            double meanResult11 = Math.abs(1253.13 - mean11) / 1253.13 * 100;
            double sumResult11 = Math.abs(904757 - sum11) / 904757 * 100;
            double dev_stdResult11 = Math.abs(42.39 - dev_std11) / 42.39 * 100;

            assertTrue(minResult11 < 5);
            assertTrue(maxResult11 < 5);
            assertTrue(meanResult11 < 5);
            assertTrue(sumResult11 < 5);
            assertTrue(dev_stdResult11 < 5);

            // Class 2
            Statistics[] statistics12 = geo1.getStatsPerBandNoRange(0, 2);

            double min12 = (Double) statistics12[0].getResult();
            double max12 = (Double) statistics12[1].getResult();
            double mean12 = (Double) statistics12[3].getResult();
            double sum12 = (Double) statistics12[2].getResult();
            double dev_std12 = (Double) statistics12[4].getResult();

            // Percentual variation between the correct statistics and the calculated
            double minResult12 = Math.abs(1192 - min12) / 1192 * 100;
            double maxResult12 = Math.abs(1430 - max12) / 1430 * 100;
            double meanResult12 = Math.abs(1254.38 - mean12) / 1254.38 * 100;
            double sumResult12 = Math.abs(883082 - sum12) / 883082 * 100;
            double dev_stdResult12 = Math.abs(43.46 - dev_std12) / 43.46 * 100;

            assertTrue(minResult12 < 5);
            assertTrue(maxResult12 < 5);
            assertTrue(meanResult12 < 5);
            assertTrue(sumResult12 < 5);
            assertTrue(dev_stdResult12 < 5);

            // Zone 2
            ZoneGeometry geo2 = zoneList.get(2);

            // Class 1
            Statistics[] statistics21 = geo2.getStatsPerBandNoRange(0, 1);

            double min21 = (Double) statistics21[0].getResult();
            double max21 = (Double) statistics21[1].getResult();
            double mean21 = (Double) statistics21[3].getResult();
            double sum21 = (Double) statistics21[2].getResult();
            double dev_std21 = (Double) statistics21[4].getResult();

            // Percentual variation between the correct statistics and the calculated
            double minResult21 = Math.abs(1178 - min21) / 1178 * 100;
            double maxResult21 = Math.abs(1351 - max21) / 1351 * 100;
            double meanResult21 = Math.abs(1279.47 - mean21) / 1279.47 * 100;
            double sumResult21 = Math.abs(509230 - sum21) / 509230 * 100;
            double dev_stdResult21 = Math.abs(35.21 - dev_std21) / 35.21 * 100;

            assertTrue(minResult21 < 5);
            assertTrue(maxResult21 < 5);
            assertTrue(meanResult21 < 5);
            assertTrue(sumResult21 < 5);
            assertTrue(dev_stdResult21 < 5);

            // Class 2
            Statistics[] statistics22 = geo2.getStatsPerBandNoRange(0, 2);

            double min22 = (Double) statistics22[0].getResult();
            double max22 = (Double) statistics22[1].getResult();
            double mean22 = (Double) statistics22[3].getResult();
            double sum22 = (Double) statistics22[2].getResult();
            double dev_std22 = (Double) statistics22[4].getResult();

            // Percentual variation between the correct statistics and the calculated
            double minResult22 = Math.abs(1189 - min22) / 1189 * 100;
            double maxResult22 = Math.abs(1348 - max22) / 1348 * 100;
            double meanResult22 = Math.abs(1280.87 - mean22) / 1280.87 * 100;
            double sumResult22 = Math.abs(490572 - sum22) / 490572 * 100;
            double dev_stdResult22 = Math.abs(32.66 - dev_std22) / 32.66 * 100;

            assertTrue(minResult22 < 5);
            assertTrue(maxResult22 < 5);
            assertTrue(meanResult22 < 5);
            assertTrue(sumResult22 < 5);
            assertTrue(dev_stdResult22 < 5);

        } finally {
            try {
                if (store != null) {
                    store.dispose();
                }
            } catch (Exception e) {
            }
            try {
                if (reader != null) {
                    reader.dispose();
                }
            } catch (Exception e) {
            }
            try {
                if (coverage2D != null) {
                    coverage2D.dispose(true);
                }
            } catch (Exception e) {
            }
            try {
                if (covClassificator != null) {
                    covClassificator.dispose(true);
                }
            } catch (Exception e) {
            }
        }
    }
}
