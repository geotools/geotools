/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.visitor;

import java.util.List;
import java.util.Optional;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.filter.expression.Expression;

/**
 * Extension of FeatureVisitor for visitors that require access to properties of the feature collection being visited.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public interface FeatureAttributeVisitor extends FeatureVisitor {

    /** List of expressions used by visitor. */
    List<Expression> getExpressions();

    /**
     * Returns the expected output type given the input type.
     *
     * @param inputTypes The type of the input expressions
     * @throws IllegalArgumentException If the list of input types is not a match for the {@link #getExpressions()}
     *     result or is not acceptable for this visito
     */
    default Optional<List<Class>> getResultType(List<Class> inputTypes) {
        return Optional.empty();
    }
}
