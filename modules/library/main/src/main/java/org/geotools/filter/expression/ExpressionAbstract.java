/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.util.Converters;
import org.opengis.filter.expression.Expression;

//
/**
 * Abstract superclass of these Expression implementations.
 *
 * @author Jody Garnett
 */
public abstract class ExpressionAbstract implements Expression {

    /**
     * Subclass should override, default implementation returns null.
     *
     * @return default implementation returns null
     */
    public Object evaluate(Object object) {
        return null;
    }
    /**
     * Default implementation delegates handling of context conversion to Converters utility class.
     *
     * <p>Subclasses are expected to make use of the {@linkplain Converters} utility class (as the
     * easiest way to provide value morphing in conformance with the Filter specification).
     */
    public <T> T evaluate(Object object, Class<T> context) {
        return Converters.convert(evaluate(object), context);
    }
}
