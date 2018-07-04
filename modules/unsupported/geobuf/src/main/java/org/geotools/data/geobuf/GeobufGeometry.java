/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;

/**
 * The GeobufGeometry class encodes and decodes geobuf geometries
 *
 * @author Jared Erickson
 */
public class GeobufGeometry {

    private int precision;

    private int dimension;

    private double maxNumberOfDecimalPlaces;

    private GeometryFactory geometryFactory;

    public GeobufGeometry() {
        this(6, 2, JTSFactoryFinder.getGeometryFactory(null));
    }

    public GeobufGeometry(int precision, int dimension) {
        this(precision, dimension, JTSFactoryFinder.getGeometryFactory(null));
    }

    public GeobufGeometry(int precision, int dimension, GeometryFactory geometryFactory) {
        this.precision = precision;
        this.dimension = dimension;
        this.maxNumberOfDecimalPlaces = Math.pow(10, precision);
        this.geometryFactory = geometryFactory;
    }

    public int getDimension() {
        return this.dimension;
    }

    public int getPrecision() {
        return this.precision;
    }

    public void encode(Geometry geometry, OutputStream out) throws IOException {
        Geobuf.Data.Builder dataBuilder = Geobuf.Data.newBuilder();
        dataBuilder.setDimensions(dimension);
        dataBuilder.setPrecision(precision);
        Geobuf.Data.Geometry g = encode(geometry);
        dataBuilder.setGeometry(g);
        dataBuilder.build().writeTo(out);
    }

    public Geometry decode(InputStream in) throws IOException {
        Geobuf.Data data = Geobuf.Data.parseFrom(in);
        if (data.getDataTypeCase() != Geobuf.Data.DataTypeCase.GEOMETRY) {
            throw new IllegalArgumentException("Geobuf data type is not Geometry!");
        }
        return decode(data.getGeometry());
    }

