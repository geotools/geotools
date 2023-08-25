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

import java.util.Arrays;
import java.util.Collections;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests style cloning
 *
 * @author Sean Geoghegan
 */
public class StyleImplObjectTest {
    private StyleFactory styleFactory;
    private FilterFactory filterFactory;

    /*
     * @see TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        styleFactory = CommonFactoryFinder.getStyleFactory();
        filterFactory = CommonFactoryFinder.getFilterFactory(null);
    }

    /*
     * @see TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        styleFactory = null;
    }

    @Test
    public void testStyle() throws Exception {
        FeatureTypeStyleImpl fts = styleFactory.createFeatureTypeStyle();
        fts.featureTypeNames().add(new NameImpl("feature-type-1"));

        FeatureTypeStyleImpl fts2 = fts2();

        StyleImpl style = styleFactory.getDefaultStyle();
        style.featureTypeStyles().add(fts);
        style.featureTypeStyles().add(fts2);

        StyleImpl clone = (StyleImpl) ((Cloneable) style).clone();
        assertClone(style, clone);

        StyleImpl notEq = styleFactory.getDefaultStyle();

        fts2 = fts2();
        notEq.featureTypeStyles().add(fts2);
        assertEqualsContract(clone, notEq, style);
    }

    private FeatureTypeStyleImpl fts2() {
        FeatureTypeStyleImpl fts2 = styleFactory.createFeatureTypeStyle();
        RuleImpl rule = (RuleImpl) styleFactory.createRule();
        fts2.rules().add(rule);
        fts2.featureTypeNames().add(new NameImpl("feature-type-2"));

        return fts2;
    }

    @Test
    public void testFeatureTypeStyle() throws Exception {
        FeatureTypeStyleImpl fts = styleFactory.createFeatureTypeStyle();
        fts.featureTypeNames().add(new NameImpl("feature-type"));

        RuleImpl rule1 = (RuleImpl) styleFactory.createRule();
        rule1.setName("rule1");
        rule1.setFilter(filterFactory.id(Collections.singleton(filterFactory.featureId("FID"))));

        RuleImpl rule2 = (RuleImpl) styleFactory.createRule();
        rule2.setElseFilter(true);
        rule2.setName("rule2");
        fts.rules().add(rule1);
        fts.rules().add(rule2);

        FeatureTypeStyleImpl clone = (FeatureTypeStyleImpl) ((Cloneable) fts).clone();
        assertClone(fts, clone);

        rule1 = (RuleImpl) styleFactory.createRule();
        rule1.setName("rule1");
        rule1.setFilter(filterFactory.id(Collections.singleton(filterFactory.featureId("FID"))));

        FeatureTypeStyleImpl notEq = styleFactory.createFeatureTypeStyle();
        notEq.setName("fts-not-equal");
        notEq.rules().add(rule1);
        assertEqualsContract(clone, notEq, fts);
    }

    @Test
    public void testRule() throws Exception {
        Symbolizer symb1 =
                styleFactory.createLineSymbolizer(styleFactory.getDefaultStroke(), "geometry");

        Symbolizer symb2 =
                styleFactory.createPolygonSymbolizer(
                        styleFactory.getDefaultStroke(), styleFactory.getDefaultFill(), "shape");

        RuleImpl rule = (RuleImpl) styleFactory.createRule();
        rule.symbolizers().addAll(Arrays.asList(symb1, symb2));

        RuleImpl clone = (RuleImpl) ((Cloneable) rule).clone();
        assertClone(rule, clone);

        symb2 =
                styleFactory.createPolygonSymbolizer(
                        styleFactory.getDefaultStroke(), styleFactory.getDefaultFill(), "shape");

        RuleImpl notEq = (RuleImpl) styleFactory.createRule();
        notEq.symbolizers().add(symb2);
        assertEqualsContract(clone, notEq, rule);

        symb1 = styleFactory.createLineSymbolizer(styleFactory.getDefaultStroke(), "geometry");
        clone.symbolizers().clear();
        clone.symbolizers().add(symb1);
        Assert.assertNotEquals(rule, clone);
    }

    @Test
    public void testPointSymbolizer() throws Exception {
        PointSymbolizerImpl pointSymb = styleFactory.createPointSymbolizer();
        PointSymbolizerImpl clone = (PointSymbolizerImpl) ((Cloneable) pointSymb).clone();
        assertClone(pointSymb, clone);

        PointSymbolizerImpl notEq = (PointSymbolizerImpl) styleFactory.getDefaultPointSymbolizer();
        notEq.setGeometryPropertyName("something_else");
        assertEqualsContract(clone, notEq, pointSymb);
    }

    @Test
    public void testTextSymbolizer() {
        TextSymbolizerImpl textSymb = (TextSymbolizerImpl) styleFactory.createTextSymbolizer();
        Expression offset = filterFactory.literal(10);
        textSymb.setLabelPlacement(styleFactory.createLinePlacement(offset));

        TextSymbolizerImpl clone = (TextSymbolizerImpl) ((Cloneable) textSymb).clone();
        assertClone(textSymb, clone);

        TextSymbolizerImpl notEq = styleFactory.getDefaultTextSymbolizer();
        Expression ancX = filterFactory.literal(10);
        Expression ancY = filterFactory.literal(10);
        AnchorPointImpl ancPoint = styleFactory.createAnchorPoint(ancX, ancY);
        LabelPlacement placement = styleFactory.createPointPlacement(ancPoint, null, null);
        notEq.setLabelPlacement(placement);
        assertEqualsContract(clone, notEq, textSymb);
    }

    @Test
    public void testFont() {
        FontImpl font = styleFactory.getDefaultFont();
        FontImpl clone = (FontImpl) ((Cloneable) font).clone();
        assertClone(font, clone);

        FontImpl other =
                styleFactory.createFont(
                        filterFactory.literal("other"),
                        filterFactory.literal("normal"),
                        filterFactory.literal("BOLD"),
                        filterFactory.literal(12));

        assertEqualsContract(clone, other, font);
    }

    @Test
    public void testHalo() {
        HaloImpl halo =
                (HaloImpl)
                        styleFactory.createHalo(
                                styleFactory.getDefaultFill(), filterFactory.literal(10));
        HaloImpl clone = (HaloImpl) ((Cloneable) halo).clone();
        assertClone(halo, clone);

        HaloImpl other =
                (HaloImpl)
                        styleFactory.createHalo(
                                styleFactory.getDefaultFill(), filterFactory.literal(12));
        assertEqualsContract(clone, other, halo);
    }

    @Test
    public void testLinePlacement() throws Exception {
        LinePlacementImpl linePlacement =
                styleFactory.createLinePlacement(filterFactory.literal(12));
        LinePlacementImpl clone = (LinePlacementImpl) ((Cloneable) linePlacement).clone();
        assertClone(linePlacement, clone);

        LinePlacementImpl other = styleFactory.createLinePlacement(filterFactory.property("NAME"));
        assertEqualsContract(clone, other, linePlacement);
    }

    @Test
    public void testAnchorPoint() {
        AnchorPointImpl anchorPoint =
                styleFactory.createAnchorPoint(filterFactory.literal(1), filterFactory.literal(2));
        AnchorPointImpl clone = (AnchorPointImpl) ((Cloneable) anchorPoint).clone();
        assertClone(anchorPoint, clone);

        AnchorPointImpl other =
                styleFactory.createAnchorPoint(filterFactory.literal(3), filterFactory.literal(4));
        assertEqualsContract(clone, other, anchorPoint);
    }

    @Test
    public void testDisplacement() {
        DisplacementImpl displacement =
                styleFactory.createDisplacement(filterFactory.literal(1), filterFactory.literal(2));
        DisplacementImpl clone = (DisplacementImpl) ((Cloneable) displacement).clone();
        assertClone(displacement, clone);

        DisplacementImpl other =
                styleFactory.createDisplacement(filterFactory.literal(3), filterFactory.literal(4));
        assertEqualsContract(clone, other, displacement);
    }

    @Test
    public void testPointPlacement() {
        PointPlacementImpl pointPl = (PointPlacementImpl) styleFactory.getDefaultPointPlacement();
        PointPlacementImpl clone = (PointPlacementImpl) ((Cloneable) pointPl).clone();
        assertClone(pointPl, clone);

        PointPlacementImpl other = (PointPlacementImpl) ((Cloneable) pointPl).clone();
        other.setRotation(filterFactory.literal(274.0));
        assertEqualsContract(clone, other, pointPl);
    }

    @Test
    public void testPolygonSymbolizer() {
        PolygonSymbolizerImpl polygonSymb = styleFactory.createPolygonSymbolizer();
        PolygonSymbolizerImpl clone = (PolygonSymbolizerImpl) ((Cloneable) polygonSymb).clone();
        assertClone(polygonSymb, clone);

        PolygonSymbolizerImpl notEq =
                (PolygonSymbolizerImpl) styleFactory.getDefaultPolygonSymbolizer();
        notEq.setGeometryPropertyName("something_else");
        assertEqualsContract(clone, notEq, polygonSymb);
    }

    @Test
    public void testLineSymbolizer() {
        LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) styleFactory.createLineSymbolizer();
        LineSymbolizerImpl clone = (LineSymbolizerImpl) ((Cloneable) lineSymb).clone();
        assertClone(lineSymb, clone);

        LineSymbolizerImpl notEq = (LineSymbolizerImpl) styleFactory.getDefaultLineSymbolizer();
        notEq.setGeometryPropertyName("something_else");
        assertEqualsContract(clone, notEq, lineSymb);
    }

    @Test
    public void testGraphic() {
        GraphicImpl graphic = styleFactory.getDefaultGraphic();
        graphic.graphicalSymbols().add(styleFactory.getDefaultMark());

        GraphicImpl clone = (GraphicImpl) ((Cloneable) graphic).clone();
        assertClone(graphic, clone);
        assertEqualsContract(clone, graphic);
        Assert.assertEquals(clone.graphicalSymbols().size(), graphic.graphicalSymbols().size());

        GraphicImpl notEq = styleFactory.getDefaultGraphic();
        assertEqualsContract(clone, notEq, graphic);
    }

    @Test
    public void testExternalGraphic() {
        ExternalGraphicImpl exGraphic =
                styleFactory.createExternalGraphic("http://somewhere", "image/png");
        ExternalGraphicImpl clone = (ExternalGraphicImpl) ((Cloneable) exGraphic).clone();
        assertClone(exGraphic, clone);

        ExternalGraphicImpl notEq =
                styleFactory.createExternalGraphic("http://somewhereelse", "image/jpeg");
        assertEqualsContract(clone, notEq, exGraphic);

        // make sure it works for different format, same url
        ExternalGraphicImpl notEq2 = (ExternalGraphicImpl) ((Cloneable) clone).clone();
        notEq2.setFormat("image/jpeg");
        assertEqualsContract(clone, notEq2, exGraphic);
    }

    @Test
    public void testMark() {
        MarkImpl mark = styleFactory.getCircleMark();
        MarkImpl clone = (MarkImpl) ((Cloneable) mark).clone();
        assertClone(mark, clone);

        MarkImpl notEq = styleFactory.getStarMark();
        assertEqualsContract(clone, notEq, mark);
    }

    @Test
    public void testFill() {
        FillImpl fill = (FillImpl) styleFactory.getDefaultFill();
        FillImpl clone = (FillImpl) ((Cloneable) fill).clone();
        assertClone(fill, clone);

        FillImpl notEq = (FillImpl) styleFactory.createFill(filterFactory.literal("#FF0000"));
        assertEqualsContract(clone, notEq, fill);
    }

    @Test
    public void testStroke() {
        StrokeImpl stroke = styleFactory.getDefaultStroke();
        DuplicatingStyleVisitor duplicate = new DuplicatingStyleVisitor(styleFactory);
        stroke.accept(duplicate);
        StrokeImpl clone = (StrokeImpl) duplicate.getCopy();

        assertClone(stroke, clone);

        StrokeImpl notEq =
                styleFactory.createStroke(
                        filterFactory.literal("#FF0000"), filterFactory.literal(10));
        assertEqualsContract(clone, notEq, stroke);

        // a stroke is a complex object with lots of properties,
        // need more extensive tests here.
        StrokeImpl dashArray = styleFactory.getDefaultStroke();
        dashArray.setDashArray(new float[] {1.0f, 2.0f, 3.0f});

        dashArray.accept(duplicate);
        StrokeImpl dashArray2 = (StrokeImpl) duplicate.getCopy();
        assertEqualsContract(dashArray, dashArray2);
    }

    private static void assertClone(Object real, Object clone) {
        Assert.assertNotNull("Real was null", real);
        Assert.assertNotNull("Clone was null", clone);
        Assert.assertNotSame("" + real.getClass().getName() + " was not cloned", real, clone);
    }

    private static void assertEqualsContract(Object controlEqual, Object controlNe, Object test) {
        Assert.assertNotNull(controlEqual);
        Assert.assertNotNull(controlNe);
        Assert.assertNotNull(test);

        // check reflexivity
        Assert.assertEquals("Reflexivity test failed", test, test);

        // check symmetric
        Assert.assertEquals("Symmetry test failed", controlEqual, test);
        Assert.assertEquals("Symmetry test failed", test, controlEqual);
        Assert.assertNotEquals("Symmetry test failed", test, controlNe);
        Assert.assertNotEquals("Symmetry test failed", controlNe, test);

        // check transitivity
        Assert.assertNotEquals("Transitivity test failed", controlEqual, controlNe);
        Assert.assertNotEquals("Transitivity test failed", test, controlNe);
        Assert.assertNotEquals("Transitivity test failed", controlNe, controlEqual);
        Assert.assertNotEquals("Transitivity test failed", controlNe, test);

        // check non-null
        Assert.assertNotEquals("Non-null test failed", null, test);

        // assertHashcode equality
        int controlEqHash = controlEqual.hashCode();
        int testHash = test.hashCode();
        Assert.assertEquals("Equal objects should return equal hashcodes", controlEqHash, testHash);
    }

    private static void assertEqualsContract(Object controlEqual, Object test) {
        Assert.assertNotNull(controlEqual);
        Assert.assertNotNull(test);

        // check reflexivity
        Assert.assertEquals("Reflexivity test failed", test, test);

        // check symmetric
        Assert.assertEquals("Symmetry test failed", controlEqual, test);
        Assert.assertEquals("Symmetry test failed", test, controlEqual);

        // check non-null
        Assert.assertNotEquals("Non-null test failed", null, test);

        // assertHashcode equality
        int controlEqHash = controlEqual.hashCode();
        int testHash = test.hashCode();
        Assert.assertEquals("Equal objects should return equal hashcodes", controlEqHash, testHash);
    }

    @Test
    public void testFeatureStyleImplCopy() throws Exception {
        // create FeatureTypeStyleImpl
        FeatureTypeStyleImpl fts = new FeatureTypeStyleImpl();
        Assert.assertNull(fts.getTransformation());
        Assert.assertNull(fts.getOnlineResource());

        // Create OnlineResource and transformation
        OnLineResource impl = new OnLineResourceImpl();
        Expression style = filterFactory.literal("square");

        // set OnlineResource and transformation
        fts.setTransformation(style);
        fts.setOnlineResource(impl);

        // test if set
        Assert.assertEquals(fts.getTransformation(), filterFactory.literal("square"));
        Assert.assertEquals(fts.getOnlineResource(), new OnLineResourceImpl());

        // create copy fts2 from fts
        FeatureTypeStyleImpl fts2 = new FeatureTypeStyleImpl(fts);

        // test if values are equal and thus copied
        Assert.assertEquals(fts.getTransformation(), fts2.getTransformation());
        Assert.assertEquals(fts.getOnlineResource(), fts2.getOnlineResource());
    }
}
