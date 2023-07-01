/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.geotools.api.filter.expression.Function;

public class GeometryFunctionTest extends FunctionTestSupport {

    @Test
    public void testNull() {
        Function geometry = ff.function("geometry");
        assertNull(geometry.evaluate(null));
    }

    @Test
    public void testNonFeature() {
        Function geometry = ff.function("geometry");
        assertNull(geometry.evaluate(Integer.valueOf(10)));
    }

    @Test
    public void testSimpleFeature() throws ParseException {
        Function geometry = ff.function("geometry");
        assertEquals(new WKTReader().read("POINT(4 4)"), geometry.evaluate(testFeatures[0]));
    }
}
