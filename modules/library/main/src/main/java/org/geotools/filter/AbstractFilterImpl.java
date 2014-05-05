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
 *
 * @source $URL$
 * @version $Id$
 * @deprecated No longer required to implement and, or and not
 */
public abstract class AbstractFilterImpl
    extends org.geotools.filter.AbstractFilter {
   
	protected AbstractFilterImpl() {
	}

}
