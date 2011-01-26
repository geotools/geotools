package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.ColorReplacement;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.expression.Expression;

public class ColorReplacementBuilder<P> implements Builder<ColorReplacement> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    private ChildExpressionBuilder<ColorReplacementBuilder<P>> propertyName = new ChildExpressionBuilder<ColorReplacementBuilder<P>>(
            this);

    private List<ChildExpressionBuilder<ColorReplacementBuilder<P>>> mapping = new ArrayList<ChildExpressionBuilder<ColorReplacementBuilder<P>>>();

    boolean unset = true; // current value is null

    public ColorReplacementBuilder() {
        this(null);
    }

    public ColorReplacementBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public ColorReplacement build() {
        if (unset) {
            return null;
        }
        List<Expression> list = new ArrayList<Expression>();
        for( ChildExpressionBuilder<ColorReplacementBuilder<P>> expressionBuilder : mapping ){
            list.add( expressionBuilder.build() );
        }
        Expression array[] = list.toArray(new Expression[list.size()]);
        
        ColorReplacement replacement = sf.colorReplacement(propertyName.build(), array);
        if( parent == null ){
            reset();
        }
        return replacement;
    }

    public P end() {
        return parent;
    }

    public ColorReplacementBuilder<P> reset() {
        propertyName.reset().property("Raster");
        mapping.clear();
        unset = false;
        return this;
    }

    public ColorReplacementBuilder<P> reset(ColorReplacement replacement) {
        if (replacement == null) {
            return unset();
        }
        propertyName.reset( replacement.getRecoding() );
        mapping.clear();
        unset = false;
        return this;
    }

    public ColorReplacementBuilder<P> unset() {
        propertyName.unset();
        mapping.clear();
        unset = true;
        return this;
    }

}
