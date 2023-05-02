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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml3.GML;
import org.junit.Test;
import org.locationtech.jts.geom.MultiPoint;
import org.w3c.dom.Document;

public class MultiPointTest extends GeometryEncoderTestSupport {
    @Test
    public void testEncodeMultiPoint() throws Exception {
        MultiPointEncoder encoder = new MultiPointEncoder(gtEncoder, "gml", GML.NAMESPACE);
        MultiPoint geometry = (MultiPoint) new WKTReader2().read("MULTIPOINT(0 0, 1 1)");
        Document doc = encode(encoder, geometry, "points");
        assertThat(
                doc,
                hasXPath("/gml:MultiPoint/gml:pointMember[1]/gml:Point/gml:pos", equalTo("0 0")));
        assertThat(
                doc,
                hasXPath("/gml:MultiPoint/gml:pointMember[2]/gml:Point/gml:pos", equalTo("1 1")));
        // ids
        assertThat(doc, hasXPath("/gml:MultiPoint/@gml:id", equalTo("points")));
        assertThat(
                doc,
                hasXPath(
                        "/gml:MultiPoint/gml:pointMember[1]/gml:Point/@gml:id",
                        equalTo("points.1")));
        assertThat(
                doc,
                hasXPath(
                        "/gml:MultiPoint/gml:pointMember[2]/gml:Point/@gml:id",
                        equalTo("points.2")));
    }

    /** no encode gml:id test */
    @Test
    public void testEncodeMultiPointNoGmlId() throws Exception {
        MultiPointEncoder encoder = new MultiPointEncoder(gtEncoder, "gml", GML.NAMESPACE, false);
        MultiPoint geometry = (MultiPoint) new WKTReader2().read("MULTIPOINT(0 0, 1 1)");
        Document doc = encode(encoder, geometry, "points");

        assertThat(
                doc,
                hasXPath(
                        "count(//gml:MultiPoint/gml:pointMember/gml:Point/@gml:id)", equalTo("0")));
    }
}
