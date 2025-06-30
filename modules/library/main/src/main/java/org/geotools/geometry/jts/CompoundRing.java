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
 * A CompoundRing is a connected sequence of circular arcs and linear segments forming a closed line.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class CompoundRing extends LinearRing implements CompoundCurvedGeometry<LinearRing>, CurvedRing {

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
     * Returns the components of this compound curve, which will be a list of straight LineString objects and
     * CircularString/CircularRing
     */
    @Override
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

    @Override
    public LinearRing linearize(double tolerance) {
        CoordinateSequence cs = delegate.getLinearizedCoordinateSequence(tolerance);
        return getFactory().createLinearRing(cs);
    }

    /* Optimized overridden methods */

    @Override
    public boolean isClosed() {
        return true;
    }

    @Override
    public int getDimension() {
        return super.getDimension();
    }

    @Override
    public int getBoundaryDimension() {
        return super.getDimension();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String getGeometryType() {
        return "CompoundRing";
    }

    @Override
    public CompoundRing reverse() {
        return (CompoundRing) super.reverse();
    }

    // should be protected when fixed in LinearRing
    @Override
    public CompoundRing reverseInternal() {
        CompoundCurve reversedDelegate = delegate.reverse();
        return new CompoundRing(reversedDelegate);
    }

    @Override
    public int getNumGeometries() {
        return delegate.getNumGeometries();
    }

    @Override
    public Geometry getGeometryN(int n) {
        return delegate.getGeometryN(n);
    }

    @Override
    public void setUserData(Object userData) {
        super.setUserData(userData);
    }

    @Override
    public int getSRID() {
        return super.getSRID();
    }

    @Override
    public void setSRID(int SRID) {
        super.setSRID(SRID);
    }

    @Override
    public GeometryFactory getFactory() {
        return super.getFactory();
    }

    @Override
    public Object getUserData() {
        return super.getUserData();
    }

    @Override
    public PrecisionModel getPrecisionModel() {
        return super.getPrecisionModel();
    }

    @Override
    public boolean isRectangle() {
        return delegate.isRectangle();
    }

    @Override
    public Point getInteriorPoint() {
        return delegate.getInteriorPoint();
    }

    @Override
    public Geometry getEnvelope() {
        return delegate.getEnvelope();
    }

    @Override
    public Envelope getEnvelopeInternal() {
        return delegate.getEnvelopeInternal();
    }

    @Override
    protected Envelope computeEnvelopeInternal() {
        return delegate.getEnvelopeInternal();
    }

    @Override
    public boolean equalsExact(Geometry other) {
        return delegate.equalsExact(other);
    }

    @Override
    public boolean equalsExact(Geometry other, double tolerance) {
        if (other instanceof CompoundRing) {
            CompoundRing csOther = (CompoundRing) other;
            return delegate.equalsExact(csOther.delegate, tolerance);
        }
        return linearize(tolerance).equalsExact(other, tolerance);
    }

    @Override
    @SuppressWarnings("NonOverridingEquals") // this is part of the interface, not overriding Object.equals()
    public boolean equals(Geometry other) {
        if (other instanceof CompoundRing) {
            CompoundRing csOther = (CompoundRing) other;
            return delegate.equals(csOther.delegate);
        }
        return linearize().equals(other);
    }

    @Override
    public boolean equalsTopo(Geometry other) {
        if (other instanceof CompoundRing) {
            CompoundRing csOther = (CompoundRing) other;
            return delegate.equalsTopo(csOther.delegate);
        }
        return linearize().equalsTopo(other);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Geometry) {
            return equals((Geometry) o);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return toCurvedText();
    }

    @Override
    public String toCurvedText() {
        return delegate.toCurvedText();
    }

    @Override
    public boolean equalsNorm(Geometry g) {
        return super.equalsNorm(g);
    }

    @Override
    public Point getPointN(int n) {
        if (n == 0) {
            return getStartPoint();
        }
        return linearize().getPointN(n);
    }

    @Override
    public Point getStartPoint() {
        return delegate.getStartPoint();
    }

    @Override
    public Point getEndPoint() {
        return delegate.getEndPoint();
    }

    /*
     * Simple linearized delegate methods
     */

    @Override
    public Coordinate[] getCoordinates() {
        return linearize().getCoordinates();
    }

    @Override
    public CoordinateSequence getCoordinateSequence() {
        // trick to avoid issues while JTS validates the ring is closed,
        // it's calling super.isClosed() breaking the local override
        if (delegate != null) {
            return linearize().getCoordinateSequence();
        } else {
            return super.getCoordinateSequence();
        }
    }

    @Override
    public Coordinate getCoordinateN(int n) {
        // trick to avoid issues while JTS validates the ring is closed,
        // it's calling super.isClosed() breaking the local override
        if (delegate != null) {
            return linearize().getCoordinateN(n);
        } else {
            return super.getCoordinateN(n);
        }
    }

    @Override
    public Coordinate getCoordinate() {
        return linearize().getCoordinate();
    }

    @Override
    public int getNumPoints() {
        // trick to avoid issues while JTS validates the ring is closed,
        // it's calling super.isClosed() breaking the local override
        if (delegate != null) {
            return linearize().getNumPoints();
        } else {
            return super.getNumPoints();
        }
    }

    @Override
    public boolean isRing() {
        return linearize().isRing();
    }

    @Override
    public double getLength() {
        // todo: maybe compute the actual circular length?
        return linearize().getLength();
    }

    @Override
    public Geometry getBoundary() {
        return linearize().getBoundary();
    }

    @Override
    public boolean isCoordinate(Coordinate pt) {
        return linearize().isCoordinate(pt);
    }

    @Override
    public void apply(CoordinateFilter filter) {
        linearize().apply(filter);
    }

    @Override
    public void apply(CoordinateSequenceFilter filter) {
        linearize().apply(filter);
    }

    @Override
    public void apply(GeometryFilter filter) {
        linearize().apply(filter);
    }

    @Override
    public void apply(GeometryComponentFilter filter) {
        linearize().apply(filter);
    }

    @Override
    public CompoundRing copyInternal() {
        return new CompoundRing(delegate);
    }

    @Override
    public void normalize() {
        linearize().normalize();
    }

    @Override
    public boolean isSimple() {
        return linearize().isSimple();
    }

    @Override
    public boolean isValid() {
        return linearize().isValid();
    }

    @Override
    public double distance(Geometry g) {
        return linearize().distance(g);
    }

    @Override
    public boolean isWithinDistance(Geometry geom, double distance) {
        return linearize().isWithinDistance(geom, distance);
    }

    @Override
    public double getArea() {
        return linearize().getArea();
    }

    @Override
    public Point getCentroid() {
        return linearize().getCentroid();
    }

    @Override
    public void geometryChanged() {
        linearize().geometryChanged();
    }

    @Override
    public boolean disjoint(Geometry g) {
        return linearize().disjoint(g);
    }

    @Override
    public boolean touches(Geometry g) {
        return linearize().touches(g);
    }

    @Override
    public boolean intersects(Geometry g) {
        return linearize().intersects(g);
    }

    @Override
    public boolean crosses(Geometry g) {
        return linearize().crosses(g);
    }

    @Override
    public boolean within(Geometry g) {
        return linearize().within(g);
    }

    @Override
    public boolean contains(Geometry g) {
        return linearize().contains(g);
    }

    @Override
    public boolean overlaps(Geometry g) {
        return linearize().overlaps(g);
    }

    @Override
    public boolean covers(Geometry g) {
        return linearize().covers(g);
    }

    @Override
    public boolean coveredBy(Geometry g) {
        return linearize().coveredBy(g);
    }

    @Override
    public boolean relate(Geometry g, String intersectionPattern) {
        return linearize().relate(g, intersectionPattern);
    }

    @Override
    public IntersectionMatrix relate(Geometry g) {
        return linearize().relate(g);
    }

    @Override
    public Geometry buffer(double distance) {
        return linearize().buffer(distance);
    }

    @Override
    public Geometry buffer(double distance, int quadrantSegments) {
        return linearize().buffer(distance, quadrantSegments);
    }

    @Override
    public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
        return linearize().buffer(distance, quadrantSegments, endCapStyle);
    }

    @Override
    public Geometry convexHull() {
        return linearize().convexHull();
    }

    @Override
    public Geometry intersection(Geometry other) {
        return linearize().intersection(other);
    }

    @Override
    public Geometry union(Geometry other) {
        return linearize().union(other);
    }

    @Override
    public Geometry difference(Geometry other) {
        return linearize().difference(other);
    }

    @Override
    public Geometry symDifference(Geometry other) {
        return linearize().symDifference(other);
    }

    @Override
    public Geometry union() {
        return linearize().union();
    }

    @Override
    public Geometry norm() {
        return linearize().norm();
    }

    @Override
    public int compareTo(Object o) {
        return linearize().compareTo(o);
    }

    @Override
    public int compareTo(Object o, CoordinateSequenceComparator comp) {
        return linearize().compareTo(o, comp);
    }

    @Override
    public String toText() {
        return linearize().toText();
    }
}
