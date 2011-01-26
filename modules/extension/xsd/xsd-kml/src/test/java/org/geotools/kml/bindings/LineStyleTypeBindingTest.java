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
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.xml.Binding;


public class LineStyleTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(LineSymbolizer.class, binding(KML.LineStyleType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.AFTER, binding(KML.LineStyleType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<LineStyle>" + "<color>ffff0000</color>" + "<width>2</width>"
            + "</LineStyle>";

        buildDocument(xml);

        LineSymbolizer line = (LineSymbolizer) parse();
        assertEquals(Color.RED, SLD.color(line));
        assertEquals(2, SLD.width(line));

        xml = "<LineStyle>" + "</LineStyle>";
        buildDocument(xml);
        line = (LineSymbolizer) parse();
        assertEquals(Color.WHITE, SLD.color(line));
        assertEquals(1, SLD.width(line));
    }
}
