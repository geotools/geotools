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
package org.geotools.filter.text.ecql;

import java.util.List;

import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.IsNullImpl;
import org.geotools.filter.function.PropertyExistsFunction;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Intersects;

/**
 * ECQL Test Case.
 * <p>
 * Test for the implementation of {@link ECQL} facade. This facade is the package interface and 
 * provides access to all functionalities of the ECQL parser. 
 * </p>
 * <p>
 * The samples are intended as an overview of <b>ECQL language </b> scope. 
 * </p>
 * <p>
 * The test method provide a basic test for each kind of ECQL predicates. 
 * The rest of test case classes included in this package implements a 
 * detailed test for each predicate.
 * </p>
 *
 * @author Jody Garnett 
 * @author Mauricio Pazos (Axios Engineering)
 *
 *
 *
 * @source $URL$
 * @version Revision: 1.9
 * @since 2.6
 */
public final class ECQLTest  {
    
    
    /**
     * Between predicate sample
     * 
     * @see ECQLBetweenPredicateTest
     * 
     * @throws CQLException
     */
    @Test
    public void betweenPredicate() throws CQLException{

        Filter filter = ECQL.toFilter("ATTR1 BETWEEN 10 AND 20");
        
        Assert.assertTrue(filter instanceof PropertyIsBetween);
    }

    /**
     * Equals predicate sample
     * 
     * @see ECQLComparisonPredicateTest
     * 
     * @throws Exception
     */
    @Test
    public void comparisonPredicate() throws Exception{

        Filter filter; 
        
        filter = ECQL.toFilter("POP_RANK > 6");
        
        Assert.assertTrue(filter instanceof PropertyIsGreaterThan);

        filter = ECQL.toFilter("area(the_geom) < 3000");
        
        Assert.assertTrue(filter instanceof PropertyIsLessThan);
    }
// TODO the syntax is under debate. It will be eliminate from release    
//    /**
//     * Spatial relate like Intersection Matrix (DE-9IM)
//     * 
//     * @see ECQLRelateLikePatternTest
//     * 
//     * @throws Exception
//     */
//    @Test
//    public void relateLikePattern() throws Exception{
//        
//        Filter filter = ECQL.toFilter("relate( the_geom1,the_geom2) like 'T**F*****'");
//
//        Assert.assertTrue(filter instanceof PropertyIsEqualTo );
//        
//        PropertyIsEqualTo eq = (PropertyIsEqualTo) filter;
//        Assert.assertTrue(eq.getExpression1()  instanceof FilterFunction_relatePattern);
//    }
    

    /**
     * GeoOperation predicate sample
     * 
     * @see ECQLGeoOperationTest
     * 
     * @throws CQLException
     */
    @Test
    public void geoOperationPredicate() throws CQLException{
        
        Filter filter;
        
        filter = ECQL.toFilter("INTERSECTS(the_geom, POINT(1 2))");

        Assert.assertTrue("Disjoint was expected", filter instanceof Intersects);

        filter = ECQL.toFilter("INTERSECTS(buffer(the_geom, 10) , POINT(1 2))");

        Assert.assertTrue("Disjoint was expected", filter instanceof Intersects);
    }
    
    @Test
    public void functionDwithinGeometry() throws Exception{
        Filter resultFilter;

        // DWITHIN
        resultFilter = ECQL.toFilter(
                "DWITHIN(buffer(the_geom,5), POINT(1 2), 10, kilometers)");

        Assert.assertTrue(resultFilter instanceof DistanceBufferOperator);
    }

    /**
     * Temporal predicate sample
     * 
     * @see ECQLTemporalPredicateTest
     * 
     * @throws Exception
     */
    @Test
    public void temporalPredicate() throws Exception{

        Filter filter = ECQL.toFilter("ATTR1 BEFORE 2006-12-31T01:30:00Z");

        Assert.assertTrue( filter instanceof PropertyIsLessThan);
    }

    /**
     * And / Or / Not predicate
     * @throws Exception 
     * 
     * @see ECQLBooleanValueExpressionTest
     */
    @Test
    public void booleanPredicate() throws Exception{

        Filter  filter;
       
        // and sample
        filter = ECQL.toFilter("ATTR1 < 10 AND ATTR2 < 2 ");
       
        Assert.assertTrue(filter instanceof And);
        
       // or sample
        filter = ECQL.toFilter("ATTR1 < 10 OR ATTR2 < 2 ");
     
        Assert.assertTrue(filter instanceof Or);

        // not sample
        filter = ECQL.toFilter("NOt ATTR < 10");
        
        Assert.assertTrue(filter instanceof Not);
    }
    
