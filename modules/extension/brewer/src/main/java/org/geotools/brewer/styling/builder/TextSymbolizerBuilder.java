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
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Font;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.TextSymbolizer;

public class TextSymbolizerBuilder extends SymbolizerBuilder<TextSymbolizer> {
    FillBuilder fill = new FillBuilder(this).unset();

    List<FontBuilder> fonts = new ArrayList<>();

    FontBuilder font;

    HaloBuilder halo = (HaloBuilder) new HaloBuilder(this).unset();

    Expression label;

    Expression geometry = null;

    Unit<Length> uom;

    GraphicBuilder shield = (GraphicBuilder) new GraphicBuilder(this).unset();

    Builder<? extends LabelPlacement> placement = new PointPlacementBuilder(this).unset();

    private Expression priority;

    public TextSymbolizerBuilder() {
        this(null);
    }

    public TextSymbolizerBuilder(RuleBuilder parent) {
        super(parent);
        reset();
    }

    public TextSymbolizerBuilder geometry(Expression geometry) {
        this.geometry = geometry;
        return this;
    }

    public TextSymbolizerBuilder geometry(String cqlExpression) {
        return geometry(cqlExpression(cqlExpression));
    }

    public HaloBuilder halo() {
        unset = false;
        return halo;
    }

    public FillBuilder fill() {
        unset = false;
        return fill;
    }

    public FontBuilder newFont() {
        unset = false;
        FontBuilder font = new FontBuilder(this);
        fonts.add(font);
        return font;
    }

    public LinePlacementBuilder linePlacement() {
        if (!(placement instanceof LinePlacementBuilder)) {
            placement = new LinePlacementBuilder(this);
        }
        unset = false;
        return (LinePlacementBuilder) placement;
    }

    public PointPlacementBuilder pointPlacement() {
        if (!(placement instanceof PointPlacementBuilder)) {
            placement = new PointPlacementBuilder(this);
        }
        unset = false;
        return (PointPlacementBuilder) placement;
    }

    public TextSymbolizerBuilder uom(Unit<Length> uom) {
        unset = false;
        this.uom = uom;
        return this;
    }

    public GraphicBuilder shield() {
        unset = false;
        return shield;
    }

    @Override
    public TextSymbolizer build() {
        if (unset) {
            return null;
        }
        Font[] array = null;
        if (!fonts.isEmpty()) {
            array = new Font[fonts.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = fonts.get(i).build();
            }
        }
        TextSymbolizer ts =
                sf.createTextSymbolizer(
                        fill.build(), array, halo.build(), label, placement.build(), null);
        ts.setGeometry(geometry);
        if (uom != null) {
            ts.setUnitOfMeasure(uom);
        }
        ts.getOptions().putAll(options);
        ts.setPriority(priority);

        if (!shield.isUnset()) {
            ts.setGraphic(shield.build());
        }

        reset();
        return ts;
    }

    @Override
    public TextSymbolizerBuilder unset() {
        return (TextSymbolizerBuilder) super.unset();
    }

    @Override
    public TextSymbolizerBuilder reset() {
        fill.reset(); // TODO: default fill for text?
        halo.unset(); // no default halo
        label = null;
        geometry = null;
        placement.reset();
        placement.unset();
        options.clear();
        uom = null;
        priority = null;
        unset = false;
        return this;
    }

    @Override
    public TextSymbolizerBuilder reset(TextSymbolizer symbolizer) {
        fill.reset(symbolizer.getFill());
        halo.reset(symbolizer.getHalo());
        label = symbolizer.getLabel();
        geometry = null;
        LabelPlacement otherPlacement = symbolizer.getLabelPlacement();
        if (symbolizer.getLabelPlacement() instanceof PointPlacement) {
            PointPlacementBuilder builder = new PointPlacementBuilder(this);
            builder.reset((PointPlacement) otherPlacement);
            placement = builder;
        } else if (symbolizer.getLabelPlacement() instanceof LabelPlacement) {
            LinePlacementBuilder builder = new LinePlacementBuilder(this);
            builder.reset((LinePlacement) otherPlacement);
            placement = builder;
        } else {
            throw new IllegalArgumentException("Unrecognized label placement: " + otherPlacement);
        }
        priority = symbolizer.getPriority();
        unset = false;
        return this;
    }

    public TextSymbolizerBuilder option(String name, Object value) {
        options.put(name, value.toString());
        unset = false;
        return this;
    }

    public TextSymbolizerBuilder label(Expression label) {
        unset = false;
        this.label = label;
        return this;
    }

    public TextSymbolizerBuilder label(String cqlExpression) {
        return label(cqlExpression(cqlExpression));
    }

    public TextSymbolizerBuilder labelText(String text) {
        return label(literal(text));
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().init(this);
    }

    public TextSymbolizerBuilder priority(Expression priority) {
        unset = false;
        this.priority = priority;
        return this;
    }

    public TextSymbolizerBuilder priority(String cql) {
        return priority(cqlExpression(cql));
    }

    public TextSymbolizerBuilder priority(int priority) {
        return priority(literal(priority));
    }
}
