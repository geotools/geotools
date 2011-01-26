/*
 *    GeoTools - The Open Source Java GIS Toolkit
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
 * 
 *    Created on 07 December 2004, 16:29
 */

package org.geotools.filter;

import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Literal;

/**
 * This class is actualy a place holder.  It resolves to 1.0 but should actualy be substituted for 
 * a literal that actualy contains the current map scale before use.
 * @author James
 * @source $URL$
 * 
 */
public class MapScaleDenominatorImpl extends DefaultExpression implements MapScaleDenominator, Literal {
    
    /** Creates a new instance of MapScaleDenominatorImpl */
    public MapScaleDenominatorImpl() {
        
    }
    
    public Object evaluate(Object f){
        return getValue();
    }
    
    public Object getValue() {
    	return new Double(1);
    }
    
    public void setValue(Object constant) {
    	throw new UnsupportedOperationException();
    }
    
    public Object accept(ExpressionVisitor visitor, Object extraData) {
    	return visitor.visit(this,extraData);
    }
    
    public String toString(){
        return MapScaleDenominator.EV_NAME;
    }
}
