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
package org.geotools.gml2.bindings;

import org.geotools.gml2.GML;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;


public class GMLCoordinatesTypeBinding2Test extends GMLTestSupport {
    public void testType() {
        assertEquals(CoordinateSequence.class, binding(GML.CoordinatesType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GML.CoordinatesType).getExecutionMode());
    }

    public void testParse() throws Exception {
        GML2MockData.coordinates(document, document);

        CoordinateSequence c = (CoordinateSequence) parse();

        assertEquals(new Coordinate(1, 2), c.getCoordinate(0));
        assertEquals(new Coordinate(3, 4), c.getCoordinate(1));
    }

    public void testEncode() throws Exception {
        Document doc = encode(GML2MockData.coordinates(), GML.coordinates);

        assertEquals("1.0,2.0 3.0,4.0", doc.getDocumentElement().getFirstChild().getNodeValue());
    }
}
