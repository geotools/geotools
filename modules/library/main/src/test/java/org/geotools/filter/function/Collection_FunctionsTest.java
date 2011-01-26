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
 *
 *    Created on May 11, 2005, 9:21 PM
 */
package org.geotools.filter.function;

import java.util.HashSet;

import org.geotools.feature.FeatureCollections;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;

/**
 * Do aggregate functions actually work?
 * 
 * @author Cory Horner, Refractions Research
 * @source $URL$
 */
public class Collection_FunctionsTest extends FunctionTestSupport{
    
    /** Creates a new instance of Collection_MinFunctionTest */
    public Collection_FunctionsTest(String testName) {
        super(testName);
    }
    
    
    public void testInstance() {
        Function cmin = ff.function("Collection_Min", ff.literal(FeatureCollections.newCollection()));
        assertNotNull(cmin);
    }
    
    public void testAverage() throws Exception {
    	performNumberTest("Collection_Average", new Double(33.375));   
    }

    public void testCount() throws Exception {
    	performNumberTest("Collection_Count", new Integer(8));   
    }

    public void testMin() throws Exception {
    	performNumberTest("Collection_Min", new Integer(4));   
    }

    public void testMedian() throws Exception {
    	performNumberTest("Collection_Median", new Double(24.5));   
    }

    public void testMax() throws Exception {
    	performNumberTest("Collection_Max", new Integer(90));  
    }

    public void testSum() throws Exception {
    	performNumberTest("Collection_Sum", new Integer(267));   
    }

    public void testUnique() throws Exception {
    	HashSet result = new HashSet(8);
    	result.add(new Integer(90));
    	result.add(new Integer(4));
    	result.add(new Integer(8));
    	result.add(new Integer(43));
    	result.add(new Integer(61));
    	result.add(new Integer(20));
    	result.add(new Integer(29));
    	result.add(new Integer(12));
    	performObjectTest("Collection_Unique", result);   
    }

    
     /**
     * Tests a function class of org.geotools.filter.function.Collection_*Function
     * 
     * Example: performTest("Collection_Min", 4);
     */
    public void performNumberTest(String functionName, Object expectedValue) throws Exception{
        PropertyName exp = ff.property("foo");
        Function func = ff.function(functionName, exp);
        Object obj = func.evaluate(featureCollection);
        Number result = (Number) obj;
        Number expected = (Number) expectedValue;
        assertEquals(expected.doubleValue(), result.doubleValue(), 0);
    }
    
    public void performObjectTest(String functionName, Object expectedValue) throws Exception{
        PropertyName exp = ff.property("foo");
        Function func = ff.function(functionName, exp);
        Object result = func.evaluate(featureCollection);
        assertEquals(expectedValue, result);
    }
    
    
}
