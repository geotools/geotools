package com.bedatadriven.jackson.datatype.jts;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

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
/** Created by mihaildoronin on 11/11/15. */
public class MultiLineStringTest extends BaseJtsModuleTest<MultiLineString> {
    @Override
    protected Class<MultiLineString> getType() {
        return MultiLineString.class;
    }

    @Override
    protected String createGeometryAsGeoJson() {
        return "{\"type\":\"MultiLineString\",\"coordinates\":[[[100.0,0.0],[101.0,1.0]],[[102.0,2.0],[103.0,3.0]]]}";
    }

    @Override
    protected MultiLineString createGeometry() {
        return gf.createMultiLineString(
                new LineString[] {
                    gf.createLineString(
                            new Coordinate[] {
                                new Coordinate(100.0, 0.0), new Coordinate(101.0, 1.0)
                            }),
                    gf.createLineString(
                            new Coordinate[] {
                                new Coordinate(102.0, 2.0), new Coordinate(103.0, 3.0)
                            })
                });
    }
}
