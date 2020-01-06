/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.filter.FunctionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.opengis.filter.capability.FunctionName;

import java.awt.*;

/** Returns a font base name */
public class MapBoxRGBAFunction extends FunctionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "mbRGBA",
                    parameter("alpha", Double.class),
                    parameter("red", Integer.class),
                    parameter("green", Integer.class),
                    parameter("blue", Integer.class),
                    parameter("alpha", Integer.class));

    public MapBoxRGBAFunction() {
        this.functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        Integer red = getParameters().get(0).evaluate(object, Integer.class);
        if (red == null) {
            throw new IllegalArgumentException(
                    "Could not convert red component to an integer value.");
        }
        Integer green = getParameters().get(1).evaluate(object, Integer.class);
        if (green == null) {
            throw new IllegalArgumentException(
                    "Could not convert green component to an integer value.");
        }
        Integer blue = getParameters().get(2).evaluate(object, Integer.class);
        if (blue == null) {
            throw new IllegalArgumentException(
                    "Could not convert blue component to an integer value.");
        }
        Double alpha = getParameters().get(3).evaluate(object, Double.class);
        if (alpha == null) {
            throw new IllegalArgumentException(
                    "Could not convert alpha component to an floating point value.");
        }

        return new Color(red, green, blue, (int) Math.round(255 * alpha));
    }
}
