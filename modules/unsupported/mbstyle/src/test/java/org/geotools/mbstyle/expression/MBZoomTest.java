package org.geotools.mbstyle.expression;

import static org.junit.Assert.assertEquals;

import org.geotools.filter.function.EnvFunction;
import org.json.simple.JSONObject;
import org.junit.Test;

public class MBZoomTest extends AbstractMBExpressionTest {
    @Override
    protected String getTestResourceName() {
        return "expressionMBZoomTest.json";
    }

    @Override
    protected Class getExpressionClassType() {
        return MBZoom.class;
    }

    /** Verify that the "zoom" MBTypes expression can be parsed correctly. */
    @Test
    public void testParseLiteralExpression() {
        EnvFunction.setGlobalValue("wms_scale_denominator", "2132.729584");
        final JSONObject j = getObjectByLayerId("zoomExpression", "paint");
        Object o = getExpressionEvaluation(j, "circle-radius");
        assertEquals(ff.literal(Math.round(17.999)), ff.literal(Math.round((double) o)));
    }
}
