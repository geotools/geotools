/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Given an original rendering envelope it visits an expression, finds all {@link
 * GeometryTransformation}, collects and merges all the returned query envelopes
 */
public class GeometryTransformationVisitor extends DefaultFilterVisitor {

    public GeometryTransformationVisitor() {}

    @Override
    public Object visit(Function expression, Object data) {
        // drill down and merge
        ReferencedEnvelope merged = new ReferencedEnvelope((ReferencedEnvelope) data);
        for (Expression param : expression.getParameters()) {
            ReferencedEnvelope result = (ReferencedEnvelope) param.accept(this, data);
            if (result != null) merged.expandToInclude(result);
        }

        // apply the current function is possible
        if (expression instanceof GeometryTransformation) {
            merged = ((GeometryTransformation) expression).invert(merged);
        }

        return merged;
    }
}
