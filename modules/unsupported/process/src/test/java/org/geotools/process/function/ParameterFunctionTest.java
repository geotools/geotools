package org.geotools.process.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;

public class ParameterFunctionTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testEmtpy() {
        Function param = ff.function("parameter");
        try {
            param.evaluate(null);
            fail("This should have failed with an illegal argument exception");
        } catch (IllegalArgumentException e) {
            // fine
        }
    }

    @Test
    public void testContext() {
        Object context = new Object();
        Function param = ff.function("parameter", ff.literal("argument"));
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) param.evaluate(context);
        assertEquals(1, result.size());
        assertEquals("argument", result.keySet().iterator().next());
        assertSame(context, result.values().iterator().next());
    }

    @Test
    public void testOne() {
        Object value = new Object();
        Function param = ff.function("parameter", ff.literal("argument"), ff.literal(value));
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) param.evaluate(null);
        assertEquals(1, result.size());
        assertEquals("argument", result.keySet().iterator().next());
        assertSame(value, result.values().iterator().next());
    }

    @Test
    public void testMany() {
        Object value1 = new Object();
        Object value2 = new Object();
        Function param =
                ff.function(
                        "parameter",
                        ff.literal("argument"),
                        ff.literal(value1),
                        ff.literal(value2));
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) param.evaluate(null);
        assertEquals(1, result.size());
        assertEquals("argument", result.keySet().iterator().next());
        @SuppressWarnings("unchecked")
        List<Object> value = (List<Object>) result.values().iterator().next();
        assertEquals(2, value.size());
        assertSame(value1, value.get(0));
        assertSame(value2, value.get(1));
    }
}
