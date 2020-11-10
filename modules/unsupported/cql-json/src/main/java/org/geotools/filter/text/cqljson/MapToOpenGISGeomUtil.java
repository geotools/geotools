/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cqljson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.geotools.filter.text.generated.parsers.ParseException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;

public class MapToOpenGISGeomUtil {
    public static GeometryFactory FACTORY = null;

    static {
        FACTORY = new GeometryFactory(new PrecisionModel(), 4326);
    }

    /**
     * Jackson returns embedded geojson as a map so we use this method to create opengis.Geometry
     * for filter creation
     *
     * @param geoJsonAsMap GeoJSON Map
     * @return Geometry for filter creation
     * @throws ParseException
     */
    public static Geometry parseMapToGeometry(Map<String, Object> geoJsonAsMap)
            throws ParseException {
        String type = "Bbox";
        List<Double> coordinatesSingle = null;
        List<List<Double>> coordinatesMulti = null;
        if (geoJsonAsMap.get("bbox") != null) {
            coordinatesSingle = convertToArrayList(geoJsonAsMap.get("bbox"));
            return parseSingleDimensionGeometry(type, coordinatesSingle);
        } else {
            type = (String) geoJsonAsMap.get("type");
            Object coordinates = geoJsonAsMap.get("coordinates");
            if (type.equals("Point")) {
                coordinatesSingle = convertToArrayList(coordinates);
                return parseSingleDimensionGeometry(type, coordinatesSingle);
            } else if (type.equals("Polygon") || type.equals("MultiLineString")) {
                List<List<List<Double>>> coordinatesThree =
                        convertToArrayListArrayListArrayList(coordinates);
                return parseThreeDimensionGeometry(type, coordinatesThree);
            } else if (type.equals("MultiPolygon")) {
                List<List<List<List<Double>>>> coordinatesFour =
                        convertToArrayListArrayListArrayListArrayList(coordinates);
                return parseFourDimensionGeometry(type, coordinatesFour);
            } else {
                coordinatesMulti = convertToArrayListArrayList(coordinates);
                return parseTwoDimensionGeometry(type, coordinatesMulti);
            }
        }
    }

    private static ArrayList<Double> convertToArrayList(Object value) {
        assert value instanceof ArrayList
                : "Object passed to Geometry function must be a ArrayList<Double>";
        ArrayList<?> valueList = (ArrayList<?>) value;
        ArrayList<Double> myInput = new ArrayList<>(valueList.size());
        for (Object entry : valueList) {
            myInput.add(((Number) entry).doubleValue());
        }
        return myInput;
    }

    private static List<List<Double>> convertToArrayListArrayList(Object value) {
        assert value instanceof ArrayList
                : "Object passed to Geometry function must be a ArrayList<ArrayList<Double>>";
        ArrayList<?> valueList = (ArrayList<?>) value;
        List<List<Double>> myInput = new ArrayList<>(valueList.size());
        for (Object entry : valueList) {
            myInput.add(convertToArrayList(entry));
        }
        return myInput;
    }

    private static List<List<List<Double>>> convertToArrayListArrayListArrayList(Object value) {
        assert value instanceof ArrayList
                : "Object passed to Geometry function must be a ArrayList<ArrayList<Double>>";
        ArrayList<?> valueList = (ArrayList<?>) value;
        List<List<List<Double>>> myInput = new ArrayList<>(valueList.size());
        for (Object entry : valueList) {
            myInput.add(convertToArrayListArrayList(entry));
        }
        return myInput;
    }

    private static List<List<List<List<Double>>>> convertToArrayListArrayListArrayListArrayList(
            Object value) {
        assert value instanceof ArrayList
                : "Object passed to Geometry function must be a ArrayList<ArrayList<Double>>";
        ArrayList<?> valueList = (ArrayList<?>) value;
        List<List<List<List<Double>>>> myInput = new ArrayList<>(valueList.size());
        for (Object entry : valueList) {
            myInput.add(convertToArrayListArrayListArrayList(entry));
        }
        return myInput;
    }

    private static Geometry parseSingleDimensionGeometry(String type, List<Double> coords)
            throws ParseException {
        Geometry result;
        switch (type) {
            case "Point":
                result = parsePoint(coords);
                break;
            case "Bbox":
                result = parseBbox(coords);
                break;
            default:
                throw new ParseException("Invalid single dimension geometry type: " + type);
        }

        return result;
    }

    private static Geometry parseTwoDimensionGeometry(String type, List<List<Double>> coords)
            throws ParseException {
        Geometry result;
        switch (type) {
            case "MultiPoint":
                result = parseMultiPoint(coords);
                break;
            case "LineString":
                result = parseLineString(coords);
                break;
            default:
                throw new ParseException("Invalid geometry type: " + type);
        }

        return result;
    }

