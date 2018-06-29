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

public class MultiPolygonTest extends GeometryEncoderTestSupport {

    public void testEncodeMultiPolygon() throws Exception {
        MultiPolygonEncoder encoder = new MultiPolygonEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Geometry geometry =
                new WKTReader2()
                        .read(
                                "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");
        Document doc = encode(encoder, geometry, "mpoly");
        // print(doc);
        // quick geom test
        assertEquals(
                "1 1 5 1 5 5 1 5 1 1",
                xpath.evaluate(
                        "/gml:MultiSurface/gml:surfaceMember[1]/gml:Polygon/gml:exterior/gml:LinearRing/gml:posList",
                        doc));
        // ids
        assertEquals("mpoly", xpath.evaluate("/gml:MultiSurface/@gml:id", doc));
        assertEquals(
                "mpoly.1",
                xpath.evaluate("/gml:MultiSurface/gml:surfaceMember[1]/gml:Polygon/@gml:id", doc));
        assertEquals(
                "mpoly.2",
                xpath.evaluate("/gml:MultiSurface/gml:surfaceMember[2]/gml:Polygon/@gml:id", doc));
    }
}
