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

import static org.junit.Assert.assertEquals;

import org.geotools.api.filter.expression.Function;
import org.geotools.se.v1_1.SETestSupport;
import org.junit.Test;

public class StringPositionBindingTest extends SETestSupport {
    @Test
    public void testParse() throws Exception {
        SEMockData.stringPosition(document, document);
        Function f = (Function) parse();

        assertEquals("4", f.evaluate(null, String.class));
    }
}
