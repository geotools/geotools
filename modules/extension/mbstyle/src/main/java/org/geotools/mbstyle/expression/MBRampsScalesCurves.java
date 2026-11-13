/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.mbstyle.expression;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.expression.Expression;
import org.geotools.mbstyle.parse.MBFormatException;
import org.json.simple.JSONArray;

public class MBRampsScalesCurves extends MBExpression {

    public MBRampsScalesCurves(JSONArray json) {
        super(json);
    }

    /**
     * Produces continuous, smooth results by interpolating between pairs of input and output values ("stops"). The
     * input may be any numeric expression (e.g., ["get", "population"]). Stop inputs must be numeric literals in
     * strictly ascending order. The output type must be number, array&lt;number&gt;, or color. Example: ["interpolate",
     * interpolation: ["linear"] | ["exponential", base] | ["cubic-bezier", x1, y1, x2, y2 ], input: number,
     * stop_input_1: number, stop_output_1: OutputType, stop_input_n: number, stop_output_n: OutputType, ... ]:
     * OutputType (number, array&lt;number&gt;, or Color)"
     */
    public Expression rscInterpolate() {
        // ["interpolate", interpolation, input, stop_in_1, stop_out_1, ..., stop_in_n, stop_out_n]
        if (json.size() < 5 || (json.size() % 2) == 0) {
            throw new MBFormatException(
                    "\"interpolate\" requires an interpolation type, an input, and stop pairs: " + json);
        }
        JSONArray interpolation = parse.jsonArray(json.get(1));
        String curve = (String) interpolation.get(0);
        Expression input = parse.string(json, 2);
        boolean color = isColorStops(4);

        List<Expression> params = new ArrayList<>();
        params.add(input);
        if ("exponential".equals(curve)) {
            double base = ((Number) interpolation.get(1)).doubleValue();
            if (base != 1.0) {
                params.add(ff.literal(base));
                addStops(params, 3, color);
                return ff.function("Exponential", params.toArray(new Expression[0]));
            }
            // base 1.0 is linear, fall through
        } else if (!"linear".equals(curve)) {
            // cubic-bezier and any other type are not supported
            throw new MBFormatException("Unsupported interpolation type \"" + curve + "\"");
        }
        addStops(params, 3, color);
        params.add(ff.literal(color ? "color" : "numeric"));
        return ff.function("Interpolate", params.toArray(new Expression[0]));
    }

    /** Appends (stopInput, stopOutput) literal/expression pairs starting at the given index. */
    private void addStops(List<Expression> params, int start, boolean color) {
        for (int i = start; i + 1 < json.size(); i += 2) {
            params.add(ff.literal(json.get(i)));
            params.add(output(i + 1, color));
        }
    }

    private Expression output(int index, boolean color) {
        Object obj = json.get(index);
        if (color && obj instanceof String s) {
            return parse.color(s);
        }
        return parse.string(json, index);
    }

    /**
     * Returns true when any literal stop output is a color string (e.g. "#fff", "rgba(...)"). An output given as an
     * expression (e.g. ["get", "col"]) is not inspected, so color values produced by expressions are not supported.
     */
    private boolean isColorStops(int firstOutput) {
        for (int i = firstOutput; i < json.size(); i += 2) {
            // parse.color() never returns null, so test the conversion; a later stop may still be a color
            if (json.get(i) instanceof String s && parse.convertToColor(s) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Produces discrete, stepped results by evaluating a piecewise-constant function defined by pairs of input and
     * output values ("stops"). The input may be any numeric expression (e.g., ["get", "population"]). Stop inputs must
     * be numeric literals in strictly ascending order. Returns the output of the last stop less than or equal to the
     * input, or the first output (output_0) when the input is below the first stop.
     */
    public Expression rscStep() {
        // ["step", input, output_0, stop_in_1, stop_out_1, ..., stop_in_n, stop_out_n]
        // valid layout is odd length: name + input + output_0 + (threshold, output) pairs
        if (json.size() < 5 || (json.size() % 2) == 0) {
            throw new MBFormatException("\"step\" requires an input, a base output, and stop pairs: " + json);
        }
        boolean color = isColorStops(2);
        List<Expression> params = new ArrayList<>();
        params.add(parse.string(json, 1));
        // Categorize needs a leading value for inputs below the first threshold: mapbox output_0
        params.add(output(2, color));
        addStops(params, 3, color);
        params.add(ff.literal("succeeding"));
        return ff.function("Categorize", params.toArray(new Expression[0]));
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case "interpolate":
                return rscInterpolate();
            case "step":
                return rscStep();
            default:
                throw new MBFormatException(name + " is an unsupported ramps, scales, curves expression");
        }
    }
}
