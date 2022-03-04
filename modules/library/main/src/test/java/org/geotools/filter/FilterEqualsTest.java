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
package org.geotools.filter;

import static org.junit.Assert.assertNotEquals;

import java.util.Collections;
import java.util.logging.Logger;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.expression.AddImpl;
import org.geotools.filter.expression.SubtractImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.Disjoint;

/**
 * Unit test for testing filters equals method.
 *
 * @author Chris Holmes, TOPP
 * @author James MacGill, CCG
 * @author Rob Hranac, TOPP
 */
public class FilterEqualsTest {

    /** Standard logging instance */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(FilterEqualsTest.class);

    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    private Expression testExp1;
    private Expression testExp2;
    private Expression testExp3;
    private Expression testExp4;
    private Filter tFilter1;

    /** Schema on which to preform tests */
    private static SimpleFeatureType testSchema = null;

    @Test
    public void testLiteralExpressionImplEquals() {
        try {
            Literal testString1 = new LiteralExpressionImpl("test literal");
            Literal testString2 = new LiteralExpressionImpl("test literal");
            Assert.assertEquals(testString1, testString2);

            Literal testOtherString = new LiteralExpressionImpl("not test literal");
            assertNotEquals(testString1, testOtherString);

            Literal testNumber34 = new LiteralExpressionImpl(Integer.valueOf(34));
            assertNotEquals(testString1, testNumber34);

            Literal testOtherNumber34 = new LiteralExpressionImpl(Integer.valueOf(34));
            Assert.assertEquals(testNumber34, testOtherNumber34);
        } catch (IllegalFilterException e) {
            LOGGER.warning("bad filter " + e.getMessage());
        }
    }

    @Test
    public void testFidFilter() {
        FidFilterImpl ff = new FidFilterImpl(Collections.singleton(new FeatureIdImpl("1")));

        FidFilterImpl ff2 = new FidFilterImpl(Collections.singleton(new FeatureIdImpl("1")));
        Assert.assertNotNull(ff2);
        Assert.assertEquals(ff, ff2);
        assertNotEquals(null, ff);
        assertNotEquals("a string not even a filter", ff);
        ff2.addFid("2");
        assertNotEquals(ff, ff2);

        ff.addFid("2");
        Assert.assertEquals(ff, ff2);
    }

    @Test
    public void testExpressionMath() {
        try {
            testExp1 = new LiteralExpressionImpl(Double.valueOf(5));
            testExp2 = new LiteralExpressionImpl(Double.valueOf(5));
            MathExpressionImpl testMath1 = new AddImpl(null, null);
            testMath1.setExpression1(testExp1);
            testMath1.setExpression2(testExp2);
            MathExpressionImpl testMath2 = new AddImpl(null, null);
            testMath2.setExpression1(testExp2);
            testMath2.setExpression2(testExp1);
            Assert.assertEquals(testMath1, testMath2);
            testExp3 = new LiteralExpressionImpl(Integer.valueOf(4));
            testExp4 = new LiteralExpressionImpl(Integer.valueOf(4));
            testMath2.setExpression1(testExp3);
            assertNotEquals(testMath1, testMath2);
            testMath1.setExpression1(testExp4);
            Assert.assertEquals(testMath1, testMath2);
            testMath1 = new SubtractImpl(null, null);
            testMath1.setExpression1(testExp4);
            testMath1.setExpression1(testExp2);
            assertNotEquals(testMath1, testMath2);
            assertNotEquals("Random Object that happens to be a string", testMath1);
        } catch (IllegalFilterException e) {
            LOGGER.warning("bad filter: " + e.getMessage());
        }
    }

