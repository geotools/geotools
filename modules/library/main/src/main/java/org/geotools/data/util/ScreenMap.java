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
package org.geotools.data.util;

import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * The screenmap is a packed bitmap of the screen, one bit per pixels. It can be used to avoid
 * rendering a lot of very small features in the same pixel.
 *
 * <p>The screenmap can be used two ways:
 *
 * <ul>
 *   <li>By working directly against the pixels using {@link #checkAndSet(int, int)}
 *   <li>By working with real world envelopes using {@link #checkAndSet(Envelope)}, in that case the
 *       full math transform from data to screen, and the generalization spans must be set
 * </ul>
 *
 * When checkAndSet returns false the geometry sits in a pixel that has been already populated and
 * can be skipped.
 *
 * @author jeichar
 * @author Andrea Aime - OpenGeo
 */
public class ScreenMap {

    double[] point = new double[2];

    int width;

    int height;

    private int minx;

    private int miny;

    MathTransform mt;

    double spanX;

    double spanY;

    BitFieldMatrix bitfield;

    public ScreenMap(int x, int y, int width, int height, MathTransform mt) {
        this.width = width;
        this.height = height;
        this.minx = x;
        this.miny = y;

        this.mt = mt;
    }

    /** Returns the bitfield, azyly instantiating it as needed */
    private BitFieldMatrix getBitField() {
        if (this.bitfield == null) {
            this.bitfield = new BitFieldMatrix();
        }

        return bitfield;
    }

    public ScreenMap(ScreenMap original, int expandBy) {
        this(
                original.minx - expandBy,
                original.miny - expandBy,
                original.width + expandBy * 2,
                original.height + expandBy * 2);
    }

    public ScreenMap(int x, int y, int width, int height) {
        this(x, y, width, height, null);
    }

    public void setTransform(MathTransform mt) {
        this.mt = mt;
    }

    public boolean checkAndSet(Envelope envelope) throws TransformException {
        if (!canSimplify(envelope)) {
            return false;
        }

        point[0] = (envelope.getMinX() + envelope.getMaxX()) / 2;
        point[1] = (envelope.getMinY() + envelope.getMaxY()) / 2;
        mt.transform(point, 0, point, 0, 1);
        int r = (int) point[0];
        int c = (int) point[1];
        return checkAndSet(r, c);
    }

    public boolean canSimplify(Envelope envelope) {
        return envelope.getWidth() < spanX && envelope.getHeight() < spanY;
    }

    public void setSpans(double spanX, double spanY) {
        this.spanX = spanX;
        this.spanY = spanY;
    }

    /**
     * Checks if the geometry should be skipped. If the test returns true it means the geometry sits
     * in a pixel that has already been used
     */
    public boolean checkAndSet(int x, int y) {
        return getBitField().checkAndSet(x, y);
    }

    public boolean get(Envelope envelope) throws TransformException {
        if (!canSimplify(envelope)) {
            return false;
        }

        point[0] = (envelope.getMinX() + envelope.getMaxX()) / 2;
        point[1] = (envelope.getMinY() + envelope.getMaxY()) / 2;
        mt.transform(point, 0, point, 0, 1);
        int r = (int) point[0];
        int c = (int) point[1];
        return get(r, c);
    }

    /** Returns true if the pixel at location x,y is set or out of bounds. */
    public boolean get(int x, int y) {
        return getBitField().get(x, y);
    }

    /**
     * Returns geometry suitable for rendering the pixel that has just been occupied. The geometry
     * is designed to actually fill the pixel
     */
    public Geometry getSimplifiedShape(Geometry geometry) {
        Envelope envelope = geometry.getEnvelopeInternal();
        return getSimplifiedShape(
                envelope.getMinX(),
                envelope.getMinY(),
                envelope.getMaxX(),
                envelope.getMaxY(),
                geometry.getFactory(),
                geometry.getClass());
    }

    /**
     * Returns geometry suitable for rendering the pixel that has just been occupied. The geometry
     * is designed to actually fill the pixel
     */
    public Geometry getSimplifiedShape(
            double minx,
            double miny,
            double maxx,
            double maxy,
            GeometryFactory geometryFactory,
            Class geometryType) {
        CoordinateSequenceFactory csf = geometryFactory.getCoordinateSequenceFactory();
        double midx = (minx + maxx) / 2;
        double midy = (miny + maxy) / 2;
        double x0 = midx - spanX / 2;
        double x1 = midx + spanX / 2;
        double y0 = midy - spanY / 2;
        double y1 = midy + spanY / 2;
        if (Point.class.isAssignableFrom(geometryType)
                || MultiPoint.class.isAssignableFrom(geometryType)) {
            CoordinateSequence cs = JTS.createCS(csf, 1, 2);
            cs.setOrdinate(0, 0, midx);
            cs.setOrdinate(0, 1, midy);
            if (Point.class.isAssignableFrom(geometryType)) {
                // people should not call this method for a point, but... whatever
                return geometryFactory.createPoint(cs);
            } else {
                return geometryFactory.createMultiPoint(
                        new Point[] {geometryFactory.createPoint(cs)});
            }
        } else if (LineString.class.isAssignableFrom(geometryType)
                || MultiLineString.class.isAssignableFrom(geometryType)) {
            CoordinateSequence cs = JTS.createCS(csf, 2, 2);
            cs.setOrdinate(0, 0, x0);
            cs.setOrdinate(0, 1, y0);
            cs.setOrdinate(1, 0, x1);
            cs.setOrdinate(1, 1, y1);
            if (MultiLineString.class.isAssignableFrom(geometryType)) {
                return geometryFactory.createMultiLineString(
                        new LineString[] {geometryFactory.createLineString(cs)});
            } else {
                return geometryFactory.createLineString(cs);
            }
        } else {
            CoordinateSequence cs = JTS.createCS(csf, 5, 2);
            cs.setOrdinate(0, 0, x0);
            cs.setOrdinate(0, 1, y0);
            cs.setOrdinate(1, 0, x0);
            cs.setOrdinate(1, 1, y1);
            cs.setOrdinate(2, 0, x1);
            cs.setOrdinate(2, 1, y1);
            cs.setOrdinate(3, 0, x1);
            cs.setOrdinate(3, 1, y0);
            cs.setOrdinate(4, 0, x0);
            cs.setOrdinate(4, 1, y0);
            LinearRing ring = geometryFactory.createLinearRing(cs);
            if (MultiPolygon.class.isAssignableFrom(geometryType)) {
                return geometryFactory.createMultiPolygon(
                        new Polygon[] {geometryFactory.createPolygon(ring, null)});
            } else {
                return geometryFactory.createPolygon(ring, null);
            }
        }
    }

    /** Sets location at position x,y to the value. */
    public void set(int x, int y, boolean value) {
        getBitField().set(x, y, value);
    }

    /**
     * Incapsulates the bitfield representation and access logic, allows for lazy creation of the
     * bitfield at the first time we actually need to use it (only fairly zoomed in requestes not
     * pixel might ever be set)
     */
    final class BitFieldMatrix {
        int[] pixels;

        public BitFieldMatrix() {
            int arraySize = ((width * height) / 32) + 1;
            pixels = new int[arraySize];
        }

        public boolean checkAndSet(int x, int y) {
            // if it's outside of the screenmap we cannot say whether it's busy or not, and
            // we cannot skip it because rendering or geometry transformation might put the geometry
            // right in the map
            if ((x - minx) < 0
                    || (x - minx) > width - 1
                    || (y - miny) < 0
                    || (y - miny) > height - 1) return false;
            int bit = bit(x - minx, y - miny);
            int index = bit / 32;
            int offset = bit % 32;
            int mask = 1 << offset;

            try {
                if ((pixels[index] & mask) != 0) {
                    return true;
                } else {
                    pixels[index] = pixels[index] | mask;
                    return false;
                }
            } catch (Exception e) {
                return true;
            }
        }

        public boolean get(int x, int y) {
            // if it's outside of the screenmap we cannot say whether it's busy or not, and
            // we cannot skip it because rendering or geometry transformation might put the geometry
            // right in the map
            if ((x - minx) < 0
                    || (x - minx) > width - 1
                    || (y - miny) < 0
                    || (y - miny) > height - 1) return false;
            int bit = bit(x - minx, y - miny);
            int index = bit / 32;
            int offset = bit % 32;
            int mask = 1 << offset;

            try {
                return ((pixels[index] & mask) != 0) ? true : false;
            } catch (Exception e) {

                return true;
            }
        }

        public void set(int x, int y, boolean value) {
            if ((x - minx) < 0
                    || (x - minx) > width - 1
                    || (y - miny) < 0
                    || (y - miny) > height - 1) return;
            int bit = bit(x - minx, y - miny);
            int index = bit / 32;
            int offset = bit % 32;
            int mask = 1;
            mask = mask << offset;

            if (value) {
                pixels[index] = pixels[index] | mask;
            } else {
                int tmp = pixels[index];
                tmp = ~tmp;
                tmp = (tmp | mask);
                tmp = ~tmp;
                pixels[index] = tmp;
            }
        }

        private int bit(int x, int y) {
            return (width * y) + x;
        }
    }
}
