package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.Font;
import org.opengis.filter.expression.Expression;

public class FontBuilder extends AbstractStyleBuilder<Font> {
    private List<Expression> families = new ArrayList<Expression>();

    private Expression style;

    private Expression weight;

    private Expression size;

    public FontBuilder() {
        this(null);
    }

    public FontBuilder(TextSymbolizerBuilder parent) {
        super(parent);
        reset();
    }

    public Font build() {
        if (unset) {
            return null;
        }
        Font font = sf.font(families, style, weight, size);
        if (parent == null) {
            reset();
        }
        return font;
    }

    public FontBuilder family(Expression family) {
        this.families.add(family);
        return this;
    }

    public FontBuilder familyName(String family) {
        return family(literal(family));
    }

    public FontBuilder family(String cqlExpression) {
        return family(cqlExpression(cqlExpression));
    }

    public FontBuilder style(Expression style) {
        this.style = style;
        return this;
    }

    public FontBuilder styleName(String style) {
        return style(literal(style));
    }

    public FontBuilder style(String cqlExpression) {
        return style(cqlExpression(cqlExpression));
    }

    public FontBuilder weight(Expression weight) {
        this.weight = weight;
        return this;
    }

    public FontBuilder weightName(String weight) {
        return weight(literal(weight));
    }

    public FontBuilder weight(String cqlExpression) {
        return weight(cqlExpression(cqlExpression));
    }

    public FontBuilder size(Expression size) {
        this.size = size;
        return this;
    }

    public FontBuilder size(double size) {
        return size(literal(size));
    }

    public FontBuilder size(String cqlExpression) {
        return size(cqlExpression(cqlExpression));
    }

    public FontBuilder reset() {
        unset = false;
        return this;
    }

    public FontBuilder reset(Font font) {
        if (font == null) {
            return reset();
        }
        this.families = font.getFamily() != null ? font.getFamily() : new ArrayList<Expression>();
        this.size = font.getSize();
        this.style = font.getStyle();
        this.weight = font.getWeight();
        unset = false;
        return this;
    }

    public FontBuilder unset() {
        return (FontBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").newFont().init(this);
    }

}
