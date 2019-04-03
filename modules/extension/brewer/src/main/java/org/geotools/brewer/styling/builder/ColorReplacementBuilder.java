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
import org.geotools.styling.ColorReplacement;
import org.opengis.filter.expression.Expression;

public class ColorReplacementBuilder extends AbstractStyleBuilder<ColorReplacement> {
    private Expression propertyName;

    private List<Expression> mapping = new ArrayList<Expression>();

    public ColorReplacementBuilder() {
        this(null);
    }

    public ColorReplacementBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ColorReplacement build() {
        if (unset) {
            return null;
        }
        Expression array[] = mapping.toArray(new Expression[mapping.size()]);
        ColorReplacement replacement = sf.colorReplacement(propertyName, array);
        if (parent == null) {
            reset();
        }
        return replacement;
    }

    public ColorReplacementBuilder reset() {
        propertyName = property("Raster");
        mapping.clear();
        unset = false;
        return this;
    }

    @Override
    public ColorReplacementBuilder reset(ColorReplacement original) {
        return reset((org.opengis.style.ColorReplacement) original);
    }

    public ColorReplacementBuilder reset(org.opengis.style.ColorReplacement replacement) {
        if (replacement == null) {
            return unset();
        }
        mapping.clear();
        if (replacement.getRecoding() != null
                && replacement.getRecoding().getParameters().size() > 0) {
            List<Expression> params = replacement.getRecoding().getParameters();
            propertyName = params.get(0);
            for (int i = 0; i < params.size(); i++) {
                mapping.add(params.get(i));
            }
        }

        unset = false;
        return this;
    }

    public ColorReplacementBuilder unset() {
        return (ColorReplacementBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        // TODO: build a raster style
        throw new UnsupportedOperationException("Can't build a style out of a color replacement");
    }
}
