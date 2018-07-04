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
 * Shade lesscss.org color function. Takes one colors and mixes it with black based on a weight (and
 * their eventual alpha)
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ShadeFunction extends FunctionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "shade",
                    parameter("result", Color.class),
                    parameter("color", Color.class),
                    parameter("weight", Double.class));
    private MixFunction delegate;

    public ShadeFunction() {
        this.functionName = NAME;
        this.delegate = new MixFunction();
    }

    @Override
    public Object evaluate(Object object) {
        Color color = (Color) getParameterValue(object, 0);
        double weight = (Double) getParameterValue(object, 1);

        return delegate.mix(Color.BLACK, color, weight);
    }
}
