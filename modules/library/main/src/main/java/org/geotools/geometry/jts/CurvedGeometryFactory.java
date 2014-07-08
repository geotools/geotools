package org.geotools.geometry.jts;

import java.util.Collection;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

public class CurvedGeometryFactory extends GeometryFactory {

    GeometryFactory delegate;

    double tolerance;

    public CurvedGeometryFactory(double tolerance) {
        this(new GeometryFactory(), tolerance);
    }

    public CurvedGeometryFactory(GeometryFactory delegate, double tolerance) {
        this.tolerance = tolerance;
        this.delegate = delegate;
    }

    /**
     * Creates a {@link CircularString} or a {@link CircularRing} depending on whether the points
     * are forming a closed ring, or not
     */
    public LineString createCurvedGeometry(double[] controlPoints) {
        if (controlPoints[0] == controlPoints[controlPoints.length - 2]
                && controlPoints[1] == controlPoints[controlPoints.length - 1]) {
            return new CircularRing(controlPoints, this, tolerance);
        } else {
            return new CircularString(controlPoints, this, tolerance);
        }
    }

    /**
     * Creates a {@link CircularString} or a {@link CircularRing} depending on whether the points
     * are forming a closed ring, or not
     */
    public LineString createCurvedGeometry(CoordinateSequence cs) {
        int lastCoordinate = cs.size() - 1;
        if (cs.getOrdinate(0, 0) == cs.getOrdinate(lastCoordinate, 0)
                && cs.getOrdinate(0, 1) == cs.getOrdinate(lastCoordinate, 1)) {
            return new CircularRing(cs, this, tolerance);
        } else {
            return new CircularString(cs, this, tolerance);
        }
    }

    /**
     * Creates a compound curve with the given components
     */
    public LineString createCurvedGeometry(List<LineString> components) {
        if (components.isEmpty()) {
            // return an empty lineString?
            return createLineString(new Coordinate[] {});
        }
        if (components.size() == 1) {
            return components.get(0);
        }
        LineString first = components.get(0);
        LineString last = components.get(components.size() - 1);
        if (first.getStartPoint().equals(last.getEndPoint())) {
            return new CompoundRing(components, this, tolerance);
        } else {
            return new CompoundCurve(components, this, tolerance);
        }
    }

    /**
     * Creates a circle from a 3 points coordinate sequence
     * 
     * @param ext
     * @param gf
     * @param maxValue
     * @return
     */
    public CircularRing createCircle(CoordinateSequence cs) {
        if (cs.size() != 3) {
            throw new IllegalArgumentException(
                    "The coordinate sequence for the circle creation must contain 3 points, the one at hand contains "
                            + cs.size() + " instead");
        }
        double[] cp = new double[cs.size() * 2 + 2];
        for (int i = 0; i < cs.size(); i++) {
            cp[i * 2] = cs.getOrdinate(i, 0);
            cp[i * 2 + 1] = cs.getOrdinate(i, 1);
        }
        // figure out the other point
        CircularArc arc = new CircularArc(cp[0], cp[1], cp[2], cp[3], cp[4], cp[5]);
        return arc.toCircle(this, tolerance);
    }

    /* Delegate methods */

    public int hashCode() {
        return delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    public Geometry toGeometry(Envelope envelope) {
        return delegate.toGeometry(envelope);
    }

    public String toString() {
        return delegate.toString();
    }

    public PrecisionModel getPrecisionModel() {
        return delegate.getPrecisionModel();
    }

    public Point createPoint(Coordinate coordinate) {
        return delegate.createPoint(coordinate);
    }

    public Point createPoint(CoordinateSequence coordinates) {
        return delegate.createPoint(coordinates);
    }

    public MultiLineString createMultiLineString(LineString[] lineStrings) {
        return delegate.createMultiLineString(lineStrings);
    }

    public GeometryCollection createGeometryCollection(Geometry[] geometries) {
        return delegate.createGeometryCollection(geometries);
    }

    public MultiPolygon createMultiPolygon(Polygon[] polygons) {
        return delegate.createMultiPolygon(polygons);
    }

    public LinearRing createLinearRing(Coordinate[] coordinates) {
        return delegate.createLinearRing(coordinates);
    }

    public LinearRing createLinearRing(CoordinateSequence coordinates) {
        return delegate.createLinearRing(coordinates);
    }

    public MultiPoint createMultiPoint(Point[] point) {
        return delegate.createMultiPoint(point);
    }

    public MultiPoint createMultiPoint(Coordinate[] coordinates) {
        return delegate.createMultiPoint(coordinates);
    }

    public MultiPoint createMultiPoint(CoordinateSequence coordinates) {
        return delegate.createMultiPoint(coordinates);
    }

    public Polygon createPolygon(LinearRing shell, LinearRing[] holes) {
        return delegate.createPolygon(shell, holes);
    }

    public Polygon createPolygon(CoordinateSequence coordinates) {
        return delegate.createPolygon(coordinates);
    }

    public Polygon createPolygon(Coordinate[] coordinates) {
        return delegate.createPolygon(coordinates);
    }

    public Polygon createPolygon(LinearRing shell) {
        return delegate.createPolygon(shell);
    }

    public Geometry buildGeometry(Collection geomList) {
        return delegate.buildGeometry(geomList);
    }

    public LineString createLineString(Coordinate[] coordinates) {
        return delegate.createLineString(coordinates);
    }

    public LineString createLineString(CoordinateSequence coordinates) {
        return delegate.createLineString(coordinates);
    }

    public Geometry createGeometry(Geometry g) {
        return delegate.createGeometry(g);
    }

    public int getSRID() {
        return delegate.getSRID();
    }

    public CoordinateSequenceFactory getCoordinateSequenceFactory() {
        return delegate.getCoordinateSequenceFactory();
    }

}
