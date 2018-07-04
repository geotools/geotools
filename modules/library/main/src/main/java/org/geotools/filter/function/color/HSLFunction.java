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
package org.geotools.filter.function.color;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.awt.Color;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

/**
 * hsl lesscss.org color function.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class HSLFunction extends FunctionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "hsl",
                    parameter("result", Color.class),
                    parameter("hue", Double.class),
                    parameter("saturation", Double.class),
                    parameter("lightness", Double.class));

    public HSLFunction() {
        this.functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        double hue = (Double) getParameterValue(object, 0);
        double saturation = (Double) getParameterValue(object, 1);
        double lightness = (Double) getParameterValue(object, 2);

        return new HSLColor(hue, saturation, lightness).toRGB();
    }
}
