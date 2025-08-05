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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

public class MidPointFunction extends FunctionExpressionImpl {
    public static FunctionName NAME = new FunctionNameImpl(
            "midPoint", parameter("point", Point.class), parameter("linestring", LineString.class));

    public MidPointFunction() {
        super(NAME);
    }

    @Override
    public Point evaluate(Object feature) {
        LineString ls = getExpression(0).evaluate(feature, LineString.class);

        LineStringCursor cursor = new LineStringCursor(ls);
        cursor.moveTo(cursor.getLineStringLength() / 2);
        Coordinate midCoordinate = cursor.getCurrentPosition();
        return ls.getFactory().createPoint(midCoordinate);
    }
}
