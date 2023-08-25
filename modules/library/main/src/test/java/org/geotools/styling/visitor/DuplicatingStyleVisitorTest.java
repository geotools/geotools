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
package org.geotools.styling.visitor;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.Icon;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.style.*;
import org.geotools.api.style.OverlapBehavior;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.styling.*;
import org.geotools.styling.ContrastEnhancementImpl;
import org.geotools.styling.DisplacementImpl;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.ExternalMarkImpl;
import org.geotools.styling.FillImpl;
import org.geotools.styling.FontImpl;
import org.geotools.styling.GraphicImpl;
import org.geotools.styling.HaloImpl;
import org.geotools.styling.LinePlacementImpl;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.OtherTextImpl;
import org.geotools.styling.PointPlacementImpl;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.RasterSymbolizerImpl;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.SelectedChannelTypeImpl;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleImpl;
import org.geotools.styling.TextSymbolizerImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for DuplicatorStyleVisitor.
 *
 * @author Cory Horner, Refractions Research Inc.
 */
public class DuplicatingStyleVisitorTest {
    StyleBuilder sb;
    StyleFactory sf;
    FilterFactory ff;
    DuplicatingStyleVisitor visitor;

    @Before
    public void setUp() throws Exception {
        sf = CommonFactoryFinder.getStyleFactory(null);
        ff = CommonFactoryFinder.getFilterFactory(null);
        sb = new StyleBuilder(sf, ff);
        visitor = new DuplicatingStyleVisitor(sf, ff);
    }

    @Test
    public void testRasterSymbolizerDuplication() {
        // create a default RasterSymbolizer
        RasterSymbolizerImpl original = sb.createRasterSymbolizer();

        // duplicate it
        original.accept(visitor);
        RasterSymbolizerImpl copy = (RasterSymbolizerImpl) visitor.getCopy();

        // compare it
        Assert.assertNotNull(copy);
        assertEquals(original, copy);
    }

    @Test
    public void testStyleDuplication() throws IllegalFilterException {
        // create a style
        StyleImpl oldStyle = sb.createStyle("FTSName", sf.createPolygonSymbolizer());
        oldStyle.featureTypeStyles()
                .get(0)
                .semanticTypeIdentifiers()
                .addAll(
                        Arrays.asList(
                                SemanticType.valueOf("simple"),
                                SemanticType.valueOf("generic:geometry")));
        // duplicate it
        oldStyle.accept(visitor);
        StyleImpl newStyle = (StyleImpl) visitor.getCopy();

        // compare it
        Assert.assertNotNull(newStyle);
        assertEquals(2, newStyle.featureTypeStyles().get(0).semanticTypeIdentifiers().size());
    }

    @Test
    public void testStyle() throws Exception {
        FeatureTypeStyleImpl fts = sf.createFeatureTypeStyle();
        fts.featureTypeNames().add(new NameImpl("feature-type-1"));

        FeatureTypeStyleImpl fts2 = fts2();

        StyleImpl style = sf.getDefaultStyle();
        style.featureTypeStyles().add(fts);
        style.featureTypeStyles().add(fts2);

        style.accept(visitor);
        StyleImpl copy = (StyleImpl) visitor.getCopy();

        // assertClone(style, clone);
        assertEqualsContract(style, copy);

        StyleImpl notEq = sf.getDefaultStyle();

        fts2 = fts2();
        notEq.featureTypeStyles().add(fts2);

        assertEqualsContract(copy, notEq, style);
    }

    private FeatureTypeStyleImpl fts2() {
        FeatureTypeStyleImpl fts2 = sf.createFeatureTypeStyle();
        RuleImpl rule = (RuleImpl) sf.createRule();
        fts2.rules().add(rule);
        fts2.featureTypeNames().add(new NameImpl("feature-type-2"));

        return fts2;
    }

