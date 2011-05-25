/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.After;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * @author Andrea Aime
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/test/java/org/geotools/filter/function/EnvFunctionTest.java $
 * @version $Id $
 */

public class EnvFunctionTest {

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @After
    public void tearDown() {
        EnvFunction.clearGlobalValues();
        EnvFunction.clearLocalValues();
    }

    public EnvFunctionTest() {}

    /**
     * Tests the use of two thread-local tables with same var names and different values
     */
    @Test
    public void testSetLocalValues() throws Exception {
        System.out.println("   setLocalValues");

        final String key1 = "foo";
        final String key2 = "bar";

        final Map<String, Object> table0 = new HashMap<String, Object>();
        table0.put(key1, 1);
        table0.put(key2, 2);

        final Map<String, Object> table1 = new HashMap<String, Object>();
        table1.put(key1, 10);
        table1.put(key2, 20);

        final List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>();
        tables.add(table0);
        tables.add(table1);

        final CountDownLatch latch = new CountDownLatch(2);

        class Task implements Runnable {
            private final int threadIndex;

            public Task(int threadIndex) {
                this.threadIndex = threadIndex;
            }

            public void run() {
                // set the local values for this thread and then wait for
                // the other thread to do the same before testing
                EnvFunction.setLocalValues(tables.get(threadIndex));
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    throw new IllegalStateException(ex);
                }

                Map<String, Object> table = tables.get(threadIndex);
                for (String name : table.keySet()) {
                    Object result = ff.function("env", ff.literal(name)).evaluate(null);
                    int value = ((Number) result).intValue();
                    assertEquals(table.get(name), value);
                }
            }
        }

        Future f1 = executor.submit(new Task(0));
        Future f2 = executor.submit(new Task(1));

