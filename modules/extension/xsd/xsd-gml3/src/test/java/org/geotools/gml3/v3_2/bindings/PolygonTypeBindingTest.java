/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.v3_2.bindings;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.gml3.v3_2.GML;
import org.geotools.gml3.v3_2.GML32TestSupport;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import org.w3c.dom.Document;

public class PolygonTypeBindingTest extends GML32TestSupport {
    //    public void testNoInterior() throws Exception {
    //        GML3MockData.polygon(document, document);
    //
    //        Polygon polygon = (Polygon) parse();
    //        assertNotNull(polygon);
    //    }
    @Test
    public void testParse() throws Exception {
        GML3MockData.polygonWithPosList(document, document);
        Polygon p = (Polygon) parse();
        assertNotNull(p);
    }

    @Test
    public void testEncode() throws Exception {
        Document dom = encode(GML3MockData.polygon(), GML.Polygon);

        assertEquals("gml:Polygon", dom.getDocumentElement().getNodeName());

        assertThat(dom, hasXPath("/gml:Polygon/gml:exterior", notNullValue(String.class)));
        assertThat(
                dom,
                hasXPath("/gml:Polygon/gml:exterior/gml:LinearRing", notNullValue(String.class)));
    }
}
