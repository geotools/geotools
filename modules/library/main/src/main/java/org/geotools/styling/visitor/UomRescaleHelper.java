/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.styling.visitor;

import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Length;
import org.geotools.measure.Units;
import org.geotools.util.Converters;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import si.uom.SI;
import systems.uom.common.USCustomary;

class UomRescaleHelper {

    private double mapScale = 1;

    private FilterFactory ff;

    public UomRescaleHelper(FilterFactory ff, double mapScale, boolean rescaleRealWorldUnits) {
        this.ff = ff;
        this.mapScale = mapScale;
    }

    /**
     * Computes a rescaling multiplier to be applied to an unscaled value.
     *
     * @param uom the unit of measure that will be used to scale.
     * @return the rescaling multiplier for the provided parameters.
     */
    protected double computeRescaleMultiplier(Unit<Length> uom) {
        // no scaling to do if UOM is PIXEL (or null, which stands for PIXEL as well)
        if (uom == null || uom.equals(Units.PIXEL)) return 1;

        if (uom == SI.METRE) {
            return mapScale;
        }

        // converts value from meters to given UOM
        UnitConverter converter = uom.getConverterTo(SI.METRE);
        return converter.convert(mapScale);
    }

    /**
     * Used to rescale the provided unscaled value.
     *
     * @param unscaled the unscaled value.
     * @param uom the unit of measure that will be used to scale.
     * @return the expression multiplied by the provided scale.
     */
    protected Expression rescale(Expression unscaled, Unit<Length> uom) {
        if (unscaled == null || unscaled.equals(Expression.NIL)) return unscaled;

        if (unscaled instanceof Literal) {
            // check if we have a uom attached at the end of the expression
            String value = unscaled.evaluate(null, String.class);
            if (value == null) {
                throw new IllegalArgumentException(
                        "Invalid empty measure '', "
                                + "was expecting a number, eventually followed by px, m or ft");
            }
            if (value.endsWith("px")) {
                // no rescale needed for this one
                value = value.substring(0, value.length() - 2);
                return ff.literal(Converters.convert(value, Double.class));
            } else {
                if (value.endsWith("ft")) {
                    value = value.substring(0, value.length() - 2);
                    uom = USCustomary.FOOT;
                } else if (value.endsWith("m")) {
                    value = value.substring(0, value.length() - 1);
                    uom = SI.METRE;
                }
                Double measure = Converters.convert(value, Double.class);
                if (measure != null) {
                    double rescaled = rescale(measure, uom);
                    return ff.literal(rescaled);
                } else {
                    throw new IllegalArgumentException(
                            "Invalid measure '"
                                    + value
                                    + "', was expecting a number, eventually followed by px, m or ft");
                }
            }
        } else {
            // otherwise, we use the rescaleToPixels function as
            double rescaleMultiplier = computeRescaleMultiplier(uom);
            return ff.multiply(unscaled, ff.literal(rescaleMultiplier));
        }
    }

    /**
     * Used to rescale the provided dash array.
     *
     * @param dashArray the unscaled dash array. If null, the method returns null.
     * @param uom the unit of measure that will be used to scale.
     * @return the rescaled dash array
     */
    protected float[] rescale(float[] dashArray, Unit<Length> uom) {
        if (dashArray == null) return null;
        if (uom == null || uom.equals(Units.PIXEL)) return dashArray;

        float[] rescaledDashArray = new float[dashArray.length];

        for (int i = 0; i < rescaledDashArray.length; i++) {
            rescaledDashArray[i] = (float) rescale((double) dashArray[i], uom);
        }
        return rescaledDashArray;
    }

    /**
     * Used to rescale the provided unscaled value.
     *
     * @param unscaled the unscaled value.
     * @param uom the unit of measure that will be used to scale.
     * @return a scaled value.
     */
    private double rescale(double unscaled, Unit<Length> uom) {
        // computes the basic rescaled value
        return unscaled * computeRescaleMultiplier(uom);
    }
}
