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
package org.geotools.filter;

import static org.junit.Assert.assertFalse;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.measure.Unit;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.Collection_AverageFunction;
import org.geotools.filter.function.Collection_BoundsFunction;
import org.geotools.filter.function.Collection_CountFunction;
import org.geotools.filter.function.Collection_MaxFunction;
import org.geotools.filter.function.Collection_MedianFunction;
import org.geotools.filter.function.Collection_MinFunction;
import org.geotools.filter.function.Collection_NearestFunction;
import org.geotools.filter.function.Collection_SumFunction;
import org.geotools.filter.function.Collection_UniqueFunction;
import org.geotools.filter.function.DefaultFunctionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.parameter.Parameter;

/**
 * Test that all functions in the DefaultFunctionFactory have the toString() method implemented.
 *
 * @author Rob Ward
 * @source $URL$
 */
public class FunctionToStringTest {
    static org.opengis.filter.FilterFactory ff;

    private DefaultFunctionFactory functionFactory;

    /** The collection function list. */
    private static List<String> collectionFunctionList =
            Arrays.asList(
                    Collection_AverageFunction.NAME.getName(),
                    Collection_BoundsFunction.NAME.getName(),
                    Collection_CountFunction.NAME.getName(),
                    Collection_MaxFunction.NAME.getName(),
                    Collection_MedianFunction.NAME.getName(),
                    Collection_MinFunction.NAME.getName(),
                    Collection_NearestFunction.NAME.getName(),
                    Collection_SumFunction.NAME.getName(),
                    Collection_UniqueFunction.NAME.getName());

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ff = null;
    }

    /**
     * Create an instance of every function using default parameters and call toString(). Check to
     * see if the implemented toString() is called, should not contain '@' symbol.
     */
    @Test
    public void testFunctionToStringMethod() {
        functionFactory = new DefaultFunctionFactory();

        // Get list of all support functions
        List<FunctionName> functionNameList = functionFactory.getFunctionNames();

        boolean fail = false;

        for (FunctionName functionName : functionNameList) {
            try {
                // Create a function expression with default parameters
                Expression expression = createExpression(functionName);
                if (expression != null) {
                    String result = expression.toString();

                    if (result.contains("@")) {
                        System.err.println(functionName.getName() + "\t\t\t" + "TOSTRING MISSING");
                    }
                } else {
                    fail = true;
                    System.err.println(functionName.getName() + "\t\t\t" + "TOSTRING FAIL");
                }
            } catch (Exception e) {
                fail = true;
                System.err.println(functionName.getName() + "\t\t\t" + "TOSTRING FAIL");
            }
        }

        assertFalse(fail);
    }

    /**
     * Creates the function expression with default parameters.
     *
     * @param functionName the function name
     * @return the expression
     */
    private Expression createExpression(FunctionName functionName) {
        if (functionName == null) {
            return null;
        }

        List<Expression> parameters = new ArrayList<Expression>();
        Literal fallback = null;

        // Retrieve default parameters for function
        createNewFunctionParameters(functionName, parameters);

        // Create function expression with supplied parameters
        Function function =
                functionFactory.function(functionName.getFunctionName(), parameters, fallback);

        return function;
    }

    /**
     * Creates the default parameters for the new function.
     *
     * <p>Some function need have specific types or number of parameters before toString() works.
     *
     * @param functionName the function name
     * @param parameters the parameters
     */
    private static void createNewFunctionParameters(
            FunctionName functionName, List<Expression> parameters) {
        String name = functionName.getName();

        if (collectionFunctionList.contains(name)) {
            parameters.add(ff.property("geom"));
        } else if (name.compareToIgnoreCase(CategorizeFunction.NAME.getName()) == 0) {
            // CategorizeFunction needs all the fields populated
            for (int index = 0; index < functionName.getArguments().size() - 1; index++) {
                parameters.add(index, ff.literal(""));
            }

            parameters.remove(parameters.size() - 1);
            parameters.add(ff.literal(CategorizeFunction.PRECEDING));
        } else {
            List<Parameter<?>> functionParamList = functionName.getArguments();

            for (int paramIndex = 0; paramIndex < functionParamList.size(); paramIndex++) {
                Parameter<?> param = functionParamList.get(paramIndex);

                Class<?> type = param.getType();
                if (type == Object.class) {
                    parameters.add(ff.literal(""));
                } else if (type == String.class) {
                    parameters.add(ff.literal(""));
                } else if ((type == Number.class) || (type == Double.class)) {
                    parameters.add(ff.literal(0.0));
                } else if (type == Float.class) {
                    parameters.add(ff.literal(0.0f));
                } else if ((type == Integer.class) || (type == Long.class)) {
                    parameters.add(ff.literal(0));
                } else if (type == Boolean.class) {
                    parameters.add(ff.literal(false));
                } else if (type == Unit.class) {
                    parameters.add(null);
                } else if (type == Color.class) {
                    parameters.add(null);
                } else if (type == Geometry.class) {
                    parameters.add(null);
                } else if (type == org.opengis.geometry.Geometry.class) {
                    parameters.add(null);
                } else if (type == LineString.class) {
                    parameters.add(null);
                } else if (type == Classifier.class) {
                    parameters.add(null);
                } else if (type == Class.class) {
                    parameters.add(null);
                } else if (type.getName()
                                .compareToIgnoreCase(
                                        "org.geotools.filter.function.color.AbstractHSLFunction$Method")
                        == 0) {
                    parameters.add(null);
                } else if (type.getName()
                                .compareToIgnoreCase("org.geotools.styling.visitor.RescalingMode")
                        == 0) {
                    parameters.add(ff.literal(0));
                } else {
                    Object newObj = null;
                    try {
                        newObj = type.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    parameters.add(ff.literal(newObj));
                }
            }
        }
    }
}
