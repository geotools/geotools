/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    reated on October 27, 2004, 11:27 AM
 */
package org.geotools.filter.function;

import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Divide;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.Subtract;
import org.geotools.api.util.ProgressListener;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.filter.DefaultExpression;
import org.geotools.filter.FunctionExpression;
import org.geotools.util.factory.GeoTools;

/**
 * Parent for classifiers which break a feature collection into the specified number of classes.
 *
 * @author James Macgill
 * @author Cory Horner, Refractions Research Inc.
 */
public abstract class ClassificationFunction extends DefaultExpression implements FunctionExpression {

    protected static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ClassificationFunction.class);

    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());

    FunctionName name;

    /** function params * */
    List<org.geotools.api.filter.expression.Expression> params = new ArrayList<>(2);

    Literal fallback;

    ProgressListener progress;

    public ClassificationFunction(FunctionName name) {
        this.name = name;
    }

    /** @see org.geotools.api.filter.expression.Expression#accept(ExpressionVisitor, Object) */
    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public abstract Object evaluate(Object arg);

    @Override
    public void setFallbackValue(Literal fallback) {
        this.fallback = fallback;
    }

    @Override
    public Literal getFallbackValue() {
        return fallback;
    }

    /**
     * Gets the name of this function.
     *
     * @return the name of the function.
     */
    @Override
    public String getName() {
        return name.getName();
    }

    @Override
    public FunctionName getFunctionName() {
        return name;
    }

    /**
     * Returns the function parameters (the contents are Expressions, usually attribute expression and literal
     * expression).
     */
    @Override
    public List<org.geotools.api.filter.expression.Expression> getParameters() {
        return params;
    }

    /** Sets the function parameters. */
    @Override
    public void setParameters(List<org.geotools.api.filter.expression.Expression> params) {
        this.params = params;
    }

    public ProgressListener getProgressListener() {
        return progress;
    }

    public void setProgressListener(ProgressListener progress) {
        this.progress = progress;
    }

    public int getClasses() {
        Literal classes = (Literal) getParameters().get(1);
        return classes.evaluate(null, Integer.class).intValue();
    }

    public void setClasses(int classes) {
        org.geotools.api.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal expression = ff.literal(classes);
        getParameters().set(1, expression);
    }

    /** Returns the implementation hints. The default implementation returns an empty map. */
    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    /** Determines the number of decimal places to truncate the interval at. */
    protected int decimalPlaces(double slotWidth) {
        if (slotWidth == 0) {
            return 5;
        }
        String str = Double.toString(slotWidth);
        if (str.indexOf(".") > -1) {
            while (str.endsWith("0")) {
                str = str.substring(0, str.length() - 1);
            }
        }
        int intPart = (int) Math.floor(slotWidth);
        double decPart = slotWidth - intPart;
        int intPoints = Integer.toString(intPart).length();
        int decPoints = str.length() - intPoints;
        if (str.indexOf(".") > -1) {
            decPoints--;
        }
        if (decPart == 0) {
            decPoints = 0;
        }
        // if there are dec points, show at least one
        if (intPart == 0) { // if int part is 0, be very specific
            if (decPoints > 6) {
                return 5;
            } else if (decPoints > 0) {
                return decPoints;
            } else {
                return 1;
            }
        } else if (decPoints == 0) { // if there are no dec points, don't show any
            return 0;
        } else { // aim for a number of digits (not including '.') up to a reasonable limit (5)
            int chars = intPoints + decPoints;
            if (chars < 6) {
                return decPoints;
            } else if (intPoints > 4) {
                return 1;
            } else {
                return 5 - intPoints;
            }
        }
    }

    /**
     * Truncates a double to a certain number of decimals places. Note: truncation at zero decimal places will still
     * show up as x.0, since we're using the double type.
     *
     * @param value number to round-off
     * @param decimalPlaces number of decimal places to leave
     * @return the rounded value
     */
    protected double round(double value, int decimalPlaces) {
        double divisor = Math.pow(10, decimalPlaces);
        double newVal = value * divisor;
        newVal = Math.round(newVal) / divisor;
        return newVal;
    }

    /**
     * Corrects a round off operation by incrementing or decrementing the decimal place (preferably the smallest one).
     * This should usually be used to adjust the bounds to include a value. Example: 0.31-->0.44 where 0.44 is the
     * maximum value and end of the range. We could just make the , round(0.31, 1)=0.3; round(0.44 max value = 0.49
     */
    protected double fixRound(double value, int decimalPlaces, boolean up) {
        double divisor = Math.pow(10, decimalPlaces);
        double newVal = value * divisor;
        if (up) newVal++; // +0.001 (for 3 dec places)
        else newVal--; // -0.001
        newVal = newVal / divisor;
        return newVal;
    }

    /** Creates a String representation of this Function with the function name and the arguments. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("(");
        List<org.geotools.api.filter.expression.Expression> params = getParameters();
        if (params != null) {
            org.geotools.api.filter.expression.Expression exp;
            for (Iterator<org.geotools.api.filter.expression.Expression> it = params.iterator(); it.hasNext(); ) {
                exp = it.next();
                sb.append("[");
                sb.append(exp);
                sb.append("]");
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * This method return percentages by using a single groupBy query to retrieve class members. Can be used if the
     * class width is the same for all the classes.
     *
     * @param collection the feature collection to classify
     * @param percentages the array of percentages to fill
     * @param totalSize the totalSize of the collection
     * @param min the min attribute value in the collection
     * @param classWidth the classWidth
     * @return
     * @throws IOException
     */
    protected double[] computeGroupByPercentages(
            FeatureCollection collection, double[] percentages, int totalSize, double min, double classWidth)
            throws IOException {
        Subtract subtract = FF.subtract(getParameters().get(0), FF.literal(min));
        Divide divide = FF.divide(subtract, FF.literal(classWidth));
        Function convert = FF.function("convert", divide, FF.literal(Integer.class));
        GroupByVisitor groupBy =
                new GroupByVisitor(Aggregate.COUNT, getParameters().get(0), Arrays.asList(convert), null);
        collection.accepts(groupBy, null);
        @SuppressWarnings("unchecked")
        Map<List<Integer>, Integer> result = groupBy.getResult().toMap();
        Map<Integer, Integer> resultIntKeys = result.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().get(0), e -> e.getValue()));
        // getting a tree set from the keys to get them asc ordered and
        // collect percentages in the right order
        Set<Integer> keys = new TreeSet<>(resultIntKeys.keySet());
        for (Integer key : keys) {
            int intKey = key.intValue();
            computePercentage(percentages, Double.valueOf(resultIntKeys.get(key)), totalSize, intKey);
        }
        return percentages;
    }

    /** Compute the percentage from the input parameters, setting in the percentages array at the specified index */
    protected void computePercentage(double[] percentages, double classMembers, double totalSize, int index) {
        // handle case when the query return one class plus,
        // e.g. classWidth is an integer so that in an interval of values 1-25,
        // for three classes, we would have value 25 falling in group with key 3
        if (index >= percentages.length) {
            int last = percentages.length - 1;
            percentages[last] += classMembers / totalSize * 100;
        } else {
            percentages[index] = classMembers / totalSize * 100;
        }
    }
}
