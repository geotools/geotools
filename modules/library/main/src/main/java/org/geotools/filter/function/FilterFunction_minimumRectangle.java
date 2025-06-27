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
 * A FilterFunction that expects a Geometry and returns it's minimum rectangle.
 *
 * @author Jared Erickson
 */
public class FilterFunction_minimumRectangle extends FunctionExpressionImpl {

    /** The FunctionName */
    public static FunctionName NAME =
            new FunctionNameImpl("minrectangle", Geometry.class, parameter("geometry", Geometry.class));

    /** Create a new FilterFunction_minimumRectangle instance */
    public FilterFunction_minimumRectangle() {
        super(NAME);
    }

    /**
     * Calculate the Geometry's minimum rectangle.
     *
     * @param feature The feature should be a Geometry
     * @return The minimum rectangle Geometry
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
                    "Filter Function problem for function minimum rectangle argument #0 - expected type Geometry");
        }

        return StaticGeometry.minimumRectangle(arg0);
    }
}
