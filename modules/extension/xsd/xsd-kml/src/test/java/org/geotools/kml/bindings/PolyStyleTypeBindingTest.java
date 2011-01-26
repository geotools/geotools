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
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.xml.Binding;


public class PolyStyleTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(PolygonSymbolizer.class, binding(KML.PolyStyleType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.AFTER, binding(KML.PolyStyleType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<PolyStyle>" + "<color>ffff0000</color>" + "<outline>1</outline>"
            + "<fill>1</fill>" + "</PolyStyle>";

        buildDocument(xml);

        PolygonSymbolizer poly = (PolygonSymbolizer) parse();
        assertEquals(Color.RED, SLD.color(poly.getFill()));
        assertEquals(1, SLD.width(poly.getStroke()));

        xml = "<PolyStyle>" + "<color>ffff0000</color>" + "<outline>0</outline>" + "<fill>0</fill>"
            + "</PolyStyle>";

        buildDocument(xml);
        poly = (PolygonSymbolizer) parse();
        assertNull(poly.getFill());
        assertNull(poly.getStroke());

        xml = "<PolyStyle>" + "</PolyStyle>";

        buildDocument(xml);
        poly = (PolygonSymbolizer) parse();
        assertEquals(Color.WHITE, SLD.color(poly.getFill()));
        assertEquals(1, SLD.width(poly.getStroke()));
    }
}
