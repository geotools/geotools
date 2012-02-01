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
import org.w3c.dom.NodeList;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;

/**
 * 
 *
 * @source $URL$
 */
public class MultiCurveTypeBindingTest extends GML3TestSupport {

    public void testEncode() throws Exception {
        Geometry geometry = GML3MockData.multiLineString();
        GML3EncodingUtils.setID(geometry, "geometry");
        Document dom = encode(geometry, GML.MultiCurve);
        // print(dom);
        assertEquals("geometry", getID(dom.getDocumentElement()));
        assertEquals(2, dom.getElementsByTagNameNS(GML.NAMESPACE, "curveMember").getLength());
        NodeList children = dom
                .getElementsByTagNameNS(GML.NAMESPACE, GML.LineString.getLocalPart());
        assertEquals(2, children.getLength());
        assertEquals("geometry.1", getID(children.item(0)));
        assertEquals("geometry.2", getID(children.item(1)));
    }

    public void testParseWithCurveMember() throws Exception {
        GML3MockData.multiCurve(document, document);
        MultiLineString mline = (MultiLineString) parse();
        
        assertEquals(2, mline.getNumGeometries());
    }

    public void testParseWithCurveMembers() throws Exception {
        GML3MockData.multiCurve(document, document, false);
        MultiLineString mline = (MultiLineString) parse();
        
        assertEquals(2, mline.getNumGeometries());
    }
}
