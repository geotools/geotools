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

import java.util.Arrays;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

/** Created by mihaildoronin on 11/11/15. */
@RunWith(Parameterized.class)
public class PointTest extends BaseJtsModuleTest<Point> {

    int maxDecimals;

    public PointTest(int maxDecimals) {
        this.maxDecimals = maxDecimals;
    }

    @Override
    public int getMaxDecimals() {
        return maxDecimals;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> precisions() {
        return Arrays.asList(new Object[][] {{1}, {2}, {3}, {4}, {5}, {6}, {7}});
    }

    @Override
    protected Class<Point> getType() {
        return Point.class;
    }

    @Override
    protected String createGeometryAsGeoJson() {
        switch (maxDecimals) {
            case 0:
                return "{\"type\":\"Point\",\"coordinates\":[1,2]}";
            case 1:
                return "{\"type\":\"Point\",\"coordinates\":[1.2,2.3]}";
            case 2:
                return "{\"type\":\"Point\",\"coordinates\":[1.23,2.35]}";
            case 3:
                return "{\"type\":\"Point\",\"coordinates\":[1.235,2.346]}";
            case 4:
                return "{\"type\":\"Point\",\"coordinates\":[1.2346,2.3457]}";
            case 5:
                return "{\"type\":\"Point\",\"coordinates\":[1.23457,2.34568]}";
            case 6:
                return "{\"type\":\"Point\",\"coordinates\":[1.234568,2.345679]}";
            case 7:
                return "{\"type\":\"Point\",\"coordinates\":[1.2345678,2.3456789]}";
            default:
                throw new RuntimeException("Unexpected precision");
        }
    }

    @Override
    protected Point createGeometry() {
        return gf.createPoint(new Coordinate(1.2345678, 2.3456789));
    }
}
