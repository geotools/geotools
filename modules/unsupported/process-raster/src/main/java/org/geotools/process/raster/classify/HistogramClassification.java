/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster.classify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classification that collects an histogram of the data instead of single values. Better suited for
 * large datasets where collecting each single value would require too much memory.
 */
public class HistogramClassification extends Classification {

    public static final class Bucket {
        int count;
        double average;
        double min;
        double max;

        public Bucket(int count, double singleValue) {
            this.count = count;
            this.average = this.min = this.max = singleValue;
        }

        public Bucket(int count, double average, double min, double max) {
            this.count = count;
            this.average = average;
            this.min = min;
            this.max = max;
        }

        public int getCount() {
            return count;
        }

        public double getAverage() {
            return average;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        @Override
        public String toString() {
            return "Bucket{"
                    + "count="
                    + count
                    + ", average="
                    + average
                    + ", min="
                    + min
                    + ", max="
                    + max
                    + '}';
        }
    }

    private final double[] maximums;
    private final int[][] bucketCounts;
    private final double[][] bucketAverages;
    private final boolean[][] bucketSingleValue;
    private final double[] minimums;
    private final double[] bucketSize;

    public HistogramClassification(int numBands, Double[][] extrema, int numBins) {
        super(null, numBands);
        if (extrema == null) {
            throw new IllegalArgumentException(
                    "Histogram based classification methods need to be provided with the extrema parameter");
        }
        checkExtremaArray(numBands, extrema, 0, "min");
        checkExtremaArray(numBands, extrema, 1, "max");
        for (int b = 0; b < numBands; b++) {
            setMin(b, extrema[0][b]);
            setMax(b, extrema[1][b]);
        }
        this.minimums = Arrays.stream(extrema[0]).mapToDouble(d -> d).toArray();
        this.maximums = Arrays.stream(extrema[1]).mapToDouble(d -> d).toArray();
        this.bucketSize = new double[numBands];
        for (int b = 0; b < numBands; b++) {
            bucketSize[b] = (extrema[1][b] - extrema[0][b]) / numBins;
        }
        bucketCounts = new int[numBands][numBins];
        bucketAverages = new double[numBands][numBins];
        bucketSingleValue = new boolean[numBands][numBins];
        for (int b = 0; b < numBands; b++) {
            Arrays.fill(bucketSingleValue[b], true);
        }
    }

    private void checkExtremaArray(
            int numBands, Double[][] extrema, int minMax, String minMaxName) {
        if (extrema[minMax].length < numBands) {
            throw new IllegalArgumentException(
                    "Illegal extrema array, should have "
                            + minMaxName
                            + " array of "
                            + numBands
                            + " elements but only has "
                            + extrema[minMax].length
                            + " instead");
        }
    }

    public void count(double value, int band) {
        // throw away all elements outside of the desired range
        double minimum = minimums[band];
        double maximum = maximums[band];
        if (value < minimum || value > maximum) {
            return;
        }

        // compute bucket involved
        int idx = (int) ((value - minimum) / bucketSize[band]);
        // update bucket count and bucket average
        int[] bucketCount = bucketCounts[band];
        // on the max value the result might be the n+1 bucket
        if (idx == bucketCount.length) {
            idx--;
        }
        // increment count
        bucketCount[idx]++;
        double[] bucketsAverage = bucketAverages[band];
        // iterative mean here
        double average = bucketsAverage[idx];
        if (bucketCount[idx] > 1) {
            bucketSingleValue[band][idx] &= (average == value);
        }
        bucketsAverage[idx] = average + (value - average) / bucketCount[idx];
    }

    /**
     * Returns a list of all non empty buckets
     *
     * @return
     */
    public List<Bucket> getBuckets(int band) {
        List<Bucket> buckets = new ArrayList<>();
        int[] histogram = bucketCounts[band];
        double[] bucketAverage = bucketAverages[band];
        double minimum = minimums[band];
        double size = bucketSize[band];
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > 0) {
                if (bucketSingleValue[band][i]) {
                    buckets.add(new Bucket(histogram[i], bucketAverage[i]));
                } else {
                    buckets.add(
                            new Bucket(
                                    histogram[i],
                                    bucketAverage[i],
                                    minimum + i * size,
                                    minimum + (i + 1) * size));
                }
            }
        }

        return buckets;
    }
}
