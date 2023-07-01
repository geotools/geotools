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
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.DirectPosition1D;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.DirectPosition3D;
import org.geotools.geometry.jts.coordinatesequence.CoordinateSequences;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.LineString;
import org.geotools.api.geometry.DirectPosition;
import org.w3c.dom.Document;

public class DirectPositionListTypeBindingTest extends GML3TestSupport {
    @Test
    public void test1D() throws Exception {
        GML3MockData.element(GML.posList, document, document);
        document.getDocumentElement().setAttribute("srsDimension", "1");
        document.getDocumentElement().setAttribute("count", "2");
        document.getDocumentElement().appendChild(document.createTextNode("1.0 2.0 "));

        DirectPosition[] dps = (DirectPosition[]) parse();
        assertNotNull(dps);

        assertEquals(2, dps.length);
        assertTrue(dps[0] instanceof DirectPosition1D);
        assertTrue(dps[1] instanceof DirectPosition1D);

        assertEquals(1d, dps[0].getOrdinate(0), 0d);
        assertEquals(2d, dps[1].getOrdinate(0), 0d);
    }

    @Test
    public void test2D() throws Exception {
        GML3MockData.element(GML.posList, document, document);
        document.getDocumentElement().setAttribute("srsDimension", "2");
        document.getDocumentElement().setAttribute("count", "1");
        document.getDocumentElement().appendChild(document.createTextNode("1.0 2.0 "));

        DirectPosition[] dps = (DirectPosition[]) parse();
        assertNotNull(dps);

        assertEquals(1, dps.length);
        assertTrue(dps[0] instanceof DirectPosition2D);

        assertEquals(1d, dps[0].getOrdinate(0), 0d);
        assertEquals(2d, dps[0].getOrdinate(1), 0d);
    }

    @Test
    public void test3D() throws Exception {
        GML3MockData.element(GML.posList, document, document);
        document.getDocumentElement().setAttribute("srsDimension", "3");

        document.getDocumentElement().appendChild(document.createTextNode("1.0 2.0 1.0 3 4 5"));

        DirectPosition[] dps = (DirectPosition[]) parse();
        assertNotNull(dps);

        assertEquals(2, dps.length);
        assertTrue(dps[0] instanceof DirectPosition3D);

        assertEquals(1d, dps[0].getOrdinate(0), 0d);
        assertEquals(2d, dps[0].getOrdinate(1), 0d);
        assertEquals(1d, dps[0].getOrdinate(2), 0d);

        assertEquals(3d, dps[1].getOrdinate(0), 0d);
        assertEquals(4d, dps[1].getOrdinate(1), 0d);
        assertEquals(5d, dps[1].getOrdinate(2), 0d);
    }

    @Test
    public void testEncode2D() throws Exception {
        LineString line = GML3MockData.lineStringLite2D();
        CoordinateSequence seq = line.getCoordinateSequence();
        Document doc = encode(seq, GML.posList);
        checkPosListOrdinates(
                doc, CoordinateSequences.coordinateDimension(seq) * line.getNumPoints());
    }

    @Test
    public void testEncode3D() throws Exception {
        LineString line = GML3MockData.lineStringLite3D();
        CoordinateSequence seq = line.getCoordinateSequence();
        Document doc = encode(seq, GML.posList);
        checkPosListOrdinates(
                doc, CoordinateSequences.coordinateDimension(seq) * line.getNumPoints());
    }
}
