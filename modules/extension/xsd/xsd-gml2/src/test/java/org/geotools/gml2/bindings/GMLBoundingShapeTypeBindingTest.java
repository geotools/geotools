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


public class GMLBoundingShapeTypeBindingTest extends GMLTestSupport {
    public void testType() {
        assertEquals(Envelope.class, binding(GML.BoundingShapeType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GML.BoundingShapeType).getExecutionMode());
    }

    public void testParseWithBox() throws Exception {
        GML2MockData.boundedBy(document, document);

        Envelope envelope = (Envelope) parse();

        assertFalse(envelope.isNull());
    }

    public void testParseWithNull() throws Exception {
        GML2MockData.boundedByWithNull(document, document);

        Envelope envelope = (Envelope) parse();

        assertTrue(envelope.isNull());
    }

    public void testEncodeWithBox() throws Exception {
        Envelope envelope = new Envelope(1, 2, 3, 4);
        Document doc = encode(envelope, GML.boundedBy);

        assertEquals(1,
            doc.getElementsByTagNameNS(GML.NAMESPACE, GML.Box.getLocalPart()).getLength());
    }

    public void testEncodeWithNull() throws Exception {
        Envelope envelope = new Envelope();
        envelope.setToNull();

        Document doc = encode(envelope, GML.boundedBy);

        assertEquals(1, doc.getElementsByTagNameNS(GML.NAMESPACE, "null").getLength());
        assertEquals("unknown", doc.getElementsByTagNameNS(GML.NAMESPACE, "null").item(0).getFirstChild().getTextContent() );
    }
}
