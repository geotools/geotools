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

import java.awt.Color;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xml.Binding;


public class ColorBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(Color.class, binding(KML.color).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.color).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<color>ffff0000</color>");

        Color c = (Color) parse();
        assertEquals(Color.RED, c);
        assertEquals(255, c.getAlpha());

        buildDocument("<color>00ff0000</color>");
        c = (Color) parse();
        assertEquals(255, c.getRed());
        assertEquals(0, c.getBlue());
        assertEquals(0, c.getGreen());
        assertEquals(0, c.getAlpha());

        buildDocument("<color>ff0000</color>");
        c = (Color) parse();
        assertEquals(Color.RED, c);
        assertEquals(255, c.getAlpha());
    }
}
