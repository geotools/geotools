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

import java.util.Arrays;

import org.geotools.factory.Hints;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * {@link FilterFactory} with support for registered functions.
 * 
 * <p>
 * 
 * This class overrides methods in {@link FilterFactoryImpl} to ensure a
 * {@link RegfuncFunctionFinder} is used, thus enabling registered function support.
 * 
 * <p>
 * 
 * TODO: pull this functionality up into {@link FilterFactoryImpl}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class RegfuncFilterFactoryImpl extends FilterFactoryImpl {

    private RegfuncFunctionFinder regfuncFunctionFinder = new RegfuncFunctionFinder(null);

    /**
     * Constructor.
     */
    public RegfuncFilterFactoryImpl() {
    }

    /**
     * Constructor.
     * 
     * @param hints
     */
    public RegfuncFilterFactoryImpl(Hints hints) {
        super(hints);
    }

    /**
     * @see org.geotools.filter.FilterFactoryImpl#function(java.lang.String,
     *      org.opengis.filter.expression.Expression[])
     */
    // @Override
    public Function function(String name, Expression[] args) {
        return regfuncFunctionFinder.findFunction(name, Arrays.asList(args));

    }

    /**
     * @see org.geotools.filter.FilterFactoryImpl#function(java.lang.String,
     *      org.opengis.filter.expression.Expression)
     */
    // @Override
    public Function function(String name, Expression arg1) {
        return regfuncFunctionFinder.findFunction(name, Arrays.asList(new Expression[] { arg1 }));

    }

    /**
     * @see org.geotools.filter.FilterFactoryImpl#function(java.lang.String,
     *      org.opengis.filter.expression.Expression, org.opengis.filter.expression.Expression)
     */
    // @Override
    public Function function(String name, Expression arg1, Expression arg2) {
        return regfuncFunctionFinder.findFunction(name, Arrays
                .asList(new Expression[] { arg1, arg2 }));
    }

    /**
     * @see org.geotools.filter.FilterFactoryImpl#function(java.lang.String,
     *      org.opengis.filter.expression.Expression, org.opengis.filter.expression.Expression,
     *      org.opengis.filter.expression.Expression)
     */
    // @Override
    public Function function(String name, Expression arg1, Expression arg2, Expression arg3) {
        return regfuncFunctionFinder.findFunction(name, Arrays.asList(new Expression[] { arg1,
                arg2, arg3 }));
    }

}
