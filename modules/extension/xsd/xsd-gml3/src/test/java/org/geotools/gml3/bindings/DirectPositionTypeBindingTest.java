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

import org.geotools.geometry.DirectPosition1D;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateXYM;
import org.locationtech.jts.geom.CoordinateXYZM;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.geometry.DirectPosition;
import org.w3c.dom.Document;

public class DirectPositionTypeBindingTest extends GML3TestSupport {
    public void test1D() throws Exception {
        GML3MockData.element(GML.pos, document, document);
        document.getDocumentElement().appendChild(document.createTextNode("1.0"));

        DirectPosition pos = (DirectPosition) parse();

        assertNotNull(pos);
        assertTrue(pos instanceof DirectPosition1D);

        assertEquals(pos.getOrdinate(0), 1.0, 0);
    }

    public void test2D() throws Exception {
        GML3MockData.element(GML.pos, document, document);
        document.getDocumentElement().appendChild(document.createTextNode("1.0 2.0"));

        DirectPosition pos = (DirectPosition) parse();

        assertNotNull(pos);
        assertTrue(pos instanceof DirectPosition2D);

        assertEquals(pos.getOrdinate(0), 1.0, 0);
        assertEquals(pos.getOrdinate(1), 2.0, 0);
    }

    public void testEncode2D() throws Exception {
        Point point = GML3MockData.pointLite2D();
        CoordinateSequence seq = point.getCoordinateSequence();
        Document doc = encode(seq, GML.pos);
        checkPosOrdinates(doc, 2);
    }

    public void testEncode3D() throws Exception {
        Point point = GML3MockData.pointLite3D();
        CoordinateSequence seq = point.getCoordinateSequence();
        Document doc = encode(seq, GML.pos);
        checkPosOrdinates(doc, 3);
    }

    /** Tests encoding for X, Y, M ordinates number */
    @Test
    public void testEncodeXYM() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Point pointM = gf.createPoint(new CoordinateXYM(1, 1, 4));
        CoordinateSequence seq = pointM.getCoordinateSequence();
        Document doc = encode(seq, GML.pos);
        checkPosOrdinates(doc, 4);
    }

    /** Tests encoding for X, Y, Z, M ordinates number */
    @Test
    public void testEncodeZM() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Point pointM = gf.createPoint(new CoordinateXYZM(1, 1, 2, 4));
        CoordinateSequence seq = pointM.getCoordinateSequence();
        Document doc = encode(seq, GML.pos);
        checkPosOrdinates(doc, 4);
    }
}
