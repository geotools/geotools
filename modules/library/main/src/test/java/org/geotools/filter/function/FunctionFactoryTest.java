/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.factory.FactoryIteratorProvider;
import org.geotools.util.factory.GeoTools;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FunctionFactoryTest {

    static FactoryIteratorProvider ffIteratorProvider;

    @BeforeClass
    public static void setUpClass() {
        ffIteratorProvider = new FactoryIteratorProvider() {

            @Override
            public <T> Iterator<T> iterator(Class<T> category) {

                if (FunctionFactory.class == category) {
                    List<FunctionFactory> l = new ArrayList<>();
                    l.add(new FunctionFactory() {

                        @Override
                        public List<FunctionName> getFunctionNames() {
                            return Arrays.asList(new FunctionNameImpl("foo", new String[] {"bar", "baz"}));
                        }

                        @Override
                        public Function function(String name, List<Expression> args, Literal fallback) {
                            return function(new NameImpl(name), args, fallback);
                        }

                        @Override
                        public Function function(Name name, List<Expression> args, Literal fallback) {
                            if ("foo".equals(name.getLocalPart())) {
                                return new FunctionImpl() {
                                    @Override
                                    public <T> T evaluate(Object object, Class<T> context) {
                                        return context.cast("theResult");
                                    }
                                };
                            }
                            return null;
                        }
                    });
                    @SuppressWarnings("unchecked")
                    Iterator<T> cast = (Iterator<T>) l.iterator();
                    return cast;
                }
                return null;
            }
        };
        GeoTools.addFactoryIteratorProvider(ffIteratorProvider);
        CommonFactoryFinder.reset();
    }

    @AfterClass
    public static void tearDownClass() {
        GeoTools.removeFactoryIteratorProvider(ffIteratorProvider);
    }

    @Test
    public void testLookup() {
        Set<FunctionFactory> factories = CommonFactoryFinder.getFunctionFactories(null);
        FunctionFactory factory = null;

        for (FunctionFactory ff : factories) {
            for (FunctionName fn : ff.getFunctionNames()) {
                if ("foo".equals(fn.getName())) {
                    factory = ff;
                    break;
                }
            }
        }

        assertNotNull(factory);
        Function f = factory.function("foo", null, null);
        assertNotNull(f);
    }

    /** GEOT-3841 */
    @Test
    public void testThreadedFunctionLookup() throws Exception {
        final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        ExecutorService es = Executors.newCachedThreadPool();
        List<Future<Exception>> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Future<Exception> f = es.submit(() -> {
                try {
                    ff.function("Length", ff.property("."));
                    return null;
                } catch (Exception e) {
                    return e;
                }
            });
            tests.add(f);
        }
        for (Future<Exception> future : tests) {
            Exception e = future.get();
            if (e != null) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            }
            assertNull("No exception was expected", e);
        }
        es.shutdown();
    }
}
