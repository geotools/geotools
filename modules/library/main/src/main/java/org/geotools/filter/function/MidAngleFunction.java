/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.geometry.jts.LineStringCursor;
import org.locationtech.jts.geom.LineString;

public class MidAngleFunction extends FunctionExpressionImpl {
    public static FunctionName NAME = new FunctionNameImpl(
            "midAngle", parameter("degrees", Double.class), parameter("linestring", LineString.class));

    public MidAngleFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object feature) {
        LineString ls = getExpression(0).evaluate(feature, LineString.class);
        if (ls == null || ls.getNumPoints() == 1) {
            return null;
        }

        LineStringCursor cursor = new LineStringCursor(ls);
        cursor.moveTo(cursor.getLineStringLength() / 2);
        double radians = cursor.getCurrentAngle();
        return -Math.toDegrees(radians);
    }
}
