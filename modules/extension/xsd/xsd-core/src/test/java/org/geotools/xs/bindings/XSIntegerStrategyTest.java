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

import java.math.BigInteger;
import javax.xml.namespace.QName;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;


public class XSIntegerStrategyTest extends TestSchema {
    /**
     * integer has a lexical representation consisting of a finite-length
     * sequence of decimal digits (#x30-#x39) with an optional leading sign.
     * If the sign is omitted, "+" is assumed.
     *
     * For example: -1, 0, 12678967543233, +100000.
     */
    public void testParse() throws Exception {
        validateValues("-1", new BigInteger("-1"));
        validateValues("0", new BigInteger("0"));
        validateValues("12678967543233", new BigInteger("12678967543233"));
        validateValues("+100000", new BigInteger("100000"));
    }

    protected QName getQName() {
        return XS.INTEGER;
    }
}
