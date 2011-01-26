package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.Font;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.expression.Expression;

public class FontBuilder<P> implements Builder<Font> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private P parent;
    private ChildExpressionBuilder<FontBuilder<P>> x = new ChildExpressionBuilder<FontBuilder<P>>(this);
    private ChildExpressionBuilder<FontBuilder<P>> y = new ChildExpressionBuilder<FontBuilder<P>>(this);
    boolean unset = true; // current value is null
    private List<ChildExpressionBuilder<GraphicBuilder<P>>> family;
    private ChildExpressionBuilder<GraphicBuilder<P>> style;
    private ChildExpressionBuilder<GraphicBuilder<P>> weight;
    private ChildExpressionBuilder<GraphicBuilder<P>> size;
    public FontBuilder(){
        parent = null;
        reset();
    }
    public FontBuilder(P parent){
        this.parent = parent;
        reset();
    }
    
    public Font build() {
        if( unset ){
            return null;
        }
        List<Expression> list = new ArrayList<Expression>();
        for(ChildExpressionBuilder<?> face : family ){
            list.add( face.build() );
        }
        Font font = sf.font( list, style.build(), weight.build(), size.build() );
        return font;
    }
    
    public P end(){
        return parent;
    }

    public FontBuilder<P> reset() {
        x.reset().literal(0);
        y.reset().literal(0);
        unset = false;        
        return this;
    }

    public FontBuilder<P> reset(Font font) {
        if( font == null ){
            return reset();
        }
        unset = false; 
        return this;
    }

    public FontBuilder<P> unset() {
        unset = true;        
        return this;
    }

}
