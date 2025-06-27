/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.encode;

import static org.geotools.ysld.TestUtils.fakeString;
import static org.geotools.ysld.TestUtils.isColor;
import static org.geotools.ysld.TestUtils.lexEqualTo;
import static org.geotools.ysld.TestUtils.numEqualTo;
import static org.geotools.ysld.TestUtils.yHasEntry;
import static org.geotools.ysld.TestUtils.yHasItem;
import static org.geotools.ysld.TestUtils.yTuple;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicLegend;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.RemoteOWS;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.UserLayer;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.UomOgcMapping;
import org.geotools.util.logging.Logging;
import org.geotools.ysld.Tuple;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlSeq;
import org.geotools.ysld.YamlUtil;
import org.geotools.ysld.Ysld;
import org.geotools.ysld.parse.YsldParser;
import org.junit.Assert;
import org.junit.Test;
import systems.uom.common.USCustomary;

public class YsldEncodeTest {

    private static final double EPSILON = 0.0000000001;

    Logger LOG = Logging.getLogger("org.geotools.ysld.Ysld");

    @Test
    public void testRoot() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();
        sld.setName("MySLD");
        sld.setTitle("My SLD");
        sld.setAbstract("Remote WMS user layer style definition");

        UserLayer layer = styleFactory.createUserLayer();
        RemoteOWS remote = styleFactory.createRemoteOWS("WMS", "http://localhost:8080/geoserver/wms");
        layer.setName("MyLayer");
        layer.setRemoteOWS(remote);

        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        style.setName("Ignored");
        layer.userStyles().add(style);

