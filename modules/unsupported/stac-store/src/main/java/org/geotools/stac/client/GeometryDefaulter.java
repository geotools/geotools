/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.client;

import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;

/**
 * GeoTools code uses the empty string to mean the default geometry, but a STAC API won't know about
 * this convention, this filter switches it to the "geometry" property of STAC items.
 */
public class GeometryDefaulter extends DuplicatingFilterVisitor {

    private static GeometryDefaulter INSTANCE = new GeometryDefaulter();

    public static Filter defaultGeometry(Filter filter) {
        if (filter == null) return null;
        return (Filter) filter.accept(INSTANCE, null);
    }

    private GeometryDefaulter() {}

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        if ("".equals(expression.getPropertyName())) {
            return getFactory(extraData).property("geometry", expression.getNamespaceContext());
        }
        return super.visit(expression, extraData);
    }
}
