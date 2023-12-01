/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.opengis.filter.expression;

import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory2;

/**
 * Implemented by functions that can turn themselves into a simpler, more efficient expression, when
 * certain conditions are met. Called by the SimplifyingfilterVisitor when the arguments of the
 * function are not all literals (in which case, a one time simplification is performed instead).
 */
public interface SimplifiableFunction {

    /**
     * Returns a simplified version of the function, or the function itself it cannot be be
     * simplified
     *
     * @param ff The filter factory to use for creating new expressions (mandatory, non null)
     * @param featureType The target feature type, if available, <code>null</code> otherwise
     * @return The simplified expression
     */
    Expression simplify(FilterFactory2 ff, FeatureType featureType);
}
