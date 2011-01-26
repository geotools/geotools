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
package org.geotools.filter;


import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterVisitor;

/**
 * Abstract implementation for Filter.
 *
 * @author Jody Garnett
 *
 * @source $URL$
 */
public abstract class FilterAbstract implements org.opengis.filter.Filter 
	 {
	
	/** filter factory **/
	protected org.opengis.filter.FilterFactory factory;
	
	/**
	 * @param factory FilterFactory injected into the filter.
	 */
	protected FilterAbstract(org.opengis.filter.FilterFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * Subclass should overrride.
	 * 
	 * Default value is false
	 */
	public boolean evaluate(SimpleFeature feature) {
		return evaluate((Object)feature);
	}
	
	/**
	 * Subclass should overrride.
	 *
	 * Default value is false
	 */
        /*force subclass to implement
	public boolean evaluate(Object object) {
		return false;
	}
        */
        
	/**
	 * Straight call throught to: evaulate( feature )
	 */
	public boolean accepts(SimpleFeature feature) {
		return evaluate( feature );
	}
	
	
	/** Subclass should override, default implementation just returns extraData */
	public Object accept(FilterVisitor visitor, Object extraData) {
		return extraData;
	}
	
	/**
	 * Helper method for subclasses to reduce null checks
	 * @param expression
	 * @param feature
	 * @return value or null
	 */
	protected Object eval( Expression expression, SimpleFeature feature ){
		if( expression == null || feature == null ) return null;
		return expression.evaluate( feature );
	}
	
	/**
	 * Helper method for subclasses to reduce null checks
     * 
	 * @param expression
	 * @param object
	 * @return value or null
	 */
	protected Object eval(org.opengis.filter.expression.Expression expression, Object object) {
		if( expression == null ) return null;
		Object value = expression.evaluate( object );
        //HACK as this method is used internally for filter 
        //evaluation comparisons, etc, they work over the
        //contents (i.e. comparing an attexpresion with a literal)
        //so, lacking a better way of doing so, I'm putting this
        //check here
        if(value instanceof org.opengis.feature.Attribute){
            value = ((org.opengis.feature.Attribute)value).getValue();
        }
        return value;
	}
	/**
	 * Helper method for subclasses to reduce null checks
	 * @param expression
	 * @param object
	 * @param context
	 * @return value or null
	 */
	protected Object eval(org.opengis.filter.expression.Expression expression, Object object, Class context) { 
		if ( expression == null ) return null;
		return expression.evaluate( object, context );
	}
}
