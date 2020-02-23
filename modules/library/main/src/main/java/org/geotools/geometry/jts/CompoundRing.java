/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import java.util.List;
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
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * A CompoundRing is a connected sequence of circular arcs and linear segments forming a closed
 * line.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CompoundRing extends LinearRing
        implements CompoundCurvedGeometry<LinearRing>, CurvedRing {

    private static final long serialVersionUID = -5796254063449438787L;

    CompoundCurve delegate;

    LineString linearized;

    public CompoundRing(List<LineString> components, GeometryFactory factory, double tolerance) {
        this(new CompoundCurve(components, factory, tolerance));
    }

    public CompoundRing(CompoundCurve delegate) {
        super(CircularRing.FAKE_RING_2D, delegate.getFactory());
        this.delegate = delegate;

        // check closed
        if (!delegate.isClosed()) {
            throw new IllegalArgumentException("The components do not form a closed ring");
        }
    }

    @Override
    public double getTolerance() {
        return delegate.getTolerance();
    }

    /**
     * Returns the components of this compound curve, which will be a list of straight LineString
     * objects and CircularString/CircularRing
     */
    public List<LineString> getComponents() {
        return delegate.components;
    }

    @Override
    public int getCoordinatesDimension() {
        return delegate.getCoordinatesDimension();
    }

    @Override
    public LinearRing linearize() {
        CoordinateSequence cs = delegate.getLinearizedCoordinateSequence(delegate.tolerance);
        return getFactory().createLinearRing(cs);
    }

    public LinearRing linearize(double tolerance) {
        CoordinateSequence cs = delegate.getLinearizedCoordinateSequence(tolerance);
        return getFactory().createLinearRing(cs);
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
        return "CompoundRing";
    }

    public Geometry reverse() {
        CompoundCurve reversedDelegate = (CompoundCurve) delegate.reverse();
        return new CompoundRing(reversedDelegate);
    }

    public int getNumGeometries() {
        return delegate.getNumGeometries();
    }

    public Geometry getGeometryN(int n) {
        return delegate.getGeometryN(n);
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
        return delegate.isRectangle();
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
        return delegate.equalsExact(other);
    }

    public boolean equalsExact(Geometry other, double tolerance) {
        if (other instanceof CompoundRing) {
            CompoundRing csOther = (CompoundRing) other;
            return delegate.equalsExact(csOther.delegate, tolerance);
        }
        return linearize(tolerance).equalsExact(other, tolerance);
    }

    public boolean equals(Geometry other) {
        if (other instanceof CompoundRing) {
            CompoundRing csOther = (CompoundRing) other;
            return delegate.equals(csOther.delegate);
        }
        return linearize().equals(other);
    }

    public boolean equalsTopo(Geometry other) {
        if (other instanceof CompoundRing) {
            CompoundRing csOther = (CompoundRing) other;
            return delegate.equalsTopo(csOther.delegate);
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

    public Point getPointN(int n) {
        if (n == 0) {
            return getStartPoint();
        }
        return linearize().getPointN(n);
    }

    public Point getStartPoint() {
        return delegate.getStartPoint();
    }

    public Point getEndPoint() {
        return delegate.getEndPoint();
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
    public CompoundRing copyInternal() {
        return new CompoundRing(delegate);
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
