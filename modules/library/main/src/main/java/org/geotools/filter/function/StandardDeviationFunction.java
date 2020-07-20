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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.StandardDeviationVisitor;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.feature.Feature;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Breaks a SimpleFeatureCollection into classes using the standard deviation classification method.
 *
 * @author Cory Horner, Refractions Research Inc.
 */
public class StandardDeviationFunction extends ClassificationFunction {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "StandardDeviation",
                    RangedClassifier.class,
                    parameter("value", Double.class),
                    parameter("classes", Integer.class),
                    parameter("percentages", Boolean.class, 0, 1));

    public StandardDeviationFunction() {
        super(NAME);
    }

    private Object calculate(SimpleFeatureCollection featureCollection) {
        try {
            int classNum = getClasses();

            // find the standard deviation
            StandardDeviationVisitor sdVisit = new StandardDeviationVisitor(getParameters().get(0));

            featureCollection.accepts(sdVisit, progress);
            if (progress != null && progress.isCanceled()) {
                return null;
            }
            CalcResult calcResult = sdVisit.getResult();
            if (calcResult == null) {
                return null;
            }
            double standardDeviation = calcResult.toDouble();
            if (standardDeviation == 0) {
                return new RangedClassifier(
                        new Comparable[] {sdVisit.getMean()}, new Comparable[] {sdVisit.getMean()});
            }

            // figure out the min and max values
            Double min[] = new Double[classNum];
            Double max[] = new Double[classNum];
            for (int i = 0; i < classNum; i++) {
                min[i] = getMin(i, classNum, sdVisit.getMean(), standardDeviation);
                max[i] = getMax(i, classNum, sdVisit.getMean(), standardDeviation);
            }
            RangedClassifier classifier = new RangedClassifier(min, max);
            if (percentages()) {
                double[] percentages =
                        getPercentages(featureCollection, classifier, getParameters().get(0));
                classifier.setPercentages(percentages);
            }
            return classifier;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "StandardDeviationFunction calculate failed", e);
            return null;
        }
    }

    public Object evaluate(Object feature) {
        if (!(feature instanceof FeatureCollection)) {
            return null;
        }
        return calculate((SimpleFeatureCollection) feature);
    }

    private Double getMin(int index, int numClasses, double average, double standardDeviation) {
        if (index <= 0 || index >= numClasses) return null;
        return Double.valueOf(average - (((numClasses / 2.0) - index) * standardDeviation));
    }

    private Double getMax(int index, int numClasses, double average, double standardDeviation) {
        if (index < 0 || index >= numClasses - 1) return null;
        return Double.valueOf(average - (((numClasses / 2.0) - 1 - index) * standardDeviation));
    }

    private double[] getPercentages(
            FeatureCollection features, RangedClassifier classifier, Expression attr) {
        int size = classifier.getSize();
        List<Filter> filters = getFiltersForPercentages(classifier, size, attr);
        int[][] bins = new int[size][1];
        try (FeatureIterator it = features.features()) {
            while (it.hasNext()) {
                Feature f = it.next();
                int i = 0;
                for (Filter filter : filters) {
                    if (filter.evaluate(f)) {
                        bins[i][0]++;
                        break;
                    }
                    i++;
                }
            }
        }
        return computePercentages(bins, features.size());
    }

    private List<Filter> getFiltersForPercentages(
            RangedClassifier classifier, int size, Expression attr) {
        List<Filter> filters = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Object min = classifier.getMin(i);
            Object max = classifier.getMax(i);
            if (min == null) {
                filters.add(FF.less(attr, FF.literal(max)));
            } else if (max == null) {
                filters.add(FF.greaterOrEqual(attr, FF.literal(min)));
            } else {
                Filter f1 = FF.greaterOrEqual(attr, FF.literal(min));
                Filter f2 = FF.less(attr, FF.literal(max));
                Filter and = FF.and(f1, f2);
                filters.add(and);
            }
        }
        return filters;
    }

    private double[] computePercentages(int[][] bins, double totalSize) {
        double[] percentages = new double[bins.length];
        for (int i = 0; i < bins.length; i++) {
            double classMembers = bins[i][0];
            if (classMembers != 0d && totalSize != 0d)
                percentages[i] = (classMembers / totalSize) * 100;
            else percentages[i] = 0d;
        }
        return percentages;
    }

    protected boolean percentages() {
        boolean percentages = false;
        if (getParameters().size() > 2) {
            Literal literal = (Literal) getParameters().get(2);
            percentages = ((Boolean) literal.getValue()).booleanValue();
        }
        return percentages;
    }
}
