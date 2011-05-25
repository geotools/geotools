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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Calculates the median of an attribute in all features of a collection
 *
 * @author Cory Horner, Refractions
 *
 * @since 2.2.M2
 *
 * @source $URL$
 */
public class MedianVisitor implements FeatureCalc {
    private Expression expr;
    private List list = new ArrayList();
    /**
	 * This var is only used to store the median for optimized functions, where
	 * we don't have a complete list, but just the answer instead (merging will
	 * be disabled until some cool code is written to handle this). Only
	 * setValue(median) should write to this var. If this value is not null, it
	 * takes priority over list.
	 */
    private Object median = null;

    public MedianVisitor(String attributeTypeName) {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(attributeTypeName);
    }
    
    public MedianVisitor(int attributeTypeIndex, SimpleFeatureType type)
        throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(type.getDescriptor(attributeTypeIndex).getLocalName());
    }

    public MedianVisitor(String attrName, SimpleFeatureType type)
        throws IllegalFilterException {
        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
        expr = factory.property(type.getDescriptor(attrName).getLocalName());
    }

    public MedianVisitor(Expression expr) throws IllegalFilterException {
        this.expr = expr;
    }

    public void init(SimpleFeatureCollection collection) {
    	//do nothing
    }
    
    public void visit(SimpleFeature feature) {
        visit((org.opengis.feature.Feature)feature);
    }
    public void visit(org.opengis.feature.Feature feature) {
    	/**
         * Visitor function
         */
        Object result = expr.evaluate(feature);
        if (result instanceof Comparable) {
            Comparable value = (Comparable) result;
            list.add(value);
        } else {
            throw new IllegalStateException("Expression is not comparable!");
        }
    }

    public Expression getExpression() {
        return expr;
    }
    
    /**
     * Return the median of all features in the collection
     */
    public Object getMedian() {
		if (median != null) {
			//median was overwritten by an optimization
			return median;
		} else {
			//we're got a list of items, determine the median...
			Object newMedian = findMedian(list);
			if (newMedian == null) {
				throw new IllegalStateException(
						"Must visit before median value is ready!");
			}
			return newMedian;
		}
	}

    /**
	 * Reset the stored information about the median.
	 */
    public void reset() {
        this.list.clear();
        this.median = null;
    }

    public CalcResult getResult() {
		if (median != null) {
			// median was overwritten by an optimization
			return new MedianResult(median);
		} else if (list.size() < 1) {
			// no items in the list
            return CalcResult.NULL_RESULT;
		} else {
			// we have a list; create a CalcResult containing the list
			return new MedianResult(list);
		}
	}
    
    public void setValue(List list) {
    	reset();
    	this.list = list;
    }

    public void setValue(Comparable median) {
    	reset();
    	this.median = median;
    }

    public static class MedianResult extends AbstractCalcResult {
        private List list;
        /**
		 * When an optimization is used, median will have a value and list will
		 * not. This var takes priority over list.
		 */
        private Object median;

        public MedianResult(List newList) {
            this.list = newList;
            this.median = null;
        }

        public MedianResult(Object median) {
            this.list = null;
            this.median = median;
        }
        
        public List getList() {
        	return list;
        }
        
        public Object getValue() {
        	if (median != null) {
        		return median;
        	} else {
        		return findMedian(list);
        	}
        }

        public boolean isCompatible(CalcResult targetResults) {
            //list each calculation result which can merge with this type of result
        	if (targetResults instanceof MedianResult || targetResults == CalcResult.NULL_RESULT) return true;
        	return false;
        }

        public boolean isOptimized() {
        	if (median != null) return true;
        	else return false;
        }
        
        public CalcResult merge(CalcResult resultsToAdd) {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException(
                    "Parameter is not a compatible type");
            }
            
            if(resultsToAdd == CalcResult.NULL_RESULT) {
        		return this;
        	}
            
            if (resultsToAdd instanceof MedianResult) {
            	MedianResult moreResults = (MedianResult) resultsToAdd;
            	//ensure both MedianResults are NOT optimized
            	if (isOptimized() || moreResults.isOptimized()) {
                	throw new IllegalArgumentException("Optimized median results cannot be merged.");
            	}
            	//merge away...
            	List toAdd = (ArrayList) moreResults.getList();
                List newList = new ArrayList();
                //extract each item to an array, and convert to a common data type
                int size = list.size() + toAdd.size();
                Object[] values = new Object[size];
                int i;
                for (i = 0; i < list.size(); i++)
                	values[i] = list.get(i);
                for (int j = 0; j < toAdd.size(); j++)
                	values[i+j] = toAdd.get(j);
                Class bestClass = CalcUtil.bestClass(values);
                for (int k = 0; k < size; k++) {
                	if (values[k].getClass() != bestClass)
                		values[k] = CalcUtil.convert(values[k], bestClass);
                	newList.add((Comparable) values[k]);
                }
                return new MedianResult(newList);
            } else {
            	throw new IllegalArgumentException(
				"The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
    
    /**
	 * Given a list, determines the median value and returns it. For numbers,
	 * the middle value is returned, or the average of the two middle numbers if
	 * there are an even number of elements. For non-numeric values (strings,
	 * etc) where the number of elements is even, a list containing the two
	 * middle elements is returned.
	 * 
	 * @param list an arraylist which is to be sorted and its median extracted
	 * @return the median
	 */
    private static Object findMedian(List list) {
    	if (list.size() < 1) {
    		return null;
    	}
    	Object median;
        Collections.sort(list);

        int index = -1;
        index = (int) list.size() / 2;

        if ((list.size() % 2) == 0) {
        	// even number of elements, so we must average the 2 middle ones, or
			// return a list for non-numeric elements
            Object input1 = list.get(index-1);
            Object input2 = list.get(index);
            
            if ((input1 instanceof Number) && (input2 instanceof Number)) {
            	Number num1 = (Number) input1;
            	Number num2 = (Number) input2;
            	Number[] numbers = new Number[2];
            	numbers[0] = num1; numbers[1] = num2;
            	median = CalcUtil.average(numbers);
            } else { //NaN
            	//return a list containing the two middle elements
            	List newList = new ArrayList();
            	newList.add(input1);
            	newList.add(input2);
            	median = newList;
            }
        } else {
        	// an odd number of elements are in the list, so we simply return
			// the one in the middle, which we've already calculated the index
			// for.
            median = list.get(index);
        }
        return median;
    }
}
