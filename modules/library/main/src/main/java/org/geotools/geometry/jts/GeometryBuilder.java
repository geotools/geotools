/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;

/**
 * A builder for {@link Geometry} objects. Primarily intended to support fluent programming in test
 * code.
 *
 * <p>Features include:
 *
 * <ul>
 *   <li>Both 2D and 3D coordinate dimensions are supported (assuming the provided {@link
 *       CoordinateSequenceFactory} supports them)
 *   <li>Sequences of ordinate values can be supplied in a number of ways
 *   <li>Rings do not need to be explicitly closed; a closing point will be supplied if needed
 *   <li>Empty geometries of all types can be created
 *   <li>Composite geometries are validated to ensure they have a consistent GeometryFactory and
 *       coordinate sequence dimension
 *       <p>Examples of intended usage are:
 *       <pre>
 *   GeometryBuilder gb = new GeometryBuilder(geomFact);
 *   LineString line = gb.linestring(1,2, 3,4);
 *   Polygon poly = gb.polygon(0,0, 0,1, 1,1, 1,0);
 *   Polygon box = gb.box(0,0, 1,1);
 *   Polygon hexagon = gb.circle(0,0, 1,1, 6);
 *   Polygon polyhole = gb.polygon(gb.linearring(0,0, 0,10, 10,10, 10,0), gb.linearring(1,1, 1,9, 9,9, 9,1))
 * </pre>
 *
 * @author Martin Davis - OpenGeo
 */
public class GeometryBuilder {

    private GeometryFactory geomFact;

    private CoordinateSequenceFactory csFact;

    /** Create a new instance using the default {@link GeometryFactory}. */
    public GeometryBuilder() {
        this(new GeometryFactory());
    }

    /**
     * Creates a new instance using a provided GeometryFactory.
     *
     * @param geomFact the factory to use
     */
    public GeometryBuilder(GeometryFactory geomFact) {
        this.geomFact = geomFact;
        csFact = geomFact.getCoordinateSequenceFactory();
    }

    /**
     * Creates an empty Point
     *
     * @return an empty Point
     */
    public Point point() {
        return geomFact.createPoint(createCS(new double[0], 2));
    }

    /**
     * Creates an empty Point with coordinate dimension = 3.
     *
     * @return an empty Point
     */
    public Point pointZ() {
        return geomFact.createPoint(createCS(new double[0], 3));
    }

    /**
     * Creates a 1D Point.
     *
     * @param x the X ordinate
     * @return a Point
     */
    public Point point(double x) {
        return geomFact.createPoint(createCS(new double[] {x}, 1));
    }

    /**
     * Creates a 2D Point.
     *
     * @param x the X ordinate
     * @param y the Y ordinate
     * @return a Point
     */
    public Point point(double x, double y) {
        return geomFact.createPoint(createCS(new double[] {x, y}, 2));
    }

    /**
     * Creates a 3D Point.
     *
     * @param x the X ordinate
     * @param y the Y ordinate
     * @param z the Z ordinate
     * @return a Point
     */
    public Point pointZ(double x, double y, double z) {
        return geomFact.createPoint(createCS(new double[] {x, y, z}, 3));
    }

    /**
     * Creates an empty 2D LineString
     *
     * @return an empty LineString
     */
    public LineString lineString() {
        return geomFact.createLineString(createCS(new double[0], 2));
    }

    /**
     * Creates an empty 3D LineString
     *
     * @return an empty LineString
     */
    public LineString lineStringZ() {
        return geomFact.createLineString(createCS(new double[0], 3));
    }

    /**
     * Creates a 2D LineString.
     *
     * @param ord the XY ordinates
     * @return a LineString
     */
    public LineString lineString(double... ord) {
        return geomFact.createLineString(createCS(ord, 2));
    }

    /**
     * Creates a 3D LineString.
     *
     * @param ord the XYZ ordinates
     * @return a LineString
     */
    public LineString lineStringZ(double... ord) {
        return geomFact.createLineString(createCS(ord, 3));
    }

    /**
     * Creates an empty 2D LinearRing
     *
     * @return an empty LinearRing
     */
    public LinearRing linearRing() {
        return geomFact.createLinearRing(createRingCS(new double[0], 2));
    }

    /**
     * Creates an empty 3D LinearRing
     *
     * @return an empty LinearRing
     */
    public LinearRing linearRingZ() {
        return geomFact.createLinearRing(createRingCS(new double[0], 3));
    }

