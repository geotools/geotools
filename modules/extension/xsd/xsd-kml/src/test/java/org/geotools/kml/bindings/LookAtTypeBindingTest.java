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
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;


public class LookAtTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(Coordinate.class, binding(KML.LookAtType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.LookAtType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<LookAt>" + "<longitude>1</longitude>" + "<latitude>2</latitude>"
            + "<altitude>3</altitude>" + "</LookAt>";
        buildDocument(xml);

        Coordinate c = (Coordinate) parse();
        assertEquals(1d, c.y, 0.1);
        assertEquals(2d, c.x, 0.1);
        assertEquals(3d, c.z, 0.1);

        xml = "<LookAt/>";
        buildDocument(xml);
        c = (Coordinate) parse();
        assertEquals(0d, c.y, 0.1);
        assertEquals(0d, c.x, 0.1);
        assertEquals(0d, c.z, 0.1);
    }
}
