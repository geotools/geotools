package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.StyleFactory;

public class LinePlacementBuilder<P> implements Builder<LinePlacement> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    private ChildExpressionBuilder<LinePlacementBuilder<P>> offset = new ChildExpressionBuilder<LinePlacementBuilder<P>>(
            this);

    private ChildExpressionBuilder<LinePlacementBuilder<P>> initialGap = new ChildExpressionBuilder<LinePlacementBuilder<P>>(
            this);

    private ChildExpressionBuilder<LinePlacementBuilder<P>> gap = new ChildExpressionBuilder<LinePlacementBuilder<P>>(
            this);
    
    boolean unset = true; // current value is null

    private boolean repeated;

    private boolean generalizedLine;

    private boolean aligned;

    public LinePlacementBuilder() {
        parent = null;
        reset();
    }

    public LinePlacementBuilder(P parent ){
        this.parent = parent;
        reset();
    }

    public LinePlacement build() {
        if (unset) {
            return null;
        }
        LinePlacement linePlacement = sf.linePlacement(offset.build(), initialGap.build(), gap.build(), repeated, aligned,
                generalizedLine);
        return linePlacement;
    }

    public P end() {
        return parent;
    }

    public LinePlacementBuilder<P> reset() {
        this.aligned = false;
        this.generalizedLine = false;
        this.repeated = false;    
        this.gap.literal(0);
        this.initialGap.literal(0);
        this.offset.literal(0);
        
        unset = false;
        return this;
    }

    public LinePlacementBuilder<P> reset(LinePlacement placement) {
        if (placement == null) {
            return reset();
        }
        this.aligned = placement.isAligned();
        this.generalizedLine = placement.isGeneralizeLine();
        this.repeated = placement.isRepeated();        
        this.gap.reset( placement.getGap() );
        this.initialGap.reset( placement.getInitialGap() );
        this.offset.reset( placement.getPerpendicularOffset() );
        
        unset = false;
        return this;
    }

    public LinePlacementBuilder<P> unset() {
        this.aligned = false;
        this.generalizedLine = false;
        this.repeated = false;    
        this.gap.unset();
        this.initialGap.unset();
        this.offset.unset();
        
        unset = true;
        return this;
    }

}
