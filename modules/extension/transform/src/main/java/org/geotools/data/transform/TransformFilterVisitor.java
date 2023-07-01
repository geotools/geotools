/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.identity.FeatureId;

/**
 * Visits a filter and expands the transforming expressions into it
 *
 * @author Andrea Aime - GeoSolutions
 */
class TransformFilterVisitor extends DuplicatingFilterVisitor {

    private final String sourceName;
    private final String targetName;
    private final Map<String, Expression> expressions;

    public TransformFilterVisitor(
            String sourceName, String targetName, Map<String, Expression> expressions) {
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.expressions = expressions;
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        Set ids = filter.getIDs();
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("Invalid fid filter provides, has no fids inside");
        }
        if (sourceName.equals(targetName)) return filter;

        Set<FeatureId> fids = new HashSet<>();
        for (Object o : ids) {
            FeatureId id = new FeatureIdImpl((String) o);
            FeatureId retyped = reTypeId(id);
            fids.add(retyped);
        }
        return ff.id(fids);
    }

    public FeatureId reTypeId(FeatureId sourceId) {
        final String prefix = targetName + ".";
        if (sourceId.getID().startsWith(prefix)) {
            return new FeatureIdImpl(
                    sourceName + "." + sourceId.getID().substring(prefix.length()));
        } else {
            return sourceId;
        }
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        String name = expression.getPropertyName();
        Expression ex = expressions.get(name);
        if (ex == null) {
            return super.visit(expression, extraData);
        } else {
            // inject the actual expression into the filter
            return ex;
        }
    }
}
