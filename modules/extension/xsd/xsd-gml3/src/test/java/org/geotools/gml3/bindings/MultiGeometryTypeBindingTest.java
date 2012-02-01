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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * 
 *
 * @source $URL$
 */
public class MultiGeometryTypeBindingTest extends GML3TestSupport {
    

    public void testParse() throws Exception {
        GML3MockData.multiGeometry(document, document);
    
        GeometryCollection multiGeom = (GeometryCollection) parse();
        assertNotNull(multiGeom);
    
        assertEquals(3, multiGeom.getNumGeometries());
    }
    
    public void testEncode() throws Exception {        Geometry geometry = GML3MockData.multiGeometry();
        GML3EncodingUtils.setID(geometry, "geometry");
        Document dom = encode(geometry, GML.MultiGeometry);
        // print(dom);
        assertEquals("geometry", getID(dom.getDocumentElement()));
        assertEquals(3, dom.getElementsByTagNameNS(GML.NAMESPACE, "geometryMember").getLength());
        // geometry.1 is not encoded on the gml:Point because user data is already being used for
        // srsDimension and srsName; not going to support the use of these inside a multigeometry
        // and combined with gml:id
        assertEquals("geometry.2", getID(dom.getElementsByTagNameNS(GML.NAMESPACE, "LineString")
                .item(0)));
        assertEquals("geometry.3",
                getID(dom.getElementsByTagNameNS(GML.NAMESPACE, "Polygon").item(0)));
    }

}
