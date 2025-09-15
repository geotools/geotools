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
package org.geotools.renderer.lite;

import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;

/**
 * Renames the specified attribute to a new target name
 *
 * @author Andrea Aime - GeoSolutions
 */
class AttributeRenameVisitor extends DuplicatingFilterVisitor {

    String source;
    String target;

    public AttributeRenameVisitor(String source, String target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        final String propertyName = expression.getPropertyName();
        if (propertyName != null && propertyName.equals(source)) {
            return getFactory(extraData).property(target);
        } else {
            return super.visit(expression, extraData);
        }
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        // rename if necessary
        Expression e1 = filter.getExpression1();
        if (e1 instanceof PropertyName pname) {
            String name = pname.getPropertyName();
            if (name != null && name.equals(source)) {
                e1 = ff.property(target);
            }
        }

        // duplicate preserving fast bbox filters
        if (filter instanceof FastBBOX fbox && e1 instanceof PropertyName name) {
            return new FastBBOX(name, fbox.getEnvelope(), getFactory(extraData));
        } else {
            return getFactory(extraData).bbox(e1, filter.getExpression2());
        }
    }
}
