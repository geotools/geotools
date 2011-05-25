/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.filter.Filter;

/**
 * Provides access to certain transaction state for the {@link PostPreProcessFilterSplittingVisitor}
 * and {@link CapabilitiesFilterSplitter}. This is only required if the transaction is kept on the
 * client and the server is unaware of it. For example PostGIS would not need to create one.
 * 
 * @author Jesse
 *
 *
 * @source $URL$
 */
public interface ClientTransactionAccessor {

	/**
	 * Returns all the filters indicating deleted feature ANDed together.  This is used to tell the server what features
	 * to NOT return.
	 * 
	 * @return all the filters indicating deleted feature ANDed together. 
	 */
	Filter getDeleteFilter();

	/**
	 * Returns all the filters of updates that affect the attribute in the expression ORed together.
	 * 
	 * @param attributePath the xpath identifier of the attribute.
	 * @return all the filters of updates that affect the attribute in the expression ORed together.
	 */
	Filter getUpdateFilter(String attributePath);

}
