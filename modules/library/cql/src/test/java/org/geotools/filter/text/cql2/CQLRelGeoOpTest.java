/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cql2;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.DistanceBufferOperator;

/**
 * Test RelGeo Operations
 *
 * <p>
 *
 * <pre>
 *   &lt;routine invocation &gt; ::=
 *       &lt;geoop name &gt; &lt;georoutine argument list &gt;
 *   |   &lt;relgeoop name &gt; &lt;relgeoop argument list &gt;
 *   |  &lt;routine name &gt; &lt;argument list &gt;
 *   &lt;relgeoop name &gt; ::=
 *       DWITHIN | BEYOND [*]
 * </pre>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
public class CQLRelGeoOpTest {

    protected final Language language;

    public CQLRelGeoOpTest() {

        this(Language.CQL);
    }

    protected CQLRelGeoOpTest(final Language language) {

        assert language != null : "language cannot be null value";

        this.language = language;
    }

    @Test
    public void dwithin() throws CQLException {
        Filter resultFilter;

        // DWITHIN
        resultFilter =
                CompilerUtil.parseFilter(language, "DWITHIN(ATTR1, POINT(1 2), 10, kilometers)");

        Assert.assertTrue(resultFilter instanceof DistanceBufferOperator);

        // test compound attribute gmd:aa:bb.gmd:cc.gmd:dd
        final String prop = "gmd:aa:bb.gmd:cc.gmd:dd";
        final String propExpected = "gmd:aa:bb/gmd:cc/gmd:dd";
        resultFilter =
                CompilerUtil.parseFilter(
                        language, "DWITHIN(" + prop + ", POINT(1 2), 10, kilometers) ");

        Assert.assertTrue(
                "DistanceBufferOperator filter was expected", resultFilter instanceof DWithin);

        DistanceBufferOperator filter = (DWithin) resultFilter;
        Expression property = filter.getExpression1();

        Assert.assertEquals(propExpected, property.toString());
    }

    @Test
    public void beyon() throws CQLException {
        Filter resultFilter;
        // Beyond
        resultFilter =
                CompilerUtil.parseFilter(
                        language, "BEYOND(ATTR1, POINT(1.0 2.0), 10.0, kilometers)");
        Assert.assertTrue(resultFilter instanceof Beyond);
        Beyond beyondFilter = (Beyond) resultFilter;

        Assert.assertEquals(beyondFilter.getDistance(), 10.0, 0.1);
        Assert.assertEquals(beyondFilter.getDistanceUnits(), "kilometers");
        Assert.assertEquals(beyondFilter.getExpression1().toString(), "ATTR1");

        Expression geomExpression = beyondFilter.getExpression2();
        Assert.assertTrue(geomExpression instanceof Literal);
        Literal literalPoint = (Literal) geomExpression;

        Object pointValue = literalPoint.getValue();
        Assert.assertTrue(pointValue instanceof Point);
        Point point = (Point) pointValue;
        Assert.assertEquals(point.getX(), 1.0, 0.1);
        Assert.assertEquals(point.getY(), 2.0, 0.1);
    }

    @Test(expected = CQLException.class)
    public void syntaxError() throws Exception {

        CompilerUtil.parseFilter(language, "EYOND(ATTR1, POINTS(1.0 2.0), 10.0, kilometers)");
    }

    @Test
    public final void syntaxErrorMessage() {

        try {
            // must polygon must have two ring (shell and hole)
            final String malformedGeometry = "WITHIN(ATTR1, POLYGON((1 2, 10 15), (10 15, 1 2)))";
            CompilerUtil.parseFilter(this.language, malformedGeometry);
            Assert.fail();
        } catch (CQLException e) {

            final String msg = e.getSyntaxError();
            Assert.assertNotNull(msg);
        }
    }

    /**
     * Test RelGeo Operations [*]
     *
     * <p>
     *
     * <pre>
     *   &lt;routine invocation &gt; ::=
     *       &lt;geoop name &gt; &lt;georoutine argument list &gt;
     *   |   &lt;relgeoop name &gt; &lt;relgeoop argument list &gt;
     *   |   &lt;routine name &gt; &lt;argument list &gt; [*]
     *  &lt;argument list&gt; ::=    [*]
     *       &lt;left paren&gt; [&lt;positional arguments&gt;] &lt;right paren&gt;
     *  &lt;positional arguments&gt; ::=
     *       &lt;argument&gt; [ { &lt;comma&amp;gt &lt;argument&gt; }... ]
     *  &lt;argument&gt;  ::=
     *       &lt;literal&gt;
     *   |   &lt;attribute name&gt;
     * </pre>
     */
    @Ignore
    public void testRoutineInvocationGeneric() throws Exception {
        // TODO (Mauricio Comments) This case is not implemented because the filter
        // model has not a
        // Routine (Like functions in Expression). We could develop easily the
        // parser but we can not build a filter for CQL <Routine invocation>.
    }
}
