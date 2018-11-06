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

import org.geotools.styling.Mark;
import org.opengis.filter.expression.Expression;

public class MarkBuilder extends AbstractStyleBuilder<Mark> {
    StrokeBuilder strokeBuilder = new StrokeBuilder(this).unset();

    FillBuilder fill = new FillBuilder(this).unset();

    ExternalMarkBuilder externalMark = new ExternalMarkBuilder(this);

    Expression wellKnownName;

    public MarkBuilder() {
        this(null);
    }

    MarkBuilder(GraphicBuilder parent) {
        super(parent);
        reset();
    }

    public MarkBuilder name(Expression name) {
        this.wellKnownName = name;
        this.externalMark.unset();
        return this;
    }

    public MarkBuilder name(String name) {
        return name(literal(name));
    }

    public ExternalMarkBuilder externalMark() {
        return externalMark;
    }

    public StrokeBuilder stroke() {
        return strokeBuilder;
    }

    public FillBuilder fill() {
        return fill;
    }

    public MarkBuilder reset() {
        // TODO: where is the default mark?
        this.wellKnownName = literal("square");
        this.externalMark.unset();
        this.strokeBuilder.reset();
        this.fill.unset();
        this.stroke().unset();
        this.unset = false;

        return this;
    }

    public Mark build() {
        if (unset) {
            return null;
        }

        Mark mark = null;
        if (!externalMark.isUnset()) {
            mark = sf.mark(externalMark.build(), fill.build(), strokeBuilder.build());
        }
        if (wellKnownName != null) {
            mark = sf.mark(wellKnownName, fill.build(), strokeBuilder.build());
        }
        if (parent == null) {
            reset();
        }
        return mark;
    }

    public MarkBuilder reset(Mark mark) {
        return reset((org.opengis.style.Mark) mark);
    }

    public MarkBuilder reset(org.opengis.style.Mark mark) {
        if (mark == null) {
            return unset();
        }
        this.wellKnownName = mark.getWellKnownName();
        this.externalMark.reset(mark.getExternalMark());
        this.strokeBuilder.reset(mark.getStroke());
        this.fill.reset(mark.getFill());
        this.unset = false;

        return this;
    }

    public MarkBuilder unset() {
        return (MarkBuilder) super.unset();
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().point().graphic().mark().init(this);
    }
}
