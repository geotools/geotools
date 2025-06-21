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
package org.geotools.process.vector;

import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.stats.Statistics;
import it.geosolutions.jaiext.stats.Statistics.StatsType;
import it.geosolutions.jaiext.stats.StatsFactory;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.util.ProgressListener;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.function.ClassificationFunction;
import org.geotools.filter.function.EqualIntervalFunction;
import org.geotools.filter.function.JenksNaturalBreaksFunction;
import org.geotools.filter.function.QuantileFunction;
import org.geotools.filter.function.RangedClassifier;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.process.ProcessException;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.classify.ClassificationStats;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;

/**
 * Process that classifies vector data into "classes" using one of the following methods:
 *
 * <ul>
 *   <li>Equal Interval ({@link EqualIntervalFunction})
 *   <li>Quantile ({@link QuantileFunction})
 *   <li>Natural Breaks ({@link JenksNaturalBreaksFunction})
 * </ul>
 */
@DescribeProcess(
        title = "featureClassStats",
        description = "Calculates statistics from feature" + " values classified into bins/classes.")
public class FeatureClassStats implements VectorProcess {

    static Logger LOG = Logging.getLogger(FeatureClassStats.class);

    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    @DescribeResult(name = "results", description = "The classified results")
    public Results execute(
            @DescribeParameter(name = "features", description = "The feature collection to analyze")
                    FeatureCollection features,
            @DescribeParameter(name = "attribute", description = "The feature attribute to analyze") String attribute,
            @DescribeParameter(
                            name = "stats",
                            description = "The statistics to calculate for each class",
                            collectionType = StatsType.class)
                    List<StatsType> statTypes,
            @DescribeParameter(name = "classes", description = "The number of breaks/classes", min = 0) Integer classes,
            @DescribeParameter(name = "method", description = "The classification method", min = 0)
                    ClassificationMethod method,
            @DescribeParameter(
                            name = "noData",
                            description = "The attribute value to be omitted from any calculation",
                            min = 0)
                    Double noData,
            ProgressListener progressListener)
            throws ProcessException, IOException {

        //
        // initial checks/defaults
        //
        if (features == null) {
            throw new ProcessException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "features"));
        }
        if (attribute == null) {
            throw new ProcessException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "attribute"));
        }
        PropertyDescriptor property = features.getSchema().getDescriptor(attribute);
        if (property == null) {
            throw new ProcessException("No such feature attribute '" + attribute + "'");
        }
        if (!Number.class.isAssignableFrom(property.getType().getBinding())) {
            throw new ProcessException("Feature attribute '" + attribute + "' is not numeric");
        }

        if (classes == null) {
            classes = 10;
        }

        if (classes < 1) {
            throw new ProcessException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "classes", classes));
        }

        // other defaults
        if (method == null) {
            method = ClassificationMethod.EQUAL_INTERVAL;
        }
        if (statTypes == null || statTypes.isEmpty()) {
            statTypes = Collections.singletonList(StatsType.MEAN);
        }

        // choose the classification function
        ClassificationFunction cf = null;
        switch (method) {
            case EQUAL_INTERVAL:
                cf = new EqualIntervalFunction();
                break;
            case QUANTILE:
                cf = new QuantileFunction();
                break;
            case NATURAL_BREAKS:
                cf = new JenksNaturalBreaksFunction();
                break;
            default:
                throw new ProcessException("Unknown method: " + method);
        }
        cf.setParameters(Arrays.asList(filterFactory.property(attribute), filterFactory.literal(classes)));

        // compute the breaks
        RangedClassifier rc = (RangedClassifier) cf.evaluate(features);

        // build up the stats
        List<Range> ranges = new ArrayList<>();
        RangeStatistics[] sampleStats = new RangeStatistics[rc.getSize()];
        for (int i = 0; i < rc.getSize(); i++) {
            ranges.add(RangeFactory.create((Double) rc.getMin(i), true, (Double) rc.getMax(i), i == rc.getSize() - 1));

            RangeStatistics rangeStatistics = new RangeStatistics(statTypes);
            if (noData != null) {
                rangeStatistics.setNodata(noData);
            }
            sampleStats[i] = rangeStatistics;
        }

        // calculate all the stats
        try (FeatureIterator it = features.features()) {
            while (it.hasNext()) {
                Feature f = it.next();
                Object val = f.getProperty(attribute).getValue();
                if (val == null) {
                    continue;
                }

                // convert to double
                Double dubVal = Converters.convert(val, Double.class);
                if (dubVal == null) {
                    LOG.warning(String.format(
                            "Unable to convert value %s (attribute '%s') to Double, " + "skipping", val, attribute));
                    continue;
                }

                int slot = rc.classify(dubVal);
                sampleStats[slot].addSample(dubVal);
            }
        }

        return new Results(ranges, statTypes, sampleStats);
    }

    public static class Results implements ClassificationStats {

        private final List<StatsType> statsTypes;
        List<Range> ranges;
        RangeStatistics[] sampleStats;

        public Results(List<Range> ranges, List<StatsType> statsTypes, RangeStatistics[] sampleStats) {
            this.ranges = ranges;
            this.sampleStats = sampleStats;
            this.statsTypes = statsTypes;
        }

        @Override
        public int size() {
            return ranges.size();
        }

        @Override
        public Set<StatsType> getStats() {
            return new LinkedHashSet<>(statsTypes);
        }

        @Override
        public Range range(int i) {
            return ranges.get(i);
        }

        @Override
        public Double value(int i, StatsType stat) {
            return sampleStats[i].getStatisticValue(stat);
        }

        @Override
        public Long count(int i) {
            return sampleStats[i].getNumAccepted();
        }

        public void print() {
            for (int i = 0; i < size(); i++) {
                LOG.info(String.valueOf(range(i)));
                for (StatsType type : statsTypes) {
                    LOG.info(type + " = " + value(i, type));
                }
            }
        }
    }

    private static class RangeStatistics {
        private final List<Statistics> stats;
        private final List<StatsType> statTypes;
        private Double noData;

        public RangeStatistics(List<StatsType> statTypes) {
            this.statTypes = statTypes;
            stats = statTypes.stream()
                    .map(t -> StatsFactory.createSimpleStatisticsObjectFromInt(t.getStatsId()))
                    .collect(Collectors.toList());
        }

        public List<Statistics> getStats() {
            return stats;
        }

        public void addSample(double value) {
            if (Double.isNaN(value) || noData != null && noData.equals(value)) {
                return; // skip noData values
            }
            for (Statistics stat : stats) {
                stat.addSample(value);
            }
        }

        public void setNodata(Double noData) {
            this.noData = noData;
        }

        public Long getNumAccepted() {
            return stats.get(0).getNumSamples();
        }

        public Double getStatisticValue(StatsType stat) {
            int idx = statTypes.indexOf(stat);
            if (idx < 0 || idx >= stats.size()) {
                throw new IllegalArgumentException("Unknown statistic: " + stat);
            }

            Statistics statistics = stats.get(idx);
            return ((Number) statistics.getResult()).doubleValue();
        }
    }
}
