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

import com.vividsolutions.jts.geom.Envelope;


public class GMLBoxTypeBinding2Test extends GMLTestSupport {
    public void testType() {
        assertEquals(Envelope.class, binding(GML.BoxType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GML.BoxType).getExecutionMode());
    }

    public void testParse() throws Exception {
        GML2MockData.box(document, document);

        Envelope box = (Envelope) parse();
        assertEquals(1.0, box.getMinX(), 0.0);
        assertEquals(2.0, box.getMinY(), 0.0);
        assertEquals(1.0, box.getMaxX(), 0.0);
        assertEquals(2.0, box.getMaxY(), 0.0);
    }

    public void testEncode() throws Exception {
        Document doc = encode(new Envelope(1, 2, 3, 4), GML.Box);

        assertEquals(2,
            doc.getElementsByTagNameNS(GML.NAMESPACE, GML.coord.getLocalPart()).getLength());
    }
}
