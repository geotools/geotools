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
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

/**
 * A CircularString is a sequence of zero or more connected circular arc segments. A circular arc
 * segment is a curved segment defined by three points in a two-dimensional plane; the first point
 * cannot be the same as the third point.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CircularString extends LineString implements SingleCurvedGeometry<LineString> {

    /**
     * Helper class that automates the scan of the list of CircularArc in the controlpoint sequence
     */
    abstract class ArcScan {
        public ArcScan() {
            if (controlPoints.length == 3) {
                // single arc case
                CircularArc arc = new CircularArc(controlPoints);
                visitArc(arc);
            } else {
                // go over each 3-points set and linearize it
                double[] arcControlPoints = new double[6];
                CircularArc arc = new CircularArc(arcControlPoints);
                for (int i = 0; i <= controlPoints.length - 6; i += 4) {
                    // have the arc work off the new control points
                    System.arraycopy(controlPoints, i, arcControlPoints, 0, 6);
                    arc.reset();
                    visitArc(arc);
                }
            }
        }

        protected abstract void visitArc(CircularArc arc);
    }

    private static final long serialVersionUID = -5796254063449438787L;

    /** This sequence is used as a fake to trick the constructor */
    static final CoordinateSequence FAKE_STRING_2D =
            new CoordinateArraySequence(
                    new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 1)});

    double[] controlPoints;

    double tolerance;

    LineString linearized;

    public CircularString(CoordinateSequence points, GeometryFactory factory, double tolerance) {
        super(FAKE_STRING_2D, factory);
        this.tolerance = tolerance;
        if (points.getDimension() != 2) {
            throw new IllegalArgumentException(
                    "Circular strings are restricted to 2 dimensions "
                            + "at the moment. Contributions to get ND support welcomed!");
        }

        int pointCount = points.size();
        controlPoints = new double[pointCount * 2];
        for (int i = 0; i < pointCount; i++) {
            controlPoints[i * 2] = points.getX(i);
            controlPoints[i * 2 + 1] = points.getY(i);
        }
        init(controlPoints, tolerance);
    }

    public CircularString(double[] controlPoints, GeometryFactory factory, double tolerance) {
        super(FAKE_STRING_2D, factory);
        init(controlPoints, tolerance);
    }

    private void init(double[] controlPoints, double tolerance) {
        int length = controlPoints.length;
        if (length % 2 != 0) {
            throw new IllegalArgumentException(
                    "Invalid number of ordinates, must be even, but it is " + length + " instead");
        }
        int pointCount = length / 2;
        if ((pointCount != 0 && pointCount < 3) || (pointCount > 3 && (pointCount % 2) == 0)) {
            throw new IllegalArgumentException(
                    "Invalid number of points, a circular string"
                            + "is always made of an odd number of points, with a mininum of 3, "
                            + "and adding 2 for each extra circular arc in the sequence");
        }
        this.controlPoints = controlPoints;
        this.tolerance = tolerance;
    }

    @Override
    public double[] getControlPoints() {
        return controlPoints;
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    @Override
    public int getNumArcs() {
        return (controlPoints.length - 6) / 4 + 1;
    }

    @Override
    public CircularArc getArcN(int arcIndex) {
        int baseIdx = arcIndex * 4;
        double[] arcControlPoints = new double[6];
        System.arraycopy(controlPoints, baseIdx, arcControlPoints, 0, 6);
        CircularArc arc = new CircularArc(arcControlPoints);
        return arc;
    }

    @Override
    public LineString linearize() {
        return linearize(this.tolerance);
    }

    public LineString linearize(double tolerance) {
        // use the cached one if we are asked for the default geometry tolerance
        boolean isDefaultTolerance = CircularArc.equals(tolerance, this.tolerance);
        if (linearized != null && isDefaultTolerance) {
            return linearized;
        }

        CoordinateSequence cs = getLinearizedCoordinateSequence(tolerance);
        LineString result = new LineString(cs, factory);
        if (isDefaultTolerance) {
            linearized = result;
        }

        return result;
    }

    public CoordinateSequence getLinearizedCoordinateSequence(final double tolerance) {
        boolean isDefaultTolerance = CircularArc.equals(tolerance, this.tolerance);
        if (linearized != null && isDefaultTolerance) {
            return linearized.getCoordinateSequence();
        }

        final GrowableOrdinateArray gar = new GrowableOrdinateArray();
        new ArcScan() {

            protected void visitArc(CircularArc arc) {
                // if it's not the first arc, we need to eliminate the last point,
                // as the end point of the last arc is the start point of the new one
                if (gar.size() > 0) {
                    gar.setSize(gar.size() - 2);
                }
                arc.linearize(tolerance, gar);
            }
        };

        CoordinateSequence cs = gar.toCoordinateSequence(getFactory());
        return cs;
    }

    /* Optimized overridden methods */

    public boolean isClosed() {
        return controlPoints[0] == controlPoints[controlPoints.length - 2]
                && controlPoints[1] == controlPoints[controlPoints.length - 1];
    }

    public int getDimension() {
        return super.getDimension();
    }

    public int getBoundaryDimension() {
        return super.getDimension();
    }

    public boolean isEmpty() {
        return controlPoints.length == 0;
    }

    public String getGeometryType() {
        return "CircularString";
    }

    public Geometry reverse() {
        // reverse the control points
        double[] reversed = new double[controlPoints.length];
        System.arraycopy(controlPoints, 0, reversed, 0, controlPoints.length);
        GrowableOrdinateArray array = new GrowableOrdinateArray();
        array.addAll(controlPoints);
        array.reverseOrdinates(0, array.size() - 1);
        return new CircularString(array.getData(), getFactory(), tolerance);
    }

    public Point getInteriorPoint() {
        int idx = controlPoints.length / 2;
        return new Point(
                new CoordinateArraySequence(
                        new Coordinate[] {
                            new Coordinate(controlPoints[idx], controlPoints[idx + 1])
                        }),
                getFactory());
    }

    public Geometry getEnvelope() {
        return super.getEnvelope();
    }

    public Envelope getEnvelopeInternal() {
        return super.getEnvelopeInternal();
    }

    @Override
    protected Envelope computeEnvelopeInternal() {
        final Envelope result = new Envelope();
        new ArcScan() {

            protected void visitArc(CircularArc arc) {
                arc.expandEnvelope(result);
            }
        };

        return result;
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

    public boolean equalsExact(Geometry other) {
        return equalsExact(other, 0);
    }

    public boolean equalsExact(Geometry other, double tolerance) {
        if (other instanceof CircularString) {
            CircularString csOther = (CircularString) other;
            if (Arrays.equals(controlPoints, csOther.controlPoints)) {
                return true;
            }
        }
        return linearize(tolerance).equalsExact(other, tolerance);
    }

    public boolean equals(Geometry other) {
        if (other instanceof CircularString) {
            CircularString csOther = (CircularString) other;
            if (Arrays.equals(controlPoints, csOther.controlPoints)) {
                return true;
            }
        }
        return linearize().equals(other);
    }

    public boolean equalsTopo(Geometry other) {
        if (other instanceof CircularString) {
            CircularString csOther = (CircularString) other;
            if (Arrays.equals(controlPoints, csOther.controlPoints)) {
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
        StringBuilder sb = new StringBuilder("CIRCULARSTRING ");
        if (isEmpty()) {
            sb.append("EMPTY");
        } else {
            sb.append("(");
            for (int i = 0; i < controlPoints.length; ) {
                sb.append(controlPoints[i++] + " " + controlPoints[i++]);
                if (i < controlPoints.length) {
                    sb.append(", ");
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public boolean equalsNorm(Geometry g) {
        return super.equalsNorm(g);
    }

    public Point getPointN(int n) {
        if (n == 0) {
            return getStartPoint();
        } else {
            return linearize().getPointN(n);
        }
    }

    public Point getStartPoint() {
        return new Point(
                new CoordinateArraySequence(
                        new Coordinate[] {new Coordinate(controlPoints[0], controlPoints[1])}),
                getFactory());
    }

    public Point getEndPoint() {
        return new Point(
                new CoordinateArraySequence(
                        new Coordinate[] {
                            new Coordinate(
                                    controlPoints[controlPoints.length - 2],
                                    controlPoints[controlPoints.length - 1])
                        }),
                getFactory());
    }

    /*
     * Simple linearized delegate methods
     */

    public Coordinate[] getCoordinates() {
        return linearize().getCoordinates();
    }

    public CoordinateSequence getCoordinateSequence() {
        return linearize().getCoordinateSequence();
    }

    public Coordinate getCoordinateN(int n) {
        return linearize().getCoordinateN(n);
    }

    public Coordinate getCoordinate() {
        return linearize().getCoordinate();
    }

    public int getNumPoints() {
        return linearize().getNumPoints();
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
    protected CircularString copyInternal() {
        return new CircularString(controlPoints, factory, tolerance);
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

    @Override
    public int getCoordinatesDimension() {
        return 2;
    }
}
