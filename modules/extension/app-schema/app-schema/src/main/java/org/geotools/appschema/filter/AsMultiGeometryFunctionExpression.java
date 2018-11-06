/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.filter;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.Attribute;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Function which wraps an instance of geometry in its associatd multi geometry type.
 *
 * <p>
 *
 * <ul>
 *   <li>Point -> MultiPoint
 *   <li>LineString -> MultiLineString
 *   <li>Polygon -> MultiPolygon
 * </ul>
 *
 * <br>
 * <br>
 * This function takes a single argument expression which must evaluate to an instanceof {@link
 * org.locationtech.jts.geom.Geometry}.
 *
 * @author Justin Deoliveira (The Open Planning Project)
 */
public class AsMultiGeometryFunctionExpression extends FunctionExpressionImpl {

    public static FunctionName NAME =
            new FunctionNameImpl(
                    "asMultiGeometry",
                    parameter("multi-geometry", Geometry.class),
                    parameter("geometry", Geometry.class));

    public AsMultiGeometryFunctionExpression() {
        super(NAME);
    }

    public Object evaluate(Object obj) {
        if (!(obj instanceof Attribute)) {
            return null;
        }
        Attribute att = (Attribute) obj;
        org.opengis.filter.expression.Expression arg = (Expression) getParameters().get(0);
        Object value = arg.evaluate(att);

        if (value != null) {
            if (value instanceof Geometry) {
                if (value instanceof GeometryCollection) {
                    return value;
                }

                return wrap((Geometry) value);
            } else {
                throw new IllegalArgumentException(
                        "function argument did not evaluate to " + Geometry.class);
            }
        }

        return null;
    }

    private GeometryCollection wrap(Geometry geometry) {
        if (geometry instanceof Point) {
            return geometry.getFactory().createMultiPoint(new Point[] {(Point) geometry});
        } else if (geometry instanceof LineString) {
            return geometry.getFactory()
                    .createMultiLineString(new LineString[] {(LineString) geometry});
        } else if (geometry instanceof Polygon) {
            return geometry.getFactory().createMultiPolygon(new Polygon[] {(Polygon) geometry});
        }

        throw new IllegalArgumentException("Unable to create multi geometry from " + geometry);
    }
}