    @Test
    public void testExpressionAttribute() throws IllegalFilterException, SchemaException {
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.add("testBoolean", Boolean.class);
        ftb.add("testString", String.class);
        ftb.setName("test2");

        SimpleFeatureType testSchema2 = ftb.buildFeatureType();
        // FeatureType testSchema2 = feaTypeFactory.getFeatureType();
        testExp1 = new AttributeExpressionImpl(testSchema, "testBoolean");
        testExp2 = new AttributeExpressionImpl(testSchema, "testBoolean");
        Assert.assertEquals(testExp1, testExp2);
        testExp3 = new AttributeExpressionImpl(testSchema, "testString");
        assertNotEquals(testExp1, testExp3);

        testExp4 = new AttributeExpressionImpl(testSchema2, "testBoolean");
        assertNotEquals(testExp1, testExp4);

        testExp1 = new AttributeExpressionImpl(testSchema2, "testBoolean");
        Assert.assertEquals(testExp1, testExp4);
    }

    @Test
    public void testCompareFilter() throws IllegalFilterException {
        testExp1 = new LiteralExpressionImpl(Integer.valueOf(45));
        testExp2 = new LiteralExpressionImpl(Integer.valueOf(45));
        testExp3 = new AttributeExpressionImpl(testSchema, "testInteger");
        testExp4 = new AttributeExpressionImpl(testSchema, "testInteger");
        PropertyIsEqualTo cFilter1 = ff.equals(testExp1, testExp3);
        PropertyIsEqualTo cFilter2 = ff.equals(testExp1, testExp3);
        Assert.assertEquals(cFilter1, cFilter2);
        cFilter2 = ff.equals(testExp2, testExp4);
        Assert.assertEquals(cFilter1, cFilter2);
        // see if converters make this work
        cFilter2 = ff.equals(ff.literal(Double.valueOf(45.0)), testExp3);
        Assert.assertEquals(cFilter1, cFilter2);
        tFilter1 = ff.between(testExp1, testExp2, testExp3);
        assertNotEquals(cFilter1, tFilter1);
    }

    @Test
    public void testBetweenFilter() throws IllegalFilterException {
        IsBetweenImpl bFilter1 = new IsBetweenImpl(null, null, null);
        IsBetweenImpl bFilter2 = new IsBetweenImpl(null, null, null);
        LiteralExpressionImpl testLit1 = new LiteralExpressionImpl(Integer.valueOf(55));
        LiteralExpressionImpl testLit2 = new LiteralExpressionImpl(Integer.valueOf(55));
        testExp1 = new LiteralExpressionImpl(Integer.valueOf(45));
        testExp2 = new LiteralExpressionImpl(Integer.valueOf(45));
        testExp3 = new AttributeExpressionImpl(testSchema, "testInteger");
        testExp4 = new AttributeExpressionImpl(testSchema, "testInteger");
        bFilter1.setExpression1(testExp1);
        bFilter2.setExpression1(testExp2);
        bFilter1.setExpression(testExp3);
        bFilter2.setExpression(testExp4);
        bFilter1.setExpression2(testLit1);
        bFilter2.setExpression2(testLit2);
        Assert.assertEquals(bFilter2, bFilter1);
        tFilter1 =
                ff.equals(
                        org.opengis.filter.expression.Expression.NIL,
                        org.opengis.filter.expression.Expression.NIL);
        assertNotEquals(bFilter2, tFilter1);
        bFilter2.setExpression2(new LiteralExpressionImpl(Integer.valueOf(65)));
        assertNotEquals(bFilter2, bFilter1);
    }

    @Test
    public void testLikeFilter() throws IllegalFilterException {
        LikeFilterImpl lFilter1 = new LikeFilterImpl();
        LikeFilterImpl lFilter2 = new LikeFilterImpl();
        String pattern = "te_st!";
        String wcMulti = "!";
        String wcSingle = "_";
        String escape = "#";
        testExp2 = new LiteralExpressionImpl(Integer.valueOf(45));
        testExp3 = new AttributeExpressionImpl(testSchema, "testInteger");
        testExp4 = new AttributeExpressionImpl(testSchema, "testInteger");
        lFilter1.setExpression(testExp3);
        lFilter2.setExpression(testExp4);

        lFilter1.setLiteral(pattern);
        lFilter1.setWildCard(wcMulti);
        lFilter1.setSingleChar(wcSingle);
        lFilter1.setEscape(escape);

        lFilter2.setLiteral(pattern);
        lFilter2.setWildCard(wcMulti);
        lFilter2.setSingleChar(wcSingle);
        lFilter2.setEscape(escape);
        Assert.assertEquals(lFilter1, lFilter2);

        lFilter2.setLiteral("te__t!");
        lFilter2.setWildCard(wcMulti);
        lFilter2.setSingleChar(wcSingle);
        lFilter2.setEscape(escape);
        assertNotEquals(lFilter1, lFilter2);

        lFilter2.setLiteral(pattern);
        lFilter2.setWildCard(wcMulti);
        lFilter2.setSingleChar(wcSingle);
        lFilter2.setEscape(escape);
        lFilter2.setExpression(testExp2);
        assertNotEquals(lFilter1, lFilter2);
    }

