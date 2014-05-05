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

import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
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
import org.opengis.filter.PropertyIsNil;
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
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;


/**
 * Base implementation of the FilterVisitor used for inorder traversal of expressions.
 * <p>
 * This class implements the full FilterVisitor interface and will visit every
 * member of a Filter object.  This class performs no actions and is not intended
 * to be used directly, instead extend it and overide the methods for the
 * expression types you are interested in.  Remember to call the super method
 * if you want to ensure that the entire filter tree is still visited.
 * </p>
 * <p>
 * You may still need to implement FilterVisitor directly if the visit order
 * set out in this class does not meet your needs.  This class visits in sequence
 * i.e. Left - Middle - Right for all expressions which have sub-expressions.
 * </p>
 * 
 * @author James Macgill, Penn State
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public class AbstractFilterVisitor implements FilterVisitor {

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
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor 
     * was set.
     */
    protected Object visit( BinaryComparisonOperator filter, Object data ) {
        if (expressionVisitor != null) {
            if (filter.getExpression1() != null) {
                filter.getExpression1().accept(expressionVisitor, data);
            }
            if (filter.getExpression2() != null) {
                filter.getExpression2().accept(expressionVisitor, data);
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
        if (expressionVisitor != null) {
            if (filter.getExpression1() != null) {
                filter.getExpression1().accept(expressionVisitor, data);
            }
            if (filter.getExpression2() != null) {
                filter.getExpression2().accept(expressionVisitor, data);
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
     * Visits filter.getExpression() if an expression visitor was set.
     */
    public Object visit( PropertyIsLike filter, Object data ) {
        if (expressionVisitor != null) {
            if (filter.getExpression() != null) {
                filter.getExpression().accept(expressionVisitor, null);
            }
        }
        return filter;
    }

    /**
     * Visits elements of filter.getChildren().
     */
    protected Object visit( BinaryLogicOperator filter, Object data ) {
        if (filter.getChildren() != null) {
            for (Iterator<Filter> i = filter.getChildren().iterator(); i.hasNext();) {
                Filter child = i.next();
                child.accept(this, data);
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
     * Visits filter.getExpression() if an expression visitor was set.
     */
    public Object visit(PropertyIsNil filter, Object extraData) {
        if ( expressionVisitor != null ) {
            if ( filter.getExpression() != null ) {
                filter.getExpression().accept( expressionVisitor, extraData );
            }
        }
        return filter;
    }

    /**
     * Does nothing.
     */
    public Object visit( Id filter, Object data ) {
    	//do nothing
    	return filter;
    }

    public Object visit(After after, Object extraData) {
        return visit((BinaryTemporalOperator)after, extraData);
    }

    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return visit((BinaryTemporalOperator)anyInteracts, extraData);
    }

    public Object visit(Before before, Object extraData) {
        return visit((BinaryTemporalOperator)before, extraData);
    }

    public Object visit(Begins begins, Object extraData) {
        return visit((BinaryTemporalOperator)begins, extraData);
    }

    public Object visit(BegunBy begunBy, Object extraData) {
        return visit((BinaryTemporalOperator)begunBy, extraData);
    }

    public Object visit(During during, Object extraData) {
        return visit((BinaryTemporalOperator)during, extraData);
    }

    public Object visit(EndedBy endedBy, Object extraData) {
        return visit((BinaryTemporalOperator)endedBy, extraData);
    }

    public Object visit(Ends ends, Object extraData) {
        return visit((BinaryTemporalOperator)ends, extraData);
    }

    public Object visit(Meets meets, Object extraData) {
        return visit((BinaryTemporalOperator)meets, extraData);
    }

    public Object visit(MetBy metBy, Object extraData) {
        return visit((BinaryTemporalOperator)metBy, extraData);
    }

    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return visit((BinaryTemporalOperator)overlappedBy, extraData);
    }

    public Object visit(TContains contains, Object extraData) {
        return visit((BinaryTemporalOperator)contains, extraData);
    }

    public Object visit(TEquals equals, Object extraData) {
        return visit((BinaryTemporalOperator)equals, extraData);
    }

    public Object visit(TOverlaps contains, Object extraData) {
        return visit((BinaryTemporalOperator)contains, extraData);
    }
    
    protected Object visit(BinaryTemporalOperator filter, Object data) {
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

    @Override
    public String toString() {
        String name = getClass().getSimpleName();
        return "AbstractFilterVisitor "+name+" [expressionVisitor=" + expressionVisitor + "]";
    }
    
}
