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
import com.vividsolutions.jts.geom.Point;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class PointTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(Point.class, binding(KML.PointType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.PointType).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<Point><coordinates>1,1</coordinates></Point>");

        Point p = (Point) parse();

        assertEquals(1d, p.getX(), 0.1);
        assertEquals(1d, p.getY(), 0.2);
    }
    
    public void testEncode() throws Exception {
        Point p = new GeometryFactory().createPoint(new Coordinate(1,1));
        Document dom = encode( p, KML.Point );
        
        Element coordinates = getElementByQName(dom, KML.coordinates );
        assertNotNull( coordinates );
    }
}
