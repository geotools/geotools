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
package org.geotools.brewer.styling.builder;

import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.styling.Style;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

abstract class AbstractStyleBuilder<T> extends AbstractSLDBuilder<T> {

    public AbstractStyleBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
    }

    protected Expression literal(Object literal) {
        return FF.literal(literal);
    }

    protected Expression property(String name) {
        return FF.property(name);
    }

    protected Expression cqlExpression(String cql) {
        try {
            return ECQL.toExpression(cql);
        } catch (CQLException e) {
            // failed to parse as ecql, attempt to fall back on to CQL
            try {
                return CQL.toExpression(cql);
            } catch (CQLException e1) {
                // throw back original exception
            }
            throw new RuntimeException("Failed to build an expression out of CQL", e);
        }
    }

    protected Filter cqlFilter(String cql) {
        try {
            return ECQL.toFilter(cql);
        } catch (CQLException e) {
            // failed to parse as ecql, attempt to fall back on to CQL
            try {
                return CQL.toFilter(cql);
            } catch (CQLException e1) {
                // throw back original exception
            }
            throw new RuntimeException("Failed to build a filter out of CQL", e);
        }
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        // sb -> user layer -> user style -> init this
        throw new UnsupportedOperationException("Implementation missing");
    }

    public Style buildStyle() {
        if (parent != null && parent instanceof AbstractStyleBuilder) {
            return ((AbstractStyleBuilder) parent).buildStyle();
        } else {
            StyleBuilder sb = new StyleBuilder();
            buildStyleInternal(sb);
            return sb.buildStyle();
        }
    }

    public Object buildRoot() {
        if (parent != null) {
            return parent.build();
        } else {
            return build();
        }
    }

    protected abstract void buildStyleInternal(StyleBuilder sb);

    protected void init(Builder<T> other) {
        reset(other.build());
    }

    public AbstractStyleBuilder<T> unset() {
        reset();
        unset = true;
        return this;
    }

    boolean isUnset() {
        return unset;
    }
}
