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
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.GraphicLegend;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.Mark;
import org.geotools.api.style.Symbol;

public class GraphicLegendBuilder extends AbstractStyleBuilder<GraphicLegend> {
    private List<Builder<? extends Symbol>> symbols = new ArrayList<>();

    private Expression opacity;

    private Expression size;

    private Expression rotation;

    private AnchorPointBuilder anchorPoint = new AnchorPointBuilder(this, 0, 0).unset();

    private DisplacementBuilder displacement = new DisplacementBuilder(this).unset();

    public GraphicLegendBuilder() {
        this(null);
    }

    public GraphicLegendBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    @Override
    public GraphicLegend build() {
        if (unset) {
            return null;
        }

        if (symbols.isEmpty()) {
            symbols.add(new MarkBuilder(this));
        }
        List<GraphicalSymbol> list = new ArrayList<>();
        for (Builder<? extends Symbol> symbol : symbols) {
            list.add(symbol.build());
        }
        GraphicLegend graphic =
                sf.graphicLegend(list, opacity, size, rotation, anchorPoint.build(), displacement.build());
        return graphic;
    }

    public AnchorPointBuilder anchor() {
        unset = false;
        return anchorPoint;
    }

    public DisplacementBuilder displacement() {
        unset = false;
        return displacement;
    }

    public GraphicLegendBuilder opacity(Expression opacity) {
        this.opacity = opacity;
        return this;
    }

    public GraphicLegendBuilder opacity(double opacity) {
        return opacity(literal(opacity));
    }

    public GraphicLegendBuilder opacity(String cqlExpression) {
        return opacity(cqlExpression(cqlExpression));
    }

    public GraphicLegendBuilder size(Expression size) {
        this.size = size;
        return this;
    }

    public GraphicLegendBuilder size(double size) {
        return size(literal(size));
    }

    public GraphicLegendBuilder size(String cqlExpression) {
        return size(cqlExpression(cqlExpression));
    }

    public GraphicLegendBuilder rotation(Expression rotation) {
        this.rotation = rotation;
        return this;
    }

    public GraphicLegendBuilder rotation(double rotation) {
        return rotation(literal(rotation));
    }

    public GraphicLegendBuilder rotation(String cqlExpression) {
        return rotation(cqlExpression(cqlExpression));
    }

    @Override
    public GraphicLegendBuilder reset() {
        opacity = literal(1);
        size = literal(16); // TODO: check what the actual default size is
        rotation = literal(0);
        anchorPoint.reset();
        displacement.reset();
        symbols.clear();
        unset = false;
        return this;
    }

    @Override
    public GraphicLegendBuilder reset(GraphicLegend graphic) {
        if (graphic == null) {
            return unset();
        }
        opacity = graphic.getOpacity();
        size = graphic.getSize();
        rotation = graphic.getRotation();
        anchorPoint.reset(graphic.getAnchorPoint());
        displacement.reset(graphic.getDisplacement());
        symbols.clear();
        for (GraphicalSymbol symbol : graphic.graphicalSymbols()) {
            final Builder<? extends Symbol> builder;
            if (symbol instanceof Mark) {
                builder = new MarkBuilder(this).reset((Mark) symbol);
            } else if (symbol instanceof ExternalGraphic) {
                builder = new ExternalGraphicBuilder(this).reset((ExternalGraphic) symbol);
            } else {
                throw new IllegalArgumentException("Unrecognized symbol type: " + symbol.getClass());
            }

            symbols.add(builder);
        }
        unset = false;
        return this;
    }

    @Override
    public GraphicLegendBuilder unset() {
        return (GraphicLegendBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().legend().init(this);
    }
}
