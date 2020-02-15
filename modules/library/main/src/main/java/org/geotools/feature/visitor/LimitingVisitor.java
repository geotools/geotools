/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import org.opengis.feature.FeatureVisitor;

/**
 * A visitor that can limit the features that can be visited (for example to implement startOffset,
 * maxFeatures pagination).
 *
 * @author Mauro Bartolomeoli (mauro.bartolomeoli at geo-solutions.it)
 * @see FeatureVisitor
 * @since 12.x
 */
public interface LimitingVisitor extends FeatureVisitor {
    /**
     * Checks if the visitor has limits.
     *
     * @return true if the visitor has limits
     */
    boolean hasLimits();

    /** Returns startIndex (first element to return) */
    int getStartIndex();

    /** Returns maxFeatures (max # of elements to return) */
    int getMaxFeatures();
}
