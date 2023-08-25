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
package org.geotools.xml.styling;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.function.EnvFunction;
import org.geotools.styling.*;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Capture the default values produce by the style factory in order to capture any regressions as we
 * move forward to SE 1.1 interfaces.
 *
 * @author iant
 */
public class StyleImplFactoryTest {
    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
    static SimpleFeature feature;
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(StyleImplFactoryTest.class);

    @Test
    public void testCommonFactoryFinder() {
        LOGGER.finer("testCreateStyle");

        Assert.assertNotNull("Failed to build styleFactory", styleFactory);
    }

    /** Test of createPointSymbolizer method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreatePointSymbolizer() {
        LOGGER.finer("testCreatePointSymbolizer");

        PointSymbolizerImpl ps = styleFactory.createPointSymbolizer();

        Assert.assertNotNull("Failed to create PointSymbolizer", ps);
    }

    /** Test of createPolygonSymbolizer method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreatePolygonSymbolizer() {
        LOGGER.finer("testCreatePolygonSymbolizer");

        PolygonSymbolizerImpl ps = styleFactory.createPolygonSymbolizer();

        Assert.assertNotNull("Failed to create PolygonSymbolizer", ps);
    }

    /** Test of createLineSymbolizer method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateLineSymbolizer() {
        LOGGER.finer("testCreateLineSymbolizer");

        LineSymbolizerImpl ls = (LineSymbolizerImpl) styleFactory.createLineSymbolizer();

        Assert.assertNotNull("Failed to create PolygonSymbolizer", ls);
    }

    /** Test of createTextSymbolizer method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateTextSymbolizer() {
        LOGGER.finer("testCreateTextSymbolizer");

        TextSymbolizerImpl ts = (TextSymbolizerImpl) styleFactory.createTextSymbolizer();

        Assert.assertNotNull("Failed to create TextSymbolizer", ts);
    }

    /** Test of createFeatureTypeStyle method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateFeatureTypeStyle() {
        LOGGER.finer("testCreateFeatureTypeStyle");

        FeatureTypeStyleImpl fts = styleFactory.createFeatureTypeStyle();

        Assert.assertNotNull("failed to create featureTypeStyle", fts);
    }

    /** Test of createRule method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateRule() {
        LOGGER.finer("testCreateRule");

        RuleImpl r = (RuleImpl) styleFactory.createRule();

        Assert.assertNotNull("failed to create Rule", r);
    }

    /** Test of createStroke method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateStroke() {
        LOGGER.finer("testCreateStroke");

        StrokeImpl s =
                styleFactory.createStroke(
                        filterFactory.literal("#000000"), filterFactory.literal(2.0));

        Assert.assertNotNull("Failed to build stroke ", s);

        s =
                styleFactory.createStroke(
                        filterFactory.literal("#000000"),
                        filterFactory.literal(2.0),
                        filterFactory.literal(0.5));

        Assert.assertNotNull("Failed to build stroke ", s);

        s =
                (StrokeImpl)
                        styleFactory.createStroke(
                                filterFactory.literal("#000000"),
                                filterFactory.literal(2.0),
                                filterFactory.literal(0.5),
                                filterFactory.literal("bevel"),
                                filterFactory.literal("square"),
                                new float[] {1.1f, 2.1f, 6f, 2.1f, 1.1f, 5f},
                                filterFactory.literal(3),
                                null,
                                null);

        Assert.assertNotNull("Failed to build stroke ", s);

        Assert.assertEquals("Wrong color ", "#000000", s.getColor().evaluate(feature).toString());
        Assert.assertEquals("Wrong width ", "2.0", s.getWidth().evaluate(feature).toString());
        Assert.assertEquals("Wrong opacity ", "0.5", s.getOpacity().evaluate(feature).toString());
        Assert.assertEquals(
                "Wrong linejoin ", "bevel", s.getLineJoin().evaluate(feature).toString());
        Assert.assertEquals(
                "Wrong linejoin ", "square", s.getLineCap().evaluate(feature).toString());
        Assert.assertEquals("Broken dash array", 2.1f, s.getDashArray()[1], 0.001f);
        Assert.assertEquals(
                "Wrong dash offset ", "3", s.getDashOffset().evaluate(feature).toString());
    }

    /** Test of createFill method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateFill() {
        LOGGER.finer("testCreateFill");

        FillImpl f = (FillImpl) styleFactory.createFill(filterFactory.literal("#808080"));

        Assert.assertNotNull("Failed to build fill", f);

        f =
                (FillImpl)
                        styleFactory.createFill(
                                filterFactory.literal("#808080"), filterFactory.literal(1.0));
        Assert.assertNotNull("Failed to build fill", f);

        f = (FillImpl) styleFactory.createFill(null);
        Assert.assertEquals(f.getColor(), ConstantFill.DEFAULT.getColor());
        Assert.assertSame(f.getColor(), ConstantFill.DEFAULT.getColor());
    }

    /** Test of createMark method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateMark() {
        LOGGER.finer("testCreateMark");

        MarkImpl m = styleFactory.createMark();

        Assert.assertNotNull("Failed to build mark ", m);
    }

    /** Test of getSquareMark method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testGetNamedMarks() {
        LOGGER.finer("testGetNamedMarks");

        MarkImpl m;
        String[] names = {"Square", "Circle", "Triangle", "Star", "X", "Cross"};

        for (String name : names) {
            try {
                Class<?> target = styleFactory.getClass();

                //                LOGGER.finer("About to load get"+names[i]+"Mark");
                Method method = target.getMethod("get" + name + "Mark", (Class<?>[]) null);

                //                LOGGER.finer("got method back " + method.toString());
                m = (MarkImpl) method.invoke(styleFactory, (Object[]) null);
                Assert.assertNotNull("Failed to get " + name + " mark ", m);

                Expression exp = filterFactory.literal(name);
                Assert.assertEquals("Wrong sort of mark returned ", exp, m.getWellKnownName());
            } catch (InvocationTargetException ite) {
                Logger.getGlobal().log(Level.SEVERE, "", ite);
                Assert.fail("InvocationTargetException " + ite.getTargetException());
            } catch (Exception e) {
                Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                Assert.fail("Exception " + e.toString());
            }
        }
    }

    /** Test of createGraphic method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateGraphic() {
        LOGGER.finer("testCreateGraphic");

        ExternalGraphicImpl[] externalGraphics = {
            styleFactory.createExternalGraphic(
                    "http://www.ccg.leeds.ac.uk/ian/geotools/icons/rail.gif", "image/gif")
        };
        MarkImpl[] marks = {styleFactory.getCircleMark()};
        MarkImpl[] symbols = new MarkImpl[0];
        Expression opacity = filterFactory.literal(0.5);
        Expression size = filterFactory.literal(10);
        Expression rotation = filterFactory.literal(145.0);
        GraphicImpl g =
                styleFactory.createGraphic(
                        externalGraphics, marks, symbols, opacity, size, rotation);

        Assert.assertNotNull("failed to build graphic ", g);
    }

    /** Test of createFont method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateFont() {
        LOGGER.finer("testCreateFont");

        Expression fontFamily = filterFactory.literal("Times");
        Expression fontStyle = filterFactory.literal("Italic");
        Expression fontWeight = filterFactory.literal("Bold");
        Expression fontSize = filterFactory.literal("12");
        FontImpl f = styleFactory.createFont(fontFamily, fontStyle, fontWeight, fontSize);

        Assert.assertNotNull("Failed to build font", f);

        Assert.assertEquals(
                "Wrong font type ", "Times", f.getFamily().get(0).evaluate(feature).toString());
        Assert.assertEquals(
                "Wrong font Style ", "Italic", f.getStyle().evaluate(feature).toString());
        Assert.assertEquals(
                "Wrong font weight ", "Bold", f.getWeight().evaluate(feature).toString());
        Assert.assertEquals("Wrong font size ", "12", f.getSize().evaluate(feature).toString());
    }

    /** Test of createLinePlacement method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateLinePlacement() {
        LOGGER.finer("testCreateLinePlacement");

        LinePlacementImpl lp = styleFactory.createLinePlacement(filterFactory.literal(10));

        Assert.assertNotNull("failed to create LinePlacement", lp);
    }

    /** Test of createPointPlacement method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreatePointPlacement() {
        LOGGER.finer("testCreatePointPlacement");

        AnchorPointImpl anchorPoint =
                styleFactory.createAnchorPoint(
                        filterFactory.literal(1.0), filterFactory.literal(0.5));
        DisplacementImpl displacement =
                styleFactory.createDisplacement(
                        filterFactory.literal(10.0), filterFactory.literal(5.0));
        Expression rotation = filterFactory.literal(90.0);
        PointPlacementImpl pp =
                (PointPlacementImpl)
                        styleFactory.createPointPlacement(anchorPoint, displacement, rotation);

        Assert.assertNotNull("failed to create PointPlacement", pp);

        Assert.assertEquals(
                "Wrong X anchorPoint ",
                "1.0",
                pp.getAnchorPoint().getAnchorPointX().evaluate(feature).toString());
        Assert.assertEquals(
                "Wrong Y anchorPoint ",
                "0.5",
                pp.getAnchorPoint().getAnchorPointY().evaluate(feature).toString());
        Assert.assertEquals(
                "Wrong X displacement ",
                "10.0",
                pp.getDisplacement().getDisplacementX().evaluate(feature).toString());
        Assert.assertEquals(
                "Wrong Y displacement ",
                "5.0",
                pp.getDisplacement().getDisplacementY().evaluate(feature).toString());
        Assert.assertEquals(
                "Wrong Rotation ", "90.0", pp.getRotation().evaluate(feature).toString());
    }

    /** Test of createHalo method, of class org.geotools.styling.StyleFactoryImpl. */
    @Test
    public void testCreateHalo() {
        LOGGER.finer("testCreateHalo");

        HaloImpl h =
                (HaloImpl)
                        styleFactory.createHalo(
                                styleFactory.getDefaultFill(), filterFactory.literal(4));

        Assert.assertNotNull("Failed to build halo", h);

        Assert.assertEquals(
                "Wrong radius", 4, ((Number) h.getRadius().evaluate(feature)).intValue());
    }

