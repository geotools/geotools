/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.ecql;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLComparisonPredicateTest;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;


/**
 * ECQL Comparison Predicate Test Case.
 * <p>
 * The implementation must parse comparison predicate using the following grammar rule:
 * <pre>
 * &lt comparison predicate &gt ::= &lt expression &gt &lt comp op &gt &lt expression &gt
 * </pre>
 * </p>
 * <p>
 * This test case extends the from CQL test in order to assure that this extension (ECQL) contains
 * the base language (CQL).
 * </p>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 * @source $URL$
 */
public class ECQLComparisonPredicateTest extends CQLComparisonPredicateTest {

    public ECQLComparisonPredicateTest() {
        // sets the language used to execute this test case
        super(Language.ECQL);
    }

    /**
     * Test: Expression on the Left hand of comparison predicate
     * 
     * <pre>
     * Sample: (1+3) > aProperty
     *         (1+3) > (4-5)
     * </pre>
     * 
     * @throws CQLException
     */
    @Test 
    public void expressionComparisonProperty() throws CQLException {

        // (1+3) > aProperty
        testComparison(FilterECQLSample.EXPRESION_GREATER_PROPERTY);

        // (1+3) > (4-5)
        testComparison(FilterECQLSample.ADD_EXPRESION_GREATER_SUBTRACT_EXPRESION);
    
        // (x+3) > (y-5)
        testComparison(FilterECQLSample.EXPRESSIONS_WITH_PROPERTIES);
    }

    
    
    /**
     * Negative value test
     * 
     * @throws CQLException
     */
    @Test
    public void negativeNumber() throws CQLException {

        // minus integer
        //aProperty > -1
        testComparison(FilterECQLSample.PROPERTY_GREATER_MINUS_INGEGER);

        //-1 > aProperty 
        testComparison(FilterECQLSample.MINUS_INTEGER_GREATER_PROPERTY);

        // minus float
        // aProperty > -1.05 
        testComparison(FilterECQLSample.PROPERTY_GREATER_MINUS_FLOAT);

        // -1.05 > aProperty
        testComparison(FilterECQLSample.MINUS_FLOAT_GREATER_PROPERTY);
        
        // -1.05 + 4.6 > aProperty
        testComparison(FilterECQLSample.MINUS_EXPR_GREATER_PROPERTY);

        //  aProperty > -1.05 + 4.6
        testComparison(FilterECQLSample.PROPERTY_GREATER_MINUS_EXPR);
        
        // -1.05 + (-4.6* -10) > aProperty 
        testComparison(FilterECQLSample.PROPERTY_GREATER_NESTED_EXPR);
        
        // 10--1.05 > aProperty
        testComparison(FilterECQLSample.MINUS_MINUS_EXPR_GRATER_PROPERTY);

    }
    
    /**
     * Test: function on the Left hand of comparison predicate
     * <pre>
     * Samples:
     *          abs(10) < aProperty
     *          area( the_geom ) < 30000
     *          area( the_geom ) < (1+3)
     *          area( the_geom ) < abs(10)
     *
     * </pre>
     * @throws CQLException
     */
    @Test
    public void functionsInComparison() throws CQLException {
        
        //abs(10) < aProperty
        testComparison(FilterECQLSample.ABS_FUNCTION_LESS_PROPERTY);

        // area( the_geom ) < 30000
        testComparison(FilterECQLSample.AREA_FUNCTION_LESS_NUMBER);
        
        // area( the_geom ) < (1+3)
        testComparison(FilterECQLSample.FUNCTION_LESS_SIMPLE_ADD_EXPR);
        
        // area( the_geom ) < abs(10)
        testComparison(FilterECQLSample.FUNC_AREA_LESS_FUNC_ABS);
    }
    
    /**
     * Asserts that the filter returned is the specified by the predicate 
     * 
     * @param testPredicate predicate to test
     * @throws CQLException
     */
    private void testComparison(final String testPredicate ) throws CQLException{
        
        Filter expected = FilterECQLSample.getSample(testPredicate);

        Filter actual = CompilerUtil.parseFilter(this.language, testPredicate);

        Assert.assertNotNull("expects filter not null", actual);

        Assert.assertEquals("compare filter error", expected, actual);
    }
}
