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

import java.util.logging.Logger;
import org.geotools.api.filter.expression.Expression;
import org.junit.Test;

public class ClassificationFunctionTest extends FunctionTestSupport {

    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ClassificationFunctionTest.class);

    @Test
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

    @Test
    public void testRound() throws Exception {
        QuantileFunction classifier = (QuantileFunction) ff.function("Quantile", Expression.NIL);
        assertEquals(100.0, classifier.round(100.0, 0), 0);
        assertEquals(1.1, classifier.round(1.12, 1), 0);
        assertEquals(0.35, classifier.round(0.34523, 2), 0);
    }

    @Test
    public void testRoundOverflow() throws Exception {
        EqualIntervalFunction eif = (EqualIntervalFunction) ff.function("EqualInterval", Expression.NIL);
        assertEquals(1477946338495.3d, eif.round(1477946338495.25d, 1), 0d);
    }
}
