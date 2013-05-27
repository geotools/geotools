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

import org.opengis.feature.FeatureVisitor;
import org.opengis.filter.expression.Expression;

/**
 * Extension of FeatureVisitor for visitors that require access to properties of the feature 
 * collection being visited. 
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public interface FeatureAttributeVisitor extends FeatureVisitor {

    /**
     * List of expressions used by visitor.
     */
    List<Expression> getExpressions();
}
