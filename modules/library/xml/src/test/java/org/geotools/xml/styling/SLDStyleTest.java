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

import static org.junit.Assert.assertArrayEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.function.FilterFunction_buffer;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedStyle;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.styling.UserLayer;
import org.geotools.test.TestData;
import org.geotools.util.factory.GeoTools;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.style.ContrastMethod;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.SemanticType;

/**
 * Try out our SLD parser and see how well it does.
 *
 * @author jamesm
 */
public class SLDStyleTest {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
    StyleBuilder sb = new StyleBuilder(sf, ff);

    /** Test of parseStyle method, of class org.geotools.styling.SLDStyle. */
    @Test
    public void testParseStyle() throws Exception {
        // java.net.URL base = getClass().getResource("testData/");
        // base = getClass().getResource("testData");
        // base = getClass().getResource("/testData");

        // java.net.URL surl = new java.net.URL(base + "/test-sld.xml");
        java.net.URL surl = TestData.getResource(this, "test-sld.xml");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();
        Assert.assertEquals("My Layer", sld.getName());
        Assert.assertEquals("A layer by me", sld.getTitle());
        Assert.assertEquals("this is a sample layer", sld.getAbstract());
        Assert.assertEquals(1, sld.getStyledLayers().length);

        UserLayer layer = (UserLayer) sld.getStyledLayers()[0];
        Assert.assertNull(layer.getName());
        Assert.assertEquals(1, layer.getUserStyles().length);

        Style style = layer.getUserStyles()[0];
        Assert.assertEquals(1, style.featureTypeStyles().size());
        Assert.assertEquals("My User Style", style.getName());
        Assert.assertEquals("A style by me", style.getDescription().getTitle().toString());
        Assert.assertEquals(
                "this is a sample style", style.getDescription().getAbstract().toString());
        Assert.assertTrue(style.isDefault());

        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        Rule rule = fts.rules().get(0);
        LineSymbolizer lineSym = (LineSymbolizer) rule.symbolizers().get(0);
        Assert.assertEquals(
                4, lineSym.getStroke().getWidth().evaluate(null, Number.class).intValue());
    }

    /** XML --> SLD --> XML */
    @Test
    public void testSLDParser() throws Exception {
        java.net.URL surl = TestData.getResource(this, "example-sld.xml");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        // convert back to xml again
        SLDTransformer aTransformer = new SLDTransformer();
        String xml = aTransformer.transform(sld);

        Assert.assertNotNull(xml);
        // we're content for the moment if this didn't throw an exception...
        // TODO: convert the buffer/resource to a string and compare
    }

    /** XML --> SLD --> XML */
    @Test
    public void testSLDParserWithLocalizedTitle() throws Exception {
        java.net.URL surl = TestData.getResource(this, "example-localized-sld.xml");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        // convert back to xml again
        SLDTransformer aTransformer = new SLDTransformer();
        String xml = aTransformer.transform(sld);

        Assert.assertNotNull(xml);
        Assert.assertTrue(xml.contains("<sld:Title>title"));
        Assert.assertTrue(
                xml.contains(
                        "<sld:Localized lang=\""
                                + Locale.ITALIAN.toString()
                                + "\">titolo</sld:Localized>"));
        Assert.assertTrue(
                xml.contains(
                        "<sld:Localized lang=\""
                                + Locale.FRENCH.toString()
                                + "\">titre</sld:Localized>"));
        Assert.assertTrue(
                xml.contains(
                        "<sld:Localized lang=\""
                                + Locale.CANADA_FRENCH.toString()
                                + "\">titre</sld:Localized>"));
    }

