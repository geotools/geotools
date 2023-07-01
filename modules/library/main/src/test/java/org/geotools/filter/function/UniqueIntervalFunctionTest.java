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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.junit.Test;

/**
 * Tests UniqueIntervalFunction
 *
 * @author Cory Horner
 */
public class UniqueIntervalFunctionTest extends FunctionTestSupport {

    /** Test of getName method, of class org.geotools.filter.functions.UniqueIntervalFunction. */
    @Test
    public void testInstance() {
        Function equInt = ff.function("UniqueInterval", ff.literal(featureCollection));
        assertNotNull(equInt);
    }

    /** Test of getName method, of class org.geotools.filter.functions.UniqueIntervalFunction. */
    @Test
    public void testGetName() {
        Function equInt = ff.function("UniqueInterval", ff.literal(featureCollection));
        assertEquals("UniqueInterval", equInt.getName());
    }

    /**
     * Test of setNumberOfClasses method, of class
     * org.geotools.filter.function.UniqueIntervalFunction.
     */
    @Test
    public void testSetClasses() throws Exception {
        Literal classes = ff.literal(3);
        PropertyName exp = ff.property("foo");
        UniqueIntervalFunction func =
                (UniqueIntervalFunction) ff.function("UniqueInterval", exp, classes);
        assertEquals(3, func.getClasses());
        func.setClasses(12);
        assertEquals(12, func.getClasses());
    }

    /** Test of getValue method, of class org.geotools.filter.function.UniqueIntervalFunction. */
    @SuppressWarnings("unchecked")
    @Test
    public void testEvaluate() throws Exception {
        Literal classes = ff.literal(2);
        PropertyName exp = ff.property("foo");
        UniqueIntervalFunction func =
                (UniqueIntervalFunction) ff.function("UniqueInterval", exp, classes);

        Object result = func.evaluate(featureCollection);
        assertTrue(result instanceof ExplicitClassifier);
        ExplicitClassifier classifier = (ExplicitClassifier) result;
        assertEquals(2, classifier.getSize());
        assertEquals(classifier.values[0].size(), classifier.values[1].size());
        assertFalse(classifier.values[0].removeAll(classifier.values[1]));
    }

    @Test
    public void testConstantValuesNumeric() {
        Function function = ff.function("UniqueInterval", ff.property("v"), ff.literal(12));
        ExplicitClassifier classifier = (ExplicitClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals(1, classifier.getValues(0).size());
        assertEquals(123.123, classifier.getValues(0).iterator().next());
    }

    @Test
    public void testConstantValuesString() {
        Function function = ff.function("UniqueInterval", ff.property("s"), ff.literal(12));
        ExplicitClassifier classifier = (ExplicitClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals(1, classifier.getValues(0).size());
        assertEquals("abc", classifier.getValues(0).iterator().next());
    }

    @Test
    public void testEvaluateNumericalWithPercentages() {
        Literal classes = ff.literal(2);
        PropertyName exp = ff.property("foo");
        UniqueIntervalFunction func =
                (UniqueIntervalFunction)
                        ff.function("UniqueInterval", exp, classes, ff.literal(true));

        Object result = func.evaluate(featureCollection);
        assertTrue(result instanceof ExplicitClassifier);
        ExplicitClassifier classifier = (ExplicitClassifier) result;
        double[] percentages = classifier.getPercentages();
        assertEquals(2, percentages.length);
        assertEquals(50.0, percentages[0], 0d);
        assertEquals(50.0, percentages[0], 0d);
    }

    @Test
    public void testEvaluateNotNumericalWithPercentages() {
        Literal classes = ff.literal(2);
        PropertyName exp = ff.property("s");
        UniqueIntervalFunction func =
                (UniqueIntervalFunction)
                        ff.function("UniqueInterval", exp, classes, ff.literal(true));

        Object result = func.evaluate(constantCollection);
        assertTrue(result instanceof ExplicitClassifier);
        ExplicitClassifier classifier = (ExplicitClassifier) result;
        double[] percentages = classifier.getPercentages();
        assertEquals(1, percentages.length);
        assertEquals(100.0, percentages[0], 0d);
    }
}
