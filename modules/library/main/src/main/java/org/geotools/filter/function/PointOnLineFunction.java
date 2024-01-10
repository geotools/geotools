/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.linearref.LengthIndexedLine;

/**
 * Returns a point on the provided line. The point is at the specified percentage of the line's
 * length. If no percentage is provided, the mid point will be returned. If the line is a collection
 * of lines, the first line is used.
 *
 * <p>No attempt is made to perform linear referencing based on a measure, althought this could be
 * implemented in the future.
 */
public class PointOnLineFunction extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "pointOnLine",
                    Point.class,
                    parameter("linestring", Geometry.class),
                    parameter("percentage", Double.class, 0, 1));

    public PointOnLineFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object) {
        Geometry geometry = getExpression(0).evaluate(object, Geometry.class);
        if (geometry == null) return null;

        // if it's a collection of one, return the first geometry
        if (geometry instanceof GeometryCollection) {
            GeometryCollection collection = (GeometryCollection) geometry;
            if (collection.getNumGeometries() == 0) return null;
            if (collection.getNumGeometries() > 1)
                throw new IllegalArgumentException(
                        "Expected a single geometry, got a collection of "
                                + collection.getNumGeometries()
                                + " geometries");
            geometry = collection.getGeometryN(0);
        }

        if (!(geometry instanceof LineString)) {
            throw new IllegalArgumentException(
                    "Expected a LineString, got a " + geometry.getGeometryType());
        }

        LineString line = (LineString) geometry;
        Double percentage = 0.5;
        if (getParameters().size() > 1) {
            Double d = getExpression(1).evaluate(object, Double.class);
            if (d != null) percentage = d;
        }

        if (percentage < 0 || percentage > 1) {
            throw new IllegalArgumentException(
                    "Expected a percentage between 0 and 1, got " + percentage);
        }

        LengthIndexedLine index = new LengthIndexedLine(line);
        double position = index.getEndIndex() * percentage;
        Coordinate c = index.extractPoint(position);
        return geometry.getFactory().createPoint(c);
    }
}
