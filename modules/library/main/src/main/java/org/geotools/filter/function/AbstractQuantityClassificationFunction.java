/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.util.NullProgressListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.FeatureCalc;
import org.opengis.filter.capability.FunctionName;

public abstract class AbstractQuantityClassificationFunction extends ClassificationFunction {

    public AbstractQuantityClassificationFunction(FunctionName name) {
        super(name);
    }

    /**
     * Returns the list visitor for the specific implementation of "quantity" (e.g., count, area,
     * ...). The visitor must return a "bins" structure matching {code}List<Comparable>[]{code},
     * where each array entry is a bin, and values inside the bin are sorted from lowest to highest
     */
    protected abstract FeatureCalc getListVisitor();

    private Object calculate(SimpleFeatureCollection featureCollection) {
        FeatureCalc quantityVisitor = getListVisitor();
        if (progress == null) progress = new NullProgressListener();
        try {
            featureCollection.accepts(quantityVisitor, progress);
        } catch (IOException e) {
            LOGGER.log(
                    Level.SEVERE, "QuantileFunction calculate(SimpleFeatureCollection) failed", e);
            return null;
        }
        if (progress.isCanceled()) return null;
        CalcResult calcResult = quantityVisitor.getResult();
        if (calcResult == null || calcResult == CalcResult.NULL_RESULT) {
            return null;
        }
        List[] bin = (List[]) calcResult.getValue();

        // generate the min and max values, and round off if applicable/necessary
        Comparable globalMin = (Comparable) bin[0].toArray()[0];
        Object lastBin[] = bin[bin.length - 1].toArray();
        if (lastBin.length == 0) {
            return null;
        }
        Comparable globalMax = (Comparable) lastBin[lastBin.length - 1];

        if ((globalMin instanceof Number) && (globalMax instanceof Number)) {
            return calculateNumerical(bin, globalMin, globalMax);
        } else {
            return calculateNonNumerical(bin, globalMin, globalMax);
        }
    }

    private Object calculateNumerical(List[] bin, Comparable globalMin, Comparable globalMax) {
        if (globalMax.equals(globalMin)) {
            return new RangedClassifier(new Comparable[] {globalMin}, new Comparable[] {globalMax});
        }

        int classNum = bin.length;
        // size arrays
        Comparable[] localMin = new Comparable[classNum];
        Comparable[] localMax = new Comparable[classNum];
        // globally consistent
        // double slotWidth = (((Number) globalMax).doubleValue() - ((Number)
        // globalMin).doubleValue()) / classNum;
        for (int i = 0; i < classNum; i++) {
            // copy the min + max values
            List thisBin = bin[i];
            localMin[i] = (Comparable) thisBin.get(0);
            localMax[i] = (Comparable) thisBin.get(thisBin.size() - 1);
            // locally accurate
            double slotWidth =
                    ((Number) localMax[i]).doubleValue() - ((Number) localMin[i]).doubleValue();
            if (slotWidth == 0.0) { // use global value, as there is only 1 value in this set
                slotWidth =
                        (((Number) globalMax).doubleValue() - ((Number) globalMin).doubleValue())
                                / classNum;
            }
            // determine number of decimal places to allow
            int decPlaces = decimalPlaces(slotWidth);
            decPlaces = Math.max(decPlaces, decimalPlaces(((Number) localMin[i]).doubleValue()));
            decPlaces = Math.max(decPlaces, decimalPlaces(((Number) localMax[i]).doubleValue()));
            // clean up truncation error
            if (decPlaces > -1) {
                localMin[i] =
                        Double.valueOf(round(((Number) localMin[i]).doubleValue(), decPlaces));
                localMax[i] =
                        Double.valueOf(round(((Number) localMax[i]).doubleValue(), decPlaces));
            }

            if (i == 0) {
                // ensure first min is less than or equal to globalMin
                if (localMin[i].compareTo(Double.valueOf(((Number) globalMin).doubleValue())) > 0)
                    localMin[i] =
                            Double.valueOf(
                                    fixRound(
                                            ((Number) localMin[i]).doubleValue(),
                                            decPlaces,
                                            false));
            } else if (i == classNum - 1) {
                // ensure last max is greater than or equal to globalMax
                if (localMax[i].compareTo(Double.valueOf(((Number) globalMax).doubleValue())) < 0)
                    localMax[i] =
                            Double.valueOf(
                                    fixRound(
                                            ((Number) localMax[i]).doubleValue(), decPlaces, true));
            }

            // synchronize previous max with current min; the ranged classifier is min <= x < y;
            if (i != 0) {
                localMax[i - 1] = localMin[i];
            }
        }
        // TODO: disallow having 2 identical bins (ie 0..0, 0..0, 0..0, 0..100)
        return new RangedClassifier(localMin, localMax);
    }

    private Object calculateNonNumerical(List[] bin, Comparable globalMin, Comparable globalMax) {
        if (globalMax.equals(globalMin)) {
            return new ExplicitClassifier(new Set[] {Collections.singleton(globalMin)});
        }

        int classNum = bin.length;
        // it's a string.. leave it be (just copy the values)
        Set[] values = new Set[classNum];
        for (int i = 0; i < classNum; i++) {
            values[i] = new HashSet();
            Iterator iterator = bin[i].iterator();
            while (iterator.hasNext()) {
                values[i].add(iterator.next());
            }
        }
        return new ExplicitClassifier(values);
        //      alternative for ranged classifier
        //      localMin[i] = (Comparable) thisBin.get(0);
        //      localMax[i] = (Comparable) thisBin.get(thisBin.size()-1);
    }

    public Object evaluate(Object feature) {
        if (!(feature instanceof FeatureCollection)) {
            return null;
        }
        return calculate((SimpleFeatureCollection) feature);
    }
}
