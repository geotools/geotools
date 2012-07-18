package org.geotools.filter.function;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.geotools.data.DataTestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionFinder;
import org.junit.After;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

/**
 * Tests the property function
 * 
 * @author Andrea Aime
 */
public class FilterFunctionPropertyTest extends DataTestCase {

    private static final int LOOPS = 5000;
    static FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);

    public FilterFunctionPropertyTest() {
        super(FilterFunctionPropertyTest.class.getName());
    }
    
    @After
    public void teardown() {
        EnvFunction.clearLocalValues();
    }

    @Test
    public void testLocateFunction() {
        Function f = new FunctionFinder(null).findFunction("property");
        assertNotNull(f);
        assertTrue(f instanceof FilterFunction_property);
    }

    @Test
    public void testEvaluateProperty() {
        Function f = FF.function("property", FF.literal("name"));
        String result = f.evaluate(roadFeatures[0], String.class);
        assertEquals("r1", result);
    }
    
    @Test
    public void testEvaluateAndConvert() {
        Function f = FF.function("property", FF.literal("geom"));
        String result = f.evaluate(roadFeatures[0], String.class);
        assertEquals("LINESTRING (1 1, 2 2, 4 2, 5 1)", result);
    }
    
    @Test
    public void testEvaluateAlternate() {
        Function f = FF.function("property", FF.function("env", FF.literal("pname")));
        
        EnvFunction.setLocalValue("pname", "name");
        String result = f.evaluate(roadFeatures[0], String.class);
        assertEquals("r1", result);
        
        EnvFunction.setLocalValue("pname", "geom");
        result = f.evaluate(roadFeatures[0], String.class);
        assertEquals("LINESTRING (1 1, 2 2, 4 2, 5 1)", result);
    }
 
    @Test
    public void testEvaluateMultithreaded() throws Exception {
        // we add this one since the property caches the last PropertyName used as 
        // an optimization
        final Function f = FF.function("property", FF.function("env", FF.literal("pname")));
        Callable<Void> nameEvaluator = new Callable<Void>() {

            public Void call() throws Exception {
                try {
                    EnvFunction.setLocalValue("pname", "name");
                    
                    for (int i = 0; i < 1000; i++) {
                        String result = f.evaluate(roadFeatures[0], String.class);
                        assertEquals("r1", result);
                    }
                    
                } finally {
                    EnvFunction.clearLocalValues();
                }
                
                return null;
            }
        };
        
        Callable<Void> geomEvaluator = new Callable<Void>() {

            public Void call() throws Exception {
                try {
                    EnvFunction.setLocalValue("pname", "geom");
                    
                    for (int i = 0; i < LOOPS; i++) {
                        String result = f.evaluate(roadFeatures[0], String.class);
                        assertEquals("LINESTRING (1 1, 2 2, 4 2, 5 1)", result);
                    }
                    
                } finally {
                    EnvFunction.clearLocalValues();
                }
                
                return null;
            }
        };
        
        ExecutorService es = Executors.newCachedThreadPool();
        try {
            Future<Void> fname = es.submit(nameEvaluator);
            Future<Void> fgeom = es.submit(geomEvaluator);
            
            fname.get();
            fgeom.get();
        } finally {
            es.shutdown();
        }
        
        
    }

}