    @Test
    public void testEmptyElements() throws Exception {
        // before GEOT-3042 this would simply fail with an NPE
        java.net.URL surl = TestData.getResource(this, "test-empty-elements.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        Assert.assertEquals(1, ((UserLayer) sld.getStyledLayers()[0]).getUserStyles().length);
        Style style = ((UserLayer) sld.getStyledLayers()[0]).getUserStyles()[0];
        Assert.assertEquals(1, style.featureTypeStyles().size());
        Assert.assertEquals(1, style.featureTypeStyles().get(0).rules().size());
        Assert.assertEquals(
                1, style.featureTypeStyles().get(0).rules().get(0).symbolizers().size());
    }

    @Test
    public void testDashArray1() throws Exception {
        // plain text in dasharray
        java.net.URL surl = TestData.getResource(this, "dasharray1.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        Stroke stroke = validateDashArrayStyle(sld);
        assertArrayEquals(new float[] {2.0f, 1.0f, 4.0f, 1.0f}, stroke.getDashArray(), 0f);
    }

    @Test
    public void testDashArray2() throws Exception {
        // using ogc:Literal in dasharray
        java.net.URL surl = TestData.getResource(this, "dasharray2.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        Stroke stroke = validateDashArrayStyle(sld);
        assertArrayEquals(new float[] {2.0f, 1.0f, 4.0f, 1.0f}, stroke.getDashArray(), 0f);
    }

    @Test
    public void testDashArray3() throws Exception {
        // using expressions in the dasharray
        java.net.URL surl = TestData.getResource(this, "dasharray3.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        List<Expression> expressions = validateDashArrayStyle(sld).dashArray();

        Assert.assertEquals("more or less expressions available", 4, expressions.size());
        Assert.assertEquals("not expected expression", expressions.get(0), ff.property("stroke1"));
        Assert.assertEquals("not expected expression", expressions.get(1), ff.literal(1.0));
        Assert.assertEquals("not expected expression", expressions.get(2), ff.property("stroke2"));
        Assert.assertEquals("not expected expression", expressions.get(3), ff.literal(2.0));
    }

    @Test
    public void testDashArray3_dynamic() throws Exception {
        java.net.URL surl = TestData.getResource(this, "dasharray3_dynamic.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        validateDynamicDashArrayStyle(sld);
    }

    private Stroke validateDashArrayStyle(StyledLayerDescriptor sld) {
        Assert.assertEquals(1, ((UserLayer) sld.getStyledLayers()[0]).getUserStyles().length);
        Style style = ((UserLayer) sld.getStyledLayers()[0]).getUserStyles()[0];
        List<FeatureTypeStyle> fts = style.featureTypeStyles();
        Assert.assertEquals(1, fts.size());
        List<Rule> rules = fts.get(0).rules();
        Assert.assertEquals(1, rules.size());
        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        Assert.assertEquals(1, symbolizers.size());

        LineSymbolizer ls = (LineSymbolizer) symbolizers.get(0);
        Assert.assertNotNull("line symbolizer is null", ls);

        Stroke stroke = ls.getStroke();
        Assert.assertNotNull("stroke is null", stroke);
        Assert.assertNotNull("stroke dasharray is null", stroke.dashArray());

        return stroke;
    }

    private void validateDynamicDashArrayStyle(StyledLayerDescriptor sld) {
        Assert.assertEquals(1, ((UserLayer) sld.getStyledLayers()[0]).getUserStyles().length);
        Style style = ((UserLayer) sld.getStyledLayers()[0]).getUserStyles()[0];
        List<FeatureTypeStyle> fts = style.featureTypeStyles();
        Assert.assertEquals(1, fts.size());
        List<Rule> rules = fts.get(0).rules();
        Assert.assertEquals(1, rules.size());
        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        Assert.assertEquals(1, symbolizers.size());

        LineSymbolizer ls = (LineSymbolizer) symbolizers.get(0);
        Assert.assertNotNull(ls.getStroke().dashArray());
        List<Expression> e = ls.getStroke().dashArray();
        Assert.assertEquals(1, e.size());
        Assert.assertEquals("2.0 1.0 4.0 1.0", e.get(0).evaluate(null));
    }

    @Test
    public void testSLDParserWithWhitespaceIsTrimmed() throws Exception {
        java.net.URL surl = TestData.getResource(this, "whitespace.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        TextSymbolizer ts =
                (TextSymbolizer)
                        ((NamedLayer) sld.getStyledLayers()[0])
                                .getStyles()[0]
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        PropertyName property = (PropertyName) ts.getLabel();
        Assert.assertEquals("testProperty", property.getPropertyName());

        Expression color = ts.getFill().getColor();
        Assert.assertEquals(Color.BLACK, SLD.color(color));
    }

    @Test
    public void testSLDParserWithhMixedContent() throws Exception {
        java.net.URL surl = TestData.getResource(this, "mixedContent.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        List<Symbolizer> symbolizers =
                ((NamedLayer) sld.getStyledLayers()[0])
                        .getStyles()[0]
                        .featureTypeStyles()
                        .get(0)
                        .rules()
                        .get(0)
                        .symbolizers();

        PolygonSymbolizer polygon = (PolygonSymbolizer) symbolizers.get(0);
        TextSymbolizer text = (TextSymbolizer) symbolizers.get(1);

        Expression fill = polygon.getFill().getColor();
        Expression label = text.getLabel();

        String fillValue = fill.evaluate(null, String.class);
        String labelValue = label.evaluate(null, String.class);

        Assert.assertEquals("#96C3F5", fillValue);
        Assert.assertEquals(
                "this is a prefix; this is an expression; this is a postfix", labelValue);
    }

    @Test
    public void testSLDExtendedColorMap() throws Exception {
        java.net.URL surl = TestData.getResource(this, "colormap.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        RasterSymbolizer rs =
                (RasterSymbolizer)
                        ((UserLayer) sld.getStyledLayers()[0])
                                .userStyles()
                                .get(0)
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        Assert.assertTrue(rs.getColorMap().getExtendedColors());
        Assert.assertEquals(rs.getColorMap().getType(), ColorMap.TYPE_RAMP);
    }

    @Test
    public void testSLDParserWithhMixedContentCDATA() throws Exception {
        java.net.URL surl = TestData.getResource(this, "mixedContentWithCDATA.xml");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        TextSymbolizer text =
                (TextSymbolizer)
                        ((NamedLayer) sld.getStyledLayers()[0])
                                .getStyles()[0]
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        Expression label = text.getLabel();

        String labelValue = label.evaluate(null, String.class);

        Assert.assertEquals("literal_1\n cdata literal_2", labelValue);
    }

    @Test
    public void testSLDParserWithhMixedContentCDATASpaces() throws Exception {
        java.net.URL surl = TestData.getResource(this, "mixedContentWithCDATASpaces.xml");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        TextSymbolizer text =
                (TextSymbolizer)
                        ((NamedLayer) sld.getStyledLayers()[0])
                                .getStyles()[0]
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        Expression label = text.getLabel();

        String labelValue = label.evaluate(null, String.class);

        Assert.assertEquals("literal_1\nliteral_2", labelValue);
    }

    @Test
    public void testSLDParserWithFuncConcatenateCDATASpaces() throws Exception {
        java.net.URL surl = TestData.getResource(this, "funcConcatenateWithCDATASpaces.xml");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        TextSymbolizer text =
                (TextSymbolizer)
                        ((NamedLayer) sld.getStyledLayers()[0])
                                .getStyles()[0]
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        Expression label = text.getLabel();

        String labelValue = label.evaluate(null, String.class);

        // System.out.println(labelValue);

        Assert.assertEquals("literal_1\n literal_2", labelValue);
    }

    @Test
    public void testStrokeCssParameter() throws Exception {
        java.net.URL surl = TestData.getResource(this, "strokeParam.sld");
        SLDParser stylereader = new SLDParser(sf, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        PolygonSymbolizer ps =
                (PolygonSymbolizer)
                        ((NamedLayer) sld.getStyledLayers()[0])
                                .getStyles()[0]
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        Expression color = ps.getStroke().getColor();
        Expression expected =
                ff.function(
                        "strConcat",
                        ff.literal("#"),
                        ff.function("env", ff.literal("stroke"), ff.literal("0000FF")));
        Assert.assertEquals(expected, color);
    }

    /** SLD --> XML --> SLD */
    @Test
    public void testSLDTransformer() throws Exception {
        // create an SLD
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        sld.setName("SLD Name");
        sld.setTitle("SLD Title");
        UserLayer layer = sf.createUserLayer();
        layer.setName("UserLayer Name");

        Style style = sf.createStyle();
        style.setName("Style Name");
        style.getDescription().setTitle("Style Title");
        Rule rule1 = sb.createRule(sb.createLineSymbolizer(new Color(0), 2));
        // note: symbolizers containing a fill will likely fail, as the SLD
        // transformation loses a little data (background colour?)
        FeatureTypeStyle fts1 = sf.createFeatureTypeStyle(rule1);
        fts1.semanticTypeIdentifiers().add(SemanticType.valueOf("generic:geometry"));
        style.featureTypeStyles().add(fts1);
        layer.setUserStyles(style);
        sld.setStyledLayers(layer);

        // convert it to XML
        SLDTransformer aTransformer = new SLDTransformer();
        String xml = aTransformer.transform(sld);

        // back to SLD
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        SLDParser stylereader = new SLDParser(sf, is);

        StyledLayerDescriptor sld2 = stylereader.parseSLD();
        // UNCOMMENT FOR DEBUGGING
        //        assertEquals(SLD.rules(SLD.styles(sld)[0]).length,
        // SLD.rules(SLD.styles(sld2)[0]).length);
        //        for (int i = 0; i < SLD.rules(SLD.styles(sld)[0]).length; i++) {
        //            Rule aRule = SLD.rules(SLD.styles(sld)[0])[i];
        //            Rule bRule = SLD.rules(SLD.styles(sld2)[0])[i];
        //            System.out.println(i+":"+aRule);
        //        	Symbolizer[] symb1 = SLD.symbolizers(aRule);
        //        	Symbolizer[] symb2 = SLD.symbolizers(bRule);
        //        	for (int j = 0; j < symb1.length; j++) {
        //        		//symbolizers are equal
        //        		assertTrue(symb1[j].equals(symb2[j]));
        //        	}
        //        	//rules are equal
        //            assertTrue(aRule.equals(bRule));
        //        }
        //        //feature type styles are equal
        //        assertTrue(SLD.featureTypeStyles(sld)[0].equals(SLD.featureTypeStyles(sld2)[0]));
        //        //styles are equal
        //        assertTrue(SLD.styles(sld)[0].equals(SLD.styles(sld2)[0]));
        //        //layers are equal
        //        StyledLayer layer1 = sld.getStyledLayers()[0];
        //        StyledLayer layer2 = sld2.getStyledLayers()[0];
        //        boolean result = layer1.equals(layer2);
        //        assertTrue(result);

        // everything is equal
        Assert.assertEquals(sld2, sld);
    }

    @Test
    public void testSLDTransformerIndentation() throws Exception {
        // create a simple object
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        NamedLayer nl = sf.createNamedLayer();
        nl.setName("named_layer_1");
        sld.addStyledLayer(nl);
        // convert it to XML
        SLDTransformer aTransformer = new SLDTransformer();
        aTransformer.setIndentation(3); // 3 spaces
        String xml1 = aTransformer.transform(sld);
        aTransformer.setIndentation(4); // 4 spaces
        String xml2 = aTransformer.transform(sld);
        // generated xml contains 4 indents, so if indentation is working, the difference should be
        // 4
        Assert.assertEquals(xml1.length() + 4, xml2.length());
    }

    @Test
    public void testParseSLD_NameSpaceAware() throws Exception {
        URL surl = TestData.getResource(this, "test-ns.sld");
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);

        SLDParser stylereader = new SLDParser(factory, surl);
        StyledLayerDescriptor sld = stylereader.parseSLD();

        Assert.assertEquals(1, sld.getStyledLayers().length);
        FeatureTypeStyle[] fts = SLD.featureTypeStyles(sld);
        Assert.assertEquals(2, fts.length);
        Assert.assertEquals(1, fts[0].semanticTypeIdentifiers().size());
        Assert.assertEquals(2, fts[1].semanticTypeIdentifiers().size());
        Assert.assertEquals(
                "colorbrewer:default",
                new ArrayList<>(fts[1].semanticTypeIdentifiers()).get(1).name());
    }

    /**
     * Test of parseSLD method to ensure NamedLayer/Name and NamedLayer/NamedStyle are parsed
     * correctly
     *
     * @throws Exception boom
     */
    @Test
    public void testParseSLDNamedLayersOnly() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "namedLayers.sld");
        SLDParser stylereader = new SLDParser(factory, surl);

        StyledLayerDescriptor sld = stylereader.parseSLD();

        final int expectedLayerCount = 3;
        final String[] layerNames = {"Rivers", "Roads", "Houses"};
        final String[] namedStyleNames = {"CenterLine", "CenterLine", "Outline"};
        StyledLayer[] layers = sld.getStyledLayers();

        Assert.assertEquals(expectedLayerCount, layers.length);

        for (int i = 0; i < expectedLayerCount; i++) {
            Assert.assertTrue(layers[i] instanceof NamedLayer);
        }

        for (int i = 0; i < expectedLayerCount; i++) {
            Assert.assertEquals(layerNames[i], layers[i].getName());
        }

        for (int i = 0; i < expectedLayerCount; i++) {
            NamedLayer layer = (NamedLayer) layers[i];
            Assert.assertEquals(1, layer.getStyles().length);
            Assert.assertTrue(layer.getStyles()[0] instanceof NamedStyle);
            Assert.assertEquals(namedStyleNames[i], layer.getStyles()[0].getName());
        }

        // find the rivers layers and test the LayerFeatureConstraints
        for (int i = 0; i < expectedLayerCount; i++) {
            NamedLayer layer = (NamedLayer) layers[i];
            if (layer.getName().equals("Rivers")) {
                FeatureTypeConstraint[] featureTypeConstraints = layer.getLayerFeatureConstraints();
                final int featureTypeConstraintCount = 1;
                Assert.assertEquals(featureTypeConstraintCount, featureTypeConstraints.length);
                Filter filter = featureTypeConstraints[0].getFilter();
                Assert.assertTrue(filter instanceof PropertyIsEqualTo);
                PropertyIsEqualTo equal = (PropertyIsEqualTo) filter;
                Assert.assertTrue(equal.getExpression1() instanceof PropertyName);
                Assert.assertTrue(equal.getExpression2() instanceof Literal);
            }
        }
    }

    /**
     * Test of parseSLD method to ensure NamedLayer/Name and NamedLayer/NamedStyle are parsed
     * correctly
     *
     * @throws Exception boom
     */
    @Test
    public void testParseSLDNamedAndUserLayers() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        java.net.URL surl = TestData.getResource(this, "mixedLayerTypes.sld");
        SLDParser stylereader = new SLDParser(factory, surl);

        StyledLayerDescriptor sld = stylereader.parseSLD();

        final int expectedLayerCount = 4;

        StyledLayer[] layers = sld.getStyledLayers();

        Assert.assertEquals(expectedLayerCount, layers.length);
        Assert.assertTrue(layers[0] instanceof NamedLayer);
        Assert.assertTrue(layers[1] instanceof UserLayer);
        Assert.assertTrue(layers[2] instanceof NamedLayer);
        Assert.assertTrue(layers[3] instanceof UserLayer);
    }

    /**
     * Verifies that geometry filters inside SLD documents are correctly parsed.
     *
     * @throws IOException boom
     */
    @Test
    public void testParseGeometryFilters() throws IOException {
        final String TYPE_NAME = "testType";
        final String GEOMETRY_ATTR = "Polygons";
        Style[] styles = getStyles("spatialFilter.xml");

        final int expectedStyleCount = 1;
        Assert.assertEquals(expectedStyleCount, styles.length);

        Style notDisjoint = styles[0];
        Assert.assertEquals(1, notDisjoint.featureTypeStyles().size());

        FeatureTypeStyle fts = notDisjoint.featureTypeStyles().get(0);
        Assert.assertEquals(TYPE_NAME, fts.featureTypeNames().iterator().next().getLocalPart());
        Assert.assertEquals(1, fts.rules().size());

        Filter filter = fts.rules().get(0).getFilter();
        Assert.assertTrue(filter instanceof Not);

        Filter spatialFilter = ((Not) filter).getFilter();
        Assert.assertTrue(spatialFilter instanceof Disjoint);

        Disjoint disjoint = (Disjoint) spatialFilter;
        Expression left = disjoint.getExpression1();
        Expression right = disjoint.getExpression2();

        Assert.assertTrue(left instanceof PropertyName);

        Assert.assertTrue(right instanceof Literal);
        Assert.assertTrue(right.evaluate(null) instanceof Geometry);

        Assert.assertEquals(GEOMETRY_ATTR, ((PropertyName) left).getPropertyName());
        Assert.assertTrue(right.evaluate(null) instanceof Polygon);

        Envelope bbox = ((Polygon) right.evaluate(null)).getEnvelopeInternal();
        Assert.assertEquals(-10D, bbox.getMinX(), 0);
        Assert.assertEquals(-10D, bbox.getMinY(), 0);
        Assert.assertEquals(10D, bbox.getMaxX(), 0);
        Assert.assertEquals(10D, bbox.getMaxY(), 0);
    }

    /**
     * Verifies that a FID Filter is correctly parsed (GEOT-992).
     *
     * @throws IOException boom
     */
    @Test
    public void testParseFidFilter() throws IOException {
        Style[] styles = getStyles("fidFilter.xml");

        final int expectedStyleCount = 1;
        Assert.assertEquals(expectedStyleCount, styles.length);

        Style style = styles[0];
        Assert.assertEquals(1, style.featureTypeStyles().size());

        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        Assert.assertEquals("Feature", fts.featureTypeNames().iterator().next().getLocalPart());
        Assert.assertEquals(1, fts.rules().size());

        Filter filter = fts.rules().get(0).getFilter();
        Assert.assertTrue(filter instanceof Id);

        Id fidFilter = (Id) filter;
        String[] fids =
                fidFilter.getIDs().stream().map(id -> (String) id).toArray(n -> new String[n]);
        Assert.assertEquals("Wrong number of fids", 5, fids.length);

        Arrays.sort(fids);

        Assert.assertEquals("fid.0", fids[0]);
        Assert.assertEquals("fid.1", fids[1]);
        Assert.assertEquals("fid.2", fids[2]);
        Assert.assertEquals("fid.3", fids[3]);
        Assert.assertEquals("fid.4", fids[4]);
    }

    @Test
    public void testParseKmlExtensions() throws IOException {
        Style[] styles = getStyles("kmlSymbolizer.sld");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        Assert.assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());
        final Rule rule = styles[0].featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(1, rule.symbolizers().size());
        TextSymbolizer2 ts = (TextSymbolizer2) rule.symbolizers().get(0);

        // abstract == property name
        Assert.assertEquals("propertyOne", ((PropertyName) ts.getSnippet()).getPropertyName());

        // abstract == mixed literal + propertyName
        Expression desc = ts.getFeatureDescription();
        Assert.assertTrue(desc instanceof Function);
        Assert.assertEquals("strConcat", ((Function) desc).getName());
        Assert.assertEquals(2, ((Function) desc).getParameters().size());
        Assert.assertTrue(((Function) desc).getParameters().get(0) instanceof Literal);
        Assert.assertTrue(((Function) desc).getParameters().get(1) instanceof PropertyName);

        // other text -> target & literal
        Assert.assertEquals("extrude", ts.getOtherText().getTarget());
        Assert.assertTrue(ts.getOtherText().getText() instanceof Literal);
    }

    @Test
    public void testParseAnchorDisplacement() throws IOException {
        Style[] styles = getStyles("markDisplacementTest.sld");
        PointSymbolizer ps =
                (PointSymbolizer)
                        styles[0].featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        Graphic graphic = ps.getGraphic();
        Displacement displacement = graphic.getDisplacement();
        Assert.assertNotNull(displacement);
        assertLiteral(11, displacement.getDisplacementX());
        assertLiteral(8, displacement.getDisplacementY());

        AnchorPoint anchorPoint = graphic.getAnchorPoint();
        Assert.assertNotNull(displacement);
        assertLiteral(0, anchorPoint.getAnchorPointX());
        assertLiteral(1, anchorPoint.getAnchorPointY());
    }

    private void assertLiteral(double expected, Expression exp) {
        Assert.assertTrue(exp instanceof Literal);
        double value = exp.evaluate(null, Double.class);
        Assert.assertEquals(expected, value, 0d);
    }

    /** Tests the parsing of a raster symbolizer sld */
    @Test
    public void testParseRasterSymbolizer() throws IOException {
        Style[] styles = getStyles("rasterSymbolizer.sld");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        Assert.assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());

        Rule r = styles[0].featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(1, r.symbolizers().size());

        RasterSymbolizer rs = (RasterSymbolizer) r.symbolizers().get(0);

        // opacity
        Assert.assertEquals(0.75, SLD.opacity(rs), 0d);

        // channels
        ChannelSelection cs = rs.getChannelSelection();
        SelectedChannelType redChannel = cs.getRGBChannels()[0];
        SelectedChannelType greenChannel = cs.getRGBChannels()[1];
        SelectedChannelType blueChannel = cs.getRGBChannels()[2];

        // channel names
        Assert.assertEquals("1", redChannel.getChannelName().evaluate(null, String.class));
        Assert.assertEquals("2", greenChannel.getChannelName().evaluate(null, String.class));
        Assert.assertEquals("6", blueChannel.getChannelName().evaluate(null, String.class));

        // contrast enhancement
        ContrastEnhancement rcs = redChannel.getContrastEnhancement();

        Assert.assertEquals("histogram", rcs.getMethod().name().toLowerCase());

        ContrastEnhancement gcs = greenChannel.getContrastEnhancement();
        Double ggamma = gcs.getGammaValue().evaluate(null, Double.class);
        Assert.assertEquals(2.8, ggamma.doubleValue(), 0d);

        ContrastEnhancement bcs = blueChannel.getContrastEnhancement();
        ContrastMethod m = bcs.getMethod();
        Assert.assertEquals("normalize", m.name().toLowerCase());

        // overlap behaviour
        Expression overlapExpr = rs.getOverlap();
        String type = (String) overlapExpr.evaluate(null);
        Assert.assertEquals("LATEST_ON_TOP", type);

        org.opengis.style.OverlapBehavior overlapBehavior = rs.getOverlapBehavior();
        type = overlapBehavior.name();
        Assert.assertEquals("LATEST_ON_TOP", type);

        // ContrastEnhancement
        ContrastEnhancement ce = rs.getContrastEnhancement();
        Double v = ce.getGammaValue().evaluate(null, Double.class);
        Assert.assertEquals(1.0, v.doubleValue(), 0d);
    }

    /** Tests the parsing of a raster symbolizer sld */
    @Test
    public void testParseRasterSymbolizerWithExpressionGammaValue() throws IOException {
        Style[] styles = getStyles("rasterSymbolizerGammaValue.sld");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        Assert.assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());

        Rule r = styles[0].featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(1, r.symbolizers().size());

        RasterSymbolizer rs = (RasterSymbolizer) r.symbolizers().get(0);

        // opacity
        Assert.assertEquals(0.75, SLD.opacity(rs), 0d);

        // channels
        ChannelSelection cs = rs.getChannelSelection();
        SelectedChannelType redChannel = cs.getRGBChannels()[0];
        SelectedChannelType greenChannel = cs.getRGBChannels()[1];
        SelectedChannelType blueChannel = cs.getRGBChannels()[2];

        // channel names
        Assert.assertEquals("1", redChannel.getChannelName().evaluate(null, String.class));
        Assert.assertEquals("2", greenChannel.getChannelName().evaluate(null, String.class));
        Assert.assertEquals("6", blueChannel.getChannelName().evaluate(null, String.class));

        // contrast enhancement
        ContrastEnhancement rcs = redChannel.getContrastEnhancement();

        Assert.assertEquals("histogram", rcs.getMethod().name().toLowerCase());

        ContrastEnhancement gcs = greenChannel.getContrastEnhancement();
        Double ggamma = gcs.getGammaValue().evaluate(null, Double.class);
        Assert.assertEquals(3.8, ggamma.doubleValue(), 0d);

        ContrastEnhancement bcs = blueChannel.getContrastEnhancement();
        ContrastMethod m = bcs.getMethod();
        Assert.assertEquals("normalize", m.name().toLowerCase());

        // overlap behaviour
        Expression overlapExpr = rs.getOverlap();
        String type = (String) overlapExpr.evaluate(null);
        Assert.assertEquals("LATEST_ON_TOP", type);

        org.opengis.style.OverlapBehavior overlapBehavior = rs.getOverlapBehavior();
        type = overlapBehavior.name();
        Assert.assertEquals("LATEST_ON_TOP", type);

        // ContrastEnhancement
        ContrastEnhancement ce = rs.getContrastEnhancement();
        Double v = ce.getGammaValue().evaluate(null, Double.class);
        Assert.assertEquals(1.5, v.doubleValue(), 0d);
    }

    /**
     * Tests the parsing of a raster symbolizer sld with ENV function expression on SelectedChannel
     */
    @Test
    public void testParseRasterChannelExpression() throws IOException {
        Style[] styles = getStyles("raster-channel-expression.xml");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        Assert.assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());

        Rule r = styles[0].featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(1, r.symbolizers().size());

        RasterSymbolizer rs = (RasterSymbolizer) r.symbolizers().get(0);

        // opacity
        Assert.assertEquals(1.0, SLD.opacity(rs), 0d);

        // channels
        ChannelSelection cs = rs.getChannelSelection();
        SelectedChannelType redChannel = cs.getRGBChannels()[0];
        SelectedChannelType greenChannel = cs.getRGBChannels()[1];
        SelectedChannelType blueChannel = cs.getRGBChannels()[2];

        // channel names
        // test default: 5
        EnvFunction.removeLocalValue("B1");
        Assert.assertEquals(
                5, redChannel.getChannelName().evaluate(null, Integer.class).intValue());
        // set env variable B1:20
        EnvFunction.setLocalValue("B1", "20");
        Assert.assertEquals(
                20, redChannel.getChannelName().evaluate(null, Integer.class).intValue());
        EnvFunction.removeLocalValue("B1");

        Assert.assertEquals("2", greenChannel.getChannelName().evaluate(null, String.class));
        Assert.assertEquals("4", blueChannel.getChannelName().evaluate(null, String.class));
    }

    /** Tests the parsing of a raster symbolizer sld with color Map */
    @Test
    public void testParseRasterSymbolizerColorMap() throws IOException {
        Style[] styles = getStyles("rasterSymbolizerColorMap.sld");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        Assert.assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());

        Rule r = styles[0].featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(1, r.symbolizers().size());

        RasterSymbolizer rs = (RasterSymbolizer) r.symbolizers().get(0);

        // opacity
        Double d = rs.getOpacity().evaluate(null, Double.class);
        Assert.assertEquals(1.0, d.doubleValue(), 0d);

        // overlap behaviour
        Expression overlapExpr = rs.getOverlap();
        String type = (String) overlapExpr.evaluate(null);
        Assert.assertEquals("AVERAGE", type);

        // ColorMap
        ColorMap cMap = rs.getColorMap();
        Assert.assertEquals(20, cMap.getColorMapEntries().length);
        ColorMapEntry[] centeries = cMap.getColorMapEntries();
        String[] colors = {
            "#00ff00", "#00fa00", "#14f500", "#28f502", "#3cf505", "#50f50a", "#64f014",
            "#7deb32", "#78c818", "#38840c", "#2c4b04", "#ffff00", "#dcdc00", "#b47800",
            "#c85000", "#be4100", "#963000", "#3c0200", "#ffffff", "#ffffff"
        };
        int[] values = {
            -500, -417, -333, -250, -167, -83, -1, 0, 30, 105, 300, 400, 700, 1200, 1400, 1600,
            2000, 3000, 5000, 13000
        };
        for (int i = 0; i < centeries.length; i++) {
            ColorMapEntry entry = centeries[i];
            String c = (String) entry.getColor().evaluate(null);
            Integer q = entry.getQuantity().evaluate(null, Integer.class);
            Assert.assertEquals(colors[i], c);
            Assert.assertEquals(values[i], q.intValue());
        }
    }

