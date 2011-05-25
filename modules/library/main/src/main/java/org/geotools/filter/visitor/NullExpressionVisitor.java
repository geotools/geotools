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
package org.geotools.filter.visitor;

import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

/**
 * This class does *nothing* - useful to prevent null checks in AbstractFilterVisitor.
 * 
 * @author Jody
 *
 *
 * @source $URL$
 */
public class NullExpressionVisitor implements ExpressionVisitor{

    public Object visit( NilExpression expression, Object extraData ) {
        return null;
    }

    public Object visit( Add expression, Object extraData ) {
        return null;
    }

    public Object visit( Divide expression, Object extraData ) {
        return null;
    }

    public Object visit( Function expression, Object extraData ) {
        return null;
    }

    public Object visit( Literal expression, Object extraData ) {
        return null;
    }

    public Object visit( Multiply expression, Object extraData ) {
        return null;
    }

    public Object visit( PropertyName expression, Object extraData ) {
        return null;
    }

    public Object visit( Subtract expression, Object extraData ) {
        return null;
    }

}
