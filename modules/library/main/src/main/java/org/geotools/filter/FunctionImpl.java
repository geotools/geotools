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
package org.geotools.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.filter.expression.ExpressionAbstract;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Default implementation of a Function; you may extend this class to
 * implement specific functionality.
 * <p>
 * 
 * @author Cory Horner, Refractions Research
 *
 *
 *
 * @source $URL$
 */
public class FunctionImpl extends ExpressionAbstract implements Function {

    /** function name **/
    String name;

    /** function params **/
    List<Expression> params = Collections.emptyList();
    
    Literal fallbackValue;
    
    /**
     * Gets the name of this function.
     *
     * @return the name of the function.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the function.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the function parameters.
     */
    public List<Expression> getParameters() {
        return new ArrayList<Expression>(params);
    }
    
    /**
     * Default implementation simply returns the fallbackValue.
     * <p>
     * Please override this method to produce a value based on the
     * provided arguments.
     * @param object Object being evaluated; often a Feature
     * @return value for the provided object
     */
    public Object evaluate(Object object) {
    	return fallbackValue.evaluate( object );
    }
    
    /**
     * Sets the function parameters.
     */
    @SuppressWarnings("unchecked")
    public void setParameters(List<Expression> params) {
        this.params = params == null? Collections.EMPTY_LIST : new ArrayList<Expression>(params);
    }

    public void setFallbackValue(Literal fallbackValue) {
        this.fallbackValue = fallbackValue;
    }
    
    public Literal getFallbackValue() {
        return fallbackValue;
    }
    
    public Object accept(ExpressionVisitor visitor, Object extraData) {
    	return visitor.visit( this, extraData );
    }	
}
