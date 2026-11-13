/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.function.EnvFunction;
import org.junit.After;
import org.junit.Test;

public class VendorOptionParserTest {

    private final VendorOptionParser parser = new VendorOptionParser();
    private final StyleFactory sf = CommonFactoryFinder.getStyleFactory();

    private TextSymbolizer option(String value) {
        TextSymbolizer ts = sf.createTextSymbolizer();
        ts.getOptions().put("opt", value);
        return ts;
    }

    private SimpleFeature featureWithPad(int pad) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        tb.add("pad", Integer.class);
        SimpleFeatureType type = tb.buildFeatureType();
        return SimpleFeatureBuilder.build(type, new Object[] {pad}, "fid");
    }

    @Test
    public void testLiteralIntUnchanged() {
        assertEquals(9, parser.getIntOption(option("9"), "opt", 2));
    }

    @Test
    public void testConstantExpressionInt() {
        assertEquals(4, parser.getIntOption(option("${1 + 3}"), "opt", 2, null));
    }

    @Test
    public void testAttributeExpressionInt() {
        assertEquals(7, parser.getIntOption(option("${pad}"), "opt", 2, featureWithPad(7)));
    }

    /** Rounds a fractional result to the nearest integer: 7 * 2.55 = 17.85 gives 18. */
    @Test
    public void testFractionalExpressionIntRounded() {
        assertEquals(18, parser.getIntOption(option("${(pad) * 2.55}"), "opt", 2, featureWithPad(7)));
    }

    @Test
    public void testExpressionWithoutFeatureFallsBackToDefault() {
        // an attribute-driven value cannot resolve against a null feature: the default is used
        assertEquals(2, parser.getIntOption(option("${pad}"), "opt", 2, null));
    }

    /** A value with an unclosed ${ fails instead of falling back to the default, like other ${cql} values. */
    @Test
    public void testMalformedExpression() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> parser.getIntOption(option("${pad"), "opt", 2, featureWithPad(7)));
        assertThat(e.getMessage(), containsString("Unclosed CQL expression 'pad'"));
    }

    @Test
    public void testDoubleExpression() {
        assertEquals(9.5, parser.getDoubleOption(option("${9.0 + 0.5}"), "opt", 2.0, null), 0.0);
    }

    @Test
    public void testBooleanExpression() {
        assertTrue(parser.getBooleanOption(option("${'true'}"), "opt", false, null));
    }

    @After
    public void clearEnv() {
        EnvFunction.clearLocalValues();
    }

    @Test
    public void testResultsFollowTheFeatureProperties() {
        TextSymbolizer ts = option("${pad * 2}");
        assertEquals(14, parser.getIntOption(ts, "opt", 0, featureWithPad(7)));
        assertEquals(16, parser.getIntOption(ts, "opt", 0, featureWithPad(8)));
        assertEquals(14, parser.getIntOption(ts, "opt", 0, featureWithPad(7)));
    }

    /** The environment is not a feature property, so the result is reused until the parser is told to forget it. */
    @Test
    public void testRememberedResultsAreClearedOnRequest() {
        TextSymbolizer ts = option("${pad + env('extra')}");
        EnvFunction.setLocalValues(Map.of("extra", 1));
        assertEquals(8, parser.getIntOption(ts, "opt", 0, featureWithPad(7)));
        EnvFunction.setLocalValues(Map.of("extra", 2));
        assertEquals(8, parser.getIntOption(ts, "opt", 0, featureWithPad(7)));
        parser.clearRememberedResults();
        assertEquals(9, parser.getIntOption(ts, "opt", 0, featureWithPad(7)));
    }

    @Test
    public void testVolatileFunctionIsEvaluatedEveryTime() {
        TextSymbolizer ts = option("${random() + pad}");
        double first = parser.getDoubleOption(ts, "opt", 0, featureWithPad(7));
        double second = parser.getDoubleOption(ts, "opt", 0, featureWithPad(7));
        assertNotEquals(first, second, 0.0);
    }
}
