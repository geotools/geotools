/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.elasticsearch;

import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.spatial.BBOX;

class BBOXRemovingFilterVisitor extends DuplicatingFilterVisitor {

    private String geometryPropertyName;

    @Override
    public Object visit(BBOX filter, Object extraData) {
        geometryPropertyName = filter.getExpression1().toString();
        return Filter.INCLUDE;
    }

    public String getGeometryPropertyName() {
        return geometryPropertyName;
    }
}
