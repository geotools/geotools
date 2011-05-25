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
package org.geotools.renderer;

import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * The screenmap is a packed bitmap of the screen, one bit per pixels. It can be used to avoid
 * rendering a lot of very small features in the same pixel.
 * 
 * <p>
 * The screenmap can be used two ways:
 * <ul>
 * <li>By working directly against the pixels using {@link #checkAndSet(int, int)}</li>
 * <li>By working with real world envelopes using {@link #checkAndSet(Envelope)}, in that case the
 * full math transform from data to screen, and the generalization spans must be set</li>
 * </ul>
 * 
 * When checkAndSet returns false the geometry sits in a pixel that has been already populated
 * and can be skipped.
 * 
 * @author jeichar
 * @author Andrea Aime - OpenGeo
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/renderer/ScreenMap.java $
 */
public class ScreenMap {
    double[] point = new double[2];

    int[] pixels;

    int width;

    int height;

    private int minx;

    private int miny;

    MathTransform mt;

    double spanX;

    double spanY;

    public ScreenMap(int x, int y, int width, int height, MathTransform mt) {
        this.width = width;
        this.height = height;
        this.minx = x;
        this.miny = y;

        int arraySize = ((width * height) / 32) + 1;
        pixels = new int[arraySize];
        this.mt = mt;
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
     * Checks if the geometry should be skipped. If the test returns true it means the geometry
     * sits in a pixel that has already been used
     */
    public boolean checkAndSet(int x, int y) {
        if ((x - minx) < 0 || (x - minx) > width - 1 || (y - miny) < 0 || (y - miny) > height - 1)
            return true;
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

    /**
     * Returns true if the pixel at location x,y is set or out of bounds.
     */
    public boolean get(int x, int y) {
        if ((x - minx) < 0 || (x - minx) > width - 1 || (y - miny) < 0 || (y - miny) > height - 1)
            return true;
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

    private int bit(int x, int y) {
        return (width * y) + x;
    }

    /**
     * Returns geometry suitable for rendering the pixel that has just been occupied.
     * The geometry is designed to actually fill the pixel
     * @param minx
     * @param miny
     * @param maxx
     * @param maxy
     * @param geometryFactory
     * @param geometryType
     * @return
     */
    public Geometry getSimplifiedShape(double minx, double miny, double maxx, double maxy,
            GeometryFactory geometryFactory, Class geometryType) {
        CoordinateSequenceFactory csf = geometryFactory.getCoordinateSequenceFactory();
        double midx = (minx + maxx) / 2;
        double midy = (miny + maxy) / 2;
        double x0 = midx - spanX / 2;
        double x1 = midx + spanX / 2;
        double y0 = midy - spanY / 2;
        double y1 = midy + spanY / 2;
        if (Point.class.isAssignableFrom(geometryType)
                || MultiPoint.class.isAssignableFrom(geometryType)) {
            CoordinateSequence cs = csf.create(1, 2);
            cs.setOrdinate(0, 0, midx);
            cs.setOrdinate(0, 1, midy);
            if (Point.class.isAssignableFrom(geometryType)) {
                // people should not call this method for a point, but... whatever
                return geometryFactory.createPoint(cs);
            } else {
                return geometryFactory.createMultiPoint(new Point[] { geometryFactory
                        .createPoint(cs) });
            }
        } else if (LineString.class.isAssignableFrom(geometryType)
                || MultiLineString.class.isAssignableFrom(geometryType)) {
            CoordinateSequence cs = csf.create(2, 2);
            cs.setOrdinate(0, 0, x0);
            cs.setOrdinate(0, 1, y0);
            cs.setOrdinate(1, 0, x1);
            cs.setOrdinate(1, 1, y1);
            if (MultiLineString.class.isAssignableFrom(geometryType)) {
                return geometryFactory.createMultiLineString(new LineString[] { geometryFactory
                        .createLineString(cs) });
            } else {
                return geometryFactory.createLineString(cs);
            }
        } else {
            CoordinateSequence cs = csf.create(5, 2);
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
                return geometryFactory.createMultiPolygon(new Polygon[] { geometryFactory
                        .createPolygon(ring, null) });
            } else {
                return geometryFactory.createPolygon(ring, null);
            }
        }
    }
    
    /**
     * Sets location at position x,y to the value.
     */
    public void set(int x, int y, boolean value) {
        if ((x - minx) < 0 || (x - minx) > width - 1 || (y - miny) < 0 || (y - miny) > height - 1)
            return;
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

}
