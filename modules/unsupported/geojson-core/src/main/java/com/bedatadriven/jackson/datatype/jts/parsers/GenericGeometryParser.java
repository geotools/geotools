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

import static com.bedatadriven.jackson.datatype.jts.GeoJson.GEOMETRY_COLLECTION;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.LINE_STRING;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.MULTI_LINE_STRING;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.MULTI_POINT;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.MULTI_POLYGON;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.POINT;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.POLYGON;
import static com.bedatadriven.jackson.datatype.jts.GeoJson.TYPE;

import java.util.HashMap;
import java.util.Map;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.exc.JsonNodeException;

/** Created by mihaildoronin on 11/11/15. */
public class GenericGeometryParser extends BaseParser implements GeometryParser<Geometry> {

    private Map<String, GeometryParser> parsers;

    public GenericGeometryParser(GeometryFactory geometryFactory) {
        super(geometryFactory);
        parsers = new HashMap<>();
        parsers.put(POINT, new PointParser(geometryFactory));
        parsers.put(MULTI_POINT, new MultiPointParser(geometryFactory));
        parsers.put(LINE_STRING, new LineStringParser(geometryFactory));
        parsers.put(MULTI_LINE_STRING, new MultiLineStringParser(geometryFactory));
        parsers.put(POLYGON, new PolygonParser(geometryFactory));
        parsers.put(MULTI_POLYGON, new MultiPolygonParser(geometryFactory));
        parsers.put(GEOMETRY_COLLECTION, new GeometryCollectionParser(geometryFactory, this));
    }

    @Override
    public Geometry geometryFromJson(JsonNode node) throws DatabindException {
        String typeName = node.get(TYPE).asString();
        GeometryParser parser = parsers.get(typeName);
        if (parser != null) {
            return parser.geometryFromJson(node);
        } else {
            // TODO find a better way of doing this!
            throw JsonNodeException.from(node, "Invalid geometry type: " + typeName);
        }
    }
}
