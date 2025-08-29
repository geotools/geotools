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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Font;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.Mark;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.UserLayer;
import org.geotools.styling.SLD;
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
                (StyledLayerDescriptor) parser.parse(getClass().getResourceAsStream("example-sld.xml"));

        assertEquals(1, sld.getStyledLayers().length);

        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        assertEquals("OCEANSEA_1M:Foundation", layer.getName());
        assertEquals(1, layer.getStyles().length);

        Style style = layer.getStyles()[0];
        assertEquals("GEOSYM", style.getName());
        assertTrue(style.isDefault());
        assertEquals(1, style.featureTypeStyles().size());

        FeatureTypeStyle ftStyle = style.featureTypeStyles().get(0);
        assertEquals(1, ftStyle.rules().size());

        Rule rule = ftStyle.rules().get(0);
        assertEquals("main", rule.getName());
        assertEquals(1, rule.symbolizers().size());

        PolygonSymbolizer ps = (PolygonSymbolizer) rule.symbolizers().get(0);
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
    public void testParserConsistency() throws ParserConfigurationException, SAXException, IOException {
        String sldText =
                """
                <?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
                  <sld:Name>emptytag</sld:Name>
                  <sld:UserLayer>
                    <sld:UserStyle>
                      <sld:Name>Empty tag test</sld:Name>
                      <sld:FeatureTypeStyle>
                        <sld:Rule>
                          <sld:TextSymbolizer uom="http://www.opengeospatial.org/se/units/pixel">
                            <sld:Label>
                              <ogc:PropertyName>NAME</ogc:PropertyName>
                            </sld:Label>
                            <sld:Font>
                              <sld:CssParameter name="font-family"/>
                              <sld:CssParameter name="font-size">14</sld:CssParameter>
                              <sld:CssParameter name="font-style">normal</sld:CssParameter>
                              <sld:CssParameter name="font-weight">normal</sld:CssParameter>
                            </sld:Font>
                          </sld:TextSymbolizer>
                        </sld:Rule>
                      </sld:FeatureTypeStyle>
                    </sld:UserStyle>
                  </sld:UserLayer>
                </sld:StyledLayerDescriptor>
                """;

        // GTXML

        Configuration config = new SLDConfiguration();
        Parser parser = new Parser(config);
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parser.parse(IOUtils.toInputStream(sldText, "UTF-8"));

        Style s = ((UserLayer) sld.layers().get(0)).getUserStyles()[0];
        TextSymbolizer symbolizer = (TextSymbolizer)
                s.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        Font font = symbolizer.fonts().get(0);
        assertFalse(font.getFamily().isEmpty());

        assertEquals("", font.getFamily().get(0).toString());
    }

    @Test
    public void testBackgroundSolid() throws ParserConfigurationException, SAXException, IOException {
        Parser parser = new Parser(new SLDConfiguration());

        // if a validation error occurs it will blow up with an exception
        parser.validate(getClass().getResourceAsStream("backgroundSolid.sld"));

        StyledLayerDescriptor sld =
                (StyledLayerDescriptor) parser.parse(getClass().getResourceAsStream("backgroundSolid.sld"));
        Style style = ((NamedLayer) sld.getStyledLayers()[0]).getStyles()[0];
        Fill fill = style.getBackground();
        assertNotNull(fill);
        assertEquals(Color.RED, fill.getColor().evaluate(null, Color.class));
        assertEquals(1, fill.getOpacity().evaluate(null, Double.class), 1);
    }

    @Test
    public void testBackgroundGraphicFill() throws ParserConfigurationException, SAXException, IOException {
        Parser parser = new Parser(new SLDConfiguration());

        // if a validation error occurs it will blow up with an exception
        parser.validate(getClass().getResourceAsStream("backgroundGraphicFill.sld"));

        StyledLayerDescriptor sld =
                (StyledLayerDescriptor) parser.parse(getClass().getResourceAsStream("backgroundGraphicFill.sld"));
        Style style = ((NamedLayer) sld.getStyledLayers()[0]).getStyles()[0];
        Fill fill = style.getBackground();
        assertNotNull(fill);
        Graphic graphic = fill.getGraphicFill();
        assertNotNull(graphic);
        GraphicalSymbol firstSymbol = graphic.graphicalSymbols().get(0);
        assertTrue(firstSymbol instanceof Mark);
        assertEquals("square", ((Mark) firstSymbol).getWellKnownName().evaluate(null, String.class));
    }
}
