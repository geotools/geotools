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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geotools.feature.NameImpl;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterVisitor;

/**
 * Abstract implementation for Filter.
 *
 * @author Jody Garnett
 *
 * @source $URL$
 */
public abstract class FilterAbstract implements org.opengis.filter.Filter {

    protected FilterAbstract() {
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
	
	/**
	 * Unpacks a value from an attribute container
	 * 
	 * @param value
	 * @return
	 */
	private Object unpack(Object value) {
	    
	    if (value instanceof org.opengis.feature.ComplexAttribute){
                Property simpleContent = ((org.opengis.feature.ComplexAttribute)value).getProperty(new NameImpl("simpleContent"));
                if (simpleContent == null) {
                    return null;
                } else {
                    return simpleContent.getValue();
                }
            }
            
            if(value instanceof org.opengis.feature.Attribute){
                return ((org.opengis.feature.Attribute)value).getValue();
            }
            
            return value;
	}
	
	/**
	 * Helper method for subclasses to reduce null checks and 
	 * automatically unpack values from attributes and collections
         * 
	 * @param expression
	 * @param object
	 * @return value or null
	 */
	@SuppressWarnings("unchecked")
        protected Object eval(org.opengis.filter.expression.Expression expression, Object object) {
		if( expression == null ) return null;
		Object value = expression.evaluate( object );
	
		if (value instanceof Collection) {
		    //unpack all elements
		    List<Object> list = new ArrayList<Object>();
		    for (Object member : (Collection<Object>) value) {
		        list.add(unpack(member));
		    }
		    return list;
		}
		            
                return unpack(value);
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
