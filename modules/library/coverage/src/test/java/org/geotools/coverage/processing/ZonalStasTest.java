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
package org.geotools.coverage.processing;

import static java.lang.Math.round;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
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
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import junit.framework.TestCase;
import org.geotools.TestData;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.logging.Logging;
import org.jaitools.CollectionFactory;
import org.jaitools.media.jai.zonalstats.Result;
import org.jaitools.media.jai.zonalstats.ZonalStats;
import org.jaitools.media.jai.zonalstats.ZonalStatsDescriptor;
import org.jaitools.numeric.Range;
import org.jaitools.numeric.Range.Type;
import org.jaitools.numeric.Statistic;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * @author Simone Giannecchini, GeoSolutions
 * @author Andrea Antonello, Hydrologis
 * @author Daniele Romagnoli, GeoSolutions
 */
public class ZonalStasTest extends TestCase {

    static final double DELTA = 10E-4;

    private static final Logger LOGGER = Logging.getLogger(ZonalStasTest.class);

    static ListFeatureCollection testPolygons;

    static {
        try {
            WKTReader reader = new WKTReader();
            Geometry g1 =
                    reader.read(
                            "POLYGON ((11.895130178020111 46.207934814601089,11.895809089115916 "
                                    + "46.207927430715209,11.895112009452415 46.207050983935503,11.895130178020111 46.207934814601089))");
            Geometry g2 =
                    reader.read(
                            "POLYGON ((11.896630238926786 46.207395686643665,11.897000925691524 "
                                    + "46.207985638846736,11.897753464657697 46.20749492858355,11.896630238926786 46.207395686643665))");
            Geometry g3 =
                    reader.read(
                            "POLYGON ((11.897871315022112 46.20812076814061,11.898581076580497 "
                                    + "46.208161712380381,11.898470449632523 46.207673246668122,11.897871315022112 46.20812076814061))");

            SimpleFeatureType schema = DataUtilities.createType("testPolygons", "the_geom:Polygon");
            testPolygons = new ListFeatureCollection(schema);
            testPolygons.add(
                    SimpleFeatureBuilder.build(schema, new Object[] {g1}, "testpolygon.1"));
            testPolygons.add(
                    SimpleFeatureBuilder.build(schema, new Object[] {g2}, "testpolygon.2"));
            testPolygons.add(
                    SimpleFeatureBuilder.build(schema, new Object[] {g3}, "testpolygon.3"));
        } catch (SchemaException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private class StatisticsTool {

        /*
         * external user params
         */
        private Set<Statistic> statisticsSet;

        private Integer[] bands;

        private GridCoverage2D gridCoverage2D;

        private List<SimpleFeature> featureList;

        private List<Range<Double>> inclusionRanges;

        private Range.Type rangesType = Type.EXCLUDE;

        private boolean isLocal = false;

        /*
         * results
         */
        private Map<String, Map<Statistic, List<Result>>> feature2StatisticsMap =
                new HashMap<String, Map<Statistic, List<Result>>>();

        private StatisticsTool(
                Set<Statistic> statisticsSet,
                GridCoverage2D gridCoverage2D,
                Integer[] bands,
                List<SimpleFeature> polygonsList,
                List<Range<Double>> inclusionRanges) {
            this(
                    statisticsSet,
                    gridCoverage2D,
                    bands,
                    polygonsList,
                    inclusionRanges,
                    Range.Type.EXCLUDE,
                    false);
        }

        private StatisticsTool(
                Set<Statistic> statisticsSet,
                GridCoverage2D gridCoverage2D,
                Integer[] bands,
                List<SimpleFeature> polygonsList,
                List<Range<Double>> ranges,
                final Range.Type rangesType,
                final boolean isLocal) {
            this.statisticsSet = statisticsSet;
            this.gridCoverage2D = gridCoverage2D;
            this.bands = bands;
            this.featureList = polygonsList;
            this.inclusionRanges = ranges;
            this.isLocal = isLocal;
            this.rangesType = rangesType;
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
            final AffineTransform gridToWorldTransformCorrected =
                    new AffineTransform(
                            (AffineTransform)
                                    ((GridGeometry2D) gridCoverage2D.getGridGeometry())
                                            .getGridToCRS2D(PixelOrientation.UPPER_LEFT));
            final MathTransform worldToGridTransform;
            try {
                worldToGridTransform =
                        ProjectiveTransform.create(gridToWorldTransformCorrected.createInverse());
            } catch (NoninvertibleTransformException e) {
                throw new IllegalArgumentException(e.getLocalizedMessage());
            }

            for (SimpleFeature feature : featureList) {
                final String fid = feature.getID();
                final Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (geometry instanceof Polygon || geometry instanceof MultiPolygon) {
                    final BoundingBox bbox = feature.getBounds();

                    /*
                     * crop on region of interest
                     */
                    final CoverageProcessor processor = CoverageProcessor.getInstance();
                    final ParameterValueGroup param =
                            processor.getOperation("CoverageCrop").getParameters();
                    param.parameter("Source").setValue(gridCoverage2D);
                    param.parameter("Envelope").setValue(new GeneralEnvelope(bbox));
                    final GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);

                    ROI roi = null;
                    final int numGeometries = geometry.getNumGeometries();
                    for (int i = 0; i < numGeometries; i++) {
                        Geometry geometryN = geometry.getGeometryN(i);
                        java.awt.Polygon awtPolygon =
                                toAWTPolygon((Polygon) geometryN, worldToGridTransform);
                        if (roi == null) {
                            roi = new ROIShape(awtPolygon);
                        } else {
                            ROI newRoi = new ROIShape(awtPolygon);
                            roi.add(newRoi);
                        }
                    }

                    final Statistic[] statistis =
                            statisticsSet.toArray(new Statistic[statisticsSet.size()]);

                    final OperationJAI op = new OperationJAI("ZonalStats");
                    ParameterValueGroup params = op.getParameters();
                    params.parameter("dataImage").setValue(cropped);
                    params.parameter("stats").setValue(statistis);
                    params.parameter("bands").setValue(bands);
                    params.parameter("roi").setValue(roi);
                    params.parameter("ranges").setValue(inclusionRanges);
                    params.parameter("rangesType").setValue(rangesType);
                    params.parameter("rangeLocalStats").setValue(isLocal);

                    final GridCoverage2D coverage = (GridCoverage2D) op.doOperation(params, null);
                    final ZonalStats stats =
                            (ZonalStats)
                                    coverage.getProperty(ZonalStatsDescriptor.ZONAL_STATS_PROPERTY);
                    final Map<Statistic, List<Result>> statsMap =
                            new HashMap<Statistic, List<Result>>();
                    for (Statistic statistic : statistis) {
                        final List<Range> inclRanges = CollectionFactory.list();
                        inclRanges.addAll(inclusionRanges);
                        List<Result> statsResult =
                                stats.ranges(inclRanges).statistic(statistic).results();
                        statsMap.put(statistic, statsResult);
                    }
                    feature2StatisticsMap.put(fid, statsMap);
                }
            }
        }

        private java.awt.Polygon toAWTPolygon(
                final Polygon roiInput, MathTransform worldToGridTransform)
                throws TransformException {
            final boolean isIdentity = worldToGridTransform.isIdentity();
            final java.awt.Polygon retValue = new java.awt.Polygon();
            final double coords[] = new double[2];
            final LineString exteriorRing = roiInput.getExteriorRing();
            final CoordinateSequence exteriorRingCS = exteriorRing.getCoordinateSequence();
            final int numCoords = exteriorRingCS.size();
            for (int i = 0; i < numCoords; i++) {
                coords[0] = exteriorRingCS.getX(i);
                coords[1] = exteriorRingCS.getY(i);
                if (!isIdentity) worldToGridTransform.transform(coords, 0, coords, 0, 1);
                retValue.addPoint((int) round(coords[0] + 0.5d), (int) round(coords[1] + 0.5d));
            }
            return retValue;
        }

        /**
         * Gets the performed statistics.
         *
         * @param fId the id of the feature used as region for the analysis.
         * @return the {@link List} of results of the analysis for all the requested {@link
         *     Statistic} for the requested bands. Note that the result contains for every {@link
         *     Statistic} a result value for every band.
         */
        public Map<Statistic, List<Result>> getStatistics(String fId) {
            return feature2StatisticsMap.get(fId);
        }
    }

    @Before
    public void setUp() throws Exception {
        TestData.unzipFile(this, "test.zip");
    }

    @Test
    public void testPolygonZoneGlobalStats() throws Exception {
        final File tiff = TestData.file(this, "test.tif");
        final File tfw = TestData.file(this, "test.tfw");

        final TIFFImageReader reader =
                (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader)
                        new TIFFImageReaderSpi().createReaderInstance();
        reader.setInput(ImageIO.createImageInputStream(tiff));
        final BufferedImage image = reader.read(0);
        reader.dispose();

        final MathTransform transform = new WorldFileReader(tfw).getTransform();
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

        List<SimpleFeature> polygonList = new ArrayList<SimpleFeature>();
        FeatureIterator<SimpleFeature> featureIterator = testPolygons.features();
        while (featureIterator.hasNext()) {
            SimpleFeature feature = featureIterator.next();
            polygonList.add(feature);
        }
        featureIterator.close();

        // choose the stats
        Set<Statistic> statsSet = new LinkedHashSet<Statistic>();
        statsSet.add(Statistic.MIN);
        statsSet.add(Statistic.MAX);
        statsSet.add(Statistic.MEAN);
        statsSet.add(Statistic.VARIANCE);
        statsSet.add(Statistic.SDEV);
        statsSet.add(Statistic.RANGE);

        // select the bands to work on
        Integer[] bands = new Integer[] {0};
        List<Range<Double>> inclusionRanges = new ArrayList<Range<Double>>();
        inclusionRanges.add(
                new Range<Double>(Double.valueOf(0), false, Double.valueOf(1300), true));
        inclusionRanges.add(
                new Range<Double>(Double.valueOf(1370), true, Double.valueOf(1600), true));

        // create the proper instance
        StatisticsTool statisticsTool =
                new StatisticsTool(
                        statsSet,
                        coverage2D,
                        bands,
                        polygonList,
                        inclusionRanges,
                        Type.INCLUDE,
                        false);

        // do analysis
        statisticsTool.run();

        // get the results
        String id = "testpolygon.1";
        Map<Statistic, List<Result>> statistics = statisticsTool.getStatistics(id);
        LOGGER.info(id + statistics.toString());
        assertEquals(statistics.get(Statistic.RANGE).get(0).getValue().doubleValue(), 343.0, DELTA);
        assertEquals(
                statistics.get(Statistic.SDEV).get(0).getValue().doubleValue(), 88.7358, DELTA);
        assertEquals(statistics.get(Statistic.MIN).get(0).getValue().doubleValue(), 1255.0, DELTA);
        assertEquals(
                statistics.get(Statistic.MEAN).get(0).getValue().doubleValue(), 1380.5423, DELTA);
        assertEquals(
                statistics.get(Statistic.VARIANCE).get(0).getValue().doubleValue(),
                7874.0598,
                DELTA);
        assertEquals(statistics.get(Statistic.MAX).get(0).getValue().doubleValue(), 1598.0, DELTA);

        id = "testpolygon.2";
        statistics = statisticsTool.getStatistics(id);
        LOGGER.info(id + statistics.toString());
        assertEquals(statistics.get(Statistic.RANGE).get(0).getValue().doubleValue(), 216.0, DELTA);
        assertEquals(
                statistics.get(Statistic.SDEV).get(0).getValue().doubleValue(), 36.7996, DELTA);
        assertEquals(statistics.get(Statistic.MIN).get(0).getValue().doubleValue(), 1192.0, DELTA);
        assertEquals(
                statistics.get(Statistic.MEAN).get(0).getValue().doubleValue(), 1248.3870, DELTA);
        assertEquals(
                statistics.get(Statistic.VARIANCE).get(0).getValue().doubleValue(),
                1354.2150,
                DELTA);
        assertEquals(statistics.get(Statistic.MAX).get(0).getValue().doubleValue(), 1408.0, DELTA);

        id = "testpolygon.3";
        statistics = statisticsTool.getStatistics(id);
        LOGGER.info(id + statistics.toString());
        assertEquals(
                statistics.get(Statistic.RANGE).get(0).getValue().doubleValue(), 127.0000, DELTA);
        assertEquals(
                statistics.get(Statistic.SDEV).get(0).getValue().doubleValue(), 30.9412, DELTA);
        assertEquals(statistics.get(Statistic.MIN).get(0).getValue().doubleValue(), 1173.0, DELTA);
        assertEquals(
                statistics.get(Statistic.MEAN).get(0).getValue().doubleValue(), 1266.3876, DELTA);
        assertEquals(
                statistics.get(Statistic.VARIANCE).get(0).getValue().doubleValue(),
                957.3594,
                DELTA);
        assertEquals(statistics.get(Statistic.MAX).get(0).getValue().doubleValue(), 1300.0, DELTA);

        reader.dispose();
        coverage2D.dispose(true);
        image.flush();
    }

    @Test
    public void testPolygonZoneLocalStats() throws Exception {
        final File tiff = TestData.file(this, "test.tif");
        final File tfw = TestData.file(this, "test.tfw");

        final TIFFImageReader reader =
                (it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader)
                        new TIFFImageReaderSpi().createReaderInstance();
        reader.setInput(ImageIO.createImageInputStream(tiff));
        final BufferedImage image = reader.read(0);
        reader.dispose();

        final MathTransform transform = new WorldFileReader(tfw).getTransform();
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

        List<SimpleFeature> polygonList = new ArrayList<SimpleFeature>();
        FeatureIterator<SimpleFeature> featureIterator = testPolygons.features();
        while (featureIterator.hasNext()) {
            SimpleFeature feature = featureIterator.next();
            polygonList.add(feature);
        }
        featureIterator.close();

        // choose the stats
        Set<Statistic> statsSet = new LinkedHashSet<Statistic>();
        statsSet.add(Statistic.MIN);
        statsSet.add(Statistic.MAX);
        statsSet.add(Statistic.MEAN);
        statsSet.add(Statistic.VARIANCE);
        statsSet.add(Statistic.SDEV);
        statsSet.add(Statistic.RANGE);

        // select the bands to work on
        Integer[] bands = new Integer[] {0};
        List<Range<Double>> inclusionRanges = new ArrayList<Range<Double>>();
        inclusionRanges.add(
                new Range<Double>(Double.valueOf(0), false, Double.valueOf(1300), true));
        inclusionRanges.add(
                new Range<Double>(Double.valueOf(1370), true, Double.valueOf(1600), true));

        // create the proper instance
        StatisticsTool statisticsTool =
                new StatisticsTool(
                        statsSet,
                        coverage2D,
                        bands,
                        polygonList,
                        inclusionRanges,
                        Range.Type.INCLUDE,
                        true);

        // do analysis
        statisticsTool.run();

        // get the results
        String id = "testpolygon.1";
        Map<Statistic, List<Result>> statistics = statisticsTool.getStatistics(id);
        LOGGER.info(id + statistics.toString());
        assertEquals(statistics.get(Statistic.RANGE).get(0).getValue().doubleValue(), 45.0, DELTA);
        assertEquals(statistics.get(Statistic.RANGE).get(1).getValue().doubleValue(), 228.0, DELTA);
        assertEquals(
                statistics.get(Statistic.SDEV).get(0).getValue().doubleValue(), 11.7972, DELTA);
        assertEquals(
                statistics.get(Statistic.SDEV).get(1).getValue().doubleValue(), 63.7335, DELTA);
        assertEquals(statistics.get(Statistic.MIN).get(0).getValue().doubleValue(), 1255.0, DELTA);
        assertEquals(statistics.get(Statistic.MIN).get(1).getValue().doubleValue(), 1370.0, DELTA);
        assertEquals(
                statistics.get(Statistic.MEAN).get(0).getValue().doubleValue(), 1283.1634, DELTA);
        assertEquals(
                statistics.get(Statistic.MEAN).get(1).getValue().doubleValue(), 1433.8979, DELTA);
        assertEquals(
                statistics.get(Statistic.VARIANCE).get(0).getValue().doubleValue(),
                139.1754,
                DELTA);
        assertEquals(
                statistics.get(Statistic.VARIANCE).get(1).getValue().doubleValue(),
                4061.9665,
                DELTA);
        assertEquals(statistics.get(Statistic.MAX).get(0).getValue().doubleValue(), 1300.0, DELTA);
        assertEquals(statistics.get(Statistic.MAX).get(1).getValue().doubleValue(), 1598.0, DELTA);

        reader.dispose();
        coverage2D.dispose(true);
        image.flush();
    }
}
