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
package org.geotools.renderer.lite;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceComparator;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryComponentFilter;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.GeometryFilter;
import com.vividsolutions.jts.geom.IntersectionMatrix;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * A subclass of {@link GeometryFactory} with special optimizations for geometry collections made of
 * a single element (a typical case in shapefiles and data imported from shapefiles)
 * 
 * @author Andrea Aime
 */
@SuppressWarnings("serial")
class SimpleGeometryFactory extends GeometryFactory {

    public SimpleGeometryFactory(CoordinateSequenceFactory csFactory) {
        super(csFactory);
    }

    @Override
    public GeometryCollection createGeometryCollection(Geometry[] geometries) {
        if (geometries != null && geometries.length == 1) {
            return new CollectionOfOne(geometries, this);
        } else {
            return super.createGeometryCollection(geometries);
        }
    }

    @Override
    public MultiLineString createMultiLineString(LineString[] lineStrings) {
        if (lineStrings != null && lineStrings.length == 1) {
            return new SingleLineCollection(lineStrings, this);
        } else {
            return super.createMultiLineString(lineStrings);
        }
    }

    @Override
    public MultiPolygon createMultiPolygon(Polygon[] polygons) {
        if (polygons != null && polygons.length == 1) {
            return new SinglePolygonCollection(polygons, this);
        } else {
            return super.createMultiPolygon(polygons);
        }
    }

    static class CollectionOfOne extends GeometryCollection {

        Geometry geometry;

        public CollectionOfOne(Geometry[] geometries, GeometryFactory factory) {
            super(geometries, factory);
            this.geometry = geometries[0];
        }

        public void apply(CoordinateFilter filter) {
            geometry.apply(filter);
        }

        public void apply(CoordinateSequenceFilter filter) {
            geometry.apply(filter);
        }

        public void apply(GeometryComponentFilter filter) {
            geometry.apply(filter);
        }

        public void apply(GeometryFilter filter) {
            geometry.apply(filter);
        }

        public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
            return geometry.buffer(distance, quadrantSegments, endCapStyle);
        }

        public Geometry buffer(double distance, int quadrantSegments) {
            return geometry.buffer(distance, quadrantSegments);
        }

        public Geometry buffer(double distance) {
            return geometry.buffer(distance);
        }

        public Object clone() {
            return geometry.clone();
        }

        public int compareTo(Object o, CoordinateSequenceComparator comp) {
            return geometry.compareTo(o, comp);
        }

        public int compareTo(Object o) {
            return geometry.compareTo(o);
        }

        public boolean contains(Geometry g) {
            return geometry.contains(g);
        }

        public Geometry convexHull() {
            return geometry.convexHull();
        }

        public boolean coveredBy(Geometry g) {
            return geometry.coveredBy(g);
        }

        public boolean covers(Geometry g) {
            return geometry.covers(g);
        }

        public boolean crosses(Geometry g) {
            return geometry.crosses(g);
        }

        public Geometry difference(Geometry other) {
            return geometry.difference(other);
        }

        public boolean disjoint(Geometry g) {
            return geometry.disjoint(g);
        }

        public double distance(Geometry g) {
            return geometry.distance(g);
        }

        public boolean equals(Geometry g) {
            return geometry.equals(g);
        }

        public boolean equals(Object obj) {
            return geometry.equals(obj);
        }

        public boolean equalsExact(Geometry other, double tolerance) {
            return geometry.equalsExact(other, tolerance);
        }

        public boolean equalsExact(Geometry other) {
            return geometry.equalsExact(other);
        }

        public void geometryChanged() {
            geometry.geometryChanged();
        }

        public double getArea() {
            return geometry.getArea();
        }

        public Geometry getBoundary() {
            return geometry.getBoundary();
        }

        public int getBoundaryDimension() {
            return geometry.getBoundaryDimension();
        }

        public Point getCentroid() {
            return geometry.getCentroid();
        }

        public Coordinate getCoordinate() {
            return geometry.getCoordinate();
        }

