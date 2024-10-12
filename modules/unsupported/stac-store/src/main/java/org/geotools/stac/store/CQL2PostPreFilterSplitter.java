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
package org.geotools.stac.store;

import java.util.List;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.temporal.BinaryTemporalOperator;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.stac.client.CQL2Conformance;

/**
 * A post/pre splitter driven by the CQL2 conformance classes, and able to handle the
 * "property/literal" comparison restriction
 */
class CQL2PostPreFilterSplitter extends PostPreProcessFilterSplittingVisitor {

    private final boolean forcePropertyLiteral;

    private static FilterCapabilities buildFilterCapabilities(List<String> conformance) {
        FilterCapabilities caps = new FilterCapabilities();
        for (CQL2Conformance cc : CQL2Conformance.values()) {
            if (cc.matches(conformance)) caps.addAll(cc.getCapabilities());
        }

        return caps;
    }

    public CQL2PostPreFilterSplitter(List<String> conformance) {
        super(buildFilterCapabilities(conformance), null, null);
        this.forcePropertyLiteral = !CQL2Conformance.PROPERTY_PROPERTY.matches(conformance);
    }

    @Override
    public Object visit(BBOX filter, Object notUsed) {
        if (forcePropertyLiteral
                && !(filter.getExpression1() instanceof PropertyName && filter.getExpression2() instanceof Literal)) {
            postStack.push(filter);
            return null;
        }

        return super.visit(filter, notUsed);
    }

    @Override
    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter) {
        if (forcePropertyLiteral
                && !(filter.getExpression1() instanceof PropertyName && filter.getExpression2() instanceof Literal)) {
            postStack.push(filter);
            return;
        }

        super.visitBinaryComparisonOperator(filter);
    }

    @Override
    protected void visitBinarySpatialOperator(BinarySpatialOperator filter) {
        if (forcePropertyLiteral
                && !(filter.getExpression1() instanceof PropertyName && filter.getExpression2() instanceof Literal)) {
            postStack.push(filter);
            return;
        }

        super.visitBinarySpatialOperator(filter);
    }

    @Override
    protected Object visit(BinaryTemporalOperator filter, Object data) {
        if (forcePropertyLiteral
                && !(filter.getExpression1() instanceof PropertyName && filter.getExpression2() instanceof Literal)) {
            postStack.push(filter);
            return data;
        }

        return super.visit(filter, data);
    }
}
