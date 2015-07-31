/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import org.geotools.styling.Normalize;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Stroke;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.style.ContrastMethod;
import org.opengis.style.Displacement;
import org.opengis.style.FeatureTypeStyle;
import org.opengis.style.GraphicFill;
import org.opengis.style.Mark;
import org.opengis.style.Rule;
import org.opengis.style.Style;
import org.opengis.style.Symbolizer;

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

    private void assertVendorOption(String expectedValue, String name,
            org.geotools.styling.Symbolizer ps) {
        assertEquals(expectedValue, ps.getOptions().get(name));
    }

	private Rule assertSingleRule(Style style) {
		assertEquals("Expected single feature type style, found " + style.featureTypeStyles().size(), 1, style.featureTypeStyles().size());
		FeatureTypeStyle fts = style.featureTypeStyles().get(0);
		assertEquals("Expected 1 rule, found " + fts.rules().size(), 1, fts.rules().size());
		return fts.rules().get(0);
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
        String css = "* { fill: symbol('circle');} :fill { fill: yellow; stroke: black; stroke-width: 3;}";
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
        String css = "* { stroke: orange; stroke-width: 10; stroke-dasharray: 10 5 1 5; stroke-dashoffset: 2; stroke-linecap: round; stroke-linejoin: round;}";
        Style style = translate(css);
        Rule rule = assertSingleRule(style);
        LineSymbolizer ls = assertSingleSymbolizer(rule, LineSymbolizer.class);
        Stroke stroke = ls.getStroke();
        assertLiteral("#ffa500", stroke.getColor());
        assertLiteral("10", stroke.getWidth());
        assertLiteral("1", stroke.getOpacity());
        assertLiteral("round", stroke.getLineCap());
        assertLiteral("round", stroke.getLineJoin());
        assertTrue(Arrays.equals(new float[] { 10, 5, 1, 5 }, stroke.getDashArray()));
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
        String css = "* { stroke: symbol('square'); stroke-size: 20; stroke-repeat: stipple;} :stroke { fill: red; }";
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
        String css = "* { mark: symbol(circle); mark-size: 10; mark-rotation: 45; mark-geometry: [centroid(the_geom)];} :mark { fill: blue; }";
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
        String css = "* { mark: symbol(circle); mark-size: 10; mark-anchor: 0 1; mark-offset: 10 20;}";
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
        String css = "* { mark: url(test.svg); mark-size: 10; mark-rotation: 45; mark-mime: 'image/png';}";
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
        String css = "* { label: 'test'; label-offset: 5 5; label-rotation: 45; label-anchor: 0.1 0.9;}";
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
        String css = "* { label: 'test'; font-family: 'Arial'; font-fill: blue; font-weight: normal; font-style: italic; font-size: 20;}";
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
        String css = "* { raster-channels: auto; raster-contrast-enhancement: normalize; raster-gamma: 0.5;}";
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
        String css = "* { raster-channels: 'band1'; raster-contrast-enhancement: normalize; raster-gamma: 0.5;}";
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
        assertEquals("band1", channels[0].getChannelName());
        assertEquals("band5", channels[1].getChannelName());
        assertEquals("band3", channels[2].getChannelName());
    }

    @Test
    public void rasterColorMap() throws Exception {
        String css = "* { raster-channels: 'auto'; raster-color-map: color-map-entry(black, 100) color-map-entry(white, 1000) color-map-entry(red, 10000, 0);}";
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
        String css = "/* This is an initial comment */\n" + //
                "\n" + //
                "/* @title This is the title */\n" + //
                "* {\n" + //
                "    mark: symbol('circle');\n" + //
                "}\n" + //
                "\n" + //
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
        assertEquals("Expected single feature type style, found "
                + style.featureTypeStyles().size(), 1, style.featureTypeStyles().size());
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
        org.geotools.styling.FeatureTypeStyle fts = (org.geotools.styling.FeatureTypeStyle) style
                .featureTypeStyles().get(0);
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
        org.geotools.styling.FeatureTypeStyle fts = (org.geotools.styling.FeatureTypeStyle) style
                .featureTypeStyles().get(0);
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
        org.geotools.styling.FeatureTypeStyle fts = (org.geotools.styling.FeatureTypeStyle) style
                .featureTypeStyles().get(0);
        assertEquals("true", fts.getOptions().get("composite-base"));
        assertEquals("multiply", fts.getOptions().get("composite"));

    }


    
    

}