        public Coordinate[] getCoordinates() {
            return geometry.getCoordinates();
        }

        public int getDimension() {
            return geometry.getDimension();
        }

        public Geometry getEnvelope() {
            return geometry.getEnvelope();
        }

        public Envelope getEnvelopeInternal() {
            return geometry.getEnvelopeInternal();
        }

        public GeometryFactory getFactory() {
            return geometry.getFactory();
        }

        public Geometry getGeometryN(int n) {
            return geometry.getGeometryN(n);
        }

        public String getGeometryType() {
            return geometry.getGeometryType();
        }

        public Point getInteriorPoint() {
            return geometry.getInteriorPoint();
        }

        public double getLength() {
            return geometry.getLength();
        }

        public int getNumGeometries() {
            return geometry.getNumGeometries();
        }

        public int getNumPoints() {
            return geometry.getNumPoints();
        }

        public PrecisionModel getPrecisionModel() {
            return geometry.getPrecisionModel();
        }

        public int getSRID() {
            return geometry.getSRID();
        }

        public Object getUserData() {
            return geometry.getUserData();
        }

        public int hashCode() {
            return geometry.hashCode();
        }

        public Geometry intersection(Geometry other) {
            return geometry.intersection(other);
        }

        public boolean intersects(Geometry g) {
            return geometry.intersects(g);
        }

        public boolean isEmpty() {
            return geometry.isEmpty();
        }

        public boolean isRectangle() {
            return geometry.isRectangle();
        }

        public boolean isSimple() {
            return geometry.isSimple();
        }

        public boolean isValid() {
            return geometry.isValid();
        }

        public boolean isWithinDistance(Geometry geom, double distance) {
            return geometry.isWithinDistance(geom, distance);
        }

        public void normalize() {
            geometry.normalize();
        }

        public boolean overlaps(Geometry g) {
            return geometry.overlaps(g);
        }

        public boolean relate(Geometry g, String intersectionPattern) {
            return geometry.relate(g, intersectionPattern);
        }

        public IntersectionMatrix relate(Geometry g) {
            return geometry.relate(g);
        }

        public Geometry reverse() {
            return geometry.reverse();
        }

        public void setSRID(int SRID) {
            geometry.setSRID(SRID);
        }

        public void setUserData(Object userData) {
            geometry.setUserData(userData);
        }

        public Geometry symDifference(Geometry other) {
            return geometry.symDifference(other);
        }

        public String toString() {
            return geometry.toString();
        }

        public String toText() {
            return geometry.toText();
        }

        public boolean touches(Geometry g) {
            return geometry.touches(g);
        }

        public Geometry union() {
            return geometry.union();
        }

        public Geometry union(Geometry other) {
            return geometry.union(other);
        }

