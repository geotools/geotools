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

import org.geotools.factory.Hints;
import org.opengis.filter.expression.Function;

/**
 * FunctionFinder with registered function support.
 * 
 * <p>
 * 
 * This class overrides the behaviour of {@link FunctionFinder} by returning a
 * {@link RegisteredFunction} if the requested function name is otherwise unknown. The effect of
 * this class is to assume that any unknown function will be implemented on the database server.
 * 
 * <p>
 * 
 * TODO: pull this functionality up into {@link FunctionFinder}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class RegfuncFunctionFinder extends FunctionFinder {

    /**
     * Constructor. See {@link FunctionFinder}.
     */
    public RegfuncFunctionFinder(Hints hints) {
        super(hints);
    }

    /**
     * Return the {@link Function} for the name and parameters, falling back to a
     * {@link RegisteredFunction} if the name is unknown.
     * 
     * @param name
     *                name of the function
     * @param parameters
     *                expression objects representing the parameters with which the function is to
     *                be called.
     * 
     * @see org.geotools.filter.FunctionFinder#findFunction(java.lang.String, java.util.List)
     */
    // @Override
    public Function findFunction(String name, List parameters) {
        try {
            return super.findFunction(name, parameters);
        } catch (RuntimeException e) {
            return new RegisteredFunction(name, parameters);
        }
    }

}
