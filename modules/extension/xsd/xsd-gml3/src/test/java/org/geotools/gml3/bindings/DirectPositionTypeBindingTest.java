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
import org.opengis.geometry.DirectPosition;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;


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
    

}
