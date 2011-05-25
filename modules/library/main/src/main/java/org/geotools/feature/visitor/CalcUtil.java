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


/**
 *
 *
 * @source $URL$
 */
public class CalcUtil {

	/**
     * Sums an array of numbers together while using the correct class type.
     *
     * @param numbers
     *
     * @return the sum contained in the most appropriate number class
     */
    static Number sum(Number[] numbers) {
        Number newSum = (Number) getObject(numbers);

        if (newSum == null) {
            return null;
        }

        //Integer, Long, Float, Double
        if (newSum instanceof Integer) {
            int sum = 0;
            int nextValue;

            for (int i = 0; i < numbers.length; i++) {
                nextValue = numbers[i].intValue();
                sum += nextValue;
            }

            newSum = new Integer(sum);
        } else if (newSum instanceof Long) {
            long sum = 0;
            long nextValue;

            for (int i = 0; i < numbers.length; i++) {
                nextValue = numbers[i].longValue();
                sum += nextValue;
            }

            newSum = new Long(sum);
        } else if (newSum instanceof Float) {
            float sum = 0;
            float nextValue;

            for (int i = 0; i < numbers.length; i++) {
                nextValue = numbers[i].floatValue();
                sum += nextValue;
            }

            newSum = new Float(sum);
        } else if (newSum instanceof Double) {
            double sum = 0;
            double nextValue;

            for (int i = 0; i < numbers.length; i++) {
                nextValue = numbers[i].doubleValue();
                sum += nextValue;
            }

            newSum = new Double(sum);
        } else {
            return null;
        }

        return newSum;
    }

    /**
     * Divides num1 by num2, and return the result in the correct number class.
     * 
     * @param num1 numerator
     * @param num2 denominator
     * @return num1/num2 in the most appropriate class
     */
    static Number divide(Number num1, Number num2) {
        Number[] both = new Number[2];
        both[0] = num1;
        both[1] = num2;

        Number division = (Number) getObject(both);

        if (division == null) {
            return null;
        }

        //Integer, Long, Float, Double
        if (division instanceof Integer) {
            //we've got 2 integers, but we're going to use double anyways
            return new Double(num1.doubleValue() / num2.doubleValue());
        } else if (division instanceof Long) {
            return new Long(num1.longValue() / num2.longValue());
        } else if (division instanceof Float) {
            return new Float(num1.floatValue() / num2.floatValue());
        } else if (division instanceof Double) {
            return new Double(num1.doubleValue() / num2.doubleValue());
        } else {
            return null;
        }
    }

    /**
     * Calculates the average, and returns it in the correct class.
     * @param numbers
     */
    static Number average(Number[] numbers) {
        Number sum = sum(numbers);

        return divide(sum, new Integer(numbers.length));
    }

    /**
     * Determines the most appropriate class to use for a multiclass calculation.
     * 
     * @param objects
     * @return the most
     */static Class bestClass(Object[] objects) {
        boolean hasInt = false;
        boolean hasFloat = false;
        boolean hasLong = false;
        boolean hasDouble = false;
        boolean hasString = false;

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof Double) {
                hasDouble = true;
            } else if (objects[i] instanceof Float) {
                hasFloat = true;
            } else if (objects[i] instanceof Long) {
                hasLong = true;
            } else if (objects[i] instanceof Integer) {
                hasInt = true;
            } else if (objects[i] instanceof String) {
                hasString = true;
            }
        }

        if (hasString) {
            return String.class;
        } else if (hasDouble) {
            return Double.class;
        } else if (hasFloat) {
            return Float.class;
        } else if (hasLong) {
            return Long.class;
        } else if (hasInt) {
            return Integer.class;
        } else { //it's a type we don't have here yet

            return null;
        }
    }

    /**
     * Casts an object to the specified type
     *
     * @param var
     * @param type
     *
     */
    static Object convert(Object var, Class type) {
        if (var instanceof Number) { //use number conversion

            Number newNum = (Number) var;

            if (type == Integer.class) {
                return new Integer(newNum.intValue());
            } else if (type == Long.class) {
                return new Long(newNum.longValue());
            } else if (type == Float.class) {
                return new Float(newNum.floatValue());
            } else if (type == Double.class) {
                return new Double(newNum.doubleValue());
            } else if (type == String.class) {
                return new String(newNum.toString());
            }
        } else { //direct cast

            if (type == Integer.class) {
                return new Integer(((Integer) var).intValue());
            } else if (type == Long.class) {
                return new Long(((Long) var).longValue());
            } else if (type == Float.class) {
                return new Float(((Float) var).floatValue());
            } else if (type == Double.class) {
                return new Double(((Double) var).doubleValue());
            } else if (type == String.class) {
                return new String(var.toString());
            }
        }

        return null;
    }

    static Object convert(Object[] objects, Object var) {
        Object newVar = getObject(objects);

        if (newVar instanceof Number) {
            Number newNum = (Number) var;

            if (newVar instanceof Integer) {
                return new Integer(newNum.intValue());
            } else if (newVar instanceof Long) {
                return new Long(newNum.longValue());
            } else if (newVar instanceof Float) {
                return new Float(newNum.floatValue());
            } else if (newVar instanceof Double) {
                return new Double(newNum.doubleValue());
            } else {
                return null;
            }
        } else if (newVar instanceof String) {
            return new String((String) newVar);
        } else {
            //TODO: add other classes
            return null;
        }
    }

    /**
     * Given an array of objects, traverses the array and determines the most
     * suitable data type to perform the calculation in. An empty object of
     * the correct class is returned;
     *
     * @param objects
     *
     */
    static Object getObject(Object[] objects) {
    	Class bestClass = bestClass(objects);
    	
        if (bestClass == String.class) {
            return new String(""); //$NON-NLS-1$
        } else if (bestClass == Double.class) {
            return new Double(0);
        } else if (bestClass == Float.class) {
            return new Float(0);
        } else if (bestClass == Long.class) {
            return new Long(0);
        } else if (bestClass == Integer.class) {
            return new Integer(0);
        } else { //it's a type we don't have here yet
            return null;
        }
    }

    /**
     * Similar to java.lang.Comparable.compareTo, but can handle 2 different
     * data types.
     *
     * @param val1
     * @param val2
     *
     */
    static int compare(Comparable val1, Comparable val2) {
        if (val1.getClass() == val2.getClass()) {
            //both the same type, no conversion is necessary.
            return val1.compareTo(val2);
        }

        //find most appropriate class
        Object[] objects = new Object[] { val1, val2 };
        Class bestClass = bestClass(objects);

        if (bestClass != val1.getClass()) {
            val1 = (Comparable) convert(val1, bestClass);
        }

        if (bestClass != val2.getClass()) {
            val2 = (Comparable) convert(val2, bestClass);
        }

        //now do the comparison
        return val1.compareTo(val2);
    }
}
