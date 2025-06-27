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
 *
 */
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Literal;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.capability.FunctionNameImpl;

/**
 * Classification function for breaking a feature collection into edible chunks of "equal" size.
 *
 * @author James Macgill
 * @author Cory Horner, Refractions Research Inc.
 */
public class EqualIntervalFunction extends ClassificationFunction {

    public static FunctionName NAME = new FunctionNameImpl(
            "EqualInterval",
            RangedClassifier.class,
            parameter("value", Double.class),
            parameter("classes", Integer.class),
            parameter("percentages", Boolean.class, 0, 1));

    public EqualIntervalFunction() {
        super(NAME);
    }

    private RangedClassifier calculate(SimpleFeatureCollection featureCollection) {
        int classNum = getClasses();
        Comparable globalMin;
        Comparable globalMax;
        try {
            MinVisitor minVisit = new MinVisitor(getParameters().get(0));
            if (progress == null) progress = new NullProgressListener();
            featureCollection.accepts(minVisit, progress);
            if (progress.isCanceled()) return null;
            globalMin = (Comparable) minVisit.getResult().getValue();

            MaxVisitor maxVisit = new MaxVisitor(getParameters().get(0));
            featureCollection.accepts(maxVisit, progress);
            if (progress.isCanceled()) return null;
            globalMax = (Comparable) maxVisit.getResult().getValue();
            RangedClassifier result;
            boolean percentages = false;
            if (getParameters().size() > 2) {
                Literal literal = (Literal) getParameters().get(2);
                percentages = (Boolean) literal.getValue();
            }
            if (globalMin instanceof Number && globalMax instanceof Number) {
                result = calculateNumerical(classNum, globalMin, globalMax);
                if (percentages) result.setPercentages(getNumericalPercentages(classNum, result, featureCollection));
            } else {
                result = calculateNonNumerical(classNum, featureCollection);
                if (percentages) result.setPercentages(getNotNumericalPercentages(classNum, featureCollection.size()));
            }

            return result;
        } catch (IllegalFilterException | IOException e) { // accepts exploded
            LOGGER.log(Level.SEVERE, "EqualIntervalFunction calculate(SimpleFeatureCollection) failed", e);
            return null;
        } // getResult().getValue() exploded
    }

    @SuppressWarnings("unchecked") // assumes it can use random comparables with numbers
    private RangedClassifier calculateNumerical(int classNum, Comparable globalMin, Comparable globalMax) {
        // handle constant value case
        if (globalMax.equals(globalMin)) {
            return new RangedClassifier(new Comparable[] {globalMin}, new Comparable[] {globalMax});
        }

        double slotWidth = (((Number) globalMax).doubleValue() - ((Number) globalMin).doubleValue()) / classNum;
        // size arrays
        Comparable[] localMin = new Comparable[classNum];
        Comparable[] localMax = new Comparable[classNum];
        for (int i = 0; i < classNum; i++) {
            // calculate the min + max values
            localMin[i] = Double.valueOf(((Number) globalMin).doubleValue() + i * slotWidth);
            localMax[i] = Double.valueOf(((Number) globalMax).doubleValue() - (classNum - i - 1) * slotWidth);
            // determine number of decimal places to allow
            int decPlaces = decimalPlaces(slotWidth);
            // clean up truncation error
            if (decPlaces > -1) {
                localMin[i] = Double.valueOf(round(((Number) localMin[i]).doubleValue(), decPlaces));
                localMax[i] = Double.valueOf(round(((Number) localMax[i]).doubleValue(), decPlaces));
            }

            if (i == 0) {
                // ensure first min is less than or equal to globalMin
                if (localMin[i].compareTo(Double.valueOf(((Number) globalMin).doubleValue())) < 0)
                    localMin[i] = Double.valueOf(fixRound(((Number) localMin[i]).doubleValue(), decPlaces, false));
            } else if (i == classNum - 1) {
                // ensure last max is greater than or equal to globalMax
                if (localMax[i].compareTo(Double.valueOf(((Number) globalMax).doubleValue())) > 0)
                    localMax[i] = Double.valueOf(fixRound(((Number) localMax[i]).doubleValue(), decPlaces, true));
            }
            // synchronize min with previous max
            if (i != 0 && !localMin[i].equals(localMax[i - 1])) {
                localMin[i] = localMax[i - 1];
            }
        }
        return new RangedClassifier(localMin, localMax);
    }

