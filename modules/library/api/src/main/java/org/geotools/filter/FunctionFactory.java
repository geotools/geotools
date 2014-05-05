/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.feature.type.Name;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Factory interface for filter functions.
 * <p>
 * The factory provides a list of {#link FunctionName} describing the available functions. Each FunctionName provides details
 * of the name, expected arguments and return type.
 * <p>
 * Individual functions can be created using a name from this list, along with the required number of arguments and a fallback value.
 * <p>
 * Prior to GeoTools 2.7 functions were made available as individual factories using {@link FunctionExpression}.
 * @author Justin Deoliveira (Boundless)
 *
 * @since 2.7
 * @see Function
 * @source $URL$
 */
public interface FunctionFactory {

    /**
     * Returns the list of function names the factory provides. 
     * 
     * @return A list of function names, possibly empty, never null.
     */
    List<FunctionName> getFunctionNames();

    /**
     * Returns a function with the specified name.
     * 
     * @param name The name of the function
     * @param args Variable list of expression arguments for the function.
     * @param fallback A fallback literal to use in cases where the function does not exist or 
     *   can not be created. This argument may be {@code null}.
     */
    Function function(String name, List<Expression> args, Literal fallback);

    /**
     * Returns a function with the specified name.
     * 
     * @param name The name of the function
     * @param args Variable list of expression arguments for the function.
     * @param fallback A fallback literal to use in cases where the function does not exist or 
     *   can not be created. This argument may be {@code null}.
     */
    Function function(Name name, List<Expression> args, Literal fallback);

}
