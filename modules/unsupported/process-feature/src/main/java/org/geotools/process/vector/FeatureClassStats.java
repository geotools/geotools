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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.function.ClassificationFunction;
import org.geotools.filter.function.EqualIntervalFunction;
import org.geotools.filter.function.JenksNaturalBreaksFunction;
import org.geotools.filter.function.QuantileFunction;
import org.geotools.filter.function.RangedClassifier;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.process.ProcessException;
import org.geotools.process.classify.ClassificationMethod;
import org.geotools.process.classify.ClassificationStats;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.jaitools.numeric.Range;
import org.jaitools.numeric.Statistic;
import org.jaitools.numeric.StreamingSampleStats;
import org.opengis.feature.Feature;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.util.ProgressListener;

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
        description =
                "Calculates statistics from feature" + " values classified into bins/classes.")
public class FeatureClassStats implements VectorProcess {

    static Logger LOG = Logging.getLogger(FeatureClassStats.class);

    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    @DescribeResult(name = "results", description = "The classified results")
    public Results execute(
            @DescribeParameter(name = "features", description = "The feature collection to analyze")
                    FeatureCollection features,
            @DescribeParameter(name = "attribute", description = "The feature attribute to analyze")
                    String attribute,
            @DescribeParameter(
                            name = "stats",
                            description = "The statistics to calculate for each class",
                            collectionType = Statistic.class)
                    Set<Statistic> stats,
            @DescribeParameter(
                            name = "classes",
                            description = "The number of breaks/classes",
                            min = 0)
                    Integer classes,
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
            throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "features"));
        }
        if (attribute == null) {
            throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "attribute"));
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
            throw new ProcessException(
                    Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "classes", classes));
        }

        // other defaults
        if (method == null) {
            method = ClassificationMethod.EQUAL_INTERVAL;
        }
        if (stats == null || stats.isEmpty()) {
            stats = Collections.singleton(Statistic.MEAN);
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
        cf.setParameters(
                Arrays.asList(filterFactory.property(attribute), filterFactory.literal(classes)));

        // compute the breaks
        RangedClassifier rc = (RangedClassifier) cf.evaluate(features);

        // build up the stats
        List<Range<Double>> ranges = new ArrayList<>();
        StreamingSampleStats[] sampleStats = new StreamingSampleStats[rc.getSize()];
        for (int i = 0; i < rc.getSize(); i++) {
            ranges.add(
                    Range.create(
                            (Double) rc.getMin(i),
                            true,
                            (Double) rc.getMax(i),
                            i == rc.getSize() - 1));

            StreamingSampleStats s = new StreamingSampleStats(Range.Type.INCLUDE);
            s.setStatistics(stats.toArray(new Statistic[stats.size()]));

            if (noData != null) {
                s.addNoDataValue(noData);
            }

            sampleStats[i] = s;
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
                    LOG.warning(
                            String.format(
                                    "Unable to convert value %s (attribute '%s') to Double, "
                                            + "skipping",
                                    val, attribute));
                    continue;
                }

                int slot = rc.classify(dubVal);
                sampleStats[slot].offer(dubVal);
            }
        }

        return new Results(ranges, sampleStats);
    }

    public static class Results implements ClassificationStats {

        List<Range<Double>> ranges;
        StreamingSampleStats[] sampleStats;
        Statistic firstStat;

        public Results(List<Range<Double>> ranges, StreamingSampleStats[] sampleStats) {
            this.ranges = ranges;
            this.sampleStats = sampleStats;
            this.firstStat = sampleStats[0].getStatistics().iterator().next();
        }

        @Override
        public int size() {
            return ranges.size();
        }

        @Override
        public Set<Statistic> getStats() {
            return sampleStats[0].getStatistics();
        }

        @Override
        public Range range(int i) {
            return ranges.get(i);
        }

        @Override
        public Double value(int i, Statistic stat) {
            return sampleStats[i].getStatisticValue(stat);
        }

        @Override
        public Long count(int i) {
            return sampleStats[i].getNumAccepted(firstStat);
        }

        public void print() {
            for (int i = 0; i < size(); i++) {
                LOG.info(String.valueOf(range(i)));
                for (Statistic stat : sampleStats[0].getStatistics()) {
                    LOG.info(stat + " = " + value(i, stat));
                }
            }
        }
    }
}
