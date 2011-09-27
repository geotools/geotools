package org.geotools.styling.builder;

import org.geotools.styling.Displacement;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class DisplacementBuilder extends AbstractStyleBuilder<Displacement> {
    private Expression x = null;

    private Expression y = null;

    public DisplacementBuilder() {
        this(null);
    }

    public DisplacementBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public Displacement build() {
        if (unset) {
            return null;
        }
        Displacement displacement = sf.displacement(x, y);
        return displacement;
    }

    public DisplacementBuilder x(Expression x) {
        this.x = x;
        return this;
    }

    public DisplacementBuilder x(double x) {
        return x(literal(x));
    }

    public DisplacementBuilder x(String cqlExpression) {
        return x(cqlExpression(cqlExpression));
    }

    public DisplacementBuilder y(Expression y) {
        this.y = y;
        return this;
    }

    public DisplacementBuilder y(double y) {
        return y(literal(y));
    }

    public DisplacementBuilder y(String cqlExpression) {
        return y(cqlExpression(cqlExpression));
    }

    public DisplacementBuilder reset() {
        x = literal(0);
        y = literal(0);
        unset = false;
        return this;
    }

    public DisplacementBuilder reset(Displacement displacement) {
        if (displacement == null) {
            return reset();
        }
        x = literal(displacement.getDisplacementX());
        y = literal(displacement.getDisplacementY());
        unset = false;
        return this;
    }

    public DisplacementBuilder unset() {
        return (DisplacementBuilder) super.unset();
    }

    public DisplacementBuilder reset(org.opengis.style.Displacement displacement) {
        if (displacement == null) {
            return reset();
        }
        x = displacement.getDisplacementX();
        y = displacement.getDisplacementY();
        unset = false;
        return this;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").pointPlacement()
                .displacement().init(this);
    }

}
