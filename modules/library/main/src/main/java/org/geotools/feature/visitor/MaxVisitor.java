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
package org.geotools.feature.visitor;

import java.util.Arrays;
import java.util.List;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Calculates the maximum value of an attribute.
 *
 * @author Cory Horner, Refractions Research Inc.
 * @since 2.2.M2
 */
public class MaxVisitor implements FeatureCalc, FeatureAttributeVisitor {
    private Expression expr;
    Comparable maxvalue;
    Comparable curvalue;
    boolean visited = false;
    int countNull = 0;
    int countNaN = 0;

    public MaxVisitor(String attributeTypeName) {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(attributeTypeName);
    }

    public MaxVisitor(int attributeTypeIndex, SimpleFeatureType type)
            throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(type.getDescriptor(attributeTypeIndex).getLocalName());
    }

    public MaxVisitor(String attrName, SimpleFeatureType type) throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(type.getDescriptor(attrName).getLocalName());
    }

    public MaxVisitor(Expression expr) throws IllegalFilterException {
        this.expr = expr;
    }

    public void init(SimpleFeatureCollection collection) {
        // do nothing
    }

    @Override
    public List<Expression> getExpressions() {
        return Arrays.asList(expr);
    }

    /**
     * Visitor function, which looks at each feature and finds the maximum.
     *
     * @param feature the feature to be visited
     */
    public void visit(SimpleFeature feature) {
        visit((org.opengis.feature.Feature) feature);
    }

    public void visit(org.opengis.feature.Feature feature) {
        Object attribValue = expr.evaluate(feature);

        if (attribValue == null) {
            countNull++; // increment the null count, but don't store its value
            return;
        }

        if (attribValue instanceof Double) {
            double doubleVal = ((Double) attribValue).doubleValue();
            if (Double.isNaN(doubleVal) || Double.isInfinite(doubleVal)) {
                countNaN++; // increment the NaN count, but don't store NaN as the max
                return;
            }
        }

        curvalue = (Comparable) attribValue;

        if ((!visited) || (curvalue.compareTo(maxvalue) > 0)) {
            maxvalue = curvalue;
            visited = true;
        }

        // throw new IllegalStateException("Expression is not comparable!");
    }

    /**
     * Get the max value.
     *
     * @return Max value
     */
    public Comparable getMax() {
        if (!visited) {
            throw new IllegalStateException("Must visit before max value is ready!");
        }

        return maxvalue;
    }

    /** @return the number of features which returned a NaN */
    public int getNaNCount() {
        return countNaN;
    }

    /** @return the number of features which returned a null */
    public int getNullCount() {
        return countNull;
    }

    public void reset() {
        /** Reset the count and current maximum */
        this.visited = false;
        this.maxvalue = Integer.valueOf(Integer.MIN_VALUE);
        this.countNaN = 0;
        this.countNull = 0;
    }

    public Expression getExpression() {
        return expr;
    }

    public CalcResult getResult() {
        if (!visited) {
            return CalcResult.NULL_RESULT;
        }

        return new MaxResult(maxvalue);
    }

    /**
     * Overwrites the result stored by the visitor. This should only be used by optimizations which
     * will tell the visitor the answer rather than visiting all features.
     *
     * <p>For 'max', the value stored is of type 'Comparable'.
     */
    public void setValue(Object result) {
        visited = true;
        maxvalue = (Comparable) result;
    }

    public static class MaxResult extends AbstractCalcResult {
        private Comparable maxValue;

        public MaxResult(Comparable newMaxValue) {
            maxValue = newMaxValue;
        }

        public Object getValue() {
            Comparable max = (Comparable) maxValue;

            return max;
        }

        public boolean isCompatible(CalcResult targetResults) {
            // list each calculation result which can merge with this type of result
            if (targetResults instanceof MaxResult || targetResults == CalcResult.NULL_RESULT) {
                return true;
            }

            return false;
        }

        public CalcResult merge(CalcResult resultsToAdd) {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException("Parameter is not a compatible type");
            }

            if (resultsToAdd == CalcResult.NULL_RESULT) {
                return this;
            }

            if (resultsToAdd instanceof MaxResult) {
                // take the smaller of the 2 values
                Comparable toAdd = (Comparable) resultsToAdd.getValue();
                Comparable newMax = maxValue;

                if (newMax.getClass()
                        != toAdd.getClass()) { // 2 different data types, therefore convert
                    Class bestClass = CalcUtil.bestClass(new Object[] {toAdd, newMax});
                    if (bestClass != toAdd.getClass())
                        toAdd = (Comparable) CalcUtil.convert(toAdd, bestClass);
                    if (bestClass != newMax.getClass())
                        newMax = (Comparable) CalcUtil.convert(newMax, bestClass);
                }
                if (newMax.compareTo(toAdd) < 0) {
                    newMax = toAdd;
                }

                return new MaxResult(newMax);
            } else {
                throw new IllegalArgumentException(
                        "The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
}
