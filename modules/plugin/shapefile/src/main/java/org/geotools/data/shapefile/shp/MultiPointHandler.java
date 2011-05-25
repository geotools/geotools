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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;

/**
 * 
 * @author aaime
 * @author Ian Schneider
 *
 * @source $URL$
 * 
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
        if ((type != ShapeType.MULTIPOINT) && (type != ShapeType.MULTIPOINTM)
                && (type != ShapeType.MULTIPOINTZ)) {
            throw new ShapefileException(
                    "Multipointhandler constructor - expected type to be 8, 18, or 28");
        }

        shapeType = type;
        this.geometryFactory = gf;
    }
    
    /**
     * Returns the shapefile shape type value for a point
     * 
     * @return int Shapefile.POINT
     */
    public ShapeType getShapeType() {
        return shapeType;
    }

    /**
     * Calcuates the record length of this object.
     * 
     * @return int The length of the record that this shapepoint will take up in
     *         a shapefile
     */
    public int getLength(Object geometry) {
        MultiPoint mp = (MultiPoint) geometry;

        int length;

        if (shapeType == ShapeType.MULTIPOINT) {
            // two doubles per coord (16 * numgeoms) + 40 for header
            length = (mp.getNumGeometries() * 16) + 40;
        } else if (shapeType == ShapeType.MULTIPOINTM) {
            // add the additional MMin, MMax for 16, then 8 per measure
            length = (mp.getNumGeometries() * 16) + 40 + 16
                    + (8 * mp.getNumGeometries());
        } else if (shapeType == ShapeType.MULTIPOINTZ) {
            // add the additional ZMin,ZMax, plus 8 per Z
            length = (mp.getNumGeometries() * 16) + 40 + 16
                    + (8 * mp.getNumGeometries()) + 16
                    + (8 * mp.getNumGeometries());
        } else {
            throw new IllegalStateException("Expected ShapeType of Arc, got "
                    + shapeType);
        }

        return length;
    }

    private Object createNull() {
        Coordinate[] c = null;
        return geometryFactory.createMultiPoint(c);
    }

    public Object read(ByteBuffer buffer, ShapeType type, boolean flatGeometry) {
        if (type == ShapeType.NULL) {
            return createNull();
        }

        // read bounding box (not needed)
        buffer.position(buffer.position() + 4 * 8);

        int numpoints = buffer.getInt();
        int dimensions = shapeType == shapeType.MULTIPOINTZ && !flatGeometry ? 3 : 2;
        CoordinateSequence cs = geometryFactory.getCoordinateSequenceFactory().create(numpoints, dimensions);

        DoubleBuffer dbuffer = buffer.asDoubleBuffer();
        double[] ordinates = new double[numpoints * 2];
        dbuffer.get(ordinates);
        for (int t = 0; t < numpoints; t++) {
            cs.setOrdinate(t, 0, ordinates[t * 2]);
            cs.setOrdinate(t, 1, ordinates[t * 2 + 1]);
        }

        if (dimensions > 2) {
            dbuffer.position(dbuffer.position() + 2);

            dbuffer.get(ordinates, 0, numpoints);
            for (int t = 0; t < numpoints; t++) {
                cs.setOrdinate(t, 2, ordinates[t]); // z
            }
        }

        return geometryFactory.createMultiPoint(cs);
    }

    public void write(ByteBuffer buffer, Object geometry) {
        MultiPoint mp = (MultiPoint) geometry;

        Envelope box = mp.getEnvelopeInternal();
        buffer.putDouble(box.getMinX());
        buffer.putDouble(box.getMinY());
        buffer.putDouble(box.getMaxX());
        buffer.putDouble(box.getMaxY());

        buffer.putInt(mp.getNumGeometries());

        for (int t = 0, tt = mp.getNumGeometries(); t < tt; t++) {
            Coordinate c = (mp.getGeometryN(t)).getCoordinate();
            buffer.putDouble(c.x);
            buffer.putDouble(c.y);
        }

        if (shapeType == ShapeType.MULTIPOINTZ) {
            double[] zExtreame = JTSUtilities.zMinMax(mp.getCoordinates());

            if (Double.isNaN(zExtreame[0])) {
                buffer.putDouble(0.0);
                buffer.putDouble(0.0);
            } else {
                buffer.putDouble(zExtreame[0]);
                buffer.putDouble(zExtreame[1]);
            }

            for (int t = 0; t < mp.getNumGeometries(); t++) {
                Coordinate c = (mp.getGeometryN(t)).getCoordinate();
                double z = c.z;

                if (Double.isNaN(z)) {
                    buffer.putDouble(0.0);
                } else {
                    buffer.putDouble(z);
                }
            }
        }

        if (shapeType == ShapeType.MULTIPOINTM
                || shapeType == ShapeType.MULTIPOINTZ) {
            buffer.putDouble(-10E40);
            buffer.putDouble(-10E40);

            for (int t = 0; t < mp.getNumGeometries(); t++) {
                buffer.putDouble(-10E40);
            }
        }
    }

}
