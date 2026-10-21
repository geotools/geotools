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
package org.geotools.mbstyle.expression;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.AttributeExpressionImpl;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

/** Tests the "format" expression: sections are concatenated to a single string, option objects are dropped. */
public class MBFormatTest {

    private final JSONParser parser = new JSONParser();
    private SimpleFeature feature;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureType type = DataUtilities.createType("format.test", "name:String,geom:Point");
        feature = SimpleFeatureBuilder.build(
                type, new Object[] {"Roma", new GeometryFactory().createPoint(new Coordinate(0, 0))}, "format.1");
    }

    private String eval(String json) throws Exception {
        return transform(json).evaluate(feature, String.class);
    }

    private Expression transform(String json) throws Exception {
        return MBExpression.transformExpression((JSONArray) parser.parse(json.replace('\'', '"')));
    }

    @Test
    public void testSingleSection() throws Exception {
        // a lone section returns the input expression directly
        Expression expression = transform("['format', ['get', 'name'], {}]");
        assertThat(expression, instanceOf(AttributeExpressionImpl.class));
        assertEquals("Roma", expression.evaluate(feature, String.class));
    }

    @Test
    public void testLineBreak() throws Exception {
        assertEquals("Roma\nIT", eval("['format', ['get', 'name'], {}, '\\n', {}, 'IT', {}]"));
    }

    @Test
    public void testTokenNotResolved() throws Exception {
        assertEquals("{name}", eval("['format', '{name}', {}]"));
    }

    @Test
    public void testNoOptions() throws Exception {
        assertEquals("Roma IT", eval("['format', ['get', 'name'], ' ', 'IT']"));
    }

    @Test
    public void testMissingAttribute() throws Exception {
        // MapLibre turns a missing value into an empty string
        assertEquals("Roma", eval("['format', ['get', 'name'], {}, ['get', 'missing'], {}]"));
    }

    @Test
    public void testLiteralOnly() throws Exception {
        assertEquals("hello", eval("['format', 'hello', {}]"));
    }

    @Test
    public void testMultipleSectionsConcatenated() throws Exception {
        // sections joined, per-section option objects (here a text-font) ignored
        assertEquals(
                "Roma - IT",
                eval("['format', ['get', 'name'], {'text-font': ['Noto Sans Regular']}, ' - ', {}, 'IT', {}]"));
    }
}
