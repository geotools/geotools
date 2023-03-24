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

import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml3.GML;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.io.ParseException;
import org.w3c.dom.Document;

/**
 * Unit test for GeometryCollectionEncoder
 *
 * @author
 */
public class GeometryCollectionEncoderTest extends GeometryEncoderTestSupport {
    @Test
    public void testGeometryCollectionEncoder() throws ParseException, Exception {
        GeometryCollectionEncoder gce =
                new GeometryCollectionEncoder(gtEncoder, "gml", GML.NAMESPACE);
        GeometryCollection geometry =
                (GeometryCollection)
                        new WKTReader2()
                                .read(
                                        "GEOMETRYCOLLECTION (LINESTRING"
                                                + " (180 200, 160 180), POINT (19 19), POINT (20 10))");
        Document doc = encode(gce, geometry, "feature.1");
        // XMLTestSupport.print(doc);
        assertThat(doc, hasXPath("count(//gml:LineString)", equalTo("1")));
        assertThat(doc, hasXPath("count(//gml:Point)", equalTo("2")));
        assertThat(doc, hasXPath("count(//gml:MultiGeometry)", equalTo("1")));
        assertThat(
                doc,
                hasXPath(
                        "//gml:MultiGeometry/gml:geometryMember/gml:LineString/gml:posList",
                        equalTo("180 200 160 180")));
        assertThat(
                doc,
                hasXPath(
                        "//gml:MultiGeometry/gml:geometryMember/gml:Point/gml:pos",
                        equalTo("19 19")));
        assertThat(doc, hasXPath("//gml:MultiGeometry/@gml:id", equalTo("feature.1")));
        assertThat(
                doc,
                hasXPath(
                        "//gml:MultiGeometry/gml:geometryMember[1]/gml:LineString/@gml:id",
                        equalTo("feature.1.1")));
        assertThat(
                doc,
                hasXPath(
                        "//gml:MultiGeometry/gml:geometryMember[2]/gml:Point/@gml:id",
                        equalTo("feature.1.2")));
        assertThat(
                doc,
                hasXPath(
                        "//gml:MultiGeometry/gml:geometryMember[3]/gml:Point/@gml:id",
                        equalTo("feature.1.3")));
    }
}
