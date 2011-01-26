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

import com.vividsolutions.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;


public class PlacemarkTypeBindingTest extends KMLTestSupport {
    public void testType() throws Exception {
        assertEquals(SimpleFeature.class, binding(KML.PlacemarkType).getType());
    }

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.AFTER, binding(KML.PlacemarkType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<Placemark>" + "<name>name</name>" + "<description>description</description>"
            + "<Point>" + "<coordinates>1,2</coordinates>" + "</Point>" + "</Placemark>";
        buildDocument(xml);

        SimpleFeature placemark = (SimpleFeature) parse();
        assertEquals("name", placemark.getAttribute("name"));
        assertEquals("description", placemark.getAttribute("description"));
        assertNotNull(placemark.getAttribute("Geometry"));
        assertTrue(placemark.getAttribute("Geometry") instanceof Point);

        Point p = (Point) placemark.getAttribute("Geometry");
        assertEquals(1d, p.getX(), 0.1);
        assertEquals(2d, p.getY(), 0.1);
    }
}
