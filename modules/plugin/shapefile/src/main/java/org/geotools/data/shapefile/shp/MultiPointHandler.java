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
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

/**
 * @author aaime
 * @author Ian Schneider
 */
public class MultiPointHandler implements ShapeHandler {
    final ShapeType shapeType;
    GeometryFactory geometryFactory;

    /** Creates new MultiPointHandler */
    public MultiPointHandler(GeometryFactory gf) {
        shapeType = ShapeType.POINT;
        this.geometryFactory = gf;
    }

    public MultiPointHandler(ShapeType type, GeometryFactory gf) throws ShapefileException {
        if (!type.isMultiPointType()) {
            throw new ShapefileException("Multipointhandler constructor - expected type to be 8, 18, or 28");
        }

        shapeType = type;
        this.geometryFactory = gf;
    }

    /**
     * Returns the shapefile shape type value for a point
     *
     * @return int Shapefile.MULTIPOINT
     */
    @Override
    public ShapeType getShapeType() {
        return shapeType;
    }

    /**
     * Calcuates the record length of this object.
     *
     * @return int The length of the record that this shapepoint will take up in a shapefile
     */
    @Override
    public int getLength(Object geometry) {
        MultiPoint mp = (MultiPoint) geometry;

        int length;

        if (shapeType == ShapeType.MULTIPOINT) {
            // two doubles per coord (16 * numgeoms) + 40 for header
            length = mp.getNumGeometries() * 16 + 40;
        } else if (shapeType == ShapeType.MULTIPOINTM) {
            // add the additional MMin, MMax for 16, then 8 per measure
            length = mp.getNumGeometries() * 16 + 40 + 16 + 8 * mp.getNumGeometries();
        } else if (shapeType == ShapeType.MULTIPOINTZ) {
            // add the additional ZMin,ZMax, plus 8 per Z
            length = mp.getNumGeometries() * 16 + 40 + 16 + 8 * mp.getNumGeometries() + 16 + 8 * mp.getNumGeometries();
        } else {
            throw new IllegalStateException("Expected ShapeType of Arc, got " + shapeType);
        }

        return length;
    }

    private Object createNull() {
        return geometryFactory.createMultiPoint((CoordinateSequence) null);
    }

    @Override
    public Object read(ByteBuffer buffer, ShapeType type, boolean flatGeometry) {
        if (type == ShapeType.NULL) {
            return createNull();
        }

        // read bounding box (not needed)
        buffer.position(buffer.position() + 4 * 8);

        int numpoints = buffer.getInt();
        int dimensions = shapeType == ShapeType.MULTIPOINTZ && !flatGeometry ? 3 : 2;
        int measure = flatGeometry ? 0 : 1;
        CoordinateSequence cs;
        if (shapeType == ShapeType.MULTIPOINTZ || shapeType == ShapeType.MULTIPOINTM) {
            cs = JTS.createCS(geometryFactory.getCoordinateSequenceFactory(), numpoints, dimensions + measure, measure);
        } else {
            cs = JTS.createCS(geometryFactory.getCoordinateSequenceFactory(), numpoints, dimensions);
        }

        DoubleBuffer dbuffer = buffer.asDoubleBuffer();
        double[] ordinates = new double[numpoints * 2];
        dbuffer.get(ordinates);
        for (int t = 0; t < numpoints; t++) {
            cs.setOrdinate(t, CoordinateSequence.X, ordinates[t * 2]);
            cs.setOrdinate(t, CoordinateSequence.Y, ordinates[t * 2 + 1]);
        }

        if (shapeType == ShapeType.MULTIPOINTZ && !flatGeometry) {
            dbuffer.position(dbuffer.position() + 2);

            dbuffer.get(ordinates, 0, numpoints);
            for (int t = 0; t < numpoints; t++) {
                cs.setOrdinate(t, CoordinateSequence.Z, ordinates[t]); // z
            }
        }

        boolean isArcZWithM = shapeType == ShapeType.MULTIPOINTZ && dbuffer.remaining() >= numpoints + 2;
        if ((isArcZWithM || shapeType == ShapeType.MULTIPOINTM) && !flatGeometry) {
            dbuffer.position(dbuffer.position() + 2);

            dbuffer.get(ordinates, 0, numpoints);
            for (int t = 0; t < numpoints; t++) {
                // Page 2 of the spec says that values less than 10E-38 are
                // NaNs
                if (ordinates[t] < -10e38) {
                    ordinates[t] = Double.NaN;
                }
                cs.setOrdinate(t, CoordinateSequence.M, ordinates[t]); // m
            }
        }

        return geometryFactory.createMultiPoint(cs);
    }

    @Override
    public void write(ByteBuffer buffer, Object geometry) {
        MultiPoint mp = (MultiPoint) geometry;

        Envelope box = mp.getEnvelopeInternal();
        buffer.putDouble(box.getMinX());
        buffer.putDouble(box.getMinY());
        buffer.putDouble(box.getMaxX());
        buffer.putDouble(box.getMaxY());

        buffer.putInt(mp.getNumGeometries());

        for (int t = 0, tt = mp.getNumGeometries(); t < tt; t++) {
            Coordinate c = mp.getGeometryN(t).getCoordinate();
            buffer.putDouble(c.x);
            buffer.putDouble(c.y);
        }

        if (shapeType == ShapeType.MULTIPOINTZ) {
            double[] result = {Double.NaN, Double.NaN};
            JTSUtilities.zMinMax(new CoordinateArraySequence(mp.getCoordinates()), result);
            double[] zExtreame = result;

            if (Double.isNaN(zExtreame[0])) {
                buffer.putDouble(0.0);
                buffer.putDouble(0.0);
            } else {
                buffer.putDouble(zExtreame[0]);
                buffer.putDouble(zExtreame[1]);
            }

            for (int t = 0; t < mp.getNumGeometries(); t++) {
                Coordinate c = mp.getGeometryN(t).getCoordinate();
                double z = c.getZ();

                if (Double.isNaN(z)) {
                    buffer.putDouble(0.0);
                } else {
                    buffer.putDouble(z);
                }
            }
        }
        // if have M coordinates
        if (shapeType == ShapeType.MULTIPOINTM || shapeType == ShapeType.MULTIPOINTZ) {
            // obtain all M values
            List<Double> mvalues = new ArrayList<>();
            for (int t = 0, tt = mp.getNumGeometries(); t < tt; t++) {
                Point point = (Point) mp.getGeometryN(t);
                mvalues.add(point.getCoordinateSequence().getM(0));
            }
            // min, max
            buffer.putDouble(mvalues.stream().min(Double::compare).get());
            buffer.putDouble(mvalues.stream().max(Double::compare).get());
            // encode all M values
            mvalues.forEach(x -> {
                buffer.putDouble(x);
            });
        }
    }
}
