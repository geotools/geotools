package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.Fill;
import org.geotools.styling.StyleFactory;

public class FillBuilder<P> implements Builder<org.opengis.style.Fill>{
    
    private P parent;
    
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    ChildExpressionBuilder<FillBuilder<P>> color = new ChildExpressionBuilder<FillBuilder<P>>(this).unset();

    ChildExpressionBuilder<FillBuilder<P>> opacity = new ChildExpressionBuilder<FillBuilder<P>>(this).unset();

    GraphicBuilder<FillBuilder<P>> graphic = new GraphicBuilder<FillBuilder<P>>(this).unset();

    private boolean unset = false;

    /**
     * Create a FillBuilder on its own; not part of a larger data structure.
     */
    public FillBuilder(){
        this( null);
    }
    public FillBuilder(P parent) {
        this.parent = parent;
        reset();
    }
    
    public FillBuilder<P> color(Object color) {
        this.color.literal( color );
        unset = false;
        return this;
    }
    
    public FillBuilder<P> opacity( Object opacity) {
        this.opacity.literal( opacity );
        unset = false;
        return this;
    }
    public ChildExpressionBuilder<FillBuilder<P>> opacity(){
        unset = false;
        return opacity;   
    }
    public GraphicBuilder graphicFill() {
        unset = false;
        return graphic;
    }
    public ChildExpressionBuilder<FillBuilder<P>> color(){
        this.unset = false;
        return color;
    }

    /**
     * Build Fill as defined; FillBuilder will be reset after this use.
     * 
     * @return Created Fill as defined
     */
    public Fill build() {
        if (unset) {
            return null;
        }
        Fill fill = sf.createFill(
           color.build(),
           null,
           opacity.build(),
           graphic.build() );

        reset();
        return fill;
    }

    public P end(){
        return parent;
    }

    public FillBuilder<P> unset() {
        unset = true;        
        return this;
    }

    /**
     * Reset to produce the default Fill.
     */
    public FillBuilder<P> reset() {
        unset = false;
        color.reset( Fill.DEFAULT.getColor() );
        opacity.reset( Fill.DEFAULT.getOpacity() );
        graphic.reset();
        return this;
    }
    public FillBuilder<P> reset(org.opengis.style.Fill original) {
        unset = false;
        color.reset( original.getColor() );
        opacity.reset( original.getOpacity() );
        graphic.reset( original.getGraphicFill() );
        return this;
    }
    
}
