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
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;

import javax.xml.transform.Source;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml3.GML;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.w3c.dom.Document;
import org.xmlunit.builder.Input;

public class LineString3DTest extends GeometryEncoderTestSupport {
    @Test
    public void testEncode3DLine() throws Exception {
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml", GML.NAMESPACE);
        LineString geometry = (LineString) new WKTReader2().read("LINESTRING(0 0 50, 120 0 100)");
        Document doc = encode(encoder, geometry, "threed");
        // print(doc);
        Source actual = Input.fromDocument(doc).build();
        assertThat(
                actual,
                hasXPath("//gml:posList", equalTo("0 0 50 120 0 100"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("//gml:LineString/@gml:id", equalTo("threed"))
                        .withNamespaceContext(NAMESPACES));
    }

    @Test
    public void testEncode3DLineFromLiteCS() throws Exception {
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml", GML.NAMESPACE);
        LiteCoordinateSequence cs =
                new LiteCoordinateSequence(new double[] {0, 0, 50, 120, 0, 100}, 3);
        LineString geometry = new GeometryFactory().createLineString(cs);
        Document doc = encode(encoder, geometry);
        Source actual = Input.fromDocument(doc).build();
        // print(doc);
        assertThat(
                actual,
                hasXPath("//gml:posList", equalTo("0 0 50 120 0 100"))
                        .withNamespaceContext(NAMESPACES));
    }
}
