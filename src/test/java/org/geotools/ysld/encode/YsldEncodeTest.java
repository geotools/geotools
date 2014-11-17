package org.geotools.ysld.encode;


import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.styling.UserLayer;
import org.geotools.ysld.TestUtils;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;
import org.geotools.ysld.Ysld;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.style.ChannelSelection;
import org.opengis.style.ContrastMethod;
import org.opengis.style.GraphicalSymbol;
import org.geotools.styling.RasterSymbolizer;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.geotools.ysld.TestUtils.lexEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class YsldEncodeTest {

    private static final double EPSILON = 0.0000000001;

    @Test
    public void testFunction() throws Exception {
         StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        Rule rule = styleFactory.createRule();

        style.featureTypeStyles().add(styleFactory.createFeatureTypeStyle());
        style.featureTypeStyles().get(0).rules().add(rule);

        Function func =
            filterFactory.function("strEndsWith", filterFactory.property("foo"), filterFactory.literal("bar"));
        rule.setFilter(filterFactory.equal(func, filterFactory.literal(true), false));

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        String filter = obj.seq("feature-styles").map(0).seq("rules").map(0).str("filter");
        assertEquals("strEndsWith(foo,'bar') = 'true'", filter);
    }

    @Test
    public void testRenderingTransformation() throws IOException {

        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        FeatureTypeStyle featureStyle = styleFactory.createFeatureTypeStyle();
        style.featureTypeStyles().add(featureStyle);

        Function p1 = filterFactory.function("parameter", filterFactory.literal("data"));
        Function p2 = filterFactory.function("parameter", filterFactory.literal("levels"), filterFactory.literal(1000),
            filterFactory.literal(1100), filterFactory.literal(1200));

        Function rt = filterFactory.function("ras:Contour", p1, p2);
        featureStyle.setTransformation(rt);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap tx = obj.seq("feature-styles").map(0).map("transform");
        assertEquals("ras:Contour", tx.get("name"));
        YamlSeq levels = tx.map("params").seq("levels");
        assertEquals("1000", levels.str(0));
        assertEquals("1100", levels.str(1));
        assertEquals("1200", levels.str(2));
    }

    @Test
    public void testRenderingTransformationHeatmap() throws IOException {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        FeatureTypeStyle featureStyle = styleFactory.createFeatureTypeStyle();
        style.featureTypeStyles().add(featureStyle);

        Function p1 = filterFactory.function("parameter", filterFactory.literal("data"));
        Function p2 = filterFactory.function("parameter",
            filterFactory.literal("weightAttr"), filterFactory.literal("pop2000"));
        Function p3 = filterFactory.function("parameter",
                filterFactory.literal("radius"), filterFactory.literal("100"));
        Function p4 = filterFactory.function("parameter",
                filterFactory.literal("pixelsPerCell"), filterFactory.literal(10));

        Function rt = filterFactory.function("vec:Heatmap", p1, p2, p3, p4);
        featureStyle.setTransformation(rt);

        Rule rule = styleFactory.createRule();
        rule.setName("Za'Ha'Dum");
        featureStyle.rules().add(rule);
        
        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap tx = obj.seq("feature-styles").map(0).map("transform");
        assertEquals("vec:Heatmap", tx.get("name"));

        YamlMap params = tx.map("params");
        assertEquals("pop2000", params.get("weightAttr"));
        assertEquals("100", params.str("radius"));
        assertEquals("10", params.str("pixelsPerCell"));
        
        YamlMap ruleMap = obj.seq("feature-styles").map(0).seq("rules").map(0);
        assertThat(ruleMap.str("name"), equalTo("Za'Ha'Dum"));
    }
    
    @Test
    public void testLabelShield() throws IOException {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();

        UserLayer layer = sf.createUserLayer();
        sld.layers().add(layer);
        Style style = sf.createStyle();
        layer.userStyles().add(style);
        FeatureTypeStyle featureStyle = sf.createFeatureTypeStyle();
        style.featureTypeStyles().add(featureStyle);
        Rule rule = sf.createRule();
        featureStyle.rules().add(rule);
        Stroke stroke = sf.stroke(ff.literal("#555555"), null, null, null, null, null, null);
        rule.symbolizers().add(
                sf.lineSymbolizer("line", null, null, null, stroke, null));
        Mark mark = sf.mark(ff.literal("circle"), sf.fill(null, ff.literal("#995555"), null), null);
        List<GraphicalSymbol> symbols = new ArrayList<GraphicalSymbol>();
        symbols.add(mark);
        TextSymbolizer2 text = (TextSymbolizer2) sf.textSymbolizer(null, ff.property("geom"), null, null, ff.property("name"),null,null,null,null);
        text.setGraphic( sf.graphic( symbols,null,null,null,null,null) );
        rule.symbolizers().add(text);
        
        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap yaml = new YamlMap(new Yaml().load(out.toString()));
        
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/1/text"), yHasEntry("label", equalTo("${name}")) );
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/1/text"), yHasEntry("graphic") );
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/1/text/graphic/symbols/0/mark"), yHasEntry("shape", equalTo("circle")) );
    }
    @Test
    public void testLabelDisplacement() throws IOException {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();

        UserLayer layer = sf.createUserLayer();
        sld.layers().add(layer);
        Style style = sf.createStyle();
        layer.userStyles().add(style);
        FeatureTypeStyle featureStyle = sf.createFeatureTypeStyle();
        style.featureTypeStyles().add(featureStyle);
        Rule rule = sf.createRule();
        featureStyle.rules().add(rule);
        
        
        LabelPlacement place = 
                sf.createPointPlacement(
                    sf.createAnchorPoint(ff.literal(0.75), ff.literal(0.25)),
                    sf.createDisplacement(ff.literal(10), ff.literal(15)),
                    ff.literal(90));
        TextSymbolizer2 text = (TextSymbolizer2) sf.textSymbolizer(null, ff.property("geom"), null, null, ff.property("name"),null,place,null,null);
        rule.symbolizers().add(text);
        
        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap yaml = new YamlMap(new Yaml().load(out.toString()));
        
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("placement", equalTo("point")) );
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("displacement", equalTo("(10,15)")) );
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("anchor", equalTo("(0.75,0.25)")) );
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("rotation", equalTo(90)) );
    }
    
    @Test
    public void testLabelLinePlacement() throws IOException {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();

        UserLayer layer = sf.createUserLayer();
        sld.layers().add(layer);
        Style style = sf.createStyle();
        layer.userStyles().add(style);
        FeatureTypeStyle featureStyle = sf.createFeatureTypeStyle();
        style.featureTypeStyles().add(featureStyle);
        Rule rule = sf.createRule();
        featureStyle.rules().add(rule);
        
        
        LabelPlacement place = 
                sf.createLinePlacement(ff.literal(10));
        
        TextSymbolizer2 text = (TextSymbolizer2) sf.textSymbolizer(null, ff.property("geom"), null, null, ff.property("name"),null,place,null,null);
        rule.symbolizers().add(text);
        
        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap yaml = new YamlMap(new Yaml().load(out.toString()));
        
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("placement", equalTo("line")) );
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("offset", equalTo(10)) );
    }
    @Test
    public void testEmptyColorMap() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = sld(styleFactory.createRasterSymbolizer());

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap symbMap = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("raster");

        assertFalse(symbMap.has("color-map"));
    }
    @Test
    public void testEmptyContrastEnhancement() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = sld(styleFactory.createRasterSymbolizer());

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap symbMap = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("raster");
        
        assertFalse(symbMap.has("contrast-enhancement"));
    }

    @Test
    public void testExpressionNil() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        p.getGraphic().setSize(Expression.NIL);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        System.out.println(out.toString());
    }
    
    @Test
    public void testNameExpressionLiteral() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression nameExpression = CommonFactoryFinder.getFilterFactory2().literal("test");
        Mark mark = CommonFactoryFinder.getStyleFactory().createMark();
        mark.setWellKnownName(nameExpression);
        p.getGraphic().graphicalSymbols().add(mark);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        String result = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("point").seq("symbols").map(0).map("mark").str("shape");
        
        assertThat(result, equalTo("test"));
    }
    @Test
    public void testNameExpressionAttribute() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression nameExpression = CommonFactoryFinder.getFilterFactory2().property("test");
        Mark mark = CommonFactoryFinder.getStyleFactory().createMark();
        mark.setWellKnownName(nameExpression);
        p.getGraphic().graphicalSymbols().add(mark);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        String result = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("point").seq("symbols").map(0).map("mark").str("shape");
        
        assertThat(result, equalTo("${test}"));
    }
    
    @Test
    public void testNonNameExpressionLiteral() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory2().literal("test");
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        String result = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("point").str("geometry");
        
        assertThat(result, equalTo("test"));
    }
    @Test
    public void testNonNameExpressionAttribute() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory2().property("test");
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        String result = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("point").str("geometry");
        
        assertThat(result, equalTo("${test}"));
    }
    @Test
    public void testEmbededExpression() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory2().function("Concatenate", 
                CommonFactoryFinder.getFilterFactory2().literal("literal0"),
                CommonFactoryFinder.getFilterFactory2().property("attribute1"),
                CommonFactoryFinder.getFilterFactory2().literal("literal2")
                );
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        String result = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("point").str("geometry");
        
        assertThat(result, equalTo("literal0${attribute1}literal2"));
    }
    @Test
    public void testEmbededExpressionEscapeLiteral() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory2().literal("$}\\");
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        String result = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("point").str("geometry");
        
        assertThat(result, equalTo("\\$\\}\\\\"));
    }
    
    @Test
    public void testEmbededExpressionEscapeExpression() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory2().function("strEndsWith", 
                CommonFactoryFinder.getFilterFactory2().property("attribute1"),
                CommonFactoryFinder.getFilterFactory2().literal("}")
                );
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        String result = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("point").str("geometry");
        
        assertThat(result, equalTo("${strEndsWith(attribute1,'\\}')}"));
    }
    
    @Test
    public void testGrayBandSelection() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory();
        RasterSymbolizer r = factory.createRasterSymbolizer();
        ChannelSelection sel = factory.channelSelection(factory.createSelectedChannelType("foo", factory.createContrastEnhancement()));
        r.setChannelSelection(sel);
        
        StringWriter out = new StringWriter();
        Ysld.encode(sld(r), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap channelMap = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("raster").map("channels");
        
        assertThat(channelMap, yHasEntry("gray"));
        assertThat(channelMap, not(yHasEntry("red")));
        assertThat(channelMap, not(yHasEntry("green")));
        assertThat(channelMap, not(yHasEntry("blue")));

        
        assertThat(channelMap.map("gray"), 
                yHasEntry("name", 
                    equalTo("foo")));
        assertThat(channelMap.map("gray"), 
                not(yHasEntry("contrast-enhancement")));

        
    }
    @Test
    public void testRGBBandSelection() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory();
        RasterSymbolizer r = factory.createRasterSymbolizer();
        ChannelSelection sel = factory.channelSelection(
                factory.createSelectedChannelType("foo", factory.createContrastEnhancement()),
                factory.createSelectedChannelType("bar", factory.createContrastEnhancement()),
                factory.createSelectedChannelType("baz", factory.createContrastEnhancement())
                );
        r.setChannelSelection(sel);
        
        StringWriter out = new StringWriter();
        Ysld.encode(sld(r), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap channelMap = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("raster").map("channels");
        
        assertThat(channelMap, not(yHasEntry("gray")));
        assertThat(channelMap, yHasEntry("red"));
        assertThat(channelMap, yHasEntry("green"));
        assertThat(channelMap, yHasEntry("blue"));
        
        assertThat(channelMap.map("red"), 
                yHasEntry("name", 
                    equalTo("foo")));
        assertThat(channelMap.map("red"), 
                not(yHasEntry("contrast-enhancement")));
        assertThat(channelMap.map("green"), 
                yHasEntry("name", 
                    equalTo("bar")));
        assertThat(channelMap.map("green"), 
                not(yHasEntry("contrast-enhancement")));
        assertThat(channelMap.map("blue"), 
                yHasEntry("name", 
                    equalTo("baz")));
        assertThat(channelMap.map("blue"), 
                not(yHasEntry("contrast-enhancement")));

        assertFalse(channelMap.has("gray"));
        assertTrue(channelMap.has("red"));
        assertTrue(channelMap.has("green"));
        assertTrue(channelMap.has("blue"));
        
        assertThat(channelMap.map("red").str("name"), equalTo("foo"));
        assertFalse(channelMap.map("red").has("contrast-enhancement"));
        assertThat(channelMap.map("green").str("name"), equalTo("bar"));
        assertFalse(channelMap.map("green").has("contrast-enhancement"));
        assertThat(channelMap.map("blue").str("name"), equalTo("baz"));
        assertFalse(channelMap.map("blue").has("contrast-enhancement"));
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    Matcher<Object> yHasEntry(final String key, final Matcher<? extends Object> m) {
        return new BaseMatcher() {

            @Override
            public boolean matches(Object arg0) {
                if(!(arg0 instanceof YamlMap)) return false;
                YamlMap map = (YamlMap) arg0;
                
                if(! map.has(key)) return false;
                Object value = null;
                try {
                    value = map.map(key);
                } catch (IllegalArgumentException ex1) {
                    try {
                        value = map.seq(key);
                    } catch (IllegalArgumentException ex2) {
                        value = map.get(key);
                    }
                }
                return (m.matches(value));
            }

            @Override
            public void describeTo(Description arg0) {
                arg0.appendText("YamlMap with entry ").appendValue(key).appendText(" and value ").appendDescriptionOf(m);
            }
            
        };
    }
    Matcher<Object> yHasEntry(final String key) { 
        return yHasEntry(key, Matchers.any(Object.class));
    }
    @Test
    public void testGrayBandSelectionWithContrast() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2();
        RasterSymbolizer r = styleFactory.createRasterSymbolizer();
        ChannelSelection sel = styleFactory.channelSelection(styleFactory.createSelectedChannelType("foo", 
                styleFactory.createContrastEnhancement(filterFactory.literal(1.2)
                        )));
        r.setChannelSelection(sel);
        
        StringWriter out = new StringWriter();
        Ysld.encode(sld(r), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap channelMap = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("raster").map("channels");
        
        assertThat(channelMap, yHasEntry("gray"));
        assertThat(channelMap, not(yHasEntry("red")));
        assertThat(channelMap, not(yHasEntry("green")));
        assertThat(channelMap, not(yHasEntry("blue")));

        
        assertThat(channelMap.map("gray"), 
                yHasEntry("name", 
                    equalTo("foo")));
        assertThat(channelMap.map("gray"), 
                yHasEntry("contrast-enhancement", 
                    yHasEntry("gamma", 
                        equalTo(1.2d))));
    }
    @Test
    public void testRGBBandSelectionWithContrast() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2();
        RasterSymbolizer r = styleFactory.createRasterSymbolizer();
        ChannelSelection sel = styleFactory.channelSelection(
                styleFactory.createSelectedChannelType("foo", 
                        styleFactory.createContrastEnhancement()),
                styleFactory.createSelectedChannelType("bar", 
                        styleFactory.createContrastEnhancement(
                                filterFactory.literal(1.2))),
                styleFactory.createSelectedChannelType("baz", 
                        styleFactory.createContrastEnhancement())
                );
        ((org.geotools.styling.ContrastEnhancement)sel.getRGBChannels()[2].getContrastEnhancement()).setMethod(ContrastMethod.HISTOGRAM);
        r.setChannelSelection(sel);
        
        StringWriter out = new StringWriter();
        Ysld.encode(sld(r), out);
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap channelMap = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("raster").map("channels");
        
        assertThat(channelMap, not(yHasEntry("gray")));
        assertThat(channelMap, yHasEntry("red"));
        assertThat(channelMap, yHasEntry("green"));
        assertThat(channelMap, yHasEntry("blue"));
        
        assertThat(channelMap.map("red"), 
                yHasEntry("name", 
                    equalTo("foo")));
        assertThat(channelMap.map("red"), 
                not(yHasEntry("contrast-enhancement")));
        assertThat(channelMap.map("green"), 
                yHasEntry("name", 
                    equalTo("bar")));
        assertThat(channelMap.map("green"), 
                yHasEntry("contrast-enhancement", 
                    yHasEntry("gamma", 
                        equalTo(1.2d))));
        assertThat(channelMap.map("blue"), 
                yHasEntry("name", 
                    equalTo("baz")));
        assertThat(channelMap.map("blue"), 
                yHasEntry("contrast-enhancement", 
                    yHasEntry("mode", 
                        equalTo("histogram"))));
    }
    
    StyledLayerDescriptor sld(Symbolizer sym) {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        Rule rule = styleFactory.createRule();

        style.featureTypeStyles().add(styleFactory.createFeatureTypeStyle());
        style.featureTypeStyles().get(0).rules().add(rule);

        rule.symbolizers().add((Symbolizer)sym);
        return sld;
    }

    @Test
    public void testTextSymbolizerPriority() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        TextSymbolizer symb = styleFactory.createTextSymbolizer();
        symb.setPriority(filterFactory.property("foo"));

        StringWriter out = new StringWriter();
        Ysld.encode(sld(symb), out);

        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap text = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("text");
        assertEquals("${foo}", text.str("priority"));
    }
    
    @Test
    public void testColourNoQuotes() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        
        Mark m1 = CommonFactoryFinder.getStyleFactory().getCircleMark();
        m1.setFill(CommonFactoryFinder.getStyleFactory().createFill(CommonFactoryFinder.getFilterFactory2().literal("#112233")));
        m1.setStroke(CommonFactoryFinder.getStyleFactory().createStroke(CommonFactoryFinder.getFilterFactory2().literal("#445566"), CommonFactoryFinder.getFilterFactory2().literal(3)));
        p.getGraphic().graphicalSymbols().add(m1);
        
        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        
        System.out.append(out.toString());
        
        YamlMap obj = new YamlMap(new Yaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles").map(0).seq("rules").map(0).seq("symbolizers").map(0).map("point").seq("symbols").map(0).map("mark");
        
        assertThat(result, yHasEntry("fill-color", equalTo(112233)));
        assertThat(result, yHasEntry("stroke-color", equalTo(445566)));
    }

}
