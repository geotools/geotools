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

public class XSLongStrategyTest extends TestSchema {
    /**
     * long has a lexical representation consisting of an optional sign followed by a finite-length
     * sequence of decimal digits (#x30-#x39). If the sign is omitted, "+" is assumed.
     *
     * <p>For example: -1, 0, 12678967543233, +100000.
     */

    /*
     * Test method for 'org.geotools.xml.strategies.xs.XSLongStrategy.parse(Element, Node[], Object)'
     */
    public void testParse() throws Exception {
        validateValues("-1", Long.valueOf(-1));
        validateValues("0", Long.valueOf(0));
        validateValues("12678967543233", Long.valueOf(12678967543233L));
        validateValues("+100000", Long.valueOf(100000));
    }

    protected QName getQName() {
        return XS.LONG;
    }
}
