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
package org.geotools.gml3.bindings.ext;

import org.geotools.gml3.GML;
import org.geotools.gml3.GML3TestSupport;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;


public class SurfacePropertyTypeBindingTest extends GML3TestSupport {
    
    protected boolean enableExtendedArcSurfaceSupport() {
        return true;
    };
    
    public void testEncode() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Polygon polygon = gf.createPolygon(gf.createLinearRing(
                    new Coordinate[] {
                        new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2),
                        new Coordinate(0, 0)
                    }), null);

        MultiPolygon multiPolygon = gf.createMultiPolygon(new Polygon[] {polygon});

        Document dom = encode(multiPolygon, GML.surfaceProperty);

        assertEquals(1, dom.getElementsByTagName("gml:Polygon").getLength());
        assertEquals(1, dom.getElementsByTagName("gml:exterior").getLength());
    }
}
