/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceComparator;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.GeometryFilter;
import org.locationtech.jts.geom.IntersectionMatrix;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

/**
 * A CircularRing is a CircularString whose start and end point coincide. The ring needs to be
 * formed of at least two arc circles, in order to be able to determine its orientation.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CircularRing extends LinearRing
        implements SingleCurvedGeometry<LinearRing>, CurvedRing {

    private static final long serialVersionUID = -5796254063449438787L;

    /** This sequence is used as a fake to trick the constructor */
    static final CoordinateSequence FAKE_RING_2D =
            new CoordinateArraySequence(
                    new Coordinate[] {
                        new Coordinate(0, 0),
                        new Coordinate(0, 1),
                        new Coordinate(1, 1),
                        new Coordinate(0, 0)
                    });

    CircularString delegate;

    public CircularRing(CoordinateSequence points, GeometryFactory factory, double tolerance) {
        super(FAKE_RING_2D, factory);
        delegate = new CircularString(points, factory, tolerance);
        if (!delegate.isClosed()) {
            throw new IllegalArgumentException(
                    "Start and end point are not matching, this is not a ring");
        }
    }

    public CircularRing(double[] controlPoints, GeometryFactory factory, double tolerance) {
        super(FAKE_RING_2D, factory);
        delegate = new CircularString(controlPoints, factory, tolerance);
        if (!delegate.isClosed()) {
            throw new IllegalArgumentException(
                    "Start and end point are not matching, this is not a ring");
        }
    }

    @Override
    public int getNumArcs() {
        return delegate.getNumArcs();
    }

    @Override
    public CircularArc getArcN(int arcIndex) {
        return delegate.getArcN(arcIndex);
    }

    @Override
    public LinearRing linearize() {
        CoordinateSequence cs = delegate.getLinearizedCoordinateSequence(delegate.tolerance);
        return getFactory().createLinearRing(cs);
    }

    public LinearRing linearize(double tolerance) {
        CoordinateSequence cs = delegate.getLinearizedCoordinateSequence(delegate.tolerance);
        return getFactory().createLinearRing(cs);
    }

    @Override
    public double getTolerance() {
        return delegate.getTolerance();
    }

    @Override
    public CoordinateSequence getLinearizedCoordinateSequence(double tolerance) {
        return delegate.getLinearizedCoordinateSequence(tolerance);
    }

    @Override
    public double[] getControlPoints() {
        return delegate.controlPoints;
    }

    /* Optimized overridden methods */

    public boolean isClosed() {
        return true;
    }

    public int getDimension() {
        return super.getDimension();
    }

    public int getBoundaryDimension() {
        return super.getDimension();
    }

    public boolean isEmpty() {
        return false;
    }

    public String getGeometryType() {
        return "CircularRing";
    }

    @Override
    public int getCoordinatesDimension() {
        return delegate.getDimension();
    }

    /** Returns a normalized ring (one that does not have a single arc closing on itself) */
    public CircularRing normalizeRing() {
        if (!isClosed() || getNumArcs() > 1) {
            return this;
        }

        // Single arc with only two points case, we need to create two arcs, trying to
        // preserve the original points

        CircularArc arc = getArcN(0);
        Coordinate center = arc.getCenter();
        double radius = arc.getRadius();
        double[] cp = arc.getControlPoints();
        double angle1 = Math.atan2(cp[1] - center.y, cp[0] - center.x);
        double angle2 = Math.atan2(cp[3] - center.y, cp[2] - center.x);

        // compute the two mid points
        double am1 = (angle1 + angle2) / 2;
        double am2 = am1 + Math.PI;

        // generate the new control points sequence
        double[] ncp = new double[10];
        ncp[0] = cp[0];
        ncp[1] = cp[1];
        ncp[2] = center.x + radius * Math.cos(am1);
        ncp[3] = center.y + radius * Math.sin(am1);
        ncp[4] = cp[2];
        ncp[5] = cp[3];
        ncp[6] = center.x + radius * Math.cos(am2);
        ncp[7] = center.y + radius * Math.sin(am2);
        ncp[8] = cp[0];
        ncp[9] = cp[1];

        return new CircularRing(ncp, factory, delegate.getTolerance());
    }

    public Geometry reverse() {
        double[] controlPoints = delegate.controlPoints;
        GrowableOrdinateArray array = new GrowableOrdinateArray();
        array.addAll(controlPoints);
        array.reverseOrdinates(0, array.size() - 1);
        return new CircularRing(array.getData(), getFactory(), delegate.tolerance);
    }

    public int getNumGeometries() {
        return 1;
    }

    public Geometry getGeometryN(int n) {
        return this;
    }

    public void setUserData(Object userData) {
        super.setUserData(userData);
    }

    public int getSRID() {
        return super.getSRID();
    }

    public void setSRID(int SRID) {
        super.setSRID(SRID);
    }

    public GeometryFactory getFactory() {
        return super.getFactory();
    }

    public Object getUserData() {
        return super.getUserData();
    }

    public PrecisionModel getPrecisionModel() {
        return super.getPrecisionModel();
    }

    public boolean isRectangle() {
        return false;
    }

    public Point getInteriorPoint() {
        return delegate.getInteriorPoint();
    }

    public Geometry getEnvelope() {
        return delegate.getEnvelope();
    }

    public Envelope getEnvelopeInternal() {
        return delegate.getEnvelopeInternal();
    }

    @Override
    protected Envelope computeEnvelopeInternal() {
        return delegate.getEnvelopeInternal();
    }

    public boolean equalsExact(Geometry other) {
        return equalsExact(other, 0);
    }

    public boolean equalsExact(Geometry other, double tolerance) {
        if (other instanceof CircularRing) {
            CircularRing csOther = (CircularRing) other;
            if (Arrays.equals(delegate.controlPoints, csOther.delegate.controlPoints)) {
                return true;
            }
        }
        return linearize(tolerance).equalsExact(other, tolerance);
    }

    public boolean equals(Geometry other) {
        if (other instanceof CircularRing) {
            CircularRing csOther = (CircularRing) other;
            if (Arrays.equals(delegate.controlPoints, csOther.delegate.controlPoints)) {
                return true;
            }
        }
        return linearize().equals(other);
    }

    public boolean equalsTopo(Geometry other) {
        if (other instanceof CircularRing) {
            CircularRing csOther = (CircularRing) other;
            if (Arrays.equals(delegate.controlPoints, csOther.delegate.controlPoints)) {
                return true;
            }
        }
        return linearize().equalsTopo(other);
    }

    public boolean equals(Object o) {
        if (o instanceof Geometry) {
            return equals((Geometry) o);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return toCurvedText();
    }

    public String toCurvedText() {
        return delegate.toCurvedText();
    }

    public boolean equalsNorm(Geometry g) {
        return super.equalsNorm(g);
    }

    /*
     * Simple linearized delegate methods
     */

    public Coordinate[] getCoordinates() {
        return linearize().getCoordinates();
    }

    public CoordinateSequence getCoordinateSequence() {
        // trick to avoid issues while JTS validates the ring is closed,
        // it's calling super.isClosed() breaking the local override
        if (delegate != null) {
            return linearize().getCoordinateSequence();
        } else {
            return super.getCoordinateSequence();
        }
    }

    public Coordinate getCoordinateN(int n) {
        // trick to avoid issues while JTS validates the ring is closed,
        // it's calling super.isClosed() breaking the local override
        if (delegate != null) {
            return linearize().getCoordinateN(n);
        } else {
            return super.getCoordinateN(n);
        }
    }

    public Coordinate getCoordinate() {
        return linearize().getCoordinate();
    }

    public int getNumPoints() {
        // trick to avoid issues while JTS validates the ring is closed,
        // it's calling super.isClosed() breaking the local override
        if (delegate != null) {
            return linearize().getNumPoints();
        } else {
            return super.getNumPoints();
        }
    }

    public Point getPointN(int n) {
        return linearize().getPointN(n);
    }

    public Point getStartPoint() {
        return linearize().getStartPoint();
    }

    public Point getEndPoint() {
        return linearize().getEndPoint();
    }

    public boolean isRing() {
        return linearize().isRing();
    }

    public double getLength() {
        // todo: maybe compute the actual circular length?
        return linearize().getLength();
    }

    public Geometry getBoundary() {
        return linearize().getBoundary();
    }

    public boolean isCoordinate(Coordinate pt) {
        return linearize().isCoordinate(pt);
    }

    public void apply(CoordinateFilter filter) {
        linearize().apply(filter);
    }

    public void apply(CoordinateSequenceFilter filter) {
        linearize().apply(filter);
    }

    public void apply(GeometryFilter filter) {
        linearize().apply(filter);
    }

    public void apply(GeometryComponentFilter filter) {
        linearize().apply(filter);
    }

    @Override
    public CircularRing copyInternal() {
        return new CircularRing(getControlPoints(), factory, getTolerance());
    }

    public void normalize() {
        linearize().normalize();
    }

    public boolean isSimple() {
        return linearize().isSimple();
    }

    public boolean isValid() {
        return linearize().isValid();
    }

    public double distance(Geometry g) {
        return linearize().distance(g);
    }

    public boolean isWithinDistance(Geometry geom, double distance) {
        return linearize().isWithinDistance(geom, distance);
    }

    public double getArea() {
        return linearize().getArea();
    }

    public Point getCentroid() {
        return linearize().getCentroid();
    }

    public void geometryChanged() {
        linearize().geometryChanged();
    }

    public boolean disjoint(Geometry g) {
        return linearize().disjoint(g);
    }

    public boolean touches(Geometry g) {
        return linearize().touches(g);
    }

    public boolean intersects(Geometry g) {
        return linearize().intersects(g);
    }

    public boolean crosses(Geometry g) {
        return linearize().crosses(g);
    }

    public boolean within(Geometry g) {
        return linearize().within(g);
    }

    public boolean contains(Geometry g) {
        return linearize().contains(g);
    }

    public boolean overlaps(Geometry g) {
        return linearize().overlaps(g);
    }

    public boolean covers(Geometry g) {
        return linearize().covers(g);
    }

    public boolean coveredBy(Geometry g) {
        return linearize().coveredBy(g);
    }

    public boolean relate(Geometry g, String intersectionPattern) {
        return linearize().relate(g, intersectionPattern);
    }

    public IntersectionMatrix relate(Geometry g) {
        return linearize().relate(g);
    }

    public Geometry buffer(double distance) {
        return linearize().buffer(distance);
    }

    public Geometry buffer(double distance, int quadrantSegments) {
        return linearize().buffer(distance, quadrantSegments);
    }

    public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
        return linearize().buffer(distance, quadrantSegments, endCapStyle);
    }

    public Geometry convexHull() {
        return linearize().convexHull();
    }

    public Geometry intersection(Geometry other) {
        return linearize().intersection(other);
    }

    public Geometry union(Geometry other) {
        return linearize().union(other);
    }

    public Geometry difference(Geometry other) {
        return linearize().difference(other);
    }

    public Geometry symDifference(Geometry other) {
        return linearize().symDifference(other);
    }

    public Geometry union() {
        return linearize().union();
    }

    public Geometry norm() {
        return linearize().norm();
    }

    public int compareTo(Object o) {
        return linearize().compareTo(o);
    }

    public int compareTo(Object o, CoordinateSequenceComparator comp) {
        return linearize().compareTo(o, comp);
    }

    @Override
    public String toText() {
        return linearize().toText();
    }
}
