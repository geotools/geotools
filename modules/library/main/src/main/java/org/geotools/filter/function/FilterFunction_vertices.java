/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.opengis.filter.capability.FunctionName;

public class FilterFunction_vertices extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "vertices",
                    parameter("vertices", MultiPoint.class),
                    parameter("geometry", Geometry.class));

    public FilterFunction_vertices() {
        super(NAME);
    }

    public Object evaluate(Object feature, Class context) {
        Geometry g = getExpression(0).evaluate(feature, Geometry.class);
        if (g == null) return null;

        MultiPointExtractor filter = new MultiPointExtractor();
        g.apply(filter);
        return filter.getMultiPoint();
    }

    static class MultiPointExtractor implements CoordinateFilter {
        List<Coordinate> coordinates = new ArrayList();

        public void filter(Coordinate c) {
            coordinates.add(c);
        }

        MultiPoint getMultiPoint() {
            Coordinate[] coorArray = coordinates.toArray(new Coordinate[coordinates.size()]);
            return new GeometryFactory().createMultiPoint(new CoordinateArraySequence(coorArray));
        }
    }
}
