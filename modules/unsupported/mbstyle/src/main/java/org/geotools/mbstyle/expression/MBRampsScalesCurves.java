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

import org.geotools.mbstyle.parse.MBFormatException;
import org.json.simple.JSONArray;
import org.opengis.filter.expression.Expression;

public class MBRampsScalesCurves extends MBExpression {

    public MBRampsScalesCurves(JSONArray json) {
        super(json);
    }

    /**
     * Produces continuous, smooth results by interpolating between pairs of input and output values
     * ("stops"). The input may be any numeric expression (e.g., ["get", "population"]). Stop inputs
     * must be numeric literals in strictly ascending order. The output type must be number,
     * array<number>, or color. Example: ["interpolate", interpolation: ["linear"] | ["exponential",
     * base] | ["cubic-bezier", x1, y1, x2, y2 ], input: number, stop_input_1: number,
     * stop_output_1: OutputType, stop_input_n: number, stop_output_n: OutputType, ... ]: OutputType
     * (number, array<number>, or Color)"
     */
    public Expression rscInterpolate() {
        return null;
    }

    /**
     * Produces discrete, stepped results by evaluating a piecewise-constant function defined by
     * pairs of input and output values ("stops"). The input may be any numeric expression (e.g.,
     * ["get", "population"]). Stop inputs must be numeric literals in strictly ascending order.
     * Returns the output value of the stop just less than the input, or the first input if the
     * input is less than the first stop.
     */
    public Expression rscStep() {
        return null;
    }

    @Override
    public Expression getExpression() throws MBFormatException {
        switch (name) {
            case "interpolate":
                return rscInterpolate();
            case "step":
                return rscStep();
            default:
                throw new MBFormatException(
                        name + " is an unsupported ramps, scales, curves expression");
        }
    }
}
