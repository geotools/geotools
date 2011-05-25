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
package org.geotools.styling;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLUnit.buildTestDocument;
import static org.custommonkey.xmlunit.XMLUnit.setXpathNamespaceContext;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Rule;
import org.opengis.style.Symbolizer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This test case captures specific problems encountered with the SLDTransformer code.
 * <p>
 * Please note that SLDTransformer is specifically targeted at SLD 1.0; for new code you should be
 * using the SLD 1.0 (or SE 1.1) xml-xsd bindings.
 * </p>
 * 
 * @author Jody
 *
 *
 * @source $URL$
 */
public class SLDTransformerTest {
    static StyleFactory2 sf = (StyleFactory2) CommonFactoryFinder.getStyleFactory(null);

    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

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
        rasterSymbolizer.setOpacity((Expression) CommonFactoryFinder.getFilterFactory(
                GeoTools.getDefaultHints()).literal(0.25));

        // set channel selection
        ChannelSelectionImpl csi = new ChannelSelectionImpl();
        // red
        SelectedChannelTypeImpl redChannel = new SelectedChannelTypeImpl();
        redChannel.setChannelName("1");
        ContrastEnhancementImpl rcei = new ContrastEnhancementImpl();
        rcei.setHistogram();
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
        bcei.setNormalize();
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
        assertEquals(0.25, SLD.rasterOpacity(out));
    }

    /**
     * This is a problem reported from uDig 1.2; we are trying to save a LineSymbolizer (and then
     * restore it) and the stroke is comming back black and with width 1 all the time.
     * 
     * @throws Exception
     */
    @Test
    public void testStroke() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sld:UserStyle xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\"><sld:Name>Default Styler</sld:Name><sld:Title>Default Styler</sld:Title><sld:FeatureTypeStyle><sld:Name>simple</sld:Name><sld:Title>title</sld:Title><sld:Abstract>abstract</sld:Abstract><sld:FeatureTypeName>Feature</sld:FeatureTypeName><sld:SemanticTypeIdentifier>generic:geometry</sld:SemanticTypeIdentifier><sld:SemanticTypeIdentifier>simple</sld:SemanticTypeIdentifier><sld:Rule><sld:Title>title</sld:Title><sld:Abstract>abstract</sld:Abstract><sld:MaxScaleDenominator>1.7976931348623157E308</sld:MaxScaleDenominator><sld:LineSymbolizer><sld:Stroke><sld:CssParameter name=\"stroke\"><ogc:Literal>#0000FF</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-linecap\"><ogc:Literal>butt</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-linejoin\"><ogc:Literal>miter</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-opacity\"><ogc:Literal>1.0</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-width\"><ogc:Literal>2.0</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-dashoffset\"><ogc:Literal>0.0</ogc:Literal></sld:CssParameter></sld:Stroke></sld:LineSymbolizer></sld:Rule></sld:FeatureTypeStyle></sld:UserStyle>";
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
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
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
                + "                              </Rule>" + "                              <Rule>"
                + "                              <TextSymbolizer>"
                + "                <Label><ogc:PropertyName>name</ogc:PropertyName></Label>"
                + "                <Font>"
                + "                    <CssParameter name=\"font-family\">Arial</CssParameter>"
                + "                    <CssParameter name=\"font-style\">normal</CssParameter>"
                + "                    <CssParameter name=\"font-size\">12</CssParameter>"
                + "                    <CssParameter name=\"font-weight\">normal</CssParameter>"
                + "                </Font>" + "                <LabelPlacement>"
                + "                      <LinePlacement>"
                + "                              <PerpendicularOffset>0</PerpendicularOffset>"
                + "                      </LinePlacement>" + "                </LabelPlacement>"
                + "                </TextSymbolizer>" + "                              </Rule>"
                + "                  </FeatureTypeStyle>" + "              </UserStyle>"
                + "      </NamedLayer>" + "</StyledLayerDescriptor>";

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
     * Another bug reported from uDig 1.2; we are trying to save a LineSymbolizer (and then restore
     * it) and the stroke is comming back black and with width 1 all the time.
     * 
     * @throws Exception
     */
    @Test
    public void testPointSymbolizer() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
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
                + "                    </sld:PointSymbolizer>" + "                </sld:Rule>"
                + "            </sld:FeatureTypeStyle>" + "        </sld:UserStyle>"
                + "    </sld:UserLayer>" + "</sld:StyledLayerDescriptor>";

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
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sld:UserStyle xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\"><sld:Name>Default Styler</sld:Name><sld:Title>Default Styler</sld:Title><sld:FeatureTypeStyle><sld:Name>simple</sld:Name><sld:Title>title</sld:Title><sld:Abstract>abstract</sld:Abstract><sld:FeatureTypeName>Feature</sld:FeatureTypeName><sld:SemanticTypeIdentifier>generic:geometry</sld:SemanticTypeIdentifier><sld:SemanticTypeIdentifier>simple</sld:SemanticTypeIdentifier><sld:Rule><sld:Title>title</sld:Title><sld:Abstract>abstract</sld:Abstract><sld:MaxScaleDenominator>1.7976931348623157E308</sld:MaxScaleDenominator><sld:LineSymbolizer><sld:Stroke><sld:CssParameter name=\"stroke\"><ogc:Literal>#0000FF</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-linecap\"><ogc:Literal>butt</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-linejoin\"><ogc:Literal>miter</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-opacity\"><ogc:Literal>1.0</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-width\"><ogc:Literal>2.0</ogc:Literal></sld:CssParameter><sld:CssParameter name=\"stroke-dashoffset\"><ogc:Literal>0.0</ogc:Literal></sld:CssParameter></sld:Stroke></sld:LineSymbolizer></sld:Rule></sld:FeatureTypeStyle></sld:UserStyle>";
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

            PointSymbolizer pointSymbolizer2 = 
                parser.parsePointSymbolizer( dom.getFirstChild());
            
            assertTrue(pointSymbolizer.getUnitOfMeasure().equals(
                    pointSymbolizer2.getUnitOfMeasure()));
        } catch (Exception e) {
            e.printStackTrace();
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

        PolygonSymbolizer polygonSymbolizer2 = 
            parser.parsePolygonSymbolizer(dom.getFirstChild());
        
        assertTrue(polygonSymbolizer.getUnitOfMeasure().equals(
                polygonSymbolizer2.getUnitOfMeasure()));
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

        RasterSymbolizer rasterSymbolizer2 = 
            parser.parseRasterSymbolizer(dom.getFirstChild());

        assertTrue(rasterSymbolizer.getUnitOfMeasure().equals(rasterSymbolizer2.getUnitOfMeasure()));
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

        LineSymbolizer lineSymbolizer2 = 
            parser.parseLineSymbolizer(dom.getFirstChild());
        
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

        TextSymbolizer textSymbolizer2 = 
            parser.parseTextSymbolizer( dom.getFirstChild());

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
            e.printStackTrace();
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
    
    /**
     * The displacement tag has not been exported to XML for a while...
     */
    @Test
    public void testDisplacement() throws Exception {
        StyleBuilder sb = new StyleBuilder();
        
        Graphic graphic;
        graphic = sb.createGraphic();
        Displacement disp = sb.createDisplacement(10.1, -5.5);
        graphic.setDisplacement(disp);
        
        SLDTransformer st = new SLDTransformer();     
        String xml = st.transform(graphic);
        
        assertTrue("XML transformation of this GraphicImpl does not contain the word 'Displacement' ", xml.contains("Displacement"));
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
    

    /**                 
     * Checks whether the "Priority" parameter of a TextSymbolizer is correctly stored and loaded
     */
    @Test
    public void testPriorityTransformOutAndIn() throws Exception {
        StyleBuilder sb = new StyleBuilder();

        TextSymbolizer ts = sb.createTextSymbolizer();
        PropertyName literalPrio = CommonFactoryFinder.getFilterFactory2(null).property(
                "quantVariable");
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
            TextSymbolizer reimportedTs = (TextSymbolizer) reimportedStyle.featureTypeStyles().get(
                    0).rules().get(0).symbolizers().get(0);
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
            TextSymbolizer reimportedTs = (TextSymbolizer) reimportedStyle.featureTypeStyles().get(
                    0).rules().get(0).symbolizers().get(0);
            assertNotNull(reimportedTs.getPriority());
            assertEquals("quantVariable", reimportedTs.getPriority().toString());

        }
    }
    
    
    /**
     * SLD Transformer did't save the type of the colormap
     */
    @Test
    public void testColorMap() throws Exception {
    	SLDTransformer st = new SLDTransformer();
      ColorMap cm = sf.createColorMap();
      
      // Test type = values
      cm.setType(ColorMap.TYPE_VALUES);
      assertTrue("parsed xml must contain attribbute type with correct value", st.transform(cm).contains("type=\"values\""));
      
      // Test type = intervals
      cm.setType(ColorMap.TYPE_INTERVALS);
      assertTrue("parsed xml must contain attribbute type with correct value", st.transform(cm).contains("type=\"intervals\""));
      
      // Test type = ramp
      cm.setType(ColorMap.TYPE_RAMP);
      assertEquals("parsed xml must contain attribbute type with correct value", -1, st.transform(cm).indexOf("type="));
    }
    
    /**
     * Checks the output of encoding a default line symbolizer does not include all the default values
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
        assertXpathEvaluatesTo("#FFFF00", "/sld:LineSymbolizer/sld:Stroke/sld:CssParameter[@name='stroke']", doc);
        assertXpathEvaluatesTo("3", "/sld:LineSymbolizer/sld:Stroke/sld:CssParameter[@name='stroke-width']", doc);
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
        assertXpathEvaluatesTo("#0000FF", "/sld:PolygonSymbolizer/sld:Fill/sld:CssParameter[@name='fill']", doc);
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
        assertXpathEvaluatesTo("0", "count(/sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Fill/*)", doc);
        assertXpathEvaluatesTo("0", "count(/sld:PointSymbolizer/sld:Graphic/sld:Mark/sld:Stroke/*)", doc);
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
        assertXpathEvaluatesTo("1", "count(/sld:RasterSymbolizer/*)", doc);
        assertXpathEvaluatesTo("grid", "/sld:RasterSymbolizer/sld:Geometry/ogc:PropertyName", doc);
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
        assertXpathEvaluatesTo("1", "count(/sld:UserStyle/sld:FeatureTypeStyle/sld:Rule/sld:PointSymbolizer)", doc);
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
        
        assertXpathEvaluatesTo("1", "/sld:StyledLayerDescriptor/sld:NamedLayer/sld:UserStyle/sld:IsDefault", doc);
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
        
        assertXpathEvaluatesTo("random", "/sld:UserStyle/sld:FeatureTypeStyle/sld:Rule/sld:PointSymbolizer/sld:Graphic/sld:Size/ogc:Function/@name", doc);
    }
    

    /**
     * TextSymbolizer2 specific properties saved and laoded again must fit
     */
    @Test
    public void textTextSymbolizer2_InAndOut() throws TransformerException, SAXException, IOException, XpathException {
    	StyleBuilder sb = new StyleBuilder(); 
    	
    	TextSymbolizer2 ts2 = (TextSymbolizer2)sf.createTextSymbolizer();
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
    	Style importedStyle = (Style) sldParser.readXML()[0];
    	TextSymbolizer2 copy = (TextSymbolizer2)importedStyle.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);

        // compare it
        assertEquals("Graphic of TextSymbolizer2 has not been correctly ex- and reimported", gr.getOpacity(), copy
                .getGraphic().getOpacity());
        assertEquals("Graphic of TextSymbolizer2 has not been correctly ex- and reimported", gr.getSize(), copy
                .getGraphic().getSize());        
        assertEquals("Snippet of TextSymbolizer2 has not been correctly ex- and reimported", snippet, copy
                .getSnippet());
        assertEquals("FeatureDescription of TextSymbolizer2 has not been correctly ex- and reimported", fD, copy
                .getFeatureDescription());
        assertEquals("OtherText of TextSymbolizer2 has not been correctly ex- and reimported", otherText.getTarget(), copy
                .getOtherText().getTarget());
        assertEquals("OtherText of TextSymbolizer2 has not been correctly ex- and reimported", otherText.getText(), copy
                .getOtherText().getText());        
        
    }
}
