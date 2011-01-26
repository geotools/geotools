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

import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Envelope;


public class EnvelopeTypeBindingTest extends GML3TestSupport {
    public void testEncode() throws Exception {
        Document dom = encode(GML3MockData.bounds(), GML.Envelope);
        assertEquals(1, dom.getElementsByTagNameNS(GML.NAMESPACE, "lowerCorner").getLength());
        assertEquals(1, dom.getElementsByTagNameNS(GML.NAMESPACE, "upperCorner").getLength());

        Element lowerCorner = (Element) dom.getElementsByTagNameNS(GML.NAMESPACE, "lowerCorner")
                                           .item(0);
        assertEquals("0.0 0.0", lowerCorner.getFirstChild().getNodeValue());

        Element upperCorner = (Element) dom.getElementsByTagNameNS(GML.NAMESPACE, "upperCorner")
                                           .item(0);
        assertEquals("10.0 10.0", upperCorner.getFirstChild().getNodeValue());

        //assertEquals("urn:x-ogc:def:crs:EPSG:6.11.2:4326",
        assertEquals("urn:x-ogc:def:crs:EPSG:4326",
            dom.getDocumentElement().getAttributeNS(null, "srsName"));
    }

    public void testEncodeNull() throws Exception {
        Envelope e = new Envelope();
        e.setToNull();

        Document dom = encode(e, GML.Envelope);

        assertEquals(1,
            dom.getElementsByTagNameNS(GML.NAMESPACE, GML.Null.getLocalPart()).getLength());
    }
}
