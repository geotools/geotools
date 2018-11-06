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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import junit.framework.TestCase;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.geotools.xsd.Parser;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ExternalGraphic;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

public class SLDExampleTest extends TestCase {

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
        assertEquals(1, sld.getStyledLayers().length);

        NamedLayer l = (NamedLayer) sld.getStyledLayers()[0];
        assertEquals("OCEANSEA_1M:Foundation", l.getName());

        assertEquals(1, l.getStyles().length);
        Style s = l.getStyles()[0];
        assertEquals("GEOSYM", s.getName());
        assertTrue(s.isDefault());

        assertEquals(1, s.getFeatureTypeStyles().length);
        FeatureTypeStyle fts = s.getFeatureTypeStyles()[0];

        assertEquals("Foundation", fts.getFeatureTypeName());
        assertEquals(1, fts.rules().size());

        Rule r = fts.rules().get(0);
        assertEquals("main", r.getName());
        assertEquals(1, r.symbolizers().size());

        PolygonSymbolizer sym = (PolygonSymbolizer) r.symbolizers().get(0);
    }

    public void testParseGraphicFill() throws Exception {
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse("../graphicFill.xml");
        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        PolygonSymbolizer ps =
                (PolygonSymbolizer)
                        layer.getStyles()[0]
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);
        Graphic graphicFill = ps.getFill().getGraphicFill();
        assertNotNull(graphicFill);
        ExternalGraphic eg = (ExternalGraphic) graphicFill.graphicalSymbols().get(0);
        assertEquals(
                new URI("http://maps.google.com/mapfiles/kml/pal2/icon4.png"),
                eg.getOnlineResource().getLinkage());
    }

    Object parse(String filename) throws Exception {
        SLDConfiguration sld = new SLDConfiguration();
        InputStream location = getClass().getResourceAsStream(filename);
        return new Parser(sld).parse(location);
    }

    List validate(String filename) throws Exception {
        SLDConfiguration sld = new SLDConfiguration();
        InputStream location = getClass().getResourceAsStream(filename);
        Parser p = new Parser(sld);
        p.validate(location);
        return p.getValidationErrors();
    }

    public void testParseSldWithExternalEntities() throws Exception {
        // this SLD file references as external entity a file on the local filesystem
        String file = "../example-textsymbolizer-externalentities.xml";

        Parser parser = new Parser(new SLDConfiguration());

        try {
            InputStream location = getClass().getResourceAsStream(file);
            parser.parse(location);
            fail(
                    "parsing should fail with a FileNotFoundException because the parser try to access a file that doesn't exist");
        } catch (FileNotFoundException e) {
        }

        // set an entity resolver to prevent access to the local file system
        parser.setEntityResolver(
                new EntityResolver2() {
                    @Override
                    public InputSource resolveEntity(String publicId, String systemId)
                            throws SAXException, IOException {
                        return new InputSource();
                    }

                    @Override
                    public InputSource getExternalSubset(String name, String baseURI)
                            throws SAXException, IOException {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public InputSource resolveEntity(
                            String name, String publicId, String baseURI, String systemId)
                            throws SAXException, IOException {
                        return new InputSource();
                    }
                });

        try {
            InputStream location = getClass().getResourceAsStream(file);
            parser.parse(location);
            fail(
                    "parsing should fail with a MalformedURLException because the EntityResolver blocked entity resolution");
        } catch (MalformedURLException e) {
        }
    }

    public void testValidateGammaValueExpression() throws Exception {
        String file = "example-sld-gamma-value.xml";
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse(file);

        // basic drill down
        assertEquals(1, sld.getStyledLayers().length);
        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        assertEquals(1, layer.getStyles().length);
        Style style = layer.getStyles()[0];
        assertEquals(1, style.featureTypeStyles().size());
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        assertEquals(1, fts.rules().size());
        Rule rule = fts.rules().get(0);
        assertEquals(1, rule.symbolizers().size());

        // every symbolizer has the vendor option
        RasterSymbolizer raster = (RasterSymbolizer) rule.symbolizers().get(0);
        ContrastEnhancement ce = raster.getContrastEnhancement();
        assertNotNull(ce);
        Expression gammaExp = ce.getGammaValue();
        assertNotNull(gammaExp);
        Double gamma = gammaExp.evaluate(null, Double.class);
        assertEquals(1.5, gamma);
    }

    public void testParseValidateVendorOptions() throws Exception {
        String file = "example-sld-vendor-option.xml";
        StyledLayerDescriptor sld = (StyledLayerDescriptor) parse(file);

        // basic drill down
        assertEquals(1, sld.getStyledLayers().length);
        NamedLayer layer = (NamedLayer) sld.getStyledLayers()[0];
        assertEquals(1, layer.getStyles().length);
        Style style = layer.getStyles()[0];
        assertEquals(1, style.featureTypeStyles().size());
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        assertEquals(1, fts.rules().size());
        Rule rule = fts.rules().get(0);
        assertEquals(4, rule.symbolizers().size());

        // every symbolizer has the vendor option
        PolygonSymbolizer poly = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertEquals(1, poly.getOptions().size());
        assertEquals("true", poly.getOptions().get("labelObstacle"));

        LineSymbolizer line = (LineSymbolizer) rule.symbolizers().get(1);
        assertEquals(1, line.getOptions().size());
        assertEquals("true", line.getOptions().get("labelObstacle"));

        PointSymbolizer point = (PointSymbolizer) rule.symbolizers().get(2);
        assertEquals(1, point.getOptions().size());
        assertEquals("true", point.getOptions().get("labelObstacle"));

        TextSymbolizer text = (TextSymbolizer) rule.symbolizers().get(3);
        assertEquals(1, text.getOptions().size());
        assertEquals("100", text.getOptions().get("repeat"));

        // check it passes validation
        List errors = validate(file);
        assertEquals(0, errors.size());
    }
}
