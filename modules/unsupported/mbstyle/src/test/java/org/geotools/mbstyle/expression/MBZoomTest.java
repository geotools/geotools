package org.geotools.mbstyle.expression;

import org.geotools.filter.function.EnvFunction;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class MBZoomTest extends AbstractMBExpressionTest {
    @Override
    protected String getTestResourceName() {
        return "expressionMBZoomTest.json";
    }

    @Override
    protected Class getExpressionClassType() {
        return MBZoom.class;
    }

    /**
     * Verify that the "zoom" MBTypes expression can be parsed correctly.
     */
    @Test
    public void testParseLiteralExpression() {
        EnvFunction.setGlobalValue("wms_scale_denominator", "2132.729584");
        final JSONObject j = getObjectByLayerId("zoomExpression", "paint");
        Object o = getExpressionEvaluation(j, "circle-radius");
        assertEquals(ff.literal(Math.round(17.999)), ff.literal(Math.round((double) o)));
    }
}
