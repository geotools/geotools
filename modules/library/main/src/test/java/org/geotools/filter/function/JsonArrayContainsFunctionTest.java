/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;

public class JsonArrayContainsFunctionTest {

    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testArrayContainsString() {
        String json =
                "{\n" //
                        + "    \"arrayString\" : [\"bar\", \"baz\"],\n" //
                        + "    \"arrayInt\" : [1, 2],\n" //
                        + "    \"arrayDate\" : [],\n" //
                        + "    \"arrayDouble\" : [3.14, 4.6],\n" //
                        + "    \"onNestedArrayStr\" : { \"nestedArray\": [\"bar\", \"baz\"] }," //
                        + "    \"onNestedArrayInt\" : { \"nestedArray\": [4, 87] }" //
                        + "}";

        assertTrue((boolean) containsFunction(json, "/arrayString", "bar").evaluate(null));
        assertFalse((boolean) containsFunction(json, "/arrayString", "notpresent").evaluate(null));
        assertFalse((boolean) containsFunction(json, "/arrayString", 1).evaluate(null));
        assertTrue((boolean) containsFunction(json, "/arrayInt", 1).evaluate(null));
        assertTrue((boolean) containsFunction(json, "/arrayDouble", 4.6).evaluate(null));
        assertTrue(
                (boolean)
                        containsFunction(json, "/onNestedArrayStr/nestedArray", "baz")
                                .evaluate(null));
        assertTrue(
                (boolean)
                        containsFunction(json, "/onNestedArrayInt/nestedArray", 87).evaluate(null));
        assertFalse((boolean) containsFunction(json, "/nonexistingpointer", "...").evaluate(null));
    }

    public Function containsFunction(String json, String pointer, Object expected) {
        return FF.function(
                "jsonArrayContains", FF.literal(json), FF.literal(pointer), FF.literal(expected));
    }
}
