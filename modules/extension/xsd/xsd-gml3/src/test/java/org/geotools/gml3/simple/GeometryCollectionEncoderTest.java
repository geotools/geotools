/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.gml3.simple;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.xml.test.XMLTestSupport;
import org.w3c.dom.Document;

public class GeometryCollectionEncoderTest extends GeometryEncoderTestSupport{
    public void testGeometryCollectionEncoder() throws ParseException, Exception {
        GeometryCollectionEncoder gce = new GeometryCollectionEncoder(gtEncoder, "gml");
        Geometry geometry = new WKTReader2().read("GEOMETRYCOLLECTION (LINESTRING"
            + " (180 200, 160 180), POINT (19 19), POINT (20 10))");
        Document doc = encode(gce, geometry);
        assertEquals(1, xpath.getMatchingNodes("//gml:LineString", doc).getLength());
        assertEquals(2, xpath.getMatchingNodes("//gml:Point", doc).getLength());
        assertEquals(1, xpath.getMatchingNodes("//gml:GeometryCollection", doc).getLength());
    }
}
