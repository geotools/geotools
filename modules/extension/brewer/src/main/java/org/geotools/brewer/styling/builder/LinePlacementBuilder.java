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

import org.geotools.styling.LinePlacement;
import org.opengis.filter.expression.Expression;

public class LinePlacementBuilder extends AbstractStyleBuilder<LinePlacement> {
    private Expression offset;

    private Expression initialGap;

    private Expression gap;

    private boolean repeated;

    private boolean generalizedLine;

    private boolean aligned;

    public LinePlacementBuilder() {
        this(null);
    }

    public LinePlacementBuilder offset(double offset) {
        this.offset = literal(offset);
        return this;
    }

    public LinePlacementBuilder offset(Expression offset) {
        this.offset = offset;
        return this;
    }

    public LinePlacementBuilder gap(double gap) {
        this.gap = literal(gap);
        return this;
    }

    public LinePlacementBuilder gap(Expression gap) {
        this.gap = gap;
        return this;
    }

    public LinePlacementBuilder repeated(boolean repeated) {
        this.repeated = repeated;
        return this;
    }

    public LinePlacementBuilder generalizedLine(boolean generalizedLine) {
        this.generalizedLine = generalizedLine;
        return this;
    }

    public LinePlacementBuilder aligned(boolean aligned) {
        this.aligned = aligned;
        return this;
    }

    LinePlacementBuilder(TextSymbolizerBuilder parent) {
        super(parent);
        reset();
    }

    public LinePlacement build() {
        if (unset) {
            return null;
        }
        LinePlacement linePlacement =
                sf.linePlacement(offset, initialGap, gap, repeated, aligned, generalizedLine);
        if (parent == null) {
            reset();
        }
        return linePlacement;
    }

    public LinePlacementBuilder reset() {
        this.aligned = false;
        this.generalizedLine = false;
        this.repeated = false;
        this.gap = literal(0);
        this.initialGap = literal(0);
        this.offset = literal(0);

        unset = false;
        return this;
    }

    public LinePlacementBuilder reset(LinePlacement placement) {
        if (placement == null) {
            return reset();
        }
        this.aligned = placement.isAligned();
        this.generalizedLine = placement.isGeneralizeLine();
        this.repeated = placement.isRepeated();
        this.gap = placement.getGap();
        this.initialGap = placement.getInitialGap();
        this.offset = placement.getPerpendicularOffset();

        unset = false;
        return this;
    }

    public LinePlacementBuilder unset() {
        return (LinePlacementBuilder) super.unset();
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").linePlacement().init(this);
    }
}
