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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.DefaultExpression;
import org.geotools.filter.FunctionExpression;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Literal;
import org.opengis.util.ProgressListener;

/**
 * Parent for classifiers which break a feature collection into the specified number of classes.
 *
 * @author James Macgill
 * @author Cory Horner, Refractions Research Inc.
 */
public abstract class ClassificationFunction extends DefaultExpression
        implements FunctionExpression {

    protected static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ClassificationFunction.class);

    FunctionName name;

    /** function params * */
    List<org.opengis.filter.expression.Expression> params =
            new ArrayList<org.opengis.filter.expression.Expression>(2);

    Literal fallback;

    ProgressListener progress;

    public ClassificationFunction(FunctionName name) {
        this.name = name;
    }

    /** @see org.opengis.filter.expression.Expression#accept(ExpressionVisitor, Object) */
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public abstract Object evaluate(Object arg);

    public void setFallbackValue(Literal fallback) {
        this.fallback = fallback;
    }

    public Literal getFallbackValue() {
        return fallback;
    }

    /**
     * Gets the name of this function.
     *
     * @return the name of the function.
     */
    public String getName() {
        return name.getName();
    }

    public FunctionName getFunctionName() {
        return name;
    }

    /**
     * Returns the function parameters (the contents are Expressions, usually attribute expression
     * and literal expression).
     */
    public List<org.opengis.filter.expression.Expression> getParameters() {
        return params;
    }

    /** Sets the function parameters. */
    public void setParameters(List<org.opengis.filter.expression.Expression> params) {
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
        return ((Integer) classes.evaluate(null, Integer.class)).intValue();
    }

    public void setClasses(int classes) {
        org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Literal expression = ff.literal(classes);
        getParameters().set(1, expression);
    }

    /** Returns the implementation hints. The default implementation returns an empty map. */
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
        int intPart = Double.valueOf(Math.floor(slotWidth)).intValue();
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
     * Truncates a double to a certain number of decimals places. Note: truncation at zero decimal
     * places will still show up as x.0, since we're using the double type.
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
     * Corrects a round off operation by incrementing or decrementing the decimal place (preferably
     * the smallest one). This should usually be used to adjust the bounds to include a value.
     * Example: 0.31-->0.44 where 0.44 is the maximum value and end of the range. We could just make
     * the , round(0.31, 1)=0.3; round(0.44 max value = 0.49
     */
    protected double fixRound(double value, int decimalPlaces, boolean up) {
        double divisor = Math.pow(10, decimalPlaces);
        double newVal = value * divisor;
        if (up) newVal++; // +0.001 (for 3 dec places)
        else newVal--; // -0.001
        newVal = newVal / divisor;
        return newVal;
    }

    /**
     * Creates a String representation of this Function with the function name and the arguments.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append("(");
        List<org.opengis.filter.expression.Expression> params = getParameters();
        if (params != null) {
            org.opengis.filter.expression.Expression exp;
            for (Iterator<org.opengis.filter.expression.Expression> it = params.iterator();
                    it.hasNext(); ) {
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
}
