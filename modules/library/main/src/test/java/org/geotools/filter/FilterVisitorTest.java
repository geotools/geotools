/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Created on July 18, 2003, 7:13 PM
 */

package org.geotools.filter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.filter.expression.AddImpl;
import org.geotools.filter.function.math.FilterFunction_min;

/**
 *
 * @author  jamesm
 *
 * @source $URL$
 */
public class FilterVisitorTest extends TestCase implements FilterVisitor {
    int checkcode;
    
      public FilterVisitorTest(String testName) {
        super(testName);
    }   
    /** 
     * Main for test runner.
     */
    public static void main(String[] args) {
        org.geotools.util.logging.Logging.GEOTOOLS.forceMonolineConsoleOutput();
        junit.textui.TestRunner.run(suite());
    }
    
    /** 
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        
        TestSuite suite = new TestSuite(FilterVisitorTest.class);
        return suite;
    }
  
    /**
     * This should never be called. This can only happen if a subclass of
     * AbstractFilter failes to implement its own version of
     * accept(FilterVisitor);
     *
     * @param filter The filter to visit
     */
    public void visit(Filter filter) {
        fail("should never end up here, overloaded method should have been called instead");
    }

   
    public void visit(BetweenFilter filter) {
        DefaultExpression left = (DefaultExpression) filter.getLeftValue();
        DefaultExpression right = (DefaultExpression) filter.getRightValue();
        DefaultExpression mid = (DefaultExpression) filter.getMiddleValue();
    }

    public void visit(LikeFilter filter) {
     
    }

    /**
     * Writes the SQL for the Logic Filter.
     *
     * @param filter the logic statement to be turned into SQL.
     */
    public void visit(LogicFilter filter) {
       
      


            java.util.Iterator list = filter.getFilterIterator();

            if (filter.getFilterType() == AbstractFilter.LOGIC_NOT) {
               
                Filters.accept((org.opengis.filter.Filter) list.next(),this);
                

            } else { //AND or OR
              
                while (list.hasNext()) {
                    Filters.accept((org.opengis.filter.Filter)list.next(),this);
                }

            }
        
    }

   
    public void visit(CompareFilter filter) {
       
        DefaultExpression left = (DefaultExpression) filter.getLeftValue();
        DefaultExpression right = (DefaultExpression) filter.getRightValue();
       
     

      
            left.accept(this);

            right.accept(this);
        
    }

    
    public void visit(GeometryFilter filter) {
        
    }

  
    public void visit(NullFilter filter) {


        DefaultExpression expr = (DefaultExpression) filter.getNullCheckValue();

       

                   expr.accept(this);
       
    }

	
	public void visit(FidFilter filter) {
            checkcode = 9;
	}

    public void visit(AttributeExpression expression) {
      
    }

    /**
     * Writes the SQL for the attribute Expression.
     *
     * @param expression the attribute to turn to SQL.
     */
    public void visit(Expression expression) {
       
    }

    
    public void visit(LiteralExpression expression) {
      checkcode+=1;
    }

   
    public void visit(MathExpression expression) {
        checkcode+=3;
            ((DefaultExpression) expression.getLeftValue()).accept(this);           
            ((DefaultExpression) expression.getRightValue()).accept(this);
    }

    public void visit(FunctionExpression expression) {
        checkcode+=6;
    }
    
    public void testVisitLiteral(){
        checkcode=0;
        LiteralExpression exp = new LiteralExpressionImpl(4);
        exp.accept(this);
        assertEquals(checkcode,1);
    
    }
    
     public void testVisitMathExpression() throws IllegalFilterException {
        checkcode=0;
        
        Expression testAttribute1 = new LiteralExpressionImpl(new Integer(4));
        Expression testAttribute2 = new LiteralExpressionImpl(new Integer(2));
        
        // Test addition
        MathExpressionImpl mathTest = new AddImpl(null,null);
        mathTest.addLeftValue(testAttribute1);
        mathTest.addRightValue(testAttribute2);
        
        mathTest.accept(this);
        
        assertEquals(5,checkcode);
    }
     
    public void testVisitFidFilter() throws IllegalFilterException {
        checkcode = 0;
        FidFilter ff = new FidFilterImpl();
        ff.accept(this);
        assertEquals(9,checkcode);
    }
    
    public void testFunctionExpression() {
        checkcode = 0;
        
        FunctionExpression min = new FilterFunction_min();
        min.accept(this);
        assertEquals(6,checkcode);
    }
        
        
}
