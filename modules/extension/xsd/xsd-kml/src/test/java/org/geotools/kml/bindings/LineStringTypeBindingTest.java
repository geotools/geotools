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
import com.vividsolutions.jts.geom.LineString;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class LineStringTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(LineString.class, binding(KML.LineStringType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.LineStringType).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<LineString><coordinates>1,1 2,2 3,3</coordinates></LineString>");

        LineString l = (LineString) parse();

        assertEquals(3, l.getNumPoints());
        assertEquals(new Coordinate(1, 1), l.getCoordinateN(0));
        assertEquals(new Coordinate(3, 3), l.getCoordinateN(2));
    }
    
    public void testEncode() throws Exception {
        LineString l = new GeometryFactory().createLineString(
            new Coordinate[]{ new Coordinate(1,1), new Coordinate(2,2), new Coordinate(3,3)}
        );
        
        Document dom = encode( l, KML.LineString );
        Element coordinates = getElementByQName( dom, KML.coordinates );
        assertNotNull( coordinates );
    }
}
