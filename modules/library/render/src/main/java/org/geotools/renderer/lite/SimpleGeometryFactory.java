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

import org.geotools.util.SuppressFBWarnings;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceComparator;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.GeometryFilter;
import org.locationtech.jts.geom.IntersectionMatrix;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * A subclass of {@link GeometryFactory} with special optimizations for geometry collections made of
 * a single element (a typical case in shapefiles and data imported from shapefiles)
 *
 * @author Andrea Aime
 */
@SuppressWarnings("serial")
@SuppressFBWarnings("EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS")
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

        @Override
        public void apply(CoordinateFilter filter) {
            geometry.apply(filter);
        }

        @Override
        public void apply(CoordinateSequenceFilter filter) {
            geometry.apply(filter);
        }

        @Override
        public void apply(GeometryComponentFilter filter) {
            geometry.apply(filter);
        }

        @Override
        public void apply(GeometryFilter filter) {
            geometry.apply(filter);
        }

        @Override
        public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
            return geometry.buffer(distance, quadrantSegments, endCapStyle);
        }

        @Override
        public Geometry buffer(double distance, int quadrantSegments) {
            return geometry.buffer(distance, quadrantSegments);
        }

        @Override
        public Geometry buffer(double distance) {
            return geometry.buffer(distance);
        }

        @Override
        public int compareTo(Object o, CoordinateSequenceComparator comp) {
            return geometry.compareTo(o, comp);
        }

        @Override
        public int compareTo(Object o) {
            return geometry.compareTo(o);
        }

        @Override
        public boolean contains(Geometry g) {
            return geometry.contains(g);
        }

        @Override
        public Geometry convexHull() {
            return geometry.convexHull();
        }

        @Override
        public boolean coveredBy(Geometry g) {
            return geometry.coveredBy(g);
        }

        @Override
        public boolean covers(Geometry g) {
            return geometry.covers(g);
        }

        @Override
        public boolean crosses(Geometry g) {
            return geometry.crosses(g);
        }

        @Override
        public Geometry difference(Geometry other) {
            return geometry.difference(other);
        }

        @Override
        public boolean disjoint(Geometry g) {
            return geometry.disjoint(g);
        }

        @Override
        public double distance(Geometry g) {
            return geometry.distance(g);
        }

        @Override
        public boolean equals(Geometry g) {
            return geometry.equalsTopo(g);
        }

        @Override
        public boolean equals(Object obj) {
            return geometry.equals(obj);
        }

        @Override
        public boolean equalsExact(Geometry other, double tolerance) {
            return geometry.equalsExact(other, tolerance);
        }

        @Override
        public boolean equalsExact(Geometry other) {
            return geometry.equalsExact(other);
        }

        @Override
        public void geometryChanged() {
            geometry.geometryChanged();
        }

        @Override
        public double getArea() {
            return geometry.getArea();
        }

        @Override
        public Geometry getBoundary() {
            return geometry.getBoundary();
        }

        @Override
        public int getBoundaryDimension() {
            return geometry.getBoundaryDimension();
        }

        @Override
        public Point getCentroid() {
            return geometry.getCentroid();
        }

        @Override
        public Coordinate getCoordinate() {
            return geometry.getCoordinate();
        }

        @Override
        public Coordinate[] getCoordinates() {
            return geometry.getCoordinates();
        }

        @Override
        public int getDimension() {
            return geometry.getDimension();
        }

        @Override
        public Geometry getEnvelope() {
            return geometry.getEnvelope();
        }

        @Override
        public Envelope getEnvelopeInternal() {
            return geometry.getEnvelopeInternal();
        }

        @Override
        public GeometryFactory getFactory() {
            return geometry.getFactory();
        }

        @Override
        public Geometry getGeometryN(int n) {
            return geometry.getGeometryN(n);
        }

        @Override
        public String getGeometryType() {
            return geometry.getGeometryType();
        }

        @Override
        public Point getInteriorPoint() {
            return geometry.getInteriorPoint();
        }

        @Override
        public double getLength() {
            return geometry.getLength();
        }

        @Override
        public int getNumGeometries() {
            return geometry.getNumGeometries();
        }

        @Override
        public int getNumPoints() {
            return geometry.getNumPoints();
        }

        @Override
        public PrecisionModel getPrecisionModel() {
            return geometry.getPrecisionModel();
        }

        @Override
        public int getSRID() {
            return geometry.getSRID();
        }

        @Override
        public Object getUserData() {
            return geometry.getUserData();
        }

        @Override
        public int hashCode() {
            return geometry.hashCode();
        }

        @Override
        public Geometry intersection(Geometry other) {
            return geometry.intersection(other);
        }

        @Override
        public boolean intersects(Geometry g) {
            return geometry.intersects(g);
        }

        @Override
        public boolean isEmpty() {
            return geometry.isEmpty();
        }

        @Override
        public boolean isRectangle() {
            return geometry.isRectangle();
        }

        @Override
        public boolean isSimple() {
            return geometry.isSimple();
        }

        @Override
        public boolean isValid() {
            return geometry.isValid();
        }

        @Override
        public boolean isWithinDistance(Geometry geom, double distance) {
            return geometry.isWithinDistance(geom, distance);
        }

        @Override
        public void normalize() {
            geometry.normalize();
        }

        @Override
        public boolean overlaps(Geometry g) {
            return geometry.overlaps(g);
        }

        @Override
        public boolean relate(Geometry g, String intersectionPattern) {
            return geometry.relate(g, intersectionPattern);
        }

        @Override
        public IntersectionMatrix relate(Geometry g) {
            return geometry.relate(g);
        }

        @Override
        public GeometryCollection reverse() {
            return new GeometryCollection(new Geometry[] {geometry.reverse()}, factory);
        }

        @Override
        public void setSRID(int SRID) {
            geometry.setSRID(SRID);
        }

        @Override
        public void setUserData(Object userData) {
            geometry.setUserData(userData);
        }

        @Override
        public Geometry symDifference(Geometry other) {
            return geometry.symDifference(other);
        }

        @Override
        public String toString() {
            return geometry.toString();
        }

        @Override
        public String toText() {
            return geometry.toText();
        }

        @Override
        public boolean touches(Geometry g) {
            return geometry.touches(g);
        }

        @Override
        public Geometry union() {
            return geometry.union();
        }

        @Override
        public Geometry union(Geometry other) {
            return geometry.union(other);
        }

        @Override
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

        @Override
        public void apply(CoordinateFilter filter) {
            lineString.apply(filter);
        }

        @Override
        public void apply(CoordinateSequenceFilter filter) {
            lineString.apply(filter);
        }

        @Override
        public void apply(GeometryComponentFilter filter) {
            lineString.apply(filter);
        }

        @Override
        public void apply(GeometryFilter filter) {
            lineString.apply(filter);
        }

        @Override
        public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
            return lineString.buffer(distance, quadrantSegments, endCapStyle);
        }

        @Override
        public Geometry buffer(double distance, int quadrantSegments) {
            return lineString.buffer(distance, quadrantSegments);
        }

        @Override
        public Geometry buffer(double distance) {
            return lineString.buffer(distance);
        }

        @Override
        public int compareTo(Object o, CoordinateSequenceComparator comp) {
            return lineString.compareTo(o, comp);
        }

        @Override
        public int compareTo(Object o) {
            return lineString.compareTo(o);
        }

        @Override
        public boolean contains(Geometry g) {
            return lineString.contains(g);
        }

        @Override
        public Geometry convexHull() {
            return lineString.convexHull();
        }

        @Override
        public boolean coveredBy(Geometry g) {
            return lineString.coveredBy(g);
        }

        @Override
        public boolean covers(Geometry g) {
            return lineString.covers(g);
        }

        @Override
        public boolean crosses(Geometry g) {
            return lineString.crosses(g);
        }

        @Override
        public Geometry difference(Geometry other) {
            return lineString.difference(other);
        }

        @Override
        public boolean disjoint(Geometry g) {
            return lineString.disjoint(g);
        }

        @Override
        public double distance(Geometry g) {
            return lineString.distance(g);
        }

        @Override
        public boolean equals(Geometry g) {
            return lineString.equalsTopo(g);
        }

        @Override
        @SuppressFBWarnings("EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS")
        public boolean equals(Object obj) {
            return lineString.equals(obj);
        }

        @Override
        public boolean equalsExact(Geometry other, double tolerance) {
            return lineString.equalsExact(other, tolerance);
        }

        @Override
        public boolean equalsExact(Geometry other) {
            return lineString.equalsExact(other);
        }

        @Override
        public void geometryChanged() {
            lineString.geometryChanged();
        }

        @Override
        public double getArea() {
            return lineString.getArea();
        }

        @Override
        public Geometry getBoundary() {
            return lineString.getBoundary();
        }

        @Override
        public int getBoundaryDimension() {
            return lineString.getBoundaryDimension();
        }

        @Override
        public Point getCentroid() {
            return lineString.getCentroid();
        }

        @Override
        public Coordinate getCoordinate() {
            return lineString.getCoordinate();
        }

        public Coordinate getCoordinateN(int n) {
            return lineString.getCoordinateN(n);
        }

        @Override
        public Coordinate[] getCoordinates() {
            return lineString.getCoordinates();
        }

        public CoordinateSequence getCoordinateSequence() {
            return lineString.getCoordinateSequence();
        }

        @Override
        public int getDimension() {
            return lineString.getDimension();
        }

        public Point getEndPoint() {
            return lineString.getEndPoint();
        }

        @Override
        public Geometry getEnvelope() {
            return lineString.getEnvelope();
        }

        @Override
        public Envelope getEnvelopeInternal() {
            return lineString.getEnvelopeInternal();
        }

        @Override
        public GeometryFactory getFactory() {
            return lineString.getFactory();
        }

        @Override
        public Geometry getGeometryN(int n) {
            return lineString.getGeometryN(n);
        }

        @Override
        public String getGeometryType() {
            return lineString.getGeometryType();
        }

        @Override
        public Point getInteriorPoint() {
            return lineString.getInteriorPoint();
        }

        @Override
        public double getLength() {
            return lineString.getLength();
        }

        @Override
        public int getNumGeometries() {
            return lineString.getNumGeometries();
        }

        @Override
        public int getNumPoints() {
            return lineString.getNumPoints();
        }

        public Point getPointN(int n) {
            return lineString.getPointN(n);
        }

        @Override
        public PrecisionModel getPrecisionModel() {
            return lineString.getPrecisionModel();
        }

        @Override
        public int getSRID() {
            return lineString.getSRID();
        }

        public Point getStartPoint() {
            return lineString.getStartPoint();
        }

        @Override
        public Object getUserData() {
            return lineString.getUserData();
        }

        @Override
        public int hashCode() {
            return lineString.hashCode();
        }

        @Override
        public Geometry intersection(Geometry other) {
            return lineString.intersection(other);
        }

        @Override
        public boolean intersects(Geometry g) {
            return lineString.intersects(g);
        }

        @Override
        public boolean isClosed() {
            return lineString.isClosed();
        }

        public boolean isCoordinate(Coordinate pt) {
            return lineString.isCoordinate(pt);
        }

        @Override
        public boolean isEmpty() {
            return lineString.isEmpty();
        }

        @Override
        public boolean isRectangle() {
            return lineString.isRectangle();
        }

        public boolean isRing() {
            return lineString.isRing();
        }

        @Override
        public boolean isSimple() {
            return lineString.isSimple();
        }

        @Override
        public boolean isValid() {
            return lineString.isValid();
        }

        @Override
        public boolean isWithinDistance(Geometry geom, double distance) {
            return lineString.isWithinDistance(geom, distance);
        }

        @Override
        public void normalize() {
            lineString.normalize();
        }

        @Override
        public boolean overlaps(Geometry g) {
            return lineString.overlaps(g);
        }

        @Override
        public boolean relate(Geometry g, String intersectionPattern) {
            return lineString.relate(g, intersectionPattern);
        }

        @Override
        public IntersectionMatrix relate(Geometry g) {
            return lineString.relate(g);
        }

        @Override
        public MultiLineString reverse() {
            return new MultiLineString(new LineString[] {lineString.reverse()}, factory);
        }

        @Override
        public void setSRID(int SRID) {
            lineString.setSRID(SRID);
        }

        @Override
        public void setUserData(Object userData) {
            lineString.setUserData(userData);
        }

        @Override
        public Geometry symDifference(Geometry other) {
            return lineString.symDifference(other);
        }

        @Override
        public String toString() {
            return lineString.toString();
        }

        @Override
        public String toText() {
            return lineString.toText();
        }

        @Override
        public boolean touches(Geometry g) {
            return lineString.touches(g);
        }

        @Override
        public Geometry union() {
            return lineString.union();
        }

        @Override
        public Geometry union(Geometry other) {
            return lineString.union(other);
        }

        @Override
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

        @Override
        public void apply(CoordinateFilter filter) {
            polygon.apply(filter);
        }

        @Override
        public void apply(CoordinateSequenceFilter filter) {
            polygon.apply(filter);
        }

        @Override
        public void apply(GeometryComponentFilter filter) {
            polygon.apply(filter);
        }

        @Override
        public void apply(GeometryFilter filter) {
            polygon.apply(filter);
        }

        @Override
        public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
            return polygon.buffer(distance, quadrantSegments, endCapStyle);
        }

        @Override
        public Geometry buffer(double distance, int quadrantSegments) {
            return polygon.buffer(distance, quadrantSegments);
        }

        @Override
        public Geometry buffer(double distance) {
            return polygon.buffer(distance);
        }

        @Override
        public int compareTo(Object o, CoordinateSequenceComparator comp) {
            return polygon.compareTo(o, comp);
        }

        @Override
        public int compareTo(Object o) {
            return polygon.compareTo(o);
        }

        @Override
        public boolean contains(Geometry g) {
            return polygon.contains(g);
        }

        @Override
        public Geometry convexHull() {
            return polygon.convexHull();
        }

        @Override
        public boolean coveredBy(Geometry g) {
            return polygon.coveredBy(g);
        }

        @Override
        public boolean covers(Geometry g) {
            return polygon.covers(g);
        }

        @Override
        public boolean crosses(Geometry g) {
            return polygon.crosses(g);
        }

        @Override
        public Geometry difference(Geometry other) {
            return polygon.difference(other);
        }

        @Override
        public boolean disjoint(Geometry g) {
            return polygon.disjoint(g);
        }

        @Override
        public double distance(Geometry g) {
            return polygon.distance(g);
        }

        @Override
        public boolean equals(Geometry g) {
            return polygon.equalsTopo(g);
        }

        @Override
        public boolean equals(Object obj) {
            return polygon.equals(obj);
        }

        @Override
        public boolean equalsExact(Geometry other, double tolerance) {
            return polygon.equalsExact(other, tolerance);
        }

        @Override
        public boolean equalsExact(Geometry other) {
            return polygon.equalsExact(other);
        }

        @Override
        public void geometryChanged() {
            polygon.geometryChanged();
        }

        @Override
        public double getArea() {
            return polygon.getArea();
        }

        @Override
        public Geometry getBoundary() {
            return polygon.getBoundary();
        }

        @Override
        public int getBoundaryDimension() {
            return polygon.getBoundaryDimension();
        }

        @Override
        public Point getCentroid() {
            return polygon.getCentroid();
        }

        @Override
        public Coordinate getCoordinate() {
            return polygon.getCoordinate();
        }

        @Override
        public Coordinate[] getCoordinates() {
            return polygon.getCoordinates();
        }

        @Override
        public int getDimension() {
            return polygon.getDimension();
        }

        @Override
        public Geometry getEnvelope() {
            return polygon.getEnvelope();
        }

        @Override
        public Envelope getEnvelopeInternal() {
            return polygon.getEnvelopeInternal();
        }

        public LineString getExteriorRing() {
            return polygon.getExteriorRing();
        }

        @Override
        public GeometryFactory getFactory() {
            return polygon.getFactory();
        }

        @Override
        public Geometry getGeometryN(int n) {
            return polygon.getGeometryN(n);
        }

        @Override
        public String getGeometryType() {
            return polygon.getGeometryType();
        }

        @Override
        public Point getInteriorPoint() {
            return polygon.getInteriorPoint();
        }

        public LineString getInteriorRingN(int n) {
            return polygon.getInteriorRingN(n);
        }

        @Override
        public double getLength() {
            return polygon.getLength();
        }

        @Override
        public int getNumGeometries() {
            return polygon.getNumGeometries();
        }

        public int getNumInteriorRing() {
            return polygon.getNumInteriorRing();
        }

        @Override
        public int getNumPoints() {
            return polygon.getNumPoints();
        }

        @Override
        public PrecisionModel getPrecisionModel() {
            return polygon.getPrecisionModel();
        }

        @Override
        public int getSRID() {
            return polygon.getSRID();
        }

        @Override
        public Object getUserData() {
            return polygon.getUserData();
        }

        @Override
        public int hashCode() {
            return polygon.hashCode();
        }

        @Override
        public Geometry intersection(Geometry other) {
            return polygon.intersection(other);
        }

        @Override
        public boolean intersects(Geometry g) {
            return polygon.intersects(g);
        }

        @Override
        public boolean isEmpty() {
            return polygon.isEmpty();
        }

        @Override
        public boolean isRectangle() {
            return polygon.isRectangle();
        }

        @Override
        public boolean isSimple() {
            return polygon.isSimple();
        }

        @Override
        public boolean isValid() {
            return polygon.isValid();
        }

        @Override
        public boolean isWithinDistance(Geometry geom, double distance) {
            return polygon.isWithinDistance(geom, distance);
        }

        @Override
        public void normalize() {
            polygon.normalize();
        }

        @Override
        public boolean overlaps(Geometry g) {
            return polygon.overlaps(g);
        }

        @Override
        public boolean relate(Geometry g, String intersectionPattern) {
            return polygon.relate(g, intersectionPattern);
        }

        @Override
        public IntersectionMatrix relate(Geometry g) {
            return polygon.relate(g);
        }

        @Override
        public MultiPolygon reverse() {
            return new MultiPolygon(new Polygon[] {polygon.reverse()}, factory);
        }

        @Override
        public void setSRID(int SRID) {
            polygon.setSRID(SRID);
        }

        @Override
        public void setUserData(Object userData) {
            polygon.setUserData(userData);
        }

        @Override
        public Geometry symDifference(Geometry other) {
            return polygon.symDifference(other);
        }

        @Override
        public String toString() {
            return polygon.toString();
        }

        @Override
        public String toText() {
            return polygon.toText();
        }

        @Override
        public boolean touches(Geometry g) {
            return polygon.touches(g);
        }

        @Override
        public Geometry union() {
            return polygon.union();
        }

        @Override
        public Geometry union(Geometry other) {
            return polygon.union(other);
        }

        @Override
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

        @Override
        public void apply(CoordinateFilter filter) {
            point.apply(filter);
        }

        @Override
        public void apply(CoordinateSequenceFilter filter) {
            point.apply(filter);
        }

        @Override
        public void apply(GeometryComponentFilter filter) {
            point.apply(filter);
        }

        @Override
        public void apply(GeometryFilter filter) {
            point.apply(filter);
        }

        @Override
        public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
            return point.buffer(distance, quadrantSegments, endCapStyle);
        }

        @Override
        public Geometry buffer(double distance, int quadrantSegments) {
            return point.buffer(distance, quadrantSegments);
        }

        @Override
        public Geometry buffer(double distance) {
            return point.buffer(distance);
        }

        @Override
        public int compareTo(Object o, CoordinateSequenceComparator comp) {
            return point.compareTo(o, comp);
        }

        @Override
        public int compareTo(Object o) {
            return point.compareTo(o);
        }

        @Override
        public boolean contains(Geometry g) {
            return point.contains(g);
        }

        @Override
        public Geometry convexHull() {
            return point.convexHull();
        }

        @Override
        public boolean coveredBy(Geometry g) {
            return point.coveredBy(g);
        }

        @Override
        public boolean covers(Geometry g) {
            return point.covers(g);
        }

        @Override
        public boolean crosses(Geometry g) {
            return point.crosses(g);
        }

        @Override
        public Geometry difference(Geometry other) {
            return point.difference(other);
        }

        @Override
        public boolean disjoint(Geometry g) {
            return point.disjoint(g);
        }

        @Override
        public double distance(Geometry g) {
            return point.distance(g);
        }

        @Override
        public boolean equals(Geometry g) {
            return point.equalsTopo(g);
        }

        @Override
        public boolean equals(Object obj) {
            return point.equals(obj);
        }

        @Override
        public boolean equalsExact(Geometry other, double tolerance) {
            return point.equalsExact(other, tolerance);
        }

        @Override
        public boolean equalsExact(Geometry other) {
            return point.equalsExact(other);
        }

        @Override
        public void geometryChanged() {
            point.geometryChanged();
        }

        @Override
        public double getArea() {
            return point.getArea();
        }

        @Override
        public Geometry getBoundary() {
            return point.getBoundary();
        }

        @Override
        public int getBoundaryDimension() {
            return point.getBoundaryDimension();
        }

        @Override
        public Point getCentroid() {
            return point.getCentroid();
        }

        @Override
        public Coordinate getCoordinate() {
            return point.getCoordinate();
        }

        @Override
        public Coordinate[] getCoordinates() {
            return point.getCoordinates();
        }

        public CoordinateSequence getCoordinateSequence() {
            return point.getCoordinateSequence();
        }

        @Override
        public int getDimension() {
            return point.getDimension();
        }

        @Override
        public Geometry getEnvelope() {
            return point.getEnvelope();
        }

        @Override
        public Envelope getEnvelopeInternal() {
            return point.getEnvelopeInternal();
        }

        @Override
        public GeometryFactory getFactory() {
            return point.getFactory();
        }

        @Override
        public Geometry getGeometryN(int n) {
            return point.getGeometryN(n);
        }

        @Override
        public String getGeometryType() {
            return point.getGeometryType();
        }

        @Override
        public Point getInteriorPoint() {
            return point.getInteriorPoint();
        }

        @Override
        public double getLength() {
            return point.getLength();
        }

        @Override
        public int getNumGeometries() {
            return point.getNumGeometries();
        }

        @Override
        public int getNumPoints() {
            return point.getNumPoints();
        }

        @Override
        public PrecisionModel getPrecisionModel() {
            return point.getPrecisionModel();
        }

        @Override
        public int getSRID() {
            return point.getSRID();
        }

        @Override
        public Object getUserData() {
            return point.getUserData();
        }

        public double getX() {
            return point.getX();
        }

        public double getY() {
            return point.getY();
        }

        @Override
        public int hashCode() {
            return point.hashCode();
        }

        @Override
        public Geometry intersection(Geometry other) {
            return point.intersection(other);
        }

        @Override
        public boolean intersects(Geometry g) {
            return point.intersects(g);
        }

        @Override
        public boolean isEmpty() {
            return point.isEmpty();
        }

        @Override
        public boolean isRectangle() {
            return point.isRectangle();
        }

        @Override
        public boolean isSimple() {
            return point.isSimple();
        }

        @Override
        public boolean isValid() {
            return point.isValid();
        }

        @Override
        public boolean isWithinDistance(Geometry geom, double distance) {
            return point.isWithinDistance(geom, distance);
        }

        @Override
        public void normalize() {
            point.normalize();
        }

        @Override
        public boolean overlaps(Geometry g) {
            return point.overlaps(g);
        }

        @Override
        public boolean relate(Geometry g, String intersectionPattern) {
            return point.relate(g, intersectionPattern);
        }

        @Override
        public IntersectionMatrix relate(Geometry g) {
            return point.relate(g);
        }

        @Override
        public MultiPoint reverse() {
            return new MultiPoint(new Point[] {point.reverse()}, factory);
        }

        @Override
        public void setSRID(int SRID) {
            point.setSRID(SRID);
        }

        @Override
        public void setUserData(Object userData) {
            point.setUserData(userData);
        }

        @Override
        public Geometry symDifference(Geometry other) {
            return point.symDifference(other);
        }

        @Override
        public String toString() {
            return point.toString();
        }

        @Override
        public String toText() {
            return point.toText();
        }

        @Override
        public boolean touches(Geometry g) {
            return point.touches(g);
        }

        @Override
        public Geometry union() {
            return point.union();
        }

        @Override
        public Geometry union(Geometry other) {
            return point.union(other);
        }

        @Override
        public boolean within(Geometry g) {
            return point.within(g);
        }
    }
}
