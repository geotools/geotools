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

import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.gml3.v3_2.GML;
import org.geotools.gml3.v3_2.GML32TestSupport;
import org.locationtech.jts.geom.Point;
import org.w3c.dom.Document;

/** @source $URL$ */
public class PointTypeBindingTest extends GML32TestSupport {
    //    public void testPos() throws Exception {
    //        GML3MockData.point(document, document);
    //
    //        Point p = (Point) parse();
    //        assertNotNull(p);
    //        assertEquals(new Coordinate(1d, 2d), p.getCoordinate());
    //
    //        assertTrue(p.getUserData() instanceof CoordinateReferenceSystem);
    //    }

    public void testEncode() throws Exception {
        Point p = GML3MockData.point();
        Document dom = encode(p, GML.Point);

        assertEquals(
                1, dom.getElementsByTagNameNS(GML.NAMESPACE, GML.pos.getLocalPart()).getLength());
        assertEquals(
                "urn:ogc:def:crs:EPSG::4326", dom.getDocumentElement().getAttribute("srsName"));
    }
}
