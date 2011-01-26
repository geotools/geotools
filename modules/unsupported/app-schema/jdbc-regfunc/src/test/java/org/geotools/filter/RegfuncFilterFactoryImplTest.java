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
 */

package org.geotools.filter;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * Tests for {@link RegfuncFilterFactoryImpl}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class RegfuncFilterFactoryImplTest extends TestCase {

    /**
     * Factory under test. Single instance shared between all tests.
     */
    private RegfuncFilterFactoryImpl filterFactory = new RegfuncFilterFactoryImpl();

    /**
     * Name of function created in tests.
     */
    private static final String FUNCTION_NAME = "some_function_name";

    /**
     * Integer literal used in tests.
     */
    private static final Expression INTEGER_LITERAL = new LiteralExpressionImpl(new Integer(3));

    /**
     * Double literal used in tests.
     */
    private static final Expression DOUBLE_LITERAL = new LiteralExpressionImpl(new Double(-27.1));

    /**
     * String literal used in tests.
     */
    private static final Expression STRING_LITERAL = new LiteralExpressionImpl(
            "Some string argument");

    /**
     * Test creation of function with an array of arguments.
     */
    public void testFunctionArray() {
        Expression[] arguments = new Expression[] { INTEGER_LITERAL, STRING_LITERAL,
                DOUBLE_LITERAL, STRING_LITERAL };
        FunctionExpression function = (FunctionExpression) filterFactory.function(FUNCTION_NAME,
                arguments);
        assertEquals(RegisteredFunction.class, function.getClass());
        assertEquals(FUNCTION_NAME, function.getName());
        assertEquals(arguments.length, function.getArgCount());
        assertEquals(arguments.length, function.getParameters().size());
        assertEquals(Arrays.asList(arguments), function.getParameters());
    }

    /**
     * Test creation of function with one argument.
     */
    public void testFunctionOneArgument() {
        FunctionExpression function = (FunctionExpression) filterFactory.function(FUNCTION_NAME,
                DOUBLE_LITERAL);
        assertEquals(RegisteredFunction.class, function.getClass());
        assertEquals(FUNCTION_NAME, function.getName());
        assertEquals(1, function.getArgCount());
        assertEquals(1, function.getParameters().size());
        assertEquals(DOUBLE_LITERAL, function.getParameters().get(0));
    }

    /**
     * Test creation of function with two arguments.
     */
    public void testFunctionTwoArguments() {
        FunctionExpression function = (FunctionExpression) filterFactory.function(FUNCTION_NAME,
                STRING_LITERAL, INTEGER_LITERAL);
        assertEquals(RegisteredFunction.class, function.getClass());
        assertEquals(FUNCTION_NAME, function.getName());
        assertEquals(2, function.getArgCount());
        assertEquals(2, function.getParameters().size());
        assertEquals(STRING_LITERAL, function.getParameters().get(0));
        assertEquals(INTEGER_LITERAL, function.getParameters().get(1));
    }

    /**
     * Test creation of function with three arguments.
     */
    public void testFunctionThreeArguments() {
        FunctionExpression function = (FunctionExpression) filterFactory.function(FUNCTION_NAME,
                STRING_LITERAL, INTEGER_LITERAL, STRING_LITERAL);
        assertEquals(RegisteredFunction.class, function.getClass());
        assertEquals(FUNCTION_NAME, function.getName());
        assertEquals(3, function.getArgCount());
        assertEquals(3, function.getParameters().size());
        assertEquals(STRING_LITERAL, function.getParameters().get(0));
        assertEquals(INTEGER_LITERAL, function.getParameters().get(1));
        assertEquals(STRING_LITERAL, function.getParameters().get(2));
    }

}
