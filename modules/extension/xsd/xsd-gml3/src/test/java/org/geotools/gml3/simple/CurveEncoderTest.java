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

public class CurveEncoderTest extends GeometryEncoderTestSupport {

    public void testEncodeCircle() throws Exception {
        CurveEncoder encoder = new CurveEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Geometry geometry =
                new WKTReader2().read("CIRCULARSTRING(-10 0, -8 2, -6 0, -8 -2, -10 0)");
        Document doc = encode(encoder, geometry, "circle.abc");
        // MLTestSupport.print(doc);
        assertEquals(
                1,
                xpath.getMatchingNodes("//gml:Curve/gml:segments/gml:ArcString/gml:posList", doc)
                        .getLength());
        assertEquals(
                "circularArc3Points",
                xpath.evaluate("//gml:Curve/gml:segments/gml:ArcString/@interpolation", doc));
        assertEquals(
                "-10 0 -8 2 -6 0 -8 -2 -10 0",
                xpath.evaluate("//gml:Curve/gml:segments/gml:ArcString/gml:posList", doc));
        // geometry ids
        assertEquals("circle.abc", xpath.evaluate("//gml:Curve/@gml:id", doc));
    }

    public void testEncodeCompound() throws Exception {
        CurveEncoder encoder = new CurveEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Geometry geometry =
                new WKTReader2()
                        .read(
                                "COMPOUNDCURVE(CIRCULARSTRING(0 0, 2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0))");
        Document doc = encode(encoder, geometry, "compound.3");
        // XMLTestSupport.print(doc);
        assertEquals(2, xpath.getMatchingNodes("//gml:Curve//gml:segments/*", doc).getLength());
        assertEquals(1, xpath.getMatchingNodes("//gml:ArcString", doc).getLength());
        assertEquals(1, xpath.getMatchingNodes("//gml:LineStringSegment", doc).getLength());
        assertEquals("circularArc3Points", xpath.evaluate("//gml:ArcString/@interpolation", doc));
        assertEquals("0 0 2 0 2 1 2 3 4 3", xpath.evaluate("//gml:ArcString/gml:posList", doc));
        assertEquals("linear", xpath.evaluate("//gml:LineStringSegment/@interpolation", doc));
        assertEquals("4 3 4 5 1 4 0 0", xpath.evaluate("//gml:LineStringSegment/gml:posList", doc));
        // geometry ids
        assertEquals("compound.3", xpath.evaluate("//gml:Curve/@gml:id", doc));
    }
}
