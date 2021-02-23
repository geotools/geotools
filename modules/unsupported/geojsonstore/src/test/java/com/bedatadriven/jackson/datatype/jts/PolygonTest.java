package com.bedatadriven.jackson.datatype.jts;
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

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

/** Created by mihaildoronin on 11/11/15. */
public class PolygonTest extends BaseJtsModuleTest<Polygon> {
    @Override
    protected Class<Polygon> getType() {
        return Polygon.class;
    }

    @Override
    protected String createGeometryAsGeoJson() {
        return "{\"type\":\"Polygon\",\"coordinates\":[[[102.0,2.0],[103.0,2.0],[103.0,3.0],[102.0,3.0],[102.0,2.0]]]}";
    }

    @Override
    protected Polygon createGeometry() {
        LinearRing shell =
                gf.createLinearRing(
                        new Coordinate[] {
                            new Coordinate(102.0, 2.0), new Coordinate(103.0, 2.0),
                            new Coordinate(103.0, 3.0), new Coordinate(102.0, 3.0),
                            new Coordinate(102.0, 2.0)
                        });
        LinearRing[] holes = new LinearRing[0];
        return gf.createPolygon(shell, holes);
    }
}
