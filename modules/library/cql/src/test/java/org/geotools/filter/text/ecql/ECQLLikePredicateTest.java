/*
 *    GeoTools - The Open Source Java GIS Tookit
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

package org.geotools.filter.text.ecql;

import org.geotools.filter.text.commons.CompilerUtil;
import org.geotools.filter.text.commons.Language;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql2.CQLLikePredicateTest;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Test for like predicate
 * 
 * <p>
 *
 * <pre>
 *  &lt;text predicate &gt; ::=
 *      &lt;expression &gt; [ NOT ] <b>LIKE</b>  &lt;character pattern &gt;
 *
 * </pre>
 * <p>
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 *
 * @source $URL$
 */
public class ECQLLikePredicateTest extends CQLLikePredicateTest {
    
    public ECQLLikePredicateTest(){
        super(Language.ECQL);
    }
    
    /**
     * Test Text Predicate
     * <p>
     * Sample: strConcat('aa', 'bbcc') like '%bb%'
     * </p>
     */
    @Test
    public void functionlikePredicate() throws Exception {

        // Like strConcat('aa', 'bbcc') like '%bb%'
        Filter resultFilter = CompilerUtil.parseFilter(this.language, FilterECQLSample.FUNCTION_LIKE_ECQL_PATTERN);

        Assert.assertNotNull("Filter expected", resultFilter);

        Assert.assertTrue(resultFilter instanceof PropertyIsLike);
        
        PropertyIsLike expected = (PropertyIsLike) FilterECQLSample.getSample(FilterECQLSample.FUNCTION_LIKE_ECQL_PATTERN);

        Assert.assertEquals("like filter was expected", expected, resultFilter);
        
        // test for strToUpperCase function
        resultFilter = ECQL.toFilter( "strToUpperCase(anAttribute) like '%BB%'"); 

        Assert.assertTrue(resultFilter instanceof PropertyIsLike);

        PropertyIsLike resultLike = (PropertyIsLike) resultFilter;
        
        Expression resultExpression = resultLike.getExpression();
        Assert.assertTrue(  resultExpression instanceof Function);
        
        Function resultFunction = (Function)resultExpression;
        Assert.assertEquals("strToUpperCase", resultFunction.getName());
        
        Assert.assertEquals( resultLike.getLiteral(),  "%BB%" );
        
    }
    
    /**
     * Test like using a pattern with spanish caracters 
     */
    @Test
    public void functionAndPatternWithSpanishCharacter() throws CQLException{

    	Filter resultFilter = ECQL.toFilter( "strToUpperCase(anAttribute) like '%año%'"); 

        Assert.assertTrue(resultFilter instanceof PropertyIsLike);

        PropertyIsLike resultLike = (PropertyIsLike) resultFilter;
        
        Expression resultExpression = resultLike.getExpression();
        Assert.assertTrue(  resultExpression instanceof Function);
        
        Function resultFunction = (Function)resultExpression;
        Assert.assertEquals("strToUpperCase", resultFunction.getName());
        
        Assert.assertEquals( resultLike.getLiteral(),  "%año%" );
        
    }
    
    /**
     * Test Text Predicate
     * <p>
     * Sample: 'aabbcc' like '%bb%'
     * </p>
     */
    @Test
    public void literallikePredicate() throws Exception {

        Filter resultFilter = CompilerUtil.parseFilter(this.language, FilterECQLSample.LITERAL_LIKE_ECQL_PATTERN);

        Assert.assertNotNull("Filter expected", resultFilter);

        Assert.assertTrue(resultFilter instanceof PropertyIsLike);
        
        PropertyIsLike expected = (PropertyIsLike) FilterECQLSample.getSample(FilterECQLSample.LITERAL_LIKE_ECQL_PATTERN);

        Assert.assertEquals("like filter was expected", expected, resultFilter);

    }

    @Test
    public void literalNotlikePredicate() throws Exception {

        Filter resultFilter = CompilerUtil.parseFilter(this.language, FilterECQLSample.LITERAL_NOT_LIKE_ECQL_PATTERN);

        Assert.assertNotNull("Filter expected", resultFilter);

        Assert.assertTrue(resultFilter instanceof Not);
        
        Not expected = (Not) FilterECQLSample.getSample(FilterECQLSample.LITERAL_NOT_LIKE_ECQL_PATTERN);
        
        Assert.assertTrue(expected.getFilter() instanceof PropertyIsLike);
        
        Assert.assertEquals("like filter was expected", expected, resultFilter);
    }
    
}
