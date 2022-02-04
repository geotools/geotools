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
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

/** Created by mihaildoronin on 11/11/15. */
public class MultiPointParser extends BaseParser implements GeometryParser<MultiPoint> {

    public MultiPointParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
    }

    public MultiPoint multiPointFromJson(JsonNode root) {
        Coordinate[] coords = PointParser.coordinatesFromJson(root.get(COORDINATES));
        List<Point> points = new ArrayList<>();
        for (Coordinate coord : coords) {
            if (coord != null) {
                points.add(geometryFactory.createPoint(coord));
            } else {
                points.add((Point) geometryFactory.createEmpty(0));
            }
        }
        return geometryFactory.createMultiPoint(points.toArray(new Point[] {}));
    }

    @Override
    public MultiPoint geometryFromJson(JsonNode node) throws JsonMappingException {
        return multiPointFromJson(node);
    }
}
