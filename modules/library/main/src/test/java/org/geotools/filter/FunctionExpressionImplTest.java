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
package org.geotools.filter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class FunctionExpressionImplTest extends TestCase {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FunctionExpressionImplTest.class.getPackage().getName());

    FunctionExpressionImpl function;

    TestExpressionVisitor testVisitor;

    public void setUp() throws Exception {
        super.setUp();
        function = new FunctionExpressionImpl("testFunction") {
            public int getArgCount() {
                return 1;
            }
        };

        testVisitor = new TestExpressionVisitor();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        function = null;
        testVisitor = null;
    }

    public void testAcceptExpressionVisitor() {
        Object extraData = new Object();

        function.accept(testVisitor, extraData);

        final Object[] expected = { Boolean.TRUE, extraData };
        final Object[] actual = testVisitor.functionVisited;

        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    public void testGetType() {
        assertEquals(ExpressionType.FUNCTION, function.getType());
    }

    public void testGetName() {
        function.setName("testFunction");
        assertEquals("testFunction", function.getName());
    }

    public void testSetName() {
        function.setName("testFunction");
        // do not try this at home
        assertEquals("testFunction", function.name);
    }

    public void testGetParameters() {
        final List expected = Collections
                .singletonList(new LiteralExpressionImpl(10d));
        // do not try this at home
        function.params = expected;
        assertEquals(expected, function.getParameters());
    }

    public void testSetParameters() {
        final List expected = Collections
                .singletonList(new LiteralExpressionImpl(10d));
        // do not try this at home
        function.setParameters(expected);
        assertEquals(expected, function.params);
    }

    public void testGetArgs() {
        final List expected = Collections
                .singletonList(new LiteralExpressionImpl(10d));
        function.setParameters(expected);
        Expression[] args = function.getArgs();
        List actual = Arrays.asList(args);
        assertEquals(expected, actual);
    }

    public void testSetArgs() {
        final List expected = Collections
                .singletonList(new LiteralExpressionImpl(10d));
        org.geotools.filter.Expression[] args = (org.geotools.filter.Expression[]) expected.toArray(new org.geotools.filter.Expression[1]);
        function.setArgs(args);
        assertEquals(expected, function.params);
    }

    public void testGetArgCount() {
        final List expected = Collections
                .singletonList(new LiteralExpressionImpl(10d));
        function.setParameters(expected);
        assertEquals(1, function.getArgCount());
    }

    public void testGetImplementationHints() {
        assertNotNull(function.getImplementationHints());
        assertTrue(function.getImplementationHints().isEmpty());
    }

    public void testImplementations() throws IOException,
            ClassNotFoundException, InstantiationException,
            IllegalAccessException {

        List functionClasses = loadFunctionClasses();

        List errors = new LinkedList();
        for (Iterator it = functionClasses.iterator(); it.hasNext();) {
            Class functionClass = (Class) it.next();
            Function function = (Function) functionClass.newInstance();
            testFunction(function, errors);
        }
        if (errors.size() > 0) {
            String errorsMessage = buildErrosMessage(errors);
            LOGGER.info(errorsMessage);
            fail(errorsMessage);
        }
    }

    /**
     * @param errors
     *            List&lt;Exception&gt
     * @return a formatted error message
     */
    private String buildErrosMessage(List errors) {
        StringBuffer sb = new StringBuffer(
                "Some function expression implementations violates contract:\n");
        int errorCount = 1;
        for (Iterator it = errors.iterator(); it.hasNext();) {
            String error = (String) it.next();
            sb.append(errorCount++ + " - ");
            sb.append(error);
            sb.append("\n");
        }
        return sb.toString();
    }

    private void testFunction(Function function, List errors)
            throws InstantiationException, IllegalAccessException {
        final String functionClass = function.getClass().getName();

        if (null == function.getName()) {
            errors.add(functionClass + ": getName() == null");
        }

        testVisitor = new TestExpressionVisitor();
        Object extraData = "[extraData correctly returned]";
        function.accept(testVisitor, extraData);
        Object[] functionVisited = testVisitor.functionVisited;
        if (Boolean.TRUE != functionVisited[0]
                || extraData != functionVisited[1]) {
            errors
                    .add(functionClass
                            + ": accept didn't called visitor.visit(Function, extraData): "
                            + " visited: " + functionVisited[0]
                            + ", extraData: " + functionVisited[1]);
        }

        try {
            String toString = function.toString();
        } catch (Exception e) {
            addExceptionError(errors, functionClass, "toString", e);
        }

        if (function instanceof FunctionExpression) {
            testDeprecatedMethods((FunctionExpression) function, errors);
        }
        
        List<Expression> parameters = function.getParameters();
        if(parameters == null){
            errors.add(functionClass + ".getParameters() returns null");
        }
    }

    private void addExceptionError(List errors, final String functionClass,
            final String method, Exception e) {
        /*
         * StringWriter stringWriter = new StringWriter(); e.printStackTrace(new
         * PrintWriter(stringWriter));
         * 
         * errors.add(functionClass + "." + method + "() throwed an exception: " +
         * stringWriter.getBuffer());
         */
        errors.add(functionClass + "." + method + "() throwed an exception: "
                + e.getMessage());
    }

    private void testDeprecatedMethods(FunctionExpression function, List errors)
            throws InstantiationException, IllegalAccessException {
        final String functionClass = function.getClass().getName();
        int argCount = function.getArgCount();

        final org.geotools.filter.Expression[] expected = new org.geotools.filter.Expression[argCount];
        for (int i = 0; i < argCount; i++) {
            org.geotools.filter.Expression ex = new AttributeExpressionImpl("attName");
            expected[i] = ex;
        }

        final List expectedList = Arrays.asList(expected);
        try {
            function.setArgs(expected);
        } catch (Exception e) {
            addExceptionError(errors, functionClass, "setArgs", e);
        }

        List returnedParams = function.getParameters();
        if (returnedParams == null) {
            errors
                    .add(functionClass
                            + ".getParameters() returned null when parameters were set through setArgs(Expression[])");
        } else if (!expectedList.equals(returnedParams)) {
            errors
                    .add(functionClass
                            + ".getParameters() returned a wrong result when parameters were set through setArgs(Expression[])");
        }

        function = (FunctionExpression) function.getClass().newInstance();
        function.setParameters(expectedList);

        Expression[] returnedArgs = function.getArgs();
        if (returnedArgs == null) {
            errors
                    .add(functionClass
                            + ".getArgs() returns null then arguments set through setParameters()");
        } else {
            returnedParams = Arrays.asList(expected);

            if (!expectedList.equals(returnedParams)) {
                errors.add(functionClass
                        + ".getArgs() incompatible with getParameters()");
            }
        }

        if (ExpressionType.FUNCTION != function.getType()) {
            errors
                    .add(functionClass + ".getType != "
                            + ExpressionType.FUNCTION);
        }
    }

    private List loadFunctionClasses() throws IOException,
            ClassNotFoundException {
        final String spiDefinitionResource = "/META-INF/services/org.opengis.filter.expression.Function";
        InputStream in = getClass().getResourceAsStream(spiDefinitionResource);
        if (in == null) {
            throw new FileNotFoundException(spiDefinitionResource);
        }

        List functionClasses = new LinkedList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String className;
        while ((className = reader.readLine()) != null) {
            Object functionClazz = Class.forName(className);
            functionClasses.add(functionClazz);
        }
        return functionClasses;
    }

    /**
     * An ExpressionVisitor for function expressions test purposes that stores
     * the visited status in a public field
     * 
     * @author Gabriel Roldan, Axios Engineering
     * 
     */
    private static class TestExpressionVisitor implements ExpressionVisitor {
        public Object[] functionVisited = { Boolean.FALSE, null };

        public Object visit(Function expression, Object extraData) {
            functionVisited[0] = Boolean.TRUE;
            functionVisited[1] = extraData;
            return null;
        }

        public Object visit(Add expression, Object extraData) {
            return null;
        }

        public Object visit(Divide expression, Object extraData) {
            return null;
        }

        public Object visit(Literal expression, Object extraData) {
            return null;
        }

        public Object visit(Multiply expression, Object extraData) {
            return null;
        }

        public Object visit(PropertyName expression, Object extraData) {
            return null;
        }

        public Object visit(Subtract expression, Object extraData) {
            return null;
        }

        public Object visit(NilExpression arg0, Object arg1) {
            return null;
        }
    }
}
