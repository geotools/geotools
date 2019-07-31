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
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.function.EnvFunction;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.Halo;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**
 * Capture the default values produce by the style factory in order to capture any regressions as we
 * move forward to SE 1.1 interfaces.
 *
 * @author iant
 */
public class StyleFactoryImplTest extends TestCase {
    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
    static SimpleFeature feature;
    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(StyleFactoryImplTest.class);

    public void testCommonFactoryFinder() {
        LOGGER.finer("testCreateStyle");

        assertNotNull("Failed to build styleFactory", styleFactory);
    }

    /** Test of createPointSymbolizer method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreatePointSymbolizer() {
        LOGGER.finer("testCreatePointSymbolizer");

        PointSymbolizer ps = styleFactory.createPointSymbolizer();

        assertNotNull("Failed to create PointSymbolizer", ps);
    }

    /** Test of createPolygonSymbolizer method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreatePolygonSymbolizer() {
        LOGGER.finer("testCreatePolygonSymbolizer");

        PolygonSymbolizer ps = styleFactory.createPolygonSymbolizer();

        assertNotNull("Failed to create PolygonSymbolizer", ps);
    }

    /** Test of createLineSymbolizer method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateLineSymbolizer() {
        LOGGER.finer("testCreateLineSymbolizer");

        LineSymbolizer ls = styleFactory.createLineSymbolizer();

        assertNotNull("Failed to create PolygonSymbolizer", ls);
    }

    /** Test of createTextSymbolizer method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateTextSymbolizer() {
        LOGGER.finer("testCreateTextSymbolizer");

        TextSymbolizer ts = styleFactory.createTextSymbolizer();

        assertNotNull("Failed to create TextSymbolizer", ts);
    }

    /** Test of createFeatureTypeStyle method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateFeatureTypeStyle() {
        LOGGER.finer("testCreateFeatureTypeStyle");

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();

        assertNotNull("failed to create featureTypeStyle", fts);
    }

    /** Test of createRule method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateRule() {
        LOGGER.finer("testCreateRule");

        Rule r = styleFactory.createRule();

        assertNotNull("failed to create Rule", r);
    }

    /** Test of createStroke method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateStroke() {
        LOGGER.finer("testCreateStroke");

        Stroke s =
                styleFactory.createStroke(
                        filterFactory.literal("#000000"), filterFactory.literal(2.0));

        assertNotNull("Failed to build stroke ", s);

        s =
                styleFactory.createStroke(
                        filterFactory.literal("#000000"),
                        filterFactory.literal(2.0),
                        filterFactory.literal(0.5));

        assertNotNull("Failed to build stroke ", s);

        s =
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

        assertNotNull("Failed to build stroke ", s);

        assertEquals("Wrong color ", "#000000", s.getColor().evaluate(feature).toString());
        assertEquals("Wrong width ", "2.0", s.getWidth().evaluate(feature).toString());
        assertEquals("Wrong opacity ", "0.5", s.getOpacity().evaluate(feature).toString());
        assertEquals("Wrong linejoin ", "bevel", s.getLineJoin().evaluate(feature).toString());
        assertEquals("Wrong linejoin ", "square", s.getLineCap().evaluate(feature).toString());
        assertEquals("Broken dash array", 2.1f, s.getDashArray()[1], 0.001f);
        assertEquals("Wrong dash offset ", "3", s.getDashOffset().evaluate(feature).toString());
    }

    /** Test of createFill method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateFill() {
        LOGGER.finer("testCreateFill");

        Fill f = styleFactory.createFill(filterFactory.literal("#808080"));

        assertNotNull("Failed to build fill", f);

        f = styleFactory.createFill(filterFactory.literal("#808080"), filterFactory.literal(1.0));
        assertNotNull("Failed to build fill", f);

        f = styleFactory.createFill(null);
        assertEquals(f.getColor(), Fill.DEFAULT.getColor());
        assertSame(f.getColor(), Fill.DEFAULT.getColor());
    }

    /** Test of createMark method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateMark() {
        LOGGER.finer("testCreateMark");

        Mark m = styleFactory.createMark();

        assertNotNull("Failed to build mark ", m);
    }

    /** Test of getSquareMark method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testGetNamedMarks() {
        LOGGER.finer("testGetNamedMarks");

        Mark m;
        String[] names = {"Square", "Circle", "Triangle", "Star", "X", "Cross"};

        for (int i = 0; i < names.length; i++) {
            try {
                Class target = styleFactory.getClass();

                //                LOGGER.finer("About to load get"+names[i]+"Mark");
                Method method = target.getMethod("get" + names[i] + "Mark", (Class[]) null);

                //                LOGGER.finer("got method back " + method.toString());
                m = (Mark) method.invoke(styleFactory, (Object[]) null);
                assertNotNull("Failed to get " + names[i] + " mark ", m);

                Expression exp = filterFactory.literal(names[i]);
                assertEquals("Wrong sort of mark returned ", exp, m.getWellKnownName());
            } catch (InvocationTargetException ite) {
                ite.getTargetException().printStackTrace();
                fail("InvocationTargetException " + ite.getTargetException());
            } catch (Exception e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                fail("Exception " + e.toString());
            }
        }
    }

    /** Test of createGraphic method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateGraphic() {
        LOGGER.finer("testCreateGraphic");

        ExternalGraphic[] externalGraphics =
                new ExternalGraphic[] {
                    styleFactory.createExternalGraphic(
                            "http://www.ccg.leeds.ac.uk/ian/geotools/icons/rail.gif", "image/gif")
                };
        Mark[] marks = new Mark[] {styleFactory.getCircleMark()};
        Mark[] symbols = new Mark[0];
        Expression opacity = filterFactory.literal(0.5);
        Expression size = filterFactory.literal(10);
        Expression rotation = filterFactory.literal(145.0);
        Graphic g =
                styleFactory.createGraphic(
                        externalGraphics, marks, symbols, opacity, size, rotation);

        assertNotNull("failed to build graphic ", g);
    }

    /** Test of createFont method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateFont() {
        LOGGER.finer("testCreateFont");

        Expression fontFamily = filterFactory.literal("Times");
        Expression fontStyle = filterFactory.literal("Italic");
        Expression fontWeight = filterFactory.literal("Bold");
        Expression fontSize = filterFactory.literal("12");
        Font f = styleFactory.createFont(fontFamily, fontStyle, fontWeight, fontSize);

        assertNotNull("Failed to build font", f);

        assertEquals(
                "Wrong font type ", "Times", f.getFamily().get(0).evaluate(feature).toString());
        assertEquals("Wrong font Style ", "Italic", f.getStyle().evaluate(feature).toString());
        assertEquals("Wrong font weight ", "Bold", f.getWeight().evaluate(feature).toString());
        assertEquals("Wrong font size ", "12", f.getSize().evaluate(feature).toString());
    }

    /** Test of createLinePlacement method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateLinePlacement() {
        LOGGER.finer("testCreateLinePlacement");

        LinePlacement lp = styleFactory.createLinePlacement(filterFactory.literal(10));

        assertNotNull("failed to create LinePlacement", lp);
    }

    /** Test of createPointPlacement method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreatePointPlacement() {
        LOGGER.finer("testCreatePointPlacement");

        AnchorPoint anchorPoint =
                styleFactory.createAnchorPoint(
                        filterFactory.literal(1.0), filterFactory.literal(0.5));
        Displacement displacement =
                styleFactory.createDisplacement(
                        filterFactory.literal(10.0), filterFactory.literal(5.0));
        Expression rotation = filterFactory.literal(90.0);
        PointPlacement pp = styleFactory.createPointPlacement(anchorPoint, displacement, rotation);

        assertNotNull("failed to create PointPlacement", pp);

        assertEquals(
                "Wrong X anchorPoint ",
                "1.0",
                pp.getAnchorPoint().getAnchorPointX().evaluate(feature).toString());
        assertEquals(
                "Wrong Y anchorPoint ",
                "0.5",
                pp.getAnchorPoint().getAnchorPointY().evaluate(feature).toString());
        assertEquals(
                "Wrong X displacement ",
                "10.0",
                pp.getDisplacement().getDisplacementX().evaluate(feature).toString());
        assertEquals(
                "Wrong Y displacement ",
                "5.0",
                pp.getDisplacement().getDisplacementY().evaluate(feature).toString());
        assertEquals("Wrong Rotation ", "90.0", pp.getRotation().evaluate(feature).toString());
    }

    /** Test of createHalo method, of class org.geotools.styling.StyleFactoryImpl. */
    public void testCreateHalo() {
        LOGGER.finer("testCreateHalo");

        Halo h = styleFactory.createHalo(styleFactory.getDefaultFill(), filterFactory.literal(4));

        assertNotNull("Failed to build halo", h);

        assertEquals("Wrong radius", 4, ((Number) h.getRadius().evaluate(feature)).intValue());
    }

