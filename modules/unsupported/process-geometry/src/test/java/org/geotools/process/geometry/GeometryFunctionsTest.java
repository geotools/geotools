/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.geometry;

import static org.junit.Assert.*;

import org.geotools.process.geometry.GeometryFunctions;
import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author mdavis
 *
 */
public class GeometryFunctionsTest {

    static WKTReader wktRdr = new WKTReader();
    
    @Test
    public void testPolygonize() throws Exception {
        Geometry lines = wktRdr.read("MULTILINESTRING ((100 100, 200 100), (200 100, 300 100), (100 100, 100 200, 200 200), (200 100, 200 200), (200 200, 300 200, 300 100))");
        Geometry output = GeometryFunctions.polygonize(lines);
        Geometry expectedOutput = wktRdr.read("GEOMETRYCOLLECTION (POLYGON ((200 100, 100 100, 100 200, 200 200, 200 100)), POLYGON ((200 100, 200 200, 300 200, 300 100, 200 100)))");
        assertTrue(output.norm().equalsExact(expectedOutput.norm()));
    }
    
    @Test
    public void testSplitPolygon() throws Exception {
        Geometry poly = wktRdr.read("POLYGON ((100 100, 200 200, 200 100, 100 100))");
        LineString line = (LineString) wktRdr.read("LINESTRING (150 200, 150 50)");
        Geometry output = GeometryFunctions.splitPolygon(poly, line);
        Geometry expectedOutput = wktRdr.read("GEOMETRYCOLLECTION (POLYGON ((150 100, 100 100, 150 150, 150 100)), POLYGON ((150 150, 200 200, 200 100, 150 100, 150 150)))");
        assertTrue(output.norm().equalsExact(expectedOutput.norm()));
    }
}
