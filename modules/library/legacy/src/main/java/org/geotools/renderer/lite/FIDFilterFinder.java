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
 */
package org.geotools.renderer.lite;

import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterVisitor;
import org.geotools.filter.visitor.AbstractFilterVisitor;

/**
 * Quick check to see of a FIDFilter is found.
 * 
 * @deprecated Please use IdFinderFilterVisitor
 *
 * @source $URL$
 */
public class FIDFilterFinder extends AbstractFilterVisitor implements FilterVisitor {
	
	public boolean hasFIDFilter = false;
	
	public void visit(FidFilter filter)
	{
		hasFIDFilter = true;
	}
}
