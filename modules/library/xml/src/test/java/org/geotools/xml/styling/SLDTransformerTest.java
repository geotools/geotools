/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.custommonkey.xmlunit.XMLUnit.buildTestDocument;
import static org.custommonkey.xmlunit.XMLUnit.setXpathNamespaceContext;
import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.ChannelSelectionImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.ContrastEnhancementImpl;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExponentialContrastMethodStrategy;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.GraphicImpl;
import org.geotools.styling.HistogramContrastMethodStrategy;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.LogarithmicContrastMethodStrategy;
import org.geotools.styling.Mark;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NormalizeContrastMethodStrategy;
import org.geotools.styling.OtherTextImpl;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.SelectedChannelTypeImpl;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactory2;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.styling.UomOgcMapping;
import org.geotools.util.GrowableInternationalString;
import org.geotools.util.factory.GeoTools;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.style.ContrastMethod;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Rule;
import org.opengis.style.Symbolizer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This test case captures specific problems encountered with the SLDTransformer code.
 *
 * <p>Please note that SLDTransformer is specifically targeted at SLD 1.0; for new code you should
 * be using the SLD 1.0 (or SE 1.1) xml-xsd bindings.
 *
 * @author Jody
 */
public class SLDTransformerTest {
    static StyleFactory2 sf = (StyleFactory2) CommonFactoryFinder.getStyleFactory(null);

    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    static final String NEWLINE = System.getProperty("line.separator");

    static SLDTransformer transformer;

    @Before
    public void setUp() throws Exception {
        transformer = new SLDTransformer();
        transformer.setIndentation(4);

        // setup xml unit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("sld", "http://www.opengis.net/sld");
        namespaces.put("ogc", "http://www.opengis.net/ogc");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }

    /**
     * This problem is reported from uDig 1.2, we are trying to save a RasterSymbolizer (used to
     * record the opacity of a raster layer) out to an SLD file for safe keeping.
     */
    @Test
    public void testEncodingRasterSymbolizer() throws Exception {
        RasterSymbolizer defaultRasterSymbolizer = sf.createRasterSymbolizer();
        String xmlFragment = transformer.transform(defaultRasterSymbolizer);
        assertNotNull(xmlFragment);

        RasterSymbolizer opacityRasterSymbolizer = sf.createRasterSymbolizer();
        opacityRasterSymbolizer.setOpacity(ff.literal(1.0));

        xmlFragment = transformer.transform(opacityRasterSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);
        parser.setInput(new StringReader(xmlFragment));
        Object out = parser.parseSLD();
        assertNotNull(out);
    }

    /**
     * Now that we have uDig 1.2 handling opacity we can start look at something more exciting - a
     * complete style object.
     */
    @Test
    public void testEncodingStyle() throws Exception {

        // simple default raster symbolizer
        RasterSymbolizer defaultRasterSymbolizer = sf.createRasterSymbolizer();
        String xmlFragment = transformer.transform(defaultRasterSymbolizer);
        assertNotNull(xmlFragment);

        // more complex raster symbolizer
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());
        StyleBuilder styleBuilder = new StyleBuilder(styleFactory);

        RasterSymbolizer rasterSymbolizer = styleFactory.createRasterSymbolizer();

