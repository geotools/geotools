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
package org.geotools.coverage.processing;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.stats.Statistics;
import it.geosolutions.jaiext.stats.Statistics.StatsType;
import it.geosolutions.jaiext.zonal.ZonalStatsDescriptor;
import it.geosolutions.jaiext.zonal.ZoneGeometry;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import junit.framework.TestCase;
import org.geotools.TestData;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.operation.ZonalStatistics;
import org.geotools.data.WorldFileReader;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * This test-class evaluates the functionalities of the "ZonalStatistics" {@link OperationJAI}. The
 * test is executed with and without the calculations of the statistics for each range. The utility
 * class {@link StatisticsTool} is used for storing the statistic results.
 *
 * @author Nicola Lagomarsini, GeoSolutions
 */
public class ZonalStatisticsTest extends TestCase {

    /** {@link Logger} used */
    private static final Logger LOGGER = Logging.getLogger(ZonalStatisticsTest.class);

    /** CoverageProcessor */
    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    /** Utility class used for calculating and preparing the results */
    private class StatisticsTool {

        /*
         * external user params
         */
        private Set<StatsType> statisticsSet;

        private int[] bands;

        private GridCoverage2D gridCoverage2D;

        private List<SimpleFeature> featureList;

        private boolean localStats;

        private List<Range> ranges;

        /*
         * results
         */
        private Map<String, Map<Integer, Map<StatsType, Object>>> feature2StatisticsMap =
                new HashMap<String, Map<Integer, Map<StatsType, Object>>>();

        private StatisticsTool(
                Set<StatsType> statisticsSet,
                GridCoverage2D gridCoverage2D,
                int[] bands,
                List<SimpleFeature> polygonsList,
                List<Range> ranges,
                boolean localStats) {
            this.statisticsSet = statisticsSet;
            this.gridCoverage2D = gridCoverage2D;
            this.bands = bands;
            this.featureList = polygonsList;
            this.localStats = localStats;
            this.ranges = ranges;
        }

        /**
         * Run the requested analysis.
         *
         * <p>This is the moment in which the analysis takes place. This method is intended to give
         * the user the possibility to choose the moment in which the workload is done.
         */
        public void run() throws Exception {
            processPolygonMode();
        }

        private void processPolygonMode() throws TransformException {

            final StatsType[] statistis =
                    statisticsSet.toArray(new StatsType[statisticsSet.size()]);

            // final OperationJAI op = new ZonalStatistics();
            ParameterValueGroup params = PROCESSOR.getOperation("Zonal").getParameters();
            params.parameter("Source").setValue(gridCoverage2D);
            params.parameter("stats").setValue(statistis);
            params.parameter("bands").setValue(bands);
            params.parameter("roilist").setValue(featureList);
            params.parameter("rangeData").setValue(ranges);
            params.parameter("localStats").setValue(localStats);
            // Execution of the operation
            final GridCoverage2D coverage =
                    (GridCoverage2D)
                            ((ZonalStatistics) PROCESSOR.getOperation("Zonal"))
                                    .doOperation(params, null);
            // Results for each geometry
            final List<ZoneGeometry> zoneList =
                    (List<ZoneGeometry>) coverage.getProperty(ZonalStatsDescriptor.ZS_PROPERTY);

            int zoneNum = zoneList.size();
            // For each input geometry the statistics are stored inside a Map
            for (int i = 0; i < zoneNum; i++) {
                // Selection of the geometry
                ZoneGeometry geom = zoneList.get(i);

                SimpleFeature feature = featureList.get(i);

                final String fid = feature.getID();
                // Creation of a Map associated with each range
                final Map<Integer, Map<StatsType, Object>> rangeMap =
                        new HashMap<Integer, Map<StatsType, Object>>();

                int count = 0;
                // If local statistics are requested, then the results are stored for each range
                if (localStats) {
                    // Cycle on all the ranges
                    for (Range range : ranges) {
                        // Selection of the statistics for the selected range
                        final Map<StatsType, Object> statsMap = new HashMap<StatsType, Object>();

                        Statistics[] stats = geom.getStatsPerBandPerClassPerRange(0, 0, range);

                        int statNum = stats.length;

                        for (int j = 0; j < statNum; j++) {

                            Statistics singleStat = stats[j];

                            StatsType type = statistis[j];

                            statsMap.put(type, singleStat.getResult());
                        }
                        // Insertion of the results
                        rangeMap.put(count, statsMap);

                        count++;
                    }
                } else {
                    // If Range statistics are not local then all the results are stored into only
                    // one
                    // map.
                    final Map<StatsType, Object> statsMap = new HashMap<StatsType, Object>();

                    Statistics[] stats = geom.getStatsPerBandNoClassifierNoRange(0);

                    int statNum = stats.length;

                    for (int j = 0; j < statNum; j++) {

                        Statistics singleStat = stats[j];

                        StatsType type = statistis[j];

                        statsMap.put(type, singleStat.getResult());
                    }

                    rangeMap.put(count, statsMap);

                    count++;
                }

                feature2StatisticsMap.put(fid, rangeMap);
            }
        }

