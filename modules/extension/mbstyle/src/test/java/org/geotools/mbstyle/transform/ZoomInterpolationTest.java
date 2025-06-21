package org.geotools.mbstyle.transform;

import static org.junit.Assert.assertEquals;

import org.geotools.api.filter.expression.Expression;
import org.geotools.filter.function.EnvFunction;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.parse.MBFunction;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

public class ZoomInterpolationTest {

    // Zoom function, Exponential Number -> Number
    @Test
    public void testZoomInterpolationExponential() throws ParseException {
        // Define an exponential zoom function
        String functionJson = "{'type':'exponential', 'base': 1, 'stops':[[0,0],[6,256],[12,1024]]}";
        JSONObject json = MapboxTestUtils.object(functionJson);
        MBFunction function = new MBFunction(json);
        Expression fn = function.numeric();

        // Set the zoom level to be between stops
        // Set scale denominator to the equivalent of zoomLevel ~ 4.25
        String scaleDenom = "14691570.9871";
        EnvFunction.setGlobalValue("wms_scale_denominator", scaleDenom);

        // Assert output is interpolated
        Double d = fn.evaluate(null, Number.class).doubleValue();
        double expectedVal = 4.25 / 6.0 * 256.0;
        assertEquals("Linear interpolation", expectedVal, d, .000001);
    }

    // Zoom function, Interval Number -> Number, Number -> String
    @Test
    public void testZoomInterpolationInterval() throws ParseException {
        // Define an interval zoom function
        String functionJson = "{'type':'interval', 'stops':[[0,0],[6,256],[12,1024]]}";
        JSONObject json = MapboxTestUtils.object(functionJson);
        MBFunction function = new MBFunction(json);
        Expression fn = function.numeric();

        // Set the zoom level to be between the lower stops
        // Set scale denominator to the equivalent of zoomLevel ~ 4.25
        EnvFunction.setGlobalValue("wms_scale_denominator", "29383141.974265");

        // Assert output is interpolated
        Double d = fn.evaluate(null, Number.class).doubleValue();
        double expectedVal = 0;
        assertEquals("Interval function", expectedVal, d, .000001);

        // Set the zoom level to be between the higher stops
        // Set scale denominator to the equivalent of zoomLevel ~ 11.25
        EnvFunction.setGlobalValue("wms_scale_denominator", "229555.796674");

        d = fn.evaluate(null, Number.class).doubleValue();
        expectedVal = 256;
        assertEquals("Interval function", expectedVal, d, .000001);

        // Set the zoom level to be above the highest stop
        // Set scale denominator to the equivalent of zoomLevel ~ 15.5
        EnvFunction.setGlobalValue("wms_scale_denominator", "12064.540408");

        d = fn.evaluate(null, Number.class).doubleValue();
        expectedVal = 1024;
        assertEquals("Interval function", expectedVal, d, .000001);
    }

    @Test
    public void testZoomInterpolationBelowLowestStop() throws ParseException {
        // Define an interval zoom function
        String functionJson = "{'type':'interval', 'stops':[[6,256],[12,1024]]}";
        JSONObject json = MapboxTestUtils.object(functionJson);
        MBFunction function = new MBFunction(json);
        Expression fn = function.numeric();

        // Set the zoom level to be below the lowest stop
        // Set scale denominator to the equivalent of zoomLevel ~ 0.25
        EnvFunction.setGlobalValue("wms_scale_denominator", "470130271.588236");

        // Assert output is interpolated
        Double d = fn.evaluate(null, Number.class).doubleValue();
        double expectedVal = 256;
        assertEquals("Interval function", expectedVal, d, .000001);
    }

    @Test
    @Ignore
    public void testZoomPropertyInterpolationExponential() throws ParseException {
        // Define an exponential zoom-and-property function

        //        String functionJson =
        //                "{'type':'exponential', "
        //                        + "'property':'someNumericProperty', "
        //                        + "'base': 1, 'stops': [ "
        //                        + "[{'zoom': 5, 'value': 5}, 128], "
        //                        + "[{'zoom': 5, 'value': 10}, 1024], "
        //                        + "[{'zoom': 15, 'value': 5}, 2048], "
        //                        + "[{'zoom': 15,' value': 10}, 4096]]"
        //                        + "}";
        //
        //        // TODO - Reduce it, then test it.
        //
        //        JSONObject json = MapboxTestUtils.object(functionJson);
        //        MBFunction function = new MBFunction(json);
        //        Expression fn = function.numeric();
        //
        //        // Test at zoom level z < 5
        //        //    With someNumericProperty in each interval
        //
        //        // Test at zoom level 5 =< z < 15
        //        //    With someNumericProperty in each interval
        //
        //        // Test at zoom level 15 <= z
        //        //    With someNumericProperty in each interval
    }

    @Test
    @Ignore
    public void testZoomPropertyInterpolationInterval() throws ParseException {
        // Define an interval zoom-and-property function

        //        String functionJson =
        //                "{'type':'interval', "
        //                        + "'property':'someNumericProperty', "
        //                        + "'stops': [ "
        //                        + "[{'zoom': 5, 'value': 5}, 'red'], "
        //                        + "[{'zoom': 5, 'value': 10}, 'green'], "
        //                        + "[{'zoom': 15, 'value': 5}, 'blue'], "
        //                        + "[{'zoom': 15,' value': 10}, 'black']]"
        //                        + "}";
        //
        //        // TODO - Reduce it, then test it.
        //
        //        JSONObject json = MapboxTestUtils.object(functionJson);
        //        MBFunction function = new MBFunction(json);
        //        Expression fn = function.numeric();
        //
        //        // Test at zoom level z < 5
        //        //    With someNumericProperty in each interval
        //
        //        // Test at zoom level 5 =< z < 15
        //        //    With someNumericProperty in each interval
        //
        //        // Test at zoom level 15 <= z
        //        //    With someNumericProperty in each interval
    }
}
