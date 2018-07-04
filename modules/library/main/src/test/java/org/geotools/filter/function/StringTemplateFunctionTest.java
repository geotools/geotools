package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

public class StringTemplateFunctionTest extends SEFunctionTestBase {

    private Literal fallback;

    @Before
    public void setup() {
        parameters = new ArrayList<Expression>();
        fallback = ff2.literal("NOT_FOUND");
    }

    @Test
    public void testConcatenate() {
        parameters.add(ff2.literal("123"));
        parameters.add(ff2.literal(".*"));
        parameters.add(ff2.literal("start${0}end"));

        Function fn = finder.findFunction("stringTemplate", parameters, fallback);
        Object result = fn.evaluate(null);

        assertEquals("start123end", result);
    }

    @Test
    public void testCapturingGroups() {
        parameters.add(ff2.literal("abc123_678"));
        parameters.add(ff2.literal(".*(\\d{3})_(\\d{3})"));
        parameters.add(ff2.literal("first${1}_second${2}_full${0}"));

        Function fn = finder.findFunction("stringTemplate", parameters, fallback);
        Object result = fn.evaluate(null);

        assertEquals("first123_second678_fullabc123_678", result);
    }

    @Test
    public void testFallback() {
        parameters.add(ff2.literal("abc12_67"));
        parameters.add(ff2.literal(".*(\\d{3})_(\\d{3})"));
        parameters.add(ff2.literal("first${1}_second${2}_full${0}"));
        parameters.add(ff2.literal("noMatch"));

        Function fn = finder.findFunction("stringTemplate", parameters, fallback);
        Object result = fn.evaluate(null);

        assertEquals("noMatch", result);
    }

    @Test
    public void testDynamic() throws SchemaException {
        parameters.add(ff2.property("input"));
        parameters.add(ff2.property("pattern"));
        parameters.add(ff2.property("template"));
        Function fn = finder.findFunction("stringTemplate", parameters, fallback);

        SimpleFeatureType type =
                DataUtilities.createType("test", "input:string,pattern:string,template:string");
        SimpleFeature f1 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {"abc123_567", ".*(\\d{3})_(\\d{3})", "${1}|${2}"},
                        null);
        assertEquals("123|567", fn.evaluate(f1));

        SimpleFeature f2 =
                SimpleFeatureBuilder.build(
                        type, new Object[] {"abc12_67", "(.*)_(\\d{3})", "${1}_${2}"}, null);
        assertEquals(null, fn.evaluate(f2));

        SimpleFeature f3 =
                SimpleFeatureBuilder.build(
                        type, new Object[] {"abc12_67", "(.*)_(.*)", "${1}|${2}"}, null);
        assertEquals("abc12|67", fn.evaluate(f3));
    }
}
