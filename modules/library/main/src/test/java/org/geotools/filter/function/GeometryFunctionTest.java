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

import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.filter.expression.Function;

public class GeometryFunctionTest extends FunctionTestSupport {

    public GeometryFunctionTest(String testName) {
        super(testName);
    }

    public void testNull() {
        Function geometry = ff.function("geometry");
        assertNull(geometry.evaluate(null));
    }

    public void testNonFeature() {
        Function geometry = ff.function("geometry");
        assertNull(geometry.evaluate(new Integer(10)));
    }

    public void testSimpleFeature() throws ParseException {
        Function geometry = ff.function("geometry");
        assertEquals(new WKTReader().read("POINT(4 4)"), geometry.evaluate(testFeatures[0]));
    }
}