    @Test
    public void testFeatureTypeStyle() throws Exception {
        FeatureTypeStyleImpl fts = sf.createFeatureTypeStyle();
        fts.featureTypeNames().add(new NameImpl("feature-type"));
        fts.getOptions().put("key", "value");

        RuleImpl rule1 = (RuleImpl) sf.createRule();
        rule1.setName("rule1");
        rule1.setFilter(ff.id(Collections.singleton(ff.featureId("FID"))));

        RuleImpl rule2 = (RuleImpl) sf.createRule();
        rule2.setElseFilter(true);
        rule2.setName("rule2");
        fts.rules().add(rule1);
        fts.rules().add(rule2);

        fts.accept(visitor);
        FeatureTypeStyleImpl clone = (FeatureTypeStyleImpl) visitor.getCopy();
        // assertClone(fts, clone);
        assertEqualsContract(fts, clone);

        rule1 = (RuleImpl) sf.createRule();
        rule1.setName("rule1");
        rule1.setFilter(ff.id(Collections.singleton(ff.featureId("FID"))));

        FeatureTypeStyleImpl notEq = sf.createFeatureTypeStyle();
        notEq.setName("fts-not-equal");
        notEq.rules().add(rule1);
        assertEqualsContract(clone, notEq, fts);

        fts.setTransformation(ff.literal("transformation"));
        fts.setOnlineResource(new OnLineResourceImpl());

        fts.accept(visitor);
        clone = (FeatureTypeStyleImpl) visitor.getCopy();
        assertEqualsContract(fts, clone);
    }

    @Test
    public void testRule() throws Exception {
        Symbolizer symb1 = sf.createLineSymbolizer(sf.getDefaultStroke(), "geometry");

        Symbolizer symb2 =
                sf.createPolygonSymbolizer(sf.getDefaultStroke(), sf.getDefaultFill(), "shape");

        RuleImpl rule = (RuleImpl) sf.createRule();
        rule.symbolizers().addAll(Arrays.asList(symb1, symb2));

        rule.accept(visitor);
        RuleImpl clone = (RuleImpl) visitor.getCopy();
        assertCopy(rule, clone);
        assertEqualsContract(rule, clone);

        symb2 = sf.createPolygonSymbolizer(sf.getDefaultStroke(), sf.getDefaultFill(), "shape");

        RuleImpl notEq = (RuleImpl) sf.createRule();
        notEq.symbolizers().add(symb2);
        assertEqualsContract(clone, notEq, rule);

        symb1 = sf.createLineSymbolizer(sf.getDefaultStroke(), "geometry");
        clone.symbolizers().add(symb1);
        Assert.assertNotEquals(rule, clone);
    }

    @Test
    public void testPointSymbolizer() throws Exception {
        URL urlExternal = getClass().getResource("/data/sld/blob.gif");
        ExternalGraphicImpl extg = sb.createExternalGraphic(urlExternal, "image/svg+xml");
        GraphicImpl graphic = sb.createGraphic(extg, null, null);
        PointSymbolizerImpl pointSymb = sb.createPointSymbolizer(graphic);

        pointSymb.accept(visitor);
        PointSymbolizerImpl clone = (PointSymbolizerImpl) visitor.getCopy();

        assertCopy(pointSymb, clone);
        assertEqualsContract(pointSymb, clone);

        PointSymbolizerImpl notEq = (PointSymbolizerImpl) sf.getDefaultPointSymbolizer();
        notEq.setGeometryPropertyName("something_else");
        assertEqualsContract(clone, notEq, pointSymb);
    }

    @Test
    public void testRasterSymbolizerWithUOM() throws Exception {
        RasterSymbolizerImpl rasterSymb = sf.createRasterSymbolizer();
        rasterSymb.setUnitOfMeasure(UomOgcMapping.FOOT.getUnit());
        rasterSymb.accept(visitor);
        RasterSymbolizerImpl clone = (RasterSymbolizerImpl) visitor.getCopy();

        assertEquals(clone.getUnitOfMeasure(), rasterSymb.getUnitOfMeasure());

        RasterSymbolizerImpl notEq = sf.createRasterSymbolizer();

        Assert.assertNotEquals(notEq.getUnitOfMeasure(), rasterSymb.getUnitOfMeasure());
    }

