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
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MultiPointTypeBindingTest extends GML3TestSupport {

    public void test() throws Exception {
        GML3MockData.multiPoint(document, document);

        MultiPoint multiPoint = (MultiPoint) parse();
        assertNotNull(multiPoint);

        assertEquals(4, multiPoint.getNumPoints());
    }

    public void test3D() throws Exception {
        GML3MockData.multiPoint3D(document, document);

        MultiPoint multiPoint = (MultiPoint) parse();
        assertNotNull(multiPoint);

        assertEquals(4, multiPoint.getNumPoints());
        Point p = (Point) multiPoint.getGeometryN(0);
        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(p.getCoordinate()));
    }

    public void testEncode() throws Exception {
        Geometry geometry = GML3MockData.multiPoint();
        GML3EncodingUtils.setID(geometry, "geometry");
        Document dom = encode(geometry, GML.MultiPoint);
        // print(dom);
        assertEquals("geometry", getID(dom.getDocumentElement()));
        assertEquals(2, dom.getElementsByTagNameNS(GML.NAMESPACE, "pointMember").getLength());
        NodeList children = dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Point.getLocalPart());
        assertEquals(2, children.getLength());
        assertEquals("geometry.1", getID(children.item(0)));
        assertEquals("geometry.2", getID(children.item(1)));
    }
}
