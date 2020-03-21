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

import static org.junit.Assert.assertTrue;

import org.geotools.filter.function.FilterFunction_relatePattern;
import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.referencing.FactoryException;

/**
 * Test Geo Operations.
 *
 * <p>
 *
 * <pre>
 *   &lt;routine invocation &gt; ::=
 *           &lt;geoop name &gt; &lt;georoutine argument list &gt;[*]
 *       |   &lt;relgeoop name &gt; &lt;relgeoop argument list &gt;
 *       |   &lt;routine name &gt; &lt;argument list &gt;
 *       |   &lt;relate geoop &gt;
 *   &lt;geoop name &gt; ::=
 *           EQUALS | DISJOINT | INTERSECTS | TOUCHES | CROSSES | [*]
 *           WITHIN | CONTAINS |OVERLAPS
 *
 *   &lt;relate geoop &gt; ::=  &lt;RELATE>&gt; &quot;(&quot; Attribute()&quot;,&quot; &lt;geometry literal&gt; &quot;,&quot; &lt;DE9IM_PATTERN&gt; &quot;)&quot;
 *   &lt;DE9IM_PATTERN&gt; ::= &lt;DE9IM_CHAR&gt;&lt;DE9IM_CHAR&gt;&lt;DE9IM_CHAR&gt; ...
 *   &lt;DE9IM_CHAR&gt; ::= *| T | F | 0 | 1 | 2
 *
 *   That rule is extended with bbox for convenience.
 *   &lt;bbox argument list &gt;::=
 *       &quot;(&quot;  &lt;attribute &gt; &quot;,&quot; &lt;min X &gt; &quot;,&quot; &lt;min Y &gt; &quot;,&quot; &lt;max X &gt; &quot;,&quot; &lt;max Y &gt;[&quot;,&quot;  &lt;srs &gt;] &quot;)&quot;
 *       &lt;min X &gt; ::=  &lt;signed numerical literal &gt;
 *       &lt;min Y &gt; ::=  &lt;signed numerical literal &gt;
 *       &lt;max X &gt; ::=  &lt;signed numerical literal &gt;
 *       &lt;max Y &gt; ::=  &lt;signed numerical literal &gt;
 *       &lt;srs &gt; ::=
 * </pre>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
public class CQLGeoOperationTest {

    protected final Language language;

    /** New instance of CQLTemporalPredicateTest */
    public CQLGeoOperationTest() {

        this(Language.CQL);
    }

    /** New instance of CQLTemporalPredicateTest */
    protected CQLGeoOperationTest(final Language language) {

        assert language != null : "language cannot be null value";

        this.language = language;
    }

    @Test
    public void disjoint() throws CQLException {

        Filter resultFilter = CompilerUtil.parseFilter(language, "DISJOINT(ATTR1, POINT(1 2))");

        assertTrue("Disjoint was expected", resultFilter instanceof Disjoint);
    }

    /**
     * Intersects geooperation
     *
     * @see intersects
     */
    @Test
    public void Intersects() throws CQLException {

        Filter resultFilter;

        resultFilter = CompilerUtil.parseFilter(language, "INTERSECTS(ATTR1, POINT(1 2))");

        assertTrue("Intersects was expected", resultFilter instanceof Intersects);

        // test bug GEOT-1980
        resultFilter =
                CompilerUtil.parseFilter(language, "INTERSECTS(GEOLOC, POINT(615358 312185))");

        assertTrue("Intersects was expected", resultFilter instanceof Intersects);
    }

    /** @throws CQLException */
    @Test
    public void relate() throws CQLException {

        PropertyIsEqualTo resultFilter;

        resultFilter =
                (PropertyIsEqualTo)
                        CompilerUtil.parseFilter(
                                language,
                                "RELATE(the_geom, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), T*****FF*)");

        Expression relateFunction = resultFilter.getExpression1();
        assertTrue(relateFunction instanceof FilterFunction_relatePattern);

        Literal trueLiteral = (Literal) resultFilter.getExpression2();
        assertTrue(trueLiteral.getValue() instanceof Boolean);
    }

    @Test
    public void relatePatterns() throws CQLException {

        testRelatePatten("T******F*");

        testRelatePatten("T012**FF*");

        testRelatePatten("100000001");

        testRelatePatten("200000000");
    }

    private void testRelatePatten(final String pattern) throws CQLException {

        PropertyIsEqualTo resultFilter;

        resultFilter =
                (PropertyIsEqualTo)
                        CompilerUtil.parseFilter(
                                language,
                                "RELATE(the_geom, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), "
                                        + pattern
                                        + ")");

        Expression relateFunction = resultFilter.getExpression1();
        assertTrue(relateFunction instanceof FilterFunction_relatePattern);

        Literal trueLiteral = (Literal) resultFilter.getExpression2();
        assertTrue(trueLiteral.getValue() instanceof Boolean);
    }

    /** The length of relate pattern must be 9 (nine) dimension characters */
    @Test(expected = CQLException.class)
    public void relateBadLongitudInPattern() throws CQLException {
        CQL.toFilter(
                "RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), **1******T)");
    }

    /** The illegal dimension character in relate pattern */
    @Test(expected = CQLException.class)
    public void relateIlegalPattern() throws CQLException {
        CQL.toFilter(
                "RELATE(geometry, LINESTRING (-134.921387 58.687767, -135.303391 59.092838), **1*****X)");
    }

    /** Invalid Geooperation Test */
    @Test(expected = CQLException.class)
    public void invalidGeoOperation() throws CQLException {
        CompilerUtil.parseFilter(
                this.language, "INTERSECT(ATTR1, POINT(1 2))"); // should be "intersects"
    }

    @Test
    public void invalidSyntaxMessage() throws CQLException {
        try {
            CompilerUtil.parseFilter(
                    this.language, "INTERSECT(ATTR1, POINT(1 2))"); // should be "intersects"
            Assert.fail("CQLException is expected");
        } catch (CQLException e) {
            final String error = e.getSyntaxError();
            Assert.assertNotNull(error);
            Assert.assertFalse("".equals(error));
        }
    }

    /** TOUCHES geooperation */
    @Test
    public void touches() throws CQLException {

        Filter resultFilter;

        // TOUCHES
        resultFilter = CompilerUtil.parseFilter(language, "TOUCHES(ATTR1, POINT(1 2))");

        assertTrue("Touches was expected", resultFilter instanceof Touches);
    }

    /** CROSSES geooperation operation */
    @Test
    public void crosses() throws CQLException {

        Filter resultFilter;

        resultFilter = CompilerUtil.parseFilter(language, "CROSSES(ATTR1, POINT(1 2))");

        assertTrue("Crosses was expected", resultFilter instanceof Crosses);
    }

    @Test
    public void contains() throws CQLException {
        Filter resultFilter = CompilerUtil.parseFilter(language, "CONTAINS(ATTR1, POINT(1 2))");

        assertTrue("Contains was expected", resultFilter instanceof Contains);
    }

    /** OVERLAPS geooperation operation test */
    @Test
    public void overlaps() throws Exception {
        Filter resultFilter;

        resultFilter = CompilerUtil.parseFilter(language, "OVERLAPS(ATTR1, POINT(1 2))");

        assertTrue("Overlaps was expected", resultFilter instanceof Overlaps);
    }

    /** EQULS geooperation operation test */
    @Test
    public void equals() throws CQLException {
        Filter resultFilter;

        // EQUALS
        resultFilter = CompilerUtil.parseFilter(language, "EQUALS(ATTR1, POINT(1 2))");

        assertTrue("not an instance of Equals", resultFilter instanceof Equals);
    }

    /** WITHIN test */
    @Test
    public void within() throws CQLException {

        Filter resultFilter =
                CompilerUtil.parseFilter(
                        language, "WITHIN(ATTR1, POLYGON((1 2, 1 10, 5 10, 1 2)) )");

        assertTrue("Within was expected", resultFilter instanceof Within);
    }

    /** BBOX test */
    @Test
    public void bbox() throws CQLException, FactoryException {

        Filter resultFilter;

        // BBOX
        resultFilter = CompilerUtil.parseFilter(language, "BBOX(ATTR1, 10.0,20.0,30.0,40.0)");
        assertTrue("BBox was expected", resultFilter instanceof BBOX);
        BBOX bboxFilter = (BBOX) resultFilter;
        assertTrue(
                JTS.equals(
                        new ReferencedEnvelope(10, 30, 20, 40, null), bboxFilter.getBounds(), 0.1));

        // BBOX using EPSG
        resultFilter =
                CompilerUtil.parseFilter(language, "BBOX(ATTR1, 10.0,20.0,30.0,40.0, 'EPSG:4326')");
        assertTrue("BBox was expected", resultFilter instanceof BBOX);
        bboxFilter = (BBOX) resultFilter;
        Assert.assertEquals(
                CRS.decode("EPSG:4326", false),
                bboxFilter.getBounds().getCoordinateReferenceSystem());
    }
}