    @Test
    public void testLogicFilter() throws IllegalFilterException {
        testExp1 = new LiteralExpressionImpl(Integer.valueOf(45));
        testExp2 = new LiteralExpressionImpl(Integer.valueOf(45));
        testExp3 = new AttributeExpressionImpl(testSchema, "testInteger");
        testExp4 = new AttributeExpressionImpl(testSchema, "testInteger");
        PropertyIsEqualTo cFilter1 = ff.equals(testExp1, testExp2);
        PropertyIsEqualTo cFilter2 = ff.equals(testExp2, testExp4);

        org.opengis.filter.Filter logFilter1 = ff.and(cFilter1, cFilter2);
        org.opengis.filter.Filter logFilter2 = ff.and(cFilter1, cFilter2);
        Assert.assertEquals(logFilter1, logFilter2);

        logFilter1 = ff.not(cFilter2);
        assertNotEquals(logFilter1, logFilter2);
        cFilter1 = ff.equals(testExp1, testExp3);
        logFilter2 = ff.not(cFilter1);
        Assert.assertEquals(logFilter1, logFilter2);
        assertNotEquals(logFilter1, ff.between(testExp1, testExp2, testExp3));
        Or logFilter3 = ff.or(logFilter1, logFilter2);
        Or logFilter4 = ff.or(logFilter1, logFilter2);
        Assert.assertEquals(logFilter3, logFilter4);

        // Questionable behavior.  Is this what we want?
        Or logFilter5 = ff.or(cFilter1, logFilter3);
        // does not change structure of logFilter3
        Or logFilter6 = ff.or(logFilter4, cFilter1);
        // different structure, but same result
        Assert.assertEquals(logFilter5, logFilter6); // do we want these equal?
        Assert.assertEquals(logFilter4, logFilter3); // shouldn't they be equal?
    }

    @Test
    public void testNullFilter() throws IllegalFilterException {
        testExp1 = new AttributeExpressionImpl(testSchema, "testDouble");
        testExp2 = new AttributeExpressionImpl(testSchema, "testDouble");
        testExp3 = new AttributeExpressionImpl(testSchema, "testBoolean");
        NullFilterImpl nullFilter1 = new NullFilterImpl(Expression.NIL);
        NullFilterImpl nullFilter2 = new NullFilterImpl(Expression.NIL);
        nullFilter1.setExpression(testExp1);
        nullFilter2.setExpression(testExp2);
        Assert.assertEquals(nullFilter1, nullFilter2);
        nullFilter1.setExpression(testExp3);
        assertNotEquals(nullFilter1, nullFilter2);
        assertNotEquals(nullFilter1, new IsBetweenImpl(null, null, null));
    }

    @Test
    public void testGeometryFilter() throws IllegalFilterException {
        Disjoint geomFilter1 = ff.disjoint(testExp1, testExp4);
        Disjoint geomFilter2 = ff.disjoint(testExp2, testExp4);
        Assert.assertEquals(geomFilter1, geomFilter2);
        geomFilter2 = ff.disjoint(testExp2, new LiteralExpressionImpl(Double.valueOf(45)));
        assertNotEquals(geomFilter1, geomFilter2);
        tFilter1 = ff.between(ff.literal(1), ff.literal(-1), ff.literal(3));
        assertNotEquals(geomFilter1, tFilter1);
    }
}
