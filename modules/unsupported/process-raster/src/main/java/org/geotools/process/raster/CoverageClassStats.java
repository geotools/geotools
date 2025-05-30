/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.jaiext.classbreaks.ClassBreaksDescriptor;
import it.geosolutions.jaiext.classbreaks.ClassBreaksRIF;
import it.geosolutions.jaiext.classbreaks.Classification;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.stats.Statistics;
import it.geosolutions.jaiext.stats.Statistics.StatsType;
import it.geosolutions.jaiext.zonal.ZonalStatsDescriptor;
import it.geosolutions.jaiext.zonal.ZoneGeometry;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.media.jai.operator.BandSelectDescriptor;
import org.geotools.api.util.ProgressListener;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.process.ProcessException;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.classify.ClassificationStats;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;

/**
 * Process that classifies vector data into "classes" using one of the following methods:
 *
 * <ul>
 *   <li>Equal Interval ({@link ClassificationMethod#EQUAL_INTERVAL})
 *   <li>Quantile ({@link ClassificationMethod#QUANTILE})
 *   <li>Natural Breaks ({@link ClassificationMethod#NATURAL_BREAKS})
 * </ul>
 */
@DescribeProcess(
        title = "coverageClassStats",
        description = "Calculates statistics from coverage" + " values classified into bins/classes.")
public class CoverageClassStats implements RasterProcess {

    private GridCoverage2D coverage;
    private List<StatsType> stats;
    private Integer band;
    private Integer classes;
    private ClassificationMethod method;
    private Double noData;
    private ProgressListener progressListener;