    public void testBuggyStyleCopy() throws Exception {
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Random rand = new Random();

        Stroke stroke = sf.createStroke(ff.literal("#8024d0"), ff.literal(rand.nextInt(10) + 1));
        stroke.setOpacity(ff.literal(rand.nextFloat()));

        LineSymbolizer lineSymb = sf.createLineSymbolizer(stroke, ".");

        Rule rule = sf.createRule();
        rule.symbolizers().add(lineSymb);
        rule.setFilter(Filter.INCLUDE);
        rule.setMaxScaleDenominator(20000);

        FeatureTypeStyle style = sf.createFeatureTypeStyle();
        style.rules().add(rule);
        style.featureTypeNames().add(new NameImpl("Feature"));

        Style namedStyle = sf.createStyle();
        namedStyle.featureTypeStyles().add(style);
        namedStyle.setName("Feature");

        DuplicatingStyleVisitor duplicator = new DuplicatingStyleVisitor();
        namedStyle.accept(duplicator);
        Style namedStyle2 = (Style) duplicator.getCopy();

        SLDTransformer writer = new SLDTransformer();
        String out = writer.transform(style);

        assertNotNull(out);
    }

    /**
     * Test comparing the behaviors of styleFactory.createFill() with a null color and the DEFAULT
     * fill.
     */
    public void testCreateFillVsDefaultFill() {
        Fill f1 = styleFactory.createFill(null);
        Fill f2 = styleFactory.getDefaultFill();
        assertEquals(f1, f2);
    }

    public void testRasterSymbolizerNoGeometry() {
        RasterSymbolizer rs = styleFactory.getDefaultRasterSymbolizer();
        assertNull(rs.getGeometryPropertyName());
        assertNull(rs.getGeometry());
    }

    // Test Expression usage in SelectedChannel for styleFactory
    @Test
    public void testSelectedChannelExpression() {
        SelectedChannelType sct =
                styleFactory.selectedChannelType(
                        filterFactory.function(
                                "env", filterFactory.literal("B1"), filterFactory.literal("1")),
                        null);
        final String b1 = "B1";
        EnvFunction.removeLocalValue("B1");
        assertEquals(1, sct.getChannelName().evaluate(null, Integer.class).intValue());
        EnvFunction.setLocalValue(b1, "20");
        assertEquals(20, sct.getChannelName().evaluate(null, Integer.class).intValue());
        EnvFunction.removeLocalValue("B1");
    }
}