    @Test
    public void testRasterSymbolizerWithOverlapBehavior() throws Exception {
        RasterSymbolizerImpl rasterSymb1 = sf.createRasterSymbolizer();
        rasterSymb1.setOverlapBehavior(OverlapBehavior.AVERAGE);
        rasterSymb1.accept(visitor);
        RasterSymbolizerImpl clone1 = (RasterSymbolizerImpl) visitor.getCopy();
        assertEquals(clone1.getOverlapBehavior(), rasterSymb1.getOverlapBehavior());

        // Try literal expression
        RasterSymbolizerImpl rasterSymbLiteral = sf.createRasterSymbolizer();
        rasterSymbLiteral.setOverlap(ff.literal(OverlapBehavior.EARLIEST_ON_TOP.toString()));
        rasterSymbLiteral.accept(visitor);
        RasterSymbolizerImpl cloneLiteral = (RasterSymbolizerImpl) visitor.getCopy();

        assertEquals(cloneLiteral.getOverlapBehavior(), rasterSymbLiteral.getOverlapBehavior());
        assertEquals(cloneLiteral.getOverlap(), rasterSymbLiteral.getOverlap());

        // Try with invalid expression string
        RasterSymbolizerImpl rasterSymb3 = sf.createRasterSymbolizer();
        try {
            rasterSymb3.setOverlap(ff.literal("invalid string"));
        } catch (IllegalArgumentException e) {
            // Expected result
        } catch (Exception e) {
            Assert.fail();
        }

        // Compare rastersymbolizer overlap behaviour
        RasterSymbolizerImpl notEq = sf.createRasterSymbolizer();
        Assert.assertNotEquals(notEq.getOverlapBehavior(), rasterSymb1.getOverlapBehavior());
    }

    @Test
    public void testPointSymbolizerWithUOM() throws Exception {
        PointSymbolizerImpl pointSymb = sf.createPointSymbolizer();
        pointSymb.setUnitOfMeasure(UomOgcMapping.FOOT.getUnit());
        pointSymb.accept(visitor);
        PointSymbolizerImpl clone = (PointSymbolizerImpl) visitor.getCopy();

        assertCopy(pointSymb, clone);
        assertEqualsContract(pointSymb, clone);

        PointSymbolizerImpl notEq = (PointSymbolizerImpl) sf.getDefaultPointSymbolizer();
        assertEqualsContract(clone, notEq, pointSymb);
    }

    @Test
    public void testTextSymbolizer2() {
        TextSymbolizerImpl textSymb = (TextSymbolizerImpl) sf.createTextSymbolizer();
        Expression offset = ff.literal(10);
        textSymb.setLabelPlacement(sf.createLinePlacement(offset));

        textSymb.accept(visitor);
        TextSymbolizerImpl clone = (TextSymbolizerImpl) visitor.getCopy();
        assertCopy(textSymb, clone);
        assertEqualsContract(textSymb, clone);

        TextSymbolizerImpl notEq = sf.getDefaultTextSymbolizer();
        Expression ancX = ff.literal(10);
        Expression ancY = ff.literal(10);
        AnchorPointImpl ancPoint = sf.createAnchorPoint(ancX, ancY);
        LabelPlacement placement = sf.createPointPlacement(ancPoint, null, null);
        notEq.setLabelPlacement(placement);
        assertEqualsContract(clone, notEq, textSymb);
    }

    @Test
    public void testTextSymbolizerVendorParams() {
        TextSymbolizerImpl textSymb = (TextSymbolizerImpl) sf.createTextSymbolizer();
        textSymb.getOptions().put("autoWrap", "100");

        textSymb.accept(visitor);
        TextSymbolizerImpl clone = (TextSymbolizerImpl) visitor.getCopy();
        assertCopy(textSymb, clone);
        assertEqualsContract(textSymb, clone);

        assertEquals(1, clone.getOptions().size());
        assertEquals("100", clone.getOptions().get("autoWrap"));
    }

    @Test
    public void testTextSymbolizerVendorOptions() {
        TextSymbolizerImpl textSymb = (TextSymbolizerImpl) sf.createTextSymbolizer();
        textSymb.getOptions().put("autoWrap", "100");

        textSymb.accept(visitor);
        TextSymbolizerImpl clone = (TextSymbolizerImpl) visitor.getCopy();
        assertCopy(textSymb, clone);
        assertEqualsContract(textSymb, clone);
    }

