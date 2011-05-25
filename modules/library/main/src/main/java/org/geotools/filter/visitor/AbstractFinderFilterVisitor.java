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
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Abstract FilterVisitor for answering yes / no questions about a filter.
 * <p>
 * These classes are not not stateless, they make use of an interal field
 * to track if something is found. The walk will be stopped and the value
 * returned.
 * 
 * @author Jody Garnett (Refractions Research)
 *
 *
 * @source $URL$
 */
public abstract class AbstractFinderFilterVisitor implements FilterVisitor, ExpressionVisitor {
    protected boolean found = false; 

    protected AbstractFinderFilterVisitor() {        
    }

    public boolean isFound() {
        return found;
    }
    public void clear(){
        found = false;
    }
    
    public Object visit( ExcludeFilter filter, Object data ) {
        return found;
    }

    public Object visit( IncludeFilter filter, Object data ) {
        return found;
    }

    public Object visit( And filter, Object data ) {
        if (filter.getChildren() != null) {
            for( Filter child : filter.getChildren()) {
                child.accept(this, data);
                if( found ) break;
            }
        }
        return found;
    }

    public Object visit( Id filter, Object data ) {
        return found;
    }

    public Object visit( Not filter, Object data ) {
        if (filter.getFilter() != null) {
            filter.getFilter().accept(this, data);
        }
        return found;
    }

    public Object visit( Or filter, Object data ) {
        if (filter.getChildren() != null) {
            for( Filter child : filter.getChildren()) {
                child.accept(this, data);
                if( found ) break;
            }
        }
        return found;
    }

    public Object visit( PropertyIsBetween filter, Object data ) {
        filter.getLowerBoundary().accept(this, data);
        filter.getExpression().accept(this, data);
        filter.getUpperBoundary().accept(this, data);
        return found;
    }

    public Object visit( PropertyIsEqualTo filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);

        return found;
    }

    public Object visit( PropertyIsNotEqualTo filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);

        return found;
    }

    public Object visit( PropertyIsGreaterThan filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);

        return found;
    }

    public Object visit( PropertyIsGreaterThanOrEqualTo filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);

        return found;
    }

    public Object visit( PropertyIsLessThan filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);

        return found;
    }

    public Object visit( PropertyIsLessThanOrEqualTo filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);

        return found;
    }

    public Object visit( PropertyIsLike filter, Object data ) {
        filter.getExpression().accept(this, data);

        return found;
    }

    public Object visit( PropertyIsNull filter, Object data ) {
        filter.getExpression().accept(this, data);
        return found;
    }

    public Object visit( final BBOX filter, Object data ) {
        // We will just use a simple wrapper until we add a getExpression method
        PropertyName property = new PropertyName(){
            public String getPropertyName() {
                return filter.getPropertyName();
            }
            public Object accept( ExpressionVisitor visitor, Object data ) {
                return visitor.visit(this, data);
            }
            public Object evaluate( Object object ) {
                return null;
            }
            public Object evaluate( Object object, Class context ) {
                return null;
            }

            public NamespaceSupport getNamespaceContext() {
                return null;
            }
        };
        property.accept(this, data);
        return found;
    }

    public Object visit( Beyond filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);
        return found;
    }

    public Object visit( Contains filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);
        return found;
    }

    public Object visit( Crosses filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);
        return found;
    }

    public Object visit( Disjoint filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);
        return found;
    }

    public Object visit( DWithin filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);
        return found;
    }

    public Object visit( Equals filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);
        return found;
    }

    public Object visit( Intersects filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);

        return found;
    }

    public Object visit( Overlaps filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);

        return found;
    }

    public Object visit( Touches filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);

        return found;
    }

    public Object visit( Within filter, Object data ) {
        filter.getExpression1().accept(this, data);
        filter.getExpression2().accept(this, data);
        
        return found;
    }

    public Object visitNullFilter( Object data ) {
        return found;
    }

    public Object visit( NilExpression expression, Object data ) {        
        return found;
    }

    public Object visit( Add expression, Object data ) {
        expression.getExpression1().accept( this, data);
        expression.getExpression2().accept( this, data);
        return found;
    }

    public Object visit( Divide expression, Object data ) {
        expression.getExpression1().accept( this, data);
        expression.getExpression2().accept( this, data);        
        return found;
    }

    public Object visit( Function expression, Object data ) {
        for( Expression parameter : expression.getParameters() ){
            data = parameter.accept( this, data);
        }
        return found;
    }

    public Object visit( Literal expression, Object data ) {        
        return found;
    }

    public Object visit( Multiply expression, Object data ) {
        expression.getExpression1().accept( this, data);
        expression.getExpression2().accept( this, data);                
        return found;
    }

    public Object visit( PropertyName expression, Object data ) {
        return found;
    }

    public Object visit( Subtract expression, Object data ) {
        expression.getExpression1().accept( this, data);
        expression.getExpression2().accept( this, data);                
        return found;
    }
}
