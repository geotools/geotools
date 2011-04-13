package org.geotools.filter.visitor;

import org.opengis.filter.And;
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
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
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
 * SearchFilterVisitor is a base class used to optimize finding specific information
 * in the filter data structure. 
 * <p>
 * This differs slightly form the DefaultFilterVisitor case in that you can abandon the depth
 * first traversal at any point by returning true from found( object ).
 * <p>
 * Most implementations accept the default functionality which simply checks if data is non null.
 * This allows you to simply return an object from any method the moment you have found what
 * you are looking for.
 * 
 * @author Jody Garnett
 */
public abstract class AbstractSearchFilterVisitor implements FilterVisitor, ExpressionVisitor {
    /**
     * Check if data is found (ie non null).
     * @return true if the item is found and we can now stop
     */
    protected boolean found( Object data ){
        return data != null; // we found it!
    }
    
    public Object visit( ExcludeFilter filter, Object data ) {        
        return data;
    }

    public Object visit( IncludeFilter filter, Object data ) {
        return data;
    }

    public Object visit( And filter, Object data ) {        
        if( found( data )) {
            return data; // short cut 
        }
        if (filter.getChildren() != null) {
            for( Filter child : filter.getChildren()) {                
                data = child.accept(this, data);
                if( found(data)){
                    return data;
                }
            }
        }
        return data;
    }

    public Object visit( Id filter, Object data ) {
        return data;
    }

    public Object visit( Not filter, Object data ) {
        Filter child = filter.getFilter();
        if (child != null) {
            data = child.accept(this, data);
        }
        return data;
    }

    public Object visit( Or filter, Object data ) {
        if( found( data )) {
            return data; // short cut 
        }
        if (filter.getChildren() != null) {
            for( Filter child : filter.getChildren()) {
                data = child.accept(this, data);
            }
            if( found(data)){
                return data;
            }
        }
        return data;
    }

    public Object visit( PropertyIsBetween filter, Object data ) {
        data = filter.getLowerBoundary().accept(this, data);
        if( found(data)) return data;
        
        data = filter.getExpression().accept(this, data);
        if( found(data)) return data;
        
        data = filter.getUpperBoundary().accept(this, data);
        return data;
    }

    public Object visit( PropertyIsEqualTo filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    public Object visit( PropertyIsNotEqualTo filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    public Object visit( PropertyIsGreaterThan filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    public Object visit( PropertyIsGreaterThanOrEqualTo filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    public Object visit( PropertyIsLessThan filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    public Object visit( PropertyIsLessThanOrEqualTo filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    public Object visit( PropertyIsLike filter, Object data ) {
        data = filter.getExpression().accept(this, data);

        return data;
    }

    public Object visit( PropertyIsNull filter, Object data ) {
        data = filter.getExpression().accept(this, data);
        return data;
    }

    public Object visit( final BBOX filter, Object data ) {
        data = filter.getExpression1().accept( this, data );
        if( found(data)) return data;
        data = filter.getExpression2().accept( this, data );        
        return data;
    }

    public Object visit( Beyond filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);
        return data;
    }

    public Object visit( Contains filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);
        return data;
    }

    public Object visit( Crosses filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);
        return data;
    }

    public Object visit( Disjoint filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);
        return data;
    }

    public Object visit( DWithin filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);
        return data;
    }

    public Object visit( Equals filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);
        return data;
    }

    public Object visit( Intersects filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    public Object visit( Overlaps filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    public Object visit( Touches filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    public Object visit( Within filter, Object data ) {
        data = filter.getExpression1().accept(this, data);
        if( found(data)) return data;
        data = filter.getExpression2().accept(this, data);
        
        return data;
    }

    public Object visitNullFilter( Object data ) {
        return data;
    }

    public Object visit( NilExpression expression, Object data ) {        
        return data;
    }

    public Object visit( Add expression, Object data ) {
        data = expression.getExpression1().accept( this, data);
        if( found(data)) return data;
        data = expression.getExpression2().accept( this, data);
        return data;
    }

    public Object visit( Divide expression, Object data ) {
        data = expression.getExpression1().accept( this, data);
        if( found(data)) return data;
        data = expression.getExpression2().accept( this, data);        
        return data;
    }

    public Object visit( Function expression, Object data ) {
        if( found(data)) return data;
        if( expression.getParameters() != null ){
            for( Expression parameter : expression.getParameters() ){
                data =  parameter.accept( this, data);
                if( found(data)) return data;
            }
        }
        return data;
    }

    public Object visit( Literal expression, Object data ) {        
        return data;
    }

    public Object visit( Multiply expression, Object data ) {
        data = expression.getExpression1().accept( this, data);
        if( found(data)) return data;
        data = expression.getExpression2().accept( this, data);                
        return data;
    }

    public Object visit( PropertyName expression, Object data ) {
        return data;
    }

    public Object visit( Subtract expression, Object data ) {
        data = expression.getExpression1().accept( this, data);
        if( found(data)) return data;
        data = expression.getExpression2().accept( this, data);                
        return data;
    }
}
