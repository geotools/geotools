/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.locationtech.jts.geom.Geometry;

/**
 * A FilterFunction that expects a Geometry and returns it's octagonal envelope.
 *
 * @author Jared Erickson
 */
public class FilterFunction_octagonalEnvelope extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl("octagonalenvelope", Geometry.class, parameter("geometry", Geometry.class));

    /** Create a new FilterFunction_octagonalEnvelope instance */
    public FilterFunction_octagonalEnvelope() {
        super(NAME);
    }

    /**
     * Calculate the Geometry's octagonal envelope.
     *
     * @param feature The feature should be a Geometry
     * @return The octagonal envelope Geometry
     * @throws IllegalArgumentException if the feature is not a Geometry
     */
    @Override
    public Object evaluate(Object feature) {
        Geometry arg0;

        // attempt to get value and perform conversion
        try {
            arg0 = (Geometry) getExpression(0).evaluate(feature);
        } catch (Exception e) {
            // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function octagonal envelope argument #0 - expected type Geometry");
        }

        return StaticGeometry.octagonalEnvelope(arg0);
    }
}
