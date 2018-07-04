/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.mbstyle.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

/** Test the {@link ExponentialFunction}, {@link ZoomLevelFunction} and {@link CSSFunction}. */
public class MBFunctionFactoryTest {

    public static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void colorFunction() throws Exception {
        Function expr = (Function) ECQL.toExpression("css('#ff0000')");
        assertEquals("hex", Color.red, expr.evaluate(null, Color.class));
        assertEquals("hex", "#FF0000", expr.evaluate(null, String.class));

        expr = (Function) ECQL.toExpression("css('red')");
        assertEquals("css", Color.red, expr.evaluate(null, Color.class));
        assertEquals("css", "#FF0000", expr.evaluate(null, String.class));

        expr = (Function) ECQL.toExpression("css('rgb(255,0,0)')");
        assertEquals("rgb", Color.red, expr.evaluate(null, Color.class));
        assertEquals("rgb", "#FF0000", expr.evaluate(null, String.class));
    }

    @Test
    public void expontentialFunctionNumeric() throws Exception {
        //
        // base 1.0 works as a simple interpolate
        //
        Function expr = (Function) ECQL.toExpression("Exponential( 0, 1.0, 0,0, 10,100)");
        assertEquals(0, (int) expr.evaluate(null, Integer.class));

        expr = (Function) ECQL.toExpression("Exponential( 5, 1.0, 0,0, 10,100)");
        assertEquals(50, (int) expr.evaluate(null, Integer.class));

        expr = (Function) ECQL.toExpression("Exponential(10, 1.0, 0,0, 10,100)");
        assertEquals(100, (int) expr.evaluate(null, Integer.class));

        //
        // base 0.7 increases curve
        //
        expr = (Function) ECQL.toExpression("Exponential( 0, 0.7, 0,0, 10,100)");
        assertEquals(0, (int) expr.evaluate(null, Integer.class));

        expr = (Function) ECQL.toExpression("Exponential( 5, 0.7, 0,0, 10,100)");
        assertTrue(50 < (int) expr.evaluate(null, Integer.class));

        expr = (Function) ECQL.toExpression("Exponential(10, 0.7, 0,0, 10,100)");
        assertEquals(100, (int) expr.evaluate(null, Integer.class));

        //
        // base 1.4 decreases curve
        //
        expr = (Function) ECQL.toExpression("Exponential( 0, 1.4, 0,0, 10,100)");
        assertEquals(0, (int) expr.evaluate(null, Integer.class));

        expr = (Function) ECQL.toExpression("Exponential( 5, 1.4, 0,0, 10,100)");
        assertTrue(50 > (int) expr.evaluate(null, Integer.class));

        expr = (Function) ECQL.toExpression("Exponential(10, 1.5, 0,0, 10,100)");
        assertEquals(100, (int) expr.evaluate(null, Integer.class));
    }

    @Test
    public void expontentialFunctionColor() throws Exception {
        //
        // base 1.0 works as a simple interpolate
        //
        Function expr =
                (Function) ECQL.toExpression("Exponential( 0, 1.0, 0,'#000000', 10,'#ffffff')");
        assertEquals(Color.BLACK, expr.evaluate(null, Color.class));

        expr = (Function) ECQL.toExpression("Exponential( 5, 1.0, 0,'#000000', 10,'#ffffff')");
        assertEquals(Color.GRAY, expr.evaluate(null, Color.class));

        expr = (Function) ECQL.toExpression("Exponential(10, 1.0, 0,'#000000', 10,'#ffffff')");
        assertEquals(Color.WHITE, expr.evaluate(null, Color.class));

        //
        // base 0.7 increases curve
        //
        expr = (Function) ECQL.toExpression("Exponential( 0, 0.7, 0,'#000000', 10,'#ffffff')");
        assertEquals(Color.BLACK, expr.evaluate(null, Color.class));

        expr = (Function) ECQL.toExpression("Exponential( 5, 0.7, 0,'#000000', 10,'#ffffff')");
        assertTrue(Color.GRAY.getRed() < expr.evaluate(null, Color.class).getRed());

        expr = (Function) ECQL.toExpression("Exponential(10, 0.7, 0,'#000000', 10,'#ffffff')");
        assertEquals(Color.WHITE, expr.evaluate(null, Color.class));

        //
        // base 1.4 decreases curve
        //
        expr = (Function) ECQL.toExpression("Exponential( 0, 1.4, 0,'#000000', 10,'#ffffff')");
        assertEquals(Color.BLACK, expr.evaluate(null, Color.class));

        expr = (Function) ECQL.toExpression("Exponential( 5, 1.4, 0,'#000000', 10,'#ffffff')");
        assertTrue(Color.GRAY.getRed() > expr.evaluate(null, Color.class).getRed());

        expr = (Function) ECQL.toExpression("Exponential(10, 1.4, 0,'#000000', 10,'#ffffff')");
        assertEquals(Color.WHITE, expr.evaluate(null, Color.class));
    }

