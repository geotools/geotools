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
public class MapBoxAlphaFunction extends FunctionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "mbAlpha",
                    parameter("alpha", Double.class),
                    parameter("color", Color.class));

    public MapBoxAlphaFunction() {
        this.functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        Color color = getParameters().get(0).evaluate(object, Color.class);
        if (color == null) {
            return 1;
        }

        return color.getAlpha() / 255d;
    }
}
