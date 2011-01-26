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
package org.geotools.filter.visitor;

import java.util.Iterator;
import java.util.logging.Logger;

import org.geotools.filter.AttributeExpression;
import org.geotools.filter.BetweenFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.Filters;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.LikeFilter;
import org.geotools.filter.LiteralExpression;
import org.geotools.filter.LogicFilter;
import org.geotools.filter.MathExpression;
import org.geotools.filter.NullFilter;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;


/**
 * A basic implementation of the FilterVisitor interface.
 * <p>
 * This class implements the full FilterVisitor interface and will visit every
 * member of a Filter object.  This class performs no actions and is not intended
 * to be used directly, instead extend it and overide the methods for the
 * expression types you are interested in.  Remember to call the super method
 * if you want to ensure that the entier filter tree is still visited.
 * </p>
 * <p>
 * You may still need to implement FilterVisitor directly if the visit order
 * set out in this class does not meet your needs.  This class visits in sequence
 * i.e. Left - Middle - Right for all expressions which have sub-expressions.
 * </p>
 * @deprecated Please use DefaultFilterVisitor (to stick with only opengis Filter)
 * @author James Macgill, Penn State
 * @author Justin Deoliveira, The Open Planning Project
 * @source $URL$
 */
public class AbstractFilterVisitor implements org.geotools.filter.FilterVisitor, FilterVisitor {
    
    /** Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter.visitor");

    /** expression visitor */
    private ExpressionVisitor expressionVisitor;
    
    /**
     * Empty constructor
     */
    public AbstractFilterVisitor() {
        this( new NullExpressionVisitor() );
    }

    /**
     * Constructs the filter visitor with an expression visitor.
     * <p>
     * Using this constructor allows expressions of a filter to be visited as well.
     * </p>
     * @param expressionVisitor
     */
    public AbstractFilterVisitor( ExpressionVisitor expressionVisitor ) {
    	this.expressionVisitor = expressionVisitor;
    }
    
    /**
     * Does nothing; will return provided data unmodified.
     */
    public Object visit(IncludeFilter filter, Object data) {
    	return data;
    }
    
    /**
     * Does nothing; will return provided data unmodified.
     */
    public Object visit(ExcludeFilter filter, Object data) {
    	return data;
    }
    
