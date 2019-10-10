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
package org.geotools.feature.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Expression;

/**
 * Obtains the data needed for a Equal Area operation (classification of features into classes each
 * roughly having the same area).
 *
 * <p>The result contains an array of lists with the expression values in each.
 *
 * @author Andrea Aime, GeoSolutions
 */
public class EqualAreaListVisitor implements FeatureCalc {

    /** Combination of value and its area */
    private static class ValueArea implements Comparable {
        Comparable value;
        double area;

        public ValueArea(Comparable value, double area) {
            this.value = value;
            this.area = area;
        }

        @Override
        public int compareTo(Object o) {
            ValueArea other = (ValueArea) o;
            return value.compareTo(other.value);
        }

        @Override
        public String toString() {
            return "ValueArea{" + "value=" + value + ", area=" + area + '}';
        }
    }

    private Expression expression;
    private Expression areaExpression;
    private int count = 0;
    private int binCount;
    private List<ValueArea> items = new ArrayList<>();
    private List<Comparable>[] bins;

    boolean visited = false;
    int countNull = 0;
    int countNaN = 0;

    public EqualAreaListVisitor(Expression expression, Expression areaExpression, int bins) {
        this.expression = expression;
        this.areaExpression = areaExpression;
        this.binCount = bins;
        this.bins = new ArrayList[bins];
    }

    public void init(SimpleFeatureCollection collection) {
        // do nothing
    }

    public CalcResult getResult() {
        if (binCount == 0 || count == 0) {
            return CalcResult.NULL_RESULT;
        }

        // sort the list
        Collections.sort(items);

        if (binCount > count) { // resize
            binCount = count;
            this.bins = new ArrayList[binCount];
        }

        // algorithm based off "Greedy 2" version in
        // "Equal-area Breaks: A Classification Scheme for Data to Obtain an Evenly-colored
        // Choropleth Map"
        // https://pdfs.semanticscholar.org/151e/8036f6aa4412e7c7923c35bb8dd8113793d3.pdf
        double totalArea = items.stream().mapToDouble(item -> item.area).sum();
        double avgArea = totalArea / binCount;
        List<Comparable> currentBin = new ArrayList<>();
        int binIndex = 0;
        double area = 0;
        for (ValueArea valueArea : items) {
            currentBin.add(valueArea.value);
            area += valueArea.area;
            if (area >= avgArea * (binIndex + 1) && binIndex < binCount - 1) {
                bins[binIndex++] = currentBin;
                currentBin = new ArrayList<>();
            }
        }
        if (currentBin.size() > 0) {
            bins[binIndex] = currentBin;
        } else {
            binIndex--;
        }

        // it is possible that we have created less bin than requested
        // (happens when the number of classes is close to the number of features being classified)
        if (binIndex < binCount - 1) {
            List[] reduced = new ArrayList[binIndex + 1];
            System.arraycopy(bins, 0, reduced, 0, binIndex + 1);
            this.bins = reduced;
        }

        return new AbstractCalcResult() {
            public Object getValue() {
                return bins;
            }
        };
    }

    public void visit(SimpleFeature feature) {
        visit((org.opengis.feature.Feature) feature);
    }

    public void visit(org.opengis.feature.Feature feature) {
        Object value = expression.evaluate(feature);

        if (value == null) {
            countNull++; // increment the null count
            return; // don't store this value
        }

        if (value instanceof Double) {
            double doubleVal = ((Double) value).doubleValue();
            if (Double.isNaN(doubleVal) || Double.isInfinite(doubleVal)) {
                countNaN++; // increment the NaN count
                return; // don't store NaN value
            }
        }

        count++;
        Double area = areaExpression.evaluate(feature, Double.class);
        items.add(new ValueArea((Comparable) value, area));
    }

    public void reset(int bins) {
        this.binCount = bins;
        this.count = 0;
        this.items = new ArrayList<>();
        this.bins = new ArrayList[bins];
        this.countNull = 0;
        this.countNaN = 0;
    }

    /** @return the number of features which returned a NaN */
    public int getNaNCount() {
        return countNaN;
    }

    /** @return the number of features which returned a null */
    public int getNullCount() {
        return countNull;
    }
}
