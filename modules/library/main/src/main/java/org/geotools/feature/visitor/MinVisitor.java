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
 * Calculates the minimum value of an attribute.
 *
 * @author Cory Horner, Refractions
 *
 * @since 2.2.M2
 *
 *
 * @source $URL$
 */
public class MinVisitor implements FeatureCalc, FeatureAttributeVisitor {
    private Expression expr;
    Comparable minvalue;
    Comparable curvalue;
    boolean visited = false;

    public MinVisitor(String attributeTypeName) {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(attributeTypeName);
    }
    
    public MinVisitor(int attributeTypeIndex, SimpleFeatureType type)
        throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(type.getDescriptor(attributeTypeIndex).getLocalName());
    }

    public MinVisitor(String attrName, SimpleFeatureType type)
        throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(type.getDescriptor(attrName).getLocalName());
    }

    public MinVisitor(Expression expr) throws IllegalFilterException {
        this.expr = expr;
    }

    public void init(SimpleFeatureCollection collection) {
    	//do nothing
    }

    @Override
    public List<Expression> getExpressions() {
        return Arrays.asList(expr);
    }

    /**
     * Visitor function, which looks at each feature and finds the minimum.
     *
     * @param feature the feature to be visited
     */
    public void visit(SimpleFeature feature) {
        visit((org.opengis.feature.Feature)feature);
    }
    public void visit(org.opengis.feature.Feature feature) {
        Object attribValue = expr.evaluate(feature);

        if (attribValue == null) {
            return; //attribute is null, therefore skip
        }

        curvalue = (Comparable) attribValue;
        if ((!visited) || (curvalue.compareTo(minvalue) < 0)) {
            minvalue = curvalue;
            visited = true;
        }
    }

    /**
     * Get the min value.
     *
     * @return Minimum value
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public Comparable getMin() {
        /**
         * Return the minimum value derived from the collection
         */
        if (!visited) {
            throw new IllegalStateException(
                "Must visit before min value is ready!");
        }

        return minvalue;
    }

    public void reset() {
        /**
         * Reset the count and current minimum
         */
        this.visited = false;
        this.minvalue = new Integer(0);
    }

    public CalcResult getResult() {
        if (!visited) {
            return CalcResult.NULL_RESULT;
        }

        return new MinResult(minvalue);
    }

    public Expression getExpression() {
        return expr;
    }

    /**
     * Overwrites the result stored by the visitor. This should only be used by
     * optimizations which will tell the visitor the answer rather than
     * visiting all features.
     * 
     * <p></p>
     * For 'min', the value stored is of type 'Comparable'.
     *
     * @param result
     */
    public void setValue(Object result) {
        visited = true;
        minvalue = (Comparable) result;
    }

    public static class MinResult extends AbstractCalcResult {
        private Comparable minValue;

        public MinResult(Comparable newMinValue) {
            minValue = newMinValue;
        }

        public Object getValue() {
            Comparable min = (Comparable) minValue;

            return min;
        }

        public boolean isCompatible(CalcResult targetResults) {
            //list each calculation result which can merge with this type of result
            if (targetResults instanceof MinResult || targetResults == CalcResult.NULL_RESULT) {
                return true;
            }

            return false;
        }

        public CalcResult merge(CalcResult resultsToAdd) {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException(
                    "Parameter is not a compatible type");
            }
            
            if(resultsToAdd == CalcResult.NULL_RESULT) {
        		return this;
        	}

            if (resultsToAdd instanceof MinResult) {
                //take the smaller of the 2 values
                Comparable toAdd = (Comparable) resultsToAdd.getValue();
                Comparable newMin = minValue;

                if (newMin.getClass() != toAdd.getClass()) { //2 different data types, therefore convert
                	Class bestClass = CalcUtil.bestClass(new Object[] {toAdd, newMin});
            		if (bestClass != toAdd.getClass())
            			toAdd = (Comparable) CalcUtil.convert(toAdd, bestClass);
            		if (bestClass != newMin.getClass())
            			newMin = (Comparable) CalcUtil.convert(newMin, bestClass);
                }
                if (newMin.compareTo(toAdd) > 0) {
                    newMin = toAdd;
                }

                return new MinResult(newMin);
            } else {
                throw new IllegalArgumentException(
                    "The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
}
