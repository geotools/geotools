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

import java.util.List;
import java.util.Optional;

public class CalcUtil {

    /**
     * Sums an array of numbers together while using the correct class type.
     *
     * @return the sum contained in the most appropriate number class
     */
    static Number sum(Number... numbers) {
        Number newSum = (Number) getObject((Object[]) numbers);

        if (newSum == null) {
            return null;
        }

        // Integer, Long, Float, Double
        if (newSum instanceof Integer) {
            int sum = 0;
            int nextValue;

            for (Number number : numbers) {
                nextValue = number.intValue();
                sum += nextValue;
            }

            newSum = Integer.valueOf(sum);
        } else if (newSum instanceof Long) {
            long sum = 0;
            long nextValue;

            for (Number number : numbers) {
                nextValue = number.longValue();
                sum += nextValue;
            }

            newSum = Long.valueOf(sum);
        } else if (newSum instanceof Float) {
            float sum = 0;
            float nextValue;

            for (Number number : numbers) {
                nextValue = number.floatValue();
                sum += nextValue;
            }

            newSum = Float.valueOf(sum);
        } else if (newSum instanceof Double) {
            double sum = 0;
            double nextValue;

            for (Number number : numbers) {
                nextValue = number.doubleValue();
                sum += nextValue;
            }

            newSum = Double.valueOf(sum);
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

        Number division = (Number) getObject((Object[]) both);

        if (division == null) {
            return null;
        }

        // Integer, Long, Float, Double
        if (division instanceof Integer) {
            // we've got 2 integers, but we're going to use double anyways
            return Double.valueOf(num1.doubleValue() / num2.doubleValue());
        } else if (division instanceof Long) {
            return Long.valueOf(num1.longValue() / num2.longValue());
        } else if (division instanceof Float) {
            return Float.valueOf(num1.floatValue() / num2.floatValue());
        } else if (division instanceof Double) {
            return Double.valueOf(num1.doubleValue() / num2.doubleValue());
        } else {
            return null;
        }
    }

    /** Calculates the average, and returns it in the correct class. */
    static Number average(Number... numbers) {
        Number sum = sum(numbers);

        return divide(sum, Integer.valueOf(numbers.length));
    }

    /**
     * Determines the most appropriate class to use for a multiclass calculation.
     *
     * @return the most
     */
    static Class bestClass(Object... objects) {
        boolean hasInt = false;
        boolean hasFloat = false;
        boolean hasLong = false;
        boolean hasDouble = false;
        boolean hasString = false;

        for (Object object : objects) {
            if (object instanceof Double) {
                hasDouble = true;
            } else if (object instanceof Float) {
                hasFloat = true;
            } else if (object instanceof Long) {
                hasLong = true;
            } else if (object instanceof Integer) {
                hasInt = true;
            } else if (object instanceof String) {
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
        } else { // it's a type we don't have here yet

            return null;
        }
    }

    /** Casts an object to the specified type */
    static Object convert(Object var, Class type) {
        if (var instanceof Number) { // use number conversion

            Number newNum = (Number) var;

            if (type == Integer.class) {
                return Integer.valueOf(newNum.intValue());
            } else if (type == Long.class) {
                return Long.valueOf(newNum.longValue());
            } else if (type == Float.class) {
                return Float.valueOf(newNum.floatValue());
            } else if (type == Double.class) {
                return Double.valueOf(newNum.doubleValue());
            } else if (type == String.class) {
                return newNum.toString();
            }
        } else { // direct cast

            if (type == Integer.class) {
                return Integer.valueOf(((Integer) var).intValue());
            } else if (type == Long.class) {
                return Long.valueOf(((Long) var).longValue());
            } else if (type == Float.class) {
                return Float.valueOf(((Float) var).floatValue());
            } else if (type == Double.class) {
                return Double.valueOf(((Double) var).doubleValue());
            } else if (type == String.class) {
                return var.toString();
            }
        }

        return null;
    }

    static Object convert(Object[] objects, Object var) {
        Object newVar = getObject(objects);

        if (newVar instanceof Number) {
            Number newNum = (Number) var;

            if (newVar instanceof Integer) {
                return Integer.valueOf(newNum.intValue());
            } else if (newVar instanceof Long) {
                return Long.valueOf(newNum.longValue());
            } else if (newVar instanceof Float) {
                return Float.valueOf(newNum.floatValue());
            } else if (newVar instanceof Double) {
                return Double.valueOf(newNum.doubleValue());
            } else {
                return null;
            }
        } else if (newVar instanceof String) {
            return (String) newVar;
        } else {
            // TODO: add other classes
            return null;
        }
    }

    /**
     * Given an array of objects, traverses the array and determines the most suitable data type to perform the
     * calculation in. An empty object of the correct class is returned;
     */
    static Object getObject(Object... objects) {
        Class bestClass = bestClass(objects);

        if (bestClass == String.class) {
            return ""; // $NON-NLS-1$
        } else if (bestClass == Double.class) {
            return Double.valueOf(0);
        } else if (bestClass == Float.class) {
            return Float.valueOf(0);
        } else if (bestClass == Long.class) {
            return Long.valueOf(0);
        } else if (bestClass == Integer.class) {
            return Integer.valueOf(0);
        } else { // it's a type we don't have here yet
            return null;
        }
    }

    /** Similar to java.lang.Comparable.compareTo, but can handle 2 different data types. */
    @SuppressWarnings("unchecked")
    static int compare(Comparable val1, Comparable val2) {
        if (val1.getClass() == val2.getClass()) {
            // both the same type, no conversion is necessary.
            return val1.compareTo(val2);
        }

        // find most appropriate class
        Object[] objects = {val1, val2};
        Class bestClass = bestClass(objects);

        if (bestClass != val1.getClass()) {
            val1 = (Comparable) convert(val1, bestClass);
        }

        if (bestClass != val2.getClass()) {
            val2 = (Comparable) convert(val2, bestClass);
        }

        // now do the comparison
        return val1.compareTo(val2);
    }

    /**
     * Utility method for {@link FeatureAttributeVisitor} implementations that are simply returnin the same type as the
     * inputs
     *
     * @param inputTypes
     * @return
     */
    public static Optional<List<Class>> reflectInputTypes(int expectedInputCount, List<Class> inputTypes) {
        if (inputTypes == null || inputTypes.size() != expectedInputCount)
            throw new IllegalArgumentException(
                    "Expecting " + expectedInputCount + " types in input, but got " + inputTypes);

        // same as the input
        return Optional.of(inputTypes);
    }
}
