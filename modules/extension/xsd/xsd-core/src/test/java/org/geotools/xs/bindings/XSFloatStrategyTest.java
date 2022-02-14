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

import javax.xml.namespace.QName;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;
import org.junit.Test;

public class XSFloatStrategyTest extends TestSchema {
    public void validateValues(String given, Float expected) throws Exception {
        Float actual = (Float) strategy.parse(element(given, qname), given);
        assertEquals(given, expected, actual);
    }

    /**
     * For example, -1E4, 1267.43233E12, 12.78e-2, 12 , -0, 0 and INF are all legal literals for
     * float.
     */
    @Test
    public void testWhiteSpace() throws Exception {
        validateValues(" \n12", Float.valueOf(12));
    }

    @Test
    public void testParse() throws Exception {
        validateValues("-1E4", Float.valueOf(-1E4f));
        validateValues("1267.43233E12", Float.valueOf(1267.43233E12f));
        validateValues("12.78e-2", Float.valueOf(12.78e-2f));
        validateValues("12", Float.valueOf(12));
        validateValues("-0", Float.valueOf("-0"));
        validateValues("0", Float.valueOf(0));
        validateValues("INF", Float.valueOf(Float.POSITIVE_INFINITY));
    }

    @Override
    protected QName getQName() {
        return XS.FLOAT;
    }

    /**
     * GEOT-7072: Non-comformant WFS implementations tend to send empty elements (e.g. {@code
     * <value></value>})
     */
    @Test
    public void testParseEmptyStringAsNull() throws Exception {
        validateValues("", null);
        validateValues("\t", null);
    }
}
