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
package org.geotools.data.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.geotools.data.geojson.GeoJSONWriter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTWriter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

class RandomGeometryBuilder {

    private final GeometryFactory geometryFactory;

    private final Random random;

    // private final GeometryJSON geometryJson;

    private final int decimals;

    private final int numPoints;

    private int numGeometries;

    private final WKTWriter wktWriter;

    public RandomGeometryBuilder() {
        geometryFactory = new GeometryFactory();
        random = new Random(123456789L);
        decimals = 4;
        numPoints = 10;
        numGeometries = 2;
        // geometryJson = new GeometryJSON(decimals);
        wktWriter = new WKTWriter();
    }

    public Point createRandomPoint() {
        return geometryFactory.createPoint(createRandomCoord());
    }

    public LineString createRandomLineString() {
        Coordinate[] coords = new Coordinate[numPoints];
        for (int i = 0; i < numPoints; i++) {
            coords[i] = createRandomCoord();
        }
        return geometryFactory.createLineString(coords);
    }

    public Polygon createRandomPolygon() {
        Coordinate[] coords = new Coordinate[numPoints + 1];
        for (int i = 0; i < numPoints; i++) {
            coords[i] = createRandomCoord();
        }
        coords[numPoints] = coords[0];
        return geometryFactory.createPolygon(coords);
    }

    public MultiPoint createRandomMultiPoint() {
        Point[] points = new Point[numGeometries];
        for (int i = 0; i < numGeometries; i++) {
            points[i] = createRandomPoint();
        }
        return geometryFactory.createMultiPoint(points);
    }

    public MultiLineString createRandomMultiLineString() {
        LineString[] lineStrings = new LineString[numGeometries];
        for (int i = 0; i < numGeometries; i++) {
            lineStrings[i] = createRandomLineString();
        }
        return geometryFactory.createMultiLineString(lineStrings);
    }

    public MultiPolygon createRandomMultiPolygon() {
        Polygon[] polygons = new Polygon[numGeometries];
        for (int i = 0; i < numGeometries; i++) {
            polygons[i] = createRandomPolygon();
        }
        return geometryFactory.createMultiPolygon(polygons);
    }

    public GeometryCollection createRandomGeometryCollection() {
        Geometry[] geometries = new Geometry[numGeometries];
        for (int i = 0; i < numGeometries; i++) {
            switch (random.nextInt(3)) {
                case 0:
                    geometries[i] = createRandomPoint();
                    break;
                case 1:
                    geometries[i] = createRandomLineString();
                    break;
                default:
                    geometries[i] = createRandomPolygon();
            }
        }
        return geometryFactory.createGeometryCollection(geometries);
    }

    public Envelope createRandomEnvelope() {
        Coordinate coord1 = createRandomCoord();
        while (coord1.x > 179 || coord1.y > 89) {
            coord1 = createRandomCoord();
        }
        final Coordinate coord2 = createRandomCoord((int) (coord1.x + 0.5), (int) (coord1.y + 0.5));
        return new Envelope(coord1, coord2);
    }

    private Coordinate createRandomCoord() {
        return createRandomCoord(-180, -90);
    }

    private Coordinate createRandomCoord(int minx, int miny) {
        int dx = 180 - minx;
        int dy = 90 - miny;
        final int factor = (int) Math.pow(10, decimals);
        final double lon = (random.nextInt(dx * factor) + minx * factor) / ((double) factor);
        final double lat = (random.nextInt(dy * factor) + miny * factor) / ((double) factor);
        return new Coordinate(lon, lat);
    }

    public Map<String, Object> toMap(Geometry geometry) throws IOException {
        final String json = GeoJSONWriter.toGeoJSON(geometry);
        return new ObjectMapper().readValue(json, new TypeReference<>() {});
    }

    public String toWkt(Geometry geometry) {
        return wktWriter.write(geometry);
    }

    public Map<String, Object> toMap(Envelope envelope) {
        final Map<String, Object> properties = new HashMap<>();
        final List<List<Double>> coordinates = new ArrayList<>();
        coordinates.add(Arrays.asList(envelope.getMinX(), envelope.getMaxY()));
        coordinates.add(Arrays.asList(envelope.getMaxX(), envelope.getMinY()));
        properties.put("type", "envelope");
        properties.put("coordinates", coordinates);
        return properties;
    }

    public void setNumGeometries(int numGeometries) {
        this.numGeometries = numGeometries;
    }
}
