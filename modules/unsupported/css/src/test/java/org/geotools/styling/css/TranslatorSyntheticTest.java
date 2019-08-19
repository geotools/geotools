/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.css;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.geotools.filter.function.EnvFunction;
import org.geotools.filter.function.color.DarkenFunction;
import org.geotools.filter.function.color.SaturateFunction;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.Halo;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Stroke;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.xml.styling.SLDTransformer;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.style.ContrastMethod;
import org.opengis.style.Displacement;
import org.opengis.style.FeatureTypeStyle;
import org.opengis.style.GraphicFill;
import org.opengis.style.Mark;
import org.opengis.style.Rule;
import org.opengis.style.Style;
import org.opengis.style.Symbolizer;
import org.parboiled.errors.ParserRuntimeException;

public class TranslatorSyntheticTest extends CssBaseTest {

    private void assertLiteral(String value, Expression ex) {
        assertTrue(ex instanceof Literal);
        String actual = ex.evaluate(null, String.class);
        assertEquals(value, actual);
    }

    private void assertExpression(String expectedCql, Expression actual) throws CQLException {
        Expression expected = ECQL.toExpression(expectedCql);
        assertEquals(expected, actual);
    }

    private void assertFilter(String expectedCql, Filter actual) throws CQLException {
        Filter expected = ECQL.toFilter(expectedCql);
        assertEquals(expected, actual);
    }

    private void assertVendorOption(
            String expectedValue, String name, org.geotools.styling.Symbolizer ps) {
        assertEquals(expectedValue, ps.getOptions().get(name));
    }

    private Rule assertSingleRule(Style style) {
        FeatureTypeStyle fts = assertSingleFeatureTypeStyle(style);
        assertEquals("Expected 1 rule, found " + fts.rules().size(), 1, fts.rules().size());
        return fts.rules().get(0);
    }

    private FeatureTypeStyle assertSingleFeatureTypeStyle(Style style) {
        assertEquals(
                "Expected single feature type style, found " + style.featureTypeStyles().size(),
                1,
                style.featureTypeStyles().size());
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        return fts;
    }

    private <T extends Symbolizer> T assertSingleSymbolizer(Rule rule, Class<T> symbolizerType) {
        assertEquals(1, rule.symbolizers().size());
        assertTrue(symbolizerType.isInstance(rule.symbolizers().get(0)));
        return (T) rule.symbolizers().get(0);
    }

    @Test
    public void fillBasic() throws IOException {
        String css = "* { fill: orange; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        assertLiteral("#ffa500", fill.getColor());
        assertLiteral("1", fill.getOpacity());
        assertNull(fill.getGraphicFill());
    }

    @Test
    public void fillOpacity() throws Exception {
        String css = "* { fill: orange; fill-opacity: 0.5; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        assertLiteral("#ffa500", fill.getColor());
        assertLiteral("0.5", fill.getOpacity());
        assertNull(fill.getGraphicFill());
    }

    @Test
    public void fillCircleDefaults() throws Exception {
        String css = "* { fill: symbol('circle'); }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        GraphicFill gf = fill.getGraphicFill();
        assertEquals(1, gf.graphicalSymbols().size());
        Mark mark = (Mark) gf.graphicalSymbols().get(0);
        assertLiteral("circle", mark.getWellKnownName());
        Fill markFill = (Fill) mark.getFill();
        assertLiteral("#808080", markFill.getColor());
        Stroke markStroke = (Stroke) mark.getStroke();
        assertLiteral("#000000", markStroke.getColor());
        assertLiteral("1", markStroke.getWidth());
        assertNull(gf.getSize());
        assertLiteral("1.0", gf.getOpacity());
        assertLiteral("0", gf.getRotation());
    }

    @Test
    public void fillCircleFilledMark() throws Exception {
        String css = "* { fill: symbol('circle');} :fill { fill: yellow;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        Graphic gf = fill.getGraphicFill();
        assertEquals(1, gf.graphicalSymbols().size());
        Mark mark = (Mark) gf.graphicalSymbols().get(0);
        assertLiteral("circle", mark.getWellKnownName());
        assertLiteral("#ffff00", mark.getFill().getColor());
        assertNull(mark.getStroke());
        assertNull(gf.getSize());
        assertLiteral("1.0", gf.getOpacity());
        assertLiteral("0", gf.getRotation());
    }

    @Test
    public void fillCircleStrokedMark() throws Exception {
        String css = "* { fill: symbol('circle');} :fill { stroke: black;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        Graphic gf = fill.getGraphicFill();
        assertEquals(1, gf.graphicalSymbols().size());
        Mark mark = (Mark) gf.graphicalSymbols().get(0);
        assertLiteral("circle", mark.getWellKnownName());
        assertNull(mark.getFill());
        assertLiteral("#000000", mark.getStroke().getColor());
        assertNull(gf.getSize());
        assertLiteral("1.0", gf.getOpacity());
        assertLiteral("0", gf.getRotation());
    }

    @Test
    public void fillCircleFilledStrokedMark() throws Exception {
        String css =
                "* { fill: symbol('circle');} :fill { fill: yellow; stroke: black; stroke-width: 3;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        Graphic gf = fill.getGraphicFill();
        assertEquals(1, gf.graphicalSymbols().size());
        Mark mark = (Mark) gf.graphicalSymbols().get(0);
        assertLiteral("circle", mark.getWellKnownName());
        assertLiteral("#ffff00", mark.getFill().getColor());
        assertLiteral("#000000", mark.getStroke().getColor());
        assertLiteral("3", mark.getStroke().getWidth());
        assertNull(gf.getSize());
        assertLiteral("1.0", gf.getOpacity());
        assertLiteral("0", gf.getRotation());
    }

    @Test
    public void fillExternalGraphic() throws Exception {
        String css = "* { fill: url(test.svg); fill-rotation: 45; fill-size: 35;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        GraphicFill gf = fill.getGraphicFill();
        assertEquals(1, gf.graphicalSymbols().size());
        ExternalGraphic eg = (ExternalGraphic) gf.graphicalSymbols().get(0);
        assertEquals("test.svg", eg.getURI());
        assertLiteral("35", gf.getSize());
        assertLiteral("1.0", gf.getOpacity());
        assertLiteral("45", gf.getRotation());
    }

    @Test
    public void fillVendorOptions() throws Exception {
        String css = "* { fill: yellow; -gt-fill-label-obstacle: true;} ";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        assertLiteral("#ffff00", fill.getColor());
        assertVendorOption("true", "labelObstacle", ps);
    }

    @Test
    public void fillGeometry() throws Exception {
        String css = "* { fill-geometry: [centroid(theGeom)]; fill: red;} ";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        assertExpression("centroid(theGeom)", ps.getGeometry());
        Fill fill = ps.getFill();
        assertLiteral("#ff0000", fill.getColor());
    }

