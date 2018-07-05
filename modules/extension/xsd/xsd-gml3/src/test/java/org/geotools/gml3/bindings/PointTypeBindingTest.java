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
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;

/** @source $URL$ */
public class PointTypeBindingTest extends GML3TestSupport {

    public void testPos() throws Exception {
        GML3MockData.point(document, document);

        Point p = (Point) parse();
        assertNotNull(p);
        assertEquals(new Coordinate(1d, 2d), p.getCoordinate());

        assertTrue(p.getUserData() instanceof CoordinateReferenceSystem);
    }

    public void testPos3D() throws Exception {
        GML3MockData.point3D(document, document);

        Point p = (Point) parse();
        assertNotNull(p);
        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(p.getCoordinate()));

        assertTrue(p.getUserData() instanceof CoordinateReferenceSystem);
    }

    public void testEncode() throws Exception {
        Point p = GML3MockData.point();
        Document dom = encode(p, GML.Point);

        assertEquals(
                1, dom.getElementsByTagNameNS(GML.NAMESPACE, GML.pos.getLocalPart()).getLength());
        // assertEquals("urn:x-ogc:def:crs:EPSG:6.11.2:4326",
        assertEquals(
                "urn:x-ogc:def:crs:EPSG:4326", dom.getDocumentElement().getAttribute("srsName"));
    }

    public void testEncode2D() throws Exception {
        Point point = GML3MockData.pointLite2D();
        Document doc = encode(point, GML.Point);

        checkDimension(doc, GML.Point.getLocalPart(), 2);
        checkPosOrdinates(doc, 2);
    }

    public void testEncode3D() throws Exception {
        Point point = GML3MockData.pointLite3D();
        Document doc = encode(point, GML.Point);

        checkDimension(doc, GML.Point.getLocalPart(), 3);
        checkPosOrdinates(doc, 3);
    }
}
