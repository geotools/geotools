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
 *    
 *    Created on 23 October 2002, 17:19
 */
package org.geotools.filter;

import org.opengis.filter.FilterFactory;


/**
 * Abstract filter implementation provides or and and methods for child filters
 * to use.
 *
 * @author Ian Turton, CCG
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class AbstractFilterImpl
    extends org.geotools.filter.AbstractFilter {
   
	protected AbstractFilterImpl(FilterFactory factory) {
		super(factory);
	}

	/**
     * Default implementation for OR - should be sufficient for most filters.
     *
     * @param filter Parent of the filter: must implement GMLHandlerGeometry.
     *
     * @return ORed filter.
     */
    public Filter or(org.opengis.filter.Filter filter) {
        try {
        	return (Filter) factory.or(this,filter);
        } catch (IllegalFilterException ife) {
            return (Filter) filter;
        }
    }

    /**
     * Default implementation for AND - should be sufficient for most filters.
     *
     * @param filter Parent of the filter: must implement GMLHandlerGeometry.
     *
     * @return ANDed filter.
     */
    public Filter and(org.opengis.filter.Filter filter) {
        try {
            return (Filter) factory.and(this, filter);
        } catch (IllegalFilterException ife) {
            return (Filter) filter;
        }
    }

    /**
     * Default implementation for NOT - should be sufficient for most filters.
     *
     * @return NOTed filter.
     */
    public Filter not() {
        try {
            return (Filter) factory.not( this );
        } catch (IllegalFilterException ife) {
            return this;
        }
    }
}