    private static Geometry parseThreeDimensionGeometry(
            String type, List<List<List<Double>>> coords) throws ParseException {
        Geometry result;
        switch (type) {
            case "MultiLineString":
                result = parseMultiLine(coords);
                break;
            case "Polygon":
                result = parsePolygon(coords);
                break;
            default:
                throw new ParseException("Invalid geometry type: " + type);
        }

        return result;
    }

    private static Geometry parseFourDimensionGeometry(
            String type, List<List<List<List<Double>>>> coords) throws ParseException {
        Geometry result;
        switch (type) {
            case "MultiPolygon":
                result = parseMultiPolygon(coords);
                break;
            default:
                throw new ParseException("Invalid geometry type: " + type);
        }

        return result;
    }

    private static Polygon parseBbox(List<Double> coords) {
        double[] doubleArr = new double[coords.size()];
        int index = 0;
        for (Number i : coords) {
            doubleArr[index] = i.doubleValue(); // unboxing is automtically done here
            index++;
        }
        List<List<Double>> polygonArr = new ArrayList<>();
        List<Double> point1 = new ArrayList<>();
        point1.add(doubleArr[0]);
        point1.add(doubleArr[1]);
        polygonArr.add(point1);
        List<Double> point2 = new ArrayList<>();
        point2.add(doubleArr[0]);
        point2.add(doubleArr[3]);
        polygonArr.add(point2);
        List<Double> point3 = new ArrayList<>();
        point3.add(doubleArr[2]);
        point3.add(doubleArr[3]);
        polygonArr.add(point3);
        List<Double> point4 = new ArrayList<>();
        point4.add(doubleArr[2]);
        point4.add(doubleArr[1]);
        polygonArr.add(point4);
        List<Double> point5 = new ArrayList<>();
        point5.add(doubleArr[0]);
        point5.add(doubleArr[1]);
        polygonArr.add(point5);
        LinearRing outer = FACTORY.createLinearRing(toCoordinates(polygonArr));
        return FACTORY.createPolygon(outer);
    }

    private static Point parsePoint(List<Double> coords) {
        assert coords.size() == 2 : "Point array should have two coordinates";
        Coordinate coordinate = new Coordinate(coords.get(0), coords.get(1));
        return FACTORY.createPoint(coordinate);
    }

    private static Coordinate[] toCoordinates(List<List<Double>> coordinates) {

        Coordinate[] result = new Coordinate[coordinates.size()];

        for (int i = 0; i < coordinates.size(); i++) {
            result[i] = new Coordinate(coordinates.get(i).get(0), coordinates.get(i).get(1));
        }

        return result;
    }

    private static MultiPoint parseMultiPoint(List<List<Double>> coords) {
        assert coords.get(0) instanceof List : "Multipoint should be a two dimensional array";
        Point[] pointSet =
                coords.stream()
                        .map(
                                coord -> {
                                    return parsePoint(coord);
                                })
                        .toArray(Point[]::new);
        return FACTORY.createMultiPoint(pointSet);
    }

    private static LineString parseLineString(List<java.util.List<java.lang.Double>> coords) {
        Coordinate[] coordinates = toCoordinates(coords);
        return FACTORY.createLineString(coordinates);
    }

    private static MultiLineString parseMultiLine(List<List<List<Double>>> coords) {
        assert coords.get(0) instanceof List : "MultiLine should be a three dimensional array";

        LineString[] curves =
                coords.stream()
                        .map(
                                p -> {
                                    return parseLineString(p);
                                })
                        .toArray(LineString[]::new);
        return FACTORY.createMultiLineString(curves);
    }

    private static Polygon parsePolygon(List<List<List<Double>>> coords) {
        assert coords.get(0) instanceof List : "Polygon should be a three dimensional array";
        LinearRing outer = FACTORY.createLinearRing(toCoordinates(coords.get(0)));
        LinearRing[] inner = null;
        if (coords.size() > 1) {
            inner = new LinearRing[coords.size() - 1];
            for (int i = 1; i < coords.size(); i++) {
                inner[i - 1] = FACTORY.createLinearRing(toCoordinates(coords.get(i)));
            }
        }
        return FACTORY.createPolygon(outer, inner);
    }

    private static MultiPolygon parseMultiPolygon(List<List<List<List<Double>>>> coords) {
        assert coords.get(0) instanceof List : "Polygon should be a four dimensional array";

        Polygon[] polys =
                coords.stream()
                        .map(
                                p -> {
                                    return parsePolygon(p);
                                })
                        .toArray(Polygon[]::new);
        return FACTORY.createMultiPolygon(polys);
    }
}
