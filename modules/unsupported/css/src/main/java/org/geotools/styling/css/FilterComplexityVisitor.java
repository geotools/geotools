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
package org.geotools.styling.css;

import org.geotools.filter.visitor.AbstractFilterVisitor;
import org.opengis.filter.And;
import org.opengis.filter.Not;

/**
 * Computes a "complexity" of a filter by counting how many simple filters are in it
 *
 * @author Andrea Aime - GeoSolutions
 */
class FilterComplexityVisitor extends AbstractFilterVisitor {
    int count = 1;

    @Override
    public Object visit(And filter, Object data) {
        count += filter.getChildren().size();
        return super.visit(filter, data);
    }

    @Override
    public Object visit(org.opengis.filter.Or filter, Object data) {
        count += filter.getChildren().size();
        return super.visit(filter, data);
    }

    @Override
    public Object visit(Not filter, Object data) {
        count++;
        return super.visit(filter, data);
    }
}
