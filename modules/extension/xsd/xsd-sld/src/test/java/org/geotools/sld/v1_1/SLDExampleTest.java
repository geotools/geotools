/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.sld.v1_1;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

public class SLDExampleTest {

    @Test
    public void testParseSLD() throws Exception {
        /*
        <StyledLayerDescriptor version="1.1.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld"
        xmlns:ogc="http://www.opengis.net/ogc" xmlns:se="http://www.opengis.net/se" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <NamedLayer>
                        <se:Name>OCEANSEA_1M:Foundation</se:Name>
                        <UserStyle>
                                <se:Name>GEOSYM</se:Name>
                                <IsDefault>1</IsDefault>
                                <se:FeatureTypeStyle>
                                        <se:FeatureTypeName>Foundation</se:FeatureTypeName>
                                        <se:Rule>
                                                <se:Name>main</se:Name>
                                                <se:PolygonSymbolizer uom="http://www.opengis.net/sld/units/pixel">
                                                        <se:Name>MySymbol</se:Name>
                                                        <se:Description>
                                                                <se:Title>Example Symbol</se:Title>
                                                                <se:Abstract>This is just a simple example.</se:Abstract>
                                                        </se:Description>
                                                        <se:Geometry>
                                                                <ogc:PropertyName>GEOMETRY</ogc:PropertyName>
                                                        </se:Geometry>
                                                        <se:Fill>
                                                                <se:SvgParameter name="fill">#96C3F5</se:SvgParameter>
                                                        </se:Fill>
                                                </se:PolygonSymbolizer>
                                        </se:Rule>
                                </se:FeatureTypeStyle>
                        </UserStyle>
                </NamedLayer>
        </StyledLayerDescriptor>*/

        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse("example-sld.xml");
        Assert.assertEquals(1, sld.getStyledLayers().length);

        NamedLayer l = (NamedLayer) sld.getStyledLayers()[0];
        Assert.assertEquals("OCEANSEA_1M:Foundation", l.getName());

        Assert.assertEquals(1, l.getStyles().length);
        Style s = l.getStyles()[0];
        Assert.assertEquals("GEOSYM", s.getName());
        assertTrue(s.isDefault());

        Assert.assertEquals(1, s.featureTypeStyles().size());
        FeatureTypeStyle fts = s.featureTypeStyles().get(0);

        Assert.assertEquals(
                "Foundation", fts.featureTypeNames().iterator().next().getLocalPart());
        Assert.assertEquals(1, fts.rules().size());

        Rule r = fts.rules().get(0);
        Assert.assertEquals("main", r.getName());
        Assert.assertEquals(1, r.symbolizers().size());

        assertTrue(r.symbolizers().get(0) instanceof PolygonSymbolizer);
    }

    @Test
    public void testParseGraphicFill() throws Exception {
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse("../graphicFill.xml");
        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        PolygonSymbolizer ps = (PolygonSymbolizer) layer.getStyles()[0]
                .featureTypeStyles()
                .get(0)
                .rules()
                .get(0)
                .symbolizers()
                .get(0);
        Graphic graphicFill = ps.getFill().getGraphicFill();
        Assert.assertNotNull(graphicFill);
        ExternalGraphic eg = (ExternalGraphic) graphicFill.graphicalSymbols().get(0);
        Assert.assertEquals(
                new URI("http://maps.google.com/mapfiles/kml/pal2/icon4.png"),
                eg.getOnlineResource().getLinkage());
    }

    Object parse(String filename) throws Exception {
        SLDConfiguration sld = new SLDConfiguration();
        try (InputStream location = getClass().getResourceAsStream(filename)) {
            return new Parser(sld).parse(location);
        }
    }

    List validate(String filename) throws Exception {
        SLDConfiguration sld = new SLDConfiguration();
        try (InputStream location = getClass().getResourceAsStream(filename)) {
            Parser p = new Parser(sld);
            p.validate(location);
            return p.getValidationErrors();
        }
    }