    @DescribeResult(name = "results", description = "The classified results")
    public Results execute(
            @DescribeParameter(name = "coverage", description = "The coverage to analyze") GridCoverage2D coverage,
            @DescribeParameter(
                            name = "stats",
                            description = "The statistics to calculate for each class",
                            collectionType = StatsType.class,
                            min = 0)
                    List<StatsType> stats,
            @DescribeParameter(name = "band", description = "The band to calculate breaks/statistics for", min = 0)
                    Integer band,
            @DescribeParameter(name = "classes", description = "The number of breaks/classes", min = 0) Integer classes,
            @DescribeParameter(name = "method", description = "The classification method", min = 0)
                    ClassificationMethod method,
            @DescribeParameter(
                            name = "noData",
                            description = "The pixel value to be ommitted from any calculation",
                            min = 0)
                    Double noData,
            ProgressListener progressListener)
            throws ProcessException, IOException {
        this.coverage = coverage;
        this.stats = stats;
        this.band = band;
        this.classes = classes;
        this.method = method;
        this.noData = noData;
        this.progressListener = progressListener;

        //
        // initial checks/defaults
        //
        if (coverage == null) {
            throw new ProcessException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "coverage"));
        }

        if (classes == null) {
            classes = 10;
        }

        if (classes < 1) {
            throw new ProcessException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "classes", classes));
        }

        RenderedImage sourceImage = coverage.getRenderedImage();

        // parse the band
        if (band == null) {
            band = 0;
        }

        final int numBands = sourceImage.getSampleModel().getNumBands();
        if (band < 0 || band >= numBands) {
            throw new ProcessException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "band", band));
        }

        if (numBands > 1) {
            sourceImage = BandSelectDescriptor.create(sourceImage, new int[] {band}, null);
        }

        // other defaults
        if (method == null) {
            method = ClassificationMethod.EQUAL_INTERVAL;
        }
        if (stats == null || stats.isEmpty()) {
            stats = Collections.singletonList(StatsType.MEAN);
        }

        // compute the class breaks
        // JD: for some reason running this in tomcat via JAI.create does not work, the operation
        // descriptor is never registered and it fails, so for now we just invoke the op directly
        // TODO: look into this more
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(sourceImage);
        pb.set(classes, 0);
        pb.set(toJAIExtMethod(method), 1);
        pb.set(null, 2);
        pb.set(null, 3);
        pb.set(new Integer[] {0}, 4);
        pb.set(1, 5);
        pb.set(1, 6);
        pb.set(noData, 7);

        RenderedImage op = new ClassBreaksRIF().create(pb, null);
        Classification c = (Classification) op.getProperty(ClassBreaksDescriptor.CLASSIFICATION_PROPERTY);

        Double[] breaks = (Double[]) c.getBreaks()[0];

        // build up the classes/ranges
        List<Range> ranges = new ArrayList<>();
        for (int i = 0; i < breaks.length - 1; i++) {
            ranges.add(createRange(breaks, i, sourceImage.getSampleModel().getDataType()));
        }

        // calculate stats for each class
        op = ZonalStatsDescriptor.create(
                sourceImage,
                null,
                null,
                null,
                null,
                null,
                false,
                new int[] {band},
                stats.toArray(StatsType[]::new),
                null,
                null,
                null,
                ranges,
                true,
                null);

        @SuppressWarnings("unchecked")
        List<ZoneGeometry> zonalStats = (List<ZoneGeometry>) op.getProperty(ZonalStatsDescriptor.ZS_PROPERTY);
        if (zonalStats == null || zonalStats.isEmpty()) {
            throw new ProcessException("No zonal statistics were calculated, check the input coverage and ranges.");
        }
        if (zonalStats.size() != 1) {
            throw new ProcessException("Multiple zonal statistics were calculated, expected only one zone.");
        }
        return new Results(stats, zonalStats.get(0), ranges);
    }

    private static Range createRange(Double[] breaks, int i, int dataType) {
        switch (dataType) {
            case java.awt.image.DataBuffer.TYPE_BYTE:
                return RangeFactory.create(
                        breaks[i].byteValue(), true, breaks[i + 1].byteValue(), i == breaks.length - 2);
            case java.awt.image.DataBuffer.TYPE_SHORT:
                return RangeFactory.create(
                        breaks[i].shortValue(), true, breaks[i + 1].shortValue(), i == breaks.length - 2);
            case java.awt.image.DataBuffer.TYPE_INT:
                return RangeFactory.create(
                        breaks[i].intValue(), true, breaks[i + 1].intValue(), i == breaks.length - 2);
            case java.awt.image.DataBuffer.TYPE_FLOAT:
                return RangeFactory.create(
                        breaks[i].floatValue(), true, breaks[i + 1].floatValue(), i == breaks.length - 2);
            case java.awt.image.DataBuffer.TYPE_DOUBLE:
                return RangeFactory.create(breaks[i], true, breaks[i + 1], i == breaks.length - 2);
            default:
                throw new ProcessException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "dataType", dataType));
        }
    }

    private it.geosolutions.jaiext.classbreaks.ClassificationMethod toJAIExtMethod(ClassificationMethod method) {
        if (method == null) {
            return null;
        }
        return it.geosolutions.jaiext.classbreaks.ClassificationMethod.valueOf(method.name());
    }

    public static class Results implements ClassificationStats {

        List<StatsType> stats;
        ZoneGeometry zonalStats;
        List<Range> ranges;

        public Results(List<StatsType> stats, ZoneGeometry zonalStats, List<Range> ranges) {
            this.stats = stats;
            this.zonalStats = zonalStats;
            this.ranges = ranges;
        }

        @Override
        public int size() {
            return ranges.size();
        }

        @Override
        public Set<StatsType> getStats() {
            return new LinkedHashSet<>(stats);
        }

        @Override
        public Range range(int i) {
            return ranges.get(i);
        }

        @Override
        public Double value(int i, StatsType stat) {
            int statIdx = stats.indexOf(stat);
            if (statIdx == -1) {
                throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "stat", stat));
            }

            // there is no classification raster, so 0 is the index for the only "raster classification value"
            Map<Range, Statistics[]> rangeMap = zonalStats.getStatsPerBand(0).get(0);
            Statistics[] zonalStats = rangeMap.get(range(i));
            return ((Number) zonalStats[statIdx].getResult()).doubleValue();
        }

        @Override
        public Long count(int i) {
            // return zonalStats.statistic(firstStat).results().get(i).getNumAccepted();
            return null;
        }

        ZoneGeometry getZonalStats() {
            return zonalStats;
        }
    }
}
