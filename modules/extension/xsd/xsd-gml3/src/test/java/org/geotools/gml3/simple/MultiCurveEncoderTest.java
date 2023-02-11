/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;
import org.xmlunit.builder.Input;

public class MultiCurveEncoderTest extends GeometryEncoderTestSupport {
    @Test
    public void testEncodeMultiCompound() throws Exception {
        MultiLineStringEncoder encoder =
                new MultiLineStringEncoder(gtEncoder, "gml", GML.NAMESPACE, true);
        Geometry geometry =
                new WKTReader2()
                        .read(
                                "MULTICURVE((100 100, 120 120), COMPOUNDCURVE(CIRCULARSTRING(0 0, 2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0)))");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);
        Source actual = Input.fromDocument(doc).build();
        assertThat(
                actual,
                hasXPath("count(//gml:MultiCurve)", equalTo("1")).withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:MultiCurve/gml:curveMember)", equalTo("2"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "//gml:MultiCurve/gml:curveMember[1]/gml:LineString/gml:posList",
                                equalTo("100 100 120 120"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "count(//gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/*)",
                                equalTo("2"))
                        .withNamespaceContext(NAMESPACES));

        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:ArcString/@interpolation",
                                equalTo("circularArc3Points"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:ArcString/gml:posList",
                                equalTo("0 0 2 0 2 1 2 3 4 3"))
                        .withNamespaceContext(NAMESPACES));

        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:LineStringSegment/@interpolation",
                                equalTo("linear"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:LineStringSegment/gml:posList",
                                equalTo("4 3 4 5 1 4 0 0"))
                        .withNamespaceContext(NAMESPACES));
    }

    @Test
    public void testEncodeMultiCurve() throws Exception {
        MultiLineStringEncoder encoder =
                new MultiLineStringEncoder(gtEncoder, "gml", GML.NAMESPACE, true);
        Geometry geometry =
                new WKTReader2()
                        .read(
                                "MULTICURVE((105 105, 125 125), CIRCULARSTRING(-10 0, -8 2, -6 0, -8 -2, -10 0))");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);
        Source actual = Input.fromDocument(doc).build();
        assertThat(
                actual,
                hasXPath("count(//gml:MultiCurve)", equalTo("1")).withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:MultiCurve/gml:curveMember)", equalTo("2"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "//gml:MultiCurve/gml:curveMember[1]/gml:LineString/gml:posList",
                                equalTo("105 105 125 125"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "count(//gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/*)",
                                equalTo("1"))
                        .withNamespaceContext(NAMESPACES));

        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:ArcString/@interpolation",
                                equalTo("circularArc3Points"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:ArcString/gml:posList",
                                equalTo("-10 0 -8 2 -6 0 -8 -2 -10 0"))
                        .withNamespaceContext(NAMESPACES));
    }

    @Test
    public void testEncodeMultiLineString() throws Exception {
        MultiLineStringEncoder encoder =
                new MultiLineStringEncoder(gtEncoder, "gml", GML.NAMESPACE, false);
        Geometry geometry = new WKTReader2().read("MULTILINESTRING((105 105, 125 125))");
        Document doc = encode(encoder, geometry, "multi");
        // XMLTestSupport.print(doc);
        Source actual = Input.fromDocument(doc).build();
        assertThat(
                actual,
                hasXPath("count(//gml:MultiLineString)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:MultiLineString/gml:lineStringMember)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "//gml:MultiLineString/gml:lineStringMember[1]/gml:LineString/gml:posList",
                                equalTo("105 105 125 125"))
                        .withNamespaceContext(NAMESPACES));

        // geometry ids
        assertThat(
                actual,
                hasXPath("/gml:MultiLineString/@gml:id", equalTo("multi"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "/gml:MultiLineString/gml:lineStringMember/gml:LineString/@gml:id",
                                equalTo("multi.1"))
                        .withNamespaceContext(NAMESPACES));
    }
}