    /**
     * Creates a 2D LinearRing. If the supplied coordinate list is not closed, a closing coordinate
     * is added.
     *
     * @return a LinearRing
     */
    public LinearRing linearRing(double... ord) {
        return geomFact.createLinearRing(createRingCS(ord, 2));
    }

    /**
     * Creates a 3D LinearRing. If the supplied coordinate list is not closed, a closing coordinate
     * is added.
     *
     * @param ord the XYZ ordinates
     * @return a LinearRing
     */
    public LinearRing linearRingZ(double... ord) {
        return geomFact.createLinearRing(createRingCS(ord, 3));
    }

    /**
     * Creates an empty 2D Polygon.
     *
     * @return an empty Polygon
     */
    public Polygon polygon() {
        return geomFact.createPolygon(linearRing(), null);
    }

    /**
     * Creates an empty 3D Polygon.
     *
     * @return an empty Polygon
     */
    public Polygon polygonZ() {
        return geomFact.createPolygon(linearRingZ(), null);
    }

    /**
     * Creates a Polygon from a list of XY coordinates.
     *
     * @param ord a list of XY ordinates
     * @return a Polygon
     */
    public Polygon polygon(double... ord) {
        return geomFact.createPolygon(linearRing(ord), null);
    }

    /**
     * Creates a Polygon from a list of XYZ coordinates.
     *
     * @param ord a list of XYZ ordinates
     * @return a Polygon
     */
    public Polygon polygonZ(double... ord) {
        return geomFact.createPolygon(linearRingZ(ord), null);
    }

    /**
     * Creates a Polygon from an exterior ring. The coordinate dimension of the Polygon is the
     * dimension of the LinearRing.
     *
     * @param shell the exterior ring
     * @return a Polygon
     */
    public Polygon polygon(LinearRing shell) {
        return geomFact.createPolygon(shell, null);
    }

    /**
     * Creates a Polygon with a hole from an exterior ring and an interior ring.
     *
     * @param shell the exterior ring
     * @param hole the interior ring
     * @return a Polygon with a hole
     */
    public Polygon polygon(LinearRing shell, LinearRing hole) {
        return geomFact.createPolygon(shell, new LinearRing[] {hole});
    }

    /**
     * Creates a Polygon with a hole from an exterior ring and an interior ring supplied by the
     * rings of Polygons.
     *
     * @param shell the exterior ring
     * @param hole the interior ring
     * @return a Polygon with a hole
     */
    public Polygon polygon(Polygon shell, Polygon hole) {
        return geomFact.createPolygon(
                (LinearRing) shell.getExteriorRing(),
                new LinearRing[] {(LinearRing) hole.getExteriorRing()});
    }

    /**
     * Creates a rectangular 2D Polygon from X and Y bounds.
     *
     * @param x1 the lower X bound
     * @param y1 the lower Y bound
     * @param x2 the upper X bound
     * @param y2 the upper Y bound
     * @return a 2D Polygon
     */
    public Polygon box(double x1, double y1, double x2, double y2) {
        double[] ord = new double[] {x1, y1, x1, y2, x2, y2, x2, y1, x1, y1};
        return polygon(ord);
    }

    /**
     * Creates a rectangular 3D Polygon from X and Y bounds.
     *
     * @param x1 the lower X bound
     * @param y1 the lower Y bound
     * @param x2 the upper X bound
     * @param y2 the upper Y bound
     * @param z the Z value for all coordinates
     * @return a 3D Polygon
     */
    public Polygon boxZ(double x1, double y1, double x2, double y2, double z) {
        double[] ord = new double[] {x1, y1, z, x1, y2, z, x2, y2, z, x2, y1, z, x1, y1, z};
        return polygonZ(ord);
    }

    /**
     * Creates an elliptical Polygon from a bounding box with a given number of sides.
     *
     * @return a 2D Polygon
     */
    public Polygon ellipse(double x1, double y1, double x2, double y2, int nsides) {
        double rx = Math.abs(x2 - x1) / 2;
        double ry = Math.abs(y2 - y1) / 2;
        double cx = Math.min(x1, x2) + rx;
        double cy = Math.min(y1, y2) + ry;

        double[] ord = new double[2 * nsides + 2];
        double angInc = 2 * Math.PI / nsides;
        // create ring in CW order
        for (int i = 0; i < nsides; i++) {
            double ang = -(i * angInc);
            ord[2 * i] = cx + rx * Math.cos(ang);
            ord[2 * i + 1] = cy + ry * Math.sin(ang);
        }
        ord[2 * nsides] = ord[0];
        ord[2 * nsides + 1] = ord[1];
        return polygon(ord);
    }

