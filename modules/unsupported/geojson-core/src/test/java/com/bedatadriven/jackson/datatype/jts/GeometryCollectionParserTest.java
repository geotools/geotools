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
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;

/** Created by mihaildoronin on 11/11/15. */
public class GeometryCollectionParserTest extends BaseJtsModuleTest<GeometryCollection> {
    @Override
    protected Class<GeometryCollection> getType() {
        return GeometryCollection.class;
    }

    @Override
    protected String createGeometryAsGeoJson() {
        return "{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[1.2346,2.3457]}]}";
    }

    @Override
    protected GeometryCollection createGeometry() {
        return gf.createGeometryCollection(
                new Geometry[] {gf.createPoint(new Coordinate(1.2345678, 2.3456789))});
    }
}
