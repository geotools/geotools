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

/** Returns "bold" or "normal" based on the font name */
public class MapBoxFontWeightFunction extends FunctionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "mbFontWeight",
                    parameter("weight", Boolean.class),
                    parameter("fontName", Integer.class));

    public MapBoxFontWeightFunction() {
        this.functionName = NAME;
    }

    @Override
    public Object evaluate(Object object) {
        String name = getParameters().get(0).evaluate(object, String.class);
        if (name == null) {
            throw new IllegalArgumentException("First argument should be a font name");
        }

        return new FontAttributesExtractor(name).isBold() ? "bold" : "normal";
    }
}
