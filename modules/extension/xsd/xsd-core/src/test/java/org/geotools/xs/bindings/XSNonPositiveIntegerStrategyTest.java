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

public class XSNonPositiveIntegerStrategyTest extends TestSchema {
    /**
     * nonPositiveInteger has a lexical representation consisting of an optional preceding sign
     * followed by a finite-length sequence of decimal digits (#x30-#x39). The sign may be "+" or
     * may be omitted only for lexical forms denoting zero; in all other lexical forms, the negative
     * sign ("-") must be present.
     *
     * <p>For example: -1, 0, -12678967543233, -100000.
     */
    public void validateValues(String text, Number expected) throws Exception {
        Object value = new BigInteger(text.trim());

        Object result = strategy.parse(element(text, qname), value);

        if (!(result instanceof BigInteger) && result instanceof Number) {
            result = BigInteger.valueOf(((Number) result).longValue());
        }

        assertEquals(integer(expected), integer(result));
    }

    public BigInteger integer(Object value) {
        return (value instanceof BigInteger)
                ? ((BigInteger) value)
                : BigInteger.valueOf(((Number) value).longValue());
    }

    public Number number(String number) {
        return BigInteger.valueOf(Integer.valueOf(number).longValue());
    }

    /*
     * Test method for 'org.geotools.xml.strategies.xs.XSNonPositiveIntegerStrategy.parse(Element, Node[], Object)'
     */
    public void testNegativeOne() throws Exception {
        validateValues("-1", number("-1"));
    }

    public void testZero() throws Exception {
        validateValues("0", number("0"));
    }

    public void testLargePositiveNumber() throws Exception {
        try {
            validateValues("-12678967543233", new BigInteger("-12678967543233"));
        } catch (IllegalArgumentException expected) {
            // yeah!
        }
    }

    public void testNegativeNumber() throws Exception {
        validateValues("-100000", Integer.valueOf("-100000"));
    }

    protected QName getQName() {
        return XS.NONPOSITIVEINTEGER;
    }
}
