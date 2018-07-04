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

import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml3.GML;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;

public class MultiCurveEncoderTest extends GeometryEncoderTestSupport {

    public void testEncodeMultiCompound() throws Exception {
        MultiLineStringEncoder encoder =
                new MultiLineStringEncoder(gtEncoder, "gml", GML.NAMESPACE, true);
        Geometry geometry =
                new WKTReader2()
                        .read(
                                "MULTICURVE((100 100, 120 120), COMPOUNDCURVE(CIRCULARSTRING(0 0, 2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0)))");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);
        assertEquals(1, xpath.getMatchingNodes("//gml:MultiCurve", doc).getLength());
        assertEquals(
                2, xpath.getMatchingNodes("//gml:MultiCurve/gml:curveMember", doc).getLength());
        assertEquals(
                "100 100 120 120",
                xpath.evaluate(
                        "//gml:MultiCurve/gml:curveMember[1]/gml:LineString/gml:posList", doc));
        assertEquals(
                2,
                xpath.getMatchingNodes(
                                "//gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/*", doc)
                        .getLength());

        assertEquals(
                "circularArc3Points",
                xpath.evaluate(
                        "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:ArcString/@interpolation",
                        doc));
        assertEquals(
                "0 0 2 0 2 1 2 3 4 3",
                xpath.evaluate(
                        "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:ArcString/gml:posList",
                        doc));

        assertEquals(
                "linear",
                xpath.evaluate(
                        "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:LineStringSegment/@interpolation",
                        doc));
        assertEquals(
                "4 3 4 5 1 4 0 0",
                xpath.evaluate(
                        "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:LineStringSegment/gml:posList",
                        doc));
    }

    public void testEncodeMultiCurve() throws Exception {
        MultiLineStringEncoder encoder =
                new MultiLineStringEncoder(gtEncoder, "gml", GML.NAMESPACE, true);
        Geometry geometry =
                new WKTReader2()
                        .read(
                                "MULTICURVE((105 105, 125 125), CIRCULARSTRING(-10 0, -8 2, -6 0, -8 -2, -10 0))");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);
        assertEquals(1, xpath.getMatchingNodes("//gml:MultiCurve", doc).getLength());
        assertEquals(
                2, xpath.getMatchingNodes("//gml:MultiCurve/gml:curveMember", doc).getLength());
        assertEquals(
                "105 105 125 125",
                xpath.evaluate(
                        "//gml:MultiCurve/gml:curveMember[1]/gml:LineString/gml:posList", doc));
        assertEquals(
                1,
                xpath.getMatchingNodes(
                                "//gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/*", doc)
                        .getLength());

        assertEquals(
                "circularArc3Points",
                xpath.evaluate(
                        "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:ArcString/@interpolation",
                        doc));
        assertEquals(
                "-10 0 -8 2 -6 0 -8 -2 -10 0",
                xpath.evaluate(
                        "/gml:MultiCurve/gml:curveMember[2]/gml:Curve/gml:segments/gml:ArcString/gml:posList",
                        doc));
    }

    public void testEncodeMultiLineString() throws Exception {
        MultiLineStringEncoder encoder =
                new MultiLineStringEncoder(gtEncoder, "gml", GML.NAMESPACE, false);
        Geometry geometry = new WKTReader2().read("MULTILINESTRING((105 105, 125 125))");
        Document doc = encode(encoder, geometry, "multi");
        // XMLTestSupport.print(doc);
        assertEquals(1, xpath.getMatchingNodes("//gml:MultiLineString", doc).getLength());
        assertEquals(
                1,
                xpath.getMatchingNodes("//gml:MultiLineString/gml:lineStringMember", doc)
                        .getLength());
        assertEquals(
                "105 105 125 125",
                xpath.evaluate(
                        "//gml:MultiLineString/gml:lineStringMember[1]/gml:LineString/gml:posList",
                        doc));

        // geometry ids
        assertEquals("multi", xpath.evaluate("/gml:MultiLineString/@gml:id", doc));
        assertEquals(
                "multi.1",
                xpath.evaluate(
                        "/gml:MultiLineString/gml:lineStringMember/gml:LineString/@gml:id", doc));
    }
}