        public boolean within(Geometry g) {
            return geometry.within(g);
        }

    }

    static class SingleLineCollection extends MultiLineString {

        LineString lineString;

        public SingleLineCollection(LineString[] lineStrings, GeometryFactory factory) {
            super(lineStrings, factory);
            this.lineString = lineStrings[0];
        }

        public void apply(CoordinateFilter filter) {
            lineString.apply(filter);
        }

        public void apply(CoordinateSequenceFilter filter) {
            lineString.apply(filter);
        }

        public void apply(GeometryComponentFilter filter) {
            lineString.apply(filter);
        }

        public void apply(GeometryFilter filter) {
            lineString.apply(filter);
        }

        public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
            return lineString.buffer(distance, quadrantSegments, endCapStyle);
        }

        public Geometry buffer(double distance, int quadrantSegments) {
            return lineString.buffer(distance, quadrantSegments);
        }

        public Geometry buffer(double distance) {
            return lineString.buffer(distance);
        }

        public Object clone() {
            return lineString.clone();
        }

        public int compareTo(Object o, CoordinateSequenceComparator comp) {
            return lineString.compareTo(o, comp);
        }

        public int compareTo(Object o) {
            return lineString.compareTo(o);
        }

        public boolean contains(Geometry g) {
            return lineString.contains(g);
        }

        public Geometry convexHull() {
            return lineString.convexHull();
        }

        public boolean coveredBy(Geometry g) {
            return lineString.coveredBy(g);
        }

        public boolean covers(Geometry g) {
            return lineString.covers(g);
        }

        public boolean crosses(Geometry g) {
            return lineString.crosses(g);
        }

        public Geometry difference(Geometry other) {
            return lineString.difference(other);
        }

        public boolean disjoint(Geometry g) {
            return lineString.disjoint(g);
        }

        public double distance(Geometry g) {
            return lineString.distance(g);
        }

        public boolean equals(Geometry g) {
            return lineString.equals(g);
        }

        public boolean equals(Object obj) {
            return lineString.equals(obj);
        }

        public boolean equalsExact(Geometry other, double tolerance) {
            return lineString.equalsExact(other, tolerance);
        }

        public boolean equalsExact(Geometry other) {
            return lineString.equalsExact(other);
        }

        public void geometryChanged() {
            lineString.geometryChanged();
        }

        public double getArea() {
            return lineString.getArea();
        }

        public Geometry getBoundary() {
            return lineString.getBoundary();
        }

        public int getBoundaryDimension() {
            return lineString.getBoundaryDimension();
        }

        public Point getCentroid() {
            return lineString.getCentroid();
        }

        public Coordinate getCoordinate() {
            return lineString.getCoordinate();
        }

        public Coordinate getCoordinateN(int n) {
            return lineString.getCoordinateN(n);
        }

        public Coordinate[] getCoordinates() {
            return lineString.getCoordinates();
        }

        public CoordinateSequence getCoordinateSequence() {
            return lineString.getCoordinateSequence();
        }

        public int getDimension() {
            return lineString.getDimension();
        }

        public Point getEndPoint() {
            return lineString.getEndPoint();
        }

        public Geometry getEnvelope() {
            return lineString.getEnvelope();
        }

        public Envelope getEnvelopeInternal() {
            return lineString.getEnvelopeInternal();
        }

        public GeometryFactory getFactory() {
            return lineString.getFactory();
        }

        public Geometry getGeometryN(int n) {
            return lineString.getGeometryN(n);
        }

        public String getGeometryType() {
            return lineString.getGeometryType();
        }

        public Point getInteriorPoint() {
            return lineString.getInteriorPoint();
        }

        public double getLength() {
            return lineString.getLength();
        }

        public int getNumGeometries() {
            return lineString.getNumGeometries();
        }

        public int getNumPoints() {
            return lineString.getNumPoints();
        }

        public Point getPointN(int n) {
            return lineString.getPointN(n);
        }

        public PrecisionModel getPrecisionModel() {
            return lineString.getPrecisionModel();
        }

        public int getSRID() {
            return lineString.getSRID();
        }

        public Point getStartPoint() {
            return lineString.getStartPoint();
        }

        public Object getUserData() {
            return lineString.getUserData();
        }

        public int hashCode() {
            return lineString.hashCode();
        }

        public Geometry intersection(Geometry other) {
            return lineString.intersection(other);
        }

        public boolean intersects(Geometry g) {
            return lineString.intersects(g);
        }

        public boolean isClosed() {
            return lineString.isClosed();
        }

        public boolean isCoordinate(Coordinate pt) {
            return lineString.isCoordinate(pt);
        }

        public boolean isEmpty() {
            return lineString.isEmpty();
        }

        public boolean isRectangle() {
            return lineString.isRectangle();
        }

        public boolean isRing() {
            return lineString.isRing();
        }

        public boolean isSimple() {
            return lineString.isSimple();
        }

        public boolean isValid() {
            return lineString.isValid();
        }

        public boolean isWithinDistance(Geometry geom, double distance) {
            return lineString.isWithinDistance(geom, distance);
        }

        public void normalize() {
            lineString.normalize();
        }

        public boolean overlaps(Geometry g) {
            return lineString.overlaps(g);
        }

        public boolean relate(Geometry g, String intersectionPattern) {
            return lineString.relate(g, intersectionPattern);
        }

        public IntersectionMatrix relate(Geometry g) {
            return lineString.relate(g);
        }

        public Geometry reverse() {
            return lineString.reverse();
        }

        public void setSRID(int SRID) {
            lineString.setSRID(SRID);
        }

        public void setUserData(Object userData) {
            lineString.setUserData(userData);
        }

        public Geometry symDifference(Geometry other) {
            return lineString.symDifference(other);
        }

        public String toString() {
            return lineString.toString();
        }

        public String toText() {
            return lineString.toText();
        }

        public boolean touches(Geometry g) {
            return lineString.touches(g);
        }

        public Geometry union() {
            return lineString.union();
        }

        public Geometry union(Geometry other) {
            return lineString.union(other);
        }

        public boolean within(Geometry g) {
            return lineString.within(g);
        }
    }

    static class SinglePolygonCollection extends MultiPolygon {

        Polygon polygon;

        public SinglePolygonCollection(Polygon[] polygons, GeometryFactory factory) {
            super(polygons, factory);
            this.polygon = polygons[0];
        }

        public void apply(CoordinateFilter filter) {
            polygon.apply(filter);
        }

        public void apply(CoordinateSequenceFilter filter) {
            polygon.apply(filter);
        }

        public void apply(GeometryComponentFilter filter) {
            polygon.apply(filter);
        }

        public void apply(GeometryFilter filter) {
            polygon.apply(filter);
        }

        public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
            return polygon.buffer(distance, quadrantSegments, endCapStyle);
        }

        public Geometry buffer(double distance, int quadrantSegments) {
            return polygon.buffer(distance, quadrantSegments);
        }

        public Geometry buffer(double distance) {
            return polygon.buffer(distance);
        }

        public Object clone() {
            return polygon.clone();
        }

        public int compareTo(Object o, CoordinateSequenceComparator comp) {
            return polygon.compareTo(o, comp);
        }

        public int compareTo(Object o) {
            return polygon.compareTo(o);
        }

        public boolean contains(Geometry g) {
            return polygon.contains(g);
        }

        public Geometry convexHull() {
            return polygon.convexHull();
        }

        public boolean coveredBy(Geometry g) {
            return polygon.coveredBy(g);
        }

        public boolean covers(Geometry g) {
            return polygon.covers(g);
        }

        public boolean crosses(Geometry g) {
            return polygon.crosses(g);
        }

        public Geometry difference(Geometry other) {
            return polygon.difference(other);
        }

        public boolean disjoint(Geometry g) {
            return polygon.disjoint(g);
        }

        public double distance(Geometry g) {
            return polygon.distance(g);
        }

        public boolean equals(Geometry g) {
            return polygon.equals(g);
        }

        public boolean equals(Object obj) {
            return polygon.equals(obj);
        }

        public boolean equalsExact(Geometry other, double tolerance) {
            return polygon.equalsExact(other, tolerance);
        }

        public boolean equalsExact(Geometry other) {
            return polygon.equalsExact(other);
        }

        public void geometryChanged() {
            polygon.geometryChanged();
        }

        public double getArea() {
            return polygon.getArea();
        }

        public Geometry getBoundary() {
            return polygon.getBoundary();
        }

        public int getBoundaryDimension() {
            return polygon.getBoundaryDimension();
        }

        public Point getCentroid() {
            return polygon.getCentroid();
        }

        public Coordinate getCoordinate() {
            return polygon.getCoordinate();
        }

        public Coordinate[] getCoordinates() {
            return polygon.getCoordinates();
        }

        public int getDimension() {
            return polygon.getDimension();
        }

        public Geometry getEnvelope() {
            return polygon.getEnvelope();
        }

        public Envelope getEnvelopeInternal() {
            return polygon.getEnvelopeInternal();
        }

        public LineString getExteriorRing() {
            return polygon.getExteriorRing();
        }

        public GeometryFactory getFactory() {
            return polygon.getFactory();
        }

        public Geometry getGeometryN(int n) {
            return polygon.getGeometryN(n);
        }

        public String getGeometryType() {
            return polygon.getGeometryType();
        }

        public Point getInteriorPoint() {
            return polygon.getInteriorPoint();
        }

        public LineString getInteriorRingN(int n) {
            return polygon.getInteriorRingN(n);
        }

        public double getLength() {
            return polygon.getLength();
        }

        public int getNumGeometries() {
            return polygon.getNumGeometries();
        }

        public int getNumInteriorRing() {
            return polygon.getNumInteriorRing();
        }

        public int getNumPoints() {
            return polygon.getNumPoints();
        }

        public PrecisionModel getPrecisionModel() {
            return polygon.getPrecisionModel();
        }

        public int getSRID() {
            return polygon.getSRID();
        }

        public Object getUserData() {
            return polygon.getUserData();
        }

        public int hashCode() {
            return polygon.hashCode();
        }

        public Geometry intersection(Geometry other) {
            return polygon.intersection(other);
        }

        public boolean intersects(Geometry g) {
            return polygon.intersects(g);
        }

        public boolean isEmpty() {
            return polygon.isEmpty();
        }

        public boolean isRectangle() {
            return polygon.isRectangle();
        }

        public boolean isSimple() {
            return polygon.isSimple();
        }

        public boolean isValid() {
            return polygon.isValid();
        }

        public boolean isWithinDistance(Geometry geom, double distance) {
            return polygon.isWithinDistance(geom, distance);
        }

        public void normalize() {
            polygon.normalize();
        }

        public boolean overlaps(Geometry g) {
            return polygon.overlaps(g);
        }

        public boolean relate(Geometry g, String intersectionPattern) {
            return polygon.relate(g, intersectionPattern);
        }

        public IntersectionMatrix relate(Geometry g) {
            return polygon.relate(g);
        }

        public Geometry reverse() {
            return polygon.reverse();
        }

        public void setSRID(int SRID) {
            polygon.setSRID(SRID);
        }

        public void setUserData(Object userData) {
            polygon.setUserData(userData);
        }

        public Geometry symDifference(Geometry other) {
            return polygon.symDifference(other);
        }

        public String toString() {
            return polygon.toString();
        }

        public String toText() {
            return polygon.toText();
        }

        public boolean touches(Geometry g) {
            return polygon.touches(g);
        }

        public Geometry union() {
            return polygon.union();
        }

        public Geometry union(Geometry other) {
            return polygon.union(other);
        }

        public boolean within(Geometry g) {
            return polygon.within(g);
        }

    }

    static class SinglePointCollection extends MultiPoint {
        Point point;

        public SinglePointCollection(Point[] points, GeometryFactory factory) {
            super(points, factory);
            this.point = points[0];
        }

        public void apply(CoordinateFilter filter) {
            point.apply(filter);
        }

        public void apply(CoordinateSequenceFilter filter) {
            point.apply(filter);
        }

        public void apply(GeometryComponentFilter filter) {
            point.apply(filter);
        }

        public void apply(GeometryFilter filter) {
            point.apply(filter);
        }

        public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
            return point.buffer(distance, quadrantSegments, endCapStyle);
        }

        public Geometry buffer(double distance, int quadrantSegments) {
            return point.buffer(distance, quadrantSegments);
        }

        public Geometry buffer(double distance) {
            return point.buffer(distance);
        }

        public Object clone() {
            return point.clone();
        }

        public int compareTo(Object o, CoordinateSequenceComparator comp) {
            return point.compareTo(o, comp);
        }

        public int compareTo(Object o) {
            return point.compareTo(o);
        }

        public boolean contains(Geometry g) {
            return point.contains(g);
        }

        public Geometry convexHull() {
            return point.convexHull();
        }

        public boolean coveredBy(Geometry g) {
            return point.coveredBy(g);
        }

        public boolean covers(Geometry g) {
            return point.covers(g);
        }

        public boolean crosses(Geometry g) {
            return point.crosses(g);
        }

        public Geometry difference(Geometry other) {
            return point.difference(other);
        }

        public boolean disjoint(Geometry g) {
            return point.disjoint(g);
        }

        public double distance(Geometry g) {
            return point.distance(g);
        }

        public boolean equals(Geometry g) {
            return point.equals(g);
        }

        public boolean equals(Object obj) {
            return point.equals(obj);
        }

        public boolean equalsExact(Geometry other, double tolerance) {
            return point.equalsExact(other, tolerance);
        }

        public boolean equalsExact(Geometry other) {
            return point.equalsExact(other);
        }

        public void geometryChanged() {
            point.geometryChanged();
        }

        public double getArea() {
            return point.getArea();
        }

        public Geometry getBoundary() {
            return point.getBoundary();
        }

        public int getBoundaryDimension() {
            return point.getBoundaryDimension();
        }

        public Point getCentroid() {
            return point.getCentroid();
        }

        public Coordinate getCoordinate() {
            return point.getCoordinate();
        }

        public Coordinate[] getCoordinates() {
            return point.getCoordinates();
        }

        public CoordinateSequence getCoordinateSequence() {
            return point.getCoordinateSequence();
        }

        public int getDimension() {
            return point.getDimension();
        }

        public Geometry getEnvelope() {
            return point.getEnvelope();
        }

        public Envelope getEnvelopeInternal() {
            return point.getEnvelopeInternal();
        }

        public GeometryFactory getFactory() {
            return point.getFactory();
        }

        public Geometry getGeometryN(int n) {
            return point.getGeometryN(n);
        }

        public String getGeometryType() {
            return point.getGeometryType();
        }

        public Point getInteriorPoint() {
            return point.getInteriorPoint();
        }

        public double getLength() {
            return point.getLength();
        }

        public int getNumGeometries() {
            return point.getNumGeometries();
        }

        public int getNumPoints() {
            return point.getNumPoints();
        }

        public PrecisionModel getPrecisionModel() {
            return point.getPrecisionModel();
        }

        public int getSRID() {
            return point.getSRID();
        }

        public Object getUserData() {
            return point.getUserData();
        }

        public double getX() {
            return point.getX();
        }

        public double getY() {
            return point.getY();
        }

        public int hashCode() {
            return point.hashCode();
        }

        public Geometry intersection(Geometry other) {
            return point.intersection(other);
        }

        public boolean intersects(Geometry g) {
            return point.intersects(g);
        }

        public boolean isEmpty() {
            return point.isEmpty();
        }

        public boolean isRectangle() {
            return point.isRectangle();
        }

        public boolean isSimple() {
            return point.isSimple();
        }

        public boolean isValid() {
            return point.isValid();
        }

        public boolean isWithinDistance(Geometry geom, double distance) {
            return point.isWithinDistance(geom, distance);
        }

        public void normalize() {
            point.normalize();
        }

        public boolean overlaps(Geometry g) {
            return point.overlaps(g);
        }

        public boolean relate(Geometry g, String intersectionPattern) {
            return point.relate(g, intersectionPattern);
        }

        public IntersectionMatrix relate(Geometry g) {
            return point.relate(g);
        }

        public Geometry reverse() {
            return point.reverse();
        }

        public void setSRID(int SRID) {
            point.setSRID(SRID);
        }

        public void setUserData(Object userData) {
            point.setUserData(userData);
        }

        public Geometry symDifference(Geometry other) {
            return point.symDifference(other);
        }

        public String toString() {
            return point.toString();
        }

        public String toText() {
            return point.toText();
        }

        public boolean touches(Geometry g) {
            return point.touches(g);
        }

        public Geometry union() {
            return point.union();
        }

        public Geometry union(Geometry other) {
            return point.union(other);
        }

        public boolean within(Geometry g) {
            return point.within(g);
        }

    }

}