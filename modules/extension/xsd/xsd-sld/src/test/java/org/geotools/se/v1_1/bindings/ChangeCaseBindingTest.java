/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import org.geotools.se.v1_1.SETestSupport;
import org.opengis.filter.expression.Function;

public class ChangeCaseBindingTest extends SETestSupport {

    public void testParseLower() throws Exception {
        SEMockData.changeCase(document, document, true);
        Function f = (Function) parse();
        assertEquals("hello", f.evaluate(null, String.class));
    }

    public void testParseUpper() throws Exception {
        SEMockData.changeCase(document, document, false);
        Function f = (Function) parse();
        assertEquals("HELLO", f.evaluate(null, String.class));
    }
}
