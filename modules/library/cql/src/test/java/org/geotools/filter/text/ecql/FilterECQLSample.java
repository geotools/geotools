/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.ecql;

import java.util.HashMap;
import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;


/**
 * Filter Samples for ECQL language
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
final class FilterECQLSample {
    protected static final FilterFactory FACTORY = CommonFactoryFinder.getFilterFactory((Hints) null);

    // ECQL Samples
    public static final String           ABS_FUNCTION_LESS_PROPERTY               = "abs(10) < aProperty";

    public static final String           AREA_FUNCTION_LESS_NUMBER                = "area( the_geom ) < 30000";

    public static final String           EXPRESION_GREATER_PROPERTY               = "(1+3) > aProperty";

    public static final String           FUNCTION_LESS_SIMPLE_ADD_EXPR            = "area( the_geom ) < (1+3)";

    public static final String           FUNC_AREA_LESS_FUNC_ABS                  = "area( the_geom ) < abs(10)";

    public static final String           ADD_EXPRESION_GREATER_SUBTRACT_EXPRESION = "(1+3) > (4-5)";

    public static final String           PROPERTY_GREATER_MINUS_INGEGER           = "aProperty > -1";

    public static final String           MINUS_INTEGER_GREATER_PROPERTY           = "-1 > aProperty";

    public static final String           PROPERTY_GREATER_MINUS_FLOAT             = "aProperty > -1.05";

    public static final String           MINUS_FLOAT_GREATER_PROPERTY             = "-1.05 > aProperty";

    public static final String           MINUS_EXPR_GREATER_PROPERTY              = "-1.05 + 4.6 > aProperty";

    public static final String           PROPERTY_GREATER_MINUS_EXPR              = "aProperty > -1.05 + 4.6";

    public static final String           PROPERTY_GREATER_NESTED_EXPR             = "-1.05 + (-4.6* -10) > aProperty";

    public static final String           MINUS_MINUS_EXPR_GRATER_PROPERTY         = "10--1.05 > aProperty";

    public static final String           FUNCTION_LIKE_ECQL_PATTERN                = "strConcat('aa', 'bbcc') like '%bb%'";

    public static final String           LITERAL_LIKE_ECQL_PATTERN                 = "'aabbcc' like '%bb%'";

    public static final String           LITERAL_NOT_LIKE_ECQL_PATTERN             = "'aabbcc' not like '%bb%'";

    public static final String           LITERAL_BETWEEN_TWO_LITERALS             = "2 between 1 and 3";

    public static final String           LITERAL_BETWEEN_TWO_EXPRESSIONS          = "2 BETWEEN (2-1) AND (2+1)";

    public static final String           FUNCTION_BETWEEN_LITERALS                = "area( the_geom ) BETWEEN 10000 AND 30000";

    public static final String           FUNCTION_BETWEEN_FUNCTIONS               = "area( the_geom ) BETWEEN abs(10000) AND abs(30000)";

    public static final String           FUNCTION_IS_NULL                         = "centroid( the_geom ) IS NULL";

    public static final String           FUNCTION_IS_NOT_NULL                     = "centroid( the_geom ) IS NOT NULL";

    public static final String           EXPRESSIONS_WITH_PROPERTIES              = "(x+4) > (y - 5)";


    /** Maintains the ECQL predicates (input) and the expected filters (output) */
    public static Map<String, Object> SAMPLES = new HashMap<String, Object>();

    static {
        Filter filter;

        // (1+3)
        Add simpleAddExpression = FACTORY.add(FACTORY.literal(1), FACTORY.literal(3));

        //sample "(1+3) > prop1"
        filter = FACTORY.greater(
                            simpleAddExpression,
                            FACTORY.property("aProperty"));

        SAMPLES.put(EXPRESION_GREATER_PROPERTY, filter);

        // abs(10) < aProperty
        Expression[] absArgs = new Expression[1];
        absArgs[0] = FACTORY.literal(10);

        Function abs = FACTORY.function("abs", absArgs);

        filter = FACTORY.less(abs, FACTORY.property("aProperty"));

        SAMPLES.put(ABS_FUNCTION_LESS_PROPERTY, filter);
        
        // area( the_geom ) < 30000
        Expression[] areaArgs = new Expression[1];
        areaArgs[0] = FACTORY.property("the_geom");

        Function area = FACTORY.function("area", areaArgs);

        filter = FACTORY.less(area, FACTORY.literal(30000));
        
        SAMPLES.put(AREA_FUNCTION_LESS_NUMBER, filter);
        
        //area( the_geom ) < (1+3)
        filter = FACTORY.less(area, simpleAddExpression);
        
        SAMPLES.put(FUNCTION_LESS_SIMPLE_ADD_EXPR, filter);
        
        // area( the_geom ) < abs(10)
        filter = FACTORY.less(area, abs);
        
        SAMPLES.put(FUNC_AREA_LESS_FUNC_ABS, filter);
        
        // (1+3) > (4-5)
        Subtract simpleSubtractExpression = FACTORY.subtract(FACTORY.literal(4), FACTORY.literal(5));

        filter = FACTORY.greater(simpleAddExpression, simpleSubtractExpression);
        
        SAMPLES.put(ADD_EXPRESION_GREATER_SUBTRACT_EXPRESION, filter);
        
        //        
        Add plusToProp = FACTORY.add( FACTORY.property("x"), FACTORY.literal(4) );
        Subtract subtractToProp = FACTORY.subtract( FACTORY.property("y"), FACTORY.literal(5) );
        
        filter = FACTORY.greater(plusToProp, subtractToProp);

        SAMPLES.put(EXPRESSIONS_WITH_PROPERTIES, filter);

        // ----------------------------------------------------
        // Expressions with minus value
        // ----------------------------------------------------
        
        //aProperty > -1
        Literal minusOne = FACTORY.literal(-1);
        PropertyName aProperty = FACTORY.property("aProperty");
        
        filter = FACTORY.greater(aProperty, minusOne);
        
        SAMPLES.put(PROPERTY_GREATER_MINUS_INGEGER, filter);

        //"-1 > aProperty"
        filter = FACTORY.greater(minusOne, aProperty);
        
        SAMPLES.put(MINUS_INTEGER_GREATER_PROPERTY,filter);

        //aProperty > -1.05
        Literal minusFloat = FACTORY.literal(-1.05);

        filter = FACTORY.greater(aProperty, minusFloat);
     
        SAMPLES.put(PROPERTY_GREATER_MINUS_FLOAT, filter);

        // -1.05 > aProperty
        filter = FACTORY.greater(minusFloat,  aProperty);
        
        SAMPLES.put(MINUS_FLOAT_GREATER_PROPERTY, filter);
        
        //-1.05 + 4.6 > aProperty";
        Add exprWithMinus = FACTORY.add(FACTORY.literal(-1.05), 
                                        FACTORY.literal(4.6));
        filter = FACTORY.greater(exprWithMinus, aProperty);
        
        SAMPLES.put(MINUS_EXPR_GREATER_PROPERTY,filter);        

        // aProperty > -1.05 + 4.6
        filter = FACTORY.greater( aProperty,  exprWithMinus);
        
        SAMPLES.put(PROPERTY_GREATER_MINUS_EXPR, filter);
        
        //-1.05 + (-4.6* -10) > aPrpoerty
        Add nestedExpr = FACTORY.add(
                                    FACTORY.literal(-1.05), 
                                    FACTORY.multiply(
                                                FACTORY.literal(-4.6), 
                                                FACTORY.literal(-10))); 
        
        filter = FACTORY.greater(nestedExpr, aProperty);
        
        SAMPLES.put(PROPERTY_GREATER_NESTED_EXPR,filter);

        // 10--1.05 > prop1
        Subtract subtractExpr = FACTORY.subtract(FACTORY.literal(10), FACTORY.literal(-1.05));

        filter = FACTORY.greater(subtractExpr, aProperty);
        
        SAMPLES.put(MINUS_MINUS_EXPR_GRATER_PROPERTY,filter); 
        
        // strConcat('aa', 'bbcc') like '%bb%'"
        Expression[] strConcatArgs = new Expression[2];
        strConcatArgs[0] = FACTORY.literal("aa");
        strConcatArgs[1] = FACTORY.literal("bbcc");
        Function strConcat = FACTORY.function("strConcat", strConcatArgs);

        filter = FACTORY.like(strConcat, "%bb%");

        SAMPLES.put( FUNCTION_LIKE_ECQL_PATTERN, filter );
        
        // 'aabbcc' like '%bb%'
        filter = FACTORY.like(FACTORY.literal("aabbcc"), "%bb%");

        SAMPLES.put( LITERAL_LIKE_ECQL_PATTERN, filter );
        
        // 'aabbcc' not like '%bb%'
        filter = FACTORY.not( FACTORY.like(FACTORY.literal("aabbcc"), "%bb%"));

        SAMPLES.put( LITERAL_NOT_LIKE_ECQL_PATTERN, filter );
    
        // ----------------------------------------------------
        // Between samples
        // ----------------------------------------------------
        // 2 between 1 and 3
        filter = 
            FACTORY.between(FACTORY.literal(2), FACTORY.literal(1), FACTORY.literal(3));
        
        SAMPLES.put(LITERAL_BETWEEN_TWO_LITERALS, filter);

        
        // 2 BETWEEN (2-1) AND (2+1)
        filter = FACTORY.between(
                            FACTORY.literal(2), 
                            FACTORY.subtract(FACTORY.literal(2), FACTORY.literal(1)), 
                            FACTORY.add(FACTORY.literal(2), FACTORY.literal(1)));

        SAMPLES.put(LITERAL_BETWEEN_TWO_EXPRESSIONS, filter);

        // area( the_geom ) BETWEEN 10000 AND 30000
        filter = FACTORY.between(
                    area, 
                    FACTORY.literal(10000),  FACTORY.literal(30000));

        SAMPLES.put(FUNCTION_BETWEEN_LITERALS, filter);               
        
        // area( the_geom ) BETWEEN abs(10000) AND abs(30000)
        Expression[] abs1Args = new Expression[1];
        abs1Args[0] = FACTORY.literal(10000);
        Function abs1 = FACTORY.function("abs", abs1Args);

        Expression[] abs2Args = new Expression[1];
        abs2Args[0] = FACTORY.literal(30000);

        Function abs2 = FACTORY.function("abs", abs2Args);

        filter= FACTORY.between(area, abs1, abs2);
        
        SAMPLES.put(FUNCTION_BETWEEN_FUNCTIONS, filter);

        // ----------------------------------------------------
        // IS NULL      
        // ----------------------------------------------------

        //centroid( the_geom ) IS NULL
        Expression[] centroidArgs = new Expression[1];
        centroidArgs[0] = FACTORY.property("the_geom");

        Function centroid = FACTORY.function("centroid", centroidArgs);
        PropertyIsNull isNullFilter = FACTORY.isNull(centroid);

        SAMPLES.put( FUNCTION_IS_NULL,isNullFilter);

        //centroid( the_geom ) IS NOT NULL
        Not notIsNullFilter = FACTORY.not(isNullFilter);
        SAMPLES.put(FUNCTION_IS_NOT_NULL, notIsNullFilter);
    }
    

    /**
     * @param predcateRequested
     * @return the filter expected for the predicate required
     */
    public static Filter getSample(final String predcateRequested) {
        Filter sample = (Filter) SAMPLES.get(predcateRequested);
        assert (sample != null) : "There is not a sample for " + predcateRequested;

        return sample;
    }
}
