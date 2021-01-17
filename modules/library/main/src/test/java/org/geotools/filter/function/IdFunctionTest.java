package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IdFunctionTest extends FunctionTestSupport {

    @Test
    public void testId() {
        assertEquals("classification.t1", ff.function("id").evaluate(testFeatures[0]));
        assertEquals("classification.t2", ff.function("id").evaluate(testFeatures[1]));
    }
}
