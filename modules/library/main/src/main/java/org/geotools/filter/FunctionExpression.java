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
package org.geotools.filter;

import java.util.List;
import org.geotools.util.factory.Factory;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

/**
 * Quick Function implementation for direct use as a factory.
 * <p>
 * Functions are published as part of DefaultFuntionFactory using the following workflow:
 * <ul>
 * <li>Implementation listed with Service Provider Interface <code>org.opengis.filter.Function</code></li>
 * <li>Object created using a no argument constructor (or a constructor that takes hints). {@link Factory#getImplementationHints()} used used to
 * review the hints that are supported by the function implementation.</li>
 * <li>{@link #setParameters(List)} is used to supply the argument expressions.</li>
 * <li>{@link #setFallbackValue(Literal)} is used to supply a placeholder Literal to be used if the function implementation is not available</li>
 * </ul>
 *
 * All implements should be registered for service provider interface
 *
 * <pre>
 * org.opengis.filter.Function</code>
 * DefaultFunctionFactor.
 *
 * <p>
 * If you have a large number of related functions consider the use of {@link FunctionFactory}.
 *
 * @author James Macgill, PSU
 * @author Jody Garnett (Boundless)
 * @see FunctionFactory
 *
 */
public interface FunctionExpression extends Factory, Function {

    /**
     * Fallback value to use in the event the function is unavailable in the requested environment.
     *
     * <p>The fallback value is not provided as one of the arguments, as it is an advanced option
     * used in style layer descriptor documents to facilitate interoperability. It allows a user to
     * specify an SQL function, and provide a value to use when the documented is used with a WFS
     * that does not support the provided function.
     */
    void setFallbackValue(Literal fallback);

    /** Sets the Parameters for the function. */
    void setParameters(List<org.opengis.filter.expression.Expression> parameters);
}
