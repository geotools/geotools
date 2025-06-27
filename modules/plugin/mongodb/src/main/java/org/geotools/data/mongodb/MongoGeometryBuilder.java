/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;
import org.geotools.geometry.jts.Geometries;
import org.locationtech.jts.algorithm.Orientation;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
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

public class MongoGeometryBuilder {

    GeometryFactory geometryFactory;

    // MongoDB 2.4 doesn't support the multi-geometry
    // GeoJSON types.  A lot of multi-geometry instances encode
    // a single geometry.  This flag will allow the conversion
    // of JTS multi-geometry types encoding a single geometry
    // to their single geometry analog. This should ease some of
    // the pain...
    boolean opportunisticMultiGeometryCoversion = true;

    public MongoGeometryBuilder() {
        this(new GeometryFactory());
    }

    public MongoGeometryBuilder(GeometryFactory geomFactory) {
        this.geometryFactory = geomFactory;
    }

    public Geometry toGeometry(DBObject obj) {
        if (obj == null) {
            return null;
        }
        String type = (String) obj.get("type");

        Geometries g = Geometries.getForName(type);
        if (g == null) {
            throw new IllegalArgumentException("Unable to create geometry of type: " + type);
        }

        BasicDBList list = (BasicDBList) obj.get("coordinates");
        switch (g) {
            case POINT:
                return toPoint(list);
            case LINESTRING:
                return toLineString(list);
            case POLYGON:
                return toPolygon(list);
            case MULTIPOINT:
                return toMultiPoint(list);
            case MULTILINESTRING:
                return toMultiLineString(list);
            case MULTIPOLYGON:
                return toMultiPolygon(list);
            case GEOMETRYCOLLECTION:
                return toGeometryCollection((BasicDBList) obj.get("geometries"));
            default:
                throw new IllegalArgumentException("Unknown geometry type: " + type);
        }
    }

    public DBObject toObject(Envelope envelope) {
        return toObject(geometryFactory.toGeometry(envelope));
    }

    public DBObject toObject(Geometry geom) {
        Geometries g = Geometries.get(geom);
        switch (g) {
            case POINT:
                return toObject((Point) geom);
            case LINESTRING:
                return toObject((LineString) geom);
            case POLYGON:
                return toObject((Polygon) geom);
            case MULTIPOINT:
                return toObject((MultiPoint) geom);
            case MULTILINESTRING:
                return toObject((MultiLineString) geom);
            case MULTIPOLYGON:
                return toObject((MultiPolygon) geom);
            case GEOMETRYCOLLECTION:
                return toObject((GeometryCollection) geom);
            default:
                throw new IllegalArgumentException("Unknown geometry type: " + geom);
        }
    }

    public GeometryCollection toGeometryCollection(BasicDBList obj) {
        List<Geometry> geoms = new ArrayList<>();
        for (Object o : obj) {
            geoms.add(toGeometry((DBObject) o)); // JG: changed from toGeometry( obj )
        }
        return geometryFactory.createGeometryCollection(geoms.toArray(new Geometry[geoms.size()]));
    }

    public DBObject toObject(GeometryCollection gc) {
        return null;
    }

    public MultiPolygon toMultiPolygon(List<?> list) {
        List<Polygon> polys = new ArrayList<>();
        for (Object o : list) {
            polys.add(toPolygon((List<?>) o));
        }
        return geometryFactory.createMultiPolygon(polys.toArray(new Polygon[polys.size()]));
    }

    public DBObject toObject(MultiPolygon mp) {
        if (opportunisticMultiGeometryCoversion && mp.getNumGeometries() == 1) {
            return toObject((Polygon) mp.getGeometryN(0));
        }
        List<Object> l = new BasicDBList();
        for (int i = 0; i < mp.getNumGeometries(); i++) {
            l.add(toList((Polygon) mp.getGeometryN(i)));
        }
        return BasicDBObjectBuilder.start()
                .add("type", "MultiPolygon")
                .add("coordinates", l)
                .get();
    }

    public MultiLineString toMultiLineString(List<?> list) {
        List<LineString> lines = new ArrayList<>();
        for (Object o : list) {
            lines.add(toLineString((List<?>) o));
        }
        return geometryFactory.createMultiLineString(lines.toArray(new LineString[lines.size()]));
    }

