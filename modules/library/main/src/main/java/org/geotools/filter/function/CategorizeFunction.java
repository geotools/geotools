/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Implementation of "Categorize" as a normal function.
 * <p>
 * This implementation is compatible with the Function
 * interface; the parameter list can be used to set the
 * threshold values etc...
 * <p>
 * This function expects:
 * <ol>
 * <li>PropertyName; use "Rasterdata" to indicate this is a color map
 * <li>Literal: lookup value
 * <li>Literal: threshold 1
 * <li>Literal: value 1
 * <li>Literal: threshold 2
 * <li>Literal: value 2
 * <li>Literal: (Optional) succeeding or preceding
 * </ol>
 * In reality any expression will do.
 * @author Jody Garnett
 * @author Johann Sorel (Geomatys)
 *
 *
 * @source $URL$
 */
public class CategorizeFunction implements Function {

    /** Use as a literal value to indicate behaviour of threshold boundary */
    public static final String SUCCEEDING = "succeeding";
    
    /** Use as a literal value to indicate behaviour of threshold boundary */
    public static final String PRECEDING = "preceding"; 
    
    /**
     * Use as a PropertyName when defining a color map.
     * The "Raterdata" is expected to apply to only a single band; if multiple
     * bands are provided it is probably a mistake; but we will use the maximum
     * value (since we are working against a threshold).
     */
    public static final String RASTER_DATA = "Rasterdata";
    
    private final List<Expression> parameters;
    private final Literal fallback;
    /**
     * True if all expressions in the param set are static values
     */
    private boolean staticTable = true;
    double[] thresholds;
    Expression[] values;
    volatile Object[] convertedValues;
    private Class convertedValuesContext;
    private String belongsTo;
    
    /**
     * Make the instance of FunctionName available in
     * a consistent spot.
     */
    public static final FunctionName NAME = new Name();

    /**
     * Describe how this function works.
     * (should be available via FactoryFinder lookup...)
     */
    public static class Name implements FunctionName {

        public int getArgumentCount() {
            return 2; // indicating unbounded, 2 minimum
        }

        public List<String> getArgumentNames() {
            return Arrays.asList(new String[]{
                        "LookupValue",
                        "Value",
                        "Threshold 1", "Value 1",
                        "Threshold 2", "Value 2",
                        "succeeding or preceding"
                    });
        }

        public String getName() {
            return "Categorize";
        }
    };

    public CategorizeFunction() {
        this( new ArrayList<Expression>(), null);
    }

    public CategorizeFunction(List<Expression> parameters, Literal fallback) {
        this.parameters = parameters;
        this.fallback = fallback;
        
        // check for valid structure, we need lookup, value, [threshold, value]*, [thresholdInclusion]
        if(parameters.size() % 2 != 0) {
            // in this case the last value must be either succeeding or preceding, as a literal
            Expression lastParameter = parameters.get(parameters.size() - 1);
            String lastValue = lastParameter.evaluate(null, String.class);
            
            if (PRECEDING.equalsIgnoreCase(lastValue)) {
                belongsTo = PRECEDING;
            } else if (SUCCEEDING.equalsIgnoreCase(lastValue)) {
                belongsTo = SUCCEEDING;
            } else {
                throw new IllegalArgumentException("The valid structure of a categorize function call is " +
                		"\"lookup, value, [threshold, value]*, [succeeding|preceding]\", " +
                		"yet there is a odd number of parameters and the last value is not succeeding nor preceeding");
            }
        }
        
        // see if the table is full of attribute independent expressions
        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
        thresholds = new double[(parameters.size() - 1) / 2];
        values = new Expression[thresholds.length + 1];
        for (int i = 1; i < parameters.size(); i++) {
            Expression expression = parameters.get(i);
            if(expression != null) {
                extractor.clear();
                expression.accept(extractor, null);
                if(!extractor.isConstantExpression()) {
                    staticTable = false;
                    thresholds = null;
                    break;
                } else { 
                    if(i % 2 == 0) {
                        Double threshold = expression.evaluate(null, Double.class);
                        if(threshold == null) {
                            staticTable = false;
                            thresholds = null;
                            break;
                        } else {
                            thresholds[i / 2 - 1] = threshold;
                        }
                    } else {
                        values[i / 2] = expression; 
                    }
                }
            }
        }
        // allow for binary search
        if(thresholds != null) {
            Arrays.sort(thresholds);
        }
    }

    public String getName() {
        return NAME.getName();
    }

    public List<Expression> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public Object evaluate(Object object) {
        return evaluate(object, Object.class);
    }

    public <T> T evaluate(Object object, Class<T> context) {
        final Expression lookupExp = parameters.get(0);
        
        // check the value we're looking for
        Double value = lookupExp.evaluate(object, Double.class);
        if(value == null) {
            value = Converters.convert(object, Double.class);
        }
        
        if(value != null && staticTable) {
            int expIdx = Arrays.binarySearch(thresholds, value);
            int valIdx;
            if(expIdx >= 0) {
                // right at the threshold
                if (PRECEDING.equals(belongsTo)) {
                    valIdx = expIdx;
                } else {
                    valIdx = expIdx + 1;
                }
            } else {
                // between threshold values, get the next value
                valIdx = -expIdx - 1;
            }
            
            // do we have a pre-converted set of values as well?
            if(convertedValues == null) {
                synchronized (this) {
                    if(convertedValues == null) {
                        convertedValues = new Object[values.length];
                        for (int i = 0; i < convertedValues.length; i++) {
                            convertedValues[i] = values[i].evaluate(object, context);
                        }
                        convertedValuesContext = context;
                    }
                }
            }
            
            // if we can use the pre-converted go for it, otherwise dynamic eval
            if(convertedValuesContext == context) {
                return (T) convertedValues[valIdx];
            } else {
                return values[valIdx].evaluate(object, context);
            }
        }

        // generic evaluation path, slow
        Expression currentExp = parameters.get(1);
        final List<Expression> splits;
        if (parameters.size() == 2) {
            return currentExp.evaluate(object, context);
        } else if (parameters.size() % 2 == 0) {
            splits = parameters.subList(2, parameters.size());
        } else {
            splits = parameters.subList(2, parameters.size() - 1);
        }
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        for (int i = 0; i < splits.size(); i += 2) {
            Expression threshholdExp = splits.get(i);
            Expression rangedExp = splits.get(i + 1);
            
            Filter isIncludedInThreshold;
            if (PRECEDING.equals(belongsTo)) {
                isIncludedInThreshold = ff.greater(lookupExp, threshholdExp);
            } else {
                isIncludedInThreshold = ff.greaterOrEqual(lookupExp, threshholdExp);
            }
            if (isIncludedInThreshold.evaluate(object)) {
                currentExp = rangedExp;
            } else {
                break;
            }
        }
        return currentExp.evaluate(object, context);
    }

    public Literal getFallbackValue() {
        return fallback;
    }
    
}
