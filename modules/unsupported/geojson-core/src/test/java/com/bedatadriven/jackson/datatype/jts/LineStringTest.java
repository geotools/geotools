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
import java.util.List;
import java.util.Locale;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

/** Created by mihaildoronin on 11/11/15. */
@RunWith(Parameterized.class)
public class LineStringTest extends BaseJtsModuleTest<LineString> {

    private final Locale previousLocale;

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> locales() {
        return Arrays.asList(new Object[][] {{Locale.ENGLISH}, {Locale.ITALIAN}});
    }

    public LineStringTest(Locale locale) {
        this.previousLocale = Locale.getDefault();
        Locale.setDefault(locale);
    }

    @After
    public void resetLocale() {
        Locale.setDefault(previousLocale);
    }

    @Override
    protected Class<LineString> getType() {
        return LineString.class;
    }

    @Override
    protected String createGeometryAsGeoJson() {
        return "{\"type\":\"LineString\",\"coordinates\":[[100.0,0.0],[101.0,1.0]]}";
    }

    @Override
    protected LineString createGeometry() {
        return gf.createLineString(
                new Coordinate[] {new Coordinate(100.0, 0.0), new Coordinate(101.0, 1.0)});
    }
}
