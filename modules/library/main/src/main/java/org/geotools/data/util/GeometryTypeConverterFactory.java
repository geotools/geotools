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
package org.geotools.data.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundCurve;
import org.geotools.geometry.jts.CompoundRing;
import org.geotools.geometry.jts.CurvedGeometry;
import org.geotools.geometry.jts.MultiCurve;
import org.geotools.geometry.jts.MultiCurvedGeometry;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
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
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Converter factory performing conversions among JTS geometries of different types.
 *
 * <p>- single type geometries (Point, LineString, Polygon) are converted to multi types
 * (MultiPoint, LineString, MultiPolygon) containing 1 geometry element. - GeometryCollection(s) are
 * converted to multi types, converting each element to the permitted type (Point, LineString or
 * Polygon).
 *
 * @author m.bartolomeoli
 */
public class GeometryTypeConverterFactory implements ConverterFactory {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GeometryTypeConverterFactory.class);

    static GeometryFactory gFac = new GeometryFactory();

    public Converter createConverter(Class<?> source, Class<?> target, Hints hints) {
        // special case for curved geometries
        if (Geometry.class.isAssignableFrom(source)
                && CurvedGeometry.class.isAssignableFrom(target)) {
            return new Converter() {

                @Override
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    Geometry result = null;
                    Geometry sourceGeometry = (Geometry) source;
                    if (MultiCurvedGeometry.class.isAssignableFrom(target)) {
                        MultiLineString multiLineString =
                                Converters.convert(source, MultiLineString.class);
                        if (multiLineString == null) {
                            return null;
                        } else {
                            List<LineString> components = new ArrayList<>();
                            double tolerance = Double.MAX_VALUE;

                            for (int i = 0; i < multiLineString.getNumGeometries(); i++) {
                                LineString geom = (LineString) multiLineString.getGeometryN(0);
                                if (geom instanceof CurvedGeometry) {
                                    tolerance =
                                            Math.min(
                                                    tolerance,
                                                    ((CurvedGeometry) geom).getTolerance());
                                }
                                components.add(geom);
                            }
                            GeometryFactory factory = ((Geometry) source).getFactory();
                            result = new MultiCurve(components, factory, tolerance);
                        }
                    } else if (source instanceof CircularRing
                            && CircularString.class.isAssignableFrom(target)) {
                        CircularRing cr = (CircularRing) source;
                        result =
                                new CircularString(
                                        cr.getControlPoints(), cr.getFactory(), cr.getTolerance());
                    } else if (source instanceof CompoundRing
                            && CompoundCurve.class.isAssignableFrom(target)) {
                        CompoundRing cr = (CompoundRing) source;
                        result =
                                new CompoundCurve(
                                        cr.getComponents(), cr.getFactory(), cr.getTolerance());
                    } else {
                        LineString converted = Converters.convert(source, LineString.class);
                        if (converted.isEmpty()) {
                            List<LineString> components = new ArrayList<>();
                            result =
                                    new CompoundRing(
                                            components, converted.getFactory(), Double.MAX_VALUE);
                        } else if (converted instanceof LinearRing) {
                            result =
                                    new CompoundRing(
                                            Arrays.asList(converted),
                                            ((LineString) source).getFactory(),
                                            Double.MAX_VALUE);
                        } else {
                            result =
                                    new CompoundCurve(
                                            Arrays.asList(converted),
                                            converted.getFactory(),
                                            Double.MAX_VALUE);
                        }
                    }
                    if (result != null) {
                        copyUserProperties(sourceGeometry, result);
                    }
                    return (T) result;
                }
            };
        } else if (Geometry.class.isAssignableFrom(source)
                && Geometry.class.isAssignableFrom(target)) {
            // we can convert geometric types
            return new Converter() {
                /**
                 * Converts all the geometries of the given GeometryCollection to a specified simple
                 * type.
                 *
                 * @param <T>
                 */
                public <T> List<T> convertAll(GeometryCollection gc, Class<T> target)
                        throws Exception {
                    List<T> result = new ArrayList<T>();
                    for (int count = 0; count < gc.getNumGeometries(); count++) {
                        T geo = (T) convert(gc.getGeometryN(count), target);
                        if (geo != null) result.add(geo);
                    }
                    return result;
                }

                public Object convert(Object source, Class target) throws Exception {
                    // hierarchy compatible geometries -> nothing to do
                    if (target.isAssignableFrom(source.getClass())) return source;
                    if (source instanceof Geometry) {
                        Geometry sourceGeometry = (Geometry) source;

                        Geometry destGeometry = null;
                        // multi<geometry> types: for each one we
                        // try the followings:
                        //  - if source is <geometry> we create a multi<geometry> with just 1
                        // element
                        //  - if source is a GeometryCollection, we try to convert each element to
                        // <geometry>
                        //  - else we firtsly convert the geometry to a <geometry> and then we
                        // create a multi<geometry> with the obtained element
                        if (MultiPoint.class.isAssignableFrom(target)) {
                            Point[] points;
                            // NC - Empty Geometry Support
                            if (sourceGeometry.isEmpty()) points = new Point[0];
                            else if (source instanceof Point) points = new Point[] {(Point) source};
                            else if (source instanceof GeometryCollection)
                                points =
                                        this.convertAll((GeometryCollection) source, Point.class)
                                                .toArray(new Point[] {});
                            else points = new Point[] {(Point) this.convert(source, Point.class)};
                            destGeometry = gFac.createMultiPoint(points);
                        } else if (MultiLineString.class.isAssignableFrom(target)) {
                            LineString[] lineStrings;
                            // NC - Empty Geometry Support
                            if (sourceGeometry.isEmpty()) lineStrings = new LineString[0];
                            else if (source instanceof LineString)
                                lineStrings = new LineString[] {(LineString) source};
                            else if (source instanceof GeometryCollection)
                                lineStrings =
                                        this.convertAll(
                                                        (GeometryCollection) source,
                                                        LineString.class)
                                                .toArray(new LineString[] {});
                            else
                                lineStrings =
                                        new LineString[] {
                                            (LineString) this.convert(source, LineString.class)
                                        };
                            destGeometry = gFac.createMultiLineString(lineStrings);
                        } else if (MultiPolygon.class.isAssignableFrom(target)) {
                            Polygon[] polygons;
                            // NC - Empty Geometry Support
                            if (sourceGeometry.isEmpty()) polygons = new Polygon[0];
                            else if (source instanceof Polygon)
                                polygons = new Polygon[] {(Polygon) source};
                            else if (source instanceof GeometryCollection)
                                polygons =
                                        this.convertAll((GeometryCollection) source, Polygon.class)
                                                .toArray(new Polygon[] {});
                            else
                                polygons =
                                        new Polygon[] {
                                            (Polygon) this.convert(source, Polygon.class)
                                        };
                            destGeometry = gFac.createMultiPolygon(polygons);
                        }

                        // target is a geometrycollection: we add the source to
                        // a new geometrycollection
                        else if (GeometryCollection.class.isAssignableFrom(target)) {
                            // NC - Empty Geometry Support
                            if (sourceGeometry.isEmpty())
                                destGeometry = gFac.createGeometryCollection(new Geometry[0]);
                            else
                                destGeometry =
                                        gFac.createGeometryCollection(
                                                new Geometry[] {(Geometry) source});
                        }

                        // target is a point: we return the centroid of any complex geometry
                        else if (Point.class.isAssignableFrom(target)) {
                            // NC - Empty Geometry Support
                            if (sourceGeometry.isEmpty())
                                destGeometry = gFac.createPoint((Coordinate) null);
                            else if (source instanceof MultiPoint
                                    && sourceGeometry.getNumGeometries() == 1)
                                destGeometry = ((MultiPoint) source).getGeometryN(0).copy();
                            else {
                                if (LOGGER.isLoggable(Level.FINE))
                                    LOGGER.fine(
                                            "Converting Geometry "
                                                    + source.toString()
                                                    + " to Point. This could be unsafe");
                                destGeometry = ((Geometry) source).getCentroid();
                            }
                        }

                        // target is a linestring: we return the linestring connecting all the
                        // geometry coordinates
                        else if (LineString.class.isAssignableFrom(target)) {
                            // NC - Empty Geometry Support
                            if (sourceGeometry.isEmpty())
                                destGeometry = gFac.createLineString(new Coordinate[0]);
                            else if (source instanceof MultiLineString
                                    && sourceGeometry.getNumGeometries() == 1)
                                destGeometry = ((MultiLineString) source).getGeometryN(0).copy();
                            else {
                                if (LOGGER.isLoggable(Level.FINE))
                                    LOGGER.fine(
                                            "Converting Geometry "
                                                    + source.toString()
                                                    + " to LineString. This could be unsafe");
                                destGeometry =
                                        gFac.createLineString(
                                                getLineStringCoordinates(
                                                        ((Geometry) source).getCoordinates()));
                            }
                        }
                        // target is a polygon: we return a polygon connecting all the coordinates
                        // of the given geometry
                        else if (Polygon.class.isAssignableFrom(target)) {
                            // NC - Empty Geometry Support
                            if (sourceGeometry.isEmpty())
                                destGeometry = gFac.createLineString(new Coordinate[0]);
                            else if (source instanceof MultiPolygon
                                    && sourceGeometry.getNumGeometries() == 1)
                                destGeometry = ((MultiPolygon) source).getGeometryN(0).copy();
                            else {
                                if (LOGGER.isLoggable(Level.FINE))
                                    LOGGER.fine(
                                            "Converting Geometry "
                                                    + source.toString()
                                                    + " to Polygon. This could be unsafe");
                                Coordinate[] coords =
                                        getPolygonCoordinates(((Geometry) source).getCoordinates());
                                destGeometry =
                                        gFac.createPolygon(
                                                gFac.createLinearRing(coords), new LinearRing[] {});
                            }
                        }

                        // NC - added cloning above for cases where an existing geometry is used
                        // for purpose for changing user data - we don't want any side effects
                        copyUserProperties(sourceGeometry, destGeometry);
                        return destGeometry;
                    }

                    return null;
                }

                @SuppressWarnings("unchecked")
                private <T> T[] arrayCopy(T[] original, int length) {
                    Class<?> arrayType = original.getClass().getComponentType();
                    T[] copy = (T[]) java.lang.reflect.Array.newInstance(arrayType, length);
                    System.arraycopy(
                            original,
                            0,
                            copy,
                            0,
                            original.length < length ? original.length : length);
                    return copy;
                }

                /**
                 * Add dummy coordinates to the given array to reach numpoints points. If the array
                 * is already made of numpoints or more coordinates, it will be returned untouched.
                 */
                private Coordinate[] growCoordinatesNum(Coordinate[] input, int numpoints) {
                    if (input.length < numpoints) {
                        Coordinate[] newCoordinates = arrayCopy(input, numpoints);
                        Arrays.fill(newCoordinates, input.length, numpoints, input[0]);

                        input = newCoordinates;
                    }
                    return input;
                }
                /**
                 * Gets a set of coordinates valid to create a linestring: - at least 2 coordinates
                 */
                private Coordinate[] getLineStringCoordinates(Coordinate[] coordinates) {
                    // at least 2 points
                    coordinates = growCoordinatesNum(coordinates, 2);
                    return coordinates;
                }

                /**
                 * Gets a set of coordinates valid to create a polygon: - at least 4 coordinates -
                 * closed path
                 */
                private Coordinate[] getPolygonCoordinates(Coordinate[] coordinates) {
                    // at least 4 points
                    coordinates = growCoordinatesNum(coordinates, 4);

                    if (!coordinates[coordinates.length - 1].equals(coordinates[0])) {
                        Coordinate[] newCoordinates =
                                arrayCopy(coordinates, coordinates.length + 1);
                        newCoordinates[newCoordinates.length - 1] = newCoordinates[0];

                        coordinates = newCoordinates;
                    }
                    return coordinates;
                }
            };
        }

        return null;
    }

    protected void copyUserProperties(Geometry sourceGeometry, Geometry destGeometry) {
        // NC-added, copy userdata
        if (destGeometry != null) {
            Map<Object, Object> newUserData = new HashMap<Object, Object>();

            // copy if anything is already in destination data
            if (destGeometry.getUserData() instanceof Map) {
                newUserData.putAll((Map) destGeometry.getUserData());
            } else if (destGeometry.getUserData() instanceof CoordinateReferenceSystem) {
                newUserData.put(CoordinateReferenceSystem.class, destGeometry.getUserData());
            }
            // overwrite with source
            if (sourceGeometry.getUserData() instanceof Map) {
                newUserData.putAll((Map) sourceGeometry.getUserData());
            } else if (sourceGeometry.getUserData() instanceof CoordinateReferenceSystem) {
                newUserData.put(CoordinateReferenceSystem.class, sourceGeometry.getUserData());
            }

            destGeometry.setUserData(newUserData);
        }
    }
}