    @Test
    public void testParseGeometryExpressions() throws Exception {
        Style[] styles = getStyles("geometryTransformation.sld");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        Assert.assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());

        Rule r = styles[0].featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(1, r.symbolizers().size());

        PolygonSymbolizer ps = (PolygonSymbolizer) r.symbolizers().get(0);
        Expression geom = ps.getGeometry();
        Assert.assertNotNull(geom);
        Assert.assertTrue(geom instanceof FilterFunction_buffer);
        FilterFunction_buffer buf = (FilterFunction_buffer) geom;
        Assert.assertTrue(buf.getParameters().get(0) instanceof PropertyName);
        Assert.assertTrue(buf.getParameters().get(1) instanceof Literal);
    }

    @Test
    public void testParsePlainGeometryExpression() throws Exception {
        Style[] styles = getStyles("geometryPlain.sld");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        Assert.assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());

        Rule r = styles[0].featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(1, r.symbolizers().size());

        PolygonSymbolizer ps = (PolygonSymbolizer) r.symbolizers().get(0);
        Expression geom = ps.getGeometry();
        Assert.assertNotNull(geom);
        Assert.assertTrue(geom instanceof PropertyName);
        PropertyName pn = (PropertyName) geom;
        Assert.assertEquals("the_geom", pn.getPropertyName());
        Assert.assertEquals("the_geom", ps.getGeometryPropertyName());
    }

    @Test
    public void testDataTransformation() throws Exception {
        Style[] styles = getStyles("transformation.xml");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        final FeatureTypeStyle fts = styles[0].featureTypeStyles().get(0);
        Assert.assertEquals(1, fts.rules().size());
        Assert.assertNotNull(fts.getTransformation());
        Function tx = (Function) fts.getTransformation();
        Assert.assertEquals("union", tx.getName());
    }

    @Test
    public void testRuleEvaluationMode() throws Exception {
        Style[] styles = getStyles("ruleEvaluationMode.xml");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        final FeatureTypeStyle fts = styles[0].featureTypeStyles().get(0);
        Assert.assertEquals(1, fts.rules().size());
        Map<String, String> options = fts.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(
                FeatureTypeStyle.VALUE_EVALUATION_MODE_FIRST,
                options.get(FeatureTypeStyle.KEY_EVALUATION_MODE));
    }

    @Test
    public void testGreenBandSelection() throws Exception {
        Style[] styles = getStyles("greenChannelSelection.sld");
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        final FeatureTypeStyle fts = styles[0].featureTypeStyles().get(0);
        Assert.assertEquals(1, fts.rules().size());
        RasterSymbolizer symbolizer = (RasterSymbolizer) fts.rules().get(0).symbolizers().get(0);
        final SelectedChannelType[] rgbChannels = symbolizer.getChannelSelection().getRGBChannels();
        Assert.assertNotNull(rgbChannels);
        Assert.assertNull(rgbChannels[0]);
        Assert.assertNotNull(rgbChannels[1]);
        Assert.assertNull(rgbChannels[2]);
    }

    @Test
    public void testMultipleFonts() throws Exception {
        Style[] styles = getStyles("multifont.sld");
        Assert.assertEquals(1, styles.length);
        List<FeatureTypeStyle> featureTypeStyles = styles[0].featureTypeStyles();
        Assert.assertEquals(1, featureTypeStyles.size());
        List<Rule> rules = featureTypeStyles.get(0).rules();
        Assert.assertEquals(1, rules.size());
        List<Symbolizer> symbolizers = rules.get(0).symbolizers();
        Assert.assertEquals(1, symbolizers.size());
        TextSymbolizer ts = (TextSymbolizer) symbolizers.get(0);
        Assert.assertEquals(1, ts.fonts().size());
        List<Expression> families = ts.fonts().get(0).getFamily();
        Assert.assertEquals(3, families.size());
        Assert.assertEquals("Comic Sans MS", families.get(0).evaluate(null, String.class));
        Assert.assertEquals("Droid Sans Fallback", families.get(1).evaluate(null, String.class));
        Assert.assertEquals("Arial", families.get(2).evaluate(null, String.class));
    }

    @Test
    public void testParseBase64EncodedContent() throws Exception {
        ExternalGraphic graphic = getGraphic("base64.sld");
        Assert.assertNotNull(graphic.getInlineContent());
        Assert.assertEquals("image/png", graphic.getFormat());
        Assert.assertNull(graphic.getLocation());
        assertImagesEqual(getReferenceImage("test.png"), graphic.getInlineContent());
    }

    @Test
    public void testInvalidInlineContent() throws Exception {
        ExternalGraphic graphic = getGraphic("invalid-content.sld");
        Assert.assertNotNull(graphic.getInlineContent());
        Assert.assertEquals("image/png", graphic.getFormat());
        Assert.assertNull(graphic.getLocation());
        Assert.assertEquals(1, graphic.getInlineContent().getIconWidth());
        Assert.assertEquals(1, graphic.getInlineContent().getIconHeight());
    }

    @Test
    public void testUnsuppotedInlineContentEncoding() throws Exception {
        ExternalGraphic graphic = getGraphic("unsupported-encoding.sld");
        Assert.assertNotNull(graphic.getInlineContent());
        Assert.assertEquals("image/svg", graphic.getFormat());
        Assert.assertNull(graphic.getLocation());
        Assert.assertEquals(1, graphic.getInlineContent().getIconWidth());
        Assert.assertEquals(1, graphic.getInlineContent().getIconHeight());
    }

    @Test
    public void testParseBackgroundSolid() throws Exception {
        Style[] styles = getStyles("backgroundSolid.sld");
        Assert.assertEquals(1, styles.length);
        Fill background = styles[0].getBackground();
        Assert.assertNotNull(background);
        Assert.assertEquals(Color.RED, background.getColor().evaluate(null, Color.class));
        Assert.assertEquals(1, background.getOpacity().evaluate(null, Double.class), 0d);
    }

    @Test
    public void testParseBackgroundGraphicFill() throws Exception {
        Style[] styles = getStyles("backgroundGraphicFill.sld");
        Assert.assertEquals(1, styles.length);
        Fill background = styles[0].getBackground();
        Assert.assertNotNull(background);
        Graphic graphic = background.getGraphicFill();
        Assert.assertNotNull(graphic);
        GraphicalSymbol firstSymbol = graphic.graphicalSymbols().get(0);
        Assert.assertTrue(firstSymbol instanceof Mark);
        Assert.assertEquals(
                "square", ((Mark) firstSymbol).getWellKnownName().evaluate(null, String.class));
    }

    public Style[] getStyles(String s) throws IOException {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL surl = TestData.getResource(this, s);
        SLDParser stylereader = new SLDParser(factory, surl);

        // basic checks
        return stylereader.readXML();
    }

    private BufferedImage getReferenceImage(String resourceName) throws IOException {
        URL url = TestData.getResource(this, resourceName);
        return ImageIO.read(url);
    }

    private ExternalGraphic getGraphic(String resourceName) throws IOException {
        URL surl = TestData.getResource(this, resourceName);
        SLDParser stylereader = new SLDParser(sf, surl);

        Style[] styles = stylereader.readXML();
        Assert.assertEquals(1, styles.length);
        Assert.assertEquals(1, styles[0].featureTypeStyles().size());
        Assert.assertEquals(1, styles[0].featureTypeStyles().get(0).rules().size());

        Rule r = styles[0].featureTypeStyles().get(0).rules().get(0);
        Assert.assertEquals(1, r.symbolizers().size());

        PolygonSymbolizer symbolizer = (PolygonSymbolizer) r.symbolizers().get(0);

        Fill fill = symbolizer.getFill();
        Assert.assertNotNull(fill);

        Graphic graphicFill = fill.getGraphicFill();
        Assert.assertNotNull(graphicFill);

        Assert.assertEquals(1, graphicFill.graphicalSymbols().size());

        GraphicalSymbol symbol = graphicFill.graphicalSymbols().get(0);
        Assert.assertTrue(symbol instanceof ExternalGraphic);
        return (ExternalGraphic) symbol;
    }

    private static void assertImagesEqual(BufferedImage expected, Icon icon) {
        BufferedImage actual =
                new BufferedImage(
                        icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = actual.createGraphics();
        try {
            icon.paintIcon(null, g, 0, 0);
        } finally {
            g.dispose();
        }

        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getWidth(), actual.getWidth());
        Assert.assertEquals(expected.getHeight(), actual.getHeight());
        int w = actual.getWidth();
        int h = actual.getHeight();
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {
                Assert.assertEquals(
                        "mismatch at (" + x + ", " + y + ")",
                        actual.getRGB(x, y),
                        expected.getRGB(x, y));
            }
        }
    }
}
