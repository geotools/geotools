/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.function;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.geotools.data.Parameter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.text.Text;
import org.geotools.util.Converters;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Generate an output by interpolating between stops just less than and just greater than the
 * function input. The domain must be numeric.
 *
 * <h2>Parameters:</h2>
 *
 * <ol start="0">
 *   <li>The interpolation input
 *   <li>The base of the interpolation
 *   <li>(...n) The remaining args are interpreted as pairs of stop values (input, output) for the
 *       interpolation. There must be an even number.
 * </ol>
 *
 * @author Jody Garnett (Boundless)
 */
public class ExponentialFunction extends FunctionImpl {
    private static final FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2(null);
    public static final FunctionName NAME;

    static {
        Parameter<Object> result = new Parameter<Object>("result", Object.class, 1, 1);
        Parameter<Object> input = new Parameter<Object>("input", Object.class, 1, 1);
        Parameter<Double> base =
                new Parameter<Double>(
                        "base",
                        Double.class,
                        Text.text("Base"),
                        Text.text(
                                "Exponential base of the interpolation curve controlling rate at which function output increases."),
                        true,
                        0,
                        1,
                        1.0,
                        null);
        Parameter<Object> stops = new Parameter<Object>("stops", Object.class, 4, -1);
        NAME = new FunctionNameImpl("Exponential", result, input, base, stops);
    }

    private static class Stop {
        Expression stop;
        Expression value;

        public Stop(Expression stop, Expression value) {
            this.stop = stop;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Stop " + stop + ": " + value;
        }
    }

    public ExponentialFunction() {
        this.functionName = NAME;
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        List<Expression> parameters = getParameters();
        List<Stop> stops = new ArrayList<>();

        Expression input = parameters.get(0);
        Expression base = parameters.get(1);

        if (parameters.size() % 2 != 0) {
            throw new IllegalArgumentException(
                    this.getClass().getSimpleName()
                            + " requires an even number of stop values, but "
                            + (parameters.size() - 2)
                            + " were provided.");
        }

        for (int i = 2; (i + 1) < parameters.size(); i = i + 2) {
            Stop stop = new Stop(parameters.get(i), parameters.get(i + 1));
            stops.add(stop);
        }

        Double inputValue = input.evaluate(object, Double.class);
        Double baseValue = base.evaluate(object, Double.class);

        if (inputValue == null) {
            return null;
        }

        if (stops.size() == 1) {
            // single stop
            Stop single = stops.get(0);
            return single.value.evaluate(object, context);
        }
        int find = find(object, inputValue, stops);

        if (find <= 0) {
            // data is below stop range, use min
            Stop min = stops.get(0);
            return min.value.evaluate(object, context);
        } else if (find >= stops.size()) {
            // data is above the stop range, use max
            Stop max = stops.get(stops.size() - 1);
            return max.value.evaluate(object, context);
        }
        Stop lower = stops.get(find - 1);
        Stop upper = stops.get(find);
        Object exponential = exponential(object, inputValue, baseValue, lower, upper, context);

        return Converters.convert(exponential, context);
    }

    private <T> Object exponential(
            Object object,
            double inputValue,
            double base,
            Stop lower,
            Stop upper,
            Class<T> context) {
        if (Color.class.isAssignableFrom(context)) {
            return colorExponential(object, inputValue, base, lower, upper);
        } else {
            return numericExponential(object, inputValue, base, lower, upper);
        }
    }

    private double numericExponential(
            Object object, double inputValue, double base, Stop lower, Stop upper) {

        double stop1 = lower.stop.evaluate(object, Double.class);
        double value1 = lower.value.evaluate(object, Double.class);
        double stop2 = upper.stop.evaluate(object, Double.class);
        double value2 = upper.value.evaluate(object, Double.class);

        // Basic exponential function:
        //
        // value_i = scale*(stop_i)^base - offset
        //
        // Determine scale and offset based on the upper and lower stops:
        double scale = (value2 - value1) / (Math.pow(stop2, base) - Math.pow(stop1, base));
        double offset = value1 - scale * Math.pow(stop1, base);

        return offset + scale * Math.pow(inputValue, base);
    }

    /**
     * Perform exponential interpolation on each of the channels of the color values at each stop.
     */
    private Object colorExponential(
            Object object, double inputValue, double base, Stop lower, Stop upper) {
        Color lowerValue = lower.value.evaluate(object, Color.class);
        Color upperValue = upper.value.evaluate(object, Color.class);

        Stop redLowerStop = new Stop(lower.stop, ff2.literal(lowerValue.getRed()));
        Stop redUpperStop = new Stop(upper.stop, ff2.literal(upperValue.getRed()));

        Stop greenLowerStop = new Stop(lower.stop, ff2.literal(lowerValue.getGreen()));
        Stop greenUpperStop = new Stop(upper.stop, ff2.literal(upperValue.getGreen()));

        Stop blueLowerStop = new Stop(lower.stop, ff2.literal(lowerValue.getBlue()));
        Stop blueUpperStop = new Stop(upper.stop, ff2.literal(upperValue.getBlue()));

        Stop alphaLowerStop = new Stop(lower.stop, ff2.literal(lowerValue.getAlpha()));
        Stop alphaUpperStop = new Stop(upper.stop, ff2.literal(upperValue.getAlpha()));

        double r = numericExponential(object, inputValue, base, redLowerStop, redUpperStop);
        double g = numericExponential(object, inputValue, base, greenLowerStop, greenUpperStop);
        double b = numericExponential(object, inputValue, base, blueLowerStop, blueUpperStop);
        double a = numericExponential(object, inputValue, base, alphaLowerStop, alphaUpperStop);

        return new Color(
                (int) Math.round(r), (int) Math.round(g), (int) Math.round(b), (int) Math.round(a));
    }

    /**
     * Find the stop containing the input value. The value returned is the index, in the stops list,
     * of the higher point of the segment between two stops.
     *
     * @return stop index; or 0 if input is below the range of the stops; or {@code max stop index +
     *     1} if it is above the range
     */
    private int find(Object object, Double input, List<Stop> stops) {
        int find = stops.size();
        for (int i = 0; i < stops.size(); i++) {
            Double stop = stops.get(i).stop.evaluate(object, Double.class);
            if (input <= stop) {
                find = i;
                break;
            }
        }
        return find;
    }
}
