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
import org.opengis.filter.expression.PropertyName;


/**
 * The geotools representation of the PropertyName tag in an xml encoded
 * filter.
 * <p>
 * It should handle xpath attributePaths of features, and should
 * report the attribute found at the attributePath of a feature.
 * </p>
 *
 * @source $URL$
 * @version $Id$
 *
 * @deprecated use {@link org.opengis.filter.expression.PropertyName}
 */
public interface AttributeExpression extends Expression, PropertyName {
    /**
     * Sets the path of the attribute of this expression. For simple,
     * non-nested, features the 'path' is simply the name of an  attribute.
     *
     * @param attributePath A string of the path.
     *
     * @throws IllegalFilterException if the path is not valid.
     *
     * @deprecated use {@link PropertyName#setPropertyName(String)}
     *
     */
    void setAttributePath(String attributePath) throws IllegalFilterException;

    /**
     * Gets the attribute value at the path held by this expression from the
     * feature.
     *
     * @param feature the feature to get this attribute from.
     *
     * @return the value of the attribute found by this expression.
     *
     * @deprecated use {@link org.opengis.filter.expression.Expression#evaluate(Feature)()}
     */
    Object getValue(SimpleFeature feature);

    /**
     * Gets the attribute path of this expression.
     *
     * @return the attribute to be queried.
     *
     * @deprecated use {@link PropertyName#getPropertyName()}
     */
    String getAttributePath();
}