        // set opacity
        rasterSymbolizer.setOpacity(
                CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()).literal(0.25));

        // set channel selection
        ChannelSelectionImpl csi = new ChannelSelectionImpl();
        // red
        SelectedChannelTypeImpl redChannel = new SelectedChannelTypeImpl();
        redChannel.setChannelName("1");
        ContrastEnhancementImpl rcei = new ContrastEnhancementImpl();
        rcei.setMethod(ContrastMethod.HISTOGRAM);
        redChannel.setContrastEnhancement(rcei);

        // green
        SelectedChannelTypeImpl greenChannel = new SelectedChannelTypeImpl();
        greenChannel.setChannelName("4");
        ContrastEnhancementImpl gcei = new ContrastEnhancementImpl();
        gcei.setGammaValue(ff.literal(2.5));
        greenChannel.setContrastEnhancement(gcei);

        // blue
        SelectedChannelTypeImpl blueChannel = new SelectedChannelTypeImpl();
        blueChannel.setChannelName("2");
        ContrastEnhancementImpl bcei = new ContrastEnhancementImpl();
        bcei.setMethod(ContrastMethod.NORMALIZE);
        blueChannel.setContrastEnhancement(bcei);

        csi.setRGBChannels(redChannel, greenChannel, blueChannel);
        rasterSymbolizer.setChannelSelection(csi);

        Style style = styleBuilder.createStyle(rasterSymbolizer);
        style.setName("simpleStyle");
        // style.setAbstract("Hello World");

        NamedLayer layer = styleFactory.createNamedLayer();
        layer.addStyle(style);

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();
        sld.addStyledLayer(layer);

        xmlFragment = transformer.transform(sld);
        // System.out.println(xmlFragment);

        assertNotNull(xmlFragment);
        SLDParser parser = new SLDParser(sf);
        parser.setInput(new StringReader(xmlFragment));
        Style[] stuff = parser.readXML();
        Style out = stuff[0];
        assertNotNull(out);
        Assert.assertEquals(0.25, SLD.rasterOpacity(out), 0d);
    }

    /**
     * This is a problem reported from uDig 1.2; we are trying to save a LineSymbolizer (and then
     * restore it) and the stroke is comming back black and with width 1 all the time.
     *
     * @throws Exception
     */
    @Test
    public void testStroke() throws Exception {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sld:UserStyle xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\"><sld:Name>Default Styler</sld:Name><sld:Title>Default Styler</sld:Title><sld:FeatureTypeStyle><sld:Name>simple</sld:Name><sld:Title>title</sld:Title><sld:Abstract>abstract</sld:Abstract><sld:FeatureTypeName>Feature</sld:FeatureTypeName><sld:SemanticTypeIdentifier>generic:geometry</sld:SemanticTypeIdentifier><sld:SemanticTypeIdentifier>simple</sld:SemanticTypeIdentifier><sld:Rule><sld:Title>title</sld:Title><sld:Abstract>abstract</sld:Abstract><sld:MaxScaleDenominator>1.7976931348623157E308</sld:MaxScaleDenominator><sld:LineSymbolizer><sld:Stroke><sld:CssParameter name=\"stroke\"><ogc:Literal>#0000FF</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-linecap\"><ogc:Literal>butt</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-linejoin\"><ogc:Literal>miter</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-opacity\"><ogc:Literal>1.0</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-width\"><ogc:Literal>2.0</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-dashoffset\"><ogc:Literal>0.0</ogc:Literal></sld:CssParameter></sld:Stroke></sld:LineSymbolizer></sld:Rule></sld:FeatureTypeStyle></sld:UserStyle>";
        StringReader reader = new StringReader(xml);
        SLDParser sldParser = new SLDParser(sf, reader);

        Style[] parsed = sldParser.readXML();
        assertNotNull("parsed xml", parsed);
        assertTrue("parsed xml into style", parsed.length > 0);

        Style style = parsed[0];
        assertNotNull(style);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        LineSymbolizer lineSymbolize = (LineSymbolizer) rule.symbolizers().get(0);
        Stroke stroke = lineSymbolize.getStroke();

        Expression color = stroke.getColor();
        Color value = color.evaluate(null, Color.class);
        assertNotNull("color", value);
        assertEquals("blue", Color.BLUE, value);
        assertEquals("expected width", 2, (int) stroke.getWidth().evaluate(null, Integer.class));
    }

    /**
     * SLD Fragment reported to produce error on user list - no related Jira.
     *
     * @throws Exception
     */
    @Test
    public void testTextSymbolizerLabelPalcement() throws Exception {
        String xml =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                        + "<StyledLayerDescriptor version=\"1.0.0\" "
                        + "              xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" "
                        + "              xmlns=\"http://www.opengis.net/sld\" "
                        + "              xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "              xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                        + "              xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                        + "      <NamedLayer>"
                        + "              <Name>Default Line</Name>"
                        + "              <UserStyle>"
                        + "                      <Title>A boring default style</Title>"
                        + "                      <Abstract>A sample style that just prints out a blue line</Abstract>"
                        + "                              <FeatureTypeStyle>"
                        + "                              <Rule>"
                        + "                                      <Name>Rule 1</Name>"
                        + "                                      <Title>Blue Line</Title>"
                        + "                                      <Abstract>A blue line with a 1 pixel width</Abstract>"
                        + "                                      <LineSymbolizer>"
                        + "                                              <Stroke>"
                        + "                                                      <CssParameter name=\"stroke\">#0000ff</CssParameter>"
                        + "                                              </Stroke>"
                        + "                                      </LineSymbolizer>"
                        + "                              </Rule>"
                        + "                              <Rule>"
                        + "                              <TextSymbolizer>"
                        + "                <Label><ogc:PropertyName>name</ogc:PropertyName></Label>"
                        + "                <Font>"
                        + "                    <CssParameter name=\"font-family\">Arial</CssParameter>"
                        + "                    <CssParameter name=\"font-style\">normal</CssParameter>"
                        + "                    <CssParameter name=\"font-size\">12</CssParameter>"
                        + "                    <CssParameter name=\"font-weight\">normal</CssParameter>"
                        + "                </Font>"
                        + "                <LabelPlacement>"
                        + "                      <LinePlacement>"
                        + "                              <PerpendicularOffset>0</PerpendicularOffset>"
                        + "                      </LinePlacement>"
                        + "                </LabelPlacement>"
                        + "                </TextSymbolizer>"
                        + "                              </Rule>"
                        + "                  </FeatureTypeStyle>"
                        + "              </UserStyle>"
                        + "      </NamedLayer>"
                        + "</StyledLayerDescriptor>";

        StringReader reader = new StringReader(xml);
        SLDParser sldParser = new SLDParser(sf, reader);

        Style[] parsed = sldParser.readXML();
        assertNotNull("parsed xml", parsed);
        assertTrue("parsed xml into style", parsed.length > 0);

        Style style = parsed[0];
        assertNotNull(style);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        LineSymbolizer lineSymbolize = (LineSymbolizer) rule.symbolizers().get(0);
        Stroke stroke = lineSymbolize.getStroke();

        Expression color = stroke.getColor();
        Color value = color.evaluate(null, Color.class);
        assertNotNull("color", value);
        assertEquals("blue", Color.BLUE, value);
    }

    /**
     * Tests whether LabelPlacement works with a completely empty PointPlacement as the spec allows.
     *
     * <p>See also http://jira.codehaus.org/browse/GEOS-6748
     */
    @Test
    public void testPointPlacementEmpty() {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\""
                        + "  xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                        + "  xsi:schemaLocation=\"http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd\">"
                        + "  <NamedLayer>"
                        + "    <Name>foo</Name>"
                        + "    <UserStyle>"
                        + "      <Name>pointplacement</Name>"
                        + "      <FeatureTypeStyle>"
                        + "        <Rule>"
                        + "          <MaxScaleDenominator>32000</MaxScaleDenominator>"
                        + "          <TextSymbolizer>"
                        + "            <Label>"
                        + "              <ogc:PropertyName>NAME</ogc:PropertyName>"
                        + "            </Label>"
                        + "            <Font>"
                        + "              <CssParameter name=\"font-family\">Arial</CssParameter>"
                        + "              <CssParameter name=\"font-weight\">Bold</CssParameter>"
                        + "              <CssParameter name=\"font-size\">14</CssParameter>"
                        + "            </Font>"
                        + "            <LabelPlacement>" // completely empty PointPlacement
                        + "              <PointPlacement />"
                        + "            </LabelPlacement>"
                        + "          </TextSymbolizer>"
                        + "        </Rule>"
                        + "      </FeatureTypeStyle>"
                        + "    </UserStyle>"
                        + "  </NamedLayer>"
                        + "</StyledLayerDescriptor>";
        StringReader reader = new StringReader(xml);
        SLDParser sldParser = new SLDParser(sf, reader);

        Style[] parsed = sldParser.readXML();
        assertNotNull("parsed xml", parsed);
        assertTrue("parsed xml into style", parsed.length > 0);

        Style style = parsed[0];
        assertNotNull(style);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        TextSymbolizer textSymbolize = (TextSymbolizer) rule.symbolizers().get(0);
        LabelPlacement labelPlacement = textSymbolize.getLabelPlacement();

        assertNotNull(labelPlacement);
    }

    /**
     * Tests whether LabelPlacement works with a PointPlacement that has no explicit AnchorPoint as
     * the spec allows.
     *
     * <p>See also http://jira.codehaus.org/browse/GEOS-6748
     *
     * @throws TransformerException
     */
    @Test
    public void testPointPlacementNoAnchorPoint() throws TransformerException {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\""
                        + "  xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                        + "  xsi:schemaLocation=\"http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd\">"
                        + "  <NamedLayer>"
                        + "    <Name>foo</Name>"
                        + "    <UserStyle>"
                        + "      <Name>pointplacement</Name>"
                        + "      <FeatureTypeStyle>"
                        + "        <Rule>"
                        + "          <TextSymbolizer>"
                        + "            <Label>"
                        + "              <ogc:PropertyName>NAME</ogc:PropertyName>"
                        + "            </Label>"
                        + "            <Font>"
                        + "              <CssParameter name=\"font-family\">Arial</CssParameter>"
                        + "              <CssParameter name=\"font-weight\">Bold</CssParameter>"
                        + "              <CssParameter name=\"font-size\">14</CssParameter>"
                        + "            </Font>"
                        + "            <LabelPlacement>"
                        + "              <PointPlacement>" // PointPlacement w/o AnchorPoint
                        + "                <Rotation>"
                        + "                  42"
                        + "                </Rotation>"
                        + "              </PointPlacement>"
                        + "            </LabelPlacement>"
                        + "          </TextSymbolizer>"
                        + "        </Rule>"
                        + "      </FeatureTypeStyle>"
                        + "    </UserStyle>"
                        + "  </NamedLayer>"
                        + "</StyledLayerDescriptor>";
        StringReader reader = new StringReader(xml);
        SLDParser sldParser = new SLDParser(sf, reader);

        Style[] parsed = sldParser.readXML();
        assertNotNull("parsed xml", parsed);
        assertTrue("parsed xml into style", parsed.length > 0);

        Style style = parsed[0];
        assertNotNull(style);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        TextSymbolizer textSymbolize = (TextSymbolizer) rule.symbolizers().get(0);
        PointPlacement pointPlacement = (PointPlacement) textSymbolize.getLabelPlacement();

        assertNotNull(pointPlacement);
        assertNotNull(pointPlacement.getRotation());
        assertNull(pointPlacement.getAnchorPoint());

        SLDTransformer transform = new SLDTransformer();

        String output = transform.transform(style);
        assertFalse(output.contains("AnchorPoint"));
    }

    /**
     * Another bug reported from uDig 1.2; we are trying to save a LineSymbolizer (and then restore
     * it) and the stroke is comming back black and with width 1 all the time.
     *
     * @throws Exception
     */
    @Test
    public void testPointSymbolizer() throws Exception {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<sld:StyledLayerDescriptor xmlns:sld=\"http://www.opengis.net/sld\" "
                        + "xmlns:ogc=\"http://www.opengis.net/ogc\""
                        + " xmlns:gml=\"http://www.opengis.net/gml\" version=\"1.0.0\">"
                        + "    <sld:UserLayer>"
                        + "       <sld:LayerFeatureConstraints>"
                        + "            <sld:FeatureTypeConstraint/>"
                        + "        </sld:LayerFeatureConstraints>"
                        + "        <sld:UserStyle>"
                        + "            <sld:Name>Default Styler</sld:Name> "
                        + "            <sld:Title>Default Styler</sld:Title>"
                        + "            <sld:Abstract/>"
                        + "            <sld:FeatureTypeStyle>"
                        + "                <sld:Name>simple</sld:Name>"
                        + "                <sld:Title>title</sld:Title>"
                        + "                <sld:Abstract>abstract</sld:Abstract>"
                        + "                <sld:FeatureTypeName>Feature</sld:FeatureTypeName>"
                        + "                <sld:SemanticTypeIdentifier>generic:geometry</sld:SemanticTypeIdentifier>"
                        + "                <sld:SemanticTypeIdentifier>simple</sld:SemanticTypeIdentifier>"
                        + "                <sld:Rule>"
                        + "                    <sld:Name>name</sld:Name>"
                        + "                    <sld:Title>title</sld:Title>"
                        + "                    <sld:Abstract>Abstract</sld:Abstract>"
                        + "                    <sld:MaxScaleDenominator>1.7976931348623157E308</sld:MaxScaleDenominator>"
                        + "                    <sld:PointSymbolizer>"
                        + "                        <sld:Graphic>"
                        + "                            <sld:Mark>"
                        + "                                <sld:WellKnownName>triangle</sld:WellKnownName>"
                        + "                                <sld:Fill>"
                        + "                                    <sld:CssParameter name=\"fill\">"
                        + "                                        <ogc:Literal>#FFFF00</ogc:Literal>"
                        + "                                    </sld:CssParameter>"
                        + "                                    <sld:CssParameter name=\"fill-opacity\">"
                        + "                                        <ogc:Literal>1.0</ogc:Literal>"
                        + "                                    </sld:CssParameter>"
                        + "                                </sld:Fill>"
                        + "                                <sld:Stroke>"
                        + "                                    <sld:CssParameter name=\"stroke\">"
                        + "                                        <ogc:Literal>#008000</ogc:Literal>"
                        + "                                    </sld:CssParameter>"
                        + "                                    <sld:CssParameter name=\"stroke-linecap\">"
                        + "                                        <ogc:Literal>butt</ogc:Literal>"
                        + "                                    </sld:CssParameter>"
                        + "                                    <sld:CssParameter name=\"stroke-linejoin\">"
                        + "                                        <ogc:Literal>miter</ogc:Literal>"
                        + "                                    </sld:CssParameter>"
                        + "                                    <sld:CssParameter name=\"stroke-opacity\">"
                        + "                                        <ogc:Literal>1.0</ogc:Literal>"
                        + "                                    </sld:CssParameter>"
                        + "                                    <sld:CssParameter name=\"stroke-width\">"
                        + "                                        <ogc:Literal>1.0</ogc:Literal>"
                        + "                                    </sld:CssParameter>"
                        + "                                    <sld:CssParameter name=\"stroke-dashoffset\">"
                        + "                                        <ogc:Literal>0.0</ogc:Literal>"
                        + "                                    </sld:CssParameter>"
                        + "                                </sld:Stroke>"
                        + "                            </sld:Mark>"
                        + "                            <sld:Opacity>"
                        + "                                <ogc:Literal>1.0</ogc:Literal>"
                        + "                            </sld:Opacity>"
                        + "                            <sld:Size>"
                        + "                                <ogc:Literal>10.0</ogc:Literal>"
                        + "                            </sld:Size>"
                        + "                            <sld:Rotation>"
                        + "                                <ogc:Literal>0.0</ogc:Literal>"
                        + "                            </sld:Rotation>"
                        + "                        </sld:Graphic>"
                        + "                    </sld:PointSymbolizer>"
                        + "                </sld:Rule>"
                        + "            </sld:FeatureTypeStyle>"
                        + "        </sld:UserStyle>"
                        + "    </sld:UserLayer>"
                        + "</sld:StyledLayerDescriptor>";

        StringReader reader = new StringReader(xml);
        SLDParser sldParser = new SLDParser(sf, reader);

        Style[] parsed = sldParser.readXML();
        assertNotNull("parsed xml", parsed);
        assertTrue("parsed xml into style", parsed.length > 0);

        Style style = parsed[0];
        assertNotNull(style);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        List<? extends Symbolizer> symbolizers = rule.symbolizers();
        assertEquals(1, symbolizers.size());
        PointSymbolizer symbolize = (PointSymbolizer) symbolizers.get(0);
        Graphic graphic = symbolize.getGraphic();
        List<GraphicalSymbol> symbols = graphic.graphicalSymbols();
        assertEquals(1, symbols.size());
        Mark mark = (Mark) symbols.get(0);
        Expression color = mark.getFill().getColor();
        Color value = color.evaluate(null, Color.class);
        assertNotNull("color", value);
        assertEquals("blue", Color.YELLOW, value);
    }

    /**
     * We have a pretty serious issue with this class not behaving well when logging is turned on!
     * This is the same test as above but with logging enganged at the FINEST level.
     *
     * @throws Exception
     */
    @Test
    public void testStrokeWithLogging() throws Exception {
        Logger logger = Logger.getLogger("org.geotools.styling");
        Level before = logger.getLevel();
        try {
            logger.setLevel(Level.FINEST);
            String xml =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sld:UserStyle xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\"><sld:Name>Default Styler</sld:Name><sld:Title>Default Styler</sld:Title><sld:FeatureTypeStyle><sld:Name>simple</sld:Name><sld:Title>title</sld:Title><sld:Abstract>abstract</sld:Abstract><sld:FeatureTypeName>Feature</sld:FeatureTypeName><sld:SemanticTypeIdentifier>generic:geometry</sld:SemanticTypeIdentifier><sld:SemanticTypeIdentifier>simple</sld:SemanticTypeIdentifier><sld:Rule><sld:Title>title</sld:Title><sld:Abstract>abstract</sld:Abstract><sld:MaxScaleDenominator>1.7976931348623157E308</sld:MaxScaleDenominator><sld:LineSymbolizer><sld:Stroke><sld:CssParameter name=\"stroke\"><ogc:Literal>#0000FF</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-linecap\"><ogc:Literal>butt</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-linejoin\"><ogc:Literal>miter</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-opacity\"><ogc:Literal>1.0</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-width\"><ogc:Literal>2.0</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-dashoffset\"><ogc:Literal>0.0</ogc:Literal></sld:CssParameter></sld:Stroke></sld:LineSymbolizer></sld:Rule></sld:FeatureTypeStyle></sld:UserStyle>";
            StringReader reader = new StringReader(xml);
            SLDParser sldParser = new SLDParser(sf, reader);

            Style[] parsed = sldParser.readXML();
            assertNotNull("parsed xml", parsed);
            assertTrue("parsed xml into style", parsed.length > 0);

            Style style = parsed[0];
            assertNotNull(style);
            Rule rule = style.featureTypeStyles().get(0).rules().get(0);
            LineSymbolizer lineSymbolize = (LineSymbolizer) rule.symbolizers().get(0);
            Stroke stroke = lineSymbolize.getStroke();

            Expression color = stroke.getColor();
            Color value = color.evaluate(null, Color.class);
            assertNotNull("color", value);
            assertEquals("blue", Color.BLUE, value);
            assertEquals(
                    "expected width", 2, (int) stroke.getWidth().evaluate(null, Integer.class));
        } finally {
            logger.setLevel(before);
        }
    }

    @Test
    public void testUOMEncodingPointSymbolizer() throws Exception {

        // simple default line symbolizer
        PointSymbolizer pointSymbolizer = sf.createPointSymbolizer();
        pointSymbolizer.setUnitOfMeasure(UomOgcMapping.FOOT.getUnit());
        String xmlFragment = transformer.transform(pointSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            Document dom = null;
            DocumentBuilder db = null;

            db = dbf.newDocumentBuilder();
            dom = db.parse(new InputSource(new StringReader(xmlFragment)));

            PointSymbolizer pointSymbolizer2 = parser.parsePointSymbolizer(dom.getFirstChild());

            assertTrue(
                    pointSymbolizer.getUnitOfMeasure().equals(pointSymbolizer2.getUnitOfMeasure()));
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    @Test
    public void testUOMEncodingPolygonSymbolizer() throws Exception {

        // simple default line symbolizer
        PolygonSymbolizer polygonSymbolizer = sf.createPolygonSymbolizer();
        polygonSymbolizer.setUnitOfMeasure(UomOgcMapping.METRE.getUnit());
        String xmlFragment = transformer.transform(polygonSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document dom = null;
        DocumentBuilder db = null;

        db = dbf.newDocumentBuilder();
        dom = db.parse(new InputSource(new StringReader(xmlFragment)));

        PolygonSymbolizer polygonSymbolizer2 = parser.parsePolygonSymbolizer(dom.getFirstChild());

        assertTrue(
                polygonSymbolizer.getUnitOfMeasure().equals(polygonSymbolizer2.getUnitOfMeasure()));
    }

    @Test
    public void testUOMEncodingRasterSymbolizer2() throws Exception {

        // simple default line symbolizer
        RasterSymbolizer rasterSymbolizer = sf.createRasterSymbolizer();
        rasterSymbolizer.setUnitOfMeasure(UomOgcMapping.PIXEL.getUnit());
        String xmlFragment = transformer.transform(rasterSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document dom = null;
        DocumentBuilder db = null;

        db = dbf.newDocumentBuilder();
        dom = db.parse(new InputSource(new StringReader(xmlFragment)));

        RasterSymbolizer rasterSymbolizer2 = parser.parseRasterSymbolizer(dom.getFirstChild());

        assertTrue(
                rasterSymbolizer.getUnitOfMeasure().equals(rasterSymbolizer2.getUnitOfMeasure()));
    }

    @Test
    public void testChannelExpressionEncodingRasterSymbolizer() throws Exception {
        final String b1 = "B1";
        Function envFunction = ff.function("env", ff.literal(b1), ff.literal("1"));
        RasterSymbolizer rasterSymbolizer = sf.createRasterSymbolizer();
        rasterSymbolizer.setChannelSelection(
                sf.createChannelSelection(
                        new SelectedChannelType[] {
                            sf.createSelectedChannelType(
                                    envFunction, sf.createContrastEnhancement())
                        }));
        String xmlFragment = transformer.transform(rasterSymbolizer);
        assertNotNull(xmlFragment);

        Document doc = buildTestDocument(xmlFragment);

        assertXpathExists(
                "//sld:RasterSymbolizer/sld:ChannelSelection"
                        + "/sld:GrayChannel/sld:SourceChannelName/ogc:Function[@name='env']/ogc:Literal",
                doc);

        assertXpathEvaluatesTo(
                "B1",
                "//sld:RasterSymbolizer/sld:ChannelSelection"
                        + "/sld:GrayChannel/sld:SourceChannelName/ogc:Function[@name='env']/ogc:Literal[1]",
                doc);

        assertXpathEvaluatesTo(
                "1",
                "//sld:RasterSymbolizer/sld:ChannelSelection"
                        + "/sld:GrayChannel/sld:SourceChannelName/ogc:Function[@name='env']/ogc:Literal[2]",
                doc);
    }

    @Test
    public void testUOMEncodingLineSymbolizer() throws Exception {

        // simple default line symbolizer
        LineSymbolizer lineSymbolizer = sf.createLineSymbolizer();
        lineSymbolizer.setUnitOfMeasure(UomOgcMapping.METRE.getUnit());
        String xmlFragment = transformer.transform(lineSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document dom = null;
        DocumentBuilder db = null;

        db = dbf.newDocumentBuilder();
        dom = db.parse(new InputSource(new StringReader(xmlFragment)));

        LineSymbolizer lineSymbolizer2 = parser.parseLineSymbolizer(dom.getFirstChild());

        assertTrue(lineSymbolizer.getUnitOfMeasure().equals(lineSymbolizer2.getUnitOfMeasure()));
    }

    @Test
    public void testUOMEncodingTextSymbolizer() throws Exception {

        // simple default text symbolizer
        TextSymbolizer textSymbolizer = sf.createTextSymbolizer();
        textSymbolizer.setUnitOfMeasure(UomOgcMapping.FOOT.getUnit());
        String xmlFragment = transformer.transform(textSymbolizer);

        assertNotNull(xmlFragment);

        xmlFragment = transformer.transform(textSymbolizer);
        // System.out.println(xmlFragment);

        assertNotNull(xmlFragment);
        SLDParser parser = new SLDParser(sf);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document dom = null;
        DocumentBuilder db = null;

        db = dbf.newDocumentBuilder();
        dom = db.parse(new InputSource(new StringReader(xmlFragment)));

        TextSymbolizer textSymbolizer2 = parser.parseTextSymbolizer(dom.getFirstChild());

        assertTrue(textSymbolizer.getUnitOfMeasure().equals(textSymbolizer2.getUnitOfMeasure()));
    }

    @Test
    public void testNullUOMEncodingPointSymbolizer() throws Exception {

        // simple default line symbolizer
        PointSymbolizer pointSymbolizer = sf.createPointSymbolizer();
        String xmlFragment = transformer.transform(pointSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            Document dom = null;
            DocumentBuilder db = null;

            db = dbf.newDocumentBuilder();
            dom = db.parse(new InputSource(new StringReader(xmlFragment)));

            PointSymbolizer pointSymbolizer2 = parser.parsePointSymbolizer(dom.getFirstChild());

            assertTrue(pointSymbolizer2.getUnitOfMeasure() == null);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    @Test
    public void testNullUOMEncodingPolygonSymbolizer() throws Exception {

        // simple default line symbolizer
        PolygonSymbolizer polygonSymbolizer = sf.createPolygonSymbolizer();
        String xmlFragment = transformer.transform(polygonSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document dom = null;
        DocumentBuilder db = null;

        db = dbf.newDocumentBuilder();
        dom = db.parse(new InputSource(new StringReader(xmlFragment)));

        PolygonSymbolizer polygonSymbolizer2 = parser.parsePolygonSymbolizer(dom.getFirstChild());

        assertTrue(polygonSymbolizer2.getUnitOfMeasure() == null);
    }

    @Test
    public void testNullUOMEncodingRasterSymbolizer2() throws Exception {

        // simple default line symbolizer
        RasterSymbolizer rasterSymbolizer = sf.createRasterSymbolizer();
        String xmlFragment = transformer.transform(rasterSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document dom = null;
        DocumentBuilder db = null;

        db = dbf.newDocumentBuilder();
        dom = db.parse(new InputSource(new StringReader(xmlFragment)));

        RasterSymbolizer rasterSymbolizer2 = parser.parseRasterSymbolizer(dom.getFirstChild());

        assertTrue(rasterSymbolizer2.getUnitOfMeasure() == null);
    }

    @Test
    public void testNullUOMEncodingLineSymbolizer() throws Exception {

        // simple default line symbolizer
        LineSymbolizer lineSymbolizer = sf.createLineSymbolizer();
        String xmlFragment = transformer.transform(lineSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document dom = null;
        DocumentBuilder db = null;

        db = dbf.newDocumentBuilder();
        dom = db.parse(new InputSource(new StringReader(xmlFragment)));

        LineSymbolizer lineSymbolizer2 = parser.parseLineSymbolizer(dom.getFirstChild());

        assertTrue(lineSymbolizer2.getUnitOfMeasure() == null);
    }

    @Test
    public void testNullUOMEncodingTextSymbolizer() throws Exception {

        // simple default text symbolizer
        TextSymbolizer textSymbolizer = sf.createTextSymbolizer();
        String xmlFragment = transformer.transform(textSymbolizer);
        assertNotNull(xmlFragment);

        SLDParser parser = new SLDParser(sf);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document dom = null;
        DocumentBuilder db = null;

        db = dbf.newDocumentBuilder();
        dom = db.parse(new InputSource(new StringReader(xmlFragment)));

        TextSymbolizer textSymbolizer2 = parser.parseTextSymbolizer(dom.getFirstChild());

        assertTrue(textSymbolizer2.getUnitOfMeasure() == null);
    }

    /** The displacement tag has not been exported to XML for a while... */
    @Test
    public void testDisplacement() throws Exception {
        StyleBuilder sb = new StyleBuilder();

        Graphic graphic;
        graphic = sb.createGraphic();
        Displacement disp = sb.createDisplacement(10.1, -5.5);
        graphic.setDisplacement(disp);

        SLDTransformer st = new SLDTransformer();
        String xml = st.transform(graphic);

        assertTrue(
                "XML transformation of this GraphicImpl does not contain the word 'Displacement' ",
                xml.contains("Displacement"));
    }

    @Test
    public void testTextSymbolizerTransformOutAndInAndOutAgain() throws Exception {
        StyleBuilder sb = new StyleBuilder();

        Style style = sb.createStyle(sb.createTextSymbolizer());

        SLDTransformer st = new SLDTransformer();
        String firstExport = st.transform(style);
        SLDParser sldp = new SLDParser(CommonFactoryFinder.getStyleFactory(null));
        sldp.setInput(new StringReader(firstExport));
        Style[] firstImport = sldp.readXML();

        assertNotNull(firstImport[0]);

        // NPE here
        String secondExport = st.transform(firstImport);
    }

    // GEOT-5726
    @Test
    public void testTextSymbolizerTransformOutAndInAndOutAndInAgainWithEmptyFontFamily()
            throws Exception {
        // Construct a style with an empty "" font
        StyleBuilder sb = new StyleBuilder();

        Font font =
                sb.createFont(
                        ff.literal(""), ff.literal("normal"), ff.literal("normal"), ff.literal(12));
        TextSymbolizer ts = sb.createStaticTextSymbolizer(Color.BLACK, font, "label");
        Style style = sb.createStyle(ts);

        assertEquals("", getFontFamily(style));

        // Encode the style, and parse it
        SLDTransformer st = new SLDTransformer();
        String firstExport = st.transform(style);
        SLDParser sldp = new SLDParser(CommonFactoryFinder.getStyleFactory(null));
        sldp.setInput(new StringReader(firstExport));
        Style[] firstImport = sldp.readXML();

        assertNotNull(firstImport[0]);

        // previously got null here
        assertEquals("", getFontFamily(firstImport[0]));

        // Encode the style, and parse it again
        String secondExport = st.transform(firstImport);
        sldp.setInput(new StringReader(secondExport));
        Style[] secondImport = sldp.readXML();

        // previously got "Serif" here
        assertEquals("", getFontFamily(secondImport[0]));
    }

    private String getFontFamily(Style s) {

        List<org.geotools.styling.Symbolizer> symbolizers =
                s.featureTypeStyles().get(0).rules().get(0).symbolizers();
        for (org.geotools.styling.Symbolizer symbolizer : symbolizers) {
            if (symbolizer instanceof TextSymbolizer) {
                Font font = ((TextSymbolizer) symbolizer).fonts().get(0);
                assertTrue(font.getFamily().size() > 0);
                return font.getFamily().get(0) == null ? null : font.getFamily().get(0).toString();
            }
        }
        fail("Style should contain a TextSymbolizer");
        return null;
    }

    /**
     * Checks whether the "Priority" parameter of a TextSymbolizer is correctly stored and loaded
     */
    @Test
    public void testPriorityTransformOutAndIn() throws Exception {
        StyleBuilder sb = new StyleBuilder();

        TextSymbolizer ts = sb.createTextSymbolizer();
        PropertyName literalPrio =
                CommonFactoryFinder.getFilterFactory2(null).property("quantVariable");
        ts.setPriority(literalPrio);
        Style style = sb.createStyle(ts);

        SLDTransformer st = new SLDTransformer();
        String firstExport = st.transform(style);
        SLDParser sldp = new SLDParser(CommonFactoryFinder.getStyleFactory(null));
        sldp.setInput(new StringReader(firstExport));
        Style[] firstImport = sldp.readXML();

        assertNotNull(firstImport[0]);

        {
            Style reimportedStyle = firstImport[0];
            TextSymbolizer reimportedTs =
                    (TextSymbolizer)
                            reimportedStyle
                                    .featureTypeStyles()
                                    .get(0)
                                    .rules()
                                    .get(0)
                                    .symbolizers()
                                    .get(0);
            assertNotNull(reimportedTs.getPriority());
            assertEquals("quantVariable", reimportedTs.getPriority().toString());
        }

        // Just for the fun do it again... we had a case where this threw NPE
        {
            // NPE here??
            String secondExport = st.transform(firstImport);
            SLDParser sldp2 = new SLDParser(CommonFactoryFinder.getStyleFactory(null));
            sldp2.setInput(new StringReader(secondExport));
            Style[] readXML = sldp2.readXML();

            Style reimportedStyle = readXML[0];
            TextSymbolizer reimportedTs =
                    (TextSymbolizer)
                            reimportedStyle
                                    .featureTypeStyles()
                                    .get(0)
                                    .rules()
                                    .get(0)
                                    .symbolizers()
                                    .get(0);
            assertNotNull(reimportedTs.getPriority());
            assertEquals("quantVariable", reimportedTs.getPriority().toString());
        }
    }

    /** SLD Transformer did't save the type of the colormap */
    @Test
    public void testColorMap() throws Exception {
        SLDTransformer st = new SLDTransformer();
        ColorMap cm = sf.createColorMap();

        // Test type = values
        cm.setType(ColorMap.TYPE_VALUES);
        assertTrue(
                "parsed xml must contain attribbute type with correct value",
                st.transform(cm).contains("type=\"values\""));

        // Test type = intervals
        cm.setType(ColorMap.TYPE_INTERVALS);
        assertTrue(
                "parsed xml must contain attribbute type with correct value",
                st.transform(cm).contains("type=\"intervals\""));

        // Test type = ramp
        cm.setType(ColorMap.TYPE_RAMP);
        assertEquals(
                "parsed xml must contain attribbute type with correct value",
                -1,
                st.transform(cm).indexOf("type="));
    }

    @Test
    public void testColorMapExtended() throws Exception {
        SLDTransformer st = new SLDTransformer();
        ColorMap cm = sf.createColorMap();

        // Test type = values, extended = true
        cm.setType(ColorMap.TYPE_VALUES);
        cm.setExtendedColors(true);
        assertTrue(
                "parsed xml must contain attribbute type with correct value",
                st.transform(cm).contains("extended=\"true\""));

        // Test type = intervals, extended = true
        cm.setType(ColorMap.TYPE_INTERVALS);
        cm.setExtendedColors(true);
        assertTrue(
                "parsed xml must contain attribbute type with correct value",
                st.transform(cm).contains("extended=\"true\""));

        // Test type = ramp, extended = true
        cm.setType(ColorMap.TYPE_RAMP);
        cm.setExtendedColors(true);
        assertTrue(
                "parsed xml must contain attribbute type with correct value",
                st.transform(cm).contains("extended=\"true\""));
    }

    /**
     * Checks the output of encoding a default line symbolizer does not include all the default
     * values
     *
     * @throws Exception
     */
    @Test
    public void testMinimumLineSymbolizer() throws Exception {
        StyleBuilder sb = new StyleBuilder();

        LineSymbolizer ls = sb.createLineSymbolizer();
        String xml = transformer.transform(ls);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        // check LineSymbolizer has the stroke element inside, but stroke does not have children
        assertXpathEvaluatesTo("1", "count(/sld:LineSymbolizer/*)", doc);
        assertXpathEvaluatesTo("0", "count(/sld:LineSymbolizer/sld:Stroke/*)", doc);

        // setup custom line width and color, and explicitly set the opacity to the default value
        ls.getStroke().setWidth(ff.literal(3));
        ls.getStroke().setColor(ff.literal(Color.YELLOW));
        ls.getStroke().setOpacity(ff.literal(1));
        xml = transformer.transform(ls);
        // System.out.println(xml);
        doc = buildTestDocument(xml);

        // same as above, but this time we expect the width and color to be set
        assertXpathEvaluatesTo("1", "count(/sld:LineSymbolizer/*)", doc);
        assertXpathEvaluatesTo("2", "count(/sld:LineSymbolizer/sld:Stroke/*)", doc);
        assertXpathEvaluatesTo(
                "#FFFF00", "/sld:LineSymbolizer/sld:Stroke/sld:CssParameter[@name='stroke']", doc);
        assertXpathEvaluatesTo(
                "3", "/sld:LineSymbolizer/sld:Stroke/sld:CssParameter[@name='stroke-width']", doc);
    }

    @Test
    public void testMinimumPolygonSymbolizer() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        PolygonSymbolizer ps = sb.createPolygonSymbolizer();
        String xml = transformer.transform(ps);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        // check PolygonSymbolizer has a fill and a stroke, both empty
        assertXpathEvaluatesTo("2", "count(/sld:PolygonSymbolizer/*)", doc);
        assertXpathEvaluatesTo("0", "count(/sld:PolygonSymbolizer/sld:Stroke/*)", doc);
        assertXpathEvaluatesTo("0", "count(/sld:PolygonSymbolizer/sld:Fill/*)", doc);

        ps.getFill().setColor(ff.literal(Color.BLUE));
        xml = transformer.transform(ps);
        // System.out.println(xml);
        doc = buildTestDocument(xml);

        // this time check the fill has the color
        assertXpathEvaluatesTo("2", "count(/sld:PolygonSymbolizer/*)", doc);
        assertXpathEvaluatesTo("0", "count(/sld:PolygonSymbolizer/sld:Stroke/*)", doc);
        assertXpathEvaluatesTo("1", "count(/sld:PolygonSymbolizer/sld:Fill/*)", doc);
        assertXpathEvaluatesTo(
                "#0000FF", "/sld:PolygonSymbolizer/sld:Fill/sld:CssParameter[@name='fill']", doc);
    }

    @Test
    public void testMinimumPointSymbolizer() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        PointSymbolizer ps = sb.createPointSymbolizer();
        String xml = transformer.transform(ps);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        // check PolygonSymbolizer has a fill and a stroke, both empty
        assertXpathEvaluatesTo("1", "count(/sld:PointSymbolizer/*)", doc);
        assertXpathEvaluatesTo("1", "count(/sld:PointSymbolizer/sld:Graphic/*)", doc);
        assertXpathEvaluatesTo("2", "count(/sld:PointSymbolizer/sld:Graphic/sld:Mark/*)", doc);
        assertXpathExists("/sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Fill", doc);
        assertXpathExists("/sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Stroke", doc);
        assertXpathEvaluatesTo(
                "0", "count(/sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Fill/*)", doc);
        assertXpathEvaluatesTo(
                "0", "count(/sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Stroke/*)", doc);
    }

    @Test
    public void testMinimumRasterSymbolizer() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        RasterSymbolizer rs = sb.getStyleFactory().createRasterSymbolizer();
        String xml = transformer.transform(rs);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        // check RasterSymbolizer just has the default geometry value
        // (which is not a default in SLD, just in our builder)
        assertXpathEvaluatesTo("1", "count(/sld:RasterSymbolizer)", doc);
        assertXpathEvaluatesTo("", "/sld:RasterSymbolizer/sld:Geometry/ogc:PropertyName", doc);
    }

    @Test
    public void testMinimumStyle() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        Style s = sb.createStyle(sb.createPointSymbolizer());
        String xml = transformer.transform(s);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        // check RasterSymbolizer just has the default geometry value
        // (which is not a default in SLD, just in our builder)
        assertXpathEvaluatesTo("2", "count(/sld:UserStyle/sld:FeatureTypeStyle/*)", doc);
        assertXpathEvaluatesTo("1", "count(/sld:UserStyle/sld:FeatureTypeStyle/sld:Name)", doc);
        assertXpathEvaluatesTo("1", "count(/sld:UserStyle/sld:FeatureTypeStyle/sld:Rule)", doc);
        assertXpathEvaluatesTo("1", "count(/sld:UserStyle/sld:FeatureTypeStyle/sld:Rule/*)", doc);
        assertXpathEvaluatesTo(
                "1",
                "count(/sld:UserStyle/sld:FeatureTypeStyle/sld:Rule/sld:PointSymbolizer)",
                doc);
    }

    @Test
    public void testDefaultStyle() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        Style s = sb.createStyle(sb.createPointSymbolizer());
        s.setDefault(true);
        StyleFactory sf = sb.getStyleFactory();
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        NamedLayer layer = sf.createNamedLayer();
        layer.setName("layerName");
        layer.addStyle(s);
        sld.addStyledLayer(layer);

        String xml = transformer.transform(sld);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo(
                "1", "/sld:StyledLayerDescriptor/sld:NamedLayer/sld:UserStyle/sld:IsDefault", doc);
    }

    @Test
    public void testLocalizedTitle() throws Exception {
        RuleImpl rule = (RuleImpl) CommonFactoryFinder.getStyleFactory().createRule();
        GrowableInternationalString intString =
                new GrowableInternationalString("title") {

                    @Override
                    public String toString() {
                        return super.toString(null);
                    }
                };
        intString.add(Locale.ITALIAN, "titolo");
        intString.add(Locale.FRENCH, "titre");
        intString.add(Locale.CANADA_FRENCH, "titre");
        rule.getDescription().setTitle(intString);
        String xml = transformer.transform(rule);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("title", "normalize-space(//sld:Title/text()[1])", doc);
        assertXpathEvaluatesTo(
                "titolo",
                "//sld:Title/sld:Localized[@lang='" + Locale.ITALIAN.toString() + "']",
                doc);
        assertXpathEvaluatesTo(
                "titre",
                "//sld:Title/sld:Localized[@lang='" + Locale.FRENCH.toString() + "']",
                doc);
        assertXpathEvaluatesTo(
                "titre",
                "//sld:Title/sld:Localized[@lang='" + Locale.CANADA_FRENCH.toString() + "']",
                doc);
    }

    public void testLocalizedAbstract() throws Exception {
        RuleImpl rule = (RuleImpl) CommonFactoryFinder.getStyleFactory().createRule();
        GrowableInternationalString intString = new GrowableInternationalString("title");
        intString.add(Locale.ITALIAN, "titolo");
        intString.add(Locale.FRENCH, "titre");
        intString.add(Locale.CANADA_FRENCH, "titre");
        rule.getDescription().setAbstract(intString);
        String xml = transformer.transform(rule);
        assertTrue(xml.contains("<sld:Abstract>title"));
        assertTrue(
                xml.contains(
                        "<sld:Localized lang=\""
                                + Locale.ITALIAN.toString()
                                + "\">titolo</sld:Localized>"));
        assertTrue(
                xml.contains(
                        "<sld:Localized lang=\""
                                + Locale.FRENCH.toString()
                                + "\">titre</sld:Localized>"));
        assertTrue(
                xml.contains(
                        "<sld:Localized lang=\""
                                + Locale.CANADA_FRENCH.toString()
                                + "\">titre</sld:Localized>"));
    }

    @Test
    public void testEncodeFunction() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        PointSymbolizer ps = sb.createPointSymbolizer();
        ps.getGraphic().setSize(sb.getFilterFactory().function("random"));
        Style s = sb.createStyle(ps);
        s.setDefault(true);

        String xml = transformer.transform(s);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo(
                "random",
                "/sld:UserStyle/sld:FeatureTypeStyle/sld:Rule/sld:PointSymbolizer/sld:Graphic/sld:Size/ogc:Function/@name",
                doc);
    }

    /** TextSymbolizer2 specific properties saved and laoded again must fit */
    @Test
    public void textTextSymbolizer2_InAndOut()
            throws TransformerException, SAXException, IOException, XpathException {
        StyleBuilder sb = new StyleBuilder();

        TextSymbolizer2 ts2 = (TextSymbolizer2) sf.createTextSymbolizer();
        // Create a Graphic with two recognizable values
        GraphicImpl gr = new GraphicImpl(ff);
        gr.setOpacity(ff.literal(0.77));
        gr.setSize(ff.literal(77));
        ts2.setGraphic(gr);
        Literal snippet = ff.literal("no idea what a snipet is good for");
        ts2.setSnippet(snippet);
        Literal fD = ff.literal("some description");
        ts2.setFeatureDescription(fD);
        OtherTextImpl otherText = new OtherTextImpl();
        otherText.setTarget("otherTextTarget");
        otherText.setText(ff.literal("otherTextText"));
        ts2.setOtherText(otherText);

        // A first check of the XML
        Document doc = buildTestDocument(transformer.transform(ts2));
        assertXpathEvaluatesTo("1", "count(/sld:TextSymbolizer/sld:Graphic)", doc);
        assertXpathEvaluatesTo("1", "count(/sld:TextSymbolizer/sld:Snippet)", doc);
        assertXpathEvaluatesTo("1", "count(/sld:TextSymbolizer/sld:OtherText)", doc);
        assertXpathEvaluatesTo("1", "count(/sld:TextSymbolizer/sld:FeatureDescription)", doc);

        // Transform and reimport and compare
        String xml = transformer.transform(sb.createStyle(ts2));

        SLDParser sldParser = new SLDParser(sf);
        sldParser.setInput(new StringReader(xml));
        Style importedStyle = sldParser.readXML()[0];
        TextSymbolizer2 copy =
                (TextSymbolizer2)
                        importedStyle
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        // compare it
        assertEquals(
                "Graphic of TextSymbolizer2 has not been correctly ex- and reimported",
                gr.getOpacity(),
                copy.getGraphic().getOpacity());
        assertEquals(
                "Graphic of TextSymbolizer2 has not been correctly ex- and reimported",
                gr.getSize(),
                copy.getGraphic().getSize());
        assertEquals(
                "Snippet of TextSymbolizer2 has not been correctly ex- and reimported",
                snippet,
                copy.getSnippet());
        assertEquals(
                "FeatureDescription of TextSymbolizer2 has not been correctly ex- and reimported",
                fD,
                copy.getFeatureDescription());
        assertEquals(
                "OtherText of TextSymbolizer2 has not been correctly ex- and reimported",
                otherText.getTarget(),
                copy.getOtherText().getTarget());
        assertEquals(
                "OtherText of TextSymbolizer2 has not been correctly ex- and reimported",
                otherText.getText(),
                copy.getOtherText().getText());
    }

    /**
     * Test that perpendicularOffset for LineSymbolizer is correctly exported and reimported
     *
     * @throws TransformerException
     * @throws SAXException
     * @throws IOException
     * @throws XpathException
     */
    @Test
    public void testLineSymbolizerWithPerpendicularOffset()
            throws TransformerException, SAXException, IOException, XpathException {
        StyleBuilder sb = new StyleBuilder();
        LineSymbolizer ls = sb.createLineSymbolizer();
        ls.setPerpendicularOffset(ff.literal(0.77));

        // check XML
        Document doc = buildTestDocument(transformer.transform(ls));
        assertXpathEvaluatesTo("1", "count(/sld:LineSymbolizer/sld:PerpendicularOffset)", doc);

        // Transform, reimport and compare
        String xml = transformer.transform(sb.createStyle(ls));

        SLDParser sldParser = new SLDParser(sf);
        sldParser.setInput(new StringReader(xml));
        Style importedStyle = sldParser.readXML()[0];
        LineSymbolizer copy =
                (LineSymbolizer)
                        importedStyle
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        // compare
        assertEquals(
                "Perpendicular offset of LineSymbolizer has not been correctly ex- and reimported",
                ls.getPerpendicularOffset(),
                copy.getPerpendicularOffset());
    }

    /**
     * Make sure the FeatureTypeStyle Transformation element survives a SLD to Style to SLD round
     * trip.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testFeatureTypeStyleTransformation() throws Exception {

        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<sld:UserStyle xmlns=\"http://www.opengis.net/sld\" "
                        + "    xmlns:sld=\"http://www.opengis.net/sld\" "
                        + "    xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "    xmlns:gml=\"http://www.opengis.net/gml\">"
                        + "    <sld:Name>Default Styler</sld:Name>"
                        + "    <sld:Title/>"
                        + "    <sld:FeatureTypeStyle>"
                        + "        <sld:Name>Buffer</sld:Name>"
                        + "        <sld:Transformation>"
                        + "            <ogc:Function name=\"buffer\">"
                        + "               <ogc:PropertyName>the_geom</ogc:PropertyName>"
                        + "               <ogc:Literal>500</ogc:Literal>"
                        + "            </ogc:Function>"
                        + "        </sld:Transformation>"
                        + "        <sld:Rule>"
                        + "            <sld:LineSymbolizer>"
                        + "                <sld:Stroke>"
                        + "                    <sld:CssParameter name=\"stroke\">#312624</sld:CssParameter>"
                        + "                    <sld:CssParameter name=\"stroke-width\">0.1</sld:CssParameter>"
                        + "                </sld:Stroke>"
                        + "            </sld:LineSymbolizer>"
                        + "            <sld:PolygonSymbolizer>"
                        + "                <sld:Fill>"
                        + "                    <sld:CssParameter name=\"fill\">#f5deb3</sld:CssParameter>"
                        + "                </sld:Fill>"
                        + "            </sld:PolygonSymbolizer>"
                        + "       </sld:Rule>"
                        + "    </sld:FeatureTypeStyle>"
                        + "</sld:UserStyle>";

        StringReader reader = new StringReader(xml);
        SLDParser sldParser = new SLDParser(sf, reader);

        Style[] styles = sldParser.readXML();
        assertNotNull("parsed xml", styles);
        assertTrue("parsed xml into style", styles.length > 0);

        SLDTransformer styleTransform = new SLDTransformer();
        styleTransform.setIndentation(2);
        StringWriter writer = new StringWriter();
        styleTransform.transform(styles[0], writer);
        String actualXml = writer.toString();
        assertTrue(actualXml.contains("<sld:Transformation>"));
        assertTrue(actualXml.contains("<ogc:Function name=\"buffer\">"));
        assertTrue(actualXml.contains("<ogc:PropertyName>the_geom</ogc:PropertyName>"));
        assertTrue(actualXml.contains("<ogc:Literal>500</ogc:Literal>"));
        assertTrue(actualXml.contains("</ogc:Function>"));
        assertTrue(actualXml.contains("</sld:Transformation>"));
    }

    @Test
    public void testDynamicSymbolizer() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        String chartURI =
                "http://chart?cht=p&chd=t:${100 * MALE / PERSONS},${100 * FEMALE / PERSONS}&chf=bg,s,FFFFFF00";
        ExternalGraphic eg = sb.createExternalGraphic(chartURI, "image/png");
        PointSymbolizer ps = sb.createPointSymbolizer(sb.createGraphic(eg, null, null));

        Style s = sb.createStyle(ps);
        s.setDefault(true);

        String xml = transformer.transform(s);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo(
                chartURI,
                "/sld:UserStyle/sld:FeatureTypeStyle/sld:Rule/sld:PointSymbolizer/sld:Graphic/sld:ExternalGraphic/sld:OnlineResource/@xlink:href",
                doc);

        SLDParser parser = new SLDParser(sf);
        parser.setInput(new StringReader(xml));
        Style importedStyle = parser.readXML()[0];
        PointSymbolizer psCopy =
                (PointSymbolizer)
                        importedStyle
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);
        ExternalGraphic egCopy = (ExternalGraphic) psCopy.getGraphic().graphicalSymbols().get(0);
        assertEquals(chartURI, egCopy.getLocation().toExternalForm());
    }

    @Test
    public void testLocalUomPoint() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        PointSymbolizer ps = sb.createPointSymbolizer();
        ps.getGraphic().setSize(ff.literal("1m"));
        StyledLayerDescriptor sld = buildSLDAroundSymbolizer(ps);

        String xml = transformer.transform(sld);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("1m", "//sld:Graphic/sld:Size", doc);
    }

    @Test
    public void testLocalUomLine() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        LineSymbolizer ls = sb.createLineSymbolizer();
        ls.getStroke().setWidth(ff.literal("1m"));
        StyledLayerDescriptor sld = buildSLDAroundSymbolizer(ls);

        String xml = transformer.transform(sld);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo(
                "1m",
                "//sld:LineSymbolizer/sld:Stroke/sld:CssParameter[@name='stroke-width']",
                doc);
    }

    @Test
    public void testLocalUomText() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.getFont().setSize(ff.literal("1m"));
        StyledLayerDescriptor sld = buildSLDAroundSymbolizer(ts);

        String xml = transformer.transform(sld);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo(
                "1m", "//sld:TextSymbolizer/sld:Font/sld:CssParameter[@name='font-size']", doc);
    }

    @Test
    public void testLabelMixedContent() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.setLabel(ff.function("strConcat", ff.literal("abc"), ff.property("myProperty")));
        StyledLayerDescriptor sld = buildSLDAroundSymbolizer(ts);

        String xml = transformer.transform(sld);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("abc", "normalize-space(//sld:Label/text()[1])", doc);
        assertXpathEvaluatesTo("ogc:PropertyName", "name(//sld:Label/*[1])", doc);
        assertXpathEvaluatesTo("myProperty", "//sld:Label/*[1]/text()", doc);
    }

    @Test
    public void testLabelCDataStart() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.setLabel(ff.function("strConcat", ff.literal(" abc"), ff.property("myProperty")));
        StyledLayerDescriptor sld = buildSLDAroundSymbolizer(ts);

        String xml = transformer.transform(sld);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("abc", "normalize-space(//sld:Label/text()[1])", doc);
        assertXpathEvaluatesTo("myProperty", "//sld:Label/ogc:PropertyName", doc);

        // normalize-space() strips indentation, but also CDATA whitespace, so we resort to string
        // comparisons here
        assertTrue(xml.contains("<![CDATA[ abc]]>"));
    }

    @Test
    public void testLabelCDataEnd() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.setLabel(ff.function("strConcat", ff.literal("abc "), ff.property("myProperty")));
        StyledLayerDescriptor sld = buildSLDAroundSymbolizer(ts);

        String xml = transformer.transform(sld);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("abc", "normalize-space(//sld:Label/text()[1])", doc);
        assertXpathEvaluatesTo("myProperty", "//sld:Label/ogc:PropertyName", doc);

        // normalize-space() strips indentation, but also CDATA whitespace, so we resort to string
        // comparisons here
        assertTrue(xml.contains("<![CDATA[abc ]]>"));
    }

    @Test
    public void testLabelCDataMid() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.setLabel(ff.function("strConcat", ff.literal("a  bc"), ff.property("myProperty")));
        StyledLayerDescriptor sld = buildSLDAroundSymbolizer(ts);

        String xml = transformer.transform(sld);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("a bc", "normalize-space(//sld:Label/text()[1])", doc);
        assertXpathEvaluatesTo("myProperty", "//sld:Label/ogc:PropertyName", doc);

        // normalize-space() strips indentation, but also CDATA whitespace, so we resort to string
        // comparisons here
        assertTrue(xml.contains("<![CDATA[a  bc]]>"));
    }

    @Test
    public void testLabelCDataNewline() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.setLabel(ff.function("strConcat", ff.literal("a \n bc"), ff.property("myProperty")));
        StyledLayerDescriptor sld = buildSLDAroundSymbolizer(ts);

        String xml = transformer.transform(sld);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("a bc", "normalize-space(//sld:Label/text()[1])", doc);
        assertXpathEvaluatesTo("myProperty", "//sld:Label/ogc:PropertyName", doc);

        // normalize-space() strips indentation, but also CDATA whitespace, so we resort to string
        // comparisons here
        assertTrue(xml.contains("<![CDATA[a " + NEWLINE + " bc]]>"));
    }

    @Test
    public void testLabelNested() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        TextSymbolizer ts = sb.createTextSymbolizer();
        ts.setLabel(
                ff.function(
                        "strConcat",
                        ff.literal("abc "),
                        ff.function("strConcat", ff.property("myProperty"), ff.literal(" def"))));
        StyledLayerDescriptor sld = buildSLDAroundSymbolizer(ts);

        String xml = transformer.transform(sld);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("abc", "normalize-space(//sld:Label/text()[1])", doc);
        assertXpathEvaluatesTo("myProperty", "//sld:Label/ogc:PropertyName", doc);
        assertXpathEvaluatesTo("def", "normalize-space(//sld:Label/text()[2])", doc);

        // normalize-space() strips indentation, but also CDATA whitespace, so we resort to string
        // comparisons here
        assertTrue(xml.contains("<![CDATA[abc ]]>"));
        assertTrue(xml.contains("<![CDATA[ def]]>"));
    }

    /**
     * Test the transformation of an WellKnownName element that contains an expression.
     *
     * @throws Exception
     */
    @Test
    public void testWellKnownNameWithExpression() throws Exception {

        String originalStyleXml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "<sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\""
                        + "                           xmlns:sld=\"http://www.opengis.net/sld\""
                        + "                           xmlns:ogc=\"http://www.opengis.net/ogc\""
                        + "                           xmlns:gml=\"http://www.opengis.net/gml\" version=\"1.0.0\">"
                        + "	<sld:NamedLayer>"
                        + "		<sld:Name>test</sld:Name>"
                        + "		<sld:UserStyle>"
                        + "			<sld:Name>test</sld:Name>"
                        + "			<sld:FeatureTypeStyle>"
                        + "				<sld:Name>name</sld:Name>"
                        + "				<sld:Rule>"
                        + "					<sld:PointSymbolizer>"
                        + "						<sld:Graphic>"
                        + "							<sld:Mark>"
                        + "								<sld:WellKnownName>"
                        + "									<ogc:Function name=\"strConcat\">"
                        + "										<ogc:Literal>mark-</ogc:Literal>"
                        + "      							    <ogc:PropertyName>MARK_NAME</ogc:PropertyName>"
                        + "									</ogc:Function>"
                        + "								</sld:WellKnownName>"
                        + "							</sld:Mark>"
                        + "						</sld:Graphic>"
                        + "					</sld:PointSymbolizer>"
                        + "				</sld:Rule>"
                        + "			</sld:FeatureTypeStyle>"
                        + "		</sld:UserStyle>"
                        + "	</sld:NamedLayer>"
                        + "</sld:StyledLayerDescriptor>";

        Style originalStyle = validateWellKnownNameWithExpressionStyle(originalStyleXml);

        SLDTransformer styleTransform = new SLDTransformer();
        styleTransform.setIndentation(2);
        StringWriter writerWriter = new StringWriter();
        styleTransform.transform(originalStyle, writerWriter);
        String transformedStyleXml = writerWriter.toString();

        validateWellKnownNameWithExpressionStyle(transformedStyleXml);
    }

    /**
     * Test the transformation of a stroke-dasharray element that contains expressions.
     *
     * @throws Exception
     */
    @Test
    public void testStrokeDasharrayWithExpressions() throws Exception {

        String originalStyleXml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "<sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\""
                        + "                           xmlns:sld=\"http://www.opengis.net/sld\""
                        + "                           xmlns:ogc=\"http://www.opengis.net/ogc\""
                        + "                           xmlns:gml=\"http://www.opengis.net/gml\" version=\"1.0.0\">"
                        + "	<sld:NamedLayer>"
                        + "		<sld:Name>test</sld:Name>"
                        + "		<sld:UserStyle>"
                        + "			<sld:Name>test</sld:Name>"
                        + "			<sld:FeatureTypeStyle>"
                        + "				<sld:Name>name</sld:Name>"
                        + "				<sld:Rule>"
                        + "					 <LineSymbolizer>"
                        + "                     <Stroke>"
                        + "                         <CssParameter name=\"stroke\">#0000FF</CssParameter>"
                        + "                         <CssParameter name=\"stroke-dasharray\">"
                        + "                             <PropertyName>stroke1</PropertyName>"
                        + "                             1.0"
                        + "                             <PropertyName>stroke2</PropertyName>"
                        + "                             <![CDATA[2.0]]>"
                        + "                         </CssParameter>"
                        + "                     </Stroke>"
                        + "                 </LineSymbolizer>"
                        + "				</sld:Rule>"
                        + "			</sld:FeatureTypeStyle>"
                        + "		</sld:UserStyle>"
                        + "	</sld:NamedLayer>"
                        + "</sld:StyledLayerDescriptor>";

        SLDTransformer styleTransform = new SLDTransformer();
        styleTransform.setIndentation(2);
        StringWriter writerWriter = new StringWriter();
        styleTransform.transform(parseStyles(originalStyleXml), writerWriter);
        String transformedStyleXml = writerWriter.toString();

        Style style = parseStyles(transformedStyleXml)[0];

        assertNotNull("style is null", style);
        assertNotNull("feature type styles are null", style.featureTypeStyles());
        assertTrue(
                "more or less that one feature type style is available",
                style.featureTypeStyles().size() == 1);
        assertNotNull("rules are null", style.featureTypeStyles().get(0).rules());
        assertTrue(
                "more or less that one rule is available",
                style.featureTypeStyles().get(0).rules().size() == 1);

        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        assertNotNull("rule is null", rule);

        List<? extends Symbolizer> symbolizers = rule.symbolizers();
        assertNotNull("symbolizers are null", symbolizers);
        assertTrue("more or less that one symbolizer is available", symbolizers.size() == 1);

        LineSymbolizer lineSymbolizer = (LineSymbolizer) symbolizers.get(0);
        assertNotNull("line symbolizer is null", lineSymbolizer);

        Stroke stroke = lineSymbolizer.getStroke();
        assertNotNull("stroke is null", stroke);
        assertNotNull("stroke dasharray is null", stroke.dashArray());

        List<Expression> expressions = stroke.dashArray();
        assertTrue("more or less expressions available", expressions.size() == 4);
        assertTrue("not expected expression", expressions.get(0).equals(ff.property("stroke1")));
        assertTrue("not expected expression", expressions.get(1).equals(ff.literal(1.0)));
        assertTrue("not expected expression", expressions.get(2).equals(ff.property("stroke2")));
        assertTrue("not expected expression", expressions.get(3).equals(ff.literal(2.0)));
    }

    /**
     * Test the transformation of a stroke-dasharray element that contains only literal expressions.
     *
     * @throws Exception
     */
    @Test
    public void testStrokeDasharrayWithOnlyLiteralExpressions() throws Exception {

        String originalStyleXml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "<sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\""
                        + "                           xmlns:sld=\"http://www.opengis.net/sld\""
                        + "                           xmlns:ogc=\"http://www.opengis.net/ogc\""
                        + "                           xmlns:gml=\"http://www.opengis.net/gml\" version=\"1.0.0\">"
                        + "	<sld:NamedLayer>"
                        + "		<sld:Name>test</sld:Name>"
                        + "		<sld:UserStyle>"
                        + "			<sld:Name>test</sld:Name>"
                        + "			<sld:FeatureTypeStyle>"
                        + "				<sld:Name>name</sld:Name>"
                        + "				<sld:Rule>"
                        + "                 <LineSymbolizer>"
                        + "                     <Stroke>"
                        + "                         <CssParameter name=\"stroke\">#0000FF</CssParameter>"
                        + "                         <CssParameter name=\"stroke-dasharray\">"
                        + "                             10.0 5.0 20.0 15.0"
                        + "                         </CssParameter>"
                        + "                     </Stroke>"
                        + "                 </LineSymbolizer>"
                        + "				</sld:Rule>"
                        + "			</sld:FeatureTypeStyle>"
                        + "		</sld:UserStyle>"
                        + "	</sld:NamedLayer>"
                        + "</sld:StyledLayerDescriptor>";

        SLDTransformer styleTransform = new SLDTransformer();
        styleTransform.setIndentation(2);
        StringWriter writerWriter = new StringWriter();
        styleTransform.transform(parseStyles(originalStyleXml), writerWriter);
        String transformedStyleXml = writerWriter.toString();

        assertTrue(
                transformedStyleXml.contains(
                        "<sld:CssParameter name=\"stroke-dasharray\">10.0 5.0 20.0 15.0</sld:CssParameter>"));
    }

    private Style[] parseStyles(String styleXml) {
        StringReader stringReader = new StringReader(styleXml);
        SLDParser sldParser = new SLDParser(sf, stringReader);
        return sldParser.readXML();
    }

    @Test
    public void testAnchorPointInGraphic() throws Exception {
        StyleBuilder sb = new StyleBuilder();

        Graphic graphic;
        graphic = sb.createGraphic();
        Displacement disp = sb.createDisplacement(10, 10);
        AnchorPoint ap = sb.createAnchorPoint(1, 0.3);
        graphic.setDisplacement(disp);
        graphic.setAnchorPoint(ap);

        SLDTransformer st = new SLDTransformer();
        String xml = st.transform(graphic);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("10.0", "//sld:Graphic/sld:Displacement/sld:DisplacementX", doc);
        assertXpathEvaluatesTo("10.0", "//sld:Graphic/sld:Displacement/sld:DisplacementY", doc);
        assertXpathEvaluatesTo("1.0", "//sld:Graphic/sld:AnchorPoint/sld:AnchorPointX", doc);
        assertXpathEvaluatesTo("0.3", "//sld:Graphic/sld:AnchorPoint/sld:AnchorPointY", doc);
    }

    @Test
    public void testFeatureTypeStyleOptions() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        Style style = sb.createStyle(sb.createPolygonSymbolizer());
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        fts.getOptions().put("key", "value");

        SLDTransformer st = new SLDTransformer();
        String xml = st.transform(style);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("1", "count(//sld:FeatureTypeStyle/sld:VendorOption)", doc);
        assertXpathEvaluatesTo(
                "value", "//sld:FeatureTypeStyle/sld:VendorOption[@name='key']", doc);
    }

    private Style validateWellKnownNameWithExpressionStyle(String xmlStyle) {

        StringReader stringReader = new StringReader(xmlStyle);
        SLDParser sldParser = new SLDParser(sf, stringReader);
        Style[] parsedStyles = sldParser.readXML();
        assertNotNull("parsing xml style returns null", parsedStyles);
        assertTrue("more or less that one style is available", parsedStyles.length == 1);
        Style style = parsedStyles[0];

        assertNotNull("style is null", style);
        assertNotNull("feature type styles are null", style.featureTypeStyles());
        assertTrue(
                "more or less that one feature type style is available",
                style.featureTypeStyles().size() == 1);
        assertNotNull("rules are null", style.featureTypeStyles().get(0).rules());
        assertTrue(
                "more or less that one rule is available",
                style.featureTypeStyles().get(0).rules().size() == 1);
        Rule rule = style.featureTypeStyles().get(0).rules().get(0);
        assertNotNull("rule is null", rule);

        List<? extends Symbolizer> symbolizers = rule.symbolizers();
        assertNotNull("symbolizers are null", symbolizers);
        assertTrue("more or less that one symbolizer is available", symbolizers.size() == 1);
        PointSymbolizer pointSymbolizer = (PointSymbolizer) symbolizers.get(0);
        assertNotNull("point symbolizer is null", pointSymbolizer);

        Graphic graphic = pointSymbolizer.getGraphic();
        assertNotNull("graphic is null", graphic);
        assertNotNull("graphic symbols are null", graphic.graphicalSymbols());
        assertTrue(
                "more or less that one graphic symbol is available",
                graphic.graphicalSymbols().size() == 1);

        Mark mark = (Mark) graphic.graphicalSymbols().get(0);
        assertNotNull("mark is null", mark);
        assertNotNull("mark wellKnownName is null", mark.getWellKnownName());
        assertTrue("wellKnownName is not a function", mark.getWellKnownName() instanceof Function);

        Function function = (Function) mark.getWellKnownName();
        assertTrue(
                "wellKnownName function is not strConcat", function.getName().equals("strConcat"));
        assertTrue(
                "wellKnownName function have a wrong number of parameters",
                function.getParameters().size() == 2);

        Expression firstParameter = function.getParameters().get(0);
        assertNotNull("first parameter is null", firstParameter);
        assertTrue("first parameter is not a literal", firstParameter instanceof Literal);

        Literal literal = (Literal) firstParameter;
        assertTrue("literal value is different of 'mark-'", literal.getValue().equals("mark-"));

        Expression secondParameter = function.getParameters().get(1);
        assertNotNull("second parameter is null", secondParameter);
        assertTrue("second parameter is", secondParameter instanceof PropertyName);

        PropertyName propertyName = (PropertyName) secondParameter;
        assertTrue(
                "property name is different of 'MARK_NAME'",
                propertyName.getPropertyName().equals("MARK_NAME"));

        return style;
    }

    @Test
    public void testContrastEnhancement() throws Exception {
        StyleBuilder sb = new StyleBuilder();

        ContrastEnhancement ce = new ContrastEnhancementImpl();
        NormalizeContrastMethodStrategy normal = new NormalizeContrastMethodStrategy();
        normal.setAlgorithm(ff.literal("ClipToMinimumMaximum"));
        normal.addParameter("p1", ff.literal(false));
        normal.addParameter("p2", ff.literal(23.5d));
        ce.setMethod(normal);
        SLDTransformer st = new SLDTransformer();
        String xml = st.transform(ce);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);
        assertXpathExists("//sld:ContrastEnhancement/sld:Normalize", doc);
        assertXpathEvaluatesTo(
                "false",
                "//sld:ContrastEnhancement/sld:Normalize/sld:VendorOption[@name='p1']",
                doc);
        assertXpathEvaluatesTo(
                "ClipToMinimumMaximum",
                "//sld:ContrastEnhancement/sld:Normalize/sld:VendorOption[@name='algorithm']",
                doc);

        HistogramContrastMethodStrategy hist = new HistogramContrastMethodStrategy();
        ce.setMethod(hist);
        xml = st.transform(ce);
        String skeleton =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sld:ContrastEnhancement xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\"><sld:Histogram/></sld:ContrastEnhancement>";
        // System.out.println(xml);
        Diff myDiff = new Diff(skeleton, xml);

        assertTrue("test XML matches control skeleton XML " + myDiff, myDiff.similar());
        // assertXpathNotExists("//sld:ContrastEnhancement/sld:Histogram/sld:Algorithm",doc);

        LogarithmicContrastMethodStrategy log = new LogarithmicContrastMethodStrategy();
        ce.setMethod(log);
        xml = st.transform(ce);
        // System.out.println(xml);
        skeleton = skeleton.replace("Histogram", "Logarithmic");
        myDiff = new Diff(skeleton, xml);

        assertTrue("test XML matches control skeleton XML " + myDiff, myDiff.similar());

        ExponentialContrastMethodStrategy exp = new ExponentialContrastMethodStrategy();
        ce.setMethod(exp);
        xml = st.transform(ce);
        // System.out.println(xml);
        skeleton = skeleton.replace("Logarithmic", "Exponential");
        myDiff = new Diff(skeleton, xml);

        assertTrue("test XML matches control skeleton XML " + myDiff, myDiff.similar());
    }

    @Test
    public void testGammaValueExpressionContrastEnhancement() throws Exception {
        StyleBuilder sb = new StyleBuilder();

        ContrastEnhancement ce = new ContrastEnhancementImpl();
        ce.setGammaValue(ff.add(ff.literal(1.0), ff.literal(0.5)));
        SLDTransformer st = new SLDTransformer();
        String xml = st.transform(ce);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);
        assertXpathExists("//sld:ContrastEnhancement/sld:GammaValue", doc);
        assertXpathExists("//sld:ContrastEnhancement/sld:GammaValue/ogc:Add", doc);

        String skeleton =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sld:ContrastEnhancement xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\"><sld:GammaValue><ogc:Add><ogc:Literal>1.0</ogc:Literal><ogc:Literal>0.5</ogc:Literal></ogc:Add></sld:GammaValue></sld:ContrastEnhancement>";
        // System.out.println(xml);
        Diff myDiff = new Diff(skeleton, xml);

        assertTrue("test XML matches control skeleton XML " + myDiff, myDiff.similar());
    }

    @Test
    public void testMultipleFontsUniform() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        Font f1 = sb.createFont("Arial", 10);
        Font f2 = sb.createFont("Comic Sans MS", 10);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, new Font[] {f1, f2}, "label");

        SLDTransformer st = new SLDTransformer();
        String xml = st.transform(ts);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("1", "count(//sld:TextSymbolizer/sld:Font)", doc);
        assertXpathEvaluatesTo(
                "2",
                "count(//sld:TextSymbolizer/sld:Font/sld:CssParameter[@name=\"font-family\"])",
                doc);
        assertXpathEvaluatesTo(
                "Arial",
                "//sld:TextSymbolizer/sld:Font/sld:CssParameter[@name=\"font-family\"][1]",
                doc);
        assertXpathEvaluatesTo(
                "Comic Sans MS",
                "//sld:TextSymbolizer/sld:Font/sld:CssParameter[@name=\"font-family\"][2]",
                doc);
        assertXpathEvaluatesTo(
                "10.0", "//sld:TextSymbolizer/sld:Font/sld:CssParameter[@name=\"font-size\"]", doc);
    }

    @Test
    public void testMultipleFontsNotUniform() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        Font f1 = sb.createFont("Arial", 10);
        Font f2 = sb.createFont("Comic Sans MS", 12);
        TextSymbolizer ts = sb.createTextSymbolizer(Color.BLACK, new Font[] {f1, f2}, "label");

        SLDTransformer st = new SLDTransformer();
        String xml = st.transform(ts);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo("2", "count(//sld:TextSymbolizer/sld:Font)", doc);
        // <sld:CssParameter name="font-family">Comic Sans MS</sld:CssParameter>
        assertXpathEvaluatesTo(
                "1",
                "count(//sld:TextSymbolizer/sld:Font[1]/sld:CssParameter[@name=\"font-family\"])",
                doc);
        assertXpathEvaluatesTo(
                "Arial",
                "//sld:TextSymbolizer/sld:Font[1]/sld:CssParameter[@name=\"font-family\"][1]",
                doc);
        assertXpathEvaluatesTo(
                "1",
                "count(//sld:TextSymbolizer/sld:Font[2]/sld:CssParameter[@name=\"font-family\"])",
                doc);
        assertXpathEvaluatesTo(
                "Comic Sans MS",
                "//sld:TextSymbolizer/sld:Font[2]/sld:CssParameter[@name=\"font-family\"]",
                doc);
        assertXpathEvaluatesTo(
                "10.0", "//sld:TextSymbolizer/sld:Font/sld:CssParameter[@name=\"font-size\"]", doc);
    }

    @Test
    public void testLineOffsetExpression() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        LineSymbolizer ls = sb.createLineSymbolizer(Color.RED);
        ls.setPerpendicularOffset(ff.multiply(ff.property("a"), ff.literal(2)));

        SLDTransformer st = new SLDTransformer();
        st.setIndentation(2);
        String xml = st.transform(ls);
        // System.out.println(xml);
        Document doc = buildTestDocument(xml);

        assertXpathEvaluatesTo(
                "a", "//sld:LineSymbolizer/sld:PerpendicularOffset/ogc:Mul/ogc:PropertyName", doc);
        assertXpathEvaluatesTo(
                "2", "//sld:LineSymbolizer/sld:PerpendicularOffset/ogc:Mul/ogc:Literal", doc);
    }

    /**
     * See https://osgeo-org.atlassian.net/browse/GEOT-5613
     *
     * @throws Exception
     */
    @Test
    public void testDefaults() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        LineSymbolizer ls = sb.createLineSymbolizer(Color.BLACK);

        SLDTransformer st = new SLDTransformer();
        st.setExportDefaultValues(true);
        st.setIndentation(2);
        String xml = st.transform(ls);

        Document doc = buildTestDocument(xml);

        assertXpathExists("//sld:LineSymbolizer/sld:Stroke", doc);
        assertXpathEvaluatesTo(
                "#000000", "//sld:LineSymbolizer/sld:Stroke/sld:CssParameter[@name='stroke']", doc);

        st.setExportDefaultValues(false);
        xml = st.transform(ls);
        // System.out.println(xml);
        doc = buildTestDocument(xml);

        assertXpathNotExists(
                "//sld:LineSymbolizer/sld:Stroke/sld:CssParameter[@name='stroke']", doc);

        st.setExportDefaultValues(true);

        PolygonSymbolizer ps = sb.createPolygonSymbolizer(Color.GRAY, Color.black, 1.0);

        xml = st.transform(ps);

        doc = buildTestDocument(xml);

        assertXpathExists("//sld:PolygonSymbolizer/sld:Stroke", doc);
        assertXpathEvaluatesTo(
                "#000000",
                "//sld:PolygonSymbolizer/sld:Stroke/sld:CssParameter[@name='stroke']",
                doc);
        assertXpathExists("//sld:PolygonSymbolizer/sld:Fill", doc);
        assertXpathEvaluatesTo(
                "#808080", "//sld:PolygonSymbolizer/sld:Fill/sld:CssParameter[@name='fill']", doc);

        st.setExportDefaultValues(false);
        xml = st.transform(ps);
        // System.out.println(xml);
        doc = buildTestDocument(xml);
        assertXpathExists("//sld:PolygonSymbolizer/sld:Stroke", doc);
        assertXpathNotExists(
                "//sld:PolygonSymbolizer/sld:Stroke/sld:CssParameter[@name='stroke']", doc);
        assertXpathExists("//sld:PolygonSymbolizer/sld:Fill", doc);
        assertXpathNotExists(
                "//sld:PolygonSymbolizer/sld:Fill/sld:CssParameter[@name='fill']", doc);

        st.setExportDefaultValues(true);
        PointSymbolizer pos =
                sb.createPointSymbolizer(sb.createGraphic(null, sb.createMark("square"), null));

        xml = st.transform(pos);
        // System.out.println(xml);
        doc = buildTestDocument(xml);
        assertXpathExists("//sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Stroke", doc);
        assertXpathEvaluatesTo(
                "#000000",
                "//sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Stroke/sld:CssParameter[@name='stroke']",
                doc);
        assertXpathExists("//sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Fill", doc);
        assertXpathEvaluatesTo(
                "#808080",
                "//sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Fill/sld:CssParameter[@name='fill']",
                doc);
        assertXpathExists("//sld:WellKnownName", doc);

        st.setExportDefaultValues(false);
        xml = st.transform(pos);
        // System.out.println(xml);
        doc = buildTestDocument(xml);
        assertXpathExists("//sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Stroke", doc);
        assertXpathNotExists(
                "//sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Stroke/sld:CssParameter[@name='stroke']",
                doc);
        assertXpathExists("//sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Fill", doc);
        assertXpathNotExists(
                "//sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Fill/sld:CssParameter[@name='fill']",
                doc);
        assertXpathNotExists("//sld:WellKnownName", doc);
    }

    private StyledLayerDescriptor buildSLDAroundSymbolizer(
            org.geotools.styling.Symbolizer symbolizer) {
        StyleBuilder sb = new StyleBuilder();
        Style s = sb.createStyle(symbolizer);
        s.setDefault(true);
        StyleFactory sf = sb.getStyleFactory();
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        NamedLayer layer = sf.createNamedLayer();
        layer.setName("layerName");
        layer.addStyle(s);
        sld.addStyledLayer(layer);
        return sld;
    }
}
