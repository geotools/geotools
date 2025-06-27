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

public class XSNegativeIntegerStrategyTest extends TestSchema {
    /**
     * negativeInteger has a lexical representation consisting of a negative sign ("-") followed by a finite-length
     * sequence of decimal digits (#x30-#x39).
     *
     * <p>For example: -1, -12678967543233, -100000.
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
        return value instanceof BigInteger ? (BigInteger) value : BigInteger.valueOf(((Number) value).longValue());
    }

    public Number number(String number) {
        return BigInteger.valueOf(Long.parseLong(number));
    }

    /*
     * Test method for 'org.geotools.xs.strategies.XSNegativeIntegerStrategy.parse(Element, Node[], Object)'
     */
    @Test
    public void testParse() throws Exception {
        validateValues("-1", new BigInteger("-1"));
        validateValues("-12678967543233", new BigInteger("-12678967543233"));
        validateValues("-100000", new BigInteger("-100000"));
    }

    @Override
    protected QName getQName() {
        return XS.NEGATIVEINTEGER;
    }
}
