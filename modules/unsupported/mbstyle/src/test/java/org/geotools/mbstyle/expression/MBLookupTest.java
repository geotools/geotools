/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.expression;

import static org.junit.Assert.*;

import java.util.List;
import org.geotools.mbstyle.MBStyle;
import org.geotools.mbstyle.layer.SymbolMBLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.xml.styling.SLDTransformer;
import org.json.simple.JSONObject;
import org.junit.Test;

public class MBLookupTest extends AbstractMBExpressionTest {

    @Override
    protected String getTestResourceName() {
        return "expressionMBLookupTest.json";
    }

    @Override
    protected Class getExpressionClassType() {
        return MBLookup.class;
    }

    /** Verify that a "get" lookup expression can be parsed correctly. */
    @Test
    public void testParseGetExpression() {
        // test get with JSONObject argument
        final JSONObject j = getObjectByLayerId("getExpression", "paint");
        Object o = getExpressionEvaluation(j, "text-color");
        assertEquals("#006fde", o);
        // confirm layout isn't null
        final JSONObject jg = getObjectByLayerId("getExpression", "layout");
        assertNotNull(jg);
        // Transform to confirm it uses PropertyName function in SLD
        MBStyle getTest = MBStyle.create(mbstyle);
        SymbolMBLayer testLayer = (SymbolMBLayer) getTest.layer("getExpression");
        List<FeatureTypeStyle> getFeatures = testLayer.transformInternal(getTest);
        try {
            String xml = new SLDTransformer().transform(getFeatures.get(0));
            assertTrue(
                    xml.contains(
                            "<ogc:Function name=\"property\"><ogc:Literal>Name</ogc:Literal></ogc:Function>"));
        } catch (Exception e) {
        }
        // test nested "get expression
        final JSONObject jn = getObjectByLayerId("getExpression", "paint");
        Object on = getExpressionEvaluation(jn, "halo-color");
        assertEquals("RED", on);
    }

    /** Verify that an "at" lookup expression can be parsed correctly. */
    @Test
    public void testParseAtExpression() {
        // test "at" expression for array
        final JSONObject j = getObjectByLayerId("atExpression", "paint");
        Object o = getExpressionEvaluation(j, "text-color");
        assertEquals("#006fde", o);
        // test nested "at" expression
        final JSONObject jn = getObjectByLayerId("atExpression", "paint");
        Object on = getExpressionEvaluation(jn, "halo-color");
        assertEquals("length", on);
    }

    /** Verify that a "length" lookup expression can be parsed correctly. */
    @Test
    public void testParseLengthExpression() {
        // test string length
        final JSONObject j = getObjectByLayerId("lengthExpression", "paint");
        Object o = getExpressionEvaluation(j, "text-color");
        assertEquals(9, o);
        // test array length
        final JSONObject jl = getObjectByLayerId("lengthExpression", "layout");
        Object ol = getExpressionEvaluation(jl, "text-field");
        assertEquals(9, ol);
        // test nested length expression
        final JSONObject jn = getObjectByLayerId("lengthExpression", "paint");
        Object on = getExpressionEvaluation(jn, "halo-color");
        assertEquals(9, on);
    }

    /** Verify that a "has" Lookup expression can be parsed correctly. */
    @Test
    public void testParseHasExpression() throws Exception {
        // "has" expression test
        final JSONObject j = getObjectByLayerId("hasExpression", "paint");
        Object o = getExpressionEvaluation(j, "text-color");
        assertEquals(true, o);
        // "has" expression test
        final JSONObject jh = getObjectByLayerId("hasExpression", "layout");
        assertNotNull(jh);
        Object oh = getExpressionEvaluation(jh, "text-field");
        assertEquals(true, oh);
        // nested "has" expression test
        final JSONObject jn = getObjectByLayerId("hasExpression", "paint");
        Object on = getExpressionEvaluation(jn, "halo-color");
        assertEquals(true, on);
        // Verify that a "has" lookup expression transforms correctly to SLD.
        MBStyle getTest = MBStyle.create(mbstyle);
        SymbolMBLayer rgbLayer = (SymbolMBLayer) getTest.layer("hasExpression");
        List<FeatureTypeStyle> getFeatures = rgbLayer.transformInternal(getTest);
        String xml = new SLDTransformer().transform(getFeatures.get(0));
        assertTrue(
                xml.contains(
                        "<ogc:Function name=\"PropertyExists\"><ogc:Literal>name</ogc:Literal></ogc:Function>"));
    }
}