        /**
         * Gets the performed statistics.
         *
         * @param fId the id of the feature used as region for the analysis.
         * @return the {@link Map} of results of the analysis for all the requested {@link Range}
         *     index, and for each index, the statistics are mapped.
         */
        public Map<Integer, Map<StatsType, Object>> getStatistics(String fId) {
            return feature2StatisticsMap.get(fId);
        }
    }

    @Before
    public void setUp() throws Exception {
        TestData.unzipFile(this, "test.zip");
    }

    @Test
    public void testPolygonLocalStats() throws Exception {
        // Input data
        final File tiff = TestData.file(this, "test.tif");
        final File tfw = TestData.file(this, "test.tfw");

        // Reading of the input image
        final TIFFImageReader reader =
                (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader)
                        new TIFFImageReaderSpi().createReaderInstance();
        reader.setInput(ImageIO.createImageInputStream(tiff));
        final BufferedImage image = reader.read(0);
        reader.dispose();

        // Transformation from the Raster space to the Model space
        final MathTransform transform = new WorldFileReader(tfw).getTransform();
        // Creation of the input coverage
        final GridCoverage2D coverage2D =
                CoverageFactoryFinder.getGridCoverageFactory(null)
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

        // Selection of the input geometries and creation of the related list.
        List<SimpleFeature> polygonList = new ArrayList<SimpleFeature>();
        FeatureIterator<SimpleFeature> featureIterator = ZonalStasTest.testPolygons.features();
        while (featureIterator.hasNext()) {
            SimpleFeature feature = featureIterator.next();
            polygonList.add(feature);
        }
        featureIterator.close();

        // choose the stats
        Set<StatsType> statsSet = new LinkedHashSet<StatsType>();
        statsSet.add(StatsType.MIN);
        statsSet.add(StatsType.MAX);
        statsSet.add(StatsType.MEAN);
        statsSet.add(StatsType.VARIANCE);
        statsSet.add(StatsType.DEV_STD);

        // Selection of the range where the calculations are performed
        List<Range> includedRanges = new ArrayList<Range>();
        includedRanges.add(RangeFactory.create(0f, false, 1300f, true, false));
        includedRanges.add(RangeFactory.create(1370f, true, 1600f, true, false));

        // select the bands to work on
        int[] bands = new int[] {0};

        // create the proper instance
        StatisticsTool statisticsTool =
                new StatisticsTool(statsSet, coverage2D, bands, polygonList, includedRanges, true);

        // do analysis
        statisticsTool.run();

        // get the results for the first polygon
        String id = "testpolygon.1";

        // First Range

        Map<StatsType, Object> statistics0 = statisticsTool.getStatistics(id).get(0);
        LOGGER.info(id + statistics0.toString());
        double sdev0 = (Double) statistics0.get(StatsType.DEV_STD);
        double min0 = (Double) statistics0.get(StatsType.MIN);
        double mean0 = (Double) statistics0.get(StatsType.MEAN);
        double var0 = (Double) statistics0.get(StatsType.VARIANCE);
        double max0 = (Double) statistics0.get(StatsType.MAX);

        // Percentual variation between the correct statistics and the calculated
        double minResult0 = Math.abs(1355d - min0) / 1355d * 100;
        double varResult0 = Math.abs(139.1754d - var0) / 139.1754d * 100;
        double maxResult0 = Math.abs(1300d - max0) / 1300d * 100;
        double meanResult0 = Math.abs(1283.1634d - mean0) / 1283.1634d * 100;
        double dev_stdResult0 = Math.abs(11.7972d - sdev0) / 11.7972d * 100;

        assertTrue(minResult0 < 10);
        assertTrue(varResult0 < 10);
        assertTrue(maxResult0 < 10);
        assertTrue(meanResult0 < 10);
        assertTrue(dev_stdResult0 < 10);

        // Second Range

        Map<StatsType, Object> statistics1 = statisticsTool.getStatistics(id).get(1);
        LOGGER.info(id + statistics1.toString());
        double sdev1 = (Double) statistics1.get(StatsType.DEV_STD);
        double min1 = (Double) statistics1.get(StatsType.MIN);
        double mean1 = (Double) statistics1.get(StatsType.MEAN);
        double var1 = (Double) statistics1.get(StatsType.VARIANCE);
        double max1 = (Double) statistics1.get(StatsType.MAX);

        // Percentual variation between the correct statistics and the calculated
        double minResult1 = Math.abs(1376d - min1) / 1376d * 100;
        double varResult1 = Math.abs(4061.9665d - var1) / 4061.9665d * 100;
        double maxResult1 = Math.abs(1598d - max1) / 1598d * 100;
        double meanResult1 = Math.abs(1433.8979d - mean1) / 1433.8979d * 100;
        double dev_stdResult1 = Math.abs(63.7335d - sdev1) / 63.7335d * 100;

        assertTrue(minResult1 < 10);
        assertTrue(varResult1 < 10);
        assertTrue(maxResult1 < 10);
        assertTrue(meanResult1 < 10);
        assertTrue(dev_stdResult1 < 10);

        reader.dispose();
        coverage2D.dispose(true);
        image.flush();
    }

