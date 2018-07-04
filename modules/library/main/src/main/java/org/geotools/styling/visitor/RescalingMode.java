/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.visitor;

import java.util.HashMap;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Length;
import org.geotools.measure.Units;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import si.uom.SI;
import systems.uom.common.USCustomary;

/**
 * Helper class that allows rescaling to pixels numbers that might carry a unit specification along
 * with them, handling the case where real world units should not be rescaled (dpi rescaling) as
 * well as the simpler case where everything should be rescaled (uom rescaling)
 */
enum RescalingMode {
    /** Rescales the values and maintains the units */
    KeepUnits {
        @Override
        public String rescaleToStringInternal(double scaleFactor, Measure measure) {
            double rescaled = measure.value * scaleFactor;
            String rescaledString;
            if (rescaled == (int) rescaled) {
                rescaledString = String.valueOf((int) rescaled);
            } else {
                rescaledString = String.valueOf(rescaled);
            }
            if (measure.isPixelInPixelDefault()) {
                return rescaledString;
            } else {
                return rescaledString + UNIT_SYMBOLS.get(measure.uom);
            }
        }
    },

    /** Only rescales pixel values */
    Pixels {
        @Override
        public String rescaleToStringInternal(double scaleFactor, Measure measure) {
            if (measure.isRealWorldUnit()) {
                if (measure.isRealWorldUnitInPixelDefault()) {
                    return String.valueOf(measure.value) + UNIT_SYMBOLS.get(measure.uom);
                } else {
                    return String.valueOf(measure.value);
                }
            } else {
                return String.valueOf(measure.value * scaleFactor);
            }
        }

        @Override
        public Expression rescaleToExpression(Expression scaleFactor, Measure measure) {
            if (measure.isRealWorldUnit()) {
                return measure.expression;
            } else {
                return super.rescaleToExpression(scaleFactor, measure);
            }
        }
    },

    /**
     * Rescales feet and meter to pixel based on the scale factor (interpreted as a scale
     * denominator)
     */
    RealWorld {
        @Override
        public String rescaleToStringInternal(double scaleFactor, Measure measure) {
            return String.valueOf(
                    measure.value * computeRescaleMultiplier(scaleFactor, measure.uom));
        }

        /**
         * Computes a rescaling multiplier to be applied to an unscaled value.
         *
         * @param mapScale the mapScale in pixels per meter.
         * @param uom the unit of measure that will be used to scale.
         * @return the rescaling multiplier for the provided parameters.
         */
        double computeRescaleMultiplier(double mapScale, Unit<Length> uom) {
            // no scaling to do if UOM is PIXEL (or null, which stands for PIXEL as well)
            if (uom == null || uom.equals(Units.PIXEL)) return 1;

            if (uom == SI.METRE) {
                return mapScale;
            }

            // converts value from meters to given UOM
            UnitConverter converter = uom.getConverterTo(SI.METRE);
            return converter.convert(mapScale);
        }
    };

    public abstract String rescaleToStringInternal(double scaleFactor, Measure measure);

    public String rescaleToString(double scaleFactor, Measure measure) {
        if (measure.value == null) {
            throw new IllegalStateException(
                    "Cannot rescale to literal, the value is a generic expression, not a static value: "
                            + measure.expression);
        }

        return rescaleToStringInternal(scaleFactor, measure);
    }

    public Expression rescaleToExpression(Expression scaleFactor, Measure measure) {
        if (measure.value != null && scaleFactor instanceof Literal) {
            Double scale = scaleFactor.evaluate(null, Double.class);
            return Measure.ff.literal(rescaleToStringInternal(scale, measure));
        } else {
            // if it's an expression, there is still a chance the expression will have, at the
            // end,
            // a unit, so we have to delay the evaluation to later
            return Measure.ff.function(
                    "rescaleToPixels",
                    measure.expression,
                    Measure.ff.literal(measure.uom),
                    scaleFactor,
                    Measure.ff.literal(this));
        }
    }

    /**
     * Translates between units and their shortcuts (we can only get the full name from the unit
     * object
     */
    final Map<Unit, String> UNIT_SYMBOLS =
            new HashMap<Unit, String>() {
                {
                    put(Units.PIXEL, "px");
                    put(USCustomary.FOOT, "ft");
                    put(SI.METRE, "m");
                }
            };
}
