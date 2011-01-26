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

import org.opengis.filter.expression.Expression;

public class ClassificationFunctionTest extends FunctionTestSupport {
    
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");
    
    public ClassificationFunctionTest(String testName) {
        super(testName);
    }
    
    public void testDecimalPlaces() throws Exception {
        EqualIntervalFunction eif = (EqualIntervalFunction) ff.function("EqualInterval", Expression.NIL);
        assertEquals(0, eif.decimalPlaces(100.0));
        assertEquals(3, eif.decimalPlaces(25.99312));
        assertEquals(1, eif.decimalPlaces(1.1));
        assertEquals(1, eif.decimalPlaces(0.9));
        assertEquals(1, eif.decimalPlaces(0.1));
        assertEquals(2, eif.decimalPlaces(0.01));
        assertEquals(3, eif.decimalPlaces(0.001));
    }
    
    public void testRound() throws Exception {
        QuantileFunction classifier = (QuantileFunction) ff.function("Quantile", Expression.NIL);
        assertEquals(100.0, classifier.round(100.0, 0), 0);
        assertEquals(1.1, classifier.round(1.12, 1), 0);
        assertEquals(0.35, classifier.round(0.34523, 2), 0);
    }
}