    @SuppressWarnings("unchecked")
    private RangedClassifier calculateNonNumerical(int classNum, FeatureCollection<?, ?> featureCollection)
            throws IOException {
        // obtain of list of unique values, so we can enumerate
        UniqueVisitor uniqueVisit = new UniqueVisitor(getParameters().get(0));
        featureCollection.accepts(uniqueVisit, new NullProgressListener());
        List<Comparable> result = uniqueVisit.getResult().toList();
        // sort the results and put them in an array
        Collections.sort(result);

        Comparable[] values = result.toArray(Comparable[]::new);

        // size arrays
        classNum = Math.min(classNum, values.length);
        Comparable[] localMin = new Comparable[classNum];
        Comparable[] localMax = new Comparable[classNum];

        // we have 2 options here:
        // 1. break apart by numeric value: (aaa, aab, aac, bbb) --> [aaa, aab, aac], [bbb]
        // 2. break apart by item count:                         --> [aaa, aab], [aac, bbb]

        // this code currently implements option #2 (this is a quantile, why don't we use their code
        // instead)

        // calculate number of items to put in each of the larger bins
        int binPop = (int) Math.ceil((double) values.length / classNum);
        // determine index of bin where the next bin has one less item
        int lastBigBin = values.length % classNum;
        if (lastBigBin == 0) lastBigBin = classNum;
        else lastBigBin--;

        int itemIndex = 0;
        // for each bin
        for (int binIndex = 0; binIndex < classNum; binIndex++) {
            // store min
            if (binIndex < localMin.length)
                localMin[binIndex] = itemIndex < values.length ? values[itemIndex] : values[values.length - 1];
            else
                localMin[localMin.length - 1] =
                        itemIndex < values.length ? values[itemIndex] : values[values.length - 1];
            itemIndex += binPop;
            // store max
            if (binIndex == classNum - 1) {
                if (binIndex < localMax.length)
                    localMax[binIndex] = itemIndex < values.length ? values[itemIndex] : values[values.length - 1];
                else
                    localMax[localMax.length - 1] =
                            itemIndex < values.length ? values[itemIndex] : values[values.length - 1];
            } else {
                if (binIndex < localMax.length)
                    localMax[binIndex] =
                            itemIndex + 1 < values.length ? values[itemIndex + 1] : values[values.length - 1];
                else
                    localMax[localMax.length - 1] =
                            itemIndex + 1 < values.length ? values[itemIndex + 1] : values[values.length - 1];
            }
            if (lastBigBin == binIndex) binPop--; // decrease the number of items in a bin for the
            // next iteration
        }
        return new RangedClassifier(localMin, localMax);
    }

    @Override
    public RangedClassifier evaluate(Object object) {
        if (!(object instanceof FeatureCollection)) {
            return null;
        }
        return calculate((SimpleFeatureCollection) object);
    }

    private double[] getNotNumericalPercentages(int classNum, int totalSize) {
        int lastBigBin = totalSize % classNum;
        if (lastBigBin != 0) lastBigBin--;
        double classMembers = (double) totalSize / classNum;
        double[] percentages = new double[classNum];
        for (int i = 0; i < classNum; i++) {
            percentages[i] = classMembers / totalSize * 100;
            if (lastBigBin != 0 && lastBigBin == i) {
                classMembers--;
            }
        }
        return percentages;
    }

    private double[] getNumericalPercentages(int classNum, RangedClassifier classifier, FeatureCollection collection)
            throws IOException {

        double max = ((Number) classifier.getMax(classifier.getSize() - 1)).doubleValue();
        double min = ((Number) classifier.getMin(0)).doubleValue();
        double classWidth = Math.ceil((max - min) / classNum);
        int totalSize = collection.size();
        double[] percentages = new double[classNum];
        return computeGroupByPercentages(collection, percentages, totalSize, min, classWidth);
    }
}
