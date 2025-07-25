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

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;
import org.junit.Test;

public class XSNonNegativeIntegerStrategyTest extends TestSchema {
    public void validateValues(String text, Number expected) throws Exception {
        Object value = new BigInteger(text.trim());

        Object result = strategy.parse(element(text, qname), value);

        if (!(result instanceof BigInteger) && result instanceof Number) {
            result = BigInteger.valueOf(((Number) result).longValue());
        }

        assertEquals(integer(expected), integer(result));
    }

    public BigInteger integer(Object value) {
        return value instanceof BigInteger ? (BigInteger) value : BigInteger.valueOf(((Number) value).longValue());
    }

    public Number number(String number) {
        return BigInteger.valueOf(Long.parseLong(number));
    }

    /*
     * Test method for 'org.geotools.xml.strategies.xs.XSNonPositiveIntegerStrategy.parse(Element, Node[], Object)'
     */
    @Test
    public void testNegativeOne() throws Exception {
        try {
            validateValues("-1", number("-1"));
        } catch (IllegalArgumentException e) {
            // yeah!
        }
    }

    @Test
    public void testZero() throws Exception {
        validateValues("0", number("0"));
    }

    @Test
    public void testLargePositiveNumber() throws Exception {
        validateValues("12678967543233", new BigInteger("12678967543233"));
    }

    @Test
    public void testPositiveNumber() throws Exception {
        validateValues("1000", Integer.valueOf("1000"));
    }

    @Override
    protected QName getQName() {
        return XS.NONNEGATIVEINTEGER;
    }
}