    @Test
    public void testBuggyStyleCopy() throws Exception {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Random rand = new Random();

        StrokeImpl stroke =
                sf.createStroke(ff.literal("#8024d0"), ff.literal(rand.nextInt(10) + 1));
        stroke.setOpacity(ff.literal(rand.nextFloat()));

        LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) sf.createLineSymbolizer(stroke, ".");

        RuleImpl rule = (RuleImpl) sf.createRule();
        rule.symbolizers().add(lineSymb);
        rule.setFilter(Filter.INCLUDE);
        rule.setMaxScaleDenominator(20000);

        FeatureTypeStyleImpl style = sf.createFeatureTypeStyle();
        style.rules().add(rule);
        style.featureTypeNames().add(new NameImpl("Feature"));

        StyleImpl namedStyle = sf.createStyle();
        namedStyle.featureTypeStyles().add(style);
        namedStyle.setName("Feature");

        DuplicatingStyleVisitor duplicator = new DuplicatingStyleVisitor();
        namedStyle.accept(duplicator);
        SLDTransformer writer = new SLDTransformer();
        String out = writer.transform(style);

        Assert.assertNotNull(out);
    }

    /**
     * Test comparing the behaviors of styleFactory.createFill() with a null color and the DEFAULT
     * fill.
     */
    @Test
    public void testCreateFillVsDefaultFill() {
        FillImpl f1 = (FillImpl) styleFactory.createFill(null);
        FillImpl f2 = (FillImpl) styleFactory.getDefaultFill();
        Assert.assertEquals(f1, f2);
    }

    @Test
    public void testRasterSymbolizerNoGeometry() {
        RasterSymbolizerImpl rs = (RasterSymbolizerImpl) styleFactory.getDefaultRasterSymbolizer();
        Assert.assertNull(rs.getGeometryPropertyName());
        Assert.assertNull(rs.getGeometry());
    }

    // Test Expression usage in SelectedChannel for styleFactory
    @Test
    public void testSelectedChannelExpression() {
        SelectedChannelTypeImpl sct =
                styleFactory.selectedChannelType(
                        filterFactory.function(
                                "env", filterFactory.literal("B1"), filterFactory.literal("1")),
                        null);
        final String b1 = "B1";
        EnvFunction.removeLocalValue("B1");
        Assert.assertEquals(1, sct.getChannelName().evaluate(null, Integer.class).intValue());
        EnvFunction.setLocalValue(b1, "20");
        Assert.assertEquals(20, sct.getChannelName().evaluate(null, Integer.class).intValue());
        EnvFunction.removeLocalValue("B1");
    }
}
