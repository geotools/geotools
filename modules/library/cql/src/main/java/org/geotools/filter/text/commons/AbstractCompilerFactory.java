/*
 *    GeoTools - The Open Source Java GIS Tookit
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

package org.geotools.filter.text.commons;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.FilterFactory;

/**
 * Provides the common behavior to make a compiler implementation
 * <p>
 * Warning: This component is not published. It is part of module implementation. 
 * Client module should not use this feature.
 * </p>
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 * @source $URL$
 */
public abstract class AbstractCompilerFactory {


    /**
     * Initializes and create the new compiler
     * 
     * @param predicate
     * @param filterFactory
     * @return CQLCompiler
     * @throws CQLException 
     */
    public ICompiler makeCompiler(final String predicate, final FilterFactory filterFactory) throws CQLException {

       
        FilterFactory ff = filterFactory;

        if (filterFactory == null) {
            ff = CommonFactoryFinder.getFilterFactory((Hints) null);
        }
        String clonePredicate = new String(predicate);
        ICompiler compiler = createCompiler(clonePredicate, ff);
        
        return compiler;
    }

    protected abstract ICompiler createCompiler(final String predicate,final FilterFactory filterFactory);
    
}