    @Test
    public void strokeBasic() throws IOException {
        String css = "* { stroke: orange; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        Stroke stroke = ls.getStroke();
        assertLiteral("#ffa500", stroke.getColor());
        assertLiteral("1", stroke.getWidth());
        assertLiteral("1", stroke.getOpacity());
        assertLiteral("butt", stroke.getLineCap());
        assertLiteral("miter", stroke.getLineJoin());
        assertNull(stroke.getDashArray());
        assertLiteral("0", stroke.getDashOffset());
        assertNull(stroke.getGraphicFill());
        assertNull(stroke.getGraphicStroke());
    }

    @Test
    public void strokeDashed() throws IOException {
        String css =
                "* { stroke: orange; stroke-width: 10; stroke-dasharray: 10 5 1 5; stroke-dashoffset: 2; stroke-linecap: round; stroke-linejoin: round;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        Stroke stroke = ls.getStroke();
        assertLiteral("#ffa500", stroke.getColor());
        assertLiteral("10", stroke.getWidth());
        assertLiteral("1", stroke.getOpacity());
        assertLiteral("round", stroke.getLineCap());
        assertLiteral("round", stroke.getLineJoin());
        assertTrue(Arrays.equals(new float[] {10, 5, 1, 5}, stroke.getDashArray()));
        assertLiteral("2", stroke.getDashOffset());
        assertNull(stroke.getGraphicFill());
        assertNull(stroke.getGraphicStroke());
    }

    @Test
    public void strokeRepeatedMark() throws IOException {
        String css = "* { stroke: symbol('square');} :stroke { fill: red; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        Stroke stroke = ls.getStroke();
        Graphic graphic = stroke.getGraphicStroke();
        assertEquals(1, graphic.graphicalSymbols().size());
        Mark mark = (Mark) graphic.graphicalSymbols().get(0);
        assertLiteral("square", mark.getWellKnownName());
        assertLiteral("#ff0000", mark.getFill().getColor());
    }

    @Test
    public void strokeFillMark() throws IOException {
        String css =
                "* { stroke: symbol('square'); stroke-size: 20; stroke-repeat: stipple;} :stroke { fill: red; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        Stroke stroke = ls.getStroke();
        Graphic graphic = stroke.getGraphicFill();
        assertEquals(1, graphic.graphicalSymbols().size());
        Mark mark = (Mark) graphic.graphicalSymbols().get(0);
        assertLiteral("square", mark.getWellKnownName());
        assertLiteral("#ff0000", mark.getFill().getColor());
        assertLiteral("20", graphic.getSize());
    }

    @Test
    public void fillStroke() throws Exception {
        String css = "* { fill: red; stroke: yellow;} ";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        assertLiteral("#ff0000", fill.getColor());
        Stroke stroke = ps.getStroke();
        assertLiteral("#ffff00", stroke.getColor());
    }

    @Test
    public void fillStrokeLabelObstacle() throws Exception {
        String css = "* { fill: red; stroke: yellow; -gt-stroke-label-obstacle: true;} ";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        assertEquals(2, rule.symbolizers().size());
        PolygonSymbolizer ps = (PolygonSymbolizer) rule.symbolizers().get(0);
        Fill fill = ps.getFill();
        assertLiteral("#ff0000", fill.getColor());
        assertTrue(ps.getOptions().isEmpty());
        LineSymbolizer ls = (LineSymbolizer) rule.symbolizers().get(1);
        Stroke stroke = ls.getStroke();
        assertLiteral("#ffff00", stroke.getColor());
        assertVendorOption("true", "labelObstacle", ls);
    }

    @Test
    public void mark() throws Exception {
        String css =
                "* { mark: symbol(circle); mark-size: 10; mark-rotation: 45; mark-geometry: [centroid(the_geom)];} :mark { fill: blue; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PointSymbolizer ps = assertSingleSymbolizer(rule, PointSymbolizer.class);
        assertExpression("centroid(the_geom)", ps.getGeometry());
        Graphic g = ps.getGraphic();
        assertLiteral("10", g.getSize());
        assertLiteral("45", g.getRotation());
        assertNotNull(g);
        assertEquals(1, g.graphicalSymbols().size());
        Mark mark = (Mark) g.graphicalSymbols().get(0);
        assertLiteral("circle", mark.getWellKnownName());
        assertLiteral("#0000ff", mark.getFill().getColor());
    }

    @Test
    public void markAnchorDisplacement() throws Exception {
        String css =
                "* { mark: symbol(circle); mark-size: 10; mark-anchor: 0 1; mark-offset: 10 20;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PointSymbolizer ps = assertSingleSymbolizer(rule, PointSymbolizer.class);
        Graphic g = ps.getGraphic();
        AnchorPoint ap = g.getAnchorPoint();
        assertNotNull(ap);
        assertEquals(0, ap.getAnchorPointX().evaluate(null, Double.class), 0d);
        assertEquals(1, ap.getAnchorPointY().evaluate(null, Double.class), 0d);
        Displacement d = g.getDisplacement();
        assertNotNull(d);
        assertEquals(10, d.getDisplacementX().evaluate(null, Double.class), 0d);
        assertEquals(20, d.getDisplacementY().evaluate(null, Double.class), 0d);
    }

    @Test
    public void markAnchorDisplacementExpressions() throws Exception {
        String css =
                "* { mark: symbol(circle); mark-size: 10; mark-anchor: [a1] [a2]; mark-offset: [o1] [o2];}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PointSymbolizer ps = assertSingleSymbolizer(rule, PointSymbolizer.class);
        Graphic g = ps.getGraphic();
        AnchorPoint ap = g.getAnchorPoint();
        assertNotNull(ap);
        assertExpression("a1", ap.getAnchorPointX());
        assertExpression("a2", ap.getAnchorPointY());
        Displacement d = g.getDisplacement();
        assertNotNull(d);
        assertExpression("o1", d.getDisplacementX());
        assertExpression("o2", d.getDisplacementY());
    }

    @Test
    public void markAnchorDisplacementSingleValue() throws Exception {
        String css = "* { mark: symbol(circle); mark-size: 10; mark-anchor: 0.5; mark-offset: 10;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PointSymbolizer ps = assertSingleSymbolizer(rule, PointSymbolizer.class);
        Graphic g = ps.getGraphic();
        AnchorPoint ap = g.getAnchorPoint();
        assertNotNull(ap);
        assertEquals(0.5, ap.getAnchorPointX().evaluate(null, Double.class), 0d);
        assertEquals(0.5, ap.getAnchorPointY().evaluate(null, Double.class), 0d);
        Displacement d = g.getDisplacement();
        assertNotNull(d);
        assertEquals(10, d.getDisplacementX().evaluate(null, Double.class), 0d);
        assertEquals(10, d.getDisplacementY().evaluate(null, Double.class), 0d);
    }

