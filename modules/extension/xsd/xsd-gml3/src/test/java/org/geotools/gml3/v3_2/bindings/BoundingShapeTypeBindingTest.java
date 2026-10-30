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
package org.geotools.gml3.v3_2.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.v3_2.GML;
import org.geotools.gml3.v3_2.GML32TestSupport;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BoundingShapeTypeBindingTest extends GML32TestSupport {
    @Test
    public void testEncode() throws Exception {
        Envelope e = new Envelope(-180, -90, 180, 90);

        Document dom = encode(e, GML.boundedBy);

        Node documentElement = dom.getDocumentElement().getFirstChild();
        assertEquals("Envelope", documentElement.getLocalName());
        assertEquals(GML.NAMESPACE, documentElement.getNamespaceURI());
    }

    @Test
    public void testEncodeWithCRS() throws Exception {
        Envelope e = new ReferencedEnvelope(-180, -90, 180, 90, CRS.decode("EPSG:4326"));
        Document dom = encode(e, GML.boundedBy);
        Node envelope = dom.getDocumentElement().getFirstChild();
        assertEquals("Envelope", envelope.getLocalName());
        assertEquals(GML.NAMESPACE, envelope.getNamespaceURI());
        assertTrue(((Element) envelope).getAttribute("srsName").endsWith("4326"));
    }

    @Test
    public void testEncodeAsNull() throws Exception {
        Envelope e = new Envelope();
        e.setToNull();

        Document dom = encode(e, GML.boundedBy);
        Node nullNode = dom.getDocumentElement().getFirstChild();
        assertEquals("Null", nullNode.getLocalName());
        assertEquals(GML.NAMESPACE, nullNode.getNamespaceURI());
    }
}
