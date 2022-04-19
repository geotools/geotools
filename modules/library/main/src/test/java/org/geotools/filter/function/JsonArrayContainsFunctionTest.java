package org.geotools.filter.function;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

import static org.junit.Assert.*;

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
        assertTrue((boolean) containsFunction(json, "/onNestedArrayStr/nestedArray", "baz").evaluate(null));
        assertTrue((boolean) containsFunction(json, "/onNestedArrayInt/nestedArray", 87).evaluate(null));
        assertFalse((boolean) containsFunction(json, "/nonexistingpointer", "...").evaluate(null));
        /*assertEquals("baz", pointer(json, "/foo/1").evaluate(null));
        assertEquals(Integer.valueOf(1), pointer(json, "/nested/a").evaluate(null));
        assertEquals(Integer.valueOf(2), pointer(json, "/nested/b").evaluate(null));
        assertEquals(3.1416, (Float) pointer(json, "/pi").evaluate(null), 0.001d);
        assertEquals(Integer.valueOf(1234), pointer(json, "/v").evaluate(null));
        assertEquals(Boolean.TRUE, pointer(json, "/t").evaluate(null));
        assertEquals(Boolean.FALSE, pointer(json, "/f").evaluate(null));
        assertNull(pointer(json, "/foobar").evaluate(null));
        assertNull(pointer(json, "/").evaluate(null));*/
    }

    public Function containsFunction(String json, String pointer, Object expected) {
        return FF.function("jsonArrayContains", FF.literal(json), FF.literal(pointer), FF.literal(expected));
    }
}
