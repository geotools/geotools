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


import org.custommonkey.xmlunit.XMLAssert;
import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.gml3.v3_2.GML;
import org.geotools.gml3.v3_2.GML32TestSupport;
import org.w3c.dom.Document;

public class PolygonTypeBindingTest extends GML32TestSupport {
//    public void testNoInterior() throws Exception {
//        GML3MockData.polygon(document, document);
//
//        Polygon polygon = (Polygon) parse();
//        assertNotNull(polygon);
//    }
    
    public void testEncode() throws Exception {
        Document dom = encode(GML3MockData.polygon(), GML.Polygon);
        print(dom);
        
        assertEquals("gml:Polygon", dom.getDocumentElement().getNodeName());
        XMLAssert.assertXpathExists("/gml:Polygon/gml:exterior", dom);
        XMLAssert.assertXpathExists("/gml:Polygon/gml:exterior/gml:LinearRing", dom);
    }
}
