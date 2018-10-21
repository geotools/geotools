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
import org.locationtech.jts.geom.Polygon;
import org.w3c.dom.Document;

public class PolygonTypeBindingTest extends GML3TestSupport {

    @Override
    protected boolean enableExtendedArcSurfaceSupport() {
        return true;
    }

    public void testNoInterior() throws Exception {
        GML3MockData.polygon(document, document);

        Polygon polygon = (Polygon) parse();
        assertNotNull(polygon);
    }

    public void testPolygon3D() throws Exception {
        GML3MockData.polygon3D(document, document, true);

        Polygon polygon = (Polygon) parse();
        assertNotNull(polygon);

        LineString exterior = polygon.getExteriorRing();
        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(exterior.getCoordinateN(0)));
        LineString interior = polygon.getInteriorRingN(0);
        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(interior.getCoordinateN(0)));
    }

    public void testPolygonPosList3D() throws Exception {
        GML3MockData.polygonWithPosList3D(document, document, true);

        Polygon polygon = (Polygon) parse();
        assertNotNull(polygon);

        LineString exterior = polygon.getExteriorRing();
        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(exterior.getCoordinateN(0)));
        LineString interior = polygon.getInteriorRingN(0);
        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(interior.getCoordinateN(0)));
    }

    public void testEncode3D() throws Exception {
        Polygon poly = GML3MockData.polygonLite3D();
        Document doc = encode(poly, GML.Polygon);

        checkDimension(doc, GML.Polygon.getLocalPart(), 3);
        checkPosListOrdinates(doc, 3 * poly.getNumPoints());
    }

    public void testEncode2D() throws Exception {
        Polygon poly = GML3MockData.polygonLite2D();
        Document doc = encode(poly, GML.Polygon);

        checkDimension(doc, GML.Polygon.getLocalPart(), 2);
        checkPosListOrdinates(doc, 2 * poly.getNumPoints());
    }

    public void testEncodeCurved() throws Exception {
        Polygon poly = GML3MockData.curvePolygon();
        Document doc = encode(poly, GML.Polygon);
        // print(doc);

    }
}