    /**
     * Tests for the {@link DefaultIfNullFunction} to verify that it falls back to the provided
     * value when the input value is null in various contexts.
     */
    @Test
    public void defaultIfNullFunctionTest() throws Exception {

        // Test that is passes through non-null values
        Function f = (Function) ECQL.toExpression("DefaultIfNull('#FF0000', '#000000')");
        assertEquals(Color.RED, f.evaluate(null, Color.class));

        // Test fallback to default when input arg is null
        f = ff.function("DefaultIfNull", ff.literal(null), ff.literal("#000000"));
        assertEquals(Color.BLACK, f.evaluate(null, Color.class));

        // Test fallback to default when the input conversion fails and returns null
        SimpleFeatureType SAMPLE =
                DataUtilities.createType("SAMPLE", "id:\"\",temperature:0.0,location=4326");
        SimpleFeature feature =
                DataUtilities.createFeature(SAMPLE, "measure1=A|NOT_A_VALID_NUMBER|POINT(0,0)");
        f = ff.function("DefaultIfNull", ff.property("temperature"), ff.literal(789));
        assertEquals(789, f.evaluate(feature, Number.class).intValue());

        // Test fallback to default when exponential fails and returns null)
        Function exp =
                ff.function(
                        "Exponential",
                        ff.property("temperature"),
                        ff.literal(1),
                        ff.literal(0),
                        ff.literal("#000000"),
                        ff.literal(10),
                        ff.literal("#ffffff"));
        f = ff.function("DefaultIfNull", exp, ff.literal(789));
        assertEquals(789, f.evaluate(feature, Number.class));

        // Test fallback to default when recode returns null
        Function recode =
                ff.function(
                        "Recode",
                        ff.property("temperature"),
                        ff.literal(1),
                        ff.literal("Output1"),
                        ff.literal(2),
                        ff.literal("Output2"));
        f = ff.function("DefaultIfNull", recode, ff.literal("DefautValue"));
        assertEquals("DefautValue", f.evaluate(feature, String.class));
    }

    /** Tests for {@link ZoomLevelFunction}, converting scale 3857 denominators to zoom levels. */
    @Test
    public void zoomFunctionTest() throws Exception {
        double tol = .00000001;

        Function f =
                (Function)
                        ECQL.toExpression(
                                "zoomLevel("
                                        + ZoomLevelFunction.EPSG_3857_O_SCALE
                                        + ", 'EPSG:3857')");
        assertEquals(0.0, f.evaluate(null, Number.class).doubleValue(), tol);

        Function f2 =
                (Function)
                        ECQL.toExpression(
                                "zoomLevel("
                                        + (ZoomLevelFunction.EPSG_3857_O_SCALE / 2.0)
                                        + ", 'EPSG:3857')");
        assertEquals(1.0, f2.evaluate(null, Number.class).doubleValue(), tol);

        Function fhalf =
                (Function)
                        ECQL.toExpression(
                                "zoomLevel("
                                        + (ZoomLevelFunction.EPSG_3857_O_SCALE
                                                / (Math.pow(2.0, 0.5)))
                                        + ", 'EPSG:3857')");
        assertEquals(0.5, fhalf.evaluate(null, Number.class).doubleValue(), tol);

        double scaleDenomForZoom22 = 133.295598972;
        Function f22 =
                (Function) ECQL.toExpression("zoomLevel(" + scaleDenomForZoom22 + ", 'EPSG:3857')");
        assertEquals(22.0, f22.evaluate(null, Number.class).doubleValue(), tol);
    }

    @Test
    public void stringTransformFunctionTest() throws Exception {
        Function f = (Function) ECQL.toExpression("StringTransform('SoMeString', 'uppercase')");
        assertEquals("SOMESTRING", f.evaluate(null, String.class));

        f = (Function) ECQL.toExpression("StringTransform('SoMeString', 'UPPERCASE')");
        assertEquals("SOMESTRING", f.evaluate(null, String.class));

        f = (Function) ECQL.toExpression("StringTransform('SoMeString', 'lowercase')");
        assertEquals("somestring", f.evaluate(null, String.class));

        f = (Function) ECQL.toExpression("StringTransform('SoMeString', 'LOWERCASE')");
        assertEquals("somestring", f.evaluate(null, String.class));

        f = (Function) ECQL.toExpression("StringTransform('SoMeString', 'none')");
        assertEquals("SoMeString", f.evaluate(null, String.class));

        f = (Function) ECQL.toExpression("StringTransform('SoMeString', 'default')");
        assertEquals("SoMeString", f.evaluate(null, String.class));

        f = (Function) ECQL.toExpression("StringTransform('SoMeString', '')");
        assertEquals("SoMeString", f.evaluate(null, String.class));

        f = (Function) ECQL.toExpression("StringTransform('', '')");
        assertEquals("", f.evaluate(null, String.class));

        f = ff.function("StringTransform", ff.literal("SoMeString"), ff.literal(null));
        assertEquals("SoMeString", f.evaluate(null, String.class));

        f = ff.function("StringTransform", ff.literal(null), ff.literal(null));
        assertTrue(null == f.evaluate(null, String.class));
    }
}
