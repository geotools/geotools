package org.geotools.filter.function;

public class IdFunctionTest extends FunctionTestSupport {

    public IdFunctionTest(String testName) {
        super(testName);
    }
    
    public void testId() {
        assertEquals("classification.t1", ff.function("id").evaluate(testFeatures[0]));
        assertEquals("classification.t2", ff.function("id").evaluate(testFeatures[1]));
    }

}