    @Test
    public void externalGraphic() throws Exception {
        String css =
                "* { mark: url(test.svg); mark-size: 10; mark-rotation: 45; mark-mime: 'image/png';}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PointSymbolizer ps = assertSingleSymbolizer(rule, PointSymbolizer.class);
        Graphic g = ps.getGraphic();
        assertLiteral("10", g.getSize());
        assertLiteral("45", g.getRotation());
        assertNotNull(g);
        assertEquals(1, g.graphicalSymbols().size());
        ExternalGraphic eg = (ExternalGraphic) g.graphicalSymbols().get(0);
        assertEquals("image/png", eg.getFormat());
        assertEquals("test.svg", eg.getURI());
    }

    @Test
    public void labelBasic() throws Exception {
        String css = "* { label: 'test'; label-geometry: [centroid(the_geom)];}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        assertLiteral("test", ts.getLabel());
        assertExpression("centroid(the_geom)", ts.getGeometry());
    }

    @Test
    public void labelPointPlacement() throws Exception {
        String css =
                "* { label: 'test'; label-offset: 5 5; label-rotation: 45; label-anchor: 0.1 0.9;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        assertLiteral("test", ts.getLabel());
        PointPlacement pp = (PointPlacement) ts.getLabelPlacement();
        assertLiteral("5", pp.getDisplacement().getDisplacementX());
        assertLiteral("5", pp.getDisplacement().getDisplacementY());
        assertLiteral("45", pp.getRotation());
        assertLiteral("0.1", pp.getAnchorPoint().getAnchorPointX());
        assertLiteral("0.9", pp.getAnchorPoint().getAnchorPointY());
    }

    @Test
    public void labelLinePlacement() throws Exception {
        String css = "* { label: 'test'; label-offset: 5;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        assertLiteral("test", ts.getLabel());
        LinePlacement lp = (LinePlacement) ts.getLabelPlacement();
        assertLiteral("5", lp.getPerpendicularOffset());
    }

    @Test
    public void labelHalo() throws Exception {
        String css = "* { label: 'test'; halo-color: white; halo-radius: 3; halo-opacity: 0.8;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        assertLiteral("test", ts.getLabel());
        Halo halo = ts.getHalo();
        assertLiteral("#ffffff", halo.getFill().getColor());
        assertLiteral("0.8", halo.getFill().getOpacity());
        assertLiteral("3", halo.getRadius());
    }

    @Test
    public void labelFont() throws Exception {
        String css =
                "* { label: 'test'; font-family: 'Arial'; font-fill: blue; font-weight: normal; font-style: italic; font-size: 20;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        assertLiteral("test", ts.getLabel());
        Font font = ts.getFont();
        assertLiteral("Arial", font.getFamily().get(0));
        assertLiteral("#0000ff", ts.getFill().getColor());
        assertLiteral("normal", font.getWeight());
        assertLiteral("italic", font.getStyle());
        assertLiteral("20", font.getSize());
    }

    @Test
    public void labelShield() throws Exception {
        String css = "* { label: 'test'; shield: symbol(square);} :shield {fill:black;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer2 ts = assertSingleSymbolizer(rule, TextSymbolizer2.class);
        assertLiteral("test", ts.getLabel());
        Graphic g = ts.getGraphic();
        assertEquals(1, g.graphicalSymbols().size());
        Mark mark = (Mark) g.graphicalSymbols().get(0);
        assertLiteral("square", mark.getWellKnownName());
        assertLiteral("#000000", mark.getFill().getColor());
    }

    @Test
    public void labelPriority() throws Exception {
        String css = "* { label: 'test'; -gt-label-priority: [priority];}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer2 ts = assertSingleSymbolizer(rule, TextSymbolizer2.class);
        assertLiteral("test", ts.getLabel());
        assertExpression("priority", ts.getPriority());
    }

    @Test
    public void labelVendorOptions() throws Exception {
        String css = "* { label: 'test'; -gt-label-follow-line: true;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer2 ts = assertSingleSymbolizer(rule, TextSymbolizer2.class);
        assertLiteral("test", ts.getLabel());
        assertEquals("true", ts.getOptions().get("followLine"));
    }

    @Test
    public void labelMixedMode() throws Exception {
        String css = "* { label: [att1]'\n('[att2]')';}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer2 ts = assertSingleSymbolizer(rule, TextSymbolizer2.class);
        assertExpression("concatenate(att1, '\n(', att2, ')')", ts.getLabel());
    }

    @Test
    public void rasterBasic() throws Exception {
        String css = "* { raster-channels: auto;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        RasterSymbolizer rs = assertSingleSymbolizer(rule, RasterSymbolizer.class);
        assertNull(rs.getChannelSelection());
        assertNull(rs.getColorMap());
    }

    @Test
    public void rasterGammaContrastEnhancement() throws Exception {
        String css =
                "* { raster-channels: auto; raster-contrast-enhancement: normalize; raster-gamma: 0.5;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        RasterSymbolizer rs = assertSingleSymbolizer(rule, RasterSymbolizer.class);
        assertNull(rs.getChannelSelection());
        assertNull(rs.getColorMap());
        assertEquals(ContrastMethod.NORMALIZE, rs.getContrastEnhancement().getMethod());
        assertLiteral("0.5", rs.getContrastEnhancement().getGammaValue());
    }

    @Test
    public void rasterChannelSelection() throws Exception {
        String css =
                "* { raster-channels: 'band1'; raster-contrast-enhancement: normalize; raster-gamma: 0.5;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        RasterSymbolizer rs = assertSingleSymbolizer(rule, RasterSymbolizer.class);
        assertNull(rs.getColorMap());
        SelectedChannelType grayChannel = rs.getChannelSelection().getGrayChannel();
        assertNotNull(grayChannel);
        assertEquals(ContrastMethod.NORMALIZE, grayChannel.getContrastEnhancement().getMethod());
        assertLiteral("0.5", grayChannel.getContrastEnhancement().getGammaValue());
    }

    @Test
    public void rasterChannelSelectionRGB() throws Exception {
        String css = "* { raster-channels: 'band1' 'band5' 'band3';}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        RasterSymbolizer rs = assertSingleSymbolizer(rule, RasterSymbolizer.class);
        assertNull(rs.getColorMap());
        SelectedChannelType[] channels = rs.getChannelSelection().getRGBChannels();
        assertEquals("band1", channels[0].getChannelName().evaluate(null, String.class));
        assertEquals("band5", channels[1].getChannelName().evaluate(null, String.class));
        assertEquals("band3", channels[2].getChannelName().evaluate(null, String.class));
    }

    /**
     * Tests expression support in channel selection
     *
     * @throws Exception
     */
    @Test
    public void rasterChannelSelectionRGBExpression() throws Exception {
        String css = "* { raster-channels: [env('B1','1')] '2' '3'; }";
        rasterChannelSelectionExpression(css);
    }