    public DBObject toObject(MultiLineString ml) {
        if (opportunisticMultiGeometryCoversion && ml.getNumGeometries() == 1) {
            return toObject((LineString) ml.getGeometryN(0));
        }
        List<Object> l = new BasicDBList();
        for (int i = 0; i < ml.getNumGeometries(); i++) {
            l.add(toList(((LineString) ml.getGeometryN(i)).getCoordinateSequence()));
        }
        return BasicDBObjectBuilder.start()
                .add("type", "MultiLineString")
                .add("coordinates", l)
                .get();
    }

    public MultiPoint toMultiPoint(List<?> list) {
        List<Point> points = new ArrayList<>();
        for (Object o : list) {
            points.add(toPoint((List<?>) o));
        }
        return geometryFactory.createMultiPoint(points.toArray(new Point[points.size()]));
    }

    public DBObject toObject(MultiPoint mp) {
        if (opportunisticMultiGeometryCoversion && mp.getNumGeometries() == 1) {
            return toObject((Point) mp.getGeometryN(0));
        }
        return BasicDBObjectBuilder.start()
                .add("type", "MultiPoint")
                .add("coordinates", toList(mp.getCoordinates()))
                .get();
    }

    public Polygon toPolygon(List<?> list) {
        LinearRing outer = (LinearRing) toLineString((List<?>) list.get(0));
        List<LinearRing> inner = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            inner.add((LinearRing) toLineString((List<?>) list.get(i)));
        }
        return geometryFactory.createPolygon(outer, inner.toArray(new LinearRing[inner.size()]));
    }

    public DBObject toObject(Polygon p) {
        return BasicDBObjectBuilder.start()
                .add("type", "Polygon")
                .add("coordinates", toList(p))
                .get();
    }

    public LineString toLineString(List<?> list) {
        List<Coordinate> coordList = new ArrayList<>(list.size());
        for (Object o : list) {
            coordList.add(toCoordinate((List<?>) o));
        }

        Coordinate[] coords = coordList.toArray(new Coordinate[coordList.size()]);
        if (coords.length > 3 && coords[0].equals(coords[coords.length - 1])) {
            return geometryFactory.createLinearRing(coords);
        }
        return geometryFactory.createLineString(coords);
    }

    public DBObject toObject(LineString l) {
        return BasicDBObjectBuilder.start()
                .add("type", "LineString")
                .add("coordinates", toList(l.getCoordinateSequence()))
                .get();
    }

    public Point toPoint(List<?> list) {
        return geometryFactory.createPoint(toCoordinate(list));
    }

    public DBObject toObject(Point p) {
        return BasicDBObjectBuilder.start()
                .add("type", "Point")
                .add("coordinates", toList(p.getCoordinate()))
                .get();
    }

    public Coordinate toCoordinate(List<?> list) {
        double x = ((Number) list.get(0)).doubleValue();
        double y = ((Number) list.get(1)).doubleValue();
        return new Coordinate(x, y);
    }

    List<?> toList(Coordinate c) {
        BasicDBList l = new BasicDBList();
        l.add(c.x);
        l.add(c.y);
        return l;
    }

    List<?> toList(CoordinateSequence cs) {
        BasicDBList l = new BasicDBList();
        for (int i = 0; i < cs.size(); i++) {
            BasicDBList m = new BasicDBList();
            m.add(cs.getX(i));
            m.add(cs.getY(i));
            l.add(m);
        }
        return l;
    }

    List<?> toList(Coordinate[] cs) {
        BasicDBList l = new BasicDBList();
        for (Coordinate c : cs) {
            BasicDBList m = new BasicDBList();
            m.add(c.x);
            m.add(c.y);
            l.add(m);
        }
        return l;
    }

    List<?> toList(Polygon p) {
        BasicDBList l = new BasicDBList();

        if (!Orientation.isCCW(p.getExteriorRing().getCoordinates())) {
            l.add(toList(p.getExteriorRing().reverse().getCoordinates()));
        } else {
            l.add(toList(p.getExteriorRing().getCoordinateSequence()));
        }

        for (int i = 0; i < p.getNumInteriorRing(); i++) {
            l.add(toList(p.getInteriorRingN(i).getCoordinateSequence()));
        }

        return l;
    }
}
