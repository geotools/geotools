/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.simple;

import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml3.GML;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;

public class Point3DTest extends GeometryEncoderTestSupport {

    public void testEncode3DPoint() throws Exception {
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Geometry geometry = new WKTReader2().read("POINT(0 0 50)");
        Document doc = encode(encoder, geometry);
        // print(doc);
        assertEquals("0 0 50", xpath.evaluate("//gml:pos", doc));
    }
}