    protected Geobuf.Data.Geometry encode(Geometry geometry) {
        Geobuf.Data.Geometry.Builder builder = Geobuf.Data.Geometry.newBuilder();
        if (geometry instanceof Point) {
            Point point = (Point) geometry;
            builder.setType(Geobuf.Data.Geometry.Type.POINT);
            addCoords(builder, new Coordinate[] {point.getCoordinate()});
        } else if (geometry instanceof LineString) {
            LineString line = (LineString) geometry;
            builder.setType(Geobuf.Data.Geometry.Type.LINESTRING);
            Coordinate[] coords = line.getCoordinates();
            addCoords(builder, coords);
        } else if (geometry instanceof Polygon) {
            Polygon polygon = (Polygon) geometry;
            builder.setType(Geobuf.Data.Geometry.Type.POLYGON);
            Coordinate[] coords = polygon.getCoordinates();
            addCoords(builder, coords);
        } else if (geometry instanceof MultiPoint) {
            MultiPoint multiPoint = (MultiPoint) geometry;
            builder.setType(Geobuf.Data.Geometry.Type.MULTIPOINT);
            Coordinate[] coords = multiPoint.getCoordinates();
            addCoords(builder, coords);
        } else if (geometry instanceof MultiLineString) {
            MultiLineString multiLineString = (MultiLineString) geometry;
            builder.setType(Geobuf.Data.Geometry.Type.MULTILINESTRING);
            for (int i = 0; i < multiLineString.getNumGeometries(); i++) {
                LineString line = (LineString) multiLineString.getGeometryN(i);
                builder.addLengths(line.getCoordinates().length);
                addCoords(builder, line.getCoordinates());
            }
        } else if (geometry instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) geometry;
            builder.setType(Geobuf.Data.Geometry.Type.MULTIPOLYGON);
            builder.addLengths(multiPolygon.getNumGeometries());
            for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
                builder.addLengths(1 + polygon.getNumInteriorRing());
                builder.addLengths(polygon.getExteriorRing().getCoordinates().length);
                addCoords(builder, polygon.getExteriorRing().getCoordinates());
                for (int j = 0; j < polygon.getNumInteriorRing(); j++) {
                    LinearRing ring = (LinearRing) polygon.getInteriorRingN(j);
                    builder.addLengths(ring.getCoordinates().length);
                    addCoords(builder, ring.getCoordinates());
                }
            }
        } else if (geometry instanceof GeometryCollection) {
            GeometryCollection geometryCollection = (GeometryCollection) geometry;
            builder.setType(Geobuf.Data.Geometry.Type.GEOMETRYCOLLECTION);
            for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
                Geometry geom = geometryCollection.getGeometryN(i);
                builder.addGeometries(encode(geom));
            }
        }
        Geobuf.Data.Geometry geom = builder.build();
        return geom;
    }

    private void addCoords(Geobuf.Data.Geometry.Builder builder, Coordinate[] coords) {
        for (int i = 0; i < coords.length; i++) {
            Coordinate coord = coords[i];
            long x = Math.round(coord.x * maxNumberOfDecimalPlaces);
            long y = Math.round(coord.y * maxNumberOfDecimalPlaces);
            if (i > 0) {
                Coordinate prevCoord = coords[i - 1];
                x = x - Math.round(prevCoord.x * maxNumberOfDecimalPlaces);
                y = y - Math.round(prevCoord.y * maxNumberOfDecimalPlaces);
            }
            builder.addCoords(x).addCoords(y);
        }
    }

    protected Geometry decode(Geobuf.Data.Geometry g) {
        if (g.getType() == Geobuf.Data.Geometry.Type.POINT) {
            return geometryFactory.createPoint(getAllCoordinates(g)[0]);
        } else if (g.getType() == Geobuf.Data.Geometry.Type.LINESTRING) {
            return geometryFactory.createLineString(getAllCoordinates(g));
        } else if (g.getType() == Geobuf.Data.Geometry.Type.POLYGON) {
            Coordinate[] coords = getAllCoordinates(g);
            if (!CoordinateArrays.isRing(coords)) {
                Coordinate[] closedCoords = new Coordinate[coords.length + 1];
                CoordinateArrays.copyDeep(coords, 0, closedCoords, 0, coords.length);
                closedCoords[closedCoords.length - 1] = coords[0];
                coords = closedCoords;
            }
            LinearRing shell = geometryFactory.createLinearRing(coords);
            LinearRing[] holes = new LinearRing[0];
            return geometryFactory.createPolygon(shell, holes);
        } else if (g.getType() == Geobuf.Data.Geometry.Type.MULTIPOINT) {
            Coordinate[] coords = getAllCoordinates(g);
            return geometryFactory.createMultiPoint(coords);
        } else if (g.getType() == Geobuf.Data.Geometry.Type.MULTILINESTRING) {
            List<Coordinate[]> listOfCoordinates = getCoordinates(g);
            LineString[] lines = new LineString[listOfCoordinates.size()];
            for (int i = 0; i < listOfCoordinates.size(); i++) {
                lines[i] = geometryFactory.createLineString(listOfCoordinates.get(i));
            }
            return geometryFactory.createMultiLineString(lines);
        } else if (g.getType() == Geobuf.Data.Geometry.Type.MULTIPOLYGON) {
            int lengthPosition = 0;
            int numberOfPolygons = g.getLengths(lengthPosition);
            lengthPosition++;
            Polygon[] polygons = new Polygon[numberOfPolygons];
            int start = 0;
            for (int p = 0; p < numberOfPolygons; p++) {
                int numberOfRings = g.getLengths(lengthPosition);
                lengthPosition++;
                LinearRing[] rings = new LinearRing[numberOfRings];
                for (int r = 0; r < numberOfRings; r++) {
                    int numberOfCoordinates = g.getLengths(lengthPosition);
                    lengthPosition++;
                    int end = start + numberOfCoordinates * dimension;
                    Coordinate[] coords = getCoordinates(g, start, end);
                    if (!CoordinateArrays.isRing(coords)) {
                        Coordinate[] closedCoords = new Coordinate[coords.length + 1];
                        CoordinateArrays.copyDeep(coords, 0, closedCoords, 0, coords.length);
                        closedCoords[closedCoords.length - 1] = coords[0];
                        coords = closedCoords;
                    }
                    rings[r] = geometryFactory.createLinearRing(coords);
                    start = end;
                }
                if (rings.length > 1) {
                    polygons[p] =
                            geometryFactory.createPolygon(
                                    rings[0], Arrays.copyOfRange(rings, 1, rings.length));
                } else {
                    polygons[p] = geometryFactory.createPolygon(rings[0]);
                }
            }
            return geometryFactory.createMultiPolygon(polygons);
        } else if (g.getType() == Geobuf.Data.Geometry.Type.GEOMETRYCOLLECTION) {
            List<Geometry> geoms = getGeometries(g);
            return geometryFactory.createGeometryCollection(geoms.toArray(new Geometry[] {}));
        } else {
            return null;
        }
    }

    protected List<Geometry> getGeometries(Geobuf.Data.Geometry g) {
        List<Geometry> geometries = new ArrayList<Geometry>();
        getGeometries(geometries, g);
        return geometries;
    }

    protected void getGeometries(List<Geometry> geometries, Geobuf.Data.Geometry g) {
        int count = g.getGeometriesCount();
        if (count < 2) {
            geometries.add(decode(g));
        } else {
            for (int i = 0; i < count; i++) {
                getGeometries(geometries, g.getGeometries(i));
            }
        }
    }

    protected Coordinate[] getCoordinates(Geobuf.Data.Geometry g, int start, int end) {
        int numberOfCoords = (end - start) / dimension;
        Coordinate[] coords = new Coordinate[numberOfCoords];
        int coordinateCounter = 0;
        int c = start;
        for (int i = start; i < start + numberOfCoords; i++) {
            Coordinate coord = new Coordinate();
            for (int k = 0; k < dimension; k++) {
                double value = g.getCoords(c);
                for (int l = start + k; l < c; l += dimension) {
                    value += g.getCoords(l);
                }
                value = value / maxNumberOfDecimalPlaces;
                if (k == 0) {
                    coord.x = value;
                } else if (k == 1) {
                    coord.y = value;
                } else if (k == 2) {
                    coord.z = value;
                }
                c++;
            }
            coords[coordinateCounter] = coord;
            coordinateCounter++;
        }
        return coords;
    }

    protected List<Coordinate[]> getCoordinates(Geobuf.Data.Geometry g) {
        List<Coordinate[]> listOfCoordinates = new ArrayList<Coordinate[]>();
        int numberOfLengths = g.getLengthsCount();
        if (numberOfLengths == 0) {
            listOfCoordinates.add(getAllCoordinates(g));
        } else {
            int c = 0;
            int start = 0;
            for (int i = 0; i < numberOfLengths; i++) {
                int len = g.getLengths(i);
                int end = start + (len * dimension);
                Coordinate[] coordinates = getCoordinates(g, start, end);
                listOfCoordinates.add(coordinates);
                start = end;
            }
        }
        return listOfCoordinates;
    }

    protected Coordinate[] getAllCoordinates(Geobuf.Data.Geometry g) {
        return getCoordinates(g, 0, g.getCoordsCount());
    }
}
