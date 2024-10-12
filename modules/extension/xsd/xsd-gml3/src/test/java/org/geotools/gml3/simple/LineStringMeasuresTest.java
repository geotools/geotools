/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.gml3.GML;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.w3c.dom.Document;

/** Test that linestring containing coordinates with measurements are correctly encoded. */
public final class LineStringMeasuresTest extends GeometryEncoderTestSupport {
    @Test
    public void testEncodeLineMFromLiteCS() throws Exception {
        // create a linestring with M values and encode it in GML
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, -1.5, 3, 4, -2.5}, 3, 1);
        LineString geometry = new GeometryFactory().createLineString(cs);
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Document document = encode(encoder, geometry, "line");
        // check that we got the expected result
        assertThat(document, hasXPath("//gml:posList", equalTo("0 1 -1.5 3 4 -2.5")));
    }

    @Test
    public void testEncodeLineMFromLiteCSNoMeasuresEncoded() throws Exception {
        // create a linestring with M values and encode it in GML
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, -1.5, 3, 4, -2.5}, 3, 1);
        LineString geometry = new GeometryFactory().createLineString(cs);
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Document document = encode(encoder, geometry, false, "line");
        // check that we got the expected result
        assertThat(document, hasXPath("//gml:posList", equalTo("0 1 3 4")));
    }

    @Test
    public void testEncodePointMFromLiteCS() throws Exception {
        // create a point with M values and encode it in GML
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, -1.5}, 3, 1);
        Point geometry = new GeometryFactory().createPoint(cs);
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Document document = encode(encoder, geometry, "line");
        // check that we got the expected result
        assertThat(document, hasXPath("//gml:pos", equalTo("0 1 -1.5")));
    }

    @Test
    public void testEncodePointMFromLiteCSNoMeasuresEncoded() throws Exception {
        // create a point with M values and encode it in GML
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, -1.5}, 3, 1);
        Point geometry = new GeometryFactory().createPoint(cs);
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Document document = encode(encoder, geometry, false, "line");
        // check that we got the expected result
        assertThat(document, hasXPath("//gml:pos", equalTo("0 1")));
    }

    @Test
    public void testEncodeLineZMFromLiteCS() throws Exception {
        // create a linestring with ZM values and encode it in GML 3.1
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, 10, -1.5, 3, 4, 15, -2.5}, 4, 1);
        LineString geometry = new GeometryFactory().createLineString(cs);
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Document document = encode(encoder, geometry, "line");
        // check that we got the expected result
        assertThat(document, hasXPath("//gml:posList", equalTo("0 1 10 -1.5 3 4 15 -2.5")));
    }

    @Test
    public void testEncodeLineZMFromLiteCSNoMeasuresEncoded() throws Exception {
        // create a linestring with ZM values and encode it in GML 3.1
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, 10, -1.5, 3, 4, 15, -2.5}, 4, 1);
        LineString geometry = new GeometryFactory().createLineString(cs);
        LineStringEncoder encoder = new LineStringEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Document document = encode(encoder, geometry, false, "line");
        // check that we got the expected result
        assertThat(document, hasXPath("//gml:posList", equalTo("0 1 10 3 4 15")));
    }

    @Test
    public void testEncodePointZMFromLiteCS() throws Exception {
        // create a point with ZM values and encode it in GML
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, 10, -1.5}, 4, 1);
        Point geometry = new GeometryFactory().createPoint(cs);
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Document document = encode(encoder, geometry, "line");
        // check that we got the expected result
        assertThat(document, hasXPath("//gml:pos", equalTo("0 1 10 -1.5")));
    }

    @Test
    public void testEncodePointZMFromLiteCSNoMeasuresEncoded() throws Exception {
        // create a point with M values and encode it in GML
        LiteCoordinateSequence cs = new LiteCoordinateSequence(new double[] {0, 1, 10, -1.5}, 4, 1);
        Point geometry = new GeometryFactory().createPoint(cs);
        PointEncoder encoder = new PointEncoder(gtEncoder, "gml", GML.NAMESPACE);
        Document document = encode(encoder, geometry, false, "line");
        // check that we got the expected result
        assertThat(document, hasXPath("//gml:pos", equalTo("0 1 10")));
    }
}
