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

package org.geotools.filter;

import java.util.List;

/**
 * Function expression registered for database server execution.
 * 
 * <p>
 * 
 * There is no requirement that this function be registered in any data store for this class to be
 * instantiated. This class could have been called
 * OhNoWeDoNotImplementThisLetUsHopeItIsADatabaseRegisteredFunction, because we might not know when
 * it is instantiated if it is registered.
 * 
 * <p>
 * 
 * The number of arguments is deduced from the number provided. We hope they match.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class RegisteredFunction extends FunctionExpressionImpl {

    private final int argCount;

    public RegisteredFunction(String name, List parameters) {
        super(name);
        // assume that we are called with the correct number of arguments
        argCount = parameters.size();
        setParameters(parameters);
    }

    /**
     * Gets the number of arguments that are set.
     * 
     * @return the number of args.
     * @see org.geotools.filter.FunctionExpressionImpl#getArgCount()
     */
    public int getArgCount() {
        return argCount;
    }

}
