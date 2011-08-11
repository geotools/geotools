package org.geotools.styling.builder;

import org.geotools.styling.AnchorPoint;
import org.opengis.filter.expression.Expression;

/**
 * AnchorPoint allows you specify which part of a graphic indicates the location.
 * 
 * As an example if your graphic is a pin the AnchorPoint will be the end of the pin.
 * 
 * <pre>
 * <code>AnchorPointBuilder<?> b = new AnchorPointBuilder();
 * AnchorPoint anchor = b.x(0.5).y(0.9).build();
 * </code>
 * </pre
 * 
 * @author Jody Garnett (LISAsoft)
 * 
 * 
 * @source $URL$
 */
public class AnchorPointBuilder extends AbstractStyleBuilder<AnchorPoint> {
    private Expression x;

    private Expression y;

    public AnchorPointBuilder() {
        this(null);
    }

    public AnchorPointBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public AnchorPoint build() {
        if (unset) {
            return null;
        }
        AnchorPoint anchorPoint = sf.anchorPoint(x, y);
        if (parent == null) {
            reset();
        }
        return anchorPoint;
    }

    public AnchorPointBuilder x(Expression x) {
        this.x = x;
        return this;
    }

    public AnchorPointBuilder x(double x) {
        return x(literal(x));
    }

    public AnchorPointBuilder x(String cqlExpression) {
        return x(cqlExpression(cqlExpression));
    }

    public AnchorPointBuilder y(Expression y) {
        this.y = y;
        return this;
    }

    public AnchorPointBuilder y(double y) {
        return y(literal(y));
    }

    public AnchorPointBuilder y(String cqlExpression) {
        return y(cqlExpression(cqlExpression));
    }

    public AnchorPointBuilder reset() {
        x = literal(0);
        y = literal(0);
        unset = false;
        return this;
    }

    public AnchorPointBuilder reset(AnchorPoint anchorPoint) {
        if (anchorPoint == null) {
            return reset();
        }
        x = anchorPoint.getAnchorPointX();
        y = anchorPoint.getAnchorPointY();
        unset = false;
        return this;
    }

    public AnchorPointBuilder unset() {
        return (AnchorPointBuilder) super.unset();
    }

    public AnchorPointBuilder reset(org.opengis.style.AnchorPoint anchorPoint) {
        if (anchorPoint == null) {
            return reset();
        }
        x = anchorPoint.getAnchorPointX();
        y = anchorPoint.getAnchorPointY();
        unset = false;
        return this;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").pointPlacement().anchor()
                .init(this);

    }

}
