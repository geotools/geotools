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
package org.geotools.mbstyle.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.mbstyle.expression.MBExpression;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the "in" membership operator in both its expression form (["in", input, ["literal", [values]]]) and its use as
 * a layer filter.
 */
public class MBInTest {

    private final JSONParser parser = new JSONParser();
    private SimpleFeatureType type;

    @Before
    public void setUp() throws Exception {
        type = DataUtilities.createType("in.test", "kind:String,rank:Integer");
    }

    private JSONArray array(String json) throws Exception {
        return (JSONArray) parser.parse(json.replace('\'', '"'));
    }

    private SimpleFeature feature(String kind) {
        return SimpleFeatureBuilder.build(type, new Object[] {kind, 2}, "in." + kind);
    }

    @Test
    public void testExpressionMembership() throws Exception {
        Expression in =
                MBExpression.transformExpression(array("['in', ['get', 'kind'], ['literal', ['park', 'wood']]]"));
        assertTrue(in.evaluate(feature("park"), Boolean.class));
        assertFalse(in.evaluate(feature("street"), Boolean.class));
    }

    @Test
    public void testFilterExpressionForm() throws Exception {
        Filter filter = new MBFilter(array("['in', ['get', 'kind'], ['literal', ['park', 'wood']]]")).filter();
        assertTrue(filter.evaluate(feature("wood")));
        assertFalse(filter.evaluate(feature("street")));
    }

    @Test
    public void testFilterConstantNeedle() throws Exception {
        // a string first argument with an array haystack is the expression form, not the legacy key form
        Filter filter = new MBFilter(array("['in', 'park', ['literal', ['park', 'wood']]]")).filter();
        assertTrue(filter.evaluate(feature("street")));
    }

    @Test
    public void testNumericMembership() throws Exception {
        // JSON numbers parse as Long, the attribute is an Integer
        Expression in = MBExpression.transformExpression(array("['in', ['get', 'rank'], ['literal', [1, 2]]]"));
        assertTrue(in.evaluate(feature("park"), Boolean.class));
    }

    @Test
    public void testExpressionHaystack() throws Exception {
        // MapLibre reads ["get", "tags"] as an expression: it must not become the two strings "get" and "tags"
        JSONArray json = array("['in', ['get', 'kind'], ['get', 'tags']]");
        MBFormatException e = assertThrows(MBFormatException.class, () -> MBExpression.transformExpression(json));
        assertThat(e.getMessage(), containsString("[\"literal\", [...]]"));
    }

    @Test
    public void testMissingHaystack() throws Exception {
        JSONArray json = array("['in', ['get', 'kind']]");
        MBFormatException e = assertThrows(MBFormatException.class, () -> MBExpression.transformExpression(json));
        assertThat(e.getMessage(), containsString("exactly 2 argument"));
    }

    @Test
    public void testFilterLegacyForm() throws Exception {
        // legacy ["in", key, v0, v1, ...] form must keep working
        Filter filter = new MBFilter(array("['in', 'kind', 'park', 'wood']")).filter();
        assertTrue(filter.evaluate(feature("park")));
        assertFalse(filter.evaluate(feature("street")));
    }
}