    /**
     * Id predicate sample
     * 
     * @see ECQLIDPredicateTest
     * 
     * @throws Exception
     */
    @Test 
    public void idPredicate() throws Exception {
        
        Filter filter = ECQL.toFilter("IN ('river.1', 'river.2')");
        Assert.assertTrue(filter instanceof Id);

        
    }
    
    /**
     * in predicate sample
     * @throws CQLException 
     * 
     * @see ECQLINPredicateTest
     */
    @Test
    public void inPredicate() throws CQLException{
        
        Filter filter = ECQL.toFilter("length IN (4100001,4100002, 4100003 )");
        
        Assert.assertTrue(filter instanceof Or);
    }
    
    /**
     * Like predicate sample
     * 
     * @see ECQLLikePredicateTest
     * 
     * @throws Exception
     */
    @Test 
    public void likePredicate() throws Exception{
        
        // using a property as expression
        Filter filter = ECQL.toFilter("aProperty like '%bb%'");
        
        Assert.assertTrue(filter instanceof PropertyIsLike);
           
        // using function as expression
        filter = ECQL.toFilter( "strToUpperCase(anAttribute) like '%BB%'");
        
        Assert.assertTrue(filter instanceof PropertyIsLike);

        PropertyIsLike like = (PropertyIsLike) filter;
        
        Assert.assertTrue(like.getExpression() instanceof Function );
    }
    
    /**
     * Null predicate sample
     * 
     * @see ECQLNullPredicateTest
     * 
     * @throws Exception
     */
    @Test
    public void isNullPredicate() throws Exception {
        
        Filter filter = ECQL.toFilter("centroid( the_geom ) IS NULL");
        
        Assert.assertTrue(filter instanceof IsNullImpl);
     
        
    }

    /**
     * Exist property predicate sample
     * 
     * @see ECQLExistenceTest
     * @throws Exception 
     */
    @Test
    public void existProperty() throws Exception{

        Filter resultFilter = ECQL.toFilter("aProperty EXISTS");

        Assert.assertTrue(resultFilter instanceof PropertyIsEqualTo);
        
        PropertyIsEqualTo eq = (PropertyIsEqualTo) resultFilter;
        
        Expression expr = eq.getExpression1() ;

        Assert.assertTrue(expr instanceof PropertyExistsFunction);
        
    }
    
    @Test
    public void expression() throws Exception{

        Expression expr = ECQL.toExpression("A + 1");
        
        Assert.assertTrue(expr instanceof Add);
    }
    
    @Test
    public void listOfPredicates() throws Exception{

        List<Filter> list = ECQL.toFilterList("A=1; B<4");
        
        Assert.assertTrue(list.size() == 2);
        
        Assert.assertTrue(list.get(0) instanceof PropertyIsEqualTo );
        
        Assert.assertTrue(list.get(1) instanceof PropertyIsLessThan );
    }
    
    
    /**
     * Verify the parser uses the provided FilterFactory implementation
     * @throws Exception
     */
    @Test
    public void toFilterUsesProvidedFilterFactory() throws Exception {
        final boolean[] called = { false };
        
        FilterFactory ff = new FilterFactoryImpl() {
                public PropertyName property(String propName) {
                    called[0] = true;

                    return super.property(propName);
                }
            };

        ECQL.toFilter("attName > 20", ff);
        Assert.assertTrue("Provided FilterFactory was not called", called[0]);
    }
    /**
     * Verify the parser uses the provided FilterFactory implementation
     * @throws Exception
     */
    @Test
    public void toExpressionUsesProvidedFilterFactory() throws Exception {
        final boolean[] called = { false };
        
        FilterFactory ff = new FilterFactoryImpl() {
                public PropertyName property(String propName) {
                    called[0] = true;

                    return super.property(propName);
                }
            };

        ECQL.toExpression("attName", ff);
        Assert.assertTrue("Provided FilterFactory was not called", called[0]);
    }
    
}
