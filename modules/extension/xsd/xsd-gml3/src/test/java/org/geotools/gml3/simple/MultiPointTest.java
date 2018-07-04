/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

public class MultiPointTest extends GeometryEncoderTestSupport {

    public void testEncodeMultiPoint() throws Exception {
        MultiPointEncoder encoder = new MultiPointEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Geometry geometry = new WKTReader2().read("MULTIPOINT(0 0, 1 1)");
        Document doc = encode(encoder, geometry, "points");
        // print(doc);
        assertEquals(
                "0 0", xpath.evaluate("/gml:MultiPoint/gml:pointMember[1]/gml:Point/gml:pos", doc));
        assertEquals(
                "1 1", xpath.evaluate("/gml:MultiPoint/gml:pointMember[2]/gml:Point/gml:pos", doc));
        // ids
        assertEquals("points", xpath.evaluate("/gml:MultiPoint/@gml:id", doc));
        assertEquals(
                "points.1",
                xpath.evaluate("/gml:MultiPoint/gml:pointMember[1]/gml:Point/@gml:id", doc));
        assertEquals(
                "points.2",
                xpath.evaluate("/gml:MultiPoint/gml:pointMember[2]/gml:Point/@gml:id", doc));
    }
}