    @Test
    public void testParseSldWithExternalEntities() throws Exception {
        // this SLD file references as external entity a file on the local filesystem
        String file = "../example-textsymbolizer-externalentities.xml";

        Parser parser = new Parser(new SLDConfiguration());

        try (InputStream location = getClass().getResourceAsStream(file)) {
            parser.parse(location);
            Assert.fail("parsing should fail with a FileNotFoundException because the parser try to "
                    + "access a file that doesn't exist");
        } catch (FileNotFoundException e) {
        }

        // set an entity resolver to prevent access to the local file system
        parser.setEntityResolver(new EntityResolver2() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                return new InputSource();
            }

            @Override
            public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
                    throws SAXException, IOException {
                return new InputSource();
            }
        });

        try (InputStream location = getClass().getResourceAsStream(file)) {
            parser.parse(location);
            Assert.fail(
                    "parsing should fail with a MalformedURLException because the EntityResolver blocked entity resolution");
        } catch (MalformedURLException e) {
        }
    }

    @Test
    public void testValidateGammaValueExpression() throws Exception {
        String file = "example-sld-gamma-value.xml";
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse(file);

        // basic drill down
        Assert.assertEquals(1, sld.getStyledLayers().length);
        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        Assert.assertEquals(1, layer.getStyles().length);
        Style style = layer.getStyles()[0];
        Assert.assertEquals(1, style.featureTypeStyles().size());
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        Assert.assertEquals(1, fts.rules().size());
        Rule rule = fts.rules().get(0);
        Assert.assertEquals(1, rule.symbolizers().size());

        // every symbolizer has the vendor option
        RasterSymbolizer raster = (RasterSymbolizer) rule.symbolizers().get(0);
        ContrastEnhancement ce = raster.getContrastEnhancement();
        Assert.assertNotNull(ce);
        Expression gammaExp = ce.getGammaValue();
        Assert.assertNotNull(gammaExp);
        Double gamma = gammaExp.evaluate(null, Double.class);
        Assert.assertEquals(1.5, gamma, 0d);
    }

    @Test
    public void testParseValidateVendorOptions() throws Exception {
        String file = "example-sld-vendor-option.xml";
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse(file);

        // basic drill down
        Assert.assertEquals(1, sld.getStyledLayers().length);
        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        Assert.assertEquals(1, layer.getStyles().length);
        Style style = layer.getStyles()[0];
        Assert.assertEquals(1, style.featureTypeStyles().size());
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        Assert.assertEquals(1, fts.rules().size());
        Rule rule = fts.rules().get(0);
        Assert.assertEquals(4, rule.symbolizers().size());

        // every symbolizer has the vendor option
        PolygonSymbolizer poly = (PolygonSymbolizer) rule.symbolizers().get(0);
        Assert.assertEquals(1, poly.getOptions().size());
        Assert.assertEquals("true", poly.getOptions().get("labelObstacle"));

        LineSymbolizer line = (LineSymbolizer) rule.symbolizers().get(1);
        Assert.assertEquals(1, line.getOptions().size());
        Assert.assertEquals("true", line.getOptions().get("labelObstacle"));

        PointSymbolizer point = (PointSymbolizer) rule.symbolizers().get(2);
        Assert.assertEquals(1, point.getOptions().size());
        Assert.assertEquals("true", point.getOptions().get("labelObstacle"));

        TextSymbolizer text = (TextSymbolizer) rule.symbolizers().get(3);
        Assert.assertEquals(1, text.getOptions().size());
        Assert.assertEquals("100", text.getOptions().get("repeat"));

        // check it passes validation
        List errors = validate(file);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testParseBackgroundSolid() throws Exception {
        String file = "../backgroundSolidSLD11.xml";
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse(file);
        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        Style style = layer.getStyles()[0];
        Fill fill = style.getBackground();
        Assert.assertNotNull(fill);
        Assert.assertEquals(Color.RED, fill.getColor().evaluate(null, Color.class));
        Assert.assertEquals(1, fill.getOpacity().evaluate(null, Double.class), 1);
    }

    @Test
    public void testParseBackgroundGraphic() throws Exception {
        String file = "../backgroundGraphicSLD11.xml";
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse(file);
        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        Style style = layer.getStyles()[0];
        Fill fill = style.getBackground();
        Assert.assertNotNull(fill);
        Graphic graphic = fill.getGraphicFill();
        Assert.assertNotNull(graphic);
        GraphicalSymbol firstSymbol = graphic.graphicalSymbols().get(0);
        assertTrue(firstSymbol instanceof Mark);
        Assert.assertEquals("square", ((Mark) firstSymbol).getWellKnownName().evaluate(null, String.class));
    }
}
