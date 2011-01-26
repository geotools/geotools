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

import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.xml.Binding;


public class StyleTypeBindingTest extends KMLTestSupport {
    public void testType() {
        assertEquals(FeatureTypeStyle.class, binding(KML.StyleType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.StyleType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<Style>" + "<LineStyle/>" + "<PolyStyle/>" + "<LabelStyle/>" + "</Style>";

        buildDocument(xml);

        FeatureTypeStyle style = (FeatureTypeStyle) parse();
        Symbolizer[] syms = style.getRules()[0].getSymbolizers();

        assertEquals(3, syms.length);
        assertTrue(syms[0] instanceof LineSymbolizer);
        assertTrue(syms[1] instanceof PolygonSymbolizer);
        assertTrue(syms[2] instanceof TextSymbolizer);
    }
}
