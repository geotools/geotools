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

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Literal;


/**
 * Defines an expression that holds a literal for return.
 *
 * @author Rob Hranac, Vision for New York
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 *
 * @deprecated use {@link org.opengis.filter.expression.Literal}
 */
public interface LiteralExpression extends Expression, Literal {
    /**
     * Sets the literal.
     *
     * @param literal The literal to store inside this expression.
     *
     * @throws IllegalFilterException This literal type is not in scope.
     *
     * @deprecated use {@link Literal#setValue(Object)}
     */
    void setLiteral(Object literal) throws IllegalFilterException;

    /**
     * Gets the value of this literal.
     *
     * @param feature Required by the interface but not used.
     *
     * @return the literal held by this expression.  Ignores the passed in
     *         feature.
     *
     * @deprecated use {@link Expression#evaluate(Feature)}.
     */
    Object getValue(SimpleFeature feature);

    /**
     * Returns the literal type.
     *
     * @return the short representation of the literal expression type.
     */
    short getType();

    /**
     * Retrieves the literal of this expression.
     *
     * @return the literal held by this expression.
     *
     * @deprecated use {@link Literal#getValue()}.
     */
    Object getLiteral();
}
