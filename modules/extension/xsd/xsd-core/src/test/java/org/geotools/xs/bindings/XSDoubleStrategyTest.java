/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xs.bindings;

import javax.xml.namespace.QName;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;
import org.geotools.xsd.ElementInstance;

public class XSDoubleStrategyTest extends TestSchema {
    /**
     * For example, -1E4, 1267.43233E12, 12.78e-2, 12 , -0, 0 and INF are all legal literals for
     * double.
     */

    /*
     * Test method for 'org.geotools.xml.strategies.xs.XSDoubleStrategy.parse(Element, Node[], Object)'
     */
    public void testParse() throws Exception {
        validateValues("-1E4", Double.valueOf(-1E4));
        validateValues("1267.43233E12", Double.valueOf(1267.43233E12));
        validateValues("12.78e-2", Double.valueOf(12.78e-2));
        validateValues("12", Double.valueOf(12));
        validateValues("-0", Double.valueOf("-0"));
        validateValues("0", Double.valueOf(0));

        ElementInstance element = element("INF", XS.DOUBLE);
        assertEquals(Double.valueOf(Double.POSITIVE_INFINITY), strategy.parse(element, "INF"));
    }

    public void testIntegerParse() throws Exception {
        ElementInstance element = element("12345", XS.INTEGER);
        assertEquals(Double.valueOf(12345.0), strategy.parse(element, "12345"));
    }

    protected QName getQName() {
        return XS.DOUBLE;
    }
}