    /**
     * Does nothing.
     */
    public Object visitNullFilter(Object data) {
    	return null;
    }
    
    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.Filter)
     * @deprecated
     */
    public void visit(Filter filter) {
       // James - unknown filter type (not good, should not happen)
    }

    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.BetweenFilter)
     * @deprecated use {@link #visit(PropertyIsBetween, Object)}
     */
    public void visit(BetweenFilter filter) {
        if (filter.getLeftValue() != null) {
            filter.getLeftValue().accept(this);
        }

        if (filter.getMiddleValue() != null) {
            filter.getMiddleValue().accept(this);
        }
        
        if (filter.getRightValue() != null) {
            filter.getRightValue().accept(this);
        } 
    }

    /**
     * Visits filter.getLowerBoundary(),filter.getExpression(),filter.getUpperBoundary() if an 
     * expression visitor was set.
     */
    public Object visit(PropertyIsBetween filter, Object data) {
    	if ( filter.getLowerBoundary() != null ) {
    		filter.getLowerBoundary().accept( expressionVisitor, data );
    	}
    	if ( filter.getExpression() != null ) {
    		filter.getExpression().accept( expressionVisitor, data );
    	}
    	if ( filter.getUpperBoundary() != null ) {
    		filter.getUpperBoundary().accept( expressionVisitor, data );
    	}    	
    	return filter;
    }
    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.CompareFilter)
     * @deprecated use one of {@link #visit(PropertyIsEqualTo, Object)}, 
     * 	{@link #visit(PropertyIsNotEqualTo, Object)}, {@link #visit(PropertyIsLessThan, Object)},
     *  {@link #visit(PropertyIsLessThanOrEqualTo, Object)},{@link #visit(PropertyIsGreaterThan, Object)},
     *  {@link #visit(PropertyIsGreaterThanEqualTo, Object)}
     */
    public void visit(CompareFilter filter) {
        if (filter.getLeftValue() != null) {
            filter.getLeftValue().accept(this);
        }

        if (filter.getRightValue() != null) {
            filter.getRightValue().accept(this);
        }
    }

    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor 
     * was set.
     */
    protected Object visit( BinaryComparisonOperator filter, Object data ) {
    	if ( expressionVisitor != null ) {
    		if ( filter.getExpression1() != null ) {
        		filter.getExpression1().accept( expressionVisitor , data );
        	}
        	if ( filter.getExpression2() != null ) {
        		filter.getExpression2().accept( expressionVisitor , data );
        	}	
    	}
    	
    	return filter;
    }
    
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor 
     * was set.
     */
    public Object visit(PropertyIsEqualTo filter, Object data) {
    	return visit( (BinaryComparisonOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor 
     * was set.
     */
    public Object visit(PropertyIsNotEqualTo filter, Object data) {
    	return visit( (BinaryComparisonOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor 
     * was set.
     */
    public Object visit(PropertyIsLessThan filter, Object data) {
    	return visit( (BinaryComparisonOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor 
     * was set.
     */
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object data) {
    	return visit( (BinaryComparisonOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor 
     * was set.
     */
    public Object visit(PropertyIsGreaterThan filter, Object data) {
    	return visit( (BinaryComparisonOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor 
     * was set.
     */
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object data) {
    	return visit( (BinaryComparisonOperator) filter, data );
    }
    
    
    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.GeometryFilter)
     */
    public void visit(GeometryFilter filter) {
        if (filter.getLeftGeometry() != null) {
            filter.getLeftGeometry().accept(this);
        }

        if (filter.getRightGeometry() != null) {
            filter.getRightGeometry().accept(this);
        }
    }
    
    /**
     * does nothing
     */
    public Object visit(BBOX filter, Object data) {
        return visit((BinarySpatialOperator)filter, data);
    }
    
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    protected Object visit( BinarySpatialOperator filter, Object data ) {
    	if ( expressionVisitor != null ) {
    		if ( filter.getExpression1() != null ) {
    			filter.getExpression1().accept( expressionVisitor, data );
    		}
    		if ( filter.getExpression2() != null ) {
    			filter.getExpression2().accept( expressionVisitor, data );
    		}
    	}
    	
    	return filter;
    }
    
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(Beyond filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(Contains filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(Crosses filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(Disjoint filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(DWithin filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(Equals filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(Intersects filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(Overlaps filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(Touches filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been 
     * set.
     */
    public Object visit(Within filter, Object data) {
    	return visit( (BinarySpatialOperator) filter, data );
    }

    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LikeFilter)
     * @deprecated use {@link #visit(PropertyIsLike, Object)}
     */
    public void visit(LikeFilter filter) {
        if (filter.getValue() != null) {
            filter.getValue().accept(this);
        }
    }

    /**
     * Visits filter.getExpression() if an expression visitor was set.
     */
    public Object visit( PropertyIsLike filter, Object data ) {
    	if ( expressionVisitor != null ) {
    		if ( filter.getExpression() != null ) {
    			filter.getExpression().accept( expressionVisitor, null );	
    		}
    	}
    	
    	return filter;
    }
    
    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LogicFilter)
     * @deprecated use one of {@link #visit(And, Object)},{@link #visit(Or, Object)},
     * 	{@link #visit(Not, Object)}
     */
    public void visit(LogicFilter filter) {
        for (Iterator it = filter.getFilterIterator(); it.hasNext();) {
            Filters.accept((org.opengis.filter.Filter)it.next(),this);
        }
    }

    /**
     * Visits elements of filter.getChildren().
     */
    protected Object visit( BinaryLogicOperator filter, Object data ) {
    	if ( filter.getChildren() != null ) {
    		for ( Iterator i = filter.getChildren().iterator(); i.hasNext(); ) {
        		org.opengis.filter.Filter child = (org.opengis.filter.Filter) i.next();
        		child.accept( this, data );
        	}	
    	}
    	
    	return filter;
    }
    
    /**
     * Visits elements of filter.getChildren().
     */
    public Object visit(And filter, Object data) {
    	return visit( (BinaryLogicOperator) filter, data );
    }
    /**
     * Visits elements of filter.getChildren().
     */
    public Object visit(Or filter, Object data) {
    	return visit( (BinaryLogicOperator) filter, data );
    }
    
    /**
     * Visits filter.getFilter().
     */
    public Object visit(Not filter, Object data) {
    	if ( filter.getFilter() != null ) {
    		filter.getFilter().accept( this, data );
    	}
    	
    	return filter;
    }
    
    
    
    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.NullFilter)
     * @deprecated use {@link #visit(PropertyIsNull, Object)}
     */
    public void visit(NullFilter filter) {
        if (filter.getNullCheckValue() != null) {
            filter.getNullCheckValue().accept(this);
        }
    }

    /**
     * Visits filter.getExpression() if an expression visitor was set.
     */
    public Object visit(PropertyIsNull filter, Object data) {
    	if ( expressionVisitor != null ) {
    		if ( filter.getExpression() != null ) {
    			filter.getExpression().accept( expressionVisitor, data );	
    		}
    	}
    	return filter;
    }
    
    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.FidFilter)
     * @deprecated use {@link #visit(Id, Object)} 
     */
    public void visit(FidFilter filter) {
        // nothing to do, the feature id is implicit and should always be
        // included, but cannot be derived from the filter itself 
    }

    /**
     * Does nothing.
     */
    public Object visit( Id filter, Object data ) {
    	//do nothing
    	return filter;
    }
    
    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.AttributeExpression)
     */
    public void visit(AttributeExpression expression) {
       //nothing to do
    }

    
    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.Expression)
     */
    public void visit(Expression expression) {
      // nothing to do
    }

    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.LiteralExpression)
     */
    public void visit(LiteralExpression expression) {
        // nothing to do
    }

    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.MathExpression)
     */
    public void visit(MathExpression expression) {
        if (expression.getLeftValue() != null) {
            expression.getLeftValue().accept(this);
        }

        if (expression.getRightValue() != null) {
            expression.getRightValue().accept(this);
        }
    }

    /**
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.FunctionExpression)
     */
    public void visit(FunctionExpression expression) {
        Expression[] args = expression.getArgs();

        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                args[i].accept(this);
            }
        }
    }
}
