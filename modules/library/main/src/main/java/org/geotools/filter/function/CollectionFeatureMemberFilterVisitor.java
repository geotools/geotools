/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2011 Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.expression.PropertyName;

/**
 * Replace "featureMembers/ * /ATTRIBUTE" change with "ATTRIBUTE"
 * <p>
 * This is used to clean up xpath expressions prior to use by
 * the various aggregate functions such as Collection_AverageFunction.
 * 
 * @since 8.0
 */
public final class CollectionFeatureMemberFilterVisitor extends DuplicatingFilterVisitor {
    public Object visit(PropertyName expression, Object data) {
        String xpath = expression.getPropertyName();
        if (xpath.startsWith("featureMembers/*/")) {
            xpath = xpath.substring(17);
        } else if (xpath.startsWith("featureMember/*/")) {
            xpath = xpath.substring(16);
        }
        return getFactory(data).property(xpath, expression.getNamespaceContext());         
    }
}