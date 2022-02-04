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
/*
 * Original code at https://github.com/bedatadriven/jackson-datatype-jts Apache2 license
 *
 */
package com.bedatadriven.jackson.datatype.jts.parsers;

import static com.bedatadriven.jackson.datatype.jts.GeoJson.COORDINATES;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/** Created by mihaildoronin on 11/11/15. */
public class PointParser extends BaseParser implements GeometryParser<Point> {

    public PointParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    public static Coordinate coordinateFromJson(JsonNode array) {
        assert array.isArray() && (array.size() == 0 || array.size() == 2 || array.size() == 3)
                : "expecting coordinate array with single point [ x, y, |z| ]";
        if (array.size() == 0) {
            return null;
        }
        if (array.size() == 2) {
            return new Coordinate(array.get(0).asDouble(), array.get(1).asDouble());
        }

        return new Coordinate(
                array.get(0).asDouble(), array.get(1).asDouble(), array.get(2).asDouble());
    }

    public static Coordinate[] coordinatesFromJson(JsonNode array) {
        Coordinate[] points = new Coordinate[array.size()];
        for (int i = 0; i != array.size(); ++i) {
            points[i] = PointParser.coordinateFromJson(array.get(i));
        }
        return points;
    }

    public Point pointFromJson(JsonNode node) {
        return geometryFactory.createPoint(coordinateFromJson(node.get(COORDINATES)));
    }

    @Override
    public Point geometryFromJson(JsonNode node) throws JsonMappingException {
        return pointFromJson(node);
    }
}
