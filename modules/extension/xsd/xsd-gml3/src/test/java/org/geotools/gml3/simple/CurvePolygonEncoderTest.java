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

public class CurvePolygonEncoderTest extends GeometryEncoderTestSupport {

    public void testCircle() throws Exception {
        PolygonEncoder encoder = new PolygonEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Geometry geometry =
                new WKTReader2()
                        .read("CURVEPOLYGON(CIRCULARSTRING(-10 0, -8 2, -6 0, -8 -2, -10 0))");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);

        assertEquals(1, xpath.getMatchingNodes("//gml:Polygon", doc).getLength());
        assertEquals(1, xpath.getMatchingNodes("//gml:Polygon/gml:exterior", doc).getLength());
        assertEquals(0, xpath.getMatchingNodes("//gml:Polygon/gml:interior", doc).getLength());
        String ext1 = "//gml:Polygon/gml:exterior/gml:Ring/gml:curveMember[1]";
        assertEquals(
                "circularArc3Points",
                xpath.evaluate(ext1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation", doc));
        assertEquals(
                "-10 0 -8 2 -6 0 -8 -2 -10 0",
                xpath.evaluate(ext1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList", doc));
    }

    public void testDonut() throws Exception {
        PolygonEncoder encoder = new PolygonEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Geometry geometry =
                new WKTReader2()
                        .read(
                                "CURVEPOLYGON(CIRCULARSTRING(-7 -8, -5 -6, -3 -8, -5 -10, -7 -8),CIRCULARSTRING(-6 -8, -5 -7, -4 -8, -5 -9, -6 -8))");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);

        assertEquals(1, xpath.getMatchingNodes("//gml:Polygon", doc).getLength());
        assertEquals(1, xpath.getMatchingNodes("//gml:Polygon/gml:exterior", doc).getLength());
        assertEquals(1, xpath.getMatchingNodes("//gml:Polygon/gml:interior", doc).getLength());
        String ext1 = "//gml:Polygon/gml:exterior/gml:Ring/gml:curveMember[1]";
        assertEquals(
                "circularArc3Points",
                xpath.evaluate(ext1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation", doc));
        assertEquals(
                "-7 -8 -5 -6 -3 -8 -5 -10 -7 -8",
                xpath.evaluate(ext1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList", doc));

        String int1 = "//gml:Polygon/gml:interior/gml:Ring/gml:curveMember[1]";
        assertEquals(
                "circularArc3Points",
                xpath.evaluate(int1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation", doc));
        assertEquals(
                "-6 -8 -5 -7 -4 -8 -5 -9 -6 -8",
                xpath.evaluate(int1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList", doc));
    }

    public void testComplex() throws Exception {
        PolygonEncoder encoder = new PolygonEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Geometry geometry =
                new WKTReader2()
                        .read(
                                "CURVEPOLYGON(COMPOUNDCURVE(CIRCULARSTRING(0 0, 2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0)), "
                                        + "CIRCULARSTRING(1.7 1, 1.4 0.4, 1.6 0.4, 1.6 0.5, 1.7 1) )");
        Document doc = encode(encoder, geometry);
        // XMLTestSupport.print(doc);

        assertEquals(1, xpath.getMatchingNodes("//gml:Polygon", doc).getLength());
        assertEquals(1, xpath.getMatchingNodes("//gml:Polygon/gml:exterior", doc).getLength());
        assertEquals(1, xpath.getMatchingNodes("//gml:Polygon/gml:interior", doc).getLength());
        String ext1 = "//gml:Polygon/gml:exterior/gml:Ring/gml:curveMember[1]";
        assertEquals(
                "circularArc3Points",
                xpath.evaluate(ext1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation", doc));
        assertEquals(
                "0 0 2 0 2 1 2 3 4 3",
                xpath.evaluate(ext1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList", doc));
        String ext2 = "//gml:Polygon/gml:exterior/gml:Ring/gml:curveMember[2]";
        assertEquals("4 3 4 5 1 4 0 0", xpath.evaluate(ext2 + "/gml:LineString/gml:posList", doc));

        String int1 = "//gml:Polygon/gml:interior/gml:Ring/gml:curveMember[1]";
        assertEquals(
                "circularArc3Points",
                xpath.evaluate(int1 + "/gml:Curve/gml:segments/gml:ArcString/@interpolation", doc));
        assertEquals(
                "1.7 1 1.4 0.4 1.6 0.4 1.6 0.5 1.7 1",
                xpath.evaluate(int1 + "/gml:Curve/gml:segments/gml:ArcString/gml:posList", doc));
    }
}
