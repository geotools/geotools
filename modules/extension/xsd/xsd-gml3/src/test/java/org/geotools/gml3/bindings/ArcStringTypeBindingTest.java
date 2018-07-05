/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.locationtech.jts.geom.LineString;
import org.w3c.dom.Document;

public class ArcStringTypeBindingTest extends GML3TestSupport {

    @Override
    protected boolean enableExtendedArcSurfaceSupport() {
        return true;
    }

    public void testParse() throws Exception {
        GML3MockData.arcStringWithPosList(document, document);
        LineString lineString = (LineString) parse();
        assertTrue(lineString instanceof CircularString);
        CircularString cs = (CircularString) lineString;

        double[] controlPoints = cs.getControlPoints();
        assertEquals(1.0, controlPoints[0], 0d);
        assertEquals(1.0, controlPoints[1], 0d);
        assertEquals(2.0, controlPoints[2], 0d);
        assertEquals(2.0, controlPoints[3], 0d);
        assertEquals(3.0, controlPoints[4], 0d);
        assertEquals(1.0, controlPoints[5], 0d);
        assertEquals(5, controlPoints[6], 0d);
        assertEquals(5, controlPoints[7], 0d);
        assertEquals(7, controlPoints[8], 0d);
        assertEquals(3, controlPoints[9], 0d);
    }

    public void testEncodeSimple() throws Exception {
        LineString curve =
                new CurvedGeometryFactory(0.1)
                        .createCurvedGeometry(
                                new LiteCoordinateSequence(
                                        new double[] {1, 1, 2, 2, 3, 1, 5, 5, 7, 3}));
        Document dom = encode(curve, GML.curveProperty);
        // print(dom);
        XpathEngine xpath = XMLUnit.newXpathEngine();
        String basePath = "/gml:curveProperty/gml:Curve/gml:segments/gml:ArcString";
        assertEquals(
                1,
                xpath.getMatchingNodes(basePath + "[@interpolation='circularArc3Points']", dom)
                        .getLength());
        assertEquals("1 1 2 2 3 1 5 5 7 3", xpath.evaluate(basePath + "/gml:posList", dom));
    }

    public void testEncodeCompound() throws Exception {
        // create a compound curve
        CurvedGeometryFactory factory = new CurvedGeometryFactory(0.1);
        LineString curve =
                factory.createCurvedGeometry(
                        new LiteCoordinateSequence(1, 1, 2, 2, 3, 1, 5, 5, 7, 3));
        LineString straight = factory.createLineString(new LiteCoordinateSequence(7, 3, 10, 15));
        LineString compound = factory.createCurvedGeometry(curve, straight);

        // encode
        Document dom = encode(compound, GML.curveProperty);
        // print(dom);
        XpathEngine xpath = XMLUnit.newXpathEngine();

        // the curve portion
        String basePath1 = "/gml:curveProperty/gml:Curve/gml:segments/gml:ArcString";
        assertEquals(
                1,
                xpath.getMatchingNodes(basePath1 + "[@interpolation='circularArc3Points']", dom)
                        .getLength());
        assertEquals("1 1 2 2 3 1 5 5 7 3", xpath.evaluate(basePath1 + "/gml:posList", dom));

        // the straight portion
        String basePath2 = "/gml:curveProperty/gml:Curve/gml:segments/gml:LineStringSegment";
        assertEquals(
                1,
                xpath.getMatchingNodes(basePath2 + "[@interpolation='linear']", dom).getLength());
        assertEquals("7 3 10 15", xpath.evaluate(basePath2 + "/gml:posList", dom));
    }
}
