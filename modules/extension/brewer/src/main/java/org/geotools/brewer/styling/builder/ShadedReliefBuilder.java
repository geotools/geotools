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

import org.geotools.styling.ShadedRelief;
import org.opengis.filter.expression.Expression;

public class ShadedReliefBuilder extends AbstractStyleBuilder<ShadedRelief> {
    private Expression factor;

    private boolean brightnessOnly;

    public ShadedReliefBuilder() {
        this(null);
    }

    public ShadedReliefBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ShadedReliefBuilder factor(Expression factor) {
        this.factor = factor;
        unset = false;
        return this;
    }

    public ShadedReliefBuilder factor(double factor) {
        return factor(literal(factor));
    }

    public ShadedReliefBuilder factor(String cqlExpression) {
        return factor(cqlExpression(cqlExpression));
    }

    public ShadedReliefBuilder brightnessOnly(boolean brightnessOnly) {
        this.brightnessOnly = brightnessOnly;
        unset = false;
        return this;
    }

    public ShadedRelief build() {
        if (unset) {
            return null;
        }
        ShadedRelief relief = sf.shadedRelief(factor, brightnessOnly);
        return relief;
    }

    public ShadedReliefBuilder reset() {
        factor = literal(0);
        brightnessOnly = false;
        unset = false;
        return this;
    }

    public ShadedReliefBuilder reset(ShadedRelief relief) {
        if (relief == null) {
            return reset();
        }
        brightnessOnly = relief.isBrightnessOnly();
        factor = relief.getReliefFactor();
        unset = false;
        return this;
    }

    public ShadedReliefBuilder unset() {
        return (ShadedReliefBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().shadedRelief().init(this);
    }
}
