/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.expression;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.mbstyle.parse.MBFormatException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

/** Tests the "is-supported-script" expression: true when the JVM fonts can display every character. */
public class MBIsSupportedScriptTest {

    private final JSONParser parser = new JSONParser();

    private Boolean eval(String text) throws Exception {
        JSONArray array = (JSONArray) parser.parse("[\"is-supported-script\", [\"get\", \"name\"]]");
        Expression expression = MBExpression.transformExpression(array);
        SimpleFeatureType type = DataUtilities.createType("iss.test", "name:String");
        SimpleFeature feature = SimpleFeatureBuilder.build(type, new Object[] {text}, "iss.1");
        return expression.evaluate(feature, Boolean.class);
    }

    @Test
    public void testLatinSupported() throws Exception {
        assertTrue(eval("Roma"));
    }

    @Test
    public void testEmptySupported() throws Exception {
        assertTrue(eval(""));
    }

    @Test
    public void testUnassignedCodePointNotSupported() throws Exception {
        // U+0378 is a permanently unassigned code point: no font provides a glyph for it
        assertFalse(eval("A\u0378B"));
    }

    @Test
    public void testMissingValueSupported() throws Exception {
        assertTrue(eval(null));
    }

    @Test
    public void testWrongArgumentCount() throws Exception {
        JSONArray array = (JSONArray) parser.parse("[\"is-supported-script\"]");
        MBFormatException e = assertThrows(MBFormatException.class, () -> MBExpression.transformExpression(array));
        assertThat(e.getMessage(), containsString("\"is-supported-script\" should have exactly 1 argument(s)"));
    }
}
