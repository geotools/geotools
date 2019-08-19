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

import java.awt.Color;
import org.geotools.styling.Fill;
import org.geotools.util.Converters;
import org.opengis.filter.expression.Expression;

public class FillBuilder extends AbstractStyleBuilder<org.opengis.style.Fill> {
    Expression color;

    Expression opacity;

    GraphicBuilder graphic = new GraphicBuilder(this).unset();

    /** Create a FillBuilder on its own; not part of a larger data structure. */
    public FillBuilder() {
        this(null);
    }

    public FillBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public FillBuilder color(Expression color) {
        this.unset = false;
        this.color = color;
        return this;
    }

    public FillBuilder color(Color color) {
        return color(literal(color));
    }

    public FillBuilder color(String cqlExpression) {
        return color(cqlExpression(cqlExpression));
    }

    public FillBuilder colorHex(String hex) {
        Color color = Converters.convert(hex, Color.class);
        if (color == null) {
            throw new IllegalArgumentException(
                    "The provided expression could not be turned into a color: " + hex);
        }
        return color(color);
    }

    public FillBuilder opacity(Expression opacity) {
        this.unset = false;
        this.opacity = opacity;
        return this;
    }

    public FillBuilder opacity(double opacity) {
        return opacity(literal(opacity));
    }

    public FillBuilder opacity(String cqlExpression) {
        return opacity(cqlExpression(cqlExpression));
    }

    public GraphicBuilder graphicFill() {
        unset = false;
        return graphic;
    }

    /**
     * Build Fill as defined; FillBuilder will be reset after this use.
     *
     * @return Created Fill as defined
     */
    public Fill build() {
        if (unset) {
            return null;
        }
        Fill fill = sf.createFill(color, null, opacity, graphic.build());

        if (parent == null) {
            reset();
        }
        return fill;
    }

    public FillBuilder unset() {
        return (FillBuilder) super.unset();
    }

    /** Reset to produce the default Fill. */
    public FillBuilder reset() {
        unset = false;
        color = Fill.DEFAULT.getColor();
        opacity = Fill.DEFAULT.getOpacity();
        graphic.unset();
        return this;
    }

    public FillBuilder reset(org.opengis.style.Fill original) {
        if (original == null) {
            return unset();
        }
        unset = false;
        color = original.getColor();
        opacity = original.getOpacity();
        graphic.reset(original.getGraphicFill());
        return this;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().polygon().fill().init(this);
    }
}
