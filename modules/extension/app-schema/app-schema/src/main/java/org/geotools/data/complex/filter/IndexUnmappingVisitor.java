/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.filter;

import org.geotools.data.complex.FeatureTypeMapping;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.expression.PropertyName;

public class IndexUnmappingVisitor extends DuplicatingFilterVisitor {

    private FeatureTypeMapping mapping;

    public IndexUnmappingVisitor(FeatureTypeMapping mapping) {
        super();
        this.mapping = mapping;
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        String targetXPath = expression.getPropertyName();
        // replace the artificial DEFAULT_GEOMETRY property with the actual one
        //        if (DEFAULT_GEOMETRY_LOCAL_NAME.equals(targetXPath)) {
        //            targetXPath = mapping.getDefaultGeometryXPath();
        //        }
        //        NamespaceSupport namespaces = mapping.getNamespaces();
        //        AttributeDescriptor root = mapping.getTargetFeature();
        //        StepList simplifiedSteps = XPath.steps(root, targetXPath, namespaces);
        //        AttributeMapping attMapping = mapping.getAttributeMapping(simplifiedSteps);
        //        if (attMapping == null || StringUtils.isEmpty(attMapping.getIndexField()))
        //            return expression;
        return new AttributeExpressionImpl(mapping.getIndexAttributeName(targetXPath));
    }
}
