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
package org.geotools.filter.text.cql2;

import org.geotools.filter.text.commons.AbstractFilterBuilder;
import org.opengis.filter.FilterFactory;

/**
 * Filter builder for CQL expressions.
 * 
 * <p>
 * Builds Filter or Expression and their components (literal, functions, etc).
 * It maintains the results of semantic actions in the stack used to build complex
 * filters and expressions.
 * </p>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @version Revision: 1.9
 * @since 2.5
 */
final class CQLFilterBuilder extends AbstractFilterBuilder {
 
    /**
     * New instance of CQLFilterBuilder
     * @param cqlSource 
     * @param filterFactory
     */
    public CQLFilterBuilder(final String cqlSource, final FilterFactory filterFactory){
        
        
        super(cqlSource, filterFactory);

    }
    
}
