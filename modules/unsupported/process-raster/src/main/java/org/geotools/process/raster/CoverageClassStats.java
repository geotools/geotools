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
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.operator.BandSelectDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.process.ProcessException;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.classify.ClassificationStats;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.renderer.i18n.Errors;
import org.jaitools.media.jai.zonalstats.Result;
import org.jaitools.media.jai.zonalstats.ZonalStats;
import org.jaitools.media.jai.zonalstats.ZonalStatsDescriptor;
import org.jaitools.numeric.Range;
import org.jaitools.numeric.Statistic;
import org.opengis.util.ProgressListener;

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
        description =
                "Calculates statistics from coverage" + " values classified into bins/classes.")
public class CoverageClassStats implements RasterProcess {

    @DescribeResult(name = "results", description = "The classified results")
    public Results execute(
            @DescribeParameter(name = "coverage", description = "The coverage to analyze")
                    GridCoverage2D coverage,
            @DescribeParameter(
                            name = "stats",
                            description = "The statistics to calculate for each class",
                            collectionType = Statistic.class,
                            min = 0)
                    Set<Statistic> stats,
            @DescribeParameter(
                            name = "band",
                            description = "The band to calculate breaks/statistics for",
                            min = 0)
                    Integer band,
            @DescribeParameter(
                            name = "classes",
                            description = "The number of breaks/classes",
                            min = 0)
                    Integer classes,
            @DescribeParameter(name = "method", description = "The classification method", min = 0)
                    ClassificationMethod method,
            @DescribeParameter(
                            name = "noData",
                            description = "The pixel value to be ommitted from any calculation",
                            min = 0)
                    Double noData,
            ProgressListener progressListener)
            throws ProcessException, IOException {

        //
        // initial checks/defaults
        //
        if (coverage == null) {
            throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "coverage"));
        }

        if (classes == null) {
            classes = 10;
        }

        if (classes < 1) {
            throw new ProcessException(
                    Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "classes", classes));
        }

        RenderedImage sourceImage = coverage.getRenderedImage();

        // parse the band
        if (band == null) {
            band = 0;
        }

        final int numBands = sourceImage.getSampleModel().getNumBands();
        if (band < 0 || band >= numBands) {
            throw new ProcessException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "band", band));
        }

        if (numBands > 1) {
            sourceImage = BandSelectDescriptor.create(sourceImage, new int[] {band}, null);
        }

        // other defaults
        if (method == null) {
            method = ClassificationMethod.EQUAL_INTERVAL;
        }
        if (stats == null || stats.isEmpty()) {
            stats = Collections.singleton(Statistic.MEAN);
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

        /*ParameterBlockJAI pb = new ParameterBlockJAI(ClassBreaksDescriptor.NAME);
        pb.setParameter("numClasses", classes);
        pb.setParameter("method", method);
        if (noData != null) {
            pb.setParameter("noData", noData);
        }

        RenderedOp op = JAI.create(ClassBreaksDescriptor.NAME, pb);*/
        Classification c =
                (Classification) op.getProperty(ClassBreaksDescriptor.CLASSIFICATION_PROPERTY);

        Double[] breaks = (Double[]) c.getBreaks()[0];

        // build up the classes/ranges
        List<Range<Double>> ranges = new ArrayList<>();
        for (int i = 0; i < breaks.length - 1; i++) {
            ranges.add(Range.create(breaks[i], true, breaks[i + 1], i == breaks.length - 2));
        }

        // calculate stats for each class
        ParameterBlockJAI pbj = new ParameterBlockJAI("ZonalStats");
        pbj.addSource(sourceImage);
        pbj.setParameter("stats", stats.toArray(new Statistic[stats.size()]));
        pbj.setParameter("bands", new Integer[] {band});
        pbj.setParameter("ranges", ranges);
        pbj.setParameter("rangesType", Range.Type.INCLUDE);
        pbj.setParameter("rangeLocalStats", true);
        // "bands",
        // "roi",
        // "zoneTransform",
        // "ranges",
        // "rangesType",
        // "rangeLocalStats",
        // "noDataRanges"
        op = JAI.create("ZonalStats", pbj);

        ZonalStats zonalStats =
                (ZonalStats) op.getProperty(ZonalStatsDescriptor.ZONAL_STATS_PROPERTY);
        return new Results(stats, zonalStats);
    }

    private it.geosolutions.jaiext.classbreaks.ClassificationMethod toJAIExtMethod(
            ClassificationMethod method) {
        if (method == null) {
            return null;
        }
        return it.geosolutions.jaiext.classbreaks.ClassificationMethod.valueOf(method.name());
    }

    public static class Results implements ClassificationStats {

        Statistic firstStat;
        Set<Statistic> stats;
        ZonalStats zonalStats;
        List<Result> ranges;

        public Results(Set<Statistic> stats, ZonalStats zonalStats) {
            this.stats = stats;
            this.zonalStats = zonalStats;
            this.firstStat = stats.iterator().next();
            ranges = zonalStats.statistic(firstStat).results();
        }

        @Override
        public int size() {
            return ranges.size();
        }

        @Override
        public Set<Statistic> getStats() {
            return stats;
        }

        @Override
        public Range range(int i) {
            return ranges.get(i).getRanges().iterator().next();
        }

        @Override
        public Double value(int i, Statistic stat) {
            return zonalStats.statistic(stat).results().get(i).getValue();
        }

        @Override
        public Long count(int i) {
            return zonalStats.statistic(firstStat).results().get(i).getNumAccepted();
        }

        ZonalStats getZonalStats() {
            return zonalStats;
        }
    }
}
