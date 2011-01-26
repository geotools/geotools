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

import com.vividsolutions.jts.geom.LineString;


public class GMLLineStringTypeBinding2Test extends GMLTestSupport {
    public void testType() {
        assertEquals(LineString.class, binding(GML.LineStringType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.BEFORE, binding(GML.LineStringType).getExecutionMode());
    }

    public void testParse() throws Exception {
        GML2MockData.lineString(document, document);

        LineString l = (LineString) parse();

        assertEquals(2, l.getCoordinates().length);
    }

    public void testEncode() throws Exception {
        Document doc = encode(GML2MockData.lineString(), GML.LineString);

        assertEquals(1,
            doc.getElementsByTagNameNS(GML.NAMESPACE, GML.coordinates.getLocalPart()).getLength());
    }
}
