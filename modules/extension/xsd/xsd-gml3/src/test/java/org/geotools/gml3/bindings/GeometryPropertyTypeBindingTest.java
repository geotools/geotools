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
package org.geotools.gml3.bindings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath;

import javax.xml.transform.Source;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xsd.Configuration;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.WKTReader;
import org.w3c.dom.Document;
import org.xmlunit.builder.Input;

public class GeometryPropertyTypeBindingTest extends GML3TestSupport {

    @Override
    protected boolean enableExtendedArcSurfaceSupport() {
        return true;
    }

    @Override
    protected Configuration createConfiguration() {
        GMLConfiguration configuration = new GMLConfiguration(enableExtendedArcSurfaceSupport());
        // configure a small number of decimals for testing purposes
        configuration.setNumDecimals(2);
        return configuration;
    }

    @Test
    public void testEncode() throws Exception {
        Document dom = encode(GML3MockData.point(), GML.geometryMember);
        assertEquals(1, dom.getElementsByTagNameNS(GML.NAMESPACE, "Point").getLength());
    }

    @Test
    public void testEncodeCurve() throws Exception {
        LineString curve =
                new CurvedGeometryFactory(0.1)
                        .createCurvedGeometry(
                                new LiteCoordinateSequence(
                                        new double[] {1, 1, 2, 2, 3, 1, 5, 5, 7, 3}));

        Document dom = encode(curve, GML.geometryMember);
        // print(dom);
        Source actual = Input.fromDocument(dom).build();
        String basePath = "/gml:geometryMember/gml:Curve/gml:segments/gml:ArcString";
        assertThat(
                actual,
                hasXPath(
                                "count(" + basePath + "[@interpolation='circularArc3Points'])",
                                equalTo("1"))
                        .withNamespaceContext(NAMESPACES));
        assertThat(
                actual,
                hasXPath(basePath + "/gml:posList", equalTo("1 1 2 2 3 1 5 5 7 3"))
                        .withNamespaceContext(NAMESPACES));
    }

    @Test
    public void testEncodePointWithDecimals() throws Exception {
        Geometry geometry = new WKTReader().read("POINT(1.234 5.678)");

        Document dom = encode(geometry, GML.geometryMember);
        // print(dom);
        Source actual = Input.fromDocument(dom).build();
        assertThat(
                actual,
                hasXPath("/gml:geometryMember/gml:Point/gml:pos", equalTo("1.23 5.68"))
                        .withNamespaceContext(NAMESPACES));
    }
}
