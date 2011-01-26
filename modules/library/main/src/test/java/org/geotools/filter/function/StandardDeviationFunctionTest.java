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
package org.geotools.filter.function;

import java.util.logging.Logger;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollections;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 *
 * @author Cory Horner, Refractions Research
 * @source $URL$
 */
public class StandardDeviationFunctionTest extends FunctionTestSupport {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
    "org.geotools.filter");
    
    public StandardDeviationFunctionTest(String testName) {
        super(testName);
    }
    
    protected void tearDown() throws java.lang.Exception {
    }
    
    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(StandardDeviationFunctionTest.class);
        
        return suite;
    }
    
    public void testInstance() {
        Function stdDev = ff.function("StandardDeviation", ff.literal(FeatureCollections.newCollection()));
        assertNotNull(stdDev);
    }
    
    public void testGetName() {
        Function equInt = ff.function("StandardDeviation", ff.literal(FeatureCollections.newCollection()));
        LOGGER.finer("testGetName");
        assertEquals("StandardDeviation",equInt.getName());
    }
    
    public void testSetNumberOfClasses() throws Exception{
        LOGGER.finer("testSetNumberOfClasses");
        
        Literal classes = ff.literal(3);
        PropertyName exp = ff.property("foo");
        StandardDeviationFunction func = (StandardDeviationFunction) ff.function("StandardDeviation", exp, classes);
        assertEquals(3, func.getClasses());
        classes = ff.literal(12);
        func = (StandardDeviationFunction) ff.function("StandardDeviation", exp, classes);
        assertEquals(12, func.getClasses());
    }
    
    public void xTestGetValue() throws Exception{
        //doesn't work yet?
        Literal classes = ff.literal(5);
        PropertyName exp = ff.property("foo");
        Function func = ff.function("StandardDeviation", exp, classes);
        
        SimpleFeatureIterator list = featureCollection.features();
        //feature 1
        SimpleFeature f = list.next();
        int slot = ((Number)func.evaluate(f)).intValue();
        assertEquals(1, slot);
        //feature 2
        f = list.next();
        slot = ((Number)func.evaluate(f)).intValue();
        assertEquals(4, slot);
        //feature 3
        f = list.next();
        slot = ((Number)func.evaluate(f)).intValue();
        assertEquals(2, slot);
        //feature 4
        f = list.next();
        slot = ((Number)func.evaluate(f)).intValue();
        assertEquals(2, slot);
        //feature 5
        f = list.next();
        slot = ((Number)func.evaluate(f)).intValue();
        assertEquals(2, slot);
        //feature 6
        f = list.next();
        slot = ((Number)func.evaluate(f)).intValue();
        assertEquals(3, slot);
        //feature 7
        f = list.next();
        slot = ((Number)func.evaluate(f)).intValue();
        assertEquals(1, slot);
        //feature 8
        f = list.next();
        slot = ((Number)func.evaluate(f)).intValue();
        assertEquals(1, slot);
    }
    
    
}
