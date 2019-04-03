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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.geotools.styling.*;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

public class SLDTest {

    @Test
    public void test() throws Exception {
        Parser parser = new Parser(new SLDConfiguration());

        StyledLayerDescriptor sld =
                (StyledLayerDescriptor)
                        parser.parse(getClass().getResourceAsStream("example-sld.xml"));

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

    @Test
    public void testValidateTransformation() throws Exception {
        Parser parser = new Parser(new SLDConfiguration());

        // if a validation error occurs it will blow up with an exception
        parser.validate(getClass().getResourceAsStream("gcontours.sld"));
    }

    @Test
    public void testValidatePerpendicularOffset() throws Exception {
        Parser parser = new Parser(new SLDConfiguration());

        // if a validation error occurs it will blow up with an exception
        parser.validate(getClass().getResourceAsStream("linePerpendicularOffset.sld"));
    }

    @Test
    public void testValidateGammaValueExpression() throws Exception {
        Parser parser = new Parser(new SLDConfiguration());

        // if a validation error occurs it will blow up with an exception
        parser.validate(getClass().getResourceAsStream("gammaValueExpression.sld"));
    }

    // Currently disabled, as this does not match either the current or previous behavior
    @Ignore
    // GEOT-5726 - test consistency with org.geotools.styling.SLDParser
    @Test
    public void testParserConsistency()
            throws ParserConfigurationException, SAXException, IOException {
        String sldText =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\">\n"
                        + "  <sld:Name>emptytag</sld:Name>\n"
                        + "  <sld:UserLayer>\n"
                        + "    <sld:UserStyle>\n"
                        + "      <sld:Name>Empty tag test</sld:Name>\n"
                        + "      <sld:FeatureTypeStyle>\n"
                        + "        <sld:Rule>\n"
                        + "          <sld:TextSymbolizer uom=\"http://www.opengeospatial.org/se/units/pixel\">\n"
                        + "            <sld:Label>\n"
                        + "              <ogc:PropertyName>NAME</ogc:PropertyName>\n"
                        + "            </sld:Label>\n"
                        + "            <sld:Font>\n"
                        + "              <sld:CssParameter name=\"font-family\"/>\n"
                        + "              <sld:CssParameter name=\"font-size\">14</sld:CssParameter>\n"
                        + "              <sld:CssParameter name=\"font-style\">normal</sld:CssParameter>\n"
                        + "              <sld:CssParameter name=\"font-weight\">normal</sld:CssParameter>\n"
                        + "            </sld:Font>\n"
                        + "          </sld:TextSymbolizer>\n"
                        + "        </sld:Rule>\n"
                        + "      </sld:FeatureTypeStyle>\n"
                        + "    </sld:UserStyle>\n"
                        + "  </sld:UserLayer>\n"
                        + "</sld:StyledLayerDescriptor>\n";

        // GTXML

        Configuration config = new SLDConfiguration();
        Parser parser = new Parser(config);
        StyledLayerDescriptor sld =
                (StyledLayerDescriptor) parser.parse(IOUtils.toInputStream(sldText));

        Style s = ((UserLayer) (sld.layers().get(0))).getUserStyles()[0];
        TextSymbolizer symbolizer =
                (TextSymbolizer) (s.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0));
        Font font = symbolizer.fonts().get(0);
        assertTrue(font.getFamily().size() > 0);

        assertEquals("", font.getFamily().get(0).toString());
    }
}
