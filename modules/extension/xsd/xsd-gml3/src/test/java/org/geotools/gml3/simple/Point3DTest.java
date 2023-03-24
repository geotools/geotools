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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml3.GML;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.w3c.dom.Document;

public class Point3DTest extends GeometryEncoderTestSupport {
    @Test
    public void testEncode3DPoint() throws Exception {
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Point geometry = (Point) new WKTReader2().read("POINT(0 0 50)");
        Document doc = encode(encoder, geometry);
        assertThat(doc, hasXPath("//gml:pos", equalTo("0 0 50")));
    }

    @Test
    public void testEncodeCoordinatesFormatting() throws Exception {
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Point geometry = (Point) new WKTReader2().read("POINT(21396814.969 0 50)");
        Document doc = encode(encoder, geometry, true, null, 4, true, false);
        assertThat(doc, hasXPath("//gml:pos", equalTo("21396814.969 0 50")));

        doc = encode(encoder, geometry, true, null, 2, true, false);
        assertThat(doc, hasXPath("//gml:pos", equalTo("21396814.97 0 50")));

        doc = encode(encoder, geometry, true, null, 4, true, true);
        assertThat(doc, hasXPath("//gml:pos", equalTo("21396814.9690 0.0000 50.0000")));

        doc = encode(encoder, geometry, true, null, 4, false, false);
        assertThat(doc, hasXPath("//gml:pos", equalTo("2.1396814969E7 0 50")));
    }
}
