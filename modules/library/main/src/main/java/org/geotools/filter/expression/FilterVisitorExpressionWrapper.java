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
package org.geotools.filter.expression;

import org.geotools.filter.AttributeExpression;
import org.geotools.filter.FilterVisitor;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.LiteralExpression;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

/**
 * Wraps an instanceof of FilterVisitor in an ExpressionVisitor.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 * @source $URL$
 */
public class FilterVisitorExpressionWrapper implements ExpressionVisitor {

	FilterVisitor delegate;
	
	public FilterVisitorExpressionWrapper(FilterVisitor delegate) {
		this.delegate = delegate;
	}
	
	private Object visitMath(BinaryExpression expression, Object data) {
		if (expression instanceof org.geotools.filter.MathExpression) {
			delegate.visit((org.geotools.filter.MathExpression)expression);
		}
		
		return data;
	}
	
    public Object visit(NilExpression expression, Object extraData){
        return extraData;
    }
	public Object visit(Add expression, Object extraData) {
		return visitMath(expression,extraData);
	}

	public Object visit(Divide expression, Object extraData) {
		return visitMath(expression,extraData);
	}

	public Object visit(Multiply expression, Object extraData) {
		return visitMath(expression,extraData);
	}
	
	public Object visit(Subtract expression, Object extraData) {
		return visitMath(expression,extraData);
	}
	
	public Object visit(Function expression, Object extraData) {
		if (expression instanceof FunctionExpression) {
			delegate.visit((FunctionExpression)expression);
		}
		
		return extraData;
	}

	public Object visit(Literal expression, Object extraData) {
		if (expression instanceof LiteralExpression) {
			delegate.visit((LiteralExpression)expression);
		}
		
		return extraData;
	}

	public Object visit(PropertyName expression, Object extraData) {
		if (expression instanceof AttributeExpression) {
			 delegate.visit((AttributeExpression)expression);
		}
		
		return extraData;
	}

	

}
