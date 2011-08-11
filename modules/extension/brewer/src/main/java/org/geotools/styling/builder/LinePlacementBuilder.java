package org.geotools.styling.builder;

import org.geotools.styling.LinePlacement;
import org.opengis.filter.expression.Expression;

public class LinePlacementBuilder extends AbstractStyleBuilder<LinePlacement> {
    private Expression offset;

    private Expression initialGap;

    private Expression gap;

    private boolean repeated;

    private boolean generalizedLine;

    private boolean aligned;

    public LinePlacementBuilder() {
        this(null);
    }

    LinePlacementBuilder(TextSymbolizerBuilder parent) {
        super(parent);
        reset();
    }

    public LinePlacement build() {
        if (unset) {
            return null;
        }
        LinePlacement linePlacement = sf.linePlacement(offset, initialGap, gap, repeated, aligned,
                generalizedLine);
        if (parent == null) {
            reset();
        }
        return linePlacement;
    }

    public LinePlacementBuilder reset() {
        this.aligned = false;
        this.generalizedLine = false;
        this.repeated = false;
        this.gap = literal(0);
        this.initialGap = literal(0);
        this.offset = literal(0);

        unset = false;
        return this;
    }

    public LinePlacementBuilder reset(LinePlacement placement) {
        if (placement == null) {
            return reset();
        }
        this.aligned = placement.isAligned();
        this.generalizedLine = placement.isGeneralizeLine();
        this.repeated = placement.isRepeated();
        this.gap = placement.getGap();
        this.initialGap = placement.getInitialGap();
        this.offset = placement.getPerpendicularOffset();

        unset = false;
        return this;
    }

    public LinePlacementBuilder unset() {
        return (LinePlacementBuilder) super.unset();
    }

    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().text().labelText("label").linePlacement().init(this);
    }

}
