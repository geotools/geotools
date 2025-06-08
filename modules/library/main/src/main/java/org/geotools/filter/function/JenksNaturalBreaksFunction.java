/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Literal;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.logging.Logging;

/**
 * Calculate the Jenks' Natural Breaks classification for a featurecollection
 *
 * @author Ian Turton
 */
public class JenksNaturalBreaksFunction extends ClassificationFunction {
    org.geotools.api.util.ProgressListener progress;

    private static final Logger logger = Logging.getLogger(JenksNaturalBreaksFunction.class);

    public static FunctionName NAME = new FunctionNameImpl(
            "Jenks",
            RangedClassifier.class,
            parameter("value", Double.class),
            parameter("classes", Integer.class),
            parameter("percentages", Boolean.class, 0, 1));

    public JenksNaturalBreaksFunction() {
        super(NAME);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.filter.function.ClassificationFunction#evaluate(java.lang.Object)
     */
    @Override
    public Object evaluate(Object feature) {
        if (!(feature instanceof FeatureCollection)) {
            return null;
        }
        return calculate((SimpleFeatureCollection) feature);
    }

    /**
     * This is based on James' GeoTools1 code which seems to be based on
     * http://lib.stat.cmu.edu/cmlib/src/cluster/fish.f
     *
     * @return a RangedClassifier
     */
    private Object calculate(SimpleFeatureCollection featureCollection) {
        ArrayList<Double> data = new ArrayList<>();
        try (SimpleFeatureIterator features = featureCollection.features()) {
            try {
                while (features.hasNext()) {
                    SimpleFeature feature = features.next();
                    final Object result = getParameters().get(0).evaluate(feature);
                    logger.finest("importing " + result);
                    if (result != null) {
                        final Double e = Double.valueOf(result.toString());
                        if (!e.isInfinite() && !e.isNaN()) data.add(e);
                    }
                }
            } catch (NumberFormatException e) {
                return null; // if it isn't a number what should we do?
            }
        }
        Collections.sort(data);

        if (data.size() == 1 || data.get(0).equals(data.get(data.size() - 1))) {
            return new RangedClassifier(new Comparable[] {data.get(0)}, new Comparable[] {data.get(0)});
        }

        final int k = getClasses();
        final int m = data.size();
        if (k == m) {
            logger.info("Number of classes ("
                    + k
                    + ") is equal to number of data points ("
                    + m
                    + ") "
                    + "unique classification returned");
            Comparable[] localMin = new Comparable[k];
            Comparable[] localMax = new Comparable[k];

            for (int id = 0; id < k - 1; id++) {

                localMax[id] = data.get(id + 1);
                localMin[id] = data.get(id);
            }
            localMax[k - 1] = data.get(k - 1);
            localMin[k - 1] = data.get(k - 1);
            return new RangedClassifier(localMin, localMax);
        }
        int[][] iwork = new int[m + 1][k + 1];
        double[][] work = new double[m + 1][k + 1];

        for (int j = 1; j <= k; j++) {
            // the first item is always in the first class!
            iwork[0][j] = 1;
            iwork[1][j] = 1;
            // initialize work matirix
            work[1][j] = 0;
            for (int i = 2; i <= m; i++) {
                work[i][j] = Double.MAX_VALUE;
            }
        }

        // calculate the class for each data item
        for (int i = 1; i <= m; i++) {
            // sum of data values
            double s1 = 0;
            // sum of squares of data values
            double s2 = 0;

            double var = 0.0;
            // consider all the previous values
            for (int ii = 1; ii <= i; ii++) {
                // index in to sorted data array
                int i3 = i - ii + 1;
                // remember to allow for 0 index
                double val = data.get(i3 - 1);
                // update running totals
                s2 = s2 + val * val;
                s1 += val;
                double s0 = ii;
                // calculate (square of) the variance
                // (http://secure.wikimedia.org/wikipedia/en/wiki/Standard_deviation#Rapid_calculation_methods)
                var = s2 - s1 * s1 / s0;
                // System.out.println(s0+" "+s1+" "+s2);
                // System.out.println(i+","+ii+" var "+var);
                int ik = i3 - 1;
                if (ik != 0) {
                    // not the last value
                    for (int j = 2; j <= k; j++) {
                        // for each class compare current value to var + previous value
                        // System.out.println("\tis "+work[i][j]+" >= "+(var + work[ik][j - 1]));
                        if (work[i][j] >= var + work[ik][j - 1]) {
                            // if it is greater or equal update classification
                            iwork[i][j] = i3 - 1;
                            // System.out.println("\t\tiwork["+i+"]["+j+"] = "+i3);
                            work[i][j] = var + work[ik][j - 1];
                        }
                    }
                }
            }
            // store the latest variance!
            iwork[i][1] = 1;
            work[i][1] = var;
        }
        if (logger.getLevel() == Level.FINER) {
            for (int i = 0; i < m; i++) {
                String tmp = i + ": " + data.get(i);
                for (int j = 2; j <= k; j++) {
                    tmp += "\t" + iwork[i][j];
                }
                logger.finer(tmp);
            }
        }
        // go through matrix and extract class breaks
        int ik = m - 1;

        Comparable[] localMin = new Comparable[k];
        Comparable[] localMax = new Comparable[k];
        localMax[k - 1] = data.get(ik);
        for (int j = k; j >= 2; j--) {
            logger.finest("index " + ik + ", class" + j);
            int id = iwork[ik][j] - 1; // subtract one as we want inclusive breaks on the
            // left?

            localMax[j - 2] = data.get(id);
            localMin[j - 1] = data.get(id);
            ik = iwork[ik][j] - 1;
        }
        localMin[0] = data.get(0);
        /*
         * for(int k1=0;k1<k;k1++) { // System.out.println(k1+" "+localMin[k1]+" - "+localMax[k1]); }
         */

        RangedClassifier classifier = new RangedClassifier(localMin, localMax);
        if (getParameters().size() > 2) {
            Literal literal = (Literal) getParameters().get(2);
            Boolean percentages = (Boolean) literal.getValue();
            if (percentages.booleanValue()) {
                setPercentages(classifier, data, m);
            }
        }
        return classifier;
    }

    private void setPercentages(RangedClassifier classifier, List<Double> data, double total) {
        int classN = classifier.getSize();
        double[] percentages = new double[classN];
        for (int i = 0; i < classN; i++) {
            double classMembers = getClassMembers(data, classifier, i);
            percentages[i] = classMembers / total * 100;
        }
        classifier.setPercentages(percentages);
    }

    private double getClassMembers(List<Double> data, RangedClassifier classifier, int currentIdx) {
        double result;
        double max = (double) classifier.getMax(currentIdx);
        double min = (double) classifier.getMin(currentIdx);
        if (max == min) {
            result = data.stream().filter(d -> d == max).count();
        } else {
            if (currentIdx == 0)
                result = data.stream().filter(d -> d >= min && d <= max).count();
            else result = data.stream().filter(d -> d > min && d <= max).count();
        }
        return result;
    }
}
