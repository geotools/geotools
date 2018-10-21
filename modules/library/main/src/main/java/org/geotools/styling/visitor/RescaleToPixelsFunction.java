/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * Applies unit to pixel rescaling around expressions whose native unit cannot be determined in a
 * static fashion
 */
public class RescaleToPixelsFunction extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "rescaleToPixels",
                    String.class,
                    /**
                     * The value to be rescaled, could be a number, e.g., 3.75, or a number plus
                     * unit, e.g. 5m, 7.8ft
                     */
                    parameter("value", String.class),
                    /** The default unit, could be null, in this case pixel is assumed */
                    parameter("defaultUnit", Unit.class),
                    /** The unit-less rescaling factor */
                    parameter("scaleFactor", Double.class),
                    /** Whether real world units should be rescaled, or left as they are */
                    parameter("rescalingMode", RescalingMode.class, 0, 1));

    public RescaleToPixelsFunction() {
        super(NAME);
    }

    public Object evaluate(Object feature) {

        String value = getExpression(0).evaluate(feature, String.class);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        Unit<Length> defaultUnit = getExpression(1).evaluate(feature, Unit.class);

        Double scaleFactor = getExpression(2).evaluate(feature, Double.class);
        if (scaleFactor == null) {
            throw new IllegalArgumentException("Invalid scale factor, it should be non null");
        }

        RescalingMode mode = RescalingMode.KeepUnits;
        if (getParameters().size() >= 3) {
            RescalingMode theMode = getExpression(3).evaluate(feature, RescalingMode.class);
            if (theMode != null) {
                mode = theMode;
            }
        }

        // compute the rescaling now that we have all the details
        Measure measure = new Measure(value, defaultUnit);
        String result = mode.rescaleToString(scaleFactor, measure);

        return result;
    }
}
