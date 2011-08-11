package org.geotools.styling.builder;

import org.geotools.styling.Halo;
import org.opengis.filter.expression.Expression;

public class HaloBuilder extends AbstractStyleBuilder<org.opengis.style.Halo> {
    Expression radius;

    FillBuilder fill = new FillBuilder(this);

    public HaloBuilder() {
        this(null);
    }

    public HaloBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    /**
     * Set the HaloBuilder to produce <code>node</code>
     * 
     * @return current HaloBuilder for chaining operations
     */
    public HaloBuilder unset() {
        return (HaloBuilder) super.unset();
    }

    /**
     * Set the HaloBuilder
     * <P>
     * to produce a default Halo.
     * 
     * @return current HaloBuilder
     *         <P>
     *         for chaining operations
     */
    public HaloBuilder reset() {
        unset = false; //
        radius = literal(0);
        fill.reset();

        return this;
    }

    /**
     * Set the HaloBuilder to produce the provided Halo.
     * 
     * @param halo Halo under construction; if null HaloBuilder will be unset()
     * @return current HaloBuilder for chaining operations
     */
    public HaloBuilder reset(org.opengis.style.Halo halo) {
        if (halo == null) {
            return unset();
        }
        fill = new FillBuilder(this).reset(halo.getFill());
        radius = halo.getRadius();

        return this;
    }

    public HaloBuilder radius(Expression radius) {
        unset = false;
        this.radius = radius;
        return this;
    }

    public HaloBuilder radius(double radius) {
        return radius(literal(radius));
    }

    public HaloBuilder radius(String cqlExpression) {
        return radius(cqlExpression(cqlExpression));
    }

    public FillBuilder fill() {
        unset = false;
        return fill;
    }

    public Halo build() {
        if (unset)
            return null;

        Halo halo = sf.createHalo(fill.build(), radius);
        if (parent == null)
            reset();

        return halo;
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().halo().init(this);
    }

}