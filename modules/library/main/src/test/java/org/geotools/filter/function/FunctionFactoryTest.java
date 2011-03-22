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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.FactoryIteratorProvider;
import org.geotools.factory.GeoTools;
import org.geotools.filter.FunctionFactory;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public class FunctionFactoryTest {

    static FactoryIteratorProvider ffIteratorProvider;
    
    @BeforeClass
    public static void setUp() {
        ffIteratorProvider = new FactoryIteratorProvider() {
            
            public <T> Iterator<T> iterator(Class<T> category) {
                
                if (FunctionFactory.class == category) {
                     List l = new ArrayList();
                     l.add(new FunctionFactory( ) {
                        
                        @SuppressWarnings("unchecked")
                        public List<FunctionName> getFunctionNames() {
                            return (List) Arrays.asList(new FunctionNameImpl("foo", 
                                new String[]{"bar", "baz"}));
                        }
                        
                        public Function function(String name, List<Expression> args, Literal fallback) {
                            if ("foo".equals(name)) {
                                return new FunctionImpl() {
                                    @Override
                                    public Object evaluate(Object object, Class context) {
                                        return "theResult";
                                    }
                                };
                            }
                            return null;
                        }
                        
                    });
                    return l.iterator(); 
                }
                return null;
            }
        }; 
        GeoTools.addFactoryIteratorProvider(ffIteratorProvider);
        CommonFactoryFinder.reset();
    }
    
    @AfterClass
    public static void tearDown() {
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
    
    /**
     * GEOT-3841
     * @throws Exception
     */
    @Test
    public void testThreadedFunctionLookup() throws Exception {
        final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        ExecutorService es = Executors.newCachedThreadPool();
        List<Future<Exception>> tests = new ArrayList<Future<Exception>>();
        for (int i = 0; i < 100; i++) {
            Future<Exception> f = es.submit(new Callable<Exception>() {

                public Exception call() throws Exception {
                    try {
                        ff.function("Length", ff.property("."));
                        return null;
                    } catch (Exception e) {
                        return e;
                    }
                }
            });
            tests.add(f);
        }
        for (Future<Exception> future : tests) {
            Exception e = future.get();
            if(e != null) {
                e.printStackTrace();
            }
            assertNull("No exception was expected", e);
        }
        es.shutdown();
    }
}
