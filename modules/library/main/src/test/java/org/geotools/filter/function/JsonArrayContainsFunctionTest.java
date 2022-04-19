package org.geotools.filter.function;

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

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
