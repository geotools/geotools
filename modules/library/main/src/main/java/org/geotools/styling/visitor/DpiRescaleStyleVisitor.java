/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Map;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**
 * This is a style visitor that will produce a copy of the provided style. The copy will be rescaled
 * by a provided factor if UOM is PIXEL.
 */
public class DpiRescaleStyleVisitor extends RescaleStyleVisitor {

    public DpiRescaleStyleVisitor(double scale) {
        super(scale);
    }

    public DpiRescaleStyleVisitor(FilterFactory2 filterFactory, double scale) {
        super(filterFactory, scale);
    }

    @Override
    protected Expression rescale(Expression expr) {
        // handle null values
        if (expr == null) {
            return null;
        }
        if (expr == Expression.NIL) {
            return Expression.NIL;
        }

        // delegate the handling of the rescaling to ValueAndUnit
        // to deal with local uom (px, m, ft suffixes)
        Measure v = new Measure(expr, defaultUnit);
        return RescalingMode.Pixels.rescaleToExpression(scale, v);
    }

    @Override
    protected void rescaleOption(Map<String, String> options, String key, double defaultValue) {
        double scaleFactor = (double) scale.evaluate(null, Double.class);
        String value = options.get(key);
        if (value == null) {
            value = String.valueOf(defaultValue);
        }

        Measure v = new Measure(value, defaultUnit);
        String rescaled = RescalingMode.Pixels.rescaleToString(scaleFactor, v);
        options.put(key, String.valueOf(rescaled));
    }

    @Override
    protected void rescaleOption(Map<String, String> options, String key, int defaultValue) {
        double scaleFactor = (double) scale.evaluate(null, Double.class);
        String value = options.get(key);
        if (value == null) {
            value = String.valueOf(defaultValue);
        }

        Measure v = new Measure(value, defaultUnit);
        String rescaled = RescalingMode.Pixels.rescaleToString(scaleFactor, v);
        options.put(key, String.valueOf(rescaled));
    }
}