    /**
     * Tests expression support in channel selection, abbreviated syntax
     *
     * @throws Exception
     */
    @Test
    public void rasterChannelSelectionRGBExpressionAbbr() throws Exception {
        String css = "* { raster-channels: @B1(1) '2' '3';}";
        rasterChannelSelectionExpression(css);
    }

    private void rasterChannelSelectionExpression(String css) throws Exception {
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        RasterSymbolizer rs = assertSingleSymbolizer(rule, RasterSymbolizer.class);
        assertNull(rs.getColorMap());
        SelectedChannelType[] channels = rs.getChannelSelection().getRGBChannels();
        // check default value
        EnvFunction.removeLocalValue("B1");
        assertEquals(1, channels[0].getChannelName().evaluate(null, Integer.class).intValue());
        // check env value
        EnvFunction.setLocalValue("B1", "20");
        assertEquals(20, channels[0].getChannelName().evaluate(null, Integer.class).intValue());
        EnvFunction.removeLocalValue("B1");

        assertEquals(2, channels[1].getChannelName().evaluate(null, Integer.class).intValue());
        assertEquals(3, channels[2].getChannelName().evaluate(null, Integer.class).intValue());
    }

    @Test
    public void rasterColorMap() throws Exception {
        String css =
                "* { raster-channels: 'auto'; raster-color-map: color-map-entry(black, 100) color-map-entry(white, 1000) color-map-entry(red, 10000, 0);}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        RasterSymbolizer rs = assertSingleSymbolizer(rule, RasterSymbolizer.class);
        ColorMap cm = rs.getColorMap();
        assertEquals(3, cm.getColorMapEntries().length);
        assertLiteral("#000000", cm.getColorMapEntry(0).getColor());
        assertLiteral("1.0", cm.getColorMapEntry(0).getOpacity());
        assertLiteral("100", cm.getColorMapEntry(0).getQuantity());
        assertLiteral("#ffffff", cm.getColorMapEntry(1).getColor());
        assertLiteral("1.0", cm.getColorMapEntry(1).getOpacity());
        assertLiteral("1000", cm.getColorMapEntry(1).getQuantity());
        assertLiteral("#ff0000", cm.getColorMapEntry(2).getColor());
        assertLiteral("0", cm.getColorMapEntry(2).getOpacity());
        assertLiteral("10000", cm.getColorMapEntry(2).getQuantity());
    }

    @Test
    public void multiComment() throws Exception {
        String css =
                "/* This is an initial comment */\n"
                        + //
                        "\n"
                        + //
                        "/* @title This is the title */\n"
                        + //
                        "* {\n"
                        + //
                        "    mark: symbol('circle');\n"
                        + //
                        "}\n"
                        + //
                        "\n"
                        + //
                        "/* This is a closing comment */" //
                        + "\n  ";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        assertSingleSymbolizer(rule, PointSymbolizer.class);
    }

    @Test
    public void scaleWithinOr() throws IOException, CQLException {
        String css = "[@scale < 10000],[foo='bar'] { fill: orange; }";
        // used to just blow
        Style style = translate(css);
        assertEquals(
                "Expected single feature type style, found " + style.featureTypeStyles().size(),
                1,
                style.featureTypeStyles().size());
        FeatureTypeStyle fts = style.featureTypeStyles().get(0);
        List<? extends Rule> rules = fts.rules();
        assertEquals(3, rules.size());
    }

    @Test
    public void testParseQuotedURL() throws Exception {
        String css = "* { mark: url('file://BidirectionShield-High.svg');}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PointSymbolizer ps = (PointSymbolizer) rule.symbolizers().get(0);
        ExternalGraphic eg = (ExternalGraphic) ps.getGraphic().graphicalSymbols().get(0);
        String uri = eg.getURI();
        assertEquals("file://BidirectionShield-High.svg", uri);
    }

