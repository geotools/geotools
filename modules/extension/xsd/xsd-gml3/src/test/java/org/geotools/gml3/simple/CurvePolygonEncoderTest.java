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
import org.locationtech.jts.geom.Polygon;
import org.w3c.dom.Document;
import org.xmlunit.builder.Input;

public class CurvePolygonEncoderTest extends GeometryEncoderTestSupport {
    @Test
    public void testCircle() throws Exception {
        PolygonEncoder encoder = new PolygonEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Polygon geometry =
                (Polygon)
                        new WKTReader2()
                                .read(
                                        "CURVEPOLYGON(CIRCULARSTRING(-10 0, -8 2, -6 0, -8 -2, -10 0))");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);

        Source actual = Input.fromDocument(doc).build();
        assertThat(
                actual,
                hasXPath("count(//gml:Polygon)", equalTo("1")).withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:Polygon/gml:exterior)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:Polygon/gml:interior)", equalTo("0"))
                        .withNamespaceContext(NAMESPACES));
        String ext1 = "//gml:Polygon/gml:exterior/gml:Ring/gml:curveMember[1]";
        assertThat(
                actual,
                hasXPath(
                                ext1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation",
                                equalTo("circularArc3Points"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                ext1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList",
                                equalTo("-10 0 -8 2 -6 0 -8 -2 -10 0"))
                        .withNamespaceContext(NAMESPACES));
    }

    @Test
    public void testDonut() throws Exception {
        PolygonEncoder encoder = new PolygonEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Polygon geometry =
                (Polygon)
                        new WKTReader2()
                                .read(
                                        "CURVEPOLYGON(CIRCULARSTRING(-7 -8, -5 -6, -3 -8, -5 -10, -7 -8),CIRCULARSTRING(-6 -8, -5 -7, -4 -8, -5 -9, -6 -8))");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);

        Source actual = Input.fromDocument(doc).build();
        assertThat(
                actual,
                hasXPath("count(//gml:Polygon)", equalTo("1")).withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:Polygon/gml:exterior)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:Polygon/gml:interior)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        String ext1 = "//gml:Polygon/gml:exterior/gml:Ring/gml:curveMember[1]";
        assertThat(
                actual,
                hasXPath(
                                ext1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation",
                                equalTo("circularArc3Points"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                ext1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList",
                                equalTo("-7 -8 -5 -6 -3 -8 -5 -10 -7 -8"))
                        .withNamespaceContext(NAMESPACES));

        String int1 = "//gml:Polygon/gml:interior/gml:Ring/gml:curveMember[1]";
        assertThat(
                actual,
                hasXPath(
                                int1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation",
                                equalTo("circularArc3Points"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                int1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList",
                                equalTo("-6 -8 -5 -7 -4 -8 -5 -9 -6 -8"))
                        .withNamespaceContext(NAMESPACES));
    }

    @Test
    public void testComplex() throws Exception {
        PolygonEncoder encoder = new PolygonEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Polygon geometry =
                (Polygon)
                        new WKTReader2()
                                .read(
                                        "CURVEPOLYGON(COMPOUNDCURVE(CIRCULARSTRING(0 0, 2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0)), "
                                                + "CIRCULARSTRING(1.7 1, 1.4 0.4, 1.6 0.4, 1.6 0.5, 1.7 1) )");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);

        Source actual = Input.fromDocument(doc).build();
        assertThat(
                actual,
                hasXPath("count(//gml:Polygon)", equalTo("1")).withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:Polygon/gml:exterior)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:Polygon/gml:interior)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        String ext1 = "//gml:Polygon/gml:exterior/gml:Ring/gml:curveMember[1]";
        assertThat(
                actual,
                hasXPath(
                                ext1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation",
                                equalTo("circularArc3Points"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                ext1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList",
                                equalTo("0 0 2 0 2 1 2 3 4 3"))
                        .withNamespaceContext(NAMESPACES));
        String ext2 = "//gml:Polygon/gml:exterior/gml:Ring/gml:curveMember[2]";
        assertThat(
                actual,
                hasXPath(ext2 + "/gml:LineString/gml:posList", equalTo("4 3 4 5 1 4 0 0"))
                        .withNamespaceContext(NAMESPACES));

        String int1 = "//gml:Polygon/gml:interior/gml:Ring/gml:curveMember[1]";
        assertThat(
                actual,
                hasXPath(
                                int1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation",
                                equalTo("circularArc3Points"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                int1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList",
                                equalTo("1.7 1 1.4 0.4 1.6 0.4 1.6 0.5 1.7 1"))
                        .withNamespaceContext(NAMESPACES));
    }
}
