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
import org.geotools.api.data.Parameter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.text.Text;
import org.geotools.util.Converters;

/**
 * Generate an output by interpolating between stops just less than and just greater than the function input. The domain
 * must be numeric.
 *
 * <h2>Parameters:</h2>
 *
 * <ol start="0">
 *   <li>The interpolation input
 *   <li>The base of the interpolation
 *   <li>(...n) The remaining args are interpreted as pairs of stop values (input, output) for the interpolation. There
 *       must be an even number.
 * </ol>
 *
 * @author Jody Garnett (Boundless)
 */
public class ExponentialFunction extends FunctionImpl {
    private static final FilterFactory ff2 = CommonFactoryFinder.getFilterFactory(null);
    public static final FunctionName NAME;

    static {
        Parameter<Object> result = new Parameter<>("result", Object.class, 1, 1);
        Parameter<Object> input = new Parameter<>("input", Object.class, 1, 1);
        Parameter<Double> base = new Parameter<>(
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
        Parameter<Object> stops = new Parameter<>("stops", Object.class, 4, -1);
        NAME = new FunctionNameImpl("Exponential", result, input, base, stops);
    }

    private static class Stop {
        final Expression stop;
        final Expression value;

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
    public Object evaluate(Object object) {
        // trying to guess the context from the input values
        List<Stop> stops = getStops(getParameters());
        Class<?> target = Color.class;
        for (Stop stop : stops) {
            if (stop.value.evaluate(object, Double.class) != null) {
                target = Double.class;
            }
        }
        return evaluate(object, target);
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        List<Expression> parameters = getParameters();

        Expression input = parameters.get(0);
        Expression base = parameters.get(1);

        List<Stop> stops = getStops(parameters);

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

    public List<Stop> getStops(List<Expression> parameters) {
        List<Stop> stops = new ArrayList<>();
        if (parameters.size() % 2 != 0) {
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                    + " requires an even number of stop values, but "
                    + (parameters.size() - 2)
                    + " were provided.");
        }

        for (int i = 2; i + 1 < parameters.size(); i = i + 2) {
            Stop stop = new Stop(parameters.get(i), parameters.get(i + 1));
            stops.add(stop);
        }
        return stops;
    }

    private <T> Object exponential(
            Object object, double inputValue, double base, Stop lower, Stop upper, Class<T> context) {
        if (Color.class.isAssignableFrom(context)) {
            return colorExponential(object, inputValue, base, lower, upper);
        } else {
            return numericExponential(object, inputValue, base, lower, upper);
        }
    }

    private double numericExponential(Object object, double inputValue, double base, Stop lower, Stop upper) {
        double stop1 = lower.stop.evaluate(object, Double.class);
        double value1 = lower.value.evaluate(object, Double.class);
        double stop2 = upper.stop.evaluate(object, Double.class);
        double value2 = upper.value.evaluate(object, Double.class);
        double t = exponentialInterpolationRatio(object, inputValue, base, stop1, stop2);

        return value1 * (1 - t) + value2 * t;
    }

    /**
     * Description from
     * https://github.com/mapbox/mapbox-gl-js/blob/master/src/style-spec/expression/definitions/interpolate.js#L220
     *
     * <p>Returns a **ratio** that can be used to interpolate between exponential function stops. How it works: Two
     * consecutive stop values define a (scaled and shifted) exponential function `f(x) = a * base^x + b`, where `base`
     * is the user-specified base, and `a` and `b` are constants affording sufficient degrees of freedom to fit the
     * function to the given stops.
     *
     * <p>Here's a bit of algebra that lets us compute `f(x)` directly from the stop values without explicitly solving
     * for `a` and `b`:
     *
     * <p>First stop value: `f(x0) = y0 = a * base^x0 + b` Second stop value: `f(x1) = y1 = a * base^x1 + b` => `y1 - y0
     * = a(base^x1 - base^x0)` => `a = (y1 - y0)/(base^x1 - base^x0)`
     *
     * <p>Desired value: `f(x) = y = a * base^x + b` => `f(x) = y0 + a * (base^x - base^x0)`
     *
     * <p>From the above, we can replace the `a` in `a * (base^x - base^x0)` and do a little algebra: ``` a * (base^x -
     * base^x0) = (y1 - y0)/(base^x1 - base^x0) * (base^x - base^x0) = (y1 - y0) * (base^x - base^x0) / (base^x1 -
     * base^x0) ```
     *
     * <p>If we let `(base^x - base^x0) / (base^x1 base^x0)`, then we have `f(x) = y0 + (y1 - y0) * ratio`. In other
     * words, `ratio` may be treated as an interpolation factor between the two stops' output values.
     *
     * <p>(Note: a slightly different form for `ratio`, `(base^(x-x0) - 1) / (base^(x1-x0) - 1) `, is equivalent, but
     * requires fewer expensive `Math.pow()` operations.)
     */
    private double exponentialInterpolationRatio(
            Object object, double inputValue, double base, double stop1, double stop2) {
        double difference = stop2 - stop1;
        double progress = inputValue - stop1;

        if (difference == 0) {
            return 0;
        } else if (base == 1) {
            return progress / difference;
        } else {
            return (Math.pow(base, progress) - 1) / (Math.pow(base, difference) - 1);
        }
    }

    /** Perform exponential interpolation on each of the channels of the color values at each stop. */
    private Object colorExponential(Object object, double inputValue, double base, Stop lower, Stop upper) {
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

        return new Color((int) Math.round(r), (int) Math.round(g), (int) Math.round(b), (int) Math.round(a));
    }

    /**
     * Find the stop containing the input value. The value returned is the index, in the stops list, of the higher point
     * of the segment between two stops.
     *
     * @return stop index; or 0 if input is below the range of the stops; or {@code max stop index + 1} if it is above
     *     the range
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