    @Test
    public void testEmptyStyle() throws Exception {
        String css = "* { line: gray }";
        try {
            translate(css);
            fail("Generating an empty style, should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // fine
        }
    }

    @Test
    public void testEnvFunction() throws Exception {
        String css = "[env('foo', 'default') = 'bar'] { fill: blue; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        assertEquals(ECQL.toFilter("env('foo', 'default') = 'bar'"), rule.getFilter());
    }

    @Test
    public void testBlendPoint() throws Exception {
        String css = "* { mark: symbol(circle); mark-composite: multiply;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        PointSymbolizer ps = assertSingleSymbolizer(rule, PointSymbolizer.class);
        assertEquals(1, ps.getOptions().size());
        assertEquals("multiply", ps.getOptions().get("composite"));
    }

    @Test
    public void testBlendLine() throws Exception {
        String css = "* { stroke: red; stroke-composite: multiply;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        assertEquals(1, ls.getOptions().size());
        assertEquals("multiply", ls.getOptions().get("composite"));
    }

    @Test
    public void testBlendPolygon() throws Exception {
        String css = "* { fill: red; fill-composite: multiply;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        assertEquals(1, ps.getOptions().size());
        assertEquals("multiply", ps.getOptions().get("composite"));
    }

    @Test
    public void testBlendRaster() throws Exception {
        String css = "* { raster-channels: auto; raster-composite : multiply; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        RasterSymbolizer rs = assertSingleSymbolizer(rule, RasterSymbolizer.class);
        assertEquals(1, rs.getOptions().size());
        assertEquals("multiply", rs.getOptions().get("composite"));
    }

    @Test
    public void testBlendFTS() throws Exception {
        String css = "* { stroke: red; composite : multiply; }";
        Style style = translate(css);
        // should not be in the symbolizer this time
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        assertEquals(0, ls.getOptions().size());
        // but in the feature type style
        org.geotools.styling.FeatureTypeStyle fts =
                (org.geotools.styling.FeatureTypeStyle) style.featureTypeStyles().get(0);
        assertEquals(2, fts.getOptions().size());
        assertEquals("multiply", fts.getOptions().get("composite"));
        assertNull(fts.getOptions().get("composite-base"));
    }

    @Test
    public void testCompositeBaseFTS() throws Exception {
        String css = "* { stroke: red; composite-base : true; }";
        Style style = translate(css);
        // should not be in the symbolizer this time
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        assertEquals(0, ls.getOptions().size());
        // but in the feature type style
        org.geotools.styling.FeatureTypeStyle fts =
                (org.geotools.styling.FeatureTypeStyle) style.featureTypeStyles().get(0);
        assertEquals("true", fts.getOptions().get("composite-base"));
        assertNull(fts.getOptions().get("composite"));
    }

    @Test
    public void testCompositeAndBaseFTS() throws Exception {
        String css = "* { stroke: red; composite: multiply; composite-base : true; }";
        Style style = translate(css);
        // should not be in the symbolizer this time
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        assertEquals(0, ls.getOptions().size());
        // but in the feature type style
        org.geotools.styling.FeatureTypeStyle fts =
                (org.geotools.styling.FeatureTypeStyle) style.featureTypeStyles().get(0);
        assertEquals("true", fts.getOptions().get("composite-base"));
        assertEquals("multiply", fts.getOptions().get("composite"));
    }

    @Test
    public void testSortBy() throws Exception {
        String css = "* { stroke: red; sort-by: \"cat A, name D\"; }";
        Style style = translate(css);
        // should not be in the symbolizer this time
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        assertEquals(0, ls.getOptions().size());
        // but in the feature type style
        org.geotools.styling.FeatureTypeStyle fts =
                (org.geotools.styling.FeatureTypeStyle) style.featureTypeStyles().get(0);
        assertEquals(
                "cat A, name D",
                fts.getOptions().get(org.geotools.styling.FeatureTypeStyle.SORT_BY));
    }

    @Test
    public void testSortByGroup() throws Exception {
        String css = "* { stroke: red; sort-by: \"cat A, name D\"; sort-by-group: \"theGroup\"}";
        Style style = translate(css);
        // should not be in the symbolizer this time
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        assertEquals(0, ls.getOptions().size());
        // but in the feature type style
        org.geotools.styling.FeatureTypeStyle fts =
                (org.geotools.styling.FeatureTypeStyle) style.featureTypeStyles().get(0);
        assertEquals(
                "cat A, name D",
                fts.getOptions().get((org.geotools.styling.FeatureTypeStyle.SORT_BY)));
        assertEquals(
                "theGroup",
                fts.getOptions().get(org.geotools.styling.FeatureTypeStyle.SORT_BY_GROUP));
    }

    @Test
    public void testMultipleFonts() throws Exception {
        String css = "* { label: \"static\"; font-family: \"Serif\" \"Sans\"; font-size: 10 15; }";
        Style style = translate(css);
        // should not be in the symbolizer this time
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        List<Font> fonts = ts.fonts();
        assertEquals(2, fonts.size());
        Font f1 = fonts.get(0);
        assertEquals("Serif", f1.getFamily().get(0).evaluate(null));
        assertEquals("10", f1.getSize().evaluate(null));
        Font f2 = fonts.get(1);
        assertEquals("Sans", f2.getFamily().get(0).evaluate(null));
        assertEquals("15", f2.getSize().evaluate(null));
    }

    @Test
    public void testMultipleFontsDefaltSize() throws Exception {
        String css = "* { label: \"static\"; font-family: \"Serif\" \"Sans\"}";
        Style style = translate(css);
        // should not be in the symbolizer this time
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        List<Font> fonts = ts.fonts();
        assertEquals(2, fonts.size());
        Font f1 = fonts.get(0);
        assertEquals("Serif", f1.getFamily().get(0).evaluate(null));
        assertEquals(10, f1.getSize().evaluate(null));
        Font f2 = fonts.get(1);
        assertEquals("Sans", f2.getFamily().get(0).evaluate(null));
        assertEquals(10, f2.getSize().evaluate(null));
    }

    @Test
    public void testMultipleFontsSingleSize() throws Exception {
        String css = "* { label: \"static\"; font-family: \"Serif\" \"Sans\"; font-size: 10 }";
        Style style = translate(css);
        // should not be in the symbolizer this time
        Rule rule = assertSingleRule(style);
        assertEquals(Filter.INCLUDE, rule.getFilter());
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        List<Font> fonts = ts.fonts();
        assertEquals(2, fonts.size());
        Font f1 = fonts.get(0);
        assertEquals("Serif", f1.getFamily().get(0).evaluate(null));
        assertEquals("10", f1.getSize().evaluate(null));
        Font f2 = fonts.get(1);
        assertEquals("Sans", f2.getFamily().get(0).evaluate(null));
        assertEquals("10", f2.getSize().evaluate(null));
    }

    @Test
    public void perpendicularOffset() throws Exception {
        String css = "* { stroke: black, yellow; stroke-offset: 0, 5;} ";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        assertEquals(2, rule.symbolizers().size());
        LineSymbolizer ls1 = (LineSymbolizer) rule.symbolizers().get(0);
        assertNull(ls1.getPerpendicularOffset());
        LineSymbolizer ls2 = (LineSymbolizer) rule.symbolizers().get(1);
        assertNotNull(ls2.getPerpendicularOffset());
        assertEquals(5, ls2.getPerpendicularOffset().evaluate(null, Double.class), 0);
    }

    @Test
    public void styleTitle() throws Exception {
        String css = "@styleTitle \"test title\";\n" + "* { mark: symbol('circle'); }";
        Style style = translate(css);
        assertEquals("test title", style.getDescription().getTitle().toString());
    }

    @Test
    public void styleAbstract() throws Exception {
        String css = "@styleAbstract \"style description\";\n" + "* { mark: symbol('circle'); }";
        Style style = translate(css);
        assertEquals("style description", style.getDescription().getAbstract().toString());
    }

    @Test
    public void testModeFlat() throws CQLException, TransformerException {
        String css =
                "@mode \"Flat\"; " + "[value1=1] { fill: green; } " + "[value2=2] { stroke: red; }";
        Style style = translate(css);
        assertEquals(1, style.featureTypeStyles().size());
        assertEquals(2, style.featureTypeStyles().get(0).rules().size());
        assertEquals(
                ECQL.toFilter("value1=1"),
                style.featureTypeStyles().get(0).rules().get(0).getFilter());
        assertEquals(
                ECQL.toFilter("value2=2"),
                style.featureTypeStyles().get(0).rules().get(1).getFilter());

        assertEquals(
                "#008000",
                ((PolygonSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getFill()
                        .getColor()
                        .toString());
        assertEquals(1, style.featureTypeStyles().get(0).rules().get(0).symbolizers().size());
        assertNull(
                ((PolygonSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getStroke());

        assertEquals(1, style.featureTypeStyles().get(0).rules().get(1).symbolizers().size());
        assertEquals(
                "#ff0000",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(1)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getColor()
                        .toString());
    }

    @Test
    public void testModeFlat_include() throws CQLException, TransformerException {
        String css =
                "@mode \"Flat\"; "
                        + "* { fill: blue; } "
                        + "[value1=1] { fill: green; } "
                        + "[value2=2] { stroke: red; }";
        Style style = translate(css);
        assertEquals(1, style.featureTypeStyles().size());
        assertEquals(3, style.featureTypeStyles().get(0).rules().size());
        assertEquals(
                ECQL.toFilter("include"),
                style.featureTypeStyles().get(0).rules().get(0).getFilter());
        assertEquals(
                ECQL.toFilter("value1=1"),
                style.featureTypeStyles().get(0).rules().get(1).getFilter());
        assertEquals(
                ECQL.toFilter("value2=2"),
                style.featureTypeStyles().get(0).rules().get(2).getFilter());
    }

    @Test
    public void testModeFlat_pseudoRules() throws Exception {
        String css = "@mode \"Flat\"; " + "* { fill: symbol('circle');} :fill { stroke: black;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ps.getFill();
        Graphic gf = fill.getGraphicFill();
        assertEquals(1, gf.graphicalSymbols().size());
        Mark mark = (Mark) gf.graphicalSymbols().get(0);
        assertLiteral("circle", mark.getWellKnownName());
        assertNull(mark.getFill());
        assertLiteral("#000000", mark.getStroke().getColor());
        assertNull(gf.getSize());
        assertLiteral("1.0", gf.getOpacity());
        assertLiteral("0", gf.getRotation());
    }

    @Test
    public void testModeFlat1() throws CQLException, TransformerException {
        String css =
                "@mode \"Flat\"; "
                        + "[value1=1] { stroke: green; stroke-width:2px;}"
                        + "[value2=2] { stroke: green; stroke-width:2px;}"
                        + "[value1=1] { stroke: blue; stroke-width:10px;}";
        Style style = translate(css);
        assertEquals(3, style.featureTypeStyles().get(0).rules().size());
        assertEquals(
                ECQL.toFilter("value1=1"),
                style.featureTypeStyles().get(0).rules().get(0).getFilter());
        assertEquals(
                ECQL.toFilter("value2=2"),
                style.featureTypeStyles().get(0).rules().get(1).getFilter());
        assertEquals(
                ECQL.toFilter("value1=1"),
                style.featureTypeStyles().get(0).rules().get(2).getFilter());
        assertEquals(
                "2",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getWidth()
                        .toString());
        assertEquals(
                "#008000",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getColor()
                        .toString());
        assertEquals(
                "2",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(1)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getWidth()
                        .toString());
        assertEquals(
                "#008000",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(1)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getColor()
                        .toString());
        assertEquals(
                "10",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(2)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getWidth()
                        .toString());
        assertEquals(
                "#0000ff",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(2)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getColor()
                        .toString());
        // printStyle(style);
    }

    private void printStyle(Style style) throws TransformerException {
        SLDTransformer transformer = new SLDTransformer();
        String xml = transformer.transform(style);
        LOGGER.info(xml);
    }

    @Test
    public void testModeFlat1_1() throws CQLException, TransformerException {
        String css =
                "@mode \"Flat\"; "
                        + "[value1=1] { stroke: green; stroke-width:2px;z-index:1;}"
                        + "[value2=2] { stroke: green; stroke-width:2px;z-index:2;}"
                        + "[value1=1] { stroke: blue; stroke-width:10px;z-index:3;}";
        Style style = translate(css);
        assertEquals(1, style.featureTypeStyles().get(0).rules().size());
        assertEquals(
                ECQL.toFilter("value1=1"),
                style.featureTypeStyles().get(0).rules().get(0).getFilter());
        assertEquals(
                ECQL.toFilter("value2=2"),
                style.featureTypeStyles().get(1).rules().get(0).getFilter());
        assertEquals(
                ECQL.toFilter("value1=1"),
                style.featureTypeStyles().get(2).rules().get(0).getFilter());
        assertEquals(
                "2",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getWidth()
                        .toString());
        assertEquals(
                "#008000",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(0)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getColor()
                        .toString());
        assertEquals(
                "2",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(1)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getWidth()
                        .toString());
        assertEquals(
                "#008000",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(1)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getColor()
                        .toString());
        assertEquals(
                "10",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(2)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getWidth()
                        .toString());
        assertEquals(
                "#0000ff",
                ((LineSymbolizer)
                                style.featureTypeStyles()
                                        .get(2)
                                        .rules()
                                        .get(0)
                                        .symbolizers()
                                        .get(0))
                        .getStroke()
                        .getColor()
                        .toString());
        // printStyle(style);
    }

    @Test
    public void testModeFlat2_mark() throws Exception {
        String css =
                "@mode \"Flat\"; "
                        + "[value1=1] { mark: symbol(circle); } [value1=1] :mark { fill: green; } [value1=1] [value2=2] :mark { fill: blue; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PointSymbolizer ps = assertSingleSymbolizer(rule, PointSymbolizer.class);
        Graphic g = ps.getGraphic();
        Mark mark = (Mark) g.graphicalSymbols().get(0);
        assertLiteral("circle", mark.getWellKnownName());
        assertLiteral("#008000", mark.getFill().getColor());
    }

    @Test
    public void testModeFlat3_mark() throws CQLException, TransformerException {
        String css =
                "@mode \"Flat\"; "
                        + "* { fill: blue; } "
                        + "[value1=1] { fill: green; } "
                        + "[value2=2] { stroke: red; } "
                        + "[value3=3] { mark: symbol(circle); mark-size: 10; mark-rotation: 45; mark-geometry: [centroid(the_geom)];} [value3=3] :mark { fill: blue; }";
        Style style = translate(css);
        assertEquals(1, style.featureTypeStyles().size());
        assertEquals(4, style.featureTypeStyles().get(0).rules().size());
        assertEquals(
                ECQL.toFilter("include"),
                style.featureTypeStyles().get(0).rules().get(0).getFilter());
        assertEquals(
                ECQL.toFilter("value1=1"),
                style.featureTypeStyles().get(0).rules().get(1).getFilter());
        assertEquals(
                ECQL.toFilter("value2=2"),
                style.featureTypeStyles().get(0).rules().get(2).getFilter());
        assertEquals(
                ECQL.toFilter("value3=3"),
                style.featureTypeStyles().get(0).rules().get(3).getFilter());

        PointSymbolizer ps =
                assertSingleSymbolizer(
                        style.featureTypeStyles().get(0).rules().get(3), PointSymbolizer.class);
        assertExpression("centroid(the_geom)", ps.getGeometry());
        Graphic g = ps.getGraphic();
        assertLiteral("10", g.getSize());
        assertLiteral("45", g.getRotation());
        assertNotNull(g);
        assertEquals(1, g.graphicalSymbols().size());
        Mark mark = (Mark) g.graphicalSymbols().get(0);
        assertLiteral("circle", mark.getWellKnownName());
        assertLiteral("#0000ff", mark.getFill().getColor());

        // printStyle(style);
    }

    @Test
    public void testModeFlat4_multiValued() throws CQLException, TransformerException {
        String css = "@mode \"Flat\"; " + "[value1=1] { fill: green, red; }";
        Style style = translate(css);
        assertEquals(1, style.featureTypeStyles().size());
        assertEquals(1, style.featureTypeStyles().get(0).rules().size());
        assertEquals(
                ECQL.toFilter("value1=1"),
                style.featureTypeStyles().get(0).rules().get(0).getFilter());
        assertEquals(2, style.featureTypeStyles().get(0).rules().get(0).symbolizers().size());
        // printStyle(style);
    }

    @Test
    public void testModeFlat5_mark() throws Exception {
        String css =
                "@mode \"Flat\"; "
                        + "* { mark: symbol(circle); mark-size: 10; mark-rotation: 45; mark-geometry: [centroid(the_geom)];} :mark { fill: blue; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PointSymbolizer ps = assertSingleSymbolizer(rule, PointSymbolizer.class);
        assertExpression("centroid(the_geom)", ps.getGeometry());
        Graphic g = ps.getGraphic();
        assertLiteral("10", g.getSize());
        assertLiteral("45", g.getRotation());
        assertNotNull(g);
        assertEquals(1, g.graphicalSymbols().size());
        Mark mark = (Mark) g.graphicalSymbols().get(0);
        assertLiteral("circle", mark.getWellKnownName());
        assertLiteral("#0000ff", mark.getFill().getColor());
    }

    @Test
    public void testSimpleTransform() throws Exception {
        String css = "* { transform: ras:Contour(levels: 1100 1200 1300); stroke: black}";
        Style style = translate(css);

        // check transformation
        FeatureTypeStyle fts = assertSingleFeatureTypeStyle(style);
        Function tx = assertTransformation(fts);
        assertContour123(tx);
    }

    @Test
    public void testTwoLevelTransform() throws Exception {
        String css =
                "* { transform: ras:Contour(levels: 1100 1200 1300); stroke: black; z-index: 0}\n"
                        + "* { transform: ras:RasterAsPointCollection(); mark: symbol('square'); z-index: 1}";
        Style style = translate(css);
        assertEquals(2, style.featureTypeStyles().size());

        // base level, contour
        FeatureTypeStyle fts1 = style.featureTypeStyles().get(0);
        Function tx1 = assertTransformation(fts1);
        assertContour123(tx1);

        // second level, raster as point collection
        FeatureTypeStyle fts2 = style.featureTypeStyles().get(1);
        Function tx2 = assertTransformation(fts2);
        assertEquals("ras:RasterAsPointCollection", tx2.getName());
        List<Expression> expressions = tx2.getParameters();
        assertEquals(1, expressions.size());
        assertParameterFunction(expressions.get(0), "data", 0);
    }

    @Test
    public void testPerpendicularOffsetExpression() throws Exception {
        String css = "* { stroke: red; stroke-offset: [a * 2]; }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        assertEquals(ECQL.toExpression("a * 2"), ls.getPerpendicularOffset());
    }

    @Test
    public void testDarkenAsCQL() throws Exception {
        String css = "* { fill: [darken('#FF0000', '30%')]}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        final Expression color = ps.getFill().getColor();
        assertThat(color, instanceOf(DarkenFunction.class));
        assertEquals(Color.decode("#660000"), color.evaluate(null, Color.class));
    }

    @Test
    public void testDarkenAsFunction() throws Exception {
        String css = "* { fill: darken(red, 30%);}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        PolygonSymbolizer ps = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        final Expression color = ps.getFill().getColor();
        assertThat(color, instanceOf(DarkenFunction.class));
        assertEquals(Color.decode("#660000"), color.evaluate(null, Color.class));
    }

    @Test
    public void testNestedFunction() throws Exception {
        String css = "* {stroke: saturate(darken(#b5d0d0, 40%), 30%)}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ps = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Expression color = ps.getStroke().getColor();
        assertThat(color, instanceOf(SaturateFunction.class));
        Function saturate = (Function) color;
        assertThat(saturate.getParameters().get(0), instanceOf(DarkenFunction.class));
    }

    @Test
    public void testScaleDependencyUnits() throws Exception {
        // k
        assertScaleMinMax("[@scale < 1k] {stroke: black}", null, 1e3);
        assertScaleMinMax("[@scale > 1k] {stroke: black}", 1e3, null);
        // m
        assertScaleMinMax("[@scale < 1M] {stroke: black}", null, 1e6);
        assertScaleMinMax("[@scale > 1M] {stroke: black}", 1e6, null);
        // g
        assertScaleMinMax("[@scale < 1G] {stroke: black}", null, 1e9);
        assertScaleMinMax("[@scale > 1G] {stroke: black}", 1e9, null);
    }

    @Test
    public void testScaleDenominatorPseudoVariable() throws Exception {
        assertScaleMinMax("[@sd < 1k] {stroke: black}", null, 1e3);
        assertScaleMinMax("[@sd > 1k] {stroke: black}", 1e3, null);
    }

    @Test
    public void testCQLErrorSelector() throws Exception {
        String css = "[thisFunctionDoesNotExists() > 10] {\nstroke: blue\n}";
        try {
            translate(css);
        } catch (ParserRuntimeException e) {
            // System.out.println(e);
            assertThat(
                    e.getMessage(),
                    both(containsString("thisFunctionDoesNotExists"))
                            .and(containsString("line 1")));
        }
    }

    @Test
    public void testCQLErrorProperty() throws Exception {
        String css = "* \n{stroke: blue; \nstroke-width: [thisFunctionDoesNotExists()]}";
        try {
            translate(css);
        } catch (ParserRuntimeException e) {
            // System.out.println(e);
            assertThat(
                    e.getMessage(),
                    both(containsString("thisFunctionDoesNotExists"))
                            .and(containsString("line 3")));
        }
    }

    @Test
    public void testDashArraySingleExpression() throws CQLException {
        String css = "* { stroke: orange; stroke-dasharray: [foo]}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        Stroke stroke = ls.getStroke();
        assertEquals(1, stroke.dashArray().size());
        assertExpression("foo", stroke.dashArray().get(0));
    }

    @Test
    public void testDashArrayMixedExpression() throws CQLException {
        String css = "* { stroke: orange; stroke-dasharray: [foo] 5 [bar]}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        Stroke stroke = ls.getStroke();
        assertEquals(3, stroke.dashArray().size());
        assertExpression("foo", stroke.dashArray().get(0));
        assertLiteral("5", stroke.dashArray().get(1));
        assertExpression("bar", stroke.dashArray().get(2));
    }

    @Test
    public void testVariableExpansion() throws CQLException {
        String css = "* { stroke: @color}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Expression color = ls.getStroke().getColor();
        assertExpression("env('color')", color);
    }

    @Test
    public void testVariableExpansionWithDefaultColor() throws CQLException {
        String css = "* { stroke: @color(black)}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Expression color = ls.getStroke().getColor();
        assertExpression("env('color', '#000000')", color);
    }

    @Test
    public void testVariableExpansionWithDefaultString() throws CQLException {
        String css = "* { stroke: @color('black')}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Expression color = ls.getStroke().getColor();
        assertExpression("env('color', 'black')", color);
    }

    @Test
    public void testVariableExpansionInCql() throws CQLException {
        String css = "* { stroke: black; stroke-width: [10 + @thick]}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Stroke stroke = ls.getStroke();
        final Expression color = stroke.getColor();
        assertExpression("'#000000'", color);
        Expression width = stroke.getWidth();
        assertExpression("10 + env('thick')", width);
    }

    @Test
    public void testVariableExpansionWithDefaultInCql() throws CQLException {
        String css = "* { stroke: black; stroke-width: [10 + @thick(0)]}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Stroke stroke = ls.getStroke();
        final Expression color = stroke.getColor();
        assertExpression("'#000000'", color);
        Expression width = stroke.getWidth();
        assertExpression("10 + env('thick', '0')", width);
    }

    @Test
    public void testVariableExpansionInSelector() throws CQLException {
        String css = "[variable > @limit] { stroke: black}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        Filter filter = rule.getFilter();
        assertFilter("variable > env('limit')", filter);

        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Stroke stroke = ls.getStroke();
        assertExpression("'#000000'", stroke.getColor());
    }

    @Test
    public void testVariableExpansionWithDefaultInSelector() throws CQLException {
        String css = "[variable > @limit(10)] { stroke: black}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        Filter filter = rule.getFilter();
        assertFilter("variable > env('limit', '10')", filter);

        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Stroke stroke = ls.getStroke();
        assertExpression("'#000000'", stroke.getColor());
    }

    @Test
    public void testNotVariablePropertyValue() throws CQLException {
        String css = "* { label: 'not@env'}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        assertExpression("'not@env'", ts.getLabel());
    }

    @Test
    public void testNotVariableCql() throws CQLException {
        String css = "* { label: ['not@env']}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        TextSymbolizer ts = assertSingleSymbolizer(rule, TextSymbolizer.class);
        assertExpression("'not@env'", ts.getLabel());
    }

    @Test
    public void testExpandWmsScaleDenominator() throws CQLException {
        String css = "* { stroke: categorize(@sd, blue, 10000, lime)}";
        assertCategorizeScaleDenominator(css);
    }

    private void assertCategorizeScaleDenominator(String css) throws CQLException {
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        Expression color = ls.getStroke().getColor();
        assertThat(color, instanceOf(Function.class));
        Function f = (Function) color;
        assertEquals("Categorize", f.getName());
        assertExpression("env('wms_scale_denominator')", f.getParameters().get(0));
        assertExpression("'#0000ff'", f.getParameters().get(1));
        assertExpression("10000", f.getParameters().get(2));
        assertExpression("'#00ff00'", f.getParameters().get(3));
    }

    @Test
    public void testExpandWmsScaleDenominatorIsoSuffix() throws CQLException {
        String css = "* { stroke: categorize(@sd, blue, 10k, lime)}";
        assertCategorizeScaleDenominator(css);
    }

    @Test
    public void testExpandWmsScaleDenominatorIsoSuffixInCql() throws CQLException {
        String css = "* { stroke: [categorize(@sd, '#0000ff', 10k, '#00ff00')]}";
        assertCategorizeScaleDenominator(css);
    }

    @Test
    public void testMeasuredExpression() throws CQLException {
        String css = "* { stroke: black; stroke-width: [a * 10]m;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        Filter filter = rule.getFilter();
        assertEquals(Filter.INCLUDE, filter);

        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Stroke stroke = ls.getStroke();
        assertExpression("'#000000'", stroke.getColor());
        assertExpression("Concatenate(a * 10, 'm')", stroke.getWidth());
    }

    private void assertScaleMinMax(String css, Double min, Double max) {
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        if (min == null) {
            assertEquals(0, rule.getMinScaleDenominator(), 0d);
        } else {
            assertEquals(min, rule.getMinScaleDenominator(), 0d);
        }
        if (max == null) {
            assertEquals(Double.POSITIVE_INFINITY, rule.getMaxScaleDenominator(), 0d);
        } else {
            assertEquals(max, rule.getMaxScaleDenominator(), 0d);
        }
    }

    private Function assertTransformation(FeatureTypeStyle fts) {
        Expression ex = fts.getTransformation();
        assertNotNull(ex);
        assertThat(ex, instanceOf(Function.class));
        Function tx = (Function) ex;
        return tx;
    }

    private void assertContour123(Function tx) {
        assertEquals("ras:Contour", tx.getName());
        List<Expression> expressions = tx.getParameters();
        assertEquals(2, expressions.size());
        assertParameterFunction(expressions.get(0), "data", 0);
        Function p2 = assertParameterFunction(expressions.get(1), "levels", 3);
        List<Expression> p2Params = p2.getParameters();
        assertEquals("1100", p2Params.get(1).evaluate(null));
        assertEquals("1200", p2Params.get(2).evaluate(null));
        assertEquals("1300", p2Params.get(3).evaluate(null));
    }

    private Function assertParameterFunction(
            Expression expression, String expectedKey, int expectedValueCount) {
        assertThat(expression, instanceOf(Function.class));
        Function f = (Function) expression;
        assertEquals("parameter", f.getName());
        final List<Expression> parameters = f.getParameters();
        assertTrue("At least one parameter, the key", parameters.size() > 0);
        assertEquals(expectedKey, parameters.get(0).evaluate(null));
        assertEquals(expectedValueCount, parameters.size() - 1);
        return f;
    }

    @Test
    public void testNone() {
        String css = "* { fill: none }";
        try {
            Style style = translate(css);
            fail("Translation should have failed");
        } catch (IllegalArgumentException e) {
            assertThat(
                    e.getMessage(),
                    CoreMatchers.startsWith(
                            "Invalid CSS style, no rule seems to activate any symbolization"));
        }
    }

    @Test
    public void testNoneFillStroke() throws CQLException {
        String css = "* { fill: red; stroke: none }";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        Filter filter = rule.getFilter();
        assertEquals(Filter.INCLUDE, filter);

        PolygonSymbolizer ls = assertSingleSymbolizer(rule, PolygonSymbolizer.class);
        Fill fill = ls.getFill();
        assertExpression("'#ff0000'", fill.getColor());
        final Stroke stroke = ls.getStroke();
        assertNull(stroke);
    }

    @Test
    public void testNoneOverrideAndDisable() throws CQLException {
        String css = "* { stroke: red; [@sd < 10k] { stroke: none }}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        Filter filter = rule.getFilter();
        assertEquals(Filter.INCLUDE, filter);
        assertEquals(10000, rule.getMinScaleDenominator(), 0d);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        final Stroke stroke = ls.getStroke();
        assertExpression("'#ff0000'", stroke.getColor());
    }

    @Test
    public void testAlternateNone() throws CQLException {
        String css =
                "* {"
                        + "    fill-geometry: [the_geom], [boundary(the_geom)];"
                        + "    fill: #E8F3E2, none;"
                        + "    stroke: none, #3EA250;"
                        + "}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        assertEquals(2, rule.symbolizers().size());
        PolygonSymbolizer ps = (PolygonSymbolizer) rule.symbolizers().get(0);
        assertExpression("'#E8F3E2'", ps.getFill().getColor());
        assertNull(ps.getStroke());
        LineSymbolizer ls = (LineSymbolizer) rule.symbolizers().get(1);
        assertExpression("'#3EA250'", ls.getStroke().getColor());
    }
}