    /**
     * Creates a circular Polygon with a given center, radius and number of sides.
     *
     * @param x the center X ordinate
     * @param y the center Y ordinate
     * @param radius the radius
     * @param nsides the number of sides
     * @return a 2D Polygon
     */
    public Polygon circle(double x, double y, double radius, int nsides) {
        return ellipse(x - radius, y - radius, x + radius, y + radius, nsides);
    }

    /**
     * Creates a MultiPoint with 2 2D Points.
     *
     * @param x1 the X ordinate of the first point
     * @param y1 the Y ordinate of the first point
     * @param x2 the X ordinate of the second point
     * @param y2 the Y ordinate of the second point
     * @return A MultiPoint
     */
    public MultiPoint multiPoint(double x1, double y1, double x2, double y2) {
        return geomFact.createMultiPoint(new Point[] {point(x1, y1), point(x2, y2)});
    }

    /**
     * Creates a MultiPoint with 2 3D Points.
     *
     * @param x1 the X ordinate of the first point
     * @param y1 the Y ordinate of the first point
     * @param z1 the Z ordinate of the first point
     * @param x2 the X ordinate of the second point
     * @param y2 the Y ordinate of the second point
     * @param z2 the Z ordinate of the second point
     * @return A 3D MultiPoint
     */
    public MultiPoint multiPointZ(
            double x1, double y1, double z1, double x2, double y2, double z2) {
        return geomFact.createMultiPoint(new Point[] {pointZ(x1, y1, z1), pointZ(x2, y2, z2)});
    }

    /**
     * Creates a MultiLineString from a set of LineStrings
     *
     * @param lines the component LineStrings
     * @return a MultiLineString
     */
    public MultiLineString multiLineString(LineString... lines) {
        return geomFact.createMultiLineString(lines);
    }

    /**
     * Creates a MultiPolygon from a set of Polygons.
     *
     * @param polys the component polygons
     * @return A MultiPolygon
     */
    public MultiPolygon multiPolygon(Polygon... polys) {
        return geomFact.createMultiPolygon(polys);
    }

    /**
     * Creates a GeometryCollection from a set of Geometrys
     *
     * @param geoms the component Geometrys
     * @return a GeometryCollection
     */
    public GeometryCollection geometryCollection(Geometry... geoms) {
        return geomFact.createGeometryCollection(geoms);
    }

    /**
     * Tests whether a sequence of ordinates of a given dimension is closed (i.e. has the first and
     * last coordinate identical).
     *
     * @param ord the list of ordinate values
     * @param dim the dimension of each coordinate
     * @return true if the sequence is closed
     */
    private boolean isClosed(double[] ord, int dim) {
        int n = ord.length / dim;
        if (n == 0) return true;

        int lastPos = dim * (n - 1);
        double lastx = ord[lastPos];
        double lasty = ord[lastPos + 1];
        boolean isClosed = lastx == ord[0] && lasty == ord[1];
        return isClosed;
    }

    /** */
    private CoordinateSequence createRingCS(double[] ord, int dim) {
        if (isClosed(ord, dim)) return createCS(ord, dim);
        double[] ord2 = new double[ord.length + dim];
        System.arraycopy(ord, 0, ord2, 0, ord.length);
        // copy first coord to last
        int lastPos = ord.length;
        for (int i = 0; i < dim; i++) {
            ord2[lastPos + i] = ord2[i];
        }
        return createCS(ord2, dim);
    }

    /** */
    private CoordinateSequence createCS(double[] ord, int dim) {
        if (ord.length % dim != 0)
            throw new IllegalArgumentException(
                    "Ordinate array length "
                            + ord.length
                            + " is not a multiple of dimension "
                            + dim);
        int n = ord.length / dim;
        CoordinateSequence cs;
        if (csFact instanceof CoordinateArraySequenceFactory && dim == 1) {
            // work around JTS 1.14 CoordinateArraySequenceFactory regression ignorning provided
            // dimension
            cs = new CoordinateArraySequence(n, dim);
        } else {
            cs = JTS.createCS(csFact, n, dim);
        }
        if (cs.getDimension() != dim) {
            // illegal state error, try and fix
            throw new IllegalStateException(
                    "Unable to use"
                            + csFact
                            + " to produce CoordinateSequence with dimension "
                            + dim);
        }
        for (int i = 0; i < n; i++) {
            for (int d = 0; d < dim; d++) cs.setOrdinate(i, d, ord[dim * i + d]);
        }
        return cs;
    }
}
