/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.styling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.Assert;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.ResourceLocator;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.junit.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.style.ContrastMethod;
import org.opengis.style.GraphicalSymbol;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SLDParserTest {

    public static String SLD =
            "<StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" version=\"1.0.0\">"
                    + " <NamedLayer>"
                    + "  <Name>layer</Name>"
                    + "  <UserStyle>"
                    + "   <Name>style</Name>"
                    + "   <FeatureTypeStyle>"
                    + "    <Rule>"
                    + "     <PolygonSymbolizer>"
                    + "      <Fill>"
                    + "       <CssParameter name=\"fill\">#FF0000</CssParameter>"
                    + "      </Fill>"
                    + "     </PolygonSymbolizer>"
                    + "    </Rule>"
                    + "   </FeatureTypeStyle>"
                    + "  </UserStyle>"
                    + " </NamedLayer>"
                    + "</StyledLayerDescriptor>";

    public static String LocalizedSLD =
            "<StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" version=\"1.0.0\">"
                    + " <NamedLayer>"
                    + "  <Name>layer</Name>"
                    + "  <UserStyle>"
                    + "   <Name>style</Name>"
                    + "   <FeatureTypeStyle>"
                    + "    <Rule>"
                    + "     <Title>sldtitle"
                    + "     <Localized lang=\"en\">english</Localized>"
                    + "     <Localized lang=\"it\">italian</Localized>"
                    + "     <Localized lang=\"fr\">french</Localized>"
                    + "     <Localized lang=\"fr_CA\">canada french</Localized>"
                    + "     </Title>"
                    + "     <Abstract>sld abstract"
                    + "     <Localized lang=\"en\">english abstract</Localized>"
                    + "     <Localized lang=\"it\">italian abstract</Localized>"
                    + "     <Localized lang=\"fr\">french abstract</Localized>"
                    + "     </Abstract>"
                    + "     <PolygonSymbolizer>"
                    + "      <Fill>"
                    + "       <CssParameter name=\"fill\">#FF0000</CssParameter>"
                    + "      </Fill>"
                    + "     </PolygonSymbolizer>"
                    + "    </Rule>"
                    + "   </FeatureTypeStyle>"
                    + "  </UserStyle>"
                    + " </NamedLayer>"
                    + "</StyledLayerDescriptor>";

    public static String EmptyTitleSLD =
            "<StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" version=\"1.0.0\">"
                    + " <NamedLayer>"
                    + "  <Name>layer</Name>"
                    + "  <UserStyle>"
                    + "   <Name>style</Name>"
                    + "   <FeatureTypeStyle>"
                    + "    <Rule>"
                    + "     <Title></Title>"
                    + "     <PolygonSymbolizer>"
                    + "      <Fill>"
                    + "       <CssParameter name=\"fill\">#FF0000</CssParameter>"
                    + "      </Fill>"
                    + "     </PolygonSymbolizer>"
                    + "    </Rule>"
                    + "   </FeatureTypeStyle>"
                    + "  </UserStyle>"
                    + " </NamedLayer>"
                    + "</StyledLayerDescriptor>";

    public static String EmptyAbstractSLD =
            "<StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" version=\"1.0.0\">"
                    + " <NamedLayer>"
                    + "  <Name>layer</Name>"
                    + "  <UserStyle>"
                    + "   <Name>style</Name>"
                    + "   <FeatureTypeStyle>"
                    + "    <Rule>"
                    + "     <Abstract></Abstract>"
                    + "     <PolygonSymbolizer>"
                    + "      <Fill>"
                    + "       <CssParameter name=\"fill\">#FF0000</CssParameter>"
                    + "      </Fill>"
                    + "     </PolygonSymbolizer>"
                    + "    </Rule>"
                    + "   </FeatureTypeStyle>"
                    + "  </UserStyle>"
                    + " </NamedLayer>"
                    + "</StyledLayerDescriptor>";

    static String SLD_DEFAULT_POINT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<StyledLayerDescriptor version=\"1.0.0\" \n"
                    + "        xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" \n"
                    + "        xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" \n"
                    + "        xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n"
                    + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                    + "    <UserStyle>\n"
                    + "        <Name>Default Styler</Name>\n"
                    + "        <Title>Default Styler</Title>\n"
                    + "        <Abstract></Abstract>\n"
                    + "        <FeatureTypeStyle>\n"
                    + "            <FeatureTypeName>Feature</FeatureTypeName>\n"
                    + "            <Rule>\n"
                    + "                <PointSymbolizer>\n"
                    + "                    <Graphic>\n"
                    + "                    </Graphic>\n"
                    + "                </PointSymbolizer>\n"
                    + "            </Rule>\n"
                    + "        </FeatureTypeStyle>\n"
                    + "    </UserStyle>\n"
                    + "</StyledLayerDescriptor>";

    static String SLD_MARK_FILE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<StyledLayerDescriptor version=\"1.0.0\" \n"
                    + "        xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" \n"
                    + "        xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" \n"
                    + "        xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n"
                    + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                    + "    <UserStyle>\n"
                    + "        <Name>Default Styler</Name>\n"
                    + "        <Title>Default Styler</Title>\n"
                    + "        <Abstract></Abstract>\n"
                    + "        <FeatureTypeStyle>\n"
                    + "            <FeatureTypeName>Feature</FeatureTypeName>\n"
                    + "            <Rule>\n"
                    + "                <PointSymbolizer>\n"
                    + "                    <Graphic>\n"
                    + "                       <Mark>\n"
                    + "                          <WellKnownName>file://foo.svg</WellKnownName>"
                    + "                          <Fill/>"
                    + "                          <Stroke/>"
                    + "                       </Mark>"
                    + "                    </Graphic>\n"
                    + "                </PointSymbolizer>\n"
                    + "            </Rule>\n"
                    + "        </FeatureTypeStyle>\n"
                    + "    </UserStyle>\n"
                    + "</StyledLayerDescriptor>";

    static String SLD_EXTERNALENTITY =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<!DOCTYPE StyledLayerDescriptor [\n"
                    + "<!ENTITY c SYSTEM \"file:///this/file/is/top/secret\">\n"
                    + "]>\n"
                    + "<StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\"\n"
                    + "  xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                    + "  xsi:schemaLocation=\"http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd\">\n"
                    + "  <NamedLayer>\n"
                    + "    <Name>tiger:poi</Name>\n"
                    + "    <UserStyle>\n"
                    + "      <Name>poi</Name>\n"
                    + "      <Title>Points of interest</Title>\n"
                    + "      <Abstract>Manhattan points of interest</Abstract>\n"
                    + "      <FeatureTypeStyle>\n"
                    + "        <Rule>\n"
                    + "          <PointSymbolizer>\n"
                    + "            <Graphic>\n"
                    + "              <Mark>\n"
                    + "                <WellKnownName>circle</WellKnownName>\n"
                    + "                <Fill>\n"
                    + "                  <CssParameter name=\"fill\">#0000FF</CssParameter>\n"
                    + "                  <CssParameter name=\"fill-opacity\">1.0</CssParameter>\n"
                    + "                </Fill>\n"
                    + "              </Mark>\n"
                    + "              <Size>11</Size>\n"
                    + "            </Graphic>\n"
                    + "          </PointSymbolizer>\n"
                    + "          <PointSymbolizer>\n"
                    + "            <Graphic>\n"
                    + "              <Mark>\n"
                    + "                <WellKnownName>circle</WellKnownName>\n"
                    + "                <Fill>\n"
                    + "                  <CssParameter name=\"fill\">#ED0000</CssParameter>\n"
                    + "                  <CssParameter name=\"fill-opacity\">1.0</CssParameter>\n"
                    + "                </Fill>\n"
                    + "              </Mark>\n"
                    + "              <Size>7</Size>\n"
                    + "            </Graphic>\n"
                    + "          </PointSymbolizer>\n"
                    + "        </Rule>\n"
                    + "        <Rule>\n"
                    + "          <MaxScaleDenominator>32000</MaxScaleDenominator>\n"
                    + "          <TextSymbolizer>\n"
                    + "            <Label>&c;</Label>\n"
                    + "            <Font>\n"
                    + "              <CssParameter name=\"font-family\">Arial</CssParameter>\n"
                    + "              <CssParameter name=\"font-weight\">Bold</CssParameter>\n"
                    + "              <CssParameter name=\"font-size\">14</CssParameter>\n"
                    + "            </Font>\n"
                    + "            <LabelPlacement>\n"
                    + "              <PointPlacement>\n"
                    + "                <AnchorPoint>\n"
                    + "                  <AnchorPointX>0.5</AnchorPointX>\n"
                    + "                  <AnchorPointY>0.5</AnchorPointY>\n"
                    + "                </AnchorPoint>\n"
                    + "                <Displacement>\n"
                    + "                  <DisplacementX>0</DisplacementX>\n"
                    + "                  <DisplacementY>-15</DisplacementY>\n"
                    + "                </Displacement>\n"
                    + "              </PointPlacement>\n"
                    + "            </LabelPlacement>\n"
                    + "            <Halo>\n"
                    + "              <Radius>\n"
                    + "                <ogc:Literal>2</ogc:Literal>\n"
                    + "              </Radius>\n"
                    + "              <Fill>\n"
                    + "                <CssParameter name=\"fill\">#FFFFFF</CssParameter>\n"
                    + "              </Fill>\n"
                    + "            </Halo>\n"
                    + "            <Fill>\n"
                    + "              <CssParameter name=\"fill\">#000000</CssParameter>\n"
                    + "            </Fill>\n"
                    + "          </TextSymbolizer>\n"
                    + "        </Rule>\n"
                    + "      </FeatureTypeStyle>\n"
                    + "    </UserStyle>\n"
                    + "  </NamedLayer>\n"
                    + "</StyledLayerDescriptor>";

    static String SLD_EXTERNAL_GRAPHIC =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<StyledLayerDescriptor version=\"1.0.0\" \n"
                    + "        xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" \n"
                    + "        xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" \n"
                    + "        xmlns:xlink=\"http://www.w3.org/1999/xlink\" \n"
                    + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                    + "    <UserStyle>\n"
                    + "        <Name>Default Styler</Name>\n"
                    + "        <Title>Default Styler</Title>\n"
                    + "        <Abstract></Abstract>\n"
                    + "        <FeatureTypeStyle>\n"
                    + "            <FeatureTypeName>Feature</FeatureTypeName>\n"
                    + "            <Rule>\n"
                    + "                <PointSymbolizer>\n"
                    + "                    <Graphic>\n"
                    + "                        <ExternalGraphic>\n"
                    + "                            <OnlineResource xlink:type=\"simple\"\n"
                    + "                                xlink:href=\"test-data/blob.gif\">\n"
                    + "                            </OnlineResource>\n"
                    + "                            <Format>image/png</Format>\n"
                    + "                        </ExternalGraphic>\n"
                    + "                        <Size>20</Size>\n"
                    + "                    </Graphic>\n"
                    + "                </PointSymbolizer>\n"
                    + "            </Rule>\n"
                    + "        </FeatureTypeStyle>\n"
                    + "    </UserStyle>\n"
                    + "</StyledLayerDescriptor>";

    static String color = "00AA00";

    static String formattedCssStrokeParameter =
            "<Stroke>"
                    + "\n\t<CssParameter name=\"stroke\">#"
                    + "\n\t\t<ogc:Function name=\"env\">"
                    + "\n\t\t\t<ogc:Literal>stroke_color</ogc:Literal>"
                    + "\n\t\t\t<ogc:Literal>"
                    + color
                    + "</ogc:Literal>"
                    + "\n\t\t</ogc:Function>"
                    + "\n\t</CssParameter>"
                    + "</Stroke>";

    static String contrastEnhance =
            " <ContrastEnhancement> "
                    + "\n\t<Normalize> "
                    + "\n\t<VendorOption name=\"Algorithm\">ClipToMinimumMaximum</VendorOption> "
                    + "\n\t<VendorOption name=\"minValue\">1</VendorOption>"
                    + "\n\t<VendorOption name=\"maxValue\">27.0</VendorOption>"
                    + "\n\t</Normalize>"
                    + "\n\t</ContrastEnhancement>";

    static String contrastEnhanceOther =
            " <ContrastEnhancement> " + "\n\t<METHOD/> " + "\n\t</ContrastEnhancement>";

    static String contrastEnhancelogExp =
            " <ContrastEnhancement> "
                    + "\n\t<METHOD> "
                    + "\n\t<VendorOption name='correctionFactor'>0.1</VendorOption>"
                    + "\n\t<VendorOption name='normalizationFactor'>10.0</VendorOption>"
                    + "\n\t</METHOD> "
                    + "\n\t</ContrastEnhancement>";

    static String contrastEnhanceBroken =
            " <ContrastEnhancement> "
                    + "\n\t<Normalize> "
                    + "\n\t<Algorithm/> "
                    + "\n\t<Parameter >1</Parameter>"
                    + "\n\t<Parameter name=\"maxValue\"/>"
                    + "\n\t</Normalize>"
                    + "\n\t</ContrastEnhancement>";

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);

    @Test
    public void testBasic() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input(SLD));
        Style[] styles = parser.readXML();
        assertStyles(styles);
    }

    @Test
    public void testLocalizedRuleTitle() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input(LocalizedSLD));
        Style[] styles = parser.readXML();
        assertEquals(
                "sldtitle",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getTitle()
                        .toString(Locale.JAPAN));
        assertEquals(
                "english",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getTitle()
                        .toString(Locale.ENGLISH));
        assertEquals(
                "english",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getTitle()
                        .toString(Locale.US));
        assertEquals(
                "english",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getTitle()
                        .toString(Locale.US));
        assertEquals(
                "italian",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getTitle()
                        .toString(Locale.ITALY));
        assertEquals(
                "french",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getTitle()
                        .toString(Locale.FRENCH));
        assertEquals(
                "canada french",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getTitle()
                        .toString(Locale.CANADA_FRENCH));
        assertEquals(
                "sld abstract",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getAbstract()
                        .toString(Locale.JAPAN));
        assertEquals(
                "english abstract",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getAbstract()
                        .toString(Locale.ENGLISH));
        assertEquals(
                "english abstract",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getAbstract()
                        .toString(Locale.US));
        assertEquals(
                "italian abstract",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getAbstract()
                        .toString(Locale.ITALY));
        assertEquals(
                "french abstract",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getAbstract()
                        .toString(Locale.FRENCH));
        assertEquals(
                "french abstract",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getAbstract()
                        .toString(Locale.CANADA_FRENCH));
        assertStyles(styles);
    }

    @Test
    public void testEmptyTitle() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input(EmptyTitleSLD));
        Style[] styles = parser.readXML();
        assertEquals(
                "",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getTitle()
                        .toString());
    }

    @Test
    public void testEmptyAbstract() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input(EmptyAbstractSLD));
        Style[] styles = parser.readXML();
        assertEquals(
                "",
                styles[0]
                        .getFeatureTypeStyles()[0]
                        .getRules()[0]
                        .getDescription()
                        .getAbstract()
                        .toString());
    }

    @Test
    public void testMultipleParse() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input(SLD));
        Style[] styles = parser.readXML();
        assertStyles(styles);

        styles = parser.readDOM();
        assertStyles(styles);

        try {
            parser.readXML();
            fail("Parsing again Should have thrown exception");
        } catch (Exception e) {
        }
    }

    @Test
    public void testDefaultPoint() throws Exception {
        // fixes for GEOS-3111 broke default point symbsolizer handling
        SLDParser parser = new SLDParser(styleFactory, input(SLD_DEFAULT_POINT));
        Style[] styles = parser.readXML();

        assertEquals(1, styles.length);
        List<FeatureTypeStyle> fts = styles[0].featureTypeStyles();
        assertEquals(1, fts.size());
        List<Rule> rules = fts.get(0).rules();
        assertEquals(1, rules.size());
        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        assertEquals(1, symbolizers.size());
        PointSymbolizer ps = (PointSymbolizer) symbolizers.get(0);
        // here we would have had two instead of one
        List<GraphicalSymbol> graphicalSymbols = ps.getGraphic().graphicalSymbols();
        assertEquals(1, graphicalSymbols.size());
        Mark mark = (Mark) graphicalSymbols.get(0);
        assertEquals(mark, CommonFactoryFinder.getStyleFactory(null).createMark());
    }

    @Test
    public void testParseMarkFileReference() throws Exception {
        SLDParser parser = new SLDParser(styleFactory, input(SLD_MARK_FILE));
        parser.setOnLineResourceLocator(
                uri -> {
                    if ("file://foo.svg".equals(uri)) {
                        try {
                            return new URL("file://test/foo.svg");
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        return null;
                    }
                });
        Style[] styles = parser.readXML();

        assertEquals(1, styles.length);
        List<FeatureTypeStyle> fts = styles[0].featureTypeStyles();
        assertEquals(1, fts.size());
        List<Rule> rules = fts.get(0).rules();
        assertEquals(1, rules.size());
        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        assertEquals(1, symbolizers.size());
        PointSymbolizer ps = (PointSymbolizer) symbolizers.get(0);
        // here we would have had two instead of one
        List<GraphicalSymbol> graphicalSymbols = ps.getGraphic().graphicalSymbols();
        assertEquals(1, graphicalSymbols.size());
        Mark mark = (Mark) graphicalSymbols.get(0);
        assertEquals("file://test/foo.svg", mark.getWellKnownName().evaluate(null, String.class));
    }

    @Test
    public void testOnlineResourceLocator() throws MalformedURLException {
        // test whether the configured resolver is called and the url is used correctly
        SLDParser parser = new SLDParser(styleFactory, input(SLD_EXTERNAL_GRAPHIC));
        parser.setOnLineResourceLocator(
                new ResourceLocator() {

                    public URL locateResource(String uri) {
                        Assert.assertEquals("test-data/blob.gif", uri);
                        return getClass().getResource(uri);
                    }
                });
        Style[] styles = parser.readXML();
        assertEquals(1, styles.length);
        List<FeatureTypeStyle> fts = styles[0].featureTypeStyles();
        assertEquals(1, fts.size());
        List<Rule> rules = fts.get(0).rules();
        assertEquals(1, rules.size());
        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        assertEquals(1, symbolizers.size());
        PointSymbolizer ps = (PointSymbolizer) symbolizers.get(0);
        // here we would have had two instead of one
        List<GraphicalSymbol> graphicalSymbols = ps.getGraphic().graphicalSymbols();
        assertEquals(1, graphicalSymbols.size());
        ExternalGraphic graphic = (ExternalGraphic) graphicalSymbols.get(0);
        assertEquals(getClass().getResource("test-data/blob.gif"), graphic.getLocation());
    }

    @Test
    public void testExternalEntitiesDisabled() {
        // this SLD file references as external entity a file on the local filesystem
        SLDParser parser = new SLDParser(styleFactory, input(SLD_EXTERNALENTITY));

        // without a custom EntityResolver, the parser will try to read the entity file on the local
        // file system
        try {
            parser.readXML();
            fail("parsing should thrown an error");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof FileNotFoundException);
        }

        parser = new SLDParser(styleFactory, input(SLD_EXTERNALENTITY));
        // Set an EntityResolver implementation to prevent reading entities from the local file
        // system.
        // When resolving an XML entity, the empty InputSource returned by this resolver provokes
        // a MalformedURLException
        parser.setEntityResolver(
                new EntityResolver() {
                    @Override
                    public InputSource resolveEntity(String publicId, String systemId)
                            throws SAXException, IOException {
                        return new InputSource();
                    }
                });

        try {
            parser.readXML();
            fail("parsing should thrown an error");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof MalformedURLException);
        }

        parser = new SLDParser(styleFactory, input(SLD_EXTERNALENTITY));
        // Set another EntityResolver
        parser.setEntityResolver(
                new EntityResolver() {
                    @Override
                    public InputSource resolveEntity(String publicId, String systemId)
                            throws SAXException, IOException {
                        if ("file:///this/file/is/top/secret".equals(systemId)) {
                            return new InputSource(new StringReader("hello"));
                        } else {
                            return new InputSource();
                        }
                    }
                });

        // now parsing shouldn't throw an exception
        parser.readXML();
    }

    @Test
    public void testStrokeColorWithEnv() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document node =
                builder.parse(new StringBufferInputStream(formattedCssStrokeParameter));
        SLDParser parser = new SLDParser(styleFactory);
        Stroke stroke = parser.parseStroke(node.getDocumentElement());
        // <strConcat([#], [env([stroke_color], [" + color + "])])>";
        Assert.assertEquals("#" + color, stroke.getColor().evaluate(Color.decode("#" + color)));
    }

    @Test
    public void testContrastEnhancement() throws Exception {

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        org.w3c.dom.Document node =
                builder.parse(new ByteArrayInputStream(contrastEnhance.getBytes()));
        // First check the happy path for normalize
        SLDParser parser = new SLDParser(styleFactory);
        ContrastEnhancement ce = parser.parseContrastEnhancement(node.getDocumentElement());
        ContrastMethod method = ce.getMethod();
        assertNotNull(ce);
        assertEquals("Wrong method type", "normalize", method.name().toLowerCase());
        assertTrue("No Algotrithm set", ce.hasOption("Algorithm"));
        assertEquals(
                "wrong Algorithm",
                "ClipToMinimumMaximum",
                ce.getOption("Algorithm").evaluate(null));
        Map<String, Expression> params = ce.getOptions();

        assertEquals("Wrong number of parameters", 3, params.size());

        assertEquals("wrong param returned", "1", params.get("minValue").evaluate(null));
        assertEquals("wrong param returned", "27.0", params.get("maxValue").evaluate(null));
        // check the other methods still work
        for (String methodName :
                new String[] {"Normalize", "Logarithmic", "Exponential", "Histogram"}) {
            String target = contrastEnhanceOther.replace("METHOD", methodName);
            node = builder.parse(new ByteArrayInputStream(target.getBytes()));
            ce = parser.parseContrastEnhancement(node.getDocumentElement());
            method = ce.getMethod();
            assertNotNull(method);
            assertEquals(
                    "Wrong method returned", methodName.toLowerCase(), method.name().toLowerCase());
        }

        for (String methodName : new String[] {"Logarithmic", "Exponential"}) {
            String target = contrastEnhancelogExp.replace("METHOD", methodName);
            // System.out.println(target);
            node = builder.parse(new ByteArrayInputStream(target.getBytes()));
            ce = parser.parseContrastEnhancement(node.getDocumentElement());
            method = ce.getMethod();
            assertNotNull(method);
            assertEquals(
                    "Wrong method returned", methodName.toLowerCase(), method.name().toLowerCase());
            params = ce.getOptions();
            assertEquals("wrong number of parameters", 2, params.size());
            assertEquals(
                    "wrong param returned",
                    "10.0",
                    params.get("normalizationFactor").evaluate(null));
            assertEquals(
                    "wrong param returned", "0.1", params.get("correctionFactor").evaluate(null));
        }
        // now see what happens if we break things
        node = builder.parse(new ByteArrayInputStream(contrastEnhanceBroken.getBytes()));
        ce = parser.parseContrastEnhancement(node.getDocumentElement());
        method = ce.getMethod();
        assertNotNull(method);
        assertNull("Algorithm set when it's not defined in SLD", ce.getOption("Algorithm"));
        params = ce.getOptions();
        assertNotNull(params);

        assertTrue("Params should be empty", params.isEmpty());
    }

    void assertStyles(Style[] styles) {
        assertEquals(1, styles.length);
        assertEquals("style", styles[0].getName());
    }

    InputStream input(String sld) {
        return new ByteArrayInputStream(sld.getBytes());
    }
}
