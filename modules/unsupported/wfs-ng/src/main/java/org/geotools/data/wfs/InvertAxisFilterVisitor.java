/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Coordinate;
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
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Returns a clone of the provided filter where all geometries and bboxes have inverted coordinates
 * (x, y) -> (y, x).
 *
 * @author "Mauro Bartolomeoli - mauro.bartolomeoli@geo-solutions.it"
 */
public class InvertAxisFilterVisitor extends DuplicatingFilterVisitor {

    private GeometryFactory geometryFactory;

    public InvertAxisFilterVisitor(FilterFactory2 factory, GeometryFactory geometryFactory) {
        super(factory);
        this.geometryFactory = geometryFactory;
    }

    public Object visit(BBOX filter, Object extraData) {
        if (filter.getExpression2() instanceof Literal) {
            Literal bboxLiteral = (Literal) filter.getExpression2();
            if (bboxLiteral.getValue() instanceof BoundingBox) {
                BoundingBox bounds = (BoundingBox) bboxLiteral.getValue();

                return ff.bbox(
                        filter.getExpression1(),
                        new ReferencedEnvelope(
                                bounds.getMinY(),
                                bounds.getMaxY(),
                                bounds.getMinX(),
                                bounds.getMaxX(),
                                bounds.getCoordinateReferenceSystem()));
            } else if (bboxLiteral.getValue() instanceof Geometry) {
                Geometry geom = (Geometry) bboxLiteral.getValue();
                Envelope geomEnvelope = geom.getEnvelopeInternal();
                CoordinateReferenceSystem crs = null;
                if (geom.getUserData() instanceof CoordinateReferenceSystem) {
                    crs = (CoordinateReferenceSystem) geom.getUserData();
                }
                return ff.bbox(
                        filter.getExpression1(),
                        new ReferencedEnvelope(
                                geomEnvelope.getMinY(),
                                geomEnvelope.getMaxY(),
                                geomEnvelope.getMinX(),
                                geomEnvelope.getMaxX(),
                                crs));
            }
        }
        return filter;
    }

    public Object visit(Literal expression, Object extraData) {
        if (!(expression.getValue() instanceof Geometry)) return super.visit(expression, extraData);

        Geometry geom = (Geometry) expression.getValue();

        return ff.literal(invertGeometryCoordinates(geom));
    }

    /** */
    private Geometry invertGeometryCoordinates(Geometry geom) {
        if (geom instanceof Point) {
            Point point = (Point) geom;
            Coordinate inverted = invertCoordinate(point.getCoordinate());
            return geometryFactory.createPoint(inverted);
        } else if (geom instanceof LineString) {
            Coordinate[] inverted = invertCoordinates(geom.getCoordinates());
            return geometryFactory.createLineString(inverted);
        } else if (geom instanceof Polygon) {
            Polygon polygon = (Polygon) geom;
            Coordinate[] shellCoordinates = polygon.getExteriorRing().getCoordinates();
            LinearRing invertedShell =
                    geometryFactory.createLinearRing(invertCoordinates(shellCoordinates));
            LinearRing[] invertedHoles = new LinearRing[polygon.getNumInteriorRing()];
            for (int count = 0; count < polygon.getNumInteriorRing(); count++) {
                Coordinate[] holeCoordinates = polygon.getInteriorRingN(count).getCoordinates();
                invertedHoles[count] =
                        geometryFactory.createLinearRing(invertCoordinates(holeCoordinates));
            }
            return geometryFactory.createPolygon(invertedShell, invertedHoles);
        } else if (geom instanceof MultiPoint) {
            return geometryFactory.createMultiPoint(
                    new CoordinateArraySequence(invertCoordinates(geom.getCoordinates())));
        } else if (geom instanceof MultiLineString) {
            MultiLineString multiLineString = (MultiLineString) geom;
            LineString[] inverted = new LineString[multiLineString.getNumGeometries()];
            for (int count = 0; count < multiLineString.getNumGeometries(); count++) {
                inverted[count] =
                        (LineString) invertGeometryCoordinates(multiLineString.getGeometryN(count));
            }
            return geometryFactory.createMultiLineString(inverted);
        } else if (geom instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) geom;
            Polygon[] inverted = new Polygon[multiPolygon.getNumGeometries()];
            for (int count = 0; count < multiPolygon.getNumGeometries(); count++) {
                inverted[count] =
                        (Polygon) invertGeometryCoordinates(multiPolygon.getGeometryN(count));
            }
            return geometryFactory.createMultiPolygon(inverted);
        } else if (geom instanceof GeometryCollection) {
            GeometryCollection collection = (GeometryCollection) geom;
            Geometry[] inverted = new Geometry[collection.getNumGeometries()];
            for (int count = 0; count < collection.getNumGeometries(); count++) {
                inverted[count] = invertGeometryCoordinates(collection.getGeometryN(count));
            }
            return geometryFactory.createGeometryCollection(inverted);
        }
        throw new IllegalArgumentException("Unknown geometry type: " + geom.getGeometryType());
    }

    /** */
    private Coordinate[] invertCoordinates(Coordinate[] coordinates) {
        Coordinate[] result = new Coordinate[coordinates.length];
        for (int count = 0; count < coordinates.length; count++) {
            result[count] = invertCoordinate(coordinates[count]);
        }
        return result;
    }

    /** */
    private Coordinate invertCoordinate(Coordinate coordinate) {
        return new Coordinate(coordinate.y, coordinate.x, coordinate.getZ());
    }
}
