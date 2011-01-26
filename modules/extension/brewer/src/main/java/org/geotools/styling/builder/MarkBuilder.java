package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.Mark;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.expression.Expression;

public class MarkBuilder<P> implements Builder<Mark> {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    P parent;
    
    StrokeBuilder<MarkBuilder<P>> strokeBuilder = new StrokeBuilder<MarkBuilder<P>>(this);

    FillBuilder<MarkBuilder<P>> fill = new FillBuilder<MarkBuilder<P>>(this);

    ExternalMarkBuilder<MarkBuilder<P>> externalMark = new ExternalMarkBuilder<MarkBuilder<P>>(this);

    ChildExpressionBuilder<MarkBuilder<P>> wellKnownName = new ChildExpressionBuilder<MarkBuilder<P>>(this);
    
    public MarkBuilder() {
        this( null );
    }
    public MarkBuilder(P parent) {
        this.parent = parent;
        reset();
    }
    public ChildExpressionBuilder<MarkBuilder<P>> wellKnownName() {
        externalMark.unset();
        return wellKnownName;
    }
    public MarkBuilder<P> wellKnownName(Expression name) {
        this.wellKnownName.reset(name);
        this.externalMark.unset();        
        return this;
    }
    public MarkBuilder<P> name(Expression name) {
        return wellKnownName(name);
    }

    public ExternalMarkBuilder<MarkBuilder<P>> externalMark() {
        return externalMark;
    }
    
    public StrokeBuilder<MarkBuilder<P>> stroke() {
        return strokeBuilder;
    }

    public FillBuilder<MarkBuilder<P>> fill() {
        return fill;
    }

    public MarkBuilder<P> reset() {
        // TODO: where is the default mark?
        this.wellKnownName.reset().literal("square");
        this.externalMark.unset();
        this.strokeBuilder.reset();
        this.fill.reset();
        
        return this;
    }

    public Mark build() {
        Mark mark = null;
        if( !externalMark.isUnset() ){
            mark = sf.mark( externalMark.build(), fill.build(), strokeBuilder.build());
        }
        if( !wellKnownName.isUnset() ){
            mark = sf.mark(wellKnownName.build(), fill.build(), strokeBuilder.build());
        }
        if( parent == null ){
            reset();
        }
        return mark;
    }

    public MarkBuilder<P> reset(Mark mark) {
        if( mark == null) return unset();
        this.wellKnownName.reset( mark.getWellKnownName() );
        this.externalMark.reset( mark.getExternalMark() );
        this.strokeBuilder.reset( mark.getStroke() );
        this.fill.reset( mark.getFill() );
        
        return null;
    }

    public MarkBuilder<P> unset() {
        externalMark.unset();
        wellKnownName.unset();
        fill.unset();
        strokeBuilder.unset();
        return this;
    }
}