    @Test
    public void testTextSymbolizerWithUOM() {
        TextSymbolizerImpl textSymb = (TextSymbolizerImpl) sf.createTextSymbolizer();
        textSymb.setUnitOfMeasure(UomOgcMapping.METRE.getUnit());
        Expression offset = ff.literal(10);
        textSymb.setLabelPlacement(sf.createLinePlacement(offset));

        textSymb.accept(visitor);
        TextSymbolizerImpl clone = (TextSymbolizerImpl) visitor.getCopy();
        assertCopy(textSymb, clone);
        assertEqualsContract(textSymb, clone);

        TextSymbolizerImpl notEq = sf.getDefaultTextSymbolizer();
        Expression ancX = ff.literal(10);
        Expression ancY = ff.literal(10);
        AnchorPointImpl ancPoint = sf.createAnchorPoint(ancX, ancY);
        LabelPlacement placement = sf.createPointPlacement(ancPoint, null, null);
        notEq.setLabelPlacement(placement);
        assertEqualsContract(clone, notEq, textSymb);
    }

    @Test
    public void testFont() {
        FontImpl font = sf.getDefaultFont();
        FontImpl clone = visitor.copy(font);
        assertCopy(font, clone);
        assertEqualsContract(font, clone);

        FontImpl other =
                sf.createFont(
                        ff.literal("other"),
                        ff.literal("normal"),
                        ff.literal("BOLD"),
                        ff.literal(12));

        assertEqualsContract(clone, other, font);
    }

    @Test
    public void testHalo() {
        HaloImpl halo = (HaloImpl) sf.createHalo(sf.getDefaultFill(), ff.literal(10));

        halo.accept(visitor);
        HaloImpl clone = (HaloImpl) visitor.getCopy();

        assertCopy(halo, clone);

        HaloImpl other = (HaloImpl) sf.createHalo(sf.getDefaultFill(), ff.literal(12));
        assertEqualsContract(clone, other, halo);
    }

    @Test
    public void testLinePlacement() throws Exception {
        LinePlacementImpl linePlacement = sf.createLinePlacement(ff.literal(12));

        linePlacement.accept(visitor);
        LinePlacementImpl clone = (LinePlacementImpl) visitor.getCopy();

        assertCopy(linePlacement, clone);

        LinePlacementImpl other = sf.createLinePlacement(ff.property("NAME"));
        assertEqualsContract(clone, other, linePlacement);
    }

    @Test
    public void testAnchorPoint() {
        AnchorPointImpl anchorPoint = sf.createAnchorPoint(ff.literal(1), ff.literal(2));
        anchorPoint.accept(visitor);

        AnchorPointImpl clone = (AnchorPointImpl) visitor.getCopy();
        assertCopy(anchorPoint, clone);

        AnchorPointImpl other = sf.createAnchorPoint(ff.literal(3), ff.literal(4));
        assertEqualsContract(clone, other, anchorPoint);
    }

    @Test
    public void testDisplacement() {
        DisplacementImpl displacement = sf.createDisplacement(ff.literal(1), ff.literal(2));

        displacement.accept(visitor);
        DisplacementImpl clone = (DisplacementImpl) visitor.getCopy();
        assertCopy(displacement, clone);

        DisplacementImpl other = sf.createDisplacement(ff.literal(3), ff.literal(4));
        assertEqualsContract(clone, other, displacement);
    }

    @Test
    public void testPointPlacement() {
        PointPlacementImpl pointPl = (PointPlacementImpl) sf.getDefaultPointPlacement();

        PointPlacementImpl clone = (PointPlacementImpl) visitor.copy(pointPl);
        assertCopy(pointPl, clone);

        PointPlacementImpl other = (PointPlacementImpl) ((Cloneable) pointPl).clone();
        other.setRotation(ff.literal(274.0));
        assertEqualsContract(clone, other, pointPl);
    }

    @Test
    public void testPolygonSymbolizer() {
        try {
            // visitor.setStrict(true);
            PolygonSymbolizerImpl polygonSymb = sf.createPolygonSymbolizer();
            PolygonSymbolizerImpl clone = (PolygonSymbolizerImpl) visitor.copy(polygonSymb);
            assertCopy(polygonSymb, clone);

            PolygonSymbolizerImpl notEq = (PolygonSymbolizerImpl) sf.getDefaultPolygonSymbolizer();
            notEq.setGeometryPropertyName("something_else");

            assertEqualsContract(clone, notEq, polygonSymb);
        } finally {
            visitor.setStrict(false);
        }
    }

