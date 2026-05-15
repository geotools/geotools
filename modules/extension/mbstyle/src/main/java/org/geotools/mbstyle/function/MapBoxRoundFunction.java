/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.Converters;

/**
 * Rounds the input to the nearest integer, with halfway values rounded away from zero. This matches the Mapbox
 * specification for the {@code round} expression (e.g., {@code round(-1.5) = -2}, {@code round(2.5) = 3}).
 *
 * <p>Neither Java's {@link Math#round(double)} (rounds toward positive infinity for .5 values) nor
 * {@link Math#rint(double)} (banker's rounding: rounds to nearest even for .5 values) match the Mapbox semantics. This
 * function implements: {@code signum(x) * floor(abs(x) + 0.5)}.
 *
 * <p>This function is registered as {@code "round_away"} through {@link MBFunctionFactory} (which is discovered via SPI
 * as a {@link org.geotools.filter.FunctionFactory}).
 *
 * <p>The {@link org.geotools.mbstyle.expression.MBMath} expression parser references it by name when translating the
 * Mapbox {@code ["round", ...]} expression.
 *
 * <p>See <a href="https://maplibre.org/maplibre-style-spec/expressions/#round">round</a> in the spec.
 */
class MapBoxRoundFunction extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl("round_away", parameter("rounded", Long.class), parameter("number", Number.class));

    public MapBoxRoundFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        Object arg0 = getExpression(0).evaluate(feature);

        if (arg0 == null) {
            return null;
        }

        arg0 = Converters.convert(arg0, Double.class);
        if (arg0 == null) {
            throw new IllegalArgumentException(
                    "Filter Function problem for function round_away argument #0 - expected type double");
        }

        double value = (Double) arg0;
        // Round half away from zero: sgn(x) * floor(|x| + 0.5)
        return Long.valueOf((long) (Math.signum(value) * Math.floor(Math.abs(value) + 0.5)));
    }
}
