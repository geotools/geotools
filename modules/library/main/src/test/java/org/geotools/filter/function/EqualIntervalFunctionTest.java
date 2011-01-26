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
package org.geotools.filter.function;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 *
 * @author James
 * @source $URL$
 */
public class EqualIntervalFunctionTest extends FunctionTestSupport {
   
    
    private static final org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public EqualIntervalFunctionTest(String testName) {
        super(testName);
    }
    
    protected void tearDown() throws java.lang.Exception {
    }
    
    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(EqualIntervalFunctionTest.class);
        return suite;
    }
    
    /**
     * Test of getName method, of class org.geotools.filter.functions.EqualIntervalFunction.
     */
    public void testInstance() {
        Function equInt = ff.function("EqualInterval", org.opengis.filter.expression.Expression.NIL);        
        assertNotNull(equInt);
        assertEquals("test get name", "EqualInterval",equInt.getName());
    }
        
    /**
     * Test of setNumberOfClasses method, of class org.geotools.filter.functions.EqualIntervalFunction.
     */
    public void testSetClasses() throws Exception{        
        PropertyName property = ff.property("foo");
        Literal literal = ff.literal(3);
        
        EqualIntervalFunction func = (EqualIntervalFunction) ff.function("EqualInterval", property, literal);
        assertEquals(3, func.getClasses());
                        
        func.getParameters().set(1, ff.literal(12));
        assertEquals(12, func.getClasses());        
    }
    
    public void testEvaluateWithExpressions() throws Exception {
        Literal classes = ff.literal(3);
        PropertyName name = ff.property("foo");
        Function func = ff.function("EqualInterval", name, classes);
        
        Object classifier = func.evaluate(featureCollection);
        assertTrue(classifier instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) classifier;
        //values = 4,90,20,43,29,61,8,12
        //4..90 = 4..32.67, 32.67..61.33, 61.33..90
        
        //correct number of classes
        assertEquals(3, ranged.getSize());
        //correct titles
        assertEquals("4..32.667", ranged.getTitle(0));
        assertEquals("32.667..61.333", ranged.getTitle(1));
        assertEquals("61.333..90", ranged.getTitle(2));
        //check classifier binning
        assertEquals(0, ranged.classify(new Double(4)));
        assertEquals(2, ranged.classify(name, testFeatures[1])); //90
        assertEquals(0, ranged.classify(new Double(20)));
        assertEquals(1, ranged.classify(new Double(43)));
        assertEquals(0, ranged.classify(new Double(29)));
        assertEquals(1, ranged.classify(new Double(61)));
        assertEquals(0, ranged.classify(name, testFeatures[6])); //8
        assertEquals(0, ranged.classify(new Double(12)));
        
        //try again with foo        
    }

    /** FIXME: Please for the love on binpop */
    public void testEvaulateWithStrings() throws Exception {
        org.opengis.filter.expression.Expression function = ff.function("EqualInterval", ff.property("group"), ff.literal(5)  );
        Classifier classifier = (Classifier) function.evaluate( featureCollection );
        assertNotNull( classifier );

        Classifier classifier2 = (Classifier) function.evaluate( featureCollection, Classifier.class );
        assertNotNull( classifier2 );
        
        Integer number = (Integer) function.evaluate( featureCollection, Integer.class );
        assertNull( number );
    }
    
    public void testUpgradeExample(){
        Function function = ff.function("equalInterval", ff.property("foo"), ff.literal(12));        
        Object value = function.evaluate(featureCollection);
        assertNotNull("classifier failed", value);
        
        Classifier split = (Classifier) value;
        Function classify = ff.function("classify", ff.property("foo"), ff.literal(split));
        
        SimpleFeature victim = testFeatures[2]; //foo = 20
        assertEquals("Feature was placed in wrong bin", new Integer(2), classify.evaluate(victim));
    }
}
