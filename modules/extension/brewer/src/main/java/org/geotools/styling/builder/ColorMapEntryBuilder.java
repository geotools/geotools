package org.geotools.styling.builder;

import java.awt.Color;

import org.geotools.styling.ColorMapEntry;
import org.geotools.util.Converters;
import org.opengis.filter.expression.Expression;

public class ColorMapEntryBuilder extends AbstractStyleBuilder<ColorMapEntry> {

    String label;

    Expression color;

    Expression opacity;

    Expression quantity;

    public ColorMapEntryBuilder() {
        this(null);
    }

    public ColorMapEntryBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ColorMapEntryBuilder color(Expression color) {
        this.color = color;
        return this;
    }

    public ColorMapEntryBuilder color(Color color) {
        return color(literal(color));
    }

    public ColorMapEntryBuilder colorHex(String hex) {
        Color color = Converters.convert(hex, Color.class);
        if (color == null) {
            throw new IllegalArgumentException(
                    "The provided expression could not be turned into a color: " + hex);
        }
        return color(color);
    }

    public ColorMapEntryBuilder color(String cqlExpression) {
        return color(cqlExpression(cqlExpression));
    }

    public ColorMapEntryBuilder opacity(Expression opacity) {
        this.opacity = opacity;
        return this;
    }

    public ColorMapEntryBuilder opacity(double opacity) {
        return opacity(literal(opacity));
    }

    public ColorMapEntryBuilder opacity(String cqlExpression) {
        return opacity(cqlExpression(cqlExpression));
    }

    public ColorMapEntryBuilder quantity(Expression quantity) {
        this.quantity = quantity;
        return this;
    }

    public ColorMapEntryBuilder quantity(double quantity) {
        return quantity(literal(quantity));
    }

    public ColorMapEntryBuilder quantity(String cqlExpression) {
        return quantity(cqlExpression(cqlExpression));
    }

    @Override
    public ColorMapEntryBuilder reset() {
        label = null;
        color = null;
        opacity = literal(1.0);
        quantity = null;
        unset = false;
        return this;
    }

    @Override
    public ColorMapEntryBuilder reset(ColorMapEntry original) {
        if (original == null) {
            return unset();
        }

        label = original.getLabel();
        color = original.getColor();
        opacity = original.getOpacity();
        quantity = original.getQuantity();
        unset = false;
        return this;
    }

    @Override
    public ColorMapEntry build() {
        if (unset) {
            return null;
        }

        ColorMapEntry entry = sf.createColorMapEntry();
        entry.setLabel(label);
        entry.setColor(color);
        entry.setOpacity(opacity);
        entry.setQuantity(quantity);

        if (parent == null) {
            reset();
        }

        return entry;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().colorMap().entry().init(this);
    }

    @Override
    public ColorMapEntryBuilder unset() {
        return (ColorMapEntryBuilder) super.unset();
    }

}