    @Test
    public void testPolygonGlobalStats() throws Exception {
        // Input data
        final File tiff = TestData.file(this, "test.tif");
        final File tfw = TestData.file(this, "test.tfw");

        // Reading of the input image
        final TIFFImageReader reader =
                (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader)
                        new TIFFImageReaderSpi().createReaderInstance();
        reader.setInput(ImageIO.createImageInputStream(tiff));
        final BufferedImage image = reader.read(0);
        reader.dispose();

        // Transformation from the Raster space to the Model space
        final MathTransform transform = new WorldFileReader(tfw).getTransform();
        // Creation of the input coverage
        final GridCoverage2D coverage2D =
                CoverageFactoryFinder.getGridCoverageFactory(null)
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

        // Selection of the input geometries and creation of the related list.
        List<SimpleFeature> polygonList = new ArrayList<SimpleFeature>();
        FeatureIterator<SimpleFeature> featureIterator = ZonalStasTest.testPolygons.features();
        while (featureIterator.hasNext()) {
            SimpleFeature feature = featureIterator.next();
            polygonList.add(feature);
        }
        featureIterator.close();

        // choose the stats
        Set<StatsType> statsSet = new LinkedHashSet<StatsType>();
        statsSet.add(StatsType.MIN);
        statsSet.add(StatsType.MAX);
        statsSet.add(StatsType.MEAN);
        statsSet.add(StatsType.VARIANCE);
        statsSet.add(StatsType.DEV_STD);

        // Selection of the range where the calculations are performed
        List<Range> includedRanges = new ArrayList<Range>();
        includedRanges.add(RangeFactory.create(0f, false, 1300f, true, false));
        includedRanges.add(RangeFactory.create(1370f, true, 1600f, true, false));

        // select the bands to work on
        int[] bands = new int[] {0};

        // create the proper instance
        StatisticsTool statisticsTool =
                new StatisticsTool(statsSet, coverage2D, bands, polygonList, includedRanges, false);

        // do analysis
        statisticsTool.run();

        // get the results for the first polygon
        String id = "testpolygon.1";
        Map<StatsType, Object> statistics = statisticsTool.getStatistics(id).get(0);
        LOGGER.info(id + statistics.toString());
        double sdev1 = (Double) statistics.get(StatsType.DEV_STD);
        double min1 = (Double) statistics.get(StatsType.MIN);
        double mean1 = (Double) statistics.get(StatsType.MEAN);
        double var1 = (Double) statistics.get(StatsType.VARIANCE);
        double max1 = (Double) statistics.get(StatsType.MAX);

        // Percentual variation between the correct statistics and the calculated
        double minResult1 = Math.abs(1255d - min1) / 1255d * 100;
        double varResult1 = Math.abs(7874.6598d - var1) / 7874.6598d * 100;
        double maxResult1 = Math.abs(1598d - max1) / 1598d * 100;
        double meanResult1 = Math.abs(1380.5423d - mean1) / 1380.5423d * 100;
        double dev_stdResult1 = Math.abs(88.7357d - sdev1) / 88.7357d * 100;

        assertTrue(minResult1 < 10);
        assertTrue(varResult1 < 10);
        assertTrue(maxResult1 < 10);
        assertTrue(meanResult1 < 10);
        assertTrue(dev_stdResult1 < 10);

        // get the results for the second polygon
        id = "testpolygon.2";
        statistics = statisticsTool.getStatistics(id).get(0);
        LOGGER.info(id + statistics.toString());
        double sdev2 = (Double) statistics.get(StatsType.DEV_STD);
        double min2 = (Double) statistics.get(StatsType.MIN);
        double mean2 = (Double) statistics.get(StatsType.MEAN);
        double var2 = (Double) statistics.get(StatsType.VARIANCE);
        double max2 = (Double) statistics.get(StatsType.MAX);

        // Percentual variation between the correct statistics and the calculated
        double minResult2 = Math.abs(1192d - min2) / 1192d * 100;
        double varResult2 = Math.abs(1354.21d - var2) / 1354.21d * 100;
        double maxResult2 = Math.abs(1408d - max2) / 1408d * 100;
        double meanResult2 = Math.abs(1248.38d - mean2) / 1248.38d * 100;
        double dev_stdResult2 = Math.abs(36.7996d - sdev2) / 36.7996d * 100;

        assertTrue(minResult2 < 10);
        assertTrue(varResult2 < 10);
        assertTrue(maxResult2 < 10);
        assertTrue(meanResult2 < 10);
        assertTrue(dev_stdResult2 < 10);

        // get the results for the third polygon
        id = "testpolygon.3";
        statistics = statisticsTool.getStatistics(id).get(0);
        LOGGER.info(id + statistics.toString());
        double sdev3 = (Double) statistics.get(StatsType.DEV_STD);
        double min3 = (Double) statistics.get(StatsType.MIN);
        double mean3 = (Double) statistics.get(StatsType.MEAN);
        double var3 = (Double) statistics.get(StatsType.VARIANCE);
        double max3 = (Double) statistics.get(StatsType.MAX);

        // Percentual variation between the correct statistics and the calculated
        double minResult3 = Math.abs(1173d - min3) / 1173d * 100;
        double varResult3 = Math.abs(957.3594d - var3) / 957.3594d * 100;
        double maxResult3 = Math.abs(1300d - max3) / 1300d * 100;
        double meanResult3 = Math.abs(1266.3876d - mean3) / 1266.3876d * 100;
        double dev_stdResult3 = Math.abs(30.9411d - sdev3) / 30.9411d * 100;

        assertTrue(minResult3 < 10);
        assertTrue(varResult3 < 10);
        assertTrue(maxResult3 < 10);
        assertTrue(meanResult3 < 10);
        assertTrue(dev_stdResult3 < 10);

        reader.dispose();
        coverage2D.dispose(true);
        image.flush();
    }
}
