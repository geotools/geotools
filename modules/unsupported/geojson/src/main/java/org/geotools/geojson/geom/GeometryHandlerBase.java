/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson.geom;

import static org.geotools.geojson.GeoJSONUtil.addOrdinate;
import static org.geotools.geojson.GeoJSONUtil.createCoordinate;
import static org.geotools.geojson.GeoJSONUtil.createCoordinates;

import java.io.IOException;
import java.util.List;
import org.geotools.geojson.HandlerBase;
import org.geotools.geojson.IContentHandler;
import org.json.simple.parser.ParseException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class GeometryHandlerBase<G extends Geometry> extends HandlerBase
        implements IContentHandler<G> {

    protected GeometryFactory factory;
    protected List<Object> ordinates;
    protected G value;

    public GeometryHandlerBase(GeometryFactory factory) {
        this.factory = factory;
    }

    public G getValue() {
        return value;
    }

    protected Coordinate coordinate(List ordinates) throws ParseException {
        return createCoordinate(ordinates);
    }

    protected Coordinate[] coordinates(List coordinates) {
        return createCoordinates(coordinates);
    }

    public boolean primitive(Object value) throws ParseException, IOException {
        // we could be receiving the "type" attribute value
        if (value instanceof Number) {
            return addOrdinate(ordinates, value);
        } else {
            return true;
        }
    }
}
