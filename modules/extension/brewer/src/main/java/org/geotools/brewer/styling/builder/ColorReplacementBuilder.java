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

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.expression.Expression;
import org.geotools.styling.ColorReplacementImpl;

public class ColorReplacementBuilder extends AbstractStyleBuilder<ColorReplacementImpl> {
    private Expression propertyName;

    private List<Expression> mapping = new ArrayList<>();

    public ColorReplacementBuilder() {
        this(null);
    }

    public ColorReplacementBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    @Override
    public ColorReplacementImpl build() {
        if (unset) {
            return null;
        }
        Expression[] array = mapping.toArray(new Expression[mapping.size()]);
        ColorReplacementImpl replacement = sf.colorReplacement(propertyName, array);
        if (parent == null) {
            reset();
        }
        return replacement;
    }

    @Override
    public ColorReplacementBuilder reset() {
        propertyName = property("Raster");
        mapping.clear();
        unset = false;
        return this;
    }

    public ColorReplacementBuilder reset(ColorReplacementImpl replacement) {
        if (replacement == null) {
            return unset();
        }
        mapping.clear();
        if (replacement.getRecoding() != null
                && replacement.getRecoding().getParameters().size() > 0) {
            List<Expression> params = replacement.getRecoding().getParameters();
            propertyName = params.get(0);
            for (Expression param : params) {
                mapping.add(param);
            }
        }

        unset = false;
        return this;
    }

    @Override
    public ColorReplacementBuilder unset() {
        return (ColorReplacementBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        // TODO: build a raster style
        throw new UnsupportedOperationException("Can't build a style out of a color replacement");
    }
}
