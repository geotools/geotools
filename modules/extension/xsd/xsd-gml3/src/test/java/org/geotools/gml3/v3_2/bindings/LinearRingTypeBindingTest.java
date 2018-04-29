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
package org.geotools.gml3.v3_2.bindings;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.gml3.v3_2.GML;
import org.geotools.gml3.v3_2.GML32TestSupport;
import org.w3c.dom.Document;

/** @source $URL$ */
public class LinearRingTypeBindingTest extends GML32TestSupport {
    //    public void testPos() throws Exception {
    //        document.appendChild(GML3MockData.linearRingWithPos(document, null));
    //
    //        LinearRing line = (LinearRing) parse();
    //        assertNotNull(line);
    //
    //        assertEquals(new Coordinate(1d, 2d), line.getPointN(0).getCoordinate());
    //        assertEquals(new Coordinate(3d, 4d), line.getPointN(1).getCoordinate());
    //        assertEquals(new Coordinate(5d, 6d), line.getPointN(2).getCoordinate());
    //        assertEquals(new Coordinate(1d, 2d), line.getPointN(3).getCoordinate());
    //    }
    //
    //    public void testPosList() throws Exception {
    //        document.appendChild(GML3MockData.linearRingWithPosList(document, null));
    //
    //        LinearRing line = (LinearRing) parse();
    //        assertNotNull(line);
    //
    //        assertEquals(new Coordinate(1d, 2d), line.getPointN(0).getCoordinate());
    //        assertEquals(new Coordinate(3d, 4d), line.getPointN(1).getCoordinate());
    //        assertEquals(new Coordinate(1d, 2d), line.getPointN(0).getCoordinate());
    //        assertEquals(new Coordinate(3d, 4d), line.getPointN(1).getCoordinate());
    //        assertEquals(new Coordinate(5d, 6d), line.getPointN(2).getCoordinate());
    //        assertEquals(new Coordinate(1d, 2d), line.getPointN(3).getCoordinate());
    //    }

    public void testEncode() throws Exception {
        Document d = encode(GML3MockData.linearRing(), GML.LinearRing);

        assertEquals("gml:LinearRing", d.getDocumentElement().getNodeName());
        assertXpathExists("/gml:LinearRing/gml:posList", d);

        // print(d);
    }
}
