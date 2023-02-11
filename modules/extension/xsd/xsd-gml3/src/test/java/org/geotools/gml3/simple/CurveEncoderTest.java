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
import org.locationtech.jts.geom.LineString;
import org.w3c.dom.Document;
import org.xmlunit.builder.Input;

public class CurveEncoderTest extends GeometryEncoderTestSupport {
    @Test
    public void testEncodeCircle() throws Exception {
        CurveEncoder encoder = new CurveEncoder(gtEncoder, "gml", GML.NAMESPACE);
        LineString geometry =
                (LineString)
                        new WKTReader2().read("CIRCULARSTRING(-10 0, -8 2, -6 0, -8 -2, -10 0)");
        Document doc = encode(encoder, geometry, "circle.abc");
        // MLTestSupport.print(doc);
        Source actual = Input.fromDocument(doc).build();
        assertThat(
                actual,
                hasXPath("count(//gml:Curve/gml:segments/gml:ArcString/gml:posList)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "//gml:Curve/gml:segments/gml:ArcString/@interpolation",
                                equalTo("circularArc3Points"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(
                                "//gml:Curve/gml:segments/gml:ArcString/gml:posList",
                                equalTo("-10 0 -8 2 -6 0 -8 -2 -10 0"))
                        .withNamespaceContext(NAMESPACES));
        // geometry ids
        assertThat(
                actual,
                hasXPath("//gml:Curve/@gml:id", equalTo("circle.abc"))
                        .withNamespaceContext(NAMESPACES));
    }

    @Test
    public void testEncodeCompound() throws Exception {
        CurveEncoder encoder = new CurveEncoder(gtEncoder, "gml", GML.NAMESPACE);
        LineString geometry =
                (LineString)
                        new WKTReader2()
                                .read(
                                        "COMPOUNDCURVE(CIRCULARSTRING(0 0, 2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0))");
        Document doc = encode(encoder, geometry, "compound.3");
        // XMLTestSupport.print(doc);
        Source actual = Input.fromDocument(doc).build();

        assertThat(
                actual,
                hasXPath("count(//gml:Curve//gml:segments/*)", equalTo("2"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:ArcString)", equalTo("1")).withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("count(//gml:LineStringSegment)", equalTo("1"))
                        .withNamespaceContext(NAMESPACES));

        assertThat(
                actual,
                hasXPath("//gml:ArcString/@interpolation", equalTo("circularArc3Points"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("//gml:ArcString/gml:posList", equalTo("0 0 2 0 2 1 2 3 4 3"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("//gml:LineStringSegment/@interpolation", equalTo("linear"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath("//gml:LineStringSegment/gml:posList", equalTo("4 3 4 5 1 4 0 0"))
                        .withNamespaceContext(NAMESPACES));
        // geometry ids
        assertThat(
                actual,
                hasXPath("//gml:Curve/@gml:id", equalTo("compound.3"))
                        .withNamespaceContext(NAMESPACES));
    }
}
