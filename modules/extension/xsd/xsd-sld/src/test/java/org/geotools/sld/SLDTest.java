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
package org.geotools.sld;

import junit.framework.TestCase;
import java.awt.Color;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.xml.Parser;


public class SLDTest extends TestCase {
    public void test() throws Exception {
        Parser parser = new Parser(new SLDConfiguration());

        StyledLayerDescriptor sld = (StyledLayerDescriptor) parser.parse(getClass()
                                                                             .getResourceAsStream("example-sld.xml"));

        assertEquals(1, sld.getStyledLayers().length);

        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        assertEquals("OCEANSEA_1M:Foundation", layer.getName());
        assertEquals(1, layer.getStyles().length);

        Style style = layer.getStyles()[0];
        assertEquals("GEOSYM", style.getName());
        assertTrue(style.isDefault());
        assertEquals(1, style.getFeatureTypeStyles().length);

        FeatureTypeStyle ftStyle = (FeatureTypeStyle) style.getFeatureTypeStyles()[0];
        assertEquals(1, ftStyle.getRules().length);

        Rule rule = ftStyle.getRules()[0];
        assertEquals("main", rule.getName());
        assertEquals(1, rule.getSymbolizers().length);

        PolygonSymbolizer ps = (PolygonSymbolizer) rule.getSymbolizers()[0];
        assertEquals("GEOMETRY", ps.getGeometryPropertyName());

        Color color = SLD.color(ps.getFill().getColor());
        assertEquals(Integer.parseInt("96", 16), color.getRed());
        assertEquals(Integer.parseInt("C3", 16), color.getGreen());
        assertEquals(Integer.parseInt("F5", 16), color.getBlue());
    }
}
