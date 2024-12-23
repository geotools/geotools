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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.gml2.GML;
import org.geotools.xsd.Binding;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.w3c.dom.Document;

public class GMLBoundingShapeTypeBindingTest extends GMLTestSupport {
    @Test
    public void testType() {
        assertEquals(Envelope.class, binding(GML.BoundingShapeType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GML.BoundingShapeType).getExecutionMode());
    }

    @Test
    public void testParseWithBox() throws Exception {
        GML2MockData.boundedBy(document, document);

        Envelope envelope = (Envelope) parse();

        assertFalse(envelope.isNull());
    }

    @Test
    public void testParseWithNull() throws Exception {
        GML2MockData.boundedByWithNull(document, document);

        Envelope envelope = (Envelope) parse();

        assertTrue(envelope.isNull());
    }

    @Test
    public void testEncodeWithBox() throws Exception {
        Envelope envelope = new Envelope(1, 2, 3, 4);
        Document doc = encode(envelope, GML.boundedBy);

        assertEquals(
                1,
                doc.getElementsByTagNameNS(GML.NAMESPACE, GML.Box.getLocalPart())
                        .getLength());
    }

    @Test
    public void testEncodeWithNull() throws Exception {
        Envelope envelope = new Envelope();
        envelope.setToNull();

        Document doc = encode(envelope, GML.boundedBy);

        assertEquals(1, doc.getElementsByTagNameNS(GML.NAMESPACE, "null").getLength());
        assertEquals(
                "unknown",
                doc.getElementsByTagNameNS(GML.NAMESPACE, "null")
                        .item(0)
                        .getFirstChild()
                        .getTextContent());
    }
}
