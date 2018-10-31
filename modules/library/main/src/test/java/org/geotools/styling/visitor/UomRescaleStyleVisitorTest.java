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
package org.geotools.styling.visitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.util.Arrays;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.measure.Units;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.Mark;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.SLD;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizerImpl;
import org.geotools.util.Converters;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import si.uom.SI;
import systems.uom.common.USCustomary;

/** @author milton */
public class UomRescaleStyleVisitorTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void testConstructorOK() {
        try {
            new UomRescaleStyleVisitor(1);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    @Test
    public void testConstructorFail() {
        try {
            new UomRescaleStyleVisitor(-1);
        } catch (IllegalArgumentException e) {
            return;
        }

        fail("Should throw IllegalArgumentException.");
    }

    protected double computeExpectedRescaleSize(
            double size, double scaleMetersToPixel, Unit<Length> uom) {
        double expectedRescaledSize = size;

        // uom == null means pixels (rescalesize == size)
        if (uom != null) {
            double scaleUomToMeters = 1;
            if (uom.equals(USCustomary.FOOT)) scaleUomToMeters *= 0.3048006096012;
            if (!uom.equals(Units.PIXEL))
                expectedRescaledSize *= scaleUomToMeters * scaleMetersToPixel;
        }

        return expectedRescaledSize;
    }

    protected void visitPointSymbolizerTest(double scaleMetersToPixel, Unit<Length> uom) {
        try {
            UomRescaleStyleVisitor visitor = null;
            double size = 100;
            double expectedRescaledSize = computeExpectedRescaleSize(size, scaleMetersToPixel, uom);

            StyleBuilder styleBuilder = new StyleBuilder();

            PointSymbolizerImpl pointSymb =
                    (PointSymbolizerImpl) styleBuilder.createPointSymbolizer();
            pointSymb.setUnitOfMeasure(uom);

            FilterFactory2 filterFactory = new FilterFactoryImpl();
            pointSymb.getGraphic().setSize(filterFactory.literal(size));

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            pointSymb.accept(visitor);
            PointSymbolizer rescaledPointSymb = (PointSymbolizer) visitor.getCopy();
            double rescaledSize =
                    rescaledPointSymb.getGraphic().getSize().evaluate(null, Double.class);

            assertEquals(Math.round(expectedRescaledSize), Math.round(rescaledSize));
            assertNotSame(rescaledPointSymb, pointSymb);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    protected void visitLineSymbolizerTest(double scaleMetersToPixel, Unit<Length> uom) {
        try {
            UomRescaleStyleVisitor visitor = null;
            double size = 100;
            double expectedRescaledSize = computeExpectedRescaleSize(size, scaleMetersToPixel, uom);

            StyleBuilder styleBuilder = new StyleBuilder();

            LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) styleBuilder.createLineSymbolizer();
            lineSymb.setUnitOfMeasure(uom);

            FilterFactory2 filterFactory = new FilterFactoryImpl();
            lineSymb.getStroke().setWidth(filterFactory.literal(size));
            lineSymb.setPerpendicularOffset(filterFactory.literal(size));

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            lineSymb.accept(visitor);
            LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();
            double rescaledSize =
                    rescaledLineSymb.getStroke().getWidth().evaluate(null, Double.class);

            assertEquals(Math.round(expectedRescaledSize), Math.round(rescaledSize));
            assertNotSame(rescaledLineSymb, lineSymb);
            assertEquals(
                    expectedRescaledSize,
                    rescaledLineSymb.getPerpendicularOffset().evaluate(null, Double.class),
                    0.1d);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    protected void visitPolygonSymbolizerTest(double scaleMetersToPixel, Unit<Length> uom) {
        try {
            UomRescaleStyleVisitor visitor = null;
            double size = 100;
            double margin = 15;
            double expectedRescaledSize = computeExpectedRescaleSize(size, scaleMetersToPixel, uom);
            int expectedGraphicMargin =
                    (int) computeExpectedRescaleSize(15, scaleMetersToPixel, uom);

            StyleBuilder styleBuilder = new StyleBuilder();

            PolygonSymbolizerImpl polySymb =
                    (PolygonSymbolizerImpl) styleBuilder.createPolygonSymbolizer();
            polySymb.setUnitOfMeasure(uom);

            FilterFactory2 filterFactory = new FilterFactoryImpl();
            polySymb.getStroke().setWidth(filterFactory.literal(size));
            polySymb.getOptions().put(PolygonSymbolizer.GRAPHIC_MARGIN_KEY, "15");

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            polySymb.accept(visitor);
            PolygonSymbolizer rescaledPolySymb = (PolygonSymbolizer) visitor.getCopy();
            double rescaledSize =
                    rescaledPolySymb.getStroke().getWidth().evaluate(null, Double.class);

            assertEquals(Math.round(expectedRescaledSize), Math.round(rescaledSize));
            assertNotSame(rescaledPolySymb, polySymb);

            String[] splitted =
                    rescaledPolySymb
                            .getOptions()
                            .get(TextSymbolizer.GRAPHIC_MARGIN_KEY)
                            .split("\\s+");
            int rescaledGraphicMargin = Converters.convert(splitted[0], Integer.class).intValue();
            assertEquals(expectedGraphicMargin, rescaledGraphicMargin);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    protected void visitTextSymbolizerTest(double scaleMetersToPixel, Unit<Length> uom) {
        try {
            UomRescaleStyleVisitor visitor = null;
            int fontSize = 100;
            double displacementX = 13;
            double displacementY = 17;
            int maxDisplacement = 10;
            double expectedRescaledFontSize =
                    computeExpectedRescaleSize(fontSize, scaleMetersToPixel, uom);
            double expectedRescaledDisplacementXSize =
                    computeExpectedRescaleSize(displacementX, scaleMetersToPixel, uom);
            double expectedRescaledDisplacementYSize =
                    computeExpectedRescaleSize(displacementY, scaleMetersToPixel, uom);
            int expectedMaxDisplacement =
                    (int) computeExpectedRescaleSize(maxDisplacement, scaleMetersToPixel, uom);
            int expectedGraphicMargin1 =
                    (int) computeExpectedRescaleSize(maxDisplacement, scaleMetersToPixel, uom);
            int expectedGraphicMargin2 =
                    (int) computeExpectedRescaleSize(maxDisplacement * 2, scaleMetersToPixel, uom);

            StyleBuilder styleBuilder = new StyleBuilder();

            TextSymbolizerImpl textSymb = (TextSymbolizerImpl) styleBuilder.createTextSymbolizer();
            textSymb.setUnitOfMeasure(uom);

            Font font =
                    styleBuilder.createFont(
                            new java.awt.Font("Verdana", java.awt.Font.PLAIN, fontSize));
            textSymb.setFont(font);

            PointPlacement placement =
                    styleBuilder.createPointPlacement(0.3, 0.3, displacementX, displacementY, 10);
            textSymb.setLabelPlacement(placement);

            // check we can rescale properly also vendor options
            textSymb.getOptions().put("maxDisplacement", String.valueOf(maxDisplacement));
            textSymb.getOptions()
                    .put(
                            TextSymbolizer.GRAPHIC_MARGIN_KEY,
                            maxDisplacement + " " + maxDisplacement * 2);

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            textSymb.accept(visitor);
            TextSymbolizer rescaledTextSymb = (TextSymbolizer) visitor.getCopy();

            double rescaledFontSize =
                    rescaledTextSymb.getFont().getSize().evaluate(null, Double.class);
            PointPlacement rescaledPlacement =
                    (PointPlacement) rescaledTextSymb.getLabelPlacement();
            double rescaledDisplacementXSize =
                    rescaledPlacement
                            .getDisplacement()
                            .getDisplacementX()
                            .evaluate(null, Double.class);
            double rescaledDisplacementYSize =
                    rescaledPlacement
                            .getDisplacement()
                            .getDisplacementY()
                            .evaluate(null, Double.class);

            assertEquals(Math.round(expectedRescaledFontSize), Math.round(rescaledFontSize));
            assertEquals(
                    Math.round(expectedRescaledDisplacementXSize),
                    Math.round(rescaledDisplacementXSize));
            assertEquals(
                    Math.round(expectedRescaledDisplacementYSize),
                    Math.round(rescaledDisplacementYSize));
            assertNotSame(rescaledTextSymb, textSymb);

            Map<String, String> options = rescaledTextSymb.getOptions();
            int rescaledMaxDisplacement =
                    Converters.convert(options.get("maxDisplacement"), Integer.class).intValue();
            assertEquals(rescaledMaxDisplacement, expectedMaxDisplacement);

            String[] splitted = options.get(TextSymbolizer.GRAPHIC_MARGIN_KEY).split("\\s+");
            int rescaledGraphicMargin1 = Converters.convert(splitted[0], Integer.class).intValue();
            int rescaledGraphicMargin2 = Converters.convert(splitted[1], Integer.class).intValue();
            assertEquals(expectedGraphicMargin1, rescaledGraphicMargin1);
            assertEquals(expectedGraphicMargin2, rescaledGraphicMargin2);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    protected void visitLineSymbolizerTestDynamicDashArray(
            double scaleMetersToPixel, Unit<Length> uom) {
        try {
            UomRescaleStyleVisitor visitor = null;
            double size = 1;
            double expectedRescaledSize =
                    Math.floor(computeExpectedRescaleSize(size, scaleMetersToPixel, uom) * 10000.0)
                            / 10000.0;
            FilterFactory2 filterFactory = new FilterFactoryImpl();
            Expression func =
                    filterFactory.function(
                            "listMultiply",
                            filterFactory.literal(expectedRescaledSize),
                            filterFactory.literal("5 10"));
            String expectedDashArray = (String) func.evaluate(null);

            StyleBuilder styleBuilder = new StyleBuilder();

            LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) styleBuilder.createLineSymbolizer();
            lineSymb.setUnitOfMeasure(uom);
            lineSymb.getStroke().setDashArray(Arrays.asList(filterFactory.literal("5.0 10.0")));

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            lineSymb.accept(visitor);
            LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();
            String rescaledDynamicDashArray =
                    (String)
                            ((Expression) rescaledLineSymb.getStroke().dashArray().get(0))
                                    .evaluate(null);

            assertEquals(expectedDashArray, rescaledDynamicDashArray);
            assertNotSame(rescaledLineSymb, lineSymb);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    // POINT SYMBOLIZER TESTS

    @Test
    public void testVisitPointSymbolizer_ScalePixelNull() {
        visitPointSymbolizerTest(10, null);
    }

    @Test
    public void testVisitPointSymbolizer_ScalePixelExplicit() {
        visitPointSymbolizerTest(10, Units.PIXEL);
    }

    @Test
    public void testVisitPointSymbolizer_ScaleMeter1() {
        visitPointSymbolizerTest(1, SI.METRE);
    }

    @Test
    public void testVisitPointSymbolizer_ScaleMeter10() {
        visitPointSymbolizerTest(10, SI.METRE);
    }

    @Test
    public void testVisitPointSymbolizer_ScaleFoot1() {
        visitPointSymbolizerTest(1, USCustomary.FOOT);
    }

    @Test
    public void testVisitPointSymbolizer_ScaleFoot10() {
        visitPointSymbolizerTest(10, USCustomary.FOOT);
    }

    // LINE SYMBOLIZER TESTS
    @Test
    public void testVisitLineSymbolizer_ScalePixelNull() {
        visitLineSymbolizerTest(10, null);
    }

    @Test
    public void testVisitLineSymbolizer_ScalePixelExplicit() {
        visitLineSymbolizerTest(10, Units.PIXEL);
    }

    @Test
    public void testVisitLineSymbolizer_ScaleMeter1() {
        visitLineSymbolizerTest(1, SI.METRE);
    }

    @Test
    public void testVisitLineSymbolizer_ScaleMeter10() {
        visitLineSymbolizerTest(10, SI.METRE);
    }

    @Test
    public void testVisitLineSymbolizer_ScaleFoot1() {
        visitLineSymbolizerTest(1, USCustomary.FOOT);
    }

    @Test
    public void testVisitLineSymbolizer_ScaleFoot10() {
        visitLineSymbolizerTest(10, USCustomary.FOOT);
    }

    // LINE SYMBOLIZER TESTS with dynamic dash arrays
    public void testVisitLineSymbolizerDynamicDashArray_ScalePixelNull() {
        visitLineSymbolizerTestDynamicDashArray(10, null);
    }

    public void testVisitLineSymbolizerDynamicDashArray_ScalePixelExplicit() {
        visitLineSymbolizerTestDynamicDashArray(10, Units.PIXEL);
    }

    public void testVisitLineSymbolizerDynamicDashArray_ScaleMeter1() {
        visitLineSymbolizerTestDynamicDashArray(1, SI.METRE);
    }

    public void testVisitLineSymbolizerDynamicDashArray_ScaleMeter10() {
        visitLineSymbolizerTestDynamicDashArray(10, SI.METRE);
    }

    public void testVisitLineSymbolizerDynamicDashArray_ScaleFoot1() {
        visitLineSymbolizerTestDynamicDashArray(1, USCustomary.FOOT);
    }

    public void testVisitLineSymbolizerDynamicDashArray_ScaleFoot10() {
        visitLineSymbolizerTestDynamicDashArray(10, USCustomary.FOOT);
    }

    // POLYGON SYMBOLIZER TESTS
    @Test
    public void testVisitPolygonSymbolizer_ScalePixelNull() {
        visitPolygonSymbolizerTest(10, null);
    }

    @Test
    public void testVisitPolygonSymbolizer_ScalePixelExplicit() {
        visitPolygonSymbolizerTest(10, Units.PIXEL);
    }

    @Test
    public void testVisitPolygonSymbolizer_ScaleMeter1() {
        visitPolygonSymbolizerTest(1, SI.METRE);
    }

    @Test
    public void testVisitPolygonSymbolizer_ScaleMeter10() {
        visitPolygonSymbolizerTest(10, SI.METRE);
    }

    @Test
    public void testVisitPolygonSymbolizer_ScaleFoot1() {
        visitPolygonSymbolizerTest(1, USCustomary.FOOT);
    }

    @Test
    public void testVisitPolygonSymbolizer_ScaleFoot10() {
        visitPolygonSymbolizerTest(10, USCustomary.FOOT);
    }

    // TEXT SYMBOLIZER TESTS
    @Test
    public void testVisitTextSymbolizer_ScalePixelNull() {
        visitTextSymbolizerTest(10, null);
    }

    @Test
    public void testVisitTextSymbolizer_ScalePixelExplicit() {
        visitTextSymbolizerTest(10, Units.PIXEL);
    }

    @Test
    public void testVisitTextSymbolizer_ScaleMeter1() {
        visitTextSymbolizerTest(1, SI.METRE);
    }

    @Test
    public void testVisitTextSymbolizer_ScaleMeter10() {
        visitTextSymbolizerTest(10, SI.METRE);
    }

    @Test
    public void testVisitTextSymbolizer_ScaleFoot1() {
        visitTextSymbolizerTest(1, USCustomary.FOOT);
    }

    @Test
    public void testVisitTextSymbolizer_ScaleFoot10() {
        visitTextSymbolizerTest(10, USCustomary.FOOT);
    }

    // EXTRA TESTS
    @Test
    public void testVisitLineSymbolizer_NullStroke() {
        try {
            UomRescaleStyleVisitor visitor = null;

            StyleBuilder styleBuilder = new StyleBuilder();

            Stroke stroke = null;
            LineSymbolizerImpl lineSymb =
                    (LineSymbolizerImpl) styleBuilder.createLineSymbolizer(stroke);
            lineSymb.setUnitOfMeasure(SI.METRE);

            visitor = new UomRescaleStyleVisitor(10);

            lineSymb.accept(visitor);
            LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();

            assertNull(rescaledLineSymb.getStroke());
            assertNotSame(rescaledLineSymb, lineSymb);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    @Test
    public void testVisitPolygonSymbolizer_NullStroke() {
        try {
            UomRescaleStyleVisitor visitor = null;

            StyleBuilder styleBuilder = new StyleBuilder();

            Fill fill = styleBuilder.createFill(Color.RED);
            PolygonSymbolizerImpl polySymb =
                    (PolygonSymbolizerImpl) styleBuilder.createPolygonSymbolizer(null, fill);

            visitor = new UomRescaleStyleVisitor(10);

            polySymb.accept(visitor);
            PolygonSymbolizer rescaledPolySymb = (PolygonSymbolizer) visitor.getCopy();

            assertEquals(polySymb.getFill(), rescaledPolySymb.getFill());
            assertNull(rescaledPolySymb.getStroke());
            assertNotSame(rescaledPolySymb, polySymb);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    @Test
    public void testVisitTextSymbolizer_LinePlacement() {
        try {
            UomRescaleStyleVisitor visitor = null;

            Unit<Length> uom = SI.METRE;
            int fontSize = 100;
            double perpOffset = 13;
            double gap = 7;
            double initialGap = 5;
            double scaleMetersToPixel = 17;
            double expectedRescaledFontSize =
                    computeExpectedRescaleSize(fontSize, scaleMetersToPixel, uom);
            double expectedRescaledPerpOffset =
                    computeExpectedRescaleSize(perpOffset, scaleMetersToPixel, uom);
            double expectedRescaledGap = computeExpectedRescaleSize(gap, scaleMetersToPixel, uom);
            double expectedRescaledInitialGap =
                    computeExpectedRescaleSize(initialGap, scaleMetersToPixel, uom);

            StyleBuilder styleBuilder = new StyleBuilder();

            TextSymbolizerImpl textSymb = (TextSymbolizerImpl) styleBuilder.createTextSymbolizer();
            textSymb.setUnitOfMeasure(uom);

            Font font =
                    styleBuilder.createFont(
                            new java.awt.Font("Verdana", java.awt.Font.PLAIN, fontSize));
            textSymb.setFont(font);

            LinePlacement placement = styleBuilder.createLinePlacement(perpOffset);
            placement.setGap(styleBuilder.literalExpression(gap));
            placement.setInitialGap(styleBuilder.literalExpression(initialGap));

            textSymb.setLabelPlacement(placement);

            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            textSymb.accept(visitor);
            TextSymbolizer rescaledTextSymb = (TextSymbolizer) visitor.getCopy();

            double rescaledFontSize =
                    rescaledTextSymb.getFont().getSize().evaluate(null, Double.class);
            LinePlacement rescaledPlacement = (LinePlacement) rescaledTextSymb.getLabelPlacement();
            double rescaledPerpOffset =
                    rescaledPlacement.getPerpendicularOffset().evaluate(null, Double.class);
            double rescaledGap = rescaledPlacement.getGap().evaluate(null, Double.class);
            double rescaledInitialGap =
                    rescaledPlacement.getInitialGap().evaluate(null, Double.class);

            assertEquals(Math.round(expectedRescaledFontSize), Math.round(rescaledFontSize));
            assertEquals(Math.round(expectedRescaledPerpOffset), Math.round(rescaledPerpOffset));
            assertEquals(Math.round(expectedRescaledGap), Math.round(rescaledGap));
            assertEquals(Math.round(expectedRescaledInitialGap), Math.round(rescaledInitialGap));
            assertNotSame(rescaledTextSymb, textSymb);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    @Test
    public void testVisitLineSymbolizer_ExpressionWithFeatureAttribute() {
        try {
            double widthValue = 13;
            double scaleMetersToPixel = 17;
            Unit<Length> uom = SI.METRE;

            StyleBuilder styleBuilder = new StyleBuilder();
            UomRescaleStyleVisitor visitor = null;

            // creates the feature used for the test
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            featureTypeBuilder.setName("TestType");
            featureTypeBuilder.add("geom", LineString.class, DefaultGeographicCRS.WGS84);
            featureTypeBuilder.add("width", Double.class);
            SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();

            GeometryFactory geomFactory = new GeometryFactory();
            Geometry geom =
                    geomFactory.createLineString(
                            new Coordinate[] {new Coordinate(1, 1), new Coordinate(2, 2)});

            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            featureBuilder.set("geom", geom);
            featureBuilder.set("width", widthValue);
            SimpleFeature feature = featureBuilder.buildFeature("1");

            // creates the symbolizer for the test
            Expression color = styleBuilder.colorExpression(Color.RED);
            Expression width = styleBuilder.attributeExpression("width");
            Stroke stroke = styleBuilder.createStroke(color, width);
            LineSymbolizerImpl lineSymb =
                    (LineSymbolizerImpl) styleBuilder.createLineSymbolizer(stroke);
            lineSymb.setUnitOfMeasure(uom);

            // rescales symbolizer
            visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

            lineSymb.accept(visitor);
            LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();

            // tests results
            org.opengis.style.Stroke rescaledStroke = rescaledLineSymb.getStroke();
            Expression rescaledWidth = rescaledStroke.getWidth();
            double rescaledWidthValue = rescaledWidth.evaluate(feature, Double.class);
            double expectedRescaledWidthValue =
                    computeExpectedRescaleSize(widthValue, scaleMetersToPixel, uom);

            assertEquals(stroke.getColor(), rescaledStroke.getColor());
            assertEquals(expectedRescaledWidthValue, rescaledWidthValue, 0d);
            assertNotSame(SI.METRE, rescaledLineSymb.getUnitOfMeasure());
            assertNotSame(rescaledLineSymb, lineSymb);
        } catch (Exception e2) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e2);
            fail(e2.getClass().getSimpleName() + " should not be thrown.");
        }
    }

    @Test
    public void testRescaleGraphicFillStrokes() {
        // create a graphic that needs rescaling
        StyleBuilder sb = new StyleBuilder();

        // a graphic stroke
        Stroke stroke = sb.createStroke();
        stroke.setColor(null);
        stroke.setGraphicStroke(
                sb.createGraphic(null, sb.createMark("square", null, sb.createStroke(1)), null));

        // a graphic fill
        Fill fill = sb.createFill();
        fill.setColor(null);
        fill.setGraphicFill(
                sb.createGraphic(null, sb.createMark("square", null, sb.createStroke(2)), null));

        // a polygon and line symbolizer using them
        PolygonSymbolizer ps = sb.createPolygonSymbolizer(stroke, fill);
        ps.setUnitOfMeasure(SI.METRE);

        // rescale it
        UomRescaleStyleVisitor visitor = new UomRescaleStyleVisitor(10);
        ps.accept(visitor);
        PolygonSymbolizer rps = (PolygonSymbolizer) visitor.getCopy();
        Mark rm = (Mark) rps.getStroke().getGraphicStroke().graphicalSymbols().get(0);
        assertEquals(10.0, rm.getStroke().getWidth().evaluate(null, Double.class), 0d);
        rm = (Mark) rps.getFill().getGraphicFill().graphicalSymbols().get(0);
        assertEquals(20.0, rm.getStroke().getWidth().evaluate(null, Double.class), 0d);

        // a line symbolizer that uses a graphic stroke
        LineSymbolizer ls = sb.createLineSymbolizer(stroke);
        ls.setUnitOfMeasure(SI.METRE);

        // rescale it
        ls.accept(visitor);
        LineSymbolizer lps = (LineSymbolizer) visitor.getCopy();
        rm = (Mark) lps.getStroke().getGraphicStroke().graphicalSymbols().get(0);
        assertEquals(10.0, rm.getStroke().getWidth().evaluate(null, Double.class), 0d);
    }

    @Test
    public void testRescaleGraphicPointSymbolizer() {
        // create a graphic that needs rescaling
        StyleBuilder sb = new StyleBuilder();

        // create a circle
        Mark circle = sb.createMark("circle", null, sb.createStroke(500));
        Graphic g = sb.createGraphic(null, circle, null);

        // a point symbolizer with the specified circle
        PointSymbolizer ps = sb.createPointSymbolizer(g);

        // first see it in feet
        ps.setUnitOfMeasure(USCustomary.FOOT);

        // rescale it
        UomRescaleStyleVisitor visitor = new UomRescaleStyleVisitor(10);
        ps.accept(visitor);
        PointSymbolizer rps = (PointSymbolizer) visitor.getCopy();
        Mark rm = (Mark) rps.getGraphic().graphicalSymbols().get(0);
        assertEquals(1524.0, rm.getStroke().getWidth().evaluate(null, Double.class), 0d);

        // now let's see the same in meters
        ps.setUnitOfMeasure(SI.METRE);

        // rescale it
        visitor = new UomRescaleStyleVisitor(10);
        ps.accept(visitor);
        rps = (PointSymbolizer) visitor.getCopy();
        rm = (Mark) rps.getGraphic().graphicalSymbols().get(0);
        assertEquals(5000.0, rm.getStroke().getWidth().evaluate(null, Double.class), 0d);
    }

    @Test
    public void visitLocalUomMeters() {
        UomRescaleStyleVisitor visitor = null;
        double size = 100;
        double scaleMetersToPixel = 10;
        double expectedRescaledSize =
                computeExpectedRescaleSize(size, scaleMetersToPixel, SI.METRE);

        StyleBuilder styleBuilder = new StyleBuilder();
        LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) styleBuilder.createLineSymbolizer();
        lineSymb.getStroke().setWidth(ff.literal(size + "m"));

        visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

        lineSymb.accept(visitor);
        LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();
        double rescaledSize = rescaledLineSymb.getStroke().getWidth().evaluate(null, Double.class);

        assertEquals(Math.round(expectedRescaledSize), Math.round(rescaledSize));
        assertNotSame(rescaledLineSymb, lineSymb);
    }

    @Test
    public void visitLocalUomOverrideFeet() {
        UomRescaleStyleVisitor visitor = null;
        double size = 100;
        double scaleMetersToPixel = 10;
        double expectedRescaledSize =
                computeExpectedRescaleSize(size, scaleMetersToPixel, SI.METRE);

        StyleBuilder styleBuilder = new StyleBuilder();
        LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) styleBuilder.createLineSymbolizer();
        lineSymb.setUnitOfMeasure(USCustomary.FOOT);
        lineSymb.getStroke().setWidth(ff.literal(size + "m"));

        visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

        lineSymb.accept(visitor);
        LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();
        double rescaledSize = rescaledLineSymb.getStroke().getWidth().evaluate(null, Double.class);

        assertEquals(Math.round(expectedRescaledSize), Math.round(rescaledSize));
        assertNotSame(rescaledLineSymb, lineSymb);
    }

    @Test
    public void visitLocalUomPixelOverridingMeters() {
        UomRescaleStyleVisitor visitor = null;
        double size = 100;
        double scaleMetersToPixel = 10;

        StyleBuilder styleBuilder = new StyleBuilder();
        LineSymbolizerImpl lineSymb = (LineSymbolizerImpl) styleBuilder.createLineSymbolizer();
        lineSymb.setUnitOfMeasure(SI.METRE);
        lineSymb.getStroke().setWidth(ff.literal(size + "px"));

        visitor = new UomRescaleStyleVisitor(scaleMetersToPixel);

        lineSymb.accept(visitor);
        LineSymbolizer rescaledLineSymb = (LineSymbolizer) visitor.getCopy();
        double rescaledSize = rescaledLineSymb.getStroke().getWidth().evaluate(null, Double.class);

        assertEquals(Math.round(size), Math.round(size));
        assertNotSame(rescaledLineSymb, lineSymb);
    }

    @Test
    public void visitNullExpression() {
        // this code generates a Displacement.NULL inside, which in turn contains
        // ConstantExpression.NULL
        Style style = SLD.createPolygonStyle(Color.YELLOW, null, 0.0f);
        UomRescaleStyleVisitor visitor = new UomRescaleStyleVisitor(1);
        // used to throw an exception here
        style.accept(visitor);
    }
}
