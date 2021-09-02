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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MultiPolygonTypeBindingTest extends GML3TestSupport {
    @Test
    public void test() throws Exception {
        GML3MockData.multiPolygon(document, document);

        MultiPolygon multiPolygon = (MultiPolygon) parse();
        assertNotNull(multiPolygon);
        assertEquals(2, multiPolygon.getNumGeometries());
    }

    @Test
    public void test3D() throws Exception {
        GML3MockData.multiPolygon3D(document, document);

        MultiPolygon multiPolygon = (MultiPolygon) parse();
        assertNotNull(multiPolygon);
        assertEquals(2, multiPolygon.getNumGeometries());

        Polygon polygon = (Polygon) multiPolygon.getGeometryN(0);
        LineString exterior = polygon.getExteriorRing();
        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(exterior.getCoordinateN(0)));
        LineString interior = polygon.getInteriorRingN(0);
        assertTrue(new Coordinate(1d, 2d, 10d).equals3D(interior.getCoordinateN(0)));
    }

    @Test
    public void testEncode() throws Exception {
        Geometry geometry = GML3MockData.multiPolygon();
        GML3EncodingUtils.setID(geometry, "geometry");
        Document dom = encode(geometry, GML.MultiPolygon);
        // print(dom);
        assertEquals("geometry", getID(dom.getDocumentElement()));
        assertEquals(2, dom.getElementsByTagNameNS(GML.NAMESPACE, "polygonMember").getLength());
        NodeList children = dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Polygon.getLocalPart());
        assertEquals(2, children.getLength());
        assertEquals("geometry.1", getID(children.item(0)));
        assertEquals("geometry.2", getID(children.item(1)));
    }

    @Test
    public void testEncodePolygon() throws Exception {
        Geometry geometry = GML3MockData.polygon();
        geometry.setUserData(CRS.decode("EPSG:4326"));
        GML3EncodingUtils.setID(geometry, "geometry");
        Document dom = encode(geometry, GML.MultiPolygon);
        // print(dom);
        assertEquals(
                "urn:x-ogc:def:crs:EPSG:4326",
                dom.getElementsByTagNameNS(GML.NAMESPACE, "MultiPolygon")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("srsName")
                        .getTextContent());
        assertNull(
                dom.getElementsByTagNameNS(GML.NAMESPACE, "Polygon")
                        .item(0)
                        .getAttributes()
                        .getNamedItem("srsName"));
    }
}
