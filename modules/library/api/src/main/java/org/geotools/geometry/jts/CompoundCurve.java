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

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceComparator;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryComponentFilter;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.GeometryFilter;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * A CompoundCurve is a connected sequence of circular arcs and linear segments.
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class CompoundCurve extends LineString implements CompoundCurvedGeometry<LineString> {

    private static final long serialVersionUID = -5796254063449438787L;

    List<LineString> components;

    LineString linearized;

    double tolerance;

    public CompoundCurve(List<LineString> components, GeometryFactory factory, double tolerance) {
        super(CircularString.FAKE_STRING_2D, factory);
        this.tolerance = tolerance;

        // sanity check, we don't want compound curves containing other compound curves
        this.components = new ArrayList<>();
        for (LineString ls : components) {
            if (ls instanceof CompoundCurve) {
                CompoundCurve cc = (CompoundCurve) ls;
                this.components.addAll(cc.components);
            } else {
                this.components.add(ls);
            }
        }

        // check connectedness
        if (components.size() > 1) {
            LineString prev = components.get(0);
            for (int i = 1; i < components.size(); i++) {
                LineString curr = components.get(i);
                Coordinate endPoint = prev.getCoordinateN(prev.getNumPoints() - 1);
                Coordinate startPoint = curr.getCoordinateN(0);
                if (!endPoint.equals(startPoint)) {
                    throw new IllegalArgumentException(
                            "Found two elements that are not connected, " + prev + " and " + curr);
                }
                prev = curr;
            }
        }
    }

    @Override
    public int getCoordinatesDimension() {
        if (components.size() == 0) {
            return 2;
        }
        int dimension = Integer.MAX_VALUE;
        for (LineString component : components) {
            int curr;
            if (component instanceof CurvedGeometry<?>) {
                curr = ((CurvedGeometry<?>) component).getCoordinatesDimension();
            } else {
                curr = component.getCoordinateSequence().getDimension();
            }
            dimension = Math.min(curr, dimension);
        }

        return dimension;
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

    protected CoordinateSequence getLinearizedCoordinateSequence(final double tolerance) {
        // collect all the points of all components
        final GrowableOrdinateArray gar = new GrowableOrdinateArray();
        for (LineString component : components) {
            // the last point of the previous element is the first point of the next one,
            // remove the duplication
            if (gar.size() > 0) {
                gar.setSize(gar.size() - 2);
            }
            // linearize with tolerance the circular strings, take the linear ones as is
            if (component instanceof SingleCurvedGeometry<?>) {
                SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) component;
                CoordinateSequence cs = curved
                        .getLinearizedCoordinateSequence(tolerance);
                gar.addAll(cs);
            } else {
                CoordinateSequence cs = component.getCoordinateSequence();
                for (int i = 0; i < cs.size(); i++) {
                    gar.add(cs.getX(i), cs.getY(i));
                }
            }
        }

        CoordinateSequence cs = gar.toCoordinateSequence(getFactory());
        return cs;
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    /**
     * Returns the components of this compound curve, which will be a list of straight LineString
     * objects and CircularString/CircularRing
     * 
     * @return
     */
    public List<LineString> getComponents() {
        return components;
    }

    /* Optimized overridden methods */

    public boolean isClosed() {
        LineString firstComponent = components.get(0);
        LineString lastComponent = components.get(components.size() - 1);
        return firstComponent.getStartPoint().equals(lastComponent.getEndPoint());
    }

    public int getDimension() {
        return super.getDimension();
    }

    public int getBoundaryDimension() {
        return super.getDimension();
    }

    public boolean isEmpty() {
        for (LineString ls : components) {
            if (!ls.isEmpty()) {
                return false;
            }
        }
        ;
        return true;
    }

    public String getGeometryType() {
        return "CompoundCurve";
    }

    public Geometry reverse() {
        // reverse the component, and reverse each component internal elements
        List<LineString> reversedComponents = new ArrayList<>(components.size());
        for (LineString ls : components) {
            LineString reversed = (LineString) ls.reverse();
            reversedComponents.add(0, reversed);
        }
        return new CompoundCurve(reversedComponents, getFactory(), tolerance);
    }

    public Point getInteriorPoint() {
        return components.get(components.size() / 2).getInteriorPoint();
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
        for (LineString ls : components) {
            result.expandToInclude(ls.getEnvelopeInternal());
        }
        return result;
    }

    public int getNumGeometries() {
        return components.size();
    }

    public Geometry getGeometryN(int n) {
        return components.get(n);
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


    public boolean equalsExact(Geometry other) {
        return equalsExact(other, 0);
    }

    public boolean equalsExact(Geometry other, double tolerance) {
        if (other instanceof CompoundCurve) {
            CompoundCurve ccOther = (CompoundCurve) other;
            if (ccOther.components.size() != components.size()) {
                return false;
            }
            for (int i = 0; i < components.size(); i++) {
                LineString ls1 = components.get(i);
                LineString ls2 = ccOther.components.get(i);
                if (!ls1.equalsExact(ls2, tolerance)) {
                    return false;
                }
            }

            return true;
        }
        return linearize(tolerance).equalsExact(other, tolerance);
    }

    public boolean equals(Geometry other) {
        if (other instanceof CompoundCurve) {
            CompoundCurve ccOther = (CompoundCurve) other;
            if (ccOther.components.size() != components.size()) {
                return false;
            }
            for (int i = 0; i < components.size(); i++) {
                LineString ls1 = components.get(i);
                LineString ls2 = ccOther.components.get(i);
                if (!ls1.equals(ls2)) {
                    return false;
                }
            }

            return true;
        }
        return linearize().equals(other);
    }

    public boolean equalsTopo(Geometry other) {
        if (other instanceof CompoundCurve) {
            CompoundCurve ccOther = (CompoundCurve) other;
            if (ccOther.components.size() != components.size()) {
                return false;
            }
            for (int i = 0; i < components.size(); i++) {
                LineString ls1 = components.get(i);
                LineString ls2 = ccOther.components.get(i);
                if (!ls1.equalsTopo(ls2)) {
                    return false;
                }
            }

            return true;
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
        StringBuilder sb = new StringBuilder("COMPOUNDCURVE ");

        if (components.size() == 0) {
            sb.append("EMPTY");
        } else {
            sb.append("(");
            for (int k = 0; k < components.size(); k++) {
                LineString component = components.get(k);
                if (component instanceof SingleCurvedGeometry<?>) {
                    SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) component;
                    sb.append(curved.toCurvedText());
                } else {
                    sb.append("(");
                    CoordinateSequence cs = component.getCoordinateSequence();
                    for (int i = 0; i < cs.size(); i++) {
                        sb.append(cs.getX(i) + " " + cs.getY(i));
                        if (i < cs.size() - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append(")");
                }
                if (k < components.size() - 1) {
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

    /*
     * Simple linearized delegate methods
     */

    public boolean isRectangle() {
        return linearize().isRectangle();
    }

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
