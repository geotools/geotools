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
package org.geotools.kml.bindings;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;


public class LinearRingTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(LinearRing.class, binding(KML.LinearRingType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.LinearRingType).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<LinearRing><coordinates>1,1 2,2 3,3 1,1</coordinates></LinearRing>");

        LinearRing l = (LinearRing) parse();

        assertEquals(4, l.getNumPoints());
        assertEquals(new Coordinate(1, 1), l.getCoordinateN(0));
        assertEquals(new Coordinate(3, 3), l.getCoordinateN(2));
        assertEquals(new Coordinate(1, 1), l.getCoordinateN(3));
    }
    
    public void testEncode() throws Exception {
        LinearRing l = new GeometryFactory().createLinearRing(
            new Coordinate[]{ new Coordinate(1,1), new Coordinate(2,2), 
                    new Coordinate(3,3), new Coordinate(1,1) }     
        );
        Document dom = encode( l, KML.LinearRing );
        assertNotNull(getElementByQName(dom, KML.coordinates));
    }
}
