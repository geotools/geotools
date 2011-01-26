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

/**
 * Abstract implementation of ExpressionVisitor that simply walks the data structure.
 * <p>
 * This class implements the full ExpressionVisitor interface and will visit every Filter member
 * of an Expression object. This class performs no actions and is not intended to be used directly,
 * instead
 * extend it and overide the methods for the Expression type you are interested in. Remember to call the
 * super method if you want to ensure that the entire expression tree is still visited.
 * 
 * <pre><code>
 * FilterVisitor allProperties = new DefaultExpressionVisitor(){
 *     public Object visit( PropertyName expr, Object data ) {
 *         Set set = (Set) data;
 *         set.addAll(expr.getPropertyName());
 *         return set;
 *     }
 * };
 * Set set = (Set) allProperties.accept(allFids, new HashSet());
 * </code></pre>
 * 
 * @author Jody
 *
 * @source $URL$
 */
public abstract class DefaultExpressionVisitor implements ExpressionVisitor {

    public DefaultExpressionVisitor() {
    }

    public Object visit( NilExpression expression, Object data ) {        
        return data;
    }

    public Object visit( Add expression, Object data ) {
        data = expression.getExpression1().accept( this, data);
        data = expression.getExpression2().accept( this, data);
        return data;
    }

    public Object visit( Divide expression, Object data ) {
        data = expression.getExpression1().accept( this, data);
        data = expression.getExpression2().accept( this, data);        
        return data;
    }

    public Object visit( Function expression, Object data ) {
        if( expression.getParameters() != null ){
            for( Expression parameter : expression.getParameters() ){
                data =  parameter.accept( this, data);
            }
        }
        return data;
    }

    public Object visit( Literal expression, Object data ) {        
        return data;
    }

    public Object visit( Multiply expression, Object data ) {
        data = expression.getExpression1().accept( this, data);
        data = expression.getExpression2().accept( this, data);                
        return data;
    }

    public Object visit( PropertyName expression, Object data ) {
        return data;
    }

    public Object visit( Subtract expression, Object data ) {
        data = expression.getExpression1().accept( this, data);
        data = expression.getExpression2().accept( this, data);                
        return data;
    }

}