    @Test
    public void testPolygonSymbolizerWithUOM() {
        try {
            // visitor.setStrict(true);
            PolygonSymbolizerImpl polygonSymb = sf.createPolygonSymbolizer();
            polygonSymb.setUnitOfMeasure(UomOgcMapping.FOOT.getUnit());
            PolygonSymbolizerImpl clone = (PolygonSymbolizerImpl) visitor.copy(polygonSymb);
            assertCopy(polygonSymb, clone);

            PolygonSymbolizerImpl notEq = (PolygonSymbolizerImpl) sf.getDefaultPolygonSymbolizer();
            notEq.setUnitOfMeasure(UomOgcMapping.PIXEL.getUnit());

            assertEqualsContract(clone, notEq, polygonSymb);
        } finally {
            visitor.setStrict(false);
        }
    }

    @Test
    public void testLineSymbolizer() {
        LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) sf.createLineSymbolizer();
        LineSymbolizerImpl clone = (LineSymbolizerImpl) visitor.copy(lineSymb);
        assertCopy(lineSymb, clone);

        LineSymbolizerImpl notEq = (LineSymbolizerImpl) sf.getDefaultLineSymbolizer();
        notEq.setGeometryPropertyName("something_else");
        assertEqualsContract(clone, notEq, lineSymb);
    }

    @Test
    public void testLineSymbolizerWithUOM() {
        LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) sf.createLineSymbolizer();
        LineSymbolizerImpl clone = (LineSymbolizerImpl) visitor.copy(lineSymb);
        assertCopy(lineSymb, clone);

        LineSymbolizerImpl notEq = (LineSymbolizerImpl) sf.getDefaultLineSymbolizer();
        notEq.setUnitOfMeasure(UomOgcMapping.METRE.getUnit());
        assertEqualsContract(clone, notEq, lineSymb);
    }

    @Test
    public void testGraphic() {
        GraphicImpl graphic = sf.getDefaultGraphic();
        graphic.graphicalSymbols().add(sf.getDefaultMark());
        assertEquals(2, graphic.graphicalSymbols().size());
        GraphicImpl clone = visitor.copy(graphic);
        assertCopy(graphic, clone);
        assertEqualsContract(graphic, clone);

        GraphicImpl notEq = sf.getDefaultGraphic();
        assertEqualsContract(clone, notEq, graphic);
    }

    @Test
    public void testExternalGraphic() {
        ExternalGraphicImpl exGraphic = sf.createExternalGraphic("http://somewhere", "image/png");
        ExternalGraphicImpl clone = (ExternalGraphicImpl) visitor.copy((ExternalGraphic) exGraphic);
        assertCopy(exGraphic, clone);

        ExternalGraphicImpl notEq = sf.createExternalGraphic("http://somewhereelse", "image/jpeg");
        assertEqualsContract(clone, notEq, exGraphic);

        // make sure it works for different format, same url
        ExternalGraphicImpl notEq2 = (ExternalGraphicImpl) visitor.copy((ExternalGraphic) clone);
        notEq2.setFormat("image/jpeg");
        assertEqualsContract(clone, notEq2, exGraphic);
    }

    @Test
    public void testExternalGraphicWithInlineContent() {
        Icon icon =
                new Icon() {
                    @Override
                    public void paintIcon(Component c, Graphics g, int x, int y) {}

                    @Override
                    public int getIconWidth() {
                        return 16;
                    }

                    @Override
                    public int getIconHeight() {
                        return 16;
                    }
                };

        ExternalGraphicImpl exGraphic = sf.createExternalGraphic(icon, "image/png");
        ExternalGraphicImpl clone = (ExternalGraphicImpl) visitor.copy((ExternalGraphic) exGraphic);
        assertCopy(exGraphic, clone);
        Assert.assertSame(exGraphic.getInlineContent(), clone.getInlineContent());

        ExternalGraphicImpl notEq = sf.createExternalGraphic(icon, "image/jpeg");
        assertEqualsContract(clone, notEq, exGraphic);

        // make sure it works for different format, same content
        ExternalGraphicImpl notEq2 = (ExternalGraphicImpl) visitor.copy((ExternalGraphic) clone);
        notEq2.setFormat("image/jpeg");
        assertEqualsContract(clone, notEq2, exGraphic);
    }

    @Test
    public void testCircleMark() {
        MarkImpl mark = sf.getCircleMark();
        MarkImpl clone = (MarkImpl) visitor.copy((Mark) mark);
        assertCopy(mark, clone);
        assertEqualsContract(mark, clone);
        MarkImpl notEq = sf.getStarMark();
        assertEqualsContract(clone, notEq, mark);
    }

    @Test
    public void testSquareMark() {
        MarkImpl mark = sf.getSquareMark();
        MarkImpl clone = (MarkImpl) visitor.copy((Mark) mark);
        assertCopy(mark, clone);
        assertEqualsContract(mark, clone);

        MarkImpl notEq = sf.getStarMark();
        assertEqualsContract(clone, notEq, mark);
    }

    @Test
    public void testExternalMark() throws URISyntaxException {
        OnLineResourceImpl or = new OnLineResourceImpl();
        or.setLinkage(new URI("ttf://wingdings"));
        ExternalMarkImpl externalMark = sf.externalMark(or, "ttf", 15);
        MarkImpl mark = sf.createMark();
        mark.setExternalMark(externalMark);
        MarkImpl clone = (MarkImpl) visitor.copy((Mark) mark);
        assertCopy(mark, clone);
        assertCopy(mark.getExternalMark(), clone.getExternalMark());
        ExternalMarkImpl emCopy = clone.getExternalMark();
        assertEquals("ttf", emCopy.getFormat());
        assertEquals("ttf://wingdings", emCopy.getOnlineResource().getLinkage().toASCIIString());
        assertEquals(15, emCopy.getMarkIndex());
    }

    @Test
    public void testFill() {
        FillImpl fill = (FillImpl) sf.getDefaultFill();
        FillImpl clone = (FillImpl) visitor.copy(fill);
        assertCopy(fill, clone);
        assertEqualsContract(fill, clone);
        FillImpl notEq = (FillImpl) sf.createFill(ff.literal("#FF0000"));
        assertEqualsContract(clone, notEq, fill);
    }

    @Test
    public void testStroke() {
        StrokeImpl stroke = sf.getDefaultStroke();
        StrokeImpl clone = (StrokeImpl) visitor.copy(stroke);
        assertCopy(stroke, clone);
        assertEqualsContract(stroke, clone);
        StrokeImpl notEq = sf.createStroke(ff.literal("#FF0000"), ff.literal(10));
        assertEqualsContract(clone, notEq, stroke);

        // a stroke is a complex object with lots of properties,
        // need more extensive tests here.
        StrokeImpl dashArray = sf.getDefaultStroke();
        dashArray.setDashArray(new float[] {1.0f, 2.0f, 3.0f});

        StrokeImpl dashArray2 = (StrokeImpl) ((Cloneable) dashArray).clone();
        assertEqualsContract(dashArray, dashArray2);
    }

    private static void assertCopy(Object real, Object clone) {
        Assert.assertNotNull("Real was null", real);
        Assert.assertNotNull("Clone was null", clone);
        Assert.assertNotSame("" + real.getClass().getName() + " was not cloned", real, clone);
    }

    private static void assertEqualsContract(Object controlEqual, Object controlNe, Object test) {
        Assert.assertNotNull(controlEqual);
        Assert.assertNotNull(controlNe);
        Assert.assertNotNull(test);

        // check reflexivity
        assertEquals("Reflexivity test failed", test, test);

        // check symmetric
        assertEquals("Symmetry test failed", controlEqual, test);
        assertEquals("Symmetry test failed", test, controlEqual);
        Assert.assertNotEquals("Symmetry test failed", test, controlNe);
        Assert.assertNotEquals("Symmetry test failed", controlNe, test);

        // check transitivity
        Assert.assertNotEquals("Transitivity test failed", controlEqual, controlNe);
        Assert.assertNotEquals("Transitivity test failed", test, controlNe);
        Assert.assertNotEquals("Transitivity test failed", controlNe, controlEqual);
        Assert.assertNotEquals("Transitivity test failed", controlNe, test);

        // check non-null
        Assert.assertNotEquals("Non-null test failed", null, test);
    }

    private static void assertEqualsContract(Object controlEqual, Object test) {
        Assert.assertNotNull(controlEqual);
        Assert.assertNotNull(test);

        // check reflexivity
        assertEquals("Reflexivity test failed", test, test);

        // check symmetric
        assertEquals("Symmetry test failed", controlEqual, test);
        assertEquals("Symmetry test failed", test, controlEqual);

        // check non-null
        Assert.assertNotEquals("Non-null test failed", null, test);

        // assertHashcode equality
        int controlEqHash = controlEqual.hashCode();
        int testHash = test.hashCode();
        assertEquals("Equal objects should return equal hashcodes", controlEqHash, testHash);
    }

    @Test
    public void testContrastEnhancementDuplication() throws Exception {

        ContrastEnhancementImpl ce = sf.createContrastEnhancement();
        ce.setGammaValue(sb.literalExpression(0.5));
        ce.setMethod(ContrastMethod.HISTOGRAM);

        ce.accept(visitor);
        ContrastEnhancementImpl ce2 = (ContrastEnhancementImpl) visitor.getCopy();

        assertEquals(
                "Gamma value incorrest after duplication",
                ((Literal) ce.getGammaValue()).getValue(),
                ((Literal) ce2.getGammaValue()).getValue());
        assertEquals(
                "ContrastMethod must be equal after duplication ", ce.getMethod(), ce2.getMethod());
    }

    @Test
    public void testColorMapEntryDuplication() throws Exception {

        ColorMapEntry cme = sf.createColorMapEntry();
        cme.setColor(sb.colorExpression(Color.YELLOW));
        cme.setLabel("thelabel");
        cme.setQuantity(sb.literalExpression(66.66));
        cme.setOpacity(sb.literalExpression(0.77));

        cme.accept(visitor);

        ColorMapEntry cme2 = (ColorMapEntry) visitor.getCopy();

        assertEquals(
                "Colormaps LABEL must be equal after duplication ",
                cme.getLabel(),
                cme2.getLabel());
        assertEquals(
                "Colormaps QUANTITY must be equal after duplication ",
                cme.getQuantity(),
                cme2.getQuantity());
        assertEquals(
                "Colormaps COLOR must be equal after duplication ",
                cme.getColor(),
                cme2.getColor());
        assertEquals(
                "Colormaps OPACITY must be equal after duplication ",
                cme.getOpacity(),
                cme2.getOpacity());
    }

    @Test
    public void testPointSymbolizerWithGeomFunction() throws Exception {
        URL urlExternal = getClass().getResource("/data/sld/blob.gif");
        ExternalGraphicImpl extg = sb.createExternalGraphic(urlExternal, "image/svg+xml");
        GraphicImpl graphic = sb.createGraphic(extg, null, null);
        PointSymbolizerImpl pointSymb = sb.createPointSymbolizer(graphic);

        // Set a function as geometry
        Function geomFunc = ff.function("centroid", ff.property("thr_geom"));
        pointSymb.setGeometry(geomFunc);

        pointSymb.accept(visitor);
        PointSymbolizerImpl copy = (PointSymbolizerImpl) visitor.getCopy();

        assertEquals(
                "Any Expression set as Geometry must be correctly replicated",
                geomFunc,
                copy.getGeometry());
    }

    @Test
    public void testRasterSymbolizerDuplicationWithGeometryFunction() {
        // create a default RasterSymbolizer
        RasterSymbolizerImpl original = sb.createRasterSymbolizer();

        // Set a function as geometry
        Function geomFunc = ff.function("centroid", ff.property("thr_geom"));
        original.setGeometry(geomFunc);

        // duplicate it
        original.accept(visitor);
        RasterSymbolizerImpl copy = (RasterSymbolizerImpl) visitor.getCopy();

        // compare it
        assertEquals(
                "Any Expression set as Geometry must be correctly replicated",
                geomFunc,
                copy.getGeometry());
    }

    @Test
    public void testLineSymbolizerWithGeometryFunction() {
        LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) sf.createLineSymbolizer();

        // Set a function as geometry
        Function geomFunc = ff.function("centroid", ff.property("thr_geom"));
        lineSymb.setGeometry(geomFunc);

        LineSymbolizerImpl copy = (LineSymbolizerImpl) visitor.copy(lineSymb);

        // compare it
        assertEquals(
                "Any Expression set as Geometry must be correctly replicated",
                geomFunc,
                copy.getGeometry());
    }

    @Test
    public void testPolygonSymbolizerWithGeometryFunction() {
        PolygonSymbolizerImpl symb = sf.createPolygonSymbolizer();

        // Set a function as geometry
        Function geomFunc = ff.function("centroid", ff.property("thr_geom"));
        symb.setGeometry(geomFunc);

        PolygonSymbolizerImpl copy = (PolygonSymbolizerImpl) visitor.copy(symb);

        // compare it
        assertEquals(
                "Any Expression set as Geometry must be correctly replicated",
                geomFunc,
                copy.getGeometry());
    }

    @Test
    public void testTextSymbolizerWithGeometryFunction() {
        TextSymbolizerImpl symb = (TextSymbolizerImpl) sf.createTextSymbolizer();

        // Set a function as geometry
        Function geomFunc = ff.function("centroid", ff.property("the_geom"));
        symb.setGeometry(geomFunc);

        TextSymbolizerImpl copy = (TextSymbolizerImpl) visitor.copy(symb);

        // compare it
        assertEquals(
                "Any Expression set as Geometry must be correctly replicated",
                geomFunc,
                copy.getGeometry());
    }

    /**
     * Test that {@link TextSymbolizerImpl} is correctly duplicated.
     *
     * @author Stefan Tzeggai, June 29th 2010
     */
    @Test
    public void testTextSymbolizer() {
        TextSymbolizerImpl symb = (TextSymbolizerImpl) sf.createTextSymbolizer();

        // Create a Graphic with two recognizable values
        GraphicImpl gr = new GraphicImpl(ff);
        gr.setOpacity(ff.literal(0.77));
        gr.setSize(ff.literal(77));
        symb.setGraphic(gr);
        Literal snippet = ff.literal("no idea what a snipet is good for");
        symb.setSnippet(snippet);
        Literal fD = ff.literal("some description");
        symb.setFeatureDescription(fD);
        OtherTextImpl otherText = new OtherTextImpl();
        otherText.setTarget("otherTextTarget");
        otherText.setText(ff.literal("otherTextText"));
        symb.setOtherText(otherText);

        // copy it
        TextSymbolizerImpl copy = (TextSymbolizerImpl) visitor.copy(symb);

        // compare it
        assertEquals(
                "Graphic of TextSymbolizer has not been correctly duplicated",
                gr,
                copy.getGraphic());
        assertEquals(
                "Graphic of TextSymbolizer has not been correctly duplicated",
                gr.getOpacity(),
                copy.getGraphic().getOpacity());
        assertEquals(
                "Graphic of TextSymbolizer has not been correctly duplicated",
                gr.getSize(),
                copy.getGraphic().getSize());
        assertEquals(
                "Snippet of TextSymbolizer has not been correctly duplicated",
                snippet,
                copy.getSnippet());
        assertEquals(
                "FeatureDescription of TextSymbolizer has not been correctly duplicated",
                fD,
                copy.getFeatureDescription());
        assertEquals(
                "OtherText of TextSymbolizer has not been correctly duplicated",
                otherText.getTarget(),
                copy.getOtherText().getTarget());
        assertEquals(
                "OtherText of TextSymbolizer has not been correctly duplicated",
                otherText.getText(),
                copy.getOtherText().getText());
    }

    /*
     * Tests that perpendicularOffset for LineSymbolizer is duplicated correctly
     */
    @Test
    public void testLineSymbolizerWithPerpendicularOffset() {
        LineSymbolizerImpl ls = (LineSymbolizerImpl) sf.createLineSymbolizer();
        ls.setPerpendicularOffset(ff.literal(0.88));

        // copy
        LineSymbolizerImpl copy = (LineSymbolizerImpl) visitor.copy(ls);

        // compare
        assertEquals(
                "PerpendicularOffset of LineSymbolizer has not been correctly duplicated",
                ls.getPerpendicularOffset(),
                copy.getPerpendicularOffset());
    }

    /** Test SelectedChannelType copy with Expression */
    @Test
    public void testSelectedChannelTypeDuplication() throws Exception {
        final String b1 = "b1";

        SelectedChannelTypeImpl sct =
                (SelectedChannelTypeImpl)
                        sf.createSelectedChannelType(
                                ff.function("env", ff.literal(b1), ff.literal("1")),
                                sf.createContrastEnhancement());
        sct.accept(visitor);
        SelectedChannelTypeImpl clone = (SelectedChannelTypeImpl) visitor.getCopy();

        assertEquals(sct, clone);
    }
}
