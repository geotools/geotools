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

import javax.xml.namespace.QName;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;


public class PolygonTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(Polygon.class, binding(KML.PolygonType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.PolygonType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<Polygon>" + "<outerBoundaryIs>"
            + "<LinearRing><coordinates>1,1 2,2 3,3 1,1</coordinates></LinearRing>"
            + "</outerBoundaryIs>" + "<innerBoundaryIs>"
            + "<LinearRing><coordinates>1,1 2,2 3,3 1,1</coordinates></LinearRing>"
            + "</innerBoundaryIs>" + "</Polygon>";

        buildDocument(xml);

        Polygon p = (Polygon) parse();

        assertEquals(1, p.getNumInteriorRing());
    }
    
    public void testEncode() throws Exception {
        Polygon p = new GeometryFactory().createPolygon(
            new GeometryFactory().createLinearRing(
                new Coordinate[]{ new Coordinate(1,1), new Coordinate(2,2), 
                    new Coordinate(3,3), new Coordinate(1,1) }
            ), new LinearRing[] {
                new GeometryFactory().createLinearRing(
                        new Coordinate[]{ new Coordinate(1,1), new Coordinate(2,2), 
                            new Coordinate(3,3), new Coordinate(1,1) }
                )
            }
        );
        Document dom = encode( p, KML.Polygon );
        
        assertNotNull( getElementByQName(dom, new QName( KML.NAMESPACE, "outerBoundaryIs") ) );
        assertNotNull( getElementByQName(dom, new QName( KML.NAMESPACE, "innerBoundaryIs") ) );
    }
}
