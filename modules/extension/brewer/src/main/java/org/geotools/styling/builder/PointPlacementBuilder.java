package org.geotools.styling.builder;

import org.geotools.styling.PointPlacement;
import org.opengis.filter.expression.Expression;

public class PointPlacementBuilder extends AbstractStyleBuilder<PointPlacement> {
    private Expression rotation;

    private AnchorPointBuilder anchor = new AnchorPointBuilder(this).unset();

    private DisplacementBuilder displacement = new DisplacementBuilder(this).unset();

    public PointPlacementBuilder() {
        this(null);
    }

    public PointPlacementBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public PointPlacement build() {
        if (unset) {
            return null;
        }
        PointPlacement placement = sf
                .pointPlacement(anchor.build(), displacement.build(), rotation);
        if (parent == null) {
            reset();
        }
        return placement;
    }

    public AnchorPointBuilder anchor() {
        unset = false;
        return anchor;
    }

    public DisplacementBuilder displacement() {
        unset = false;
        return displacement;
    }

    public PointPlacementBuilder rotation(Expression rotation) {
        this.rotation = rotation;
        return this;
    }

    public PointPlacementBuilder rotation(double rotation) {
        return rotation(literal(rotation));
    }

    public PointPlacementBuilder rotation(String cqlExpression) {
        return rotation(cqlExpression(cqlExpression));
    }

    public PointPlacementBuilder reset() {
        rotation = literal(0);
        anchor.reset();
        displacement.reset();
        unset = false;
        return this;
    }

    public PointPlacementBuilder reset(PointPlacement placement) {
        if (placement == null) {
            return reset();
        }
        rotation = placement.getRotation();
        anchor.reset(placement.getAnchorPoint());
        displacement.reset(placement.getDisplacement());
        unset = false;
        return this;
    }

    public PointPlacementBuilder unset() {
        return (PointPlacementBuilder) super.unset();
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").pointPlacement().init(this);
    }

}
