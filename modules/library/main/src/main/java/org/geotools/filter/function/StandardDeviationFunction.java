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
import java.util.*;
import java.util.logging.Level;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.StandardDeviationVisitor;
import org.geotools.filter.capability.FunctionNameImpl;
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
                        getPercentages(
                                featureCollection,
                                classifier,
                                getParameters().get(0),
                                standardDeviation);
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
            FeatureCollection features,
            RangedClassifier classifier,
            Expression attr,
            double standardDeviation)
            throws IOException {
        int classSize = classifier.getSize();
        Object firstMax = classifier.getMax(0);
        Filter less = FF.less(attr, FF.literal(firstMax));
        int sizeFirstClass = features.subCollection(less).size();
        int totalSize = features.size();
        double[] percentages = new double[classSize];
        // we don't know the min value in the collection because the
        // the first interval is open to infinity to the left.
        // needs a query to get the classMembers
        percentages[0] = (sizeFirstClass / totalSize) * 100;

        double min = ((Number) classifier.getMin(1)).doubleValue();
        return computeGroupByPercentages(features, percentages, totalSize, min, standardDeviation);
    }

    @Override
    protected void computePercentage(
            double[] percentages, double classMembers, double totalSize, int index) {
        // since we are grouping by using the min of the second interval adds +1 to the index
        super.computePercentage(percentages, classMembers, totalSize, index + 1);
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
