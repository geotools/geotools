package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.filter.expression.ExpressionBuilder;
import org.geotools.styling.Halo;
import org.geotools.styling.StyleFactory;

public class HaloBuilder<P> implements Builder<org.opengis.style.Halo> {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    P parent;

    boolean unset;

    ChildExpressionBuilder<HaloBuilder<P>> radius = new ChildExpressionBuilder<HaloBuilder<P>>(this);

    FillBuilder<HaloBuilder<P>> fill = new FillBuilder<HaloBuilder<P>>(this);

    public HaloBuilder() {
        this(null);
    }

    public HaloBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    /**
     * Set the HaloBuilder
     * <P>
     * to produce <code>node</code>
     * 
     * @return current HaloBuilder
     *         <P>
     *         for chaining operations
     */
    public HaloBuilder<P> unset() {
        unset = true;
        return this;
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
    public HaloBuilder<P> reset() {
        unset = false; // 
        radius.reset();
        fill.reset();

        return this;
    }

    /**
     * Set the HaloBuilder
     * <P>
     * to produce the provided Halo.
     * 
     * @param halo
     *            Halo under construction; if null HaloBuilder
     *            <P>
     *            will be unset()
     * @return current HaloBuilder
     *         <P>
     *         for chaining operations
     */
    public HaloBuilder<P> reset(org.opengis.style.Halo halo) {
        if (halo == null) {
            return unset();
        }
        fill = new FillBuilder<HaloBuilder<P>>(this).reset(halo.getFill());
        radius = new ChildExpressionBuilder<HaloBuilder<P>>(this).reset(halo.getRadius());

        return this;
    }

    public HaloBuilder<P> radius(Object radius) {
        this.radius.literal(radius);
        return this;
    }

    public ExpressionBuilder radius() {
        return radius;
    }

    public HaloBuilder<P> fill(Object color) {
        this.fill.color().literal(color);
        return this;
    }

    public FillBuilder<HaloBuilder<P>> fill() {
        return fill;
    }

    public Halo build() {
        if( unset ) return null;
        
        Halo halo = sf.createHalo(fill.build(), radius.build());
        if( parent == null ) reset();
        
        return halo;
    }
}