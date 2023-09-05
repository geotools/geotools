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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.kml.KML;
import org.geotools.kml.KMLTestSupport;
import org.geotools.xsd.Binding;
import org.junit.Test;

public class StyleTypeBindingTest extends KMLTestSupport {
    @Test
    public void testType() {
        assertEquals(FeatureTypeStyle.class, binding(KML.StyleType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(KML.StyleType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        String xml = "<Style>" + "<LineStyle/>" + "<PolyStyle/>" + "<LabelStyle/>" + "</Style>";

        buildDocument(xml);

        FeatureTypeStyle style = (FeatureTypeStyle) parse();
        List<Symbolizer> syms = style.rules().get(0).symbolizers();

        assertEquals(3, syms.size());
        assertTrue(syms.get(0) instanceof LineSymbolizer);
        assertTrue(syms.get(1) instanceof PolygonSymbolizer);
        assertTrue(syms.get(2) instanceof TextSymbolizer);
    }
}