        // calling get on the Futures ensures that this test method
        // completes before another starts
        f1.get();
        f2.get();
    }

    /**
     * Tests the use of a single var name with two thread-local values
     */
    @Test
    public void testSetLocalValue() throws Exception {
        System.out.println("   setLocalValue");

        final String varName = "foo";
        final int[] values = {1, 2};

        final CountDownLatch latch = new CountDownLatch(2);

        class Task implements Runnable {
            private final int threadIndex;

            public Task(int threadIndex) {
                this.threadIndex = threadIndex;
            }

            public void run() {
                // set the local var then wait for the other thread
                // to do the same before testing
                EnvFunction.setLocalValue(varName, values[threadIndex]);
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    throw new IllegalStateException(ex);
                }

                Object result = ff.function("env", ff.literal(varName)).evaluate(null);
                int value = ((Number) result).intValue();
                assertEquals(values[threadIndex], value);
            }
        }

        Future f1 = executor.submit(new Task(0));
        Future f2 = executor.submit(new Task(1));

        // calling get on the Futures ensures that this test method
        // completes before another starts
        f1.get();
        f2.get();
    }

    /**
     * Tests setting global values and accessing them from different threads
     */
    @Test
    public void testSetGlobalValues() throws Exception {
        System.out.println("   setGlobalValues");

        final Map<String, Object> table = new HashMap<String, Object>();
        table.put("foo", 1);
        table.put("bar", 2);
        EnvFunction.setGlobalValues(table);

        final CountDownLatch latch = new CountDownLatch(2);

        class Task implements Runnable {
            final String key;

            Task(String key) {
                if (!table.containsKey(key)) {
                    throw new IllegalArgumentException("Invalid arg " + key);
                }
                this.key = key;
            }

            public void run() {
                // set the global value assigned to this thread then wait for the other
                // thread to do the same
                EnvFunction.setGlobalValue(key, table.get(key));
                latch.countDown();
                try {
                    latch.await();
                } catch (InterruptedException ex) {
                    throw new IllegalStateException(ex);
                }

                for (String name : table.keySet()) {
                    Object result = ff.function("env", ff.literal(name)).evaluate(null);
                    int value = ((Number) result).intValue();
                    assertEquals(table.get(name), value);
                }
            }
        }

        Future f1 = executor.submit(new Task("foo"));
        Future f2 = executor.submit(new Task("bar"));

        // calling get on the Futures ensures that this test method
        // completes before another starts
        f1.get();
        f2.get();
    }

    /**
     * Tests setting a global value and accessing it from different threads
     */
    @Test
    public void testSetGlobalValue() throws Exception {
        System.out.println("   setGlobalValue");

        final String varName = "foo";
        final String varValue = "a global value";
        EnvFunction.setGlobalValue(varName, varValue);

        class Task implements Runnable {

            public void run() {
                Object result = ff.function("env", ff.literal(varName)).evaluate(null);
                assertEquals(varValue, result.toString());
            }
        }

        Future f1 = executor.submit(new Task());
        Future f2 = executor.submit(new Task());

        // calling get on the Futures ensures that this test method
        // completes before another starts
        f1.get();
        f2.get();
    }

    @Test
    public void testCaseInsensitiveGlobalLookup() {
        System.out.println("   test case-insensitive global lookup");

        final String varName = "foo";
        final String altVarName = "FoO";
        final String varValue = "globalCaseTest";

        EnvFunction.setGlobalValue(varName, varValue);
        Object result = ff.function("env", ff.literal(altVarName)).evaluate(null);
        assertEquals(varValue, result.toString());
    }

    @Test
    public void testCaseInsensitiveLocalLookup() {
        System.out.println("   test case-insensitive local lookup");

        final String varName = "foo";
        final String altVarName = "FoO";
        final String varValue = "localCaseTest";

        EnvFunction.setLocalValue(varName, varValue);
        Object result = ff.function("env", ff.literal(altVarName)).evaluate(null);
        assertEquals(varValue, result.toString());
    }

    @Test
    public void testClearGlobal() {
        System.out.println("   clearGlobalValues");

        final String varName = "foo";
        final String varValue = "clearGlobal";

        EnvFunction.setGlobalValue(varName, varValue);
        EnvFunction.clearGlobalValues();
        Object result = ff.function("env", ff.literal(varName)).evaluate(null);
        assertNull(result);
    }

    @Test
    public void testClearLocal() {
        System.out.println("   clearLocalValues");

        final String varName = "foo";
        final String varValue = "clearLocal";

        EnvFunction.setLocalValue(varName, varValue);
        EnvFunction.clearLocalValues();
        Object result = ff.function("env", ff.literal(varName)).evaluate(null);
        assertNull(result);
    }

    @Test
    public void testGetArgCount() {
        System.out.println("   getArgCount");
        EnvFunction fn = new EnvFunction();
        assertEquals(1, fn.getArgCount());
    }

    @Test
    public void testLiteralDefaultValue() {
        System.out.println("   literal default value");

        int defaultValue = 42;

        Object result = ff.function("env", ff.literal("doesnotexist"), ff.literal(defaultValue)).evaluate(null);
        int value = ((Number) result).intValue();
        assertEquals(defaultValue, value);
    }

    @Test
    public void testNonLiteralDefaultValue() {
        System.out.println("   non-literal default value");

        int x = 21;
        Expression defaultExpr = ff.add(ff.literal(x), ff.literal(x));

        Object result = ff.function("env", ff.literal("doesnotexist"), defaultExpr).evaluate(null);
        int value = ((Number) result).intValue();
        assertEquals(x + x, value);
    }

    /**
     * The setFallback method should log a warning and ignore 
     * the argument.
     */
    @Test
    public void testSetFallbackNotAllowed() {
        Logger logger = Logger.getLogger(EnvFunction.class.getName());

        Formatter formatter = new SimpleFormatter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Handler handler = new StreamHandler(out, formatter);
        logger.addHandler(handler);

        try {
            EnvFunction function = new EnvFunction();
            function.setFallbackValue(ff.literal(0));

            handler.flush();
            String logMsg = out.toString();
            assertNotNull(logMsg);
            assertTrue(logMsg.toLowerCase().contains("setfallbackvalue"));

        } finally {
            logger.removeHandler(handler);
        }
    }
}
