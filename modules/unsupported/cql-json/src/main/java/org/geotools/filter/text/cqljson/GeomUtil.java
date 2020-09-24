/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Set;
import java.util.stream.Collectors;
import org.geotools.filter.text.generated.parsers.ParseException;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.aggregate.MultiCurve;
import org.opengis.geometry.aggregate.MultiPoint;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;

public class GeomUtil {
    public static GeometryBuilder FACTORY = null;

    static {
        FACTORY = new GeometryBuilder(DefaultGeographicCRS.WGS84);
    }

    /**
     * Jackson returns embedded geojson as a map so we use this method to create opengis.Geometry
     * for filter creation
     *
     * @param geoJsonAsMap GeoJSON Map
     * @return Geometry for filter creation
     * @throws ParseException
     */
    public static org.opengis.geometry.Geometry parseMapToGeometry(Map<String, Object> geoJsonAsMap)
            throws ParseException {
        String type = "Bbox";
        ArrayList coordinates = null;
        if (geoJsonAsMap.get("bbox") != null) {
            type = "Bbox";
            coordinates = (ArrayList<Double>) geoJsonAsMap.get("bbox");
        } else {
            type = (String) geoJsonAsMap.get("type");
            coordinates = (ArrayList<Double>) geoJsonAsMap.get("coordinates");
        }
        return parseGeometry(type, coordinates);
    }

    private static org.opengis.geometry.Geometry parseGeometry(String type, ArrayList coords)
            throws ParseException {
        org.opengis.geometry.Geometry result;
        switch (type) {
            case "Point":
                result = parsePoint(coords);
                break;
            case "MultiPoint":
                result = parseMultiPoint(coords);
                break;
            case "LineString":
                result = parseLineString(coords);
                break;
            case "Polygon":
                result = parsePolygon(coords);
                break;
            case "MultiLineString":
                result = parseMultiLine(coords);
                break;
            case "MultiPolygon":
                result = parseMultiPolygon(coords);
                break;
            case "Bbox":
                result = parseBbox(coords);
                break;
            default:
                throw new ParseException("Invalid geometry type: " + type);
        }

        return result;
    }

    private static Surface parseBbox(ArrayList<Double> coords) {
        double[] doubleArr = new double[coords.size()];
        int index = 0;
        for (Number i : coords) {
            doubleArr[index] = i.doubleValue(); // unboxing is automtically done here
            index++;
        }
        double[] polygonArr = new double[10];
        polygonArr[0] = doubleArr[0];
        polygonArr[1] = doubleArr[1];
        polygonArr[2] = doubleArr[0];
        polygonArr[3] = doubleArr[3];
        polygonArr[4] = doubleArr[2];
        polygonArr[5] = doubleArr[3];
        polygonArr[6] = doubleArr[2];
        polygonArr[7] = doubleArr[1];
        polygonArr[8] = doubleArr[0];
        polygonArr[9] = doubleArr[1];
        PointArray pointArray = FACTORY.createPointArray(polygonArr);
        SurfaceBoundary ring = FACTORY.createSurfaceBoundary(pointArray);

        return FACTORY.createSurface(ring);
    }

    private static Point parsePoint(ArrayList<Double> coords) {
        assert coords.size() == 2 : "Point array should have two coordinates";
        double[] arr = coords.stream().mapToDouble(i -> i).toArray();
        GeneralDirectPosition generalDirectPosition = new GeneralDirectPosition(arr[0], arr[1]);
        generalDirectPosition.setCoordinateReferenceSystem(FACTORY.getCoordinateReferenceSystem());
        return FACTORY.createPoint(generalDirectPosition);
    }

    private static PointArray parsePointArray(List<List<Double>> coords) {
        assert coords.get(0) instanceof List : "Expected a two dimensional array";
        List<Double> collect =
                coords.stream()
                        .collect(
                                ArrayList::new,
                                List::addAll,
                                List::addAll); // multi point is a two dimensional list
        double[] arr = collect.stream().mapToDouble(i -> i).toArray();
        return FACTORY.createPointArray(arr);
    }

    private static MultiPoint parseMultiPoint(ArrayList<ArrayList<Double>> coords) {
        assert coords.get(0) instanceof List : "Multipoint should be a two dimensional array";
        Set<Point> pointSet =
                coords.stream()
                        .map(
                                coord -> {
                                    return parsePoint(coord);
                                })
                        .collect(Collectors.toSet());
        return FACTORY.createMultiPoint(pointSet);
    }

    private static Curve parseLineString(ArrayList coords) {
        PointArray pointArray = parsePointArray(coords);
        return FACTORY.createCurve(pointArray, false);
    }

    private static MultiCurve parseMultiLine(ArrayList<ArrayList<Double>> coords) {
        assert coords.get(0) instanceof List : "MultiLine should be a two dimensional array";

        List collect = coords.stream().collect(ArrayList::new, List::addAll, List::addAll);
        Set<Curve> curves =
                (Set<Curve>)
                        coords.stream()
                                .map(
                                        p -> {
                                            PointArray pointArray = parsePointArray(collect);
                                            return FACTORY.createCurve(pointArray, false);
                                        })
                                .collect(Collectors.toSet());
        return FACTORY.createMultiCurve(curves);
    }

    private static Surface parsePolygon(ArrayList<ArrayList<Double>> coords) throws ParseException {
        assert coords.get(0) instanceof List : "Polygon should be a three dimensional array";

        List collect = coords.stream().collect(ArrayList::new, List::addAll, List::addAll);
        PointArray pointArray = parsePointArray(collect);
        SurfaceBoundary ring = FACTORY.createSurfaceBoundary(pointArray);

        return FACTORY.createSurface(ring);
    }

    private static MultiSurface parseMultiPolygon(ArrayList<ArrayList<Double>> coords) {
        assert coords.get(0) instanceof List : "Polygon should be a four dimensional array";

        List collect = coords.stream().collect(ArrayList::new, List::addAll, List::addAll);
        List collect2 =
                ((List<List>) collect).stream().collect(ArrayList::new, List::addAll, List::addAll);
        Set<Surface> surfaces =
                (Set<Surface>)
                        coords.stream()
                                .map(
                                        p -> {
                                            PointArray pointArray = parsePointArray(collect2);
                                            SurfaceBoundary ring =
                                                    FACTORY.createSurfaceBoundary(pointArray);
                                            return FACTORY.createSurface(ring);
                                        })
                                .collect(Collectors.toSet());

        return FACTORY.createMultiSurface(surfaces);
    }
}
