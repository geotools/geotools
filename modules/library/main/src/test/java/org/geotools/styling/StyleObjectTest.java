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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.util.Cloneable;

/**
 * Tests style cloning
 *
 * @author Sean Geoghegan
 */
public class StyleObjectTest {
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
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();
        fts.featureTypeNames().add(new NameImpl("feature-type-1"));

        FeatureTypeStyle fts2 = fts2();

        Style style = styleFactory.getDefaultStyle();
        style.featureTypeStyles().add(fts);
        style.featureTypeStyles().add(fts2);

        Style clone = (Style) ((Cloneable) style).clone();
        assertClone(style, clone);

        Style notEq = styleFactory.getDefaultStyle();

        fts2 = fts2();
        notEq.featureTypeStyles().add(fts2);
        assertEqualsContract(clone, notEq, style);
    }

    private FeatureTypeStyle fts2() {
        FeatureTypeStyle fts2 = styleFactory.createFeatureTypeStyle();
        Rule rule = styleFactory.createRule();
        fts2.rules().add(rule);
        fts2.featureTypeNames().add(new NameImpl("feature-type-2"));

        return fts2;
    }

    @Test
    public void testFeatureTypeStyle() throws Exception {
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();
        fts.featureTypeNames().add(new NameImpl("feature-type"));

        Rule rule1 = styleFactory.createRule();
        rule1.setName("rule1");
        rule1.setFilter(filterFactory.id(Collections.singleton(filterFactory.featureId("FID"))));

        Rule rule2 = styleFactory.createRule();
        rule2.setElseFilter(true);
        rule2.setName("rule2");
        fts.rules().add(rule1);
        fts.rules().add(rule2);

        FeatureTypeStyle clone = (FeatureTypeStyle) ((Cloneable) fts).clone();
        assertClone(fts, clone);

        rule1 = styleFactory.createRule();
        rule1.setName("rule1");
        rule1.setFilter(filterFactory.id(Collections.singleton(filterFactory.featureId("FID"))));

        FeatureTypeStyle notEq = styleFactory.createFeatureTypeStyle();
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

        Rule rule = styleFactory.createRule();
        rule.symbolizers().addAll(Arrays.asList(symb1, symb2));

        Rule clone = (Rule) ((Cloneable) rule).clone();
        assertClone(rule, clone);

        symb2 =
                styleFactory.createPolygonSymbolizer(
                        styleFactory.getDefaultStroke(), styleFactory.getDefaultFill(), "shape");

        Rule notEq = styleFactory.createRule();
        notEq.symbolizers().add(symb2);
        assertEqualsContract(clone, notEq, rule);

        symb1 = styleFactory.createLineSymbolizer(styleFactory.getDefaultStroke(), "geometry");
        clone.symbolizers().clear();
        clone.symbolizers().add(symb1);
        Assert.assertNotEquals(rule, clone);
    }

    @Test
    public void testPointSymbolizer() throws Exception {
        PointSymbolizer pointSymb = styleFactory.createPointSymbolizer();
        PointSymbolizer clone = (PointSymbolizer) ((Cloneable) pointSymb).clone();
        assertClone(pointSymb, clone);

        PointSymbolizer notEq = styleFactory.getDefaultPointSymbolizer();
        notEq.setGeometryPropertyName("something_else");
        assertEqualsContract(clone, notEq, pointSymb);
    }

    @Test
    public void testTextSymbolizer() {
        TextSymbolizer textSymb = styleFactory.createTextSymbolizer();
        Expression offset = filterFactory.literal(10);
        textSymb.setLabelPlacement(styleFactory.createLinePlacement(offset));

        TextSymbolizer clone = (TextSymbolizer) ((Cloneable) textSymb).clone();
        assertClone(textSymb, clone);

        TextSymbolizer notEq = styleFactory.getDefaultTextSymbolizer();
        Expression ancX = filterFactory.literal(10);
        Expression ancY = filterFactory.literal(10);
        AnchorPoint ancPoint = styleFactory.createAnchorPoint(ancX, ancY);
        LabelPlacement placement = styleFactory.createPointPlacement(ancPoint, null, null);
        notEq.setLabelPlacement(placement);
        assertEqualsContract(clone, notEq, textSymb);
    }

    @Test
    public void testFont() {
        Font font = styleFactory.getDefaultFont();
        Font clone = (Font) ((Cloneable) font).clone();
        assertClone(font, clone);

        Font other =
                styleFactory.createFont(
                        filterFactory.literal("other"),
                        filterFactory.literal("normal"),
                        filterFactory.literal("BOLD"),
                        filterFactory.literal(12));

        assertEqualsContract(clone, other, font);
    }

    @Test
    public void testHalo() {
        Halo halo =
                styleFactory.createHalo(styleFactory.getDefaultFill(), filterFactory.literal(10));
        Halo clone = (Halo) ((Cloneable) halo).clone();
        assertClone(halo, clone);

        Halo other =
                styleFactory.createHalo(styleFactory.getDefaultFill(), filterFactory.literal(12));
        assertEqualsContract(clone, other, halo);
    }

    @Test
    public void testLinePlacement() throws Exception {
        LinePlacement linePlacement = styleFactory.createLinePlacement(filterFactory.literal(12));
        LinePlacement clone = (LinePlacement) ((Cloneable) linePlacement).clone();
        assertClone(linePlacement, clone);

        LinePlacement other = styleFactory.createLinePlacement(filterFactory.property("NAME"));
        assertEqualsContract(clone, other, linePlacement);
    }

    @Test
    public void testAnchorPoint() {
        AnchorPoint anchorPoint =
                styleFactory.createAnchorPoint(filterFactory.literal(1), filterFactory.literal(2));
        AnchorPoint clone = (AnchorPoint) ((Cloneable) anchorPoint).clone();
        assertClone(anchorPoint, clone);

        AnchorPoint other =
                styleFactory.createAnchorPoint(filterFactory.literal(3), filterFactory.literal(4));
        assertEqualsContract(clone, other, anchorPoint);
    }

    @Test
    public void testDisplacement() {
        Displacement displacement =
                styleFactory.createDisplacement(filterFactory.literal(1), filterFactory.literal(2));
        Displacement clone = (Displacement) ((Cloneable) displacement).clone();
        assertClone(displacement, clone);

        Displacement other =
                styleFactory.createDisplacement(filterFactory.literal(3), filterFactory.literal(4));
        assertEqualsContract(clone, other, displacement);
    }

    @Test
    public void testPointPlacement() {
        PointPlacement pointPl = styleFactory.getDefaultPointPlacement();
        PointPlacement clone = (PointPlacement) ((Cloneable) pointPl).clone();
        assertClone(pointPl, clone);

        PointPlacement other = (PointPlacement) ((Cloneable) pointPl).clone();
        other.setRotation(filterFactory.literal(274.0));
        assertEqualsContract(clone, other, pointPl);
    }

    @Test
    public void testPolygonSymbolizer() {
        PolygonSymbolizer polygonSymb = styleFactory.createPolygonSymbolizer();
        PolygonSymbolizer clone = (PolygonSymbolizer) ((Cloneable) polygonSymb).clone();
        assertClone(polygonSymb, clone);

        PolygonSymbolizer notEq = styleFactory.getDefaultPolygonSymbolizer();
        notEq.setGeometryPropertyName("something_else");
        assertEqualsContract(clone, notEq, polygonSymb);
    }

    @Test
    public void testLineSymbolizer() {
        LineSymbolizer lineSymb = styleFactory.createLineSymbolizer();
        LineSymbolizer clone = (LineSymbolizer) ((Cloneable) lineSymb).clone();
        assertClone(lineSymb, clone);

        LineSymbolizer notEq = styleFactory.getDefaultLineSymbolizer();
        notEq.setGeometryPropertyName("something_else");
        assertEqualsContract(clone, notEq, lineSymb);
    }

    @Test
    public void testGraphic() {
        Graphic graphic = styleFactory.getDefaultGraphic();
        graphic.graphicalSymbols().add(styleFactory.getDefaultMark());

        Graphic clone = (Graphic) ((Cloneable) graphic).clone();
        assertClone(graphic, clone);
        assertEqualsContract(clone, graphic);
        Assert.assertEquals(clone.graphicalSymbols().size(), graphic.graphicalSymbols().size());

        Graphic notEq = styleFactory.getDefaultGraphic();
        assertEqualsContract(clone, notEq, graphic);
    }

    @Test
    public void testExternalGraphic() {
        ExternalGraphic exGraphic =
                styleFactory.createExternalGraphic("http://somewhere", "image/png");
        ExternalGraphic clone = (ExternalGraphic) ((Cloneable) exGraphic).clone();
        assertClone(exGraphic, clone);

        ExternalGraphic notEq =
                styleFactory.createExternalGraphic("http://somewhereelse", "image/jpeg");
        assertEqualsContract(clone, notEq, exGraphic);

        // make sure it works for different format, same url
        ExternalGraphic notEq2 = (ExternalGraphic) ((Cloneable) clone).clone();
        notEq2.setFormat("image/jpeg");
        assertEqualsContract(clone, notEq2, exGraphic);
    }

    @Test
    public void testMark() {
        Mark mark = styleFactory.getCircleMark();
        Mark clone = (Mark) ((Cloneable) mark).clone();
        assertClone(mark, clone);

        Mark notEq = styleFactory.getStarMark();
        assertEqualsContract(clone, notEq, mark);
    }

    @Test
    public void testFill() {
        Fill fill = styleFactory.getDefaultFill();
        Fill clone = (Fill) ((Cloneable) fill).clone();
        assertClone(fill, clone);

        Fill notEq = styleFactory.createFill(filterFactory.literal("#FF0000"));
        assertEqualsContract(clone, notEq, fill);
    }

    @Test
    public void testStroke() {
        Stroke stroke = styleFactory.getDefaultStroke();
        DuplicatingStyleVisitor duplicate = new DuplicatingStyleVisitor(styleFactory);
        stroke.accept(duplicate);
        Stroke clone = (Stroke) duplicate.getCopy();

        assertClone(stroke, clone);

        Stroke notEq =
                styleFactory.createStroke(
                        filterFactory.literal("#FF0000"), filterFactory.literal(10));
        assertEqualsContract(clone, notEq, stroke);

        // a stroke is a complex object with lots of properties,
        // need more extensive tests here.
        Stroke dashArray = styleFactory.getDefaultStroke();
        dashArray.setDashArray(new float[] {1.0f, 2.0f, 3.0f});

        dashArray.accept(duplicate);
        Stroke dashArray2 = (Stroke) duplicate.getCopy();
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
        FeatureTypeStyle fts = new FeatureTypeStyleImpl();
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
