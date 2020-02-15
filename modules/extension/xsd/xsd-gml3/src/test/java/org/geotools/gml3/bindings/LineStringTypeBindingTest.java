/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.bindings;

import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.w3c.dom.Document;

public class LineStringTypeBindingTest extends GML3TestSupport {

    public void testPos() throws Exception {
        document.appendChild(GML3MockData.lineStringWithPos(document, null));

        LineString line = (LineString) parse();
        assertNotNull(line);

        assertEquals(new Coordinate(1d, 2d), line.getPointN(0).getCoordinate());
        assertEquals(new Coordinate(3d, 4d), line.getPointN(1).getCoordinate());
    }

    public void testPos3D() throws Exception {
        document.appendChild(GML3MockData.lineStringWithPos3D(document, null));

        LineString line = (LineString) parse();
        assertNotNull(line);

        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(line.getPointN(0).getCoordinate()));
        assertTrue(new Coordinate(3d, 4d, 20d).equals3D(line.getPointN(1).getCoordinate()));
    }

    public void testPosList() throws Exception {
        document.appendChild(GML3MockData.lineStringWithPosList(document, null));

        LineString line = (LineString) parse();
        assertNotNull(line);

        assertEquals(new Coordinate(1d, 2d), line.getPointN(0).getCoordinate());
        assertEquals(new Coordinate(3d, 4d), line.getPointN(1).getCoordinate());
    }

    public void testPosList3D() throws Exception {
        document.appendChild(GML3MockData.lineStringWithPosList3D(document, null));

        LineString line = (LineString) parse();
        assertNotNull(line);

        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(line.getPointN(0).getCoordinate()));
        assertTrue(new Coordinate(3d, 4d, 20d).equals3D(line.getPointN(1).getCoordinate()));
    }

    /**
     * Tests encoding using a CoordinateArraySequence (which requires special logic to get the
     * dimension correct)
     */
    public void testEncodeLineString() throws Exception {
        LineString line = GML3MockData.lineString();
        Document doc = encode(line, GML.LineString);

        checkDimension(doc, GML.LineString.getLocalPart(), 2);
        checkPosListOrdinates(doc, 2 * line.getNumPoints());
    }

    public void testEncodeLite2D() throws Exception {
        LineString line = GML3MockData.lineStringLite2D();
        Document doc = encode(line, GML.LineString);

        checkDimension(doc, GML.LineString.getLocalPart(), 2);
        checkPosListOrdinates(doc, 2 * line.getNumPoints());
    }

    public void testEncodeLite3D() throws Exception {
        LineString line = GML3MockData.lineStringLite3D();
        Document doc = encode(line, GML.LineString);

        checkDimension(doc, GML.LineString.getLocalPart(), 3);
        checkPosListOrdinates(doc, 3 * line.getNumPoints());
    }

    /**
     * Test a long LineString to catch problems that only show up with large numbers of ordinates
     */
    public void testEncode2DLong() throws Exception {
        LineString line = GML3MockData.lineStringLite2D(10);
        Document doc = encode(line, GML.LineString);

        checkDimension(doc, GML.LineString.getLocalPart(), 2);
        checkPosListOrdinates(doc, 2 * line.getNumPoints());
    }
}
