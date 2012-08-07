/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression;

import org.geotools.filter.FunctionImpl;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.InternalFunction;
import org.opengis.filter.expression.VolatileFunction;

/**
 * A base class functions (i.e. anonymous inner classes) that are both {@link VolatileFunction
 * volatile} (i.e. explicitly stating evaluation needs to happen for each object in the collection
 * being traversed) and {@link InternalFunction internal} (i.e. are not subject of SPI lookup, such
 * as anonymous inner classes).
 * 
 * @since 9.0
 */
public abstract class InternalVolatileFunction extends FunctionImpl implements InternalFunction,
        VolatileFunction {

    public InternalVolatileFunction() {
        this("InternalFunctionImpl");
    }

    public InternalVolatileFunction(String name) {
        setName(name);
    }

    /**
     * This default implementation just returns {@code this} if the number of expected parameters is
     * zero, otherwise throws an {@link IllegalArgumentException}.
     * <p>
     * A subclass that do expect {@link Expression} parameters shall override this method and return
     * a new instance of the same kind of InternalFunction configured to work against the given
     * {@code parameters}.
     * 
     * @see org.opengis.filter.expression.InternalFunction#duplicate(org.opengis.filter.expression.Expression[])
     */
    @Override
    public InternalFunction duplicate(Expression... parameters) {
        final int expectedParams = super.getParameters().size();
        if (expectedParams > 0) {
            throw new IllegalArgumentException(
                    "This method must be overriten by subclasses that expect Expression arguments");
        }

        return this;
    }

    @Override
    public abstract Object evaluate(Object object);

    @Override
    public Object evaluate(Object object, @SuppressWarnings("rawtypes") Class context) {
        return super.evaluate(object, context);
    }
}
