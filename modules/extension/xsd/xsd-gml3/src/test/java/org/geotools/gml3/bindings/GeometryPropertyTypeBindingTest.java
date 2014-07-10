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

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.LineString;

/**
 * 
 * 
 * @source $URL$
 */
public class GeometryPropertyTypeBindingTest extends GML3TestSupport {

    @Override
    protected boolean enableExtendedArcSurfaceSupport() {
        return true;
    }

    public void testEncode() throws Exception {
        Document dom = encode(GML3MockData.point(), GML.geometryMember);
        assertEquals(1, dom.getElementsByTagNameNS(GML.NAMESPACE, "Point").getLength());
    }

    public void testEncodeCurve() throws Exception {
        LineString curve = new CurvedGeometryFactory(0.1)
                .createCurvedGeometry(new LiteCoordinateSequence(new double[] { 1, 1, 2, 2, 3, 1,
                        5, 5, 7, 3 }));

        Document dom = encode(curve, GML.geometryMember);
        print(dom);
        XpathEngine xpath = XMLUnit.newXpathEngine();
        String basePath = "/gml:geometryMember/gml:Curve/gml:segments/gml:ArcString";
        assertEquals(1,
                xpath.getMatchingNodes(basePath + "[@interpolation='circularArc3Points']", dom)
                        .getLength());
        assertEquals("1.0 1.0 2.0 2.0 3.0 1.0 5.0 5.0 7.0 3.0",
                xpath.evaluate(basePath + "/gml:posList", dom));
    }
}
