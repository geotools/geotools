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
package org.geotools.filter.text.cql2;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.temporal.AnyInteracts;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * FilterToCQLTest
 *
 * <p>Unit test for {@link FilterToCQL}
 *
 * @author Johann Sorel
 */
public class FilterToCQLTest {

    @Test
    public void testSample() throws Exception {
        Filter filter = CQL.toFilter(FilterCQLSample.LESS_FILTER_SAMPLE);
        FilterToCQL toCQL = new FilterToCQL();

        String output = filter.accept(toCQL, null).toString();
        Assert.assertNotNull(output);
        Assert.assertEquals(FilterCQLSample.LESS_FILTER_SAMPLE, output);
    }

    @Test
    public void testNotBetween() throws Exception {
        cqlTest("NOT (ATTR1 BETWEEN 10 AND 20)");
    }

    @Test
    public void testANDOR() throws Exception {
        cqlTest("(ATTR1 < 10 AND ATTR2 < 2) OR ATTR3 > 10");
    }
    /** (ATTR1 > 10 OR ATTR2 < 2) */
    @Test
    public void testOR() throws Exception {
        cqlTest("ATTR1 > 10 OR ATTR2 < 2");
    }

    @Test
    public void testLike() throws Exception {
        cqlTest("ATTR1 LIKE '%ABC%'");
    }

    @Test
    public void testNotEqualTo() throws Exception {
        String cql = "ATTR1 <> 'foo'";
        Filter filter = CQL.toFilter(cql);
        Assert.assertNotNull(filter);

        String cql2 = "NOT (ATTR1 = 'foo')";
        String output2 = CQL.toFilter(cql2).accept(new FilterToCQL(), null).toString();

        FilterToCQL toCQL = new FilterToCQL();
        String output = filter.accept(toCQL, null).toString();
        Assert.assertEquals(output2, output);
    }

    @Test(expected = CQLException.class)
    public void testFailNotEqualTo() throws Exception {

        cqlTest("ATTR1 != 'foo'");
    }

    @Test
    public void testBbox() throws Exception {
        cqlTest("BBOX(the_geom, 10.0,20.0,30.0,40.0)");
    }

    @Test
    public void testAfter() throws Exception {

        cqlTest("attr AFTER 2006-12-31T01:30:00+00:00");
    }

    @Test
    public void testBefore() throws Exception {

        cqlTest("attr BEFORE 2006-12-31T01:30:00+00:00");
    }

    @Test
    public void testBeforeAndAfter() throws Exception {

        cqlTest("dateAttr AFTER 2006-10-10T01:30:00+00:00 AND dateAttr BEFORE 2010-12-31T01:30:00+00:00");
    }

    @Test
    public void testDuring() throws Exception {

        cqlTest("dateAttr DURING 2006-10-10T01:30:00+00:00/2010-12-31T01:30:00+00:00");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEndedByUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        EndedBy filter = ff.endedBy(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEndsUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Ends filter = ff.ends(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMeetsUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Meets filter = ff.meets(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMetByUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        MetBy filter = ff.metBy(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOverlappedByUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        OverlappedBy filter = ff.overlappedBy(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTContainsUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        TContains filter = ff.tcontains(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTEqualsUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        TEquals filter = ff.tequals(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTOverlapsUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        TOverlaps filter = ff.toverlaps(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    protected Literal newSampleDate() throws ParseException {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date dateTime = dateFormatter.parse("2006-11-30T01:30:00Z");
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        return ff.literal(dateTime);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBeginsUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Begins filter = ff.begins(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test
    public void testColorLiteral() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property("color"), ff.literal(Color.RED), false);

        FilterToCQL toCQL = new FilterToCQL();
        StringBuilder cql = (StringBuilder) toCQL.visit(filter, null);

        Assert.assertEquals("color = '#FF0000'", cql.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBegunByUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BegunBy filter = ff.begunBy(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnyInteractsUnsuported() throws Exception {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        AnyInteracts filter = ff.anyInteracts(ff.property("date"), newSampleDate());

        FilterToCQL toCQL = new FilterToCQL();
        filter.accept(toCQL, null);
    }

    @Test
    public void testIntersectsPoint() throws Exception {

        cqlTest("INTERSECTS(the_geom, POINT (1 2))");
    }

    @Test
    public void testIntersects() throws Exception {
        cqlTest("INTERSECTS(theGeom, POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0)))");
    }

    @Test
    public void testOverlaps() throws Exception {
        cqlTest("OVERLAPS(theGeom, POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0)))");
    }

    @Test
    public void testCrosses() throws Exception {
        cqlTest("CROSSES(theGeom, POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0)))");
    }

    @Test
    public void testContains() throws Exception {
        cqlTest("CONTAINS(theGeom, POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0)))");
    }

    @Test
    public void testTouches() throws Exception {
        cqlTest("TOUCHES(theGeom, POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0)))");
    }

    @Ignore // Parser doesn't implement this
    @Test
    public void testAttributeContainsQuote() throws Exception {
        String cql = "INTERSECTS(\"the\"\"geom\", POINT (1 2))";
        Filter filter = CQL.toFilter(cql);
        Assert.assertNotNull(filter);

        // double quote escaped by repeating should be unescaped to just one
        Assert.assertEquals("the\"geom", ((PropertyName) ((Intersects) filter).getExpression1()).getPropertyName());

        FilterToCQL toCQL = new FilterToCQL();
        String output = filter.accept(toCQL, null).toString();
        // Should be escaped again
        Assert.assertEquals(cql, output);
    }

    @Ignore // Parser doesn't implement this
    @Test
    public void testAttributeContainsSpace() throws Exception {
        cqlTest("INTERSECTS(\"the geom\", POINT (1 2))");
    }

    @Test
    public void testAttributeContainsOperator() throws Exception {
        cqlTest("INTERSECTS(\"the-geom\", POINT (1 2))");
    }

    @Test
    public void testAttributeContainsComma() throws Exception {
        cqlTest("INTERSECTS(\"the,geom\", POINT (1 2))");
    }

    @Test
    public void testAttributeIsReservedUpperCase() throws Exception {
        cqlTest("INTERSECTS(\"POINT\", POINT (1 2))");
    }

    @Test
    public void testAttributeIsReservedLowerCase() throws Exception {
        cqlTest("INTERSECTS(\"point\", POINT (1 2))");
    }

    @Test
    public void testAttributeEqualsBoolean() throws Exception {
        cqlTest("foo = true");
    }

    @Test
    public void testFunctionOr() throws Exception {
        // this is a contrived example, but it's a function that's available in this modules
        ecqlReparseTest("PropertyExists('name') = true OR PropertyExists('name') = false");
        ecqlReparseTest("PropertyExists('name') IN (true, false)");
    }

    protected void cqlTest(String cql) throws Exception {
        Filter filter = CQL.toFilter(cql);
        Assert.assertNotNull(filter);

        FilterToCQL toCQL = new FilterToCQL();
        String output = filter.accept(toCQL, null).toString();
        Assert.assertEquals(cql, output);
    }

    protected void ecqlReparseTest(String cql) throws Exception {
        Filter filter = ECQL.toFilter(cql);
        Assert.assertNotNull(filter);
        Assert.assertEquals(ECQL.toFilter(ECQL.toCQL(filter)), filter);
    }
}
