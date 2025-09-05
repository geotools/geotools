/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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

import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.eclipse.imagen.ROI;
import org.eclipse.imagen.RenderedOp;
import org.eclipse.imagen.media.range.Range;
import org.eclipse.imagen.media.range.RangeDouble;
import org.eclipse.imagen.media.stats.Statistics;
import org.eclipse.imagen.media.stats.Statistics.StatsType;
import org.eclipse.imagen.media.zonal.ZonalStatsDescriptor;
import org.eclipse.imagen.media.zonal.ZoneGeometry;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.GridCoverage2DRIA;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * A process computing zonal statistics based on a raster data set and a set of polygonal zones of interest
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Emanuele Tajariol (GeoSolutions)
 * @author Andrea Aime - GeoSolutions
 */
@DescribeProcess(
        title = "Raster Zonal Statistics",
        description = "Computes statistics for the distribution of a certain quantity in a set of polygonal zones.")
public class RasterZonalStatistics implements RasterProcess {

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

    @DescribeResult(
            name = "statistics",
            description =
                    "A feature collection with the attributes of the zone layer (prefixed by 'z_') and the statistics fields count,min,max,sum,avg,stddev")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "data", description = "Input raster to compute statistics for")
                    GridCoverage2D coverage,
            @DescribeParameter(
                            name = "band",
                            description = "Source band used to compute statistics (default is 0)",
                            min = 0,
                            defaultValue = "0")
                    Integer band,
            @DescribeParameter(name = "zones", description = "Zone polygon features for which to compute statistics")
                    SimpleFeatureCollection zones,
            @DescribeParameter(
                            name = "classification",
                            description =
                                    "Raster whose values will be used as classes for the statistical analysis. Each zone reports statistics partitioned by classes according to the values of the raster. Must be a single band raster with integer values.",
                            min = 0)
                    GridCoverage2D classification) {
        int iband = 0;
        if (band != null) {
            iband = band;
        }

        return new RasterZonalStatisticsCollection(coverage, iband, zones, classification);
    }

    /**
     * A feature collection that computes zonal statitics in a streaming fashion
     *
     * @author Andrea Aime - OpenGeo
     */
    static class RasterZonalStatisticsCollection extends DecoratingSimpleFeatureCollection {
        GridCoverage2D coverage;

        SimpleFeatureType targetSchema;

        int band;

        GridCoverage2D classification;

        public RasterZonalStatisticsCollection(
                GridCoverage2D coverage, int band, SimpleFeatureCollection zones, GridCoverage2D classification) {
            super(zones);
            this.coverage = coverage;
            this.band = band;
            this.classification = classification;

            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            for (AttributeDescriptor att : zones.getSchema().getAttributeDescriptors()) {
                tb.minOccurs(att.getMinOccurs());
                tb.maxOccurs(att.getMaxOccurs());
                tb.restrictions(att.getType().getRestrictions());
                if (att instanceof GeometryDescriptor) {
                    GeometryDescriptor gatt = (GeometryDescriptor) att;
                    tb.crs(gatt.getCoordinateReferenceSystem());
                }
                tb.add("z_" + att.getLocalName(), att.getType().getBinding());
            }
            if (classification != null) {
                tb.add("classification", Integer.class);
            }
            tb.add("count", Long.class);
            tb.add("min", Double.class);
            tb.add("max", Double.class);
            tb.add("sum", Double.class);
            tb.add("avg", Double.class);
            tb.add("stddev", Double.class);
            tb.setName(zones.getSchema().getName());
            targetSchema = tb.buildFeatureType();
        }

        @Override
        public SimpleFeatureType getSchema() {
            return targetSchema;
        }

        @Override
        public SimpleFeatureIterator features() {
            return new RasterZonalStatisticsIterator(delegate.features(), coverage, band, targetSchema, classification);
        }
    }

    /** An iterator computing statistics as we go */
    static class RasterZonalStatisticsIterator implements SimpleFeatureIterator {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        SimpleFeatureIterator zones;

        SimpleFeatureBuilder builder;

        GridCoverage2D dataCoverage;

        int band;

        RenderedImage classificationRaster;

        List<SimpleFeature> features = new ArrayList<>();

        public RasterZonalStatisticsIterator(
                SimpleFeatureIterator zones,
                GridCoverage2D coverage,
                int band,
                SimpleFeatureType targetSchema,
                GridCoverage2D classification) {
            this.zones = zones;
            this.builder = new SimpleFeatureBuilder(targetSchema);
            this.dataCoverage = coverage;
            this.band = band;

            // prepare the classification image if necessary
            if (classification != null) {
                // find nodata values
                GridSampleDimension sampleDimension = classification.getSampleDimension(0);
                double[] nodataarr = sampleDimension.getNoDataValues();
                double[] nodata = nodataarr != null ? nodataarr : new double[] {Double.NaN};

                // this will adapt the classification image to the projection and image layout
                // of the data coverage
                classificationRaster = GridCoverage2DRIA.create(classification, dataCoverage, nodata);
            }
        }

        @Override
        public void close() {
            zones.close();
        }

        @Override
        public boolean hasNext() {
            return !features.isEmpty() || zones.hasNext();
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            // build the next set of features if necessary
            if (features.isEmpty()) {
                // grab the current zone
                SimpleFeature zone = zones.next();

                try {
                    // grab the geometry and eventually reproject it to the
                    Geometry zoneGeom = (Geometry) zone.getDefaultGeometry();
                    CoordinateReferenceSystem dataCrs = dataCoverage.getCoordinateReferenceSystem();
                    CoordinateReferenceSystem zonesCrs =
                            builder.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem();
                    if (!CRS.equalsIgnoreMetadata(zonesCrs, dataCrs)) {
                        zoneGeom = JTS.transform(zoneGeom, CRS.findMathTransform(zonesCrs, dataCrs, true));
                    }

                    // gather the statistics (should be only one, we invoked the process with a single zone)
                    List<ZoneGeometry> zoneGeometries = processStatistics(zoneGeom);

                    // build the resulting feature
                    if (zoneGeometries != null && !zoneGeometries.isEmpty()) {
                        ZoneGeometry zg = zoneGeometries.get(0);
                        if (classificationRaster != null) {
                            Map<Integer, Map<Range, Statistics[]>> statsPerBand = zg.getStatsPerBand(band);
                            statsPerBand.entrySet().forEach(entry -> {
                                Integer classValue = entry.getKey();
                                Map<Range, Statistics[]> statsMap = entry.getValue();
                                // we did not provide a set of classification ranges, so only one range is expected
                                Statistics[] stats =
                                        statsMap.values().iterator().next();
                                builder.addAll(zone.getAttributes());
                                builder.add(classValue);
                                if (stats != null) {
                                    addStatsToFeature(stats);
                                }
                                features.add(builder.buildFeature(zone.getID()));
                            });
                        } else {
                            // no classification raster, only one set of stats expected
                            Statistics[] stats = zg.getStatsPerBand(0)
                                    .values()
                                    .iterator()
                                    .next()
                                    .values()
                                    .iterator()
                                    .next();
                            builder.addAll(zone.getAttributes());
                            addStatsToFeature(stats);
                            features.add(builder.buildFeature(zone.getID()));
                        }
                    } else {
                        builder.addAll(zone.getAttributes());
                        features.add(builder.buildFeature(zone.getID()));
                    }
                } catch (Exception e) {
                    throw new ProcessException("Failed to compute statistics on feature " + zone, e);
                }
            }
            // return the first feature in the current buffer
            SimpleFeature f = features.remove(0);
            return f;
        }

        /** Add the statistics to the feature builder */
        void addStatsToFeature(Statistics[] stats) {
            // the statistics are returned in the order of the requested stats, for reference:
            // {StatsType.EXTREMA, StatsType.MEAN, StatsType.DEV_STD, StatsType.SUM};
            double[] minMax = (double[]) stats[0].getResult();
            double count = stats[0].getNumSamples().doubleValue();
            double avg = ((Number) stats[1].getResult()).doubleValue();
            double stdDev = ((Number) stats[2].getResult()).doubleValue();
            double sum = ((Number) stats[3].getResult()).doubleValue();
            builder.add(count); // count
            builder.add(minMax[0]);
            builder.add(minMax[1]);
            builder.add(sum);
            builder.add(avg);
            builder.add(stdDev);
        }

        @SuppressWarnings("unchecked")
        private List<ZoneGeometry> processStatistics(Geometry geometry) throws TransformException {
            GridCoverage2D cropped = null;
            try {
                // first off, cut the geometry around the coverage bounds if necessary
                ReferencedEnvelope coverageEnvelope = new ReferencedEnvelope(dataCoverage.getEnvelope2D());
                ReferencedEnvelope geometryEnvelope = new ReferencedEnvelope(
                        geometry.getEnvelopeInternal(), dataCoverage.getCoordinateReferenceSystem());
                if (!coverageEnvelope.intersects((Envelope) geometryEnvelope)) {
                    // no intersection, no stats
                    return null;
                } else if (!coverageEnvelope.contains((Envelope) geometryEnvelope)) {
                    // the geometry goes outside of the coverage envelope, that makes
                    // the stats fail for some reason
                    geometry = JTS.toGeometry((Envelope) coverageEnvelope).intersection(geometry);
                    geometryEnvelope = new ReferencedEnvelope(
                            geometry.getEnvelopeInternal(), dataCoverage.getCoordinateReferenceSystem());
                }

                // check if the novalue is != from NaN
                List<RangeDouble> noDataValueRangeList = CoverageUtilities.getNoDataAsList(dataCoverage);
                RangeDouble noData = noDataValueRangeList == null || noDataValueRangeList.isEmpty()
                        ? null
                        : noDataValueRangeList.get(0);

                /*
                 * crop on region of interest
                 */
                cropped = CoverageUtilities.crop(dataCoverage, geometryEnvelope);
                ROI roi = CoverageUtilities.getSimplifiedRoiGeometry(dataCoverage, geometry);
                StatsType[] reqStatsTypes = {StatsType.EXTREMA, StatsType.MEAN, StatsType.DEV_STD, StatsType.SUM};

                RenderedOp op = ZonalStatsDescriptor.create(
                        cropped.getRenderedImage(),
                        classificationRaster,
                        null,
                        Arrays.asList(roi),
                        noData,
                        null,
                        false,
                        new int[] {band},
                        reqStatsTypes,
                        null,
                        false,
                        null);
                return (List<ZoneGeometry>) op.getProperty(ZonalStatsDescriptor.ZS_PROPERTY);
            } finally {
                // dispose coverages
                if (cropped != null) {
                    cropped.dispose(true);
                }
            }
        }
    }
}
