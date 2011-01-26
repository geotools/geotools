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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.opengis.filter.expression.Function;

/**
 * Test for {@link RegfuncFunctionFinder}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class RegfuncFunctionFinderTest extends TestCase {

    /**
     * Test that {@link RegfuncFunctionFinder} will invent a new function object for some name and
     * list of parameters.
     */
    public void testFindFunction() {
        FunctionFinder finder = new RegfuncFunctionFinder(null);
        String name = "made_up_function_name";
        List parameters = new ArrayList() {
            {
                add(new Integer(2));
                add("whatever");
            }
        };
        Function function = finder.findFunction(name, parameters);
        assertEquals(name, function.getName());
        assertEquals(parameters, function.getParameters());
        assertEquals(RegisteredFunction.class, function.getClass());
    }

}
