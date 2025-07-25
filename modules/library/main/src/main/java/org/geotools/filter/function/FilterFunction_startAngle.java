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

// this code is autogenerated - you shouldnt be modifying it!

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.api.filter.capability.FunctionName;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.LineString;

public class FilterFunction_startAngle extends FunctionExpressionImpl {
    public static FunctionName NAME = new FunctionNameImpl(
            "startAngle", parameter("degrees", Double.class), parameter("linestring", LineString.class));

    public FilterFunction_startAngle() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        LineString ls;

        try { // attempt to get value and perform conversion
            ls = getExpression(0).evaluate(feature, LineString.class);
        } catch (Exception e) {
            // probably a type error
            throw new IllegalArgumentException(
                    "Filter Function problem for function startAngle argument #0 - expected type Geometry");
        }
        if (ls == null || ls.getNumPoints() == 1) {
            return null;
        }

        CoordinateSequence cs = ls.getCoordinateSequence();

        double dx = cs.getX(1) - cs.getX(0);
        double dy = cs.getY(1) - cs.getY(0);
        return -Math.toDegrees(Math.atan2(dy, dx));
    }
}
