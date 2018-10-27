/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009 - 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.styling.filter.expression;

import org.geotools.brewer.styling.builder.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;

public class LiteralBuilder implements Builder<Literal> {
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
    Object literal = null; // will result in Expression.NIL
    boolean unset = false;

    public LiteralBuilder() {
        reset();
    }

    public LiteralBuilder(Literal literal) {
        reset(literal);
    }

    public LiteralBuilder value(Object literal) {
        this.literal = literal;
        unset = false;
        return this;
    }

    public Literal build() {
        if (unset) {
            return null;
        }
        return ff.literal(literal);
    }

    public LiteralBuilder reset() {
        unset = false;
        literal = null;
        return this;
    }

    public LiteralBuilder reset(Literal original) {
        unset = false;
        literal = original.getValue();
        return this;
    }

    public LiteralBuilder unset() {
        unset = true;
        return this;
    }
}
