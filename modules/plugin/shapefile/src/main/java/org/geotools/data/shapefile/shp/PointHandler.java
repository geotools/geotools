/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.shp;

import java.nio.ByteBuffer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.CoordinateXYM;
import org.locationtech.jts.geom.CoordinateXYZM;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Wrapper for a Shapefile point.
 *
 * @author aaime
 * @author Ian Schneider
 */
public class PointHandler implements ShapeHandler {

    final ShapeType shapeType;
    GeometryFactory geometryFactory;

    public PointHandler(ShapeType type, GeometryFactory gf) throws ShapefileException {
        if (type != ShapeType.POINT && type != ShapeType.POINTM && type != ShapeType.POINTZ) { // 2d, 2d+m, 3d+m
            throw new ShapefileException("PointHandler constructor: expected a type of 1, 11 or 21");
        }

        shapeType = type;
        this.geometryFactory = gf;
    }

    public PointHandler() {
        shapeType = ShapeType.POINT; // 2d
    }

    /**
     * Returns the shapefile shape type value for a point
     *
     * @return int Shapefile.POINT
     */
    @Override
    public ShapeType getShapeType() {
        return shapeType;
    }

    @Override
    public int getLength(Object geometry) {
        int length;
        if (shapeType == ShapeType.POINT) {
            length = 20;
        } else if (shapeType == ShapeType.POINTM) {
            length = 28;
        } else if (shapeType == ShapeType.POINTZ) {
            length = 36;
        } else {
            throw new IllegalStateException("Expected ShapeType of Point, got" + shapeType);
        }
        return length;
    }

    @Override
    public Object read(ByteBuffer buffer, ShapeType type, boolean flatGeometry) {
        if (type == ShapeType.NULL) {
            return createNull();
        }

        Coordinate c;
        if (shapeType == ShapeType.POINTZ && !flatGeometry) {

            c = new CoordinateXYZM();
        } else if (shapeType == ShapeType.POINTM && !flatGeometry) {
            c = new CoordinateXYM();
        } else {
            c = new CoordinateXY();
        }
        c.setX(buffer.getDouble());
        c.setY(buffer.getDouble());

        if (shapeType == ShapeType.POINTM && !flatGeometry) {
            double m = buffer.getDouble();
            // Page 2 of the spec says that values less than 10E-38 are
            // NaNs
            if (m < -10e38) {
                m = Double.NaN;
            }
            c.setM(m);
        }

        if (shapeType == ShapeType.POINTZ && !flatGeometry) {
            c.setZ(buffer.getDouble());
            if (buffer.hasRemaining()) c.setM(buffer.getDouble());
        }

        return geometryFactory.createPoint(c);
    }

    private Object createNull() {
        return geometryFactory.createPoint(new Coordinate(Double.NaN, Double.NaN, Double.NaN));
    }

    @Override
    public void write(ByteBuffer buffer, Object geometry) {
        Point point = (Point) geometry;
        Coordinate c = point.getCoordinate();

        buffer.putDouble(c.x);
        buffer.putDouble(c.y);

        if (shapeType == ShapeType.POINTZ) {
            if (Double.isNaN(c.getZ())) { // nan means not defined
                buffer.putDouble(0.0);
            } else {
                buffer.putDouble(c.getZ());
            }
        }

        if (shapeType == ShapeType.POINTZ || shapeType == ShapeType.POINTM) {
            double m = point.getCoordinateSequence().getM(0);
            buffer.putDouble(Double.isNaN(m) ? 0.0 : m); // M
        }
    }
}
