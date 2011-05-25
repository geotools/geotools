/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 * Unit Test for Comparison Predicate
 * <p>
 * By default test executes the test parsing Comparison Predicate using
 * the CQL compiler.
 * </p>
 * <p>
 * The subclass could use this as default and add more test cases with different language. 
 * </p>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.5
 *
 *
 * @source $URL$
 */
public class CQLComparisonPredicateTest {
    
    protected final Language language;

    public CQLComparisonPredicateTest(){
        
        this(Language.CQL);
    }

    public CQLComparisonPredicateTest(final Language language){
        
        assert language != null: "language cannot be null value";
        
        this.language = language;
    }
    
    /**
     * Test Comparison Predicate
     * <p>
     *
     * <pre>
     * &lt;comparison predicate &gt; ::=
     *      &lt;attribute name &gt;  &lt;comp op &gt;  &lt;literal &gt;
     * </pre>
     *
     * </p>
     */
    @Test
    public void comparisonOperators() throws Exception {
        Filter expected;
        Filter actual;
        // attr1 < 5
        expected = FilterCQLSample.getSample(FilterCQLSample.LESS_FILTER_SAMPLE);

        actual = CompilerUtil.parseFilter(this.language, FilterCQLSample.LESS_FILTER_SAMPLE);

        Assert.assertNotNull("expects filter not null", actual);

        Assert.assertEquals("less than compare filter error", expected, actual);

        // attr1 <= 5
        expected = FilterCQLSample.getSample(FilterCQLSample.LESS_EQ_FILTER_SAMPLE);

        actual = CompilerUtil.parseFilter(this.language, FilterCQLSample.LESS_EQ_FILTER_SAMPLE);

        Assert.assertNotNull("expects filter not null", actual);

        Assert.assertEquals("less or equal compare filter error", expected, actual);

        // attr <> 5
        expected = FilterCQLSample.getSample(FilterCQLSample.NOT_EQ_FILTER);

        actual = CompilerUtil.parseFilter(this.language, FilterCQLSample.NOT_EQ_FILTER);

        Assert.assertNotNull("expects filter not null", actual);

        Assert.assertEquals("not equal compare filter error", expected, actual);

    }
    
    /**
     * Tests attribute names in comparison predicate
     * <pre>
     * Sample: gmd:aa:bb.gmd:cc.gmd:dd
     * </pre>
     * 
     * 
     * @throws Exception
     */
    @Test
    public void attributeName()throws Exception{
        
        // "gmd:aa:bb.gmd:cc.gmd:dd"
      final String prop = "gmd:aa:bb.gmd:cc.gmd:dd";
      final String propExpected = "gmd:aa:bb/gmd:cc/gmd:dd";
      
      Filter actual = CompilerUtil.parseFilter(this.language, prop + " < 100");
    
      Assert.assertTrue("PropertyIsLessThan filter was expected", actual instanceof PropertyIsLessThan);
    
      PropertyIsLessThan lessFilter = (PropertyIsLessThan) actual;
      Expression property = lessFilter.getExpression1();
    
      Assert.assertEquals(propExpected, property.toString());

    }
    
    

    /**
     * Test comparison Predicate.
     * <p>
     *
     * <pre>
     * &lt;comparison predicate &gt; ::=
     *      &lt;attrsibute name &gt;  &lt;comp op &gt;  &lt;literal &gt;
     * </pre>
     *
     * </p>
     */
    @Test
    public void propertyComparisonSimpleExpressions()
            throws Exception {
        Filter expected;
        Filter actual;

        expected = FilterCQLSample.getSample(FilterCQLSample.FILTER_SIMPLE_EXPR);
        actual = CompilerUtil.parseFilter(this.language, FilterCQLSample.FILTER_SIMPLE_EXPR);

        Assert.assertNotNull("expects filter not null", actual);

        Assert.assertEquals("not equal compare filter error", expected, actual);
    }
    
    /**
     * Comparison with boolean values
     * @throws Exception
     */
    @Test
    public void booleanLiteral() throws Exception {
       
        Filter filter;
        PropertyIsEqualTo eqFilter;
        
        //test true value
        filter = CompilerUtil.parseFilter(this.language, "attr = true");
        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filter;
        Assert.assertEquals("attr", ((PropertyName) eqFilter.getExpression1()).getPropertyName());
        Assert.assertEquals(Boolean.TRUE, ((Literal) eqFilter.getExpression2()).getValue());

        //test false value
        filter = CompilerUtil.parseFilter(this.language, "attr = false");
        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filter;
        Assert.assertEquals("attr", ((PropertyName) eqFilter.getExpression1()).getPropertyName());
        Assert.assertEquals(Boolean.FALSE, ((Literal) eqFilter.getExpression2()).getValue());
    }

    @Test
    public void LongLiteral() throws Exception {
       
        Filter filter;
        PropertyIsEqualTo eqFilter;
        
        //test true value
        final String expectedValue = Long.toString(Long.MAX_VALUE);
        filter = CompilerUtil.parseFilter(this.language, "attr = " + expectedValue );
        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof PropertyIsEqualTo);

        eqFilter = (PropertyIsEqualTo) filter;
        Assert.assertEquals("attr", ((PropertyName) eqFilter.getExpression1()).getPropertyName());
        Assert.assertEquals(Long.parseLong(expectedValue), ((Literal) eqFilter.getExpression2()).getValue());
    }

    @Test
    public void parenRoundtripExpression() throws Exception {
        // ATTR1 > ((1 + 2) / 3)
        testEqualsExpressions(FilterCQLSample.FILTER_WITH_PAREN_ROUNDTRIP_EXPR);

        // "ATTR1 < (1 + ((2 / 3) * 4))"
        testEqualsExpressions(FilterCQLSample.FILTER_WITH_NESTED_PAREN_EXPR);
    }

    @Test
    public void bracketRoundtripFilter() throws Exception {

        testEqualsExpressions(FilterCQLSample.FILTER_WITH_BRACKET_ROUNDTRIP_EXPR);

        Filter f1 = CompilerUtil.parseFilter(this.language,"[[[ X < 4 ] AND NOT [ Y < 4 ]] AND [ Z < 4 ]]");
        Assert.assertTrue(f1 instanceof And);
        
        Filter f2 = CompilerUtil.parseFilter(this.language, "[X<4 AND Y<4 ] OR Z<4");
        Or orf2 = (Or) f2;
        Filter leftf2 =  orf2.getChildren().get(0);
        Assert.assertTrue(leftf2 instanceof And);
        
        Filter rightf2 = orf2.getChildren().get(1);
        Assert.assertTrue(rightf2 instanceof PropertyIsLessThan);

        Filter f3 = CompilerUtil.parseFilter(this.language,"[([ X < 4 ] AND NOT [ Y < 4 ]) AND [ Z < 4 ]]");
        Assert.assertTrue(f3 instanceof And);

    }

    /**
     * General test for cql expressions
     *
     * @param cqlSample
     * @throws Exception
     */
    private void testEqualsExpressions(final String cqlSample)
        throws Exception {
        Filter expected = FilterCQLSample.getSample(cqlSample);
        Filter actual = CompilerUtil.parseFilter(language, cqlSample);

        Assert.assertNotNull("expects filter not null", actual);
        Assert.assertEquals("this is not the filter expected", expected, actual);
    }
    

}