        Style defaultStyle = styleFactory.createStyle();
        defaultStyle.setDefault(true);
        defaultStyle.setName("MyStyle");
        layer.userStyles().add(defaultStyle);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));

        assertThat(obj, yHasEntry("sld-name", lexEqualTo("MySLD")));
        assertThat(obj, yHasEntry("sld-title", lexEqualTo("My SLD")));
        assertThat(obj, yHasEntry("sld-abstract", lexEqualTo("Remote WMS user layer style definition")));

        assertThat(obj, yHasEntry("user-name", lexEqualTo("MyLayer")));
        assertThat(obj, yHasEntry("user-remote", lexEqualTo("http://localhost:8080/geoserver/wms")));
        assertThat(obj, yHasEntry("user-service", lexEqualTo("WMS")));

        assertThat(obj, yHasEntry("user-name", lexEqualTo("MyLayer")));
        assertThat(obj, yHasEntry("name", lexEqualTo("MyStyle")));
    }

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

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        String filter = obj.seq("feature-styles").map(0).seq("rules").map(0).str("filter");
        assertEquals("${strEndsWith(foo,'bar') = true}", filter);
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
        Function p2 = filterFactory.function(
                "parameter",
                filterFactory.literal("levels"),
                filterFactory.literal(1000),
                filterFactory.literal(1100),
                filterFactory.literal(1200));

        Function rt = filterFactory.function("ras:Contour", p1, p2);
        featureStyle.setTransformation(rt);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap tx = obj.seq("feature-styles").map(0).map("transform");
        assertThat(tx, yHasEntry("name", lexEqualTo("ras:Contour")));
        assertThat(tx, not(yHasEntry("input")));
        assertThat(
                tx,
                yHasEntry(
                        "params",
                        allOf(
                                not(yHasEntry("data")),
                                yHasEntry(
                                        "levels",
                                        allOf(
                                                yHasItem(0, lexEqualTo(1000)),
                                                yHasItem(1, lexEqualTo(1100)),
                                                yHasItem(2, lexEqualTo(1200)))))));
    }

    @Test
    public void testRenderingTransformationInput() throws IOException {

        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        FeatureTypeStyle featureStyle = styleFactory.createFeatureTypeStyle();
        style.featureTypeStyles().add(featureStyle);

        Function p1 = filterFactory.function("parameter", filterFactory.literal("alternateInput"));
        Function p2 = filterFactory.function(
                "parameter",
                filterFactory.literal("levels"),
                filterFactory.literal(1000),
                filterFactory.literal(1100),
                filterFactory.literal(1200));

        Function rt = filterFactory.function("ras:Contour", p1, p2);
        featureStyle.setTransformation(rt);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap tx = obj.seq("feature-styles").map(0).map("transform");
        assertThat(tx, yHasEntry("name", lexEqualTo("ras:Contour")));
        assertThat(tx, yHasEntry("input", lexEqualTo("alternateInput")));
        assertThat(
                tx,
                yHasEntry(
                        "params",
                        allOf(
                                not(yHasEntry("data")),
                                not(yHasEntry("alternateInput")),
                                yHasEntry(
                                        "levels",
                                        allOf(
                                                yHasItem(0, lexEqualTo(1000)),
                                                yHasItem(1, lexEqualTo(1100)),
                                                yHasItem(2, lexEqualTo(1200)))))));
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
        Function p2 = filterFactory.function(
                "parameter", filterFactory.literal("weightAttr"), filterFactory.literal("pop2000"));
        Function p3 =
                filterFactory.function("parameter", filterFactory.literal("radius"), filterFactory.literal("100"));
        Function p4 =
                filterFactory.function("parameter", filterFactory.literal("pixelsPerCell"), filterFactory.literal(10));
        Function p5 = filterFactory.function(
                "parameter",
                filterFactory.literal("outputBBOX"),
                filterFactory.function("env", filterFactory.literal("wms_bbox")));
        Function p6 = filterFactory.function(
                "parameter",
                filterFactory.literal("outputWidth"),
                filterFactory.function("env", filterFactory.literal("wms_width")));
        Function p7 = filterFactory.function(
                "parameter",
                filterFactory.literal("outputHeight"),
                filterFactory.function("env", filterFactory.literal("wms_height")));

        Function rt = filterFactory.function("vec:Heatmap", p1, p2, p3, p4, p5, p6, p7);
        featureStyle.setTransformation(rt);

        Rule rule = styleFactory.createRule();
        rule.setName("Za'Ha'Dum");
        featureStyle.rules().add(rule);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap tx = obj.seq("feature-styles").map(0).map("transform");
        assertEquals("vec:Heatmap", tx.get("name"));

        YamlMap params = tx.map("params");
        assertThat(params, yHasEntry("weightAttr", lexEqualTo("pop2000")));
        assertThat(params, yHasEntry("radius", lexEqualTo(100)));
        assertThat(params, yHasEntry("pixelsPerCell", lexEqualTo(10)));

        assertThat(params, not(yHasEntry("outputBBOX")));
        assertThat(params, not(yHasEntry("outputWidth")));
        assertThat(params, not(yHasEntry("outputHeight")));

        YamlMap ruleMap = obj.seq("feature-styles").map(0).seq("rules").map(0);
        assertThat(ruleMap.str("name"), equalTo("Za'Ha'Dum"));
    }

    @Test
    public void testRenderingTransformationHeatmapAltBBOX() throws IOException {
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
        Function p2 = filterFactory.function(
                "parameter", filterFactory.literal("weightAttr"), filterFactory.literal("pop2000"));
        Function p3 =
                filterFactory.function("parameter", filterFactory.literal("radius"), filterFactory.literal("100"));
        Function p4 =
                filterFactory.function("parameter", filterFactory.literal("pixelsPerCell"), filterFactory.literal(10));
        Function p5 = filterFactory.function(
                "parameter",
                filterFactory.literal("outputBBOX"),
                filterFactory.function("env", filterFactory.literal("something_else")));
        Function p6 = filterFactory.function(
                "parameter",
                filterFactory.literal("outputWidth"),
                filterFactory.function("env", filterFactory.literal("wms_width")));
        Function p7 = filterFactory.function(
                "parameter",
                filterFactory.literal("outputHeight"),
                filterFactory.function("env", filterFactory.literal("wms_height")));

        Function rt = filterFactory.function("vec:Heatmap", p1, p2, p3, p4, p5, p6, p7);
        featureStyle.setTransformation(rt);

        Rule rule = styleFactory.createRule();
        rule.setName("Za'Ha'Dum");
        featureStyle.rules().add(rule);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap tx = obj.seq("feature-styles").map(0).map("transform");
        assertEquals("vec:Heatmap", tx.get("name"));

        YamlMap params = tx.map("params");
        assertThat(params, yHasEntry("weightAttr", lexEqualTo("pop2000")));
        assertThat(params, yHasEntry("radius", lexEqualTo(100)));
        assertThat(params, yHasEntry("pixelsPerCell", lexEqualTo(10)));

        assertThat(params, yHasEntry("outputBBOX", equalTo("${env('something_else')}")));
        assertThat(params, not(yHasEntry("outputWidth")));
        assertThat(params, not(yHasEntry("outputHeight")));

        YamlMap ruleMap = obj.seq("feature-styles").map(0).seq("rules").map(0);
        assertThat(ruleMap.str("name"), equalTo("Za'Ha'Dum"));
    }

    @Test
    public void testRenderingTransformationSimplifyWithWMSParams() throws IOException {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        FeatureTypeStyle featureStyle = styleFactory.createFeatureTypeStyle();
        style.featureTypeStyles().add(featureStyle);

        Function p1 = filterFactory.function("parameter", filterFactory.literal("features"));
        Function p2 =
                filterFactory.function("parameter", filterFactory.literal("distance"), filterFactory.literal(10.0));
        Function p3 = filterFactory.function(
                "parameter", filterFactory.literal("preserveTopology"), filterFactory.literal(true));

        Function p5 = filterFactory.function(
                "parameter",
                filterFactory.literal("outputBBOX"),
                filterFactory.function("env", filterFactory.literal("wms_bbox")));
        Function p6 = filterFactory.function(
                "parameter",
                filterFactory.literal("outputWidth"),
                filterFactory.function("env", filterFactory.literal("wms_width")));
        Function p7 = filterFactory.function(
                "parameter",
                filterFactory.literal("outputHeight"),
                filterFactory.function("env", filterFactory.literal("wms_height")));

        Function rt = filterFactory.function("vec:Simplify", p1, p2, p3, p5, p6, p7);
        featureStyle.setTransformation(rt);

        Rule rule = styleFactory.createRule();
        rule.setName("Za'Ha'Dum");
        featureStyle.rules().add(rule);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap tx = obj.seq("feature-styles").map(0).map("transform");
        assertThat(tx, yHasEntry("name", equalTo("vec:Simplify")));
        assertThat(tx, yHasEntry("input", equalTo("features")));

        YamlMap params = tx.map("params");
        assertThat(params, yHasEntry("distance", lexEqualTo(10.0)));
        assertThat(params, yHasEntry("preserveTopology", lexEqualTo(true)));

        assertThat(params, yHasEntry("outputBBOX", equalTo("${env('wms_bbox')}")));
        assertThat(params, yHasEntry("outputWidth", equalTo("${env('wms_width')}")));
        assertThat(params, yHasEntry("outputHeight", equalTo("${env('wms_height')}")));

        YamlMap ruleMap = obj.seq("feature-styles").map(0).seq("rules").map(0);
        assertThat(ruleMap.str("name"), equalTo("Za'Ha'Dum"));
    }

    @Test
    public void testRenderingTransformationNested() throws IOException {

        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        FeatureTypeStyle featureStyle = styleFactory.createFeatureTypeStyle();
        style.featureTypeStyles().add(featureStyle);

        Function p1_1 = filterFactory.function("parameter", filterFactory.literal("data"));
        Function p1_2 =
                filterFactory.function("parameter", filterFactory.literal("valueAttr"), filterFactory.literal("foo"));
        Function rt1 = filterFactory.function("vec:BarnesSurface", p1_1, p1_2);

        Function p2_1 = filterFactory.function("parameter", filterFactory.literal("data"), rt1);
        Function p2_2 = filterFactory.function(
                "parameter",
                filterFactory.literal("levels"),
                filterFactory.literal(1000),
                filterFactory.literal(1100),
                filterFactory.literal(1200));

        Function rt2 = filterFactory.function("ras:Contour", p2_1, p2_2);
        featureStyle.setTransformation(rt2);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap tx = obj.seq("feature-styles").map(0).map("transform");

        assertThat(tx, yHasEntry("name", lexEqualTo("ras:Contour")));
        assertThat(tx, not(yHasEntry("input")));
        assertThat(
                tx,
                yHasEntry(
                        "params",
                        allOf(
                                yHasEntry(
                                        "data",
                                        allOf(
                                                yHasEntry("name", equalTo("vec:BarnesSurface")),
                                                yHasEntry("input", equalTo("data")), // Specify the input
                                                // parameter
                                                yHasEntry(
                                                        "params",
                                                        allOf(
                                                                yHasEntry("valueAttr", equalTo("foo")),
                                                                not(yHasEntry("data")) // Indicated by the input
                                                                // parameter above
                                                                )))),
                                yHasEntry(
                                        "levels",
                                        allOf(
                                                yHasItem(0, lexEqualTo(1000)),
                                                yHasItem(1, lexEqualTo(1100)),
                                                yHasItem(2, lexEqualTo(1200)))))));
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
        rule.symbolizers().add(sf.lineSymbolizer("line", null, null, null, stroke, null));
        Mark mark = sf.mark(ff.literal("circle"), sf.fill(null, ff.literal("#995555"), null), null);
        List<GraphicalSymbol> symbols = new ArrayList<>();
        symbols.add(mark);
        TextSymbolizer text =
                sf.textSymbolizer(null, ff.property("geom"), null, null, ff.property("name"), null, null, null, null);
        text.setGraphic(sf.graphic(symbols, null, null, null, null, null));
        rule.symbolizers().add(text);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap yaml = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));

        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/1/text"), yHasEntry("label", equalTo("${name}")));
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/1/text"), yHasEntry("graphic"));
        assertThat(
                yaml.lookupY("feature-styles/0/rules/0/symbolizers/1/text/graphic/symbols/0/mark"),
                yHasEntry("shape", equalTo("circle")));
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

        LabelPlacement place = sf.createPointPlacement(
                sf.createAnchorPoint(ff.literal(0.75), ff.literal(0.25)),
                sf.createDisplacement(ff.literal(10), ff.literal(15)),
                ff.literal(90));
        TextSymbolizer text =
                sf.textSymbolizer(null, ff.property("geom"), null, null, ff.property("name"), null, place, null, null);
        rule.symbolizers().add(text);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);
        LOG.fine(out.toString());
        YamlMap yaml = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));

        assertThat(
                yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("placement", equalTo("point")));
        assertThat(
                yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"),
                yHasEntry("displacement", yTuple(numEqualTo(10), numEqualTo(15))));
        assertThat(
                yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"),
                yHasEntry("anchor", yTuple(numEqualTo(0.75, EPSILON), numEqualTo(0.25, EPSILON))));
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("rotation", equalTo(90)));

        assertThat(kvpLine(out.toString(), "displacement"), equalTo("[10, 15]"));
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

        LabelPlacement place = sf.createLinePlacement(ff.literal(10));

        TextSymbolizer text =
                sf.textSymbolizer(null, ff.property("geom"), null, null, ff.property("name"), null, place, null, null);
        rule.symbolizers().add(text);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap yaml = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));

        assertThat(
                yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("placement", equalTo("line")));
        assertThat(yaml.lookupY("feature-styles/0/rules/0/symbolizers/0/text"), yHasEntry("offset", equalTo(10)));
    }

    @Test
    public void testEmptyColorMap() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = sld(styleFactory.createRasterSymbolizer());

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap symbMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("raster");

        assertThat(symbMap, not(yHasEntry("color-map")));
    }

    @Test
    public void testEmptyContrastEnhancement() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = sld(styleFactory.createRasterSymbolizer());

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap symbMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("raster");

        assertFalse(symbMap.has("contrast-enhancement"));
    }

    @Test
    public void testColorMap() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        RasterSymbolizer symb = styleFactory.createRasterSymbolizer();
        ColorMapEntry e1 = styleFactory.createColorMapEntry();
        e1.setColor(filterFactory.literal("#000011"));
        e1.setQuantity(filterFactory.literal(0.0d));
        ColorMapEntry e2 = styleFactory.createColorMapEntry();
        e2.setColor(filterFactory.literal("#0000EE"));
        e2.setQuantity(filterFactory.literal(1.0d));
        symb.getColorMap().addColorMapEntry(e1);
        symb.getColorMap().addColorMapEntry(e2);
        StyledLayerDescriptor sld = sld(symb);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap symbMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("raster");

        assertThat(
                symbMap,
                yHasEntry(
                        "color-map",
                        yHasEntry(
                                "entries",
                                allOf(
                                        yHasItem(
                                                0,
                                                yTuple(
                                                        fakeString("#000011"),
                                                        lexEqualTo(""),
                                                        numEqualTo(0.0, EPSILON),
                                                        lexEqualTo(""))),
                                        yHasItem(
                                                1,
                                                yTuple(
                                                        fakeString("#0000EE"),
                                                        lexEqualTo(""),
                                                        numEqualTo(1.0, EPSILON),
                                                        lexEqualTo("")))))));
    }

    @Test
    public void testColorMapWithExpression() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        RasterSymbolizer symb = styleFactory.createRasterSymbolizer();
        ColorMapEntry e1 = styleFactory.createColorMapEntry();
        e1.setColor(filterFactory.literal("#000011"));
        e1.setQuantity(filterFactory.literal(0.0d));
        ColorMapEntry e2 = styleFactory.createColorMapEntry();
        e2.setColor(filterFactory.literal("#0000EE"));
        e2.setQuantity(filterFactory.function("pow", filterFactory.literal(1.2d), filterFactory.literal(2.0d)));
        symb.getColorMap().addColorMapEntry(e1);
        symb.getColorMap().addColorMapEntry(e2);
        StyledLayerDescriptor sld = sld(symb);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap symbMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("raster");

        assertThat(
                symbMap,
                yHasEntry(
                        "color-map",
                        yHasEntry(
                                "entries",
                                allOf(
                                        yHasItem(
                                                0,
                                                yTuple(
                                                        fakeString("#000011"),
                                                        lexEqualTo(""),
                                                        numEqualTo(0.0, EPSILON),
                                                        lexEqualTo(""))),
                                        yHasItem(
                                                1,
                                                yTuple(
                                                        fakeString("#0000EE"),
                                                        lexEqualTo(""),
                                                        equalTo("${pow(1.2,2.0)}"),
                                                        lexEqualTo("")))))));
    }

    @Test
    public void testExpressionNil() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        p.getGraphic().setSize(Expression.NIL);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        LOG.fine(out.toString());
    }

    @Test
    public void testNameExpressionLiteral() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression nameExpression = CommonFactoryFinder.getFilterFactory().literal("test");
        Mark mark = CommonFactoryFinder.getStyleFactory().createMark();
        mark.setWellKnownName(nameExpression);
        p.getGraphic().graphicalSymbols().add(mark);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        String result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .seq("symbols")
                .map(0)
                .map("mark")
                .str("shape");

        assertThat(result, equalTo("test"));
    }

    @Test
    public void testNameExpressionAttribute() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression nameExpression = CommonFactoryFinder.getFilterFactory().property("test");
        Mark mark = CommonFactoryFinder.getStyleFactory().createMark();
        mark.setWellKnownName(nameExpression);
        p.getGraphic().graphicalSymbols().add(mark);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        String result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .seq("symbols")
                .map(0)
                .map("mark")
                .str("shape");

        assertThat(result, equalTo("${test}"));
    }

    @Test
    public void testNonNameExpressionLiteral() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory().literal("test");
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        String result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .str("geometry");

        assertThat(result, equalTo("test"));
    }

    @Test
    public void testNonNameExpressionAttribute() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory().property("test");
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        String result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .str("geometry");

        assertThat(result, equalTo("${test}"));
    }

    @Test
    public void testEmbededExpression() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory()
                .function(
                        "Concatenate",
                        CommonFactoryFinder.getFilterFactory().literal("literal0"),
                        CommonFactoryFinder.getFilterFactory().property("attribute1"),
                        CommonFactoryFinder.getFilterFactory().literal("literal2"));
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        String result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .str("geometry");

        assertThat(result, equalTo("literal0${attribute1}literal2"));
    }

    @Test
    public void testEmbededExpressionEscapeLiteral() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory().literal("$}\\");
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        String result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .str("geometry");

        assertThat(result, equalTo("\\$\\}\\\\"));
    }

    @Test
    public void testEmbededExpressionEscapeExpression() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        Expression expression = CommonFactoryFinder.getFilterFactory()
                .function(
                        "strEndsWith",
                        CommonFactoryFinder.getFilterFactory().property("attribute1"),
                        CommonFactoryFinder.getFilterFactory().literal("}"));
        p.setGeometry(expression);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        String result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .str("geometry");

        assertThat(result, equalTo("${strEndsWith(attribute1,'\\}')}"));
    }

    @Test
    public void testFilter() throws Exception {
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        Rule rule = styleFactory.createRule();

        rule.setFilter(filterFactory.less(filterFactory.property("foo"), filterFactory.literal(2)));

        style.featureTypeStyles().add(styleFactory.createFeatureTypeStyle());
        style.featureTypeStyles().get(0).rules().add(rule);

        PointSymbolizer p = styleFactory.createPointSymbolizer();
        rule.symbolizers().add(p);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles").map(0).seq("rules").map(0);

        assertThat(result, yHasEntry("filter", equalTo("${foo < 2}")));
    }

    @Test
    public void testFilterEscape() throws Exception {
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        Rule rule = styleFactory.createRule();

        rule.setFilter(filterFactory.less(filterFactory.property("foo"), filterFactory.literal("}$")));

        style.featureTypeStyles().add(styleFactory.createFeatureTypeStyle());
        style.featureTypeStyles().get(0).rules().add(rule);

        PointSymbolizer p = styleFactory.createPointSymbolizer();
        rule.symbolizers().add(p);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles").map(0).seq("rules").map(0);

        assertThat(result, yHasEntry("filter", equalTo("${foo < '\\}\\$'}")));
    }

    @Test
    public void testScale() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        style.featureTypeStyles().add(styleFactory.createFeatureTypeStyle());

        Rule rule = styleFactory.createRule();

        rule.setMinScaleDenominator(5_000_000);
        rule.setMaxScaleDenominator(10_000_000);

        style.featureTypeStyles().get(0).rules().add(rule);

        rule = styleFactory.createRule();

        rule.setMinScaleDenominator(2_000_000);
        rule.setMaxScaleDenominator(5_000_000);

        style.featureTypeStyles().get(0).rules().add(rule);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlSeq result = obj.seq("feature-styles").map(0).seq("rules");

        assertThat(
                result,
                yHasItem(0, yHasEntry("scale", yTuple(numEqualTo(5_000_000, 0.1), numEqualTo(10_000_000, 0.1)))));
        assertThat(
                result,
                yHasItem(1, yHasEntry("scale", yTuple(numEqualTo(2_000_000, 0.1), numEqualTo(5_000_000, 0.1)))));
    }

    @Test
    public void testScaleMinMaxKeywords() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);

        style.featureTypeStyles().add(styleFactory.createFeatureTypeStyle());

        Rule rule = styleFactory.createRule();

        rule.setMinScaleDenominator(5_000_000);

        style.featureTypeStyles().get(0).rules().add(rule);

        rule = styleFactory.createRule();

        rule.setMaxScaleDenominator(5_000_000);

        style.featureTypeStyles().get(0).rules().add(rule);

        StringWriter out = new StringWriter();
        Ysld.encode(sld, out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlSeq result = obj.seq("feature-styles").map(0).seq("rules");

        assertThat(result, yHasItem(0, yHasEntry("scale", yTuple(numEqualTo(5_000_000, 0.1), equalTo("max")))));
        assertThat(result, yHasItem(1, yHasEntry("scale", yTuple(equalTo("min"), numEqualTo(5_000_000, 0.1)))));
    }

    @Test
    public void testGrayBandSelection() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory();
        RasterSymbolizer r = factory.createRasterSymbolizer();
        ChannelSelection sel =
                factory.channelSelection(factory.createSelectedChannelType("foo", factory.createContrastEnhancement()));
        r.setChannelSelection(sel);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(r), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap channelMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("raster")
                .map("channels");

        assertThat(channelMap, yHasEntry("gray"));
        assertThat(channelMap, not(yHasEntry("red")));
        assertThat(channelMap, not(yHasEntry("green")));
        assertThat(channelMap, not(yHasEntry("blue")));

        assertThat(channelMap.map("gray"), yHasEntry("name", equalTo("foo")));
        assertThat(channelMap.map("gray"), not(yHasEntry("contrast-enhancement")));
    }

    @Test
    public void testRGBBandSelection() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory();
        RasterSymbolizer r = factory.createRasterSymbolizer();
        ChannelSelection sel = factory.channelSelection(
                factory.createSelectedChannelType("foo", factory.createContrastEnhancement()),
                factory.createSelectedChannelType("bar", factory.createContrastEnhancement()),
                factory.createSelectedChannelType("baz", factory.createContrastEnhancement()));
        r.setChannelSelection(sel);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(r), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap channelMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("raster")
                .map("channels");

        assertThat(channelMap, not(yHasEntry("gray")));
        assertThat(channelMap, yHasEntry("red"));
        assertThat(channelMap, yHasEntry("green"));
        assertThat(channelMap, yHasEntry("blue"));

        assertThat(channelMap.map("red"), yHasEntry("name", equalTo("foo")));
        assertThat(channelMap.map("red"), not(yHasEntry("contrast-enhancement")));
        assertThat(channelMap.map("green"), yHasEntry("name", equalTo("bar")));
        assertThat(channelMap.map("green"), not(yHasEntry("contrast-enhancement")));
        assertThat(channelMap.map("blue"), yHasEntry("name", equalTo("baz")));
        assertThat(channelMap.map("blue"), not(yHasEntry("contrast-enhancement")));

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

    @Test
    public void testStrokeGraphic() throws Exception {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        LineSymbolizer l = sf.createLineSymbolizer();
        Mark mark = sf.mark(ff.literal("circle"), sf.fill(null, ff.literal("#995555"), null), null);
        Graphic g = sf.createGraphic(null, new Mark[] {mark}, null, null, null, null);
        Stroke s = sf.createStroke(null, null);
        s.setGraphicStroke(g);
        l.setStroke(s);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(l), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap lineMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("line");

        assertThat(
                lineMap,
                yHasEntry(
                        "stroke-graphic",
                        yHasEntry(
                                "symbols",
                                yHasItem(
                                        0,
                                        yHasEntry(
                                                "mark",
                                                allOf(
                                                        yHasEntry("shape", equalTo("circle")),
                                                        yHasEntry("fill-color", equalTo("#995555"))))))));
    }

    @Test
    public void testStrokeGraphicFill() throws Exception {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        LineSymbolizer l = sf.createLineSymbolizer();
        Mark mark = sf.mark(ff.literal("circle"), sf.fill(null, ff.literal("#995555"), null), null);
        Graphic g = sf.createGraphic(null, new Mark[] {mark}, null, null, null, null);
        Stroke s = sf.createStroke(null, ff.literal(10));
        s.setGraphicFill(g);
        l.setStroke(s);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(l), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap lineMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("line");

        assertThat(
                lineMap,
                yHasEntry(
                        "stroke-graphic-fill",
                        yHasEntry(
                                "symbols",
                                yHasItem(
                                        0,
                                        yHasEntry(
                                                "mark",
                                                allOf(
                                                        yHasEntry("shape", equalTo("circle")),
                                                        yHasEntry("fill-color", equalTo("#995555"))))))));
    }

    @Test
    public void testGrayBandSelectionWithContrast() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();
        RasterSymbolizer r = styleFactory.createRasterSymbolizer();
        ChannelSelection sel = styleFactory.channelSelection(styleFactory.createSelectedChannelType(
                "foo", styleFactory.createContrastEnhancement(filterFactory.literal(1.2))));
        r.setChannelSelection(sel);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(r), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap channelMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("raster")
                .map("channels");

        assertThat(channelMap, yHasEntry("gray"));
        assertThat(channelMap, not(yHasEntry("red")));
        assertThat(channelMap, not(yHasEntry("green")));
        assertThat(channelMap, not(yHasEntry("blue")));

        assertThat(channelMap.map("gray"), yHasEntry("name", equalTo("foo")));
        assertThat(channelMap.map("gray"), yHasEntry("contrast-enhancement", yHasEntry("gamma", equalTo(1.2d))));
    }

    @Test
    public void testRGBBandSelectionWithContrast() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();
        RasterSymbolizer r = styleFactory.createRasterSymbolizer();
        ChannelSelection sel = styleFactory.channelSelection(
                styleFactory.createSelectedChannelType("foo", styleFactory.createContrastEnhancement()),
                styleFactory.createSelectedChannelType(
                        "bar", styleFactory.createContrastEnhancement(filterFactory.literal(1.2))),
                styleFactory.createSelectedChannelType("baz", styleFactory.createContrastEnhancement()));
        sel.getRGBChannels()[2].getContrastEnhancement().setMethod(ContrastMethod.HISTOGRAM);
        r.setChannelSelection(sel);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(r), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap channelMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("raster")
                .map("channels");

        assertThat(channelMap, not(yHasEntry("gray")));
        assertThat(channelMap, yHasEntry("red"));
        assertThat(channelMap, yHasEntry("green"));
        assertThat(channelMap, yHasEntry("blue"));

        assertThat(channelMap.map("red"), yHasEntry("name", equalTo("foo")));
        assertThat(channelMap.map("red"), not(yHasEntry("contrast-enhancement")));
        assertThat(channelMap.map("green"), yHasEntry("name", equalTo("bar")));
        assertThat(channelMap.map("green"), yHasEntry("contrast-enhancement", yHasEntry("gamma", equalTo(1.2d))));
        assertThat(channelMap.map("blue"), yHasEntry("name", equalTo("baz")));
        assertThat(channelMap.map("blue"), yHasEntry("contrast-enhancement", yHasEntry("mode", equalTo("histogram"))));
    }

    @Test
    public void testBandSelectionExpression() throws Exception {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();
        Expression nameExpression =
                filterFactory.function("env", filterFactory.literal("B1"), filterFactory.literal("1"));
        RasterSymbolizer r = styleFactory.createRasterSymbolizer();
        ChannelSelection sel = styleFactory.channelSelection(
                styleFactory.createSelectedChannelType(nameExpression, (Expression) null));
        r.setChannelSelection(sel);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(r), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap channelMap = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("raster")
                .map("channels")
                .map("gray");
        assertThat(channelMap, yHasEntry("name", equalTo("${env('B1','1')}")));
    }

    StyledLayerDescriptor sld(Symbolizer sym) {
        return sld(fts(sym));
    }

    private FeatureTypeStyle fts(Symbolizer sym) {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        Rule rule = styleFactory.createRule();

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();
        fts.rules().add(rule);

        rule.symbolizers().add(sym);
        return fts;
    }

    private StyledLayerDescriptor sld(FeatureTypeStyle fts) {
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        UserLayer layer = styleFactory.createUserLayer();
        sld.layers().add(layer);

        Style style = styleFactory.createStyle();
        layer.userStyles().add(style);
        style.featureTypeStyles().add(fts);
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

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap text = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("text");
        assertEquals("${foo}", text.str("priority"));
    }

    public static String kvpLine(String text, String key) {
        Pattern p = Pattern.compile("^\\s*" + key + ":\\s*(.*?)\\s*$");
        for (String line : text.split("\\r?\\n")) {
            java.util.regex.Matcher m = p.matcher(line);
            if (m.matches()) {
                return m.group(1);
            }
        }
        return null;
    }

    @Test
    public void testColourNoQuotes() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();

        Mark m1 = CommonFactoryFinder.getStyleFactory().getCircleMark();
        m1.setFill(CommonFactoryFinder.getStyleFactory()
                .createFill(CommonFactoryFinder.getFilterFactory().literal("#112233")));
        m1.setStroke(CommonFactoryFinder.getStyleFactory()
                .createStroke(
                        CommonFactoryFinder.getFilterFactory().literal("#005566"),
                        CommonFactoryFinder.getFilterFactory().literal(3)));
        p.getGraphic().graphicalSymbols().add(m1);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        LOG.fine(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .seq("symbols")
                .map(0)
                .map("mark");

        assertThat(result, yHasEntry("fill-color", isColor("112233")));
        assertThat(result, yHasEntry("stroke-color", isColor("005566")));

        assertThat(kvpLine(out.toString(), "fill-color"), equalTo("'#112233'"));
        assertThat(kvpLine(out.toString(), "stroke-color"), equalTo("'#005566'"));
    }

    @Test
    public void testScientificNotationCase() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();

        Mark m1 = CommonFactoryFinder.getStyleFactory().getCircleMark();
        m1.setFill(CommonFactoryFinder.getStyleFactory()
                .createFill(
                        CommonFactoryFinder.getFilterFactory().literal("#112233"),
                        CommonFactoryFinder.getFilterFactory().literal(Double.MIN_NORMAL)));
        p.getGraphic().graphicalSymbols().add(m1);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);
        LOG.fine(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .seq("symbols")
                .map(0)
                .map("mark");

        assertThat(
                kvpLine(out.toString(), "fill-opacity"),
                equalTo(Double.toString(Double.MIN_NORMAL).toUpperCase()));
    }

    @Test
    public void testColourCase() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();

        Mark m1 = CommonFactoryFinder.getStyleFactory().getCircleMark();
        m1.setFill(CommonFactoryFinder.getStyleFactory()
                .createFill(
                        CommonFactoryFinder.getFilterFactory().literal("#abcdef"),
                        CommonFactoryFinder.getFilterFactory().literal(Double.MIN_NORMAL)));
        p.getGraphic().graphicalSymbols().add(m1);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        LOG.fine(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .seq("symbols")
                .map(0)
                .map("mark");

        assertThat(kvpLine(out.toString(), "fill-color"), equalTo("'#ABCDEF'"));
    }

    @Test
    public void testColourLiteral() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();

        Mark m1 = CommonFactoryFinder.getStyleFactory().getCircleMark();
        m1.setFill(CommonFactoryFinder.getStyleFactory()
                .createFill(
                        CommonFactoryFinder.getFilterFactory().literal(Color.RED),
                        CommonFactoryFinder.getFilterFactory().literal(Double.MIN_NORMAL)));
        p.getGraphic().graphicalSymbols().add(m1);

        StringWriter out = new StringWriter();
        Ysld.encode(sld(p), out);

        LOG.fine(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point")
                .seq("symbols")
                .map(0)
                .map("mark");

        assertThat(kvpLine(out.toString(), "fill-color"), equalTo("'#FF0000'"));
    }

    @Test
    public void testFTSVendorOption() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        FeatureTypeStyle fts = fts(p);
        fts.getOptions().put("foo", "bar");

        StringWriter out = new StringWriter();
        Ysld.encode(sld(fts), out);

        LOG.fine(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles").map(0);

        assertThat(result.str("x-foo"), equalTo("bar"));
    }

    @Test
    public void testSymbolizerVendorOption() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        FeatureTypeStyle fts = fts(p);
        p.getOptions().put("foo", "bar");

        StringWriter out = new StringWriter();
        Ysld.encode(sld(fts), out);

        LOG.fine(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point");

        assertThat(result.str("x-foo"), equalTo("bar"));
    }

    @Test
    public void testSymbolizerUoMMetre() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        FeatureTypeStyle fts = fts(p);
        p.setUnitOfMeasure(UomOgcMapping.METRE.getUnit());

        StringWriter out = new StringWriter();
        Ysld.encode(sld(fts), out);

        LOG.fine(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point");

        assertThat(result.str("uom"), equalTo("metre"));
    }

    @Test
    public void testSymbolizerUoMFoot() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        FeatureTypeStyle fts = fts(p);
        p.setUnitOfMeasure(UomOgcMapping.FOOT.getUnit());

        StringWriter out = new StringWriter();
        Ysld.encode(sld(fts), out);

        LOG.fine(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point");

        assertThat(result.str("uom"), equalTo("foot"));
    }

    @Test
    public void testSymbolizerUoMPixel() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        FeatureTypeStyle fts = fts(p);
        p.setUnitOfMeasure(UomOgcMapping.PIXEL.getUnit());

        StringWriter out = new StringWriter();
        Ysld.encode(sld(fts), out);

        LOG.fine(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .seq("symbolizers")
                .map(0)
                .map("point");

        assertThat(result.str("uom"), equalTo("pixel"));
    }

    @Test
    public void testSymbolizerUoMOther() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        FeatureTypeStyle fts = fts(p);
        p.setUnitOfMeasure(USCustomary.LIGHT_YEAR);

        StringWriter out = new StringWriter();

        Assert.assertThrows(IllegalArgumentException.class, () -> Ysld.encode(sld(fts), out));
    }

    @Test
    public void testLegend() throws Exception {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        PointSymbolizer p = sf.createPointSymbolizer();
        Mark mark = sf.mark(ff.literal("circle"), sf.fill(null, ff.literal("#FF0000"), null), null);
        p.setGraphic(sf.createGraphic(null, new Mark[] {mark}, null, null, null, null));
        Rule rule = sf.createRule();
        rule.symbolizers().add(p);
        ExternalGraphic eg = sf.createExternalGraphic("smileyface.png", "image/png");
        rule.setLegend((GraphicLegend) sf.createGraphic(new ExternalGraphic[] {eg}, null, null, null, null, null));

        StringWriter out = new StringWriter();
        Ysld.encode(sld(sf.createFeatureTypeStyle(rule)), out);
        // System.out.append(out.toString());

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles")
                .map(0)
                .seq("rules")
                .map(0)
                .map("legend")
                .seq("symbols")
                .map(0)
                .map("external");
        assertEquals("smileyface.png", result.str("url"));
        assertEquals("image/png", result.str("format"));
    }

    @Test
    public void testEncodeColorMapEntry() throws IOException {
        StyledLayerDescriptor style = new YsldParser(new ByteArrayInputStream(("name:  Test\n"
                                + "title: Test Style title\n"
                                + "abstract: Styling of Test layer\n"
                                + "feature-styles:\n"
                                + "- rules:\n"
                                + "  - title: raster\n"
                                + "    symbolizers:\n"
                                + "      - raster:\n"
                                + "          opacity: 1.0\n"
                                + "          color-map:\n"
                                + "            type: values\n"
                                + "            entries:\n"
                                + "            - ['#e20374', 1.0, 1, Lorem Ipsum (magenta = covered)]")
                        .getBytes(StandardCharsets.UTF_8)))
                .parse();

        RasterSymbolizer symbolizer = (RasterSymbolizer) ((NamedLayer) style.getStyledLayers()[0])
                .styles()
                .get(0)
                .featureTypeStyles()
                .get(0)
                .rules()
                .get(0)
                .symbolizers()
                .get(0);

        ColorMap colorMap = symbolizer.getColorMap();
        RasterSymbolizerEncoder.ColorMapEntryIterator iterator =
                new RasterSymbolizerEncoder(symbolizer).new ColorMapEntryIterator(colorMap);
        Tuple map = iterator.next();
        assertEquals("('#E20374',1.0,1,Lorem Ipsum (magenta = covered))", map.toString());
    }

    @Test
    public void testRuleVendorOption() throws Exception {
        PointSymbolizer p = CommonFactoryFinder.getStyleFactory().createPointSymbolizer();
        FeatureTypeStyle fts = fts(p);
        fts.rules().get(0).getOptions().put("foo", "bar");

        StringWriter out = new StringWriter();
        Ysld.encode(sld(fts), out);

        YamlMap obj = new YamlMap(YamlUtil.getSafeYaml().load(out.toString()));
        YamlMap result = obj.seq("feature-styles").map(0).seq("rules").map(0);

        assertEquals("bar", result.str("x-foo"));
    }
}
