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
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;

import javax.xml.transform.Source;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml3.GML;
import org.junit.Test;
import org.locationtech.jts.geom.MultiPolygon;
import org.w3c.dom.Document;
import org.xmlunit.builder.Input;

public class MultiPolygonTest extends GeometryEncoderTestSupport {
    @Test
    public void testEncodeMultiPolygon() throws Exception {
        MultiPolygonEncoder encoder = new MultiPolygonEncoder(gtEncoder, "gml", GML.NAMESPACE);
        MultiPolygon geometry =
                (MultiPolygon)
                        new WKTReader2()
                                .read(
                                        "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");
        Document doc = encode(encoder, geometry, "mpoly");
        // print(doc);
        // quick geom test
        Source actual = Input.fromDocument(doc).build();
        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiSurface/gml:surfaceMember[1]/gml:Polygon/gml:exterior/gml:LinearRing/gml:posList",
                                equalTo("1 1 5 1 5 5 1 5 1 1"))
                        .withNamespaceContext(NAMESPACES));
        // ids
        assertThat(
                actual,
                hasXPath("/gml:MultiSurface/@gml:id", equalTo("mpoly"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiSurface/gml:surfaceMember[1]/gml:Polygon/@gml:id",
                                equalTo("mpoly.1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiSurface/gml:surfaceMember[2]/gml:Polygon/@gml:id",
                                equalTo("mpoly.2"))
                        .withNamespaceContext(NAMESPACES));
    }

    /** No encode gml:id test */
    @Test
    public void testEncodeMultiPolygonNoGmlId() throws Exception {
        MultiPolygonEncoder encoder =
                new MultiPolygonEncoder(gtEncoder, "gml", GML.NAMESPACE, false);
        MultiPolygon geometry =
                (MultiPolygon)
                        new WKTReader2()
                                .read(
                                        "MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");
        Document doc = encode(encoder, geometry, "mpoly");
        Source actual = Input.fromDocument(doc).build();

        assertThat(
                actual,
                hasXPath(
                                "count(/gml:MultiSurface/gml:surfaceMember/gml:Polygon/@gml:id)",
                                equalTo("0"))
                        .withNamespaceContext(NAMESPACES));
    }
}
